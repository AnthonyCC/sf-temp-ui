package com.freshdirect.cms.ui.tapestry;

import java.util.Collections;
import java.util.Set;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;

public final class ContentKeySelectionModel implements IPropertySelectionModel {

	private final boolean showIds;
	private final boolean showParents;
	private final ContentKey[] keys;

	public ContentKeySelectionModel(ContentKey[] keys) {
		this(keys, false, false);
	}

	public ContentKeySelectionModel(ContentKey[] keys, boolean showIds, boolean showParents) {
		this.keys = keys;
		this.showIds = showIds;
		this.showParents = showParents;
	}

	public int getOptionCount() {
		return keys.length;
	}

	public Object getOption(int index) {
		return getContentKey(index);
	}

	public String getValue(int index) {
		ContentKey key = getContentKey(index);
		return key == null ? "" : key.getEncoded();
	}

	public Object translateValue(String value) {
		if ("".equals(value)) {
			return null;
		}
		ContentKey valueKey = ContentKey.decode(value);
		for (int i = 0; i < keys.length; i++) {
			ContentKey key = keys[i];
			if (key != null && key.equals(valueKey)) {
				return key;
			}
		}
		return null;
	}

	public String getLabel(int index) {
		ContentKey key = getContentKey(index);
		if (key == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		if (showIds) {
			sb.append(" (").append(key.getId()).append(")");
		}

		Set parentKeys;
		while (true) {
			sb.insert(0, getLabel(key));
			parentKeys = showParents ? CmsManager.getInstance().getParentKeys(key) : Collections.EMPTY_SET;
			if (parentKeys.isEmpty()) {
				break;
			}
			sb.insert(0, " > ");
			key = (ContentKey) parentKeys.iterator().next();
		}

		return sb.toString();
	}

	private String getLabel(ContentKey key) {
		ContentNodeI node = key.lookupContentNode();
		if (node == null) {
			return key.getEncoded();
		}
		return node.getLabel();

	}

	private ContentKey getContentKey(int index) {
		return keys[index];
	}

}