package javafx.model;

import java.util.List;

/**
 * interface for Book gateways
 * @author kael
 *
 */
public interface BookGateway {
	
	public List<Book> getBooks();
	//public List<Make> getMakes();
	public int createBook(String title, String isbn, int yearPublished, String summary);
	public Book readBook(String title);
	public void updateBook(Book Book) throws GatewayException;
	public void deleteBook(Book book);
	public void close();
	
	public void insertBook(Book Book);
	//public void deleteBook(Book Book);
}
