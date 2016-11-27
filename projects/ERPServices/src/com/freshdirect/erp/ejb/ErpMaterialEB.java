package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * ErpMaterial remote interface.
 *
 * @author kkanuganti   
 */
public interface ErpMaterialEB extends EntityBeanRemoteI {
	
	/**
	 * Set ErpSalesUnitModel entries. Overwrites existing collection.
	 * 
	 * @param salesUnits collection of ErpSalesUnitModel objects
	 *
	 * @return true if the entires were stored
	 * @throws RemoteException 
	 */
	public boolean setSalesUnits(@SuppressWarnings("rawtypes") Collection salesUnits)  throws RemoteException;

	/**
	 * Set ErpSalesUnitModel entries. Overwrites existing collection.
	 * 
	 * @param prices collection of ErpSalesUnitModel objects
	 *
	 * @return true if the entires were stored
	 * @throws RemoteException 
	 */
	public boolean setPrices(@SuppressWarnings("rawtypes") Collection prices)  throws RemoteException;

	
	/**
	 * Set ErpPlantMaterialModel entries. Overwrites existing collection.
	 * @param plantMaterials
	 * @return
	 * @throws RemoteException
	 */
	public boolean setMaterialPlants(@SuppressWarnings("rawtypes") Collection plantMaterials)  throws RemoteException;
	
	/**
	 * Set ErpMaterialSalesAreaModel entries. Overwrites existing collection.
	 * @param salesAreas
	 * @return
	 * @throws RemoteException
	 */
	public boolean setMaterialSalesAreas(@SuppressWarnings("rawtypes") Collection salesAreas)  throws RemoteException;

}

