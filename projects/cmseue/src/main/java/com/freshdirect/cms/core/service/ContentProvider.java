package com.freshdirect.cms.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.google.common.base.Optional;

public interface ContentProvider {

    /**
     * Content source describes which features are supported by this provider
     *
     * @return
     */
    ContentSource getSource();

    /**
     * Get a list of ContentKeys from all contentKeys in the system. Every ContentKey should be in the list only once
     *
     * @return list of ContentKeys
     */
    Set<ContentKey> getContentKeys();

    /**
     * Get a list of ContentKeys from all contentKeys in the system for the given type. Every ContentKey should be in the list only once
     *
     * @param type
     *            The contentType for which the ContentKeys are returned
     * @return list of ContentKeys
     */
    Set<ContentKey> getContentKeysByType(ContentType type);

    /**
     * Obtain parent keys that refer to the given key in one of their navigable relationships
     *
     * @param contentKey
     * @return
     */
    Set<ContentKey> getParentKeys(ContentKey contentKey);


    /**
     * Get an attribute's value for the given contentKey
     *
     * @param contentKey
     *            defines for which node the attribute value is returned
     * @param attribute
     *            the attribute for which the value is returned
     * @return an {@link Optional} containing the attribute value if any
     */
    Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute);

    /**
     * Get multiple attribute values for the given contentKey. If a non-existing attribute for the contentKey is also queried, that attribute won't be in the returned map
     *
     * @param contentKey
     *            the contentKey to identify the node
     * @param attributes
     *            list of attributes for which the values will be queried
     * @return Map of attribute -> value of the attribute.
     */
    Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes);

    /**
     * Loads all of the existing attributes with their values for a contentNode
     *
     * @param contentKey
     *            ContentKey to identify the contentNode
     * @return Map of attribute -> value
     */
    Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey);

    /**
     *
     * @param contentKeys
     * @param attributes
     * @return
     */
    Map<ContentKey, Map<Attribute, Object>> getAttributesForContentKeys(List<ContentKey> contentKeys, List<? extends Attribute> attributes);

    /**
     * Save an attribute for the contentNode If the contentNode is not existing yet, it will be automatically created
     *
     * NOTE: Not every implementation supports the save operation!
     *
     * @param contentKey
     *            ContentKey to identify the ContentNode
     * @param attribute
     *            the attribute for which the value will be saved
     * @param attributeValue
     *            the value to save
     */
    void saveAttribute(ContentKey contentKey, Attribute attribute, Object attributeValue);

    /**
     * Saves multiple attribute for a contentNode, identified by the contentKey. If the contenNode is not existing yet, it will be automatically created.
     *
     * NOTE: Not every implementation supports the save operation!
     *
     * @param contentKey
     *            ContentKey to identify the ContentNode
     * @param attributesWithValues
     *            Map of Attribute->Value to save
     */
    void saveAttributes(ContentKey contentKey, Map<Attribute, Object> attributesWithValues);

    /**
     * This method is for loading everything at once. This is intended to be used for warmup-like purposes
     *
     * @return Map of all contentKey -> attributes with values
     */
    Map<ContentKey, Map<Attribute, Object>> loadAll();

    /**
     * This method calculates the full map of parent keys
     * in order to fill the parent keys cache
     *
     * @return
     */
    Map<ContentKey, Set<ContentKey>> generateParentKeysMap();

    /**
     * Loads all of the existing attributes with their values for the contentKeys
     *
     * @param contentKeys
     *            Set of ContentKey to identify the contentNodes
     * @return Map of ContentKey -> Map of attribute -> value
     */
    Map<ContentKey, Map<Attribute, Object>> getAllAttributesForContentKeys(Set<ContentKey> contentKeys);
}
