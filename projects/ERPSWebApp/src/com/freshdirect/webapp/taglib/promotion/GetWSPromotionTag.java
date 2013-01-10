package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetWSPromotionTag extends AbstractGetterTag {

	private WSPromoFilterCriteria filter;
	

	public WSPromoFilterCriteria getFilter() {
		return filter;
	}

	public void setFilter(WSPromoFilterCriteria filter) {
		this.filter = filter;
	}
	
	@Override
	protected Object getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		List<WSPromotionInfo> infos = new ArrayList<WSPromotionInfo>();
		if(null != filter &&(null != filter.getFromDate() && null != filter.getToDate() && filter.getToDate().before(filter.getFromDate()))){
			pageContext.setAttribute("endDateBeforeErr", " 'To' date should not be before 'From' date for filtering.");
		}else if(filter!=null){
			 infos = FDPromotionNewManager.getWSPromotionInfos(filter.getFromDate(), filter.getToDate(),filter.getStatus());
		}else
			 infos = FDPromotionNewManager.getWSPromotionInfos(null, null,null);
		
		request.getSession().setAttribute("wsFilter", filter);
		return infos;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
}
