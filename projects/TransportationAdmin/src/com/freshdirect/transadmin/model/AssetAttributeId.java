package com.freshdirect.transadmin.model;

public class AssetAttributeId implements java.io.Serializable {

	private String assetId;
	private String attributeType;
	
	public AssetAttributeId() {
	}

	public AssetAttributeId(String assetId, String attributeType) {
		this.assetId = assetId;
		this.attributeType = attributeType;
	}

	public String getAssetId() {
		return assetId;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
		result = prime * result
				+ ((attributeType == null) ? 0 : attributeType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetAttributeId other = (AssetAttributeId) obj;
		if (assetId == null) {
			if (other.assetId != null)
				return false;
		} else if (!assetId.equals(other.assetId))
			return false;
		if (attributeType == null) {
			if (other.attributeType != null)
				return false;
		} else if (!attributeType.equals(other.attributeType))
			return false;
		return true;
	}
	

}
