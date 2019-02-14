package org.srm.Vertx.Service.Deployment;

import org.srm.Vertx.Service.Flowable.FlowableAP;
import org.srm.Vertx.Service.Flowable.FlowableStartEvent;
import org.srm.Vertx.Service.Flowable.Delegates.ServiceValidateDelegate;
import org.srm.Vertx.Service.Flowable.Workers.ServiceValidateVerticle;
import org.srm.Vertx.Service.Gateway.VertxAPI;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Deployment {
	static Logger log = LoggerFactory.getLogger(Deployment.class);

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		// Gateway
//		vertx.deployVerticle(VertxAPI.class.getName());
//		
//		// Flowable
//		vertx.deployVerticle(FlowableStartEvent.class.getName());
//		//vertx.deployVerticle(FlowableAP.class.getName());
//		// Delegate
//		//vertx.deployVerticle(ServiceValidateDelegate.class.getName());
//		
//		// Delegate Workers
//		vertx.deployVerticle(ServiceValidateVerticle.class.getName(), new DeploymentOptions().setWorker(true).setWorkerPoolSize(2));

		Config config = new Config();

		NetworkConfig network = config.getNetworkConfig();
		network.getInterfaces().setEnabled(true).addInterface("127.0.0.*");
		network.setPort(5701);
		JoinConfig join = network.getJoin();
		join.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1:5701").addMember("127.0.0.1:5702");
		join.getMulticastConfig().setEnabled(false);
		final ClusterManager mgr = new HazelcastClusterManager(config);

		final VertxOptions options = new VertxOptions().setClusterManager(mgr).setClusterHost("127.0.0.1");
		Vertx.clusteredVertx(options, cluster -> {
			if (cluster.succeeded()) {
				cluster.result().deployVerticle(VertxAPI.class.getName(),
						new DeploymentOptions().setInstances(1).setWorker(true), res -> {
							if (res.succeeded()) {
								log.info(VertxAPI.class.getSimpleName() + " Deployment Suceeded.");
								log.info("Deployment id is: " + res.result());
							} else {
								log.error("Deployment failed!");
							}
						});
			} else {
				log.error("Cluster up failed: " + cluster.cause());
			}
		});
	}
}