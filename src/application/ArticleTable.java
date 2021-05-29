package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ArticleTable {
	static TableView<Article> table;
	static ObservableList<Article> articles = FXCollections.observableArrayList();
	final static Clipboard clipboard = Clipboard.getSystemClipboard();
	final static ClipboardContent content = new ClipboardContent();
	static Label console;
	static HBox top;
	static CheckBox copyChoice;
	
	public static void display(String title) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		VBox root = new VBox();
		top = new HBox();
		top.setPadding(new Insets(5,5,5,5));
		top.setSpacing(10);
		
		console = new Label();
		copyChoice = new CheckBox("Copy on click?");
		copyChoice.setSelected(true);
		top.getChildren().addAll(copyChoice,console);
		
		TableColumn<Article, String> codeColumn = new TableColumn<>("Artikelcode");
		codeColumn.setMinWidth(200);
		codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
		
		TableColumn<Article, String> nameColumn = new TableColumn<>("Omschrijving");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Article, String> typeColumn = new TableColumn<>("Type");
		typeColumn.setMinWidth(200);
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		table = new TableView<>();
		table.setItems(getArticle());
		table.getColumns().addAll(codeColumn, nameColumn, typeColumn);
		
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if(copyChoice.isSelected()) {
				System.out.println(table.getSelectionModel().getSelectedItem().getCode());
		        content.putString(table.getSelectionModel().getSelectedItem().getCode());
		        clipboard.setContent(content);
		        console.setText("Copied " + table.getSelectionModel().getSelectedItem().getCode() + " to the clipboard.");
			}
		});
		
		copyChoice.setOnAction(e -> {
			if(copyChoice.isSelected()) console.setText("Selection will copy"); else console.setText("Selections will not be copied. Copy with CTRL-C");
		});
		
		
		
		
		root.getChildren().addAll(top, table);
		Scene scene = new Scene(root);
		
		// Proberen kopieren
		scene.getAccelerators()
        .put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {
                int row = table.getSelectionModel().getSelectedIndex();
                Article tmp = table.getItems().get(row);
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                if(table.getSelectionModel().isSelected(row, codeColumn)){
                    System.out.println(tmp.getCode());
                    content.putString(tmp.getCode().toString());
                }
                else{
                    System.out.println(tmp.getCode());
                    content.putString(tmp.getCode());
                }
                clipboard.setContent(content);
            }
        });
		
		window.setScene(scene);
		window.showAndWait();
	}
	
	public static void resetList() {
		articles.clear();
	}
	
	public static ObservableList<Article> getArticle(){
		return articles;
	}
	
	public static void addArticle(Article article) {
		articles.add(article);
	}
	
	public static void removeArticle(Article article) {
		//
	}
}
