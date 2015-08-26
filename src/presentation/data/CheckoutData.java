package presentation.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.gui.GuiConstants;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.usecasecontrol.BrowseAndSelectController;

public enum CheckoutData {
	INSTANCE;

	public Address createAddress(String street, String city, String state,
			String zip, boolean isShip, boolean isBill) {
		return CustomerSubsystemFacade.createAddress(street, city, state, zip,
				isShip, isBill);
	}

	public CreditCard createCreditCard(String nameOnCard,
			String expirationDate, String cardNum, String cardType) {
		return CustomerSubsystemFacade.createCreditCard(nameOnCard,
				expirationDate, cardNum, cardType);
	}

	// Customer Ship Address Data
	private ObservableList<CustomerPres> shipAddresses =loadShipAddresses();

	// Customer Bill Address Data
	private ObservableList<CustomerPres> billAddresses = loadBillAddresses();

	private ObservableList<CustomerPres> loadShipAddresses() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();

		List<CustomerPres> list = null;
		try {
			list = getAllAddresses()
					.stream()
					.filter(address -> address.isShippingAddress())
					.map(address -> new CustomerPres(cust.getCustomerProfile(),
							address)).collect(Collectors.toList());
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FXCollections.observableList(list);

		// List<CustomerPres> list = DefaultData.CUSTS_ON_FILE
		// .stream()
		// .filter(cust -> cust.getAddress().isBillingAddress())
		// .collect(Collectors.toList());
		// return FXCollections.observableList(list);
	}

	private ObservableList<CustomerPres> loadBillAddresses() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		List<CustomerPres> list = null;
		try {
			list = getAllAddresses()
					.stream()
					.filter(address -> address.isBillingAddress())
					.map(address -> new CustomerPres(cust.getCustomerProfile(),
							address)).collect(Collectors.toList());
		} catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FXCollections.observableList(list);

		// List list = DefaultData.CUSTS_ON_FILE
		// .stream()
		// .filter(cust -> cust.getAddress().isBillingAddress())
		// .collect(Collectors.toList());
		// return FXCollections.observableList(list);
	}

	public ObservableList<CustomerPres> getCustomerShipAddresses() {
		return shipAddresses;
	}

	public ObservableList<CustomerPres> getCustomerBillAddresses() {
		return billAddresses;
	}

	public List<String> getDisplayAddressFields() {
		return GuiConstants.DISPLAY_ADDRESS_FIELDS;
	}

	public List<String> getDisplayCredCardFields() {
		return GuiConstants.DISPLAY_CREDIT_CARD_FIELDS;
	}

	public List<String> getCredCardTypes() {
		return GuiConstants.CREDIT_CARD_TYPES;
	}

	public Address getDefaultShippingData() {
		// implement
		CustomerSubsystem defaultshippingAdress = DataUtil.readCustFromCache();
		return defaultshippingAdress.getDefaultShippingAddress();
		// return
		// CustomerSubsystemFacade.createAddress(defaultshippingAdress.getDefaultShippingAddress().getStreet(),
		// defaultshippingAdress.getDefaultShippingAddress().getCity(),
		// defaultshippingAdress.getDefaultShippingAddress().getState(),
		// defaultshippingAdress.getDefaultShippingAddress().getZip(), true,
		// false);
	}

	public Address getDefaultBillingData() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		return cust.getDefaultBillingAddress();
		// List<String> add = DefaultData.DEFAULT_BILLING_DATA;
		// return CustomerSubsystemFacade.createAddress(add.get(0), add.get(1),
		// add.get(2), add.get(3), false, true);
	}

	public List<String> getDefaultPaymentInfo() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		List<String> creditCardInfo = new ArrayList<>();
		creditCardInfo.add(cust.getDefaultPaymentInfo().getNameOnCard());
		creditCardInfo.add(cust.getDefaultPaymentInfo().getCardNum());
		creditCardInfo.add(cust.getDefaultPaymentInfo().getCardType());
		creditCardInfo.add(cust.getDefaultPaymentInfo().getExpirationDate());
		return creditCardInfo;
	}

	public List<Address> getAllAddresses() throws BackendException {
		// implement
		CustomerSubsystem defaultshippingAdress = DataUtil.readCustFromCache();
		return defaultshippingAdress.getAllAddresses();
		// return
		// CustomerSubsystemFacade.createAddress(defaultshippingAdress.getDefaultShippingAddress().getStreet(),
		// defaultshippingAdress.getDefaultShippingAddress().getCity(),
		// defaultshippingAdress.getDefaultShippingAddress().getState(),
		// defaultshippingAdress.getDefaultShippingAddress().getZip(), true,
		// false);
	}

	public CustomerProfile getCustomerProfile() {
		return BrowseAndSelectController.INSTANCE.getCustomerProfile();
	}

	private class ShipAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			shipAddresses = list;
		}
	}

	public ShipAddressSynchronizer getShipAddressSynchronizer() {
		return new ShipAddressSynchronizer();
	}

	private class BillAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			billAddresses = list;
		}
	}

	public BillAddressSynchronizer getBillAddressSynchronizer() {
		return new BillAddressSynchronizer();
	}

	public static class ShipBill {
		public boolean isShipping;
		public String label;
		public Synchronizer synch;

		public ShipBill(boolean shipOrBill, String label, Synchronizer synch) {
			this.isShipping = shipOrBill;
			this.label = label;
			this.synch = synch;
		}

	}

}
