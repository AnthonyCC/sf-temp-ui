package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;

public class GetAllPromotionsNewTag extends AbstractPromotionGetterTag {
	private static final long serialVersionUID = 1L;

	private PromoFilterCriteria filter;
	
	
	public PromoFilterCriteria getFilter() {
		return filter;
	}

	public void setFilter(PromoFilterCriteria filter) {
		this.filter = filter;
	}

	@Override
	protected List<PromoNewRow> getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
//		request.setAttribute("filter",filter);
		List<FDPromotionNewModel> promotions = null;
		if(null != request.getParameter("promo_refresh_submit")){
			FDPromotionNewModelFactory.getInstance().forceRefresh();
		}
		if(null == filter || filter.isEmpty()){
			if(null == request.getParameter("promo_filter_submit"))
				filter = (PromoFilterCriteria)request.getSession().getAttribute("filter");
		}
		request.getSession().setAttribute("filter",filter);
		if(null != filter && !filter.isEmpty()){

			promotions = (List<FDPromotionNewModel>) filterPromotions(filter);
		}else{
			promotions = new ArrayList<FDPromotionNewModel>(FDPromotionNewModelFactory.getInstance().getPromotions());
		}
		
		// Convert promos to promo rows
		List<PromoNewRow> promoRows = toRows(promotions);

		// And sort them
		sortRows(promoRows, request);
		if(null !=request.getParameter("actionName")){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=PromotionsList_Export.xls");
		    response.setCharacterEncoding("utf-8");
		}
		
//		request.setAttribute("createdUsers", FDPromotionNewModelFactory.getInstance().getPromotionsCreatedUsers());
//		request.setAttribute("modifiedUsers", FDPromotionNewModelFactory.getInstance().getPromotionsModifiedUsers());
		return promoRows;
	}
	
	

	
	private List<FDPromotionNewModel> filterPromotions(PromoFilterCriteria filter){
	
		List<FDPromotionNewModel> promotions = new ArrayList<FDPromotionNewModel>(FDPromotionNewModelFactory.getInstance().getPromotions());
		List<FDPromotionNewModel> filteredPromotions = new ArrayList<FDPromotionNewModel>();
		for (Object object: promotions) {
			boolean isMatched = true;
			FDPromotionNewModel promotion = (FDPromotionNewModel)object;
			if(null != filter.getOfferType() && null != EnumPromotionType.getEnum(filter.getOfferType())){
				isMatched = checkOfferType(filter,promotion);
			}
			if(isMatched){
				if(null != filter.getCustomerType() && !"".equalsIgnoreCase(filter.getCustomerType())){
					isMatched = checkCustomerType(filter, promotion);
					
				}
			}
			if(isMatched){
				if(null != filter.getPromoStatus() && !"".equalsIgnoreCase(filter.getPromoStatus())){
					isMatched = checkPromoStatus(filter, promotion);
				}
			}
			if(isMatched){
				if(null != filter.getCreatedBy() && !"".equalsIgnoreCase(filter.getCreatedBy())){
					isMatched = filter.getCreatedBy().equalsIgnoreCase(promotion.getCreatedBy());
				}
			}
			if(isMatched){
				if(null != filter.getModifiedBy() && !"".equalsIgnoreCase(filter.getModifiedBy())){
					isMatched = filter.getModifiedBy().equalsIgnoreCase(promotion.getModifiedBy());
				}
			}
			if(isMatched && null !=filter.getKeyword() && !"".equalsIgnoreCase(filter.getKeyword().trim())){
				isMatched = checkKeyword(filter, promotion);
			}
			
			if(isMatched){
				filteredPromotions.add(promotion);
			}
		}
		return filteredPromotions;
	}


	private boolean checkOfferType(PromoFilterCriteria filter,
			 FDPromotionNewModel promotion) {
		boolean isMatched = false;
		if(promotion.getPromotionType().equalsIgnoreCase(EnumPromotionType.getEnum(filter.getOfferType()).getName())){
			isMatched = true;
		}
		return isMatched;
	}


	private boolean checkPromoStatus(PromoFilterCriteria filter,
			FDPromotionNewModel promotion) {
		boolean isMatched = false;
		if(null != promotion.getStatus()){
			if("EXPIRED_CANCELLED".equalsIgnoreCase(filter.getPromoStatus())){
				if((promotion.getStatus().equals(EnumPromotionStatus.getEnum("EXPIRED")) || promotion.getStatus().equals(EnumPromotionStatus.getEnum("CANCELLED")))){
					isMatched = true;
				}
			}else if("DRAFT_TEST_PROGRESS".equalsIgnoreCase(filter.getPromoStatus())){
				if((promotion.getStatus().equals(EnumPromotionStatus.getEnum("DRAFT")) || promotion.getStatus().equals(EnumPromotionStatus.getEnum("TEST")) || promotion.getStatus().equals(EnumPromotionStatus.getEnum("PROGRESS")))){
					isMatched = true;
				}
			}else if(promotion.getStatus().equals(EnumPromotionStatus.getEnum(filter.getPromoStatus()))){
					isMatched = true;
			}
		}
		return isMatched;
	}


	private boolean checkKeyword(PromoFilterCriteria filter,
			FDPromotionNewModel promotion) {
		String keyword = filter.getKeyword().trim().toUpperCase();
		boolean isMatched = false;
		String desc = promotion.getDescription();
		if(desc != null) {
			desc = desc.toUpperCase();
		} else {
			desc = "";
		}
		
		String name = promotion.getName();
		if(name != null) {
			name = name.toUpperCase();
		} else {
			name = "";
		}

		String code = promotion.getPromotionCode();
		if(code != null) {
			code = code.toUpperCase();
		} else {
			code = "";
		}
		
		String redempCode = promotion.getRedemptionCode();;
		if(redempCode != null) {
			redempCode = redempCode.toUpperCase();
		} else {
			redempCode = "";
		}

		if( (desc.indexOf(keyword) >= 0) ||
			(code.indexOf(keyword) >= 0) ||
			(redempCode.indexOf(keyword) >= 0) ||
			(name.indexOf(keyword) >= 0)) {
			isMatched = true;
		}
		return isMatched;
	}


	private boolean checkCustomerType(PromoFilterCriteria filter,
			FDPromotionNewModel promotion) {
		String custType = filter.getCustomerType();
		boolean isMatched = false;
		/*List<FDPromotionAttributeParam> attrList = promotion.getAttributeList();
		for (FDPromotionAttributeParam promotionAttributeParam : attrList) {
//			if("NEW".equalsIgnoreCase(custType)){
				if(custType.equalsIgnoreCase(promotionAttributeParam.getAttributeName())){
					isMatched = true;
					break;
				}
//		}
		}*/
		
		if("ChefsTable".equalsIgnoreCase(custType)){
			isMatched = promotion.isForChef();
		}else if("New".equalsIgnoreCase(custType)){
			isMatched = promotion.isForNew();
		}else if("COSPilot".equalsIgnoreCase(custType)){
			isMatched = promotion.isForCOS();
		}else if("COSNew".equalsIgnoreCase(custType)){
			isMatched = promotion.isForCOSNew();
		}else if("DeliveryPass".equalsIgnoreCase(custType)){
			isMatched = promotion.isForDPActiveOrRTU();
		}else if("MarketingPromo".equalsIgnoreCase(custType)){
			isMatched = promotion.isForMarketing();
		} 
			
		return isMatched;
	}
}