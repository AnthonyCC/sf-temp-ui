<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="crm" prefix="crm" %>
<%
String successPage = "/customerprofile/customer_profile_summary.jsp";
boolean editable = false;
String case_required = "<span class=\"cust_module_content_edit\">-Case required to set/modify Customer Profile-</span>"; 
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
<crm:GetLockedCase id="cm">
    <% if (cm!=null && cm.getCustomerPK()!=null) { 
        String erpCustId = cm.getCustomerPK().getId();
            if (user.getIdentity().getErpCustomerPK().equals(erpCustId)) {
                editable = true;
            } 
        } %>
</crm:GetLockedCase>
<% 
    
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
    }
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
String department = request.getParameter("department");

%>
<script type="text/javascript">

function clear(p) {
    var x = p;

    for(i=0; i<x.length; i++) {

        x[i].checked= false;
        x[i].disabled= false;
        x[i].selectedIndex=0;
    }
}

function _submit(p) {
   p.submit();
}
</script>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Customer Profile</tmpl:put><tmpl:put name='content' direct='true'>

<fd:CustomerProfileSurveyTag actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="Customer Profile Survey">
 <form name="request_product" method="POST">	
<table cellpadding="0" cellspacing="0" border="0" class="text12">
<tr><td colspan="10">
    
    <input type="hidden" name="department" value="<%=department%>">
    <br>
    


<% request.setAttribute("Survey","Customer Profile Survey");%>
<%@ include file="/includes/customerprofile/i_customer_profile.jspf" %>
	

</td>
</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	</tr>

	<tr>
		<td colspan="10" align="center">
        <% if (editable) {%>
        
			<a href="javascript:clear(document.request_product)"><img src="/media_stat/images/template/newproduct/b_clear.gif" width="47" height="17" border="0" alt="Clear"></a>&nbsp;&nbsp;
            <a href="javascript:_submit(document.request_product)"><img src="/media_stat/images/template/newproduct/b_send.gif" width="45" height="15" border="0" alt="Send Request"></a>&nbsp;&nbsp;
        <%} else {%>
            <%= case_required%>
        <%}%>
    </td>
    </tr>

</table>
</form>
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>

</fd:CustomerProfileSurveyTag>
	</tmpl:put>
    </tmpl:insert>

