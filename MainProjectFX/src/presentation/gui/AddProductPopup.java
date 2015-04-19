package presentation.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import presentation.control.ManageProductsUIControl;
import presentation.data.DefaultData;
import presentation.data.ProductPres;
import business.externalinterfaces.*;
import business.productsubsystem.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class AddProductPopup extends Popup {
	MaintainProductsWindow maintainProductsWindow;
	TextField catalogName = new TextField();
	TextField id = new TextField();
	TextField name = new TextField();
	TextField manufactureDate = new TextField();
	TextField numAvail = new TextField();
	TextField unitPrice = new TextField();
	TextField description = new TextField();
	
	
	
	HBox sceneTitle;
	HBox topLevel;
	Text messageBar = new Text();
	private HBox setUpTopLabel() {
		Label label = new Label("Add Product");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private void setBorder() {
		final String cssDefault = "-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";
                //+ "-fx-border-style: dashed;\n";
        topLevel.setStyle(cssDefault);
	}
	public AddProductPopup(MaintainProductsWindow maintainProductsWindow) {
		setX(50);
        setY(50);
        topLevel = new HBox(10);
        topLevel.setOpacity(1);
        setBackground(Color.KHAKI);
        setBorder();
        catalogName.setEditable(false);
        //manageProductsUIControl = new ManageProductsUIControl(this);
        
		this.maintainProductsWindow = maintainProductsWindow;
		messageBar.setFill(Color.FIREBRICK);
		HBox sceneTitle = setUpTopLabel();
		Label catalogNameLabel = new Label("Catalog");
		Label idLabel = new Label("Product Id:");
		Label nameLabel = new Label("Product Name:");
		Label mgfDateLabel = new Label("Manufacture Date (mm/dd/yyyy):");
		Label numAvailLabel = new Label("Number Items In Stock:");
		Label unitPriceLabel = new Label("Unit Price:");
		Label descriptionLabel = new Label("Description:");
		
		HBox btnbox = setUpButtons();
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		grid.add(sceneTitle, 0, 0, 2, 1);
		grid.add(catalogNameLabel, 0,1);
		grid.add(catalogName, 1,1);
		grid.add(idLabel, 0, 2);
		grid.add(id, 1, 2);
		grid.add(nameLabel, 0, 3);
		grid.add(name, 1, 3);
		grid.add(mgfDateLabel, 0, 4);
		grid.add(manufactureDate, 1, 4);
		grid.add(numAvailLabel, 0, 5);
		grid.add(numAvail, 1, 5);
		grid.add(unitPriceLabel, 0, 6);
		grid.add(unitPrice, 1, 6);
		grid.add(descriptionLabel, 0, 7);
		grid.add(description, 1, 7);
		
		grid.add(messageBar, 0, 9, 2, 1);
		grid.add(btnbox, 0, 10, 2, 1);
		topLevel.getChildren().add(grid);
		getContent().addAll(topLevel);	
	}
	private HBox setUpButtons() {
		Button addButton = new Button("Add Product");
		Button cancelButton = new Button("Cancel");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(cancelButton);
		
	
		addButton.setOnAction(evt -> {
			ManageProductsUIControl.INSTANCE.saveProduct();		   
		});
		
		cancelButton.setOnAction(evt -> {
			messageBar.setText("");
			hide();
		});
		
		return btnBox;
	}
	public void setCatalog(String name) {
		catalogName.setText(name);
	}
	void setBackground(Color color) {
		topLevel.backgroundProperty().set(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	//getters and setter for field

	public TextField getId() {
		return id;
	}
	public void setId(TextField id) {
		this.id = id;
	}
	public TextField getName() {
		return name;
	}
	public void setName(TextField name) {
		this.name = name;
	}
	public TextField getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(TextField manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public TextField getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(TextField unitPrice) {
		this.unitPrice = unitPrice;
	}
	public TextField getNumAvail() {
		return numAvail;
	}
	public void setNumAvail(TextField numAvail) {
		this.numAvail = numAvail;
	}
	public TextField getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(TextField catalogName) {
		this.catalogName = catalogName;
	}
	public TextField getDescription() {
		return description;
	}
	public void setDescription(TextField description) {
		this.description = description;
	}
	public Text getMessageBar() {
		return messageBar;
	}
	public void setMessageBar(Text messageBar) {
		this.messageBar = messageBar;
	}
	
	
	
	
	
		
	
}