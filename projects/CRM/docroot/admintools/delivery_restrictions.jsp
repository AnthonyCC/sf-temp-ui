<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.AdminToolsControllerTag" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Delivery Restrictions</tmpl:put>

<tmpl:put name='content' direct='true'>


<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:91%;">
<crm:AdminToolsController result="result">
<%

	//List restrictions = (List)session.getAttribute("DEL_RESTRICTION_SEARCH");
    request.setAttribute("admResult",result);

%>
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='cancelsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
<crm:GenericLocator id="restrictions" searchParam='DEL_RESTRICTION_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
<form name="delRestrictions" id="delRestrictions" method='POST' onsubmit="javascript:doSearch();">
<%@ include file="/includes/admintools/i_del_restriction_search.jspf"%>
<input type="hidden" name="restrictionId" value="">
 <%
           String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";

	int prcLimit = FDStoreProperties.getOrderProcessingLimit();
%>
<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<td colspan="2" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="submit" value="SEARCH RESTRICTIONS" class="submit">&nbsp;&nbsp;
			<input type="button" value="CLEAR" class="submit" onclick="javascript:clearAll();">
		</td>


	</tr>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>
	<tr colspan="2">
	<td>
		<span class="header_text"><b>Results</b></span>
	</td>
	</tr>
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:420;">
		<%@ include file="/includes/admintools/restrictions_list.jspf"%>
	</div>
	</td></tr>
    <tr colspan="2">
	<td>
		<span class="header_text"><b>Add Blocked Day</b> Restrict Order Placement/deliveries for a specific day. Use for Christmas, New Years, picnic and other required plant closures</span>
	</td>
	</tr>
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:180;">
		<%@ include file="/includes/admintools/add_blocked_day.jspf"%>
	</div>
	</td></tr>
    <tr colspan="2">
	<td>
		<span class="header_text"><b>Update Restriction Short Cut</b></span>
	</td>
	</tr>
	<tr><td colspan="2">
    <span class="header_text"><b>Plater Cutoffs</b> Modify kitchen/catering website cutoff for given day of week </span>
	</td></tr>
    <tr><td colspan="2">
      <A HREF="javascript:void(0)" onclick="window.open('/admintools/platterForm.jsp?actionType=getPlatterRestriction','getPlatterRestriction','width=350,height=400,menubar=no,status=no')">View/Edit</a>
	</td></tr>
   <!-- <tr><td colspan="2">
    <span class="header_text"><b>Holiday Pre-order</b> Reverse Restrictions on annual receuuring holiday , items only available on specific days
	</td></tr>
    <tr><td colspan="2">
      <a href="">View/Edit</a>
	</td></tr>  -->
    <tr><td colspan="2">
    <span class="header_text"><b>Kosher Holiday Schedule</b> Update Jewish holiday Dates
	</td></tr>
    <tr><td colspan="2">
      <A HREF="javascript:void(0)" onclick="window.open('/admintools/kosherForm.jsp?actionType=getKosherRestriction','getKosherRestriction','width=450,height=400,menubar=no,status=no')">View/Edit</a>
	</td></tr>
</table>
</form>
</crm:GenericLocator>
</crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>

