package com.freshdirect.webapp.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.util.RequestUtil;

public abstract class AbstractGetterTag<X> extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long	serialVersionUID	= 1242883824723700468L;
	
	private static Logger LOGGER = LoggerFactory.getInstance( AbstractGetterTag.class );
	
	protected HttpServletRequest request;

	// 'id' is already defined in TagSupport class therefore no need to overlap it
	// private String id = null;
	
	public AbstractGetterTag() {
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		request = (HttpServletRequest) pageContext.getRequest();
	}

	public int doStartTag() throws JspException {
		Object result;
		try {

			result = this.getResult();

		} catch (Exception ex) {
			if(ex instanceof FDNotFoundException || ex instanceof FDSkuNotFoundException) {
				LOGGER.info("FDSKUISSUE01:" + RequestUtil.getClientIp(request) + ":" + request.getHeader("User-Agent") + ":" + CookieMonster.getCookie(request) 
													+ " : " + request.getRequestURL() + ":" + ex.getMessage());
				
			} else {
				LOGGER.warn("Exception occured in getResult", ex);
			}
			throw new JspException(ex);
		}

		if (result==null) {
			return SKIP_BODY;	
		}

		pageContext.setAttribute(this.id, result);


		return EVAL_BODY_BUFFERED;
	}
	
	@Override
	public void release() {
		request = null;
		super.release();
	}

	protected abstract X getResult() throws Exception;

	protected static abstract class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					this.getResultType(),
					true,
					getScope())
			};

		}
		
		protected int getScope() {
			return VariableInfo.NESTED;
		}

		protected abstract String getResultType();
			
	}
	
}
