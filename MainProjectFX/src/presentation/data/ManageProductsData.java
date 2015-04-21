package presentation.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.javafx.collections.MappingChange.Map;

import presentation.gui.GuiUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import business.CartItemData;
import business.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.productsubsystem.ProductSubsystemFacade;
import business.usecasecontrol.BrowseAndSelectController;
import business.usecasecontrol.ManageProductsController;

public enum ManageProductsData {
	INSTANCE;
	ProductSubsystem productSubsystem = new ProductSubsystemFacade();
	private CatalogPres defaultCatalog = readDefaultCatalogFromDataSource();
	private ManageProductsController mpc = new ManageProductsController();
	
	private CatalogPres readDefaultCatalogFromDataSource() {
//		try {
//		return mpc.getCatalogList()
//			    .stream()
//			    .map(catalog -> Util.catalogToCatalogPres(catalog))
//			    .collect(Collectors.toList()).get(0);
//		} catch (BackendException e) {
//			return new CatalogPres();
//		}
		return DefaultData.CATALOG_LIST_DATA.get(0);
	}
	public CatalogPres getDefaultCatalog() {
		return defaultCatalog;
	}
	
	private CatalogPres selectedCatalog = defaultCatalog;
	public void setSelectedCatalog(CatalogPres selCatalog) {
		selectedCatalog = selCatalog;
	}
	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}
	//////////// Products List model
	private ObservableMap<CatalogPres, List<ProductPres>> productsMap
	   = readProductsFromDataSource();
