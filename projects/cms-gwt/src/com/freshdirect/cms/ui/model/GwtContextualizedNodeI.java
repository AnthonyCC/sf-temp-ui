package com.freshdirect.cms.ui.model;

import java.io.Serializable;

public interface GwtContextualizedNodeI extends Serializable {
	public GwtNodeData getNodeData();
	public String getContextPath();
}
