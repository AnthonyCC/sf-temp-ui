package com.freshdirect.webapp.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.DispatcherServlet;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.warmup.WarmupService;


public class CMSDispatcherServlet extends DispatcherServlet {
    
	private final static Logger LOGGER = LoggerFactory.getInstance(CMSDispatcherServlet.class);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LOGGER.info("CMSDispatcherServlet after init.........");
        WarmupService.defaultService().repeatWarmup();
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("CMSDispatcherServlet after destroy.........");
    }
}