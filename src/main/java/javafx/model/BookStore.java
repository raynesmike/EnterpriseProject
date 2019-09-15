package javafx.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
			bookList.add(new Book(bookData[0], bookData[1], bookData[2], Integer.parseInt(bookData[3]) , Integer.parseInt(bookData[4]) ));
		}
		
	}
	
//	public void searchBook(String title, String author, String genre, int isbn) {
//		
//	}
	public boolean searchBook(String title, String author, String genre, int isbn){
		System.out.println("inside searchBook");
		ArrayList<Book> newArrayList = new ArrayList<Book>();
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookTitle().equals(title)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookAuthor().equals(author)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookGenre().equals(genre)).collect(Collectors.toList());
	    newArrayList = (ArrayList<Book>) bookList.stream().filter(o -> o.getBookISBN() == (isbn)).collect(Collectors.toList());
	    
	    //	    boolean foundAuthor = bookList.stream().filter(o -> o.getBookAuthor().equals(author)).findFirst().isPresent();
//	    boolean foundGenre= bookList.stream().filter(o -> o.getBookGenre().equals(genre)).findFirst().isPresent();
//	    boolean foundISBN = bookList.stream().filter(o -> o.getBookISBN() == (isbn)).findFirst().isPresent();
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
