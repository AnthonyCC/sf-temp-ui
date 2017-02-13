package com.freshdirect.fdstore.content;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.fdstore.cache.EhCacheUtil;

public abstract class ContentNodeModelImpl implements ContentNodeModel, Cloneable {

    private static final long serialVersionUID = -1228043164570842323L;

    public interface ContentNodeModelAttributeFinder {

        /**
         * should return true if the finder found the needed information, and no more traversal needed.
         * 
         * @param model
         * @param attribute
         * @return
         */
        public boolean check(ContentNodeModel model, Object attributeValue);

    }

    private final ContentKey key;
    private boolean fresh = true;
    private ContentNodeModel parentNode;
    private int priority = 1;
    private final String contentType;

    public ContentNodeModelImpl(ContentKey key) {
        if (key == null) {
            throw new IllegalArgumentException("ContentKey must not be null");
        }
        this.key = key;
        this.contentType = ContentNodeModelUtil.getCachedContentType(key.getType().getName());
    }

    //
    // core
    //

    protected ContentNodeI getCMSNode() {
    	ContentNodeI node = null;
    	final String cacheKey = EhCacheUtil.getRequestIdCacheKey(key.getId());
    	if (!cacheKey.isEmpty()){
    		node = EhCacheUtil.getObjectFromCache(EhCacheUtil.CMS_CONTENT_NODE_CACHE_NAME, cacheKey);
    	}
    	if (node == null){
    		node = ContentFactory.getInstance().getContentNode(key);
    		if (!cacheKey.isEmpty()){
    			EhCacheUtil.putObjectToCache(EhCacheUtil.CMS_CONTENT_NODE_CACHE_NAME, cacheKey, node);
    		}
    	}
        return node;
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
        return this.getContentKey().getId();
    }

    @Override
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
        ContentNodeI node = this.getCMSNode();
        if (node == null)
            return null;

        Object value = node.getAttributeValue(name);
        if (value != null) {
            // empty list means 'inherit from parent' - legacy functionality
            if (value instanceof List) {
                List<?> listValue = (List<?>) value;
                if (!listValue.isEmpty()) {
                    if (listValue.size() == 1 && ContentKey.NULL_KEY.equals(listValue.get(0))) {
                        return Collections.EMPTY_LIST;
                    }
                    return listValue;
                } else {
                    // inherit from parent
                }
            } else {
                return value;
            }
        }

