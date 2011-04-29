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
<%@ page import='java.util.List' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>

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
            String path=NVL.apply(request.getParameter("path"), "");
            
      
    %>
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
		path=restriction.getPath();
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
            <input type="hidden" name="actionName" value="updateRestriction">
        <%= restrictionId %> </td>				
	</tr>	
	<tr>
		<td class="case_content_field">TYPE</td>        
		<td>		
			<select name="restrictedType" style="width: 45px;">
            <%
			    List enumTypes=EnumDlvRestrictionType.getEnumList();
			%>
			<logic:iterate collection="<%= enumTypes %>" id="enumType" type="com.freshdirect.delivery.restriction.EnumDlvRestrictionType">
			<%  
				EnumDlvRestrictionType eType=(EnumDlvRestrictionType)enumType;
			%>
				<option value="<%= eType.getName() %>" <%= eType.getName().equals(restrictedType) ? "SELECTED" : "" %>><%= eType.getName() %></option>
			</logic:iterate><fd:ErrorHandler result='<%=result %>' name='restrictedType' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			</select>                     			
		</td>		
	</tr>
	<tr>
		<td class="case_content_field">NAME</td>
		<td>			            
            <input type="text" length="50" size="50" name="name" value="<%=name%>">        
			<fd:ErrorHandler result='<%=result%>' name='name' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
</tr>
	<tr><td colspan="4"></td></tr>
	<tr valign="top">
		<td class="case_content_field">DAY_OF_WEEK</td>
		<td>			
             <input type="text" name="dayOfWeek" value="<%=dayOfWeek%>">
			<fd:ErrorHandler result='<%=result%>' name='dayOfWeek' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
		</td>
	</tr>
	<tr>
		<td class="case_content_field">START TIME</td>
		<td>            
			<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">                        
            <input type="text" name="newStartDate" id="newStartDate" size="10" value="<%=startDate%>" disabled="true" onchange="setDate1(this);"> &nbsp;<a href="#" id="trigger_startDate" style="font-size: 9px;">>></a>
            <fd:ErrorHandler result='<%=result%>' name='newStartDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
 		        <script language="javascript">
			    function setDate1(field){
			    document.getElementById("startDate").value=field.value;

			    }


			    Calendar.setup(
			    {
			    showsTime : false,
			    electric : false,
			    inputField : "newStartDate",
			    ifFormat : "%Y-%m-%d",
			    singleClick: true,
			    button : "trigger_startDate" 
			    }
			    );
			    
			    function clearAll(){
			    	 var d = new Date();
				    var date = d.getDate();
				    var month = d.getMonth()+1;
				    var year = d.getFullYear();
			    	var fd = year + "-" + month + "-" + date;                    
			    	document.getElementById("startDate").value = fd;
                  document.getElementById("newStartDate").value = fd;
			    	document.getElementById("reason").value = "";
			    	document.getElementById("message").value = "all";
			    	document.getElementById("restrictedType").value = "";
			    }
			    
			    			    
					//document.getElementById("actionName").value = actionName;
					//document.getElementById("searchcriteria").submit();	
					
				
			    	
			    

			    function openURL(inLocationURL) {

			    	popResize(inLocationURL, 400,400,'');

			    }

			</script>
		</td>		
	</tr>
	<tr>
		<td class="case_content_field">END TIME</td>
		<td>			
			<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">                        
            <input type="text" name="newEndDate" id="newEndDate" size="10" value="<%=endDate%>" disabled="true" onchange="setDate2(this);"> &nbsp;<a href="#" id="trigger_endDate" style="font-size: 9px;">>></a>
            <fd:ErrorHandler result='<%=result%>' name='newEndDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
 		        <script language="javascript">
			    function setDate2(field){
			    document.getElementById("endDate").value=field.value;

			    }


			    Calendar.setup(
			    {
			    showsTime : false,
			    electric : false,
			    inputField : "newEndDate",
			    ifFormat : "%Y-%m-%d",
			    singleClick: true,
			    button : "trigger_endDate" 
			    }
			    );			    									                
			    
			    function openURL(inLocationURL) {

			    	popResize(inLocationURL, 400,400,'');

			    }

			</script>
			
		</td>		
	</tr>
   	<tr>
		<td class="case_content_field">REASON</td>
		<td>
        <select name="reason" style="width: 120px;">				
            <%
			List enumReasons=EnumDlvRestrictionReason.getNonAlcoholEnumList();
			%>
			<logic:iterate collection="<%= enumReasons %>" id="enumReason" type="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason">
			<%  
				EnumDlvRestrictionReason eReason=(EnumDlvRestrictionReason)enumReason;
			%>
				<option value="<%= eReason.getName() %>" <%= eReason.getName().equals(reason) ? "SELECTED" : "" %>><%= eReason.getName() %></option>
			</logic:iterate>
		</select>    <fd:ErrorHandler result='<%=result%>' name='reason' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
			
		</td>		
	</tr>
    <tr>
		<td class="case_content_field">MESSAGE</td>
		<td>			
			<input type="text" length="50" size="50" name="message" value="<%=message%>">        
            <fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>    
    <tr>
		<td class="case_content_field">MEDIA PATH</td>
		<td>			
			<input type="text" length="50" size="50" name="path" value="<%=path%>">        
            <fd:ErrorHandler result='<%=result%>' name='path' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>    
    <tr>
		<td class="case_content_field">CRITERION</td>
		<td>			
        <select name="criterion" style="width: 120px;">				
            <%
			List enumCriterion=EnumDlvRestrictionCriterion.getEnumList();
			%>
			<logic:iterate collection="<%= enumCriterion %>" id="enumReason" type="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion">
			<%  
				EnumDlvRestrictionCriterion eReason=(EnumDlvRestrictionCriterion)enumReason;
			%>
				<option value="<%= eReason.getName() %>" <%= eReason.getName().equals(criterion) ? "SELECTED" : "" %>><%= eReason.getName() %></option>
			</logic:iterate>
			</select>                   			
            <fd:ErrorHandler result='<%=result%>' name='criterion' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>    
	<tr>
		<td></td>
		<td colspan="3">
			<input type="submit" value="UPDATE" class="submit">&nbsp;&nbsp;
		</td>        
	</tr>
    	<tr>
		<td></td>
		<td colspan="3">
			<INPUT TYPE="button" VALUE="CANCEL" onClick="window.close()">
		</td>        
	</tr>
    
</form>
</table>
	
 </crm:GetRestrictedDlv>
 </crm:AdminToolsController>

