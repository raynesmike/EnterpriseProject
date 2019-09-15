package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.Book;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookDetailController implements Initializable, MyController{

    @FXML
    private TextField fieldISBN;

    @FXML
    private TextArea areaSummary;

    @FXML
    private TextField fieldTitle;

    @FXML
    private TextField fieldYear;

    private Book book;
	private static Logger logger = LogManager.getLogger();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("@BookDetailController initialize()");
		
		fieldTitle.setText(book.getBookTitle());
		fieldYear.setText(Integer.toString(book.getBookPublished()));
		fieldISBN.setText(Integer.toString(book.getBookISBN()));
		areaSummary.setText(book.getBookSummary());
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		logger.info("@BookDetailController save()");
		book.setBookTitle(fieldTitle.getText());
		book.setBookPublished(Integer.parseInt(fieldYear.getText()));
		book.setBookISBN(Integer.parseInt(fieldISBN.getText()));
		book.setBookSummary(areaSummary.getText());
		
	}
	public BookDetailController(Book book) {
		this.book = book;
	}
}
