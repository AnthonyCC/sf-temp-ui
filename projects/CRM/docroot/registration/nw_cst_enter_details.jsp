<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.payment.EnumBankAccountType" %>
     
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Customer > Enter Details</tmpl:put>
<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
%>
<fd:RegistrationController actionName="register" successPage='/main/account_details.jsp' result='result' fraudPage='/registration/proceed_w_caution.jsp'>
<tmpl:put name='content' direct='true'>
<form name="registration" method="POST" action="nw_cst_enter_details.jsp">
<input type="hidden" name="terms" value="true">
<table width="100%" cellpadding="0" cellspacing="2" border="0" align="CENTER" class="sub_nav">
	<TR valign="MIDDLE">
		<TD width="35%" class="sub_nav_title">&nbsp;Create New Customer: 2. Enter Details</TD>
		<TD width="45%" class="error_detail">
			<FONT class="space4pix"><BR></FONT>&nbsp;
			<%= (result.isFailure() && !result.hasError("technical_difficulty") && !result.hasError("fraud")) ? "&raquo; The marked fields contain invalid or missing data. " : "" %>
			<% if (result.isFailure()) { %><a href="#" onclick="var k=document.getElementById('detail-err-view');if(k){k.style.display = (k.style.display=='none'?'':'none');};return false;">(error list)</a><% } %>
			<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='fraud' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler>
		</TD>
		<TD width="20%" align="RIGHT"><a href="<%= response.encodeURL("/registration/nw_cst_check_zone.jsp") %>" class="cancel">CANCEL</a> </TD>
		<td width="20%" align="right"><input type="submit" class="submit" value="CREATE ACCOUNT"></td>
	</TR>
<!-- detail error list view START -->
<%
if (result.isFailure()) {
    Collection errs = result.getErrors();
    Collection wrns = result.getWarnings();
%>
    <tr id="detail-err-view" style="display:none">
       <td style="width: 100%; border: 4px solid red; padding: 6px 6px" colspan="4">
<%
    // iterate errors
    if (errs.size() > 0) {
%>
	       <b>Errors:</b><br/>
	       <table border="0" width="100%">
<%
        Iterator it = errs.iterator();
        while (it.hasNext()) {
            ActionError msg = (ActionError) it.next();
%>
                <tr>
                    <td style="vertical-align: top; color: red"><%= msg.getType() %></td>
                    <td style="width:100%" nowrap><%= msg.getDescription() %></td>
                </tr>
<%
        }
%>
            </table>
<%
    } // end of errors

    // iterate warnings
    if (wrns.size() > 0) {
%>
            <b>Warnings:</b><br/>
            <table cellspacing="0" cellpadding="0" width="100%">
<%
        Iterator it = wrns.iterator();
        while (it.hasNext()) {
            ActionWarning msg = (ActionWarning) it.next();
%>
                <tr>
                    <td style="vertical-align: top; color: red"><%= msg.getType() %></td>
                    <td style="width:100%" nowrap><%= msg.getDescription() %></td>
                </tr>
<%
        }
%>
            </table>
<%
    } // end of warnings
%>
        </td>
    </tr>
<%
}
%>
<!-- detail error list view END -->
</TABLE>
<div class="content_scroll" style="padding: 0px; height: 85%;">
<script language="JavaScript" type="text/javascript">
	function toggleUseDelivery(formObj) {
		if (formObj.useDelivery.checked == true) {
			// sync the billing and delivery addresses
			formObj.<%= AddressName.BIL_ADDRESS_1 %>.value = formObj.<%= AddressName.DLV_ADDRESS_1 %>.value;
			formObj.<%= AddressName.BIL_ADDRESS_2 %>.value = formObj.<%= AddressName.DLV_ADDRESS_2 %>.value;
			formObj.<%= AddressName.BIL_APARTMENT %>.value = formObj.<%= AddressName.DLV_APARTMENT %>.value;
			formObj.<%= AddressName.BIL_CITY %>.value = formObj.<%= AddressName.DLV_CITY %>.value;
			formObj.<%= AddressName.BIL_STATE %>.value = formObj.<%= AddressName.DLV_STATE %>.value;
			formObj.<%= AddressName.BIL_ZIPCODE %>.value = formObj.<%= AddressName.DLV_ZIPCODE %>.value;
			
			// make the billing address fields read-only
			formObj.<%= AddressName.BIL_ADDRESS_1 %>.readOnly = true;
			formObj.<%= AddressName.BIL_ADDRESS_2 %>.readOnly = true;
			formObj.<%= AddressName.BIL_APARTMENT %>.readOnly = true;
			formObj.<%= AddressName.BIL_CITY %>.readOnly = true;
			formObj.<%= AddressName.BIL_STATE %>.readOnly = true;
			formObj.<%= AddressName.BIL_ZIPCODE %>.readOnly = true;
		} else {
			// make the billing address fields writeable
			formObj.<%= AddressName.BIL_ADDRESS_1 %>.readOnly = false;
			formObj.<%= AddressName.BIL_ADDRESS_2 %>.readOnly = false;
			formObj.<%= AddressName.BIL_APARTMENT %>.readOnly = false;
			formObj.<%= AddressName.BIL_CITY %>.readOnly = false;
			formObj.<%= AddressName.BIL_STATE %>.readOnly = false;
			formObj.<%= AddressName.BIL_ZIPCODE %>.readOnly = false;
			
			// make the billing address fields what's in the params if any
			formObj.<%= AddressName.BIL_ADDRESS_1 %>.value = "<%= request.getParameter(AddressName.BIL_ADDRESS_1) %>";
			formObj.<%= AddressName.BIL_ADDRESS_2 %>.value = "<%= request.getParameter(AddressName.BIL_ADDRESS_2) %>";
			formObj.<%= AddressName.BIL_APARTMENT %>.value = "<%= request.getParameter(AddressName.BIL_APARTMENT) %>";
			formObj.<%= AddressName.BIL_CITY %>.value = "<%= request.getParameter(AddressName.BIL_CITY) %>";
			formObj.<%= AddressName.BIL_STATE %>.value = "<%= request.getParameter(AddressName.BIL_STATE) %>";
			formObj.<%= AddressName.BIL_ZIPCODE %>.value = "<%= request.getParameter(AddressName.BIL_ZIPCODE) %>";
		}
	}
	
	function updateNameFields(formObj) {
		updateAddrNames(formObj);
		updateCCName(formObj);
		updateContactNum(formObj);
	}
	
	function updateAddrNames(formObj) {
		formObj.<%= AddressName.DLV_FIRST_NAME %>.value = formObj.first_name.value;
		formObj.<%= AddressName.DLV_LAST_NAME %>.value = formObj.last_name.value;
	}
	
	function updateCCName(formObj) {
		var first_name = formObj.first_name.value;
		var last_name = formObj.last_name.value;
		formObj.<%= PaymentMethodName.ACCOUNT_HOLDER %>.value = first_name + ' ' + last_name;
	}
	
	function updateContactNum(formObj) {
		var phoneNum = formObj.homephone.value;
		formObj.<%= AddressName.DLV_HOME_PHONE %>.value = phoneNum;
	}
	
	function updateRepeatEmail(formObj) {
		var email = formObj.email.value;
		formObj.repeat_email.value = email;
	}
