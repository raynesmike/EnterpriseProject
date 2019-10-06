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
    private Text alertISBN, alertYear, alertTitle, alertSummary, alertCreateStatus;

    @FXML
    private Button buttonCreate;
	private static BookGateway bookGateway;

	private Book bookToAdd;

	private static final Logger logger = LogManager.getLogger();
	
	public BookCreateController() {
		//Initialize the dummy book
		this.bookToAdd = new Book();	// Will start with id = 0
	}
	
	public static void initBookGateway() {
		//create gateways
		try {
			bookGateway = new BookTableGatewayMySQL();
			
		} catch (javafx.model.GatewayException e) {
			//e.printStackTrace();
			logger.error(e);
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

    	Boolean isValid = true;
    	
    	alertTitle.setText("");
//    	a. title must be between 1 and 255 chars
    	if(title.length() < 1 || title.length() > 255) {
    		alertTitle.setText("title must be between 1 and 255 chars");
    		isValid = false;
    	}

		alertSummary.setText("");
//    	b. summary must be < 65536 characters. can be blank
    	if(summary.length() > 65536) {
    		alertSummary.setText("summary must be < 65536 characters. can be blank");
    		isValid = false;
    	}
    	alertYear.setText("");
    	int yearPublished = Integer.parseInt(labelYearPublished.getText());
//    	c. year_published must be between 1455 and the current year (inclusive)
    	if(yearPublished < 1455  || yearPublished > 2019) {
    		alertYear.setText("1455 up to current year");
    		isValid = false;
    	}
    	//yearText = Integer.toString(yearPublished);
    	
//    	d. isbn cannot be > 13 characters. can be blank Implement these business rules as validation methods
    	alertISBN.setText("");
    	if(isbn.length() > 13) {
    		alertISBN.setText("Cannot be > 13 characters.");
    		isValid = false;
    	}

		// Check if all fields are valid
		if (!isValid){
			//Exit function, feedback sent and nothing more to do here.
			logger.info("Exiting onCreate");
			return;
		}

		//If we made it this far, check for an initialized book

		// Book doesn't exist for this session, create a new book!
		try {

			if (this.bookToAdd.getId() == 0) {
			    // New book, populate the internal values
                bookToAdd.setId(MainController.getBookGateway().createBook(title, isbn, yearPublished, summary));
                bookToAdd.setBookTitle(title);
                bookToAdd.setBookISBN(isbn);
                bookToAdd.setBookPublished(yearPublished);
                bookToAdd.setBookSummary(summary);
                alertCreateStatus.setText("Create Success");
            }
			else {
                // Not a new book, simply update it.
                MainController.getBookGateway().updateBook(bookToAdd);
//
//                alertCreateStatus.setText("Updated the new Book");
            }

		} catch (Exception e) {
			//System.out.println(labelTitle.getText() + Integer.parseInt(labelISBN.getText().trim())
			//		+ Integer.parseInt(labelYearPublished.getText()) + labelSummary.getText());
			logger.debug(labelTitle.getText() + Integer.parseInt(labelISBN.getText().trim())
					+ Integer.parseInt(labelYearPublished.getText()) + labelSummary.getText());
			logger.error("ERROR: @BookCreateController handleButtonAction" + e.toString());
			logger.error(e);
		}

		logger.info("Made it to end of onCreate()");
    }
   

}
