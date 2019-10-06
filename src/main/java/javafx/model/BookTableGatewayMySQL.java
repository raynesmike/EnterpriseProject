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
import java.util.Scanner;
import java.util.stream.Collectors;

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
		FileInputStream input = null;
		
		try {
			input = new FileInputStream("db.properties");
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
				System.out.println(rs.getInt("id") + rs.getString("title") + rs.getString("summary") +  rs.getInt("year_published") + rs.getString("isbn") );
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
	
	public void createBook(String title, String isbn, int yearPublished, String summary) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//st = conn.prepareStatement("INSERT INTO Book(title, summary, year_published, isbn) VALUES (title,summary,yearPublished,isbn))");
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
				logger.debug("Record inserted and returned key: " + rs.getInt(1));
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
	}
	
	public void readBook() {
		
	}
	

	public void updateBook(Book book) throws GatewayException{
		PreparedStatement st = null;
		
		try{
			conn.setAutoCommit(false);
			
			conn.commit();
			
		} catch (SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
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

	public void deleteBook() {
		
	}
	
//	public void loadBooks(){
//		logger.info("@BookTableGatewayMySQL loading Books()");
//
//		Scanner scanner = null;
//		try {
//			InputStream resource = this.getClass().getResourceAsStream("Books.csv");
//
//			scanner = new Scanner(resource);
//		} catch (Exception e) {
//
//			logger.error(e);
//		}
//		while(scanner.hasNextLine()) {
//			//Shouldn't need a try-catch block here but it's working so I dunno
//			try {
//				String line = scanner.nextLine();
//				logger.debug(line);
//				String[] bookData = line.split(", ");
//				bookList.add(new Book(bookData[0], bookData[1], bookData[2], Integer.parseInt(bookData[3].trim()), Integer.parseInt(bookData[4].trim()), bookData[5]));
//			} catch (Exception e) {
//				logger.error(e);
//			}
//		}
//	}
	

//	public boolean searchBook(String title, String author, String genre, int isbn){
//
//		logger.info("@BookTableGatewayMySQL searchBook()");
//		ArrayList<Book> newArrayList = new ArrayList<Book>();
//	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookTitle().equals(title)).collect(Collectors.toList());
//	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookAuthor().equals(author)).collect(Collectors.toList());
//	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookGenre().equals(genre)).collect(Collectors.toList());
//	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookISBN() == (isbn)).collect(Collectors.toList());
//
//	    if(!newArrayList.isEmpty()) {
//	    	for(Book book: newArrayList) {
//	    		//System.out.println(book);
//				logger.debug(book);
//	    	}
//	    	return true;
//	    }else { 
//	    	return false;
//	    }
//	}
//	
//	public String toString() {
//		String newString = "";
//		for(Book book: bookList) {
//			newString += book.toString() + "\n------------------\n";
//		}
//		return newString;
//	}
//
//	public ArrayList<Book> getBookList() {
//		return bookList;
//	}
//
//	public void setBookList(ArrayList<Book> bookList) {
//		this.bookList = bookList;
//	}


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
