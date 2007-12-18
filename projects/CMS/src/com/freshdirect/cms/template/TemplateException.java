/**
 * 
 */
package com.freshdirect.cms.template;

import com.freshdirect.cms.CmsException;

/**
 * @author kocka
 *
 */
public class TemplateException extends CmsException {

	public TemplateException() {
		super();
	}

	public TemplateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
