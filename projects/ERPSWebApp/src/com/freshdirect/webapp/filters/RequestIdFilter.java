package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.RequestIdCache;

public class RequestIdFilter extends AbstractFilter {

    private final String filterName = this.getClass().getName();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        RequestIdCache.init();

        filterChain.doFilter(request, response);

        RequestIdCache.clear();
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

}
