<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Customer > Delivery Zone</tmpl:put>

<tmpl:put name='content' direct='true'>
<fd:SiteAccessController action='saveEmail' successPage='/main/index.jsp' result='result'>
<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>
<%!
    java.text.SimpleDateFormat dFormat = new java.text.SimpleDateFormat("MMMMMMMM d");
%>

<div class="content_scroll">
<div align="center">
<% if (user.isNotServiceable()) { %>
	<br>
	<img src="/media_stat/images/template/site_access/fd_logo.gif" width="267" height="53" alt="FreshDirect"><br><br>
     <img src="/media_stat/images/template/site_access/not_in_area.gif" width="491" height="29" alt="IS NOT AVAILABLE IN YOUR AREA"><br>
     <br><br>
     <font class="text12">Thanks for your interest in FreshDirect. We currently serve only metro<br>
     New York, but we invite you to visit our online store.</font>
<% } else { %>
	<a href="/registration/nw_cst_enter_details.jsp" class="checkout"><b>CREATE NEW CUSTOMER ACCOUNT</b></a><br><br>
	<img src="/media_stat/images/template/site_access/fd_logo.gif" width="267" height="53" alt="FreshDirect"><br><br>
	<img src="/media_stat/images/template/site_access/home_delivery_no.gif" width="388" height="66" alt="HOME DELIVERY IS NOT AVAILABLE IN YOUR AREA">
<%  }   %>
<br><br>
<% if (user.isNotServiceable()) { %>
<table cellpadding="0" cellspacing="15" border="0">
<form name="site_access" method="post" action="<%= response.encodeURL(request.getRequestURI()) %>">
<tr align="center">
     <td valign="bottom" width="212">
     <img src="/media_stat/images/template/site_access/truck.jpg" width="90" height="65"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
     Give us your e-mail address and we'll drop you a note when we begin delivering to your area.<br>
     <img src="/media_stat/images/layout/clear.gif" width="1" height="6">
     <br>
     <font class="text12"><b>Enter your e-mail:</b></font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
     <input class="text11" type="text" size="30" name="email" value="<%= request.getParameter("email")!=null?request.getParameter("email"):"" %>">
     <br>
     <fd:ErrorHandler result='<%= result %>' name='technicalDifficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <fd:ErrorHandler result='<%= result %>' name='email' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <img src="/media_stat/images/layout/clear.gif" width="8" height="1">
     <br>
     <input type="image" src="/media_stat/images/template/site_access/send_your_email.gif" alt="Send Your E-mail and Visit Our Store" border="0" value="submit">
     </td>
     <td><img src="/media_stat/images/template/site_access/or.gif" width="53" height="35"></td>
     <td width="212"><input type="image" src="/media_stat/images/template/site_access/visit_store.gif" width="212" height="204"  alt="Visit Our Store" border="0" value="submit"></td>
</tr>
</form>
</table>
<% } else { %>
<table cellpadding="0" cellspacing="0" border="0">
<form name="site_access" method="post" action="<%= response.encodeURL(request.getRequestURI()) %>">
<tr align="center"><td class="text12">Give us your e-mail address and we'll drop you a note<br>when we begin delivering to your area.</td></tr>
<tr align="center">
     <td class="text12"><br><b>Enter your e-mail:</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
     <input class="text11" type="text" size="30" name="email" value="<%= request.getParameter("email")!=null?request.getParameter("email"):"" %>">
     <br>
     <fd:ErrorHandler result='<%= result %>' name='technicalDifficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <fd:ErrorHandler result='<%= result %>' name='email' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <img src="/media_stat/images/layout/clear.gif" width="8" height="1">
     <br>
     <input type="image" src="/media_stat/images/template/site_access/send_email.gif" width="176" height="41" alt="Send your e-mail and visit our store" border="0" value="submit">
     </td>
</tr>
<tr align="center"><td><img src="/media_stat/images/template/site_access/or_s.gif" width="28" height="19" vspace="14"></td></tr>
<tr align="center">
     <td><input type="image" src="/media_stat/images/template/site_access/visit_store_s.gif" width="176" height="40"  alt="Visit our store" border="0" value="submit"></td>
</tr>
</form>
</table>
<% } %>
<br>
<% if (!user.isNotServiceable()) { %>
<br>
<img src="/media_stat/images/layout/cccccc.gif" width="570" height="1" alt="" border="0"><br>								
<img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt="" border="0"><br>
<img src="/media_stat/images/template/site_access/zipfail_did_you_know.gif" width="316" height="13" border="0" vspace="8"><br>You can also enjoy food from FreshDirect by<br>picking up your order at our <a href="javascript:popup('/delivery_popup.jsp','large');">Long Island City facility</a>.*
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt="" border="0"><br>
<a href="javascript:popup('/delivery_popup.jsp','large');"><img src="/media_stat/images/template/pickup/lic_pickup_img.jpg" width="115" height="75" border="0" alt="Our Facility" vspace="6"><br><img src="/media_stat/images/template/site_access/zipfail_our_facility.gif" width="72" height="9" border="0"></a><br>Minutes from Manhattan!<br>
<br>
<% } %>
<br>
<a href="javascript:popup('/help/delivery_zones.jsp','large')">View our current delivery zones</a> or <a href="nw_cst_check_zone.jsp">enter a different zip code.</a>
<br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="15"><br>
<img src="/media_stat/images/layout/cccccc.gif" width="270" height="1"><br>								
<img src="/media_stat/images/layout/clear.gif" width="1" height="20"><br>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
<br><br><br>

</div>
</div>
</fd:SiteAccessController>

</tmpl:put>

</tmpl:insert>


