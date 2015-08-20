package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpInventoryManagerSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpInventoryManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public void updateInventories(List inventories) {
		ErpInventoryHome inventoryHome = this.getInventoryHome();

		UserTransaction utx = null;

		for (Iterator i = inventories.iterator(); i.hasNext();) {

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();

				ErpInventoryModel inv = (ErpInventoryModel) i.next();
				LOGGER.debug("Updating inventory " + inv);
				try {

					ErpInventoryEB eb = inventoryHome.findByPrimaryKey(new PrimaryKey(inv.getSapId()));
					ErpInventoryModel inventoryModel =(ErpInventoryModel) eb.getModel();
					List<ErpInventoryEntryModel> currInvEntries = inventoryModel.getEntries();
					List<ErpInventoryEntryModel> newInvEntries = new ArrayList<ErpInventoryEntryModel>();
					Set<String> newInvPlants = new HashSet<String>();
					newInvEntries.addAll(inv.getEntries());
					for (Iterator iterator = newInvEntries.iterator(); iterator.hasNext();) {
						ErpInventoryEntryModel newInvEntryModel = (ErpInventoryEntryModel) iterator.next();
						newInvPlants.add(newInvEntryModel.getPlantId());
					}
					if(null !=currInvEntries){
						for (ErpInventoryEntryModel currInvEntryModel : currInvEntries) {
							if( !newInvPlants.contains(currInvEntryModel.getPlantId())/* && !newInvEntries.contains(currInvEntryModel)*/){
								newInvEntries.add(currInvEntryModel);							
							}
						}
					}
					
					// update the inventory cache in the persistent store					
					eb.setEntries(new Date(), newInvEntries);

				} catch (ObjectNotFoundException ex) {
					// not found, create inventory
					inventoryHome.create(inv);
				}

				utx.commit();

			} catch (Exception e) {
				LOGGER.warn("Exception occured while storing inventory", e);
				if (utx != null)
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
				// just keep going :)
			}
		}

	}

	public void updateRestrictedInfos(Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials) {
		Connection conn = null;
		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			conn = this.getConnection();
			processRestrictedEntries(conn, restrictedInfos, deletedMaterials);
			utx.commit();
		} catch (Exception e) {
			LOGGER.warn("Exception occured while storing restriction info", e);
			if (utx != null)
				try {
					utx.setRollbackOnly();
		            if (conn != null) {
		                try {
		                    conn.close();
		                } catch (SQLException sqle2) {
		                    LOGGER.error("ErpInventoryManagerSessionBean.updateRestrictedInfos - Unable to close the connection.", sqle2);
		                    throw new EJBException(sqle2);
		                }
		            }
					utx.rollback();
				} catch (SystemException se) {
					LOGGER.warn("Error while trying to rollback transaction", se);
				}
				throw new EJBException("An error occurred while storing the availability restriction Info.",e);
		} finally {
            try {
            	if (conn != null && !conn.isClosed()) {
                    conn.close();
            	}
            } catch (SQLException sqle2) {
                LOGGER.error("ErpInventoryManagerSessionBean.updateRestrictedInfos - Unable to close the connection.", sqle2);
                throw new EJBException(sqle2);
            }
		}
	}
	
	private final String DELETE_RESTRICTED_ENTRIES = "delete from ERPS.AVAILABILITY_DELIVERY_DATES where MATERIAL_SAP_ID IN (";
	
	private final String INSERTED_RESTRICTED_ENTRIES = "insert into ERPS.AVAILABILITY_DELIVERY_DATES(MATERIAL_SAP_ID, DATE_AVAILABLE) VALUES (?,?)";
			
	
	private void processRestrictedEntries(Connection conn, Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials) throws SQLException{
		PreparedStatement ps = null;
		try{
			StringBuffer buf = new StringBuffer(DELETE_RESTRICTED_ENTRIES);
			int i = 0;
			int size= deletedMaterials != null ? deletedMaterials.size() : 0;
			if(size > 0) {			
				for(Iterator<String> it = deletedMaterials.iterator(); it.hasNext();){
					String matNo =  it.next();
					buf.append("?");
					if(i < size -1) {
						buf.append(",");
					} else {
						buf.append(")");
					}
					i++;
				}

				//Delete all deleted entries in SAP.
				conn = this.getConnection();
				LOGGER.debug("Delete Statement $$$$ "+buf.toString());
				ps = conn.prepareStatement(buf.toString());
				int j = 1;
				for(Iterator<String> it = deletedMaterials.iterator(); it.hasNext();){
					String matNo =  it.next();
					ps.setString(j, matNo);
					j++;
				}
				ps.executeUpdate();
				ps.close();
			}
			if(restrictedInfos != null && restrictedInfos.size() > 0) {
				//Insert all inserted/updates entries in SAP.
				ps = conn.prepareStatement(INSERTED_RESTRICTED_ENTRIES);
				for(Iterator<ErpRestrictedAvailabilityModel> it = restrictedInfos.iterator(); it.hasNext();){
					ErpRestrictedAvailabilityModel model = it.next();
					ps.setString(1, model.getMaterialNumber());
					ps.setDate(2, new java.sql.Date(model.getRestrictedDate().getTime()));
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}catch(SQLException se){
            throw se;
		}finally {
			if(ps != null) ps.close();
		}
	}
	
	private ErpInventoryHome getInventoryHome() {
		try {
			return (ErpInventoryHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpInventory");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
