package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.framework.util.NVL;

public class JspTableSorter {
	private final String baseParams;
	private final String sortBy;
	private final boolean ascending;

	public JspTableSorter(HttpServletRequest request) {
		this.baseParams = RequestUtil.getFilteredQueryString(request, new String[] { "sortBy", "sortOrder" });
		this.sortBy = request.getParameter("sortBy");
		this.ascending = !NVL.apply(request.getParameter("sortOrder"), "desc").equals("desc");
	}
	
	public String getSortBy() {
		return this.sortBy;
	}

	public boolean isAscending() {
		return this.ascending;
	}
	
	public String getSortParams() {
		StringBuffer sb = new StringBuffer();
		if (this.sortBy != null) {
			sb.append("sortBy=").append(sortBy);
			sb.append("&sortOrder=").append(ascending ? "asc" : "desc");
		}
		return sb.toString();
	}

	public String getFieldParams(String fieldName) {
		StringBuffer sb = new StringBuffer(baseParams);
		if (sb.length() > 0) {
			sb.append('&');
		}
		sb.append("sortBy=").append(fieldName);
		sb.append("&sortOrder=").append((ascending && fieldName.equals(sortBy)) ? "desc" : "asc");
		return sb.toString();
	}

}
