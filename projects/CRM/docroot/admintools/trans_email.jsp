<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>
<script>
    function doAction(actionName) {
	if(actionName == 'fixTEmailBatch'){
		var doFix = confirm ("Do you want to fix this failed email transactions?");
		if(doFix == false){
			return;
		}            		
		
		
		document.forms[0].elements[0].value = actionName;
		document.forms[0].submit();	
	}
	if(actionName == 'genarateTEmailFile'){
		var doFix = confirm ("Do you really want to generate file for failed email transactions?");
		if(doFix == false){
			return;
		}            		
		
		alert("forwarding to new URL");
		document.forms[0].elements[0].value = actionName;
		document.forms[0].action="trans_email_error.jsp";
		//window.location = "http://www.google.com/"
		document.forms[0].submit();	
	}
	
    }

</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Settlement Batch</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />
<crm:GenericLocator id="searchResults" searchParam="TEMAILS_BATCH_SEARCH" result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='fixsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>



<%
       Map errorMap=(Map)request.getAttribute("trans_error");
       if(errorMap!=null && errorMap.size()>0){  
   %>  <b3><font color="red">Threre are total <%=((String)errorMap.get("total_count"))%> errors in the   transactional email  <b3></font><br><br>
    <%        
	Set key=errorMap.keySet();
				Iterator iterator=key.iterator();
				while(iterator.hasNext()){
					String keyStr=(String)iterator.next();
					if(keyStr.equalsIgnoreCase("total_count")) continue;
					String value=(String)errorMap.get(keyStr);
     %>
              <b4> <%= value%> records of type <%=keyStr%> failed    <b4><br>
      <%					
				}
      }
%>
<br>
Displaying last 100 failed transactions
<br>
<table width="100%" cellpadding="0" cellspacing="3 border="3" bgcolor="#FFFFFF"> 
	<tr>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>TRANSACTION ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>TEMPLATE ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>ORDER ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>CUSTOMER ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>TRANSACTION TYPE</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>STATUS</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>TIME</b></span></td>	
	<td class="border_bold">&nbsp;</td>
    </tr>
	
	<%
		if(searchResults ==  null || searchResults.size() == 0) {
	%>
	<tr><td><span class="error">No failed transaction emails to fix.</span></td></tr>

	<%
		} else {
	%>	

	<logic:iterate id="sbInfo" collection="<%= searchResults %>" type="com.freshdirect.fdstore.temails.TransEmailInfoModel" indexId="index">
	<tr>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getId() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getTemplateId() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getOrderId() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getCustomerId() %></span></td>
    <td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getEmailTransactionType() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getEmailStatus() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"></span><%= sbInfo.getCroModDate() %></td>
	<td class="border_bottom">&nbsp;</td>	
    </tr>
    <tr>
	</tr>
	</logic:iterate>
	
	
	   <form name="tranEmails" method="POST">
	      <input type="hidden" name="actionName" id="actionName" value="">
	
      <tr><td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	<td><input type="button" value="FIX TRAN EMAIL BATCH" class="submit" onclick="javascript:doAction('fixTEmailBatch');">&nbsp;&nbsp;</td>
	<td><input type="button" value="GENERATE FILE" class="submit" onclick="javascript:doAction('genarateTEmailFile');">&nbsp;&nbsp;</td>
      </tr>
      </form>
	
	<%
		}
	%>	
	
</table>
</crm:AdminToolsController>
</crm:GenericLocator>
</tmpl:put>
</tmpl:insert>