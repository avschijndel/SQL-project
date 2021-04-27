package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlarmBox {
	public static void display(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(400);
		window.setMinHeight(250);
		window.setMaxHeight(500);
		
		VBox layout = new VBox(10);
		Label msgText = new Label(message);
		msgText.setMaxHeight(500);
		Button closeButton = new Button("OK");
		closeButton.setOnAction(e -> window.close());
		
		layout.getChildren().addAll(msgText, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
