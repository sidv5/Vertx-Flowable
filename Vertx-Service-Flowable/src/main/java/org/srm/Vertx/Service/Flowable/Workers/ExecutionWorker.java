package org.srm.Vertx.Service.Flowable.Workers;

import java.util.concurrent.CountDownLatch;

import org.flowable.engine.delegate.DelegateExecution;

public class ExecutionWorker extends Thread {
	private CountDownLatch latch;
	private DelegateExecution execution;
	private boolean value;

	public ExecutionWorker(CountDownLatch latch, DelegateExecution execution, String name, boolean value) {
		super(name);
		this.latch = latch;
		this.execution = execution;
		this.value = value;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
			execution.setVariable("approved", value);
			latch.countDown();
			System.out.println(Thread.currentThread().getName() + " finished");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
