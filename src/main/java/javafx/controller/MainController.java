package javafx.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.BookGateway;
import javafx.Gateway.BookTableGatewayMySQL;
import javafx.Gateway.PublisherGateway;
import javafx.Gateway.PublisherTableGateway;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.model.Publisher;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainController{
	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@FXML
    private static BorderPane rootPane;

	private static BookGateway bookGateway;
	private static PublisherGateway publisherGateway;
	

	
	private MainController() {
	}
	
	public static void initBookGateway() {
		//create gateways
		try {
			bookGateway = new BookTableGatewayMySQL();
			publisherGateway = new PublisherTableGateway();
			
		} catch (javafx.Gateway.GatewayException e) {
			logger.error(e);
			Platform.exit();
		}
	}
	
	public static boolean showView(ViewType viewType, Book book) {
    	logger.info("@MainController showView()");
    	
		FXMLLoader loader = new FXMLLoader();
		logger.debug(viewType);
		List<Book> books;
		List<Publisher> publishers;
		AuditTrailEntry audit;

		if(viewType == ViewType.BOOK_LIST) {
			books = bookGateway.getBooks();
			
			loader.setLocation(MainController.class.getResource("/javafx/view/BookListView.fxml"));
			loader.setController(new BookListController(books));
		
		// CREATE UPDATE AND DELETE
		} else if(viewType == ViewType.BOOK_DETAIL) { 
			publishers = publisherGateway.fetchPublishers();
			
			loader = new FXMLLoader(MainController.class.getResource("/javafx/view/BookDetailView.fxml"));
			loader.setController(new BookDetailController(book, publishers));
			
		} else if(viewType == ViewType.BOOK_AUDIT) { 
			audit = new AuditTrailEntry();
			
			loader = new FXMLLoader(MainController.class.getResource("/javafx/view/BookAuditView.fxml"));
			loader.setController(new BookAuditController(audit, book));
			
		}
		
		Parent viewNode = null;
		try {
			viewNode = loader.load();
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error(e);
		}

		rootPane.setCenter(viewNode);
		return true;
	}
	
	public static BookGateway getBookGateway() {
		return bookGateway;
	}

	public static void setBookGateway(BookGateway bookGateway) {
		MainController.bookGateway = bookGateway;
	}
	
	public static void close() {
		bookGateway.close();
	}
		
	public BorderPane getRootPane() {
		return rootPane;
	}

	public static void setRootPane(BorderPane rPane) {
		rootPane = rPane;
	}
}
