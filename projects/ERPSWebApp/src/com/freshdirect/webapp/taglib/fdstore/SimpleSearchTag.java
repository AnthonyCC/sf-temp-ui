package com.freshdirect.webapp.taglib.fdstore;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.http.HttpHeaders;

import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.webapp.search.SearchService;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.RequestUtil;

public class SimpleSearchTag extends AbstractGetterTag<SearchResults> {
	private static final long serialVersionUID = -2366286812494532490L;

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return SearchResults.class.getName();
		}
		
		@Override
		protected int getScope() {
			return VariableInfo.AT_END;
		}
	}
	
	protected FilteringNavigator nav;

	@Override
	protected SearchResults getResult() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        SearchResults results = SearchService.getInstance().searchProducts(nav.getSearchTerm(), request.getCookies(), user, RequestUtil.getFullRequestUrl(request),
                request.getHeader(HttpHeaders.REFERER));
		// Google Analytics Custom Variables
		if (!nav.isRefined()) {
			Map<String, String> vars = new LinkedHashMap<String, String>();
			vars.put("searchProductCount", Integer.toString(results.getProducts().size()));
			vars.put("searchTotalCount", Integer.toString(results.getProducts().size() + results.getRecipes().size()));
			if (results.getSuggestedTerm() != null)
				vars.put("suggestedTerm", results.getSuggestedTerm());
			pageContext.getRequest().setAttribute(SessionName.GA_CUSTOM_VARIABLES, vars);
		}
		return results;
	}

	public FilteringNavigator getNav() {
		return nav;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

}
