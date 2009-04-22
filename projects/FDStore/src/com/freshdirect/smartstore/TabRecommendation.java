package com.freshdirect.smartstore;

import java.util.List;

import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class TabRecommendation {
	
    public static final String        PIP_DEFAULT_DESC = ""; //"&lt;No description has been configured. Contact administrator.&gt;";

    final List                  variants;
    final RecommendationService tabRecommender;
    final YmalSource            source;
    String[] featureImpId;

    public TabRecommendation(RecommendationService tabRecommender, List variants, YmalSource source) {
        this.tabRecommender = tabRecommender;
        this.variants = variants;
        this.source = source;
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
        return (Variant) variants.get(index);
    }

    public String getTabTitle(int index) {
        Variant variant = get(index);

        String varPrezTitle = variant.getServiceConfig().getPresentationTitle();
        if (varPrezTitle != null) {
            return varPrezTitle;
        }

        EnumSiteFeature siteFeature = variant.getSiteFeature();
        String sfPrezTitle = siteFeature.getPresentationTitle();
        if (sfPrezTitle != null) {
            return sfPrezTitle;
        }

        String sfTitle = siteFeature.getTitle();
        if (sfTitle != null) {
            return sfTitle;
        } else {
            return siteFeature.getName();
        }
    }

    public String getTabDescription(int index) {
        Variant variant = get(index);

        if (EnumSiteFeature.YMAL.equals(variant.getSiteFeature()) && source != null && source.getYmalHeader() != null) {
            return source.getYmalHeader();
        }

        String varPrezDescription = variant.getServiceConfig().getPresentationDescription();
        if (varPrezDescription != null) {
            return varPrezDescription;
        }

        EnumSiteFeature siteFeature = variant.getSiteFeature();
        String sfPrezDescription = siteFeature.getPresentationDescription();
        if (sfPrezDescription != null) {
            return sfPrezDescription;
        } else {
            return PIP_DEFAULT_DESC;
        }
    }

    public RecommendationService getTabRecommender() {
        return tabRecommender;
    }

}
