package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.ERPServiceLocator;
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
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.ejb.ErpGrpInfoHome;
import com.freshdirect.erp.ejb.ErpGrpInfoSB;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDStoreProperties;
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
 */
class FDProductHelper {

	private final static Category LOGGER = LoggerFactory.getInstance( FDProductHelper.class );
	private final static boolean DEBUG = false;

	private transient ErpCharacteristicValuePriceHome charValueHome = null;
	private transient ErpNutritionHome nutritionHome = null;
	private transient ErpGrpInfoHome grpHome = null;

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
		ArrayList<FDNutrition> nutrition = new ArrayList<FDNutrition>();
		for (Iterator<String> nIter = nutrModel.getKeyIterator(); nIter.hasNext(); ) {
			String key = nIter.next();
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
		} 
		else {
			status = NVL.apply(EnumAvailabilityStatus.getEnumByStatusCode(erpProductInfo.getUnavailabilityStatus()), EnumAvailabilityStatus.AVAILABLE);
			if(EnumAvailabilityStatus.OUT_OF_SEASON.equals(status)) {
				status = EnumAvailabilityStatus.DISCONTINUED;
			}
		}
		

		List<ErpMaterialPrice> matPrices = Arrays.asList(erpProductInfo.getMaterialPrices());
		Collections.sort(matPrices, PricingFactory.ERP_MAT_PRICE_COMPARATOR);

