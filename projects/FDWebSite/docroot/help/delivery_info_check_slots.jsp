<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>

<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" recognizedAllowed="true" />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

//if(user.isDepotUser() || user.isCorporateUser()){
%>
<%--jsp:forward page="/your_account/delivery_info_avail_slots_b.jsp" /--%>
<%
//}

	String fldAddress1 = request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode());
	String fldZipCode = request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode());

    if ((fldZipCode == null) || "".equals(fldZipCode)) {
        if (user != null) {
            fldZipCode = user.getZipCode();
        }
    } if (fldZipCode == null) fldZipCode = "";
//same check for address
	if ((fldAddress1 == null) || "".equals(fldAddress1)) {
        if (user != null) {
            fldAddress1 = user.getAddress().getAddress1();
        }
    } if (fldAddress1 == null) fldAddress1 = "";

%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="693" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="2" class="title16"><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>Check Available Delivery Time Slots<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td colspan="2" class="text12">Enter your address to see available time slots for your neighborhood.</td></tr>
<tr><td colspan="2" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="24"><br><b>Enter Delivery Address</b><img src="/media_stat/images/layout/clear.gif" width="80" height="1"><span class="text11">* Required Information</span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td bgcolor="#CCCCCC" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<fd:CheckAvailableTimeslots result="result" successPage="/help/delivery_info_avail_slots.jsp" actionName="makeAddress">
<form name="address" method="POST">
<tr valign="top"><td class="text12" align="right">* Street Address&nbsp;&nbsp;</td>
<td><input type="text" class="text11" maxlength="50" size="21" name="<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>" value="<%=fldAddress1%>">
&nbsp;<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
<tr valign="top"><td class="text12" align="right"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>* Zip Code&nbsp;&nbsp;</td>
<td><input type="text"  maxlength="5" class="text11" size="6" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=fldZipCode%>">
&nbsp;<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
<tr><td>&nbsp;</td><td><input type="image" src="/media_stat/images/buttons/check_my_address.gif" height="16" width="112" name="address_check" vspace="8" value="Check My Address" border="0">
<br><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_NOT_IN_ZONE.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_CANT_GEOCODE.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='technicalDifficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
</form>
<%
	for (Iterator i=result.getErrors().iterator();i.hasNext();) {
		ActionError ae = (ActionError) i.next();
		//out.println(ae.getType() + "<br>");
	}
%>
</fd:CheckAvailableTimeslots>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="120" height="24"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="573" height="24"></td></tr>
</table>
</tmpl:put>
</tmpl:insert>
