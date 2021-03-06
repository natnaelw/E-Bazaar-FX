package business.ordersubsystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
 
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.DbClassPaymentInfoForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.shoppingcartsubsystem.DbClassShoppingCart;

public class OrderSubsystemFacade implements OrderSubsystem {
	private static final Logger LOG = Logger
			.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;

	public OrderSubsystemFacade(CustomerProfile custProfile) {
		this.custProfile = custProfile;
	}
public OrderSubsystemFacade(){}
	/**
	 * Used whenever an order item needs to be created from outside the order
	 * subsystem
	 */
	public static OrderItem createOrderItem(Integer prodId, Integer orderId,
			String quantityReq, String totalPrice) {

		int qty = Integer.parseInt(quantityReq);
		double price = Double.parseDouble(totalPrice) / qty;

		return new OrderItemImpl(prodId, orderId, qty, price);
	}

	
	public static Order createOrder(Integer orderId, LocalDate orderDate, List<OrderItem> orderItems) {
    	OrderImpl order = new OrderImpl();
    	//autoboxing of Integer will throw an exception if orderId is null
    	if(orderId != null) order.setOrderId(orderId);
    	order.setDate(orderDate);
    	order.setOrderItems(orderItems);
    	return order;
    	
    }
	/** to create an Order object from outside the subsystem */
	public static Order createOrder(Integer orderId, String orderDate,
			String totalPrice) {
		
		 DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(
			        FormatStyle.MEDIUM).withLocale(Locale.ENGLISH);
	    
		final LocalDate dt = LocalDate.parse(orderDate, formatter);
	
		OrderImpl order = new OrderImpl();
		order.setOrderId(orderId);
		order.setDate(dt);
		return order;
	}

	// /////////// Methods internal to the Order Subsystem -- NOT public
	  List<Integer> getAllOrderIds() throws DatabaseException {

		DbClassOrder dbClass = new DbClassOrder();
		return dbClass.getAllOrderIds(custProfile);

	}

	public List<OrderItem> getOrderItems(Integer orderId) throws BackendException {
		DbClassOrder dbClass = new DbClassOrder();
		return dbClass.getOrderItems(orderId);
	}

	OrderImpl getOrderData(Integer orderId) throws DatabaseException {
		DbClassOrder dbClass = new DbClassOrder();
		return dbClass.getOrderData(orderId);
	}

	@Override
	public List<Order> getOrderHistory() throws BackendException {
		DbClassOrder dbClass = new DbClassOrder(this.custProfile);
		return dbClass.getOrderHistory();
	}

	@Override
	public void submitOrder(ShoppingCart shopCart) throws BackendException {
		DbClassOrder dbClass = new DbClassOrder(this.custProfile);
		DbClassShoppingCart dbShoppingCart = new DbClassShoppingCart();
		try {
			dbClass.submitOrder(shopCart);
			 
		} catch (DatabaseException e) {
		
			// throw new DatabaseException(  e);
			LOG.log(Level.SEVERE, e.getMessage());
			 
		}
		
	}
	
	//for testing
	@Override
	public DbClassOrderForTest getGenericDbClassOrder() {
		 return new DbClassOrder();
		 
		 
	}

}
