package javafx.model;

import java.util.Date;

public class Publisher {
	private int id;
	private String name;
	private Date date;
	
	public Publisher() {
		this.id = 0;
		this.name = "";
		this.date = null;
	}
	public Publisher(int id, String name, Date date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}
	
	public String toString() {
		return name;
	}
}
