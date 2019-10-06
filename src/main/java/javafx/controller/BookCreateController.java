package javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.model.BookGateway;
import javafx.model.BookTableGatewayMySQL;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    private Text alertISBN, alertYear, alertTitle, alertSummary;

    @FXML
    private Button buttonCreate;
	private static BookGateway bookGateway;

	private static final Logger logger = LogManager.getLogger();
	
	public BookCreateController() {
	}
	
	public static void initBookGateway() {
		//create gateways
		try {
			bookGateway = new BookTableGatewayMySQL();
			
		} catch (javafx.model.GatewayException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonCreate) {
			onCreate();
		}
	}
	
	@FXML public void onCreate() {
    	logger.info("@BookCreateController onSearch()");
    	
    	String title = labelTitle.getText();
    	String isbn = labelISBN.getText();
    	String yearText = labelYearPublished.getText();
    	String summary = labelSummary.getText();
    	

//    	a. title must be between 1 and 255 chars
    	if(title.length() < 1 || title.length() > 255) {
    		alertTitle.setText("title must be between 1 and 255 chars");
    	}

//    	b. summary must be < 65536 characters. can be blank
    	if(summary.length() > 65536) {
    		alertSummary.setText("summary must be < 65536 characters. can be blank");
    	}
    	
    	int yearPublished = Integer.parseInt(labelYearPublished.getText());
//    	c. year_published must be between 1455 and the current year (inclusive)
    	if(yearPublished < 1455  || yearPublished > 2019) {
    		alertISBN.setText("1455 up to current year");
    	}
    	

//    	int isbn = Integer.parseInt(labelISBN.getText());
//    	d. isbn cannot be > 13 characters. can be blank Implement these business rules as validation methods 
    	if(isbn.length() > 13) {
    		alertYear.setText("Cannot be > 13 characters.");
    	}
    	
    	try {	

    	
//    	MainController.getBookGateway().createBook(title, isbn, yearPublished, summary);
    	
    	}catch(Exception e){
    		System.out.println(labelTitle.getText()+Integer.parseInt(labelISBN.getText().trim())
			+Integer.parseInt(labelYearPublished.getText())+labelSummary.getText());
    		logger.error("ERROR: @BookCreateController handleButtonAction" + e.toString());
    	}
    }
   

}
