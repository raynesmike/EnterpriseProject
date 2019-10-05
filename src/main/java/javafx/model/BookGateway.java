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
	public void createBook(String title, int isbn, int yearPublished, String summary);
	public void readBook();
	public void updateBook(Book Book) throws GatewayException;
	public void deleteBook();
	public void close();
	
	public void insertBook(Book Book);
	//public void deleteBook(Book Book);
}
