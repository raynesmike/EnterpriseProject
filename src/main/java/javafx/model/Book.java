package javafx.model;

public class Book {
	private String bookTitle;
	private String bookAuthor;
	private String bookGenre;
	private String bookISBN;		//Unique ID - new 13 digits, old 10 digits
	private int bookPublished;
	
	public Book(String bookTitle, String bookAuthor, String bookGenre, String bookISBN, int bookPublished) {
		
		//TODO: Check if data is valid
		this.bookTitle = bookTitle;
		this.bookAuthor = bookAuthor;
		this.bookGenre = bookGenre;
		this.bookISBN = bookISBN;
		this.bookPublished = bookPublished;
	}
	

	
	public String toString() {
		return bookTitle + " by "+ bookAuthor +"\n" + bookGenre + "\n" +bookISBN + "-" + bookPublished;
	}
}
