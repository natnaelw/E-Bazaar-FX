
package business.usecasecontrol;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale.Category;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductImpl;
import business.productsubsystem.ProductSubsystemFacade;


public class ManageProductsController   {
       
	public static Product createProduct(Catalog c, Integer pi, String pn,
			int qa, double up, LocalDate md, String desc) {
		return  ProductSubsystemFacade.createProduct(c, pi, pn, qa, up, md, desc);
	}
    public List<Product> getProductsList(Catalog catalog) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	return pss.getProductList(catalog);    	
    }
    
    public Catalog getCalalogByName(String name) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	return pss.getCatalogFromName(name);
    	
    }
    
    public List<Catalog> getCalalogList() throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	return pss.getCatalogList();
    	
    }
    public void saveNewProduct( Product product)  throws BackendException{
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	pss.saveNewProduct(product);
    }
    public void updateProduct( Product product)  throws BackendException{
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	pss.updateProduct(product);
    }
    public void deleteProduct(Product product) throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	pss.deleteProduct(product);
    }
    public void saveNewCatalog(Catalog catalog)  throws BackendException{
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	pss.saveNewCatalog(catalog);
    }
    public void deleteCatalog(Catalog catalog)  throws BackendException {
    	ProductSubsystem pss = new ProductSubsystemFacade();    	
    	pss.deleteCatalog(catalog);
    }
    
}
