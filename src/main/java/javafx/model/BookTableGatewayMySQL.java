package javafx.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookTableGatewayMySQL implements BookGateway {
	private Connection conn;
//	private ArrayList<Book> bookList;
	private static Logger logger = LogManager.getLogger();
//	
	
	public BookTableGatewayMySQL() throws GatewayException {
		conn = null;

		logger.info("DB Connection Begin");
		
		//connect to data source and create a connection instance
		//read db credentials from properties file
		Properties props = new Properties();
		//FileInputStream input = null;
		InputStream input = null;
		
		try {
			//input = new FileInputStream("../db.properties");
			input = this.getClass().getResourceAsStream("db.properties");
			logger.debug(input);
			props.load(input);
			input.close();
			
			//create the datasource
			MysqlDataSource ds = new MysqlDataSource();
			ds.setUrl(props.getProperty("MYSQL_DB_URL"));
			ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
			ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
		
			//create the connection
			conn = ds.getConnection();
			logger.info("DB Connection Finish");
		
		}catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e);
			throw new GatewayException(e);
		}
		
		
	}
	public List<Book> getBooks(){
		List<Book> books = new ArrayList<Book>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//1. prepare the statement
		try {
			//Parameterized because I HAVE TO
			String query = "select * "
					+ " from Book";
			//st = conn.prepareStatement("select * from Book");
			st = conn.prepareStatement(query);
			
			//2. execute the query
			rs = st.executeQuery();
			
			//3 transform db data into objects
			while(rs.next()) {
//				System.out.println(rs.getInt("id") + rs.getString("title") + rs.getString("summary") +  rs.getInt("year_published") + rs.getString("isbn") );
				// Create new Book object
				Book newBook = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"), rs.getInt("year_published"), rs.getString("isbn"));
				//Push this to collection
				books.add(newBook);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//4. cleanup
		}
		return books;
	}
	
	public int createBook(String title, String isbn, int yearPublished, String summary) {
		PreparedStatement st = null;
		ResultSet rs = null;

		int returnKey = -1;
		
		try {
			String query = "INSERT INTO Book "
					+ "(title, summary, year_published, isbn) "
					+ "values(?, ?, ?, ?)";
			st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			// PLUG IN THE VALUES
			st.setString(1, title);
			st.setString(2, summary);
			st.setInt(3, yearPublished);
			st.setString(4, isbn);
			st.executeUpdate();	//This executes the query!

			//We asked for a return of the key generated, so get it back
			rs = st.getGeneratedKeys();
			// rs has the ability to be null for some reason
			if (rs != null && rs.next()){
				// THIS is where the key would be!
				returnKey = rs.getInt(1);
				logger.debug("Record inserted and returned key: " + returnKey);
			}
			
		} catch(SQLException e) {
			//e.printStackTrace();
			//TODO MAKE GATEWAYEXCEPTION WORK
			//throw new GatewayException(e);
			logger.error(e);
		} finally {
			//4. cleanup
			//TODO This needs to close the connection
		}

		//RETURN THE GENERATED KEY
		return returnKey;
	}
	
	public Book readBook(String title) {
		return null;
	}

	public boolean lockBeforeUpdate(Book book) throws GatewayException{
		// Starts a DB Transaction to lock a record in the Book table.
		PreparedStatement st = null;
		logger.info("@lockBeforeUpdate()");

		try{
			conn.setAutoCommit(false);

			// Use a select query using the FOR UPDATE clause to WRITE LOCK the record!
			String up_query = "SELECT * "
					+ "FROM Book "
					+ "WHERE id = ? "
					+ "FOR UPDATE";
			st = conn.prepareStatement(up_query);
			st.setInt(1, book.getId());

			// Execute the query, generate the lock, DO NOT COMMIT.
			st.execute();


		} catch (Exception e){
			logger.error(e);
			// If this fails, need to return
			// This indicates it failed to lock the record
			return false;
		}

		// Function completed successfully
		return true;
	}

	public void updateBook(Book book) throws GatewayException{
		PreparedStatement lock_st = null;
		PreparedStatement st = null;
		logger.info("@updateBook()");
		
		try{

			// Now it should be locked!

			//Write SQL query to update the book entry - book object contains the primary key!
			String query = "UPDATE Book "
					+ "SET title = ?, summary = ?, year_published = ?, isbn = ? "
					+ "WHERE id = ?";
			st = conn.prepareStatement(query);
			st.setString(1, book.getBookTitle());
			st.setString(2, book.getBookSummary());
			st.setInt(3, book.getYearPublished());
			st.setString(4, book.getBookISBN());
			st.setInt(5, book.getId());	// THIS is the primary key to be updated

			st.executeUpdate();	//This executes the query!
			logger.debug("BEFORE COMMIT");
			logger.debug(st);
			
			conn.commit();
			logger.debug("AFTER COMMIT");
			
		} catch (SQLException e){
			logger.error(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//e1.printStackTrace();
				logger.error(e);
			}
		} finally{
			//clean up
			try {
				if( st != null)
					st.close();
				conn.setAutoCommit(true);
			} catch(SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
	}

	public void deleteBook(Book book) {
		logger.info("@DeleteBook()");
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			String query = "DELETE from Book " 
						+ "WHERE id = ?";

			st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setInt(1, book.getId());
			
			// PLUG IN THE VALUES
			
			st.executeUpdate();	//This executes the query!

			//We asked for a return of the key generated, so get it back
			rs = st.getGeneratedKeys();
		
			
		} catch(SQLException e) {
			//e.printStackTrace();
			//TODO MAKE GATEWAYEXCEPTION WORK
			//throw new GatewayException(e);
			logger.error(e);
		} finally {
			//4. cleanup
			//TODO This needs to close the connection
		}
	}

	@Override
	public void close() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void insertBook(Book Book) {
		// TODO Auto-generated method stub
		
	}
	
	
}
