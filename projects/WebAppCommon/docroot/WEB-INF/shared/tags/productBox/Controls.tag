<%@ tag import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ tag import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ tag import="com.freshdirect.webapp.util.TransactionalProductImpression"%>
<%@ tag import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%@ attribute name="trackingCode" required="false" rtexprvalue="true" %>
<%@ attribute name="isFullyAvailable" required="true" rtexprvalue="true" type="Boolean" %>
<%@ attribute name="seq" required="true" rtexprvalue="true" type="Integer" %>
<%	ProductModel product = productImpression.getProductModel(); %>
<div class="grid-item-controls">
<span class="grid-item-controls-content">
<%
	String statusPlaceholderId = "instant_atc_status_" + seq;
	if (isFullyAvailable == true ) {
		String namespaceName = "instant_atc_ns_" + seq;
		String formName = "instant_atc_form_" + seq;
		String subTotalId = "instant_atc_subtotal_" + seq;
%>
<fd:TxSingleProductPricingSupport formName="<%= formName %>" namespace="<%= namespaceName %>"
		customer="<%= (FDUserI) session.getAttribute(SessionName.USER) %>" impression="<%= productImpression %>" statusPlaceholder="<%= statusPlaceholderId %>"
		subTotalPlaceholderId="<%= subTotalId %>" />
<%
		if (productImpression.isTransactional()) {
%>
<form method="get" action="#" name="<%= formName %>" onsubmit="return false;">
<input type="hidden" name="itemCount" value="1">
<fd:TxProductControl txNumber="0" namespace="<%= namespaceName %>" impression="<%= (TransactionalProductImpression) productImpression %>" setMinimumQt="true" />
<div id="<%= subTotalId %>" class="subtotal">subtotal: <span id="<%= subTotalId %>_value" class="value"></span></div>
<div><button class="addtocart" onclick="<%= namespaceName %>.instantATC();"></button></div>
</form>
<%
		} else {
			String deptId = product.getDepartment().getContentKey().getId();
%>
<button class="customize" onclick="FD_QuickBuy.showPanel('<%= deptId %>', '<%= product.getCategory().getContentKey().getId() %>', '<%= product.getContentKey().getId() %>', 'CUSTOMIZE', '<%= namespaceName %>')();"></button>
<%
		}
	} else {
%>
Sorry! Unavailable!
<%
	}
%>
</span>
<div class="grid-item-status" id="<%= statusPlaceholderId %>"></div>
</div>