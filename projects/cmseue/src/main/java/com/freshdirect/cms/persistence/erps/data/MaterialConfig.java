package com.freshdirect.cms.persistence.erps.data;

public class MaterialConfig {

    private String materialId;
    private String characteristicName;
    private String characteristicValue;
    private String characteristicValueDescription;

    public MaterialConfig(String materialId, String characteristicName, String characteristicValue, String characteristicValueDescription) {
        this.materialId = materialId;
        this.characteristicName = characteristicName;
        this.characteristicValue = characteristicValue;
        this.characteristicValueDescription = characteristicValueDescription;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public String getCharacteristicValue() {
        return characteristicValue;
    }

    public String getCharacteristicValueDescription() {
        return characteristicValueDescription;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public void setCharacteristicValue(String characteristicValue) {
        this.characteristicValue = characteristicValue;
    }

    public void setCharacteristicValueDescription(String characteristicValueDescription) {
        this.characteristicValueDescription = characteristicValueDescription;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Override
    public String toString() {
        return "MaterialConfig [materialId=" + materialId + ", characteristicName=" + characteristicName + ", characteristicValue=" + characteristicValue
                + ", characteristicValueDescription=" + characteristicValueDescription + "]";
    }

}
