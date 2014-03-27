package com.freshdirect.webapp.ajax.filtering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.browse.data.PagerData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsFilteringNavigator {
	
	//navigation id (dept or cat id)
	private String id;
	
	//catId for special pages like presiden't picks
	private String catId;
	
	//paging
	private int pageSize = PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD * PagerData.GRID_ITEM_ROW_PER_PAGE_THRESHOLD;
	private int activePage = 1;

	//sorting
	private String sortBy;
	private boolean isOrderAscending;
	
	//filtering
	private Map<String, List<String>> requestFilterParams = new HashMap<String, List<String>>();
	
	//show all product on the actual page
	private boolean all = false;
	
	//are we on a product page?
	private boolean pdp = false;
	
	//are we on a special layout page?
	private boolean specialPage = false;
	
	//use cmevent param in order to determine the action (navigation/filtering/paging/sorting)
	private String browseEvent;
	
	/**
	 * Creates a CmsFilteringNavigator instance out of request parameter map.
	 * 
	 * @param paramMap - @see javax.servlet.http.HttpServletRequest#getParameterMap()
	 * @return CmsFilteringNavigator
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws InvalidFilteringArgumentException 
	 * @throws FDResourceException 
	 * @throws HttpErrorResponse 
	 */
	public static CmsFilteringNavigator createInstance(HttpServletRequest request) throws InvalidFilteringArgumentException, FDResourceException {

		@SuppressWarnings("unchecked")
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<String> paramNames = new TreeSet<String>(paramMap.keySet()); 
		CmsFilteringNavigator cmsFilteringNavigator = null;
		if (paramMap.get("data") != null && !"".equals(paramMap.get("data"))) {

			try {
				cmsFilteringNavigator = JsonHelper.parseRequestData( request, CmsFilteringNavigator.class );
			} catch (JsonParseException e) {
				throw new InvalidFilteringArgumentException(e, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
			} catch (JsonMappingException e) {
				throw new InvalidFilteringArgumentException(e, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
			} catch (IOException e) {
				throw new FDResourceException(e);
			}
			
		} else {

			cmsFilteringNavigator = new CmsFilteringNavigator();
			for (String param : paramNames){
				
				String[] paramValues = paramMap.get(param);
				for (String paramValue : paramValues) {
				
					if("pageSize".equalsIgnoreCase(param)) {
		
						cmsFilteringNavigator.setPageSize(Integer.parseInt(paramValue));
					
					} else if("all".equalsIgnoreCase(param)) {
		
						cmsFilteringNavigator.setAll(Boolean.parseBoolean(paramValue.toLowerCase()));
					
					} else if("activePage".equalsIgnoreCase(param)) {
						
						cmsFilteringNavigator.setActivePage(Integer.parseInt(paramValue));
						
					} else if("sortBy".equalsIgnoreCase(param)) {
						
						cmsFilteringNavigator.setSortBy(paramValue);
						
					} else if("orderAsc".equalsIgnoreCase(param)) {
						
						cmsFilteringNavigator.setOrderAscending(Boolean.parseBoolean(paramValue.toLowerCase()));
						
					} else if("id".equalsIgnoreCase(param)) { //case sensitive
		
						cmsFilteringNavigator.setId(paramValue);
					
					} else if("catId".equalsIgnoreCase(param)) { //case sensitive
		
						cmsFilteringNavigator.setId(paramValue);
					
					} else { //No match for any other CmsFilteringNavigator property => must be a filtering domain
						
						String filteringDomainId = param;
						String[] filteringIds = paramValue.split("\\|\\|");
						
						List<String> filteringIdList = new ArrayList<String>();
						Collections.addAll(filteringIdList, filteringIds);
						
						cmsFilteringNavigator.getRequestFilterParams().put(filteringDomainId, filteringIdList);
						
					}
				}
			}
		}

		String id = cmsFilteringNavigator.getId();
		if (id == null || id.equals("")){
			throw new InvalidFilteringArgumentException("ID parameter is null", InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
		}
		
		return cmsFilteringNavigator;
	}
	
	/**
	 * @return non URL encoded query string
	 */
	public String assembleQueryString() {
		
		StringBuffer queryString = new StringBuffer();
		
		queryString.append("id=").append(id);
		queryString.append("&");
		queryString.append("pageSize=").append(pageSize);
		queryString.append("&");
		queryString.append("all=").append(all);
		queryString.append("&");
		queryString.append("activePage=").append(activePage);
		queryString.append("&");
		queryString.append("sortBy=").append(sortBy);
		queryString.append("&");
		queryString.append("orderAsc=").append(isOrderAscending);
		queryString.append("&");
		for (String filteringDomainId : requestFilterParams.keySet()) {
			
			StringBuffer filteringIds = new StringBuffer();
			for (String filteringId : requestFilterParams.get(filteringDomainId)) {
				filteringIds.append(filteringId).append("-");
			}
			filteringIds.deleteCharAt(filteringIds.length() - 1); //remove last unnecessary '-'
			
			queryString.append(filteringDomainId).append("=").append(filteringIds.toString());
			queryString.append("&");
				
		}
		queryString.deleteCharAt(queryString.length() - 1); //remove last unnecessary '&'
		
		return queryString.toString();
	}
		
	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isOrderAscending() {
		return isOrderAscending;
	}

	public void setOrderAscending(boolean isOrderAscending) {
		this.isOrderAscending = isOrderAscending;
	}

	public Map<String, List<String>> getRequestFilterParams() {
		return requestFilterParams;
	}

	public void setRequestFilterParams(Map<String, List<String>> requestFilterParams) {
		this.requestFilterParams = requestFilterParams;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getActivePage() {
		return activePage;
	}

	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}

	public boolean isPdp() {
		return pdp;
	}

	public void setPdp(boolean pdp) {
		this.pdp = pdp;
	}

	public boolean isSpecialPage() {
		return specialPage;
	}

	public void setSpecialPage(boolean specialPage) {
		this.specialPage = specialPage;
	}

	public String getBrowseEvent() {
		return browseEvent;
	}

	public void setBrowseEvent(String browseEvent) {
		this.browseEvent = browseEvent;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}	
	
}
