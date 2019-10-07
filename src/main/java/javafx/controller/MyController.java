package javafx.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.Book;
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
			MainController.showView(ViewType.BOOK_DETAIL, new Book());
			return;
		}
		if(source == bookRead) {
			//get a collection of books from the gateway
			MainController.showView(ViewType.BOOK_LIST, null);
			return;
		}
		if(source == bookUpdate) {
			// List the book to be able to Update a specific book
			MainController.showView(ViewType.BOOK_LIST, null);
			return;
		}
		if(source == bookDelete) {
			// List the book to be able to Delete a specific book
			MainController.showView(ViewType.BOOK_LIST, null);
			return;
		}
	}
}
