package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginScreen {
	static String username;
	static String password;
	static Boolean loginSuccess;
	
	public static Boolean display() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Login");
		window.setMinWidth(300);
		//window.setMinHeight(300);
		
		//Root into Stage
		VBox root = new VBox(10);
		
		//Username label and field
		HBox usernameBox = new HBox(20);
		Label lblUsername = new Label("Username:");
		TextField tfUsername = new TextField();
		tfUsername.setText("");
		tfUsername.setPromptText("username");
		usernameBox.getChildren().addAll(lblUsername, tfUsername);
		
		//Password label and field
		HBox passwordBox = new HBox(20);
		Label lblPassword = new Label("Password:");
		TextField tfPass = new TextField();
		tfPass.setText("");
		tfPass.setPromptText("password");
		passwordBox.getChildren().addAll(lblPassword, tfPass);
		
		
		//Buttons
		HBox buttonBox = new HBox(10);
		Button loginButton = new Button("Login");
		loginButton.setAlignment(Pos.CENTER);
		loginButton.setOnAction(e -> {
			if(!tfUsername.getText().trim().equalsIgnoreCase("") && !tfPass.getText().trim().equalsIgnoreCase("")) {
				System.out.println("You tried logging in with username " + tfUsername.getText().trim() + " and password " + tfPass.getText().trim());
				loginSuccess = true;
				username = tfUsername.getText().trim();
				password = tfPass.getText().trim();
			} else {
				System.out.println("Fill username and password");
				loginSuccess = false;
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> window.close());
		buttonBox.getChildren().addAll(loginButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		//Root vullen
		root.getChildren().addAll(usernameBox, passwordBox, buttonBox);
		
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.showAndWait();
		
		return loginSuccess;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static String getPassword() {
		return password;
	}
}
