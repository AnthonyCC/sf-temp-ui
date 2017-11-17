package com.freshdirect.cms.contentvalidation.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.validator.Validator;

/**
 * Ensures that the <code>PRIMARY_HOME</code> attribute points to a valid parent for nodes of type <code>Product</code>.
 */
@Component
public class PrimaryHomeValidator implements Validator {

    public static final String INVALID_PRIMARY_HOME_MESSAGE = "Primary Homes contain invalid primary home!";
    public static final String NO_PRIMARY_HOME_MESSAGE = "There is no Primary Home for product";

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (ContentType.Product.equals(contentKey.type)) {

            Map<ContentKey, ContentKey> primaryHomesPerStore = contentSource.findPrimaryHomes(contentKey);
            Set<ContentKey> containingStores = findContainingStores(contentKey, contentSource);

            Set<ContentKey> parentsOfProduct = contentSource.getParentKeys(contentKey);
            if (parentsOfProduct == null) {
                parentsOfProduct = Collections.emptySet();
            }

            if ((containingStores == null || containingStores.isEmpty()) && (contentSource.getAttributeValue(contentKey, ContentTypes.Product.PRIMARY_HOME).isPresent()
                    && !((List<ContentKey>) contentSource.getAttributeValue(contentKey, ContentTypes.Product.PRIMARY_HOME).get()).isEmpty())) {
                validationResults.addValidationResult(contentKey, INVALID_PRIMARY_HOME_MESSAGE, ValidationResultLevel.ERROR, PrimaryHomeValidator.class);
            }

            if (containingStores != null && (contentSource.getAttributeValue(contentKey, ContentTypes.Product.PRIMARY_HOME).isPresent()
                    && ((List<ContentKey>) contentSource.getAttributeValue(contentKey, ContentTypes.Product.PRIMARY_HOME).get()).size() > containingStores.size())) {
                validationResults.addValidationResult(contentKey, INVALID_PRIMARY_HOME_MESSAGE, ValidationResultLevel.ERROR, PrimaryHomeValidator.class);
            }

            for (ContentKey storeKey : Arrays.asList(RootContentKey.STORE_FOODKICK.contentKey, RootContentKey.STORE_FRESHDIRECT.contentKey)) {
                if ((primaryHomesPerStore == null || primaryHomesPerStore.get(storeKey) == null) && !contentSource.isOrphan(contentKey, storeKey)) {
                    validationResults.addValidationResult(contentKey, NO_PRIMARY_HOME_MESSAGE, ValidationResultLevel.ERROR, PrimaryHomeValidator.class);
                }

                if ((!contentSource.isOrphan(contentKey, storeKey) && primaryHomesPerStore.get(storeKey) != null && !parentsOfProduct.contains(primaryHomesPerStore.get(storeKey)))
                        || (primaryHomesPerStore.get(storeKey) != null && contentSource.isOrphan(contentKey, storeKey))) {
                    validationResults.addValidationResult(contentKey, INVALID_PRIMARY_HOME_MESSAGE, ValidationResultLevel.ERROR, PrimaryHomeValidator.class);
                }
            }
        }
        return validationResults;
    }

    private Set<ContentKey> findContainingStores(ContentKey contentKey, ContextualContentProvider contentProviderService) {
        List<List<ContentKey>> contexts = contentProviderService.findContextsOf(contentKey);
        Set<ContentKey> storeKeys = new HashSet<ContentKey>();
        for (List<ContentKey> context : contexts) {
            ContentKey topKeyInContext = context.get(context.size() - 1);
            if (topKeyInContext.type == ContentType.Store) {
                storeKeys.add(topKeyInContext);
            }
        }
        return storeKeys;
    }
}
