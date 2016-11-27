package com.freshdirect.cms.ui.model.publish;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class GwtValidationError extends BaseModelData implements Serializable {
	private static final long serialVersionUID = -566936483118774194L;

	public GwtValidationError() {
	}

	public GwtValidationError(String key, String attribute, String message) {
		set("key", key);
		set("attribute", attribute);
		set("message", message);
		set("humanReadable", toHumanReadableString());
	}

	public String getKey() {
		return (String) get("key");
	}

	public String getAttribute() {
		return (String) get("attribute");
	}

	public String getMessage() {
		return (String) get("message");
	}

	public String getHumanReadable() {
		return (String) get("humanReadable");
	}

	private String toHumanReadableString() {
		return "Problem in node '" + getKey() + "' "
				+ (getAttribute() != null ? " with the " + getAttribute() + " property : " : " : ") + getMessage();
	}
}
