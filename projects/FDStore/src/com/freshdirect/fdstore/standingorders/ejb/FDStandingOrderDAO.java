package com.freshdirect.fdstore.standingorders.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDStandingOrderDAO {
	
	private static final Logger LOGGER = LoggerFactory.getInstance( FDStandingOrderDAO.class );

	private static final String FIELDZ_ALL = "SO.ID, SO.CUSTOMER_ID, SO.CUSTOMERLIST_ID, SO.ADDRESS_ID, SO.PAYMENTMETHOD_ID, SO.START_TIME, SO.END_TIME, SO.NEXT_DATE, SO.FREQUENCY, SO.ALCOHOL_AGREEMENT, SO.DELETED, SO.LAST_ERROR, SO.ERROR_HEADER, SO.ERROR_DETAIL, CCL.NAME";

	private static final String LOAD_CUSTOMER_STANDING_ORDERS =
		"select " + FIELDZ_ALL + " " +
		"from CUST.STANDING_ORDER SO " +
		"left join CUST.CUSTOMERLIST CCL on(CCL.id = SO.CUSTOMERLIST_ID) " +
		"where CCL.CUSTOMER_ID = ? and SO.DELETED<>1 " +
		"order by CCL.NAME";

	private static final String LOAD_ACTIVE_STANDING_ORDERS =
		"select " + FIELDZ_ALL + " " +
		"from CUST.STANDING_ORDER SO " +
		"left join CUST.CUSTOMERLIST CCL on(CCL.id = SO.CUSTOMERLIST_ID) " +
		"where SO.DELETED<>1 and NAME='GS Test' " +
		"order by CCL.NAME";

	private static final String CREATE_EMPTY_STANDING_ORDER = "INSERT INTO CUST.STANDING_ORDER(ID, CUSTOMER_ID, CUSTOMERLIST_ID) VALUES(?,?,?)";
	
	private static final String LOAD_STANDING_ORDER =
		"select " + FIELDZ_ALL + " " +
		"from CUST.STANDING_ORDER SO " +
		"left join CUST.CUSTOMERLIST CCL on(CCL.id = SO.CUSTOMERLIST_ID) " +
		"where SO.ID=?";

	private static final String INSERT_STANDING_ORDER = "insert into CUST.STANDING_ORDER(ID, CUSTOMER_ID, CUSTOMERLIST_ID, ADDRESS_ID, PAYMENTMETHOD_ID, START_TIME, END_TIME, NEXT_DATE, FREQUENCY, ALCOHOL_AGREEMENT, DELETED, LAST_ERROR, ERROR_HEADER, ERROR_DETAIL) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_STANDING_ORDER = "update CUST.STANDING_ORDER set " +
	"CUSTOMER_ID = ?, " +
	"CUSTOMERLIST_ID = ?, " +
	"ADDRESS_ID = ?, " +
	"PAYMENTMETHOD_ID = ?, " +
	"START_TIME = ?, " +
	"END_TIME = ?, " +
	"NEXT_DATE = ?, " +
	"FREQUENCY = ?, " +
	"ALCOHOL_AGREEMENT = ?, " +
	"DELETED = ?, " +
	"LAST_ERROR = ?, " +
	"ERROR_HEADER = ?, " +	
	"ERROR_DETAIL = ? " +	
	"where ID = ?";
	
	private static final String DELETE_STANDING_ORDER = "update CUST.STANDING_ORDER SET DELETED=1, CUSTOMERLIST_ID=NULL where ID=? and DELETED=0";
	private static final String DELETE_CUSTOMER_LIST = "delete from CUST.CUSTOMERLIST where ID=?";
	private static final String DELETE_CUSTOMER_LIST_DETAILS = "delete from CUST.CUSTOMERLIST_DETAILS where LIST_ID = ?";
	
	private static final String ASSIGN_SO_TO_SALE = "UPDATE CUST.SALE SET STANDINGORDER_ID=? WHERE ID=?";
	
	private static final String DELETED_LIST_NAME = "Deleted Standing Order";
	
	protected String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextId(conn, "CUST");
	}
	
	
	public String createEmptyStandingOrder(Connection conn, String customerPK, String customerListPK) throws SQLException {
		if (customerPK == null || customerListPK == null)
			return null;
		
		PreparedStatement ps = null;
		boolean flag = false;
		String myId = null;

		try {
			ps = conn.prepareStatement(CREATE_EMPTY_STANDING_ORDER);
			
			myId = getNextId(conn);
			
			ps.setString(1, myId);
			ps.setString(2, customerPK);
			ps.setString(3, customerListPK);

			flag = ps.execute();
			
			ps.close();

			return flag ? myId : null;
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}

	
	/**
	 * Load standing orders
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public Collection<FDStandingOrder> loadActiveStandingOrders(Connection conn) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FDStandingOrder> sorders = new ArrayList<FDStandingOrder>();

		try {
			ps = conn.prepareStatement(LOAD_ACTIVE_STANDING_ORDERS);

			rs = ps.executeQuery();
			
			while (rs.next()) {
				FDStandingOrder so = new FDStandingOrder();
				
				sorders.add( populate(rs, so) );
			}

			rs.close();
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		
		return sorders;
	}


	
	/**
	 * Returns customer's active standing orders
	 * 
	 * @param conn
	 * @param identity
	 * @return
	 * @throws SQLException
	 */
	public Collection<FDStandingOrder> loadCustomerStandingOrders(Connection conn, FDIdentity identity) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<FDStandingOrder> sorders;
		try {
			ps = conn.prepareStatement(LOAD_CUSTOMER_STANDING_ORDERS);
			ps.setString(1, identity.getErpCustomerPK());

			sorders = new ArrayList<FDStandingOrder>();
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				FDStandingOrder so = new FDStandingOrder();
				
				sorders.add( populate(rs, so) );
			}

			rs.close();
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		
		return sorders;
	}

	
	/**
	 * Load a single standing order record
	 * 
	 * @param conn
	 * @param pk
	 * @return
	 * @throws SQLException
	 */
	public FDStandingOrder load(Connection conn, String pk) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		FDStandingOrder so = null;
		
		try {
			ps = conn.prepareStatement(LOAD_STANDING_ORDER);
			ps.setString(1, pk);

			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				so = new FDStandingOrder();
				
				populate(rs, so);
			}

			rs.close();
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		
		return so;
	}

	private FDStandingOrder populate( ResultSet rs, FDStandingOrder so ) throws SQLException {
		
		so.setId(rs.getString("ID"));

		so.setCustomerId( rs.getString("CUSTOMER_ID") );
		so.setCustomerListId( rs.getString("CUSTOMERLIST_ID") );
		so.setAddressId( rs.getString("ADDRESS_ID") );
		so.setPaymentMethodId( rs.getString("PAYMENTMETHOD_ID") );
		
		so.setStartTime( rs.getTime("START_TIME") );
		so.setEndTime( rs.getTime("END_TIME") );
		so.setNextDeliveryDate( rs.getDate("NEXT_DATE") );
		so.setPreviousDeliveryDate(so.getNextDeliveryDate());
		
		so.setFrequency( rs.getInt("FREQUENCY") );
		so.setAlcoholAgreement( rs.getBoolean("ALCOHOL_AGREEMENT") );
		
		so.setDeleted( rs.getBoolean("DELETED") );

		so.setLastError( rs.getString( "LAST_ERROR" ), rs.getString( "ERROR_HEADER" ), rs.getString( "ERROR_DETAIL" ) );
		
		String listName = rs.getString( "NAME" );
		if ( listName == null )
			listName = DELETED_LIST_NAME;
		so.setCustomerListName( listName );

		return so;
	}
	
	
	public void deleteStandingOrder( Connection conn, String soPk, String listPk ) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement( DELETE_STANDING_ORDER );
		ps.setString( 1, soPk );
		ps.executeUpdate();
		ps.close();
		
		ps = conn.prepareStatement( DELETE_CUSTOMER_LIST_DETAILS );
		ps.setString( 1, listPk );
		ps.executeUpdate();		
		ps.close();
		
		ps = conn.prepareStatement( DELETE_CUSTOMER_LIST );
		ps.setString( 1, listPk );
		ps.executeUpdate();		
		ps.close();
	}
	
	public String createStandingOrder(Connection conn, FDStandingOrder so) throws SQLException {
		LOGGER.debug( "FDStandingOrderDAO.createStandingOrder()" );

		String myId = null;
		PreparedStatement ps = null; 
		
		try {
			myId = getNextId(conn);

			ps = conn.prepareStatement(INSERT_STANDING_ORDER);
			
			final Date startDate = so.getStartTime();
			final Date endDate = so.getEndTime();
			final Date nextDeliveryDate = so.getNextDeliveryDate();

			int counter = 1;
			ps.setString(counter++, myId);
			ps.setString(counter++, so.getCustomerId());
			ps.setString(counter++, so.getCustomerListId());
			ps.setString(counter++, so.getAddressId());
			ps.setString(counter++, so.getPaymentMethodId());
			ps.setTime(counter++, startDate != null ? new java.sql.Time( startDate.getTime() ) : null );
			ps.setTime(counter++, endDate != null ? new java.sql.Time( endDate.getTime() ) : null);
			ps.setDate(counter++, nextDeliveryDate != null ? new java.sql.Date( nextDeliveryDate.getTime() ) : null);
			ps.setInt(counter++, so.getFrequency());
			ps.setBoolean(counter++, so.isAlcoholAgreement());
			ps.setBoolean(counter++, so.isDeleted());
			ps.setString(counter++, so.getLastError() == null ? null : so.getLastError().name());
			ps.setString(counter++, so.getErrorHeader());
			ps.setString(counter++, so.getErrorDetail());
			
			ps.execute();
			
			ps.close();

			so.setId(myId);
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
		
		return myId;
	}

	public void updateStandingOrder(Connection conn, FDStandingOrder so) throws SQLException {
		LOGGER.debug( "FDStandingOrderDAO.updateStandingOrder()" );
		
		if (so == null || so.getId() == null)
			return;
				
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_STANDING_ORDER);
			int counter = 1;
			ps.setString(counter++, so.getCustomerId());
			ps.setString(counter++, so.getCustomerListId());
			ps.setString(counter++, so.getAddressId());
			ps.setString(counter++, so.getPaymentMethodId());
			ps.setTime(counter++, so.getStartTime() != null ? new java.sql.Time( so.getStartTime().getTime() ) : null );
			ps.setTime(counter++, so.getEndTime() != null ? new java.sql.Time( so.getEndTime().getTime() ) : null);
			ps.setDate(counter++, so.getNextDeliveryDate() != null ? new java.sql.Date( so.getNextDeliveryDate().getTime() ) : null);
			ps.setInt(counter++, so.getFrequency());
			ps.setBoolean(counter++, so.isAlcoholAgreement());
			ps.setBoolean(counter++, so.isDeleted());
			ps.setString(counter++, so.getLastError() == null ? null : so.getLastError().name());
			ps.setString(counter++, so.getErrorHeader());
			ps.setString(counter++, so.getErrorDetail());

			ps.setString(counter++, so.getId());
			
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}
	

	public void assignToSale(Connection conn, String soPK, String salePK ) throws SQLException {
		
		LOGGER.debug( "assigning SO["+soPK+"] to SALE["+salePK+"]" );
		
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(ASSIGN_SO_TO_SALE);
		
			ps.setString(1, soPK);
			ps.setString(2, salePK);
			
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}
	
	
	private static final String GET_ACTIVE_STANDING_ORDERS_CUST_INFO =
		"select  so.id,c.user_id,A.COMPANY_NAME,SO.NEXT_DATE ,SO.CUSTOMER_ID,SO.FREQUENCY, SO.ERROR_HEADER,SO.LAST_ERROR,SO.START_TIME,SO.END_TIME," +
		" A.ADDRESS1||', '||a.ADDRESS2||', '||a.APARTMENT||', '||a.CITY||', '||a.STATE||', '||a.ZIP as address," +
		" NVL(CI.BUSINESS_PHONE||'-'||CI.BUSINESS_EXT,'') as BUSINESS_PHONE,NVL(CI.CELL_PHONE,'') as CELL_PHONE" +
		" from cust.address a, cust.customerinfo ci,cust.customer c,CUST.STANDING_ORDER so " +
		" where SO.ADDRESS_ID=a.id(+) and c.id=ci.customer_id and so.customer_id=c.id  ";
	

	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(Connection conn,FDStandingOrderFilterCriteria filter) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		List<FDStandingOrderInfo> soOrders = new ArrayList<FDStandingOrderInfo>();
		FDStandingOrderInfoList infoList = new FDStandingOrderInfoList(soOrders);
	
		try {
			/*String query = 
				"select  so.id,c.user_id,A.COMPANY_NAME,SO.NEXT_DATE ,SO.CUSTOMER_ID,SO.FREQUENCY, SO.ERROR_HEADER,SO.LAST_ERROR,SO.START_TIME,SO.END_TIME," +
				" A.ADDRESS1||', '||a.ADDRESS2||', '||a.APARTMENT||', '||a.CITY||', '||a.STATE||', '||a.ZIP as address," +
				" NVL(CI.BUSINESS_PHONE||'-'||CI.BUSINESS_EXT,'') as BUSINESS_PHONE,NVL(CI.CELL_PHONE,'') as CELL_PHONE" +
				" from cust.address a, cust.customerinfo ci,cust.customer c,CUST.STANDING_ORDER so " +
				" where SO.ADDRESS_ID=a.id(+) and c.id=ci.customer_id and so.customer_id=c.id  ";*/
//			ps = conn.prepareStatement(GET_ACTIVE_STANDING_ORDERS_CUST_INFO);
			
			CriteriaBuilder builder = new CriteriaBuilder();
			boolean isActiveOnly = true;
			if(null!=filter){
				if(filter.getFrequency()!=null){
					builder.addObject("SO.FREQUENCY", filter.getFrequency());
				}
				if(null !=filter.getErrorType() && !"".equals(filter.getErrorType().trim())){
					builder.addString("SO.LAST_ERROR", filter.getErrorType());
				}
				if(null !=filter.getDayOfWeek()){
					builder.addObject(" to_char(SO.NEXT_DATE,'D')", filter.getDayOfWeek());
				}
				if(true == filter.isActiveOnly()){
					builder.addObject(" SO.DELETED", "0");
				}else{
					isActiveOnly = false;
				}
			}
//			ps = conn.prepareStatement(query);
			Object[] par = builder.getParams();
			if(null != par && par.length > 0){
				ps = conn.prepareStatement(GET_ACTIVE_STANDING_ORDERS_CUST_INFO+" and "+builder.getCriteria());
			}else{
				if(isActiveOnly){
					ps = conn.prepareStatement(GET_ACTIVE_STANDING_ORDERS_CUST_INFO+ " and SO.DELETED= 0");
				}else{
					ps = conn.prepareStatement(GET_ACTIVE_STANDING_ORDERS_CUST_INFO);
				}
			}
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}
			rs = ps.executeQuery();
			
			while (rs.next()) {
				FDStandingOrderInfo soInfo = new FDStandingOrderInfo();
				soInfo.setSoID(rs.getString("ID"));
				soInfo.setUserId(rs.getString("USER_ID"));
				soInfo.setCompanyName(rs.getString("COMPANY_NAME"));
				soInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
				soInfo.setAddress(rs.getString("ADDRESS"));
				soInfo.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
				soInfo.setCellPhone(rs.getString("CELL_PHONE"));
				soInfo.setNextDate( rs.getDate("NEXT_DATE") );
				soInfo.setFrequency(rs.getInt("FREQUENCY"));
				soInfo.setLastError(rs.getString("LAST_ERROR"));
				soInfo.setErrorHeader(rs.getString("ERROR_HEADER"));
				soInfo.setStartTime(rs.getTime("START_TIME"));
				soInfo.setEndTime(rs.getTime("END_TIME"));
				soOrders.add( soInfo );
			}
	
			rs.close();
			ps.close();
		} catch (SQLException exc) {
			throw exc;
		} finally {
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		
		return infoList;
	}


	private static final String CLEAR_ERRORS_STANDING_ORDERS =
		"UPDATE CUST.STANDING_ORDER SO SET SO.LAST_ERROR = null, SO.ERROR_HEADER=null, SO.ERROR_DETAIL= null WHERE ";
	public void clearStandingOrderErrors(Connection conn,String[] soIDs) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		CriteriaBuilder builder = new CriteriaBuilder();
		try {
			
			builder.addInString("SO.ID", soIDs);
			
			Object[] par = builder.getParams();
			if(null != par && par.length > 0){
				ps = conn.prepareStatement(CLEAR_ERRORS_STANDING_ORDERS+builder.getCriteria());
			}
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}
			int total = ps.executeUpdate();			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
				if(rs != null){
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			}
	}
}