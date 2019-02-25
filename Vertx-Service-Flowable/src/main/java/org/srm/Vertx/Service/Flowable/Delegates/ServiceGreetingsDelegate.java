package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.srm.Vertx.Service.Deployment.MainDeployment;

public class ServiceGreetingsDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
//		System.out.println("Greetings");
//		execution.setVariable("greet", "Hello "+execution.getVariable("name")+"!");
		MainDeployment.vertx.eventBus().send("flowable.fed.resp", "Greetings "+execution.getVariable("name")+"!");
	}
}
