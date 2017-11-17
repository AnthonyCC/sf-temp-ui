package com.freshdirect.webapp.taglib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.Html;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.MediaI;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.MediaUtils;

public class IncludeHtmlTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 4782537708649004233L;

	private Html html;

	private Map parameters;

	boolean withErrorReport;

	public int doStartTag() throws JspException {
		try {
			if (html == null)
				return SKIP_BODY;

			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			
			StringWriter writer = new StringWriter();
			
			// Pass the pricing context to the template context
			MediaUtils.render(html.getPath(), writer, parameters, withErrorReport, user != null
					&& user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);
			
			String out = writer.toString();
			
			//fix media if needed
			out = MediaUtils.fixMedia(out);
			
			this.pageContext.getOut().write(out);

			return SKIP_BODY;

		} catch (FileNotFoundException e) {
			return SKIP_BODY;
		} catch (IOException e) {
			return SKIP_BODY;
		} catch (TemplateException e) {
			throw new JspException(e);
		}
	}

	public Html getHtml() {
		return html;
	}

	public void setHtml(Html html) {
		this.html = html;
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public boolean isWithErrorReport() {
		return withErrorReport;
	}

	public void setWithErrorReport(boolean withErrorReport) {
		this.withErrorReport = withErrorReport;
	}
}
