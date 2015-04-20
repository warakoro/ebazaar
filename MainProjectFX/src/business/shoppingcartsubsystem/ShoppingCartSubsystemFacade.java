package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.customersubsystem.RulesPayment;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;

public enum ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {
	INSTANCE;
	
	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	ShoppingCartImpl savedCart;
	Integer shopCartId;
	DbClassShoppingCart dbClass = new DbClassShoppingCart();
	CustomerProfile customerProfile;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());

	// interface methods
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}
	
	public void makeSavedCartLive() {
		liveCart = savedCart;
	}
	
	public ShoppingCart getLiveCart() {
		return liveCart;
	}
	

	public void retrieveSavedCart() throws BackendException {
		try {
//			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			ShoppingCartImpl cartFound = (ShoppingCartImpl)dbClass.retrieveSavedCart(customerProfile);
			if(cartFound == null) {
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {
				savedCart = cartFound;
			}
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}

	}
	
	public static CartItem createCartItem(String productName, String quantity,
            String totalprice) {
		try {
			return new CartItemImpl(productName, quantity, totalprice);
		} catch(BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}
	
	public void updateShoppingCartItems(List<CartItem> list) {
		liveCart.setCartItems(list);
	}
	
	
	public List<CartItem> getCartItems() {
		return liveCart.getCartItems();
	}
	
	//interface methods for testing
	
	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}

	
	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	@Override
	public void clearLiveCart() {
		// TODO Auto-generated method stub
		liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
		
	}

	@Override
	public List<CartItem> getLiveCartItems() {
		// TODO Auto-generated method stub
		return liveCart.getCartItems();
	}

	@Override
	public void setShippingAddress(Address addr) {
		// TODO Auto-generated method stub
		liveCart.setShipAddress(addr);
		
	}

	@Override
	public void setBillingAddress(Address addr) {
		// TODO Auto-generated method stub
		liveCart.setBillAddress(addr);
		
	}

	@Override
	public void setPaymentInfo(CreditCard cc) {
		// TODO Auto-generated method stub
		liveCart.setPaymentInfo(cc);
		
	}

	@Override
	public void saveLiveCart() throws BackendException {
		// TODO Auto-generated method stub

		try {
			dbClass.saveCart(customerProfile, liveCart);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			throw new BackendException(e);
		}
		
	}

	@Override
	public void runShoppingCartRules() throws RuleException, BusinessException {
		// TODO Auto-generated method stub
		Rules transferObject = new RulesShoppingCart(liveCart);
				transferObject.runRules();
		
	}

	@Override
	public void runFinalOrderRules() throws RuleException, BusinessException {
		// TODO Auto-generated method stub
		Rules transferObject = new RulesFinalOrder(liveCart);
		transferObject.runRules();
		
	}
}
	
	


