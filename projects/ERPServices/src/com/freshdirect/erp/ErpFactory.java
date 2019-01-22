package com.freshdirect.erp;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.ecomm.gateway.ErpInfoService;
import com.freshdirect.ecomm.gateway.ErpNutritionService;
import com.freshdirect.ecomm.gateway.ErpNutritionServiceI;
import com.freshdirect.erp.ejb.BatchManagerHome;
import com.freshdirect.erp.ejb.BatchManagerSB;
import com.freshdirect.erp.ejb.ErpClassEB;
import com.freshdirect.erp.ejb.ErpClassHome;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.payment.service.FDECommerceService;

/**
 * Singleton class for accessing the ERP-layer remote objects.
 *
 */
public class ErpFactory {

	private ErpFactory() {
		super();
	}

	private static ErpFactory factory = null;

	public static ErpFactory getInstance() {
		if (factory == null) {
			factory = new ErpFactory();
			if(FDStoreProperties.isLocalDeployment()) {
				factory.lastRefresh = System.currentTimeMillis();
				factory.REFRESH_PERIOD = FDStoreProperties.TEN_DAYS_IN_MILLIS;
			}
		}
		return factory;
	}

	private List<Map<String, String>> nutritionReport = null;

	private long REFRESH_PERIOD = 1000 * 60 * 15; // 15 minutes
	private long lastRefresh = 0;


