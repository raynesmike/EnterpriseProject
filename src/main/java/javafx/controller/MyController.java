package javafx.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.ViewType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.model.Book;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;


public class MyController {
	// THIS USED TO BE A SINGLETON. BUT I REMOVED THAT AND MADE IT NASTY STATIC
	private static Logger logger = LogManager.getLogger();
	

    @FXML
    private MenuItem bookCreate, bookRead, bookDelete, bookUpdate;
    @FXML
    private MenuItem quitYes;
	
	public MyController() {
	}
	
	@FXML
	private void handleBook(ActionEvent action) throws Exception {
		Object source = action.getSource();
		if(source == quitYes) {
			Platform.exit();
			
		}
		if(source == bookCreate) {
			//get a collection of books from the gateway
			MainController.showView(ViewType.BOOK_CREATE, null);
			return;
		}
		if(source == bookRead) {
			//get a collection of books from the gateway
			MainController.showView(ViewType.BOOK_LIST, null);
			return;
		}
		if(source == bookUpdate) {
			Book book = new Book();
			book.setGateway(MainController.getBookGateway());
			MainController.showView(ViewType.BOOK_DETAIL, null);
			return;
		}
		if(source == bookDelete) {
			Book book = new Book();
			book.setGateway(MainController.getBookGateway());
			MainController.showView(ViewType.BOOK_DETAIL, null);
			return;
		}
	}
}
