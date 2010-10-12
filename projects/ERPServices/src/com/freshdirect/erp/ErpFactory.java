/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2003 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import com.freshdirect.erp.ejb.BatchManagerHome;
import com.freshdirect.erp.ejb.BatchManagerSB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.ejb.ErpClassEB;
import com.freshdirect.erp.ejb.ErpClassHome;
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
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * Singleton class for accessing the ERP-layer remote objects.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpFactory {

	private ErpFactory() {
		super();
	}

	private static ErpFactory factory = null;

	public static ErpFactory getInstance() {
		if (factory == null) {
			factory = new ErpFactory();
		}
		return factory;
	}

	private List nutritionReport = null;

	private long REFRESH_PERIOD = 1000 * 60 * 15; // 15 minutes
	private long lastRefresh = 0;

	private ErpInfoHome erpInfoHome = null;

	public Collection findMaterialsBySapId(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
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
			return infoSB.findProductsLikeUPC(searchterm);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection findProductsByUPC(String searchterm) throws FDResourceException {
		if (erpInfoHome == null) {
			lookupInfoHome();
		}
		try {
			ErpInfoSB infoSB = erpInfoHome.create();
			return infoSB.findProductsByUPC(searchterm);
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

	private ErpCharacteristicValuePriceHome cvpHome = null;

	public ErpCharacteristicValuePriceModel getCharacteristicValuePrice(
		ErpMaterialModel material,
		ErpCharacteristicValueModel charValue)
		throws FDResourceException {
		if (cvpHome == null) {
			lookupCVPriceHome();
		}
		try {
			ErpCharacteristicValuePriceEB charValEB =
				cvpHome.findByMaterialAndCharValue((VersionedPrimaryKey) material.getPK(), (VersionedPrimaryKey) charValue.getPK());
			return (ErpCharacteristicValuePriceModel) charValEB.getModel();
		} catch (ObjectNotFoundException onfe) {
			//
			// not found, price is zero...
			//
			ErpCharacteristicValuePriceModel charValuePrice = new ErpCharacteristicValuePriceModel();
			charValuePrice.setCharacteristicValueId(charValue.getPK().getId());
			charValuePrice.setConditionType("");
			charValuePrice.setMaterialId(material.getPK().getId());
			charValuePrice.setPrice(0.0);
			charValuePrice.setPricingUnit("");
			charValuePrice.setSapId("");
			return charValuePrice;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private void lookupCVPriceHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			cvpHome = (ErpCharacteristicValuePriceHome) ctx.lookup(ErpServicesProperties.getCharacteristicValuePriceHome());
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
			BatchManagerSB batchSB = batchHome.create();
			BatchModel bm = (BatchModel) batchSB.getBatch(batchId);
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
			BatchManagerSB batchSB = batchHome.create();
			return batchSB.getRecentBatches();
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

	public Collection getClasses() throws FDResourceException {
		if (classHome == null) {
			lookupClassHome();
		}
		try {
			LinkedList models = new LinkedList();
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
			AttributeFacadeSB atrSB = attributeHome.create();
			GetRootNodesErpVisitor idVisitor = new GetRootNodesErpVisitor();
			erpModel.accept(idVisitor);
			String[] rootIds = idVisitor.getRootIds();
			FlatAttributeCollection attrs = atrSB.getAttributes(rootIds);
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

	public void saveAttributes(ErpModelSupport erpModel) throws FDResourceException {
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
			atrSB.storeAttributes(attrs);
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

	public List generateNutritionReport() throws FDResourceException {
		this.refreshNutritionReportCache();
		return this.nutritionReport;
	}

	public List generateClaimsReport() throws FDResourceException {
		try {
			if (nutritionHome == null) {
				lookupNutritionHome();
			}
			ErpNutritionSB nutrSB = nutritionHome.create();
			return nutrSB.generateClaimsReport();
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

				ErpNutritionSB nutrSB = nutritionHome.create();
				this.nutritionReport = nutrSB.generateNutritionReport();

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
			return nutrSB.getNutrition(skuCode);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public void saveNutrition(ErpNutritionModel nutrition) throws FDResourceException {
		if (nutritionHome == null) {
			lookupNutritionHome();
		}
		try {
			ErpNutritionSB nutrSB = nutritionHome.create();
			nutrSB.updateNutrition(nutrition);
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

}
