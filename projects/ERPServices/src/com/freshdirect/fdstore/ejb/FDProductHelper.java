/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.util.DealsHelper;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.AttributeComparator;
import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.content.attributes.GetRootNodesErpVisitor;
import com.freshdirect.content.attributes.SetAttributesErpVisitor;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class FDProductHelper {

	private final static Category LOGGER = LoggerFactory.getInstance( FDProductHelper.class );
	private final static boolean DEBUG = false;

	private transient ErpCharacteristicValuePriceHome charValueHome = null;
	private transient ErpNutritionHome nutritionHome = null;

	public FDProduct getFDProduct(ErpProductModel product) throws FDResourceException {
		// debug
		if (DEBUG) LOGGER.debug( new DebugVisitor(product) );

		// load attributes for tree
		this.bindAttributes( product );

		// construct sales units
		FDSalesUnit[] salesUnits = this.getSalesUnits(product.getSalesUnits());

		// construct variations
		FDVariation[] variations = this.getVariations(product.getCharacteristics());
		
		// build Pricing object
		Pricing pricing = this.getPricing(product.getProxiedMaterial());
		
		// get version
		int version = ((VersionedPrimaryKey)product.getPK()).getVersion();
		
		// get attributes for material
		AttributesI attribs = new AttributeCollection(product.getProxiedMaterial().getAttributes());
		
		// get nutritional information
		ErpNutritionModel nutrModel = FDNutritionCache.getInstance().getNutrition(product.getSkuCode());

		// construct display sales units
		FDSalesUnit[] displaySalesUnits = this.getSalesUnits(product.getDisplaySalesUnits());
		


		// create FDMaterial
		ErpMaterialModel material = product.getProxiedMaterial();
		FDMaterial fdMaterial =
			new FDMaterial(
				attribs,
				material.getSapId(),
				material.getATPRule(),
				material.getSalesUnitCharacteristic(),
				material.getQuantityCharacteristic(),
				material.getAlcoholicContent(),
				material.isTaxable(),
				material.isKosherProduction(),
				material.isPlatter(),
				material.getBlockedDays());
		
		// Construct FDNutrition from ErpNutritionModel.value map
		ArrayList nutrition = new ArrayList();
		for (Iterator nIter = nutrModel.getKeyIterator(); nIter.hasNext(); ) {
			String key = (String) nIter.next();
			FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(), nutrModel.getValueFor(key), nutrModel.getUomFor(key));
			nutrition.add(fdn);
		}

		// construct FDProduct
		return new FDProduct(product.getSkuCode(), version, product.getPricingDate(), fdMaterial, variations, salesUnits, pricing, nutrition,displaySalesUnits);
	}
	
	public FDProductInfo getFDProductInfo(ErpProductInfoModel erpProductInfo) throws FDResourceException {


		String s = erpProductInfo.getUnavailabilityStatus();
		EnumAvailabilityStatus status = null;
		if ("TEST".equals(s)){
			status = EnumAvailabilityStatus.DISCONTINUED;
		} else {
			status = (EnumAvailabilityStatus) NVL.apply(EnumAvailabilityStatus.getEnumByStatusCode(erpProductInfo.getUnavailabilityStatus()),
				EnumAvailabilityStatus.AVAILABLE);
		}
		

		List<ErpMaterialPrice> matPrices =Arrays.asList(erpProductInfo.getMaterialPrices());
		Collections.sort(matPrices, PricingFactory.erpMatpriceComparator);
		
		ZonePriceInfoListing zonePriceInfoList = new ZonePriceInfoListing();
		if(erpProductInfo.getUnavailabilityStatus() == null || !erpProductInfo.getUnavailabilityStatus().equals(EnumAvailabilityStatus.DISCONTINUED)){
			//Form zone price listing only if product is not discontinued.
			String sapZoneId = "";
			List subList = new ArrayList<ErpMaterialPrice>();
			for(Iterator it = matPrices.iterator() ; it.hasNext();){
				ErpMaterialPrice matPrice = (ErpMaterialPrice) it.next();
				
				if(sapZoneId.length() == 0 || sapZoneId.equals(matPrice.getSapZoneId())){
					subList.add(matPrice);
				}
				else if(!sapZoneId.equals(matPrice.getSapZoneId())) {
					ZonePriceInfoModel zpInfoModel = buildZonePriceInfo(erpProductInfo.getSkuCode(), subList, sapZoneId);
					zonePriceInfoList.addZonePriceInfo(sapZoneId, zpInfoModel);
					subList.clear();
					subList.add(matPrice);
				}
				sapZoneId = matPrice.getSapZoneId();
			}
			//Do the same for the last zone in the list.
			ZonePriceInfoModel zpInfoModel = buildZonePriceInfo(erpProductInfo.getSkuCode(), subList, sapZoneId);
			zonePriceInfoList.addZonePriceInfo(sapZoneId, zpInfoModel);
			subList.clear();
		}		
		return new FDProductInfo(
			erpProductInfo.getSkuCode(),
			erpProductInfo.getVersion(),
			erpProductInfo.getMaterialSapIds(),
			erpProductInfo.getATPRule(),
			status,
			erpProductInfo.getUnavailabilityDate(),
			null,
			erpProductInfo.getRating(),
			erpProductInfo.getFreshness(),
			zonePriceInfoList
		);
	
	}

	private ZonePriceInfoModel buildZonePriceInfo(String skuCode, List matPriceList, String sapZoneId) {
		double defaultPrice=0.0;
		String defaultPriceUnit = "";
		double promoPrice = 0.0;
		if(matPriceList == null || matPriceList.size() == 0)
			return null;
		ErpMaterialPrice matPrice = (ErpMaterialPrice) matPriceList.get(0);
		if(matPrice.getScaleUnit().length() == 0){
			//no scales
			defaultPrice = matPrice.getPrice();
			defaultPriceUnit = matPrice.getUnit();
			promoPrice = matPrice.getPromoPrice();
		} else {
			//has scales. sort by scale quantity and take lowest scale quantity 
			Collections.sort(matPriceList, PricingFactory.scaleQuantityComparator);
			//Get the lowet scale quantity element from the list which is the first element after sorting.
			matPrice = (ErpProductInfoModel.ErpMaterialPrice) matPriceList.get(0);
			defaultPrice = matPrice.getPrice();
			defaultPriceUnit = matPrice.getUnit();
			promoPrice = matPrice.getPromoPrice();
		}
		
		boolean itemOnSale = DealsHelper.isItemOnSale(defaultPrice,promoPrice);;
		int dealsPercentage=0;
		if(itemOnSale) {
			//LOGGER.debug("has was price for SKU " + erpProductInfo.getSkuCode());			
			dealsPercentage=Math.max(DealsHelper.getVariancePercentage(defaultPrice, promoPrice), 0);
		}
		ErpMaterialPrice[] matPrices = (ErpMaterialPrice[]) matPriceList.toArray(new ErpMaterialPrice[0]);
		int tieredDeal = DealsHelper.determineTieredDeal(defaultPrice, matPrices);
		if (tieredDeal > 0 && DealsHelper.isDealOutOfBounds(tieredDeal)) {
			//LOGGER.debug("tiered deal is out of bounds for SKU " + erpProductInfo.getSkuCode());
			tieredDeal = 0;
		}
		String[] ids = { skuCode };
		FlatAttributeCollection rawAttrs = FDAttributeCache.getInstance().getAttributes(ids);
		// SKU attributes are flat, hence no need for visitor 
		AttributeCollection attributeCollection = new AttributeCollection( rawAttrs.getAttributeMap(ids) );

		String displayablePricingUnit = attributeCollection.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), "");
		return new ZonePriceInfoModel(
				defaultPrice, 
				promoPrice, 
				defaultPriceUnit, 
				displayablePricingUnit, 
				itemOnSale, 
				dealsPercentage, 
				tieredDeal, 
				sapZoneId);
	}
	
	protected void bindAttributes(ErpProductModel product) throws FDResourceException {
		
		GetRootNodesErpVisitor rootNodesVisitor = new GetRootNodesErpVisitor(product);
		String[] rootIds = rootNodesVisitor.getRootIds();
		if (rootIds.length!=0) {
			
			FlatAttributeCollection attrz = FDAttributeCache.getInstance().getAttributes(rootIds);
			
			product.accept( new SetAttributesErpVisitor(attrz) );
		} else {
			product.accept( new SetAttributesErpVisitor(new FlatAttributeCollection()) );
		}
	}
	
	protected ErpNutritionModel getNutrition(ErpProductModel product) throws FDResourceException {
		if (this.nutritionHome==null) {
			this.lookupNutritionHome();
		}
		try {
			ErpNutritionSB sb = this.nutritionHome.create();
			ErpNutritionModel nutr = sb.getNutrition(product.getSkuCode());
			return nutr;
		} catch (CreateException ce) {
			this.nutritionHome=null;
			throw new FDResourceException(ce, "Error creating ErpNutrition session bean");
		} catch (RemoteException re) {
			this.nutritionHome=null;
			throw new FDResourceException(re, "Error talking to ErpNutrition session bean");
		}
	}

	private static class SalesUnitComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			ErpSalesUnitModel su1 = (ErpSalesUnitModel) o1;
			ErpSalesUnitModel su2 = (ErpSalesUnitModel) o2;
			double ratio1 = ((double)su1.getNumerator()) / ((double)su1.getDenominator());
			double ratio2 = ((double)su2.getNumerator()) / ((double)su2.getDenominator());
			if (ratio1 < ratio2)
				return -1;
			else if (ratio1 > ratio2)
				return 1;
			else
				return 0;
		}
	}

	protected FDSalesUnit[] getSalesUnits(List erpSalesUnits) {
		List suList = new ArrayList( erpSalesUnits );
		
		Collections.sort(suList, new SalesUnitComparator() );
		
		FDSalesUnit[] salesUnits = new FDSalesUnit[suList.size()];
		for (int i=0; i<salesUnits.length; i++) {
			ErpSalesUnitModel su = (ErpSalesUnitModel) suList.get(i);
			if (DEBUG) LOGGER.debug("Setting SalesUnit attributes from "+su);

			// get attributes
			AttributesI attribs = new AttributeCollection( su.getAttributes() );

//			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription());
			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription(),su.getNumerator(),su.getDenominator(),su.getBaseUnit());
		}
		return salesUnits;
	}

	protected FDVariation[] getVariations(List chList) {
		FDVariation[] variations = new FDVariation[chList.size()];
		for (int i=0; i<variations.length; i++) {
			ErpCharacteristicModel ch = (ErpCharacteristicModel) chList.get(i);

			if (DEBUG) LOGGER.debug("Setting Variation attributes from "+ch);
			// get attributes
			AttributesI attribs = new AttributeCollection( ch.getAttributes() );

			variations[i] = new FDVariation(attribs, ch.getName(), this.getVariationOptions(ch));
		}
		return variations;
	}

	private static class VariationOptionComparator extends AttributeComparator.Priority {
		public int compare(Object o1, Object o2) {
			int prio = super.compare(o1, o2);
			if (prio!=0) {
				return prio;
			}
			String a1 = ((ErpCharacteristicValueModel)o1).getDescription();
			String a2 = ((ErpCharacteristicValueModel)o2).getDescription();
			return a1.compareTo(a2);
		}
	}

	protected FDVariationOption[] getVariationOptions(ErpCharacteristicModel characteristic) {
		List cvList = new ArrayList( characteristic.getCharacteristicValues() );

		Collections.sort(cvList, new VariationOptionComparator() );

		FDVariationOption[] variationOptions = new FDVariationOption[cvList.size()];
		for (int i=0; i<variationOptions.length; i++) {
			ErpCharacteristicValueModel cv = (ErpCharacteristicValueModel) cvList.get(i);

			if (DEBUG) LOGGER.debug("Setting VariationOption attributes from "+cv);
			// get attributes
			AttributesI attribs = new AttributeCollection( cv.getAttributes() );

			variationOptions[i] = new FDVariationOption(attribs, cv.getName(), cv.getDescription());
		}
		return variationOptions;
	}

	protected Pricing getPricing(ErpMaterialModel material) throws FDResourceException {
		if (this.charValueHome==null) {
			this.lookupCharValueHome();
		}
		try {
			Collection cvPriceEBs = this.charValueHome.findByMaterial( (VersionedPrimaryKey)material.getPK() );
			ErpCharacteristicValuePriceModel[] cvPrices = new ErpCharacteristicValuePriceModel[cvPriceEBs.size()];
			
			// create model array
			int count=0;
			for (Iterator i=cvPriceEBs.iterator(); i.hasNext(); count++) {
				cvPrices[count]= (ErpCharacteristicValuePriceModel) ((ErpCharacteristicValuePriceEB)i.next()).getModel();
			}
	
			return PricingFactory.getPricing( material, cvPrices );
		} catch (RemoteException ex) {
			this.charValueHome=null;
			throw new FDResourceException(ex);
		} catch (FinderException ex) {
			this.charValueHome=null;
			throw new FDResourceException(ex);
		}
	}

	private void lookupCharValueHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.charValueHome = (ErpCharacteristicValuePriceHome) ctx.lookup("java:comp/env/ejb/ErpCharacteristicValuePrice");
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}
	
	private void lookupNutritionHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.nutritionHome = (ErpNutritionHome) ctx.lookup("java:comp/env/ejb/ErpNutrition");
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}


}
