package com.freshdirect.mobileapi.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.warmup.Warmup;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BootstrapServlet extends HttpServlet {
    final static Category LOGGER = LoggerFactory.getInstance(BootstrapServlet.class);

    /**
     * 
     */
    private static final long serialVersionUID = 8076528955184614309L;

    @Override
    public void init(ServletConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        super.init(arg0);

        Thread thread = new Thread() {
            @Override
            public void run() {
                LOGGER.info("Running Warmup");
                Warmup warmup = new Warmup();
                warmup.warmup();
            }
        };
        thread.start();

        LOGGER.debug("****** Bootstrap Servlet loaded ********");
    }

}
