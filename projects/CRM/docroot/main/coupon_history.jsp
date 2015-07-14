<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.fdlogistics.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import='com.freshdirect.fdstore.ecoupon.*' %>
<%@ page import='com.freshdirect.fdstore.ecoupon.model.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%!
private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

public class PromoRow {
	private String code;
	private String redempCode;
	private String name;
	private double amount;
	private String orderNum;
	private Date dateUsed;
	private String expires;
	private EnumPromotionType type;
	private FDCustomerCoupon fdCoupon;
	private String status;
}

private static Comparator COMP_NAME = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		return p1.name.compareTo(p2.name);
	}
};
private static Comparator COMP_CODE = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		return p1.code.compareTo(p2.code);
	}
};
private static Comparator COMP_REDEMPCODE = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		return p1.redempCode.compareTo(p2.redempCode);
	}
};
private static Comparator COMP_AMOUNT = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		Double d=new Double(p1.amount - p2.amount);
		if(d > 0){
			return 1;
		}else if(d < 0){
			return -1;
		}else{
			return 0;
		}
	}
};

private static Comparator COMP_ORDERNUM = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		if(null !=p1.orderNum && null!= p2.orderNum){
			return p1.orderNum.compareTo(p2.orderNum);
		}else{
			return 0;
		}
	}
};
private static Comparator COMP_USED = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		if(null !=p1.dateUsed && null!= p2.dateUsed){
			return DATE_FORMATTER.format(p1.dateUsed).compareTo(DATE_FORMATTER.format(p2.dateUsed));
		}else{
			return 0;
		}
	}
};

private static Comparator COMP_EXPIRES = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		if(null !=p1.expires && null!= p2.expires){	
			try{
				return DATE_FORMATTER.parse(p1.expires).compareTo(DATE_FORMATTER.parse(p2.expires));
			}catch(ParseException pe){
				return 0;
			}
		}else{
			return 0;
		}
	}
};

private static Comparator COMP_STATUS = new Comparator () {
	public int compare(Object o1, Object o2) {
		PromoRow p1 = (PromoRow)o1;
		PromoRow p2 = (PromoRow)o2;
		
		if(p1.status == null) return 1;
		if(p2.status == null) return -1;
		
		return p1.expires.compareTo(p2.expires);
	}
};

public final static Map PROMO_COMPARATORS = new HashMap();
static {
	PROMO_COMPARATORS.put("code", COMP_CODE);
	PROMO_COMPARATORS.put("redempCode", COMP_REDEMPCODE);
	PROMO_COMPARATORS.put("name", COMP_NAME);
	PROMO_COMPARATORS.put("amt", COMP_AMOUNT);
	PROMO_COMPARATORS.put("ordernum", COMP_ORDERNUM);
	PROMO_COMPARATORS.put("status", COMP_STATUS);
	PROMO_COMPARATORS.put("expires", COMP_EXPIRES);
	PROMO_COMPARATORS.put("used", COMP_USED);
}
%>
<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
List promoRows = new ArrayList();
String customerId=user.getIdentity().getErpCustomerPK();
FDCustomerCouponWallet cWallet = user.getCouponWallet();
List<FDCustomerCouponHistoryInfo> custCouponInfoList = FDCouponManager.getCustomersCouponUsage(customerId);
/* Collection erpSaleInfos = ((ErpOrderHistory)user.getOrderHistory()).getErpSaleInfos();
for(Iterator i = erpSaleInfos.iterator(); i.hasNext();){
    ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
	FDOrderI order = CrmSession.getOrder(session, saleInfo.getSaleId() ,true);
	List orderlines = order.getOrderLines();	
	for(Iterator ol = orderlines.iterator();ol.hasNext();){
		double discountAmt = 0;
		String coupon_id = null;
		FDCustomerCoupon fdCoupon = null;
		FDCartLineI orderline = (FDCartLineI) ol.next();
		if(orderline.getCouponDiscount() != null) {
			discountAmt = orderline.getCouponDiscount().getDiscountAmt();
			coupon_id = orderline.getCouponDiscount().getCouponId();
			fdCoupon = user.getCustomerCoupon(orderline, EnumCouponContext.VIEWORDER);
		}
		
		if (coupon_id != null) {
			FDCouponInfo cInfo = FDCouponFactory.getInstance().getCoupon(coupon_id);
			PromoRow p = new PromoRow();            
			p.name = cInfo.getShortDescription();
			p.amount = discountAmt;
			p.orderNum = saleInfo.getSaleId();
			p.dateUsed = saleInfo.getCreateDate();
			p.expires = cInfo.getExpirationDate();	
			p.fdCoupon = fdCoupon;			
			p.code = coupon_id;
			if(order.getOrderStatus().equals(EnumSaleStatus.SETTLED)) {
				p.status = "Redeemed";
			} else {
				p.status = "Redeem Pending";
			}
			promoRows.add(p);     
		}
	}
	
} */
if(null !=custCouponInfoList){
	for(Iterator ol = custCouponInfoList.iterator();ol.hasNext();){
		double discountAmt = 0;
		String coupon_id = null;
		FDCustomerCoupon fdCoupon = null;
		FDCustomerCouponHistoryInfo orderline = (FDCustomerCouponHistoryInfo) ol.next();
		PromoRow p = new PromoRow();            
			p.name = orderline.getCouponDesc();
			p.amount = orderline.getDiscountAmt();
			p.orderNum = orderline.getSaleId();
			p.dateUsed = orderline.getSaleDate();
			p.expires = "";	
			//p.fdCoupon = fdCoupon;			
			p.code = orderline.getCouponId();
			if(null !=cWallet){
				if(cWallet.isRedeemed(orderline.getCouponId())){
					p.status = "Redeemed";
					promoRows.add(p);
				}else if(cWallet.isRedeemPending(orderline.getCouponId())){
					p.status = "Redeem Pending";
					promoRows.add(p);
				}				
			}
			else {
				if(orderline.getSaleStatus().equals(EnumSaleStatus.SETTLED)) {
					p.status = "Redeemed";
				} else {
					p.status = "Redeem Pending";
				}
				//FDCouponInfo cInfo = FDCouponFactory.getInstance().getCoupon(orderline.getCouponId());
				//p.fdCoupon = fdCoupon;	
				promoRows.add(p); 
			}
	}
}

