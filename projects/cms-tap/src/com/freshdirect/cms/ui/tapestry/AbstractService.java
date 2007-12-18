package com.freshdirect.cms.ui.tapestry;

import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Base implementation of a Tapestry service, with the necessary
 * setters,getters.
 * 
 * @author zsombor
 * 
 */
public abstract class AbstractService implements IEngineService {

	protected RequestExceptionReporter exceptionReporter;

	protected LinkFactory linkFactory;

	protected ClassResolver classResolver;

	protected WebRequest request;

	protected WebResponse response;

	public final void setExceptionReporter(
			RequestExceptionReporter exceptionReporter) {
		this.exceptionReporter = exceptionReporter;
	}

	public final void setLinkFactory(LinkFactory linkFactory) {
		this.linkFactory = linkFactory;
	}

	public final void setClassResolver(ClassResolver classResolver) {
		this.classResolver = classResolver;
	}

	public final void setRequest(WebRequest request) {
		this.request = request;
	}

	public final void setResponse(WebResponse response) {
		this.response = response;
	}

	protected ILink constructLink(boolean post, Map parameters) {
		return this.linkFactory.constructLink(this, post, parameters, true);
	}

	/**
	 * @return the classResolver
	 */
	protected final ClassResolver getClassResolver() {
		return classResolver;
	}

	/**
	 * @return the exceptionReporter
	 */
	protected final RequestExceptionReporter getExceptionReporter() {
		return exceptionReporter;
	}

	/**
	 * @return the linkFactory
	 */
	protected final LinkFactory getLinkFactory() {
		return linkFactory;
	}

	/**
	 * @return the request
	 */
	protected final WebRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	protected final WebResponse getResponse() {
		return response;
	}

}