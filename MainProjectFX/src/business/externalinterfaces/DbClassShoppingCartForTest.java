package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassShoppingCartForTest extends DbClass {
	public ShoppingCart retrieveSavedCart(CustomerProfile custProfile) throws DatabaseException;
}