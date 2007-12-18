<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<html>
	<head>
		<title>Add Bad Account</title>
		<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	</head>
	<body>
		<div>
			<div class="main_nav" style="background: #FFFFFF; border-bottom: 2px #000000 solid;">
				<div class="login_header" style="padding: 2px; border-bottom: 2px #000000 solid;">
				<table>
					<tr valign="bottom">
						<td width="50%" align="left" class="login_header" style="padding-left:2px;">Add Bad Account</td>
						<td>&nbsp;</td>
						<td width="49%" align="right" style="padding-left:2px;"><a HREF="javascript:self.close();">x Close</a></td>							
					</tr>
				</table>
				</div>
			<div>
		</div>
		<crm:CrmRestrictedPaymentMethodController restrictedPaymentMethod="<%=new RestrictedPaymentMethodModel() %>" successPage="/main/close_window.jsp" result="result" actionName="add">
		<table>
			<form name="add_bad_account" method="POST">
			<script language="JavaScript">
			    function checkForm(thisForm) {
			        var okToSubmit= true;
			        var abaRouteNumber = thisForm.aba_route_number.value;
			        var accountNumber = thisForm.account_number.value;
			        
			        if(isNaN(abaRouteNumber)) {
			            alert('The ABA ROUTE NUMBER field is invalid. Please correct and try again.');
			            okToSubmit = false;
			        }
			
			        if(isNaN(accountNumber)) {
			            alert('The ACCOUNT NUMBER field is invalid. Please correct and try again.');
			            okToSubmit = false;
			        }                        
			        if (okToSubmit) {
			            thisForm.submit();
			        }
			    }    
			</script>
			<tr>
				<td><fd:ErrorHandler result="<%= result %>" name="technical_difficulty" id="error"><span class="error_detail"><%=error%></span></fd:ErrorHandler></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b>Account #:</b></td>
				<td><b>Routing #:</b></td>
				<td><b>Bank Account Type:</b></td>
			</tr>
			<tr>
		        <td><input type="text" name="account_number" size="15" maxlength="15" tabindex="1" class="text" value="<%=request.getParameter("account_number")%>">&nbsp;
		        <fd:ErrorHandler result="<%= result %>" name="account_number" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td>
		        <td><input type="text" name="aba_route_number" size="9" maxlength="9" tabindex="2" class="text" value="<%=request.getParameter("aba_route_number")%>">&nbsp;
		        <fd:ErrorHandler result="<%= result %>" name="aba_route_number" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td>
		        <td>
		            <select name="bank_account_type" required="true" class="pulldown" tabindex="3">
		                <option value=""></option>
		                            <%
		                            String bankAccountType = request.getParameter("bank_account_type");  
		                            Iterator iterBAT = EnumBankAccountType.iterator();   
		                            while (iterBAT.hasNext()) {
		                            	EnumBankAccountType bat = (EnumBankAccountType) iterBAT.next(); 
		                            %>
		                <option value="<%= bat.getName() %>" <%= (bat.getName().equals(bankAccountType))?"selected":"" %>><%= bat.getDescription() %></option>
		                            <%  } %>
		            </select>&nbsp;<fd:ErrorHandler result="<%= result %>" name="bank_account_type" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
		        </td>
			</tr>
			<tr>
				<td><b>First Name:</b></td>
				<td><b>Last Name:</b></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
		        <td><input type="text" name="first_name" size="25" maxlength="35" tabindex="4" class="text" value="<%=request.getParameter("first_name")%>">&nbsp;
		        <fd:ErrorHandler result="<%= result %>" name="first_name" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td>
		        <td><input type="text" name="last_name" size="25" maxlength="35" tabindex="5" class="text" value="<%=request.getParameter("last_name")%>">&nbsp;
		        <fd:ErrorHandler result="<%= result %>" name="last_name" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b>Reason:</b></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
		        <td>
		            <select name="reason_code" required="true" class="pulldown" tabindex="6">
		                <option value=""></option>
		                            <%
		                            String reasonCode = request.getParameter("reason_code");  
		                            Iterator iterRR = EnumRestrictionReason.iterator();   
		                            while (iterRR.hasNext()) {
		                            	EnumRestrictionReason reason = (EnumRestrictionReason) iterRR.next(); 
		                            %>
		                <option value="<%= reason.getName() %>" <%= (reason.getName().equals(reasonCode))?"selected":"" %>><%= reason.getDescription() %></option>
		                            <%  } %>
		            </select>&nbsp;<fd:ErrorHandler result="<%= result %>" name="reason_code" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;        
		        </td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>			
			<tr>
				<td><b>Note:</b></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
			<td><textarea rows="4" cols="40" name="note" tabindex="7" class="text"><%=request.getParameter("note")%></textarea><td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr></tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="submit" value="ADD ACCOUNT" class="submit" name="submit" onClick="javascript:checkForm(add_bad_account); return false;" style="width: 120px;" tabindex="6"></td>
				<td>&nbsp;</td>
			</tr>
			</form>
		</table>
		</crm:CrmRestrictedPaymentMethodController>
	</body>
</html>