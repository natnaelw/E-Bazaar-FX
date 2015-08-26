package presentation.control;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.data.CheckoutData;
import presentation.data.DataUtil;
import presentation.data.ErrorMessages;
import presentation.gui.CatalogListWindow;
import presentation.gui.FinalOrderWindow;
import presentation.gui.OrderCompleteWindow;
import presentation.gui.PaymentWindow;
import presentation.gui.ShippingBillingWindow;
import presentation.gui.ShoppingCartWindow;
import presentation.gui.TermsWindow;
//import rulesengine.OperatingException;
//import rulesengine.ReteWrapper;
//import rulesengine.ValidationException;
//import system.rulescore.rulesengine.*;
//import system.rulescore.rulesupport.*;
//import system.rulescore.util.*;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerProfile;
import business.shoppingcartsubsystem.CartItemImpl;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.usecasecontrol.CheckoutController;

public enum CheckoutUIControl {
	INSTANCE;

	private static final Logger LOG = Logger.getLogger(CheckoutUIControl.class
			.getPackage().getName());
	// Windows managed by CheckoutUIControl
	ShippingBillingWindow shippingBillingWindow;
	PaymentWindow paymentWindow;
	TermsWindow termsWindow;
	FinalOrderWindow finalOrderWindow;
	OrderCompleteWindow orderCompleteWindow;

	public ShippingBillingWindow getShippingBillingWindow() {
		return shippingBillingWindow;
	}

