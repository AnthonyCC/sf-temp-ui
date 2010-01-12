package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.Map;

import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;

public interface EditorDecoratorI {
	public void compareDecorate( GwtContextualizedNodeData comparedNode, Map<String, Serializable> result);
}
