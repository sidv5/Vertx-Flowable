package org.srm.Vertx.Service.Flowable;

import java.util.HashMap;
import java.util.List;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class FlowableStartEvent extends AbstractVerticle {
	Logger log = LoggerFactory.getLogger(FlowableStartEvent.class);

	@Override
	public void start() throws Exception {
		super.start();

		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				.setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1").setJdbcUsername("srm").setJdbcPassword("")
				.setJdbcDriver("org.h2.Driver")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		ProcessEngineConfigurationImpl processEngineConfig = (ProcessEngineConfigurationImpl) cfg;
		processEngineConfig.initVariableTypes();
		/*processEngineConfig.getVariableTypes().addType(new CustomObjectType("vertx", Vertx.class))
				.addType(new CustomObjectType("message", Message.class));*/

		ProcessEngine processEngine = processEngineConfig.buildProcessEngine();

		String pName = processEngine.getName();
		String ver = ProcessEngine.VERSION;
		log.info("ProcessEngine [" + pName + "] Version: [" + ver + "]");
		RepositoryService repositoryService = processEngine.getRepositoryService();

		Deployment deployment = repositoryService.createDeployment().addClasspathResource("vertx.bpmn20.xml").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		log.info("Found process definition [" + processDefinition.getName() + "] with id [" + processDefinition.getId()
				+ "]");
		
		MessageConsumer<String> flowableMessage = vertx.eventBus().consumer("flowable.feed.req");
		MessageConsumer<String> messageReply = vertx.eventBus().consumer("greet.reply");
		HashMap<String, Object> variables = new HashMap<>();
		messageReply.handler(handler -> {
			messageReply.resume();
		});
		flowableMessage.handler(message -> {

			variables.put("name", message.body().toString());

			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vertx", variables);

			TaskService taskService = processEngine.getTaskService();
			List<Task> tasks = taskService.createTaskQuery().list();
			Task task = tasks.get(0);
			taskService.complete(task.getId());

			messageReply.bodyStream().handler(handler -> {
				message.reply(handler);
			});
			/*
			 * HistoryService historyService = processEngine.getHistoryService();
			 * HistoricVariableInstance historicVariables = historyService
			 * .createHistoricVariableInstanceQuery()
			 * .processInstanceId(processInstance.getId())
			 * .variableName("greet").singleResult();
			 * 
			 * 
			 * 
			 * String replyMessage = historicVariables.getValue().toString();
			 */

		});
	}

}
