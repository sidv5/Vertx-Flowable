package org.srm.Vertx.Service.Flowable.Workers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;

import io.vertx.core.Vertx;

public class ExecutionFuture implements Future<Void> {
	private DelegateExecution execution;
	private final CountDownLatch latch;
	private Vertx vertx;
	private String name;
	private volatile boolean cancelled = false;

	public ExecutionFuture(DelegateExecution execution, String name, Vertx vertx) {
		this.execution = execution;
		this.name = name;
		this.vertx = vertx;
		latch = new CountDownLatch(1);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (isDone()) {
			return false;
		} else {
			latch.countDown();
			cancelled = true;
			return !isDone();
		}
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return latch.getCount() == 0;
	}

	@Override
	public Void get() throws InterruptedException, ExecutionException {
		latch.countDown();
		vertx.eventBus().send("service.validate", name, reply -> {
			try {
				
				Boolean res = Boolean.parseBoolean(reply.result().body().toString());
				this.execution.setVariable("approved", res);
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		});
		return null;
	}

	@Override
	public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		latch.await(timeout, unit);
		return null;
	}

	public void getResult() {
		latch.countDown();
		vertx.eventBus().send("service.validate", name, reply -> {
			Boolean res = Boolean.parseBoolean(reply.result().body().toString());
			this.execution.setVariable("approved", res);
		});
	}

}
