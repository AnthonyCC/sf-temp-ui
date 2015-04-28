package com.freshdirect.webapp.ajax.cache;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.cache.data.CmsChangeRequestObject;
import com.freshdirect.webapp.ajax.cache.service.CmsChangePropagatorService;

public class CmsChangePropagatorServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getInstance(CmsChangePropagatorServlet.class);
	private static final long serialVersionUID = -392690243179954063L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
		doPut(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!CmsManager.getInstance().isReadOnlyContent()) {
			CmsChangeRequestObject requestData = null;
			try {
				requestData = JsonHelper.parseRequestData(request, CmsChangeRequestObject.class);
				if (requestData != null) {
					CmsChangePropagatorService.defaultService().propagateCmsChangesToCaches(requestData.getContentKeys());
				}
			} catch (IOException e) {
				LOG.error("Cannot read JSON", e);
				response.sendError(400);
			}
		}
	}

}
