package com.freshdirect.fdstore.content;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;

public abstract class ContentNodeModelImpl extends CmsContentNodeAdapter implements ContentNodeModel {

	public ContentNodeModelImpl(ContentKey key) {
		super(key);
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
		return (Html) this.getAttribute("EDITORIAL", (Html) null);
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
	
	public List getAssocEditorial(){
		return (List) this.getAttribute("ASSOC_EDITORIAL").getValue();
	}
	
	public boolean isSearchable() {
		return !this.getAttribute("NOT_SEARCHABLE", false);
	}

	public boolean isHidden() {
		return this.hasAttribute("HIDE_URL") && !(ContentFactory.getInstance().getPreviewMode());
	}
	
	public String getHideUrl() {
		return (String)this.getAttribute("HIDE_URL").getValue();
	}

	public boolean isDisplayable() {
		return true;
	}
	
	public String getPath() {
		if (!this.getContentType().equals(ContentNodeI.TYPE_DEPARTMENT)
			&& !this.getContentType().equals(ContentNodeI.TYPE_CATEGORY)
			&& !this.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) {
			return null;
		}
		ContentNodeModel p = this.getParentNode();
		if (p != null) {
			if (p.getContentType().equals(ContentNodeI.TYPE_STORE)) {
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

}
