package javafx.controller;

import javafx.Gateway.GatewayException;
import javafx.application.Platform;
import javafx.auth.LoginDialog;
import javafx.auth.LoginException;
import javafx.auth.Sha;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.model.Book;
import javafx.model.Login;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.util.Pair;


public class MyController implements Initializable{
	// THIS USED TO BE A SINGLETON. BUT I REMOVED THAT AND MADE IT NASTY STATIC
	private static Logger logger = LogManager.getLogger();
	

    @FXML
    private MenuItem bookCreate, bookRead, reportsBookDetail;
    @FXML
    private MenuItem quitYes;
    private ViewType currentView;
    private Book book;

    private MainController instance;
    private static Login login;
	
	public MyController() {

	}
	
	@FXML
	private void handleBook(ActionEvent action) throws Exception {
		Object source = action.getSource();
		AlertBox alert = new AlertBox();

		logger.info("@MyController handleBook()" + MainController.getCurrentView());
		this.currentView = MainController.getCurrentView();
		
		//if(((currentView==ViewType.BOOK_DETAIL && source!=bookCreate) || MainController.getCurrentView()== ViewType.BOOK_DETAIL)) {
		if (MainController.getCurrentView() == ViewType.BOOK_DETAIL){
			alert = AlertBox.display( true, "You are leaving Detail, Do you want to save your changes?");
		}
		if(alert.getReply().equals("yes")) {
			//I.. Do not know what this thing is doing.
				if(currentView==ViewType.BOOK_DETAIL) {
					MainController.getBdc().onCreate();
				}else {
					MainController.getBdc().onUpdate();
				}
		}if(alert.getReply().equals("no")) {
			//Rollback the pending transaction and then change the view (last part done automatically)
			try {
				MainController.getBookGateway().rollbackPendingTransaction();
			} catch (GatewayException e) {
				logger.error(e);
			}
		} else if(alert.getReply().equals("cancel")) {
			return;
		}
		if(source == quitYes) {
			Platform.exit();
			
		}
		if(source == bookCreate) {
			currentView=ViewType.BOOK_DETAIL;
			MainController.showView(ViewType.BOOK_DETAIL, new Book());
			return;
		}
		if(source == bookRead) {
			currentView=ViewType.BOOK_LIST;
			MainController.showView(ViewType.BOOK_LIST, null);
			return;
		}
		if(source == reportsBookDetail) {
			logger.info("reporting BOOK");
			Login.report(MainController.getSessionToken());
		}

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Pair<String, String> creds = LoginDialog.showLoginDialog();
		if(creds == null) {
			return;
		}
		
		String userName = creds.getKey();
		String pw = creds.getValue();
		
		logger.info("username: " + userName + "\nPassword: " + pw);
		
		String pwHash = Sha.sha256(pw);
		
		logger.info("sha25 Password: " + pwHash);
	}
	

}
