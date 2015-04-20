package business.ordersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;

public class OrderSubsystemFacade implements OrderSubsystem {
	private static final Logger LOG = Logger.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;
	    
    public OrderSubsystemFacade(CustomerProfile custProfile){
        this.custProfile = custProfile;
    }
	
	
	
	/** Used whenever an order item needs to be created from outside the order subsystem */
    public static OrderItem createOrderItem(Integer prodId,Integer orderId, String quantityReq, String totalPrice) {
    	return null;
    }
    
    /** to create an Order object from outside the subsystem */
    public static Order createOrder(Integer orderId, String orderDate, String totalPrice) {
    	return null;
    }
    
    ///////////// Methods internal to the Order Subsystem -- NOT public
    List<Integer> getAllOrderIds() throws DatabaseException {
        
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getAllOrderIds(custProfile);
        
    }
    List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getOrderItems(orderId);
    }
    
    OrderImpl getOrderData(Integer orderId) throws DatabaseException {
    	DbClassOrder dbClass = new DbClassOrder();
    	return dbClass.getOrderData(orderId);
    }


    /** 
     *  Used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade */
	@Override
	public List<Order> getOrderHistory() throws BackendException {
		/*try{
			List<Order> orders =  new ArrayList();
			for(Integer orderId: getAllOrderIds()){
				orders.add(getOrderData(orderId));
			}
			return orders;
		}catch(DatabaseException e){
			throw new BackendException(e);
		}*/
		
		//DONE\\ By mo Delete as soon as you need
		OrderItemImpl orderItem1 = new OrderItemImpl("sfsf", 2, 4334);
		OrderItemImpl orderItem2 = new OrderItemImpl("sdfsdf",3, 343);
		OrderItemImpl orderItem3 = new OrderItemImpl("dfsdf",2, 454);
		OrderItemImpl orderItem4 = new OrderItemImpl("dfsf",1, 343);
		List<Order> listOrder = new ArrayList<Order>();
		OrderImpl order1 = new OrderImpl();
		OrderImpl order2 = new OrderImpl();
		orderItem1.setOrderItemId(1001);
		orderItem2.setOrderItemId(1002);
		orderItem3.setOrderItemId(1003);
		orderItem4.setOrderItemId(1004);
		orderItem1.setOrderId(101);
		orderItem2.setOrderId(101);
		orderItem3.setOrderId(102);
		orderItem4.setOrderId(102);
		order1.setOrderId(10);
		order2.setOrderId(11);
		order1.setDate(LocalDate.of(2014, 11, 11));
		order2.setDate(LocalDate.of(2015, 2, 5));
		order1.setOrderItems(Arrays.asList(orderItem1, orderItem2));
		order2.setOrderItems(Arrays.asList(orderItem3, orderItem4));
		listOrder = Arrays.asList(order1,order2);
		return listOrder;
	}


	/** 
	 * Used by customer subsystem when a final order is submitted; this method extracts order and order items
	 * from the passed in shopping cart and saves to database using data access subsystem
	 */ 
	@Override
	public void submitOrder(ShoppingCart shopCart) throws BackendException {
		// TODO Auto-generated method stub
		
	}
}
