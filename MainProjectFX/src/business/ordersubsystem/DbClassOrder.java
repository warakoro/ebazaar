
package business.ordersubsystem;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.ShoppingCart;



class DbClassOrder implements DbClass {
	private static final Logger LOG = 
		Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
    private String query;
    private String queryType;
    private final String GET_ORDER_ITEMS = "GetOrderItems";
    private final String GET_ORDER_IDS = "GetOrderIds";
    private final String GET_ORDER_DATA = "GetOrderData";
    private final String SUBMIT_ORDER = "SubmitOrder";
    private final String SUBMIT_ORDER_ITEM = "SubmitOrderItem";	
    private CustomerProfile custProfile;
    private Integer orderId;
    private List<Integer> orderIds;
    private List<OrderItem> orderItems;
    private OrderImpl orderData;
	private OrderItem orderItem;
    private Order order;  
    
    DbClassOrder(){}
    
    DbClassOrder(OrderImpl order){
        this.order = order;
    }
    
    DbClassOrder(OrderItem orderItem ){
        this.orderItem = orderItem;
    }
    
    DbClassOrder(CustomerProfile custProfile) {
    	this.custProfile = custProfile;
    }
    
    DbClassOrder(OrderImpl order, CustomerProfile custProfile){
        this(order);
        this.custProfile = custProfile;
    } 
    
    List<Integer> getAllOrderIds(CustomerProfile custProfile) throws DatabaseException {
        this.custProfile=custProfile;
        queryType = GET_ORDER_IDS;
        dataAccessSS.atomicRead(this);
        return Collections.unmodifiableList(orderIds);      
    }
    
    OrderImpl getOrderData(Integer orderId) throws DatabaseException {
    	queryType = GET_ORDER_DATA;
    	this.orderId=orderId;  
    	dataAccessSS.atomicRead(this);      	
        return orderData;
    }
    
    // Precondition: CustomerProfile has been set by the constructor
    void submitOrder(ShoppingCart shopCart) throws DatabaseException {
    	//implement
    }
	    
    
    /** This is part of the general submitOrder method */
	private Integer submitOrderData() throws DatabaseException {	
		queryType = SUBMIT_ORDER;
		//creation and release of connection handled by submitOrder
		return dataAccessSS.save();    
	}
	
	/** This is part of the general submitOrder method */
	private void submitOrderItem(OrderItem item) throws DatabaseException {
        this.orderItem = item;
        queryType=SUBMIT_ORDER_ITEM;
        //creation and release of connection handled by submitOrder
        dataAccessSS.save();        
	}
   
    public List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
        queryType = GET_ORDER_ITEMS;
        this.orderId=orderId;
    	dataAccessSS.atomicRead(this);
        return Collections.unmodifiableList(orderItems);        
    }
    
    public void buildQuery() {
        if(queryType.equals(GET_ORDER_ITEMS)) {
            buildGetOrderItemsQuery();
        }
        else if(queryType.equals(GET_ORDER_IDS)) {
            buildGetOrderIdsQuery();
        }
        else if(queryType.equals(GET_ORDER_DATA)) {
        	buildGetOrderDataQuery();
        }
        else if(queryType.equals(SUBMIT_ORDER)) {
            buildSaveOrderQuery();
        }
        else if(queryType.equals(SUBMIT_ORDER_ITEM)) {
            buildSaveOrderItemQuery();
        }
    }
    
	private void buildSaveOrderQuery(){
        Address shipAddr = order.getShipAddress();
        Address billAddr = order.getBillAddress();
        CreditCard cc = order.getPaymentInfo();
        query = "INSERT into Ord "+
        "(orderid, custid, shipaddress1, shipcity, shipstate, shipzipcode, billaddress1, billcity, billstate,"+
           "billzipcode, nameoncard,  cardnum,cardtype, expdate, orderdate, totalpriceamount)" +
        "VALUES(NULL," + custProfile.getCustId() + ",'"+
                  shipAddr.getStreet()+"','"+
                  shipAddr.getCity()+"','"+
                  shipAddr.getState()+"','"+
                  shipAddr.getZip()+"','"+
                  billAddr.getStreet()+"','"+
                  billAddr.getCity()+"','"+
                  billAddr.getState()+"','"+
                  billAddr.getZip()+"','"+
                  cc.getNameOnCard()+"','"+
                  cc.getCardNum()+"','"+
                  cc.getCardType()+"','"+
                  cc.getExpirationDate()+"','"+
                  order.getOrderDate()+"',"+
                  order.getTotalPrice()+")";       
    }
	
    private void buildSaveOrderItemQuery(){
    	//implement
        query = "";
    }

    private void buildGetOrderDataQuery() {
        query = "SELECT orderid, orderdate, totalpriceamount FROM Ord WHERE orderid = " + orderId;     
    }
    
    private void buildGetOrderIdsQuery() {
        query = "SELECT orderid FROM Ord WHERE custid = "+custProfile.getCustId();     
    }
    
    private void buildGetOrderItemsQuery() {
        query = "SELECT * FROM OrderItem WHERE orderid = "+orderId; 
    }
    
    private void populateOrderItems(ResultSet rs) throws DatabaseException, BackendException {
    	orderItems= new LinkedList<OrderItem>();
        try {
            while(rs.next()){
            	orderItem = OrderSubsystemFacade.createOrderItem( rs.getInt("productid"), rs.getInt("orderid"), rs.getInt("quantity"), rs.getInt("totalprice"));			   
                orderItems.add(orderItem);
            }
        }
        catch(SQLException e){
            throw new BackendException(e);
        }     	
      
    }
    
    private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
        orderIds = new LinkedList<Integer>();
        try {
            while(resultSet.next()){
                orderIds.add(resultSet.getInt("orderid"));
            }

        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }
    
    private void populateOrderData(ResultSet resultSet) throws DatabaseException, BackendException {  	
    	orderData = new OrderImpl();
    	try {
            while(resultSet.next()){
            	
            	orderData.setOrderId(resultSet.getInt("orderId"));
                orderData.setOrderItems(getOrderItems(resultSet.getInt("orderId")));
            	orderData.setDate(resultSet.getDate("orderData").toLocalDate());  
            	orderData.getTotalPrice();
            }
        }
        catch(SQLException e){
            throw new BackendException(e);
        }       
    }    
 
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        if(queryType.equals(GET_ORDER_ITEMS)){
            try {
				populateOrderItems(resultSet);
			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if(queryType.equals(GET_ORDER_IDS)){
            populateOrderIds(resultSet);
        }
        else if(queryType.equals(GET_ORDER_DATA)){
        	try {
				populateOrderData(resultSet);
			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }       
    }
    
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }
    
    public String getQuery() {
        return query;
    }
     
    public void setOrderId(Integer orderId){
        this.orderId = orderId;       
    }   
}
