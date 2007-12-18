package com.freshdirect.cms.fdstore;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.labels.ILabelProvider;

/**
 * {@link com.freshdirect.cms.labels.ILabelProvider} implementation for
 * content nodes of type <code>Image</code>, <code>Html</code>, <code>MediaFolder</code>.
 */
public class MediaLabelProvider implements ILabelProvider {

	private final static Set MEDIA_TYPES = new HashSet();
	static {
		MEDIA_TYPES.add(FDContentTypes.IMAGE);
		MEDIA_TYPES.add(FDContentTypes.HTML);
		MEDIA_TYPES.add(FDContentTypes.MEDIAFOLDER);
	}

	private final static String ROOT_LABEL = "Media";

	public String getLabel(ContentNodeI node) {
		if (!MEDIA_TYPES.contains(node.getKey().getType())) {
			return null;
		}
		String path = getString(node, "path");
		if (path == null) {
			return null;
		}
		if ("/".equals(path)) {
			return ROOT_LABEL;
		}
		int idx = path.lastIndexOf("/");
		String fileName = path.substring(idx + 1);
		String title = getString(node, "title");
		return title == null ? fileName : fileName + " (" + title + ")";
	}

	private String getString(ContentNodeI node, String attrName) {
		AttributeI a = node.getAttribute(attrName);
		if (a == null) {
			return null;
		}
		Object v = a.getValue();
		return v == null ? null : String.valueOf(v);
	}

}
