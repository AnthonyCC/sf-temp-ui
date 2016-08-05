package com.freshdirect.mobileapi.api.service;

import org.springframework.stereotype.Component;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.cms.util.PublishId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.Buildver;
import com.freshdirect.mobileapi.controller.data.response.Configuration;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

@Component
public class ConfigurationService {

    public Configuration getConfiguration(FDSessionUser user) {
        Configuration configuration = new Configuration();
        configuration.setAkamaiImageConvertorEnabled(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.akamaiimageconvertor, user.getUser()));
        configuration.setApiCodeVersion(Buildver.getInstance().getBuildver());
        configuration.setStoreVersion(PublishId.getInstance().getPublishId());
        configuration.setVerifyPaymentMethod(FDStoreProperties.isPaymentMethodVerificationEnabled());
        configuration.setEcouponEnabled(FDCouponProperties.isCouponsEnabled());
        configuration.setAdServerUrl(FDStoreProperties.getAdServerUrl());
        configuration.setTipRange(FDStoreProperties.getTipRangeConfig());
        configuration.setMiddleTierUrl(FDStoreProperties.getMiddleTierProviderURL());
        configuration.setSocialLoginEnabled(FDStoreProperties.isSocialLoginEnabled());
        configuration.setMasterPassEnabled(MobileApiProperties.isMasterpassEnabled());
        configuration.setPayPalEnabled(MobileApiProperties.isPayPalEnabled());
        return configuration;
    }
}
