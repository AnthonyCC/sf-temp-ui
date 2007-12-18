/*
 * Created on Mar 16, 2005
 */
package com.freshdirect.cms.ui.tapestry;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.link.DefaultLinkRenderer;

/**
 * This renderer emits javascript to launch the link in a window.
 * It also submits the current form (or the one specified by the formName property).
 * 
 * @author vszathmary
 */
public class FormPopupLinkRenderer extends DefaultLinkRenderer {

	private String formName;

	private String windowName;

	private String features;

	protected void beforeBodyRender(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent link) {
		super.beforeBodyRender(writer, cycle, link);
		writer.attribute("onclick", "setDirtyFormWarning(false);");
	}

	protected String constructURL(ILinkComponent component, IRequestCycle cycle) {
		ILink link = component.getLink(cycle);
		String url = link.getURL(component.getAnchor(), true);
		String form = formName == null ? Form.get(cycle).getName() : formName;
		return "javascript:if (window.setDirtyFormWarning) setDirtyFormWarning(false); var w=window.open("
			+ quote(url)
			+ ", "
			+ quote(getWindowName())
			+ ", "
			+ quote(getFeatures())
			+ "); w.focus(); document.forms["
			+ quote(form)
			+ "].submit();";
	}

	private String quote(String str) {
		return str == null ? "''" : "'" + str + "'";
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}
}