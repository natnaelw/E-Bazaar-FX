package presentation.data;

import java.util.List;
import java.util.stream.Collectors;

import presentation.gui.GuiUtils;
import business.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.Order;
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
	
	public List<OrderPres> getOrders() throws BackendException {
		//return DefaultData.ALL_ORDERS;
	 
		
		return ViewOrdersController.INSTANCE.getOrders()
			    .stream()
			    .map(order -> GuiUtils.orderToOrderPres(order))
			    .collect(Collectors.toList());
	}
	public List<OrderItemPres> getOrderItems(Order order)throws BackendException {
		
		return GuiUtils.orderItemsToOrderItemsPres(ViewOrdersController.INSTANCE.getOrderItems(order));
			     
		 
	}
}
