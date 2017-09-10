package com.freshdirect.fdstore.ecomm.gateway.customconverters.promotion;

import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDateModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDayModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDollarDiscount;
import com.freshdirect.fdstore.promotion.management.FDPromoPaymentStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.delivery.EnumComparisionType;
import com.freshdirect.delivery.EnumDeliveryOption;
import com.freshdirect.delivery.EnumPromoFDXTierType;
import com.freshdirect.ecommerce.data.promotion.management.*;
import com.freshdirect.ecommerce.data.promotion.management.FDPromotionNewData;

public class PromotionDTOConverter {
	FDPromotionNewData targetDTO;
	FDPromotionNewModel targetModel;


	public List< 	FDPromotionNewModel> converttoModel(final List<FDPromotionNewData> dataLst){
		List <	FDPromotionNewModel>modelLst = new ArrayList<	FDPromotionNewModel>();
		for( FDPromotionNewData data: 	dataLst){
			modelLst .add( this.convert(data));
		}
		return modelLst;
	}
	public List< FDPromotionNewData> converttoDTO(final List<FDPromotionNewModel> models){
		List <FDPromotionNewData>dataLst = new ArrayList<FDPromotionNewData>();
		for( FDPromotionNewModel model: models){
			dataLst .add( this.convert(model));
		}
		return dataLst;
	}
	/**
	 * Used to convert a FDPromotionNewModel to a FDPromotionNewData
	 * @param model
	 * @return
	 */
	public FDPromotionNewData convert(final FDPromotionNewModel model) {
		targetDTO = new FDPromotionNewData();
		convertSimpleFields(model);
		convertSimpleLists(model);
		convertComplexLists(model);
		convertMaps(model);
		return targetDTO;

	}
	/**
	 * Used to convert a FDPromotionNewData to a  FDPromotionNewModel
	 * @param model
	 * @return
	 */
	public FDPromotionNewModel convert(final FDPromotionNewData data) {
		targetModel = new FDPromotionNewModel();
		convertSimpleFields(data);
		convertSimpleLists(data);
		convertComplexLists(data);
		convertMaps(data);
		return targetModel;

	}

	private void convertMaps(FDPromotionNewModel model) {
		this.targetDTO.setZipRestrictions(convert(model.getZipRestrictions()));
		this.targetDTO.setAssignedCustomerParams(convert(model.getAssignedCustomerParams()));

	}

	private void convertMaps(FDPromotionNewData data) {
		this.targetModel.setZipRestrictions(convertFDPromoZipRestrictionMap(data.getZipRestrictions()));
		this.targetModel.setAssignedCustomerParams(convertAssignedCustomerDataMap(data.getAssignedCustomerParams()));

	}

	private void convertSimpleLists(FDPromotionNewModel model) {
		this.targetDTO.setAssignedCustomerUserIds(model.getAssignedCustomerUserIds());
	}

	private void convertSimpleLists(FDPromotionNewData data) {
		this.targetModel.setAssignedCustomerUserIds(data.getAssignedCustomerUserIds());
	}

	private void convertSimpleFields(FDPromotionNewModel model) {
		// THIS IS THE PRIMARY KEY FIELD.
		this.targetDTO.setID(model.getId());

		this.targetDTO.setPromotionCode(model.getPromotionCode());

		this.targetDTO.setName(model.getName());
		this.targetDTO.setDescription(model.getDescription());
		this.targetDTO.setRedemptionCode(model.getRedemptionCode());
		this.targetDTO.setStartDate(model.getStartDate());
		this.targetDTO.setStartDay(model.getStartDay());
		this.targetDTO.setStartMonth(model.getStartMonth());
		this.targetDTO.setStartYear(model.getStartYear());
		this.targetDTO.setExpirationDate(model.getExpirationDate());
		this.targetDTO.setExpirationDay(model.getExpirationDay());
		this.targetDTO.setExpirationMonth(model.getExpirationMonth());
		this.targetDTO.setExpirationYear(model.getExpirationYear());
		this.targetDTO.setRollingExpirationDays(model.getRollingExpirationDays());
		this.targetDTO.setRollingExpDayFrom1stOrder(model.isRollingExpDayFrom1stOrder());
		this.targetDTO.setMaxUsage(model.getMaxUsage());
		this.targetDTO.setPromotionType(model.getPromotionType());
		this.targetDTO.setMinSubtotal(model.getMinSubtotal());
		this.targetDTO.setMaxAmount(model.getMaxAmount());
		this.targetDTO.setPercentOff(model.getPercentOff());
		this.targetDTO.setWaiveChargeType(model.getWaiveChargeType());

		this.targetDTO.setStatus((model.getStatus() != null ? model.getStatus().getName() : null));// EnumPromotionStatus
		this.targetDTO.setOfferDesc(model.getOfferDesc());
		this.targetDTO.setAudienceDesc(model.getAudienceDesc());
		this.targetDTO.setTerms(model.getTerms());
		this.targetDTO.setRedeemCount(model.getRedeemCount());
		this.targetDTO.setSkuQuantity(model.getSkuQuantity());
		this.targetDTO.setPerishable(model.isPerishable());

		this.targetDTO.setTmpAssignedCustomerUserIds(model.getTmpAssignedCustomerUserIds());
		this.targetDTO.setNeedDryGoods(model.isNeedDryGoods());
		this.targetDTO.setNeedCustomerList(model.isNeedCustomerList());
		this.targetDTO.setRuleBased(model.isRuleBased());
		this.targetDTO.setFavoritesOnly(model.isFavoritesOnly());
		this.targetDTO.setCombineOffer(model.isCombineOffer());
		this.targetDTO.setCreatedDate(model.getCreatedDate());
		this.targetDTO.setModifiedDate(model.getModifiedDate());
		this.targetDTO.setCreatedBy(model.getCreatedBy());
		this.targetDTO.setModifiedBy(model.getModifiedBy());
		this.targetDTO.setLastPublishedDate(model.getModifiedDate());
		this.targetDTO.setApplyFraud(model.isApplyFraud());
		this.targetDTO.setStartDateStr(model.getStartDateStr());
		this.targetDTO.setStartTimeStr(model.getStartTimeStr());
		this.targetDTO.setExpirationDateStr(model.getExpirationDateStr());
		this.targetDTO.setExpirationTimeStr(model.getExpirationTimeStr());
		this.targetDTO.setProductName(model.getProductName());
		this.targetDTO.setCategoryName(model.getCategoryName());
		this.targetDTO.setExtendDpDays(model.getExtendDpDays());
		this.targetDTO.setOfferType(model.getOfferType());
		this.targetDTO.setSubTotalExcludeSkus(model.getSubTotalExcludeSkus());
		this.targetDTO.setProfileOperator(model.getProfileOperator());
		this.targetDTO.setMaxItemCount(model.getMaxItemCount());
		this.targetDTO.setOnHold(model.isOnHold());
		this.targetDTO.setGeoRestrictionType(model.getGeoRestrictionType());

		this.targetDTO.setDcpdDepts(model.getDcpdDepts());
		this.targetDTO.setDcpdCats(model.getDcpdCats());
		this.targetDTO.setDcpdRecCats(model.getDcpdRecCats());
		this.targetDTO.setDcpdRecps(model.getDcpdRecps());
		this.targetDTO.setDcpdSkus(model.getDcpdSkus());
		this.targetDTO.setDcpdBrands(model.getDcpdBrands());

		this.targetDTO.setCartDepts(model.getCartDepts());
		this.targetDTO.setCartCats(model.getCartCats());
		this.targetDTO.setCartSkus(model.getCartSkus());
		this.targetDTO.setCartBrands(model.getCartBrands());

		this.targetDTO.setAssignedCustomerSize(model.getAssignedCustomerSize());
		this.targetDTO.setFuelSurchargeIncluded(model.isFuelSurchargeIncluded());

		this.targetDTO.setReferralPromo(model.isReferralPromo());
		this.targetDTO.setSkuLimit(model.getSkuLimit());
		this.targetDTO.setTsaPromoCode(model.getTsaPromoCode());
		this.targetDTO.setRadius(model.getRadius());
		this.targetDTO.setMaxPercentageDiscount(model.getMaxPercentageDiscount());
		this.targetDTO.setBatchNumber(model.getBatchNumber());
		this.targetDTO.setBatchId(model.getBatchId());
		this.targetDTO.setBatchPromo(model.isBatchPromo());
		this.targetDTO.setDcpdMinSubtotal(model.getDcpdMinSubtotal());
		this.targetDTO.setSapConditionType(model.getSapConditionType());
		this.targetDTO.setRafPromoCode(model.getRafPromoCode());

		this.targetDTO.setPublishes(model.getPublishes());

	}

