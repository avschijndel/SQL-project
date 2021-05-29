package application;
	
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
//import javafx.util.Duration;


public class Main extends Application implements EventHandler<ActionEvent> {
	static Stage window;
	static Button button = new Button("Click");
	static Button TableButton = new Button("Table");
	static Button changeDatabase = new Button("Change database");
	static HBox buttonsBox;
	static VBox layout;
	static StringProperty databaseName = new SimpleStringProperty();
	static StringProperty serverName = new SimpleStringProperty();
	static Label lblConnection = new Label("Server");
	static String savedDatabaseName;
	static String savedServerName;
	static TextField queryInputField = new TextField();
	static ObservableList<ObservableList> data;
	static TableView table;// = new TableView();
	
	//JDBC
	static String loginName, loginPassword;
	static Boolean loginSuccess = false;
	static Boolean databasePicked = false;	
	
	final static Clipboard clipboard = Clipboard.getSystemClipboard();
	final static ClipboardContent content = new ClipboardContent();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			window.setTitle("");

			setupScreen();
			
			Scene scene = new Scene(layout,800,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());			
			window.setScene(scene);
				
			while(!LoginScreen.getLoginSuccess() || !DatabasePicker.getConnected()) {
				if(LoginScreen.display()) {
					DatabasePicker.launch();
				}
				
				if(LoginScreen.getCloseRequest()) break;
			}
			
			if(LoginScreen.getLoginSuccess() && DatabasePicker.getConnected()) {
				window.show();
				queryInputField.requestFocus();
			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
		databaseName.bind(Server.getDatabaseName());
		serverName.bind(Server.getServerName());
		databaseName.addListener((v, oldValue, newValue) -> {
			lblConnection.setText("Server: " + Server.getServerName().getValue() + " and database " + Server.getDatabaseName().getValue());;
		});
		serverName.addListener((v, oldValue, newValue) -> {
			lblConnection.setText("Server: " + Server.getServerName().getValue() + " and database " + Server.getDatabaseName().getValue());;
		});
		launch(args);
	}

	
	private static void go(String dbName) {
//		Connection con;
		Statement query;
		ResultSet result;
		
		try {
//			String url = "jdbc:sqlserver://" + Database.getServerName().getValue() + ":1433;"
//                    + "database=" + Database.getDatabaseName().getValue()
//                    + ";integratedSecurity=true;";
//			//System.out.println("Connecting to \"jdbc:sqlserver://" + DatabasePicker.getServerName().getValue()+ ";databaseName=" + DatabasePicker.getDatabaseName().getValue() + ";integratedSecurity=true;\"");
//			con = DriverManager.getConnection(url);
			
			query = Server.getCon().createStatement();
			result = query.executeQuery("select * from ZINDEX_004");
			
			ArticleTable.resetList();
			while(result.next()) {
				ArticleTable.addArticle(new Article(result.getString("code"), result.getString("oms").toUpperCase(), result.getString("type").toUpperCase()));
			}
			
			
			ArticleTable.display("Code tabel");
//			con.close();
		} catch (Exception ex){
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			SimpleErrorMessage.display("SQL error", exceptionAsString);
		}
	}
	
	private void setupScreen(){

		//Knoppen container
		buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(button, TableButton, changeDatabase, lblConnection);
		buttonsBox.setAlignment(Pos.CENTER_LEFT);
		//Functies van knoppen
		button.setOnAction(e -> SimpleErrorMessage.display("Foutmelding", "Klik op OK om door te gaan"));
		TableButton.setOnAction(e -> go(Server.getDatabaseName().getValue()));
		changeDatabase.setOnAction(e -> {
			savedDatabaseName = databaseName.getValue();
			savedServerName = serverName.getValue();
			window.hide();
			DatabasePicker.launch();
			window.show();
		});
		queryInputField.setPromptText("Enter SQL query");
		queryInputField.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				QueryResultView.display(queryInputField.getText());
			}
		});
		

				
		//Root layout
		layout = new VBox();
		layout.getChildren().addAll(buttonsBox, queryInputField);
		
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static private TableView<String> getResultView(String query) {
		table = new TableView();
		Connection con;
		String url = "jdbc:sqlserver://" + Server.getServerName().getValue() + ":1433;"
                + "database=" + Server.getDatabaseName().getValue()
                + ";integratedSecurity=true;";

		data = FXCollections.observableArrayList();
		
		try {
			con = DriverManager.getConnection(url);
			Statement statement = con.createStatement();
			 
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 0; i < rsmd.getColumnCount(); i++) {
				int j = i;
				TableColumn tempColumn = new TableColumn(rsmd.getColumnName(i+1));
				tempColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });
				
				table.getColumns().add(tempColumn);
			}
			 /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.addAll(row);
            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println(newValue);
			System.out.println(table.getSelectionModel().selectedIndexProperty());
//	        content.putString(table.getSelectionModel().getSelectedItem().getCode());
//	        clipboard.setContent(content);
//	        console.setText("Copied " + table.getSelectionModel().getSelectedItem().getCode() + " to the clipboard.");
		});
		
		return table;
	}
	
	public static Stage getWindow() {
		return window;
	}
	
	public static String getSavedDatabaseName() {
		return savedDatabaseName;
	}

	public static void setSavedDatabaseName(String savedDatabaseName) {
		Main.savedDatabaseName = savedDatabaseName;
	}

	public static String getSavedServerName() {
		return savedServerName;
	}

	public static void setSavedServerName(String savedServerName) {
		Main.savedServerName = savedServerName;
	}
	
}
 