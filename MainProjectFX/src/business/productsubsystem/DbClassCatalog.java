package business.productsubsystem;

import java.sql.ResultSet;
import java.util.logging.Logger;

import business.externalinterfaces.Catalog;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCatalog implements DbClass {
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		Logger.getLogger(DbClassCatalog.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	
	private Integer catalogId;
	private String catalogName;
	private String query;
    private String queryType;
    private final String SAVE = "Save";
    private final String DELETE = "Delete";
    
    public void saveNewCatalog(Catalog cat) throws DatabaseException {
    	this.catalogName = cat.getName();
    	queryType = SAVE;
    	dataAccessSS.saveWithinTransaction(this);  	
    }
    //creating a new method to delete catalog
    public void deleteCatalog(Catalog catalog) throws DatabaseException{
    	this.catalogId = catalog.getId();
    	queryType = DELETE;
    	dataAccessSS.deleteWithinTransaction(this);
    }
     
	public void buildQuery() throws DatabaseException {
		if(queryType.equals(SAVE)) {
			buildSaveQuery();
		}	
		else if(queryType.equals(DELETE)) {
			buildDeleteQuery();
		}		
	}
	
	void buildSaveQuery() throws DatabaseException {
		query = "INSERT into CatalogType "+
		"(catalogid,catalogname) " +
		"VALUES(NULL,'"+
				  catalogName+"')"; 
	}

	void buildDeleteQuery() throws DatabaseException {
 
	  query = "DELETE FROM CatalogType WHERE catalogId = " + catalogId.intValue();
	}
	
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		// do nothing
		
	}
	
}
