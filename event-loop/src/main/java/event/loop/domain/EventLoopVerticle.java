package event.loop.domain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * Event loop
 * Created by claudio on 13/07/17.
 */
public class EventLoopVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopVerticle.class);

    public void start() {
        final Router router = Router.router(vertx);
        router.get("/test").handler(req -> {
            LOGGER.info(" receiving request");
            req.response().putHeader("content-type", "text/plain").end(NormalProcess.process());
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}
