package com.freshdirect.dataloader.sap.jco.server.param;

public class AbstractMaterialParameter implements java.io.Serializable {
	private static final long serialVersionUID = 7176838775667965367L;

	private String distributionChannelId;

	private String salesOrganizationId;

	private String materialDescription;

	private String materialID;

	private String plantCode;

	private String skuCode;

	private String pricingZoneId;

	public void setDistributionChannelId(final String distributionChannelId) {
		this.distributionChannelId = distributionChannelId;
	}

	public String getDistributionChannelId() {
		return distributionChannelId;
	}

	public void setSalesOrganizationId(final String salesOrganizationId) {
		this.salesOrganizationId = salesOrganizationId;
	}

	public String getSalesOrganizationId() {
		return salesOrganizationId;
	}

	public void setMaterialDescription(final String materialDescription) {
		this.materialDescription = materialDescription;
	}

	public String getMaterialDescription() {
		return materialDescription;
	}

	public void setMaterialID(final String materialID) {
		this.materialID = materialID;
	}

	public String getMaterialID() {
		return materialID;
	}

	public void setPlantCode(final String plantCode) {
		this.plantCode = plantCode;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setSkuCode(final String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setPricingZoneId(final String pricingZoneId) {
		this.pricingZoneId = pricingZoneId;
	}

	public String getPricingZoneId() {
		return pricingZoneId;
	}
}
