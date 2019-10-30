package javafx.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.GatewayException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class BookDetailController{
	private static Logger logger = LogManager.getLogger();

    @FXML
    private TextField fieldISBN, fieldTitle, fieldYear;
    @FXML
    private TextArea areaSummary;
    @FXML
    private Button buttonDelete, buttonUpdate, buttonCreate;
    @FXML
    private Text alertISBN, alertYear, alertTitle, alertStatus;
    @FXML
    private ComboBox<AuditTrailEntry> publisherBox;

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
		AlertBox alert = new AlertBox();

		alert = AlertBox.display( source.toString()+"Button", 
				"You are leaving Detail, Do you want to save your changes?");
		System.out.println("ALERTBOXXXXXXXXX" + alert.getReply());
		if(alert.getReply().equals("yes")) {
			if(source == buttonCreate) {
				onCreate();
			} else if(source == buttonUpdate) {

				onUpdate();
			} else if(source == buttonDelete) {
				onDelete();
			}
		} else if(alert.getReply().equals("no")) {
			//TODO: rollback
		} else {
			//TODO: Do Nothing
		}
	}
	
    
	public void initialize() {
		logger.info("@BookDetailController initialize()");

		fieldTitle.setText(this.book.getBookTitle());
		fieldYear.setText(Integer.toString(this.book.getYearPublished()));
		fieldISBN.setText(this.book.getBookISBN());
		areaSummary.setText(this.book.getBookSummary());

	}
	@FXML public void onCreate() {
    	logger.info("@BookCreateController onSearch()");
    	try {
	    	String title = fieldTitle.getText();
	    	String isbn = fieldISBN.getText();
	    	String summary = areaSummary.getText();
	    	int yearPublished = Integer.parseInt(fieldYear.getText());
	    	
	    	if (!validate(title, summary, isbn, yearPublished)){
				//Exit function, feedback sent and nothing more to do here.
				logger.info("Exiting onCreate");
				return;
			}
			//If we made it this far, check for an initialized book
	
			// Book doesn't exist for this session, create a new book!
			try {
	
				if (this.book.getId() == 0) {
				    // New book, populate the internal values
	                book.setId(MainController.getBookGateway().createBook(title, isbn, yearPublished, summary));
	                book.setBookTitle(title);
	                book.setBookISBN(isbn);
	                book.setYearPublished(yearPublished);
	                book.setBookSummary(summary);
	                alertStatus.setText("Create Success");
	            } else {
	                // Not a new book, simply update it.
	                MainController.getBookGateway().updateBook(book);
	//
	                alertStatus.setText("Updated the new Book");
	            }
	
			} catch (Exception e) {
				logger.debug(fieldTitle.getText() + Integer.parseInt(fieldISBN.getText().trim())
						+ Integer.parseInt(fieldYear.getText()) + areaSummary.getText());
				logger.error("ERROR: @BookCreateController handleButtonAction" + e.toString());
				logger.error(e);
			}
	
			logger.info("Made it to end of onCreate()");
		} catch(Exception last) {
			logger.error("ERROR: @BookCreateController" + last.toString());
		}
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
			MainController.showView(ViewType.BOOK_LIST, null);
//            alertStatus.setText("Updated SUCCESS");
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
	
	private Boolean validate(String title, String summary, String isbn, int yearPublished) {
		Boolean isValid = true;
    	alertTitle.setText("");
    	if(!book.setBookTitle(title)) {
    		alertTitle.setText("Between 1 and 255 chars");
    		isValid = false;
    	}

    	areaSummary.setText("");
    	if(!book.setBookSummary(summary)) {
    		areaSummary.setText("Summary must be < 65536 characters. can be blank");
    		isValid = false;
    	}
    	
    	alertYear.setText("");
    	if(!book.setYearPublished(yearPublished)) {
    		alertYear.setText("1455 - Current Year");
    		isValid = false;
    	}
    	
    	alertISBN.setText("");
    	if(!book.setBookISBN(isbn)) {
    		alertISBN.setText("Less than 13 characters.");
    		isValid = false;
    	}
    	return isValid;
	}

}
