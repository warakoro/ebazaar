package business.customersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderItemImpl;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

public class CustomerSubsystemFacade implements CustomerSubsystem {
	//private static final Logger LOG = Logger.getLogger(CustomerSubsystemFacade.class.getPackage().getName(), null);
	ShoppingCartSubsystem shoppingCartSubsystem;
	OrderSubsystem orderSubsystem ;
	List<Order> orderHistory;
	AddressImpl defaultShipAddress;
	AddressImpl defaultBillAddress;
	CreditCardImpl defaultPaymentInfo;
	CustomerProfileImpl customerProfile;
	CreditVerification creditVerification;
	ShoppingCart shoppingCart;
	
	
	/** Use for loading order history,
	 * default addresses, default payment info, 
	 * saved shopping cart,cust profile
	 * after login*/
    public void initializeCustomer(Integer id, int authorizationLevel) 
    		throws BackendException {
    	boolean isAdmin = (authorizationLevel >= 1);
		loadCustomerProfile(id, isAdmin);
		loadDefaultShipAddress();
		loadDefaultBillAddress();
		loadDefaultPaymentInfo();
		loadOrderData();
		shoppingCartSubsystem = ShoppingCartSubsystemFacade.INSTANCE;
		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.retrieveSavedCart();
    }
    
    void loadCustomerProfile(int id, boolean isAdmin) throws BackendException {
    	try {
			DbClassCustomerProfile dbclass = new DbClassCustomerProfile();
			dbclass.readCustomerProfile(id);
			customerProfile = dbclass.getCustomerProfile();
			customerProfile.setIsAdmin(isAdmin);

		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
    }
    void loadDefaultShipAddress() throws BackendException {
    	//implement
    	/////DONE\\\\\    	
    	try {
    		DbClassAddress dbclass = new DbClassAddress();
			dbclass.readDefaultShipAddress(customerProfile);
			defaultShipAddress = dbclass.getDefaultShipAddress();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
    	
    }
	void loadDefaultBillAddress() throws BackendException {
	//implement
	/////DONE\\\\\    	
    	try {
    		DbClassAddress dbclass = new DbClassAddress();
			dbclass.readDefaultBillAddress(customerProfile);
			defaultBillAddress = dbclass.getDefaultBillAddress();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
	void loadDefaultPaymentInfo() throws BackendException {
		//implement
		//Created new Class DbClassPayment for this to work
		//DONE\\
		try {
    		DbClassPayment dbclass = new DbClassPayment();
			dbclass.readDefaultPaymentInfo(customerProfile);
			defaultPaymentInfo = dbclass.getDefaultPaymentInfo();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	void loadOrderData() throws BackendException {
		
		// retrieve the order history for the customer and store here
		//DONE\\
		orderSubsystem = new OrderSubsystemFacade(customerProfile);

	
	}
    /**
     * Returns true if user has admin access
     */
    public boolean isAdmin() {
    	return customerProfile.isAdmin();
    }
    
    
    
    /** 
     * Use for saving an address created by user  
     */
    public void saveNewAddress(Address addr) throws BackendException {
    	try {
			DbClassAddress dbClass = new DbClassAddress();
			dbClass.setAddress(addr);
			dbClass.saveAddress(customerProfile);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
    }
    
    public CustomerProfile getCustomerProfile() {

		return customerProfile;
	}

	public Address getDefaultShippingAddress() {
		return defaultShipAddress;
	}

	public Address getDefaultBillingAddress() {
		return defaultBillAddress;
	}
	public CreditCard getDefaultPaymentInfo() {
		return defaultPaymentInfo;
	}
 
    
    /** 
     * Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window 
	 */
    public List<Address> getAllAddresses() throws BackendException {
		// ///DONE\\\\\
    	//Check the query
		List<Address> listOfAddress = new ArrayList<Address>();
		try {
			DbClassAddress dbclass = new DbClassAddress();
			dbclass.readAllAddresses(customerProfile);
			listOfAddress = dbclass.getAddressList();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		/*
		 * Stubbing Address add1 = new AddressImpl("1000 N 4th street",
		 * "Fairfield", "Iowa", "52557", false, true); Address add2 = new
		 * AddressImpl("1000 Bullington street", "New York City", "New York",
		 * "52557", false, true); listOfAddress.add(add1);
		 * listOfAddress.add(add2);
		 */
		return listOfAddress;
	}

	public Address runAddressRules(Address addr) throws RuleException,
			BusinessException {

		Rules transferObject = new RulesAddress(addr);
		transferObject.runRules();

		// updates are in the form of a List; 0th object is the necessary
		// Address
		AddressImpl update = (AddressImpl) transferObject.getUpdates().get(0);
		return update;
	}

	public void runPaymentRules(Address addr, CreditCard cc)
			throws RuleException, BusinessException {
		Rules transferObject = new RulesPayment(addr, cc);
		transferObject.runRules();
	}
	
	
	public static Address createAddress(String street, String city,
			String state, String zip, boolean isShip, boolean isBill) {
		return new AddressImpl(street, city, state, zip, isShip, isBill);
	}

	public static CustomerProfile createCustProfile(Integer custid,
			String firstName, String lastName, boolean isAdmin) {
		return new CustomerProfileImpl(custid, firstName, lastName, isAdmin);
	}

	public static CreditCard createCreditCard(String nameOnCard,
			String expirationDate, String cardNum, String cardType) {
		return new CreditCardImpl(nameOnCard, expirationDate, cardNum, cardType);
	}

	@Override
	public List<Order> getOrderHistory() throws BackendException {
		/*implement*/
		///DONE\\\\
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
		//orderSubsystem = new OrderSubsystemFacade(customerProfile);
		//return orderSubsystem.getOrderHistory();
		return listOrder;
	}

	@Override
	public void setShippingAddressInCart(Address addr) {
		///DONE\\\
			this.shoppingCartSubsystem.setShippingAddress(addr);
	}

	@Override
	public void setBillingAddressInCart(Address addr) {
		///DONE\\\
		this.shoppingCartSubsystem.setBillingAddress(addr);
	}

	@Override
	public void setPaymentInfoInCart(CreditCard cc) {
		///DONE\\\
		this.shoppingCartSubsystem.setPaymentInfo(cc);
	}

	@Override
	public void submitOrder() throws BackendException {
		///DONE\\\
		//this operation is done by order subsystem
		orderSubsystem.submitOrder(shoppingCartSubsystem.getLiveCart());
	}

	@Override
	public void refreshAfterSubmit() throws BackendException {
		///DONE\\\
		//Reload the order data
		loadOrderData();
	}

	@Override
	public ShoppingCartSubsystem getShoppingCart() {
		/*DONE*/
		//***********check
		return shoppingCartSubsystem;
	}

	@Override
	public void saveShoppingCart() throws BackendException {
		///DONE\\\
		///this operation is done by shoppingCard subsystem 
		shoppingCartSubsystem.saveLiveCart();
	}

	@Override
	public void checkCreditCard(CreditCard cc) throws BusinessException {
		//
		//verifying credit card
		try {
			creditVerification.checkCreditCard(customerProfile, defaultBillAddress, cc, shoppingCart.getTotalPrice());
		} catch (MiddlewareException e) {
			e.printStackTrace();
		}
	}

	//TESTING
	@Override
	public DbClassAddressForTest getGenericDbClassAddress() {
		///DONE\\\
		return new DbClassAddress();
	}

	@Override
	public CustomerProfile getGenericCustomerProfile() {
		///DONE\\\
		return new CustomerProfileImpl(1, "FirstTest", "LastTest");
	}
}