	public Collection findMaterialsBySapId(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsBySapId(searchterm);
		
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsBySku(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsBySku(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByDescription(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsByDescription(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByClass(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsByClass(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByCharacteristic(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsByCharacteristic(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByBatch(int batchNum) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findMaterialsByBatch(batchNum);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsBySapId(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsBySapId(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public ErpProductInfoModel findProductBySku(String searchterm) throws FDResourceException {
		
		try {
			
					return ErpInfoService.getInstance().findProductBySku(searchterm);
				
			
		} catch (ObjectNotFoundException onfe) {
			return null;
		}catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsLikeSku(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsLikeSku(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsByDescription(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsByDescription(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsLikeUPC(String searchterm) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsLikeUPC(searchterm);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsByUPC(upc);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().findProductsByCustomerUPC(erpCustomerPK,upc);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}


	private BatchManagerHome batchHome = null;

	public BatchModel getBatch(int batchId) throws FDResourceException {
		if (batchHome == null) {
			lookupBatchHome();
		}
		try {
			BatchModel bm = null;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.BatchManagerSB")){
				bm =FDECommerceService.getInstance().getBatch(batchId);
			}else{
				BatchManagerSB batchSB = batchHome.create();
				bm = (BatchModel) batchSB.getBatch(batchId);
			}
			return bm;
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection getBatches() throws FDResourceException {
		if (batchHome == null) {
			lookupBatchHome();
		}
		try {
			Collection batches = null;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.BatchManagerSB")){
				batches =FDECommerceService.getInstance().getRecentBatches();
			}else{
				BatchManagerSB batchSB = batchHome.create();
				batches = batchSB.getRecentBatches();
			}
			return batches;
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupBatchHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			batchHome = (BatchManagerHome) ctx.lookup(ErpServicesProperties.getBatchHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

	private ErpClassHome classHome = null;

	public ErpClassModel getClass(String sapId) throws FDResourceException {
		if (classHome == null) {
			lookupClassHome();
		}
		try {
			ErpClassEB classEB = classHome.findBySapId(sapId);
			ErpClassModel classModel = (ErpClassModel) classEB.getModel();
			applyAttributes(classModel);
			return classModel;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection<ErpClassModel> getClasses() throws FDResourceException {
		if (classHome == null) {
			lookupClassHome();
		}
		try {
			LinkedList<ErpClassModel> models = new LinkedList<ErpClassModel>();
			Collection classEnts = classHome.findAllClasses();
			Iterator iter = classEnts.iterator();
			while (iter.hasNext()) {
				models.add((ErpClassModel) ((ErpClassEB) iter.next()).getModel());
			}
			return models;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupClassHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			classHome = (ErpClassHome) ctx.lookup(ErpServicesProperties.getClassHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}


	public List<Map<String, String>> generateNutritionReport() throws FDResourceException {
		this.refreshNutritionReportCache();
		return this.nutritionReport;
	}

	public List<Map<String, String>> generateClaimsReport() throws FDResourceException {
		try {
			if (nutritionHome == null) {
				lookupNutritionHome();
			}
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
				ErpNutritionServiceI service = ErpNutritionService.getInstance();
				return service.generateClaimsReport();
			}else{
			ErpNutritionSB nutrSB = nutritionHome.create();
			return nutrSB.generateClaimsReport();
			}
		} catch (RemoteException ce) {
			throw new FDResourceException(ce);
		} catch (CreateException re) {
			throw new FDResourceException(re);
		}

	}

	private synchronized void refreshNutritionReportCache() throws FDResourceException {
		if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			try {
				if (nutritionHome == null) {
					lookupNutritionHome();
				}
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
					this.nutritionReport=ErpNutritionService.getInstance().generateNutritionReport();
				}else{
				ErpNutritionSB nutrSB = nutritionHome.create();
				this.nutritionReport = nutrSB.generateNutritionReport();
				}

				lastRefresh = System.currentTimeMillis();
			} catch (CreateException ce) {
				throw new FDResourceException(ce);
			} catch (RemoteException re) {
				throw new FDResourceException(re);
			}
		}
	}

	private ErpNutritionHome nutritionHome = null;

	public ErpNutritionModel getNutrition(String skuCode) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try {
			ErpNutritionSB nutrSB = nutritionHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
        		return ErpNutritionService.getInstance().getNutrition(skuCode);
        	}else{
        		return nutrSB.getNutrition(skuCode);
        	}
			
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public NutritionPanel getNutritionPanel(String skuCode) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try {
			ErpNutritionSB nutrSB = nutritionHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
        		return ErpNutritionService.getInstance().getNutritionPanel(skuCode);
        	}else{
        		return nutrSB.getNutritionPanel(skuCode);
        	}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public void saveNutrition(ErpNutritionModel nutrition, String user) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try { 
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
				ErpNutritionService.getInstance().updateNutrition(nutrition, "dataloader");
        }else {
			ErpNutritionSB nutrSB = nutritionHome.create();
			nutrSB.updateNutrition(nutrition, user);
        }
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public void saveNutritionPanel(NutritionPanel panel) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try {
			ErpNutritionSB nutrSB = nutritionHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
				ErpNutritionService.getInstance().saveNutritionPanel(panel);
			}else{
			nutrSB.saveNutritionPanel(panel);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public void deleteNutritionPanel(String skuCode) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try {
			ErpNutritionSB nutrSB = nutritionHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
				ErpNutritionService.getInstance().deleteNutritionPanel(skuCode);
			}else{
			nutrSB.deleteNutritionPanel(skuCode);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupNutritionHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			nutritionHome = (ErpNutritionHome) ctx.lookup(ErpServicesProperties.getNutritionHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}
	
	
	public Collection<FDGroup> findGrpsForMaterial(String matId) throws FDResourceException {
		
		try {
			
				return FDECommerceService.getInstance().findGrpsForMaterial(matId);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides) throws FDResourceException {
		
		try {
			
			
				ErpInfoService.getInstance().setOverriddenBackInStock(sku, salesAreaOverrides);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides) throws FDResourceException {
		
		try {
			
			
				ErpInfoService.getInstance().setOverriddenNewness(sku, salesAreaOverrides);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String,String> getOverriddenBackInStock(String sku) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().getOverriddenBackInStock(sku);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public Map<String,String> getOverriddenNewness(String sku) throws FDResourceException {
		
		try {
			
			
				return ErpInfoService.getInstance().getOverriddenNewness(sku);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

}