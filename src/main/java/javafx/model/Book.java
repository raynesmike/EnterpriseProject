package javafx.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Book {
	private int id;
	private String title;
	private String summary;
	private String isbn;		//Unique ID - new 13 digits, old 10 digits
	private int yearPublished;
	private BookGateway gateway;
	
	public Book() {
		this.id = 0;
		this.title = "";
		this.summary = ""; 
		this.isbn = "";
		this.yearPublished = 0;
		this.gateway= null;

	}
	
	public Book(int id, String bookTitle, String bookSummary, int yearPublished, String bookISBN){
		this.id = id;
		this.isbn = bookISBN;
		this.title = bookTitle;
		this.yearPublished = yearPublished;
		this.summary = bookSummary;
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

	public String toString() {
		return title;
	}

	public BookGateway getGateway() {
		return gateway;
	}

	public void setGateway(BookGateway gateway) {
		this.gateway = gateway;
	}
}
