<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.referral.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>

<%!
private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

private static Comparator REF_NAME = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getName().compareTo(r2.getName());
    }
};
private static Comparator REF_EMAIL_ADDR = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getEmailAddress().compareTo(r2.getEmailAddress());
    }
};
private static Comparator EMAIL_ADDR_2 = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
	
		String  sea1 = 	r1.getEmailAddress2();
		String  sea2 = 	r2.getEmailAddress2();
		
		if (sea1 == null) {	sea1 = ""; }
		if (sea2 == null) {	sea2 = ""; }
		
        return sea1.compareTo(sea2);
    }
};
private static Comparator REF_DATE = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralDate().compareTo(r2.getReferralDate());
    }
};
private static Comparator REF_STATUS = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralStatus().compareTo(r2.getReferralStatus());
    }
};
private static Comparator REF_PROG_DESC = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralProgramDesc().compareTo(r2.getReferralProgramDesc());
    }
};
private static Comparator REF_PROG_STATUS = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralProgramStatus().compareTo(r2.getReferralProgramStatus());
    }
};
private static Comparator REF_PROG_START_DATE = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralProgramStartDate().compareTo(r2.getReferralProgramStartDate());
    }
};
private static Comparator REF_PROG_EXP_DATE = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getReferralProgramExpirationDate().compareTo(r2.getReferralProgramExpirationDate());
    }
};

private static Comparator NUM_DEL_ORDERS = new Comparator () {
    public int compare(Object o1, Object o2) {
        FDReferralReportLine r1 = (FDReferralReportLine)o1;
        FDReferralReportLine r2 = (FDReferralReportLine)o2;
        
        return r1.getNumDeliveredOrders()-r2.getNumDeliveredOrders();
    }
};

public final static Map REFERRAL_COMPARATORS = new HashMap();
static {
	REFERRAL_COMPARATORS.put("name", REF_NAME);
	REFERRAL_COMPARATORS.put("emailAddress", REF_EMAIL_ADDR);
	REFERRAL_COMPARATORS.put("emailAddress2", EMAIL_ADDR_2);
	REFERRAL_COMPARATORS.put("referralDate", REF_DATE);
	REFERRAL_COMPARATORS.put("referralStatus", REF_STATUS);
	REFERRAL_COMPARATORS.put("numDeliveredOrders", NUM_DEL_ORDERS);
	REFERRAL_COMPARATORS.put("referralProgramDescription", REF_PROG_DESC);
	REFERRAL_COMPARATORS.put("referralProgramStatus", REF_PROG_STATUS);
	REFERRAL_COMPARATORS.put("referralProgramStartDate", REF_PROG_START_DATE);
	REFERRAL_COMPARATORS.put("referralProgramExpirationDate", REF_PROG_EXP_DATE);
}
%>
<%

String reportType = request.getParameter("rpt_type");
if (reportType == null) {
	reportType = "REFERRER";
}
 
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
List referralRptList = new ArrayList();

if ("REFERRER".equalsIgnoreCase(reportType)) {
	referralRptList = FDReferralManager.loadReferralReportFromReferrerCustomerId(identity.getErpCustomerPK());
} else {
	referralRptList = FDReferralManager.loadReferralReportFromReferralCustomerId(identity.getErpCustomerPK());
}

JspTableSorter sort = new JspTableSorter(request);

