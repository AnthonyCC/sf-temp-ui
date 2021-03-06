package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.warmup.Warmup;
import com.freshdirect.fdstore.warmup.WarmupService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.data.RedirectData;

public class WarmupMonitorFilter extends AbstractFilter {
	
	private static Category LOGGER = LoggerFactory.getInstance(WarmupMonitorFilter.class);
	
    private static final String AJAX_INDICATOR_REQUEST_HEADER_NAME = "X-Requested-With";
    private static final String AJAX_INDICATOR_REQUEST_HEADER_VALUE = "XMLHttpRequest";
    public static final String WARMUP_PAGE_PATH = "/test/warmup/warmup.jsp";
    private static final String WARMUP_STATE_MOBILE_API_ENDPOINT = "/configvalue/warmupstate";
    private static final String WARMUP_AJAX_WARNING_MESSAGE = "Please redirect based on redirectUrl or '/configvalue/warmupstate' Mobile API endpoint to get the warmup state.";

    private final String filterName = getClass().getName();

    /**
     * Monitors warmup state and redirects request if it's in progress.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        try {
	        if (!WarmupService.defaultService().isWarmupFinished()) {
	            String requestURI = ((HttpServletRequest) request).getRequestURI();
	            if (!WARMUP_PAGE_PATH.equals(requestURI)) {
	                String requestHeader = httpServletRequest.getHeader(AJAX_INDICATOR_REQUEST_HEADER_NAME);
	                if (AJAX_INDICATOR_REQUEST_HEADER_VALUE.equals(requestHeader)) {
	                    if (!requestURI.endsWith(WARMUP_STATE_MOBILE_API_ENDPOINT)) {
	                    	LOGGER.error("[WARMUPMONITOR] WarmupMonitor-REDIRECT-AJAX");
	                        RedirectData redirectData = new RedirectData();
	                        redirectData.setRedirectUrl(WARMUP_PAGE_PATH);
	                        redirectData.setMessage(WARMUP_AJAX_WARNING_MESSAGE);
	                        JsonHelper.writeResponseData((HttpServletResponse) response, redirectData);
	                    }
	                } else {
	                	LOGGER.error("[WARMUPMONITOR] WarmupMonitor-REDIRECT-NORMAL");
	                    ((HttpServletResponse) response).sendRedirect(WARMUP_PAGE_PATH);
	                    //return; //If we dont return it will continue to execute FilterChain which might cause exception
	                }
	            }
	        }
        }catch(Exception e) {
        	LOGGER.error("[WARMUPMONITOR] WarmupMonitor-REDIRECT-EXCEPTION");
        	e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

}
