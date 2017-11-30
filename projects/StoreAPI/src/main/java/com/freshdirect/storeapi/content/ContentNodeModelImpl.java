package com.freshdirect.storeapi.content;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.AttributeI;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public abstract class ContentNodeModelImpl implements ContentNodeModel, Cloneable {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getInstance(ContentNodeModelImpl.class);

    private static final long serialVersionUID = -1228043164570842323L;

    protected final ContentKey key;

    @Deprecated
    protected final String contentType;

    /**
     * If set, overrides default parent key
     */
    private ContentNodeModel parentNode;

    @Deprecated
    private int priority = 1;

    protected final ContentNodeI cmsNode;

    protected Map<Attribute, Object> inheritedValues;

    public ContentNodeModelImpl(ContentKey contentKey) {
        this(contentKey, null);
    }

    public ContentNodeModelImpl(ContentKey contentKey, ContentKey parentKey) {
        if (contentKey == null) {
            throw new IllegalArgumentException("ContentKey must not be null");
        }

        this.key = contentKey;
        this.contentType = ContentNodeModelUtil.getCachedContentType(contentKey.type);

        // pre-load payload
        this.cmsNode = CmsManager.getInstance().getContentNode(contentKey);

    }

    //
    // core
    //

    @Deprecated
    protected ContentNodeI getCMSNode() {
        return cmsNode;
    }

    @Override
    public ContentKey getContentKey() {
        return key;
    }

    /**
     *
     * @return the id of the content node
     */
    @Override
    public String getContentName() {
        return this.key.id;
    }

    @Override
    @Deprecated
    public String getContentType() {
        return this.contentType;
    }

    //
    // attributes
    //

    /**
     * Return the primitive values, lists, content key values for the given attributum.
     *
     * @param name
     * @return
     */
    @Override
    public Object getCmsAttributeValue(String name) {
        final AttributeI attribute = cmsNode.getAttribute(name);
        if (attribute == null)
            return null;

        // pick model value
        Object value = attribute.getValue();
        if (value != null) {
            return value;
        }

        // check if value is inheritable
        Attribute def = attribute.getDefinition();
        if (def == null || !def.getFlags().isInheritable()) {
            return null;
        }

        ContentNodeModel parent = getParentNode();
        return parent == null ? null : parent.getCmsAttributeValue(name);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected boolean hasAttribute(String key) {
        return getCmsAttributeValue(key) != null;
    }

    protected String getAttribute(String key, String defaultValue) {
        Object value = this.getCmsAttributeValue(key);
        return (value != null) ? (String) value : defaultValue;
    }

    protected int getAttribute(String key, int defaultValue) {
        Object value = this.getCmsAttributeValue(key);
        return (value != null) ? ((Integer) value).intValue() : defaultValue;
    }

    protected boolean getAttribute(String key, boolean defaultValue) {
        Object value = this.getCmsAttributeValue(key);
        return (value != null) ? ((Boolean) value).booleanValue() : defaultValue;
    }

    protected double getAttribute(String key, double defaultValue) {
        Object value = this.getCmsAttributeValue(key);
        return (value != null) ? ((Double) value).doubleValue() : defaultValue;
    }

    //
    // contextual information
    //

    /**
     * @return Returns the priority.
     */
    @Deprecated
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            The priority to set.
     */
    @Deprecated
    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public void setParentNode(ContentNodeModel parentNode) {
        this.parentNode = parentNode;
    }

    public ContentKey getParentKey() {
        ContentNodeModel parent = getParentNode();

        return parent != null ? parent.getContentKey() : null;
    }

    /**
     * Select parent key from contexts list By default a contexts lists consists of only one key chain starting from parent key up to top (store) key The only exception are
     * products
     *
     * @return selected parent key otherwise null if none found
     */
    private ContentKey findDefaultParentKey(ContentKey overrideKey) {
        if (RootContentKey.isRootKey(overrideKey)) {
            return null;
        }

        ContentKey selectedParentKey = null;

        // use overridden parent, if set
        if (ContentType.Product == overrideKey.type) {
            selectedParentKey = ContentFactory.getInstance().getPrimaryHomeKey(overrideKey);
        }

        if (selectedParentKey == null) {
            Set<ContentKey> keys = ContentFactory.getInstance().getParentKeys(overrideKey);

            selectedParentKey = !keys.isEmpty() ? keys.iterator().next() : null;
        }

        return selectedParentKey;
    }

    @Override
    public ContentNodeModel getParentNode() {
        if (parentNode == null) {
            ContentKey selectedKey = findDefaultParentKey(key);
            if (selectedKey != null) {
                parentNode = ContentFactory.getInstance().getContentNodeByKey(selectedKey);
            }
        }

        return parentNode;
    }

    // TODO - move this code to content node util
    @Override
    public boolean hasParentWithName(String[] contentNames) {
        ContentNodeModel p = this.getParentNode();
        if (p == null) {
            return false;
        }
        final String parentName = p.getContentName();
        for (int i = contentNames.length; --i >= 0;) {
            if (parentName.equals(contentNames[i])) {
                return true;
            }
        }
        return p.hasParentWithName(contentNames);
    }

    //
    // infrastructure
    //

    // TODO kill clone
    @Override
    public Object clone() {
        try {
            ContentNodeModelImpl clone = (ContentNodeModelImpl) super.clone();
            // clone.fresh = true;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ContentNodeModel) {
            return key.equals(((ContentNodeModel) o).getContentKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Very conveniently returns contentName.
     */
    @Override
    public String toString() {
        return this.getContentName();
    }

    // TODO: Remove major, monster, stupid hack to make new-style
    // store look like old-style. In old-style, everything returns null by default
    // AFTER store validates, change back to defaulting "" rather than null.
    @Override
    public String getAltText() {
        return this.getAttribute("ALT_TEXT", null);
    }

    @Override
    public String getBlurb() {
        return this.getAttribute("BLURB", null);
    }

    @Override
    public Html getEditorial() {
        return FDAttributeFactory.constructHtml(this, "EDITORIAL");
    }

    @Override
    public String getEditorialTitle() {
        return this.getAttribute("EDITORIAL_TITLE", null);
    }

    @Override
    public String getFullName() {
        return this.getAttribute("FULL_NAME", null);
    }

    public String getTopText() {
        return this.getAttribute("topText", null);
    }

    public String getBottomText() {
        return this.getAttribute("bottomText", null);
    }

    public String getPrimaryText() {
        return this.getAttribute("PrimaryText", null);
    }

    public String getRedirectURL() {
        return this.getAttribute("REDIRECT_URL", null);
    }

    public String getSecondaryText() {
        return this.getAttribute("SecondaryText", null);
    }

    @Override
    public String getGlanceName() {
        return this.getAttribute("GLANCE_NAME", null);
    }

    @Override
    public String getNavName() {
        return this.getAttribute("NAV_NAME", null);
    }

    @Override
    public String getKeywords() {
        return this.getAttribute("KEYWORDS", null);
    }

    @Override
    public boolean isSearchable() {
        final boolean isNotSearchable = this.getAttribute("NOT_SEARCHABLE", false);
        return !isNotSearchable;
    }

    @Override
    public boolean isHidden() {
        return this.hasAttribute("HIDE_URL") && !(ContentFactory.getInstance().getPreviewMode());
    }

    @Override
    public String getHideUrl() {
        return this.getAttribute("HIDE_URL", null);
    }

    // FIXME - only used by setOasSitePage - use contexts
    // Example: www.freshdirect.com/snk/snk_candy_chocolate/snk_candy_bars/gro_pid_4010559
    @Override
    public String getPath() {
        if (key.type != ContentType.Department && key.type != ContentType.Category && key.type != ContentType.Product && key.type != ContentType.SuperDepartment) {
            return null;
        }

        ContentNodeModel p = this.getParentNode();
        if (p != null) {
            if (p.getContentKey().getType().equals(ContentType.Store)) {
                return "www.freshdirect.com/" + this.getContentName();
            }
            String parentPath = p.getPath();
            if (parentPath != null) {
                return parentPath + "/" + this.getContentName();
            }
        }
        return null;
    }

    @Override
    public boolean isOrphan() {
        ContentNodeModel start = this;
        while ((start != null) && !(start instanceof StoreModel || RootContentKey.RECIPES.contentKey.equals(start.getContentKey()))) {
            start = start.getParentNode();
        }
        return start == null;
    }

    @Override
    public String getParentId() {
        ContentKey key = getParentKey();
        return key != null ? key.id : null;
    }

    @Override
    public Image getSideNavImage() {
        return FDAttributeFactory.constructImage(this, "SIDENAV_IMAGE");
    }

    @SuppressWarnings("unchecked")
    public <T extends ContentNodeModel> T getSingleRelationshipNode(String name) {
        ContentKey key = (ContentKey) getCmsAttributeValue(name);

        if (key != null) {
            return (T) ContentFactory.getInstance().getContentNodeByKey(key);
        }
        return null;
    }
}
