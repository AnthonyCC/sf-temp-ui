package com.freshdirect.cms.config;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.validation.BasicValidator;
import com.freshdirect.cms.validation.ConditionalFieldValidator;
import com.freshdirect.cms.validation.ConfiguredProductValidator;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.cms.validation.DateIntervalValidator;
import com.freshdirect.cms.validation.PrimaryHomeValidator;
import com.freshdirect.cms.validation.RecipeChildNodeValidator;
import com.freshdirect.cms.validation.StructureValidator;
import com.freshdirect.cms.validation.UniqueContentKeyValidator;

public final class ContentValidatorConfiguration {

    private ContentValidatorConfiguration() {
    }

    private static final List<ContentValidatorI> VALIDATOR_LIST = new ArrayList<ContentValidatorI>(8);

    static {
        VALIDATOR_LIST.add(new BasicValidator());
        VALIDATOR_LIST.add(new PrimaryHomeValidator());
        VALIDATOR_LIST.add(new StructureValidator());
        VALIDATOR_LIST.add(new UniqueContentKeyValidator());
        VALIDATOR_LIST.add(new RecipeChildNodeValidator());
        VALIDATOR_LIST.add(new ConfiguredProductValidator());
        VALIDATOR_LIST.add(new DateIntervalValidator());
        VALIDATOR_LIST.add(new ConditionalFieldValidator());
    }

    public static List<ContentValidatorI> getValidatorList() {
        return VALIDATOR_LIST;
    }
}
