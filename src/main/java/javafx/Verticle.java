package javafx;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Verticle {
	private static final Logger logger = LogManager.getLogger();

	private SQLClient dbClient;
	private Vertx vertx;
	private String currentSessionKey;
	public void stop(Promise<Void> stopPromise) throws Exception {

		logger.info("Stopping verticle...");

		dbClient.close();

		stopPromise.complete();
	}
	
	public void start(Promise<Void> startPromise) throws Exception {
		logger.info("Starting verticle...");
		vertx = Vertx.vertx();

		Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
		steps.setHandler(ar -> { 
			if(ar.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(ar.cause());
			}
		});
	}

	private Future<Void> prepareDatabase() {
		Promise<Void> promise = Promise.promise();

		logger.info("Preparing database...");
		
		// see https://vertx.io/docs/vertx-mysql-postgresql-client/java/#_configuration
		JsonObject mySQLClientConfig = new JsonObject()
				.put("host", "easel2.fulgentcorp.com")
				.put("username", "ouo655")
				.put("password", "01704568")
				.put("database", "ouo655");
		//SQLClient mySQLClient 
		dbClient = MySQLClient.createShared(vertx, mySQLClientConfig);
		promise.complete();
		return promise.future();
	}
	

	private Future<Void> startHttpServer() {
		logger.info("Starting http server...");

		Promise<Void> promise = Promise.promise();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		router.get("/").handler(this::indexHandler);
		router.get("/book/:id").handler(this::fetchBook);

		router.get("/login").handler(this::login);
		router.get("/reports/bookdetail").handler(this::reports);
		
		server
		.requestHandler(router)
		.listen(8888, ar -> {
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

	
	private void indexHandler(RoutingContext context) {
		context.response().putHeader("Content-Type", "text/html");
		StringBuilder output = new StringBuilder();
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
				//promise.fail(ar.cause());
			} else {
				
				logger.info("Fetching books from database...");
				
				SQLConnection connection = ar.result();
				connection.query("select id, title, summary from Book order by id", result -> {
					connection.close();
					
					output.append("<h1>Hello world</h1>");

					if(result.failed()) {
						logger.error("Database preparation error", result.cause());
						// promise.fail(create.cause());
					} else {
						// promise.complete();
						List<JsonArray> rows = result.result().getResults();
						logger.info("Iterating over rows of length " + rows.size() + "...");

						output.append("<table>");
						for(JsonArray row : rows) {
							output.append("<tr><td>" + row.toString() + "</td></tr>");
							//logger.info(row.toString());
						}
						output.append("</table>");
					}
					
					context.response().end(output.toString());

				});
			}
		});
	}
	
	
	private void fetchBook(RoutingContext context) {
		StringBuilder output = new StringBuilder();
		
		context.response().putHeader("Content-Type", "text/json");
		int bookId = Integer.parseInt(context.pathParam("id"));
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
				//promise.fail(ar.cause());
			} else {
				
				logger.info("Fetching book " + bookId + " from database...");
				
				SQLConnection connection = ar.result();
				JsonArray params = new JsonArray().add(bookId);
				connection.queryWithParams("select id, title, summary from Book where id = ? order by id"
						, params
						, result -> {
					connection.close();
					if(result.failed()) {
						logger.error("Database preparation error", result.cause());
						// promise.fail(create.cause());
					} else {
						// promise.complete();
						List<JsonArray> rows = result.result().getResults();

						if(rows.size() < 1) {
							
						} else {
							JsonObject bookJson = new JsonObject();
							//logger.info("Iterating over rows of length " + rows.size() + "...");
							bookJson.put("id", rows.get(0).getInteger(0));
							bookJson.put("title", rows.get(0).getString(1));
							bookJson.put("summary", rows.get(0).getString(1));
							output.append(bookJson.toString());
						}
					}
					
					context.response().end(output.toString());

				});
			}
		});		
	}
	
	private void login(RoutingContext context) {
		StringBuilder output = new StringBuilder();
		
		context.response().putHeader("Content-Type", "text/json");
		
			String userName = context.request().getParam("username");
			String userPass = context.request().getParam("password");
			dbClient.getConnection(ar -> {
				if(ar.failed()) {
					logger.error("Could not open a database connection to get User Login info", ar.cause());
				} else {
					logger.info("DBM connection success, now looking for Username: " + userName );
					
					SQLConnection connection = ar.result();
					JsonArray params = new JsonArray().add(userName).add(userPass);
					connection.queryWithParams("SELECT * FROM User WHERE username = ? and password_hash in (select sha2(?, 256))"
							, params
							, result -> {
								connection.close();
							
						if(result.failed()) {
							context.response().setStatusCode(401);
						}else {

							List<JsonArray> userRows = result.result().getResults();
							JsonObject bookJson = new JsonObject();
							if(userRows.size() < 1) {
								context.response().setStatusCode(401);
								context.response().end("Login Error");
							} else {
								logger.info("Found " + userName );
								// CREATING SESSION
								int rowId = userRows.get(0).getInteger(0);
								String sha2 = " SHA2( CONCAT( NOW(), 'my secret value' ) , 256)";
							
								logger.info("Creating Session " );
								JsonArray params2 = new JsonArray();
								connection.update("INSERT INTO session (user_id, token, expiration) select " + rowId 
													+ ", SHA2( CONCAT( NOW(), 'my secret value' ) , 256), CURRENT_TIMESTAMP + interval '100' minute", sessionCreate -> { 
													connection.close();
										
										if(sessionCreate.failed()) {
											logger.error("Failed Create Session ");
										}else {
											logger.info("Success Create Session");
											params2.add(sessionCreate.result().getKeys().getInteger(0));
										}
										logger.info("Getting Session " );
										
										connection.queryWithParams("SELECT token FROM session WHERE id = ?", params2, sessionResult -> { 
															connection.close();
												
												if(sessionResult.failed()) {
													logger.error("SELECT Session Failed ");
												}else {
													logger.info("SELECT Session Success ");
													List<JsonObject> sessionRows = sessionResult.result().getRows();
													bookJson.put("response", "ok");
													bookJson.put("session token", sessionRows.get(0).getValue("token"));
													currentSessionKey = sessionRows.get(0).getValue("token").toString();
													
													output.append(bookJson.toString());
													context.response().end(output.toString());
												}
										});
								});
							}
						}
					});
				}
			});
			
	}
	
	private void reports(RoutingContext context) {
//		System.out.println(currentSessionKey + "@@@@@@@@");
		
		String testAllowed = "f6ec20ec615f3030e227ce1d2a64bf40fc5d1871696bcff1a05af3c952d86dc8"; //Bob is allowed and not expired session
		String testNotAllowed = "2629902373a6c284035a7cbe8d93fcd2313698ab63c8e97946c94d044db2f320"; // sue is not allowed even with session
//		SELECT permission.allowed FROM permission inner join session on permission.user_id = session.user_id 
//		WHERE session.token = 'f6ec20ec615f3030e227ce1d2a64bf40fc5d1871696bcff1a05af3c952d86dc8' and 
//		permission.permission = 'book report' and session.expiration >now() and permission.allowed = 1
//		SELECT permission.allowed FROM permission inner join session on permission.user_id = session.user_id 
//		WHERE session.token = 'f6ec20ec615f3030e227ce1d2a64bf40fc5d1871696bcff1a05af3c952d86dc8' and 
//		permission.permission = 'book report' and session.expiration >now() and permission.allowed = 1

		String queryAllowed = "SELECT permission.allowed FROM permission inner join session on permission.user_id = session.user_id "
				+ "WHERE session.token = '?' and "
				+ "permission.permission = 'book report' and session.expiration >now() and permission.allowed = 1";
		
		context.response().putHeader("Content-Type", "text/html");
		StringBuilder output = new StringBuilder();
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
			} else {
				
				logger.info("Checking if user is allowed and not expired");
				
				SQLConnection connection = ar.result();
				JsonArray params = new JsonArray().add(testAllowed);
				
				connection.queryWithParams(queryAllowed
						, params
						, allowedResult -> {
							connection.close();
							
					if(allowedResult.failed()) {
						logger.error("Error: Can't Read Allowed Allowed");
						context.response().setStatusCode(401);
						context.response().end("Login Error");
						
					}else {
					
						List<JsonArray> allowed = allowedResult.result().getResults();
						logger.info("CHECKING STATUS " + allowed.get(0).getInteger(1));
						if(allowed.size() < 1) {
							context.response().setStatusCode(401);
							context.response().end("User is Not Allowed");
						} else {
							logger.info("User is Allowed ");
							System.out.println(allowedResult.result().getResults().toString());
							context.response().end("LOGIN SUCCESS");
						}
					}
				});
			

			}
		});
