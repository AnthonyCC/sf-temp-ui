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

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
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
		"where SO.DELETED<>1 and LAST_ERROR IS NULL " +
		"order by CCL.NAME";

	private static final String CREATE_EMPTY_STANDING_ORDER = "INSERT INTO CUST.STANDING_ORDER(ID, CUSTOMER_ID, CUSTOMERLIST_ID) VALUES(?,?,?)";
	
	private static final String LOAD_STANDING_ORDER =
		"select " + FIELDZ_ALL + " " +
		"from CUST.STANDING_ORDER SO " +
		"left join CUST.CUSTOMERLIST CCL on(CCL.id = SO.CUSTOMERLIST_ID) " +
		"where SO.ID=?";

	private static final String INSERT_STANDING_ORDER = "insert into CUST.STANDING_ORDER(ID, CUSTOMER_ID, CUSTOMERLIST_ID, ADDRESS_ID, PAYMENTMETHOD_ID, START_TIME, END_TIME, NEXT_DATE, FREQUENCY, ALCOHOL_AGREEMENT, DELETED, LAST_ERROR, ERROR_HEADER, ERROR_DETAIL) " +
	"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
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
	"ERROR_HEADER = ? " +	
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
}
