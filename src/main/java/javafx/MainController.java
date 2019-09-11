package javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {
	private static final Logger logger = LogManager.getLogger();

	private static MainController instance = null;
	
	private MainController() {
	}
	
	public static MainController getInstance() {
		if(instance == null)
			instance = new MainController();
		return instance;
	}
	
	@FXML
    private BorderPane rootPane;

	// eclipse used the wrong import: import java.awt.event.ActionEvent;
	// void onBeer(ActionEvent event) {
	@FXML
    void onBeer(ActionEvent event) {
		logger.info("Clicked on Beer");
		
		showView(ViewType.DETAIL2);
    }
	
	public void showView(ViewType viewType) {
		// load view according to viewType and plug into center of rootPane
		FXMLLoader loader = null;
		Parent viewNode;
		MyController controller = null;
		switch(viewType) {
			case DETAIL1 : 
				loader = new FXMLLoader(this.getClass().getResource("detail1.fxml"));
				controller = new Detail1Controller();
				break;
			case DETAIL2 : 
				loader = new FXMLLoader(this.getClass().getResource("detail2.fxml"));
				controller = new Detail2Controller();
				break;
		}
		viewNode = null;
		loader.setController(controller);
		try {
			viewNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rootPane.setCenter(viewNode);
	}
			
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
