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
/**
 *@deprecated Please use the ErpNutritionController and ErpNutritionServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpNutritionSB extends EJBObject{

	@Deprecated public ErpNutritionModel getNutrition(String skuCode) throws RemoteException;
	@Deprecated public Map<String, ErpNutritionModel> loadNutrition(Date lastModified) throws RemoteException;
	@Deprecated public void createNutrition(ErpNutritionModel nutrition) throws RemoteException;
	@Deprecated public void updateNutrition(ErpNutritionModel nutrition, String user) throws RemoteException;
	@Deprecated public void removeNutrition(String skuCode) throws RemoteException;
	@Deprecated public void createUpcSkuMapping(String skuCode, String upc) throws RemoteException;
	@Deprecated public String getSkuCodeForUpc(String upc) throws FinderException, RemoteException;
	@Deprecated public List<Map<String, String>> generateNutritionReport() throws RemoteException;
	@Deprecated public List<Map<String, String>> generateClaimsReport() throws RemoteException;
	@Deprecated public Map<String, NutritionPanel> loadNutritionPanels(Date lastModified) throws RemoteException;
	@Deprecated public NutritionPanel getNutritionPanel(String skuCode) throws RemoteException;
	@Deprecated public void saveNutritionPanel(NutritionPanel panel) throws RemoteException, FDResourceException;
	@Deprecated public void deleteNutritionPanel(String skuCode) throws RemoteException, FDResourceException;

}