</script>
<TABLE width="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" align="CENTER"  class="register_header" style="padding: 3px; margin-bottom: 4px;">
	<tr>
		<td width="35%">&nbsp;&nbsp;Name &amp; Contact Info</td>
		<td width="40%">&nbsp;&nbsp;Username &amp; Password</td>
		<td width="25%"><%  if (user.isDepotUser()) { %>
            &nbsp;&nbsp;Depot Info <span class="register_header_detail">*&nbsp;Required information</span>
    <%  }   %></td>
	</tr>
</table>
<TABLE width="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" align="CENTER" class="register">
	<TR valign="TOP">
		<TD width="35%">
			<table width="99%" cellpadding="2" cellspacing="0" border="0" class="register">
				<TR>
					<td width="30%" align="right">Title:&nbsp;</td>
					<TD width="70%"><select name="title">
						<OPTION VALUE=""></OPTION>
						<OPTION VALUE="Mr." <%= "Mr.".equals(request.getParameter("title")) ? "selected" : "" %>>Mr.</OPTION>
						<OPTION VALUE="Mrs." <%= "Mrs.".equals(request.getParameter("title")) ? "selected" : "" %>>Mrs.</OPTION>
						<OPTION VALUE="Ms." <%= "Ms.".equals(request.getParameter("title")) ? "selected" : "" %>>Ms</OPTION>
						<OPTION VALUE="Dr." <%= "Dr.".equals(request.getParameter("title")) ? "selected" : "" %>>Dr.</OPTION>
						</select>
						<fd:ErrorHandler result='<%=result%>' name='title' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler>
					</TD>
				</TR>
				<TR>
					<td width="30%" align="right">*&nbsp;First:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="15" name="first_name" onChange="updateNameFields(document.registration);" value="<%= request.getParameter("first_name") %>"><fd:ErrorHandler result='<%=result%>' name='dlvfirstname' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="30%" align="right">Middle:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="15" name="middle_name" value="<%= request.getParameter("middle_name") %>"></TD>
				</TR>
				<TR>
					<td width="30%" align="right">*&nbsp;Last:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="15" name="last_name" onChange="updateNameFields(document.registration);" value="<%= request.getParameter("last_name") %>"><fd:ErrorHandler result='<%=result%>' name='dlvlastname' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="30%" align="right">*&nbsp;Home&nbsp;#:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="10" name="homephone" onChange="updateNameFields(document.registration);" value="<%= request.getParameter("homephone") %>"> Ext.&nbsp;<INPUT TYPE="text" SIZE="5" name="homephoneext" value="<%= request.getParameter("homephoneext") %>"><fd:ErrorHandler result='<%=result%>' name='ext' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='dlvhomephone' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="30%" align="right"><% if (user.isDepotUser()) out.print("*&nbsp;"); %>Work&nbsp;#:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="10" name="busphone" value="<%= request.getParameter("busphone") %>"> Ext.&nbsp;<INPUT TYPE="text" SIZE="5" name="busphoneext" value="<%= request.getParameter("busphoneext") %>"><br><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='ext' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="30%" align="right">Cell&nbsp;#:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="10" name="cellphone" value="<%= request.getParameter("cellphone") %>"> Ext.&nbsp;<INPUT TYPE="text" SIZE="5" name="cellphoneext" value="<%= request.getParameter("cellphoneext") %>"></TD>
				</TR>
				<TR>
					<td width="30%" align="right">Alt.&nbsp;Email:&nbsp;</td>
					<TD width="70%"><INPUT TYPE="text" SIZE="15" name="alt_email" value="<%= request.getParameter("alt_email") %>"><fd:ErrorHandler result='<%=result%>' name='alt_email' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
                <%  if (user.isDepotUser()) { %>
                <TR>
					<td width="35%" align="right">*&nbsp;Dept/Division:&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" SIZE="15" name="workDepartment" value="<%=request.getParameter("workDepartment")%>"><fd:ErrorHandler result='<%=result%>' name='workDepartment' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
                <%      com.freshdirect.delivery.depot.DlvDepotModel depot = FDDepotManager.getInstance().getDepot(user.getDepotCode());
                        if (depot.getRequireEmployeeId()) { %>
                <TR>
					<td width="35%" align="right">*&nbsp;Employee Id:&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" SIZE="15" name="employeeId" value="<%=request.getParameter("employeeId")%>"><fd:ErrorHandler result='<%=result%>' name='employeeId' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
                <%      }
                    }   %>
			</TABLE>
		</TD>
		<TD width="40%">
			<table width="99%" cellpadding="2" cellspacing="0" border="0" class="register">
				<TR>
					<td width="40%" align="right">* Username/Email Address:&nbsp;</td>
					<TD width="60%"><INPUT TYPE="text" SIZE="15" name="email" value="<%= request.getParameter(EnumUserInfoName.EMAIL.getCode()) %>"  onChange="updateRepeatEmail(document.registration);"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
					<INPUT type="hidden" name="repeat_email" value="<%= request.getParameter("email") %>">
				</TR>
				<TR>
					<td width="40%" align="right">* New Password:&nbsp;</td>
					<TD width="60%"><INPUT SIZE="15" type="password" name="password" value="<%= request.getParameter("password") %>"><fd:ErrorHandler result='<%=result%>' name='password' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="40%" align="right">* Repeat New Password:&nbsp;<BR><BR></td>
					<TD width="60%"><INPUT SIZE="15" type="password" name="repeat_password" value="<%= request.getParameter("repeat_password") %>"><fd:ErrorHandler result='<%=result%>' name='repeat_password' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler><BR><font class="note"><i>Must be at least 4 characters. Case sensitive.</i></font><br></TD>
				</TR>
				<TR>
					<td width="40%" align="right">Town of Birth or&nbsp;<br>Mother's Maiden Name:&nbsp;</td>
					<TD width="60%"><INPUT TYPE="text" SIZE="15" name="password_hint" value="<%= request.getParameter("password_hint") %>"><fd:ErrorHandler result='<%=result%>' name='password_hint' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
			</TABLE>
		</TD>
		<TD width="25%">
        <% if (user.isDepotUser()) { %>
			<table width="99%" cellpadding="2" cellspacing="0" border="0" class="register">
				<TR>
					<td width="30%" align="right">Company:&nbsp;</td>
					<TD width="70%">
                        <fd:GetDepots id="depots">
                        <logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
                        <%  if (depot.getDepotCode().equals(user.getDepotCode())) { out.print(depot.getName()); }   %>
                        </logic:iterate>
                        </fd:GetDepots>
                    </TD>
				</TR>
			</TABLE>
        <%  } %>
		</TD>
	</TR>
