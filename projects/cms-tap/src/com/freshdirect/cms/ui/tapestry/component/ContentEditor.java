package com.freshdirect.cms.ui.tapestry.component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;

public abstract class ContentEditor extends BaseComponent implements PageDetachListener {

	public ContentNodeI getEditor() {
		if (editor == null) {
			editor = findEditor(getNode().getKey().getType());
		}
		return editor;
	}

	private ContentNodeI editor;

	private ContentNodeI findEditor(ContentType type) {
		String typeName = type.getName();
		Set editorKeys = CmsManager.getInstance().getContentKeysByType(ContentType.get("CmsEditor"));
		Map nodes = CmsManager.getInstance().getContentNodes(editorKeys);
		for (Iterator i = nodes.values().iterator(); i.hasNext();) {
			ContentNodeI e = (ContentNodeI) i.next();
			if (typeName.equals(e.getAttribute("contentType").getValue().toString())) {
				return e;
			}
		}
		return null;
	}

	public void pageDetached(PageEvent arg0) {
		this.editor = null;
	}

	public AttributeI getCurrAttribute() {
		String attrName = (String) getCurrField().getAttribute("attribute").getValue();
		return getNode().getAttribute(attrName);
	}

	public abstract ContentNodeI getCurrField();

	public abstract ContentNodeI getNode();

}