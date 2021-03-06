package javafx.controller;

import java.io.IOException;
import java.util.List;

import javafx.Gateway.*;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.auth.LoginDialog;
import javafx.auth.Sha;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.model.Publisher;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

public class MainController{
	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@FXML
    private static BorderPane rootPane;

	private static BookGateway bookGateway;
	private static PublisherGateway publisherGateway;
	private static ViewType currentView = ViewType.NONE;
	private static BookDetailController bdc;
	private static Book book;

	// HTTP Session token
	private static String session_token;

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
		AlertBox alert;


		setCurrentView(viewType);
		logger.debug("ViewType = " + getCurrentView());

		if(viewType == ViewType.BOOK_LIST) {
			books = bookGateway.getBooks();
			
			loader.setLocation(MainController.class.getResource("/javafx/view/BookListView.fxml"));
			loader.setController(new BookListController(books));
		
		// CREATE UPDATE AND DELETE
		} else if(viewType == ViewType.BOOK_DETAIL) { 
			publishers = publisherGateway.fetchPublishers();
			
			loader = new FXMLLoader(MainController.class.getResource("/javafx/view/BookDetailView.fxml"));
			loader.setController(setBdc(new BookDetailController(book, publishers)));
			
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

	public static ViewType getCurrentView() {
		return currentView;
	}

	public static void setCurrentView(ViewType currentView) {
		MainController.currentView = currentView;
	}

	/**
	 * @return the book
	 */
	public static Book getBook() {
		return MainController.book;
	}

	/**
	 * @param book the book to set
	 */
	public static void setBook(Book book) {
		MainController.book = book;
	}

	/**
	 * @return the bdc
	 */
	public static BookDetailController getBdc() {
		return bdc;
	}

	/**
	 * @param bdc the bdc to set
	 */
	public static BookDetailController setBdc(BookDetailController bdc) {
		MainController.bdc = bdc;
		return bdc;
	}

	/**
	 * Sets the session token for HTTP requests
	 * @param token - token string usually retrieved from login info
	 */
	public static void setSessionToken(String token){
		session_token = token;
	}

	/**
	 * Get the session token for HTTP requests
	 * @return - Session token string
	 */
	public static String getSessionToken(){ return session_token;}
	

}
