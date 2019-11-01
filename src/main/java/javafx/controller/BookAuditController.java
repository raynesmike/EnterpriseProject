package javafx.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Gateway.GatewayException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.AuditTrailEntry;
import javafx.model.Book;
import javafx.scene.control.Button;
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
    @FXML
    private Button buttonBack;


    
    private Book book;
    private AuditTrailEntry audit;
    private List<AuditTrailEntry> audits;
    
    public BookAuditController(AuditTrailEntry audit, Book book) {
		this.book = book;
		this.audit = audit;
		this.audits = new ArrayList<AuditTrailEntry>();
	}
    
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		AlertBox alert = new AlertBox();

		if(source == buttonBack) {
			onBack();
		} 
	}
    
    
	private void onBack() {
		MainController.showView(ViewType.BOOK_DETAIL, book);
	}

	public void initialize() {
		logger.info("@BookAuditController initialize()");
		auditTrailName.setText("Audit Trail for " + book.getBookTitle());
		audits = book.getAudits();
		System.out.println("@@@@@@@@@@@@@@@@2");
		for(AuditTrailEntry a: audits) {
			System.out.println(a.toString());
		}
		ObservableList<AuditTrailEntry> items = tableViewAudits.getItems();
		items.addAll(audits);
		
		timestamp.setCellValueFactory(new PropertyValueFactory<AuditTrailEntry, Date>("dateAdded"));
		message.setCellValueFactory(new PropertyValueFactory<AuditTrailEntry, String>("message"));
			
	}
    
    
}
