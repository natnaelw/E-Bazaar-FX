package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassOrderForTest extends DbClass {
	public List<Order> readAllOrders(CustomerProfile custProfile) throws DatabaseException;
}
