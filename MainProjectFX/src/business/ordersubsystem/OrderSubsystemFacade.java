package business.ordersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ProductSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.productsubsystem.ProductSubsystemFacade;

public class OrderSubsystemFacade implements OrderSubsystem {

	private static final Logger LOG = Logger
			.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;

	public OrderSubsystemFacade(CustomerProfile custProfile) {
		this.custProfile = custProfile;
	}

	/**
	 * Used whenever an order item needs to be created from outside the order
	 * subsystem
	 * @throws BackendException 
	 */
	public static OrderItem createOrderItem(Integer prodId, Integer orderId,
			int quantityReq, double totalPrice) throws BackendException {

		ProductSubsystem pss = new ProductSubsystemFacade();
		String name = null;

		name = pss.getProductFromId(prodId).getProductName();

		double unitPrice = totalPrice / quantityReq;
		return new OrderItemImpl(name, quantityReq, unitPrice);
	}

	/** to create an Order object from outside the subsystem */
	public static Order createOrder(Integer orderId, LocalDate orderDate,
			List<OrderItem> orderItems) {
		OrderImpl order = new OrderImpl();
		if (orderId != null)
			order.setOrderId(orderId);
		order.setDate(orderDate);
		order.setOrderItems(orderItems);
		return order;
	}

	// /////////// Methods internal to the Order Subsystem -- NOT public
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
	 * Used by customer subsystem at login to obtain this customer's order
	 * history from the database. Assumes cust id has already been stored into
	 * the order subsystem facade
	 * 
	 * @throws DatabaseException
	 */
	@Override
	public List<Order> getOrderHistory() throws BackendException,
			DatabaseException {
		List<Order> orders = new ArrayList<Order>();
		for (Integer orderId : getAllOrderIds()) {
			orders.add(getOrderData(orderId));
		}
		return orders;
	}

	/**
	 * Used by customer subsystem when a final order is submitted; this method
	 * extracts order and order items from the passed in shopping cart and saves
	 * to database using data access subsystem
	 * 
	 * @throws DatabaseException
	 */
	@Override
	public void submitOrder(ShoppingCart shopCart) throws BackendException,
			DatabaseException {
        LOG.info("Submitted Order");
		DbClassOrder dbClass = new DbClassOrder(custProfile);
		dbClass.submitOrder(shopCart);

	}

	@Override
	public DbClassOrderForTest getGenericDbClassOrder() {
		// TODO Auto-generated method stub
		return new DbClassOrder();
		//return null;
	}

	@Override
	public OrderItem getGenericOrderItem() {
		return new OrderItemImpl("Test item",1,20);
	}


	
	
}