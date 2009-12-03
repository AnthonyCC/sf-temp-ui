package com.freshdirect.mobileapi.model;

import com.freshdirect.fdstore.FDVariationOption;

public class VariationOption {
    String name;

    String description;

    String characteristicValuePrice;

    FDVariationOption option;

    public VariationOption(FDVariationOption option) {
        this.option = option;
        this.name = option.getName();
        this.description = option.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCharacteristicValuePrice() {
        return characteristicValuePrice;
    }

    public void setCharacteristicValuePrice(String cvp) {
        this.characteristicValuePrice = cvp;
    }

    public String getName() {
        return name;
    }
}