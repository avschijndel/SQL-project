package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginScreen {
	static String username;
	static String password;
	static Boolean loginSuccess = false;
	static TextField tfUsername, tfPass;
	static Button loginButton, cancelButton;
	static Label loginLabel;
	
	public static Boolean display() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Login");
		window.setMinWidth(400);
		window.setMinHeight(400);
		
		//Root into Stage
		VBox root = new VBox(10);
		
		//Username label and field
		HBox usernameBox = new HBox(20);
		usernameBox.setAlignment(Pos.CENTER);
		Label lblUsername = new Label("Username:");
		tfUsername = new TextField();
		tfUsername.setText("");
		tfUsername.setPromptText("username");
		usernameBox.getChildren().addAll(lblUsername, tfUsername);
		tfUsername.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				tfPass.requestFocus();
			}
		});
		
		//Password label and field
		HBox passwordBox = new HBox(20);
		passwordBox.setAlignment(Pos.CENTER);
		Label lblPassword = new Label("Password:");
		tfPass = new TextField();
		tfPass.setText("");
		tfPass.setPromptText("password");
		passwordBox.getChildren().addAll(lblPassword, tfPass);
		tfPass.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				loginButton.fire();
			}
		});
		
		
		//Buttons
		HBox buttonBox = new HBox(10);
		loginButton = new Button("Login");
		loginButton.setAlignment(Pos.CENTER);
		loginButton.setOnAction(e -> {
			if(!tfUsername.getText().trim().isEmpty() && !tfPass.getText().trim().isEmpty()) {
				System.out.println("You tried logging in with username " + tfUsername.getText().trim() + " and password " + tfPass.getText().trim());
				username = tfUsername.getText().trim();
				password = tfPass.getText().trim();
				
				if(username.equals("Alain") && password.equals("Alain")) {
					System.out.println("Login successful");
					loginSuccess = true;
					window.close();
					
				} else {
					System.out.println("Login failed");
					loginLabel.setText("Wrong username password combination");
					tfPass.clear();
					tfPass.requestFocus();
				}
			} else {
				loginLabel.setText("Fill username and password");
				System.out.println("Fill username and password");
				tfUsername.requestFocus();
			}
		});
		
		cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			loginSuccess = false;
			window.close();
		});
		buttonBox.getChildren().addAll(loginButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		//Login label for warnings
		HBox loginLabelContainer = new HBox();
		loginLabel = new Label("");
		loginLabel.setStyle("-fx-text-color: red");
		loginLabelContainer.getChildren().add(loginLabel);
		loginLabelContainer.setAlignment(Pos.CENTER);
		
		//Root vullen
		Pane spacerTop = new Pane();
		spacerTop.setMinHeight(100);
		spacerTop.setStyle("-fx-background-color: grey;");
		root.getChildren().addAll(spacerTop, usernameBox, passwordBox, loginLabelContainer, buttonBox);
		
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
