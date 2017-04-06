package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.SAPConstants;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.EnumProductApprovalStatus;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.ejb.ErpClassEB;
import com.freshdirect.erp.ejb.ErpClassHome;
import com.freshdirect.erp.ejb.ErpMaterialEB;
import com.freshdirect.erp.ejb.ErpMaterialHome;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A session bean that takes a set of anonymous models representing ERP objects
 * to be created and processes them in the correct order together in a single
 * batch (version).
 * 
 * @author kkanuganti
 */
public class SAPLoaderSessionBean extends SessionBeanSupport {

	private static final long serialVersionUID = -3595874586761397842L;

	/**
	 * logger for messages
	 */
	private static Category LOG = LoggerFactory.getInstance(SAPLoaderSessionBean.class);

	/** Creates new SAPLoaderSessionBean */
	public SAPLoaderSessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 * 
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.dataloader.sap.ejb.SAPLoaderHome";
	}

	/**
	 * naming context for locating remote objects
	 */
	Context initCtx = null;

	/**
	 * a cache of models of classes created during this batch
	 */
	// private transient HashMap<String, ErpClassModel> createdClasses = null;

	/**
	 * a cache of materials created during this batch
	 */
	// private transient ErpMaterialModel createdMaterial = null;

	/**
	 * @return int the batch version
	 * @throws LoaderException
	 */
	public int createBatch() throws LoaderException {
		// get the user transaction
		UserTransaction utx = getSessionContext().getUserTransaction();
		Connection conn = null;
		int batchNumber;
		try {
			utx.begin();

			// set a timeout period for this transaction (in seconds)
			utx.setTransactionTimeout(3000);

			// get the next batch number from the batch sequence
			try {
				conn = getConnection();
				// get a new batch number
				PreparedStatement ps = conn.prepareStatement("select erps.batch_seq.nextval from dual");
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					batchNumber = rs.getInt(1);
				} else {
					LOG.error("Unable to begin new batch.  Didn't get a new batch number.");
					throw new LoaderException("Unable to begin new batch.  Didn't get a new batch number.");
				}
				rs.close();
				ps.close();

				// make an entry in the history table
				ps = conn
						.prepareStatement("insert into erps.history (version, date_created, created_by, approval_status) values (?,?,?,?)");
				ps.setInt(1, batchNumber);
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.setString(3, "Loader");
				ps.setString(4, EnumApprovalStatus.LOADING.getStatusCode());
				int rowsaffected = ps.executeUpdate();
				if (rowsaffected != 1) {
					throw new LoaderException("Unable to begin new batch.  Couldn't update loader history table.");
				}
				rs.close();
				ps.close();

				conn.close();

				try {
					utx.commit();
				} catch (RollbackException re) {
					LOG.error(
							"Unable to start a batch.  UserTransaction had already rolled back before attempt to commit.",
							re);
					throw new LoaderException(re,
							"Unable to start a batch.  UserTransaction had already rolled back before attempt to commit.");
				} catch (HeuristicMixedException hme) {
					LOG.error("Unable to start a batch.  TransactionManager aborted due to mixed heuristics.", hme);
					throw new LoaderException(hme,
							"Unable to start a batch.  TransactionManager aborted due to mixed heuristics.");
				} catch (HeuristicRollbackException hre) {
					LOG.error("Unable to start a batch.  TransactionManager heuristically rolled back transaction.",
							hre);
					throw new LoaderException(hre,
							"Unable to start a batch.  TransactionManager heuristically rolled back transaction.");
				}

			} catch (SQLException sqle) {
				LOG.error("Unable to begin new batch.", sqle);
				utx.setRollbackOnly();
				close(conn);
				utx.rollback();
				throw new LoaderException("Unable to begin a new batch.  " + sqle.getMessage());
			}

		} catch (NotSupportedException nse) {
			LOG.error("Unable to complete a failed batch.  Unable to begin a UserTransaction.", nse);
			throw new LoaderException(nse, "Unable to start batch.  Unable to begin a UserTransaction.");
		} catch (SystemException se) {
			LOG.error("Unable to complete a failed batch.  Unable to begin a UserTransaction.", se);
			throw new LoaderException(se, "Unable to start batch.  Unable to begin a UserTransaction.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
					sqle2.printStackTrace();
					LOG.error(
							"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.",
							sqle2);
					throw new LoaderException(
							"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  "
									+ sqle2);
				}
			}
		}
		return batchNumber;
	}

	/**
	 * Makes an entry in the history table indicating that a batch succeeded and
	 * is ready for review
	 * 
	 * @param batchNumber
	 * @param batchStatus
	 * 
	 * @throws LoaderException
	 *             any unexpected errors updating the history table
	 */
	public void updateBatchStatus(int batchNumber, EnumApprovalStatus batchStatus) throws LoaderException {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement("update erps.history set approval_status=? where version=?");
			ps.setString(1, batchStatus.getStatusCode());
			ps.setInt(2, batchNumber);

			int rowsaffected = ps.executeUpdate();

			if (rowsaffected != 1) {
				throw new LoaderException(
						"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.");
			}
			ps.close();
			ps = null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			LOG.error(
					"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.",
					sqle);
			throw new LoaderException(
					"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  "
							+ sqle);
		} finally {
			close(conn);
		}
	}

	/**
	 * @return ErpMaterialBatchHistoryModel the batch model
	 * 
	 * @throws LoaderException
	 */
	public ErpMaterialBatchHistoryModel getMaterialBatchInfo() throws LoaderException {
		ErpMaterialBatchHistoryModel batchModel = null;

		UserTransaction utx = getSessionContext().getUserTransaction();
		Connection conn = null;
		try {
			utx.begin();
			utx.setTransactionTimeout(3000);

			try {
				conn = getConnection();

				PreparedStatement ps = conn
						.prepareStatement("select version, date_created, approval_status from erps.history where date_created = (select max(date_created) from erps.history) ");

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					batchModel = new ErpMaterialBatchHistoryModel();
					batchModel.setVersion(rs.getInt(1));
					batchModel.setCreatedDate(rs.getTimestamp(2));
					batchModel.setStatus(EnumApprovalStatus.getApprovalStatus(rs.getString(3)));
				}
				rs.close();
				ps.close();

				conn.close();

				try {
					utx.commit();
				} catch (RollbackException re) {
					LOG.error(
							"Unable to get batch details.  UserTransaction had already rolled back before attempt to commit.",
							re);
					throw new LoaderException(re,
							"Unable to get batch details.  UserTransaction had already rolled back before attempt to commit.");
				} catch (HeuristicMixedException hme) {
					LOG.error("Unable to get batch details.  TransactionManager aborted due to mixed heuristics.", hme);
					throw new LoaderException(hme,
							"Unable to start a batch.  TransactionManager aborted due to mixed heuristics.");
				} catch (HeuristicRollbackException hre) {
					LOG.error(
							"Unable to get batch details.  TransactionManager heuristically rolled back transaction.",
							hre);
					throw new LoaderException(hre,
							"Unable to get batch details.  TransactionManager heuristically rolled back transaction.");
				}

			} catch (SQLException sqle) {
				LOG.error("Unable to get batch details.", sqle);
				utx.setRollbackOnly();
				close(conn);
				utx.rollback();
				throw new LoaderException("Unable to get batch details.  " + sqle.getMessage());
			}

		} catch (NotSupportedException nse) {
			LOG.error("Unable to complete a failed batch.  Unable to begin a UserTransaction.", nse);
			throw new LoaderException(nse, "Unable to start batch.  Unable to begin a UserTransaction.");
		} catch (SystemException se) {
			LOG.error("Unable to complete a failed batch.  Unable to begin a UserTransaction.", se);
			throw new LoaderException(se, "Unable to start batch.  Unable to begin a UserTransaction.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
					sqle2.printStackTrace();
					LOG.error(
							"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.",
							sqle2);
					throw new LoaderException(
							"Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  "
									+ sqle2);
				}
			}
		}
		return batchModel;
	}

	/**
	 * performs the batch load. processes each of the objects in the correct
	 * order.
	 * 
	 * @param batchNumber
	 *            the batch number
	 * @param classes
	 *            the collection of classes to create or update in this batch
	 * @param material
	 *            the material to create in this batch
	 * @param characteristicValuePrices
	 *            the collection of characteristic value prices to create or
	 *            update in this batch
	 * @throws LoaderException
	 *             any problems encountered while creating or updating objects
	 *             in the system
	 */
	public void loadData(int batchNumber, ErpMaterialModel material, Map<String, ErpClassModel> classes,
			Map<ErpCharacteristicValuePriceModel, Map<String, String>> characteristicValuePrices)
			throws LoaderException {

		LOG.debug("Beginning to load data for material +");
		try {
			this.initCtx = new InitialContext();
			try {
				UserTransaction utx = getSessionContext().getUserTransaction();
				utx.setTransactionTimeout(3000);
				try {
					utx.begin();
					refillMaterialModel(material);
					Map<String, ErpClassModel> createdClasses = processClass(batchNumber, material, classes);
					material = processMaterial(batchNumber, material, createdClasses);
					processCharacteristicValuePrices(batchNumber, characteristicValuePrices, material, createdClasses);

					try {
						// try to commit all the changes together
						utx.commit();
						LOG.debug("Completed SAPLoaderSessionBean loadData");
					} catch (RollbackException re) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  UserTransaction had already rolled back before attempt to commit.",
								re);
						throw new LoaderException(re,
								"Unable to update ERPS objects.  UserTransaction had already rolled back before attempt to commit.");
					} catch (HeuristicMixedException hme) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  TransactionManager aborted due to mixed heuristics.",
								hme);
						throw new LoaderException(hme,
								"Unable to update ERPS objects.  TransactionManager aborted due to mixed heuristics.");
					} catch (HeuristicRollbackException hre) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.",
								hre);
						throw new LoaderException(hre,
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.");
					} catch (RuntimeException rune) {
						utx.setRollbackOnly();
						LOG.error("Unexpected runtime exception in SAPLoaderSessionBean loadData", rune);
						throw new LoaderException(rune, "Unexpected runtime exception");
					}

				} catch (LoaderException le) {
					utx.setRollbackOnly();
					LOG.error("Aborting SAPLoaderSessionBean loadData", le);
					utx.rollback();
					throw (le);
				}
			} catch (NotSupportedException nse) {
				LOG.error("Unable to update ERPS objects.  Unable to begin a UserTransaction.", nse);
				throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} catch (SystemException se) {
				LOG.error("Unable to update ERPS objects.  Unable to begin a UserTransaction.", se);
				throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} finally {
				// close the naming context
				try {
					this.initCtx.close();
				} catch (NamingException ne) {
					// don't need to re-throw this since the transaction has
					// already completed or failed
					LOG.warn("Had difficulty closing naming context after transaction had completed.  "
							+ ne.getMessage());
				}
			}
		} catch (NamingException ne) {
			LOG.error("Unable to get naming context to locate components required by the loader.", ne);
			throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
		}
	}

	/**
	 * @param classes
	 * @throws LoaderException
	 */
	private Map<String, ErpClassModel> processClass(int batchNumber, ErpMaterialModel material, Map<String, ErpClassModel> classes)
			throws LoaderException {
		// create a new HashMap of models of classes so that subsequent loader
		// steps can form the proper foreign key relationships in the database
		Map<String, ErpClassModel> createdClasses = new HashMap<String, ErpClassModel>();
		try {
			if (classes != null) {
				LOG.info("Starting to process " + classes.size() + " Classes for batch " + batchNumber);

				ErpClassHome classHome = (ErpClassHome) initCtx.lookup("java:comp/env/ejb/ErpClass");

				Iterator<String> classKeyIter = classes.keySet().iterator();
				while (classKeyIter.hasNext()) {
					ErpClassModel erpClsModel = classes.get(classKeyIter.next());
					populateMaterialSalesUnitChar(erpClsModel,material);
					erpClsModel = loadClass(batchNumber, classHome, erpClsModel);
					createdClasses.put(erpClsModel.getSapId(), erpClsModel);
				}
				LOG.info("Completed processing Classes for batch " + batchNumber + "");
			}
		} catch (NamingException ne) {
			throw new LoaderException(ne, "Unable to find home for ErpClass");
		} catch (CreateException ce) {
			throw new LoaderException(ce, "Unable to create a new version of an ErpClass");
		} catch (RemoteException re) {
			throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpClass");
		}
		return createdClasses;
	}

	/**
	 * @param batchNumber
	 * @param classHome
	 * @param erpClsModel
	 * @throws RemoteException
	 * @throws CreateException
	 */
	private ErpClassModel loadClass(int batchNumber, ErpClassHome classHome, ErpClassModel erpClsModel)
			throws RemoteException, CreateException {
		ErpClassEB erpClsEB = null;
		try {
			erpClsEB = classHome.findBySapIdAndVersion(erpClsModel.getSapId(), batchNumber);
			erpClsModel = (ErpClassModel) erpClsEB.getModel();
		} catch (FinderException e) {
			LOG.info("Creating Class " + erpClsModel.getSapId() + "");
			erpClsEB = classHome.create(batchNumber, erpClsModel);
			erpClsModel = (ErpClassModel) erpClsEB.getModel();
			LOG.info("Successfully created Class " + erpClsModel.getSapId() + ", id= " + erpClsModel.getPK().getId()
					+ "");
		}
		// add this model to the createdClasses HashMap, keyed by Name
		// createdClasses.put(erpClsModel.getSapId(), erpClsModel);
		return erpClsModel;
	}

	/**
	 * @param activeMaterials
	 * @throws LoaderException
	 * 
	 */
	@SuppressWarnings("unchecked")
	private ErpMaterialModel processMaterial(int batchNumber, ErpMaterialModel materialModel,
			Map<String, ErpClassModel> classMap) throws LoaderException {
		try {
			ErpMaterialHome materialHome = (ErpMaterialHome) initCtx.lookup("java:comp/env/ejb/ErpMaterial");

			if (classMap != null) {
				LinkedList<ErpClassModel> matlClasses = new LinkedList<ErpClassModel>();
				Iterator<String> matlClassIter = classMap.keySet().iterator();
				while (matlClassIter.hasNext()) {
					String className = matlClassIter.next();
					ErpClassModel erpClsModel = classMap.get(className);
					/*
					 * ErpClassModel erpClsModel =
					 * createdClasses.get(className); if (erpClsModel == null) {
					 * throw new LoaderException(
					 * "Unable to form an association between the material number "
					 * + materialModel.getSapId() + " and Class " + className);
					 * }
					 */
					matlClasses.add(erpClsModel);
				}
				materialModel.setClasses(matlClasses);
			}

//			refillMaterialModel(materialModel, materialHome);

			// create the new material
			LOG.info("Creating new material " + materialModel.getSapId() + ", version " + batchNumber);

			ErpMaterialEB erpMatlEB = materialHome.create(batchNumber, materialModel);
			materialModel = (ErpMaterialModel) erpMatlEB.getModel();

			LOG.info("Successfully created Material " + materialModel.getSapId() + ", id= "
					+ materialModel.getPK().getId() + "");

			// set the model to the createdMaterial object
			// createdMaterial = materialModel;
		} catch (NamingException ne) {
			throw new LoaderException(ne, "Unable to find home for ErpMaterial");
		} catch (CreateException ce) {
			throw new LoaderException(ce, "Unable to create a new version of an ErpMaterial");
		} catch (RemoteException re) {
			throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpMaterial");
		}
		return materialModel;
	}

	/**
	 * @param materialModel
	 * @param materialHome
	 * @throws RemoteException
	 */
	private void refillMaterialModel(ErpMaterialModel materialModel)
			throws NamingException,LoaderException {
		ErpMaterialHome materialHome = (ErpMaterialHome) initCtx.lookup("java:comp/env/ejb/ErpMaterial");

		// if previously existing product, make sure to carry forward any
		// hidden sales units, prices or plant materials
		ErpMaterialEB materialEB = null;
		try {
			materialEB = materialHome.findBySapId(materialModel.getSapId());
			ErpMaterialModel existingMaterialModel = (ErpMaterialModel) materialEB.getModel();

			// clone the salesunit, prices & plant materials from previous
			// version if any before creating new entry
			if (existingMaterialModel != null) {
				// sales units
				List<ErpSalesUnitModel> existingSalesUnitModels = existingMaterialModel.getSalesUnits();
				List<ErpSalesUnitModel> existingDisplaySalesUnits = existingMaterialModel.getDisplaySalesUnits();
				if (null != existingSalesUnitModels && !existingSalesUnitModels.isEmpty()) {
					materialModel.setSalesUnits(cloneSalesUnits(existingSalesUnitModels, existingDisplaySalesUnits));
				}

				// prices
				List<ErpMaterialPriceModel> existingPriceRows = existingMaterialModel.getPrices();
				if (null != existingPriceRows && !existingPriceRows.isEmpty()) {
					materialModel.setPrices(clonePrices(existingPriceRows));
				}

				// plant specific material
				List<ErpPlantMaterialModel> existingMaterialPlantModels = existingMaterialModel.getMaterialPlants();
				if (null != existingMaterialPlantModels && !existingMaterialPlantModels.isEmpty()) {
					materialModel.setMaterialPlants(clonePlantModels(existingMaterialPlantModels));
				}
				// sales area specific material availability info
				List<ErpMaterialSalesAreaModel> existinMaterialSalesAreaModels = existingMaterialModel
						.getMaterialSalesAreas();
				if (null != existinMaterialSalesAreaModels && !existinMaterialSalesAreaModels.isEmpty()) {
					materialModel.setMaterialSalesAreas(cloneSalesAreas(existinMaterialSalesAreaModels));
				}
			}
		} catch (FinderException e) {
			// No entry found for the material. Create new entry!
		} catch (RemoteException re) {
			throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpMaterial");
		}
	}

	/**
	 * @param existinMaterialSalesAreaModels
	 * @return
	 */
	private List<ErpMaterialSalesAreaModel> cloneSalesAreas(
			List<ErpMaterialSalesAreaModel> existinMaterialSalesAreaModels) {
		List<ErpMaterialSalesAreaModel> materialSalesAreaModels = null;
		if (existinMaterialSalesAreaModels != null && existinMaterialSalesAreaModels.size() > 0) {
			materialSalesAreaModels = new ArrayList<ErpMaterialSalesAreaModel>();
			for (ErpMaterialSalesAreaModel materialSalesAreaModel : existinMaterialSalesAreaModels) {
				materialSalesAreaModels.add(cloneMaterialSalesAreaModel(materialSalesAreaModel));
			}
		}
		return materialSalesAreaModels;
	}

	/**
	 * @param existingMaterialPlantModels
	 * @return
	 */
	private List<ErpPlantMaterialModel> clonePlantModels(List<ErpPlantMaterialModel> existingMaterialPlantModels) {
		List<ErpPlantMaterialModel> plantMaterials = null;
		if (existingMaterialPlantModels != null && existingMaterialPlantModels.size() > 0) {
			plantMaterials = new ArrayList<ErpPlantMaterialModel>();
			for (ErpPlantMaterialModel plantMaterialModel : existingMaterialPlantModels) {
				plantMaterials.add(clonePlantMaterialModel(plantMaterialModel));
			}
		}
		return plantMaterials;
	}

	/**
	 * @param model
	 * @return ErpPlantMaterialModel
	 */
	private ErpPlantMaterialModel clonePlantMaterialModel(ErpPlantMaterialModel model) {
		ErpPlantMaterialModel plantMaterialModel = null;
		if (model != null) {
			plantMaterialModel = new ErpPlantMaterialModel();
			plantMaterialModel.setPlantId(model.getPlantId());
			/*
			 * plantMaterialModel.setSalesOrg(model.getSalesOrg());
			 * plantMaterialModel.setDistChannel(model.getDistChannel());
			 * 
			 * plantMaterialModel.setUnavailabilityStatus(model.
			 * getUnavailabilityStatus());
			 * plantMaterialModel.setUnavailabilityReason
			 * (model.getUnavailabilityReason());
			 * plantMaterialModel.setUnavailabilityDate
			 * (model.getUnavailabilityDate());
			 */
			plantMaterialModel.setAtpRule(model.getAtpRule());
			plantMaterialModel.setBlockedDays(model.getBlockedDays());
			plantMaterialModel.setLeadTime(model.getLeadTime());
			plantMaterialModel.setDays_in_house(model.getDays_in_house());

			plantMaterialModel.setKosherProduction(model.isKosherProduction());
			plantMaterialModel.setPlatter(model.isPlatter());
			plantMaterialModel.setRating(model.getRating());
			plantMaterialModel.setSustainabilityRating(model.getSustainabilityRating());
			plantMaterialModel.setHideOutOfStock(model.isHideOutOfStock());
			plantMaterialModel.setNumberOfDaysFresh(model.getNumberOfDaysFresh());

		}
		return plantMaterialModel;
	}

	/**
	 * @param model
	 * @return ErpMaterialSalesAreaModel
	 */
	private ErpMaterialSalesAreaModel cloneMaterialSalesAreaModel(ErpMaterialSalesAreaModel model) {
		ErpMaterialSalesAreaModel materialSalesAreaModel = null;
		if (model != null) {
			materialSalesAreaModel = new ErpMaterialSalesAreaModel();
			materialSalesAreaModel.setSalesOrg(model.getSalesOrg());
			materialSalesAreaModel.setDistChannel(model.getDistChannel());
			materialSalesAreaModel.setUnavailabilityStatus(model.getUnavailabilityStatus());
			materialSalesAreaModel.setUnavailabilityReason(model.getUnavailabilityReason());
			materialSalesAreaModel.setUnavailabilityDate(null != model.getUnavailabilityDate() ? model
					.getUnavailabilityDate() : SAPConstants.THE_FUTURE);
			materialSalesAreaModel.setSkuCode(model.getSkuCode());
			materialSalesAreaModel.setDayPartSelling(model.getDayPartSelling());
			materialSalesAreaModel.setPickingPlantId(model.getPickingPlantId());
		}
		return materialSalesAreaModel;
	}

	/**
	 * @param priceRow
	 * @return ErpMaterialPriceModel
	 */
	private ErpMaterialPriceModel clonePriceRow(ErpMaterialPriceModel priceRow) {
		if (priceRow != null) {
			ErpMaterialPriceModel priceRowModel = new ErpMaterialPriceModel();

			priceRowModel.setSalesOrg(priceRow.getSalesOrg());
			priceRowModel.setDistChannel(priceRow.getDistChannel());
			priceRowModel.setPrice(priceRow.getPrice());
			priceRowModel.setPricingUnit(priceRow.getPricingUnit());
			priceRowModel.setScaleQuantity(priceRow.getScaleQuantity());
			priceRowModel.setScaleUnit(priceRow.getScaleUnit());
			priceRowModel.setSapId(priceRow.getSapId());
			priceRowModel.setSapZoneId(priceRow.getSapZoneId().length()<=6?"0000"+priceRow.getSapZoneId():priceRow.getSapZoneId());
			priceRowModel.setPromoPrice(priceRow.getPromoPrice());

			return priceRowModel;
		}
		return null;
	}

	/**
	 * 
	 * @param salesUnitModel
	 * @return ErpSalesUnitModel
	 */
	private ErpSalesUnitModel cloneSalesUnitModel(ErpSalesUnitModel salesUnitModel) {
		if (salesUnitModel != null) {
			ErpSalesUnitModel salesUnit = new ErpSalesUnitModel();

			salesUnit.setDenominator(salesUnitModel.getDenominator());
			salesUnit.setAlternativeUnit(salesUnitModel.getAlternativeUnit());
			salesUnit.setDescription(salesUnitModel.getDescription());
			salesUnit.setNumerator(salesUnitModel.getNumerator());
			salesUnit.setBaseUnit(salesUnitModel.getBaseUnit());
			salesUnit.setDisplayInd(salesUnitModel.isDisplayInd());

			salesUnit.setUnitPriceNumerator(salesUnitModel.getUnitPriceNumerator());
			salesUnit.setUnitPriceDenominator(salesUnitModel.getUnitPriceDenominator());
			salesUnit.setUnitPriceUOM(salesUnitModel.getUnitPriceUOM());
			salesUnit.setUnitPriceDescription(salesUnitModel.getUnitPriceDescription());

			return salesUnit;
		}
		return null;
	}

	/**
	 * @param characteristicValuePrices
	 * @throws LoaderException
	 */
	private void processCharacteristicValuePrices(int batchNumber,
			Map<ErpCharacteristicValuePriceModel, Map<String, String>> characteristicValuePrices,
			ErpMaterialModel createdMaterial, Map<String, ErpClassModel> createdClasses) throws LoaderException {
		try {
			if (characteristicValuePrices != null) {
				LOG.info("Processing " + characteristicValuePrices.size() + " characteristic value prices for batch "
						+ batchNumber);

				ErpCharacteristicValuePriceHome cvpHome = (ErpCharacteristicValuePriceHome) initCtx
						.lookup("java:comp/env/ejb/ErpCharacteristicValuePrice");

				Iterator<ErpCharacteristicValuePriceModel> cvpKeyIter = characteristicValuePrices.keySet().iterator();
				while (cvpKeyIter.hasNext()) {
					ErpCharacteristicValuePriceModel erpCvpModel = cvpKeyIter.next();
					Map<String, String> extraInfo = characteristicValuePrices.get(erpCvpModel);

					// find the material for this characteristic value price
					String materialNo = extraInfo.get(SAPConstants.MATERIAL_NUMBER);

					// If created material wasn't in the list of created
					// materials, this characteristic value price was exported
					// as part of a discontinued material.
					// don't insert this characteristic value price.

					if (createdMaterial != null && erpCvpModel.getPrice() > 0) {
						erpCvpModel.setMaterialId(createdMaterial.getPK().getId());

						// find the characteristic value for this characteristic
						// value price
						String charValueName = extraInfo.get("CHARACTERISTIC_VALUE");
						try {
							ErpClassModel erpClass = createdClasses.get(extraInfo.get("CLASS"));
							ErpCharacteristicModel erpCharacteristic = erpClass.getCharacteristic(extraInfo
									.get("CHARACTERISTIC_NAME"));

							ErpCharacteristicValueModel erpCharVal = erpCharacteristic
									.getCharacteristicValue(charValueName);
							erpCvpModel.setCharacteristicValueId(erpCharVal.getPK().getId());
						} catch (NullPointerException npe) {
							npe.printStackTrace();
							throw new LoaderException("Unable to form an association between CharacteristicValuePrice "
									+ erpCvpModel.getSapId() + " and CharacteristicValue " + charValueName);
						}

						
						// create the new characteristic value price
						LOG.info("Creating new Characteristic Value Price " + erpCvpModel.getSapId() + " -CharacteristicValue: "+charValueName);
						if(erpCvpModel.getPrice() > 0){
							ErpCharacteristicValuePriceEB erpCvpEB = cvpHome.create(batchNumber, erpCvpModel);
	
							// read back the model as a sanity check
							erpCvpModel = (ErpCharacteristicValuePriceModel) erpCvpEB.getModel();
							LOG.info("Successfully created CharacteristicValuePrice " + erpCvpModel.getSapId() + ", id= "
									+ erpCvpModel.getPK().getId() + "");
						}
					}
					LOG.debug("Completed processing CharacteristicValuePrices for batch " + batchNumber
							+ " materialNo " + materialNo);
				}
			}
		} catch (NamingException ne) {
			throw new LoaderException(ne, "Unable to find home for ErpCharacteristicValuePrice");
		} catch (CreateException ce) {
			throw new LoaderException(ce, "Unable to create a new version of an ErpCharacteristicValuePrice");
		} catch (RemoteException re) {
			throw new LoaderException(re,
					"Unexpected system level exception while trying to create an ErpCharacteristicValuePrice");
		}
	}

	/**
	 * Method to process sales unit for each material.
	 * 
	 * @param materialNo
	 *            the material number
	 * @param salesUnits
	 *            the collection of sales unit to create
	 * 
	 * @throws LoaderException
	 *             any problems encountered while creating or updating objects
	 *             in the system
	 */
	public void loadSalesUnits(int batchNumber, String materialNo, HashSet<ErpSalesUnitModel> salesUnits)
			throws LoaderException {

		LOG.debug("Beginning to load sales unit data for material " + materialNo);
		ErpMaterialModel materialModel = null;
		try {
			this.initCtx = new InitialContext();
			try {
				UserTransaction utx = getSessionContext().getUserTransaction();
				utx.setTransactionTimeout(3000);
				try {
					utx.begin();
					ErpMaterialEB materialEB = null;
					try {
						ErpMaterialHome materialHome = (ErpMaterialHome) initCtx
								.lookup("java:comp/env/ejb/ErpMaterial");

						// find global material
						/*
						 * materialEB = materialHome.findBySapId(materialNo);
						 * ErpMaterialModel erpMaterialModel =
						 * (ErpMaterialModel) materialEB.getModel();
						 * updateMaterialApprovalStatus(erpMaterialModel);
						 * 
						 * Collections.sort(priceRows, matlPriceComparator);
						 * materialEB.setPrices(priceRows);
						 */

						materialEB = materialHome.findBySapId(materialNo);
						ErpMaterialModel existingMaterialModel = (ErpMaterialModel) materialEB.getModel();

						// clone the salesunit, prices & plant materials from
						// previous version if any before creating new entry
						if (existingMaterialModel != null) {

							materialModel = cloneGlobalMaterialModel(existingMaterialModel);

							List<ErpClassModel> classes = existingMaterialModel.getClasses();
							List<ErpClassModel> clonedClasses = new ArrayList<ErpClassModel>();
							clonedClasses = cloneAndLoadClasses(batchNumber, classes, clonedClasses);
							materialModel.setClasses(clonedClasses);

							// sales units
							if (null != salesUnits && !salesUnits.isEmpty()) {
								materialModel.setSalesUnits(new ArrayList<ErpSalesUnitModel>(salesUnits));
							}

							// prices
							List<ErpMaterialPriceModel> existingPriceRows = existingMaterialModel.getPrices();
							if (null != existingPriceRows && !existingPriceRows.isEmpty()) {
								materialModel.setPrices(clonePrices(existingPriceRows));
							}

							List<ErpPlantMaterialModel> existingPlantModels = existingMaterialModel.getMaterialPlants();
							if (null != existingPlantModels && !existingPlantModels.isEmpty()) {
								materialModel.setMaterialPlants(clonePlantModels(existingPlantModels));
							}

							List<ErpMaterialSalesAreaModel> existingSalesAreas = existingMaterialModel
									.getMaterialSalesAreas();
							if (null != existingSalesAreas && !existingSalesAreas.isEmpty()) {
								materialModel.setMaterialSalesAreas(cloneSalesAreas(existingSalesAreas));
							}

							updateMaterialApprovalStatus(materialModel);
							// create the new material
							LOG.info("Creating new material " + materialModel.getSapId() + ", version " + batchNumber);

							ErpMaterialEB erpMatlEB = materialHome.create(batchNumber, materialModel);
							materialModel = (ErpMaterialModel) erpMatlEB.getModel();

							LOG.info("Successfully created Material " + materialModel.getSapId() + ", id= "
									+ materialModel.getPK().getId() + "");

							// set the model to the createdMaterial object
							// createdMaterial = materialModel;

							cloneAndLoadCharacteristicValuePrices(batchNumber, materialModel,
									existingMaterialModel.getPK(),existingMaterialModel.getCharacteristics());
						}
					} catch (FinderException fe) {
						throw new LoaderException("No base product found for the material");
					} catch (RemoteException re) {
						throw new LoaderException(re,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					} catch (CreateException ce) {
						throw new LoaderException(ce,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					}

					try {
						utx.commit();
						LOG.debug("Completed loading price rows data for material " + materialNo);
					} catch (RollbackException re) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.",
								re);
						throw new LoaderException(re,
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.");
					} catch (HeuristicMixedException hme) {
						utx.setRollbackOnly();
						LOG.error("Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.",
								hme);
						throw new LoaderException(hme,
								"Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.");
					} catch (HeuristicRollbackException hre) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.",
								hre);
						throw new LoaderException(hre,
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.");
					} catch (RuntimeException rune) {
						utx.setRollbackOnly();
						LOG.error("Unexpected runtime exception in SAPLoaderSessionBean loadPriceRows", rune);
						throw new LoaderException(rune, "Unexpected runtime exception");
					}

				} catch (LoaderException le) {
					utx.setRollbackOnly();
					LOG.error("Aborting loadPriceRows", le);
					utx.rollback();
					throw (le);
				}
			} catch (NotSupportedException nse) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", nse);
				throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} catch (SystemException se) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", se);
				throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} finally {
				try {
					this.initCtx.close();
				} catch (NamingException ne) {
					// don't need to re-throw this since the transaction has
					// already completed or failed
					LOG.warn("Had difficulty closing naming context after transaction had completed.  "
							+ ne.getMessage());
				}
			}
		} catch (NamingException ne) {
			LOG.error("Unable to get naming context to locate components required by the loader.", ne);
			throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
		}

	}

	/**
	 * @param result
	 * @param materialMap
	 * @param materialClassMap
	 * @param variantPriceMap
	 */
	@SuppressWarnings("unused")
	private void setCharacteristicSalesUnit(ErpMaterialModel erpMaterial, List<ErpSalesUnitModel> salesUnits,
			Map<String, Map<ErpCharacteristicValuePriceModel, Map<String, String>>> variantPriceMap) {
		// TODO, set the materiai characteristicsalesunit
	}

	/**
	 * Method to process price rows for each material.
	 * 
	 * @param materialNo
	 * @param priceRows
	 * 
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	public void loadPriceRows(int batchNumber, String materialNo, List<ErpMaterialPriceModel> priceRows)
			throws RemoteException, LoaderException {

		LOG.debug("Beginning to load price rows data for material " + materialNo);
		ErpMaterialModel materialModel = null;
		try {
			this.initCtx = new InitialContext();
			try {
				UserTransaction utx = getSessionContext().getUserTransaction();
				utx.setTransactionTimeout(3000);
				try {
					utx.begin();
					ErpMaterialEB materialEB = null;
					try {
						ErpMaterialHome materialHome = (ErpMaterialHome) initCtx
								.lookup("java:comp/env/ejb/ErpMaterial");

						// find global material
						/*
						 * materialEB = materialHome.findBySapId(materialNo);
						 * ErpMaterialModel erpMaterialModel =
						 * (ErpMaterialModel) materialEB.getModel();
						 * updateMaterialApprovalStatus(erpMaterialModel);
						 * 
						 * Collections.sort(priceRows, matlPriceComparator);
						 * materialEB.setPrices(priceRows);
						 */

						materialEB = materialHome.findBySapId(materialNo);
						ErpMaterialModel existingMaterialModel = (ErpMaterialModel) materialEB.getModel();

						// clone the salesunit, prices & plant materials from
						// previous version if any before creating new entry
						if (existingMaterialModel != null) {

							materialModel = cloneGlobalMaterialModel(existingMaterialModel);

							List<ErpClassModel> classes = existingMaterialModel.getClasses();
							List<ErpClassModel> clonedClasses = new ArrayList<ErpClassModel>();
							clonedClasses = cloneAndLoadClasses(batchNumber, classes, clonedClasses);
							materialModel.setClasses(clonedClasses);

							// cloneAndLoadCharacteristicValuePrices(batchNumber,
							// existingMaterialModel);

							// sales units
							List<ErpSalesUnitModel> existingSalesUnitModels = existingMaterialModel.getSalesUnits();
							List<ErpSalesUnitModel> existingDisplaySalesUnits = existingMaterialModel.getDisplaySalesUnits();
							if (null != existingSalesUnitModels && !existingSalesUnitModels.isEmpty()) {
								materialModel.setSalesUnits(cloneSalesUnits(existingSalesUnitModels,existingDisplaySalesUnits));
							}

							// prices
							if (null != priceRows && !priceRows.isEmpty()) {
								Collections.sort(priceRows, matlPriceComparator);
								materialModel.setPrices(priceRows);
							}

							List<ErpPlantMaterialModel> existingPlantModels = existingMaterialModel.getMaterialPlants();
							if (null != existingPlantModels && !existingPlantModels.isEmpty()) {
								materialModel.setMaterialPlants(clonePlantModels(existingPlantModels));
							}

							List<ErpMaterialSalesAreaModel> existingSalesAreas = existingMaterialModel
									.getMaterialSalesAreas();
							if (null != existingSalesAreas && !existingSalesAreas.isEmpty()) {
								materialModel.setMaterialSalesAreas(cloneSalesAreas(existingSalesAreas));
							}

							updateMaterialApprovalStatus(materialModel);

							// create the new material
							LOG.info("Creating new material " + materialModel.getSapId() + ", version " + batchNumber);

							ErpMaterialEB erpMatlEB = materialHome.create(batchNumber, materialModel);
							materialModel = (ErpMaterialModel) erpMatlEB.getModel();

							LOG.info("Successfully created Material " + materialModel.getSapId() + ", id= "
									+ materialModel.getPK().getId() + "");

							// set the model to the createdMaterial object
							// createdMaterial = materialModel;

							cloneAndLoadCharacteristicValuePrices(batchNumber, materialModel,
									existingMaterialModel.getPK(),existingMaterialModel.getCharacteristics());
						}
					} catch (FinderException fe) {
						throw new LoaderException("No base product found for the material");
					} catch (RemoteException re) {
						throw new LoaderException(re,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					} catch (CreateException ce) {
						throw new LoaderException(ce,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					}

					try {
						utx.commit();
						LOG.debug("Completed loading price rows data for material " + materialNo);
					} catch (RollbackException re) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.",
								re);
						throw new LoaderException(re,
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.");
					} catch (HeuristicMixedException hme) {
						utx.setRollbackOnly();
						LOG.error("Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.",
								hme);
						throw new LoaderException(hme,
								"Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.");
					} catch (HeuristicRollbackException hre) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.",
								hre);
						throw new LoaderException(hre,
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.");
					} catch (RuntimeException rune) {
						utx.setRollbackOnly();
						LOG.error("Unexpected runtime exception in SAPLoaderSessionBean loadPriceRows", rune);
						throw new LoaderException(rune, "Unexpected runtime exception");
					}

				} catch (LoaderException le) {
					utx.setRollbackOnly();
					LOG.error("Aborting loadPriceRows", le);
					utx.rollback();
					throw (le);
				}
			} catch (NotSupportedException nse) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", nse);
				throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} catch (SystemException se) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", se);
				throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} finally {
				try {
					this.initCtx.close();
				} catch (NamingException ne) {
					// don't need to re-throw this since the transaction has
					// already completed or failed
					LOG.warn("Had difficulty closing naming context after transaction had completed.  "
							+ ne.getMessage());
				}
			}
		} catch (NamingException ne) {
			LOG.error("Unable to get naming context to locate components required by the loader.", ne);
			throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
		}
	}

	/**
	 * Method to process plant info for each material.
	 * 
	 * @param materialNo
	 * @param materialPlants
	 * 
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	/*
	 * public void loadMaterialPlants(String materialNo,
	 * List<ErpPlantMaterialModel> materialPlants) throws RemoteException,
	 * LoaderException { LOG.debug("Beginning to load plant data for material "
	 * + materialNo); try { this.initCtx = new InitialContext(); try {
	 * UserTransaction utx = getSessionContext().getUserTransaction();
	 * 
	 * utx.setTransactionTimeout(3000); try { utx.begin(); ErpMaterialEB
	 * materialEB = null; try { ErpMaterialHome materialHome = (ErpMaterialHome)
	 * initCtx .lookup("java:comp/env/ejb/ErpMaterial");
	 * 
	 * // find global material materialEB =
	 * materialHome.findBySapId(materialNo); ErpMaterialModel erpMaterialModel =
	 * (ErpMaterialModel) materialEB.getModel();
	 * updateMaterialApprovalStatus(erpMaterialModel);
	 * 
	 * materialEB.setMaterialPlants(materialPlants); } catch (FinderException
	 * fe) { throw new
	 * LoaderException("No base product found for the material"); } catch
	 * (RemoteException re) { throw new LoaderException(re,
	 * "Unexpected system level exception while trying to create an ErpPlantMaterialModel"
	 * ); }
	 * 
	 * try { utx.commit();
	 * LOG.debug("Completed loading plant data for material " + materialNo); }
	 * catch (RollbackException re) { utx.setRollbackOnly(); LOG.error(
	 * "Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit."
	 * , re); throw new LoaderException(re,
	 * "Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit."
	 * ); } catch (HeuristicMixedException hme) { utx.setRollbackOnly();
	 * LOG.error(
	 * "Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics."
	 * , hme); throw new LoaderException(hme,
	 * "Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics."
	 * ); } catch (HeuristicRollbackException hre) { utx.setRollbackOnly();
	 * LOG.error(
	 * "Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction."
	 * , hre); throw new LoaderException(hre,
	 * "Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction."
	 * ); } catch (RuntimeException rune) { utx.setRollbackOnly(); LOG.error(
	 * "Unexpected runtime exception in SAPLoaderSessionBean loadMaterialPlants"
	 * , rune); throw new LoaderException(rune, "Unexpected runtime exception");
	 * }
	 * 
	 * } catch (LoaderException le) { utx.setRollbackOnly();
	 * LOG.error("Aborting loadMaterialPlants", le); utx.rollback(); throw (le);
	 * } } catch (NotSupportedException nse) {
	 * LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction."
	 * , nse); throw new LoaderException(nse,
	 * "Unable to update ERPS objects.  Unable to begin a UserTransaction."); }
	 * catch (SystemException se) {
	 * LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction."
	 * , se); throw new LoaderException(se,
	 * "Unable to update ERPS objects.  Unable to begin a UserTransaction."); }
	 * finally { try { this.initCtx.close(); } catch (NamingException ne) { //
	 * don't need to re-throw this since the transaction has // already
	 * completed or failed LOG.warn(
	 * "Had difficulty closing naming context after transaction had completed.  "
	 * + ne.getMessage()); } } } catch (NamingException ne) { LOG.error(
	 * "Unable to get naming context to locate components required by the loader."
	 * , ne); throw new LoaderException(ne,
	 * "Unable to get naming context to locate components required by the loader."
	 * ); } }
	 */

	/**
	 * 
	 * @param materialNo
	 * @param salesAreas
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	/*
	 * public void loadMaterialSalesAreas(String materialNo,
	 * List<ErpMaterialSalesAreaModel> salesAreas) throws RemoteException,
	 * LoaderException { LOG.debug("Beginning to load plant data for material "
	 * + materialNo); try { this.initCtx = new InitialContext(); try {
	 * UserTransaction utx = getSessionContext().getUserTransaction();
	 * 
	 * utx.setTransactionTimeout(3000); try { utx.begin(); ErpMaterialEB
	 * materialEB = null; try { ErpMaterialHome materialHome = (ErpMaterialHome)
	 * initCtx .lookup("java:comp/env/ejb/ErpMaterial");
	 * 
	 * // find global material materialEB =
	 * materialHome.findBySapId(materialNo); ErpMaterialModel erpMaterialModel =
	 * (ErpMaterialModel) materialEB.getModel();
	 * updateMaterialApprovalStatus(erpMaterialModel);
	 * 
	 * materialEB.setMaterialSalesAreas(salesAreas); } catch (FinderException
	 * fe) { throw new LoaderException("No base product found for the material:"
	 * + materialNo); } catch (RemoteException re) { throw new
	 * LoaderException(re,
	 * "Unexpected system level exception while trying to create an ErpMaterialSalesAreaModel"
	 * ); }
	 * 
	 * try { utx.commit();
	 * LOG.debug("Completed loading plant data for material " + materialNo); }
	 * catch (RollbackException re) { utx.setRollbackOnly(); LOG.error(
	 * "Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit."
	 * , re); throw new LoaderException(re,
	 * "Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit."
	 * ); } catch (HeuristicMixedException hme) { utx.setRollbackOnly();
	 * LOG.error(
	 * "Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics."
	 * , hme); throw new LoaderException(hme,
	 * "Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics."
	 * ); } catch (HeuristicRollbackException hre) { utx.setRollbackOnly();
	 * LOG.error(
	 * "Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction."
	 * , hre); throw new LoaderException(hre,
	 * "Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction."
	 * ); } catch (RuntimeException rune) { utx.setRollbackOnly(); LOG.error(
	 * "Unexpected runtime exception in SAPLoaderSessionBean loadMaterialPlants"
	 * , rune); throw new LoaderException(rune, "Unexpected runtime exception");
	 * }
	 * 
	 * } catch (LoaderException le) { utx.setRollbackOnly();
	 * LOG.error("Aborting loadMaterialPlants", le); utx.rollback(); throw (le);
	 * } } catch (NotSupportedException nse) {
	 * LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction."
	 * , nse); throw new LoaderException(nse,
	 * "Unable to update ERPS objects.  Unable to begin a UserTransaction."); }
	 * catch (SystemException se) {
	 * LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction."
	 * , se); throw new LoaderException(se,
	 * "Unable to update ERPS objects.  Unable to begin a UserTransaction."); }
	 * finally { try { this.initCtx.close(); } catch (NamingException ne) { //
	 * don't need to re-throw this since the transaction has // already
	 * completed or failed LOG.warn(
	 * "Had difficulty closing naming context after transaction had completed.  "
	 * + ne.getMessage()); } } } catch (NamingException ne) { LOG.error(
	 * "Unable to get naming context to locate components required by the loader."
	 * , ne); throw new LoaderException(ne,
	 * "Unable to get naming context to locate components required by the loader."
	 * ); } }
	 */

	/**
	 * Method to check the material completion to set the approval status
	 * 
	 * @param erpMaterialModel
	 */
	public void updateMaterialApprovalStatus(ErpMaterialModel erpMaterialModel) {
		if (erpMaterialModel != null) {
			String materialType = erpMaterialModel.getMaterialType();
			erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);//TODO: Decide whether to use this flag or not.
			if ("HAWA".equalsIgnoreCase(materialType)) {
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getMaterialPlants() != null
						&& erpMaterialModel.getPrices() != null && erpMaterialModel.getSalesUnits().size() > 0
						&& erpMaterialModel.getMaterialPlants().size() > 0 && erpMaterialModel.getPrices().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			} else if ("FERT".equalsIgnoreCase(materialType)) {
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getMaterialPlants() != null
						&& erpMaterialModel.getPrices() != null && erpMaterialModel.getSalesUnits().size() > 0
						&& erpMaterialModel.getMaterialPlants().size() > 0 && erpMaterialModel.getPrices().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			} else if ("NLAG".equalsIgnoreCase(materialType)) {
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getMaterialPlants() != null
						&& erpMaterialModel.getPrices() != null && erpMaterialModel.getSalesUnits().size() > 0
						&& erpMaterialModel.getMaterialPlants().size() > 0 && erpMaterialModel.getPrices().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			} else if ("ZALT".equalsIgnoreCase(materialType)) {
				// adding based on ERP chart, not sure how materials of this
				// type are handled without plant info.
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getPrices() != null
						&& erpMaterialModel.getSalesUnits().size() > 0 && erpMaterialModel.getPrices().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			} else if ("UNBW".equalsIgnoreCase(materialType)) {
				// adding based on ERP chart, not sure how materials of this
				// type are handled without price info.
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getMaterialPlants() != null
						&& erpMaterialModel.getSalesUnits().size() > 0
						&& erpMaterialModel.getMaterialPlants().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			} else if ("ZMKT".equalsIgnoreCase(materialType)) {
				// adding based on ERP chart, not sure how materials of this
				// type are handled without price info.
				if (erpMaterialModel.getSalesUnits() != null && erpMaterialModel.getMaterialPlants() != null
						&& erpMaterialModel.getSalesUnits().size() > 0
						&& erpMaterialModel.getMaterialPlants().size() > 0) {
					erpMaterialModel.setApprovalStatus(EnumProductApprovalStatus.APPROVED);
				}
			}
		}
	}

	/**
	 * 
	 * a convenience object that can compare sales units by their ratio
	 * (numerator / denominator)
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static Comparator salesUnitComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			ErpSalesUnitModel su1 = (ErpSalesUnitModel) o1;
			ErpSalesUnitModel su2 = (ErpSalesUnitModel) o2;
			double ratio1 = su1.getNumerator() / su1.getDenominator();
			double ratio2 = su2.getNumerator() / su2.getDenominator();
			if (ratio1 < ratio2) {
				if (!su1.isDisplayInd() || (su1.isDisplayInd() == su2.isDisplayInd())) {
					return -1;
				} else {
					return 1;
				}
			} else if (ratio2 < ratio1) {
				if (!su2.isDisplayInd() || (su1.isDisplayInd() == su2.isDisplayInd())) {
					return 1;
				} else {
					return -1;
				}
			} else
				return 0;
		}
	};

	private Comparator<ErpMaterialPriceModel> matlPriceComparator = new Comparator<ErpMaterialPriceModel>() {
		public int compare(ErpMaterialPriceModel price1, ErpMaterialPriceModel price2) {
			if (price1.getScaleQuantity() == price2.getScaleQuantity())
				return 0;
			else if (price1.getScaleQuantity() < price2.getScaleQuantity())
				return -1;
			else
				return 1;
		}
	};

	/**
	 * Method to Save Plant and Sales Area info for each material.
	 * @param batchNumber
	 * @param materialNo
	 * @param plants
	 * @param salesAreas
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	public void loadMaterialPlantsAndSalesAreas(int batchNumber, String materialNo, List<ErpPlantMaterialModel> plants,
			List<ErpMaterialSalesAreaModel> salesAreas) throws RemoteException, LoaderException {
		LOG.debug("Beginning to load plants and salesarea rows data for material " + materialNo);
		ErpMaterialModel materialModel = null;
		try {
			this.initCtx = new InitialContext();
			try {
				UserTransaction utx = getSessionContext().getUserTransaction();
				utx.setTransactionTimeout(3000);
				try {
					utx.begin();
					ErpMaterialEB materialEB = null;
					try {
						ErpMaterialHome materialHome = (ErpMaterialHome) initCtx
								.lookup("java:comp/env/ejb/ErpMaterial");

						// find global material
						/*
						 * materialEB = materialHome.findBySapId(materialNo);
						 * ErpMaterialModel erpMaterialModel =
						 * (ErpMaterialModel) materialEB.getModel();
						 * updateMaterialApprovalStatus(erpMaterialModel);
						 * 
						 * Collections.sort(priceRows, matlPriceComparator);
						 * materialEB.setPrices(priceRows);
						 */

						materialEB = materialHome.findBySapId(materialNo);
						ErpMaterialModel existingMaterialModel = (ErpMaterialModel) materialEB.getModel();

						// clone the salesunit, prices & plant materials from
						// previous version if any before creating new entry
						if (existingMaterialModel != null) {

							materialModel = cloneGlobalMaterialModel(existingMaterialModel);

							List<ErpClassModel> classes = existingMaterialModel.getClasses();
							List<ErpClassModel> clonedClasses = new ArrayList<ErpClassModel>();
							clonedClasses = cloneAndLoadClasses(batchNumber, classes, clonedClasses);
							materialModel.setClasses(clonedClasses);

							// cloneAndLoadCharacteristicValuePrices(batchNumber,
							// existingMaterialModel);

							// sales units
							List<ErpSalesUnitModel> existingSalesUnitModels = existingMaterialModel.getSalesUnits();
							List<ErpSalesUnitModel> existingDisplaySalesUnits = existingMaterialModel.getDisplaySalesUnits();
							if (null != existingSalesUnitModels && !existingSalesUnitModels.isEmpty()) {
								materialModel.setSalesUnits(cloneSalesUnits(existingSalesUnitModels,existingDisplaySalesUnits));
							}

							// prices
							List<ErpMaterialPriceModel> existingPriceRows = existingMaterialModel.getPrices();
							if (null != existingPriceRows && !existingPriceRows.isEmpty()) {
								materialModel.setPrices(clonePrices(existingPriceRows));
							}

							if (null != plants && !plants.isEmpty()) {
								materialModel.setMaterialPlants(plants);
							}
							if (null != salesAreas && !salesAreas.isEmpty()) {
								for (Iterator iterator = salesAreas.iterator(); iterator.hasNext();) {
									ErpMaterialSalesAreaModel salesAreaModel = (ErpMaterialSalesAreaModel) iterator
											.next();
									salesAreaModel.setSkuCode(existingMaterialModel.getSkuCode());
									
								}
								materialModel.setMaterialSalesAreas(salesAreas);
							}

							updateMaterialApprovalStatus(materialModel);

							// create the new material
							LOG.info("Creating new material " + materialModel.getSapId() + ", version " + batchNumber);

							ErpMaterialEB erpMatlEB = materialHome.create(batchNumber, materialModel);
							materialModel = (ErpMaterialModel) erpMatlEB.getModel();

							LOG.info("Successfully created Material " + materialModel.getSapId() + ", id= "
									+ materialModel.getPK().getId() + "");

							// set the model to the createdMaterial object
							// createdMaterial = materialModel;

							cloneAndLoadCharacteristicValuePrices(batchNumber, materialModel,
									existingMaterialModel.getPK(),existingMaterialModel.getCharacteristics());
						}
					} catch (FinderException fe) {
						throw new LoaderException("No base product found for the material");
					} catch (RemoteException re) {
						throw new LoaderException(re,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					} catch (CreateException ce) {
						throw new LoaderException(ce,
								"Unexpected system level exception while trying to create an ErpMaterialPriceModel");
					}

					try {
						utx.commit();
						LOG.debug("Completed loading plants and salesarea rows data for material " + materialNo);
					} catch (RollbackException re) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.",
								re);
						throw new LoaderException(re,
								"Unable to update ERPS objects. UserTransaction had already rolled back before attempt to commit.");
					} catch (HeuristicMixedException hme) {
						utx.setRollbackOnly();
						LOG.error("Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.",
								hme);
						throw new LoaderException(hme,
								"Unable to update ERPS objects. TransactionManager aborted due to mixed heuristics.");
					} catch (HeuristicRollbackException hre) {
						utx.setRollbackOnly();
						LOG.error(
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.",
								hre);
						throw new LoaderException(hre,
								"Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.");
					} catch (RuntimeException rune) {
						utx.setRollbackOnly();
						LOG.error("Unexpected runtime exception in SAPLoaderSessionBean loadPriceRows", rune);
						throw new LoaderException(rune, "Unexpected runtime exception");
					}

				} catch (LoaderException le) {
					utx.setRollbackOnly();
					LOG.error("Aborting loadMaterialPlantsAndSalesAreas", le);
					utx.rollback();
					throw (le);
				}
			} catch (NotSupportedException nse) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", nse);
				throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} catch (SystemException se) {
				LOG.error("Unable to update ERPS objects. Unable to begin a UserTransaction.", se);
				throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
			} finally {
				try {
					this.initCtx.close();
				} catch (NamingException ne) {
					// don't need to re-throw this since the transaction has
					// already completed or failed
					LOG.warn("Had difficulty closing naming context after transaction had completed.  "
							+ ne.getMessage());
				}
			}
		} catch (NamingException ne) {
			LOG.error("Unable to get naming context to locate components required by the loader.", ne);
			throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
		}
	}

	/**
	 * @param batchNumber
	 * @param existingMaterialModel
	 * @throws NamingException
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	private void cloneAndLoadCharacteristicValuePrices(int batchNumber, ErpMaterialModel materialModel,
			PrimaryKey existingMatPk, List<ErpCharacteristicModel> oldCharacteristics) throws NamingException, RemoteException, CreateException {
		ErpCharacteristicValuePriceHome cvpHome = (ErpCharacteristicValuePriceHome) initCtx
				.lookup(ErpServicesProperties.getCharacteristicValuePriceHome());
		try {
			Collection<ErpCharacteristicValuePriceEB> cvPriceEBs = cvpHome.findByMaterial((VersionedPrimaryKey) existingMatPk);
			if (null != cvPriceEBs && !cvPriceEBs.isEmpty()) {
				for (Iterator<ErpCharacteristicValuePriceEB> i = cvPriceEBs.iterator(); i.hasNext();) {
					ErpCharacteristicValuePriceModel cvpModel = (ErpCharacteristicValuePriceModel) i.next().getModel();
					for (ErpCharacteristicModel oldCharcModel : oldCharacteristics) {
						List<ErpCharacteristicValueModel> oldCharcValues = oldCharcModel.getCharacteristicValues();
						for (ErpCharacteristicValueModel oldCharcValueModel : oldCharcValues) {
							if(cvpModel.getCharacteristicValueId().equalsIgnoreCase(oldCharcValueModel.getId())){
								cvpModel.setCharacteristicName(oldCharcModel.getName());
								cvpModel.setCharacteristicValueName(oldCharcValueModel.getName());
							}
						}
						
					}
					ErpCharacteristicValuePriceModel clonedCvpModel = cloneCharacteristicValuePriceModel(cvpModel);
					clonedCvpModel.setMaterialId(materialModel.getId());
					if(cvpModel.getPrice() > 0){
						if (null != materialModel.getCharacteristics()) {
//							for (ErpClassModel classModel : (List<ErpClassModel>) materialModel.getClasses()) {
							for (ErpCharacteristicModel charcModel : (List<ErpCharacteristicModel>) materialModel.getCharacteristics()) {
//								if (classModel.getSapId().equals(clonedCvpModel.getClassName())) {
//									ErpCharacteristicModel charcModel = classModel.getCharacteristic(clonedCvpModel
//											.getCharacteristicName());
									if (null != charcModel && charcModel.getName().equalsIgnoreCase(clonedCvpModel.getCharacteristicName())){
										ErpCharacteristicValueModel charcValModel = charcModel
												.getCharacteristicValue(clonedCvpModel.getCharacteristicValueName());
										if(null !=charcValModel){
											clonedCvpModel.setCharacteristicValueId(charcValModel.getPK().getId());
											
											clonedCvpModel.setConditionType(cvpModel.getConditionType());
											clonedCvpModel.setPrice(cvpModel.getPrice());
											clonedCvpModel.setSapId(cvpModel.getSapId());
											clonedCvpModel.setPricingUnit(cvpModel.getPricingUnit());
											clonedCvpModel.setSalesOrg(cvpModel.getSalesOrg());
											clonedCvpModel.setDistChannel(cvpModel.getDistChannel());
										}
									}
//								}
	
							}
						}
						if(clonedCvpModel.getPrice() >0 && null !=clonedCvpModel.getCharacteristicValueId()){
							ErpCharacteristicValuePriceEB erpCvpEB = cvpHome.create(batchNumber, clonedCvpModel);
							clonedCvpModel = (ErpCharacteristicValuePriceModel) erpCvpEB.getModel();
						}
					}
				}
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param batchNumber
	 * @param classes
	 * @param clonedClasses
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	private List<ErpClassModel> cloneAndLoadClasses(int batchNumber, List<ErpClassModel> classes,
			List<ErpClassModel> clonedClasses) throws NamingException, RemoteException, CreateException {
		if (null != classes && !classes.isEmpty()) {
			ErpClassHome classHome = (ErpClassHome) initCtx.lookup("java:comp/env/ejb/ErpClass");
			for (ErpClassModel classModel : classes) {
				ErpClassModel clonedClassModel = cloneClassModel(classModel);
				clonedClassModel = loadClass(batchNumber, classHome, clonedClassModel);
				clonedClasses.add(clonedClassModel);
			}
		}
		return clonedClasses;
	}

	/**
	 * @param existingPriceRows
	 * @return
	 */
	private List<ErpMaterialPriceModel> clonePrices(List<ErpMaterialPriceModel> existingPriceRows) {
		List<ErpMaterialPriceModel> priceRows = null;
		if (existingPriceRows != null && existingPriceRows.size() > 0) {
			priceRows = new ArrayList<ErpMaterialPriceModel>();
			for (ErpMaterialPriceModel priceRow : existingPriceRows) {
				priceRows.add(clonePriceRow(priceRow));
			}
			Collections.sort(priceRows, matlPriceComparator);
		}
		return priceRows;
	}

	/**
	 * @param unitList
	 * @param existingSalesUnitModels
	 * @return
	 */
	private List<ErpSalesUnitModel> cloneSalesUnits(List<ErpSalesUnitModel> existingSalesUnitModels, List<ErpSalesUnitModel> existingDisplaySalesUnits) {
		List<ErpSalesUnitModel> clonedSalesUnits = null;
		if (existingSalesUnitModels != null && existingSalesUnitModels.size() > 0) {
			clonedSalesUnits = new ArrayList<ErpSalesUnitModel>();
			for (ErpSalesUnitModel salesUnitModel : existingSalesUnitModels) {
				clonedSalesUnits.add(cloneSalesUnitModel(salesUnitModel));
			}
			if(null !=existingDisplaySalesUnits && existingDisplaySalesUnits.size() >0 ){
				for (ErpSalesUnitModel salesUnitModel : existingDisplaySalesUnits) {
					clonedSalesUnits.add(cloneSalesUnitModel(salesUnitModel));
				}
			}
			Collections.sort(clonedSalesUnits, salesUnitComparator);

		}
		return clonedSalesUnits;
	}

	private ErpClassModel cloneClassModel(ErpClassModel classModel) {
		ErpClassModel clonedClassModel = new ErpClassModel();
		clonedClassModel.setSapId(classModel.getSapId());
		clonedClassModel.setCharacteristics(cloneCharacteristics(classModel.getCharacteristics()));
		return clonedClassModel;
	}

	private List<ErpCharacteristicModel> cloneCharacteristics(List<ErpCharacteristicModel> characteristics) {
		List<ErpCharacteristicModel> clonedCharacteristics = new ArrayList<ErpCharacteristicModel>();
		for (ErpCharacteristicModel erpCharcModel : characteristics) {
			ErpCharacteristicModel clonedCharcModel = new ErpCharacteristicModel();
			clonedCharcModel.setName(erpCharcModel.getName());
			for (Iterator<ErpCharacteristicValueModel> iterator = erpCharcModel.getCharacteristicValues().iterator(); iterator
					.hasNext();) {
				ErpCharacteristicValueModel erpCharacValModel = (ErpCharacteristicValueModel) iterator.next();
				ErpCharacteristicValueModel clonedCharcValModel = new ErpCharacteristicValueModel();
				clonedCharcValModel.setName(erpCharacValModel.getName());
				clonedCharcValModel.setDescription(erpCharacValModel.getDescription());
				clonedCharcModel.addCharacteristicValue(clonedCharcValModel);
			}
			clonedCharacteristics.add(clonedCharcModel);
		}
		return clonedCharacteristics;
	}

	/**
	 * @param charcValPriceModel
	 * @return
	 */
	private ErpCharacteristicValuePriceModel cloneCharacteristicValuePriceModel(
			ErpCharacteristicValuePriceModel charcValPriceModel) {
		ErpCharacteristicValuePriceModel clonedCharcValPriceModel = new ErpCharacteristicValuePriceModel();
		clonedCharcValPriceModel.setConditionType(charcValPriceModel.getConditionType());
		clonedCharcValPriceModel.setPrice(charcValPriceModel.getPrice());
		clonedCharcValPriceModel.setSapId(charcValPriceModel.getSapId());
		clonedCharcValPriceModel.setPricingUnit(charcValPriceModel.getPricingUnit());
		clonedCharcValPriceModel.setSalesOrg(charcValPriceModel.getSalesOrg());
		clonedCharcValPriceModel.setDistChannel(charcValPriceModel.getDistChannel());
		clonedCharcValPriceModel.setCharacteristicValueName(charcValPriceModel.getCharacteristicValueName());
		clonedCharcValPriceModel.setCharacteristicName(charcValPriceModel.getCharacteristicName());
		clonedCharcValPriceModel.setClassName(charcValPriceModel.getClassName());
		return clonedCharcValPriceModel;
	}

	/**
	 * 
	 * @param materialModel
	 * @return
	 */
	private ErpMaterialModel cloneGlobalMaterialModel(ErpMaterialModel materialModel) {
		ErpMaterialModel clonedMatModel = new ErpMaterialModel();
		clonedMatModel.setSapId(materialModel.getSapId());
		clonedMatModel.setSkuCode(materialModel.getSkuCode());
		clonedMatModel.setBaseUnit(materialModel.getBaseUnit());
		clonedMatModel.setDescription(materialModel.getDescription());
		clonedMatModel.setUPC(materialModel.getUPC());
		clonedMatModel.setDaysFresh(materialModel.getDaysFresh());
		clonedMatModel.setMaterialType(materialModel.getMaterialType());
		clonedMatModel.setTaxable(materialModel.isTaxable());
		clonedMatModel.setTaxCode(materialModel.getTaxCode());
		clonedMatModel.setAlcoholicContent(materialModel.getAlcoholicContent());
		clonedMatModel.setQuantityCharacteristic(materialModel.getQuantityCharacteristic());
		clonedMatModel.setSalesUnitCharacteristic(materialModel.getSalesUnitCharacteristic());
		clonedMatModel.setMaterialGroup(materialModel.getMaterialGroup());
		return clonedMatModel;

	}

	private void populateMaterialSalesUnitChar(ErpClassModel erpClass, ErpMaterialModel erpMaterial) throws LoaderException {
		List<ErpCharacteristicModel> charList = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics());
		ListIterator<ErpCharacteristicModel> charListIter = charList.listIterator();
		while (charListIter.hasNext()) {
			ErpCharacteristicModel erpCharac = charListIter.next();
			if(erpCharac.isSalesUnit()){
				charListIter.remove();
			}
		}
		erpClass.setCharacteristics(charList);
	}
	/*private void populateMaterialSalesUnitChar(ErpClassModel erpClass, ErpMaterialModel erpMaterial) throws LoaderException {
		List<ErpCharacteristicModel> charList = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics());
		ListIterator<ErpCharacteristicModel> charListIter = charList.listIterator();
		while (charListIter.hasNext()) {
			ErpCharacteristicModel erpCharac = charListIter.next();

			boolean isSalesUnit = false;
			boolean matchError = false;

			//
			// try to find a match between characteristic value names
			// and sales unit names
			//
			int matchCount = 0;
			Iterator<ErpCharacteristicValueModel> charValIter = erpCharac.getCharacteristicValues().iterator();
			if (null != erpMaterial.getSalesUnits()) {
				while (charValIter.hasNext()) {
					ErpCharacteristicValueModel erpCharValue = charValIter.next();
					Iterator salesUnitIter = erpMaterial.getSalesUnits().iterator();
					while (salesUnitIter.hasNext()) {
						ErpSalesUnitModel erpSalesUnit = (ErpSalesUnitModel) salesUnitIter.next();
						if (erpCharValue.getName().equals(erpSalesUnit.getAlternativeUnit())) {
							//
							// found a match
							//
							++matchCount;
						}
					}
				}
			}
			//
			// see if matches were found between the characteristic values and
			// the sales units
			//
			if ((matchCount != 0) && (matchCount == erpMaterial.numberOfSalesUnits())
					&& (matchCount == erpCharac.numberOfCharacteristicValues())) {
				//
				// if the match count is equal to the number of sales units a
				// material has and the number of values
				// of the matched characteristic, then the characterisitic is a
				// sales unit
				//
				erpMaterial.setSalesUnitCharacteristic(erpCharac.getName());
				isSalesUnit = true;
			} else if ((matchCount != 0)
					&& ((matchCount != erpMaterial.numberOfSalesUnits()) || (matchCount != erpCharac
							.numberOfCharacteristicValues()))) {
				//
				// if some matches were found, but the number of matches isn't
				// equal to the number of
				// sales units for the material or the number of values for the
				// matched characteristic
				// this probably means that there was some data entry error on
				// the SAP side and we should flag this as an exception
				//
				matchError = true;
				throw new LoaderException("There was a mismatch between the sales units for Material "
						+ erpMaterial.getSapId() + " and the values of Characteristic " + erpCharac.getName()
						+ " in Class " + erpClass.getSapId());

			}

			//
			// if the characteristic was really a sales unit and no erroneous
			// partial matches were found
			// remove the characteristic from the class
			//
			if (isSalesUnit && !matchError) {
				//
				// get the list of characteristic from the class
				//
				List<ErpCharacteristicModel> characs = new ArrayList<ErpCharacteristicModel>(
						erpClass.getCharacteristics());
				ListIterator<ErpCharacteristicModel> clIter = characs.listIterator();
				while (clIter.hasNext()) {
					if (clIter.next().getName().equals(erpCharac.getName())) {
						//
						// remove the characteristic that matched the sales
						// units from the list
						//
						clIter.remove();
						break;
					}
				}
				//
				// set the list of characteristics back on the class
				//
//				erpClass.setCharacteristics(characs);
//			}

		}
	}*/
}
