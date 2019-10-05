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
	public void updateBook(Book Book) throws GatewayException;
	public void close();
	
	public void insertBook(Book Book);
	//public void deleteBook(Book Book);
}