</TABLE>
<br>
<table width="100%" cellpadding="0" cellspacing="0" border="0" align="CENTER"  class="register_header" style="padding: 3px; margin-bottom: 8px;">
<tr><td>&nbsp;&nbsp;Delivery Address <span class="register_header_detail">*&nbsp;Required information</span></td></tr>
</table>
<%	
	String dlvFirstName = request.getParameter(AddressName.DLV_FIRST_NAME);
	String dlvLastName = request.getParameter(AddressName.DLV_LAST_NAME);

	AddressModel adr = user.getAddress();
	String fldAddress1 = adr.getAddress1();
	if (request.getParameter(AddressName.DLV_ADDRESS_1) != null)
		fldAddress1 = request.getParameter(AddressName.DLV_ADDRESS_1);
	String fldAddress2 = adr.getAddress2();
	if (request.getParameter(AddressName.DLV_ADDRESS_2) != null)
		fldAddress2 = request.getParameter(AddressName.DLV_ADDRESS_2);
	String fldApartment = adr.getApartment();
	if (request.getParameter(AddressName.DLV_APARTMENT) != null)
		fldApartment = request.getParameter(AddressName.DLV_APARTMENT);
	String fldCity = adr.getCity();
	if (request.getParameter(AddressName.DLV_CITY) != null)
		fldCity = request.getParameter(AddressName.DLV_CITY);
	String fldZipCode = adr.getZipCode();
	if (request.getParameter(AddressName.DLV_ZIPCODE) != null)
		fldZipCode = request.getParameter(AddressName.DLV_ZIPCODE);
	String fldState = adr.getState();
	if (request.getParameter(AddressName.DLV_STATE) != null)
		fldState = request.getParameter(AddressName.DLV_STATE);
        String fldCompanyName = adr.getCompanyName();
	if (request.getParameter(AddressName.DLV_COMPANY_NAME) != null)
		fldCompanyName = request.getParameter(AddressName.DLV_COMPANY_NAME);        
