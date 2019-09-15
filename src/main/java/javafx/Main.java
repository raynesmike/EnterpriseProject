package javafx;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.model.BookStore;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Logger logger = LogManager.getLogger();
	public static BookStore firstBookStore;
	
	public static void main(String[] args) {
		logger.info("@Main Starts Here()");
		
		launch(args);
	}
	 
	@Override
	public void start(Stage stage) {
		logger.info("@Main start()");
		
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("main_view.fxml"));
		MainController controller = MainController.getInstance();
		loader.setController(controller);

		Parent rootNode = null; 
		try {
			rootNode = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e);
		}
		stage.setScene(new Scene(rootNode));
		stage.setTitle("Books");
		stage.show();
		
		firstBookStore = new BookStore();
		firstBookStore.loadBooks();

		logger.debug("HERE");
		controller.showView(ViewType.DETAIL1, null);
	}

	@Override
	public void init() throws Exception {
		super.init();
		
		logger.error(": @Main init");
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		
		logger.info("@Main stop");

		// strange, but app hangs when executing from command line
		// Maven may be waiting on an explicit error signal
		
		// note: 0 indicates normal shutdown as per oracle API docs
		System.exit(0);
	}
}
