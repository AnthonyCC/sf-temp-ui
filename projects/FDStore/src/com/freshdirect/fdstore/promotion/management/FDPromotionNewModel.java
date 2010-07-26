package com.freshdirect.fdstore.promotion.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class FDPromotionNewModel extends ModelSupport {

	private Set<String> assignedCustomerUserIds = new HashSet<String>();
	private TreeMap<Date,FDPromoZipRestriction> zipRestrictions = new TreeMap<Date,FDPromoZipRestriction>();
	private List<FDPromotionAttributeParam> attributeList = new ArrayList<FDPromotionAttributeParam>();
	private Map<String,AssignedCustomerParam> assignedCustomerParams;
	// private String id;
	private String promotionCode;
	private String name;
	private String description;
	private String redemptionCode;
	private Date startDate;
	private String startDay;
	private String startMonth;
	private String startYear;
	private Date expirationDate;	
	private String expirationDay;
	private String expirationMonth;
	private String expirationYear;
	private Integer rollingExpirationDays;
	private String maxUsage;
	private String promotionType;
	private String minSubtotal;
	private String maxAmount;
	private String percentOff;
	private String waiveChargeType;
	private EnumPromotionStatus status;	
	private String offerDesc;
	private String audienceDesc;
	private String terms;
	private Integer redeemCount;
	private Integer skuQuantity;
	private boolean perishable;	
	private String tmpAssignedCustomerUserIds;
	private List<FDPromoContentModel> dcpdData;
	private List<FDPromoContentModel> cartStrategies;
	private List<FDPromoCustStrategyModel> custStrategies;//Always one record
	private List<FDPromoPaymentStrategyModel> paymentStrategies;
	private List<FDPromoDlvZoneStrategyModel> dlvZoneStrategies;
	private List<FDPromoDlvDateModel> dlvDates;
	private List<FDPromoChangeModel> auditChanges;
	private boolean needDryGoods;
	private boolean needCustomerList;
	private boolean ruleBased;
	private boolean favoritesOnly;
	private boolean combineOffer;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private Date lastPublishedDate;
	private boolean applyFraud = true;
	private String startDateStr;
	private String startTimeStr;
	private String expirationDateStr;
	private String expirationTimeStr;
	private String productName;
	private String categoryName;
	private Integer extendDpDays;
	private String offerType;
	private String subTotalExcludeSkus;
	private String profileOperator;
	private Integer maxItemCount;
	private boolean onHold;
	private String geoRestrictionType;
	/*
	 * Number of successful publishes
	 */
	private int publishes = 0;
	
	public FDPromotionNewModel() {
		super();
	}

	public FDPromotionNewModel(PrimaryKey pk) {
		this();
		this.setPK(pk);
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRedemptionCode() {
		return redemptionCode;
	}

	public void setRedemptionCode(String redemptionCode) {
		this.redemptionCode = redemptionCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getRollingExpirationDays() {
		return rollingExpirationDays;
	}

	public void setRollingExpirationDays(Integer rollingExpirationDays) {
		this.rollingExpirationDays = rollingExpirationDays;
	}

	public String getMaxUsage() {
		return maxUsage;
	}

	public void setMaxUsage(String maxUsage) {
		this.maxUsage = maxUsage;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getMinSubtotal() {
		return minSubtotal;
	}

	public void setMinSubtotal(String minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getPercentOff() {
		return percentOff;
	}

	public void setPercentOff(String percentOff) {
		this.percentOff = percentOff;
	}

	public String getWaiveChargeType() {
		return waiveChargeType;
	}

	public void setWaiveChargeType(String waiveChargeType) {
		this.waiveChargeType = waiveChargeType;
	}

	public EnumPromotionStatus getStatus() {
		return status;
	}

	public void setStatus(EnumPromotionStatus status) {
		this.status = status;
	}

	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	public String getAudienceDesc() {
		return audienceDesc;
	}

	public void setAudienceDesc(String audienceDesc) {
		this.audienceDesc = audienceDesc;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public Integer getRedeemCount() {
		return redeemCount;
	}

	public void setRedeemCount(Integer redeemCount) {
		this.redeemCount = redeemCount;
	}

	public Integer getSkuQuantity() {
		return skuQuantity;
	}

	public void setSkuQuantity(Integer skuQuantity) {
		this.skuQuantity = skuQuantity;
	}

	public boolean isPerishable() {
		return perishable;
	}

	public void setPerishable(boolean perishable) {
		this.perishable = perishable;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getExpirationDay() {
		return expirationDay;
	}

	public void setExpirationDay(String expirationDay) {
		this.expirationDay = expirationDay;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getAssignedCustomerUserIds() {
		String str = "";
		if (this.assignedCustomerUserIds != null && !this.assignedCustomerUserIds.isEmpty()) {
			Iterator<String> iter = this.assignedCustomerUserIds.iterator();
			while (iter.hasNext()) {
				if (!"".equals(str)) {
					str += ",";
				}
				str += ((String)iter.next()).trim();
			}
		}

		return str; //(!"".equals(str)) ? str : null;
	}
	
	public int getAssignedCustomerSize(){
		if (this.assignedCustomerUserIds != null && !this.assignedCustomerUserIds.isEmpty()) {
			return assignedCustomerUserIds.size();
		}
		return 0;
	}
	public boolean isCustomerInAssignedList(String userId){
		if (this.assignedCustomerUserIds != null && !this.assignedCustomerUserIds.isEmpty()) {
			return this.assignedCustomerUserIds.contains(userId);
		}
		return false;
	}
	
	public void setAssignedCustomerUserIds(String assignedCustomerUserIds) {
		this.assignedCustomerUserIds.clear();
		this.assignedCustomerUserIds.addAll(Arrays.asList(StringUtils.split(assignedCustomerUserIds, ",")));
	}

	public void setAssignedCustomerUserIds(Set<String> assignedCustomerUserIds) {
		this.assignedCustomerUserIds = assignedCustomerUserIds;
	}

	public TreeMap<Date,FDPromoZipRestriction> getZipRestrictions() {
		return zipRestrictions;
	}

	public void setZipRestrictions(TreeMap<Date,FDPromoZipRestriction> zipRestrictions) {
		this.zipRestrictions = zipRestrictions;
	}

	public Map<String,AssignedCustomerParam> getAssignedCustomerParams() {
		return assignedCustomerParams;
	}

	public void setAssignedCustomerParams(Map<String,AssignedCustomerParam> assignedCustomerParams) {
		this.assignedCustomerParams = assignedCustomerParams;
	}	

	public List<FDPromotionAttributeParam> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<FDPromotionAttributeParam> attributeList) {
		this.attributeList = attributeList;
	}

	public String getTmpAssignedCustomerUserIds() {
		return tmpAssignedCustomerUserIds;
	}

	public void setTmpAssignedCustomerUserIds(String tmpAssignedCustomerUserIds) {
		this.tmpAssignedCustomerUserIds = tmpAssignedCustomerUserIds;
	}

	public List<FDPromoContentModel> getDcpdData() {
		return dcpdData;
	}

	public void setDcpdData(List<FDPromoContentModel> dcpdData) {
		this.dcpdData = dcpdData;
	}

	public List<FDPromoContentModel> getCartStrategies() {
		return cartStrategies;
	}

	public void setCartStrategies(List<FDPromoContentModel> cartStrategies) {
		this.cartStrategies = cartStrategies;
	}

	public List<FDPromoCustStrategyModel> getCustStrategies() {
		return custStrategies;
	}

	public void setCustStrategies(List<FDPromoCustStrategyModel> custStrategies) {
		this.custStrategies = custStrategies;
	}

	public List<FDPromoPaymentStrategyModel> getPaymentStrategies() {
		return paymentStrategies;
	}

	public void setPaymentStrategies(List<FDPromoPaymentStrategyModel> paymentStrategies) {
		this.paymentStrategies = paymentStrategies;
	}

	public List<FDPromoChangeModel> getAuditChanges() {
		return auditChanges;
	}

	public void setAuditChanges(List<FDPromoChangeModel> auditChanges) {
		this.auditChanges = auditChanges;
	}


	public void addAuditChange(FDPromoChangeModel aChange) {
		if (this.auditChanges == null)
			this.auditChanges = new ArrayList<FDPromoChangeModel>();
		
		this.auditChanges.add(aChange);
	}
	
	
	public boolean isNeedDryGoods() {
		return needDryGoods;
	}

	public void setNeedDryGoods(boolean needDryGoods) {
		this.needDryGoods = needDryGoods;
	}

	public boolean isNeedCustomerList() {
		return needCustomerList;
	}

	public void setNeedCustomerList(boolean needCustomerList) {
		this.needCustomerList = needCustomerList;
	}

	public List<FDPromoDlvZoneStrategyModel> getDlvZoneStrategies() {
		return dlvZoneStrategies;
	}

	public void setDlvZoneStrategies(
			List<FDPromoDlvZoneStrategyModel> dlvZoneStrategies) {
		this.dlvZoneStrategies = dlvZoneStrategies;
	}

	public List<FDPromoDlvDateModel> getDlvDates() {
		return dlvDates;
	}

	public void setDlvDates(List<FDPromoDlvDateModel> dlvDates) {
		this.dlvDates = dlvDates;
	}

	public boolean isRuleBased() {
		return ruleBased;
	}

	public void setRuleBased(boolean ruleBased) {
		this.ruleBased = ruleBased;
	}

	public boolean isFavoritesOnly() {
		return favoritesOnly;
	}

	public void setFavoritesOnly(boolean favoritesOnly) {
		this.favoritesOnly = favoritesOnly;
	}

	public boolean isCombineOffer() {
		return combineOffer;
	}

	public void setCombineOffer(boolean combineOffer) {
		this.combineOffer = combineOffer;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getLastPublishedDate() {
		return lastPublishedDate;
	}

	public void setLastPublishedDate(Date lastPublishedDate) {
		this.lastPublishedDate = lastPublishedDate;
	}

	public boolean isApplyFraud() {
		return applyFraud;
	}

	public void setApplyFraud(boolean applyFraud) {
		this.applyFraud = applyFraud;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getExpirationDateStr() {
		return expirationDateStr;
	}

	public void setExpirationDateStr(String expirationDateStr) {
		this.expirationDateStr = expirationDateStr;
	}

	public String getExpirationTimeStr() {
		return expirationTimeStr;
	}

	public void setExpirationTimeStr(String expirationTimeStr) {
		this.expirationTimeStr = expirationTimeStr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getExtendDpDays() {
		return extendDpDays;
	}

	public void setExtendDpDays(Integer extendDpDays) {
		this.extendDpDays = extendDpDays;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getSubTotalExcludeSkus() {
		return subTotalExcludeSkus;
	}

	public void setSubTotalExcludeSkus(String subTotalExcludeSkus) {
		this.subTotalExcludeSkus = subTotalExcludeSkus;
	}

	public String getProfileOperator() {
		return profileOperator;
	}

	public void setProfileOperator(String profileOperator) {
		this.profileOperator = profileOperator;
	}

	public Integer getMaxItemCount() {
		return maxItemCount;
	}

	public void setMaxItemCount(Integer maxItemCount) {
		this.maxItemCount = maxItemCount;
	}

	public boolean isOnHold() {
		return onHold;
	}

	public void setOnHold(boolean onHold) {
		this.onHold = onHold;
	}

	public String getGeoRestrictionType() {
		return geoRestrictionType;
	}

	public void setGeoRestrictionType(String geoRestrictionType) {
		this.geoRestrictionType = geoRestrictionType;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("FDPromotionModel[");
		sb.append(this.promotionType).append(" / ").append(this.promotionCode);
		sb.append(" (").append(this.description).append(")");
		return sb.toString();
	}
	


	/**
	 * Utility method to remove references from freshly imported promotions to
	 * avoid integrity violation issue
	 */
	public void removeReferences() {
		if (custStrategies != null)
			for (FDPromoCustStrategyModel model : custStrategies) {
				model.setPromotionId(null);
			}

		if (cartStrategies != null)
			for (FDPromoContentModel model : cartStrategies) {
				model.setPromotionId(null);
			}

		if (dcpdData != null)
			for (FDPromoContentModel model : dcpdData) {
				model.setPromotionId(null);
			}

		if (paymentStrategies != null)
			for (FDPromoPaymentStrategyModel model : paymentStrategies) {
				model.setPromotionId(null);
			}

		if (dlvZoneStrategies != null)
			for (FDPromoDlvZoneStrategyModel model : dlvZoneStrategies) {
				model.setPromotionId(null);
			}

		if (dlvDates != null)
			for (FDPromoDlvDateModel model : dlvDates) {
				model.setPromoId(null);
			}

		if (auditChanges != null)
			for (FDPromoChangeModel model : auditChanges) {
				if (model.getChangeDetails() != null)
					for (FDPromoChangeDetailModel model2 : model.getChangeDetails()) {
						model2.setPromoChangeId(null);
					}

				model.setPromotionId(null);
			}
	}

	/**
	 * Returns the number of successful publish events
	 * @return
	 */
	public int getPublishes() {
		return publishes;
	}
	
	public void setPublishes(int cnt) {
		this.publishes = cnt;
	}
	
	public boolean isForChef(){
		boolean isMatched = false;
		List<FDPromotionAttributeParam> attrList = this.getAttributeList();
		for (FDPromotionAttributeParam promotionAttributeParam : attrList) {
			if("ChefsTable".equalsIgnoreCase(promotionAttributeParam.getAttributeName())){
				isMatched = true;
				break;
			}
		}
		return isMatched;
	}
	
	public boolean isForCOS(){
		boolean isMatched = false;
		List<FDPromoCustStrategyModel> custStrategies = this.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			for (Iterator iterator = custStrategies.iterator(); iterator.hasNext();) {
				FDPromoCustStrategyModel promoCustStrategyModel = (FDPromoCustStrategyModel) iterator.next();
				isMatched = promoCustStrategyModel.isOrderTypeCorporate();
				break;
			}
		}
		return isMatched;
	}
	
	public boolean isForCOSNew(){
		boolean isMatched = false;
		List<FDPromoCustStrategyModel> custStrategies = this.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			for (Iterator iterator = custStrategies.iterator(); iterator.hasNext();) {
				FDPromoCustStrategyModel promoCustStrategyModel = (FDPromoCustStrategyModel) iterator.next();
				isMatched = (promoCustStrategyModel.isOrderTypeCorporate() && (promoCustStrategyModel.getOrderRangeStart()==1) || (promoCustStrategyModel.getOrderRangeEnd()==1));
				break;
			}
		}
		return isMatched;
	}
	public boolean isForNew(){
		boolean isMatched = false;
		List<FDPromoCustStrategyModel> custStrategies = this.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			for (Iterator iterator = custStrategies.iterator(); iterator.hasNext();) {
				FDPromoCustStrategyModel promoCustStrategyModel = (FDPromoCustStrategyModel) iterator.next();
				isMatched = (promoCustStrategyModel.getOrderRangeStart()==1) || (promoCustStrategyModel.getOrderRangeEnd()==1);
				break;
			}
		}
		return isMatched;
	}
	
	public boolean isForDPActiveOrRTU(){
		boolean isMatched = false;
		List<FDPromoCustStrategyModel> custStrategies = this.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			for (Iterator iterator = custStrategies.iterator(); iterator.hasNext();) {
				FDPromoCustStrategyModel promoCustStrategyModel = (FDPromoCustStrategyModel) iterator.next();
				isMatched = EnumDlvPassStatus.ACTIVE.getName().equalsIgnoreCase(promoCustStrategyModel.getDpStatus()) || EnumDlvPassStatus.READY_TO_USE.getName().equalsIgnoreCase(promoCustStrategyModel.getDpStatus()) ;
				break;
			}
		}
		return isMatched;
	}
	
	public boolean isForMarketing(){
		boolean isMatched = false;
		List<FDPromotionAttributeParam> attrList = this.getAttributeList();
		for (FDPromotionAttributeParam promotionAttributeParam : attrList) {
			if("MarketingPromo".equalsIgnoreCase(promotionAttributeParam.getAttributeName())){
				isMatched = true;
				break;
			}
		}
		return isMatched;
	}
}
