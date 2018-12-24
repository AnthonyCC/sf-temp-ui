package com.freshdirect.storeapi.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.log4j.Category;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNode;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.content.EnumCatalogType;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.storeapi.multistore.MultiStoreContext;
import com.freshdirect.storeapi.multistore.MultiStoreContextUtil;
import com.freshdirect.storeapi.multistore.MultiStoreProperties;
import com.google.common.base.Optional;

@CmsLegacy
public class CmsManager {

    private final static Category LOGGER = LoggerFactory.getInstance(CmsManager.class);

    private final static CmsManager INSTANCE = new CmsManager();

    private static Map<ContentKey, ContentKey> primaryHomeMap;
    
    private static Map<ContentKey, ContentKey> corporateHomeMap;

    private ContextualContentProvider contentProviderService = CmsServiceLocator.contentProviderService();

    private ContentTypeInfoService contentTypeInfoService = CmsServiceLocator.contentTypeInfoService();

    private ContentKey singleStoreKey;

    private EnumEStoreId eStoreId;

    private CmsManager() {
        this.singleStoreKey = calculateSingleStoreId();
        // guess corresponding eStore ID
        if (this.singleStoreKey != null) {
            final EnumEStoreId eStore = EnumEStoreId.valueOfContentId(singleStoreKey.getId());

            if (eStore != null) {
                this.eStoreId = eStore;
            } else {
                LOGGER.warn("Failed to find eStore Id, falling back to " + EnumEStoreId.FD);
                this.eStoreId = EnumEStoreId.FD;
            }
        } else {
            LOGGER.warn("No E-STORE ID is guessed in multi-store mode!");
        }

        initPrimaryHomeMap();
        initCorporateHomeMap();
    }

    public static CmsManager getInstance() {
        return INSTANCE;
    }

    public Set<ContentKey> getContentKeys() {
        return contentProviderService.getContentKeys();
    }

    public Set<ContentKey> getContentKeysByType(ContentType type) {
        return contentProviderService.getContentKeysByType(type);
    }

    public Set<ContentKey> getParentKeys(ContentKey contentKey) {
        return contentProviderService.getParentKeys(contentKey);
    }

