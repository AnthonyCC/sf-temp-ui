package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.ReverseComparator;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.JspTableSorter;

public abstract class AbstractPromotionGetterTag extends AbstractGetterTag<List<PromoNewRow>> {
	private static final long serialVersionUID = -4705389681670882755L;

	protected List<PromoNewRow> toRows(Collection<FDPromotionNewModel> promotions) {
		List<PromoNewRow> promoRows = new ArrayList<PromoNewRow>();
		if(promotions == null){
			return promoRows;
		}

		for (FDPromotionNewModel promo : promotions) {
		    //
		    //Do not display gift card promotions on this page
		    //
		    if( EnumPromotionType.GIFT_CARD.getName().equals(promo.getPromotionType())){
		        continue;
		    }
			
		    String promoCode = promo.getPromotionCode();
		    if(promoCode == null) promoCode = "n/a";
			
			PromoNewRow p = new PromoNewRow();
			p.id = promo.getPK().getId();
		    p.code = promoCode;
		    p.name = promo.getName();
		    p.description = promo.getDescription();
		    String amt = promo.getMaxAmount();
		    p.amount = (amt != null && amt.length() >0) ? Double.parseDouble(amt) : 0.0;
		    p.start = promo.getStartDate();
		    p.expire = promo.getExpirationDate();
		    p.type = EnumPromotionType.getEnum(promo.getPromotionType());
		    String rCode = promo.getRedemptionCode();
		    p.redemptionCode = rCode != null ? rCode : "";
		    p.status = promo.getStatus();
		    p.createdBy = null !=promo.getCreatedBy()?promo.getCreatedBy():"";
		    p.modifiedBy = null !=promo.getModifiedBy()?promo.getModifiedBy():"";
		    p.createdModifiedBy = p.createdBy+"-"+p.modifiedBy;
		    
		    p.setChef(promo.isForChef());
		    p.setCos(promo.isForCOS());
		    p.setCosNew(promo.isForCOSNew());
		    p.setDp(promo.isForDPActiveOrRTU());
		    p.setMktg(promo.isForMarketing());
		    p.setNewCust(promo.isForNew());
		              
		 
		  promoRows.add(p);
		}
		
		return promoRows;
	}




	protected void sortRows(List<PromoNewRow> promoRows, HttpServletRequest request) {
		JspTableSorter sort = new JspTableSorter(request);
		Comparator<PromoNewRow> comp = PROMO_COMPARATORS.get(sort.getSortBy());
		if (comp == null) {
			Collections.sort(promoRows, new ReverseComparator<PromoNewRow>(COMP_START));
		} else {
			if (comp.equals(COMP_EXPIRE) || comp.equals(COMP_START)) {
				Collections.sort(promoRows, sort.isAscending() ? new ReverseComparator<PromoNewRow>(comp) : comp);
			} else {
				Collections.sort(promoRows, sort.isAscending() ? comp : new ReverseComparator<PromoNewRow>(comp));
			}
		}
		//Collections.sort(promoRows, COMP_TYPE);
	}
	


	protected static Comparator<PromoNewRow> COMP_CREATED_MODIFIED = new Comparator<PromoNewRow>() { 
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	    	return p1.createdModifiedBy.compareTo(p2.createdModifiedBy);
	    } 
	};
	protected static Comparator<PromoNewRow> COMP_TYPE = new Comparator<PromoNewRow>() { 
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	    	return p1.type.getName().compareTo(p2.type.getName());
	    } 
	};
	
	protected static Comparator<PromoNewRow> COMP_NAME = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.name.toUpperCase().compareTo(p2.name.toUpperCase());
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_DESCRIPTION = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.description.toUpperCase().compareTo(p2.description.toUpperCase());
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_AMOUNT = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return new Double(p1.amount - p2.amount).intValue();
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_START = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        if(p1.start == null) return 1;
	        if(p2.start == null) return -1;
	        
	        return p1.start.compareTo(p2.start);
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_EXPIRE = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        if(p1.expire == null) return 1;
	        if(p2.expire == null) return -1;
	        
	        return p1.expire.compareTo(p2.expire);
	    }
	};	
	
	
	protected static Comparator<PromoNewRow> COMP_CODE = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.code.toUpperCase().compareTo(p2.code.toUpperCase());
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_REDEMP_CODE = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.redemptionCode.toUpperCase().compareTo(p2.redemptionCode.toUpperCase());
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_STATUS = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.status.getName().toUpperCase().compareTo(p2.status.getName());
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_CREATED = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.createdBy.toUpperCase().compareTo(p2.createdBy);
	    }
	};
	
	protected static Comparator<PromoNewRow> COMP_MODIFIED = new Comparator<PromoNewRow>() {
	    public int compare(PromoNewRow p1, PromoNewRow p2) {
	        return p1.modifiedBy.toUpperCase().compareTo(p2.modifiedBy);
	    }
	};
	
	
	public final static Map<String,Comparator<PromoNewRow>> PROMO_COMPARATORS = new HashMap<String,Comparator<PromoNewRow>>();
	static {
		PROMO_COMPARATORS.put("name", COMP_NAME);
	    PROMO_COMPARATORS.put("description", COMP_DESCRIPTION);
//		PROMO_COMPARATORS.put("amount", COMP_AMOUNT);
		PROMO_COMPARATORS.put("start", COMP_START);
	    PROMO_COMPARATORS.put("expire", COMP_EXPIRE);
//		PROMO_COMPARATORS.put("zone", COMP_ZONE);
		PROMO_COMPARATORS.put("code", COMP_CODE);
		PROMO_COMPARATORS.put("redemptionCode", COMP_REDEMP_CODE);
		PROMO_COMPARATORS.put("status", COMP_STATUS);
		PROMO_COMPARATORS.put("type", COMP_TYPE);
		PROMO_COMPARATORS.put("createdModifiedBy", COMP_CREATED_MODIFIED);
		PROMO_COMPARATORS.put("createdBy", COMP_CREATED);
		PROMO_COMPARATORS.put("modifiedBy", COMP_MODIFIED);
		
	}	




	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List<com.freshdirect.webapp.taglib.promotion.PromoNewRow>";
		}
	}
}
