package business.usecasecontrol;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;



public enum CheckoutController  {
	INSTANCE;
	CustomerSubsystem cust = 
			(CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	ShoppingCartSubsystem sc = ShoppingCartSubsystemFacade.INSTANCE;
		
	public void runShoppingCartRules() throws RuleException, BusinessException {
		//implement
		 sc.runShoppingCartRules();	
	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		//implement
		cust.runPaymentRules(addr, cc);
	}
	
	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
	
		return cust.runAddressRules(addr);
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		//implement
		
		 sc.runFinalOrderRules();	
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard() throws BusinessException {
		//implement
		cust.checkCreditCard(sc.getLiveCart().getPaymentInfo()); 
	}
	
	public void saveNewAddress(Address addr) throws BackendException {
				
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		//implement
		cust.submitOrder();
	}
	
	public List<Address> retrieveShippingAddresses() {
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

	public void setBillingShippingOmLiveCart(Address bill, Address ship) {
		setBillingAddressOnLiveCart(bill);
		setShippingAddressOnLiveCart(ship);
	}

}