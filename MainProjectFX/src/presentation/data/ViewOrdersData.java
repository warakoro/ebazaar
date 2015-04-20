package presentation.data;

import java.util.List;

import business.Util;
import business.exceptions.BackendException;
import business.usecasecontrol.ViewOrdersController;

public enum ViewOrdersData {
	INSTANCE;
	private OrderPres selectedOrder;
	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}
	
	public List<OrderPres> getOrders() throws BackendException{
	//	return DefaultData.ALL_ORDERS;
		return Util.orderListToOrderPresList(ViewOrdersController.INSTANCE.getOrderHistory());
	}
}
