package javafx.model;

import java.util.Date;

public class AuditTrailEntry {
	private int id;
	private String message;
	private Date dateAdded;
	
	
	public AuditTrailEntry() {
		this.id = 0;
		this.message = "";
		this.dateAdded = null;
	}
	
	public AuditTrailEntry(int id, String message, Date dateAdded) {
		this.id = id;
		this.message = message;
		this.dateAdded = dateAdded;
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
	
}
