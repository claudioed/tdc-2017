package event.loop.bus.domain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Event loop
 * Created by claudio on 13/07/17.
 */
public class BusVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusVerticle.class);

    public void start() {
        this.vertx.eventBus().consumer("data-stream",handler ->{
            LOGGER.info(" Message received on BUS");
            handler.reply(LongRunningProcess.longProcess());
        });
    }

}
