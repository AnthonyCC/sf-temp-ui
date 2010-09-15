<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>

<% 
boolean submitted = request.getParameter("info") != null && request.getParameter("info").indexOf("thankyou") > -1; 

String redirectSuccessPage = NVL.apply(request.getParameter("successPage"), "");
if ("".equals(redirectSuccessPage)) {
    redirectSuccessPage = "/index.jsp";
}

String survey_source=(String)request.getAttribute("survey_source");
%>
    
<fd:CorporateServiceSurvey result='result' actionName='submitCorporateServiceSurvey' successPage='<%=request.getRequestURI()%>'>
       

<% if (submitted) { %>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<% if (request.getRequestURI().indexOf("cos_site_access_survey") > 0){%>
    <tr><td align="center" class="text12"><b><span class="title18" style="font-size: 28px;">THANK YOU</span><br><span class="text15">We greatly appreciate your time and interest.  We will keep you notified as our FreshDirect At The Office service expands.  To contact us in person, please call 1-866-283-7374.<br>
    <a href="<%=request.getRequestURI()%>?successPage=<%=redirectSuccessPage%>&#survey"><b>Click here to submit another name.</a>
    </td></tr>
<% } else { %>
    <tr><td align="center" class="text12"><b><span class="title18" style="font-size: 28px;">THANK YOU</span><br><span class="text15">For more details about FreshDirect At The Office <a href="javascript:popup('/help/faq_home_pop.jsp?page=cos','large')">click here</a>.<br>
    <a href="<%=request.getRequestURI()%>?successPage=<%=redirectSuccessPage%>&#survey"><b>Click here to submit another name.</a>
    </td></tr>
<% } %>
</table>
<% } else { %>
<b>Unfortunately we are unable to offer the FreshDirect At The Office corporate service to you at this time.</b> We're expanding all the time based on demand, so to understand your needs, we ask that you fill out the following brief survey. Or, you can skip the survey and continue to our store.
<br><br>
 	<%
        if (!result.isSuccess()) {
          String errorMsg=SystemMessageList.MSG_MISSING_SURVEY_INFO;
        %>
           <%@ include file="/includes/i_error_messages.jspf" %>   
    <% } %>
<form method="post" name="corporateServiceSurvey" action="<%=request.getRequestURI()%>#survey">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<%-- 1 --%>
    <tr>
        <td><img src="/media_stat/images/layout/clear.gif" width="140" height="1" border="0"></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="180" height="8" border="0"></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" border="0"></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1" border="0"></td>
    </tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="companyName"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Company Name<fd:ErrorHandler result="<%=result%>" name="companyName"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="25" class="text13" name="companyName" value="<%=request.getParameter("companyName")%>"></td>
    </tr>
    <tr><td><span class="space4pix"><br></span></td></tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="streetAddress"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Street Address<fd:ErrorHandler result="<%=result%>" name="streetAddress"></span></fd:ErrorHandler></td>
        <td><input type="text" size="25" class="text13" name="streetAddress" value="<%=request.getParameter("streetAddress")%>"></td>
        <td class="text12" align="right"><fd:ErrorHandler result="<%=result%>" name="floorSuite"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Floor/Suite #<fd:ErrorHandler result="<%=result%>" name="floorSuite"></span></fd:ErrorHandler>&nbsp;</td>
        <td><input type="text" size="5" class="text13" name="floorSuite" value="<%=request.getParameter("floorSuite")%>"></td>
    </tr>
    <tr><td><span class="space2pix"><br></span></td></tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="city"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>City<fd:ErrorHandler result="<%=result%>" name="city"></span></fd:ErrorHandler></td>
        <td><input type="text" size="25" class="text13" name="city" value="<%=request.getParameter("city")%>"></td>
        <td align="right" class="text12"><fd:ErrorHandler result="<%=result%>" name="state"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>State<fd:ErrorHandler result="<%=result%>" name="state"></span></fd:ErrorHandler>&nbsp;</td>
        <td><input type="text" size="3" class="text13" name="state" value="NY" value="<%=request.getParameter("state")%>"></td>
    </tr>
    <tr><td><span class="space4pix"><br></span></td></tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="zip"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>ZIP<fd:ErrorHandler result="<%=result%>" name="zip"></span></fd:ErrorHandler> <fd:ErrorHandler result="<%=result%>" name="zip4"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>+ 4<fd:ErrorHandler result="<%=result%>" name="zip4"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="5" maxlength="5" class="text13" name="zip" value="<%=request.getParameter("zip")%>"> - <input type="text" size="4" class="text13" maxlength="4" name="zip4" value="<%=request.getParameter("zip4")%>"></td>
    </tr>
    <tr><td><span class="space4pix"><br></span></td></tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="numEmp"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Number of Employees<fd:ErrorHandler result="<%=result%>" name="numEmp"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="8" class="text13" name="numEmp" value="<%=request.getParameter("numEmp")%>"></td>
    </tr>
    <tr><td><span class="space8pix"><br></span></td></tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="contact"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Contact Name<fd:ErrorHandler result="<%=result%>" name="contact"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="25" class="text13" name="contact" value="<%=request.getParameter("contact")%>"></td>
    </tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="title"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Title<fd:ErrorHandler result="<%=result%>" name="title"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="25" class="text13" name="title" value="<%=request.getParameter("title")%>"></td>
    </tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="phone"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Contact Number<fd:ErrorHandler result="<%=result%>" name="phone"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="25" class="text13" name="phone" value="<%=request.getParameter("phone")%>"></td>
    </tr>
    <tr>
        <td class="text12"><fd:ErrorHandler result="<%=result%>" name="email"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Email Address<fd:ErrorHandler result="<%=result%>" name="email"></span></fd:ErrorHandler></td>
        <td colspan="3"><input type="text" size="25" class="text13" name="email" value="<%=request.getParameter("email")%>"><fd:ErrorHandler result="<%=result%>" name="email" id="errorMsg"> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
    </tr>
        <tr>
            <td colspan="4" class="text12" align="center"><br><img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"><br>
            <input type="image" value="submit" src="/media_stat/images/template/help/b_submit.gif" width="68" height="18">
            <input type="image"  src="/media_stat/images/template/help/b_clear.gif"  onclick="document.corporateServiceSurvey.reset(); return false;" width="69" height="19">
            <%--input type="submit" value="SUBMIT SURVEY" style="background-color:#336600; color:#FFFFFF; font-weight: bold; font-size:9pt; padding-left: 40px; padding-right: 40px;"--%><br>
            <img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10">
        </tr>
	<input type="hidden" name="successPage" value="<%=redirectSuccessPage%>">
 <% 
       if(survey_source!=null && survey_source.trim().length()>0)
       {
    %>
      <input type="hidden" name="survey_source" value="<%=survey_source%>">
    <%  }  %>  
</table>
</form>
<% } %>
</fd:CorporateServiceSurvey>