if(null !=cWallet){
	Set<String> cCoupons = cWallet.getClippedActiveIds();
	Iterator iter = cCoupons.iterator();
	while (iter.hasNext()) {
		String coupon_id = (String) iter.next();
		FDCouponInfo cInfo = FDCouponFactory.getInstance().getCoupon(coupon_id);
		if(cInfo != null) {
			PromoRow p = new PromoRow();            
			p.name = cInfo.getShortDescription();
			p.expires = DateUtil.formatDate(cInfo.getExpirationDate());	
			FDCustomerCoupon fdCoupon = new FDCustomerCoupon(cInfo,EnumCouponStatus.COUPON_CLIPPED_ACTIVE,null,false,null);
			//set display status so columsn will show info
			fdCoupon.setDisplayStatus(EnumCouponDisplayStatus.COUPON_CLIPPED_DISABLED);
			p.fdCoupon = fdCoupon;			
			p.code = coupon_id;
			p.status = "Applied";
			p.amount= Double.parseDouble(fdCoupon.getValue());
			promoRows.add(p);   
		}
	}
}
JspTableSorter sort = new JspTableSorter(request);

Comparator comp = (Comparator)PROMO_COMPARATORS.get(sort.getSortBy());
if(comp != null)
	Collections.sort(promoRows, sort.isAscending() ? comp : new ReverseComparator(comp));

%>
<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>
<tmpl:put name='title' direct='true'>Ecoupon History</tmpl:put>
<tmpl:put name='content' direct='true'>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="list_header">
		<td width="25" style="padding: 4px 0;">&nbsp;</td>
        <td width=""><a href="?<%= sort.getFieldParams("name") %>" class="list_header_text">Ecoupon Offer</a></td>
        <td width="100">&nbsp;</td>
		<td width="100"><a href="?<%= sort.getFieldParams("code") %>" class="list_header_text">Coupon ID</a></td>
		<td width="150"><a href="?<%= sort.getFieldParams("status")%>" class="list_header_text">Status</a></td>
        <td width="100"><a href="?<%= sort.getFieldParams("amt") %>" class="list_header_text">Amount</a></td>
        <td width="100"><a href="?<%= sort.getFieldParams("ordernum") %>" class="list_header_text">Order #</a></td>
        <td width="100"><a href="?<%= sort.getFieldParams("used") %>" class="list_header_text">Date Used</a></td>
        <td width="120"><a href="?<%= sort.getFieldParams("expires") %>" class="list_header_text">Coupon Expires</a></td>
        <td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	<%
		int counter = 0;
		for(Iterator i = promoRows.iterator(); i.hasNext();){
			PromoRow p = (PromoRow) i.next();
			counter++;
	%>
    <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
		<td>&nbsp;</td>
        <td><%=p.name%></td>
        <td><display:FDCoupon coupon="<%= p.fdCoupon %>" contClass="fdCoupon_cartlineChckout_CRMrec" showMsg="false" showCouponImage="false" showClipBox="false"></display:FDCoupon></td>
		<td><%= p.code %></td>
		<td><%= p.status %></td>
        <td><%=JspMethods.formatPrice(p.amount)%></td>
        <td><a href="order_details.jsp?orderId=<%=p.orderNum%>" class="key"><%=p.orderNum%></td>
        <td><%=(p.dateUsed != null)?DATE_FORMATTER.format(p.dateUsed):""%></td>
        <td><%=p.expires %></td>
		<td>&nbsp;</td>
	</tr>
    <tr class="list_separator" style="padding: 0px;">
		<td colspan="10"></td>
    </tr>
<%
    }
%>
</table>

</tmpl:put>
</tmpl:insert>
