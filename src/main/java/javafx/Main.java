package javafx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.controller.MainController;
import javafx.controller.MyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Logger logger = LogManager.getLogger();
	//public static BookTableGatewayMySQL firstBookStore;
	
	@Override
	public void init() throws Exception {
		super.init();
		MainController.initBookGateway();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		
		logger.info("@Main stop");

		MainController.close();
		System.exit(0);
	}
	 
	@Override
	public void start(Stage stage) throws Exception {
		logger.info("@Main start()");
		//init the view
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/main_view.fxml"));
		
		//init the controller 
		MyController controller = new MyController();
		
		//assign to loader
		loader.setController(controller);

		Parent rootNode = loader.load(); 
	
		MainController.setRootPane((BorderPane) rootNode);
		
		stage.setScene(new Scene(rootNode));
		stage.setTitle("Books");
		stage.show();

		logger.debug("HERE");
	}

	public static void main(String[] args) {
		logger.info("@Main Starts Here()");
		
		launch(args);
	}


}
