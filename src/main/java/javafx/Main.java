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
		System.out.println("hello world!");
		
//		logger.error("hello error");
//		
//		logger.info("hello debug");

		launch(args);
	}
	 
	@Override
	public void start(Stage stage) {
		logger.info("in start method");
		
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("main_view.fxml"));
		MainController controller = MainController.getInstance();
		loader.setController(controller);

		Parent rootNode = null; 
		try {
			rootNode = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage.setScene(new Scene(rootNode));
		stage.setTitle("Books");
		
		stage.show();
		
		firstBookStore = new BookStore();
		firstBookStore.loadBooks();
		System.out.println(firstBookStore.toString());

		logger.debug("HERE");
		controller.showView(ViewType.DETAIL1, null);
	}

	@Override
	public void init() throws Exception {
		super.init();
		
		logger.info("calling init...");
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		
		logger.info("calling stop...");

		// strange, but app hangs when executing from command line
		// Maven may be waiting on an explicit error signal
		
		// note: 0 indicates normal shutdown as per oracle API docs
		System.exit(0);
	}
}
