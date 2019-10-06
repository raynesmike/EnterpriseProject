package javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.model.BookGateway;
import javafx.model.BookTableGatewayMySQL;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookCreateController {
    @FXML
    private TextField labelYearPublished, labelTitle, labelISBN;

    @FXML
    private TextArea labelSummary;

    @FXML
    private Button buttonCreate;
	private static BookGateway bookGateway;

	private static final Logger logger = LogManager.getLogger();
	
	public BookCreateController() {
	}
	
	public static void initBookGateway() {
		//create gateways
		try {
			bookGateway = new BookTableGatewayMySQL();
			
		} catch (javafx.model.GatewayException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		
		Object source = action.getSource();
		if(source == buttonCreate) {
			onCreate();
		}
	}
	
	@FXML public void onCreate() {
    	logger.info("@BookCreateController onSearch()");
    	
    	try {	

    	
    	MainController.getBookGateway().createBook(labelTitle.getText(), Integer.parseInt(labelISBN.getText())
    			,Integer.parseInt(labelYearPublished.getText()),labelSummary.getText());
    	
    	}catch(Exception e){
    		System.out.println(labelTitle.getText()+Integer.parseInt(labelISBN.getText().trim())
			+Integer.parseInt(labelYearPublished.getText())+labelSummary.getText());
    		logger.error("ERROR: @BookCreateController handleButtonAction" + e.toString());
    	}
    }
   

}
