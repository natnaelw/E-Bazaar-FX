package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassPaymentInfoForTest;

class DbClassPayment implements DbClass, DbClassPaymentInfoForTest{

	private static final Logger LOG = Logger.getLogger(DbClassAddress.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
    private CustomerProfile custProfile;
    private CreditCardImpl defaultPayInfo;
    private String queryType;
    private String query;

    private final String READ_DEFAULT_PAY = "ReadDefaultPay";
    
	//column names

    private final String NAME_ON_CARD = "nameoncard";
    private final String EXP_DATE = "expdate";
    private final String CARD_TYPE = "cardtype";
    private final String CARD_NUM = "cardnum";
	
    
    public void buildQuery() throws DatabaseException {
    	LOG.info("Query  for " + queryType + ": " + query);
        if(queryType.equals(READ_DEFAULT_PAY)){
        	buildReadDefaultPaymentQuery();
        }
    }
    
    
    CreditCardImpl getDefaultPayInfo(){
    	return this.defaultPayInfo;
    }
	
    
    public void readDefaultPayment(CustomerProfile custProfile) throws DatabaseException {
    	//implement    
    	this.custProfile = custProfile;
    	queryType = READ_DEFAULT_PAY;
    	dataAccessSS.atomicRead(this);
    }
        
    
  void buildReadDefaultPaymentQuery() {
    	
        query = "SELECT nameoncard, expdate, cardtype, cardnum "+
        "FROM Customer "+
        "WHERE custid = " + custProfile.getCustId();
    }
  
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    public String getQuery() {
        return query;
        
    }
     
    void populateDefaultPayInfo(ResultSet rs) throws DatabaseException {
    	try{
    	rs.next();
    	if(rs!=null){
    		defaultPayInfo = new CreditCardImpl(rs.getString(NAME_ON_CARD), rs.getString(EXP_DATE), 
    				                            rs.getString(CARD_NUM),rs.getString(CARD_TYPE));
    	}
    	}catch(SQLException e){
    	   throw new DatabaseException(e);	
    	}
    }
	
	
    /* used only when we read from the database
     */
    public void populateEntity(ResultSet rs) throws DatabaseException {
       if(queryType.equals(this.READ_DEFAULT_PAY)){
        	populateDefaultPayInfo(rs);
        }
    }


	@Override
	public CreditCardImpl getdefaultPaymentInfo() {
		return this.defaultPayInfo;
	}

}