package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassProductForTest extends DbClass {

	Product readProduct(Integer i) throws DatabaseException;

}
