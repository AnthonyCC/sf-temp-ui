/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

//import java.io.*;
import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

import com.freshdirect.framework.webapp.*;

/*
 * usage notes:
 * field 	- show list: set var errorMsg in body
 * 			- msg only, set id = errorMsg
 * name 	- set id = errorMsg
 * 
 */

public class ErrorHandlerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private String name = null;
	private String[] field = null;
	private ActionResult result = null;
	private String id = null;
	
	/*/ temp put back top msg
	public final static String HTML_START = "<span class='text11rbold'>";
	public final static String HTML_END = "</span>";

	private String topMessageType = null;
	private String topMessage = null;
	// temp*/
	
	
		
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
	
/*/	temp top msg
	
	public void setTopMessageType(String topMessageType) {	
			this.topMessageType = topMessageType;
	}

	public String getTopMessageType() {
		return this.topMessageType;
	}

	public void setTopMessage(String topMessage) {	
		this.topMessage = topMessage;
	}

	public String getTopMessage() {
		return this.topMessage;
	}

// temp top msg*/
		 
	public int doStartTag() throws JspException {
	
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		if ((this.name == null && this.field == null) || (this.name != null && this.field != null)) {
		
			throw new JspException("Specify either name or field only.");
		
		}
		
		if (!result.isSuccess()) {
			
			if (this.field != null ) {
				System.out.println("fields: " + this.field);
				List currentErrors = new ArrayList();
				for (int i = 0; i < this.field.length; i++) {
					String errorType = (String) this.field[i];
					if (result.getError(errorType) != null) {
						currentErrors.add(errorType);
					}
				}
				System.out.println("errorList: " + currentErrors);
				
				if (currentErrors.size() > 0) {
					if (this.id!=null) {
						request.setAttribute(this.id, result.getError(currentErrors.get(0).toString()).getDescription());
						return EVAL_BODY_BUFFERED;
					} else {
						request.setAttribute("errorList", currentErrors);
						return EVAL_BODY_BUFFERED;
					}
				}

			} else if (this.name != null && result.getError(this.name) != null) {
				System.out.println("name: " + this.name);
				String errorDescription = result.getError(this.name).getDescription();
 
				if (this.id!=null) {
					request.setAttribute(this.id, errorDescription);
					return EVAL_BODY_BUFFERED;
				/*/ temp top msg type
				} else if (this.topMessage!=null) {
					pageContext.setAttribute(this.topMessage, topMessageType);
					return EVAL_BODY_BUFFERED;
				// temp top msg type*/
				} else {
					return EVAL_BODY_BUFFERED;
				}
			}
		}
		if (result.hasWarning(this.name)) {
			System.out.println("name: " + this.name);
			String warnDescription = result.getWarning(this.name).getDescription();

			if (this.id!=null) {
				request.setAttribute(this.id, warnDescription);
				return EVAL_BODY_BUFFERED;
			/*/ temp top msg type
			} else if (this.topMessage!=null) {
				pageContext.setAttribute(this.topMessage, topMessageType);
				return EVAL_BODY_BUFFERED;
			// temp top msg type*/
			} else {
				return EVAL_BODY_BUFFERED;
			}
		}

		return SKIP_BODY;
	}
	
}
