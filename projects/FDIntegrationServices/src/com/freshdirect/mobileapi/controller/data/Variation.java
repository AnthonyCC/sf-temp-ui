package com.freshdirect.mobileapi.controller.data;

import java.util.List;
import java.util.ArrayList;

public class Variation {
    private List<VariationOption> variationOptions = new ArrayList<VariationOption>();

    private boolean optional;

    private String variationDescription;

    private String variationUnderLabel;

    private String variationName;
    
    private String helpNote;

    public Variation() {

    }

    public String getHelpNote() {
		return helpNote;
	}

	public void setHelpNote(String helpNote) {
		this.helpNote = helpNote;
	}

    public Variation(com.freshdirect.mobileapi.model.Variation variation) {
        this.setVariationName(variation.getName());
        this.setOptional(variation.isOptional());
        if (variation.getOptions().size() > 1) {
            this.setVariationDescription(variation.getDescription());
        }
        this.setVariationUnderLabel(variation.getUnderLabel());
        this.setHelpNote(variation.getHelpNote());
        for (com.freshdirect.mobileapi.model.VariationOption vo : variation.getOptions()) {
            VariationOption option = new VariationOption();
            option.setDescription(vo.getDescription());
            option.setName(vo.getName());
            option.setCvp(vo.getCharacteristicValuePrice());
            variationOptions.add(option);
        }
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public List<VariationOption> getVariationOptions() {
        return variationOptions;
    }

    public void setVariationOptions(List<VariationOption> variationOptions) {
        this.variationOptions = variationOptions;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getVariationDescription() {
        return variationDescription;
    }

    public void setVariationDescription(String variationDescription) {
        this.variationDescription = variationDescription;
    }

    public String getVariationUnderLabel() {
        return variationUnderLabel;
    }

    public void setVariationUnderLabel(String variationUnderLabel) {
        this.variationUnderLabel = variationUnderLabel;
    }

}
