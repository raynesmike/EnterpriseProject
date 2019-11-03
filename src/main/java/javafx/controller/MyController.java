package javafx.controller;

import javafx.Gateway.GatewayException;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.model.Book;
import javafx.scene.control.MenuItem;


public class MyController {
	// THIS USED TO BE A SINGLETON. BUT I REMOVED THAT AND MADE IT NASTY STATIC
	private static Logger logger = LogManager.getLogger();
	

    @FXML
    private MenuItem bookCreate, bookRead;
    @FXML
    private MenuItem quitYes;
    private ViewType currentView;
    private Book book;

    private MainController instance;
	
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

	}
}
