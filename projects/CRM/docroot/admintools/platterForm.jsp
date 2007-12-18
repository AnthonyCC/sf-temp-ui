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
<%@ page import="com.freshdirect.framework.util.TimeOfDayRange"%>
<%@ page import="com.freshdirect.framework.util.TimeOfDay"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Calendar" %>
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



   
    <crm:AdminToolsController result="result">
    <%
    String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";
            
            
     final TreeMap timeMap=new TreeMap();
    timeMap.put(new Integer(0),"00:00 AM");    
    timeMap.put(new Integer(1),"01:00 AM");    
    timeMap.put(new Integer(2),"02:00 AM");    
    timeMap.put(new Integer(3),"03:00 AM");    
    timeMap.put(new Integer(4),"04:00 AM");    
    timeMap.put(new Integer(5),"05:00 AM");    
    timeMap.put(new Integer(6),"06:00 AM");    
    timeMap.put(new Integer(7),"07:00 AM");    
    timeMap.put(new Integer(8),"08:00 AM");        
    timeMap.put(new Integer(10),"10:00 AM");    
    timeMap.put(new Integer(11),"11:00 AM");    
    timeMap.put(new Integer(12),"12:00 PM");    
    timeMap.put(new Integer(13),"01:00 PM");    
    timeMap.put(new Integer(14),"02:00 PM");    
    timeMap.put(new Integer(15),"03:00 PM");    
    timeMap.put(new Integer(16),"04:00 PM");    
    timeMap.put(new Integer(17),"05:00 PM");    
    timeMap.put(new Integer(18),"06:00 PM");    
    timeMap.put(new Integer(19),"07:00 PM");    
    timeMap.put(new Integer(20),"08:00 PM");    
    timeMap.put(new Integer(21),"09:00 PM");    
    timeMap.put(new Integer(22),"10:00 PM");    
    timeMap.put(new Integer(23),"11:00 PM");     

    %>
	<crm:GetRestrictedDlv id="restrictionList" restrictionId="">


&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>
<br>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<form name="platter_form" method="POST">        
	<tr>
		<td class="case_content_field">Id</td>
		<td>
            <input type="hidden" name="actionName" value="updatePlatter">
       </td>				
	</tr>	

	<tr>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Name #</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Platter CutOff Time</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	</tr>  
<% int index=0; %>    
    <logic:iterate collection="<%= restrictionList %>" id="restriction" type="com.freshdirect.delivery.restriction.RestrictionI">
        <tr>		            			
			<%  
				RecurringRestriction rRestriction=(RecurringRestriction)restriction;
                System.out.println(rRestriction.getTimeRange().getStartTime().getAsHours());
			%>            
                <td> <%=rRestriction.getName()%> </td>			
				<td></td>                
                <td>
                <select name="platterStartTime<%=index++%>">                
               <%                  
                     java.util.Set keySet=timeMap.keySet();  
                     java.util.Iterator iterator=keySet.iterator();
                     while(iterator.hasNext()){
                       Integer key=(Integer)iterator.next();                    
                       String value=(String)timeMap.get(key);
                     
               %>
                <option value="<%=value%>" <% if(rRestriction.getTimeRange().getStartTime().getAsHours()-5==key.intValue()){ %> <%="selected"%> <% } %> > <%=value%></option>
               <%
                    }
                 
                %>                                               
               
                </select>
                 </td>			
			    <td></td>        
		</tr>		
     </logic:iterate>                 	
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

