package business.customersubsystem;

import java.util.ArrayList;
import java.util.List;

import middleware.exceptions.DatabaseException;
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
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

public class CustomerSubsystemFacade implements CustomerSubsystem {
	//private static final Logger LOG = Logger.getLogger(CustomerSubsystemFacade.class.getPackage().getName(), null);
	ShoppingCartSubsystem shoppingCartSubsystem;
	OrderSubsystem orderSubsystem;
	List<Order> orderHistory;
	AddressImpl defaultShipAddress;
	AddressImpl defaultBillAddress;
	CreditCardImpl defaultPaymentInfo;
	CustomerProfileImpl customerProfile;
	
	
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
		shoppingCartSubsystem = ShoppingCartSubsystemFacade.INSTANCE;
		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.retrieveSavedCart();
		loadOrderData();
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
		return orderSubsystem.getOrderHistory();
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public DbClassAddressForTest getGenericDbClassAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerProfile getGenericCustomerProfile() {
		///DONE\\\
		/*suttubing
		CustomerProfile custPro = new CustomerProfileImpl(1,"Mamadou","DIARRA");*/
		return this.customerProfile;
	}
}
