package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassPayment implements DbClass {
	private static final Logger LOG = 
		Logger.getLogger(DbClassPayment.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 	new DataAccessSubsystemFacade();
    private final String READ_DEFAULT_PAYMENT_INFO = "ReadDefaultPaymentInfo";
    String query;
    private String queryType;
    private CustomerProfileImpl custProfile;
    private CreditCardImpl creditCard;
    
	public CreditCardImpl getDefaultPaymentInfo() {
		return creditCard;
	}
    
    public void readDefaultPaymentInfo(CustomerProfileImpl custProfile) throws DatabaseException {
		 this.custProfile = custProfile;
        queryType=READ_DEFAULT_PAYMENT_INFO;
        dataAccessSS.atomicRead(this);
	}
 
    public void buildQuery() {
    	LOG.info("Query for " + queryType + ": " + query);
        if(queryType.equals(READ_DEFAULT_PAYMENT_INFO)){
            buildReadQuery();
        }
    }
    
    void buildReadQuery() {
        query = "SELECT nameoncard,expdate, cardtype,cardnum "+
                "FROM Customer "+
                "WHERE custid = "+custProfile.getCustId();
    }
    
 
    public void populateEntity(ResultSet rs) throws DatabaseException {
        try {
        
            //we take the first returned row
            if(rs.next()){
            	creditCard = new CreditCardImpl(rs.getString("nameoncard"),rs.getString("expdate"),rs.getString("cardtype"),rs.getString("cardnum"));
               
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }


    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
 
    }

 
    public String getQuery() {
        return query;
 
    }



 
}
