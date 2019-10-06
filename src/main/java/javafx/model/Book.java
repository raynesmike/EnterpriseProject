package javafx.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Book {
	private int id;
	private String title;
	private String summary;
	private String isbn;		//Unique ID - new 13 digits, old 10 digits
	private int yearpublished;
	private BookGateway gateway;

//	private String bookAuthor;

	
	public Book() {
		this.id = 0;
		this.title = "";
		this.summary = ""; 
		this.isbn = "";
		this.yearpublished = 0;
		this.gateway= null;

//		this.bookAuthor = "";
	}
	
	public Book(String bookTitle, String bookAuthor, String bookGenre, String bookISBN, int bookPublished, String bookSummary) {
		
		this.title = bookTitle;
		this.isbn = bookISBN;
		this.yearpublished = bookPublished;
		this.summary = bookSummary; 
		this.gateway = null;
		

//		this.bookAuthor = bookAuthor;
	}

	public Book(int id, String bookTitle, String bookSummary, int bookPublished, String bookISBN){
		this.id = id;
		this.isbn = bookISBN;
		this.title = bookTitle;
		this.yearpublished = bookPublished;
		this.summary = bookSummary;
	}

	public void setId(int id) { this.id = id;}

	public int getId() { return this.id; }
	
	public String getBookTitle() {
		return title;
	}

	public void setBookTitle(String bookTitle) {
		this.title = bookTitle;
	}

//	public String getBookAuthor() {
//		return bookAuthor;
//	}
//
//	public void setBookAuthor(String bookAuthor) {
//		this.bookAuthor = bookAuthor;
//	}


	public String getBookISBN() {
		return isbn;
	}

	public void setBookISBN(String bookISBN) {
		this.isbn = bookISBN;
	}

	public int getBookPublished() {
		return yearpublished;
	}

	public void setBookPublished(int bookPublished) {
		this.yearpublished = bookPublished;
	}

	public String getBookSummary() {
		return summary;
	}

	public void setBookSummary(String bookSummary) {
		this.summary = bookSummary;
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
