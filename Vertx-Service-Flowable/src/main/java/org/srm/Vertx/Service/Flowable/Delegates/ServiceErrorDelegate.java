package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ServiceErrorDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		
		execution.setVariable("greet", "Invalid Name.");

	}

}
