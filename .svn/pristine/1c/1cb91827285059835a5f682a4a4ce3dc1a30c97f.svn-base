package business.externalinterfaces;

import java.util.List;

import business.customersubsystem.CreditCardImpl;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassPaymentInfoForTest extends DbClass{
	public CreditCardImpl getdefaultPaymentInfo();
	public void readDefaultPayment(CustomerProfile custProfile) throws DatabaseException;
}
