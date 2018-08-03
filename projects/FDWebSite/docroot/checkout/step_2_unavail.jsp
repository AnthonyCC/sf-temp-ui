<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import='java.util.*' %>

<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_2_UNAVAIL_TOTAL = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<potato:unavailability/>

<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Checkout - Unavailability"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Unavailability</tmpl:put> --%>
<tmpl:put name='content' direct='true'>

<div class="atpfailure">
<div class="navigationLine">
	<a class="cssbutton green" href="/">shop the store</a>
	<a class="cssbutton orange icon-arrow-right-after" href="#" data-component="atpsubmit">continue checkout</a>
</div>

<soy:render template="checkout.deliveryTime" data="${unavailabilityPotato}" />

<soy:render template="checkout.unavailableNonReplaceableLines" data="${unavailabilityPotato}" />

<soy:render template="checkout.replaceableWrapper" data="${unavailabilityPotato}" />


<%-- Deliver Passes block --%>
<c:if test="${!empty unavailabilityPotato.passes}">
<table>
		<tr valign="top"><td colspan="5" class="success13text">DeliveryPass</td></tr>
		<tr><td class="text11"colspan="5">
				We're sorry, the pass(es) below will be removed from your cart for the following reason(s).
				<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4">
		</td></tr>
		
		<c:forEach var="line" items="${unavailabilityPotato.passes}">
			<tr VALIGN="TOP">
			<td WIDTH="30">&nbsp;&nbsp;<b>${line.cartLine.quantity}</b></td>
			<td WIDTH="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-367%>">
				<b>${line.cartLine.description}</b>
				<c:if test="${!empty line.cartLine.configurationDescription}">(${line.cartLine.configurationDescription})</c:if>
			</td>
			<td WIDTH="17">&nbsp;</td>
			<td WIDTH="5">&nbsp;&nbsp;</td>
			<td WIDTH="315">
			<b>${line.description}</b>
			</td>
			</tr>
		</c:forEach>
</table>
</c:if>
<br>

<%-- Bottom line --%>
<div class="navigationLine">
	<a class="cssbutton green" href="/">shop the store</a>
	<a class="cssbutton orange icon-arrow-right-after" href="#" data-component="atpsubmit">continue checkout</a>
</div>

<form id="atpfailureform" name="atpfailure" method="POST" data-component="atpform" action="/checkout/step_2_adjust.jsp">
	<input type="checkbox" checked="checked" name="successPage" value='/checkout/step_2_check.jsp?successPage=<c:url value='${param.successPage}'/>' />
</form>

<script>
      window.FreshDirect = window.FreshDirect || {};
      window.FreshDirect.unavailability = window.FreshDirect.unavailability || {};

      window.FreshDirect.unavailability.data = <fd:ToJSON object="${unavailabilityPotato}" noHeaders="true"/>
 </script>
 </div>
 
 <%-- include submit handler JS logic --%>
 <jwr:script src="/atp.js" useRandomParam="false" />
 
</tmpl:put>
</tmpl:insert>
