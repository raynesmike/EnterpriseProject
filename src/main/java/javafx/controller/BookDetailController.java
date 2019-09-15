package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.Book;
import javafx.scene.control.Button;
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
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		System.out.println(MainController.getInstance()); 
		fieldTitle.setText(book.getBookTitle());
		fieldYear.setText(Integer.toString(book.getBookPublished()));
		fieldISBN.setText(Integer.toString(book.getBookISBN()));
		areaSummary.setText(book.getBookSummary());
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		book.setBookTitle(fieldTitle.getText());
		book.setBookPublished(Integer.parseInt(fieldYear.getText()));
		book.setBookISBN(Integer.parseInt(fieldISBN.getText()));
		book.setBookSummary(areaSummary.getText());
		System.out.println(book);
		
	}
	public BookDetailController(Book book) {
		this.book = book;
	}
}
