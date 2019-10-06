package javafx.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController {
	private static final Logger logger = LogManager.getLogger();
	@FXML
	private ListView<Book> bookListView;
	@FXML
    private Button buttonDelete, buttonUpdate;
	
	private List<Book> books;
	
	private Book selectedBook;
	
	public BookListController(List <Book> books) {
		this.books = books;
	}
	public BookListController(Book book) {
		this.selectedBook = book;
	}
	
	
	@FXML
    public void handleListButton(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonUpdate) {
			onUpdate();
		}
		if(source == buttonDelete) {
			onDelete();
		}
	}

	// get the book and send it to BOOK_DETAIL view
	public void onUpdate() {

		//Use ListView's getSelected Item
    	Book selected = bookListView.getSelectionModel().getSelectedItem();
		MainController.showView(ViewType.BOOK_DETAIL, selected);
	}
	

	// DELETE the book and refresh the pages
	public void onDelete() {

    	Book selected = bookListView.getSelectionModel().getSelectedItem();
		MainController.getBookGateway().deleteBook(selected);
		MainController.showView(ViewType.BOOK_LIST, null);
	}
	
	
	public void initialize() {
		ObservableList<Book> items = bookListView.getItems();
		
		
		items.addAll(books);
		
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
		
	}
}


