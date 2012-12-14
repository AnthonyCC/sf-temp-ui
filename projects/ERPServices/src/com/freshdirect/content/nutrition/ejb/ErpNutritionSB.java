/*
 * ErpNutritionSB.java
 *
 * Created on August 17, 2001, 5:16 PM
 */

package com.freshdirect.content.nutrition.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.fdstore.FDResourceException;

public interface ErpNutritionSB extends EJBObject{

	 public ErpNutritionModel getNutrition(String skuCode) throws RemoteException;
	 public Map<String, ErpNutritionModel> loadNutrition(Date lastModified) throws RemoteException;
	 public void createNutrition(ErpNutritionModel nutrition) throws RemoteException;
	 public void updateNutrition(ErpNutritionModel nutrition, String user) throws RemoteException;
	 public void removeNutrition(String skuCode) throws RemoteException;
	 public void createUpcSkuMapping(String skuCode, String upc) throws RemoteException;
	 public String getSkuCodeForUpc(String upc) throws FinderException, RemoteException;
	 public List<Map<String, String>> generateNutritionReport() throws RemoteException;
	 public List<Map<String, String>> generateClaimsReport() throws RemoteException;
	 public Map<String, NutritionPanel> loadNutritionPanels(Date lastModified) throws RemoteException;
	 public NutritionPanel getNutritionPanel(String skuCode) throws RemoteException;
	 public void saveNutritionPanel(NutritionPanel panel) throws RemoteException, FDResourceException;
	 public void deleteNutritionPanel(String skuCode) throws RemoteException, FDResourceException;

}
