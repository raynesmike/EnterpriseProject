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

		/*
		vertx.createHttpServer().requestHandler(req -> {
			req.response()
			.putHeader("content-type", "text/plain")
			.end("Hello from Vert.x!");
		}).listen(8888, http -> {
			if(http.succeeded()) {
				startPromise.complete();
				System.out.println("HTTP server started on port 8888");
			} else {
				startPromise.fail(http.cause());
			}
		});
		 */
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

		/*
		dbClient = JDBCClient.createShared(vertx, new JsonObject()  (1)
				.put("url", "jdbc:hsqldb:file:db/wiki")   (2)
				.put("driver_class", "org.hsqldb.jdbcDriver")   (3)
				.put("max_pool_size", 30));   (4)

				dbClient.getConnection(ar -> {
					if (ar.failed()) {
						logger.error("Could not open a database connection", ar.cause());
						promise.fail(ar.cause());
					} else {

						SQLConnection connection = ar.result();
						connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
							connection.close();   (8)
							if (create.failed()) {
								LOGGER.error("Database preparation error", create.cause());
								promise.fail(create.cause());
							} else {
								promise.complete();  (9)
							}
						});

					}
				});
		 */
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



		//router.get("/wiki/:page").handler(this::pageRenderingHandler);
		//router.post().handler(BodyHandler.create());
		//router.post("/save").handler(this::pageUpdateHandler);
		//router.post("/create").handler(this::pageCreateHandler);
		//router.post("/delete").handler(this::pageDeletionHandler);

		//templateEngine = FreeMarkerTemplateEngine.create(vertx);

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
		//context.put("title", "Wiki home");
		//context.put("pages", "help");
		//context.request().getHeader("Authorization");
		
		context.response().putHeader("Content-Type", "text/html");
		//context.response().end("<h1>Hello world</h1>");
		//if(true) {
		//	return;
		//}
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
							
						/*
								.getResults()
								.stream()
								.map(json -> json.getString(0))
								.sorted()
								.collect(Collectors.toList());
								*/
						//output.append((new JsonObject().put("rows", new JsonArray(rows)).toString()));
						
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
					logger.info("DBM connection success, now getting Username " + userName );
					
					SQLConnection connection = ar.result();
					JsonArray params = new JsonArray().add(userName).add(userPass);
					connection.queryWithParams("SELECT * FROM User WHERE username = ? and password_hash in (select sha2(?, 256))"
							, params
							, result -> {
								connection.close();
							
						if(result.failed()) {
							logger.error("Username doesn't Exist", result.cause());
						}else {
							List<JsonArray> userRows = result.result().getResults();
							if(userRows.size() < 1) {
								context.response().setStatusCode(401);
							} else {

								logger.info("Found " + userName );
								// CREATING SESSION
								int rowId = userRows.get(0).getInteger(0);
								String sha2 = " SHA2( CONCAT( NOW(), 'my secret value' ) , 256)";
								SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = Calendar.getInstance().getTime();
								java.sql.Date date_added = new java.sql.Date(date.getTime());
								
								

								logger.error("Creating Session " );
//								String createSessionQuery = "INSERT INTO session "
//										+ "(user_id) "
//										+ "values(" + 1 + ")";
								JsonArray params2 = new JsonArray().add(rowId).add(sha2).add(date.getTime());
//								INSERT INTO session (user_id, token, expiration) select '1', SHA2('String', 256), now()
								connection.update("INSERT INTO session (user_id, token, expiration) select " + rowId 
													+ ", SHA2( CONCAT( NOW(), 'my secret value' ) , 256), NOW()", sessionResult -> { 
									int newId = sessionResult.result().getKeys().getInteger(0);
											
									if(newId==0) {
										logger.error("Failed Session " + newId );
									}else {
										logger.info("Created Session " + newId );	
									}
									
								});

								logger.info("Printing Json " );
								JsonObject bookJson = new JsonObject();
								bookJson.put("response", "ok");
								bookJson.put("session token", userRows.get(0).getString(1));
								output.append(bookJson.toString());
							}
						}
						context.response().end(output.toString());
					});
				}
			});
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
	private void reports(RoutingContext context) {
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

}
