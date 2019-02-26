package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.srm.Vertx.Service.Deployment.MainDeployment;

public class ServiceGreetingsDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		String name = execution.getVariable("name").toString();
		MainDeployment.vertx.eventBus().publish("greet.reply", "Greetings " + name + "!");
	}

}
