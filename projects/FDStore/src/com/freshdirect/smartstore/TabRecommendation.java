package com.freshdirect.smartstore;

import java.util.List;

public class TabRecommendation {
	
    private static final String PIP_DEFAULT_DESC = "These are some of the items we recommend you:";

    private final List<Variant> variants;
    private final Variant tabVariant;
    private String parentImpressionId;
    private String[] featureImpId;
    private int selected;
    private boolean error;
    private String selectedSiteFeature;

    public TabRecommendation(Variant tabVariant, List<Variant> variants) {
        this.tabVariant = tabVariant;
        this.variants = variants;
        this.featureImpId = new String[variants.size()];
    }

    public int size() {
        return variants.size();
    }
    
    public void setFeatureImpressionId(int pos, String featureImpId) {
        this.featureImpId[pos] = featureImpId;
    }
    
    public String getFeatureImpressionId(int pos) {
        return featureImpId[pos];
    }

    public Variant get(int index) {
        return variants.get(index);
    }

    public String getTabDescription(int index) {
        Variant variant = get(index);
        String varPrezDescription = variant.getServiceConfig().getPresentationDescription();
        if (varPrezDescription != null) {
            return varPrezDescription;
        }

        return PIP_DEFAULT_DESC;
    }
    
    public String getTabFooter(int index) {
        Variant variant = get(index);
        return variant.getServiceConfig().getPresentationFooter();
    }
    
    public Variant getTabVariant() {
        return tabVariant;
    }
    
    public String getParentImpressionId() {
        return parentImpressionId;
    }
    
    public void setParentImpressionId(String parentImpressionId) {
        this.parentImpressionId = parentImpressionId;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
    
    public int getSelected() {
        return selected;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getTabIndex(String tabId) {
        for (int i = 0; i <variants.size(); i++) {
            Variant v = variants.get(i);
            if (v.getId().equals(tabId)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Returns the list of all variants.
     * @return
     */
    public List<Variant> getVariants() {
        return this.variants;
    }

    public String getSelectedSiteFeature() {
        return selectedSiteFeature;
    }

    public void setSelectedSiteFeature(String selectedSiteFeature) {
        this.selectedSiteFeature = selectedSiteFeature;
    }

}
