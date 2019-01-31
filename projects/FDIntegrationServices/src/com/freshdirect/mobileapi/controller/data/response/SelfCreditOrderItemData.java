package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

public class SelfCreditOrderItemData {

    private String orderLineId;
    private String brand;
    private String productName;
    private double quantity;
    private String productImage;
    private List<SelfCreditComplaintReason> complaintReasons;
    private double basePrice;
    private String basePriceUnit;
    private String configurationDescription;
    private boolean sample;
    private boolean free;
    private boolean mealBundle;
    private List<String> cartonNumbers;
    private double finalPrice;
    private boolean substituted;
    private double taxDepositSum;
    private double savedAmount;

    public SelfCreditOrderItemData(com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData selfCreditOrderItemData) {
        this.orderLineId = selfCreditOrderItemData.getOrderLineId();
        this.brand = selfCreditOrderItemData.getBrand();
        this.productName = selfCreditOrderItemData.getProductName();
        this.quantity = selfCreditOrderItemData.getQuantity();
        this.productImage = selfCreditOrderItemData.getProductImage();
        this.complaintReasons = com.freshdirect.mobileapi.controller.data.response.SelfCreditComplaintReason.wrap(selfCreditOrderItemData.getComplaintReasons());
        this.basePrice = selfCreditOrderItemData.getBasePrice();
        this.basePriceUnit = selfCreditOrderItemData.getBasePriceUnit();
        this.configurationDescription = selfCreditOrderItemData.getConfigurationDescription();
        this.sample = selfCreditOrderItemData.isSample();
        this.free = selfCreditOrderItemData.isFree();
        this.mealBundle = selfCreditOrderItemData.isMealBundle();
        this.cartonNumbers = selfCreditOrderItemData.getCartonNumbers();
        this.finalPrice = selfCreditOrderItemData.getFinalPrice();
        this.substituted = selfCreditOrderItemData.isSubstituted();
        this.taxDepositSum = selfCreditOrderItemData.getTaxDepositSum();
        this.savedAmount = selfCreditOrderItemData.getSavedAmount();
    }

    public static List<SelfCreditOrderItemData> wrap(
            List<com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData> orderLines) {
        List<SelfCreditOrderItemData> selfCreditOrderItemDatas = new ArrayList<SelfCreditOrderItemData>();
        for (com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData selfCreditOrderItemData : orderLines) {
            selfCreditOrderItemDatas.add(new SelfCreditOrderItemData(selfCreditOrderItemData));
        }
        return selfCreditOrderItemDatas;
    }

    public String getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<SelfCreditComplaintReason> getComplaintReasons() {
        return complaintReasons;
    }

    public void setComplaintReasons(List<SelfCreditComplaintReason> complaintReasons) {
        this.complaintReasons = complaintReasons;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getBasePriceUnit() {
        return basePriceUnit;
    }

    public void setBasePriceUnit(String basePriceUnit) {
        this.basePriceUnit = basePriceUnit;
    }

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public void setConfigurationDescription(String configurationDescription) {
        this.configurationDescription = configurationDescription;
    }

    public boolean isSample() {
        return sample;
    }

    public void setSample(boolean sample) {
        this.sample = sample;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isMealBundle() {
        return mealBundle;
    }

    public void setMealBundle(boolean mealBundle) {
        this.mealBundle = mealBundle;
    }

	public List<String> getCartonNumbers() {
		return cartonNumbers;
	}

	public void setCartonNumbers(List<String> cartonNumbers) {
		this.cartonNumbers = cartonNumbers;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

    public boolean isSubstituted() {
        return substituted;
    }

    public void setSubstituted(boolean substituted) {
        this.substituted = substituted;
    }

    public double getTaxDepositSum() {
        return taxDepositSum;
    }

    public void setTaxDepositSum(double taxDepositSum) {
        this.taxDepositSum = taxDepositSum;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }
}
