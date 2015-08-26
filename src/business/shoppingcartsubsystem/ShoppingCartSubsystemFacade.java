package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.RulesQuantity;
import business.customersubsystem.CreditCardImpl;
import business.customersubsystem.RulesPayment;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;

public enum ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {
	INSTANCE;
	
	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	ShoppingCartImpl savedCart;
	Integer shopCartId;
	CustomerProfile customerProfile;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());

	// interface methods
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;		
	}
	
	public void makeSavedCartLive() {
		liveCart = savedCart;
	}
	
	public ShoppingCart getLiveCart() {
		return liveCart;
	}
	
	public ShoppingCart getLivCart() {
		return liveCart;
	}
	public void retrieveSavedCart() throws BackendException {
		try {
			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			ShoppingCartImpl cartFound = dbClass.retrieveSavedCart(customerProfile);
			if(cartFound == null) {
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {
				savedCart = cartFound;
			}
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
	
	public void savedCart() throws BackendException {
		try {
			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			dbClass.saveCart(customerProfile, liveCart);			
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
	
	public static CartItem createCartItem(String productName, String quantity,
            String totalprice) {
		try {
			return new CartItemImpl(productName, quantity, totalprice);
		} catch(BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}
	
	
	
	public CartItem addCartItem(String productName, String quantity,
            String totalprice) {
		
		try {
			
			return new CartItemImpl(productName, quantity, totalprice);
		} catch(BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}
	
	public void updateShoppingCartItems(List<CartItem> list) {
		liveCart.setCartItems(list);
	}
	
	public List<CartItem> getCartItems() {
		return liveCart.getCartItems();
	}
	
	public void runFinalOrderRules(ShoppingCart shoppingCart)
			throws RuleException, BusinessException {
		Rules transferObject = new RulesFinalOrder(shoppingCart);
		transferObject.runRules();
	}
	
	
	//interface methods for testing
	
	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}

	
	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	@Override
	public void setShippingAddress(Address addr) {
		liveCart.setShipAddress(addr);
		
	}

	@Override
	public void setBillingAddress(Address addr) {
		liveCart.setBillAddress(addr);
		
	}

	@Override
	public void setPaymentInfo(CreditCard cc) {
		liveCart.setPaymentInfo(new CreditCardImpl("", "11/11/2000", "", ""));
		
	}

	@Override
	public void saveLiveCart() throws BackendException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runShoppingCartRules(ShoppingCart shoppingCart) throws RuleException, BusinessException {
		// TODO Auto-generated method stub
		Rules transferObject = new RulesShoppingCart(shoppingCart);
		transferObject.runRules();
		
	}

	@Override
	public void runFinalOrderRules() throws RuleException, BusinessException {
		// TODO Auto-generated method stub
		
	}

}
