package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class BookSearchController implements Initializable, MyController {
    @FXML
    private TextField labelAuthor;

    @FXML
    private TextField labelGenre;

    @FXML
    private TextField labelTitle;

    @FXML
    private TextField labelISBN; // UniqueID(UID)
    
    private static Logger logger = LogManager.getLogger();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO print all the book title here

	}
	

    @FXML
    void onSearch(ActionEvent event) {
    	//TODO: check database if ISBN exist first and match the rest in BookStore
    	String a = labelTitle.getText();
    	String b = labelAuthor.getText();
    	String c = labelGenre.getText();
    	String d = labelISBN.getText();
    	if(d=="") {
    		d = "0";
    	}
    	try {
    		System.out.println(Main.firstBookStore.searchBook(a, b, c, Integer.parseInt(d)) );
    	}catch(Exception e){
    		logger.error("Search Error");
    	}
    }
   

}
