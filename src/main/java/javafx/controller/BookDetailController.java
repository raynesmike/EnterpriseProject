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
    private Button buttonDelete, buttonSave;

    //mode member reference
    private Book book;
    
    public BookDetailController(Book book) {
    	//Instructions say to create a copy, so we're creating a copy manually
		//This is a shallow copy but whatever, it'll work fine.
		this.book = new Book();
		this.book.setId(book.getId());
		this.book.setBookTitle(book.getBookTitle());
		this.book.setBookISBN(book.getBookISBN());
		this.book.setBookSummary(book.getBookSummary());
    	//this.book = book;
    }
    
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonSave) {
			onSave();
		} else if(source == buttonSave) {
			onDelete();
		}else if(source == buttonDelete) {
			onDelete();
		}
	}
    
	public void initialize() {
		logger.info("@BookDetailController initialize()");

		fieldYear.setText(Integer.toString(this.book.getBookPublished()));
		fieldISBN.setText(Integer.toString(this.book.getBookISBN()));
		areaSummary.setText(this.book.getBookSummary());
		fieldTitle.setText(this.book.getBookTitle());

	}
	
	@FXML
	public void onSave() {
		logger.info("@BookDetailController save()");

//		book.setBookTitle(fieldTitle.getText());
//		book.setBookPublished(Integer.parseInt(fieldYear.getText()));
//		book.setBookISBN(Integer.parseInt(fieldISBN.getText()));
//		book.setBookSummary(areaSummary.getText());
		
	}
	
	@FXML
	public void onDelete() {
		logger.info("@BookDetailController Delete()");
		
	}

}
