package webservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;


public class TestVerticle extends AbstractVerticle {
	private static final Logger logger = LogManager.getLogger();

	// Local variables
	private Vertx vertx;

	
	public static void main(String[] args) throws Exception {
		
		Verticle verticle = new Verticle();
		Promise<Void> promise = Promise.promise();

//		verticle.createExcel(Router);
		verticle.start(promise);

	}
	
}
