package com.freshdirect.cms.listeners;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class NotificationReceiverServlet extends HttpServlet {
	private static final long serialVersionUID = 594518904238049087L;
	
	private static Logger LOGGER = LoggerFactory.getInstance(NotificationReceiverServlet.class);
	
	private MediaEventHandlerI handler;

    public void init() throws ServletException {
        super.init();
        try {
        	handler = (MediaEventHandlerI) FDRegistry.getInstance().getService(MediaEventHandlerI.class);
        } catch (RuntimeException e) {
        	LOGGER.error("cannot initialize servlet, running in DUMMY mode");
        	handler = DummyMediaEventHandler.getInstance();
        }
    }
    
    
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	LOGGER.debug("-->service()");
        String cmd = req.getParameter("cmd");
        String user = req.getParameter("user");
        String src = req.getParameter("src");
        
        try {
	        if ("create".equals(cmd)) {
	            handler.create(create(req), user);
	        } else if ("move".equals(cmd)) {
	            String dst = req.getParameter("dst");
	            handler.move(src, dst, user);
	        } else if ("update".equals(cmd)) {
	            handler.update(create(req), user);
	        } else if ("delete".equals(cmd)) {
	            handler.delete(src, user);
	        } else
	        	throw new IllegalArgumentException("unknown command: " + cmd);
        } catch (Exception e) {
        	LOGGER.error("cannot perform operation '" + cmd + "' on '" + src + "'", e);
       		resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
       				e.getMessage() != null ? e.getMessage() : "Unknown error");
        	LOGGER.debug("<--service()");
        	return;
		}
        resp.sendError(HttpServletResponse.SC_OK);
        LOGGER.info("command '" + cmd + "' on '" + src + "' has been completed");
    	LOGGER.debug("<--service()");
    }
    
    Media create(HttpServletRequest req) {
        String uri = req.getParameter("src");
        String mimeType = req.getParameter("mime");
        ContentType type = Media.determineType(uri, mimeType);
        Integer width = null;
        Integer height = null;
        if (FDContentTypes.IMAGE.equals(type)) {
            String dimension = req.getParameter("dim");
            String[] values = StringUtils.split(dimension, 'x');
            width = Integer.valueOf(values[0]);
            height = Integer.valueOf(values[1]);
        }
        return new Media(null, uri, type, width, height, mimeType, new Date());
    }
    
}
