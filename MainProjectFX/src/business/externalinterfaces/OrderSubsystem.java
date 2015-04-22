package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;


public interface OrderSubsystem {
    
	/** 
     *  Used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade 
	 * @throws DatabaseException */
    List<Order> getOrderHistory() throws BackendException, DatabaseException;
	
	/** 
	 * Used by customer subsystem when a final order is submitted; this method extracts order and order items
	 * from the passed in shopping cart and saves to database using data access subsystem
	 * @throws DatabaseException 
	 */ 
    void submitOrder(ShoppingCart shopCart) throws BackendException, DatabaseException;

    public DbClassOrderForTest getGenericDbClassOrder();
    public OrderItem getGenericOrderItem();

 
	

}