	private void convertComplexLists(FDPromotionNewModel model) {
		this.targetDTO.setDcpdData(convertPromoContentModelLst(model.getDcpdData()));
		this.targetDTO.setCartStrategies(convertPromoContentModelLst(model.getCartStrategies()));
		this.targetDTO.setCustStrategies(convertFDPromoCustStrategyModelLst(model.getCustStrategies()));
		this.targetDTO.setPaymentStrategies(this.convetFDPromoPaymentStrategyModelLst(model.getPaymentStrategies()));
		this.targetDTO.setDlvZoneStrategies(convertFDPromoDlvZoneStrategyModelLst(model.getDlvZoneStrategies()));
		this.targetDTO.setDlvDates(convertFDPromoDlvDateModelLst(model.getDlvDates()));
		this.targetDTO.setAuditChanges(convertFDPromoChangeModelLst(model.getAuditChanges()));

		this.targetDTO.setDollarOffList(convertFDPromoDollarDiscountLst(model.getDollarOffList()));
		this.targetDTO.setAttributeList(convertFDPromotionAttributeParam(model.getAttributeList()));

	}

	private void convertComplexLists(FDPromotionNewData data) {
		this.targetModel.setDcpdData(convertPromoContentDataLst(data.getDcpdData()));
		this.targetModel.setCartStrategies(convertPromoContentDataLst(data.getCartStrategies()));
		this.targetModel.setCustStrategies(convertFDPromoCustStrategyDataLst(data.getCustStrategies()));
		this.targetModel.setPaymentStrategies(this.convetFDPromoPaymentStrategyDataLst(data.getPaymentStrategies()));
		this.targetModel.setDlvZoneStrategies(convertFDPromoDlvZoneStrategyDataLst(data.getDlvZoneStrategies()));
		this.targetModel.setDlvDates(convertFDPromoDlvDateDataLst(data.getDlvDates()));
		this.targetModel.setAuditChanges(convertFDPromoChangeDataLst(data.getAuditChanges()));

		this.targetModel.setDollarOffList(convertFDPromoDollarDiscountDataLst(data.getDollarOffList()));
		this.targetModel.setAttributeList(convertFDPromotionAttributeData(data.getAttributeList()));

	}

	// phase I model to dto.
	// started
	// private List<FDPromoCustStrategyModel> custStrategies;// Always one
	// record
	private List<FDPromoCustStrategyData> convertFDPromoCustStrategyModelLst(List<FDPromoCustStrategyModel> modelLst) {
		List<FDPromoCustStrategyData> datalst = new ArrayList<FDPromoCustStrategyData>();
		for (FDPromoCustStrategyModel model : modelLst) {
			datalst.add(convert(model));

		}
		return datalst;
	}

