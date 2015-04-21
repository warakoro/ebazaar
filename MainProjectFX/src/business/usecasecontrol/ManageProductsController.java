
package business.usecasecontrol;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import presentation.data.ManageProductsData;
import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public class ManageProductsController   {
	ManageProductsData mpd;
    
    private static final Logger LOG = 
    	Logger.getLogger(ManageProductsController.class.getName());
    
    public List<Product> getProductsList(String catalog) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	//return pss.getProductList(catalog);
    	return null;
    }
    
    
//    public void deleteProduct() {
//    	//implement
//    }
    
    public void addCatalog(Catalog catalog) throws BackendException {
    	ProductSubsystem pss  = new ProductSubsystemFacade();
    	pss.saveNewCatalog(catalog);
    }
    public Catalog createCatalog(int id, String name) {
    	return ProductSubsystemFacade.createCatalog(id, name);
    	
    }
    public void addProduct(Product product) throws BackendException {
    	ProductSubsystem pss  = new ProductSubsystemFacade();
    	pss.saveNewProduct(product);
    }
    public Catalog getCatalog(String catName) throws BackendException{
    	ProductSubsystem pss  = new ProductSubsystemFacade();
    	return pss.getCatalogFromName(catName);
    }
    
	public Product createProduct(Catalog catalog, int parseInt, String name,
			int parseInt2, double parseDouble, LocalDate parse,
			String description) {
		 return	ProductSubsystemFacade.createProduct(catalog, parseInt, name, parse, parseInt2, parseDouble);
	}


	/*public void saveProduct(Product newProd,Catalog cat) throws BackendException {
		ProductSubsystem pss  = new ProductSubsystemFacade();
		pss.saveNewProduct(newProd, cat);
		
	}*/
	public List<Product> getProductList(Catalog catalog) throws BackendException {
		ProductSubsystem pss  = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}
	 public List<Catalog> getCatalogList() throws BackendException{
		 ProductSubsystem pss  = new ProductSubsystemFacade();
		return pss.getCatalogList();
	 }
	 public void deleteProduct(Product product) throws BackendException {
		 ProductSubsystem pss  = new ProductSubsystemFacade();
		 pss.deleteProduct(product);
	 }
	 public void deleteCatalog(Catalog catalog) throws BackendException {
		 ProductSubsystem pss  = new ProductSubsystemFacade();
		 pss.deleteCatalog(catalog);
	 }
}
