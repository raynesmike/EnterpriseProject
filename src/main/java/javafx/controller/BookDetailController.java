package javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.Book;
import javafx.model.GatewayException;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookDetailController{
	private static Logger logger = LogManager.getLogger();

    @FXML
    private TextField fieldISBN, fieldTitle, fieldYear;
    @FXML
    private TextArea areaSummary;
    @FXML
    private Button buttonDelete, buttonUpdate;

    //mode member reference
    private Book book;
    
    public BookDetailController(Book book) {
    	//Instructions say to create a copy, so we're creating a copy manually
		//This is a shallow copy but whatever, it'll work fine.
		this.book = new Book();
		this.book.setId(book.getId());
		this.book.setBookTitle(book.getBookTitle());
		this.book.setYearPublished(book.getYearPublished());
		this.book.setBookISBN(book.getBookISBN());
		this.book.setBookSummary(book.getBookSummary());
    	//this.book = book;
    }
    
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonUpdate) {
			onUpdate();
		} else if(source == buttonDelete) {
			onDelete();
		}
	}
    
	public void initialize() {
		logger.info("@BookDetailController initialize()");

		fieldTitle.setText(this.book.getBookTitle());
		fieldYear.setText(Integer.toString(this.book.getYearPublished()));
		fieldISBN.setText(this.book.getBookISBN());
		areaSummary.setText(this.book.getBookSummary());

	}
	
	@FXML
	public void onUpdate() {
		logger.info("@BookDetailController save()");
		

		try {
			this.book.setBookTitle(fieldTitle.getText());
			this.book.setYearPublished(Integer.parseInt(fieldYear.getText()));
			this.book.setBookISBN(fieldISBN.getText());
			this.book.setBookSummary(areaSummary.getText());
	
			MainController.getBookGateway().updateBook(book);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void onDelete() {
		logger.info("@BookDetailController Delete()");
		// DELETE the book and refresh the pages
		MainController.getBookGateway().deleteBook(book);
		MainController.showView(ViewType.BOOK_LIST, null);
	}

}
