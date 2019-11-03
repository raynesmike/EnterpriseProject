package javafx.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	private static String reply;
	public AlertBox() {
		reply="";
	}
	public static AlertBox display(Boolean button, String message) {
		Stage window = new Stage();
		
		
		window.initModality(Modality.WINDOW_MODAL);
		window.setTitle("Alert");
		window.setMinWidth(250);
		
		Label label = new Label();
		VBox layout = new VBox(10);
		GridPane pane = new GridPane();
		
		layout.getChildren().addAll(label, pane);
		
		label.setText(message);
		
		if(button) {
			Button yesButton = new Button("Yes");
			yesButton.setOnAction(e->{
				reply ="yes";
				window.close();
			});
			
			Button noButton = new Button("No");
			noButton.setOnAction(e->{
				reply = "no";
				window.close();
			});
	
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e-> {
				reply = "cancel";
				window.close();
			});
	
			pane.add(yesButton, 0, 0);
			pane.add(noButton, 1, 0);
			pane.add(cancelButton, 2, 0);
			
		} else {
			Button cancelButton = new Button("Ok");
			cancelButton.setOnAction(e-> {
				reply = "cancel";
				window.close();
			});
			pane.add(cancelButton, 1, 0);
		}
		pane.setAlignment(Pos.BASELINE_CENTER);
		
		layout.setAlignment(Pos.CENTER);
	
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		return null;
	}
	public static String getReply() {
		return reply;
	}
	public static void setReply(String reply) {
		AlertBox.reply = reply;
	}
}
