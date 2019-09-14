package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class Detail1Controller implements Initializable, MyController {
    @FXML
    private TextField labelAuthor;

    @FXML
    private TextField labelGenre;

    @FXML
    private TextField labelTitle;

    @FXML
    private TextField labelISBN; // UniqueID(UID)
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	

    @FXML
    void onSearch(ActionEvent event) {
    	//TODO: check database if ISBN exist first and match the rest in BookStore
    	String a =labelTitle.getText();
    	String b = labelAuthor.getText();
    	String c = labelGenre.getText();
    	String d =labelISBN.getText();
    	
    	Main.firstBookStore.searchBook(a, b,c , d);
    	
    }

}
