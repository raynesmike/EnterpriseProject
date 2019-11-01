package javafx.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.GatewayException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.model.Publisher;
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
    private Button buttonDelete, buttonUpdate, buttonCreate, buttonAudit;
    @FXML
    private Text alertISBN, alertYear, alertTitle, alertStatus;
    @FXML
    private ComboBox<Publisher> publisherBox;
    private List<Publisher> publishers;

    //mode member reference
    private Book book;
    
    public BookDetailController(Book book, List<Publisher> publishers) {
    	//Instructions say to create a copy, so we're creating a copy manually
		//This is a shallow copy but whatever, it'll work fine.
		this.book = new Book();
		this.book.setId(book.getId());
		this.book.setBookTitle(book.getBookTitle());
		this.book.setYearPublished(book.getYearPublished());
		this.book.setBookISBN(book.getBookISBN());
		this.book.setBookSummary(book.getBookSummary());
		this.book.setPublisher_id(book.getPublisher_id());
		this.publishers=publishers;
    	//this.book = book;
    }
    
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		AlertBox alert = new AlertBox();

		alert = AlertBox.display( true , 
				"You are leaving Detail, Do you want to save your changes?");
		System.out.println("ALERTBOXXXXXXXXX" + alert.getReply());
		if(alert.getReply().equals("yes")) {
			if(source == buttonCreate) {
				onCreate();
			} else if(source == buttonUpdate) {

				onUpdate();
			} else if(source == buttonDelete) {
				onDelete();
			} else if(source == buttonAudit) {
				onAudit();
			}
		} else if(alert.getReply().equals("no")) {
			//TODO: rollback
			try {
				MainController.getBookGateway().rollbackPendingTransaction();
			} catch (GatewayException e) {
				logger.error(e);
			}
		} else {
			//TODO: Do Nothing
		}
	}
	
    

	@FXML public void onCreate() {
    	logger.info("@BookCreateController onSearch()");
    	try {
	    	String title = fieldTitle.getText();
	    	String isbn = fieldISBN.getText();
	    	String summary = areaSummary.getText();
	    	int yearPublished = Integer.parseInt(fieldYear.getText());
	    	int publisher_id = publishers.indexOf(publisherBox.getValue())+1;
//			this.book.setPublisher_id(publishers.indexOf(publisherBox.getValue())+1);
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
	                book.setId(MainController.getBookGateway().createBook(title, isbn, yearPublished, summary, publisher_id));
	                book.setBookTitle(title);
	                book.setBookISBN(isbn);
	                book.setYearPublished(yearPublished);
	                book.setBookSummary(summary);
	                book.setPublisher_id(publisher_id);
	                // TODO: initiate the audit created and link to this book
	                MainController.getBookGateway().createAudit(book.getId(), "Book Added");
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
//			this.book.setBookTitle(fieldTitle.getText());
//			this.book.setYearPublished(Integer.parseInt(fieldYear.getText()));
//			this.book.setBookISBN(fieldISBN.getText());
//			this.book.setBookSummary(areaSummary.getText());
//			this.book.setPublisher_id(publishers.indexOf(publisherBox.getValue())+1);
//            
			if(!this.book.getBookTitle().equals(fieldTitle.getText())) {
			    MainController.getBookGateway().createAudit(book.getId(),"Title changed from " + book.getBookTitle() + " to " + fieldTitle.getText());
	            this.book.setBookTitle(fieldTitle.getText());
			}
			
			if(this.book.getYearPublished() != Integer.parseInt(fieldYear.getText())) {
				MainController.getBookGateway().createAudit(book.getId(),"Year Published changed from " + book.getYearPublished() + " to " + fieldYear.getText());
				this.book.setYearPublished(Integer.parseInt(fieldYear.getText()));
			}
			//TODO: this 3 properties
			if(!this.book.getBookISBN().equals(fieldISBN.getText())) {
				MainController.getBookGateway().createAudit(book.getId(),"ISBN changed from " + book.getBookISBN() + " to " + fieldISBN.getText());
	            this.book.setBookISBN(fieldISBN.getText());
			}
			
			if(!this.book.getBookSummary().equals(areaSummary.getText())) {
				MainController.getBookGateway().createAudit(book.getId(),"Summary changed from " + book.getBookSummary() + " to " + areaSummary.getText());
	        	this.book.setBookSummary(areaSummary.getText());
			}
			
			if(this.book.getPublisher_id() != publishers.indexOf(publisherBox.getValue())+1) {
				MainController.getBookGateway().createAudit(book.getId(),"Publisher changed from " + publishers.get(book.getPublisher_id()-1) + " to " + publisherBox.getValue());
	        	this.book.setPublisher_id(publishers.indexOf(publisherBox.getValue())+1);
			}
			
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
	
	public void onAudit() {
		logger.info("@BookAuditController Delete()");
		// DELETE the book and refresh the pages
		
//		MainController.getBookGateway().getAudit(book);
		MainController.showView(ViewType.BOOK_AUDIT, book);
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
	
	public void initialize() {
		logger.info("@BookDetailController initialize()");
		ObservableList<Publisher> items = publisherBox.getItems();
		items.addAll(publishers);
		
		if(book.getId()==0) {
			buttonDelete.setDisable(true);
			buttonUpdate.setDisable(true);
			buttonAudit.setDisable(true);
		} else {
			buttonCreate.setDisable(true);
		}
		
		fieldTitle.setText(this.book.getBookTitle());
		fieldYear.setText(Integer.toString(this.book.getYearPublished()));
		fieldISBN.setText(this.book.getBookISBN());
		areaSummary.setText(this.book.getBookSummary());
		publisherBox.setValue(items.get(book.getPublisher_id()-1));
//		System.out.println("@@@@@@@@lllll" +publishers.indexOf(publisherBox.getValue()));
		

	}

}
