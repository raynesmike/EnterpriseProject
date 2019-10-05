package javafx.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javafx.ViewType;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController {
	private static final Logger logger = LogManager.getLogger();
	@FXML
	private ListView<Book> bookListView;
	
	private List<Book> books;
	
	public BookListController(List <Book> books) {
		this.books = books;
	}
	
//	@FXML
//    void doButton(ActionEvent event) {
//		logger.info("@BookListController onListClick()");
//		
//		 MainController.getInstance().showView(ViewType.BOOK_DETAIL, bookListView.getSelectionModel().getSelectedItem());
//	}
	
	public void initialize() {
		ObservableList<Book> items = bookListView.getItems();
		
		
		//items.addAll(books);
		
		bookListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                	//Use ListView's getSelected Item
                	Book selected = bookListView.getSelectionModel().getSelectedItem();
                   
                	logger.info("double-clicked " + selected);
						MainController.showView(ViewType.BOOK_DETAIL, selected);
					}
				}
			
		});
		
		
//		books = FXCollections.observableArrayList();
//		books.addAll(Main.firstBookStore.getBookList());
//    	bookListView.setItems(books);
//    	
	}
}


