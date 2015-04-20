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
	
	private Catalog catalogName;
	private String query;
    private String queryType;
    private final String SAVE = "Save";
    
    
    public void saveNewCatalog(Catalog cat) throws DatabaseException {
    	this.catalogName = cat;
    	queryType = SAVE;
    	dataAccessSS.saveWithinTransaction(this);  	
    }
    //creating a new method to delete catalog
//    public void deleteCatalog(Catalog cat){
//    	this.catalogName = cat;
//    	queryType = Delete;
//    	dataAccessSS.saveWithinTransaction(this); 
//    }
     
	public void buildQuery() throws DatabaseException {
		if(queryType.equals(SAVE)) {
			buildSaveQuery();
		}		
	}
	
	void buildSaveQuery() throws DatabaseException {
		query = "INSERT into CatalogType "+
		"(catalogid,catalogname) " +
		"VALUES(NULL,'"+
				  catalogName+"')"; 
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
