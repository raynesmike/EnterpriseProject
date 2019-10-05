package javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.model.BookGateway;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookCreateController {
    @FXML
    private TextField labelYearPublished, labelTitle, labelISBN;

    @FXML
    private TextArea labelSummary;

    @FXML
    private Button buttonCreate;
	private static BookGateway bookGateway;

    

	private static final Logger logger = LogManager.getLogger();
	private Book book;
	
	public BookCreateController() {
		this.book = null;
	}
	
	public void initialize() {
		// TODO print all the book title here

	}
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonCreate) {
			onCreate();
		}
	}
	
	@FXML public void onCreate() {
    	logger.info("@BookCreateController onSearch()");
    	
    	try {	
    	book.setBookTitle(labelTitle.getText());
    	book.setBookISBN(Integer.parseInt(labelISBN.getText().trim()));
    	book.setBookPublished(Integer.parseInt(labelYearPublished.getText()));
    	book.setBookSummary(labelSummary.getText());
    	
    	bookGateway.createBook(book);
    	}catch(Exception e){
    		logger.error("ERROR: @BookCreateController onSearch" + e.toString());
    	}
    }
   

}
