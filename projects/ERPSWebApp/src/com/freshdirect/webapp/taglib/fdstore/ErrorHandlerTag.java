package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;

/*
 * usage notes:
 * field 	- show list: set var errorMsg in body
 * 			- msg only, set id = errorMsg
 * name 	- set id = errorMsg
 * 
 */

public class ErrorHandlerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long	serialVersionUID	= 5372290892971245086L;

	private static final Logger LOGGER = LoggerFactory.getInstance( ErrorHandlerTag.class );
	
	private String name = null;
	private String[] field = null;
	private ActionResult result = null;
	private String id = null;
		
		
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getField() {
		return this.field;
	}

	public void setField(String[] field) {
		this.field = field;
	}
	
	public void setResult(ActionResult result) {	
		this.result = result;
	}
	
	public ActionResult getResult() {
		return this.result;
	}




	public int doStartTag() throws JspException {
	
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		if ((this.name == null && this.field == null) || (this.name != null && this.field != null)) {
		
			throw new JspException("Specify either name or field only.");
		
		}
		
		if (!result.isSuccess()) {
			
			if (this.field != null ) {
				LOGGER.debug("fields: " + this.field);
				List<String> currentErrors = new ArrayList<String>();
				for (int i = 0; i < this.field.length; i++) {
					String errorType = this.field[i];
					if (result.getError(errorType) != null) {
						currentErrors.add(errorType);
					}
				}
				LOGGER.debug("errorList: " + currentErrors);
				
				if (currentErrors.size() > 0) {
					if (this.id!=null) {
						request.setAttribute(this.id, result.getError(currentErrors.get(0).toString()).getDescription());
					} else {
						request.setAttribute("errorList", currentErrors);
					}
					return EVAL_BODY_BUFFERED;
				}

			} else if (this.name != null && result.getError(this.name) != null) {
				LOGGER.debug("name: " + this.name);
				String errorDescription = result.getError(this.name).getDescription();
 
				if (this.id!=null) {
					request.setAttribute(this.id, errorDescription);
				}
				return EVAL_BODY_INCLUDE;
			}
		}
		if (result.hasWarning(this.name)) {
			LOGGER.debug("name: " + this.name);
			String warnDescription = result.getWarning(this.name).getDescription();

			if (this.id!=null) {
				request.setAttribute(this.id, warnDescription);
			}
			return EVAL_BODY_BUFFERED;
		}

		return SKIP_BODY;
	}
	
	
	
}
