
package business.usecasecontrol;

import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public class ManageProductsController   {
    
    private static final Logger LOG = 
    	Logger.getLogger(ManageProductsController.class.getName());
    
    public List<Product> getProductsList(Catalog catalog) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	return pss.getProductList(catalog);
    	//return null;
    }
    
    
    public void deleteProduct(Product product) throws BackendException {
    	//implement
    	ProductSubsystem pss = new ProductSubsystemFacade(); 
    	pss.deleteProduct(product);
    }
    
    public void addProduct(Product product, Catalog catalog) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade(); 
    	pss.saveNewProduct(product, catalog);
    }
}
