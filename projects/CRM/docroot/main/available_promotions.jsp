<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='bean' prefix='bean' %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Available Promotions</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
	<div class="sub_nav">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">
	<tr>
		<td width="10%">
			<span class="sub_nav_title">Promotions</span> 
		</td>
		<td width="25%">
			<form method="POST"
				action='<%=request.getRequestURI() + "?" + request.getQueryString()%>'>
				<input type="text" name="search">
				<input type="submit" value="Search">&nbsp;<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmPromotionsHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Promotions Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a>
			</form>
		</td>
		<td width="45%">
			&nbsp;&nbsp;&nbsp; Jump to: 
			<a href="#redemption">
				<b>Redemption</b>
			</a> &middot; 
			<a href="#sample">
				<b>Sample</b>
			</a> &middot; 
			<a href="#signup">
				<b>Signup</b>
			</a> &middot; 
			<a href='<%=request.getRequestURI()%>'>
				<b>ALL</b>
			</a> &nbsp;&nbsp;&nbsp; 
			(Click column headers to sort)
		</td>
		<td width="20%"align="right">
			<% if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) { %><a href="/promotion/compose_promotion.jsp">Add a Promotion &raquo;</a><%}%>
		</td>
	</tr>
	</table>
	</div>
<%    
	JspTableSorter sort = new JspTableSorter(request);
%>

	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
	    <tr bgColor="#333366">
			<td width="1%"></td>
	        <td width="20%"><a href="?<%= sort.getFieldParams("name") %>" class="list_header_text">Name</a></td>
            <td width="12%"><a href="?<%= sort.getFieldParams("description")%>" class="list_header_text">Description</a></td>
			<td width="8%"><a href="?<%= sort.getFieldParams("amount") %>" class="list_header_text">Amount</a></td>
			<td width="2%"></td>
	        <td width="12%"><a href="?<%= sort.getFieldParams("start") %>" class="list_header_text">Start</a></td>
			<td width="14%"><a href="?<%= sort.getFieldParams("zone") %>" class="list_header_text">Zone</a></td>
	        <td width="8%"><a href="?<%= sort.getFieldParams("expire") %>" class="list_header_text">Expire</a></td>
	        <td width="10%"><a href="?<%= sort.getFieldParams("redemptionCode") %>" class="list_header_text">Redemp. Code</a></td>
	        <td width="10%"><a href="?<%= sort.getFieldParams("code") %>" class="list_header_text">System Code</a></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="12" height="1"></td>
		</tr>
<fd:GetAllPromotions id = "promoRows">
<% EnumPromotionType lastPromoType = null; %>
<logic:iterate id="p" collection="<%= promoRows %>" type="com.freshdirect.webapp.taglib.promotion.PromoRow">
<% 
	EnumPromotionType ptype = p.getType();
	if (lastPromoType==null || !lastPromoType.equals(ptype)) {
       		lastPromoType = ptype;
%>
     		<tr bgcolor="#DDDDDD" class="list_content"><td colspan="10">&nbsp;<a name="<%=ptype.getName().toLowerCase()%>"></a><i>Promotion Type:</i> <b><%=ptype.getName()%></b></td></tr> 
<%
    	}
%>
<% 
	String linkURL = "#";
	/*if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) { 
		linkURL = "/promotion/edit_promotion.jsp?promoId="+p.getCode();
	} else {
		linkURL = "/main/view_promotion.jsp?promoId="+p.getCode();
	}*/	
	linkURL = "/main/view_promotion.jsp?promoId="+p.getCode();
%>
            <tr valign="top" class="list_content" <%--= counter % 2 == 0 ? "class='list_odd_row'" : "" --%> style="padding-top: 3px; padding-bottom: 3px;">
                <td width="1%"  class="border_bottom">&nbsp;</td>
                <td width="20%" class="border_bottom" style="font-weight: normal"><a href="<%= linkURL %>"><%=p.getName()%></a></td>
                <td width="20%" class="border_bottom" style="font-weight: normal"><a href="<%= linkURL %>"><%=p.getDescription() %></a>&nbsp;</td>
				<td width="10%" class="border_bottom" style="font-weight: normal"><%=CCFormatter.formatCurrency(p.getAmount())%>&nbsp;</td>
                <td width="4%"  class="border_bottom" style="font-weight: normal"><a href='javascript:popup("/shared/promotion_popup.jsp?promoCode=<%=p.getCode()%>&cc=true", "<%=EnumPromotionType.SIGNUP.equals(ptype)?"large":"small"%>")'>Details</a>&nbsp;</td>
				<td width="15%" class="border_bottom" style="font-weight: normal"><%=CCFormatter.formatDate(p.getStart())%>&nbsp;</td>
                <td width="18%" class="border_bottom" style="font-weight: normal"><%=p.getZone()%>&nbsp;</td>
                <%
                  	Date expire = p.getExpire();
                %>
				<td width="10%" class="border_bottom" style="font-weight: normal"><%= expire != null ? CCFormatter.formatDate(expire) : ""%>&nbsp;</td>
		<%
		  	String rCode = p.getRedemptionCode();
		%>
                <td width="10%" class="border_bottom" style="font-weight: normal"><%=(rCode != null && !rCode.equals(""))? rCode.toUpperCase() : "N/A" %>&nbsp;&nbsp;&nbsp;</td>
                <td width="10%" class="border_bottom" style="font-weight: normal"><%=p.getCode() %>&nbsp;</td>
            </tr>

</logic:iterate>
</fd:GetAllPromotions>
</table>
</div>
</crm:GetCurrentAgent>
</tmpl:put>
</tmpl:insert>
