package shopping.cart.domain;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.circuitbreaker.CircuitBreaker;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Future;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import rx.Single;

/**
 * Product Verticle
 * Created by claudio on 13/07/17.
 */
public class ShoppingCartVerticleWithCircuitBreaker extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory
          .getLogger(ShoppingCartVerticleWithCircuitBreaker.class);

  @Override
  public void start() throws Exception {
    final Router router = Router.router(vertx);

    final ServiceDiscoveryOptions serviceDiscoveryOptions = new ServiceDiscoveryOptions()
            .setBackendConfiguration(
                    new JsonObject()
                            .put("host", "127.0.0.1")
                            .put("port", "6379")
            );

    CircuitBreaker circuit = CircuitBreaker.create("product-circuit",
            vertx, new CircuitBreakerOptions()
                    .setFallbackOnFailure(true)
                    .setTimeout(2000)
                    .setMaxFailures(5)
                    .setResetTimeout(5000)
    );

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, serviceDiscoveryOptions);

    router.get("/product/:id").handler(req -> {
      LOGGER.info(" receiving request");
      final String id = req.request().getParam("id");
      circuit.rxExecuteCommandWithFallback(
              future ->
                      HttpEndpoint.rxGetWebClient(discovery, rec -> rec.getName().endsWith("product")).subscribe(
                              webClient -> {
                                webClient.get("/product/" + id)
                                        .rxSend()
                                        .map(HttpResponse::bodyAsJsonObject)
                                        .subscribe(future::complete, future::fail);
                              }
                      ),
              t -> new JsonObject().put("message", "Fallback")
      ).subscribe(
              json -> {
                req.response().putHeader("content-type", "application/json")
                        .end(json.encode());
              }
      );
    });
    vertx.createHttpServer().requestHandler(router::accept)
            .rxListen(8084)
            .flatMap(httpServer -> discovery
                    .rxPublish(HttpEndpoint.createRecord("shopping-cart", "localhost", 8084, "/")))
            .subscribe(rec -> LOGGER.info("Shopping Cart Service is published"));
  }

}
