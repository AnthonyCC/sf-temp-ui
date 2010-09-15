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

import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.util.DealsHelper;
import com.freshdirect.content.attributes.AttributeComparator;
import com.freshdirect.content.attributes.ErpIdPathVisitor;
import com.freshdirect.content.attributes.ErpsAttributeContainerI;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
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

	private final static Category LOGGER = LoggerFactory.getInstance(FDProductHelper.class);
	private final static boolean DEBUG = false;

	private transient ErpCharacteristicValuePriceHome charValueHome = null;
	private transient ErpNutritionHome nutritionHome = null;

	public FDProduct getFDProduct(ErpProductModel product) throws FDResourceException {
		// debug
		if (DEBUG)
			LOGGER.debug(new DebugVisitor(product));

		// load attributes for tree
		product.accept(new ErpIdPathVisitor());

		// construct sales units
		FDSalesUnit[] salesUnits = this.getSalesUnits(product.getSalesUnits());

		// construct variations
		FDVariation[] variations = this.getVariations(product.getCharacteristics());

		// build Pricing object
		Pricing pricing = this.getPricing(product.getProxiedMaterial());

		// get version
		int version = ((VersionedPrimaryKey) product.getPK()).getVersion();

		// get nutritional information
		ErpNutritionModel nutrModel = FDNutritionCache.getInstance().getNutrition(product.getSkuCode());

		// construct display sales units
		FDSalesUnit[] displaySalesUnits = this.getSalesUnits(product.getDisplaySalesUnits());

		// create FDMaterial
		ErpMaterialModel material = product.getProxiedMaterial();
		FDMaterial fdMaterial = new FDMaterial(material);

		// construct FDProduct
		return new FDProduct(product.getSkuCode(), version, product.getPricingDate(), fdMaterial, variations, salesUnits, pricing,
				displaySalesUnits, nutrModel);
	}

	public FDProductInfo getFDProductInfo(ErpProductInfoModel erpProductInfo) throws FDResourceException {

		String s = erpProductInfo.getUnavailabilityStatus();
		EnumAvailabilityStatus status = null;
		if ("TEST".equals(s)) {
			status = EnumAvailabilityStatus.DISCONTINUED;
		} else {
			status = (EnumAvailabilityStatus) NVL.apply(EnumAvailabilityStatus.getEnumByStatusCode(erpProductInfo
					.getUnavailabilityStatus()), EnumAvailabilityStatus.AVAILABLE);
		}

		List<ErpMaterialPrice> matPrices = Arrays.asList(erpProductInfo.getMaterialPrices());
		Collections.sort(matPrices, PricingFactory.erpMatpriceComparator);

		ZonePriceInfoListing zonePriceInfoList = new ZonePriceInfoListing();
		String pricingUnit = null;
		if (erpProductInfo.getUnavailabilityStatus() == null
				|| !erpProductInfo.getUnavailabilityStatus().equals(EnumAvailabilityStatus.DISCONTINUED)) {
			// Form zone price listing only if product is not discontinued.
			String sapZoneId = "";
			List<ErpMaterialPrice> subList = new ArrayList<ErpMaterialPrice>();
			for (Iterator<ErpMaterialPrice> it = matPrices.iterator(); it.hasNext();) {
				ErpMaterialPrice matPrice = it.next();

				if (sapZoneId.length() == 0 || sapZoneId.equals(matPrice.getSapZoneId())) {
					subList.add(matPrice);
				} else if (!sapZoneId.equals(matPrice.getSapZoneId())) {
					Collections.sort(subList, matlPriceComparator);
					pricingUnit = buildZonePriceInfoList(zonePriceInfoList, subList, erpProductInfo.getSkuCode(), sapZoneId, pricingUnit);
					subList.add(matPrice);
				}
				sapZoneId = matPrice.getSapZoneId();
			}
			// Do the same for the last zone in the list.
			pricingUnit = buildZonePriceInfoList(zonePriceInfoList, subList, erpProductInfo.getSkuCode(), sapZoneId, pricingUnit);
		}
		if (pricingUnit == null)
			throw new FDResourceException("improper SKU (no price unit in material prices for master default zone): " + erpProductInfo.getSkuCode());
		return new FDProductInfo(erpProductInfo.getSkuCode(), erpProductInfo.getVersion(), erpProductInfo.getMaterialSapIds(),
				erpProductInfo.getATPRule(), status, erpProductInfo.getUnavailabilityDate(), null, erpProductInfo.getRating(),
				erpProductInfo.getFreshness(), pricingUnit, zonePriceInfoList);

	}

	private String buildZonePriceInfoList(ZonePriceInfoListing zonePriceInfoList, List<ErpMaterialPrice> subList,
			String skuCode, String sapZoneId, String pricingUnit) {
		if (sapZoneId.equals(PricingContext.DEFAULT.getZoneId()))
			if (subList.size() > 0)
				pricingUnit = subList.get(0).getUnit();
		ZonePriceInfoModel zpInfoModel = buildZonePriceInfo(skuCode, subList, sapZoneId);
		zonePriceInfoList.addZonePriceInfo(sapZoneId, zpInfoModel);
		subList.clear();
		return pricingUnit;
	}

	private Comparator<ErpMaterialPrice> matlPriceComparator = new Comparator<ErpMaterialPrice>() {
		public int compare(ErpMaterialPrice price1, ErpMaterialPrice price2) {
			if (price1.getScaleQuantity() == price2.getScaleQuantity())
				return 0;
			else if (price1.getScaleQuantity() < price2.getScaleQuantity())
				return -1;
			else
				return 1;
		}
	};

	private ZonePriceInfoModel buildZonePriceInfo(String skuCode, List<ErpMaterialPrice> matPriceList, String sapZoneId) {
		double defaultPrice = 0.0;
		double promoPrice = 0.0;
		if (matPriceList == null || matPriceList.size() == 0)
			return null;

		// Form zone price listing only if product is not discontinued.

		if (matPriceList.size() > 1) {
			List<ErpMaterialPrice> newSubList = new ArrayList<ErpMaterialPrice>();
			ErpMaterialPrice basePrice = matPriceList.get(0);
			newSubList.add(basePrice);
			for (int i = 1; i < matPriceList.size(); i++) {
				ErpProductInfoModel.ErpMaterialPrice nextPrice = (ErpProductInfoModel.ErpMaterialPrice) matPriceList.get(i);
				// ErpMaterialPriceModel
				// nextPrice=(ErpMaterialPriceModel)erpPrices[i];
				if (basePrice.getPromoPrice() > 0) {
					if (basePrice.getPromoPrice() >= nextPrice.getPrice())
						newSubList.add(nextPrice);
				} else if (basePrice.getPrice() >= nextPrice.getPrice()) {
					newSubList.add(nextPrice);
				} else {
					LOGGER.debug("scale price is less then promo price :" + sapZoneId);
				}

				if (nextPrice.getPrice() <= basePrice.getPrice()) {
					newSubList.add(basePrice);
				}
			}
			matPriceList = newSubList;
		}

		ErpMaterialPrice matPrice = (ErpMaterialPrice) matPriceList.get(0);
		if (matPrice.getScaleUnit().length() == 0) {
			// no scales
			defaultPrice = matPrice.getPrice();
			promoPrice = matPrice.getPromoPrice();
		} else {
			// has scales. sort by scale quantity and take lowest scale quantity

			// Collections.sort(matPriceList,
			// PricingFactory.scaleQuantityComparator);
			// Get the lowet scale quantity element from the list which is the
			// first element after sorting.
			matPrice = (ErpProductInfoModel.ErpMaterialPrice) matPriceList.get(0);
			defaultPrice = matPrice.getPrice();
			promoPrice = matPrice.getPromoPrice();
		}

		boolean itemOnSale = DealsHelper.isItemOnSale(defaultPrice, promoPrice);
		;
		int dealsPercentage = 0;
		if (itemOnSale) {
			// LOGGER.debug("has was price for SKU " +
			// erpProductInfo.getSkuCode());
			dealsPercentage = Math.max(DealsHelper.getVariancePercentage(defaultPrice, promoPrice), 0);
		}

		boolean isShowBurstImage = DealsHelper.isShowBurstImage(defaultPrice, promoPrice);
		;

		ErpMaterialPrice[] matPrices = (ErpMaterialPrice[]) matPriceList.toArray(new ErpMaterialPrice[0]);
		int tieredDeal = DealsHelper.determineTieredDeal(defaultPrice, matPrices);
		if (tieredDeal > 0 && DealsHelper.isDealOutOfBounds(tieredDeal)) {
			// LOGGER.debug("tiered deal is out of bounds for SKU " +
			// erpProductInfo.getSkuCode());
			tieredDeal = 0;
		}
		return new ZonePriceInfoModel(defaultPrice, promoPrice, itemOnSale, dealsPercentage, tieredDeal, sapZoneId,
				isShowBurstImage);
	}

	protected ErpNutritionModel getNutrition(ErpProductModel product) throws FDResourceException {
		if (this.nutritionHome == null) {
			this.lookupNutritionHome();
		}
		try {
			ErpNutritionSB sb = this.nutritionHome.create();
			ErpNutritionModel nutr = sb.getNutrition(product.getSkuCode());
			return nutr;
		} catch (CreateException ce) {
			this.nutritionHome = null;
			throw new FDResourceException(ce, "Error creating ErpNutrition session bean");
		} catch (RemoteException re) {
			this.nutritionHome = null;
			throw new FDResourceException(re, "Error talking to ErpNutrition session bean");
		}
	}

	private static class SalesUnitComparator implements Comparator<ErpSalesUnitModel> {
		public int compare(ErpSalesUnitModel su1, ErpSalesUnitModel su2) {
			double ratio1 = ((double) su1.getNumerator()) / ((double) su1.getDenominator());
			double ratio2 = ((double) su2.getNumerator()) / ((double) su2.getDenominator());
			if (ratio1 < ratio2)
				return -1;
			else if (ratio1 > ratio2)
				return 1;
			else
				return 0;
		}
	}

	protected FDSalesUnit[] getSalesUnits(List<ErpSalesUnitModel> erpSalesUnits) {
		List<ErpSalesUnitModel> suList = new ArrayList<ErpSalesUnitModel>(erpSalesUnits);

		Collections.sort(suList, new SalesUnitComparator());

		FDSalesUnit[] salesUnits = new FDSalesUnit[suList.size()];
		for (int i = 0; i < salesUnits.length; i++) {
			ErpSalesUnitModel su = suList.get(i);
			if (DEBUG)
				LOGGER.debug("Setting SalesUnit attributes from " + su);

			// get attributes
			// salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(),
			// su.getDescription());
			salesUnits[i] = new FDSalesUnit(su);
		}
		return salesUnits;
	}

	protected FDVariation[] getVariations(List<ErpCharacteristicModel> chList) {
		FDVariation[] variations = new FDVariation[chList.size()];
		for (int i = 0; i < variations.length; i++) {
			ErpCharacteristicModel ch = chList.get(i);

			if (DEBUG)
				LOGGER.debug("Setting Variation attributes from " + ch);

			variations[i] = new FDVariation(ch, this.getVariationOptions(ch));
		}
		return variations;
	}

	private static class VariationOptionComparator extends AttributeComparator.Priority {
	    @Override
		public int compare(ErpsAttributeContainerI o1, ErpsAttributeContainerI o2) {
			int prio = super.compare(o1, o2);
			if (prio != 0) {
				return prio;
			}
			String a1 = ((ErpCharacteristicValueModel) o1).getDescription();
			String a2 = ((ErpCharacteristicValueModel) o2).getDescription();
			return a1.compareTo(a2);
		}
	}

	protected FDVariationOption[] getVariationOptions(ErpCharacteristicModel characteristic) {
		@SuppressWarnings("unchecked")
		List<ErpCharacteristicValueModel> cvList = new ArrayList<ErpCharacteristicValueModel>(characteristic.getCharacteristicValues());

		Collections.sort(cvList, new VariationOptionComparator());

		FDVariationOption[] variationOptions = new FDVariationOption[cvList.size()];
		for (int i = 0; i < variationOptions.length; i++) {
			ErpCharacteristicValueModel cv = (ErpCharacteristicValueModel) cvList.get(i);

			if (DEBUG)
				LOGGER.debug("Setting VariationOption attributes from " + cv);

			variationOptions[i] = new FDVariationOption(cv);
		}
		return variationOptions;
	}

	protected Pricing getPricing(ErpMaterialModel material) throws FDResourceException {
		if (this.charValueHome == null) {
			this.lookupCharValueHome();
		}
		try {
			@SuppressWarnings("unchecked")
			Collection<ErpCharacteristicValuePriceEB> cvPriceEBs = this.charValueHome.findByMaterial((VersionedPrimaryKey) material.getPK());
			ErpCharacteristicValuePriceModel[] cvPrices = new ErpCharacteristicValuePriceModel[cvPriceEBs.size()];

			// create model array
			int count = 0;
			for (Iterator<ErpCharacteristicValuePriceEB> i = cvPriceEBs.iterator(); i.hasNext(); count++) {
				cvPrices[count] = (ErpCharacteristicValuePriceModel) i.next().getModel();
			}

			return PricingFactory.getPricing(material, cvPrices);
		} catch (RemoteException ex) {
			this.charValueHome = null;
			throw new FDResourceException(ex);
		} catch (FinderException ex) {
			this.charValueHome = null;
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
			} catch (NamingException ne) {
			}
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
			} catch (NamingException ne) {
			}
		}
	}

}
