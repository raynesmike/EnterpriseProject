package javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Detail2Controller implements Initializable, MyController {
	private static final Logger logger = LogManager.getLogger();
	
	@FXML
    void doButton(ActionEvent event) {
		// logger.error("switch to view 1");
		MainController.getInstance().showView(ViewType.DETAIL1);
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
