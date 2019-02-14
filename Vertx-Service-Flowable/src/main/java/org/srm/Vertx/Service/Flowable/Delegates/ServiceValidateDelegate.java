package org.srm.Vertx.Service.Flowable.Delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import org.srm.Vertx.Service.Deployment.*;

public class ServiceValidateDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		String name = execution.getVariable("name").toString();
		System.out.println("name:"+name);
		execution.setVariable("approved", true);
		try {
			System.out.println("HIT");
			MainDeployment.vertx.eventBus().send("service.validate", name, reply -> {
				Boolean res = Boolean.parseBoolean(reply.result().body().toString());
				System.out.println(res);
				//execution.setVariable("approved", res);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
