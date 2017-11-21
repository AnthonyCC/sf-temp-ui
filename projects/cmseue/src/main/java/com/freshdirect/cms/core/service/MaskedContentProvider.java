package com.freshdirect.cms.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.google.common.base.Optional;

public class MaskedContentProvider extends ContextualContentProvider {

    private final ContextualContentProvider baseProvider;

    private final ContextualContentProvider maskProvider;

    public MaskedContentProvider(ContextualContentProvider baseProvider, ContextualContentProvider maskProvider) {
        super.contextService = new ContextService();
        super.contentTypeInfoService = baseProvider.contentTypeInfoService;

        this.baseProvider = baseProvider;
        this.maskProvider = maskProvider;
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Map<Attribute, Object> content = new HashMap<Attribute, Object>();

        if (baseProvider.containsContentKey(contentKey)) {
            content.putAll(baseProvider.getAllAttributesForContentKey(contentKey));
        }
        if (maskProvider.containsContentKey(contentKey)) {
            content.putAll(maskProvider.getAllAttributesForContentKey(contentKey));
        }
        return content;
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        if (maskProvider.containsContentKey(contentKey)) {
            Optional<Object> attrVal = maskProvider.getAttributeValue(contentKey, attribute);
            if (attrVal.isPresent()) {
                return attrVal;
            }
        }
        if (baseProvider.containsContentKey(contentKey)) {
            return baseProvider.getAttributeValue(contentKey, attribute);
        }
        return Optional.absent();
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        Map<Attribute, Object> content = new HashMap<Attribute, Object>();

        if (baseProvider.containsContentKey(contentKey)) {
            content.putAll(baseProvider.getAttributeValues(contentKey, attributes));
        }
        if (maskProvider.containsContentKey(contentKey)) {
            content.putAll(maskProvider.getAttributeValues(contentKey, attributes));
        }
        return content;
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        Set<ContentKey> result = new HashSet<ContentKey>();
        result.addAll(baseProvider.getContentKeys());
        result.addAll(maskProvider.getContentKeys());
        return result;
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Set<ContentKey> result = new HashSet<ContentKey>();
        result.addAll(baseProvider.getContentKeysByType(type));
        result.addAll(maskProvider.getContentKeysByType(type));
        return result;
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key) {
        Set<ContentKey> mergedParentKeys = new HashSet<ContentKey>();
        final Set<ContentKey> baseParentKeys = baseProvider.getParentKeys(key);
        if (baseParentKeys != null) {
            mergedParentKeys.addAll(baseParentKeys);
        }
        final Set<ContentKey> maskParentKeys = maskProvider.getParentKeys(key);
        if (maskParentKeys != null) {
            mergedParentKeys.addAll(maskParentKeys);
        }

        Map<ContentKey, Map<Attribute, Object>> nodes = new HashMap<ContentKey, Map<Attribute, Object>>(mergedParentKeys.size());
        for (ContentKey contentKey : mergedParentKeys) {
            nodes.put(contentKey, getAllAttributesForContentKey(contentKey));
        }

        Map<ContentKey, Set<ContentKey>> calculatedParents = maskProvider.contentKeyParentsCollectorService.createParentKeysMap(nodes);

        return calculatedParents.containsKey(key) ? calculatedParents.get(key) : Collections.<ContentKey> emptySet();
    }

    @Override
    public void initializeContent() {
        // does nothing
    }

    @Override
    public boolean isReadOnlyContent() {
        return maskProvider.isReadOnlyContent();
    }

    @Override
    public Optional<ContentChangeSetEntity> updateContent(LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, ContentUpdateContext context) {
        Optional<ContentChangeSetEntity> result;

        if (!isReadOnlyContent()) {
            result = maskProvider.updateContent(payload, context);
        } else {
            result = Optional.absent();
        }

        return result;
    }

    @Override
    public boolean containsContentKey(ContentKey key) {
        return baseProvider.containsContentKey(key) || maskProvider.containsContentKey(key);
    }

    @Override
    public boolean isOrphan(ContentKey contentKey, ContentKey storeKey) {
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(storeKey == null || ContentType.Store == storeKey.type, "Bad store key type");

        final Set<ContentKey> rootKeySet = RootContentKey.selectRootContentKeys(storeKey);

        boolean orphan = true;

        if (rootKeySet.contains(contentKey)) {
            orphan = false;
        } else {
            Set<ContentKey> topKeys = contextService.selectTopKeysOf(contentKey, findContextsOf(contentKey));
            topKeys.retainAll(rootKeySet);
            orphan = topKeys.isEmpty();
        }

        return orphan;
    }

}
