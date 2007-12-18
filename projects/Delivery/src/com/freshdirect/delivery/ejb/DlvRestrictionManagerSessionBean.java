package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvRestrictionManagerSessionBean  extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionManagerSessionBean.class);
	
	public RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getDlvRestriction(conn,restrictionId);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}

	public List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getDlvRestrictions(conn,dlvReason,dlvType,dlvCriterion);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	
	public RestrictedAddressModel getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException, RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getAddressRestriction(conn,address1,apartment,zipCode);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	public void addDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.insertDeliveryRestriction(conn,restriction);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	
	public void addAddressRestriction(RestrictedAddressModel restriction)  throws FDResourceException, RemoteException{
	Connection conn = null;
	try {
		conn = getConnection();
		DlvRestrictionDAO.insertAddressRestriction(conn,restriction);
	} catch (SQLException e) {
		LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
		throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
	} finally {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
			}
		}
	}
	}
	
	public void storeDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.updateDeliveryRestriction(conn,restriction);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	public void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode)  throws FDResourceException, RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.updateAddressRestriction(conn,restriction,address1,apartment,zipCode);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
		
	}
	
	public void deleteDlvRestriction(String restrictionId)  throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.deleteDeliveryRestriction(conn,restrictionId);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException, RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.deleteAddressRestriction(conn,address1,apartment,zipCode);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Address Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
		
	}
}
