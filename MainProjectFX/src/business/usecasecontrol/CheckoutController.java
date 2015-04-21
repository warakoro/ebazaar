package business.usecasecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import middleware.exceptions.DatabaseException;
import presentation.data.BrowseSelectData;
import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

public enum CheckoutController {
	INSTANCE;
	CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
			.get(BusinessConstants.CUSTOMER);
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	ShoppingCartSubsystem sc = ShoppingCartSubsystemFacade.INSTANCE;

	public void runShoppingCartRules() throws RuleException, BusinessException {
		// implement
		ShoppingCartSubsystem sc = ShoppingCartSubsystemFacade.INSTANCE;
		sc.runShoppingCartRules();
	}

	public void runPaymentRules(Address addr, CreditCard cc)
			throws RuleException, BusinessException {
		// implement
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		cust.runPaymentRules(addr, cc);
	}

	public Address runAddressRules(Address addr) throws RuleException,
			BusinessException {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}

	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules()
			throws RuleException, BusinessException {
		// implement
		ShoppingCartSubsystem sc = ShoppingCartSubsystemFacade.INSTANCE;
		sc.runFinalOrderRules();
	}

	/**
	 * Asks Customer Subsystem to check credit card against Credit Verification
	 * System
	 */
	public void verifyCreditCard() throws BusinessException {
		// implement
		ShoppingCartSubsystem sc = ShoppingCartSubsystemFacade.INSTANCE;
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		cust.checkCreditCard(sc.getLiveCart().getPaymentInfo());
	}

	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		cust.saveNewAddress(addr);
	}

	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		// implement
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		BrowseSelectData.INSTANCE.updateShoppingCart();
		try {
			cust.submitOrder();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Address> retrieveShippingAddresses() {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		try {
			List<Address> allAddresses = new ArrayList();
			if (cust != null) {
				allAddresses = cust.getAllAddresses();
			} else {
				return allAddresses;
			}
			return allAddresses.stream()
					.filter(address -> address.isShippingAddress())
					.collect(Collectors.toList());
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			LOG.severe("Error:Could not retrieve shipping addresses");
		}
		return new ArrayList<>();
	}

	public List<Address> retrieveBillingAddresses() {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance()
				.get(BusinessConstants.CUSTOMER);
		try {
			List<Address> allAddresses = new ArrayList();
			if(cust !=null){
			 allAddresses = cust.getAllAddresses();
			}else{
				return allAddresses;
			}
			return allAddresses.stream()
					.filter(address -> address.isBillingAddress())
					.collect(Collectors.toList());
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			LOG.severe("Error:Could not retrieve billing addresses");
		}
		return new ArrayList<>();
	}

	public void setBillingAddressOnLiveCart(Address add) {
		sc.setBillingAddress(add);
	}

	public void setShippingAddressOnLiveCart(Address add) {
		sc.setShippingAddress(add);
	}

	public void setBillingShippingOnLiveCart(Address bill, Address ship) {
		setBillingAddressOnLiveCart(bill);
		setShippingAddressOnLiveCart(ship);
	}
	
	public void setPaymentOnLiveCart(CreditCard cc){
		sc.setPaymentInfo(cc);
	}

}
