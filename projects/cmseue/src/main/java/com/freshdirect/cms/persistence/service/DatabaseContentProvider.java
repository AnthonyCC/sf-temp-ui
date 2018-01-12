package com.freshdirect.cms.persistence.service;

import static com.freshdirect.cms.changecontrol.entity.ContentChangeType.CRE;
import static com.freshdirect.cms.changecontrol.entity.ContentChangeType.MOD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.freshdirect.cms.cache.CacheEvictors;
import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeDetailEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.core.converter.ScalarValueToSerializedValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentNodeComparatorUtil;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentSource;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.UpdatableContentProvider;
import com.freshdirect.cms.persistence.entity.AttributeEntity;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;
import com.freshdirect.cms.persistence.entity.converter.AttributeEntityToValueConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentKeyToContentNodeEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentNodeEntityToContentKeyConverter;
import com.freshdirect.cms.persistence.entity.converter.RelationshipToRelationshipEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ScalarToAttributeEntityConverter;
import com.freshdirect.cms.persistence.repository.AttributeEntityRepository;
import com.freshdirect.cms.persistence.repository.BatchSavingRepository;
import com.freshdirect.cms.persistence.repository.ContentNodeEntityRepository;
import com.freshdirect.cms.persistence.repository.NavigationTreeRepository;
import com.freshdirect.cms.persistence.repository.RelationshipEntityRepository;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.freshdirect.cms.validation.service.ValidatorService;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

@Profile("database")
@Service
public class DatabaseContentProvider implements ContentProvider, UpdatableContentProvider {

    private static final int CHANGEDETAIL_NEWVALUE_LENGTH_WITHOUT_DOTS = 3995;
    private static final int CHANGEDETAIL_NEWVALUE_MAX_LENGTH = 3999;
    private static final int SQL_IN_CLAUSE_MAX_ELEMENTS = 1000;

    private static final String PARENT_KEYS_CACHE_NAME = "parentKeysCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseContentProvider.class);

    private final Set<ContentKey> allKeys = Collections.newSetFromMap(new ConcurrentHashMap<ContentKey, Boolean>());

    @Autowired
    protected ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private AttributeEntityRepository attributeEntityRepository;

    @Autowired
    private AttributeEntityToValueConverter attributeEntityToValueConverter;

    @Autowired
    private ContentKeyToContentNodeEntityConverter contentKeyToContentNodeEntityConverter;

    @Autowired
    private ContentNodeEntityRepository contentNodeEntityRepository;

    @Autowired
    private ContentNodeEntityToContentKeyConverter contentNodeEntityToContentKeyConverter;

    @Autowired
    private RelationshipEntityRepository relationshipEntityRepository;

    @Autowired
    private RelationshipToRelationshipEntityConverter relationshipToRelationshipEntityConverter;

    @Autowired
    private ScalarToAttributeEntityConverter scalarToAttributeEntityConverter;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ContentChangeControlService contentChangeControlService;

    @Autowired
    private ScalarValueToSerializedValueConverter scalarValueToSerializedValueConverter;

    @Autowired
    private NavigationTreeRepository navigationTreeRepository;

    @Autowired
    private CacheEvictors cacheEvictors;

    @Autowired
    private BatchSavingRepository batchSavingRepository;

