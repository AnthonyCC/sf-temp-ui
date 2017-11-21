package com.freshdirect.cms.persistence.erps.data;

public class SkuMaterial {

    private String sku;
    private String materialId;

    public SkuMaterial(String sku, String sapId) {
        this.sku = sku;
        this.materialId = sapId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Override
    public String toString() {
        return "SkuMaterial [sku=" + sku + ", materialId=" + materialId + "]";
    }

}
