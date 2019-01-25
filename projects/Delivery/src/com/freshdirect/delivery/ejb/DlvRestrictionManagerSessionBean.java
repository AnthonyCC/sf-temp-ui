package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *@deprecated Please use the DlvRestrictionController and DlvRestrictionManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class DlvRestrictionManagerSessionBean  extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionManagerSessionBean.class);
	

	public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode, String apartment) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvRestrictionDAO.isAlcoholDeliverable(conn, scrubbedAddress, zipcode, apartment);
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				LOGGER.warn("Exception while trying to cleanup", ex);
			}
		}
	}

	public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) {
		Connection conn = null;
		try {
			
			conn = this.getConnection();
			return DlvRestrictionDAO.isAddressRestricted(conn, address);
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				LOGGER.warn("Exception while trying to cleanup", ex);
			}
		}
	}


	
	public List<RestrictionI> getDlvRestrictions() throws FDResourceException {

		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getDlvRestrictions(conn);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			this.getSessionContext().setRollbackOnly();
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

	public AlcoholRestriction getAlcoholRestriction(String restrictionId, String municipalityId) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getAlcoholRestriction(conn, restrictionId, municipalityId);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Alcohol Restriction.");
			throw new FDResourceException(e, "Could not get the Alcohol Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Alcohol Restriction.", e);
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
	
	public String addAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.insertAlcoholRestriction(conn,restriction);
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

	public void storeAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.updateAlcoholRestriction(conn,restriction);
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
	
	public void deleteAlcoholRestriction(String restrictionId)  throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.deleteAlcoholRestriction(conn,restrictionId);
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
	
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException {
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
	
	public void setAlcoholRestrictedFlag(String municipalityId, boolean restricted)  throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvRestrictionDAO.updateAlcoholRestrictedFlag(conn, municipalityId, restricted);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while updating alcohol restriction flag.");
			throw new FDResourceException(e, "Could not set alcohol restriction flag due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after setting alcohol restriction flag.", e);
				}
			}
		}
	}
	
	public Map<String, List<String>> getMunicipalityStateCounties()throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getMunicipalityStateCounties(conn);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting municipality state and counties.");
			throw new FDResourceException(e, "Could not get municipality state and counties. due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after setting alcohol restriction flag.", e);
				}
			}
		}
	}
}
