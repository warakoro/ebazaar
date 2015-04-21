package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassCustomerProfileForTest extends DbClass  {
	
	//used to test customer profile
		public CustomerProfile getCustomerProfile();
		public void readCustomerProfile(Integer i) throws DatabaseException;


}
