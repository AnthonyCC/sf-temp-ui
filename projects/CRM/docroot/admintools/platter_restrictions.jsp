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

<tmpl:put name='title' direct='true'>Supervisor Resources > Platter Restrictions</tmpl:put>

<tmpl:put name='content' direct='true'>
<script type="text/javascript">
	function toggleDate(resType){
	}
	
	  


	  function deleteRestriction(id) {
	      
	      document.delRestrictions.restrictionId.value=id;     
	      doAction('deleteRestrctions');    

	  }
</script>

<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:91%;">
<%
		String state = null;
		String county = null;
		
		/*
		GenericSearchCriteria criteria = (GenericSearchCriteria) session.getAttribute("ALCOHOL_RESTRICTION_CRITERIA");
		System.out.println("Criteria : " + criteria);
		if(criteria != null){
			Map criteriaMap = criteria.getCriteriaMap();
			state = (String)criteriaMap.get("state");
			county = (String)criteriaMap.get("county");
			reason = (EnumDlvRestrictionReason) criteriaMap.get("reason");
			restrictedType = (EnumDlvRestrictionType)criteriaMap.get("type");
		} else {*/
			state = NVL.apply(request.getParameter("state"), "NY");
			county = NVL.apply(request.getParameter("county"), "");
		
		//}

%>
<crm:AdminToolsController result="result">
<%

	//List restrictions = (List)session.getAttribute("DEL_RESTRICTION_SEARCH");
    //request.setAttribute("admResult",result);

%>
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='cancelsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
<crm:GenericLocator id="restrictions" searchParam='PLATTER_RESTRICTION_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
<form name="delRestrictions" id="delRestrictions" method='POST' onsubmit="javascript:doSearch();">
<%@ include file="/includes/admintools/i_platter_restriction_search.jspf"%>
<input type="hidden" name="restrictionId" value="">
 <%
           String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";

%>
<table class="case_content_text" border="0" cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<td colspan="2" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="submit" value="SEARCH PLATTER RESTRICTIONS" class="submit">&nbsp;&nbsp;
			<input type="button" value="CLEAR" class="submit" onclick="javascript:clearAll();">
		</td>


	</tr>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>
    <tr colspan="2">
	<td>
    <A HREF="javascript:void(0)" onclick="window.open('/admintools/platterForm.jsp?actionType=getPlatterRestriction','getPlatterRestriction','width=350,height=400,menubar=no,status=no')"><b>View/Edit Weekly Platter Restrictions</b></a>
    </td>
	</tr>
	<tr colspan="2">
	<td>&nbsp;
    </td>
	</tr>
	<tr colspan="2">
	<td>
		<span class="header_text"><b>List of Platter Restrictions</b></span>
	</td>
	</tr>
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:200;">
		<%@ include file="/includes/admintools/restrictions_list.jspf"%>
	</div>
	</td></tr>
	<tr>
		<td colspan="2" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="button" value="ADD PLATTER RESTRICTION" class="submit" onclick="javascript:location.href='/admintools/platterRestrictionForm.jsp';">
		</td>
	</tr>
	
</table>
</form>
</crm:GenericLocator>
</crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>

