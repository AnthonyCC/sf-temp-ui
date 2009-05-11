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

        x[i].checked= false;
        x[i].disabled= false;
        x[i].selectedIndex=0;
    }
}

function _submit(p) {
   p.submit();
}
</script>
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:CustomerProfileSurveyTag actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="Customer Profile Survey">
<div style="width:700px;"><fd:IncludeMedia name="/media/editorial/site_pages/survey/cps_intro.html" /></div>
<form id="junk" name="request_product" method="POST">	
<table width="700" cellpadding="0" cellspacing="0" border="0" align="center">
    
<input type="hidden" name="department" value="<%=department%>">
<tr>
	<td colspan="10" class="text12">
    
<% if (submitted) {%>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br>
<% } else { %>
<% request.setAttribute("Survey","Customer Profile Survey");%>
<%@ include file="/includes/your_account/i_customer_profile.jspf" %>
<% } %>
	</td>
</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="28"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="28"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="28"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="18"></td>
	</tr>

	<tr>
		<td colspan="10" style="border-top:solid 1px #CCCCCC;"><br>
			<a href="javascript:document.request_product.submit()"><img src="/media_stat/images/buttons/submit_profile.gif" width="166" height="20" border="0" alt="SUBMIT MY PROFILE"></a>
			&nbsp;&nbsp;
 			<a href="javascript:clear(document.request_product)"><img src="/media_stat/images/buttons/clear_form.gif" width="110" height="20" border="0" alt="CLEAR FORM"></a>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
			Of course, we keep all of your information private 
	</td>
</form>
    </tr>
</table>
<!--table width="693" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
			<td width="30"><font class="space2pix"><br></font><input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></td>
			<td width="345"><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" width="57" HEIGHT="9" border="0" alt="CHECKOUT" VSPACE="0"></td>

			<td width="283" align="right" valign="middle">Clear preferences</td>
			<td width="35" align="right"><a href="/index.jsp"><img src="/media_stat/images/buttons/x_green.gif" width="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></td>
		</tr>
	</table-->
</fd:CustomerProfileSurveyTag>
	</tmpl:put>
</tmpl:insert>
