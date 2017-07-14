package event.loop.bus.domain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * Event Loop Bus
 * Created by claudio on 13/07/17.
 */
public class RequestResourceVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusVerticle.class);

    public void start() {
        final EventBus eventBus = vertx.eventBus();
        final Router router = Router.router(vertx);
        router.get("/test").handler(req -> {
            LOGGER.info(" receiving request");
            eventBus.send("data-stream", new JsonObject(), responseBus -> {
                if (responseBus.succeeded()) {
                    req.response().putHeader("content-type", "text/plain").end(LongRunningProcess.longProcess());
                }
            });
        });
        vertx.createHttpServer().requestHandler(router::accept).listen(8082);
    }

}
