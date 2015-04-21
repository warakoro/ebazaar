
package business.externalinterfaces;
import java.util.List;





import business.exceptions.BackendException;

public interface ProductSubsystem {

	public int readQuantityAvailable(Product product);
	
	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException;
    
    /** reads the product from the productid */
	public Product getProductFromId(Integer prodId) throws BackendException;
	
	public List<Catalog> getCatalogList() throws BackendException;
	
	/** gets a list of products from the database, based on catalog */
	public List<Product> getProductList(Catalog catalog) throws BackendException;
	
	public Integer getProductIdFromName(String prodName) throws BackendException;
	
	/** retrieves list of catalogs from database */

	
	public Catalog getCatalogFromName(String catName) throws BackendException;

	/** saves newly created catalog */
	public void saveNewCatalog(Catalog cat) throws BackendException;

	/** saves a new product obtained from user input */
	public void saveNewProduct(Product product) throws BackendException;

	/** deletes a product obtained from user input */
	public void deleteProduct(Product product, Catalog cat) throws BackendException;
	
	/** deletes a catalog obtained from user input */
	public void deleteCatalog(Catalog catalog) throws BackendException;

}