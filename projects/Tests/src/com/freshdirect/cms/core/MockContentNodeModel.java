package com.freshdirect.cms.core;

import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.framework.core.PrimaryKey;

public class MockContentNodeModel implements ContentNodeModel {
    private String fullName;
    private String keywords;
    private Collection parentKeys;
    private String path;
    private boolean displayable = true;
    private boolean hidden;
    private boolean orphan;
    private boolean searchable = true;
    private ContentKey key;
    private ContentNodeModel parentNode;
    private int priority = 1;

    public MockContentNodeModel() {
    }
    
    public MockContentNodeModel(ContentKey key) {
        this.key = key;
    }

    public MockContentNodeModel(ContentType type,String id) throws InvalidContentKeyException {
        this.key = ContentKey.create(type, id);
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ContentNodeModel))
			return false;
		ContentNodeModel other = (ContentNodeModel) obj;
		if (key == null) {
			if (other.getContentKey() != null)
				return false;
		} else if (!key.equals(other.getContentKey()))
			return false;
		return true;
	}

	public String getAltText() {
        return null;
    }

    public List getAssocEditorial() {
        return null;
    }

    public String getBlurb() {
        return null;
    }

    public Html getEditorial() {
        return null;
    }

    public String getEditorialTitle() {
        return null;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGlanceName() {
        return null;
    }

    public String getHideUrl() {
        return null;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getNavName() {
        // TODO Auto-generated method stub
        return null;
    }

    public AttributeI getNotInheritedAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object getNotInheritedAttributeValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getParentKeys() {
        return parentKeys;
    }

    public String getPath() {
        return path;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isOrphan() {
        return orphan;
    }

    public boolean isSearchable() {
        return searchable;
    }


    public String getAttribute(String key, String defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getAttribute(String key, int defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean getAttribute(String key, boolean defaultValue) {
        // TODO Auto-generated method stub
        return false;
    }

    public double getAttribute(String key, double defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    public AttributeI getCmsAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    

    public Object getCmsAttributeValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public ContentKey getContentKey() {
        return key;
}

    public String getContentName() {
        return this.getContentKey().getId();
    }


    public String getContentType() {
        return key.getType().getName();
    }

    public PrimaryKey getPK() {
        return null;
    }

    public ContentNodeModel getParentNode() {
        return parentNode;
    }

    public boolean hasAttribute(String key) {
        return false;
    }

    public boolean hasParentWithName(String[] contentNames) {
        return false;
    }
    
    
    // Mock setters

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setParentKeys(Collection parentKeys) {
        this.parentKeys = parentKeys;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setOrphan(boolean orphan) {
        this.orphan = orphan;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public void setKey(ContentKey key) {
        this.key = key;
    }

    public void setParentNode(ContentNodeModel parentNode) {
        this.parentNode = parentNode;
    }

    public String toString() {
        return "Mock["+key+']';
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
        return null;
    }
    
    @Override
    public Image getSideNavImage() {
        return null;
    }

    @Override
    public int getPriority() {
    	return priority;
    }

    public void setPriority(int priority) {
		this.priority = priority;
	}
}