	private FDPromoCustStrategyData convert(FDPromoCustStrategyModel model) {
		/*
		 * //String, Integer, Integer, String[], String[], String, Date, Date,
		 * boolean, boolean, boolean, boolean, List<String>, String,
		 * EnumComparisionType, EnumPromoFDXTierType, String[]) (String
		 * promotionId, Integer orderRangeStart, Integer orderRangeEnd, String[]
		 * cohorts, String[] dpTypes,String dpStatus, Date dpExpStart, Date
		 * dpExpEnd, boolean orderTypeHome, boolean orderTypePickup, boolean
		 * orderTypeCorporate, boolean orderTypeFDX, List<String> paymentType,
		 * String priorEcheckUse,String echeckMatchType, String
		 * fdxTierType,String[] orderRangeDeliveryTypes)
		 */
		List<String> cardTypeslst = new ArrayList<String>();
		if (model.getPaymentType() != null) {
			for (EnumCardType cardType : model.getPaymentType()) {
				cardTypeslst.add(cardType.getName());
			}
		}
		// String, Integer, Integer, String[], String[], String, Date, Date,
		// boolean, boolean, boolean, boolean, List<String>, String,
		// EnumComparisionType, EnumPromoFDXTierType, String[])
		String echeckMatchType = model.getEcheckMatchType() != null ? model.getEcheckMatchType().getName() : null;
		String fdxTierType = model.getFdxTierType() != null ? model.getFdxTierType().getName() : null;

		FDPromoCustStrategyData data = new FDPromoCustStrategyData(model.getPromotionId(), model.getOrderRangeStart(),
				model.getOrderRangeEnd(), model.getCohorts(), model.getDpTypes(), model.getDpStatus(),
				model.getDpExpStart(), model.getDpExpEnd(), model.isOrderTypeHome(), model.isOrderTypePickup(),
				model.isOrderTypeCorporate(), model.isOrderTypeFDX(), cardTypeslst, model.getPriorEcheckUse(),
				echeckMatchType, fdxTierType, model.getOrderRangeDeliveryTypes());
		data.setID(model.getId());
		return data;
	}

	// finished
	private List<FDPromoPaymentStrategyData> convetFDPromoPaymentStrategyModelLst(
			List<FDPromoPaymentStrategyModel> modelLst) {
		List<FDPromoPaymentStrategyData> datalst = new ArrayList<FDPromoPaymentStrategyData>();
		if (modelLst != null) {
			for (FDPromoPaymentStrategyModel model : modelLst) {
				datalst.add(convert(model));
			}
		}
		return datalst;
	}

	private FDPromoPaymentStrategyData convert(FDPromoPaymentStrategyModel model) {
		/*
		 * //String, Integer, Integer, String[], String[], String, Date, Date,
		 * boolean, boolean, boolean, boolean, List<String>, String,
		 * EnumComparisionType, EnumPromoFDXTierType, String[]) (String
		 * promotionId, Integer orderRangeStart, Integer orderRangeEnd, String[]
		 * cohorts, String[] dpTypes,String dpStatus, Date dpExpStart, Date
		 * dpExpEnd, boolean orderTypeHome, boolean orderTypePickup, boolean
		 * orderTypeCorporate, boolean orderTypeFDX, List<String> paymentType,
		 * String priorEcheckUse,String echeckMatchType, String
		 * fdxTierType,String[] orderRangeDeliveryTypes)
		 */
		List<String> cardTypeslst = new ArrayList<String>();
		if (model.getPaymentType() != null) {
			for (EnumCardType cardType : model.getPaymentType()) {
				cardTypeslst.add(cardType.getName());
			}
		}
		// String, Integer, Integer, String[], String[], String, Date, Date,
		// boolean, boolean, boolean, boolean, List<String>, String,
		// EnumComparisionType, EnumPromoFDXTierType, String[])

		FDPromoPaymentStrategyData data = new FDPromoPaymentStrategyData(model.getPromotionId(),
				model.isOrderTypeHome(), model.isOrderTypePickup(), model.isOrderTypeCorporate(), cardTypeslst,
				model.getPriorEcheckUse(),
				model.getEcheckMatchType() != null ? model.getEcheckMatchType().getName() : null);
		data.setID(model.getId());
		return data;
	}
	// private List<FDPromoContentModel> cartStrategies;
	// private List<FDPromoContentData> dcpdData;

