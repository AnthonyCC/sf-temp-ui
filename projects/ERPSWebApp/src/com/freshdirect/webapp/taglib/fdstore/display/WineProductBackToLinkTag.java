package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.util.ProductDisplayUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.FDURLUtil;

public class WineProductBackToLinkTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	
	
	private static Logger LOGGER = LoggerFactory.getInstance(WineProductBackToLinkTag.class);

	
	@Override
	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();


		BackToLink link = BackToLink.DEFAULT;
		try {
			link = decodeLink(request);
		} catch (IOException exc) {
			LOGGER.error(exc);
		}
		


		try {
			link.appendLink(pageContext.getOut());
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		
		return SKIP_BODY;
	}



	private BackToLink decodeLink(HttpServletRequest request) throws IOException {
		BackToLink result = null;
		
		final String backPage = request.getParameter("backPage");
		
		if (null == request.getCharacterEncoding() ) {
			// HACK HACK HACK
			request.setCharacterEncoding("UTF-8");
		}

		final String backPageTitle = request.getParameter("refTitle");
		/* final String backPageTitle = URLDecoder.decode(parameter, "UTF-8"); */
		if (backPage != null) {
			return new BackToLink(backPage, backPageTitle);
		}
		

		// try restore page from saved parameters
		String trk = request.getParameter("_trk");
		String catId = request.getParameter("_catId");
		String deptId = request.getParameter("_deptId");
		if ( (catId != null && !"".equals(catId)) || (deptId != null && !"".equals(deptId)) ) {
			result = decodeContentNode(catId, deptId, trk, request);
			if (result != null)
				return result;
		}


		// did it came from wine filter page?
		if (request.getParameter("wineFilter") != null && !"".equals(request.getParameter("wineFilter")) ) {
			result = decodeWineFilterPage(trk, request);
			if (result != null)
				return result;
		}
		
		// fall back to catId and/or deptId
		trk = request.getParameter("trk");
		catId = request.getParameter("catId");
		deptId = request.getParameter("deptId");
		if ( (catId != null && !"".equals(catId)) || (deptId != null && !"".equals(deptId)) ) {
			result = decodeContentNode(catId, deptId, trk, request);
			if (result != null)
				return result;
		}

		return BackToLink.DEFAULT;
	}




	private BackToLink decodeContentNode(String catId, String deptId, String trk, HttpServletRequest request) throws IOException {
		if (catId != null && !"".equals(catId) ) {
			CategoryModel model = (CategoryModel) ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, catId);
			StringBuilder buf = new StringBuilder();
			buf.append( FDURLUtil.getCategoryURI(model, trk) );
			
			// append wine params
			for (String p : FDURLUtil.WINE_PARAMS) {
				String v = request.getParameter(p);
				if (request.getParameter(p) != null && !"".equals(request.getParameter(p)) ) {
					buf.append(ProductDisplayUtil.URL_PARAM_SEP).append(p).append("=").append(v);
				}
			}
			
			
			final BackToLink link = new BackToLink(buf.toString(), model.getFullName());

			LOGGER.debug("Link decoded from category " + catId + " => " + link);
			
			return link;
		} else if (deptId != null && !"".equals(deptId)) {
			DepartmentModel model = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, deptId);
			StringBuilder buf = new StringBuilder();
			buf.append( FDURLUtil.getDepartmentURI(deptId, trk) );
			
			appendWineParams(buf, request);

			final BackToLink link = new BackToLink(buf.toString(), model.getFullName());
			
			LOGGER.debug("Link decoded from department " + deptId + " => " + link);

			return link;

		}
		return null;
	}

	
	private BackToLink decodeWineFilterPage(String trk, HttpServletRequest request) throws IOException {
		String[] dvalues = request.getParameter("wineFilter").split(",");
		DomainValue dv = null;
		if (dvalues.length == 1)
			dv = (DomainValue) ContentFactory.getInstance().getContentNode( dvalues[0] );
		
		StringBuilder buf = new StringBuilder();
		buf.append("/wine/filter.jsp");

		int k = 0;
		
		if (trk == null)
			trk = "wflt";
		buf.append(k++ == 0 ? "?" : ProductDisplayUtil.URL_PARAM_SEP).append("trk=").append(trk);

		appendWineParams(buf, request);
		
		final BackToLink link = new BackToLink(buf.toString(), dv != null ? dv.getLabel() : "Results");
		LOGGER.debug("Link decoded from wine filter params " + request.getParameter("wineFilter") + " => " + link);
		return link;
	}
	

	private Appendable appendWineParams(Appendable buf, HttpServletRequest request) throws IOException {
		// append wine params
		for (String p : FDURLUtil.WINE_PARAMS) {
			String v = request.getParameter(p);
			if (request.getParameter(p) != null && !"".equals(request.getParameter(p)) ) {
				buf.append(ProductDisplayUtil.URL_PARAM_SEP).append(p).append("=").append(v);
			}
		}

		return buf;
	}



	static class BackToLink {
		public static final String BACK_TO_BUTTON = "/media_stat/images/wine/productpage_returnbutton.gif";

		String URL;
		String description;
		
		public BackToLink(String URL, String description) {
			this.URL = URL;
			this.description = description;
		}
		
		public String getURL() {
			return URL;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void appendLink(Appendable buf) throws IOException {
			buf.append("<span class=\"title14\"><a href=\"").append(this.URL).append("\">\n");
			buf.append("<img style=\"vertical-align: middle; border: 0;\" src=\"").append(BACK_TO_BUTTON).append("\"/></a>");
			buf.append("&nbsp;<a href=\"").append(this.URL).append("\">");
			buf.append("Back To ");
			buf.append(this.description);
			buf.append("</a></span>\n");
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			
			buf.append("LINK: ");
			buf.append(URL);
			buf.append(" DESC: ");
			buf.append(description);
			
			return buf.toString();
		}

		public static final BackToLink DEFAULT = new BackToLink("javascript:history.back();", "Results");
	}
}