%>

<fd:ErrorHandler result='<%=result%>' name='<%=AddressName.DLV_ADDRESS_SUGGEST%>' id='errorMsg'>
	<%
String	addressMessage = "";
ArrayList suggestions = null;   // a holder for suggested addresses if the original address is not unique
FDDeliveryManager dlvManager = FDDeliveryManager.getInstance();
EnumAddressVerificationResult addressResult = null;
AddressModel address = new AddressModel();

address.setAddress1(fldAddress1);
address.setAddress2(fldAddress2);
address.setApartment(fldApartment);
address.setCity(fldCity);
address.setState(fldState);
address.setZipCode(fldZipCode);

suggestions = dlvManager.findSuggestionsForAmbiguousAddress(address);
%>
<script>
function fillAddress(arg_address, arg_apt){
	document.forms['address'].<%= EnumUserInfoName.DLV_ADDRESS_1.getCode() %>.value = arg_address;
}
</script>


	<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0">
	<TR VALIGN="MIDDLE">
		<td colspan="4" class="error_detail">
	
		<%=addressMessage = result.getError(EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode()).getDescription()%>

		<%  if (suggestions != null) {  %>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
			<%      for (Iterator sIter = suggestions.iterator(); sIter.hasNext(); ) {
			            AddressModel suggestion = (AddressModel) sIter.next();  %>
			    <tr>
					<td class="error_detail">
			        	<img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"><a href="javascript:fillAddress('<%= suggestion.getAddress1() %>', '<% if (!"".equals(suggestion.getApartment())) { %> <%= suggestion.getApartment() %> <%}%>' )"><%= suggestion.getAddress1() %> <% if ( suggestion.getApartment() != null && !"".equals(suggestion.getApartment())) { %>Apt # <%= suggestion.getApartment() %><% } %></a>
			    	</td>
				</tr>
			<%      }   %>
			</table>
		<%  }   %>
	
		</TD>
	</tr>
	</table>
	<br>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' name='<%=AddressName.DLV_NOT_IN_ZONE%>' id='errorMsg'>
	<TABLE WIDTH="98%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="MIDDLE">
		<TD colspan="4" class="error_detail">
        <%= SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS %>
        </TD>
    </TR>
    </TABLE>