    public ContentNodeI getContentNode(ContentKey contentKey) {
        return getContentNodeInternal(contentKey);
    }

    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys) {
        Map<ContentKey, ContentNodeI> result = new HashMap<ContentKey, ContentNodeI>();
        for (ContentKey contentKey : keys) {
            ContentNodeI node = getContentNode(contentKey);
            if (node != null) {
                result.put(contentKey, node);
            }
        }

        return result;
    }

    public Map<Attribute, Object> getInheritedValuesOf(ContentKey contentKey, ContentKey parentKey) {
        return contentProviderService.fetchAllContextualizedAttributesForContentKey(contentKey, parentKey, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
    }

    public Map<Attribute, Object> getInheritedValuesOf(ContentKey contentKey, List<ContentKey> context) {
        return contentProviderService.fetchAllContextualizedAttributesForContentKey(contentKey, context, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
    }

    public DraftContext getCurrentDraftContext() {
        return CmsServiceLocator.draftContextHolder().getDraftContext();
    }

    public boolean isReadOnlyContent() {
        return contentProviderService.isReadOnlyContent();
    }

    public EnumEStoreId getEStoreEnum() {
        return eStoreId;
    }

    public String getEStoreId() {
        return eStoreId != null ? eStoreId.getContentId() : null;
    }

    public ContentKey getSingleStoreKey() {
        return singleStoreKey;
    }

    public ContentKey getPrimaryHomeKey(ContentKey productKey) {
        return getPrimaryHomeKey(productKey, singleStoreKey);
    }

    public ContentKey getPrimaryHomeKey(ContentKey productKey, ContentKey storeKey) {
        if (isReadOnlyContent() && primaryHomeMap != null && productKey != null) {
            return primaryHomeMap.get(productKey);
        } else {
            Map<ContentKey, ContentKey> primaryHomes = contentProviderService.findPrimaryHomes(productKey);
            return primaryHomes.get(storeKey);
        }
    }

    public ContentKey getCorporateHomeKey(ContentKey productKey) {
        return corporateHomeMap.get(productKey);
    }

    public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate searchPredicate) {
        Set<ContentKey> result = contentProviderService.getContentKeysByType(type);
        Map<ContentKey, ContentNodeI> filteredNodes = new HashMap<ContentKey, ContentNodeI>();
        for (ContentKey contentKey : result) {
            ContentNodeI node = getContentNodeInternal(contentKey);
            if (node != null && searchPredicate.evaluate(node)) {
                filteredNodes.put(contentKey, node);
            }
        }

        return filteredNodes;
    }

    public boolean containsContentKey(ContentKey contentKey) {
        return contentProviderService.containsContentKey(contentKey);
    }

    public boolean isNodeOrphan(ContentKey contentKey) {
        return contentProviderService.isOrphan(contentKey, singleStoreKey);
    }

    public List<List<ContentKey>> findContextsOf(ContentKey contentKey) {
        final List<List<ContentKey>> allContexts = contentProviderService.findContextsOf(contentKey);

        List<List<ContentKey>> filteredContexts = null;
        if (singleStoreKey != null) {
            filteredContexts = new ArrayList<List<ContentKey>>();
            Set<ContentKey> rootKeys = RootContentKey.selectRootContentKeys(singleStoreKey);
            for (List<ContentKey> path : allContexts) {
                if (!path.isEmpty() && rootKeys.contains(path.get(path.size() - 1))) {
                    filteredContexts.add(path);
                }
            }
        } else {
            filteredContexts = allContexts;
        }

        return filteredContexts;
    }

    public Media getMedia(ContentKey mediaContentKey) {
        MediaService svc = CmsServiceLocator.mediaService();

        Optional<Media> optionalMedia = svc.getMediaByContentKey(mediaContentKey);

        return optionalMedia.orNull();
    }

    private ContentNodeI getContentNodeInternal(ContentKey contentKey) {
        Map<Attribute, Object> payload = contentProviderService.getAllAttributesForContentKey(contentKey);
        if (payload == null) {
            return null;
        }

        // add media attributes if applicable
        if (Media.isMediaType(contentKey)) {
            Media media = getMedia(contentKey);
            if (media != null) {
                final Map<ContentKey, Map<Attribute, Object>> mediaAttributes = CmsServiceLocator.mediaToAttributeConverter().convert(media);

                // merge values
                Map<Attribute, Object> extendedPayload = new HashMap<Attribute, Object>();
                if (!payload.isEmpty()) {
                    extendedPayload.putAll(payload);
                }
                if (mediaAttributes.get(contentKey) != null) {
                    extendedPayload.putAll(mediaAttributes.get(contentKey));
                }

                payload = extendedPayload;
            }
        }

        Set<ContentKey> navigableChildKeys = contentProviderService.getChildKeys(contentKey, true);

        ContentNodeI node = buildContentNode(contentKey, payload, navigableChildKeys);

        return node;
    }
    
    public void initPrimaryHomeMap() {
        if (isReadOnlyContent() && singleStoreKey != null) {
            primaryHomeMap = new HashMap<ContentKey, ContentKey>();
            Set<ContentKey> keys = getContentKeysByType(ContentType.Product);
            for (ContentKey key : keys) {
                Map<ContentKey, ContentKey> primaryHomes = contentProviderService.findPrimaryHomes(key);
                if (primaryHomes != null && primaryHomes.containsKey(singleStoreKey)) {
                    primaryHomeMap.put(key, primaryHomes.get(singleStoreKey));
                }
            }
        }
    }
    
    public void initCorporateHomeMap() {
        corporateHomeMap = new HashMap<ContentKey, ContentKey>();
        Set<ContentKey> keys = getContentKeysByType(ContentType.Product);
        for (ContentKey key : keys) {
            for (List<ContentKey> context : findContextsOf(key)) {
                for (ContentKey contentKey : context) {
                    if (ContentType.Department == contentKey.type) {
                        ContentNodeI contentNode = getContentNode(contentKey);
                        EnumCatalogType attributeValue = EnumCatalogType
                                .valueOf(NVL.apply((String) contentNode.getAttributeValue(ContentTypes.Department.catalog), EnumCatalogType.EMPTY.name()));
                        if (attributeValue.isCorporate()) {
                            corporateHomeMap.put(key, context.get(1));
                        }
                    }
                }
            }
        }
    }

    private ContentNodeI buildContentNode(ContentKey contentKey, Map<Attribute, Object> payload, Set<ContentKey> navigableChildKeys) {
        ContentNodeI node = null;

        if (payload != null) {
            node = new ContentNode(contentKey, payload, navigableChildKeys, contentTypeInfoService);
        }
        return node;
    }

    private ContentKey calculateSingleStoreId() {

        // find out current context
        final MultiStoreContext ctx = MultiStoreContextUtil.getContext();

        final String theKey;
        if (ctx.isSingleStore()) {
            //
            // == Single-Store CMS mode ==
            //

            if (ctx.isPreviewNode()) {
                // Preview mode
                final String candidate = MultiStoreProperties.getCmsStoreId();
                if (candidate == null) {
                    LOGGER.warn("[SINGLE-STORE CMS MODE] failed to acquire store key, most probably multistore.store.id property is not set!");
                    LOGGER.warn("... falling back to default key FreshDirect");
                    theKey = EnumEStoreId.FD.getContentId();
                } else {
                    theKey = candidate;
                }

                LOGGER.info("[SINGLE-STORE CMS MODE] booting as Preview Node with store key : " + theKey);
            } else {
                // find the first Store key
                Set<ContentKey> storeKeys = this.getContentKeysByType(FDContentTypes.STORE);
                LOGGER.debug(".. Available store keys in CMS are : " + storeKeys);
                if (storeKeys == null || storeKeys.isEmpty()) {
                    theKey = "FreshDirect";
                    LOGGER.error("[SINGLE-STORE CMS MODE] ATTENTION!! No store nodes found in CMS. Get ready for system failure!");
                } else {
                    theKey = storeKeys.iterator().next().getId();
                    LOGGER.info("[SINGLE-STORE CMS MODE] booting as Production Node with store key : " + theKey);
                }
            }
        } else if (ctx.isMultiStore()) {
            //
            // == Multi-Store CMS mode ==
            //
            if (!MultiStoreProperties.hasCmsStoreID()) {
                LOGGER.warn("[MULTI-STORE CMS MODE] No explicit CMS Store key is set! Will use default store key ...");
            }

            // acquire key from prop file, defaulting to FreshDirect
            theKey = MultiStoreProperties.getCmsStoreId();
            LOGGER.info("[MULTI-STORE CMS MODE] Multi-Store mode ENABLED, booting with store key : " + theKey);
        } else {
            // no-store mode
            LOGGER.info("[NO-STORE CMS MODE] CMS is not available or no store node found");
            theKey = null;
        }

        return theKey != null ? ContentKeyFactory.get(FDContentTypes.STORE, theKey) : null;
    }
}
