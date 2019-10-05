package javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.ViewType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.model.Book;
import javafx.model.BookGateway;
import javafx.model.BookTableGatewayMySQL;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainController{
	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@FXML
    private static BorderPane rootPane;

	private static BookGateway bookGateway;

	
	private MainController() {
	}
	
	public static void initBookGateway() {
		//create gateways
		try {
			bookGateway = new BookTableGatewayMySQL();
			
		} catch (javafx.model.GatewayException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	public static boolean showView(ViewType viewType, Book book) {
    	logger.info("@MainController showView()");
    	
		FXMLLoader loader = null;
		System.out.println(viewType);
		
		//CREATE
		if(viewType == ViewType.BOOK_CREATE) { 
			loader = new FXMLLoader(MainController.class.getResource("../view/BookCreateView.fxml"));
			loader.setController(new BookCreateController());
		
		//READ
		} else if(viewType == ViewType.BOOK_LIST) {
			List<Book> books = bookGateway.getBooks();
			
			loader = new FXMLLoader(MainController.class.getResource("../view/BookListView.fxml"));
			loader.setController(new BookListController(books));
		
		//UPDATE AND DELETE
		} else if(viewType == ViewType.BOOK_DETAIL) { 
			loader = new FXMLLoader(MainController.class.getResource("../view/BookDetailView.fxml"));
			loader.setController(new BookDetailController((Book) book));
			
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
//	
//	public static MainController getInstance() {
//		if(instance == null)
//			instance = new MainController();
//		return instance;
//	}

	// eclipse used the wrong import: import java.awt.event.ActionEvent;
	// void onBeer(ActionEvent event) {
//	@FXML
//    void onBook(ActionEvent event) {
//    	logger.info("@MainController onBook()");
//		
//		showView(ViewType.BOOK_LIST, null);
//    }
//	
//	@FXML
//    void quitYes(ActionEvent event) {
//    	logger.info("@MainController Quit()");
//    	
//		Platform.exit();
//    }
			
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		// TODO Auto-generated method stub
//
//	}

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
