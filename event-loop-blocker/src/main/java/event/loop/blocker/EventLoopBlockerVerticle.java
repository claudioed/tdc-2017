package event.loop.blocker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.time.LocalDateTime;

/**
 * Event loop blocker
 * Created by claudio on 13/07/17.
 */
public class EventLoopBlockerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopBlockerVerticle.class);

    public void start() {
        final Router router = Router.router(vertx);
        router.get("/test").handler(req -> {
            LOGGER.info(" receiving request");
            LongRunningProcess.longProcess();
            final String date = LocalDateTime.now().toString();
            LOGGER.info(" response ready");
            req.response().putHeader("content-type", "text/plain").end(String.format("TDC 2017 - Event Loop Blocker at %s", date));

        });
        vertx.createHttpServer().requestHandler(router::accept).listen(8081);
    }

}
