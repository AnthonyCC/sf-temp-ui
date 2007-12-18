<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>

<%!
private static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);
private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

public class PromoRow {
    private String code;
    private String name;
    private double amount;
    private String orderNum;
    private Date dateUsed;
    private Date expires;
    private EnumPromotionType type;
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
private static Comparator COMP_AMOUNT = new Comparator () {
    public int compare(Object o1, Object o2) {
        PromoRow p1 = (PromoRow)o1;
        PromoRow p2 = (PromoRow)o2;
        
        return new Double(p1.amount - p2.amount).intValue();
    }
};
private static Comparator COMP_DATE_USED = new Comparator () {
    public int compare(Object o1, Object o2) {
        PromoRow p1 = (PromoRow)o1;
        PromoRow p2 = (PromoRow)o2;
        
        return p1.dateUsed.compareTo(p2.dateUsed);
    }
};

private static Comparator COMP_ORDERNUM = new Comparator () {
    public int compare(Object o1, Object o2) {
        PromoRow p1 = (PromoRow)o1;
        PromoRow p2 = (PromoRow)o2;
        
        return p1.orderNum.compareTo(p2.orderNum);
    }
};
private static Comparator COMP_EXPIRES = new Comparator () {
    public int compare(Object o1, Object o2) {
        PromoRow p1 = (PromoRow)o1;
        PromoRow p2 = (PromoRow)o2;
        
        if(p1.expires == null) return 1;
        if(p2.expires == null) return -1;
        
        return p1.expires.compareTo(p2.expires);
    }
};

public final static Map PROMO_COMPARATORS = new HashMap();
static {
	PROMO_COMPARATORS.put("code", COMP_CODE);
	PROMO_COMPARATORS.put("name", COMP_NAME);
	PROMO_COMPARATORS.put("amt", COMP_AMOUNT);
	PROMO_COMPARATORS.put("used", COMP_DATE_USED);
        PROMO_COMPARATORS.put("ordernum", COMP_ORDERNUM);
        PROMO_COMPARATORS.put("expires", COMP_EXPIRES);
}
%>
<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
List promoRows = new ArrayList();

for(Iterator i = user.getOrderHistory().getErpSaleInfos().iterator(); i.hasNext();){
    ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
    for(Iterator pc = saleInfo.getUsedPromotionCodes().iterator();pc.hasNext();){
        String promoCode = (String)pc.next();
        PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
        if(promo != null){
            PromoRow p = new PromoRow();
            p.code = promo.getPromotionCode();
            p.name = promo.getDescription();
            p.amount = promo.getHeaderDiscountTotal();
            p.orderNum = saleInfo.getSaleId();
            p.dateUsed = saleInfo.getCreateDate();
            p.expires = promo.getExpirationDate();
            p.type = promo.getPromotionType();
            
            promoRows.add(p);
        }
    }
}

JspTableSorter sort = new JspTableSorter(request);

Comparator comp = (Comparator)PROMO_COMPARATORS.get(sort.getSortBy());
if (comp == null) {
	Collections.sort(promoRows, new ReverseComparator(COMP_DATE_USED));
} else {
	if (comp.equals(COMP_DATE_USED)) {
		Collections.sort(promoRows, sort.isAscending() ? new ReverseComparator(comp) : comp);
	} else {
		Collections.sort(promoRows, sort.isAscending() ? comp : new ReverseComparator(comp));
	}
}
%>
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Promotion History</tmpl:put>
<tmpl:put name='content' direct='true'>
<div class="list_header">
    <table border="0" cellspacing="2" cellpadding="0 width="100%" class="list_header_text">
        <tr>
            <td width="2%"></td>
            <td width="11%"><a href="?<%= sort.getFieldParams("code") %>" class="list_header_text">Code</a></td>
            <td width="32%"><a href="?<%= sort.getFieldParams("name") %>" class="list_header_text">Name</a></td>
            <td width="11%">&nbsp;</td>
            <td width="11%"><a href="?<%= sort.getFieldParams("amt") %>" class="list_header_text">Amount</a></td>
            <td width="11%"><a href="?<%= sort.getFieldParams("ordernum") %>" class="list_header_text">Order #</a></td>
            <td width="11%"><a href="?<%= sort.getFieldParams("used") %>" class="list_header_text">Date Used</a></td>
            <td width="11%"><a href="?<%= sort.getFieldParams("expires") %>" class="list_header_text">Promo Expires</a></td>
            <td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
        </tr>
    </table>
</div>
<div class="list_content">
<table border="0" cellspacing="0" cellpadding="2" width='100%'>
<%
    int counter = 0;
    for(Iterator i = promoRows.iterator(); i.hasNext();){
        PromoRow p = (PromoRow) i.next();
        counter++;
%>
            <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
                <td width="2%"></td>
                <td width="11%"><%=p.code%></td>
                <td width="32%"><%=p.name%></td>
                <td width="11%"><a href='javascript:popup("/shared/promotion_popup.jsp?promoCode=<%=p.code%>&cc=true", "<%=EnumPromotionType.SIGNUP.equals(p.type)?"large":"small"%>")'>Details</td>
                <td width="11%"><%=CURRENCY_FORMATTER.format(p.amount)%></td>
                <td width="11%"><a href="order_details.jsp?orderId=<%=p.orderNum%>" class="key"><%=p.orderNum%></td>
                <td width="11%"><%=DATE_FORMATTER.format(p.dateUsed)%></td>
                <td width="11%"><%=p.expires != null?DATE_FORMATTER.format(p.expires): ""%></td>
            </tr>
            <tr class="list_separator" style="padding: 0px;">
                <td colspan="9"></td>
            </tr>
<%
    }
%>
</table>
</div>    
</tmpl:put>
</tmpl:insert>
