<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUser" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
boolean notServiceable = false;
String successPage = NVL.apply(request.getParameter("successPage"), "/index.jsp");
String serviceType = NVL.apply(request.getParameter("serviceType"), "PREREG");
if (user.isNotServiceable()) {
	notServiceable = true;
}
boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
boolean emailSent = request.getParameter("email") != null && "sent".equalsIgnoreCase(request.getParameter("email"));
%>
<fd:SiteAccessController action='doPrereg' successPage='<%= successPage %>' result='result' serviceType='<%=serviceType%>'>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
<%-- <title><%= isBestCellars ? "Best Cellars" : "FreshDirect"%></title> --%>
 <% String var = isBestCellars ? "Best Cellars" : "FreshDirect"; %>
 <fd:SEOMetaTag title="<%=var %>"/>

<fd:javascript src="/assets/javascript/common_javascript.js"/>

    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">


<%!
    java.text.SimpleDateFormat dFormat = new java.text.SimpleDateFormat("MMMMMMMM d");
%>
<div align="center">
<form name="site_access" method="post" action="<%= request.getRequestURI() %>">
<input type="hidden" name="successPage" value="<%= successPage %>">
<input type="hidden" name="serviceType" value="<%= serviceType %>">
<% if (isBestCellars) { %>
	<img src="/media_stat/images/template/wine/bc_logo_home_original.gif" width="336" height="52">
	<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="16"><br>
	<img src="/media_stat/images/template/site_access/not_in_area.gif" width="491" height="29" alt="IS NOT AVAILABLE IN YOUR AREA"><br>
	<br><br>
	<font class="text12">Thanks for your interest in Best Cellars New York. We currently serve only metro<br>
	New York, but we invite you to visit our online store.<br><br><br>
	<input type="image" src="/media_stat/images/template/site_access/visit_store.gif" width="212" height="204" alt="Visit Our Store" border="0" value="submit"><br>
	<br><img src="/media_stat/images/layout/999966.gif" alt="" width="590" height="1" vspace="10"><br>
	<%@ include file="/includes/copyright_wine.jspf" %></font>
<% } else { %>
	<img src="/media_stat/images/template/site_access/fd_logo.gif" width="267" height="53" alt="FreshDirect"><br><br>
	<% if (notServiceable) { %>
	     <img src="/media_stat/images/template/site_access/zipfail_notavailable.gif" width="619" height="27" alt="is not available in your area"><br>
		 <br>
	     <font class="text15"><b>We currently serve the  New York Metropolitan area.</b></font>
		 <br><br>
	     <% if (!emailSent) { %><font class="text12">Give us your e-mail address and we'll drop you a note<br>if our service expands to your area.<br><% } %></font>
	<% } else { %>
		  <img src="/media_stat/images/template/site_access/zipfail_homenotinarea.gif" width="496" height="61" alt="home delivery is not available in your area">
	<%  }   %>
	<br>
	<% if (notServiceable) { %>
	<table cellpadding="0" cellspacing="0" border="0">
	<tr align="center">
	     <td class="text12">
		 <% if (!emailSent) { %>
		 	<b>Enter your zipcode:</b><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
		     <input class="text11" type="text" size="30" maxlength="5" name="zipcode" value="<%= request.getParameter("zipcode")!=null?request.getParameter("zipcode"):"" %>">
		 	<b>Enter your e-mail:</b><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
		     <input class="text11" type="text" size="30" name="email" value="<%= request.getParameter("email")!=null?request.getParameter("email"):"" %>">
		     <br>
		     <fd:ErrorHandler result='<%= result %>' name='technicalDifficulty' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
		     <fd:ErrorHandler result='<%= result %>' name='email' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
		     <fd:ErrorHandler result='<%= result %>' name='zipcode' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
		     <img src="/media_stat/images/layout/clear.gif" alt="" width="8" height="1">
		     <br>
			 <table align="center">
			 <tr>
			 	<td><input type="image" src="/media_stat/images/template/site_access/zipfail_button_email_visit.gif" alt="Send Your E-mail and Visit Our Store" border="0" value="submit" width="176" height="41"></td>
				<td><img src="/media_stat/images/template/site_access/zipfail_or.gif" width="28" height="19" hspace="20"></td>
				<td><a href="<%=successPage%>"><img src="/media_stat/images/template/site_access/zipfail_button_visit.gif" width="176" height="41" alt="Visit Our Store" border="0"></a></td>
			</tr>
			</table>
		 <% } else { %>
		 	<br><font color="#FF9900"><b>We have received your e-mail address<br>and will notify you when we start<br>delivering to your area.</b></font>
		 <% } %>
	     </td>
	</tr>
	</table>
	<% } else { %><br>
	<table cellpadding="0" cellspacing="0" border="0">
	<tr align="center"><td class="text12">Give us your e-mail address and we'll drop you a note<br>when we begin delivering to your area.</td></tr>
	<tr align="center">
		 	<b>Enter your zipcode:</b><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
		     <input class="text11" type="text" size="30" maxlength="5" name="zipcode" value="<%= request.getParameter("zipcode")!=null?request.getParameter("zipcode"):"" %>">
	     <td class="text12"><br><b>Enter your e-mail:</b><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
	     <input class="text11" type="text" size="30" name="email" value="<%= request.getParameter("email")!=null?request.getParameter("email"):"" %>">
	     <br>
	     <fd:ErrorHandler result='<%= result %>' name='technicalDifficulty' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
	     <fd:ErrorHandler result='<%= result %>' name='email' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
		     <fd:ErrorHandler result='<%= result %>' name='zipcode' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
	     <img src="/media_stat/images/layout/clear.gif" alt="" width="8" height="1">
	     <br>
	     <input type="image" src="/media_stat/images/template/site_access/send_email.gif" width="176" height="41" alt="Send your e-mail and visit our store" border="0" value="submit">
	     </td>
	</tr>
	</table>
	<% } %>
	<br>
	<br>
	<a href="javascript:fd.components.zipCheckPopup.openZipCheckPopup()">View our current delivery zones</a> or <a href='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'>enter a different zip code.</a>
	<br><br><br>
	<% if (notServiceable) { %>
		<img src="/media_stat/images/template/site_access/zipfail_coming_to_ny.gif" width="541" height="17">	
	<% } else { %>
		<img src="/media_stat/images/template/site_access/zipfail_other_great_ways.gif" width="541" height="17">
	<% } %>
	<br>
	<fd:IncludeMedia name="/media/editorial/site_access/other_service_location.html" />
	<a href="<%=successPage%>"><img src="/media_stat/images/template/site_access/zipfail_button_shop_pickup.gif" border="0" width="214" height="41" vspace="20"></a>
    <%@ include file="/site_access/includes/i_check_for_includes.jspf" %>
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="15"><br>
	<img src="/media_stat/images/layout/cccccc.gif" alt="" width="541" height="1"><br>								
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10"><br>			
	<%@ include file="/shared/template/includes/copyright.jspf" %>
	<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
<% } %>
<br><br>
</form>
</div>

</body>
</html>
</fd:SiteAccessController>