package application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class QueryResultView {
	private static String query;
	private static ObservableList<ObservableList> data;
	private static TableView table;
	
	public static void display(String queryInput) {
		Stage window = new Stage();
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Query results");
		window.setMaxHeight(500);
		window.setMaxWidth(800);
		
		VBox layout = new VBox();
		query = queryInput;
		if(getTableView()) {
			ScrollPane sp = new ScrollPane();
			sp.setContent(table);
			sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
			layout.getChildren().add(sp);
			Scene scene = new Scene(layout);
			window.setScene(scene);
			window.show();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Boolean getTableView() {
		Boolean properQuery = false;
		table = new TableView();
		data = FXCollections.observableArrayList();
		try {
			Statement statement = Server.getCon().createStatement();
			ResultSet rs = statement.executeQuery(query);
			System.out.println(Server.getServerName().getValue() + Server.getDatabaseName().getValue());
			System.out.println(query);
			for(int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn tempColumn = new TableColumn(rs.getMetaData().getColumnName(i+1));
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
                System.out.println("Row [1] added "+row );
                data.addAll(row);

            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
            properQuery = true;
		    
		} catch (SQLException e) {
			properQuery = false;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			SimpleErrorMessage.display("SQL Exception", exceptionAsString);
		}
		return properQuery;
	}
	
}