    @Override
    public ContentSource getSource() {
        return ContentSource.RELATIONAL_DATABASE;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<ContentKey, Map<Attribute, Object>> loadAll() {
        long methodStart = System.currentTimeMillis();
        
        // load all contentKeys to fill the allKeys map
        allKeys.clear();
        allKeys.addAll(contentNodeEntityToContentKeyConverter.convert(contentNodeEntityRepository.findAll()));

        List<AttributeEntity> attributeEntities = attributeEntityRepository.findAll();
        List<RelationshipEntity> relationshipEntities = relationshipEntityRepository.findAll();
        Map<ContentKey, Map<Attribute, Object>> allNodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        processFetchedAttributes(allNodes, attributeEntities);
        processFetchedRelationships(allNodes, relationshipEntities);

        // fill up result map with empty content bodies where no attributes sets
        Set<ContentKey> keysHavingEmptyBody = new HashSet<ContentKey>(allKeys);
        keysHavingEmptyBody.removeAll(allNodes.keySet());
        for (ContentKey key : keysHavingEmptyBody) {
            allNodes.put(key, new HashMap<Attribute, Object>());
        }

        buildAttributesCache(allNodes); // putting the loaded data in the "attributeCache"
        
        LOGGER.info("Database warmup executed in " + (System.currentTimeMillis() - methodStart) + " ms");
        return allNodes;
    }

    @Override
    public Map<ContentKey, Map<Attribute, Object>> getAllAttributesForContentKeys(Set<ContentKey> contentKeys) {
        List<String> contentKeysForRepository = new ArrayList<String>();
        for (ContentKey contentKey : contentKeys) {
            contentKeysForRepository.add(contentKey.toString());
        }

        List<AttributeEntity> attributeEntities = new ArrayList<AttributeEntity>();
        List<RelationshipEntity> relationshipEntities = new ArrayList<RelationshipEntity>();

        for (int i = 0; i <= contentKeysForRepository.size() / SQL_IN_CLAUSE_MAX_ELEMENTS; i++) {
            int sublistEnd = ((i * SQL_IN_CLAUSE_MAX_ELEMENTS + SQL_IN_CLAUSE_MAX_ELEMENTS) > contentKeysForRepository.size()) ? (contentKeysForRepository.size())
                    : (i * SQL_IN_CLAUSE_MAX_ELEMENTS + SQL_IN_CLAUSE_MAX_ELEMENTS);
            LOGGER.info("Loading node attributes from " + (i * SQL_IN_CLAUSE_MAX_ELEMENTS) + " to " + sublistEnd);
            attributeEntities.addAll(attributeEntityRepository.findByContentKeyIn(contentKeysForRepository.subList(i * SQL_IN_CLAUSE_MAX_ELEMENTS, sublistEnd)));
            relationshipEntities.addAll(relationshipEntityRepository.findByRelationshipSourceIn(contentKeysForRepository.subList(i * SQL_IN_CLAUSE_MAX_ELEMENTS, sublistEnd)));
        }

        Map<ContentKey, Map<Attribute, Object>> nodesInQuestion = new HashMap<ContentKey, Map<Attribute, Object>>();

        processFetchedAttributes(nodesInQuestion, attributeEntities);
        processFetchedRelationships(nodesInQuestion, relationshipEntities);

        return nodesInQuestion;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ContentKey, Set<ContentKey>> generateParentKeysMap() {
        return navigationTreeRepository.fetchParentKeysMap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");

        Map<Attribute, Object> attributesWithValues = null;

        Cache attributeCache = cacheManager.getCache("attributeCache");
        ValueWrapper cachedAttributes = attributeCache.get(contentKey);
        if (cachedAttributes != null) {
            attributesWithValues = (Map<Attribute, Object>) cachedAttributes.get();
        } else {
            Set<Attribute> attributesList = contentTypeInfoService.selectAttributes(contentKey.type);
            List<String> attributeNames = collectAttributeNames(attributesList);
            attributesWithValues = Collections.emptyMap();

            if (!attributesList.isEmpty()) {
                List<AttributeEntity> attributeEntities = attributeEntityRepository.findByContentKeyAndNameIn(contentKey.toString(), attributeNames);
                List<RelationshipEntity> relationshipEntities = relationshipEntityRepository.findByRelationshipSourceAndRelationshipNameIn(contentKey.toString(), attributeNames);

                attributesWithValues = collectAttributesForContentKey(contentKey, attributeEntities, relationshipEntities,
                        contentTypeInfoService.selectAttributes(contentKey.type));
            }

            attributeCache.put(contentKey, attributesWithValues);
        }
        return new HashMap<Attribute, Object>(attributesWithValues);
    }

    @Override
    public Map<ContentKey, Map<Attribute, Object>> getAttributesForContentKeys(List<ContentKey> contentKeys, List<? extends Attribute> attributes) {
        Assert.notNull(contentKeys, "ContentKeys parameter can't be null!");
        Assert.notNull(attributes, "Attributes parameter can't be null!");

        Map<ContentKey, Map<Attribute, Object>> result = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey contentKey : contentKeys) {
            Map<Attribute, Object> attributesForContentKey = selectAttributes(getAllAttributesForContentKey(contentKey), attributes);
            if (!attributesForContentKey.isEmpty()) {
                result.put(contentKey, attributesForContentKey);
            }
        }
        return result;
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attributes, "Attributes parameter can't be null!");

        Map<Attribute, Object> allAttributesWithValues = getAllAttributesForContentKey(contentKey);

        return selectAttributes(allAttributesWithValues, attributes);
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attribute, "Attribute parameter can't be null!");

        Map<Attribute, Object> resultMap = getAllAttributesForContentKey(contentKey);
        return Optional.fromNullable(resultMap.get(attribute));
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        return Collections.unmodifiableSet(allKeys);
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Assert.notNull(type, "ContentType parameter can't be null!");
        final long t0 = System.currentTimeMillis();

        Set<ContentKey> result = new HashSet<ContentKey>();        
        for (ContentKey key : allKeys) {
            if (type == key.type) {
                result.add(key);
            }
        }

        final long t1 = System.currentTimeMillis();
        LOGGER.debug("Collected " + result.size() + " keys of type " + type + " in " + (t1 - t0) + " ms");

