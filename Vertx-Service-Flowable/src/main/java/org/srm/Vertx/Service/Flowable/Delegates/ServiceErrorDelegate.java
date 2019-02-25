package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.srm.Vertx.Service.Deployment.MainDeployment;

public class ServiceErrorDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		
//		execution.setVariable("greet", "Invalid Name.");
		MainDeployment.vertx.eventBus().send("flowable.fed.resp", "Invalid Name");

	}

}
