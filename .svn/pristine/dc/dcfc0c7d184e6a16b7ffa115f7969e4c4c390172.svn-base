
package business.usecasecontrol;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.BusinessConstants;
import business.SessionCache;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ProductSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;
import presentation.data.LoginData;
import presentation.data.OrderPres;



/**
 * @author pcorazza
 */
public enum ViewOrdersController   {
	
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());

	public List<Order> getOrders() throws BackendException {
		// LoginData
		 CustomerSubsystem cs =(CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		 
		OrderSubsystem oss = new OrderSubsystemFacade(cs.getCustomerProfile());
		return oss.getOrderHistory();
	}

	public List<OrderItem> getOrderItems(Order order) throws BackendException  {
		OrderSubsystem oss = new OrderSubsystemFacade();
		return oss.getOrderItems(order.getOrderId());
	}
	
	 
}
