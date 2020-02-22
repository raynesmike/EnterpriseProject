package javafx.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.BookGateway;
import javafx.controller.MainController;

public class Book {
	private int id;
	private String title;
	private String summary;
	private String isbn;		//Unique ID - new 13 digits, old 10 digits
	private int yearPublished;
	private int publisher_id;
	private List<AuditTrailEntry> audit;
	private BookGateway gateway;
	
	public Book() {
		this.id = 0;
		this.title = "";
		this.summary = ""; 
		this.isbn = "";
		this.yearPublished = 0;
		this.publisher_id = 1;
		this.audit = new ArrayList<AuditTrailEntry>();
		this.gateway= null;

	}
	
	public Book(int id, String bookTitle, String bookSummary, int yearPublished, String bookISBN, int publisher_id){
		this.id = id;
		this.isbn = bookISBN;
		this.title = bookTitle;
		this.yearPublished = yearPublished;
		this.summary = bookSummary;
		this.publisher_id = publisher_id;
		
	}

	public void setId(int id) { this.id = id;}

	public int getId() { return this.id; }
	
	public String getBookTitle() {
		return title;
	}

	//    	a. title must be between 1 and 255 chars
	public Boolean setBookTitle(String bookTitle) {
		if(bookTitle.length() < 1 || bookTitle.length() > 255) {
			return false;
		}
		this.title = bookTitle;
		return true;
	}

	public String getBookISBN() {
		return isbn;
	}
	
	//	d. isbn cannot be > 13 characters. can be blank Implement these business rules as validation methods
	public Boolean setBookISBN(String bookISBN) {
		if(bookISBN.length() > 13) {
			return false;
		}
		this.isbn = bookISBN;
		return true;
	}

	public int getYearPublished() {
		return yearPublished;
	}
	
	//	c. year_published must be between 1455 and the current year (inclusive)
	public Boolean setYearPublished(int bookPublished) {	
		if(bookPublished < 1455  || bookPublished > 2019) {
			return false;
		}
		this.yearPublished = bookPublished;
		return true;
	}

	public String getBookSummary() {
		return summary;
	}

	//	b. summary must be < 65536 characters. can be blank
	public Boolean setBookSummary(String bookSummary) {
		if(bookSummary.length() > 65536) {
			return false;
		}
		this.summary = bookSummary;
		return true;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public BookGateway getGateway() {
		return gateway;
	}

	public void setGateway(BookGateway gateway) {
		this.gateway = gateway;
	}
	
	public List<AuditTrailEntry> getAudits() {
		return MainController.getBookGateway().getAudits(id);
	}

	public void setAudit(List<AuditTrailEntry> audit) {
		this.audit = audit;
	}
	
	public String toString() {
		return  title;
//				"Title: " + title + 
//				"-Summary: " + summary +
//				"-ISBN: " + isbn + 
//				"-publisher_id: " + Integer.toString(publisher_id);
	}


}