Comparator comp = (Comparator)REFERRAL_COMPARATORS.get(sort.getSortBy());
if (comp == null) {
	Collections.sort(referralRptList, new ReverseComparator(REF_DATE));
} else {
	if (comp.equals(REF_DATE)) {
		Collections.sort(referralRptList, sort.isAscending() ? new ReverseComparator(comp) : comp);
	} else {
		Collections.sort(referralRptList, sort.isAscending() ? comp : new ReverseComparator(comp));
	}
}
%>
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Referral History - <%=(("REFERRER".equalsIgnoreCase(reportType))) ? "Referrals Sent" : "Referrals Received"%></tmpl:put>
<tmpl:put name='content' direct='true'>
<div class="list_header">
<table border="0" cellspacing="2" cellpadding="0 width="100%" class=list_header_text">
    <tr>
        <td><a href="?rpt_type=REFERRER" class="list_header_text">Referrals Sent</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="?rpt_type=REFERRAL" class="list_header_text">Referrals Received</a></td>
    </tr>
    <tr>
        <td class="list_header_text"><%=referralRptList.size()%> <%=(("REFERRER".equalsIgnoreCase(reportType))) ? "Referrals Sent" : "Referrals Received"%></td>
    </tr>
</table>
</div>
<div class="list_header">
    <table border="0" cellspacing="2" cellpadding="0 width="100%" class="list_header_text">
        <tr>
            <td width="2%"></td>
            <td width="13%"><a href="?<%= sort.getFieldParams("name") %>" class="list_header_text"><%=(("REFERRER".equalsIgnoreCase(reportType))) ? "Sent To" : "Sender"%></a></td>
            <td width="18%"><a href="?<%= sort.getFieldParams("emailAddress") %>" class="list_header_text">Email</a></td>
            <td width="8%"><a href="?<%= sort.getFieldParams("referralDate") %>" class="list_header_text">Date</a></td>
            <td width="10%"><a href="?<%= sort.getFieldParams("referralStatus") %>" class="list_header_text">Ref. Status</a></td>
            <td width="18%"><a href="?<%= sort.getFieldParams("emailAddress2") %>" class="list_header_text"><%=(("REFERRER".equalsIgnoreCase(reportType))) ? "Signup Email" : "Sent To"%></a></td>
            <td width="5%" class="list_header_text"><a href="?<%= sort.getFieldParams("numDeliveredOrders") %>" class="list_header_text">Orders</a></td>
            <td width="8%"><a href="?<%= sort.getFieldParams("referralProgramDescription") %>" class="list_header_text">Program</a></td>
            <td width="10%"><a href="?<%= sort.getFieldParams("referralProgramStatus") %>" class="list_header_text">Status</a></td>
            <td width="8%"><a href="?<%= sort.getFieldParams("referralProgramExpirationDate") %>" class="list_header_text">Exp.</a></td>
            <td><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
        </tr>
    </table>
</div>
<div class="list_content">
<table border="0" cellspacing="0" cellpadding="2" width='100%'>
<%
    int counter = 0;
    for(Iterator i = referralRptList.iterator(); i.hasNext();){
        FDReferralReportLine r = (FDReferralReportLine) i.next();
        counter++;
        if (r.getIsReferralAccepted()) {
        	r.setReferralStatus(EnumReferralStatus.SIGNUP);
        }
%>
            <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
                <td width="2%"></td>
                <td width="13%"><%= (r.getIsReferralAccepted()) ? "*" : "" %><%=r.getName()%></td>
                <td width="18%"><%=r.getEmailAddress()%></td>
                <td width="8%"><%=DATE_FORMATTER.format(r.getReferralDate())%></td>
                <td width="10%"><%=r.getReferralStatus()%></td>
                <td width="18%"><%=r.getEmailAddress2()%></td>
                <td width="5%"><%=r.getNumDeliveredOrders()%></td>
                <td width="8%"><a href="javascript:pop('/media/editorial/tell_a_friend/<%=r.getReferralProgramCampaignCode().toLowerCase()%>.html',400,585);"><%=r.getReferralProgramCampaignCode()%></a></td>
                <td width="10%"><%=r.getReferralProgramStatus()%></td>
                <td width="8%"><%=DATE_FORMATTER.format(r.getReferralProgramExpirationDate())%></td>
	            <td><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
            </tr>
            <tr class="list_separator" style="padding: 0px;">
                <td colspan="11"></td>
            </tr>
<%
    }
%>
</table>
</div>    
</tmpl:put>
</tmpl:insert>