        return result;
    }

    @Cacheable(value = PARENT_KEYS_CACHE_NAME, key = "#contentKey")
    @Override
    public Set<ContentKey> getParentKeys(ContentKey contentKey) {
        Assert.notNull(contentKey, "contentKey parameter can't be null!");

        List<ContentNodeEntity> parentNodes = contentNodeEntityRepository.findParentsByContentKey(contentKey.toString());

        Set<ContentKey> result = Collections.emptySet();
        if (parentNodes != null && !parentNodes.isEmpty()) {
            List<ContentKey> convertedParentKeys = contentNodeEntityToContentKeyConverter.convert(parentNodes);
            result = new HashSet<ContentKey>(convertedParentKeys);
        }
        return result;
    }

    @Override
    @Transactional
    public void saveAttribute(ContentKey contentKey, Attribute attribute, Object attributeValue) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attribute, "Attribute parameter can't be null!");

        validateNode(contentKey, attribute, attributeValue);

        saveAttributeInternal(contentKey, attribute, attributeValue);

        cacheEvictors.evictAttributeCacheWithContentKey(contentKey);
    }

    public void saveAttributeInternal(ContentKey contentKey, Attribute attribute, Object attributeValue) {

        if (attribute instanceof Scalar) {
            saveScalar(contentKey, (Scalar) attribute, attributeValue);
        } else if (attribute instanceof Relationship) {
            Relationship relationship = (Relationship) attribute;
            saveRelationship(contentKey, relationship, attributeValue);
        }
    }

    @Override
    @Transactional
    public void saveAttributes(ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attributesWithValues, "Attribute values parameter can't be null!");

        validateNode(contentKey, attributesWithValues);

        for (Map.Entry<Attribute, Object> entry : attributesWithValues.entrySet()) {
            saveAttributeInternal(contentKey, entry.getKey(), entry.getValue());
        }

        cacheEvictors.evictAttributeCacheWithContentKey(contentKey);
    }

    private void updateContentKeys(Collection<ContentKey> contentKeys) {
        Assert.notNull(contentKeys, "Null value is not allowed");
        if (contentKeys.isEmpty()) {
            return;
        }

        // find keys not persisted in DB
        Set<ContentKey> newKeys = collectKeysNotPersisted(contentKeys);

        // update DB and all keys cache
        if (!newKeys.isEmpty()) {
            // persist new keys in DB and update content keys cache
            List<ContentNodeEntity> newKeyEntities = contentKeyToContentNodeEntityConverter.convert(new ArrayList<ContentKey>(newKeys));
            batchSavingRepository.batchSaveContentNodes(newKeyEntities);
        }
    }

    private void updateContentKeysCache(Collection<ContentKey> contentKeys) {
        allKeys.addAll(contentKeys);
    }

    private Set<ContentKey> collectKeysNotPersisted(Collection<ContentKey> contentKeys) {
        Set<String> contentIds = new HashSet<String>(contentKeys.size());
        for (ContentKey key : contentKeys) {
            contentIds.add(key.toString());
        }

        // find persisted keys
        Set<ContentKey> existingKeys = new HashSet<ContentKey>(
                contentNodeEntityToContentKeyConverter.convert(contentNodeEntityRepository.findByContentKeyIn(new ArrayList<String>(contentIds))));

        // build the key set to be created
        Set<ContentKey> newKeys = new HashSet<ContentKey>(contentKeys);
        newKeys.removeAll(existingKeys);

        return newKeys;
    }

    @SuppressWarnings("unchecked")
    private void saveRelationship(ContentKey contentKey, Relationship relationship, Object attributeValue) {
        batchSavingRepository.deleteRelationship(contentKey.toString(), relationship.getName());

        if (attributeValue != null) {
            if (RelationshipCardinality.ONE == relationship.getCardinality()) {
                if (attributeValue instanceof ContentKey) {
                    saveSingleRelationship(contentKey, relationship, (ContentKey) attributeValue);
                } else {
                    LOGGER.error("Discarding unexpected value type upon saving single-cardinality relationship " + relationship + " of " + contentKey);
                }
            } else {
                if (attributeValue instanceof List) {
                    saveMultiRelationship(contentKey, relationship, (List<ContentKey>) attributeValue);
                } else {
                    LOGGER.error("Discarding unexpected value type upon saving multi-cardinality relationship " + relationship + " of " + contentKey);
                }
            }
        }
    }

    private void saveSingleRelationship(ContentKey contentKey, Relationship relationship, ContentKey singleValue) {
        RelationshipEntity relationshipEntity = relationshipToRelationshipEntityConverter.convert(contentKey, relationship, singleValue);
        batchSavingRepository.saveSingleRelationship(relationshipEntity);
    }

    private void saveMultiRelationship(ContentKey contentKey, Relationship relationship, List<ContentKey> destinationKeys) {
        List<RelationshipEntity> relationshipEntities = relationshipToRelationshipEntityConverter.convert(contentKey, relationship, destinationKeys);
        batchSavingRepository.batchSaveRelationships(relationshipEntities);
    }

    private void saveScalar(ContentKey contentKey, Scalar scalar, Object attributeValue) {
        if (attributeValue == null) {
            batchSavingRepository.deleteScalarAttribute(contentKey.toString(), scalar.getName());
        } else {
            AttributeEntity attributeEntity = attributeEntityRepository.findByContentKeyAndName(contentKey.toString(), scalar.getName());
            attributeEntity = scalarToAttributeEntityConverter.convert(contentKey, scalar, attributeValue);

            batchSavingRepository.saveScalarAttribute(attributeEntity);
        }
    }

    @CacheEvict(value = "attributeCache", key = "#contentKey")
    public void cacheEvict(ContentKey contentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Set<ContentKey> keysToUpdate = new HashSet<ContentKey>();
        keysToUpdate.add(contentKey);
        updateContentKeysCache(keysToUpdate);
        evictParentKeysCache(keysToUpdate, Collections.<ContentKey> emptySet());
        cacheEvictors.evictContentFactoryCaches(contentKey);
        LOGGER.debug("Evicting attributeCache, parentKeysCache, nodesByIdCache and nodesByKeyCache caches for contentKey: " + contentKey);
    }

    private Map<Attribute, Object> selectAttributes(Map<Attribute, Object> allAttributes, List<? extends Attribute> attributesToSelect) {
        Map<Attribute, Object> selectedAttributes = Collections.emptyMap();
        if (allAttributes != null && !allAttributes.isEmpty()) {
            selectedAttributes = new HashMap<Attribute, Object>();
            for (Attribute attribute : attributesToSelect) {
                if (allAttributes.containsKey(attribute)) {
                    selectedAttributes.put(attribute, allAttributes.get(attribute));
                }
            }
        }
        return selectedAttributes;
    }

    private List<String> collectAttributeNames(Set<Attribute> attributes) {
        List<String> attributeNames = new ArrayList<String>();
        for (Attribute attribute : attributes) {
            attributeNames.add(attribute.getName());
        }
        return attributeNames;
    }

    private Map<Attribute, Object> collectScalarAttributes(Attribute attribute, List<AttributeEntity> attributeEntities) {
        Map<Attribute, Object> attributesWithValues = Collections.emptyMap();
        if (attribute instanceof Scalar) {
            attributesWithValues = new HashMap<Attribute, Object>();
            for (AttributeEntity attributeEntity : attributeEntities) {
                if (attributeEntity.getName().equals(attribute.getName())) {
                    attributesWithValues.put(attribute, attributeEntityToValueConverter.convert(attribute, attributeEntity));
                    break;
                }
            }
        }
        return attributesWithValues;
    }

    private Map<Attribute, Object> collectOneCardinalityRelationships(Attribute attribute, List<RelationshipEntity> relationshipEntities) {
        Map<Attribute, Object> attributesWithValues = Collections.emptyMap();
        if (attribute instanceof Relationship && RelationshipCardinality.ONE == ((Relationship) attribute).getCardinality()) {
            attributesWithValues = new HashMap<Attribute, Object>();
            for (RelationshipEntity relationshipEntity : relationshipEntities) {
                if (relationshipEntity.getRelationshipName().equals(attribute.getName())) {
                    attributesWithValues.put(attribute, attributeEntityToValueConverter.convert(attribute, relationshipEntity));
                }
            }
        }
        return attributesWithValues;
    }

    private Map<Attribute, Object> collectManyCardinalityRelationships(Attribute attribute, List<RelationshipEntity> relationshipEntities) {
        Map<Attribute, Object> attributesWithValues = Collections.emptyMap();
        if (attribute instanceof Relationship && RelationshipCardinality.MANY == ((Relationship) attribute).getCardinality()) {
            attributesWithValues = new HashMap<Attribute, Object>();

            // narrow down relationship entries to ones associated to the given attribute
            List<RelationshipEntity> filteredRelationshipValues = new ArrayList<RelationshipEntity>();
            for (RelationshipEntity relationshipEntity : relationshipEntities) {
                if (relationshipEntity.getRelationshipName().equals(attribute.getName())) {
                    filteredRelationshipValues.add(relationshipEntity);
                }
            }

            if (!filteredRelationshipValues.isEmpty()) {
                // [LP-226] handle edge case
                if (filteredRelationshipValues.size() == 1 && "Null".equals(filteredRelationshipValues.get(0).getRelationshipDestinationType())) {
                    attributesWithValues.put(attribute, Collections.emptyList());
                } else {
                    attributesWithValues.put(attribute, attributeEntityToValueConverter.convert(attribute, filteredRelationshipValues));
                }
            }
            // empty list values should be translated to null
            // but we won't fill up value map with entries having null value
        }
        return attributesWithValues;
    }

    private void validateNode(ContentKey contentKey, Attribute attribute, Object attributeValue) {
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(attribute, attributeValue);
        validateNode(contentKey, attributesWithValues);
    }

    private void validateNode(ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {
        Map<Attribute, Object> attributesForContentKey = getAllAttributesForContentKey(contentKey);
        for (Map.Entry<Attribute, Object> entry : attributesWithValues.entrySet()) {
            attributesForContentKey.put(entry.getKey(), entry.getValue());
        }
        validatorService.validate(contentKey, attributesForContentKey);
    }

    private List<AttributeEntity> selectAttributesByContentKeyAndName(List<AttributeEntity> attributes, ContentKey contentKey, List<String> names) {
        if (attributes == null || attributes.isEmpty()) {
            return Collections.emptyList();
        }

        List<AttributeEntity> selected = new ArrayList<AttributeEntity>();
        final String selectorKey = contentKey.toString();
        for (AttributeEntity attribute : attributes) {
            final String entityKey = attribute.getContentKey();
            if (entityKey == null) {
                LOGGER.error("Attribute " + attribute.getId() + " had no content key!");
            } else {
                if (entityKey.equals(selectorKey) && names.contains(attribute.getName())) {
                    selected.add(attribute);
                }
            }
        }
        return selected;
    }

    private List<RelationshipEntity> selectRelationshipsByContentKeyAndName(List<RelationshipEntity> relationships, ContentKey contentKey, List<String> names) {
        List<RelationshipEntity> selected = new ArrayList<RelationshipEntity>();
        for (RelationshipEntity relationship : relationships) {
            if (relationship.getRelationshipSource().equals(contentKey.toString()) && names.contains(relationship.getRelationshipName())) {
                selected.add(relationship);
            }
        }
        return selected;
    }

    private void buildAttributesCache(Map<ContentKey, Map<Attribute, Object>> allNodes) {
        Cache attributeCache = cacheManager.getCache("attributeCache");
        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : allNodes.entrySet()) {
            attributeCache.put(entry.getKey(), entry.getValue());
        }
    }

    private Map<Attribute, Object> collectAttributesForContentKey(ContentKey contentKey, List<AttributeEntity> attributeEntitiesToSearch,
            List<RelationshipEntity> relationshipEntitiesToSearch, Set<Attribute> attributesOfContentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");

        Map<Attribute, Object> attributesWithValues = Collections.emptyMap();

        if (!attributesOfContentKey.isEmpty()) {
            List<String> attributeNames = collectAttributeNames(attributesOfContentKey);

            List<AttributeEntity> attributeEntities = selectAttributesByContentKeyAndName(attributeEntitiesToSearch, contentKey, attributeNames);
            List<RelationshipEntity> relationshipEntities = selectRelationshipsByContentKeyAndName(relationshipEntitiesToSearch, contentKey, attributeNames);

            attributesWithValues = new HashMap<Attribute, Object>();

            for (Attribute attribute : attributesOfContentKey) {
                if (attributeEntities != null) {
                    attributesWithValues.putAll(collectScalarAttributes(attribute, attributeEntities));
                }
                if (relationshipEntities != null) {
                    attributesWithValues.putAll(collectOneCardinalityRelationships(attribute, relationshipEntities));
                    attributesWithValues.putAll(collectManyCardinalityRelationships(attribute, relationshipEntities));
                }
            }
        }
        return attributesWithValues;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        Assert.notNull(payload, "Content payload cannot be null");
        Assert.notNull(context, "Update context must be provided");

        // -- validation phase --

        // validate updated content
        ValidationResults validationResults = validateContent(payload);
        if (validationResults.hasError()) {
            throw new ValidationFailedException("Validation failed, save not happened!", validationResults);
        }

        // collect keys of new content nodes
        Set<ContentKey> newKeys = collectKeysCreatedInPayload(payload);

        // fetch original values for creating change records
        final Map<ContentKey, Map<Attribute, Object>> originalValues = loadOriginalValues(payload);

        // collect child keys from original values
        final Set<ContentKey> originalChildKeys = collectChildKeysOf(originalValues);

        // -- update phase --

        // create content keys not existed yet
        updateContentKeys(newKeys);

        // and the remainder
        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : payload.entrySet()) {
            final ContentKey contentKey = entry.getKey();
            try {
                LOGGER.debug("Updating content with key " + contentKey);

                saveAttributes(contentKey, entry.getValue());
            } catch (DataAccessException exc) {
                LOGGER.error("Failed to update content node with key " + contentKey);
                throw new RuntimeException("Failed to update content node with key " + contentKey, exc);
            }
        }

        // -- post update phase --

        // track changes
        Optional<ContentChangeSetEntity> changes = recordChanges(newKeys, originalValues, payload, context);

        // post-save operations
        updateContentKeysCache(newKeys);
        evictParentKeysCache(payload.keySet(), originalChildKeys);
        evicAttributeCache(payload.keySet());

        return changes;
    }

    /**
     * Perform content validation on input before saving changes
     */
    private ValidationResults validateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload) {
        ValidationResults validationResults = new ValidationResults();
        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : payload.entrySet()) {
            ContentKey contentKey = entry.getKey();
            Map<Attribute, Object> allAttributes = getAllAttributesForContentKey(contentKey);
            allAttributes.putAll(entry.getValue()); // overriding the saved values with the changed ones
            validationResults.addAll(validatorService.validate(contentKey, allAttributes));
        }
        return validationResults;
    }

    private Optional<ContentChangeSetEntity> recordChanges(Set<ContentKey> newKeys, Map<ContentKey, Map<Attribute, Object>> originalValues,
            Map<ContentKey, Map<Attribute, Object>> changedValues, ContentUpdateContext context) {
        // iterate over payload by content keys
        // make change entities
        // save
        ContentChangeSetEntity updateResult = null;

        List<ContentChangeEntity> changes = prepareContentNodeChanges(newKeys, originalValues, changedValues);
        if (!changes.isEmpty()) {
            ContentChangeSetEntity changeSet = prepareChangeSet(context, changes);

            // save changeset
            updateResult = contentChangeControlService.save(changeSet);
        }

        return Optional.fromNullable(updateResult);
    }

    private List<ContentChangeEntity> prepareContentNodeChanges(Set<ContentKey> newKeys, Map<ContentKey, Map<Attribute, Object>> originalValues,
            Map<ContentKey, Map<Attribute, Object>> changedValues) {
        List<ContentChangeEntity> nodeLevelChanges = new ArrayList<ContentChangeEntity>();
        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : changedValues.entrySet()) {
            final ContentKey contentKey = entry.getKey();
            Map<Attribute, Object> originals = originalValues.get(contentKey);

            final Map<Attribute, Object> updated = entry.getValue();
            Optional<ContentChangeEntity> optionalChangeEntity = createContentChangeRecord(contentKey, originals, updated);
            if (optionalChangeEntity.isPresent()) {
                final ContentChangeEntity changeRecord = optionalChangeEntity.get();
                if (newKeys.contains(contentKey)) {
                    changeRecord.setChangeType(CRE);
                }

                nodeLevelChanges.add(changeRecord);
            }
        }
        return nodeLevelChanges;
    }

    private ContentChangeSetEntity prepareChangeSet(ContentUpdateContext context, List<ContentChangeEntity> changes) {
        ContentChangeSetEntity changeSet = new ContentChangeSetEntity();
        changeSet.setUserId(context.getAuthor());
        changeSet.setTimestamp(context.getUpdatedAt());
        if (context.getNote().isPresent()) {
            changeSet.setNote(context.getNote().get());
        }
        changeSet.setChanges(changes);
        return changeSet;
    }

    private Optional<ContentChangeEntity> createContentChangeRecord(ContentKey contentKey, Map<Attribute, Object> originalValues, Map<Attribute, Object> changedValues) {

        List<ContentChangeDetailEntity> changeDetails = new ArrayList<ContentChangeDetailEntity>();
        for (Map.Entry<Attribute, Object> entry : changedValues.entrySet()) {
            final Attribute attr = entry.getKey();
            final Object changedValue = entry.getValue();
            final Object originalValue = originalValues.get(attr);

            if (ContentNodeComparatorUtil.isValueChanged(originalValue, changedValue)) {
                ContentChangeDetailEntity detail = null;

                if (attr instanceof Scalar) {
                    detail = createScalarValueChangeDetail((Scalar) attr, originalValue, changedValue);
                } else if (attr instanceof Relationship) {
                    Relationship relationship = (Relationship) attr;

                    if (RelationshipCardinality.ONE == relationship.getCardinality()) {
                        detail = createSingleRelationshipValueChangeDetail(relationship, originalValue, changedValue);
                    } else {
                        if (relationship.getFlags().isInheritable()) {
                            detail = createEmptyListChangeDetail(relationship, originalValue, changedValue);
                        } else {
                            detail = createManyRelationshipValueChangeDetail(relationship, originalValue, changedValue);
                        }
                    }
                }

                if (detail != null) {
                    changeDetails.add(detail);
                }
            }
        }

        // wrap attribute change details into a single change entity
        Optional<ContentChangeEntity> result = Optional.absent();
        if (!changeDetails.isEmpty()) {
            ContentChangeEntity changeEntity = new ContentChangeEntity();

            changeEntity.setContentKey(contentKey);
            changeEntity.setChangeType(MOD);
            changeEntity.setDetails(changeDetails);

            result = Optional.of(changeEntity);
        }
        return result;
    }

    private ContentChangeDetailEntity createScalarValueChangeDetail(Scalar attr, Object originalValue, Object changedValue) {
        ContentChangeDetailEntity detail = new ContentChangeDetailEntity();
        detail.setAttributeName(attr.getName());

        detail.setOldValue(scalarValueToSerializedValueConverter.convert(attr, originalValue));
        detail.setNewValue(scalarValueToSerializedValueConverter.convert(attr, changedValue));

        return detail;
    }

    private ContentChangeDetailEntity createSingleRelationshipValueChangeDetail(Relationship relationship, Object originalValue, Object changedValue) {
        ContentChangeDetailEntity detail = new ContentChangeDetailEntity();
        detail.setAttributeName(relationship.getName());

        detail.setOldValue(originalValue != null ? originalValue.toString() : null);
        detail.setNewValue(changedValue != null ? changedValue.toString() : null);

        return detail;
    }

    @SuppressWarnings("unchecked")
    private ContentChangeDetailEntity createManyRelationshipValueChangeDetail(Relationship relationship, Object originalValue, Object changedValue) {
        ContentChangeDetailEntity detail = null;

        List<ContentKey> oldKeysList = originalValue != null ? (List<ContentKey>) originalValue : Collections.<ContentKey> emptyList();
        List<ContentKey> changedKeysList = changedValue != null ? (List<ContentKey>) changedValue : Collections.<ContentKey> emptyList();

        if (!oldKeysList.equals(changedKeysList)) {
            detail = new ContentChangeDetailEntity();
            detail.setAttributeName(relationship.getName());

            Joiner joiner = Joiner.on(", ").skipNulls();

            Set<ContentKey> removed = new HashSet<ContentKey>(oldKeysList);
            removed.removeAll(changedKeysList);
            Set<ContentKey> added = new HashSet<ContentKey>(changedKeysList);
            added.removeAll(oldKeysList);

            detail.setOldValue(removed.isEmpty() ? null : "Removed: " + joiner.join(removed));
            detail.setNewValue(added.isEmpty() ? null : "Added: " + joiner.join(added));

            if (detail.getNewValue() != null && detail.getNewValue().length() > CHANGEDETAIL_NEWVALUE_MAX_LENGTH) {
                detail.setNewValue(detail.getNewValue().substring(0, CHANGEDETAIL_NEWVALUE_LENGTH_WITHOUT_DOTS) + "...");
            }
        }

        return detail;
    }

    private ContentChangeDetailEntity createEmptyListChangeDetail(Relationship relationship, Object originalValue, Object changedValue) {
        ContentChangeDetailEntity detail = null;

        final boolean isOriginalEmptyList = originalValue instanceof List && ((List<?>) originalValue).isEmpty();
        final boolean isChangedValueEmptyList = changedValue instanceof List && ((List<?>) changedValue).isEmpty();

        if (isOriginalEmptyList && changedValue == null) {
            detail = new ContentChangeDetailEntity();
            detail.setAttributeName(relationship.getName());

            detail.setOldValue("Removed: Null:null");
            detail.setNewValue(null);
        } else if (originalValue == null && isChangedValueEmptyList) {
            detail = new ContentChangeDetailEntity();
            detail.setAttributeName(relationship.getName());

            detail.setOldValue(null);
            detail.setNewValue("Added: Null:null");

        } else {
            detail = createManyRelationshipValueChangeDetail(relationship, originalValue, changedValue);
        }

        return detail;
    }

    private void invalidateParentKeysCacheEntry(Set<ContentKey> keysToInvalidate) {
        Cache parentKeysCache = cacheManager.getCache(PARENT_KEYS_CACHE_NAME);
        for (ContentKey contentKey : keysToInvalidate) {
            parentKeysCache.evict(contentKey);
        }
    }

    private Set<ContentKey> collectKeysForCacheInvalidation(Set<ContentKey> contentKeys, Collection<ContentKey> additionalKeys) {
        Set<ContentKey> keysToUpdate = new HashSet<ContentKey>();
        keysToUpdate.addAll(contentKeys);

        // extend scope with child keys
        keysToUpdate.addAll(collectChildKeysOf(contentKeys));

        // include additional keys if available
        if (additionalKeys != null) {
            keysToUpdate.addAll(additionalKeys);
        }

        return keysToUpdate;
    }

    /**
     * Collect child keys of content nodes
     *
     * @param contentKeys
     * @return
     */
    private Set<ContentKey> collectChildKeysOf(Map<ContentKey, Map<Attribute, Object>> content) {
        Set<ContentKey> childKeys = new HashSet<ContentKey>();

        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : content.entrySet()) {
            final ContentKey contentKey = entry.getKey();
            final Map<Attribute, Object> originals = entry.getValue();

            childKeys.addAll(getChildKeys(contentKey, originals, false));
        }
        return childKeys;
    }

    /**
     * Collect child keys of content nodes referenced by content keys
     *
     * @param contentKeys
     * @return
     */
    private Set<ContentKey> collectChildKeysOf(Collection<ContentKey> contentKeys) {
        Set<ContentKey> result = navigationTreeRepository.queryChildrenOfContentKey(new HashSet<ContentKey>(contentKeys));
        return result;
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
    private Set<ContentKey> getChildKeys(ContentKey key, Map<Attribute, Object> valueMap, boolean navigableOnly) {
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

    private void evictParentKeysCache(Set<ContentKey> contentKeys, Collection<ContentKey> additionalKeys) {
        Set<ContentKey> keysToUpdate = collectKeysForCacheInvalidation(contentKeys, additionalKeys);
        invalidateParentKeysCacheEntry(keysToUpdate);
    }

    private void evicAttributeCache(Set<ContentKey> contentKeys) {
        for (ContentKey contentKey : contentKeys) {
            cacheEvictors.evictAttributeCacheWithContentKey(contentKey);
        }
    }

    private void processFetchedAttributes(Map<ContentKey, Map<Attribute, Object>> allNodes, List<AttributeEntity> attributeEntities) {
        for (AttributeEntity attribute : attributeEntities) {
            // [LP-226] special case ... NOTE: attributes never use Null keys!
            if (attribute.getContentType().equals("Null")) {
                continue;
            }
            Optional<Attribute> domainAttributeOpt = contentTypeInfoService.findAttributeByName(ContentType.valueOf(attribute.getContentType()), attribute.getName());
            if (domainAttributeOpt.isPresent()) {
                Attribute domainAttribute = domainAttributeOpt.get();
                ContentKey key = ContentKeyFactory.get(attribute.getContentKey());
                if (!allNodes.containsKey(key)) {
                    allNodes.put(key, new HashMap<Attribute, Object>());
                }
                allNodes.get(key).put(domainAttribute, attributeEntityToValueConverter.convert(domainAttribute, attribute));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processFetchedRelationships(Map<ContentKey, Map<Attribute, Object>> allNodes, List<RelationshipEntity> relationshipEntities) {
        for (RelationshipEntity relationshipEntity : relationshipEntities) {
            ContentKey key = ContentKeyFactory.get(relationshipEntity.getRelationshipSource());
            Optional<Attribute> relationshipOpt = contentTypeInfoService.findAttributeByName(key.type, relationshipEntity.getRelationshipName());
            if (relationshipOpt.isPresent()) {
                Relationship relationship = (Relationship) relationshipOpt.get();
                if (!allNodes.containsKey(key)) {
                    allNodes.put(key, new HashMap<Attribute, Object>());
                }
                if (relationship.getCardinality().equals(RelationshipCardinality.ONE)) {
                    allNodes.get(key).put(relationship, attributeEntityToValueConverter.convert(relationship, relationshipEntity));
                } else {
                    if (allNodes.get(key).get(relationship) == null) {
                        allNodes.get(key).put(relationship, new ArrayList<Object>());
                    }
                    List<Object> targetKeys = (List<Object>) allNodes.get(key).get(relationship);
                    // this is important, the relationship targets has to be sorted by the ordinal!
                    // FIXME: this only works if the input is already (almost) sorted by the ordinals
                    int index = targetKeys.size() < relationshipEntity.getOrdinal() ? targetKeys.size() : relationshipEntity.getOrdinal();
                    Object value = attributeEntityToValueConverter.convert(relationship, relationshipEntity);
                    if (value != null) {
                        targetKeys.add(index, value);
                    }
                }
            }
        }
    }

    /**
     * Utility function to load not-modified values for a potentially changed payload Primarily used by {@link #updateContent(LinkedHashMap, ContentUpdateContext)} method
     *
     * @param payload
     * @return
     */
    private Map<ContentKey, Map<Attribute, Object>> loadOriginalValues(Map<ContentKey, Map<Attribute, Object>> payload) {
        // fetch original values for creating change records
        Map<ContentKey, Map<Attribute, Object>> originalValues = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : payload.entrySet()) {
            final ContentKey changedContentKey = entry.getKey();
            final Map<Attribute, Object> changedContent = entry.getValue();
            final List<Attribute> listOfChangedAttributes = new ArrayList<Attribute>(changedContent.keySet());
            final Map<Attribute, Object> originals = getAttributeValues(changedContentKey, listOfChangedAttributes);

            // record original values
            originalValues.put(changedContentKey, originals);
        }
        return originalValues;
    }

    private Set<ContentKey> collectKeysCreatedInPayload(Map<ContentKey, Map<Attribute, Object>> payload) {
        Set<ContentKey> newKeys = new HashSet<ContentKey>(payload.keySet());

        Set<ContentKey> childKeys = new HashSet<ContentKey>();
        for (ContentKey contentKey : newKeys) {
            childKeys.addAll(getChildKeys(contentKey, payload.get(contentKey), false));
        }

        newKeys.addAll(childKeys);

        Set<ContentKey> persistedContentKeys = new HashSet<ContentKey>();
        List<String> newKeysForQuery = new ArrayList<String>();
        for (ContentKey key : newKeys) {
            newKeysForQuery.add(key.toString());
        }

        for (List<String> thousandKeys : Lists.partition(newKeysForQuery, SQL_IN_CLAUSE_MAX_ELEMENTS)) {
            persistedContentKeys.addAll(contentNodeEntityToContentKeyConverter.convert(contentNodeEntityRepository.findByContentKeyIn(thousandKeys)));
        }

        if (persistedContentKeys != null) {
            newKeys.removeAll(persistedContentKeys);
        }

        return newKeys;
    }
}
