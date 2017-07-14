package product;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import java.util.Map;

/**
 * Product Verticle
 * Created by claudio on 13/07/17.
 */
public class ProductVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductVerticle.class);

  private Map<String, Product> products = ImmutableMap
      .of("CAN", Product.builder().id("CAN").name("Candies").build(),
          "TV", Product.builder().id("TV").name("TV").build());

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
      req.response().putHeader("content-type", "application/json")
          .end(new Gson().toJson(products.get(id)));

    });
    vertx.createHttpServer().requestHandler(router::accept)
        .rxListen(8083)
        .flatMap(httpServer -> discovery
            .rxPublish(HttpEndpoint.createRecord("product", "localhost", 8083, "/")))
        .subscribe(rec -> LOGGER.info("Product Service is published"));
  }

}
