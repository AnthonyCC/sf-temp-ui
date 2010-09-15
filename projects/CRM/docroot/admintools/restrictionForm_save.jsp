<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeReverseRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RecurringRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RestrictionI"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>

	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>




    <% 
      String restrictionId=request.getParameter("restrictionId");
      String startDate = 
			NVL.apply(request.getParameter("startDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
      String endDate = 
			NVL.apply(request.getParameter("endDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
			String reason = NVL.apply(request.getParameter("reason"), "all");
			String message = NVL.apply(request.getParameter("message"), "");
			String restrictedType = NVL.apply(request.getParameter("restrictedType"), "");
            String name=NVL.apply(request.getParameter("name"), "");
            String criterion=NVL.apply(request.getParameter("criterion"), "");
            String dayOfWeek=NVL.apply(request.getParameter("dayOfWeek"), "");
            
      
    %>
    <script>
function editRestrictionPage(){
   
    document.location.href = "/admintools/restrictionForm.jsp?restrictionId=<%= restrictionId %>&actionType=editRestriction";
}
</script>
    <crm:AdminToolsController result="result">
    <%
    String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";
    %>
	<crm:GetRestrictedDlv id="restrictionList" restrictionId="<%= restrictionId %>">
<%
      if(restrictionList!=null && restrictionList.size()>0 && request.getParameter("actionName")==null){
        RestrictionI restriction=(RestrictionI)restrictionList.get(0);
        restrictionId=restriction.getId();
        restrictedType=restriction.getType().getName();
		name=restriction.getName();		
		reason=restriction.getReason().getName();
		message=restriction.getMessage();
		criterion=restriction.getCriterion().getName();
          if(restriction instanceof OneTimeReverseRestriction){
                OneTimeReverseRestriction otrRestriction=(OneTimeReverseRestriction)restriction;			
                startDate=CCFormatter.formatDateYear(otrRestriction.getDateRange().getStartDate());
                endDate=CCFormatter.formatDateYear(otrRestriction.getDateRange().getEndDate());			
          }else if(restriction instanceof OneTimeRestriction){
                OneTimeRestriction otRestriction=(OneTimeRestriction)restriction;			
                startDate=CCFormatter.formatDateYear(otRestriction.getDateRange().getStartDate());
                endDate=CCFormatter.formatDateYear(otRestriction.getDateRange().getEndDate());			
          }else if(restriction instanceof RecurringRestriction){
                RecurringRestriction rRestriction=(RecurringRestriction)restriction;
                dayOfWeek=""+rRestriction.getDayOfWeek();
                startDate=CCFormatter.formatDateYear(rRestriction.getTimeRange().getStartTime().getNormalDate());
                endDate=CCFormatter.formatDateYear(rRestriction.getTimeRange().getEndTime().getNormalDate());			
          }
      }
%>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>
<br>
	<table width="90%" cellpadding="0" cellspacing="5" border="0" class="case_content_text">
<form name="restriction_detail" method="POST">        
	<tr>
		<td class="case_content_field">Id</td>
		<td><input type="hidden" name="restrictionId" value="<%=restrictionId%>">            
        <%= restrictionId %> </td>				
	</tr>	
	<tr>
		<td class="case_content_field">TYPE</td>        
		<td>	<%= restrictedType %>	
		</td>		
	</tr>
	<tr>
		<td class="case_content_field">NAME</td>
		<td>			            
            <%=name%>			
		</td>
</tr>
	<tr><td colspan="4"></td></tr>
	<tr valign="top">
		<td class="case_content_field">DAY_OF_WEEK</td>
		<td>			
             <%=dayOfWeek%>			
		</td>
	</tr>
	<tr>
		<td class="case_content_field">START TIME</td>
		<td>  <%=startDate%>          

		</td>		
	</tr>
	<tr>
		<td class="case_content_field">END TIME</td>
		<td>	<%=endDate%>					
		</td>		
	</tr>
   	<tr>
		<td class="case_content_field">REASON</td>
		<td>
            <%=reason %>			
		</td>		
	</tr>
    <tr>
		<td class="case_content_field">MESSAGE</td>
		<td>			
			<%=message%>
		</td>		
	</tr>    
    <tr>
		<td class="case_content_field">CRITERION</td>
		<td>	<%= criterion %>		
		</td>		
	</tr>    
	<tr>
		<td></td>
		<td colspan="3">
			<input type="BUTTON" value="EDIT RESTRICTION" class="submit" onClick="javascript:editRestrictionPage();">&nbsp;&nbsp;
		</td>
	</tr>
</form>
</table>
	
 </crm:GetRestrictedDlv>
 </crm:AdminToolsController>

