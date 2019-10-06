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
    	this.book = book;
    }
    
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonSave) {
			onSave();
		} else if(source == buttonSave) {
			onDelete();
		}
	}
    
	public void initialize() {
		logger.info("@BookDetailController initialize()");
		
//		fieldTitle.setText(book.getBookTitle());
//		fieldYear.setText(Integer.toString(book.getBookPublished()));
//		fieldISBN.setText(Integer.toString(book.getBookISBN()));
//		areaSummary.setText(book.getBookSummary());
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
