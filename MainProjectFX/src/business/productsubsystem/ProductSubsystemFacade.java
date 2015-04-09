package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.util.TwoKeyHashMap;

public class ProductSubsystemFacade implements ProductSubsystem {
	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}
	public static Product createProduct(Catalog c, String name, 
			LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}
	public static Product createProduct(Catalog c, Integer pi, String pn, int qa, 
			double up, LocalDate md, String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}
	
	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException {
    	try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}	
    }
    public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer,String,Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
    public Product getProductFromId(Integer prodId) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
    
    public List<Catalog> getCatalogList() throws BackendException {
    	try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
    }
    
    public List<Product> getProductList(Catalog catalog) throws BackendException {
    	try {
    		DbClassProduct dbclass = new DbClassProduct();
    		return dbclass.readProductList(catalog);
    	} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
    }
	
	public int readQuantityAvailable(Product product) {
		//IMPLEMENT
		return 5;
	}
}
