package com.freshdirect.cms.persistence.erps.data;

public class MaterialSalesUnit {

    private String materialId;
    private String salesUnit;
    private String salesUnitDescription;

    public MaterialSalesUnit(String materialId, String salesUnit, String salesUnitDescription) {
        this.materialId = materialId;
        this.salesUnit = salesUnit;
        this.salesUnitDescription = salesUnitDescription;
    }

    public String getMaterialId() {
        return materialId;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public String getSalesUnitDescription() {
        return salesUnitDescription;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public void setSalesUnitDescription(String salesUnitDescription) {
        this.salesUnitDescription = salesUnitDescription;
    }

    @Override
    public String toString() {
        return "MaterialSalesUnit [materialId=" + materialId + ", salesUnit=" + salesUnit + ", salesUnitDescription=" + salesUnitDescription + "]";
    }

}
