package presentation.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import business.exceptions.BackendException;
import business.externalinterfaces.Product;
import business.productsubsystem.ProductSubsystemFacade;
import presentation.data.CatalogPres;
import presentation.data.DefaultData;
import presentation.data.ManageProductsData;
import presentation.data.ProductPres;
import presentation.gui.AddProductPopup;
import presentation.gui.MaintainCatalogsWindow;
import presentation.gui.MaintainProductsWindow;
import presentation.gui.MessageableWindow;
import presentation.gui.TableUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public enum ManageProductsUIControl {
	INSTANCE;

	private Stage primaryStage;
	private Callback startScreenCallback;

	public void setPrimaryStage(Stage ps, Callback returnMessage) {
		primaryStage = ps;
		startScreenCallback = returnMessage;
	}

	// windows managed by this class
	MaintainCatalogsWindow maintainCatalogsWindow;
	MaintainProductsWindow maintainProductsWindow;

	// Manage catalogs
	private class MaintainCatalogsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			maintainCatalogsWindow = new MaintainCatalogsWindow(primaryStage);
			ObservableList<CatalogPres> list = ManageProductsData.INSTANCE.getCatalogList();
			maintainCatalogsWindow.setData(list);
			maintainCatalogsWindow.show();
			primaryStage.hide();

		}
	}
	
	public MaintainCatalogsHandler getMaintainCatalogsHandler() {
		return new MaintainCatalogsHandler();
	}
	
	private class MaintainProductsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			maintainProductsWindow = new MaintainProductsWindow(primaryStage);
			CatalogPres selectedCatalog = ManageProductsData.INSTANCE.getSelectedCatalog();
			if(selectedCatalog != null) {
				ObservableList<ProductPres> list = ManageProductsData.INSTANCE.getProductsList(selectedCatalog);
				maintainProductsWindow.setData(ManageProductsData.INSTANCE.getCatalogList(), list);
			}
			maintainProductsWindow.show();  
	        primaryStage.hide();
		}
	}
	public MaintainProductsHandler getMaintainProductsHandler() {
		return new MaintainProductsHandler();
	}
	
	private class BackButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {		
			maintainCatalogsWindow.clearMessages();		
			maintainCatalogsWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();
		}
			
	}
	public BackButtonHandler getBackButtonHandler() {
		return new BackButtonHandler();
	}
	
	private class BackFromProdsButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {		
			maintainProductsWindow.clearMessages();		
			maintainProductsWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();
		}
			
	}
	public BackFromProdsButtonHandler getBackFromProdsButtonHandler() {
		return new BackFromProdsButtonHandler();
	}
	
	AddProductPopup addProdPopup;
	ManageProductsData manageProductData; 
	//getFor save product
	public saveProduct saveProduct(){
		return new saveProduct();
	}
	
	// this connect AddProductPopUp to Data
	private class saveProduct implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			//Rules should be managed in a more maintainable way
			if(addProdPopup.getId().getText().trim().equals("")) {
				addProdPopup.getMessageBar().setText("Product Id field must be nonempty! \n[Type '0' to auto-generate ID.]");
			}
			else if(addProdPopup.getName().getText().trim().equals("")) addProdPopup.getMessageBar().setText("Product Name field must be nonempty!");
			else if(addProdPopup.getManufactureDate().getText().trim().equals("")) addProdPopup.getMessageBar().setText("Manufacture Date field must be nonempty!");
			else if(addProdPopup.getNumAvail().getText().trim().equals("")) addProdPopup.getMessageBar().setText("Number in Stock field must be nonempty!");
			else if(addProdPopup.getUnitPrice().getText().trim().equals("")) addProdPopup.getMessageBar().setText("Unit Price field must be nonempty!");
			else if(addProdPopup.getDescription().getText().trim().equals("")) addProdPopup.getMessageBar().setText("Description field must be nonempty!");
			else {
				String idNewVal = addProdPopup.getId().getText();
				if(idNewVal.equals("0")) {
					idNewVal = DefaultData.generateId(100);
				} //Catalog c, Integer pi, String pn, int qa, double up, LocalDate md, String d
			
			}
			
			//creating product
			Product newProd = ProductSubsystemFacade.createProduct(DefaultData.CATALOG_MAP.get(addProdPopup.getCatalogName().getText()), 
					Integer.parseInt(addProdPopup.getId().getText()), addProdPopup.getName().getText(), Integer.parseInt(addProdPopup.getNumAvail().getText()), 
					    Double.parseDouble(addProdPopup.getUnitPrice().getText()), LocalDate.parse(addProdPopup.getManufactureDate().getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
					    addProdPopup.getDescription().getText());
			ProductPres prodPres = new ProductPres();
			prodPres.setProduct(newProd);
			maintainProductsWindow.addItem(prodPres);
			addProdPopup.getMessageBar().setText("");
			addProdPopup.hide();
			
			//creating catalog press
			CatalogPres catPres = new CatalogPres();

			try {
				manageProductData.addToProdList(catPres, prodPres);
			} catch (BackendException e1) {
				e1.printStackTrace();
			}
			

		}
	}


	/*
	 * private MenuItem maintainCatalogs() { MenuItem retval = new
	 * MenuItem("Maintain Catalogs"); retval.setOnAction(evt -> {
	 * MaintainCatalogsWindow maintain = new
	 * MaintainCatalogsWindow(primaryStage); ObservableList<CatalogPres> list =
	 * FXCollections.observableList( DefaultData.CATALOG_LIST_DATA);
	 * maintain.setData(list); maintain.show(); primaryStage.hide();
	 * 
	 * }); return retval; } private MenuItem maintainProducts() { MenuItem
	 * retval = new MenuItem("Maintain Products"); retval.setOnAction(evt -> {
	 * MaintainProductsWindow maintain = new
	 * MaintainProductsWindow(primaryStage); ObservableList<Product> list =
	 * FXCollections.observableList(
	 * DefaultData.PRODUCT_LIST_DATA.get(DefaultData.BOOKS_CATALOG));
	 * maintain.setData(DefaultData.CATALOG_LIST_DATA, list); maintain.show();
	 * primaryStage.hide();
	 * 
	 * }); return retval; }
	 */
}
