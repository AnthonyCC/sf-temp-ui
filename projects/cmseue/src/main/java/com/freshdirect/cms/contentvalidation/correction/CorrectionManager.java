package com.freshdirect.cms.contentvalidation.correction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Profile({"database", "test"})
@Service
public class CorrectionManager {

    @Autowired
    private RecipeChildNodeCorrectionService recipeChildNodeCorrectionService;

    @Autowired
    private PrimaryHomeCorrectionService primaryHomeCorrectionService;

    /**
     * Runs the correction services on the given node, which is identified by the contentKey and a map of its attributes. The validationResults object holds all the validation
     * results. Only {@link ValidationResultLevel.ERROR} level validation results are going to be fixed with the correctionServices.
     *
     * NOTE: there is no guarantee that any CorrectionService exists for the error, and if any, it's not sure that the CorrectionService can fix the error
     *
     * @param contentKey
     *            the contentKey which identifies the contentNode
     * @param attributesWithValues
     *            the map which consists of the attributes of the contentNode with its values
     * @param validationResults
     *            the validationResults object, holding all the validation results
     */
    public void runCorrectionServicesOn(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ValidationResults validationResults,
            ContextualContentProvider contentSource) {
        if (validationResults.hasError()) {
            List<ValidationResult> errorResults = validationResults.getValidationResultsForLevel(ValidationResultLevel.ERROR);
            List<CorrectionService> correctionServices = getCorrectionServices();

            for (ValidationResult validationResult : errorResults) {
                for (CorrectionService correctionService : correctionServices) {
                    if (correctionService.getSupportedValidator().equals(validationResult.getValidatorClass())) {
                        correctionService.correct(contentKey, attributesWithValues, contentSource);
                    }
                }
            }
        }
    }

    private List<CorrectionService> getCorrectionServices() {
        return Arrays.asList(recipeChildNodeCorrectionService, primaryHomeCorrectionService);
    }
}
