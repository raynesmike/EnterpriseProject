package javafx.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.GatewayException;
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
	private AlertBox alert;
	
	public BookListController(List <Book> books) {
		this.books = books;
		alert = new AlertBox();
	}
	public BookListController(Book book) {
		this.selectedBook = book;
		alert = new AlertBox();
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
    	try {
			if (!MainController.getBookGateway().lockBeforeUpdate(selected)){
				alert = AlertBox.display( "Button", 
						"Someone is updating this record");
				logger.info("Someone is updating this record");
				//TODO: need to update the list after the first updater finish we just need to refresh 
				
				return;
			}
		} catch (GatewayException e) {
    		logger.error("LockBeforeUpdate threw exception");
			logger.error(e);
			return;
		}
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
					onUpdate();
                	//Book selected = bookListView.getSelectionModel().getSelectedItem();
                   
                	//logger.info("double-clicked " + selected);
                	/// BEFORE SHOWING THE VIEW, THE CONTROLLER/MODEL NEEDS TO DO A DATABASE LOCK

					//MainController.showView(ViewType.BOOK_DETAIL, selected);
				}
			}
			
		});
		
	}
}


