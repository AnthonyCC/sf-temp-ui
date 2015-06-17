<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Name & Contact Info</tmpl:put>

	<tmpl:put name='content' direct='true'>
<script src="/assets/javascript/webpurify.jQuery.js" type="text/javascript" charset="utf-8"></script>

		
		<script type="text/javascript">
			$jq(document).ready(function() {
				$jq.webpurify.init("<%=FDStoreProperties.getProfanityCheckURL()%>","<%=FDStoreProperties.getProfanityCheckPass()%>");
			});
			
			function checkForProfanity(){
				if($jq("#displayName").val().length>0)
				{
					$jq.webpurify.check( jQuery("#displayName").val(), function(isProfane){
						if(!isProfane) {
							document.name_contact_info.submit();
						} else {
							$jq("#profaneText").html("That Display Name is invalid. Please enter a different Display Name.");
							return false;
						}
					});
				}
				else
				{
					document.name_contact_info.submit();
				}
			}
		</script>
		<div class="cust_module" style="float: none;">
		<crm:GetFDUser id="user">
		<crm:GetErpCustomer id="customer" user="<%=user%>">
		<%ErpCustomerInfoModel customerInfo = customer.getCustomerInfo();%>
		<crm:CrmCustomerController customerInfo="<%=customerInfo%>" actionName="updateCustomerInfo" result="result" successPage="/main/account_details.jsp">
		<form name="name_contact_info" method="POST">
			<div class="cust_module_header" style="width: auto;">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="cust_module_header_text">Edit Name & Contact Information</td>
						<td width="50%"><a href="/main/account_details.jsp" class="cancel">CANCEL</a>&nbsp;&nbsp;<a href="javascript:void(0);" onclick="checkForProfanity();" 
						 class="save">SAVE</a></td>
						<td><fd:ErrorHandler result="<%=result%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						<td align="center" class="note">* Required</td>
					</tr>
				</table>
			</div>
			<div class="cust_module_content">
			<span class="error" id="profaneText"></span>
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
						<td>* First<br><input type="text" class="input_text" style="width: 120px;" name="firstName" value="<%=customerInfo.getFirstName()%>"><fd:ErrorHandler result="<%=result%>" name="firstName" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						<td>Middle<br><input type="text" class="input_text" style="width: 80px;" name="middleName" value="<%=customerInfo.getMiddleName()%>"></td>
						<td>* Last<br><input type="text" class="input_text" style="width: 120px;" name="lastName" value="<%=customerInfo.getLastName()%>"><fd:ErrorHandler result="<%=result%>" name="lastName" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td align="right">Home #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 120px;" name="homePhone" id="nci_homePhone" title="Home Phone" value="<%=customerInfo.getHomePhone() != null ? customerInfo.getHomePhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="homeExt" value="<%=customerInfo.getHomePhone() != null ? customerInfo.getHomePhone().getExtension() : "" %>">
								<fd:ErrorHandler result="<%=result%>" name="homePhone" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td align="right">Work #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 120px;" name="workPhone" id="nci_workPhone" title="Business Phone" value="<%=customerInfo.getBusinessPhone() != null ? customerInfo.getBusinessPhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="workExt" value="<%=customerInfo.getBusinessPhone() != null ? customerInfo.getBusinessPhone().getExtension() : "" %>">
								<fd:ErrorHandler result="<%=result%>" name="busPhone" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td align="right">Cell #:&nbsp;&nbsp;</td>
						<td><input type="text" class="input_text" style="width: 120px;" name="cellPhone" id="nci_cellPhone" title="Cell Phone" value="<%=customerInfo.getCellPhone() != null ? customerInfo.getCellPhone().getPhone() : "" %>"></td>
						<td align="right">Ext.&nbsp;&nbsp;</td>
						<td><input type="text" maxlength="5" class="input_text" style="width: 80px;" name="cellExt" value="<%=customerInfo.getCellPhone() != null ? customerInfo.getCellPhone().getExtension() : "" %>">
								<fd:ErrorHandler result="<%=result%>" name="cellPhone" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
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
					
					<tr>
						<td align="right">Industry:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 150px;" name="industry" value="<%=customerInfo.getIndustry() %>"></td>
					</tr>
					
					<tr>
						<td align="right"># of Employees:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 150px;" name="numOfEmployees" value="<%=customerInfo.getNumOfEmployees() %>"></td>
					</tr>
					
					<tr>
						<td align="right">2nd Email Address:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" class="input_text" style="width: 150px;" name="secondEmailAddress" value="<%=customerInfo.getSecondEmailAddress() %>"></td>
					</tr>
					
					
					
					<tr>
						<td align="right">Display Name:&nbsp;&nbsp;</td>
						<td colspan="3"><input type="text" maxlength="30" class="input_text" style="width: 150px;" name="displayName" id="displayName" value="<%=customerInfo.getDisplayName() %>">
						<fd:ErrorHandler result="<%=result%>" name="displayName" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
					</tr>
				</table><br>
			</div>
			</form>
			<script type="text/javascript">
			FreshDirect.PhoneValidator.register(document.getElementById("nci_homePhone"));
			FreshDirect.PhoneValidator.register(document.getElementById("nci_workPhone"));
			FreshDirect.PhoneValidator.register(document.getElementById("nci_cellPhone"));
			</script>
			</crm:CrmCustomerController>
			</crm:GetErpCustomer>
			</crm:GetFDUser>
		</div>
	<br clear="all">
	</tmpl:put>

</tmpl:insert>
