<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Place auto_renew Order</tmpl:put>
<% 
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); 
    
%>
<crm:GetCurrentAgent id="currentAgent">
<crm:GetErpCustomer id="customer" user="<%= user %>">

<tmpl:put name='content' direct='true'>

<fd:ModifyOrderController action='<%= request.getParameter("action") %>' orderId='' result='result' successPage='<%= "/main/order_details.jsp?orderId=" %>'>
<TABLE>
<form name="place_order" METHOD="POST" action="">
<input type="hidden" name="action" value="place_auto_renew_order">
<input type="hidden" name="payment_id" value="">
</form>
</TABLE>





    <%	boolean showEditOrderButtons = false;
		boolean showFeesSection = true;
		boolean cartMode = false; %>
	
    <%--
	<%@ include file="/includes/i_cart_details.jspf"--%>

<table width=""100% class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td class="cust_module_header_text">Payment Options</td></tr></table>
<% if (result != null) { %>
<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='delivery_pass_error' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='no_payment_selected' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
<% } %>
</fd:ModifyOrderController>
<%
	// Get user's payment methods
    List paymentMethods = customer.getPaymentMethods();
	
	int numCreditCards = 0;
	int ccNum = 0;	
	
%>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<% int cellCounter = 0;
    boolean showCCButtons = true;
    boolean showDeleteButtons = paymentMethods.size() > 1 ? true : false; %>
<%-- ------------ EDIT/DELETE CREDIT CARD ADDRESS JAVASCRIPT ------------ --%>
<script language="javascript">

    //sets the value of the hidden field named deletePaymentId
    function setDeletePaymentId(frmObj,payId) {
			if (frmObj == null) {
				frmObj = document.forms.payment;
			}
            if (frmObj["deletePaymentId"]!=null) {
                frmObj.deletePaymentId.value=payId;         
            }
            return true;
    }
	function confirmDeletePayment(frmObj,payId) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (doDelete == true) {
			if (frmObj == null) {
				frmObj = document.forms.payment;
			}
			setDeletePaymentId(frmObj, payId);
			setActionName(frmObj,'deletePaymentMethod');
			frmObj.submit();
		}
	}
    
    function setPaymentId(frmObj,payId) {
			if (frmObj == null) {
				frmObj = document.forms.place_order;
			}
            if (frmObj["payment_id"]!=null) {
                frmObj.payment_id.value=payId;         
            }
            return true;
    }

</script>
<% 	String actionName = request.getParameter("actionName");
	String returnPage = "/main/place_auto_renew_order.jsp";
	String returnParam = "returnPage=" + returnPage;
%>
<crm:CrmPaymentMethodController actionName="<%=actionName%>" result="ccResult" successPage="<%=returnPage%>">
<form NAME="payment" METHOD="POST">
    <input type="hidden" name="deletePaymentId" value="">
    <input type="hidden" name="actionName" value="">
<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border: solid 1 px #666666; border-bottom: none;"><b>Credit cards</b></td><td align="right" width="79%"><a href="/customer_account/new_credit_card.jsp?<%=returnParam%>" class="add">ADD</a></td><td width="1%"></td></tr></table>

<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%  
	if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) { 
		numCreditCards++;
	} 
%>
</logic:iterate>

<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
	<%
	if(EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())){
	%>
    <div class="cust_inner_module" style="width: 33%;<%=ccNum < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">	
	<%
            String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
			
    %>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" onClick="javascript:setPaymentId(document.forms.place_order, <%= paymentPKId %>);"> <%=ccNum + 1%></td>
                    <td><%@ include file="/includes/i_payment_select.jspf"%></td>
                </tr>
            </table>
        </div>
    </div>
        <%if(ccNum != 0 && (ccNum + 1) % 3 == 0 && ((ccNum + 1) < numCreditCards)){%>
            <br clear="all">
        <%}
		ccNum++;	
	} %>        
    </logic:iterate>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
</form>
</crm:CrmPaymentMethodController>
<br clear="all">
<table width="100%" class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td width="40%"><span class="cust_module_header_text"> 
</span></b></span></td><td width="60%"><input type="submit" class="submit" style="width: 250px;" value="PLACE THIS ORDER" onClick="javascript:document.forms['place_order'].submit();"></td></tr></table></div>


</tmpl:put>

</crm:GetErpCustomer>
</crm:GetCurrentAgent>
</tmpl:insert>