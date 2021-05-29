package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimpleErrorMessage {
	public static void display(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMaxHeight(500);
		
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(30,30,5,30));
		
		ScrollPane sp = new ScrollPane();
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setMaxSize(800, 400);
		
		Label msgText = new Label(message);
		sp.setContent(msgText);
		Button closeButton = new Button("OK");
		closeButton.setOnAction(e -> window.close());
		
		layout.getChildren().addAll(sp, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
