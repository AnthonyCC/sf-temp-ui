package com.freshdirect.cms.contentvalidation.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.contentvalidation.validator.ConditionalFieldValidator;
import com.freshdirect.cms.contentvalidation.validator.ConfiguredProductValidator;
import com.freshdirect.cms.contentvalidation.validator.DateIntervalValidator;
import com.freshdirect.cms.contentvalidation.validator.PrimaryHomeValidator;
import com.freshdirect.cms.contentvalidation.validator.RatingGroupValidator;
import com.freshdirect.cms.contentvalidation.validator.RecipeChildNodeValidator;
import com.freshdirect.cms.contentvalidation.validator.StructureValidator;
import com.freshdirect.cms.contentvalidation.validator.UniqueContentKeyValidator;
import com.freshdirect.cms.validation.validator.Validator;

@Service
public class ContentValidatorConfigurationService {

    @Autowired
    private ConditionalFieldValidator conditionalFieldValidator;

    @Autowired(required = false)
    private ConfiguredProductValidator configuredProductValidator;

    @Autowired
    private DateIntervalValidator dateIntervalValidator;

    @Autowired
    private PrimaryHomeValidator primaryHomeValidator;

    @Autowired
    private RecipeChildNodeValidator recipeChildNodeValidator;

    @Autowired
    private StructureValidator structureValidator;

    @Autowired
    private UniqueContentKeyValidator uniqueContentKeyValidator;

    @Autowired
    private RatingGroupValidator ratingGroupValidator;

    public List<Validator> getValidators() {
        if (configuredProductValidator != null) {
            return Arrays.asList(conditionalFieldValidator, configuredProductValidator, dateIntervalValidator, primaryHomeValidator, recipeChildNodeValidator, structureValidator,
                    uniqueContentKeyValidator, ratingGroupValidator);
        } else {
            return Arrays.asList(conditionalFieldValidator, dateIntervalValidator, primaryHomeValidator, recipeChildNodeValidator, structureValidator,
                uniqueContentKeyValidator, ratingGroupValidator);
        }
    }
}
