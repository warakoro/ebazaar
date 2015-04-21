
package business.usecasecontrol;

import java.util.ArrayList;
import java.util.List;

import middleware.exceptions.DatabaseException;
import presentation.data.OrderPres;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.ordersubsystem.OrderImpl;



/**
 * @author pcorazza
 */
public class ViewOrdersController   {
	
	CustomerSubsystem customerSubsystem = new CustomerSubsystemFacade() ;
	public ViewOrdersController(){}
	
	public List<OrderPres> getOrderHistoryToPres(){
		List<Order> orderList = new ArrayList<Order>();
		List<OrderPres> orderPresList = new ArrayList<OrderPres>();
		try {
			orderList = customerSubsystem.getOrderHistory();
			for(Order order : orderList){
				Order orderImpl = new OrderImpl();
				orderImpl = order;
				OrderPres orderPres = new OrderPres();
				orderPres.setOrder((OrderImpl) orderImpl);
				orderPresList.add(orderPres);				
				
			}
		} catch (BackendException | DatabaseException e) {
			e.printStackTrace();
		}
		
		return orderPresList;		
	}
	
	
}
