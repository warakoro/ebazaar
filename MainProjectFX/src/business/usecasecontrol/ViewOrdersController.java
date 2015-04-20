
package business.usecasecontrol;

import java.util.List;

import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;


/**
 * @author pcorazza
 */
public enum ViewOrdersController   {
	INSTANCE;
	
	public List<Order> getOrderHistory() throws BackendException{
		CustomerSubsystem css = (CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		OrderSubsystem pss = new OrderSubsystemFacade(css.getCustomerProfile());
		return pss.getOrderHistory();
		}
	
}
