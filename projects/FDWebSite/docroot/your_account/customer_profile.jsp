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
<%
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
    }
	boolean submitted = "thankyou".equalsIgnoreCase(request.getParameter("info"));
	boolean hasTaken = false;
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	if ("FILL".equals(customer.getProfile().getAttribute("Usability"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
String department = request.getParameter("department");

%>
<script type="text/javascript">

function clear(p) {
    var x = p;

    for(i=0; i<x.length; i++) {

        x[i].value = '';
        x[i].checked= false;
        x[i].disabled= false;
    }
}


</script>
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:CustomerProfileSurveyTag actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="Customer Profile Survey">
<fd:IncludeMedia name="/media/editorial/site_pages/survey/cps_intro.html" />	
 <form id="junk" name="request_product" method="POST">	
<table cellpadding="0" cellspacing="0" border="0" class="text12">
<tr><td colspan="10">
    
    <input type="hidden" name="department" value="<%=department%>">
    <br>
    
<% if (submitted) {%>
<tr>
	<td colspan="10" class="text12" align="center">
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>

<% request.setAttribute("Survey","Customer Profile Survey");%>
<%@ include file="/includes/your_account/i_customer_profile.jspf" %>
	

</td>
</tr>
<% } %>
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
			<a href="javascript:clear(document.request_product)"><img src="/media_stat/images/template/newproduct/b_clear.gif" width="47" height="17" border="0" alt="Clear"></a>&nbsp;&nbsp;
            <a href="javascript:document.request_product.submit()"><img src="/media_stat/images/template/newproduct/b_send.gif" width="45" height="15" border="0" alt="Send Request"></a>&nbsp;&nbsp;
    </td>
    </tr>

</table>
</form>
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>
</TABLE>

</fd:CustomerProfileSurveyTag>
	</tmpl:put>
</tmpl:insert>
