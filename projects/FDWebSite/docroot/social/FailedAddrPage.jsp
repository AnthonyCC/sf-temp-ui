<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@page
	import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.AddressModel"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.mail.EmailUtil"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='template' prefix='tmpl' %>

<%
session.setAttribute("lastpage", "FailedAddrPage");
String serviceType = request.getParameter("serviceType");

/* String companyname = "", firstname = "", lastname = "", streetaddr = "", 
suite = "", zipcode = "", city = "", state = "", 
 busphone = "", mobilephno = "", email = "";

companyname = NVL.apply(
	request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME
			.getCode()), "");
firstname = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_FIRST_NAME
			.getCode()), "");
lastname = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_LAST_NAME
			.getCode()), "");
streetaddr = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_ADDRESS_1
			.getCode()), "");
suite = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_APARTMENT
			.getCode()), "");
zipcode = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_ZIPCODE
			.getCode()), "");
city = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_CITY
			.getCode()), "");
state = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_STATE
			.getCode()), "");
busphone = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_WORK_PHONE
			.getCode()), "");
mobilephno = NVL.apply(request
	.getParameter(EnumUserInfoName.DLV_HOME_PHONE
			.getCode()), "");
email = NVL.apply(request.getParameter("email"), "").trim(); */
System.out.println("-----JSP-----");
String companyname = NVL.apply(request.getParameter("companyname"), "").trim();
System.out.println("companyname:"+companyname);
String firstname = NVL.apply(request.getParameter("firstname"), "").trim();
System.out.println("firstname:"+firstname);
String lastname = NVL.apply(request.getParameter("lastname"), "").trim();
System.out.println("lastname:"+lastname);
String streetaddr = NVL.apply(request.getParameter("streetaddr"), "").trim();
System.out.println("streetaddr:"+streetaddr);
String suite = NVL.apply(request.getParameter("suite"), "").trim();
System.out.println("suite:"+suite);
String zipcode = NVL.apply(request.getParameter("zipcode"), "").trim();
System.out.println("zipcode:"+zipcode);
String city = NVL.apply(request.getParameter("city"), "").trim();
System.out.println("city:"+city);
String state = NVL.apply(request.getParameter("state"), "").trim();
System.out.println("state:"+state);
String busphone = NVL.apply(request.getParameter("busphone"), "").trim();
System.out.println("busphone:"+busphone);
String mobilephno = NVL.apply(request.getParameter("mobilephno"), "").trim();
System.out.println("mobilephno:"+mobilephno);
String email = NVL.apply(request.getParameter("email"), "").trim();
System.out.println("email:"+email);
%>
<fd:SiteAccessController action='saveEmail' successPage='/main/index.jsp' result='result' serviceType='<%=serviceType%>'>
<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>
<%!
    java.text.SimpleDateFormat dFormat = new java.text.SimpleDateFormat("MMMMMMMM d");
%>

<div align="center">
<br>
<br>
<h4 align="center">OH NO!</h4><br>	
<h4 align="center">We don't deliver to that address yet.</h4><br>
<a href="/social/DeliveryAddress.jsp?serviceType=<%= serviceType %>&email=<%= email%>&companyname=<%= companyname%>&firstname=<%= firstname%>&lastname=<%= lastname%>&streetaddr=<%= streetaddr%>&suite=<%= suite%>&zipcode=<%= zipcode%>&city=<%= city%>&state=<%= state%>&busphone=<%= busphone%>&mobilephno=<%= mobilephno%>">Try Another address.</a>
<br>
<br>

<table cellpadding="0" cellspacing="0" border="0">
<form name="site_access" method="post" action="<%= response.encodeURL(request.getRequestURI()) %>">
<input type="hidden" name="serviceType" value="<%= serviceType %>">
<input type="hidden" name="email" value="<%= email %>">
<tr align="center"><td class="text12">Let us notify you when we are in your area.</td></tr>
<input type="hidden" name="serviceType" value="<%= serviceType %>">
<tr align="center">
     <td class="text12"><br><b>Enter your e-mail:</b><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
     <input class="text11" type="text" size="30" name="userEmail" value="<%= email %>" placeholder="Email">
     <br>
     <fd:ErrorHandler result='<%= result %>' name='technicalDifficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <fd:ErrorHandler result='<%= result %>' name='email' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
     <img src="/media_stat/images/layout/clear.gif" alt="" width="8" height="1">
     <br>
     <input type="submit" id="signupbtn" maxlength="25" size="19" value="Notify Me"> 
     </td>
</tr>
</form>
</table>

</div>
</fd:SiteAccessController>


