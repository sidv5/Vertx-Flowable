package org.srm.Vertx.Service.Gateway;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class VertxAPI extends AbstractVerticle {
	Logger log = LoggerFactory.getLogger(VertxAPI.class);
	private int PORT = 8080;

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		/*
		 * ProcessEngineConfiguration cfg = new MongoDbProcessEngineConfiguration()
		 * .setConnectionUrl("localhost:27017") .setDisableIdmEngine(true)
		 * .setDatabaseSchemaUpdate(MongoDbProcessEngineConfiguration.
		 * DB_SCHEMA_UPDATE_TRUE) .setHistoryLevel(HistoryLevel.AUDIT);
		 */

		Router route = Router.router(vertx);

		route.get("/:name").handler(rC -> {

			/*getVertx().eventBus().send("flowable.feed", rC.request().getParam("name"), reply -> {
				if (reply.succeeded()) {
					log.info(reply.result().body().toString());
					rC.response().end(reply.result().body().toString());
				} else {
					rC.response().end("No Reply!");
				}
			});*/
			
			getVertx().eventBus().publish("flowable.feed.req", rC.request().getParam("name"));
			rC.response().end("hh");
			getVertx().eventBus().consumer("flowable.fed.resp", reply ->  {
				
			});
			/*, handler -> {
				getVertx().eventBus().send("service.validate", rC.request().getParam("name"));	
				
			});*/
		});
		vertx.createHttpServer().requestHandler(route).listen(PORT);
		log.info("Server running on port : " + PORT);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	
}
