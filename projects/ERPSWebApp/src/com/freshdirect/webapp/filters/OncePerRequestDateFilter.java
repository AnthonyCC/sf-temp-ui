package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.storeapi.content.ContentFactory;

public class OncePerRequestDateFilter extends AbstractFilter {

	private final String filterName = this.getClass().getName();

	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		EnumEStoreId storeId = ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId();

		if (!this.isFilterApplied(request)) {
			OncePerRequestDateCache.init(storeId);
		}

		filterChain.doFilter(request, response);

		if (!this.isFilterApplied(request)) {
			OncePerRequestDateCache.clear();
		}
	}


	@Override
    public String getFilterName(){
		return this.filterName;
	}

}
