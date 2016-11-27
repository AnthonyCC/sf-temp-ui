package com.freshdirect.webapp.taglib.buildver;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.Buildver;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public abstract class AbstractBuildverTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -7541053836956135666L;

	@Override
	public final int doStartTag() throws JspException {
		try {
			StringBuilder uri = new StringBuilder(getUri());
			if (FDStoreProperties.isBuildverEnabled()) {
				if (uri.indexOf("?") != -1)
					uri.append("&buildver=");
				else
					uri.append("?buildver=");
				uri.append(Buildver.getInstance().getBuildver());
			}
			renderTag(uri.toString(), pageContext.getOut());
		} catch (IOException e) {
			throw new FDRuntimeException(e, "cannot render tag");
		}
		return SKIP_BODY;
	}

	@Override
	public final int doEndTag() throws JspException {
		return super.doEndTag();
	}

	@Override
	public final int doAfterBody() throws JspException {
		return super.doAfterBody();
	}

	@Override
	public final void doInitBody() throws JspException {
		super.doInitBody();
	}

	protected abstract String getUri();

	protected abstract void renderTag(String uri, JspWriter out) throws IOException;
}
