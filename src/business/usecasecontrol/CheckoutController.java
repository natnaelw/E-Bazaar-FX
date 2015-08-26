package business.usecasecontrol;

import java.util.logging.Logger;

import presentation.data.DataUtil;
import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;


public enum CheckoutController  {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	
	public void runShoppingCartRules(ShoppingCart shoppingCart) throws RuleException, BusinessException {
		ShoppingCartSubsystemFacade.INSTANCE.runShoppingCartRules(shoppingCart);	
		
	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		//implement
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		cust.runPaymentRules(addr, cc);
	}
	
	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCart sc) throws RuleException, BusinessException {
		
		ShoppingCartSubsystemFacade.INSTANCE.runFinalOrderRules(sc);
		
	 
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard() throws BusinessException {
		//implement
		 CustomerSubsystem cust = DataUtil.readCustFromCache();
		 cust.checkCreditCard();
	}
	
	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);			
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		//implement
		CustomerSubsystem cust =  DataUtil.readCustFromCache();;
		cust.submitOrder();
	}
	



}
