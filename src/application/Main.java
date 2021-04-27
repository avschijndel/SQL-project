package application;
	
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application implements EventHandler<ActionEvent> {
	Stage window;
	Button button = new Button("Click");
	Button TableButton = new Button("Table");
	HBox versionPicker;
	HBox buttonsBox;
	VBox layout;
	
	//JDBC
	static String databaseName, serverName;
	static RadioButton versie1, versie2, versie3;
	static ToggleGroup versieToggle = new ToggleGroup();
	static Label lblVersie, lblServer;
	static StringProperty versieLabel = new SimpleStringProperty();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			window.setTitle("Database connectorz");
			
			setupScreen();
			
			Scene scene = new Scene(layout,800,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Click2");
	}
	
	private static void go(String dbName) {
		Connection con;
		Statement query;
		ResultSet result;
		
		try {
			String url = "jdbc:sqlserver:" + serverName  + ";databaseName=" + dbName + ";integratedSecurity=true;";
			con = DriverManager.getConnection(url);
			//AlarmBox.display("SQL Studio", "You have connected to " + dbName);
			
			query = con.createStatement();
			result = query.executeQuery("select * from ZINDEX_004");
			
			ArticleTable.resetList();
			while(result.next()) {
				ArticleTable.addArticle(new Article(result.getString("code"), result.getString("oms").toUpperCase(), result.getString("type").toUpperCase()));
			}
			
			
			ArticleTable.display("Code tabel");
		} catch (Exception ex){
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			AlarmBox.display("SQL error", exceptionAsString);
		}
	}
	
	private void setupScreen(){
		//RadioButtons voor de versie
		versionPicker = new HBox();
		versionPicker.setSpacing(10);
		versionPicker.setPadding(new Insets(5,5,5,5));
				
		versie1 = new RadioButton();
		versie1.setText("HiX 6.1");
		versie1.setToggleGroup(versieToggle);
		versie1.setUserData("VERSIE61");
		versie1.setSelected(true); 					// Eerste selectie is 6.1
				
		versie2 = new RadioButton();
		versie2.setText("HiX 6.2");
		versie2.setToggleGroup(versieToggle);
		versie2.setUserData("VERSIE62");
		
		versie3 = new RadioButton();
		versie3.setText("HiX 6.3");
		versie3.setToggleGroup(versieToggle);
		versie3.setUserData("VERSIE63");
		
		//Database name
		databaseName = versieToggle.getSelectedToggle().getUserData().toString();
		serverName = "//localhost\\SQLEXPRESS";
		
		// Versie label met de gekozen database + listener
		lblServer = new Label("Server: //localhost\\SQLEXPRESS");
		lblVersie = new Label("Database: " + versieToggle.getSelectedToggle().getUserData());
		versieToggle.selectedToggleProperty().addListener((v, oldValue, newValue) -> {
			databaseName = (String) newValue.getUserData();
			lblVersie.setText("Database: " + newValue.getUserData());
		});
		
		//Bovenste region
		versionPicker.getChildren().addAll(versie1, versie2, versie3, lblServer, lblVersie);
		
		//Knoppen container
		buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(button, TableButton);
		//Functies van knoppen
		button.setOnAction(e -> AlarmBox.display("Foutmelding", "Klik op OK om door te gaan"));
		TableButton.setOnAction(e -> go(databaseName));
				
		//Root layout
		layout = new VBox();
		layout.getChildren().addAll(versionPicker, buttonsBox);
		
		
	}
	
}
 