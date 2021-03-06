package javafx.Gateway;

import java.util.List;

import javafx.model.AuditTrailEntry;
import javafx.model.Book;

public interface BookGateway {
	
	public List<Book> getBooks();
	//public List<Make> getMakes();
	public int createBook(Book book);
	public Book readBook(String title);
	public boolean lockBeforeUpdate(Book book) throws GatewayException;
	public void rollbackPendingTransaction() throws GatewayException;
	public void updateBook(Book Book) throws GatewayException;
	public void deleteBook(Book book);
	public void close();
	
	public void insertBook(Book Book);
	public List<AuditTrailEntry> getAudits(int book_id);
	public int createAudit(int book_id, String entry_msg);
}
