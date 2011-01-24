<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.giftcard.*" %>
<%@ page import="com.freshdirect.crm.CrmAgentModel" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<%
    String orderId = request.getParameter("orderId");
%>
<tmpl:put name='title' direct='true'>Order <%= orderId%> Transactions</tmpl:put>

<tmpl:put name='content' direct='true'>
    <%@ include file="/includes/order_nav.jspf"%>
    <%
    FDOrderI order = CrmSession.getOrder(session, orderId);
    FDOrderI cart = order;
    // Remove and replace any existing RECENT_ORDER, RECENT_ORDER_NUMBER in session
    session.removeAttribute(SessionName.RECENT_ORDER);
    session.removeAttribute(SessionName.RECENT_ORDER_NUMBER);
    session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderId);
    
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    %>

    <%@ include file="/includes/order_summary.jspf"%>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
    <TR>
        <td width="17%">Time</td>
        <td width="16%">Source</td>
        <td width="17%">Amount</td>
        <td width="50%">Action <a href="javascript:popResize('/kbit/crm.jsp?show=OrderTransaction','715','940','kbit')" onmouseover="return overlib('Click for help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a></td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
    </tr>
</table>
</div>
<div class="list_content" style="height: 65%;">
<table width="100%"  cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<%
    ErpSaleModel saleModel = ((com.freshdirect.fdstore.customer.adapter.FDOrderAdapter) order).getSale();
    List txList = new ArrayList( saleModel.getTransactions() );
  	Collections.sort(txList, ErpTransactionI.TX_DATE_COMPARATOR);
	//CrmAgentModel currentAgent = CrmSession.getCurrentAgent(session);
    for (Iterator txIter = txList.iterator(); txIter.hasNext(); ) {
        ErpTransactionModel txModel = (ErpTransactionModel) txIter.next();
%>
        <tr valign="top">
            <td width="17%"><%= CCFormatter.formatDateTime(txModel.getTransactionDate()) %></td>
            <td width="16%"><%= txModel.getTransactionSource().getName() %></td>
            <td width="17%"><%= JspMethods.formatPrice(txModel.getAmount()) %></td>
            <td width="50%">
			<% 
			//TEMP solution!
			String transType = txModel.getTransactionType().getName(); 
			String quickHelp = "";
			if (transType.indexOf("Create") > -1) {
				quickHelp = "Order created, confirmation email sent: ordered items & estimated total.<br>";
			} else if (transType.indexOf("Modify") > -1) {
				quickHelp = "Order modified, confirmation email sent: ordered items & estimated total.<br>";
			} else if (transType.indexOf("Invoice") > -1) {
				quickHelp = "Order confirmed produced and packaged.<br>";
			} else if (transType.indexOf("Capture") > -1) {
				quickHelp = "Order confirmed delivered. Funds release requested.<br>";
			} else if (transType.indexOf("Settle") > -1) {
				quickHelp = "Order paid for. Auth hold released within 24-72 hrs.<br>";
			} else if (transType.indexOf("Credit") > -1) {
				quickHelp = "Store credit issued, applicable for subsequent orders.<br>";
			} else if (transType.indexOf("Cash") > -1) {
				quickHelp = "Refund issued on customer\'s Credit Card.<br>";
			} 
				quickHelp +="Click for help.";
				%>
			  <%= transType %>&nbsp;<a href="javascript:popResize('/kbit/crm.jsp?show=OrderTransaction','715','940','kbit')" onmouseover="return overlib('<%=quickHelp%>', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help" style="font-size: 6pt;">?</a>
            <% if (txModel.getTransactionType().equals(EnumTransactionType.AUTHORIZATION)) { 
                ErpAuthorizationModel auth = (ErpAuthorizationModel) txModel;
            %>
                <%= auth.getResponseCode().getName() %> : <%= auth.getDescription() %> :  AVS response = <%= auth.getAvs()%>
				<%	String cardType = "";
					if (auth.getCardType() != null && !"".equals(auth.getCardType().getFdName())) {
						cardType = auth.getCardType().getFdName();
					} else {
						// for older authoirzations that don't have their card types saved
						if (EnumPaymentMethodType.ECHECK.equals(auth.getPaymentMethodType())) {
							cardType = "ECP";
						} else {
							cardType = "CC";
						}
					}
				%>
				<br>Auth Code: <%=auth.getAuthCode()%> &nbsp;&nbsp; <%=cardType%> last 4: <%=auth.getCcNumLast4()%> : Affiliate = <%= auth.getAffiliate()%>
            <% } else if (txModel.getTransactionType().equals(EnumTransactionType.CAPTURE)) { 
                ErpCaptureModel capture = (ErpCaptureModel) txModel;
            %>
				<%	String cardType = "";
					if (capture.getCardType() != null && !"".equals(capture.getCardType().getFdName())) {
						cardType = capture.getCardType().getFdName();
					} else {
						// for older captures that don't have their card types saved
						if (EnumPaymentMethodType.ECHECK.equals(capture.getPaymentMethodType())) {
							cardType = "ECP";
						} else {
							cardType = "CC";
						}
					}
				%>
				<%=cardType%> last 4: <%=capture.getCcNumLast4()%> : Affiliate = <%= capture.getAffiliate()%>
            <% } else if (txModel.getTransactionType().equals(EnumTransactionType.PREAUTH_GIFTCARD)) { 
                ErpPreAuthGiftCardModel preAuth = (ErpPreAuthGiftCardModel) txModel;
            %>
				<%	
                    String certNum = preAuth.getCertificateNum();
                    String authCode = preAuth.getAuthCode();
                    String status = preAuth.getGcTransactionStatus().getDescription();
				%>
				certNum: <%=certNum %> Auth Code: <%=authCode%> : Status = <%= status %>
            <% } else if (txModel.getTransactionType().equals(EnumTransactionType.REVERSEAUTH_GIFTCARD)) { 
                ErpReverseAuthGiftCardModel revAuth = (ErpReverseAuthGiftCardModel) txModel;
            %>
				<%	
                    String certNum = revAuth.getCertificateNum();
                    String authCode = revAuth.getAuthCode();
                    String status = revAuth.getGcTransactionStatus().getDescription();
				%>
				certNum: <%=certNum %> Auth Code: <%=authCode%> : Status = <%= status %>
            <% } else if (txModel.getTransactionType().equals(EnumTransactionType.POSTAUTH_GIFTCARD)) { 
                ErpPostAuthGiftCardModel postAuth = (ErpPostAuthGiftCardModel) txModel;
            %>
				<%	
                    String certNum = postAuth.getCertificateNum();
                    String authCode = postAuth.getAuthCode();
                    String status = postAuth.getGcTransactionStatus().getDescription();
				%>
				certNum: <%=certNum %> Auth Code: <%=authCode%> : Status = <%= status %>
            <% } %>



            </td>
        </tr>
		<tr><td colspan="4" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
<%
    }
%>
</table>
</div>
</tmpl:put>

</tmpl:insert>
