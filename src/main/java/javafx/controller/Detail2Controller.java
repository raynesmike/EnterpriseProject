package javafx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class Detail2Controller implements Initializable, MyController {
	private static final Logger logger = LogManager.getLogger();
	@FXML
	private ListView<Book> bookListView;
	
	ObservableList<Book> books;
	
//	public Detail2Controller(List<Book> beers) {
//		this.books = beers;
//	}
	
	@FXML
    void onListClick(MouseEvent event) {
		if(event.getClickCount() == 2) {
			Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
			if(selectedBook != null) {
//				MainController.getInstance().showView(ViewType.DETAIL1);
				logger.info("Click");
			}
		}
    }
	
	@FXML
    void doButton(ActionEvent event) {
		// logger.error("switch to view 1");
//		for(Book book: Main.firstBookStore.getBookList()) {
//			bookListView.(Main.firstBookStore.getBookList());
//		}

//		MainController.getInstance().showView(ViewType.DETAIL1);
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		books = FXCollections.observableArrayList();
    	for(Book book: Main.firstBookStore.getBookList()) {
    		books.add(book);
    	}		
    	bookListView.setItems(books);
    	
	}
}


