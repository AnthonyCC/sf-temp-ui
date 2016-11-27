package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;

/**
 * The remote interface for the SAPLoader session bean
 * 
 * @author kkanuganti
 * 
 */
public interface SAPLoaderSB extends EJBObject
{	
	/**
	 * @return ErpMaterialBatchHistoryModel
	 * 	the batch model
	 * @throws RemoteException
	 *    any system level problems
	 * @throws LoaderException
	 *   any problems encountered creating batch number in the system
	 */
	public ErpMaterialBatchHistoryModel getMaterialBatchInfo() throws RemoteException, LoaderException;
	
	/**
	 * @return int
	 * 	the batch number
	 * @throws RemoteException
	 *    any system level problems
	 * @throws LoaderException
	 *   any problems encountered creating batch number in the system
	 */
	public int createBatch() throws RemoteException, LoaderException;
	
	
	/**
    * makes an entry in the history table indicating that a batch succeeded and is ready for review
	 * @param batchNumber 
	 * @param batchStatus
    *
   * @throws RemoteException
	 *    any system level problems
	 * @throws LoaderException
	 *   any problems encountered updating batch record in the system
    */
   public void updateBatchStatus(int batchNumber, EnumApprovalStatus batchStatus) throws RemoteException, LoaderException;

	/**
	 * Performs the actual load of material, class, characteristic, characteristic value & characteristic value prices
	 * 
	 * @param batchNumber 
	 *				 the batch number (version)
	 * @param classes
	 *           the collection of classes to be updated in this batch for the material
	 * @param material
	 *           the base material to be created or updated in this batch
	 * @param characteristicValuePrices
	 *           the collection of characteristic value prices to create or update in this batch for the material
	 * 
	 * @throws RemoteException
	 *            any system level problems
	 * @throws LoaderException
	 *            any problems encountered creating or updating objects in the system
	 */
	public void loadData(int batchNumber, ErpMaterialModel material, Map<String, ErpClassModel> classes, 
			Map<ErpCharacteristicValuePriceModel, Map<String, String>> characteristicValuePrices) throws RemoteException,
			LoaderException;
	
	 /**
    * Method to process sales unit for each material.
    * 
    * @param materialNo 
    *		the material number
    * @param salesUnits 
    * 		the collection of sales unit to create
    * 
    * @throws LoaderException 
    * 		any problems encountered while creating or updating objects in the system
    */
	public void loadSalesUnits(int batchNumber, String materialNo, HashSet<ErpSalesUnitModel> salesUnits) throws RemoteException, LoaderException;


	/**
	 * Method to process price rows for each material.
	 * 
	 * @param materialNo
	 * @param priceRows
	 * 
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	public void loadPriceRows(int batchNumber, String materialNo, List<ErpMaterialPriceModel> priceRows) throws RemoteException, LoaderException;
	
	
	/**
	 * 
	 * @param materialNo
	 * @param priceRows
	 * @throws RemoteException
	 * @throws LoaderException
	 */
//	public void loadMaterialPlants(String materialNo, List<ErpPlantMaterialModel> plants) throws RemoteException, LoaderException;
	
	/**
	 * 
	 * @param materialNo
	 * @param salesAreas
	 * @throws RemoteException
	 * @throws LoaderException
	 */
//	public void loadMaterialSalesAreas(String materialNo, List<ErpMaterialSalesAreaModel> salesAreas) throws RemoteException, LoaderException;
	
	/**
	 * 
	 * @param materialNo
	 * @param plants
	 * @param salesAreas
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	public void loadMaterialPlantsAndSalesAreas(int batchNumber, String materialNo, List<ErpPlantMaterialModel> plants, List<ErpMaterialSalesAreaModel> salesAreas) throws RemoteException, LoaderException;
	

}