</fd:ErrorHandler>

			<TABLE width="80%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="register">
			<tr valign="top"><td width="40%">
			<TABLE width="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="register">
                                <tr>
                                    <td valign="top" align="right">Address Type:&nbsp;&nbsp;</td>
                                    <td colspan='2'>
                                        <%EnumServiceType selected = request.getParameter(AddressName.DLV_SERVICE_TYPE) != null ? EnumServiceType.getEnum(request.getParameter(AddressName.DLV_SERVICE_TYPE)) : EnumServiceType.HOME ; %>
                                        <input type="radio" name="<%=AddressName.DLV_SERVICE_TYPE%>" value="<%=EnumServiceType.HOME.getName()%>" <%= EnumServiceType.HOME.equals(selected) ? "CHECKED" : "" %>>&nbsp;&nbsp;Residential</br>
                                        <input type="radio" name="<%=AddressName.DLV_SERVICE_TYPE%>" value="<%=EnumServiceType.CORPORATE.getName()%>" <%= EnumServiceType.CORPORATE.equals(selected) ? "CHECKED" : "" %>>&nbsp;&nbsp;Commercial<br>
                                        <fd:ErrorHandler result="<%=result%>" name="<%=AddressName.DLV_SERVICE_TYPE%>" id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
                                    </td>
                                </tr>
                                <TR>
					<td width="35%" align="right">* First Name:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="15" name="<%= AddressName.DLV_FIRST_NAME %>" value="<%= dlvFirstName %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_FIRST_NAME %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Last Name:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="15" name="<%= AddressName.DLV_LAST_NAME %>" value="<%= dlvLastName %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_LAST_NAME %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
                                <TR>
					<td width="35%" align="right">Company Name:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="15" name="<%= AddressName.DLV_COMPANY_NAME %>" value="<%= fldCompanyName %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_COMPANY_NAME %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Address Line 1:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="15" name="<%= AddressName.DLV_ADDRESS_1 %>" value="<%= fldAddress1 %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_ADDRESS_1 %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">Address Line 2:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="10" name="<%= AddressName.DLV_ADDRESS_2 %>" value="<%= fldAddress2 %>"></TD>
				</TR>
				<TR>
					<td width="35%" align="right">Apt./Floor:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="5" name="<%= AddressName.DLV_APARTMENT %>" value="<%= fldApartment %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_APARTMENT %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* City:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="10" name="<%= AddressName.DLV_CITY %>" value="<%= fldCity %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_CITY %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* State:&nbsp;</td>
					<TD width="65%">
					<select class="pulldown" name="<%=AddressName.DLV_STATE%>">
							<option value="NY" <%="NY".equalsIgnoreCase(fldState) ? "selected" : "" %>>NY</option>
							<option value="NJ" <%="NJ".equalsIgnoreCase(fldState) ? "selected" : "" %>>NJ</option>
							<option value="CT" <%="CT".equalsIgnoreCase(fldState) ? "selected" : "" %>>CT</option>
					</select>
					&nbsp;&nbsp;&nbsp;* Zip <INPUT TYPE="text" SIZE="5" name="<%= AddressName.DLV_ZIPCODE %>" value="<%= fldZipCode %>"><br><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.DLV_ZIPCODE %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Contact #:&nbsp;</td>
					<TD width="65%" colspan="3"><INPUT TYPE="text" SIZE="15" name="<%= AddressName.DLV_HOME_PHONE %>" value="<%=request.getParameter(AddressName.DLV_HOME_PHONE)%>"></TD>
				</TR>
				</table>
				</td>
				<input type="hidden" name="country" value="US">
				<td width="40%">
					<% if (!user.isDepotUser()) { %>
                    <TABLE width="93%" CELLPADDING="0" CELLSPACING="0" BORDER="0" align="CENTER" class="register">
						<TR>
							<td>Special Delivery instructions (optional):<BR>
							<TEXTAREA ROWS="4" COLS="30" WRAP="virtual" name="<%= AddressName.DLV_DELIVERY_INSTRUCTIONS %>"><%= request.getParameter(AddressName.DLV_DELIVERY_INSTRUCTIONS) %></TEXTAREA></TD>
						</TR>
						<tr>
							<td><br>Alternate Delivery</td>
						</tr>
						<tr>
							<td><input type="radio" name="<%=AddressName.DLV_ALTERNATE_DELIVERY%>" value="<%=EnumDeliverySetting.NONE.getDeliveryCode()%>" <%=EnumDeliverySetting.NONE.equals(request.getParameter(AddressName.DLV_ALTERNATE_DELIVERY)) || !EnumDeliverySetting.DOORMAN.equals(request.getParameter(AddressName.DLV_ALTERNATE_DELIVERY)) ? "checked" : ""%>> None</td>
						</tr>
						<tr>
							<td><input type="radio" name="<%=AddressName.DLV_ALTERNATE_DELIVERY%>" value="<%=EnumDeliverySetting.DOORMAN.getDeliveryCode()%>" <%=EnumDeliverySetting.DOORMAN.equals(request.getParameter(AddressName.DLV_ALTERNATE_DELIVERY)) ? "checked" : ""%>> Doorman</td>
						</tr>
					</TABLE>
                <%  }   %>
				</td>
				</tr>
				</TABLE><BR>

