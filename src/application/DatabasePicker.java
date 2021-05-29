package application;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DatabasePicker {
	//Variabelen
	static Boolean connected = false;
	static Boolean closeRequest = false;
	static BooleanProperty serverBtnConnected = new SimpleBooleanProperty(false);
	
	//User interface
	static VBox root, versionToggle, dropdownContainer;
	static HBox serverBox, databaseBox, buttonBox, topContainer;
	static Label lblServer, lblDatabase;
	static ComboBox<String> serverComboBox, databaseComboBox;
	static ArrayList<String> serverList, databaseList;
	static Button connectToDatabaseButton, cancelButton;
	static ToggleGroup dbToggler = new ToggleGroup();
	static Connection con = null;
	static Button connectToServerButton = new Button(">");

	
	public static Boolean launch() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Database");
		window.setMinHeight(300);
		window.setMinWidth(400);
		window.setOnCloseRequest(e -> cancelButton.fire());
		
		populateServerList();
		serverBtnConnected.set(false);

		// Server kiezen
		serverBox = new HBox();
		lblServer = new Label("Server: ");
		serverComboBox = new ComboBox<String>();
		serverComboBox.getItems().addAll(serverList);
		serverComboBox.setEditable(true);
		serverComboBox.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				connectToServerButton.fire();
			}
		});
		
		serverBox.getChildren().addAll(lblServer, serverComboBox, connectToServerButton);
		
		
		// Database kiezen
		databaseBox = new HBox();
		lblDatabase = new Label("Database: ");
		lblDatabase.setVisible(false);
		databaseComboBox = new ComboBox<String>();
		databaseComboBox.setDisable(true);
		databaseComboBox.setVisible(false);
		databaseComboBox.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				connectToDatabaseButton.fire();
			}
		});
		databaseBox.getChildren().addAll(lblDatabase, databaseComboBox);

		
		// Knoppen
		buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		connectToDatabaseButton = new Button("Connect");
		connectToDatabaseButton.setDisable(true);
		connectToDatabaseButton.setOnAction(e -> {
			if(Server.tryConnectToDatabase(databaseComboBox.getSelectionModel().getSelectedItem().toString())) {
				window.close();
			}
			
		});
		connectToServerButton.setOnAction(e -> {
			if(Server.tryConnectToServer(serverComboBox.getEditor().getText())) {
				connected = true;
				databaseComboBox.getItems().clear();
				databaseComboBox.getItems().addAll(Server.getDatabaseList());
				databaseComboBox.getSelectionModel().selectFirst();
				databaseComboBox.setDisable(false);
				serverComboBox.setDisable(true);
				serverBtnConnected.set(!serverBtnConnected.getValue());
			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			System.out.println("Cancel database picking");
			connected = false;
			closeRequest = true;
			if(Main.getSavedDatabaseName() != null) {
				Server.setDatabaseName(Main.getSavedDatabaseName());
				Server.setServerName(Main.getSavedServerName());
			}
			window.close();
		});
		buttonBox.getChildren().addAll(connectToDatabaseButton, cancelButton);
		
		serverBtnConnected.addListener((v, oldValue, newValue) -> {
			if(newValue) {
				connectToServerButton.setText("Reset");
				serverComboBox.setDisable(true);
				databaseComboBox.setDisable(false);
				databaseComboBox.setVisible(true);
				lblDatabase.setVisible(true);
				connectToDatabaseButton.setDisable(false);
				databaseComboBox.requestFocus();
			} else {
				connectToServerButton.setText(">");
				serverComboBox.setDisable(false);
				databaseComboBox.setDisable(true);
				databaseComboBox.setVisible(false);
				lblDatabase.setVisible(false);
				connectToDatabaseButton.setDisable(true);
				serverComboBox.getSelectionModel().selectFirst();
			}
		});	
		
		
		
		// Optie menu
		topContainer = new HBox();
		topContainer.setPadding(new Insets(10,10,10,10));
		topContainer.setSpacing(20);
		dropdownContainer = new VBox();
		dropdownContainer.setSpacing(5);
		
		dropdownContainer.getChildren().addAll(serverBox, databaseBox);
		topContainer.getChildren().addAll(dropdownContainer);

		root = new VBox();
		root.getChildren().addAll(topContainer, buttonBox);
		
		initializeControls();
		
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.showAndWait();
		return connected;
	}

	//Populate server/db lists
	private static void populateServerList(){
		serverList = new ArrayList<String>();
		serverList.add("DESKTOP-G7KO39L");
		serverList.add("localhost");
	}


	private static void initializeControls() {
		serverComboBox.getSelectionModel().selectFirst();
		databaseComboBox.getSelectionModel().selectFirst();	
		serverComboBox.requestFocus();
	}
	
	//Getters and setters	
	public static Boolean getConnected() {
		return connected;
	}
	public static void setConnected(Boolean connected) {
		DatabasePicker.connected = connected;
	}
	public static Boolean getCloseRequest() {
		return closeRequest;
	}
	public static void setCloseRequest(Boolean closeRequest) {
		DatabasePicker.closeRequest = closeRequest;
	}
	}
