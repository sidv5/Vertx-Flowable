package org.srm.Vertx.Service.Flowable.Delegates;

import java.util.concurrent.CountDownLatch;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.srm.Vertx.Service.Deployment.MainDeployment;

import io.vertx.core.eventbus.Message;

public class ServiceValidateDelegate implements JavaDelegate {
	public static Boolean testString;

	@Override
	public void execute(DelegateExecution execution) {
		CountDownLatch latch = new CountDownLatch(1);
		
		String name = execution.getVariable("name", false).toString();
		try {
			Thread thread = new Thread() {
				public void run() {
					try {
						MainDeployment.vertx.eventBus().send("service.validate", name, reply -> {
							Boolean res = Boolean.parseBoolean(reply.result().body().toString());
							ServiceValidateDelegate.testString = res;
							latch.countDown();
						});
					} catch (Exception e) {
						System.out.println("ERROR  :  " + e.getMessage());
					}
				}
			};
			thread.start();
			latch.await();
			execution.setVariable("approved", ServiceValidateDelegate.testString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
