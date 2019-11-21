package javafx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;


public class testVertx {

	public static void main(String[] args) {
	
		Vertx vertx = Vertx.vertx();
		
		HttpServer httpServer = vertx.createHttpServer();
		
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
		
		
		httpServer
					.requestHandler(router::accept)
					.listen(8888);
	    	
	   
	}

}
