package org.srm.Vertx.Service.Flowable.Workers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ServiceValidateVerticle extends AbstractVerticle {
	Logger log = LoggerFactory.getLogger(ServiceValidateVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		vertx.eventBus().consumer("service.validate", message -> {
			String name = message.body().toString();
			String reply = "false";
			if (name.length() >= 3 && name.length() <=10) {
				reply = "true";
			}
			message.reply(reply);
		});
		log.info(ServiceValidateVerticle.class.getSimpleName() + " Deployed.");
	}
}