	// finished
	private List<FDPromoDlvTimeSlotData> convertFDPromoDlvTimeSlotModelLst(List<FDPromoDlvTimeSlotModel> modelLst) {
		List<FDPromoDlvTimeSlotData> datalst = new ArrayList<FDPromoDlvTimeSlotData>();
		if (modelLst != null) {
			for (FDPromoDlvTimeSlotModel model : modelLst) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	private FDPromoDlvTimeSlotData convert(FDPromoDlvTimeSlotModel model) {
		FDPromoDlvTimeSlotData data = new FDPromoDlvTimeSlotData();
		/*
		 * private String promoDlvZoneId; private Integer dayId; private String
		 * dlvTimeStart; private String dlvTimeEnd; private String radius;
		 * private String[] windowTypes;
		 */
		data.setPromoDlvZoneId(model.getPromoDlvZoneId());
		data.setDayId(model.getDayId());
		data.setDlvTimeStart(model.getDlvTimeStart());
		data.setDlvTimeEnd(model.getDlvTimeEnd());
		data.setRadius(model.getRadius());
		data.setWindowTypes(model.getWindowTypes());
		data.setID(model.getId());
		return data;

	}

	// finished
	private List<FDPromoDlvDayData> convertFDPromoDlvDayModelLst(List<FDPromoDlvDayModel> modelLst) {
		List<FDPromoDlvDayData> datalst = new ArrayList<FDPromoDlvDayData>();
		if (modelLst != null) {
			for (FDPromoDlvDayModel model : modelLst) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	private FDPromoDlvDayData convert(FDPromoDlvDayModel model) {
		FDPromoDlvDayData data = new FDPromoDlvDayData();
		/*
		 * private String promoDlvZoneId; private Integer dayId; private String
		 * dlvTimeStart; private String dlvTimeEnd; private String radius;
		 * private String[] windowTypes;
		 */
		data.setPromoDlvZoneId(model.getPromoDlvZoneId());
		data.setDayId(model.getDayId());
		data.setRedeemCount(model.getRedeemCount());
		data.setID(model.getId());

		return data;

	}

	//////
	/*
	 * FDPromoDollarDiscount extends ModelSupport{
	 * 
	 * 
	 */
	// finished
	private List<FDPromoDollarDiscountData> convertFDPromoDollarDiscountLst(List<FDPromoDollarDiscount> modelLst) {
		List<FDPromoDollarDiscountData> datalst = new ArrayList<FDPromoDollarDiscountData>();
		if (modelLst != null) {
			for (FDPromoDollarDiscount model : modelLst) {
				datalst.add(convert(model));

			}

		}
		return datalst;
	}

	private FDPromoDollarDiscountData convert(FDPromoDollarDiscount model) {
		FDPromoDollarDiscountData data = new FDPromoDollarDiscountData();
		/*
		 * private String promoId; private String pddId; private Double
		 * dollarOff; private Double OrderSubtotal;
		 */
		data.setPromoId(model.getPromoId());
		data.setPddId(model.getPddId());
		data.setDollarOff(model.getDollarOff());
		data.setOrderSubtotal(model.getOrderSubtotal());
		data.setID(model.getId());

		return data;

	}
	//////

	// finished
	private List<FDPromoDlvDateData> convertFDPromoDlvDateModelLst(List<FDPromoDlvDateModel> modelLst) {
		List<FDPromoDlvDateData> datalst = new ArrayList<FDPromoDlvDateData>();
		if (modelLst != null) {
			for (FDPromoDlvDateModel model : modelLst) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	private FDPromoDlvDateData convert(FDPromoDlvDateModel model) {
		FDPromoDlvDateData data = new FDPromoDlvDateData();

		data.setPromoId(model.getPromoId());
		data.setDlvDateStart(model.getDlvDateStart());
		data.setDlvDateEnd(model.getDlvDateEnd());
		data.setID(model.getId());

		return data;

	}
	// finished

	private List<FDPromoDlvZoneStrategyData> convertFDPromoDlvZoneStrategyModelLst(
			List<FDPromoDlvZoneStrategyModel> modelLst) {
		List<FDPromoDlvZoneStrategyData> datalst = new ArrayList<FDPromoDlvZoneStrategyData>();
		if (modelLst != null) {
			for (FDPromoDlvZoneStrategyModel model : modelLst) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	private FDPromoDlvZoneStrategyData convert(FDPromoDlvZoneStrategyModel model) {
		FDPromoDlvZoneStrategyData data = new FDPromoDlvZoneStrategyData();
		/*
		 * private String promotionId; private String dlvDays; private String[]
		 * dlvZones;
		 * 
		 * private List<FDPromoDlvTimeSlotModel> dlvTimeSlots; private
		 * List<FDPromoDlvDayModel> dlvDayRedemtions;
		 */
		data.setPromotionId(model.getPromotionId());
		data.setDlvDays(model.getDlvDays());
		data.setDlvZones(model.getDlvZones());

		data.setDlvTimeSlots(convertFDPromoDlvTimeSlotModelLst(model.getDlvTimeSlots()));

		data.setDlvDayRedemtions(convertFDPromoDlvDayModelLst(model.getDlvDayRedemtions()));
		data.setID(model.getId());
		return data;

	}

	// FINISHED
	private List<FDPromoContentData> convertPromoContentModelLst(List<FDPromoContentModel> modelLst) {
		List<FDPromoContentData> datalst = new ArrayList<FDPromoContentData>();
		if (modelLst != null) {
			for (FDPromoContentModel model : modelLst) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	private FDPromoContentData convert(FDPromoContentModel model) {
		FDPromoContentData data = new FDPromoContentData();
		/*
		 * private String promotionId; private String
		 * contentType;//EnumDCPDContentType private String contentId; private
		 * boolean excluded; private boolean loopThru; private boolean
		 * recCategory = false; private Integer content_set_num;
		 */
		data.setPromotionId(model.getPromotionId());

		data.setContentType(model.getContentType() != null ? model.getContentType().getName() : null);// EnumDCPDContentType
		data.setContentId(model.getContentId());
		data.setID(model.getId());
		data.setExcluded(model.isExcluded());
		data.setLoopEnabled(model.isLoopEnabled());
		data.setRecCategory(model.isRecCategory());
		data.setContent_set_num(model.getContent_set_num());
		data.setID(model.getId());
		return data;
	}

	// FINISHED
	private List<FDPromotionAttributeParamData> convertFDPromotionAttributeParam(
			List<FDPromotionAttributeParam> params) {
		List<FDPromotionAttributeParamData> datalst = new ArrayList<FDPromotionAttributeParamData>();
		if (params != null) {
			for (FDPromotionAttributeParam param : params) {
				datalst.add(convert(param));

			}
		}
		return datalst;
	}

	/**
	 * convert a FDPromotionAttributeParam to a FDPromotionAttributeParamData or
	 * dto
	 */
	private FDPromotionAttributeParamData convert(FDPromotionAttributeParam param) {
		FDPromotionAttributeParamData data = new FDPromotionAttributeParamData();
		data.setAttributeIndex(param.getAttributeIndex());
		data.setAttributeName(param.getAttributeName());
		data.setDesiredValue(param.getDesiredValue());
		data.setID(param.getId());
		return data;

	}

	// finished
	// FDPromoChangeModel
	public  List<FDPromoChangeData> convertFDPromoChangeModelLst(List<FDPromoChangeModel> models) {
		List<FDPromoChangeData> datalst = new ArrayList<FDPromoChangeData>();
		if (models != null) {
			for (FDPromoChangeModel model : models) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	/**
	 * convert a FDPromotionAttributeParam to a FDPromotionAttributeParamData or
	 * dto
	 */
	public  FDPromoChangeData convert(FDPromoChangeModel model) {
		FDPromoChangeData data = new FDPromoChangeData();
		data.setID(model.getId());
		data.setUserId(model.getUserId());
		data.setActionDate(model.getActionDate());// EnumPromotionSection
		data.setActionType(model.getActionType() != null ? model.getActionType().getName() : null);
		data.setChangeDetails(convertFDPromoChangeDetailModel(model.getChangeDetails()));

		return data;

	}

	// finished

	private List<FDPromoChangeDetailData> convertFDPromoChangeDetailModel(List<FDPromoChangeDetailModel> models) {
		List<FDPromoChangeDetailData> datalst = new ArrayList<FDPromoChangeDetailData>();
		if (models != null) {
			for (FDPromoChangeDetailModel model : models) {
				datalst.add(convert(model));

			}
		}
		return datalst;
	}

	/**
	 * convert a FDPromotionAttributeParam to a FDPromotionAttributeParamData or
	 * dto
	 */
	private FDPromoChangeDetailData convert(FDPromoChangeDetailModel model) {
		FDPromoChangeDetailData data = new FDPromoChangeDetailData();
		data.setPromoChangeId(model.getPromoChangeId());
		data.setChangeSectionId(model.getChangeSectionId() != null ? model.getChangeSectionId().getName() : null);// EnumPromotionSection
		data.setChangeFieldName(model.getChangeFieldName());
		data.setChangeFieldOldValue(model.getChangeFieldOldValue());

		return data;

	}

	// finished
	/**
	 * convert FDPromoZipRestriction to FDPromoZipRestriction DTO
	 * 
	 * @param zipRestriction
	 *            instance of FDPromoZipRestriction
	 * @return
	 */
	private FDPromoZipRestrictionData convert(FDPromoZipRestriction zipRestriction) {
		FDPromoZipRestrictionData data = new FDPromoZipRestrictionData();
		/*
		 * private Date startDate; private List zipCodes; private String type;
		 */
		data.setStartDate(zipRestriction.getStartDate());
		data.setZipCodes(zipRestriction.getZipCodeList());
		data.setType(zipRestriction.getType());
		return data;

	}

	private TreeMap<Date, FDPromoZipRestrictionData> convert(TreeMap<Date, FDPromoZipRestriction> modelmap) {
		TreeMap<Date, FDPromoZipRestrictionData> dtomap = new TreeMap<Date, FDPromoZipRestrictionData>();
		if (modelmap != null) {
			for (Map.Entry<Date, FDPromoZipRestriction> entryset : modelmap.entrySet()) {
				dtomap.put(entryset.getKey(), convert(entryset.getValue()));
				// System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}

		return dtomap;
	}

	// DONE
	// private Map<String, AssignedCustomerParamData> assignedCustomerParams;
	private Map<String, AssignedCustomerParamData> convert(Map<String, AssignedCustomerParam> modelmap) {
		Map<String, AssignedCustomerParamData> dtomap = new HashMap<String, AssignedCustomerParamData>();
		if (modelmap != null) {
			for (Map.Entry<String, AssignedCustomerParam> entryset : modelmap.entrySet()) {
				dtomap.put(entryset.getKey(), convert(entryset.getValue()));
				// System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}
		return dtomap;
	}

	private AssignedCustomerParamData convert(AssignedCustomerParam param) {
		AssignedCustomerParamData data = new AssignedCustomerParamData();
		/*
		 * private Integer usageCount; private Date expirationDate;
		 */
		data.setUsageCount(param.getUsageCount());
		data.setExpirationDate(param.getExpirationDate());
		return data;

	}

	/********************************
	 * THIS IS THE DATA TO MODEL SECTION
	 ***********************************************/

	private List<FDPromoCustStrategyModel> convertFDPromoCustStrategyDataLst(List<FDPromoCustStrategyData> dataLst) {
		List<FDPromoCustStrategyModel> modellst = new ArrayList<FDPromoCustStrategyModel>();
		if (dataLst != null) {
			for (FDPromoCustStrategyData data : dataLst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	private FDPromoCustStrategyModel convert(FDPromoCustStrategyData data) {
		/*
		 * //String, Integer, Integer, String[], String[], String, Date, Date,
		 * boolean, boolean, boolean, boolean, List<String>, String,
		 * EnumComparisionType, EnumPromoFDXTierType, String[]) (String
		 * promotionId, Integer orderRangeStart, Integer orderRangeEnd, String[]
		 * cohorts, String[] dpTypes,String dpStatus, Date dpExpStart, Date
		 * dpExpEnd, boolean orderTypeHome, boolean orderTypePickup, boolean
		 * orderTypeCorporate, boolean orderTypeFDX, List<String> paymentType,
		 * String priorEcheckUse,String echeckMatchType, String
		 * fdxTierType,String[] orderRangeDeliveryTypes)
		 * 
		 * 
		 * private EnumPromoFDXTierType fdxTierType; private EnumCardType[]
		 * paymentType; private String priorEcheckUse; private boolean
		 * excludeSameDayDlv; private EnumDeliveryOption deliveryDayType;
		 * private EnumComparisionType echeckMatchType;
		 * 
		 * 
		 */

		List<EnumCardType> cardTypeslst = new ArrayList<EnumCardType>();
		boolean cardtypelistvalid=false;
		if (data.getPaymentType() != null) {
			for (String cardType :  data.getPaymentType()) {
				if ( cardType!=null&& ! cardType.isEmpty()){
					cardTypeslst.add(EnumCardType.getEnum(cardType));
					cardtypelistvalid=true;
				}
			}
		}
		EnumCardType[] arrOfEnumCardTypes=null;
		/*
		 * due to the way that java 6 initiializes a list, it has 10 entries of type object which screws things up 
		 * unless extraordinary precautions are taken.
		 */
		if (cardtypelistvalid) {arrOfEnumCardTypes = (EnumCardType[]) cardTypeslst.toArray();}
		// String, Integer, Integer, String[], String[], String, Date, Date,
		// boolean, boolean, boolean, boolean, List<String>, String,
		// EnumComparisionType, EnumPromoFDXTierType, String[])
		FDPromoCustStrategyModel model = new FDPromoCustStrategyModel(data.getPromotionId(), data.getOrderRangeStart(),
				data.getOrderRangeEnd(), data.getCohorts(), data.getDpTypes(), data.getDpStatus(), data.getDpExpStart(),
				data.getDpExpEnd(), data.isOrderTypeHome(), data.isOrderTypePickup(), data.isOrderTypeCorporate(),
				data.isOrderTypeFDX(),arrOfEnumCardTypes, data.getPriorEcheckUse(),
				data.getEcheckMatchType() != null ? EnumComparisionType.getEnum(data.getEcheckMatchType()) : null,
				data.getFdxTierType() != null ? EnumPromoFDXTierType.getEnum(data.getFdxTierType()) : null,
				data.getOrderRangeDeliveryTypes());

		model.setId(data.getId());
		return model;
	}

	private List<FDPromoPaymentStrategyModel> convetFDPromoPaymentStrategyDataLst(
			List<FDPromoPaymentStrategyData> datalst) {
		List<FDPromoPaymentStrategyModel> modelLst = new ArrayList<FDPromoPaymentStrategyModel>();
		if (datalst != null) {
			for (FDPromoPaymentStrategyData data : datalst) {
				modelLst.add(convert(data));

			}
		}
		return modelLst;
	}

	private FDPromoPaymentStrategyModel convert(FDPromoPaymentStrategyData data) {
		/*
		 * //String, Integer, Integer, String[], String[], String, Date, Date,
		 * boolean, boolean, boolean, boolean, List<String>, String,
		 * EnumComparisionType, EnumPromoFDXTierType, String[]) (String
		 * promotionId, Integer orderRangeStart, Integer orderRangeEnd, String[]
		 * cohorts, String[] dpTypes,String dpStatus, Date dpExpStart, Date
		 * dpExpEnd, boolean orderTypeHome, boolean orderTypePickup, boolean
		 * orderTypeCorporate, boolean orderTypeFDX, List<String> paymentType,
		 * String priorEcheckUse,String echeckMatchType, String
		 * fdxTierType,String[] orderRangeDeliveryTypes)
		 */
		List<EnumCardType> cardTypeslst = new ArrayList<EnumCardType>();
		if (data.getPaymentType() != null) {
			for (String cardType : data.getPaymentType()) {
				cardTypeslst.add(EnumCardType.getEnum(cardType));
			}
		}
		// (String promotionId, boolean orderTypeHome, boolean orderTypePickup,
		// boolean orderTypeCorporate, EnumCardType[] paymentType, String
		// priorEcheckUse,
		// EnumComparisionType echeckMatchType)

		FDPromoPaymentStrategyModel model = new FDPromoPaymentStrategyModel(data.getPromotionId(),
				data.isOrderTypeHome(), data.isOrderTypePickup(), data.isOrderTypeCorporate(),
				(EnumCardType[]) cardTypeslst.toArray(), data.getPriorEcheckUse(),
				data.getEcheckMatchType() != null ? EnumComparisionType.getEnum(data.getEcheckMatchType()) : null);
		data.setID(data.getId());
		return model;
	}

	private List<FDPromoDollarDiscount> convertFDPromoDollarDiscountDataLst(List<FDPromoDollarDiscountData> datalst) {
		List<FDPromoDollarDiscount> modellst = new ArrayList<FDPromoDollarDiscount>();
		if (datalst != null) {
			for (FDPromoDollarDiscountData data : datalst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	private FDPromoDollarDiscount convert(FDPromoDollarDiscountData data) {
		FDPromoDollarDiscount model = new FDPromoDollarDiscount();
		/*
		 * private String promoId; private String pddId; private Double
		 * dollarOff; private Double OrderSubtotal;
		 */
		model.setPromoId(data.getPromoId());
		model.setPddId(data.getPddId());
		model.setDollarOff(data.getDollarOff());
		model.setOrderSubtotal(data.getOrderSubtotal());
		model.setId(data.getId());

		return model;

	}

	private List<FDPromoDlvDateModel> convertFDPromoDlvDateDataLst(List<FDPromoDlvDateData> datalst) {
		List<FDPromoDlvDateModel> modellst = new ArrayList<FDPromoDlvDateModel>();
		if (datalst != null) {
			for (FDPromoDlvDateData data : datalst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	private FDPromoDlvDateModel convert(FDPromoDlvDateData data) {
		FDPromoDlvDateModel model = new FDPromoDlvDateModel();

		model.setPromoId(data.getPromoId());
		model.setDlvDateStart(data.getDlvDateStart());
		model.setDlvDateEnd(data.getDlvDateEnd());
		model.setId(data.getId());

		return model;

	}

	private List<FDPromoDlvZoneStrategyModel> convertFDPromoDlvZoneStrategyDataLst(
			List<FDPromoDlvZoneStrategyData> datalst) {
		List<FDPromoDlvZoneStrategyModel> modellst = new ArrayList<FDPromoDlvZoneStrategyModel>();
		if (datalst != null) {
			for (FDPromoDlvZoneStrategyData data : datalst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	private FDPromoDlvZoneStrategyModel convert(FDPromoDlvZoneStrategyData data) {
		FDPromoDlvZoneStrategyModel model = new FDPromoDlvZoneStrategyModel();

		model.setPromotionId(data.getPromotionId());
		model.setDlvDays(data.getDlvDays());
		model.setDlvZones(data.getDlvZones());

		model.setDlvTimeSlots(convertFDPromoDlvTimeSlotDataLst(data.getDlvTimeSlots()));

		model.setDlvDayRedemtions(convertFDPromoDlvDayDataLst(data.getDlvDayRedemtions()));
		model.setId(data.getId());
		return model;

	}

	/* placeholder1 */

	private List<FDPromoDlvTimeSlotModel> convertFDPromoDlvTimeSlotDataLst(List<FDPromoDlvTimeSlotData> datalst) {
		List<FDPromoDlvTimeSlotModel> modellst = new ArrayList<FDPromoDlvTimeSlotModel>();
		if (datalst != null) {
			for (FDPromoDlvTimeSlotData data : datalst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	private FDPromoDlvTimeSlotModel convert(FDPromoDlvTimeSlotData data) {
		FDPromoDlvTimeSlotModel model = new FDPromoDlvTimeSlotModel();

		model.setPromoDlvZoneId(data.getPromoDlvZoneId());
		model.setDayId(data.getDayId());
		model.setDlvTimeStart(data.getDlvTimeStart());
		model.setDlvTimeEnd(data.getDlvTimeEnd());
		model.setRadius(data.getRadius());
		model.setWindowTypes(data.getWindowTypes());
		model.setId(data.getId());
		return model;

	}
	/* placeholder2 */

	private List<FDPromoDlvDayModel> convertFDPromoDlvDayDataLst(List<FDPromoDlvDayData> datalst) {
		List<FDPromoDlvDayModel> modellst = new ArrayList<FDPromoDlvDayModel>();
		if (datalst != null) {
			for (FDPromoDlvDayData model : datalst) {
				modellst.add(convert(model));

			}
		}
		return modellst;
	}

	private FDPromoDlvDayModel convert(FDPromoDlvDayData data) {
		FDPromoDlvDayModel model = new FDPromoDlvDayModel();
		/*
		 * private String promoDlvZoneId; private Integer dayId; private String
		 * dlvTimeStart; private String dlvTimeEnd; private String radius;
		 * private String[] windowTypes;
		 */
		model.setPromoDlvZoneId(data.getPromoDlvZoneId());
		model.setDayId(data.getDayId());
		model.setRedeemCount(data.getRedeemCount());
		model.setId(data.getId());

		return model;

	}

	private List<FDPromoContentModel> convertPromoContentDataLst(List<FDPromoContentData> datalst) {
		List<FDPromoContentModel> modelLst = new ArrayList<FDPromoContentModel>();
		if (datalst != null) {
			for (FDPromoContentData data : datalst) {
				modelLst.add(convert(data));

			}
		}
		return modelLst;
	}

	private FDPromoContentModel convert(FDPromoContentData data) {
		FDPromoContentModel model = new FDPromoContentModel();

		model.setPromotionId(data.getPromotionId());
		model.setContentType(EnumDCPDContentType.getEnum(data.getContentType()));// EnumDCPDContentType
		model.setContentId(data.getContentId());
		model.setId(data.getId());
		model.setExcluded(data.isExcluded());
		model.setLoopEnabled(data.isLoopEnabled());
		model.setRecCategory(data.isRecCategory());
		model.setContent_set_num(data.getContent_set_num());

		return model;
	}

	private List<FDPromotionAttributeParam> convertFDPromotionAttributeData(
			List<FDPromotionAttributeParamData> datalst) {
		List<FDPromotionAttributeParam> paramslst = new ArrayList<FDPromotionAttributeParam>();
		if (datalst != null) {
			for (FDPromotionAttributeParamData param : datalst) {
				paramslst.add(convert(param));

			}
		}
		return paramslst;
	}

	/**
	 * convert a FDPromotionAttributeParam to a FDPromotionAttributeParamData or
	 * dto
	 */
	private FDPromotionAttributeParam convert(FDPromotionAttributeParamData data) {
		FDPromotionAttributeParam param = new FDPromotionAttributeParam();
		param.setAttributeIndex(data.getAttributeIndex());
		param.setAttributeName(data.getAttributeName());
		param.setDesiredValue(data.getDesiredValue());
		param.setId(data.getId());
		return param;

	}

	// FDPromoChangeData to FDPromoChangeModel, three methods together.
	public List<FDPromoChangeModel> convertFDPromoChangeDataLst(List<FDPromoChangeData> datalst) {

		List<FDPromoChangeModel> modellst = new ArrayList<FDPromoChangeModel>();
		if (datalst != null) {
			for (FDPromoChangeData data : datalst) {
				modellst.add(convert(data));

			}
		}
		return modellst;
	}

	/**
	 * convert a FDPromotionAttributeParam to a FDPromotionAttributeParamData or
	 * dto
	 */
	public  FDPromoChangeModel convert(FDPromoChangeData data) {
		FDPromoChangeModel model = new FDPromoChangeModel();
		model.setId(data.getId());
		model.setUserId(data.getUserId());
		model.setActionDate(data.getActionDate());// EnumPromotionSection
		model.setActionType(data.getActionType() != null ? EnumPromoChangeType.getEnum(data.getActionType()) : null);
		model.setChangeDetails(convertFDPromoChangeDetailData(data.getChangeDetails()));

		return model;

	}

	private List<FDPromoChangeDetailModel> convertFDPromoChangeDetailData(List<FDPromoChangeDetailData> datalst) {
		List<FDPromoChangeDetailModel> modellst = new ArrayList<FDPromoChangeDetailModel>();
		if (datalst != null) {
			for (FDPromoChangeDetailData model : datalst) {
				modellst.add(convert(model));

			}
		}
		return modellst;
	}

	/**
	 * convert a FDPromoChangeDetailData to a FDPromoChangeDetailModel
	 */
	private FDPromoChangeDetailModel convert(FDPromoChangeDetailData data) {
		FDPromoChangeDetailModel model = new FDPromoChangeDetailModel();
		model.setPromoChangeId(data.getPromoChangeId());
		model.setChangeSectionId(
				data.getChangeSectionId() != null ? EnumPromotionSection.getEnum(data.getChangeSectionId()) : null);// EnumPromotionSection
		model.setChangeFieldName(data.getChangeFieldName());
		model.setChangeFieldOldValue(data.getChangeFieldOldValue());

		return model;

	}

	/**
	 * convert FDPromoZipRestriction to FDPromoZipRestriction DTO
	 * 
	 * @param zipRestriction
	 *            instance of FDPromoZipRestriction
	 * @return
	 */
	private FDPromoZipRestriction convert(FDPromoZipRestrictionData zipRestriction) {
		FDPromoZipRestriction model = new FDPromoZipRestriction();
		/*
		 * private Date startDate; private List zipCodes; private String type;
		 */
		model.setStartDate(zipRestriction.getStartDate());
		model.setZipCodes(zipRestriction.getZipCodes());
		model.setType(zipRestriction.getType());
		return model;

	}

	private TreeMap<Date, FDPromoZipRestriction> convertFDPromoZipRestrictionMap(
			TreeMap<Date, FDPromoZipRestrictionData> datamap) {
		TreeMap<Date, FDPromoZipRestriction> modelmap = new TreeMap<Date, FDPromoZipRestriction>();
		if (datamap != null) {

			for (Map.Entry<Date, FDPromoZipRestrictionData> entryset : datamap.entrySet()) {
				modelmap.put(entryset.getKey(), convert(entryset.getValue()));
				// System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}

		return modelmap;
	}

	// private Map<String, AssignedCustomerParamData> assignedCustomerParams;
	private Map<String, AssignedCustomerParam> convertAssignedCustomerDataMap(
			Map<String, AssignedCustomerParamData> datamap) {
		Map<String, AssignedCustomerParam> dtomap = new HashMap<String, AssignedCustomerParam>();
		if (datamap != null) {

			for (Map.Entry<String, AssignedCustomerParamData> entryset : datamap.entrySet()) {
				dtomap.put(entryset.getKey(), convert(entryset.getValue()));
				// System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}
		return dtomap;
	}

	private AssignedCustomerParam convert(AssignedCustomerParamData data) {
		AssignedCustomerParam model = new AssignedCustomerParam(data.getUsageCount(), data.getExpirationDate());

		return model;

	}

	private void convertSimpleFields(FDPromotionNewData data) {
		// THIS IS THE PRIMARY KEY FIELD.
		this.targetModel.setId(data.getId());

		this.targetModel.setPromotionCode(data.getPromotionCode());

		this.targetModel.setName(data.getName());
		this.targetModel.setDescription(data.getDescription());
		this.targetModel.setRedemptionCode(data.getRedemptionCode());
		this.targetModel.setStartDate(data.getStartDate());
		this.targetModel.setStartDay(data.getStartDay());
		this.targetModel.setStartMonth(data.getStartMonth());
		this.targetModel.setStartYear(data.getStartYear());
		this.targetModel.setExpirationDate(data.getExpirationDate());
		this.targetModel.setExpirationDay(data.getExpirationDay());
		this.targetModel.setExpirationMonth(data.getExpirationMonth());
		this.targetModel.setExpirationYear(data.getExpirationYear());
		this.targetModel.setRollingExpirationDays(data.getRollingExpirationDays());
		this.targetModel.setRollingExpDayFrom1stOrder(data.isRollingExpDayFrom1stOrder());
		this.targetModel.setMaxUsage(data.getMaxUsage());
		this.targetModel.setPromotionType(data.getPromotionType());
		this.targetModel.setMinSubtotal(data.getMinSubtotal());
		this.targetModel.setMaxAmount(data.getMaxAmount());
		this.targetModel.setPercentOff(data.getPercentOff());
		this.targetModel.setWaiveChargeType(data.getWaiveChargeType());
		this.targetModel.setStatus(data.getStatus() != null ? EnumPromotionStatus.getEnum(data.getStatus()) : null);// EnumPromotionStatus
		this.targetModel.setOfferDesc(data.getOfferDesc());
		this.targetModel.setAudienceDesc(data.getAudienceDesc());
		this.targetModel.setTerms(data.getTerms());
		this.targetModel.setRedeemCount(data.getRedeemCount());
		this.targetModel.setSkuQuantity(data.getSkuQuantity());
		this.targetModel.setPerishable(data.isPerishable());

		this.targetModel.setTmpAssignedCustomerUserIds(data.getTmpAssignedCustomerUserIds());
		this.targetModel.setNeedDryGoods(data.isNeedDryGoods());
		this.targetModel.setNeedCustomerList(data.isNeedCustomerList());
		this.targetModel.setRuleBased(data.isRuleBased());
		this.targetModel.setFavoritesOnly(data.isFavoritesOnly());
		this.targetModel.setCombineOffer(data.isCombineOffer());
		this.targetModel.setCreatedDate(data.getCreatedDate());
		this.targetModel.setModifiedDate(data.getModifiedDate());
		this.targetModel.setCreatedBy(data.getCreatedBy());
		this.targetModel.setModifiedBy(data.getModifiedBy());
		this.targetModel.setLastPublishedDate(data.getModifiedDate());
		this.targetModel.setApplyFraud(data.isApplyFraud());
		this.targetModel.setStartDateStr(data.getStartDateStr());
		this.targetModel.setStartTimeStr(data.getStartTimeStr());
		this.targetModel.setExpirationDateStr(data.getExpirationDateStr());
		this.targetModel.setExpirationTimeStr(data.getExpirationTimeStr());
		this.targetModel.setProductName(data.getProductName());
		this.targetModel.setCategoryName(data.getCategoryName());
		this.targetModel.setExtendDpDays(data.getExtendDpDays());
		this.targetModel.setOfferType(data.getOfferType());
		this.targetModel.setSubTotalExcludeSkus(data.getSubTotalExcludeSkus());
		this.targetModel.setProfileOperator(data.getProfileOperator());
		this.targetModel.setMaxItemCount(data.getMaxItemCount());
		this.targetModel.setOnHold(data.isOnHold());
		this.targetModel.setGeoRestrictionType(data.getGeoRestrictionType());

		this.targetModel.setDcpdDepts(data.getDcpdDepts());
		this.targetModel.setDcpdCats(data.getDcpdCats());
		this.targetModel.setDcpdRecCats(data.getDcpdRecCats());
		this.targetModel.setDcpdRecps(data.getDcpdRecps());
		this.targetModel.setDcpdSkus(data.getDcpdSkus());
		this.targetModel.setDcpdBrands(data.getDcpdBrands());

		this.targetModel.setCartDepts(data.getCartDepts());
		this.targetModel.setCartCats(data.getCartCats());
		this.targetModel.setCartSkus(data.getCartSkus());
		this.targetModel.setCartBrands(data.getCartBrands());

		this.targetModel.setAssignedCustomerSize(data.getAssignedCustomerSize());
		this.targetModel.setFuelSurchargeIncluded(data.isFuelSurchargeIncluded());

		this.targetModel.setReferralPromo(data.isReferralPromo());
		this.targetModel.setSkuLimit(data.getSkuLimit());
		this.targetModel.setTsaPromoCode(data.getTsaPromoCode());
		this.targetModel.setRadius(data.getRadius());
		this.targetModel.setMaxPercentageDiscount(data.getMaxPercentageDiscount());
		this.targetModel.setBatchNumber(data.getBatchNumber());
		this.targetModel.setBatchId(data.getBatchId());
		this.targetModel.setBatchPromo(data.isBatchPromo());
		this.targetModel.setDcpdMinSubtotal(data.getDcpdMinSubtotal());
		this.targetModel.setSapConditionType(data.getSapConditionType());
		this.targetModel.setRafPromoCode(data.getRafPromoCode());

		this.targetModel.setPublishes(data.getPublishes());

	}

}
