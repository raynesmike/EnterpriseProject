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
	
	public MyController() {
	}
	
	@FXML
	private void handleBook(ActionEvent action) throws Exception {
		Object source = action.getSource();
		AlertBox alert = new AlertBox();
		
		if((currentView==ViewType.BOOK_DETAIL && source!=bookCreate) || MainController.getCurrentView()== ViewType.BOOK_DETAIL) {
			alert = AlertBox.display( true , 
					"You are leaving Detail, Do you want to save your changes?");
		}
		if(alert.getReply().equals("yes")) {
			if(source == quitYes) {
				Platform.exit();
				
			}
			if(source == bookCreate) {
				//get a collection of books from the gateway
				currentView=ViewType.BOOK_DETAIL;
				MainController.showView(ViewType.BOOK_DETAIL, new Book());
				return;
			}
			if(source == bookRead) {
				//get a collection of books from the gateway
				currentView=ViewType.BOOK_LIST;
				MainController.showView(ViewType.BOOK_LIST, null);
				return;
			}
		}else if(alert.getReply().equals("no")) {
			//TODO: rollback
			try {
				MainController.getBookGateway().rollbackPendingTransaction();
			} catch (GatewayException e) {
				logger.error(e);
			}
		} else {
			//TODO: Do Nothing
		}
		

	}
}
