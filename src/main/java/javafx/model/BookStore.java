package javafx.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookStore {
	
	private ArrayList<Book> bookList;
	
	public BookStore() {
		this.bookList = new ArrayList<Book>();
	}
	
	public void loadBooks(){
		Scanner scanner = null;
		try {
			scanner = new Scanner (new File("Books.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			String[] bookData= scanner.nextLine().split(", ");
			bookList.add(new Book(bookData[0], bookData[1], bookData[2], bookData[3], Integer.parseInt(bookData[4]) ));
		}
		
	}
	
	public void searchBook(String title, String author, String genre, String isbn) {
		
	}
	
	public String toString() {
		String newString = "";
		for(Book book: bookList) {
			newString += book.toString() + "\n------------------\n";
		}
		return newString;
	}
}
