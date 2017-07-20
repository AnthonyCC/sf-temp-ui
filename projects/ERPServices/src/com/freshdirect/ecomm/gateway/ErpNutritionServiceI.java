package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;

public interface ErpNutritionServiceI {
	
	public void createNutrition(ErpNutritionModel buildNutritionModel) throws  RemoteException;

	public ErpNutritionModel getNutrition(String skuCode) throws  RemoteException;

	public NutritionPanel getNutritionPanel(String skuCode) throws RemoteException;

	public String getSkuCodeForUpc(String upc) throws RemoteException;

	public Map<String, ErpNutritionModel> loadNutrition(Date lastModified) throws RemoteException;

	public Map<String, NutritionPanel> loadNutritionPanels(Date lastModified) throws RemoteException;

	public void createUpcSkuMapping(String skuCode, String upcCode)	throws RemoteException;

	public void saveNutritionPanel(NutritionPanel nutritionModel)throws RemoteException;

	public void deleteNutritionPanel(String skuCode) throws RemoteException;
	public void updateNutrition(ErpNutritionModel enm, String string) throws RemoteException;

	public void removeNutrition(String skuCode) throws RemoteException;

	public List<Map<String, String>> generateNutritionReport()throws RemoteException;

	public List<Map<String, String>> generateClaimsReport()throws RemoteException;

}
