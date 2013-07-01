<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<fd:CheckLoginStatus guestAllowed="true" />
<% 
boolean submitted = request.getParameter("info") != null && request.getParameter("info").indexOf("thankyou") > -1; 
boolean fromSiteAccess = request.getParameter("sa") != null && request.getParameter("sa").equalsIgnoreCase("true");
String referringPage = request.getRequestURI();
if (fromSiteAccess && (request.getHeader("Referer") != null || request.getHeader("Referer") != "")) {
	referringPage = request.getHeader("Referer");
}

String redirectSuccessPage = NVL.apply(request.getParameter("successPage"), "");
if ("".equals(redirectSuccessPage)) {
    redirectSuccessPage = "/index.jsp";
}

String survey_source=(String)request.getAttribute("survey_source");
String sPage = request.getRequestURI();
String slite = "";
if("slite".equals(request.getParameter("referrer_page")) && sPage.indexOf("referrer_page=slite") == -1) {
	if(sPage.indexOf("?") != -1) {
		sPage = sPage + "&referrer_page=slite";		
	} else {
		sPage = sPage + "?referrer_page=slite";
	}
}
boolean curNewCust = request.getParameter("curNewCust") != null && Boolean.parseBoolean(request.getParameter("curNewCust"));

%>
<fd:CorporateServiceSurvey result='result' actionName='COS_Survey_Catering' successPage='<%=sPage%>'>

<%
	String fd_successPage=NVL.apply((String)request.getAttribute("fd_successPage"), "");
	if (fd_successPage.indexOf("thankyou") > -1) {
		submitted = true;
	}
%>
<% if (submitted) { %>
	<fd:IncludeMedia name="/media/editorial/site_pages/survey/cos_catering_survey_success.html" />
<% } else { %>
	 	<%
	        if (!result.isSuccess()) {
	          String errorMsg=SystemMessageList.MSG_MISSING_SURVEY_INFO;
	        %>
	           <%@ include file="/includes/i_error_messages.jspf" %>   
	    <% } %>
	<form method="post" name="cosCateringSurvey" id="cosCateringSurvey" action="<%=request.getRequestURI()%><%= (fromSiteAccess) ? "?sa=true":"" %>#survey">
	<div align="center">
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	    <tr>
	        <td><img src="/media_stat/images/layout/clear.gif" width="110" height="1" border="0"></td>
	        <td><img src="/media_stat/images/layout/clear.gif" width="260" height="1" border="0"></td>
	        <td><img src="/media_stat/images/layout/clear.gif" width="100" height="1" border="0"></td>
	        <td><img src="/media_stat/images/layout/clear.gif" width="60" height="1" border="0"></td>
	    </tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="companyName"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Company Name<fd:ErrorHandler result="<%=result%>" name="companyName"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="companyName" value="<%=request.getParameter("companyName")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space4pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="streetAddress"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Street Address<fd:ErrorHandler result="<%=result%>" name="streetAddress"></span></fd:ErrorHandler></td>
	        <td><input type="text" size="35" class="text13" name="streetAddress" value="<%=request.getParameter("streetAddress")%>"></td>
	        <td class="text12" align="right"><fd:ErrorHandler result="<%=result%>" name="floorSuite"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Floor/Suite #<fd:ErrorHandler result="<%=result%>" name="floorSuite"></span></fd:ErrorHandler>&nbsp;</td>
	        <td><input type="text" size="5" class="text13" name="floorSuite" value="<%=request.getParameter("floorSuite")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space2pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="city"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>City<fd:ErrorHandler result="<%=result%>" name="city"></span></fd:ErrorHandler></td>
	        <td><input type="text" size="35" class="text13" name="city" value="<%=request.getParameter("city")%>"></td>
	        <td align="right" class="text12"><fd:ErrorHandler result="<%=result%>" name="state"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>State<fd:ErrorHandler result="<%=result%>" name="state"></span></fd:ErrorHandler>&nbsp;</td>
	        <td><input type="text" size="3" class="text13" name="state" value="NY" value="<%=request.getParameter("state")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space4pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="zip"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>ZIP<fd:ErrorHandler result="<%=result%>" name="zip"></span></fd:ErrorHandler> <fd:ErrorHandler result="<%=result%>" name="zip4"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>+ 4<fd:ErrorHandler result="<%=result%>" name="zip4"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="5" maxlength="5" class="text13" name="zip" value="<%=request.getParameter("zip")%>"> - <input type="text" size="4" class="text13" maxlength="4" name="zip4" value="<%=request.getParameter("zip4")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space4pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="contact"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Contact Name<fd:ErrorHandler result="<%=result%>" name="contact"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="contact" value="<%=request.getParameter("contact")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space2pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="title"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Title<fd:ErrorHandler result="<%=result%>" name="title"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="title" value="<%=request.getParameter("title")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space2pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="phone"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Contact Number<fd:ErrorHandler result="<%=result%>" name="phone"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="phone" value="<%=request.getParameter("phone")%>"></td>
	    </tr>
	    <tr><td colspan="4"><span class="space2pix"><br></span></td></tr>
	    <tr>
	        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="email"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Email Address<fd:ErrorHandler result="<%=result%>" name="email"></span></fd:ErrorHandler></td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="email" value="<%=request.getParameter("email")%>"><fd:ErrorHandler result="<%=result%>" name="email" id="errorMsg"> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
	    </tr>
	    <tr><td colspan="4"><span class="space4pix"><br></span></td></tr>
	    <tr>
	        <td class="text12">Current Customer</td>
	        <td colspan="3"><input type="checkbox" class="text13" name="currentCust" value="<%= curNewCust %>" <%= (curNewCust)?"checked=\"checked\"":"" %>></td>
	    </tr>
	    <tr><td colspan="4"><span class="space4pix"><br></span></td></tr>
	    <tr>
	        <td class="text12">Date of Catering</td>
	        <td colspan="3"><input type="text" size="35" class="text13" name="dateOfCatering" value="<%=request.getParameter("dateOfCatering")%>"></td>
	    </tr>
	        <tr>
	            <td colspan="4" class="text12" align="center"><br><img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"><br>
	            <input type="image" value="submit" src="/media_stat/images/template/help/b_submit.gif" width="68" height="18">
	            <input type="image"  src="/media_stat/images/template/help/b_clear.gif"  onclick="document.cosCateringSurvey.reset(); return false;" width="69" height="19">
	            <%--input type="submit" value="SUBMIT SURVEY" style="background-color:#336600; color:#FFFFFF; font-weight: bold; font-size:9pt; padding-left: 40px; padding-right: 40px;"--%><br>
	            <img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"></td>
	        </tr>
	</table>
	<input type="hidden" name="successPage" value="<%=redirectSuccessPage%>">
	<% if(survey_source!=null && survey_source.trim().length()>0) { %>
		<input type="hidden" name="survey_source" value="<%=survey_source%>">
	<% } %>
	<%-- this field is required during validation --%>
	<input type="hidden" name="numEmp" value="-1" />
	<input type="hidden" name="isAjax" value="true" />
	</div>
	</form>
<% } %>
</fd:CorporateServiceSurvey>
