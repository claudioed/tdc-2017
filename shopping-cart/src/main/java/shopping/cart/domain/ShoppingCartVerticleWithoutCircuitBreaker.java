package shopping.cart.domain;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

/**
 * Product Verticle
 * Created by claudio on 13/07/17.
 */
public class ShoppingCartVerticleWithoutCircuitBreaker extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ShoppingCartVerticleWithoutCircuitBreaker.class);

  @Override
  public void start() throws Exception {
    final Router router = Router.router(vertx);

    final ServiceDiscoveryOptions serviceDiscoveryOptions = new ServiceDiscoveryOptions()
        .setBackendConfiguration(
            new JsonObject()
                .put("host", "127.0.0.1")
                .put("port", "6379")
        );

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx,serviceDiscoveryOptions);
    router.get("/product/:id").handler(req -> {
      LOGGER.info(" receiving request");
      final String id = req.request().getParam("id");
      HttpEndpoint.rxGetWebClient(discovery, rec -> rec.getName().endsWith("product"))
          .flatMap(client ->
              client.get("/product/" + id).as(BodyCodec.string()).rxSend())
          .subscribe(response -> req.response().putHeader("content-type", "application/json")
              .end(response.body()));

    });
    vertx.createHttpServer().requestHandler(router::accept)
        .rxListen(8084)
        .flatMap(httpServer -> discovery
            .rxPublish(HttpEndpoint.createRecord("shopping-cart", "localhost", 8084, "/")))
        .subscribe(rec -> LOGGER.info("Shopping Cart Service is published"));
  }

}
