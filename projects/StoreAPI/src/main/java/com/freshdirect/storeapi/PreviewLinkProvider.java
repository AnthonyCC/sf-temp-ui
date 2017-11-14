package com.freshdirect.storeapi;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.storeapi.application.CmsManager;
import com.google.common.base.Optional;

/**
 * Provides links to the preview site for content objects.
 */
@Service
public class PreviewLinkProvider {

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private DraftService draftService;

    @Autowired
    private ContentProviderService contentProviderService;

    @Autowired
    private MediaService mediaService;

    /**
     * Generate preview link for the given content node
     *
     * @param key
     *            content node key
     * @param storeKey
     *            optional, except for products. No store key results relative URI
     *
     * @return Preview URL
     */
    public String getLink(ContentKey key, ContentKey storeKey, boolean isAbsoluteUrl) {
        Assert.notNull(key);

        String uri = null;

        if (ContentType.Product == key.type) {
            if (contentProviderService.containsContentKey(key)) {
                if (storeKey == null) {
                    // storeKey is essential!
                    return null;
                }

                Map<ContentKey, ContentKey> homeKeys = contentProviderService.findPrimaryHomes(key);
                ContentKey homeKey = homeKeys.get(storeKey);
                if (homeKey != null) {
                    uri = "/pdp.jsp?catId=" + homeKey.id + "&productId=" + key.id;
                }
            }

        } else if (ContentType.Category == key.type) {
            uri = "/browse.jsp?id=" + key.id;

        } else if (ContentType.Department == key.type) {
            uri = "/browse.jsp?id=" + key.id;

        } else if (ContentType.Recipe == key.type) {
            uri = "/recipe.jsp?recipeId=" + key.id;

        } else if (ContentType.RecipeCategory == key.type) {
            uri = "/recipe_cat.jsp?catId=" + key.id;

        } else if (ContentType.RecipeSubcategory == key.type) {
            Set<ContentKey> parentKeys = contentProviderService.getParentKeys(key);
            if (parentKeys.size() > 0) {
                ContentKey parentKey = parentKeys.iterator().next();
                uri = "/recipe_subcat.jsp?catId=" + parentKey.getId() + "&subCatId=" + key.id;
            }
        } else if (ContentType.RecipeDepartment == key.type) {
            uri = "/department.jsp?deptId=" + key.id;

        } else if (ContentType.RecipeSearchPage == key.type) {
            uri = "/recipe_search.jsp?deptId=" + key.id;
        } else if (ContentType.Html == key.type) {
            Optional<Media> htmlMedia = mediaService.getMediaByContentKey(key);
            if (htmlMedia.isPresent()) {
                uri = "/test/content/preview.jsp?template=" + htmlMedia.get().getUri();
            }
        } else if (ContentType.YmalSet == key.type) {
            uri = "/test/content/ymal_set_preview.jsp?ymalSetId=" + key.id;
        } else if (ContentType.Page == key.type) {
            uri = "/page.jsp?pageId=" + key.id;
        } else if (ContentType.Tag == key.type) {
            uri = "/test/migration/products_tagged.jsp?tag=" + key.id;
        } else if (ContentType.SuperDepartment == key.type) {
            uri = "/browse.jsp?id=" + key.id;
        } else if (ContentType.WebPage == key.type) {
            Optional<Object> value = contentProviderService.getAttributeValue(key, ContentTypes.WebPage.URL);
            if (value.isPresent()) {
                uri = (String) value.get();
                if (uri != null && !uri.contains("?")) {
                    uri += ("?");
                }
            }
        } else if (ContentType.ModuleContainer == key.type) {
            uri = "/test/module_content/modulecontainer.jsp?moduleContainerId=" + key.getId();
        }

        if (uri != null && isAbsoluteUrl) {
            return uri;
        }
        if (uri != null && storeKey != null) {
            Optional<Object> value = contentProviderService.getAttributeValue(key, ContentTypes.Store.PREVIEW_HOST_NAME);
            if (value.isPresent()) {
                String previewHostName = (String) value.get();
                if (previewHostName != null) {
                    uri = "//" + previewHostName + (uri.startsWith("/") ? uri : "/" + uri);
                }
            }
        }

        DraftContext draftContext = draftContextHolder.getDraftContext();
        if (! DraftContext.MAIN.equals(draftContext) && uri != null) {
            uri = draftService.decorateUrlWithDraft(uri, draftContext.getDraftId(), draftContext.getDraftName());
        }
        return uri;
    }

    public String getLink(ContentKey key, ContentKey storeKey) {
        return getLink(key, storeKey, false);
    }

    /**
     * Method supporting legacy layer.
     * It relies on default store and draft context values.
     * Only content key is required.
     *
     * @param contentKey
     * @return
     */
    public String getPreviewLink(ContentKey contentKey) {
        Assert.notNull(contentKey);

        ContentKey storeKey = CmsManager.getInstance().getSingleStoreKey();

        return getLink(contentKey, storeKey, false);
    }
}
