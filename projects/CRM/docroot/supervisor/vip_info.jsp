<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUser"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="crm" prefix="crm" %>

<tmpl:insert template='/template/top_nav.jsp'> 

    <tmpl:put name='title' direct='true'>Account Details > Add/Remove VIP status</tmpl:put>

        <tmpl:put name='content' direct='true'>
            <crm:GetFDUser id="user">
            <crm:CrmVIPSettingController user="<%=user%>" result="result" actionName="updateVIPSettings" successPage="/main/account_details.jsp">
            <div class="cust_module" style="width: 60%;">
                <form name="vipForm" method="POST">
                <div class="cust_module_header">
                    <table width="100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="cust_module_header_text">Add/Remove VIP status</td>
                            <td width="60%"><a href="/main/account_details.jsp" class="cancel">CANCEL</a>&nbsp;&nbsp;<a href="javascript:document.vipForm.submit();" class="save">SAVE</a></td>
                        </tr>
                    </table>
                </div>
                <div class="cust_module_content">
                    <table width="100%" cellpadding="3" cellspacing="5" class="cust_module_text" align="center">
                        <tr>
                            <td align="center">
							<fd:ErrorHandler result="<%=result%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span><br><br></fd:ErrorHandler>
							<input type='checkbox' name="vipSetting" value="true" <%=user.getFDCustomer().getProfile().isVIPCustomer() ? "checked":""%>> <span class="sub_nav_title">VIP Customer</span> <fd:ErrorHandler result="<%=result%>" name="vipSetting" id='errorMsg'><br><span class="error"><%=errorMsg%></span><br></fd:ErrorHandler>
                            <br>
							<span class="cust_module_content_note">Check to set, uncheck to remove VIP status</span>
							</td>
                        </tr>
                    </table>
                </div>
                </form>
            </div>
            <br clear="all">
            </crm:CrmVIPSettingController>
            </crm:GetFDUser>
        </tmpl:put>
</tmpl:insert>