package middleware.externalinterfaces;

import middleware.exceptions.MiddlewareException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;

public interface CreditVerification {
	public void checkCreditCard(CustomerProfile custProfile, Address billingAddress, 
			CreditCard creditCard, double amount) throws MiddlewareException;
}
