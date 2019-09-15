package javafx.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BookStore {
	
	private ArrayList<Book> bookList;
	private static Logger logger = LogManager.getLogger();
	
	
	public BookStore() {
		this.bookList = new ArrayList<Book>();
	}

	public void loadBooks(){
		logger.info("@BookStore loading Books()");
		
		Scanner scanner = null;
		try {
			InputStream resource = this.getClass().getResourceAsStream("Books.csv");

			scanner = new Scanner(resource);
		} catch (Exception e) {

			logger.error(e);
		}
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			logger.debug(line);
			String[] bookData = line.split(", ");
			bookList.add(new Book(bookData[0], bookData[1], bookData[2], Integer.parseInt(bookData[3]) , Integer.parseInt(bookData[4]), bookData[5] ));
		}
	}
	

	public boolean searchBook(String title, String author, String genre, int isbn){

		logger.info("@BookStore searchBook()");
		ArrayList<Book> newArrayList = new ArrayList<Book>();
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookTitle().equals(title)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookAuthor().equals(author)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookGenre().equals(genre)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookISBN() == (isbn)).collect(Collectors.toList());

	    if(!newArrayList.isEmpty()) {
	    	for(Book book: newArrayList) {
	    		System.out.println(book);
	    	}
	    	return true;
	    }else { 
	    	return false;
	    }
	}
	
	public String toString() {
		String newString = "";
		for(Book book: bookList) {
			newString += book.toString() + "\n------------------\n";
		}
		return newString;
	}

	public ArrayList<Book> getBookList() {
		return bookList;
	}

	public void setBookList(ArrayList<Book> bookList) {
		this.bookList = bookList;
	}
	
	
}