<% if (user.isDepotUser()) { %>
<table width="100%" cellpadding="0" cellspacing="0" border="0" align="CENTER"  class="register_header" style="padding: 3px; margin-bottom: 8px;"><tr><td>&nbsp;&nbsp;Depot Delivery Address</td></tr></table>
<TABLE width="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" align="CENTER" class="register">
    <tr valign="top">
	<td width="2%"></td>
    <fd:GetDepotLocations id='locations' depotCode='<%=user.getDepotCode()%>'>
        <%  Date now = new Date();  %>
        <logic:iterate id="location" collection="<%= locations %>" type="com.freshdirect.delivery.depot.DlvLocationModel" indexId="count">
        <%  
            boolean checkedAddress = ("DEPOT_"+location.getPK().getId()).equalsIgnoreCase(request.getParameter("selectAddressList"));
            //
            // skip locations that aren't ready yet or have expired                                          
            //
            if (now.before(location.getStartDate()) || now.after(location.getEndDate())) continue;
        %>
        <td>
            <TABLE width="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" align="CENTER" class="register">
                <tr>
                    <TD width="5%" align="right" valign="top"><input type="radio" name="selectAddressList" class="radio" value="DEPOT_<%=location.getPK().getId()%>" <%=checkedAddress?"checked":"" %>>&nbsp;</TD>
                    <TD valign="top" width="95%">
                        <font class="text12bold"><%=location.getFacility()%></font><br>
                        <%=location.getAddress().getAddress1()%><br>
                        <% if(location.getAddress().getAddress2()!= null ){%>
                            <%=location.getAddress().getAddress2()%><br>
                        <%}%>
                        <%=location.getAddress().getCity()%> <%=location.getAddress().getState()%>, <%=location.getAddress().getZipCode()%><br>
                        <br>
                        <%=location.getInstructions()%><br>
                    </td>
                </tr>
            </table>
        </td>
        </logic:iterate>
    </fd:GetDepotLocations>
    </tr>
</table>
<%  } %>
<br>
<table width="100%" cellpadding="0" cellspacing="0" border="0" align="CENTER"  class="register_header" style="padding: 3px; margin-bottom: 8px;"><tr><td>&nbsp;&nbsp;Payment Information: <span class="register_header_detail">(Optional)</span></td></tr></table>
<div style="width: 40%; float: left;">
			<table width="90%" cellpadding="1" cellspacing="0" border="0" class="register">
				<tr><td align="right"><span class="module_header_text">Credit Card</span><br></td><td></td></tr>
				<TR>
					<td width="35%" align="right">* Name on card:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" SIZE="20" name="<%= PaymentMethodName.ACCOUNT_HOLDER %>" value="<%= request.getParameter(PaymentMethodName.ACCOUNT_HOLDER) %>"><fd:ErrorHandler result='<%=result%>' name='<%= PaymentMethodName.ACCOUNT_HOLDER %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Card type:&nbsp;&nbsp;</td>
					<TD width="65%">
						<SELECT name="<%= PaymentMethodName.CARD_BRAND %>">
<%
	java.util.List ccTypes = com.freshdirect.common.customer.EnumCardType.getCardTypes();
	for (Iterator ccItr = ccTypes.iterator(); ccItr.hasNext(); ) {
	    EnumCardType ccType = (EnumCardType)ccItr.next();
%>
							<OPTION value="<%=ccType.getFdName()%>" <%= ( ccType.getFdName().equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_BRAND) ) ) ? "SELECTED" : "nbsp" %>><%=ccType.getFdName()%></OPTION>