		ZonePriceInfoListing zonePriceInfoList = new ZonePriceInfoListing();
		if(erpProductInfo.getUnavailabilityStatus() == null || !erpProductInfo.getUnavailabilityStatus().equals(EnumAvailabilityStatus.DISCONTINUED)){
			//Form zone price listing only if product is not discontinued.
			String sapZoneId = "";
			List<ErpMaterialPrice> subList = new ArrayList<ErpMaterialPrice>();
			for(Iterator<ErpMaterialPrice> it = matPrices.iterator() ; it.hasNext();){
				ErpMaterialPrice matPrice = it.next();
				
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
		//Get Group Identify if applicable.
		FDGroup group = null;
		if(FDStoreProperties.isGroupScaleEnabled()) {//otherwise group will not be associated with the product.
			ErpGrpInfoSB remote;
			try {
				if (this.grpHome ==null) {
					this.lookupGroupPriceHome();
				}
				remote = this.grpHome.create();
				group = remote.getGroupIdentityForMaterial(erpProductInfo.getMaterialSapIds()[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				throw new FDResourceException( e1 );
			} catch (CreateException e1) {
				e1.printStackTrace();
				throw new FDResourceException( e1 );
			}					
		}
		 Date[] availDates = new Date[0];
		 if(FDStoreProperties.isLimitedAvailabilityEnabled()) {//otherwise group will not be associated with the product.
			ErpInfoSB remote;
			try{
				remote = getErpInfoSB();
				List<Date> deliveryDates = remote.getAvailableDeliveryDates(erpProductInfo.getMaterialSapIds()[0], FDStoreProperties.getAvailDaysInPastToLookup());
				if(deliveryDates != null && !deliveryDates.isEmpty())
					availDates = (Date[])deliveryDates.toArray(new Date[deliveryDates.size()]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				throw new FDResourceException( e1 );
			} 	
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
			zonePriceInfoList,
			group,
			erpProductInfo.getSustainabilityRating(),
			erpProductInfo.getUpc(), availDates);
	}
	
	 private Comparator<ErpMaterialPrice> matlPriceComparator = new Comparator<ErpMaterialPrice>() {
	        public int compare(ErpMaterialPrice price1, ErpMaterialPrice price2) {
	            if (price1.getScaleQuantity() == price2.getScaleQuantity()) return 0;
	            else if (price1.getScaleQuantity() < price2.getScaleQuantity()) return -1;
	            else return 1;
	        }
	    };

	private ZonePriceInfoModel buildZonePriceInfo(String skuCode, List<ErpMaterialPrice> matPriceList, String sapZoneId) {
		double defaultPrice=0.0;
		String defaultPriceUnit = "";
		double promoPrice = 0.0;
		if(matPriceList == null || matPriceList.size() == 0)
			return null;
						
//		Form zone price listing only if product is not discontinued.		
		
		if(matPriceList.size()>1)
		{
			List<ErpMaterialPrice> newSubList=new ArrayList<ErpMaterialPrice>();
			Collections.sort(matPriceList, matlPriceComparator);												
			ErpMaterialPrice basePrice=matPriceList.get(0);
			newSubList.add(basePrice);						
			for(int i=1;i<matPriceList.size();i++){								
				ErpProductInfoModel.ErpMaterialPrice nextPrice=matPriceList.get(i);								
				//ErpMaterialPriceModel nextPrice=(ErpMaterialPriceModel)erpPrices[i];
				if(basePrice.getPromoPrice()>0){
				    if(basePrice.getPromoPrice()>=nextPrice.getPrice())
					       newSubList.add(nextPrice);
				}else if(basePrice.getPrice() >=nextPrice.getPrice()){
					newSubList.add(nextPrice);
				}else{
					LOGGER.debug("scale price is less then promo price :"+sapZoneId);
				}
				
				if(nextPrice.getPrice()<=basePrice.getPrice()){
					newSubList.add(basePrice);
				}
			}
			matPriceList=newSubList;
		}										 						
		
		ErpMaterialPrice matPrice = matPriceList.get(0);
		if(matPrice.getScaleUnit().length() == 0){
			//no scales
			defaultPrice = matPrice.getPrice();
			defaultPriceUnit = matPrice.getUnit();
			promoPrice = matPrice.getPromoPrice();
		} else {
			//has scales. sort by scale quantity and take lowest scale quantity
											
			//Collections.sort(matPriceList, PricingFactory.scaleQuantityComparator);
			//Get the lowet scale quantity element from the list which is the first element after sorting.
			matPrice = matPriceList.get(0);
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
		
		boolean isShowBurstImage = DealsHelper.isShowBurstImage(defaultPrice,promoPrice);;
		
		ErpMaterialPrice[] matPrices = matPriceList.toArray(new ErpMaterialPrice[0]);
		int tieredDeal = DealsHelper.determineTieredDeal(defaultPrice, matPrices);
		if (tieredDeal > 0 && DealsHelper.isDealOutOfBounds(tieredDeal)) {
			//LOGGER.debug("tiered deal is out of bounds for SKU " + erpProductInfo.getSkuCode());
			tieredDeal = 0;
		}
		if ( tieredDeal > 0 ) {
			isShowBurstImage = true;
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
				sapZoneId,
				isShowBurstImage
				);
	}
	
	protected void bindAttributes(ErpProductModel product) {
		
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

	private static class SalesUnitComparator implements Comparator<ErpSalesUnitModel> {
		public int compare(ErpSalesUnitModel su1, ErpSalesUnitModel su2) {
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

	protected FDSalesUnit[] getSalesUnits(List<ErpSalesUnitModel> erpSalesUnits) {
		List<ErpSalesUnitModel> suList = new ArrayList<ErpSalesUnitModel>( erpSalesUnits );
		
		Collections.sort(suList, new SalesUnitComparator() );
		
		FDSalesUnit[] salesUnits = new FDSalesUnit[suList.size()];
		for (int i=0; i<salesUnits.length; i++) {
			ErpSalesUnitModel su = suList.get(i);
			if (DEBUG) LOGGER.debug("Setting SalesUnit attributes from "+su);

			// get attributes
			AttributesI attribs = new AttributeCollection( su.getAttributes() );

//			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription());
			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription(),su.getNumerator(),su.getDenominator(),su.getBaseUnit());
		}
		return salesUnits;
	}

	protected FDVariation[] getVariations(List<ErpCharacteristicModel> chList) {
		FDVariation[] variations = new FDVariation[chList.size()];
		for (int i=0; i<variations.length; i++) {
			ErpCharacteristicModel ch = chList.get(i);

			if (DEBUG) LOGGER.debug("Setting Variation attributes from "+ch);
			// get attributes
			AttributesI attribs = new AttributeCollection( ch.getAttributes() );

			variations[i] = new FDVariation(attribs, ch.getName(), this.getVariationOptions(ch));
		}
		return variations;
	}

	private static class VariationOptionComparator extends AttributeComparator.Priority<ErpCharacteristicValueModel> {
		public int compare(ErpCharacteristicValueModel o1, ErpCharacteristicValueModel o2) {
			int prio = super.compare(o1, o2);
			if (prio!=0) {
				return prio;
			}
			String a1 = o1.getDescription();
			String a2 = o2.getDescription();
			return a1.compareTo(a2);
		}
	}

	protected FDVariationOption[] getVariationOptions(ErpCharacteristicModel characteristic) {
		List<ErpCharacteristicValueModel> cvList = new ArrayList<ErpCharacteristicValueModel>( characteristic.getCharacteristicValues() );

		Collections.sort(cvList, new VariationOptionComparator() );

		FDVariationOption[] variationOptions = new FDVariationOption[cvList.size()];
		for (int i=0; i<variationOptions.length; i++) {
			ErpCharacteristicValueModel cv = cvList.get(i);

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
			Collection<ErpCharacteristicValuePriceEB> cvPriceEBs = this.charValueHome.findByMaterial( (VersionedPrimaryKey)material.getPK() );
			ErpCharacteristicValuePriceModel[] cvPrices = new ErpCharacteristicValuePriceModel[cvPriceEBs.size()];
			
			// create model array
			int count=0;
			for (Iterator<ErpCharacteristicValuePriceEB> i=cvPriceEBs.iterator(); i.hasNext(); count++) {
				cvPrices[count]= (ErpCharacteristicValuePriceModel) i.next().getModel();
			}
			
						
			return PricingFactory.getPricing( material, cvPrices );
			
		}  catch (RemoteException ex) {
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


	private void lookupGroupPriceHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.grpHome = (ErpGrpInfoHome) ctx.lookup("java:comp/env/ejb/GrpPriceInfo");
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}

	protected ErpInfoSB getErpInfoSB() {
	    return ERPServiceLocator.getInstance().getErpInfoSessionBean();
	}	
}
