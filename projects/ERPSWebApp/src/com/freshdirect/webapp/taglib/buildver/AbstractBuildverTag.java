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
			StringBuilder uri = new StringBuilder(preProcessUri(getUri()));
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

	public static String preProcessUri(String uri) {
		String path = uri;
		String query = null;
		if (uri.indexOf('?') != -1) {
			path = uri.substring(0, uri.indexOf('?'));
			query = uri.substring(uri.indexOf('?') + 1);
		}
		String suffix = null;
		if (path.endsWith(".css"))
			suffix = ".css";
		if (path.endsWith(".js"))
			suffix = ".js";
		if (suffix == null)
			return uri;
		path = path.substring(0, path.length() - suffix.length());
		if (Buildver.getInstance().useMinified()) {
			if (!path.endsWith("-min"))
				path += "-min";
		} else {
			if (path.endsWith("-min"))
				path = path.substring(0, path.length() - "-min".length());
		}
		path += suffix;
		if (query != null)
			path += "?" + query;
		return path;
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
