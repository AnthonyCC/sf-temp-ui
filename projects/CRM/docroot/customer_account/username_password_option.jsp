<%@page import="monfox.toolkit.snmp.agent.modules.SnmpV2Mib.SysOREntry"%>
<%@page import="monfox.toolkit.snmp.agent.modules.SnmpV2Mib.SysOREntry"%>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*,com.freshdirect.delivery.sms.*,com.freshdirect.sms.*, com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<tmpl:insert template='/template/top_nav_pwdstrng.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Username & Password & Options</tmpl:put>
    <tmpl:put name='content' direct='true'>
	<crm:GetFDUser id="user">
	<crm:GetCustomerInfo id="customerInfo" user="<%=user%>">
		<crm:CrmCustomerInfoController customerInfo="<%=customerInfo%>" successPage="/main/account_details.jsp" result="result" actionName="updateCustomerInfo">
        <div class="cust_module" style="width: 60%;">
		<form method="POST" name="userInfo">
            <div class="cust_module_header">
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="cust_module_header_text">Edit Username & Password & Options</td>
                        <td width="50%"><a href="/main/account_details.jsp" class="cancel">CANCEL</a>&nbsp;&nbsp;<a href="javascript:document.userInfo.submit();" class="save">SAVE</a></td>
                        <td><fd:ErrorHandler result="<%=result%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						<td align="center" class="note">* Required</td>
                    </tr>
                </table>
            </div>
            <div class="cust_module_content">
                <table cellpadding="3" cellspacing="5" class="cust_module_text" align="center">
                    <tr>
                        <td align="right">* Username/Email Address:&nbsp;&nbsp;</td>
                        <td><input type="text" class="input_text" style="width: 200px;" name="userId" value="<%=customerInfo.getUserId()%>"><fd:ErrorHandler result="<%=result%>" name="userId" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
                    
                    <!--  Added for Password Strength Display -->
                    <tr>					
						<td align="right">* New Password:&nbsp;</td>
						<td>
							<div class="container1">		
								<div class="content-group password">
								<div class="subgroup">
									<div class="password-hinter">
										<div class="password-instructions">
											<ul>
												<li id="pw-length" class="invalid"><strong>6</strong> or more characters <strong>(Required)</strong></li>
												<li class="subhead"><strong>Make your password stronger with:</strong></li>
												<li id="pw-letter" class="invalid"><strong>1</strong> or more letters</li>
												<li id="pw-number" class="invalid"><strong>1</strong> or more numbers</li>
												<li id="pw-capital" class="invalid"><strong>1</strong> or more capital letters</li>
												<li id="pw-special" class="invalid"><strong>1</strong> or more special characters</li>
											</ul>
										</div>
									</div><!-- // .password-hinter -->
									<div>
										<input id="password1" SIZE="15" type="password" name="password" class="password" data-indicator="pwindicator" value="<%= request.getParameter("password") %>">
										<span class="case-sensitive">Passwords are case sensitive</span>
									</div>
									<!--  
									<div id="pwindicator">
										   <div class="bar"></div>
										   <div class="label"></div>
									</div>
									-->
								</div><!-- // .subgroup -->			
								</div><!-- // .content-group -->
								</div><!-- // .container -->
						</td>						
					</tr>	
					<tr>
                        <td align="right"></td>
                        <td>
                        	<div id="pwindicator">
								   <div class="bar"></div>
								   <div class="label"></div>
							</div>
                        </td>
                    </tr>					
					<!--  Added for Password Strength Display -->
                    
                    <!--
                    <tr>
                        <td align="right">* New Password:&nbsp;&nbsp;</td>
                        <td><input type="password" class="input_text" style="width: 150px;" name="password"><fd:ErrorHandler result="<%=result%>" name="password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
                    -->
                    <!--  Changed for Password Strength Display -->
					<tr>					
						<td align="right"></td>
						<td>
							<fd:ErrorHandler result="<%=result%>" name="password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						</td>	
					</tr>
					<!--  Changed for Password Strength Display -->
                    <tr>
                        <td align="right">* Repeat New Password:&nbsp;&nbsp;</td>
                        <td><input type="password" class="input_text" style="width: 150px;" name="verifyPassword"><fd:ErrorHandler result="<%=result%>" name="verifyPassword" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
                    <tr>
                        <td align="right">* Town of Birth or&nbsp;&nbsp;<br>Mother's Maiden Name:&nbsp;&nbsp;</td>
                        <td><input type="text" class="input_text" style="width: 100px;" name="passwordHint" value="<%=customerInfo.getPasswordHint()%>"><fd:ErrorHandler result="<%=result%>" name="passwordHint" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
                    <tr>
                        <td colspan="2""><br><b>Options:</b><hr class="black1px"></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" name="recieveFdNews" <%=customerInfo.isRecieveFdNews() ? "checked" : "" %>> Please include me on FD emails/newsletters</td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" name="recieveFdxNews" <%=customerInfo.isReceiveFdxNews() ? "checked" : "" %>> Please include me on FDX emails/newsletters</td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" name="textOnlyEmail" <%=customerInfo.isTextOnlyEmail() ? "checked" : "" %>> Please send me text-only email</td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" name="receiveOptinNewsletter" <%=customerInfo.isReceiveOptinNewsletter() ? "checked" : "" %>> Please include me on the President's Picks newsletter</td>
                    </tr>
					<tr>
                        <td colspan="2" align="center"><br><b>Mobile Options:</b><hr class="black1px"></td>
                    </tr>
                    <tr>
                        <td colspan="1" align="center" width="25"><br>FD</b><hr class="black1px"><td colspan="1" align="center"><br>FDX</b><hr class="black1px"></td>
                    </tr>
					<tr>
                   <tr><td align="left">Mobile number:&nbsp;<input type="text" class="input_text" style="width: 200px;" name="mobile_number" value="<%= customerInfo.getMobileNumber() != null?customerInfo.getMobileNumber().getPhone():"" %>"><fd:ErrorHandler result="<%=result%>" name="mobile_number" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                 	<td align="left">Mobile number:&nbsp;<input type="text" class="input_text" style="width: 200px;" name="fdx_mobile_number" value="<%= customerInfo.getFdxMobileNumber() != null?customerInfo.getFdxMobileNumber().getPhone():"" %>"><fd:ErrorHandler result="<%=result%>" name="fdx_mobile_number" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
						<tr>
	                        <td colspan="1"><input class="radio" type="checkbox" name="order_notices" value="Y" <%= EnumSMSAlertStatus.NONE.value().equals(customerInfo.getOrderNotices())?"":"checked"%>/>&nbsp;Order Notices</td>
	                        <td colspan="1" align="left"><input class="radio" type="checkbox" name="fdx_order_notices" value="Y" <%= EnumSMSAlertStatus.NONE.value().equals(customerInfo.getFdxOrderNotices())?"":"checked"%>/>&nbsp;Delivery Update</td>
	                    </tr>
						<tr valign="top">
							<td colspan="1"><input class="radio" type="checkbox" name="order_exceptions" value="Y" <%=EnumSMSAlertStatus.NONE.value().equals(customerInfo.getOrderExceptions())? "":"checked"%>/>&nbsp;Order Exceptions</td>
							<td colspan="1" align="left"><input class="radio" type="checkbox" name="fdx_order_exceptions" value="Y" <%= EnumSMSAlertStatus.NONE.value().equals(customerInfo.getFdxOrderExceptions())?"":"checked"%>/>&nbsp;Order Status</td>
						</tr>
						<tr valign="top">
							<td colspan="1"><input class="radio" type="checkbox" name="offers" value="Y" <%= EnumSMSAlertStatus.NONE.value().equals(customerInfo.getOffers())?"":"checked"%>/>&nbsp;Offers</td>
							<td colspan="1" align="left"><input class="radio" type="checkbox" name="fdx_offers" value="Y" <%= EnumSMSAlertStatus.NONE.value().equals(customerInfo.getFdxOffers())?"":"checked"%>/>&nbsp;FDX Offers</td>
						</tr>
					<%-- <tr valign="top">
						<td colspan="2"><input class="radio" type="checkbox" name="partner_nessages" value="Y" <%=customerInfo.getPartnerMessages().equals(EnumSMSAlertStatus.NONE.value()) ? "" : "checked"%>/>&nbsp;Partner Messages</td>
					</tr> --%>
					<tr>
                        <td colspan="2" hidden="true"><br><b>Go Green:</b><hr class="black1px"></td>
                    </tr>
					<tr valign="top">
						<td colspan="2" hidden="true"><input class="radio" type="checkbox" name="go_green" value="Y" <%=customerInfo.isGoGreen() ? "checked":""%>>I want to turn off paper statement delivery and receive my statements online.
						</td>
					</tr>
                </table><br>
            </div>
		</form>
        </div>
		<br clear="all">
		</crm:CrmCustomerInfoController>
	</crm:GetCustomerInfo>
	</crm:GetFDUser>
    </tmpl:put>
</tmpl:insert>