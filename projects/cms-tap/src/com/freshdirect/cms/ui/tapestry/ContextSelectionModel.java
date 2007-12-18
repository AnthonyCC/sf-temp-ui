package com.freshdirect.cms.ui.tapestry;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.context.Context;

public final class ContextSelectionModel implements IPropertySelectionModel {

	private final Context[] contexts;

	public ContextSelectionModel(Context[] contexts) {
		this.contexts = contexts;
	}

	public int getOptionCount() {
		return contexts.length;
	}

	public Object getOption(int index) {
		return getContext(index);
	}

	public String getValue(int index) {
		Context ctx = getContext(index);
		return ctx == null ? "" : ctx.getPath();
	}

	public Object translateValue(String value) {
		if ("".equals(value)) {
			return null;
		}
		for (int i = 0; i < contexts.length; i++) {
			Context ctx = contexts[i];
			if (ctx != null && ctx.getPath().equals(value)) {
				return ctx;
			}
		}
		return null;
	}

	public String getLabel(int index) {
		Context ctx = getContext(index);
		ctx = ctx == null ? null : ctx.getParentContext();
		return ctx == null ? "" : ctx.getLabel();
	}

	private Context getContext(int index) {
		return contexts[index];
	}

}