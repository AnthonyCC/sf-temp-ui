<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.delivery.model.RestrictedAddressModel"%>
<%@ page import="com.freshdirect.delivery.EnumRestrictedAddressReason"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>
<%@ page import='java.util.List' %>
<html>
<head></head>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>

    <script language="JavaScript">
    
    function doAction(actionName) {
             document.addr_restriction_detail.submit();                
	}
     </script> 

    <% 
      System.out.println("inside restrictionForm.jsp :"+request.getParameter("address1"));
      String address1=request.getParameter("address1");
      String reason = NVL.apply(request.getParameter("reason"), "all");
	  String apartment = NVL.apply(request.getParameter("apartment"), "");
       System.out.println("inside restrictionForm.jsp : "+apartment);
      String zipCode = NVL.apply(request.getParameter("zipCode"), "");
                              
    %>
    <crm:AdminToolsController result="result">
    <%
    String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";
    %>
	<crm:GetRestrictedDlv id="restrictionList" restrictionId="">
<%
      if(restrictionList!=null && restrictionList.size()>0 && request.getParameter("actionName")==null){
        RestrictedAddressModel restriction=(RestrictedAddressModel)restrictionList.get(0);
        address1=restriction.getAddress1();
        apartment=restriction.getApartment();
		zipCode=restriction.getZipCode();		
		EnumRestrictedAddressReason enumReason=restriction.getReason();
		reason=enumReason.getCode();	
      }
%>

 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>
<fd:ErrorHandler result='<%=result%>' name='GLOBAL_ERROR' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>                			

<br>
	<table width="90%" cellpadding="0" cellspacing="5" border="0" class="case_content_text">
<form name="addr_restriction_detail"  method="POST">        
	<tr>
		<td class="case_content_field">Id</td>      
		<td> 
            <input type="hidden" name="oldAddress1" value="<%=address1%>">
            <input type="hidden" name="oldApt" value="<%=apartment%>">
            <input type="hidden" name="oldZipCode" value="<%=zipCode%>">
            <input type="hidden" name="actionName" id="actionName" value="updateAddrRestriction">             
         </td>				      
	</tr>	
	<tr>
		<td class="case_content_field">reason</td>        
		<td>		
			<select name="reason" value="" style="width: 120px;">				
            <%
			List enumReasons=EnumRestrictedAddressReason.getEnumList();
			%>
			<logic:iterate collection="<%= enumReasons %>" id="enumReason" type="com.freshdirect.delivery.EnumRestrictedAddressReason">
			<%  
				EnumRestrictedAddressReason eReason=(EnumRestrictedAddressReason)enumReason;
			%>
				<option value="<%= eReason.getCode() %>" <%= reason.equals(eReason.getCode()) ? "SELECTED" : "" %>><%= eReason.getDescription() %></option>
			</logic:iterate>

			</select><fd:ErrorHandler result='<%=result%>' name='reason' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>                			
		</td>		
	</tr>
	<tr>
		<td class="case_content_field">Scrubbed Address</td>
		<td>
            <input type="text" length="50" size="50" name="address1" id="address1" value="<%=address1%>">        
			<fd:ErrorHandler result='<%=result%>' name='address1' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>                			
		</td>
    </tr>
    <tr>
		<td class="case_content_field">Apartment</td>
		<td>			            
            <input type="text" length="50" size="50" name="apartment" id="apartment" value="<%=apartment%>">        
			<fd:ErrorHandler result='<%=result%>' name='apartment' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>                			
		</td>
	</tr>
    <tr>
		<td class="case_content_field">Zip Code</td>
		<td>			            
            <input type="text" length="50" size="50" name="zipCode" id="zipCode" value="<%=zipCode%>">        
			<fd:ErrorHandler result='<%=result%>' name='zipCode' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>                			
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

</html>