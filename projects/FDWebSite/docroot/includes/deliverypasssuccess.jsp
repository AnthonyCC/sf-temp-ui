<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"  %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="userDP"guestAllowed="false" recognizedAllowed="false" />
<fd:GetOrder id='orderDP' saleId='<%=(String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER)%>'>
<div class="dpn">
	<div class="dpn-success-container">
		<div class="dpn-success">
			<div class="dpn-success-header">
				<div class="dpn-success-check"><img src="/media/editorial/site_pages/deliverypass/images/large-check.svg" alt="Success check"></div>
				<p>Success!</p>
			</div>
			<div class="dpn-success-text-receipt">You have purchased a <%= orderDP.getOrderLine(0).getDescription() %>! Your selected payment method has been charged.</div>
			<div class="dpn-success-text-table">
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left"><%= orderDP.getOrderLine(0).getDescription() %></div><div class="dpn-success-text-table-right"><%= JspMethods.formatPrice(orderDP.getOrderLine(0).getPrice()) %></div></div>
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left">Total Tax</div><div class="dpn-success-text-table-right"><%= JspMethods.formatPrice(orderDP.getTaxValue()) %></div></div>
				<% if(orderDP.getTotalDiscountValue() > 0){ %>
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left"><%= orderDP.getDiscountDescription() %> </div><div class="dpn-success-text-table-right">-<%= JspMethods.formatPrice(orderDP.getTotalDiscountValue()) %></div></div>
				<% } %>
				<% if(orderDP.getTotalAppliedGCAmount() > 0){ %>
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left">Gift Card Amount to Be Applied</div><div class="dpn-success-text-table-right"><%= JspMethods.formatPrice(orderDP.getTotalAppliedGCAmount()) %></div></div>
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left">Remaining Gift Card Balance</div><div class="dpn-success-text-table-right"><%= JspMethods.formatPriceWithNegativeSign(userDP.getGiftcardsTotalBalance()) %></div></div>
				<div class="dpn-success-text-table-line"><div class="dpn-success-text-table-left">Amount to Be Charged to Your Account</div><div class="dpn-success-text-table-right"><%= JspMethods.formatPrice(orderDP.getTotal() - orderDP.getTotalAppliedGCAmount()) %></div></div>
				<% } %>
				<div class="dpn-success-text-table-line-total"><div class="dpn-success-text-table-left">Total</div><div class="dpn-success-text-table-right"><%= JspMethods.formatPrice(orderDP.getTotal()) %></div></div>
			</div>
			<div class="dpn-success-a"><a href="/" class="dpn-success-start-shopping cssbutton cssbutton-flat orange">Start Saving</a></div>
			<div class="dpn-success-setting"><a href="/your_account/delivery_pass.jsp">DeliveryPass<sup>&reg;</sup> Settings</a></div>
		</div>
	</div>
</div>
</fd:GetOrder>