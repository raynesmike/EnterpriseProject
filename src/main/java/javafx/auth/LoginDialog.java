package javafx.auth;


import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.model.Login;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;

public class LoginDialog {
	private static Logger logger = LogManager.getLogger();
	private static Login login;
	
	public static Pair<String, String> showLoginDialog(){
		login = new Login();
		///create dialog
		Dialog<Pair<String, String>> dialog = new Dialog<>();
	    dialog.setTitle("Login Dialog");
	    // Set the button types.
	    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

	    GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    
	    Label message = new Label();
        message.setText("Please Enter Username and Password");

	    TextField username = new TextField();
	    username.setPromptText("Username");
	    
	    TextField password = new TextField();
	    password.setPromptText("Password");

        gridPane.add(message, 0, 0);
        gridPane.add(new Label("Username: "), 0, 1);
	    gridPane.add(username, 1, 1);
	    gridPane.add(new Label("Password: "), 0, 2);
	    gridPane.add(password, 1, 2);
	    
	    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		
		// Validation using lambda( Java8)
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});
		
	    dialog.getDialogPane().setContent(gridPane);

	    // Request focus on the username field by default.
	    Platform.runLater(() -> username.requestFocus());

	    // Convert the result to a username-password-pair when the login button is clicked.
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == loginButtonType) {
	            return new Pair<>(username.getText(), password.getText());
	        }
	        return null;
	    });
		
		Optional<Pair<String, String>> result;
		while(true) {
			result = dialog.showAndWait();
	           try {	   
				if(login.login(result.get().getKey(), result.get().getValue()) == false) {
					errorLogin();

				}else {
					System.out.println(Login.token);
					break;
				}
			} catch (NoSuchElementException e) {
				logger.error("Empty Login");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!result.isPresent()) {
			return null;
		}
	
		return result.get();
		
	}
	
	public static void errorLogin() {
		Alert alert = new Alert(AlertType.WARNING);
		ButtonType OK = new ButtonType("OK");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().setAll(OK);
		alert.setContentText("401 UNAUTHORIZED");
		Optional<ButtonType> show = alert.showAndWait();
	}
	
	
	
}
