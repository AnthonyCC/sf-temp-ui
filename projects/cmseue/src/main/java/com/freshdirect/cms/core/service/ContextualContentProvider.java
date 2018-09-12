package com.freshdirect.cms.core.service;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.contentvalidation.service.ValidatorService;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentNodeComparatorUtil;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.google.common.base.Optional;

public abstract class ContextualContentProvider {

    @Autowired
    protected ContentTypeInfoService contentTypeInfoService;

    @Autowired
    protected ContextService contextService;

    @Autowired
    private ValidatorService validatorService;

    public abstract Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey);

    public abstract Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute);

    public abstract Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes);

    public abstract Set<ContentKey> getContentKeys();

    public abstract Set<ContentKey> getContentKeysByType(ContentType type);

    public abstract Set<ContentKey> getParentKeys(ContentKey key);

    public abstract void initializeContent();

    public abstract boolean isOrphan(ContentKey contentKey, ContentKey storeKey);

    public abstract boolean isReadOnlyContent();

    public abstract Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context);

    public abstract boolean containsContentKey(ContentKey key);

    public List<List<ContentKey>> findContextsOf(ContentKey contentKey) {
        return contextService.findContextsOf(contentKey, buildParentIndexFor(contentKey));
    }

    public Map<Attribute, Object> fetchAllContextualizedAttributesForContentKey(ContentKey contentKey, ContentKey parentKey, ContextualAttributeFetchScope fetchScope) {
        notNull(contentKey, "Content key is mandatory parameter");

        // fetch initial values for the content key itself
        Map<Attribute, Object> attributeValues = new HashMap<Attribute, Object>();
        if (ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES == fetchScope) {
            attributeValues.putAll(getAllAttributesForContentKey(contentKey));
        }

        Optional<List<ContentKey>> selectedContext = calculateSelectedContext(contentKey, parentKey);
        if (selectedContext.isPresent()) {
            // fetch inherited values
            Map<Attribute, Object> inheritedValues = fetchInheritedValuesOfContentKeyFromContext(contentKey, selectedContext.get());

            // merge results
            for (Map.Entry<Attribute, Object> inheritedValueEntry : inheritedValues.entrySet()) {
                if (!attributeValues.keySet().contains(inheritedValueEntry.getKey())) {
                    attributeValues.put(inheritedValueEntry.getKey(), inheritedValueEntry.getValue());
                }
            }
        }

        return attributeValues;
    }

    public Map<Attribute, Object> fetchAllContextualizedAttributesForContentKey(ContentKey contentKey, List<ContentKey> path, ContextualAttributeFetchScope fetchScope) {
        notNull(contentKey, "Content key is mandatory parameter");
        notNull(path, "Content key is mandatory parameter");

        // fetch initial values for the content key itself
        Map<Attribute, Object> attributeValues = new HashMap<Attribute, Object>();
        if (ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES == fetchScope) {
            attributeValues.putAll(getAllAttributesForContentKey(contentKey));
        }

        // fetch inherited values
        Map<Attribute, Object> inheritedValues = fetchInheritedValuesOfContentKeyFromContext(contentKey, path);

        // merge results
        for (Map.Entry<Attribute, Object> inheritedValueEntry : inheritedValues.entrySet()) {
            if (!attributeValues.keySet().contains(inheritedValueEntry.getKey())) {
                attributeValues.put(inheritedValueEntry.getKey(), inheritedValueEntry.getValue());
            }
        }

        return attributeValues;
    }

    public Optional<Object> fetchContextualizedAttributeValue(ContentKey contentKey, Attribute attribute, ContentKey parentKey, ContextualAttributeFetchScope fetchScope) {
        Assert.notNull(contentKey, "Content key is mandatory parameter");
        Assert.notNull(attribute, "Attribute is mandatory parameter");

        Optional<List<ContentKey>> selectedContext = calculateSelectedContext(contentKey, parentKey);

        // fetch initial value for the content key itself
        Optional<Object> attributeValue = ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES == fetchScope ? getAttributeValue(contentKey, attribute) : Optional.absent();

        // no initial value and we have context, go and get that value from parents
        if (!attributeValue.isPresent() && attribute.getFlags().isInheritable() && selectedContext.isPresent()) {
            List<ContentKey> context = selectedContext.get();

            Set<Attribute> attributesToUpdate = new HashSet<Attribute>(1);
            attributesToUpdate.add(attribute);

            int keyIndexInParentChain = 1;
            while (keyIndexInParentChain < context.size() && !attributeValue.isPresent()) {
                Set<Attribute> nonInheritableAttributes = contentTypeInfoService.findInheritableAttributesForType(context.get(keyIndexInParentChain).type, attributesToUpdate);
                if (!nonInheritableAttributes.isEmpty()) {
                    break;
                }

                attributeValue = getAttributeValue(context.get(keyIndexInParentChain), attribute);
                keyIndexInParentChain++;
            }
        }

        return attributeValue;

    }

    private Map<Attribute, Object> fetchInheritedValuesOfContentKeyFromContext(ContentKey contentKey, List<ContentKey> path) {
        Map<Attribute, Object> inheritedValues = new HashMap<Attribute, Object>();

        // expand result with missing inheritable attribute values
        for (Attribute attribute : contentTypeInfoService.selectAttributes(contentKey.type)) {
            if (attribute.getFlags().isInheritable()) {
                inheritedValues.put(attribute, null);
            }
        }

        // if not all attributes have value and we have context, go and get that value from parents
        Set<Attribute> attributesToUpdate = filterAttributesWithoutValue(inheritedValues);
        if (!attributesToUpdate.isEmpty()) {
            loadInheritedValues(path, inheritedValues);
        }

        // finally clean up null-value entries
        Iterator<Map.Entry<Attribute, Object>> resultEntries = inheritedValues.entrySet().iterator();
        while (resultEntries.hasNext()) {
            Map.Entry<Attribute, Object> entry = resultEntries.next();
            if (entry.getValue() == null) {
                resultEntries.remove();
            }
        }
        return inheritedValues;
    }

    /**
     * Find the primary homes of a product per store. Every product has a maximum of one primary home per store.
     *
     * @param productKey
     *            the product contentKey for which the primary homes will be returned
     * @return a map of store contentKey -> category (primary home) contentKey
     */

    @SuppressWarnings("unchecked")
    public Map<ContentKey, ContentKey> findPrimaryHomes(final ContentKey productKey) {
        Assert.notNull(productKey, "Product key parameter can't be null!");
        Assert.isTrue(productKey.type == ContentType.Product, "Only products have primary homes!");

        Map<ContentKey, ContentKey> primaryHomes = new HashMap<ContentKey, ContentKey>();

        List<List<ContentKey>> productContexts = findContextsOf(productKey);
        Optional<Object> savedPrimaryHomeAttributeValue = getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME);
        List<ContentKey> savedPrimaryHomes = null;
        if (savedPrimaryHomeAttributeValue.isPresent()) {
            savedPrimaryHomes = (List<ContentKey>) savedPrimaryHomeAttributeValue.get();
        } else {
            savedPrimaryHomes = Collections.emptyList();
        }
        for (List<ContentKey> context : productContexts) {
            ContentKey topKeyInContext = context.get(context.size() - 1);
            if (savedPrimaryHomes.contains(context.get(1)) && ContentType.Store == topKeyInContext.type && ContentType.Category == context.get(1).type) {
                primaryHomes.put(topKeyInContext, context.get(1));
            }
        }

        return primaryHomes;
    }

    /**
     * Sort content parent keys to store labeled buckets
     *
     * @param contentKey
     *            content key, typically of product
     * @return parent keys per store
     */
    public Map<ContentKey, Set<ContentKey>> collectParentsPerStore(ContentKey contentKey) {
        Map<ContentKey, Set<ContentKey>> parentCategoriesPerStore = new HashMap<ContentKey, Set<ContentKey>>();

        if (ContentType.Product == contentKey.getType()) {
            List<List<ContentKey>> contextList = findContextsOf(contentKey);

            for (List<ContentKey> context : contextList) {
                if (context.size() < 2) {
                    continue;
                }

                ContentKey parentCategoryKey = context.get(1);
                ContentKey topmostKey = context.get(context.size() - 1);

                if (ContentType.Store != topmostKey.type) {
                    continue;
                }

                Set<ContentKey> parentsPerStore = parentCategoriesPerStore.get(topmostKey);
                if (parentsPerStore == null) {
                    parentsPerStore = new HashSet<ContentKey>();
                    parentCategoriesPerStore.put(topmostKey, parentsPerStore);
                }
                parentsPerStore.add(parentCategoryKey);
            }

        }

        return parentCategoriesPerStore;
    }

    private void loadInheritedValues(List<ContentKey> context, Map<Attribute, Object> attributeValues) {
        if (context.size() < 2) {
            return;
        }

        // check if not all attributes have initial value and we have context, go and get that value from parents
        Set<Attribute> attributesToUpdate = filterAttributesWithoutValue(attributeValues);
        int keyIndexInParentChain = 1; // start with the parent
        while (keyIndexInParentChain < context.size() && !attributesToUpdate.isEmpty()) {

            // check if inheritance chain breaks for the rest of the attributes
            Set<Attribute> nonInheritableAttributes = contentTypeInfoService.findInheritableAttributesForType(context.get(keyIndexInParentChain).type, attributesToUpdate);
            attributesToUpdate.removeAll(nonInheritableAttributes);
            if (attributesToUpdate.isEmpty()) {
                continue;
            }

            // try to load attribute values where they are still missing
            Map<Attribute, Object> valuesForNoValueAttributes = getAttributeValues(context.get(keyIndexInParentChain), new ArrayList<Attribute>(attributesToUpdate));

            // update values with recent non-null values
            for (Map.Entry<Attribute, Object> attributeWithValueEntry : valuesForNoValueAttributes.entrySet()) {
                if (attributeWithValueEntry.getValue() != null) {
                    attributeValues.put(attributeWithValueEntry.getKey(), attributeWithValueEntry.getValue());
                }
            }

            // update set of 'empty' inheritable attributes
            attributesToUpdate = filterAttributesWithoutValue(attributeValues);

            // climb up the parent chain
            keyIndexInParentChain++;
        }
    }

    private Set<Attribute> filterAttributesWithoutValue(Map<Attribute, Object> contentValueMap) {
        Set<Attribute> noValueAttributes = new HashSet<Attribute>();
        for (Map.Entry<Attribute, Object> entry : contentValueMap.entrySet()) {
            if (entry.getKey().getFlags().isInheritable() && entry.getValue() == null) {
                noValueAttributes.add(entry.getKey());
            }
        }
        return noValueAttributes;
    }

    private Optional<List<ContentKey>> calculateSelectedContext(final ContentKey contentKey, ContentKey parentKey) {
        List<List<ContentKey>> allContextsOf = findContextsOf(contentKey);
        return contextService.selectContextOf(contentKey, parentKey, allContextsOf);
    }

    @SuppressWarnings("unchecked")
    private Set<ContentKey> collectRelationshipTargets(Map<ContentKey, Map<Attribute, Object>> payload, ContextualContentProvider contentProviderService) {
        Set<ContentKey> relationshipTargets = new HashSet<ContentKey>();
        for (ContentKey key : payload.keySet()) {
            Map<Attribute, Object> attributes = payload.get(key);
            for (Attribute changedAttribute : attributes.keySet()) {
                if (changedAttribute instanceof Relationship && attributes.get(changedAttribute) != null) {
                    Relationship relationship = (Relationship) changedAttribute;
                    Optional<Object> originalValue = contentProviderService.getAttributeValue(key, changedAttribute);
                    if (originalValue.isPresent()) {
                        if (relationship.getCardinality().equals(RelationshipCardinality.ONE)) {
                            relationshipTargets.add((ContentKey) originalValue.get());
                        } else {
                            relationshipTargets.addAll((List<ContentKey>) originalValue.get());
                        }
                    }
                }
            }
        }
        return relationshipTargets;
    }

    /** Validates and auto-corrects the payload. Sometimes the payload will be changed during validation! Validation results refer to the changed payload, not the original! */
    public void validateContent(Map<ContentKey, Map<Attribute, Object>> payload) throws ValidationFailedException {

        MaskedContentProvider modifiedContent = new MaskedContentProvider(this,
                new NodeCollectionContentProvider(contentTypeInfoService, contextService, payload));

        // collect keys to be validated
        Set<ContentKey> keysToValidate = new HashSet<ContentKey>(payload.keySet());
        keysToValidate.addAll(collectRelationshipTargets(payload, modifiedContent)); // new relationship targets
        keysToValidate.addAll(collectRelationshipTargets(payload, this)); // original relationship targets

        // validate nodes against the masked content
        ValidationResults validationResults = new ValidationResults();
        for (ContentKey key : keysToValidate) {
            Map<Attribute, Object> modifiedAttrMap = modifiedContent.getAllAttributesForContentKey(key);
            // Note: validate() can modify the attribute map, those changes need to be put back into the payload!
            validationResults.addAll(validatorService.validate(key, modifiedAttrMap, modifiedContent));
            modifiedAttrMap = modifiedContent.getAllAttributesForContentKey(key);
            // update payload if validator changed some attributes
            Map<Attribute, Object> originalAttrMap = getAllAttributesForContentKey(key);
            if (ContentNodeComparatorUtil.isNodeChanged(originalAttrMap, modifiedAttrMap)) {
                for (Attribute attribute : modifiedAttrMap.keySet()) {
                    if (ContentNodeComparatorUtil.isValueChanged(originalAttrMap.get(attribute), modifiedAttrMap.get(attribute))) {
                        Map<Attribute, Object> attrs = payload.get(key);
                        if (attrs == null) {
                            attrs = new HashMap<Attribute, Object>();
                            payload.put(key, attrs);
                        }
                        attrs.put(attribute, modifiedAttrMap.get(attribute));
                    }
                }
            }
        }
    }

    /**
     * Collect child key of content node referenced by key
     *
     * @param key
     * @param navigableOnly
     * @return
     */
    public Set<ContentKey> getChildKeys(ContentKey key, boolean navigableOnly) {
        List<Relationship> relationships = contentTypeInfoService.selectRelationships(key.type, navigableOnly);
        Map<Attribute, Object> valueMap = getAttributeValues(key, relationships);

        return getChildKeys(key, valueMap, navigableOnly);
    }

    /**
     * Collect child key of content node payload
     *
     * @param key
     * @param valueMap
     * @param navigableOnly
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<ContentKey> getChildKeys(ContentKey key, Map<Attribute, Object> valueMap, boolean navigableOnly) {
        Set<ContentKey> childKeys = new HashSet<ContentKey>();

        List<Relationship> relationships = contentTypeInfoService.selectRelationships(key.type, navigableOnly);

        for (Relationship relationship : relationships) {
            Object value = valueMap.get(relationship);
            if (value != null) {
                switch (relationship.getCardinality()) {
                    case ONE:
                        childKeys.add((ContentKey) value);
                        break;
                    case MANY:
                        childKeys.addAll((List<ContentKey>) value);
                        break;
                    default:
                        break;
                }
            }
        }

        return childKeys;
    }

    protected Set<ContentKey> collectChildKeysOf(Map<ContentKey, Map<Attribute, Object>> content) {
        Set<ContentKey> childKeys = new HashSet<ContentKey>();

        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : content.entrySet()) {
            final ContentKey contentKey = entry.getKey();
            final Map<Attribute, Object> originals = entry.getValue();

            childKeys.addAll(getChildKeys(contentKey, originals, false));
        }
        return childKeys;
    }

    protected Map<ContentKey, Set<ContentKey>> buildParentIndexFor(final ContentKey contentKey) {
        Set<ContentKey> actualParents = getParentKeys(contentKey);
        Map<ContentKey, Set<ContentKey>> parentIndex = new HashMap<ContentKey, Set<ContentKey>>();
        if (!actualParents.isEmpty()) {
            for (ContentKey parent : actualParents) {
                parentIndex.putAll(buildParentIndexFor(parent));
            }
        }
        parentIndex.put(contentKey, actualParents);

        return parentIndex;
    }

    public Set<ContentKey> getOrphanKeys() {
        Set<ContentKey> orphans = new HashSet<ContentKey>();
        for (ContentKey key : getContentKeys()) {
            if (!isIgnorableTypeForOrphans(key.type) && !RootContentKey.isRootKey(key) && getParentKeys(key).isEmpty()) {
                orphans.add(key);
            }
        }
        return orphans;
    }

    private boolean isIgnorableTypeForOrphans(ContentType type) {
        return type == ContentType.Image || type == ContentType.Html || type == ContentType.ErpCharacteristic;
    }
}
