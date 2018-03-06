package com.freshdirect.webapp.jawr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

import net.jawr.web.JawrConstant;
import net.jawr.web.servlet.*;

public class JawrServlet extends net.jawr.web.servlet.JawrServlet{

	private static final long serialVersionUID = 5399652638526520038L;
	
	private static final Logger LOGGER = LoggerFactory.getInstance( JawrServlet.class );
	

	@Override
	public void init() throws ServletException {
		try {
			String type = getServletConfig().getInitParameter(JawrConstant.TYPE_INIT_PARAMETER);
			requestHandler = new JawrRequestHandler(getServletContext(), getServletConfig());
			
		} catch (ServletException e) {
			LOGGER.error(e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(e);
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		requestHandler.doGet(req, resp);
	}
}
