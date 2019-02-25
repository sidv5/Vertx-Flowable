package org.srm.Vertx.Service.Deployment;

import org.srm.Vertx.Service.Flowable.FlowableStartEvent;
import org.srm.Vertx.Service.Flowable.Delegates.ServiceValidateDelegate;
import org.srm.Vertx.Service.Flowable.Workers.ServiceValidateVerticle;
import org.srm.Vertx.Service.Gateway.VertxAPI;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainDeployment {
	static Logger log = LoggerFactory.getLogger(Deployment.class);
	public static Vertx vertx;

	public static void main(String[] args) {
		vertx = Vertx.vertx();

		// Gateway
		vertx.deployVerticle(VertxAPI.class.getName());

		// Flowable
		vertx.deployVerticle(FlowableStartEvent.class.getName()/*, new DeploymentOptions().setWorker(true)*/);
		// vertx.deployVerticle(FlowableAP.class.getName());
		// Delegate
		// vertx.deployVerticle(ServiceValidateDelegate.class.getName());

		// Delegate Workers
		vertx.deployVerticle(ServiceValidateVerticle.class.getName());
	}
}
