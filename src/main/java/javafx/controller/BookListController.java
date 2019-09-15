package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Main;
import javafx.ViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.Book;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController implements Initializable, MyController {
	private static final Logger logger = LogManager.getLogger();
	@FXML
	private ListView<Book> bookListView;
	
	ObservableList<Book> books;
	
	@FXML
    void onListClick(MouseEvent event) {
		logger.info("@BookListController onListClick()");
		
		if(event.getClickCount() == 2) {
			Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
			if(selectedBook != null) {
//				MainController.getInstance().showView(ViewType.DETAIL1);
				logger.info("Clicked on " + selectedBook);
				MainController.getInstance().showView(ViewType.DETAIL3, selectedBook);
			}
		}
    }
	
	@FXML
    void doButton(ActionEvent event) {
		logger.info("@BookListController onListClick()");
		
		 MainController.getInstance().showView(ViewType.DETAIL3, bookListView.getSelectionModel().getSelectedItem());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("@BookListController onListClick()");
		
		books = FXCollections.observableArrayList();
		books.addAll(Main.firstBookStore.getBookList());
    	bookListView.setItems(books);
    	
	}
}


