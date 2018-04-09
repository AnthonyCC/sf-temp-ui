package com.freshdirect.cms.contentvalidation.correction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.freshdirect.cms.contentvalidation.validator.PrimaryHomeValidator;
import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.google.common.base.Optional;

@Profile({ "database", "test" })
@Component
public class PrimaryHomeCorrectionService implements CorrectionService {

    @Override
    public Class<? extends Validator> getSupportedValidator() {
        return PrimaryHomeValidator.class;
    }

    /**
     * BE AWARE if you are calling this with a not in-memory ContextualContentProvider implementation, this will modify the persistent data!
     */
    @Override
    public void correct(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        if (contentKey.type == ContentType.Product) {
            Map<ContentKey, ContentKey> fixedPrimaryHomes = pickPrimaryHomes(contentKey, contentSource);
            LinkedHashMap<ContentKey, Map<Attribute, Object>> fixedPrimaryHomesPayload = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
            Map<Attribute, Object> primaryHomes = new HashMap<Attribute, Object>();
            if (!fixedPrimaryHomes.isEmpty()) {
                primaryHomes.put(ContentTypes.Product.PRIMARY_HOME, Arrays.asList(fixedPrimaryHomes.values().toArray()));
            } else {
                primaryHomes.put(ContentTypes.Product.PRIMARY_HOME, Collections.emptyList());
            }
            fixedPrimaryHomesPayload.put(contentKey, primaryHomes);
            contentSource.updateContent(fixedPrimaryHomesPayload, null);
        }
    }

    public Map<ContentKey, ContentKey> pickPrimaryHomes(ContentKey productKey, ContextualContentProvider contentSource) {

        Map<ContentKey, ContentKey> primaryHomes = new HashMap<ContentKey, ContentKey>();

        List<List<ContentKey>> contextsOfProduct = contentSource.findContextsOf(productKey);
        Set<ContentKey> stores = new HashSet<ContentKey>();
        for (List<ContentKey> context : contextsOfProduct) {
            ContentKey topKeyByContext = context.get(context.size() - 1);
            if (topKeyByContext.type.equals(ContentType.Store)) {
                stores.add(topKeyByContext);
            }
        }

        Map<ContentKey, ContentKey> primaryHomesPerStore = contentSource.findPrimaryHomes(productKey);

        for (ContentKey storeKey : stores) {
            Optional<ContentKey> fixedPrimaryHome = fixedPrimaryHome(productKey, storeKey, null, contentSource);
            ContentKey ph = primaryHomesPerStore.get(storeKey);
            if (ph != null) {
                primaryHomes.put(storeKey, ph);
            } else if (fixedPrimaryHome.isPresent()) {
                primaryHomes.put(storeKey, fixedPrimaryHome.get());
            }
        }

        return primaryHomes;
    }

    private Set<ContentKey> filterParentsByStore(ContentKey contentKey, ContentKey storeKey, ContextualContentProvider contentSource) {
        List<List<ContentKey>> contextsOfCandidate = contentSource.findContextsOf(contentKey);

        Set<ContentKey> filteredParentKeys = new HashSet<ContentKey>();

        for (List<ContentKey> context : contextsOfCandidate) {
            if (context.get(context.size() - 1).equals(storeKey)) {
                filteredParentKeys.add(context.get(1));
            }
        }

        return filteredParentKeys;
    }

    private boolean contextContainsHiddenCategory(ContentKey productKey, ContentKey parentKey, ContextualContentProvider contentSource) {
        boolean isHidden = false;
        Optional<Object> optionalHidden = contentSource.fetchContextualizedAttributeValue(productKey, ContentTypes.Product.HIDE_URL, parentKey,
                ContextualAttributeFetchScope.EXCLUDE_MODEL_VALUES);
        if (optionalHidden.isPresent()) {
            isHidden = true;
        }
        return isHidden;
    }

    private Optional<ContentKey> fixedPrimaryHome(ContentKey productKey, ContentKey storeKey, List<ContentKey> primaryHomes, ContextualContentProvider contentSource) {
        ContentKey fixedPrimaryHomeValue = null;

        Set<ContentKey> filteredHomeCategories = filterParentsByStore(productKey, storeKey, contentSource);
        Assert.notEmpty(filteredHomeCategories);

        if (primaryHomes == null) {
            primaryHomes = new ArrayList<ContentKey>();
        }

        final Set<ContentKey> filteredPrimaryHomes = new HashSet<ContentKey>(filteredHomeCategories);
        filteredPrimaryHomes.retainAll(primaryHomes);

        if (filteredPrimaryHomes.isEmpty()) {
            // no primary home found for the given store
            // FIX IT!
            // Note: selectPrimaryHome method may return null!
            fixedPrimaryHomeValue = selectPrimaryHome(productKey, storeKey, filteredHomeCategories, contentSource);
        } else {
            fixedPrimaryHomeValue = filteredPrimaryHomes.iterator().next();
        }

        return Optional.fromNullable(fixedPrimaryHomeValue);
    }

    private ContentKey selectPrimaryHome(ContentKey productKey, ContentKey storeKey, Set<ContentKey> parentKeys, ContextualContentProvider contentSource) {
        LinkedList<ContentKey> filteredParents = new LinkedList<ContentKey>();
        for (ContentKey parent : parentKeys) {
            if (!contentSource.isOrphan(parent, storeKey)) {
                if (contextContainsHiddenCategory(productKey, parent, contentSource)) {
                    filteredParents.addLast(parent);
                } else {
                    filteredParents.addFirst(parent);
                }
            }
        }
        return filteredParents.isEmpty() ? null : filteredParents.getFirst();
    }

}
