package com.freshdirect.cms.ui.editor.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContentProviderService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
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
    private DraftContentProviderService contentProviderService;

    @Autowired
    private MediaService mediaService;

    /**
     * Get a preview link for a content object.
     *
     * @param key
     *            content key (never null)
     * @return URI string or null if no preview link exists
     */
    @Deprecated
    public String getLink(ContentKey key) {
        return getLink(key, null);
    }

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
        ContentType type = key.type;
        String id = key.id;
        String uri = null;

        if (ContentType.Product == type) {
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

        } else if (ContentType.Category == type) {
            uri = "/browse.jsp?id=" + id;

        } else if (ContentType.Department == type) {
            uri = "/browse.jsp?id=" + id;

        } else if (ContentType.Recipe == type) {
            uri = "/recipe.jsp?recipeId=" + id;

        } else if (ContentType.RecipeCategory == type) {
            uri = "/recipe_cat.jsp?catId=" + id;

        } else if (ContentType.RecipeSubcategory == type) {
            Set<ContentKey> parentKeys = contentProviderService.getParentKeys(key);
            if (parentKeys.size() > 0) {
                ContentKey parentKey = parentKeys.iterator().next();
                uri = "/recipe_subcat.jsp?catId=" + parentKey.getId() + "&subCatId=" + id;
            }
        } else if (ContentType.RecipeDepartment == type) {
            uri = "/department.jsp?deptId=" + id;

        } else if (ContentType.RecipeSearchPage == type) {
            uri = "/recipe_search.jsp?deptId=" + id;
        } else if (ContentType.Html == type) {
            Optional<Media> htmlMedia = mediaService.getMediaByContentKey(key);
            if (htmlMedia.isPresent()) {
                uri = "/test/content/preview.jsp?template=" + htmlMedia.get().getUri();
            }
        } else if (ContentType.YmalSet == type) {
            uri = "/test/content/ymal_set_preview.jsp?ymalSetId=" + id;
        } else if (ContentType.Page == type) {
            uri = "/page.jsp?pageId=" + id;
        } else if (ContentType.Tag == type) {
            uri = "/test/migration/products_tagged.jsp?tag=" + id;
        } else if (ContentType.SuperDepartment == type) {
            uri = "/browse.jsp?id=" + id;
        } else if (ContentType.WebPage == type) {
            Optional<Object> value = contentProviderService.getAttributeValue(key, ContentTypes.WebPage.URL);
            if (value.isPresent()) {
                uri = (String) value.get();
                if (uri != null && !uri.contains("?")) {
                    uri += ("?");
                }
            }
        } else if (ContentType.ModuleContainer == type) {
            uri = "/test/module_content/modulecontainer.jsp?moduleContainerId=" + key.getId();
        }

        if (uri != null && isAbsoluteUrl) {
            return uri;
        }
        if (uri != null && storeKey != null) {
            Optional<Object> value = contentProviderService.getAttributeValue(storeKey, ContentTypes.Store.PREVIEW_HOST_NAME);
            if (value.isPresent()) {
                String previewHostName = (String) value.get();
                if (previewHostName != null) {
                    uri = "//" + previewHostName + (uri.startsWith("/") ? uri : "/" + uri);
                }
            }
        }

        if (uri != null) {
            DraftContext currentDraftContext = draftContextHolder.getDraftContext();
            uri = draftService.decorateUrlWithDraft(uri, currentDraftContext.getDraftId(), currentDraftContext.getDraftName());
        }
        return uri;
    }

    public String getLink(ContentKey key, ContentKey storeKey) {
        return getLink(key, storeKey, false);
    }

}
