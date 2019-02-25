package org.srm.Vertx.Service.Flowable.Delegates;

import java.util.concurrent.CountDownLatch;

import org.flowable.engine.delegate.DelegateExecution;
import org.srm.Vertx.Service.Deployment.MainDeployment;

public class WorkerService implements Runnable{

    CountDownLatch latch = null;
    DelegateExecution execution = null;
    String name = null;
    Boolean check = true;
    String test = "1234";
    public WorkerService(CountDownLatch latch, DelegateExecution execution, String name) {
        this.latch = latch;
        this.execution = execution;
        this.name = name;
    }

    public void run() {
        try {
        	System.out.println("worker");
        	System.out.println(test);
        	MainDeployment.vertx.eventBus().send("service.validate", name, reply -> {
        		test = "qwerty";
        		System.out.println(test);
				Boolean res = Boolean.parseBoolean(reply.result().body().toString());
				System.out.println(res);
				execution.setVariable("approved", res);
							
				latch.countDown();
				check = false;
				
			});
        	System.out.println(test);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Waiter Released");
    }
}
