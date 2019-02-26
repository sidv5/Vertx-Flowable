package org.srm.Vertx.Service.Gateway;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
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
			getVertx().eventBus().send("flowable.feed.req", rC.request().getParam("name"), reply -> {
				HttpServerResponse response = rC.response();
				response.setChunked(true);
				response.end(reply.result().body().toString());
			});
		});
		vertx.createHttpServer().requestHandler(route).listen(PORT);
		log.info("Server running on port : " + PORT);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
}
