package com.freshdirect.webapp.quickshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.QuickShopSortType;
import com.freshdirect.fdstore.content.SortTypeI;
import com.freshdirect.fdstore.util.FilteringNavigator;

public class QuickShopListRequestObject {
	
	private String userId;
	private String yourListId;
	private String starterListId;
	private String searchTerm;
	private List<Object> orderIdList;
	private List<Object> deptIdList;
	private List<Object> filterIdList;
	private String timeFrame;
	private String sortId;
	private boolean orderAsc=true;
	private int pageSize = QuickShopServlet.DEFAULT_PAGE_SIZE;
	private int activePage = 0;
	private int pageCount;
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public List<Object> getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(List<Object> orderIdList) {
		this.orderIdList = orderIdList;
	}
	public List<Object> getDeptIdList() {
		return deptIdList;
	}
	public void setDeptIdList(List<Object> deptIdList) {
		this.deptIdList = deptIdList;
	}
	public List<Object> getFilterIdList() {
		return filterIdList;
	}
	public void setFilterIdList(List<Object> filterIdList) {
		this.filterIdList = filterIdList;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public boolean isOrderAsc() {
		return orderAsc;
	}
	public void setOrderAsc(boolean isOrderAsc) {
		this.orderAsc = isOrderAsc;
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
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}	
	public String getTimeFrame() {
		return timeFrame;
	}
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	public String getStarterListId() {
		return starterListId;
	}	
	public void setStarterListId(String starterListId) {
		this.starterListId = starterListId;
	}	
	public String getYourListId() {
		return yourListId;
	}
	public void setYourListId(String yourListId) {
		this.yourListId = yourListId;
	}
	
	public boolean compareSort(String otherSortId){
		
		if(otherSortId==null){
			if(sortId==null){
				return true;
			}else{
				return false;
			}
		}else if(otherSortId.equals(sortId)){
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		if(deptIdList==null){
			result = prime * result + 0;
		}else{
			for(Object o: deptIdList){
				result = prime * result + o.hashCode();
			}			
		}
		
		if(filterIdList==null){
			result = prime * result + 0;
		}else{
			for(Object o: filterIdList){
				result = prime * result + o.hashCode();
			}			
		}
		
		if(orderIdList==null){
			result = prime * result + 0;
		}else{
			for(Object o: orderIdList){
				result = prime * result + o.hashCode();
			}			
		}
				
		result = prime * result + ((searchTerm == null) ? 0 : searchTerm.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((starterListId == null) ? 0 : starterListId.hashCode());
		result = prime * result + ((timeFrame == null) ? 0 : timeFrame.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		QuickShopListRequestObject other = (QuickShopListRequestObject) obj;
		
		if (deptIdList == null) {
			if (other.getDeptIdList() != null)
				return false;
		} else {
			if(other.getDeptIdList()==null){
				return false;
			}else{
				for(Object o1:deptIdList){
					if(!other.getDeptIdList().contains(o1)){
						return false;
					}
				}
			}
		}
		
		if (filterIdList == null) {
			if (other.getFilterIdList() != null)
				return false;
		} else {
			if(other.getFilterIdList()==null){
				return false;
			}else{
				for(Object o1:filterIdList){
					if(!other.getFilterIdList().contains(o1)){
						return false;
					}
				}
			}
		}
		
		if (orderIdList == null) {
			if (other.getOrderIdList() != null)
				return false;
		} else {
			if(other.getOrderIdList()==null){
				return false;
			}else{
				for(Object o1:orderIdList){
					if(!other.getOrderIdList().contains(o1)){
						return false;
					}
				}
			}
		}
		
		if (searchTerm == null) {
			if (other.searchTerm != null)
				return false;
		} else if (!searchTerm.equals(other.searchTerm))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if ((starterListId == null && other.starterListId != null) || (!starterListId.equals(other.starterListId))) {
			return false;
		}
		if (timeFrame == null) {
			if (other.timeFrame != null)
				return false;
		} else if (!timeFrame.equals(other.timeFrame))
			return false;
		
		return true;
	}
	
	
	public final FilteringNavigator createFilteringNavigatorFromThis() {
		
		QuickShopListRequestObject reqSrc = this;
		Map<FilteringValue, List<Object>> filterValues = new HashMap<FilteringValue, List<Object>>();
		SortTypeI sortBy = null;
		boolean isOrderAscending = true;
		String searchTerm = "";
		
		if(reqSrc!=null){
			
			//department filter
			if(reqSrc.getDeptIdList()!=null && !reqSrc.getDeptIdList().isEmpty()){
				filterValues.put(EnumQuickShopFilteringValue.DEPT, reqSrc.getDeptIdList());				
			}
			
			//order filter
			if(reqSrc.getOrderIdList()!=null && !reqSrc.getOrderIdList().isEmpty()){
				filterValues.put(EnumQuickShopFilteringValue.ORDERS_BY_DATE, reqSrc.getOrderIdList());				
			}
			
			//preferences (kosher, sale, etc.)
			if(reqSrc.getFilterIdList()!=null && !reqSrc.getFilterIdList().isEmpty()){
				for(Object pref: reqSrc.getFilterIdList()){
					List<Object> values = new ArrayList<Object>();
					values.add(pref);
					filterValues.put(EnumQuickShopFilteringValue.getByName((String)pref), values);
				}							
			}
			
			//starter list id
			if(reqSrc.getStarterListId()!=null){
				List<Object> values = new ArrayList<Object>();
				values.add(reqSrc.getStarterListId());
				filterValues.put(EnumQuickShopFilteringValue.STARTER_LISTS, values);
			}
			
			//timeframe filter
			if(reqSrc.getTimeFrame()!=null){
				List<Object> values = new ArrayList<Object>();
				values.add(EnumQuickShopFilteringValue.getByName(reqSrc.getTimeFrame()).getName());
				filterValues.put(EnumQuickShopFilteringValue.getByName(reqSrc.getTimeFrame()), values);
			}
			
			//sortid
			if(reqSrc.getSortId()!=null){
				sortBy=QuickShopSortType.findByLabel(reqSrc.getSortId());
			}
			
			//your listid filter
			if(reqSrc.getYourListId()!=null){
				List<Object> values = new ArrayList<Object>();
				values.add(reqSrc.getYourListId());
				filterValues.put(EnumQuickShopFilteringValue.YOUR_LISTS, values);
			}
			
			isOrderAscending=reqSrc.isOrderAsc();
			searchTerm = reqSrc.getSearchTerm();
		}
		
		return new FilteringNavigator( filterValues, sortBy, isOrderAscending, searchTerm );
	}
	

}
