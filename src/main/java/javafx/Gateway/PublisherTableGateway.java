package javafx.Gateway;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;

import javafx.model.Book;
import javafx.model.Publisher;

public class PublisherTableGateway implements PublisherGateway {
	private Connection conn;
//	private ArrayList<Book> bookList;
	private static Logger logger = LogManager.getLogger();
//	
	
	public PublisherTableGateway() throws GatewayException {
		conn = null;

		logger.info("DB Connection Begin");
		
		//connect to data source and create a connection instance
		//read db credentials from properties file
		Properties props = new Properties();
		//FileInputStream input = null;
		InputStream input = null;
		
		try {
			//input = new FileInputStream("../db.properties");
			input = this.getClass().getResourceAsStream("../db.properties");
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
	public List<Publisher> fetchPublishers(){

		List<Publisher> publishers = new ArrayList<Publisher>();
		PreparedStatement st = null;
		ResultSet rs = null;

		//1. prepare the statement
		try {
			//Parameterized because I HAVE TO
			String query = "select * "
					+ " from Publisher";
			st = conn.prepareStatement(query);
			
			//2. execute the query
			rs = st.executeQuery();
			//3 transform db data into objects
			while(rs.next()) {
				// Create new Publisher object
				Publisher newPublisher = new Publisher(rs.getInt("id"), rs.getString("publisher_name"), rs.getDate("date_added"));
				//Push this to collection

				publishers.add(newPublisher);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//4. cleanup
		}
		return publishers;
	}
	


}
