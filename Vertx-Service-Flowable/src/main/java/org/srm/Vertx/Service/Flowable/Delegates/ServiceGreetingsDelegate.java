package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ServiceGreetingsDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("Greetings");
		execution.setVariable("greet", "Hello "+execution.getVariable("name")+"!");
	}

}
