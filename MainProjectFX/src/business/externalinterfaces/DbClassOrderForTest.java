package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassOrderForTest extends DbClass {
	
	//for testing read order

	public List<OrderItem> getOrderItems(Integer i) throws DatabaseException;
}
