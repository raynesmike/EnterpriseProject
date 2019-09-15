package javafx.model;

public class Book {
	private String bookTitle;
	private String bookAuthor;
	private String bookGenre;
	private int bookISBN;		//Unique ID - new 13 digits, old 10 digits
	private int bookPublished;
	private String bookSummary;
	
	public Book(String bookTitle, String bookAuthor, String bookGenre, int bookISBN, int bookPublished, String bookSummary) {
		
		//TODO: Check if data is valid
		this.bookTitle = bookTitle;
		this.bookAuthor = bookAuthor;
		this.bookGenre = bookGenre;
		this.bookISBN = bookISBN;
		this.bookPublished = bookPublished;
		this.bookSummary = bookSummary; 
	}
	

	
	public String getBookTitle() {
		return bookTitle;
	}



	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}



	public String getBookAuthor() {
		return bookAuthor;
	}



	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}



	public String getBookGenre() {
		return bookGenre;
	}



	public void setBookGenre(String bookGenre) {
		this.bookGenre = bookGenre;
	}



	public int getBookISBN() {
		return bookISBN;
	}



	public void setBookISBN(int bookISBN) {
		this.bookISBN = bookISBN;
	}



	public int getBookPublished() {
		return bookPublished;
	}



	public void setBookPublished(int bookPublished) {
		this.bookPublished = bookPublished;
	}



	public String getBookSummary() {
		return bookSummary;
	}



	public void setBookSummary(String bookSummary) {
		this.bookSummary = bookSummary;
	}



	public String toString() {
		return bookTitle;
	}
}
