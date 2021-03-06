package webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

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

public class Verticle extends AbstractVerticle {
	private static final Logger logger = LogManager.getLogger();
	private static final String DEST_FILE_PATH = "excel.xls";

	private SQLClient dbClient;
	private Vertx vertx;
	private String currentSessionKey;

	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {

		logger.info("Stopping verticle...");

		dbClient.close();

		stopPromise.complete();
	}
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		logger.info("Starting verticle...");
		vertx = Vertx.vertx();
		RoutingContext context = null;
		

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
					//connection.queryWithParams("SELECT * FROM User WHERE username = ? and password_hash in (select sha2(?, 256))"
                    connection.queryWithParams("SELECT * FROM User WHERE username = ? and password_hash = ?"
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
													+ ", SHA2( CONCAT( NOW(), 'my secret value' ) , 256), CURRENT_TIMESTAMP + interval '1' minute", sessionCreate -> {
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
		
		String testAllowed = "16c3bb6f425a2e0b0d54876cd52399884ec7170892699ae1fd707d3ecf2fd7b9"; //Bob is allowed and not expired session
		String testNotAllowed = "2629902373a6c284035a7cbe8d93fcd2313698ab63c8e97946c94d044db2f320"; // sue is not allowed even with session

		String queryAllowed = "SELECT permission.allowed FROM permission inner join session on permission.user_id = session.user_id "
				+ "WHERE session.token = ? and "
				+ "permission.permission = 'book report' and session.expiration >now() and permission.allowed = 1";
		
		context.response().putHeader("Content-Type", "text/html");
		StringBuilder output = new StringBuilder();
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
			} else {
				
				logger.info("Checking if user is allowed and not expired");
				
				SQLConnection connection = ar.result();

				// Get bearer Authorization heading.
				String auth_header = context.request().getHeader("Authorization");
				String auth_token = "";
				if (auth_header != null) {
					logger.debug(auth_header);
					// Check if bearer token header exists
					logger.debug(auth_header.substring(0, 7));
					if (auth_header.substring(0, 7).equals("Bearer ")) {
						//Trim
						auth_token = auth_header.substring(7);
					} else {
						logger.warn("Authorization token did not parse (no 'bearer ' in the beginning of the field)");
					}
				}
				logger.debug(auth_token);
				JsonArray params = new JsonArray().add(auth_token);
				//JsonArray params = new JsonArray().add(testAllowed);

				
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
//						logger.info("CHECKING STATUS " + allowed.get(0).getInteger(1));

						logger.debug(allowed);
						//logger.info("CHECKING STATUS " + allowed.get(0).getInteger(1));

						if(allowed.size() < 1) {
							context.response().setStatusCode(401);
							context.response().end("User is Not Allowed");
						} else {
							logger.info("User is Allowed ");
							
							connection.query("SELECT Book.title, Publisher.publisher_name, Book.year_published "
									+ "FROM Book, Publisher "
									+ "WHERE Book.publisher_id = Publisher.id order by publisher_name", result -> {
								connection.close();
								
								if(result.failed()) {
									logger.error("Database preparation error", result.cause());
									// promise.fail(create.cause());
								} else {
									// promise.complete();
									List<JsonArray> rows = result.result().getResults();
									logger.info("Iterating over rows of length " + rows.size() + "...");

									createExcel(rows);
								}
								
								//context.response().end(output.toString());
                                context.response().putHeader("Content-Type", "application/vnd.ms-excel");
                                context.response().putHeader("Content-Disposition", "attachment; filename=" + DEST_FILE_PATH);
                                context.response().sendFile(DEST_FILE_PATH);

							});
						}
					}
				});
			

			}
		});
		
	}

	public void createExcel(List<JsonArray> rows) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("LETS COUNT THEM BOOK YEEEAAAH");

		//title row
		//create a title font
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontName("Courier New");
		titleFont.setFontHeightInPoints((short) 24);
		titleFont.setBold(true);
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);
		
		//sheet.setColumnWidth(0, 5000);
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Publishers & Books");
		cell.setCellStyle(titleStyle);
		
		//row indexes are base 0 of course
		//let's put a space between title and header rows, and start header and data on row 2
		int rowNum = 2;

		//set default alignment of Age column to be centered
		HSSFCellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);
		sheet.setDefaultColumnStyle(2, centerStyle);
		
		//create a bold font to use for header row and summary row
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle boldStyle = workbook.createCellStyle();
		boldStyle.setFont(font);
		//create a bold + centered font to use for age header and average
		//surely there is a better way to apply 2 different styles to 1 cell???
		HSSFCellStyle boldCenterStyle = workbook.createCellStyle();
		boldCenterStyle.cloneStyleFrom(boldStyle);
		boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);

		//create header row
		row = sheet.createRow(rowNum++);
		//add cells 0 to 3
		int cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Book Title");
		cell.setCellStyle(boldStyle);

		cell = row.createCell(cellNum++);
		cell.setCellValue("Publisher");
		cell.setCellStyle(boldStyle);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue("Year Published");
		cell.setCellStyle(boldCenterStyle);
		
		int bookCount=0;
		int publisherCount=0;
		int i=0;
		
		for(JsonArray book : rows) {
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			cell = row.createCell(cellNum++);
			cell.setCellValue(book.getString(0));
			cell = row.createCell(cellNum++);
			cell.setCellValue(book.getString(1));
			cell = row.createCell(cellNum++);
			cell.setCellValue(book.getInteger(2));
			
			if(!("Unknown".equals(book.getString(1)))) {
				publisherCount++;
			}
			i++;
			bookCount++;

		}
		
		for(i = 0; i < bookCount; i++ ) {
			
		}
		row = sheet.createRow(4 + i++);
		cell = row.createCell(0);;
		cell.setCellValue("Total Publisher");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(2);
		
		cell.setCellFormula("" +publisherCount );
		cell.setCellStyle(boldCenterStyle);
		
		row = sheet.createRow(4 + i++);
		cell = row.createCell(0);
		cell.setCellValue("Total Books");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(2);
		
		cell.setCellFormula("" + bookCount);
		cell.setCellStyle(boldCenterStyle);
		
		sheet.autoSizeColumn(0);

		//write to a file
		try (FileOutputStream f = new FileOutputStream(new File(DEST_FILE_PATH))) {
			workbook.write(f);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
