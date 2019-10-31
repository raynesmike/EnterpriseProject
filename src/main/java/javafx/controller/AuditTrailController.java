package javafx.controller;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.model.Publisher;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class AuditTrailController {
	

	//	Audit Trail for <book title>” 
    @FXML
    private TextField auditTrailName;

    @FXML
    private TableColumn<AuditTrailEntry, String> message;

    @FXML
    private TableColumn<AuditTrailEntry, Date> timestamp;
    private Book book;
    
    public AuditTrailController(AuditTrailEntry audit) {
		// TODO Auto-generated constructor stub
	}
    
    
    
}