	// handler for ShoppingCartWindow proceeding to checkout
	private class ProceedFromCartToShipBill implements
			EventHandler<ActionEvent>, Callback {
		CheckoutData data = CheckoutData.INSTANCE;

		public void doUpdate() {
			CustomerProfile custProfile = data.getCustomerProfile();
			Address defaultShipAddress = data.getDefaultShippingData();
			Address defaultBillAddress = data.getDefaultBillingData();

			shippingBillingWindow.setShippingAddress(custProfile.getFirstName()
					+ " " + custProfile.getLastName(),
					defaultShipAddress.getStreet(),
					defaultShipAddress.getCity(),
					defaultShipAddress.getState(), defaultShipAddress.getZip());
			shippingBillingWindow.setBillingAddress(custProfile.getFirstName()
					+ " " + custProfile.getLastName(),
					defaultBillAddress.getStreet(),
					defaultBillAddress.getCity(),
					defaultBillAddress.getState(), defaultBillAddress.getZip());
			ShoppingCartSubsystemFacade.INSTANCE.getLiveCart().getCartItems().clear();
			List<CartItem> list=new LinkedList<CartItem>();
			for (CartItemPres element : ShoppingCartWindow.INSTANCE.getCartItems()) {
				try {
					list.add(new CartItemImpl(element.getCartItem().getItemName(), String.valueOf( element.getCartItem().getQuantity()), String.valueOf(element.getCartItem().getTotalPrice())));
				} catch (BackendException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ShoppingCartSubsystemFacade.INSTANCE.getLiveCart().setCartItems(list);
			shippingBillingWindow.show();
		}

		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.clearMessages();
			ShoppingCartWindow.INSTANCE.setTableAccessByRow();

			boolean rulesOk = true;
			/* check that cart is not empty before going to next screen */

			try {

				CheckoutController.INSTANCE
						.runShoppingCartRules(ShoppingCartSubsystemFacade.INSTANCE
								.getLivCart());
			} catch (RuleException e) {
				rulesOk = false;
				ShoppingCartWindow.INSTANCE.displayError(e.getMessage());

			} catch (BusinessException e) {
				// handle
				rulesOk = false;
				ShoppingCartWindow.INSTANCE
						.displayError("An error has occurred that prevents further processing.");
				// ShoppingCartWindow.INSTANCE.setVisible(true);
			}

			if (rulesOk) {
				ShoppingCartWindow.INSTANCE.hide();
				boolean isLoggedIn = DataUtil.isLoggedIn();
				shippingBillingWindow = new ShippingBillingWindow();
				if (!isLoggedIn) {
					LoginUIControl loginControl = new LoginUIControl(
							shippingBillingWindow, ShoppingCartWindow.INSTANCE,
							this);
					loginControl.startLogin();
				} else {
					doUpdate();
				}
			}

		}
		@Override
		public Text getMessageBar() {
			return ShoppingCartWindow.INSTANCE.getMessageBar();
		}
	}

	public ProceedFromCartToShipBill getProceedFromCartToShipBill() {
		return new ProceedFromCartToShipBill();
	}

	// handlers for ShippingBillingWindow
	private class BackToShoppingCartHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			shippingBillingWindow.clearMessages();
			shippingBillingWindow.hide();
		}
	}

	public BackToShoppingCartHandler getBackToShoppingCartHandler() {
		return new BackToShoppingCartHandler();
	}

	private class ProceedToPaymentHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			shippingBillingWindow.clearMessages();
			boolean rulesOk = true;
			Address cleansedShipAddress = null;
			Address cleansedBillAddress = null;
			if (shippingBillingWindow.getSaveShipAddr()) {
				try {
					cleansedShipAddress = CheckoutController.INSTANCE
							.runAddressRules(shippingBillingWindow
									.getShippingAddress());
				} catch (RuleException e) {
					rulesOk = false;
					shippingBillingWindow
							.displayError("Shipping address error: "
									+ e.getMessage());
				} catch (BusinessException e) {
					rulesOk = false;
					shippingBillingWindow
							.displayError(ErrorMessages.GENERAL_ERR_MSG
									+ ": Message: " + e.getMessage());
				}
			}
			if (rulesOk) {
				if (shippingBillingWindow.getSaveBillAddr()) {
					try {
						cleansedBillAddress = CheckoutController.INSTANCE
								.runAddressRules(shippingBillingWindow
										.getBillingAddress());
					} catch (RuleException e) {
						rulesOk = false;
						shippingBillingWindow
								.displayError("Billing address error: "
										+ e.getMessage());
					} catch (BusinessException e) {
						rulesOk = false;
						shippingBillingWindow
								.displayError(ErrorMessages.GENERAL_ERR_MSG
										+ ": Message: " + e.getMessage());
					}
				}
			}
			if (rulesOk) {

				LOG.info("Address Rules passed!");
				if (cleansedShipAddress != null) {
					try {

						CheckoutController.INSTANCE
								.saveNewAddress(cleansedShipAddress);
						ShoppingCartSubsystemFacade.INSTANCE
								.setShippingAddress(cleansedShipAddress);
					} catch (BackendException e) {
						shippingBillingWindow
								.displayError("New shipping address not saved. Message: "
										+ e.getMessage());

					}
				}else{
					CheckoutData data = CheckoutData.INSTANCE;
					ShoppingCartSubsystemFacade.INSTANCE.setShippingAddress(data.getDefaultShippingData());
				}
				
				if (cleansedBillAddress != null) {
					try {

						CheckoutController.INSTANCE
								.saveNewAddress(cleansedBillAddress);
						ShoppingCartSubsystemFacade.INSTANCE
								.setBillingAddress(cleansedBillAddress);
					} catch (BackendException e) {
						shippingBillingWindow
								.displayError("New billing address not saved. Message: "
										+ e.getMessage());

					}
				}else{
					CheckoutData data = CheckoutData.INSTANCE;
					ShoppingCartSubsystemFacade.INSTANCE.setBillingAddress(data.getDefaultBillingData());
				}

				paymentWindow = new PaymentWindow();
				paymentWindow.show();
				shippingBillingWindow.hide();
			}
		}
	}

	public ProceedToPaymentHandler getProceedToPaymentHandler() {
		return new ProceedToPaymentHandler();
	}

	public class SaveShipChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");
		}
	}

	public class SaveBillChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");

		}
	}

	public SaveShipChangeListener getSaveShipChangeListener() {
		return new SaveShipChangeListener();
	}

	public SaveBillChangeListener getSaveBillChangeListener() {
		return new SaveBillChangeListener();
	}

	// handlers for PaymentWindow
	private class BackToShipBillWindow implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			shippingBillingWindow.show();
			paymentWindow.hide();
		}
	}

	public BackToShipBillWindow getBackToShipBillWindow() {
		return new BackToShipBillWindow();
	}

	private class BackToCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			ShoppingCartWindow.INSTANCE.show();
			paymentWindow.hide();
		}
	}

	public BackToCartHandler getBackToCartHandler() {
		return new BackToCartHandler();
	}

	private class ProceedToTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			try {
				LOG.info("Now Running Payment Rules");
				CheckoutController.INSTANCE.runPaymentRules(
						shippingBillingWindow.getBillingAddress(),
						paymentWindow.getCreditCardFromWindow());
				CheckoutController.INSTANCE.runPaymentRules(
						shippingBillingWindow.getShippingAddress(),
						paymentWindow.getCreditCardFromWindow());
				LOG.info("Finished Running Payment Rules");
				ShoppingCartSubsystemFacade.INSTANCE
						.setPaymentInfo(paymentWindow.getCreditCardFromWindow());
				paymentWindow.clearMessages();
				paymentWindow.hide();
				termsWindow = new TermsWindow();
				termsWindow.show();
			} catch (RuleException e) {
				paymentWindow.displayError(e.getMessage());
			} catch (BusinessException e) {
				paymentWindow.displayError(ErrorMessages.DATABASE_ERROR);
			}
		}
	}

	public ProceedToTermsHandler getProceedToTermsHandler() {
		return new ProceedToTermsHandler();
	}

	// handlers for TermsWindow

	private class ToCartFromTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			termsWindow.hide();
			ShoppingCartWindow.INSTANCE.show();
		}
	}

	public ToCartFromTermsHandler getToCartFromTermsHandler() {
		return new ToCartFromTermsHandler();
	}

	private class AcceptTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			finalOrderWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			finalOrderWindow.show();
			termsWindow.hide();
		}
	}

	public AcceptTermsHandler getAcceptTermsHandler() {
		return new AcceptTermsHandler();
	}

	// handlers for FinalOrderWindow
		private class SubmitHandler implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent evt) {
				try {

					boolean isRuleOk = true;
					try {
						CheckoutController.INSTANCE
								.runFinalOrderRules(ShoppingCartSubsystemFacade.INSTANCE
										.getLivCart());
					} catch (RuleException e) {
						isRuleOk = false;
						finalOrderWindow.displayError(e.getMessage());
					} catch (BusinessException e) {
						isRuleOk = false;
						finalOrderWindow.displayError(e.getMessage());
					}
					if (isRuleOk) {
						CheckoutController.INSTANCE.submitFinalOrder();
						orderCompleteWindow = new OrderCompleteWindow();
						orderCompleteWindow.show();
						finalOrderWindow.clearMessages();
						finalOrderWindow.hide();
						ShoppingCartWindow.INSTANCE.ClearCartItems();
					}
				} catch (BackendException e) {
					LOG.setLevel(Level.SEVERE);
				}
				
			}

		}


	public SubmitHandler getSubmitHandler() {
		return new SubmitHandler();
	}

	private class CancelOrderHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow.displayInfo("Order Cancelled");
		}
	}

	public CancelOrderHandler getCancelOrderHandler() {
		return new CancelOrderHandler();
	}

	private class ToShoppingCartFromFinalOrderHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			finalOrderWindow.hide();
			finalOrderWindow.clearMessages();
		}
	}

	public ToShoppingCartFromFinalOrderHandler getToShoppingCartFromFinalOrderHandler() {
		return new ToShoppingCartFromFinalOrderHandler();
	}

	// handlers for OrderCompleteWindow

	private class ContinueFromOrderCompleteHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			//CatalogListWindow.getInstance().show();
			CatalogListWindow window;
			try {
				window = CatalogListWindow.getInstance(null, 
						FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
				window.clearMessages();
				window.show();
				orderCompleteWindow.hide();
			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				 
			
		}
	}

	public ContinueFromOrderCompleteHandler getContinueFromOrderCompleteHandler() {
		return new ContinueFromOrderCompleteHandler();
	}
}