<%	} %>
						</SELECT><fd:ErrorHandler result='<%=result%>' name='<%= PaymentMethodName.CARD_BRAND %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler>
					</TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Card number:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" SIZE="20" name="<%= PaymentMethodName.ACCOUNT_NUMBER %>" value="<%= request.getParameter(PaymentMethodName.ACCOUNT_NUMBER) %>"><fd:ErrorHandler result='<%=result%>' name='<%= PaymentMethodName.ACCOUNT_NUMBER %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='payment_method_fraud' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Expires:&nbsp;&nbsp;</td>
					<TD width="65%">
						<SELECT name="<%= PaymentMethodName.CARD_EXP_MONTH %>">
							<option value="">Choose a month</option>
							<option value="01" <%= ( "01".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>January</option>
							<option value="02" <%= ( "02".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>February</option>
							<option value="03" <%= ( "03".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>March</option>
							<option value="04" <%= ( "04".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>April</option>
							<option value="05" <%= ( "05".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>May</option>
							<option value="06" <%= ( "06".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>June</option>
							<option value="07" <%= ( "07".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>July</option>
							<option value="08" <%= ( "08".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>August</option>
							<option value="09" <%= ( "09".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>September</option>
							<option value="10" <%= ( "10".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>October</option>
							<option value="11" <%= ( "11".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>November</option>
							<option value="12" <%= ( "12".equalsIgnoreCase( request.getParameter(PaymentMethodName.CARD_EXP_MONTH) ) ) ? "SELECTED" : "" %>>December</option>
						</SELECT>&nbsp;/&nbsp;
						<SELECT name="<%= PaymentMethodName.CARD_EXP_YEAR %>">
							<OPTION value="">Choose a year</OPTION>
<%
	int startYear = Calendar.getInstance().get(Calendar.YEAR);
	int endYear = startYear+10;
	for (int yr = startYear; yr < endYear; yr++) {
%>
							<OPTION VALUE="<%= yr %>" <%= ( (""+yr).equalsIgnoreCase( request.getParameter( PaymentMethodName.CARD_EXP_YEAR) ) ) ? "SELECTED" : "nbsp" %>><%= yr %></OPTION>
<%	} %>
						</SELECT><fd:ErrorHandler result='<%=result%>' name='expiration' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler>
					</TD>
				</TR>
				<TR>
					<TD width="35%" align="RIGHT"><b>Billing Address</b>&nbsp;&nbsp;</TD>
					<TD width="65%"><INPUT TYPE="checkbox" name="useDelivery" onClick="toggleUseDelivery(document.registration)" <%= request.getParameter("useDelivery") == null ? "" : "checked" %>>Use delivery address</TD>
				</TR>
				<TR>
					<td width="35%" align="right">* Address line 1:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" readOnly="false" SIZE="20" name="<%= AddressName.BIL_ADDRESS_1 %>" value="<%= request.getParameter(AddressName.BIL_ADDRESS_1) %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.BIL_ADDRESS_1 %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">Address line 2:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" readOnly="false" SIZE="20" name="<%= AddressName.BIL_ADDRESS_2 %>" value="<%= request.getParameter(AddressName.BIL_ADDRESS_2) %>"></TD>
				</TR>
				<TR>
					<td width="35%" align="right">Apt./Floor:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" readOnly="false" SIZE="6" name="<%= AddressName.BIL_APARTMENT %>" value="<%= request.getParameter(AddressName.BIL_APARTMENT) %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.BIL_APARTMENT %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* City:&nbsp;&nbsp;</td>
					<TD width="65%"><INPUT TYPE="text" readOnly="false" SIZE="20" name="<%= AddressName.BIL_CITY %>" value="<%= request.getParameter(AddressName.BIL_CITY) %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.BIL_CITY %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<td width="35%" align="right">* State:&nbsp;&nbsp;</td>
					<TD width="65%"><input type="text" readOnly="false" size="2" name="<%= AddressName.BIL_STATE %>" value="NY"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.BIL_STATE %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler>&nbsp;&nbsp;&nbsp;* Zip <INPUT TYPE="text" SIZE="6" name="<%= AddressName.BIL_ZIPCODE %>" value="<%= request.getParameter(AddressName.BIL_ZIPCODE) %>"><fd:ErrorHandler result='<%=result%>' name='<%= AddressName.BIL_ZIPCODE %>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></TD>
				</TR>
			</TABLE><br>
</div>
			<%--CHECKING ACCT ADD--%>
			<% if (false) { // turn off for now  %>
			<div style="width: 40%; float: left;">
			<table width="90%" cellpadding="1" cellspacing="0" border="0" class="register">
				<tr>
					<td colspan="2"><span class="module_header_text" style="padding: 0px;">Checking Account</span><br>
						Addition of this method upon registration is limited to:<br>
						- Corporate customers<br>
						- VIP/Special Interest customers<br>
						<textarea rows="4" style="width: 350px;" wrap="virtual">Agreement text Agreement textAgreement text Agreement text Agreement text Agreement text Agreement text Agreement text</textarea>
						<br>
						<input type="checkbox" name="checking_account_terms_agreement"> <b>I have read these terms clearly to the customer and<br>customer has verbally agreed to them.</b><span class="space4pix"><br><br></span>
					</td>
				</tr>
				<tr>
					<td width="35%" align="right">* Name on account:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" SIZE="20" name="<%=PaymentMethodName.ACCOUNT_HOLDER%>" value="<%= request.getParameter(PaymentMethodName.ACCOUNT_HOLDER) %>"><fd:ErrorHandler result='<%=result%>' name='<%=PaymentMethodName.ACCOUNT_HOLDER%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr valign="top">
					<td width="35%" align="right">* Account type:</td>
					<td width="65%">
			<%
					Iterator iter = EnumBankAccountType.iterator();
					while (iter.hasNext()) {
						EnumBankAccountType bankAccountType = (EnumBankAccountType) iter.next();
			%>
					<input type="radio" name="<%=PaymentMethodName.BANK_ACCOUNT_TYPE%>" value="<%=bankAccountType.getName()%>" <%=(bankAccountType.getName().equals(request.getParameter(PaymentMethodName.BANK_ACCOUNT_TYPE))) ? "CHECKED" : ""%>><%=bankAccountType.getDescription()%><br>
			<% 		
					} 
			%>
				<fd:ErrorHandler result='<%=result%>' name='<%=PaymentMethodName.BANK_ACCOUNT_TYPE%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">* Account number:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" SIZE="20" name="<%=PaymentMethodName.ACCOUNT_NUMBER%>" value="<%=request.getParameter(PaymentMethodName.ACCOUNT_NUMBER)%>"><fd:ErrorHandler result='<%=result%>' name='<%=PaymentMethodName.ACCOUNT_NUMBER%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='credit_card_fraud' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">* Routing/Transit number:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" SIZE="20" name="<%=PaymentMethodName.ABA_ROUTE_NUMBER%>" value="<%= request.getParameter(PaymentMethodName.ABA_ROUTE_NUMBER) %>"><fd:ErrorHandler result='<%=result%>' name='<%=PaymentMethodName.ABA_ROUTE_NUMBER%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='credit_card_fraud' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">* Bank name:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" SIZE="20" name="<%=PaymentMethodName.BANK_NAME%>" value="<%= request.getParameter(PaymentMethodName.BANK_NAME) %>"><fd:ErrorHandler result='<%=result%>' name='<%=PaymentMethodName.BANK_NAME%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span><br></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='credit_card_fraud' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right"><b>Bank account address</b></td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="checkbox" name="useDelivery" onClick="toggleUseDelivery(document.registration)" <%= request.getParameter("useDelivery") == null ? "" : "checked" %>>Use delivery address</td>
				</tr>
				<tr>
					<td width="35%" align="right">* Address line 1:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" readOnly="false" SIZE="20" name="<%=EnumUserInfoName.BIL_ADDRESS_1.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_ADDRESS_1.getCode())%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.BIL_ADDRESS_1.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">Address line 2:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" readOnly="false" SIZE="20" name="<%=EnumUserInfoName.BIL_ADDRESS_2.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_ADDRESS_2.getCode())%>"></td>
				</tr>
				<tr>
					<td width="35%" align="right">Apt./Floor:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" readOnly="false" SIZE="6" name="<%=EnumUserInfoName.BIL_APARTMENT.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_APARTMENT.getCode())%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.BIL_APARTMENT.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">* City:</td>
					<td width="65%">&nbsp;&nbsp;<INPUT TYPE="text" readOnly="false" SIZE="20" name="<%=EnumUserInfoName.BIL_CITY.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_CITY.getCode())%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.BIL_CITY.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td width="35%" align="right">* State:</td>
					<td width="65%">&nbsp;&nbsp;<input type="text" readOnly="false" size="2" name="<%=EnumUserInfoName.BIL_STATE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_STATE.getCode())%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.BIL_STATE.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler>&nbsp;&nbsp;&nbsp;* Zip <INPUT TYPE="text" SIZE="6" name="<%=EnumUserInfoName.BIL_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.BIL_ZIPCODE.getCode())%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.BIL_ZIPCODE.getCode()%>' id='errorMsg'><span class="error_detail"><%= errorMsg %></span></fd:ErrorHandler></td>
				</tr>
			</table><br>
			</div>
			<% } %>
			<%--CHECKING ACCT ADD !! REMOVE DISPLAY STYLE !!--%>
			
	</div>
</tmpl:put>

</form>

</fd:RegistrationController>

</tmpl:insert>
