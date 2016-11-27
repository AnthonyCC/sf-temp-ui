<%@ taglib uri='template' prefix='tmpl' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ CRM KnowledgeBase : <tmpl:get name="title"/> /</title>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/kb.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
</head>
<body bgcolor="#FFFFFF" style="background: #FFFFFF; margin: 0px; padding: 0px;" onLoad="focus()">
<% String show = request.getParameter("show"); %>
<%@ include file="/includes/context_help.jspf" %>
<%try{%>
    <%-- header on top and store tree in the leftnav background: #99CCFF; border-right: solid 1px #333333;--%>
	<div class="sub_nav" style="padding: 0px; background: #FFCCCC;"><a href="/kbit/index.jsp" class="sub_nav_title" style=" text-decoration: none; padding: 4px; width: 23%; margin: 0px; padding-left: 10px; " title="Return to main">CRM Knowledgebase</a></div>
	
	<div style="float: left; background: #FFFFCE; padding: 8px; width: 23%; border-right: solid 1px #666666; border-bottom: solid 1px #666666;">
		<jsp:include page="/includes/kbit_toc.jsp"/><br clear="all"><span class="order_detail" style="margin-left: -8px;">Quicklinks to FreshDirect Web FAQs:<br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=about','715','940','freshdirect')">What We Do</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=signup','715','940','freshdirect')">Signing Up</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=security','715','940','freshdirect')">Security &amp; Privacy</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=shopping','715','940','freshdirect')">Shopping</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=payment','715','940','freshdirect')">Payment</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=deliveryHome','715','940','freshdirect')">Home Delivery</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=cos','715','940','freshdirect')">Corporate Delivery</A><br>
		<A HREF="javascript:popResize('http://www.freshdirect.com/help/faq_home.jsp?page=inside','715','940','freshdirect')">Jobs &amp; Corporate Info</A><br>
		<br><b>Telephone Project codes:</b>
		<table style="border:solid 1px #999999; background: #FFFFFF;">
		<tr align="right" valign="top">
		<td class="field_column">Break</td>
		<td>1000</td>
		<td></td>
		<td class="field_column">Meal</td>
		<td>1001</td>
		</tr>
		<tr align="right" valign="top">
		<td class="field_column">Project</td>
		<td>1002</td>
		<td></td>
		<td class="field_column">Credits</td>
		<td>2000</td>
		</tr>
		<tr align="right" valign="top">
		<td class="field_column">Email</td>
		<td>3000</td>
		<td></td>
		<td class="field_column">Floor coaching</td>
		<td>4000</td>
		</tr>
		<tr align="right" valign="top">
		<td class="field_column">Acct Services</td>
		<td>5000</td>
		<td></td>
		<td class="field_column">Transportation</td>
		<td>6000</td>
		</tr>
		<tr align="right" valign="top">
		<td class="field_column">Briefing</td>
		<td>7000</td>
		<td></td>
		<td class="field_column">Worklist</td>
		<td>8000</td>
		</tr>
		</table>
	</div>
	
	<div style="float: left; padding: 8px; width: 73%;">
        <tmpl:get name="content"/>
    </div>

<%}catch (Exception ex){
	ex.printStackTrace();
	throw ex;
}
%>
</body>
</html>