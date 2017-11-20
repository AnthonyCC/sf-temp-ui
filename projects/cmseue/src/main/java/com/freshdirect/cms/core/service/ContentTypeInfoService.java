package com.freshdirect.cms.core.service;

import static org.springframework.util.Assert.notNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.context.DirectedGraph;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.google.common.base.Optional;

@Service
public class ContentTypeInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentTypeInfoService.class);

    /**
     * Attribute lookup table
     */
    private Map<ContentType, Set<Attribute>> typeAttributesLookup = new HashMap<ContentType, Set<Attribute>>();

    private Map<ContentType, Set<ContentType>> inheritedTypes;

    public ContentTypeInfoService() {
        initializeAttributeLookup();
        initializeInheritedAttributes();
        typeAttributesLookup = Collections.unmodifiableMap(typeAttributesLookup);
    }

    /**
     * Returns content types defined in CMS type system.
     *
     * @return
     */
    public Set<ContentType> getContentTypes() {
        return typeAttributesLookup.keySet();
    }

    /**
     * Returns set of types reachable from input type via inheritance chain
     *
     * @param type
     * @return
     */
    public Set<ContentType> getReachableContentTypes(ContentType type) {
        final Set<ContentType> subTypes = inheritedTypes.get(type);
        return subTypes != null ? Collections.unmodifiableSet(subTypes) : Collections.<ContentType>emptySet();
    }

    /**
     * Return attributes assigned to the particular content type
     *
     * @param type
     *            Content Type
     * @return
     */
    public Set<Attribute> selectAttributes(ContentType type) {
        notNull(type, "Content type required!");

        final Set<Attribute> typeAttributes = typeAttributesLookup.get(type);
        return typeAttributes != null ? typeAttributes : Collections.<Attribute>emptySet();
    }

    /**
     * Returns attribute names of the given content type
     *
     * @param type
     * @return
     */
    public Set<String> selectAttributeNames(ContentType type) {
        notNull(type);

        Set<Attribute> attrList = typeAttributesLookup.get(type);
        if (attrList == null) {
            attrList = Collections.emptySet();
        }

        Set<String> attrNames = new HashSet<String>();
        for (Attribute attr : attrList) {
            attrNames.add(attr.getName());
        }
        return attrNames;
    }

    /**
     * Look up attribute by name for the given type
     *
     * @param type
     *            Content Type
     * @param name
     *            Attribute name
     * @return
     */
    public Optional<Attribute> findAttributeByName(ContentType type, String name) {
        notNull(type, "Content type required!");
        notNull(name, "Attribute name required!");

        Set<Attribute> attrList = selectAttributes(type);

        Optional<Attribute> result = Optional.absent();
        for (Attribute attr : attrList) {
            if (name.equals(attr.getName())) {
                result = Optional.of(attr);
                break;
            }
        }
        return result;
    }

    /**
     * Check if input attributes do exist for the given target type
     * and also inheritable
     *
     * @param contentType target content type on which attributes will be checked
     * @param attributes set of inheritable attributes
     * @return filteredAttributes attributes matched the criteria above
     */
    public Set<Attribute> findInheritableAttributesForType(ContentType contentType, Set<Attribute> attributes) {
        Set<Attribute> filteredAttributes = new HashSet<Attribute>();
        for (Attribute attr : attributes) {
            Optional<Attribute> possibleParentAttribute = findAttributeByName(contentType, attr.getName());
            if (possibleParentAttribute.isPresent() && !possibleParentAttribute.get().getFlags().isInheritable()) {
                filteredAttributes.add(possibleParentAttribute.get());
            }
        }

        return filteredAttributes;
    }


    /**
     * Return list of relationships defined on the type.
     *
     * @param type
     *            Content Type
     * @return
     */
    public List<Relationship> selectRelationships(ContentType type, boolean navigableOnly) {
        notNull(type, "Content type required!");

        List<Relationship> result = new ArrayList<Relationship>();
        if (typeAttributesLookup.get(type) != null) {
            if (navigableOnly) {
                for (Attribute attr : typeAttributesLookup.get(type)) {
                    if (attr instanceof Relationship && ((Relationship) attr).isNavigable()) {
                        result.add((Relationship) attr);
                    }
                }
            } else {
                for (Attribute attr : typeAttributesLookup.get(type)) {
                    if (attr instanceof Relationship) {
                        result.add((Relationship) attr);
                    }
                }
            }
        }

        return result;

    }

    /**
     * Collect all relationships defined in type system
     *
     * @return list of relationships assigned to content types
     */
    public Map<ContentType, List<Relationship>> selectAllRelationships() {
        Map<ContentType, List<Relationship>> result = new HashMap<ContentType, List<Relationship>>();

        for (ContentType type : ContentType.values()) {
            List<Relationship> relationshipList = selectRelationships(type, false);
            if (!relationshipList.isEmpty()) {
                result.put(type, relationshipList);
            }
        }

        return result;
    }

    /**
     * Collect all navigable relationships defined in type system
     *
     * @return list of relationships assigned to content types
     */
    public Map<ContentType, List<Relationship>> selectAllNavigableRelationships() {
        Map<ContentType, List<Relationship>> result = new HashMap<ContentType, List<Relationship>>();

        for (ContentType type : ContentType.values()) {
            List<Relationship> relationshipList = selectRelationships(type, true);
            if (!relationshipList.isEmpty()) {
                result.put(type, relationshipList);
            }
        }

        return result;
    }

    /**
     * Extract attribute definitions of the given content type. This method does not care about the attribute inheritance.
     *
     * @param type
     *            content type in scope
     * @param contentTypeDefinitionClass
     *            class containing attributes definitions stored in final fields
     *
     * @return list of identified attributes
     */
    public Set<Attribute> collectContentTypeAttributes(ContentType type, Class<?> contentTypeDefinitionClass) {
        Set<Attribute> typeAttributes = new HashSet<Attribute>();

        for (Field attrField : contentTypeDefinitionClass.getDeclaredFields()) {
            try {
                Object fieldValue = attrField.get(null);
                if (fieldValue instanceof Attribute) {
                    typeAttributes.add((Attribute) fieldValue);
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error happened during " + type + " attribute " + attrField.getName() + " calculation", e);
            }
        }

        return typeAttributes;
    }

    public Set<Attribute> collectContentTypeAttributes(ContentType type) {
        Class<?>[] contentTypeClasses = ContentTypes.class.getDeclaredClasses();
        Class<?> typeRelatedClass = null;
        for (Class<?> typeClass : contentTypeClasses) {
            if (typeClass.getName().equals(type.name())) {
                typeRelatedClass = typeClass;
                break;
            }
        }
        Set<Attribute> typeAttributes = Collections.emptySet();
        if (typeRelatedClass != null) {
            typeAttributes = collectContentTypeAttributes(type, typeRelatedClass);
        }
        return typeAttributes;
    }

    private void initializeAttributeLookup() {
        Set<ContentType> declaredTypes = new HashSet<ContentType>();
        for (Class<?> attributeHolderClass : ContentTypes.class.getDeclaredClasses()) {
            try {
                final ContentType contentType = ContentType.valueOf(attributeHolderClass.getSimpleName());

                declaredTypes.add(contentType);

                // extract attributes of the particular content type
                Set<Attribute> typeAttributes = collectContentTypeAttributes(contentType, attributeHolderClass);
                if (!typeAttributes.isEmpty()) {
                    typeAttributesLookup.put(contentType, typeAttributes);
                }

            } catch (IllegalArgumentException exc) {
                LOGGER.error("Error occurred when looking up declared attributes for content type", exc);
            }
        }
    }

    private void initializeInheritedAttributes() {
        // build graph representing attribute inheritance across content types
        DirectedGraph<ContentType> inheritanceGraph = buildInheritanceGraph(typeAttributesLookup);

        inheritedTypes = buildTypeInheritanceMap(typeAttributesLookup.keySet(), inheritanceGraph);

        // compute attributes inheritable by types
        Map<ContentType, Set<Attribute>> inheritedAttributes = buildInheritedAttributesMap(typeAttributesLookup, inheritedTypes);

        // append inherited attributes to type attribute lookup
        for (ContentType type : inheritedAttributes.keySet()) {
            appendInheritablesToTypeAttributes(type, inheritedAttributes.get(type));
        }
    }

    private void appendInheritablesToTypeAttributes(ContentType type, Set<Attribute> inheriteds) {
        // Add inherited attributes to type attribute lookup
        final Set<Attribute> rawAttributes = typeAttributesLookup.get(type);
        rawAttributes.addAll(inheriteds);
        typeAttributesLookup.put(type, Collections.unmodifiableSet(rawAttributes));
    }

    private DirectedGraph<ContentType> buildInheritanceGraph(Map<ContentType, Set<Attribute>> typeAttributes) {
        DirectedGraph<ContentType> inheritanceGraph = new DirectedGraph<ContentType>();

        for (Map.Entry<ContentType, Set<Attribute>> entry : typeAttributes.entrySet()) {
            addTypeDependenciesToGraph(entry.getKey(), entry.getValue(), inheritanceGraph);
        }

        return inheritanceGraph;
    }

    /**
     * Add new edges to inheritance graph by processing navigable relationships in type attributes An edge points from type to destination type of navigable relationship
     *
     * @param contentType
     *            source type
     * @param typeAttributes
     *            list of type attributes
     * @param inheritanceGraph
     *            graph to expand with new edges
     */
    private void addTypeDependenciesToGraph(ContentType contentType, Set<Attribute> typeAttributes, DirectedGraph<ContentType> inheritanceGraph) {
        for (Attribute attribute : typeAttributes) {
            if (attribute instanceof Relationship) {
                final Relationship relationship = (Relationship) attribute;
                if (relationship.isNavigable()) {
                    for (ContentType destType : relationship.getDestinationTypes()) {
                        inheritanceGraph.addEdge(contentType, destType);
                    }
                }
            }
        }
    }

    /**
     * Build list of inherited attributes for each content type in the given set
     *
     * @param typeAttributesMap
     *            List of attribute definitions per content types.
     * @param inheritanceGraph
     *            inheritance graph
     * @return list of inherited attribute definitions per content type
     */
    private Map<ContentType, Set<Attribute>> buildInheritedAttributesMap(Map<ContentType, Set<Attribute>> typeAttributesMap, Map<ContentType, Set<ContentType>> inheritedTypes) {
        Map<ContentType, Set<Attribute>> inheritedAttributes = new HashMap<ContentType, Set<Attribute>>();
        for (ContentType type : inheritedTypes.keySet()) {
            Set<ContentType> reachableTypes = inheritedTypes.get(type);
            if (!reachableTypes.isEmpty()) {
                Set<Attribute> inheritedAttrs = new HashSet<Attribute>();

                for (ContentType reachableType : reachableTypes) {
                    for (Attribute attr : typeAttributesMap.get(reachableType)) {
                        if (AttributeFlags.INHERITABLE == attr.getFlags()) {
                            inheritedAttrs.add(attr);
                        }
                    }
                }

                if (!inheritedAttrs.isEmpty()) {
                    inheritedAttributes.put(type, inheritedAttrs);
                }
            }
        }
        return inheritedAttributes;
    }

    private Map<ContentType, Set<ContentType>> buildTypeInheritanceMap(Collection<ContentType> types, DirectedGraph<ContentType> inheritanceGraph) {
        Map<ContentType, Set<ContentType>> inheritedTypes = new HashMap<ContentType, Set<ContentType>>();
        for (ContentType type : types) {
            Set<ContentType> reachableTypes = inheritanceGraph.findReachableNodes(type);
            if (!reachableTypes.isEmpty()) {
                inheritedTypes.put(type, reachableTypes);
            }
        }
        return inheritedTypes;
    }
}