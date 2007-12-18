package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.framework.util.ReverseComparator;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspTableSorter;

public class GetAllPromotionsTag extends AbstractGetterTag {

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		//Clean up any uncommitted promotion from the session.
		session.removeAttribute(SessionName.EDIT_PROMOTION);
		
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String search = request.getParameter("search"); 
		boolean runSearch = false;
		List promotions = null;
		if(search != null && !search.equals("")) {
		   	promotions = new ArrayList(FDPromotionModelFactory.getInstance().searchByString(search));
		   	runSearch = true;
		} else {
			promotions = new ArrayList(FDPromotionModelFactory.getInstance().getPromotions());
		}
		List promoRows = new ArrayList();
		if(promotions == null){
			return promoRows;
		}
		for(Iterator n = promotions.iterator(); n.hasNext();) {
		    FDPromotionModel promo = (FDPromotionModel)n.next();
		    //
		    //Do not display gift card promotions on this page
		    //
		    if( EnumPromotionType.GIFT_CARD.getName().equals(promo.getPromotionType()) &&
		    	!runSearch){
		        continue;
		    }
			
		    String promoCode = promo.getPromotionCode();
		    if(promoCode == null) promoCode = "n/a";
			
			PromoRow p = new PromoRow();
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
		              
		  if(!promo.getZipRestrictions().isEmpty()){
		    Map map = promo.getZipRestrictions();
		    TreeSet keys = new TreeSet(map.keySet());
		    Date curDate = (Date)keys.last();

		    if(curDate != null) { 
			FDPromoZipRestriction zipRestriction = (FDPromoZipRestriction) map.get(curDate);
			p.zone = "ZIP codes: ";
			String desc = "zipcodes";
			boolean allCodes = zipRestriction.getZipCodes().equals("ALL");
			boolean except = zipRestriction.getType().equals("EXCEPT");
			int count = 1;
			List zipCodeList = (List)zipRestriction.getZipCodeList();
			p.zone += except && allCodes ? "No " + desc:"";
			p.zone += except && !allCodes && !zipCodeList.isEmpty() ? "All " + desc+", except: " : "";
			p.zone += !except && allCodes ? "All " + desc : "";

			if(!allCodes){
				StringBuffer sb = new StringBuffer();
				for(Iterator x = zipCodeList.iterator(); x.hasNext(); ){
				    if(count == zipCodeList.size()){
					sb.append(x.next());
				    }
				    else {
					sb.append(x.next()+",");
				    }
				    if(count !=0 && count%4==0){
					sb.append("\r\n");
				    }
				    count++;
				}
				p.zone += sb.toString();
			 } 
			 p.zone += "<br>Depots: ";
			 desc="depots";
			 p.zone += except && allCodes ? "No " + desc:"";
			 p.zone += except && !allCodes && !zipCodeList.isEmpty() ? "All " + desc : "";
			 p.zone += !except && allCodes ? "All " + desc : "";
			 p.zone += !except && !allCodes ? "No " + desc : "";
		     } 

		  }
		  else {
		    p.zone = "All Zones and Depots";
		  }
		  promoRows.add(p);
		}
//		Collections.sort((List)promotions,promoTypeComparator);
		JspTableSorter sort = new JspTableSorter(request);
		Comparator comp = (Comparator)PROMO_COMPARATORS.get(sort.getSortBy());
		if (comp == null) {
			Collections.sort((List)promoRows, new ReverseComparator(COMP_START));
		} else {
			if (comp.equals(COMP_EXPIRE) || comp.equals(COMP_START)) {
				Collections.sort((List)promoRows, sort.isAscending() ? new ReverseComparator(comp) : comp);
			} else {
				Collections.sort((List)promoRows, sort.isAscending() ? comp : new ReverseComparator(comp));
			}
		}
		Collections.sort((List)promoRows, COMP_TYPE);
		return promoRows;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
	public class PromoRow {
		private String id;
	    private String code;
	    private String redemptionCode = "";
	    private String name;
	    private String description;
	    private double amount;
	    private Date start;
	    private Date expire;
	    private String zone;
	    private EnumPromotionType type;
		public double getAmount() {
			return amount;
		}
		public String getCode() {
			return code;
		}
		public String getDescription() {
			return description;
		}
		public Date getExpire() {
			return expire;
		}
		public String getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public String getRedemptionCode() {
			return redemptionCode;
		}
		public Date getStart() {
			return start;
		}
		public EnumPromotionType getType() {
			return type;
		}
		public String getZone() {
			return zone;
		}
	}

	public Comparator COMP_TYPE = new Comparator() { 
	    public int compare(Object o1, Object o2) { 
	    	PromoRow p1 = (PromoRow)o1;
	    	PromoRow p2 = (PromoRow)o2;
	    	return p1.type.compareTo(p2.type);
	    } 
	};

	private static Comparator COMP_NAME = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        return p1.name.toUpperCase().compareTo(p2.name.toUpperCase());
	    }
	};

	private static Comparator COMP_DESCRIPTION = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        return p1.description.toUpperCase().compareTo(p2.description.toUpperCase());
	    }
	};

	private static Comparator COMP_AMOUNT = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        return new Double(p1.amount - p2.amount).intValue();
	    }
	};

	private static Comparator COMP_START = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        if(p1.start == null) return 1;
	        if(p2.start == null) return -1;
	        
	        return p1.start.compareTo(p2.start);
	    }
	};

	private static Comparator COMP_EXPIRE = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        if(p1.expire == null) return 1;
	        if(p2.expire == null) return -1;
	        
	        return p1.expire.compareTo(p2.expire);
	    }
	};

	private static Comparator COMP_ZONE = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        return p1.zone.compareTo(p2.zone);
	    }
	};

	private static Comparator COMP_CODE = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;
	        
	        return p1.code.toUpperCase().compareTo(p2.code.toUpperCase());
	    }
	};

	private static Comparator COMP_REDEMP_CODE = new Comparator () {
	    public int compare(Object o1, Object o2) {
	        PromoRow p1 = (PromoRow)o1;
	        PromoRow p2 = (PromoRow)o2;

	        return p1.redemptionCode.toUpperCase().compareTo(p2.redemptionCode.toUpperCase());
	    }
	};


	public final static Map PROMO_COMPARATORS = new HashMap();
	static {
		PROMO_COMPARATORS.put("name", COMP_NAME);
	    PROMO_COMPARATORS.put("description", COMP_DESCRIPTION);
		PROMO_COMPARATORS.put("amount", COMP_AMOUNT);
		PROMO_COMPARATORS.put("start", COMP_START);
	    PROMO_COMPARATORS.put("expire", COMP_EXPIRE);
		PROMO_COMPARATORS.put("zone", COMP_ZONE);
		PROMO_COMPARATORS.put("code", COMP_CODE);
		PROMO_COMPARATORS.put("redemptionCode", COMP_REDEMP_CODE);
	}
}
