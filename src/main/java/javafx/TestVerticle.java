package javafx;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestVerticle extends AbstractVerticle {
	private static final Logger logger = LogManager.getLogger();

	// Local variables
	private Vertx vertx;

	
	public static void main(String[] args) throws Exception {
		
	
		Vertx vertx = Vertx.vertx();
		Verticle verticle = new Verticle();
		Promise<Void> promise = Promise.promise();
		verticle.start(promise);
		 
//		HttpServer httpServer = vertx.createHttpServer();
//		
//		Router router = Router.router(vertx);
//		Route handler1 = router
//				//login?username=<user name>&password=<hashed password>
//				//login?username=bob&password=1234
//				.get("/login")
//				.handler(routingContext -> {
//					String username = routingContext.request().getParam("username");
//					String password = routingContext.request().getParam("password");
//					System.out.println("Came to say hello");
//					HttpServerResponse response = routingContext.response();
//					response.setChunked(true);
//					response.write("Hello " + username + " with " +password);
//					response.end();					//text/json
//					//response.putHeader("content-type", "text/plain");
//					//response.end("Hello World");
//				});
//		Route handler2 = router
//				.get("/reports/bookdetail")
//				.handler(routingContext -> {
//					System.out.println("Came to say hello");
//					HttpServerResponse response = routingContext.response();
//					response.setChunked(true);
//					response.write("Hello Michael");
//					response.putHeader("Content-Type", "application/vnd.ms-excel");
////					response.putHeader("Content-Disposition", "attachment; filename=book_report.xls");
//					//text/json
////					response.putHeader("content-type", "text/plain");
//					
//					//response.end("Hello World");
//					response.end();
//				});
//		
//		
//		httpServer
//					.requestHandler(router::accept)
//					.listen(8888);
		
		

	}
	
}
