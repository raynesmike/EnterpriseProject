package webservice;

import io.vertx.core.Promise;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);

        logger.info("Stopping verticle...");

        //dbClient.close();

        stopPromise.complete();
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("Starting verticle...");
        startHttpServer();
    }


    private Future<Void> startHttpServer() {
        logger.info("Starting http server...");

        Promise<Void> promise = Promise.promise();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        Route handler1 = router
                //login?username=<user name>&password=<hashed password>
                .get("/login")
                .handler(routingContext -> {
                    String username = routingContext.request().getParam("username");
                    String password = routingContext.request().getParam("password");
                    System.out.println("Came to say hello");
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.write("Hello " + username + " with " +password);
                    response.end();					//text/json
                    //response.putHeader("content-type", "text/plain");
                    //response.end("Hello World");
                });
        Route handler2 = router
                .get("/reports/bookdetail")
                .handler(routingContext -> {
                    System.out.println("Came to say hello");
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.write("Hello Michael");
                    response.end();
                    //text/json
                    //response.putHeader("content-type", "text/plain");
                    //response.end("Hello World");
                });

        server.requestHandler(router::accept).listen(8888, ar -> {
            if (ar.succeeded()) {
                logger.info("HTTP server running on port 8888");
                promise.complete();
            } else {
                logger.error("Could not start a HTTP server", ar.cause());
                promise.fail(ar.cause());
            }
        });

        return promise.future();
    }

}
