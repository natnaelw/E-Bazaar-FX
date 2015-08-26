package presentation.control;

import java.util.logging.Logger;

import business.exceptions.BackendException;
 
import business.exceptions.UnauthorizedException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.data.*;
import presentation.gui.OrderDetailWindow;
import presentation.gui.OrdersWindow;

public enum ViewOrdersUIControl {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(ViewOrdersUIControl.class
			.getPackage().getName());
	
	private OrdersWindow ordersWindow;
	private OrderDetailWindow orderDetailWindow;
    private Stage primaryStage;
    private Callback startScreenCallback;
	
	public void setPrimaryStage(Stage ps, Callback returnMessage) {
		primaryStage = ps;
		startScreenCallback = returnMessage;
	}
	
	private class ViewOrdersHandler implements EventHandler<ActionEvent>, Callback {
		/*@Override
		public void handle(ActionEvent evt) {
			ordersWindow = new OrdersWindow(primaryStage);
			try {
				ordersWindow.setData(FXCollections.observableList(ViewOrdersData.INSTANCE.getOrders()));
			} catch (BackendException e) {
				LOG.warning(e.getMessage());
			}
			ordersWindow.show();
	        primaryStage.hide();			
		}	*/
		
		
		@Override
		public void handle(ActionEvent evt) {
			ordersWindow = new OrdersWindow(primaryStage);
			boolean isLoggedIn = DataUtil.isLoggedIn();
			if (!isLoggedIn) {
				LoginUIControl loginControl = new LoginUIControl(ordersWindow, primaryStage, this);
				loginControl.startLogin();
			} else {
				doUpdate();
				
			}
			 
		}

		public void doUpdate() {
			try {
				Authorization.checkAuthorization(ordersWindow,
						DataUtil.custIsAdmin());
			} catch (UnauthorizedException e) {
				ordersWindow.displayError(e.getMessage());
				return;
			}
			 
			try {
				ordersWindow.setData(FXCollections.observableList(ViewOrdersData.INSTANCE.getOrders()));
			} catch (BackendException e) {
				LOG.warning(e.getMessage());
			}
			ordersWindow.show();
			primaryStage.hide();
		 
		}

		public Text getMessageBar() {
			return startScreenCallback.getMessageBar();
		}
	}
	public ViewOrdersHandler getViewOrdersHandler() {
		return new ViewOrdersHandler();
	}
	
	//OrdersWindow
	private class ViewOrderDetailsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			TableView<OrderPres> table = ordersWindow.getTable();
			OrderPres selectedOrder = table.getSelectionModel().getSelectedItem();
			if(selectedOrder == null) {
				ordersWindow.displayError("Please select a row.");
			} else {
				ordersWindow.clearMessages();
				orderDetailWindow = new OrderDetailWindow();
				try {
					orderDetailWindow.setData(FXCollections.observableList(ViewOrdersData.INSTANCE.getOrderItems(selectedOrder.getOrder())));
				} catch (BackendException e) {
					LOG.warning(e.getMessage());
				}
				ordersWindow.hide();
				orderDetailWindow.show();
			}
		}
	}
	
	public ViewOrderDetailsHandler getViewOrderDetailsHandler() {
		return new ViewOrderDetailsHandler();
	}
	
	
	//OrderDetailWindow
	private class OrderDetailsOkHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ordersWindow.show();
			orderDetailWindow.hide();
		}
	}
	public OrderDetailsOkHandler getOrderDetailsOkHandler() {
		return new OrderDetailsOkHandler();
	}
	
	private class CancelHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			ordersWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();
			
		}
	}
	public CancelHandler getCancelHandler() {
		return new CancelHandler();
	}
}
