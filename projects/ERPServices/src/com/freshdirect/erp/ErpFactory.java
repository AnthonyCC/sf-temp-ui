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
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.content.attributes.GetAttributesErpVisitor;
import com.freshdirect.content.attributes.GetRootNodesErpVisitor;
import com.freshdirect.content.attributes.SetAttributesErpVisitor;
import com.freshdirect.content.attributes.ejb.AttributeFacadeHome;
import com.freshdirect.content.attributes.ejb.AttributeFacadeSB;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.ecomm.gateway.ErpInfoService;
import com.freshdirect.ecomm.gateway.ErpNutritionService;
import com.freshdirect.ecomm.gateway.ErpNutritionServiceI;
import com.freshdirect.erp.ejb.BatchManagerHome;
import com.freshdirect.erp.ejb.BatchManagerSB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.ejb.ErpClassEB;
import com.freshdirect.erp.ejb.ErpClassHome;
import com.freshdirect.erp.ejb.ErpGrpInfoHome;
import com.freshdirect.erp.ejb.ErpGrpInfoSB;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.ejb.ErpMaterialEB;
import com.freshdirect.erp.ejb.ErpMaterialHome;
import com.freshdirect.erp.ejb.ErpProductEB;
import com.freshdirect.erp.ejb.ErpProductHome;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.VersionedPrimaryKey;
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

	private ErpInfoHome erpInfoHome = null;

	public Collection findMaterialsBySapId(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsBySapId(searchterm);
			else
				return infoSB.findMaterialsBySapId(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsBySku(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsBySku(searchterm);
			else
				return infoSB.findMaterialsBySku(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByDescription(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsByDescription(searchterm);
			else
			return infoSB.findMaterialsByDescription(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByClass(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsByClass(searchterm);
			else
				return infoSB.findMaterialsByClass(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByCharacteristic(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsByCharacteristic(searchterm);
			else
			return infoSB.findMaterialsByCharacteristic(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findMaterialsByBatch(int batchNum) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findMaterialsByBatch(batchNum);
			else
				return infoSB.findMaterialsByBatch(batchNum);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsBySapId(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsBySapId(searchterm);
			else
				return infoSB.findProductsBySapId(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public ErpProductInfoModel findProductBySku(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			try {
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
					return ErpInfoService.getInstance().findProductBySku(searchterm);
				else
					return infoSB.findProductBySku(searchterm);
			} catch (ObjectNotFoundException onfe) {
				return null;
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsLikeSku(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsLikeSku(searchterm);
			else
				return infoSB.findProductsLikeSku(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsByDescription(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsByDescription(searchterm);
			else
				return infoSB.findProductsByDescription(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsLikeUPC(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsLikeUPC(searchterm);
			else
				return infoSB.findProductsLikeUPC(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsByUPC(upc);
			else
				return infoSB.findProductsByUPC(upc);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB"))
				return ErpInfoService.getInstance().findProductsByCustomerUPC(erpCustomerPK,upc);
			else
			return infoSB.findProductsByCustomerUPC(erpCustomerPK, upc);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupInfoHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			erpInfoHome = (ErpInfoHome) ctx.lookup(ErpServicesProperties.getInfoHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

	private ErpMaterialHome materialHome = null;

	public ErpMaterialModel getMaterial(String sapId) throws FDResourceException {
		if (materialHome == null) {
			lookupMaterialHome();
		}
		try {
			ErpMaterialEB matlEB = materialHome.findBySapId(sapId);
			ErpMaterialModel materialModel = (ErpMaterialModel) matlEB.getModel();
			applyAttributes(materialModel);
			return materialModel;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupMaterialHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			materialHome = (ErpMaterialHome) ctx.lookup(ErpServicesProperties.getMaterialHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

	private ErpProductHome productHome = null;

	public ErpProductModel getProduct(String skuCode) throws FDResourceException {
		if (productHome == null) {
			lookupProductHome();
		}
		try {
			ErpProductModel productModel = null;
			try {
				ErpProductEB prodEB = productHome.findBySkuCode(skuCode);
				productModel = (ErpProductModel) prodEB.getModel();
				applyAttributes(productModel);
			} catch (ObjectNotFoundException onfe) {
				// no such product for this sku, just return an empty product object
				productModel = new ErpProductModel();
				productModel.setAttributes(new AttributeCollection());
			}
			
			return productModel;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
    
	
	private void lookupProductHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			productHome = (ErpProductHome) ctx.lookup(ErpServicesProperties.getProductHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
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

	private AttributeFacadeHome attributeHome = null;

	public void applyAttributes(ErpModelSupport erpModel) throws FDResourceException {
		if (attributeHome == null) {
			lookupAttributesHome();
		}
		try {
			//
			// get the attributes for this erpmodel and its children
			//
			FlatAttributeCollection attrs = null;
			GetRootNodesErpVisitor idVisitor = new GetRootNodesErpVisitor();
			erpModel.accept(idVisitor);
			String[] rootIds = idVisitor.getRootIds();
			AttributeFacadeSB atrSB = attributeHome.create();
			attrs = atrSB.getAttributes(rootIds);
			//
			// apply attributes to the erpmodel and its children
			//
			erpModel.accept(new SetAttributesErpVisitor(attrs));
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (AttributeException ae) {
			throw new FDResourceException(ae);
		}
	}

	public void saveAttributes(ErpModelSupport erpModel, String user, String sapId) throws FDResourceException {
		if (attributeHome == null) {
			lookupAttributesHome();
		}
		//
		// get all the attributes from the erpObject
		//
		GetAttributesErpVisitor atrGetVisitor = new GetAttributesErpVisitor();
		erpModel.accept(atrGetVisitor);
		FlatAttributeCollection attrs = atrGetVisitor.getAttributes();

		AttributeFacadeSB atrSB = null;
		try {
			//
			// find the attributes session bean
			//
			atrSB = attributeHome.create();
			//
			// ask it to save the attributes
			//
			
			atrSB.storeAttributes(attrs, user, sapId);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (AttributeException ae) {
			throw new FDResourceException(ae);
		} finally {
			if (atrSB != null) {
				try {
					//
					// read them back again to make sure eveything worked
					//
					GetRootNodesErpVisitor idVisitor = new GetRootNodesErpVisitor();
					erpModel.accept(idVisitor);
					String[] rootIds = idVisitor.getRootIds();
					attrs = atrSB.getAttributes(rootIds);
					///JJ
					//
					// and re-apply attributes to the erpObject and its characteristics
					//
					erpModel.accept(new SetAttributesErpVisitor(attrs));

				} catch (AttributeException ae) {
					throw new FDResourceException(ae);
				} catch (RemoteException re) {
					throw new FDResourceException(re);
				}
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

	private void lookupAttributesHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			attributeHome = (AttributeFacadeHome) ctx.lookup(ErpServicesProperties.getAttributesHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
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
	
	private ErpGrpInfoHome erpGrpInfoHome = null;

	public Collection<FDGroup> findGrpsForMaterial(String matId) throws FDResourceException {
		if (erpGrpInfoHome == null) {
			lookupGrpInfoHome();
		}
		try {
			ErpGrpInfoSB remote = erpGrpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpGrpInfoSB")){

				return FDECommerceService.getInstance().findGrpsForMaterial(matId);
			}else{
				return remote.findGrpsForMaterial(matId);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	private void lookupGrpInfoHome() throws FDResourceException {
		if (erpGrpInfoHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			erpGrpInfoHome = (ErpGrpInfoHome) ctx.lookup("freshdirect.erp.GrpInfoManager");
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}
	
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB")){
				ErpInfoService.getInstance().setOverriddenBackInStock(sku, salesAreaOverrides);
			}else{
				infoSB.setOverriddenBackInStock(sku, salesAreaOverrides);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB")){
				ErpInfoService.getInstance().setOverriddenNewness(sku, salesAreaOverrides);
			}else{
				infoSB.setOverriddenNewness(sku, salesAreaOverrides);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String,String> getOverriddenBackInStock(String sku) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB")){
				return ErpInfoService.getInstance().getOverriddenBackInStock(sku);
			}else{
			 return infoSB.getOverriddenBackInStock(sku);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public Map<String,String> getOverriddenNewness(String sku) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInfoSB")){
				return ErpInfoService.getInstance().getOverriddenNewness(sku);
			}else{
			 return infoSB.getOverriddenNewness(sku);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

}