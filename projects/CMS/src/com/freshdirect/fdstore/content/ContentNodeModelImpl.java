package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public abstract class ContentNodeModelImpl implements ContentNodeModel,Cloneable, PrioritizedI {

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
        this.contentType = (String) ContentNodeModelUtil.CONTENT_TO_TYPE_MAP.get(key.getType().getName());
    }	
	

        //
        // core
        //

        protected ContentNodeI getCMSNode() {
                return CmsManager.getInstance().getContentNode(key);
        }

        public ContentKey getContentKey() {
                return key;
        }

        /**
         * 
         * @return the id of the content node
         */
        public String getContentName() {
                return this.getContentKey().getId();
        }

        public String getContentType() {
                return this.contentType;
        }

        //
        // attributes
        //

        
        /**
         * Return the primitive values, lists, content key values for the given attributum. 
         * @param name
         * @return
         */
        public Object getCmsAttributeValue(String name) {
            ContentNodeI node = this.getCMSNode();
            if (node == null) return null;

            Object value = node.getAttributeValue(name);
            if (value != null && !(value instanceof List && ((List) value).isEmpty())) {
                    return value;
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
        public int getPriority() {
                return priority;
        }

        /**
         * @param priority The priority to set.
         */
        protected void setPriority(int priority) {
                this.priority = priority;
        }

        protected void setParentNode(ContentNodeModel parentNode) {
                if (!fresh && this.parentNode != parentNode) {
                        throw new IllegalStateException("Cannot reparent node " + key
                                        + " from " + this.parentNode + " to " + parentNode
                                        + ". Object has to be reconstructed or cloned.");
                }
                this.parentNode = parentNode;
                fresh = false;
        }

        public ContentNodeModel getParentNode() {
                if (parentNode == null) {
                        parentNode = ContentNodeModelUtil.findDefaultParent(getContentKey());
                        fresh = false;
                }
                return parentNode;
        }

        public Collection<ContentKey> getParentKeys() {
            return CmsManager.getInstance().getParentKeys(key);
        }
        
        /**
         * Recursively find the first parent node of specified type.
         *
         * @return null if not found
         */
//      protected ContentNodeModel getParentNode(String contentType) {
//              ContentNodeModel p = this.getParentNode();
//              return p == null ? null : (p.getContentType().equals(contentType) ? p : p.getParentNode(contentType));
//      }

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

        public Object clone() {
                try {                   
                        ContentNodeModelImpl clone = (ContentNodeModelImpl) super.clone();
                        clone.fresh = true;
                        return clone;
                } catch (CloneNotSupportedException e) {
                        throw new RuntimeException();
                }
        }

        public boolean equals(Object o) {
                if (o == this) {
                        return true;
                }
                if (o instanceof ContentNodeModel) {
                        return key.equals(((ContentNodeModel) o).getContentKey());
                }
                return false;
        }

        public int hashCode() {
                return key.hashCode();
        }

    protected AttributeI getNotInheritedAttribute(String name) {
        ContentNodeI node = this.getCMSNode();
        if (node == null) {
            return null;
        }
        return node.getAttribute(name);
    }

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
        public String toString() {
                return this.getContentName();
        }
	
	
	
	// TODO: Remove major, monster, stupid hack to make new-style
	//       store look like old-style.  In old-style, everything returns null by default
	//       AFTER store validates, change back to defaulting "" rather than null.
	public String getAltText(){
		return this.getAttribute("ALT_TEXT", null);
	}
	
	public String getBlurb(){
		return this.getAttribute("BLURB", null);
	}
	
	public Html getEditorial(){
	        return FDAttributeFactory.constructHtml(this, "EDITORIAL");
	}
	
	public String getEditorialTitle(){
		return this.getAttribute("EDITORIAL_TITLE", null);
	}
	
	public String getFullName(){
		return this.getAttribute("FULL_NAME", null);
	}
	
	public String getGlanceName(){
		return this.getAttribute("GLANCE_NAME", null);
	}
	
	public String getNavName(){
		return this.getAttribute("NAV_NAME", null);
	}
	
	public String getKeywords() {
		return this.getAttribute("KEYWORDS", null);
	}
	
	private static ContentNodeModelAttributeFinder FIND_TRUE_VALUE = new ContentNodeModelAttributeFinder() {
	    public boolean check(ContentNodeModel model, Object value) {
	        return Boolean.TRUE.equals(value);
	    }
	};
	
	public boolean isSearchable() {
	    
	    Object attributeI = find("NOT_SEARCHABLE", FIND_TRUE_VALUE);
	    return attributeI==null;
	}

	public boolean isHidden() {
		return this.hasAttribute("HIDE_URL") && !(ContentFactory.getInstance().getPreviewMode());
	}
	
	public String getHideUrl() {
		//return (String)this.getAttribute("HIDE_URL").getValue();
        return this.getAttribute("HIDE_URL", null);
	}

	public boolean isDisplayable() {
		return true;
	}
	
	public String getPath() {
		if (!this.getContentType().equals(ContentNodeModel.TYPE_DEPARTMENT)
			&& !this.getContentType().equals(ContentNodeModel.TYPE_CATEGORY)
			&& !this.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			return null;
		}
		ContentNodeModel p = this.getParentNode();
		if (p != null) {
			if (p.getContentType().equals(ContentNodeModel.TYPE_STORE)) {
				return "www.freshdirect.com/" + this.getContentName();
			} else {
				String parentPath = p.getPath();
				if (parentPath != null) {
					return parentPath + "/" + this.getContentName();
				}
			}
		}
		return null;
	}
	
	private final static ContentKey RECIPE_ROOT_FOLDER = new ContentKey(FDContentTypes.FDFOLDER, "recipes");

	// FIXME orphan handling is inelegant
	public boolean isOrphan() {
		ContentNodeModel start = this;
		while ((start != null)
				&& !(start instanceof StoreModel || RECIPE_ROOT_FOLDER
						.equals(start.getContentKey()))) {
			start = start.getParentNode();
		}
		return start == null;
	}

	public Object find(String name, ContentNodeModelAttributeFinder finder) {
            ContentNodeModel current = this;
            while(current!=null) {
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
}
