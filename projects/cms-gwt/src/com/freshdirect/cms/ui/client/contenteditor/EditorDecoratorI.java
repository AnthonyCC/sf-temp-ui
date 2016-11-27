package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.Map;

import com.freshdirect.cms.ui.model.GwtNodeData;

public interface EditorDecoratorI {
	public void compareDecorate( GwtNodeData comparedNode, Map<String, Serializable> result);
}
