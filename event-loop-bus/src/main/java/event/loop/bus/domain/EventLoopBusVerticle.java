package event.loop.bus.domain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Event loop
 * Created by claudio on 13/07/17.
 */
public class EventLoopBusVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopBusVerticle.class);

    public void start() {
        this.vertx.deployVerticle(new BusVerticle(),new DeploymentOptions().setWorker(true));
        this.vertx.deployVerticle(new RequestResourceVerticle());
    }

}
