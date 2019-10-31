package javafx.model;

import java.util.Date;

public class AuditTrailEntry {
	private int id;
	private int book_id;
	private String message;
	private Date dateAdded;
	
	
	public AuditTrailEntry() {
		this.id = 0;
		this.book_id = 0;
		this.message = "";
		this.dateAdded = null;
	}
	
	public AuditTrailEntry(int id, int book_id, Date dateAdded, String message) {
		this.id = id;
		this.book_id = book_id;
		this.dateAdded = dateAdded;
		this.message = message;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message + dateAdded;
		
	}
	
}
