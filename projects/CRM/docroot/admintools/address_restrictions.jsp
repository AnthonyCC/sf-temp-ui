<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.AdminToolsControllerTag" %>
<%@ page import="com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason" %>
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

<tmpl:put name='title' direct='true'>Supervisor Resources > Address Restrictions</tmpl:put>

<tmpl:put name='content' direct='true'>

	<jsp:include page="/includes/admintools_nav.jsp" />
	
	<div class="admin_module_content">
		<crm:AdminToolsController result="result">
		<crm:GenericLocator id="restrictions" searchParam='ADDR_RESTRICTION_SEARCH' result="result">
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
		<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>
		<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>	
		
		<form name="addrRestrictions" id="addrRestrictions" method='POST' onsubmit="javascript:doSearch();">
			<input type="hidden" name="restrictionId" value="">
			
			<%@ include file="/includes/admintools/i_addr_restriction_search.jspf"%>
			
			<table class="home_search_module_field" border="0" cellpadding="0" cellspacing="0" width="99%">
				<tr><td class="border_bold"></td></tr>
				<tr>
					<td class="border_bold">
						<div class="home_search_module_content" style="background-color: #fff; width: 100%; height:600px;">
							<%@ include file="/includes/admintools/address_restrictions_list.jspf"%>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="home_search_module_content" style="background-color: transparent; width: 100%; height: 100px;">
							<%@ include file="/includes/admintools/add_address_restriction.jspf"%>
						</div>		
					</td>
				</tr>
			</table>
		</form>
		</crm:GenericLocator>
		</crm:AdminToolsController>
	</div>
</tmpl:put>
</tmpl:insert>

