<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

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
                    <tr>
                        <td align="right">* New Password:&nbsp;&nbsp;</td>
                        <td><input type="password" class="input_text" style="width: 150px;" name="password"><fd:ErrorHandler result="<%=result%>" name="password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                    </tr>
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
                        <td colspan="2"><input type="checkbox" name="textOnlyEmail" <%=customerInfo.isTextOnlyEmail() ? "checked" : "" %>> Please send me text-only email</td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" name="receiveOptinNewsletter" <%=customerInfo.isReceiveOptinNewsletter() ? "checked" : "" %>> Please include me on the President's Picks newsletter</td>
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