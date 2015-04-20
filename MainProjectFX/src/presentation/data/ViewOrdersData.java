package presentation.data;

import java.util.List;


import business.usecasecontrol.ViewOrdersController;

public enum ViewOrdersData {
	INSTANCE;
	ViewOrdersController viewOrdersController = new ViewOrdersController();
	private OrderPres selectedOrder;
	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}
	
	public List<OrderPres> getOrders() {		
		return viewOrdersController.getOrderHistoryToPres();
	}

}