//		indexHandler(context);
//		HttpServer httpServer = vertx.createHttpServer();
//		
//		Router router = Router.router(vertx);

//		Route handler2 = router
//		.get("/reports/bookdetail")
//		.handler(routingContext -> {
//			System.out.println("Came to say hello");
//			HttpServerResponse response = routingContext.response();
//			response.setChunked(true);
//			response.write("Hello Michael");
//			response.putHeader("Content-Type", "application/vnd.ms-excel");
////			response.putHeader("Content-Disposition", "attachment; filename=book_report.xls");
//			//text/json
////			response.putHeader("content-type", "text/plain");
//			
//			//response.end("Hello World");
//			response.end();
//		});
//
//
//httpServer
//			.requestHandler(router::accept)
//			.listen(8888);
	}

	public void garbage() {
		dbClient.getConnection(ar -> {
			if(ar.succeeded()) {
				SQLConnection connection = ar.result();
				JsonArray params = new JsonArray().add(1);
				connection.queryWithParams("select * from stuff where id = ?", params, fetchResult -> {
					List<JsonObject> rows = fetchResult.result().getRows();
					for(JsonObject row : rows) {
						int rowId = row.getInteger("id");
						connection.update("insert into some_table (row_id) values ( " + rowId + ") ", insertResult -> {
							int newId = insertResult.result().getKeys().getInteger(0);
						});
					}
				});
			}
		});
	}


}
