package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public final class TestContentBuilder {

    private ContentKey lastKey = null;

    private ContentKey currentKey = null;

    private Map<Attribute, Object> currentContent = null;

    private Map<ContentKey, Map<Attribute, Object>> content = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();

    public TestContentBuilder() {
    }

    public TestContentBuilder withEmptyBody(ContentKey contentKey) {
        completeCurrentEntry();

        content.put(contentKey, new HashMap<Attribute, Object>());

        return this;
    }

    public TestContentBuilder newSingleValueContent(ContentKey contentKey, Attribute attribute, Object value) {
        completeCurrentEntry();

        Map<Attribute, Object> singleValueMap = ImmutableMap.<Attribute, Object>of(attribute, value);

        content.put(contentKey, singleValueMap);

        return this;
    }

    public TestContentBuilder newContentWithKey(ContentKey contentKey) {
        completeCurrentEntry();

        currentKey = contentKey;
        currentContent = new HashMap<Attribute, Object>();

        return this;
    }

    public TestContentBuilder newProduct(String id, String fullName) {
        completeCurrentEntry();

        currentKey = ContentKeyFactory.get(ContentType.Product, id);
        currentContent = new HashMap<Attribute, Object>();
        currentContent.put(ContentTypes.Product.FULL_NAME, fullName);

        return this;
    }

    public TestContentBuilder newProduct(String id) {
        return newProduct(id, randomNameFor(ContentType.Product));
    }

    public TestContentBuilder newDepartment(String id, String fullName) {
        completeCurrentEntry();

        currentKey = ContentKeyFactory.get(ContentType.Department, id);
        currentContent = new HashMap<Attribute, Object>();
        currentContent.put(ContentTypes.Product.FULL_NAME, fullName);

        return this;
    }

    public TestContentBuilder newDepartment(String id) {
        return newDepartment(id, randomNameFor(ContentType.Department));
    }

    public TestContentBuilder newCategory(String id, String fullName) {
        completeCurrentEntry();

        currentKey = ContentKeyFactory.get(ContentType.Category, id);
        currentContent = new HashMap<Attribute, Object>();
        currentContent.put(ContentTypes.Product.FULL_NAME, fullName);

        return this;
    }

    public TestContentBuilder newCategory(String id) {
        return newCategory(id, randomNameFor(ContentType.Category));
    }

    public TestContentBuilder andAttribute(Attribute attribute, Object value) {
        currentContent.put(attribute, value);

        return this;
    }

    public TestContentBuilder connectedTo(ContentKey targetContentKey) {
        Assert.notNull(currentKey);
        Assert.notNull(targetContentKey);

        Optional<Relationship> relationship = findRelationshipBetween(currentKey, targetContentKey);

        Assert.isTrue(relationship.isPresent());

        connect(currentKey, relationship.get(), targetContentKey);

        return this;
    }

    private Optional<Relationship> findRelationshipBetween(ContentKey childKey, ContentKey parentKey) {
        Relationship relationship = null;

        switch (childKey.type) {
            case Department:
                Assert.isTrue(ContentType.Store == parentKey.type);
                relationship = (Relationship) ContentTypes.Store.departments;
                break;
            case Category:
                Assert.isTrue(ContentType.Department == parentKey.type || ContentType.Category == parentKey.type);
                if (ContentType.Department == parentKey.type) {
                    relationship = (Relationship) ContentTypes.Department.categories;
                }
                else if (ContentType.Category == parentKey.type) {
                    relationship =(Relationship) ContentTypes.Category.subcategories;
                }
                break;
            case Product:
                Assert.isTrue(ContentType.Category == parentKey.type);
                relationship = (Relationship) ContentTypes.Category.products;
                break;
            case Sku:
                Assert.isTrue(ContentType.Product == parentKey.type);
                relationship = (Relationship) ContentTypes.Product.skus;
                break;
            default:
                Assert.state(false, "Not implemented yet");
                break;
        }
        return Optional.fromNullable(relationship);
    }

    public TestContentBuilder newConnection(ContentKey childKey, ContentKey parentKey) {
        Assert.notNull(childKey);
        Assert.notNull(parentKey);

        completeCurrentEntry();

        Optional<Relationship> relationship = findRelationshipBetween(childKey, parentKey);

        Assert.isTrue(relationship.isPresent());

        connect(childKey, relationship.get(), parentKey);

        return this;
    }

    public TestContentBuilder asChild() {
        return connectedTo(lastKey);
    }

    private void connect(ContentKey childKey, Relationship relationship, ContentKey targetKey) {
        Assert.isTrue(relationship.isNavigable());

        Map<Attribute, Object> targetContent = content.get(targetKey);
        Assert.notNull(targetContent);

        if (RelationshipCardinality.ONE == relationship.getCardinality()) {
            targetContent.put(relationship, childKey);
        } else {
            List<ContentKey> childKeys = (List<ContentKey>) targetContent.get(relationship);
            if (childKeys == null) {
                childKeys = new ArrayList<ContentKey>();
            }
            childKeys.add(childKey);
            targetContent.put(relationship, childKeys);
        }

    }

    public Map<ContentKey, Map<Attribute, Object>> build() {
        completeCurrentEntry();

        return content;
    }

    private void completeCurrentEntry() {
        if (currentKey != null && currentContent != null) {
            content.put(currentKey, currentContent);

            lastKey = currentKey;
        }

        currentKey = null;
        currentContent = null;
    }

    private String randomNameFor(ContentType type) {
        return type.name() + "_" + RandomStringUtils.randomNumeric(6);
    }
}
