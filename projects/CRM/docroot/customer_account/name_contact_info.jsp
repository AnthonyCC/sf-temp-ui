<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Name & Contact Info</tmpl:put>

	<tmpl:put name='content' direct='true'>

		<div class="cust_module" style="width: 60%;">
		<crm:GetFDUser id="user">
		<crm:GetErpCustomer id="customer" user="<%=user%>">
		<%ErpCustomerInfoModel customerInfo = customer.getCustomerInfo();%>
		<crm:CrmCustomerController customerInfo="<%=customerInfo%>" actionName="updateCustomerInfo" result="result" successPage="/main/account_details.jsp">
		<form name="name_contact_info" method="POST">
			<div class="cust_module_header">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="cust_module_header_text">Edit Name & Contact Information</td>
						<td width="50%"><a href="/main/account_details.jsp" class="cancel">CANCEL</a>&nbsp;&nbsp;<a href="javascript:document.name_contact_info.submit();" class="save">SAVE</a></td>
						<td><fd:ErrorHandler result="<%=result%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						<td align="center" class="note">* Required</td>
					</tr>
				</table>
			</div>
			<div class="cust_module_content">
				<table cellpadding="3" cellspacing="5" class="cust_module_text" align="center">
					<tr>
						<td align="right">Title:&nbsp;&nbsp;</td>
						<td colspan="3">
							<select class="pulldown" name="title">
								<option value="">Title</option>
								<option <%="M".equalsIgnoreCase(customerInfo.getTitle()) ? "selected" : "" %>>M.</option>
								<option <%="Mr".equalsIgnoreCase(customerInfo.getTitle()) ? "selected" : "" %>>Mr.</option>
								<option <%="Ms".equalsIgnoreCase(customerInfo.getTitle()) ? "selected" : "" %>>Ms.</option>
								<option <%="Mrs".equalsIgnoreCase(customerInfo.getTitle()) ? "selected" : "" %>>Mrs.</option>
							</select>
						</td>
					</tr>
					<tr valign="bottom">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr valign="top">
						<td align="right" valign="middle">Name:&nbsp;&nbsp;</td>
						<td>* First<br><input type="text" class="input_text" style="width: 100px;" name="firstName" value="<%=customerInfo.getFirstName()%>"><fd:ErrorHandler result="<%=result%>" name="firstName" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						<td>Middle<br><input type="text" class="input_text" style="width: 80px;" name="middleName" value="<%=customerInfo.getMiddleName()%>"></td>
						<td>* Last<br><input type="text" class="input_text" style="width: 120px;" name="lastName" value="<%=customerInfo.getLastName()%>"><fd:ErrorHandler result="<%=result%>" name="lastName" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td align="right">* Home #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 100px;" name="homePhone" value="<%=customerInfo.getHomePhone() != null ? customerInfo.getHomePhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="homeExt" value="<%=customerInfo.getHomePhone() != null ? customerInfo.getHomePhone().getExtension() : "" %>"></td>
					</tr>
					<tr>
						<td align="right">Work #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 100px;" name="workPhone" value="<%=customerInfo.getBusinessPhone() != null ? customerInfo.getBusinessPhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="workExt" value="<%=customerInfo.getBusinessPhone() != null ? customerInfo.getBusinessPhone().getExtension() : "" %>"></td>
					</tr>
					<tr>
						<td align="right">Cell #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 100px;" name="cellPhone" value="<%=customerInfo.getCellPhone() != null ? customerInfo.getCellPhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="cellExt" value="<%=customerInfo.getCellPhone() != null ? customerInfo.getCellPhone().getExtension() : "" %>"></td>
					</tr>
					<tr>
						<td align="right">Alt. Email:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 180px;" name="altEmail" value="<%=customerInfo.getAlternateEmail()%>"></td>
					</tr>
					<tr>
						<td align="right">Dept/Division:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 150px;" name="workDepartment" value="<%=customerInfo.getWorkDepartment()%>"></td>
					</tr>
					<tr>
						<td align="right">* Employee Id:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 150px;" name="employeeId" value="<%=customerInfo.getEmployeeId() %>"></td>
					</tr>
				</table><br>
			</div>
			</form>
			</crm:CrmCustomerController>
			</crm:GetErpCustomer>
			</crm:GetFDUser>
		</div>
	<br clear="all">
	</tmpl:put>

</tmpl:insert>
