package javafx.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.model.Publisher;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookAuditController {
	private static Logger logger = LogManager.getLogger();

    @FXML
    private TextField auditTrailName;
    @FXML
    private TableView<AuditTrailEntry> tableViewAudits;
    @FXML
    private TableColumn<AuditTrailEntry, Date> timestamp;
    @FXML
    private TableColumn<AuditTrailEntry, String> message;
    
    private Book book;
    private AuditTrailEntry audit;
    private List<AuditTrailEntry> audits;
    
    public BookAuditController(AuditTrailEntry audit, Book book) {
		this.book = book;
		this.audit = audit;
		this.audits = new ArrayList<AuditTrailEntry>();
	}
    
	public void initialize() {
		logger.info("@BookAuditController initialize()");
		auditTrailName.setText("Audit Trail for " + book.getBookTitle());
		audits = book.getAudits();
		
		ObservableList<AuditTrailEntry> items = tableViewAudits.getItems();
		items.addAll(audits);
		timestamp.setCellValueFactory(new PropertyValueFactory<AuditTrailEntry, Date>("timestamp"));
		message.setCellValueFactory(new PropertyValueFactory<AuditTrailEntry, String>("message"));
		
		
	
		
	}
    
    
}
