package business.ordersubsystem;

import business.exceptions.BackendException;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public class OrderItemImpl implements OrderItem {
	private int orderItemId;
	private int orderId;
	private String productName;
	private int productId;
	private int quantity;
	private double unitPrice;
	ProductSubsystem ps = new ProductSubsystemFacade();
	
	public OrderItemImpl( String name, int quantity, double price) {
		 
		productName = name;
		this.quantity = quantity;
		this.unitPrice = price;
	}
	
	public OrderItemImpl(int prodId, int orderId, int quantity, double price) {
		 
		 this.productId = prodId;
		this.quantity = quantity;
		this.orderId = orderId;
		this.unitPrice = price;
		 
		try {
			Product prod = ps.getProductFromId(prodId);
			this.setProductName(prod.getProductName());
			 
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int itemID) {
		this.orderItemId = itemID;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderID) {
		this.orderId = orderID;
	}


	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String n) {
		productName = n;
	}


	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int q) {
		quantity = q;
	}


	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double price) {
		unitPrice = price;
	}

	public double getTotalPrice() {
		return unitPrice * quantity;
	}


	@Override
	public int getProductId() {
		return productId;
	}

	@Override
	public void setProductId(int id) {
		productId = id;
		
	}

	

}
