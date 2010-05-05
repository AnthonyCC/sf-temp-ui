/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class AbstractGetterTag<X> extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Logger LOGGER = LoggerFactory.getInstance( AbstractGetterTag.class );

	// 'id' is already defined in TagSupport class therefore no need to overlap it
	// private String id = null;

	public void setId(String id) {
		this.id = id;
	}

	public int doStartTag() throws JspException {
		Object result;
		try {

			result = this.getResult();

		} catch (Exception ex) {
			LOGGER.warn("Exception occured in getResult", ex);
			throw new JspException(ex);
		}

		if (result==null) {
			return SKIP_BODY;	
		}

		pageContext.setAttribute(this.id, result);


		return EVAL_BODY_BUFFERED;
	}

	protected abstract X getResult() throws Exception;

	protected static abstract class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					this.getResultType(),
					true,
					VariableInfo.NESTED )
			};

		}
		
		protected abstract String getResultType();
			
	}
	
}
