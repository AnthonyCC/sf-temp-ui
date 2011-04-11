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

}