        AttributeDefI def = node.getDefinition().getAttributeDef(name);
        if (def == null || !def.isInheritable()) {
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
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            The priority to set.
     */
    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public void setParentNode(ContentNodeModel parentNode) {
        if (!fresh && this.parentNode != parentNode) {
            throw new IllegalStateException("Cannot reparent node " + key + " from " + this.parentNode + " to " + parentNode + ". Object has to be reconstructed or cloned.");
        }
        this.parentNode = parentNode;
        fresh = false;
    }

    @Override
    public ContentNodeModel getParentNode() {
        if (parentNode == null) {
            parentNode = ContentNodeModelUtil.findDefaultParent(getContentKey());
            fresh = false;
        }
        return parentNode;
    }

    @Override
    public Collection<ContentKey> getParentKeys() {
    	Set<ContentKey> parentKeys = null;
    	final String cacheKey = EhCacheUtil.getRequestIdCacheKey(key.getId());
    	if (!cacheKey.isEmpty()){
    		parentKeys = EhCacheUtil.getObjectFromCache(EhCacheUtil.CMS_PARENT_KEY_CACHE_NAME, cacheKey);
    	}
    	if (parentKeys == null){
    		parentKeys = ContentFactory.getInstance().getParentKeys(key);
    		if (!cacheKey.isEmpty()){
    			EhCacheUtil.putObjectToCache(EhCacheUtil.CMS_PARENT_KEY_CACHE_NAME, cacheKey, parentKeys);
    		}
    	}
        return parentKeys;
    }

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

    @Override
    public Object clone() {
        try {
            ContentNodeModelImpl clone = (ContentNodeModelImpl) super.clone();
            clone.fresh = true;
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

    @Override
    public Object getNotInheritedAttributeValue(String name) {
        ContentNodeI node = this.getCMSNode();
        if (node == null) {
            return null;
        }
        return node.getAttributeValue(name);
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
   
	public String getPrimaryText(){
		return this.getAttribute("PrimaryText", null);
	}
	
	public String getRedirectURL(){
		return this.getAttribute("REDIRECT_URL", null);
	}
	
	public String getSecondaryText(){
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

    private static ContentNodeModelAttributeFinder FIND_TRUE_VALUE = new ContentNodeModelAttributeFinder() {

        @Override
        public boolean check(ContentNodeModel model, Object value) {
            return Boolean.TRUE.equals(value);
        }
    };

    @Override
    public boolean isSearchable() {
        Object attributeI = find("NOT_SEARCHABLE", FIND_TRUE_VALUE);
        return attributeI == null;
    }

    @Override
    public boolean isHidden() {
        return this.hasAttribute("HIDE_URL") && !(ContentFactory.getInstance().getPreviewMode());
    }

    @Override
    public String getHideUrl() {
        return this.getAttribute("HIDE_URL", null);
    }

    @Override
    public String getPath() {
        if (!this.getContentType().equals(ContentNodeModel.TYPE_DEPARTMENT) && !this.getContentType().equals(ContentNodeModel.TYPE_CATEGORY)
                && !this.getContentType().equals(ContentNodeModel.TYPE_PRODUCT) && !this.getContentType().equals(ContentNodeModel.TYPE_SUPERDEPARTMENT)) {
            return null;
        }
        ContentNodeModel p = this.getParentNode();
        if (p != null) {
            if (p.getContentType().equals(ContentNodeModel.TYPE_STORE)) {
                return "www.freshdirect.com/" + this.getContentName();
            }
            String parentPath = p.getPath();
            if (parentPath != null) {
                return parentPath + "/" + this.getContentName();
            }
        }
        return null;
    }

    public ContentNodeModel getStoreNode() {
        if (!this.getContentType().equals(ContentNodeModel.TYPE_DEPARTMENT) && !this.getContentType().equals(ContentNodeModel.TYPE_CATEGORY)
                && !this.getContentType().equals(ContentNodeModel.TYPE_PRODUCT) && !this.getContentType().equals(ContentNodeModel.TYPE_SUPERDEPARTMENT)) {
            return null;
        }
        ContentNodeModel p = this.getParentNode();
        if (p != null) {
            if (p.getContentType().equals(ContentNodeModel.TYPE_STORE)) {
                return p;
            }
        }
        return null;
    }

    private final static ContentKey RECIPE_ROOT_FOLDER = ContentKey.getContentKey(FDContentTypes.FDFOLDER, "recipes");
    private final static ContentKey FAQ_ROOT_FOLDER = ContentKey.getContentKey(FDContentTypes.FDFOLDER, "FAQ");

    // FIXME orphan handling is inelegant
    @Override
    public boolean isOrphan() {
        ContentNodeModel start = this;
        while ((start != null) && !(start instanceof StoreModel || RECIPE_ROOT_FOLDER.equals(start.getContentKey()) || FAQ_ROOT_FOLDER.equals(start.getContentKey()))) {
            start = start.getParentNode();
        }
        return start == null;
    }

    public Object find(String name, ContentNodeModelAttributeFinder finder) {
        ContentNodeModel current = this;
        while (current != null) {
            Object attribute = current.getNotInheritedAttributeValue(name);
            if (finder.check(current, attribute)) {
                return attribute;
            }
            current = current.getParentNode();
        }
        return null;
    }

    @Override
    public String getParentId() {
        ContentNodeModel pnode = getParentNode();
        if (pnode != null) {
            return pnode.getContentName();
        }
        return null;
    }

    @Override
    public AttributeDefI getAttributeDef(String name) {
        return getCMSNode().getDefinition().getAttributeDef(name);
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
