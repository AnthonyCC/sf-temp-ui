package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.ZoneInfo;
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
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.ecomm.gateway.ErpNutritionService;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceEB;
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.EnumDayPartValueType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDMaterialSalesArea;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDPlantMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.util.UnitPriceUtil;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDProductHelper {

	private final static Category LOGGER = LoggerFactory.getInstance( FDProductHelper.class );
	private final static boolean DEBUG = false;

	private transient ErpCharacteristicValuePriceHome charValueHome = null;

	public FDProduct getFDProduct(ErpMaterialModel material) throws FDResourceException {
		// debug
		if (DEBUG) LOGGER.debug( new DebugVisitor(material) );

		// load attributes for tree
		this.bindAttributes( material );

		// construct sales units
		FDSalesUnit[] salesUnits = this.getSalesUnits(material.getSalesUnits());

		// construct variations
		FDVariation[] variations = this.getVariations(material.getCharacteristics());
		
		
		
		// build Pricing object
		Pricing pricing = this.getPricing(material);
		
		// get version
		int version = ((VersionedPrimaryKey)material.getPK()).getVersion();
		
		// get attributes for material
		AttributesI attribs = new AttributeCollection(material.getAttributes());
		
		// get nutrition information
		ErpNutritionModel nutrModel = FDNutritionCache.getInstance().getNutrition(material.getSkuCode());

		// construct display sales units
		FDSalesUnit[] displaySalesUnits = this.getSalesUnits(material.getDisplaySalesUnits());
		
		// get nutrition panel information
		NutritionPanel nutritionPanel = FDNutritionPanelCache.getInstance().getNutritionPanel(material.getSkuCode());

		// create FDMaterial
//		ErpMaterialModel material = product.getProxiedMaterial();
/*		FDMaterial fdMaterial =
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
				material.getBlockedDays(),
				material.getLeadTime());*/
		
		FDMaterial fdMaterial =
				new FDMaterial(
					attribs,
					material.getSapId(),
					material.getSalesUnitCharacteristic(),
					material.getQuantityCharacteristic(),
					material.getAlcoholicContent(),
					material.isTaxable(),material.getSkuCode(), material.getTaxCode(), material.getMaterialGroup());
		 Map<String, FDPlantMaterial> plantMaterialMap = getMaterialPlants( material.getMaterialPlants());
		fdMaterial.setMaterialPlants(plantMaterialMap);
		Map<SalesAreaInfo, FDMaterialSalesArea> materialSalesAreaMap = getMaterialSalesAreas(material.getMaterialSalesAreas());
		fdMaterial.setMaterialSalesAreas(materialSalesAreaMap);
		
		// Construct FDNutrition from ErpNutritionModel.value map
		ArrayList<FDNutrition> nutrition = new ArrayList<FDNutrition>();
		for (Iterator<String> nIter = nutrModel.getKeyIterator(); nIter.hasNext(); ) {
			String key = nIter.next();
			FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(), nutrModel.getValueFor(key), nutrModel.getUomFor(key));
			nutrition.add(fdn);
		}

		// construct FDProduct
		return new FDProduct(material.getSkuCode(), version, null, fdMaterial, variations, salesUnits, pricing, nutrition,displaySalesUnits, nutritionPanel,material.getUPC());
	}

	private Map<String, FDPlantMaterial>  getMaterialPlants(List<ErpPlantMaterialModel> erpPlantMaterialList) {		
		FDPlantMaterial plantMaterial = null;//new ArrayList<FDPlantMaterial>();
		Map<String, FDPlantMaterial> plantMaterialMap = new HashMap<String, FDPlantMaterial>();
		if(null !=erpPlantMaterialList)
		for (Iterator iterator = erpPlantMaterialList.iterator(); iterator.hasNext();) {
			ErpPlantMaterialModel plantMaterialModel = (ErpPlantMaterialModel) iterator.next();
			plantMaterial =plantMaterialMap.get(plantMaterialModel.getPlantId());
			if(null == plantMaterial){
				plantMaterial = new FDPlantMaterial(plantMaterialModel.getAtpRule(), plantMaterialModel.isKosherProduction(), plantMaterialModel.isPlatter(), plantMaterialModel.getBlockedDays(), plantMaterialModel.getLeadTime(), plantMaterialModel.getPlantId(), plantMaterialModel.isHideOutOfStock());	
				plantMaterialMap.put(plantMaterialModel.getPlantId(),plantMaterial);			
			}
		}
		return plantMaterialMap;
	}
	
	private Map<SalesAreaInfo, FDMaterialSalesArea>  getMaterialSalesAreas(List<ErpMaterialSalesAreaModel> erpMaterialSalesAreaList) {		
		FDMaterialSalesArea materialSalesArea = null;//new ArrayList<FDPlantMaterial>();
		Map<SalesAreaInfo, FDMaterialSalesArea> materialSalesAreaMap = new HashMap<SalesAreaInfo, FDMaterialSalesArea>();
		if(null !=erpMaterialSalesAreaList)
		for (Iterator iterator = erpMaterialSalesAreaList.iterator(); iterator.hasNext();) {
			ErpMaterialSalesAreaModel materialSalesAreaModel = (ErpMaterialSalesAreaModel) iterator.next();
			SalesAreaInfo salesAreaInfo =new SalesAreaInfo(materialSalesAreaModel.getSalesOrg(), materialSalesAreaModel.getDistChannel());
			materialSalesArea =materialSalesAreaMap.get(salesAreaInfo);
			if(null == materialSalesArea){
				materialSalesArea = new FDMaterialSalesArea(salesAreaInfo,materialSalesAreaModel.getUnavailabilityStatus(),materialSalesAreaModel.getUnavailabilityDate(),materialSalesAreaModel.getUnavailabilityReason(),EnumDayPartValueType.getEnum(materialSalesAreaModel.getDayPartSelling()),materialSalesAreaModel.getPickingPlantId());
				materialSalesAreaMap.put(salesAreaInfo,materialSalesArea);
			}						
		}
		return materialSalesAreaMap;
	}
	
	private Map<String, FDMaterialSalesArea>  getMaterialSalesAreas(ErpMaterialSalesAreaInfo[] salesAreas) {		
		FDMaterialSalesArea materialSalesArea = null;//new ArrayList<FDPlantMaterial>();
		Map<String, FDMaterialSalesArea> materialSalesAreaMap = new HashMap<String, FDMaterialSalesArea>();
		if(null !=salesAreas)
		for (int i=0;i<salesAreas.length;i++) {
			ErpMaterialSalesAreaInfo materialSalesAreaModel = salesAreas[i];
			SalesAreaInfo salesAreaInfo =materialSalesAreaModel.getSalesAreaInfo();
			materialSalesArea =materialSalesAreaMap.get(salesAreaInfo);
			if(null == materialSalesArea){
				materialSalesArea = new FDMaterialSalesArea(salesAreaInfo,materialSalesAreaModel.getUnavailabilityStatus(),materialSalesAreaModel.getUnavailabilityDate(),materialSalesAreaModel.getUnavailabilityReason(),EnumDayPartValueType.getEnum(materialSalesAreaModel.getDayPartType()),materialSalesAreaModel.getPickingPlantId());
				materialSalesAreaMap.put(new String(salesAreaInfo.getSalesOrg()+salesAreaInfo.getDistChannel()).intern(),materialSalesArea);
			}						
		}
		return materialSalesAreaMap;
	}
	
	public FDProductInfo getFDProductInfoNew(ErpProductInfoModel erpProductInfo) throws FDResourceException {
		
		List<ErpMaterialPrice> matPrices = Arrays.asList(erpProductInfo.getMaterialPrices());
		Collections.sort(matPrices, PricingFactory.ERP_MAT_PRICE_COMPARATOR);
		ErpPlantMaterialInfo[] matPlants = erpProductInfo.getMaterialPlants();
		Map<String, FDMaterialSalesArea> materialSalesAreaMap = getMaterialSalesAreas(erpProductInfo.getSalesAreas());
		
		List<FDPlantMaterial> fdPlantMaterials = new ArrayList<FDPlantMaterial>();
		for (ErpPlantMaterialInfo erpPlantMaterialInfo : matPlants) {
			EnumAvailabilityStatus status = null;
			FDPlantMaterial plantMaterial = new FDPlantMaterial(erpPlantMaterialInfo.getAtpRule(), erpPlantMaterialInfo.isKosherProduction(), erpPlantMaterialInfo.isPlatter(), erpPlantMaterialInfo.getBlockedDays(), erpPlantMaterialInfo.getLeadTime(), erpPlantMaterialInfo.getPlantId()
					,erpPlantMaterialInfo.getFreshness(),EnumOrderLineRating.getEnumByStatusCode(erpPlantMaterialInfo.getRating()),EnumSustainabilityRating.getEnumByStatusCode(erpPlantMaterialInfo.getSustainabilityRating()),erpPlantMaterialInfo.isLimitedQuantity());
			
			fdPlantMaterials.add(plantMaterial);
			
		}
		
		ZonePriceInfoListing zonePriceInfoList = new ZonePriceInfoListing();
			ZoneInfo pricingZone=null;
			ZoneInfo mpPricingZone=null;
			List<ErpMaterialPrice> subList = new ArrayList<ErpMaterialPrice>();
			for(Iterator<ErpMaterialPrice> it = matPrices.iterator() ; it.hasNext();){
				ErpMaterialPrice matPrice = it.next();
				mpPricingZone=new ZoneInfo(matPrice.getSapZoneId(),"1000".equals(matPrice.getSalesOrg())?FDStoreProperties.getDefaultFdSalesOrg():matPrice.getSalesOrg(), "1000".equals(matPrice.getDistChannel())?FDStoreProperties.getDefaultFdDistributionChannel():matPrice.getDistChannel());
				if(pricingZone==null ||pricingZone.equals(mpPricingZone)) {
					subList.add(matPrice);
				}
				else if(!pricingZone.equals(mpPricingZone)) {
					ZonePriceInfoModel zpInfoModel = buildZonePriceInfo(erpProductInfo.getSkuCode(), subList, pricingZone);
					zonePriceInfoList.addZonePriceInfo(pricingZone, zpInfoModel);
					subList.clear();
					subList.add(matPrice);
				}
				pricingZone = mpPricingZone;
			}
			//Do the same for the last zone in the list.
			ZonePriceInfoModel zpInfoModel = buildZonePriceInfo(erpProductInfo.getSkuCode(), subList, pricingZone);
			zonePriceInfoList.addZonePriceInfo(pricingZone, zpInfoModel);
			subList.clear();
//		}
		//Get Group Identify if applicable.
			 Map<String,FDGroup> groups = null;
		if(FDStoreProperties.isGroupScaleEnabled()) {//otherwise group will not be associated with the product.
			if(!FDStoreProperties.isGroupScalePerfImproveEnabled()){
				try {
					groups = FDECommerceService.getInstance().getGroupIdentityForMaterial(erpProductInfo.getMaterialSapIds()[0]);
					System.out.println("**************Group Scale Query: "+erpProductInfo.getMaterialSapIds()[0]);
				} catch (RemoteException e1) {
					e1.printStackTrace();
					throw new FDResourceException( e1 );
				} 
			}else{
				groups = FDCachedFactory.getGroupsByMaterial(erpProductInfo.getMaterialSapIds()[0]);
			}
		}
		
		return new FDProductInfo(
			erpProductInfo.getSkuCode(),
			erpProductInfo.getVersion(),
			erpProductInfo.getMaterialSapIds(),
			groups,
			erpProductInfo.getUpc(),
			fdPlantMaterials,
			zonePriceInfoList,
			materialSalesAreaMap,erpProductInfo.getAlcoholicType());
	
	}
	 private Comparator<ErpMaterialPrice> matlPriceComparator = new Comparator<ErpMaterialPrice>() {
	        @Override
            public int compare(ErpMaterialPrice price1, ErpMaterialPrice price2) {
	            if (price1.getScaleQuantity() == price2.getScaleQuantity()) return 0;
	            else if (price1.getScaleQuantity() < price2.getScaleQuantity()) return -1;
	            else return 1;
	        }
	    };

	private ZonePriceInfoModel buildZonePriceInfo(String skuCode, List<ErpMaterialPrice> matPriceList, ZoneInfo pricingZone) {
		double defaultPrice=0.0;
		String defaultPriceUnit = "";
		double promoPrice = 0.0;
		//LOGGER.debug("Came to buildZonePriceInfo for :" + skuCode);
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
					LOGGER.debug("scale price is less then promo price :"+pricingZone);
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
			//LOGGER.debug("has was price for SKU " + skuCode);			
			dealsPercentage=Math.max(DealsHelper.getVariancePercentage(defaultPrice, promoPrice), 0);
		} 
		
		boolean isShowBurstImage = DealsHelper.isShowBurstImage(defaultPrice,promoPrice);;
		
        int tieredDeal = DealsHelper.determineTieredDeal(defaultPrice, matPriceList);
		//LOGGER.debug("Tiered deal for sku: " + skuCode + " is:" + tieredDeal);
		if (tieredDeal > 0 && DealsHelper.isDealOutOfBounds(tieredDeal)) {
			//LOGGER.debug("tiered deal is out of bounds for SKU " + skuCode);
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
				pricingZone,
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
	
	protected void bindAttributes(ErpMaterialModel material) {
		
		GetRootNodesErpVisitor rootNodesVisitor = new GetRootNodesErpVisitor(material);
		String[] rootIds = rootNodesVisitor.getRootIds();
		if (rootIds.length!=0) {
			
			FlatAttributeCollection attrz = FDAttributeCache.getInstance().getAttributes(rootIds);
			
			material.accept( new SetAttributesErpVisitor(attrz) );
		} else {
			material.accept( new SetAttributesErpVisitor(new FlatAttributeCollection()) );
		}
	}
	protected ErpNutritionModel getNutrition(ErpProductModel product) throws FDResourceException {
		
		try {
			ErpNutritionModel nutr;
			nutr=ErpNutritionService.getInstance().getNutrition(product.getSkuCode());
        	
			
			return nutr;
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to ErpNutrition session bean");
		}
	}

	private static class SalesUnitComparator implements Comparator<ErpSalesUnitModel> {
		@Override
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
//			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription(),su.getNumerator(),su.getDenominator(),su.getBaseUnit());
			salesUnits[i] = new FDSalesUnit(attribs, su.getAlternativeUnit(), su.getDescription(),su.getNumerator(),su.getDenominator(),su.getBaseUnit(),su.getUnitPriceNumerator(),su.getUnitPriceDenominator(),UnitPriceUtil.getFormattedUnitPriceUOM(su.getUnitPriceUOM()),su.getUnitPriceDescription());
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
		@Override
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
	
		
		try {
			Collection<ErpCharacteristicValuePriceEB> cvPriceEBs;
			ErpCharacteristicValuePriceModel[] cvPrices ;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDFactorySB_WarmUp)){
				VersionedPrimaryKey verMatIds = (VersionedPrimaryKey)material.getPK();
				Collection<ErpCharacteristicValuePriceModel> charList = FDECommerceService.getInstance().findByMaterialId(verMatIds.getId(),verMatIds.getVersion());
				cvPrices = new ErpCharacteristicValuePriceModel[charList.size()];
				int count=0;
				for(Iterator<ErpCharacteristicValuePriceModel> i=charList.iterator(); i.hasNext(); count++){
					cvPrices[count]=(ErpCharacteristicValuePriceModel) i.next();
							}
			}else{
				if (this.charValueHome==null) {
					this.lookupCharValueHome();
				}
				
				cvPriceEBs = this.charValueHome.findByMaterial( (VersionedPrimaryKey)material.getPK() );
				 cvPrices = new ErpCharacteristicValuePriceModel[cvPriceEBs.size()];
				// create model array
				int count=0;
				for (Iterator<ErpCharacteristicValuePriceEB> i=cvPriceEBs.iterator(); i.hasNext(); count++) {
					cvPrices[count]= (ErpCharacteristicValuePriceModel) i.next().getModel();
				}
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

	protected ErpInfoSB getErpInfoSB() {
	    return ERPServiceLocator.getInstance().getErpInfoSessionBean();
	}	
}