/*	
	*//** Initializes the productsMap *//*
	private ObservableMap<CatalogPres, List<ProductPres>> readProductsFromDataSource() {
		return DefaultData.PRODUCT_LIST_DATA;
	}
	
	*//** Delivers the requested products list to the UI *//*
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) {
		return FXCollections.observableList(productsMap.get(catPres));
	}
	
	public ProductPres productPresFromData(Catalog c,int id, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price) {
		
		Product product = ProductSubsystemFacade.createProduct(c,id, name, 
				GuiUtils.localDateForString(date), numAvail, price);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
	public void addToProdList(CatalogPres catPres, ProductPres prodPres) {
		ObservableList<ProductPres> newProducts =
		           FXCollections.observableArrayList(prodPres);
		List<ProductPres> specifiedProds = productsMap.get(catPres);
		
		//Place the new item at the bottom of the list
		specifiedProds.addAll(newProducts);
	}
	
	*//** This method looks for the 0th element of the toBeRemoved list 
	 *  and if found, removes it. In this app, removing more than one product at a time
	 *  is  not supported.
	 * @throws BackendException 
	 *//*
	public boolean removeFromProductList(CatalogPres cat, ObservableList<ProductPres> toBeRemoved) throws BackendException {
		ManageProductsController mpc = new ManageProductsController();
		if(toBeRemoved != null && !toBeRemoved.isEmpty()) {
			mpc.deleteProduct(toBeRemoved.get(0).getProduct(), cat.getCatalog());
			boolean result = productsMap.get(cat).remove(toBeRemoved.get(0));
			//boolean result = productsMap.remove(toBeRemoved.get(0), toBeRemoved.get(0).getProduct());
			return result;
		}
		return false;
	}*/
		
	//////// Catalogs List model
	private ObservableMap<CatalogPres, List<ProductPres>> readProductsFromDataSource() {
		ObservableMap<CatalogPres, List<ProductPres>> productsMap =
	            FXCollections.observableHashMap();
		//Map<CatalogPres, List<ProductPres>> productsMap = new HashMap<CatalogPres, List<ProductPres>>();
		
		List<CatalogPres> catPresList = readCatalogsFromDataSource();
		for (CatalogPres catPres : catPresList) {
			List<Product> products;
			try {
				products = productSubsystem.getProductList(catPres.getCatalog());
				
				List<ProductPres> productsPres = new ArrayList<ProductPres>();
				for (Product product : products) {
					productsPres.add(new ProductPres(product));
				}
				
				productsMap.put(catPres, productsPres);
			} catch (BackendException e) {
				e.printStackTrace();
			}
		}
		
		return FXCollections.observableMap(productsMap);
	}
	
	/** Delivers the requested products list to the UI */
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) {
		return FXCollections.observableList(productsMap.get(catPres));
	}
	
	public ProductPres productPresFromData(Catalog c,Integer id, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price) {
		
		
		Product product = ProductSubsystemFacade.createProduct(c, id,name, 
				GuiUtils.localDateForString(date), numAvail, price);
		ProductPres prodPres = new ProductPres(product);
		prodPres.setProduct(product);
		return prodPres;
	}
	
	public void addToProdList(CatalogPres catPres, ProductPres prodPres) {
		ProductSubsystemFacade productSubsystemFacade = new ProductSubsystemFacade();
		prodPres.getProduct().setCatalog(catPres.getCatalog());
		try {
			Integer productId = productSubsystemFacade.saveNewProduct(prodPres.getProduct());
			prodPres.getProduct().setProductId(productId);
		} catch (BackendException e) {
			e.printStackTrace();
		}
		
		ObservableList<ProductPres> newProducts =
		           FXCollections.observableArrayList(prodPres);
		List<ProductPres> specifiedProds = productsMap.get(catPres);
		
		//Place the new item at the bottom of the list
		specifiedProds.addAll(newProducts);
	}
	
	/** This method looks for the 0th element of the toBeRemoved list 
	 *  and if found, removes it. In this app, removing more than one product at a time
	 *  is  not supported.
	 */
	public boolean removeFromProductList(CatalogPres cat, ObservableList<ProductPres> toBeRemoved) {
		if(toBeRemoved != null && !toBeRemoved.isEmpty()) {
			ProductSubsystemFacade productSubsystemFacade = new ProductSubsystemFacade();
			try {
				productSubsystemFacade.deleteProduct(toBeRemoved.get(0).getProduct());
			} catch (BackendException e) {
				e.printStackTrace();
			}
			boolean result = productsMap.get(cat).remove(toBeRemoved.get(0));
			
			return result;
		}
		return false;
	}
	private ObservableList<CatalogPres> catalogList = readCatalogsFromDataSource();

	/** Initializes the catalogList */
	private ObservableList<CatalogPres> readCatalogsFromDataSource() {
		List<CatalogPres> catalogListPress  = new ArrayList<CatalogPres>();
		try {
			catalogListPress  = mpc.getCatalogList()
				    .stream()
				    .map(catalog -> Util.catalogToCatalogPres(catalog))
				    .collect(Collectors.toList());
			} catch (BackendException e) {
				
			}
		
		return FXCollections.observableList(catalogListPress);
		//return FXCollections.observableList(DefaultData.CATALOG_LIST_DATA);
	}

	/** Delivers the already-populated catalogList to the UI */
	public ObservableList<CatalogPres> getCatalogList() {
		catalogList = readCatalogsFromDataSource();
		return catalogList;
	}

	public CatalogPres catalogPresFromData(int id, String name) {
		Catalog cat = ProductSubsystemFacade.createCatalog(id, name);
		CatalogPres catPres = new CatalogPres();
		catPres.setCatalog(cat);
		return catPres;
	}

	public void addToCatalogList(CatalogPres catPres) {
		ObservableList<CatalogPres> newCatalogs = FXCollections
				.observableArrayList(catPres);

		// Place the new item at the bottom of the list
		// catalogList is guaranteed to be non-null
		boolean result = catalogList.addAll(newCatalogs);
		if(result) { //must make this catalog accessible in productsMap
			productsMap.put(catPres, FXCollections.observableList(new ArrayList<ProductPres>()));
		}
	}

	/**
	 * This method looks for the 0th element of the toBeRemoved list in
	 * catalogList and if found, removes it. In this app, removing more than one
	 * catalog at a time is not supported.
	 * 
	 * This method also updates the productList by removing the products that
	 * belong to the Catalog that is being removed.
	 * 
	 * Also: If the removed catalog was being stored as the selectedCatalog,
	 * the next item in the catalog list is set as "selected"
	 * @throws BackendException 
	 */
	public boolean removeFromCatalogList(ObservableList<CatalogPres> toBeRemoved) throws BackendException {
		ManageProductsController mpc = new ManageProductsController();
		boolean result = false;
		CatalogPres item = toBeRemoved.get(0);
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			mpc.deleteCatalog(toBeRemoved.get(0).getCatalog());
			result = catalogList.remove(item);
		}
		if(item.equals(selectedCatalog)) {
			if(!catalogList.isEmpty()) {
				selectedCatalog = catalogList.get(0);
			} else {
				selectedCatalog = null;
			}
		}
		if(result) {//update productsMap
			productsMap.remove(item);
		}
		return result;
	}
	
	//Synchronizers
	public class ManageProductsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			productsMap.put(selectedCatalog, list);
		}
	}
	public ManageProductsSynchronizer getManageProductsSynchronizer() {
		return new ManageProductsSynchronizer();
	}
	
	private class ManageCatalogsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			catalogList = list;
		}
	}
	public ManageCatalogsSynchronizer getManageCatalogsSynchronizer() {
		return new ManageCatalogsSynchronizer();
	}
}
