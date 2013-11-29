<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<%@ page import="com.freshdirect.delivery.model.*" %>
<%@ page import="com.freshdirect.webapp.taglib.promotion.WSPromoFilterCriteria" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionStatus"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>

<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.GenericLocatorTag"%>
<%@ page import="com.freshdirect.fdstore.promotion.PromotionFactory"%>

<%
	WSPromoFilterCriteria  wsFilter =  (WSPromoFilterCriteria)request.getSession().getAttribute("wsFilter"); 
	DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	String fromDateStr = null;
	String toDateStr = null;
	String dlvDateStr = null;
	String zone = null;
	String promoStatus = "LIVE";
	if(null != request.getParameter("ws_filter_submit")){
		fromDateStr = request.getParameter("wsFromDate");
		toDateStr = request.getParameter("wsToDate");
		dlvDateStr = request.getParameter("wsDlvDate");
		zone = request.getParameter("zone");
		promoStatus = request.getParameter("promoStatus");
		
		wsFilter = new WSPromoFilterCriteria();
		wsFilter.setFromDateStr(fromDateStr);
		wsFilter.setToDateStr(toDateStr);
		wsFilter.setStatus(promoStatus);
		wsFilter.setDlvDateStr(dlvDateStr);
		wsFilter.setZone(zone);
	} else if(null != wsFilter){
		fromDateStr = wsFilter.getFromDateStr();
		toDateStr = wsFilter.getToDateStr();
		promoStatus = wsFilter.getStatus();
		dlvDateStr = wsFilter.getDlvDateStr();
		zone = wsFilter.getZone();
	}
	final EnumPromotionStatus[] statuses;
	if (FDStoreProperties.isPromoPublishNodeMaster()) {
		statuses = new EnumPromotionStatus[]{
			EnumPromotionStatus.DRAFT, EnumPromotionStatus.APPROVE, EnumPromotionStatus.PROGRESS, EnumPromotionStatus.TEST, EnumPromotionStatus.PUBLISHED, EnumPromotionStatus.EXPIRED, EnumPromotionStatus.CANCELLING, EnumPromotionStatus.CANCELLED
		};
	} else {
		statuses = new EnumPromotionStatus[]{
				EnumPromotionStatus.LIVE, EnumPromotionStatus.CANCELLED
		};
	}


	for (EnumPromotionStatus s : statuses) {
		if(s.getName().equalsIgnoreCase(promoStatus)){
			promoStatus = s.getDescription();
		}
	}
	%>
	<table border="1">
				<tr>					
					<th >Date(s) From:<%= null!=fromDateStr && !"".equalsIgnoreCase(fromDateStr) ? fromDateStr : "" %> To <%= null!=toDateStr && !"".equalsIgnoreCase(toDateStr) ? toDateStr : "" %> </th>
					<th >Delivery date:<%= null!=dlvDateStr && !"".equalsIgnoreCase(dlvDateStr) ? dlvDateStr:"" %></th>
					<th >Zone:<%= null!=zone && !"".equalsIgnoreCase(zone) ? zone:"ALL" %></th>
					<th >Status:<%= null!=promoStatus && !"".equalsIgnoreCase(promoStatus) ? promoStatus:"ALL" %></th>
					<th >&nbsp;</th>
				</tr>
				<tr>
				</tr>
	</table>
	<table width="100%" cellspacing="0" cellpadding="0" border="1">
		<%    
	JspTableSorter sort = new JspTableSorter(request);
%>
		<tbody>
			<tr>				
				<th width="193px">Name</th>	
				<th width="110px">Delivery Date</th>	
				<th width="110px">Delivery Day</th>	
				<th width="90px">Start Date</th>	
				<th width="90px">End Date</th>	
				<th width="90px">Zone</th>	
				<th width="90px">Start Time</th>	
				<th width="90px">End Time</th>	
				<th width="100px">Window Type</th>	
				<th width="100px">Discount</th>	
				<th width="90px">Redemption Limit</th>	
				<th width="90px">Actual Redemptions</th>	
				<th width="90px">Status</th>				
			</tr>
		</tbody>
		</table>
	
	<fd:GetWSPromotions id="promoRows" filter="<%= wsFilter %>">	
	<table width="100%" cellspacing="0" cellpadding="0" border="1" >

	
<% EnumPromotionType lastPromoType = null; %>
<% if(promoRows.isEmpty()){ %>
<tr valign="top" style="color:#CC0000; font-weight: bold;"><td colspan="11" align="center">No matching promotions found.</td></tr>
<%} %>
<%
	DecimalFormat df = new DecimalFormat("0.00");
%>
<logic:iterate id="p" collection="<%= promoRows %>" type="com.freshdirect.fdstore.promotion.management.WSPromotionInfo" indexId="idx">
		
		<tr valign="top">			
			<td  style="font-weight: normal"><%= p.getName() %></span></td>
			<td  style="font-weight: normal"><%= p.getEffectiveDate() != null ? CCFormatter.formatDateMonth(p.getEffectiveDate()) : null %></span></td>
			<td  style="font-weight: normal"><%= p.getDayOfWeekStr() %></span></td>
			<td  style="font-weight: normal"><%= p.getStartDate() != null ? CCFormatter.formatCaseDate(p.getStartDate()) : null %></span></td>
			<td  style="font-weight: normal"><%= p.getEndDate() != null ? CCFormatter.formatCaseDate(p.getEndDate()) : null %></span></td>
			<td  style="font-weight: normal"><%= p.getZoneCode() %></span></td>
			<td  style="font-weight: normal"><%= p.getStartTime() %></span></td>
			<td  style="font-weight: normal"><%= p.getEndTime() %></span></td>
			<td  style="font-weight: normal"><%= p.getWindowTypeStr() %></span></td>
			<td  style="font-weight: normal">$<%=df.format(p.getDiscount()) %></span></td>
			<td  style="font-weight: normal"><%= p.getRedeemCount() %></span></td>
			<td  style="font-weight: normal"><%= PromotionFactory.getInstance().getRedemptions(p.getPromotionCode(), null) %></span></td>
			<td  style="font-weight: normal"><%= p.getStatus().getName() %></span></td>
		</tr>

</logic:iterate>
</table>
</fd:GetWSPromotions>
	
