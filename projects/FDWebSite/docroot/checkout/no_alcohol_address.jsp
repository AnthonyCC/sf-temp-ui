<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.net.URLEncoder'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Age Verification for orders containing alcohol</tmpl:put>
<tmpl:put name='content' direct='true'>

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	if("POST".equals(request.getMethod())){
		user.getShoppingCart().removeAlcoholicLines();
		response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_select.jsp"));
	}
	pageContext.setAttribute("user", user);
	pageContext.setAttribute("belowMinimum", !user.isOrderMinimumMet(true));
%>

<div class="noAlcohol">
	<form id="noAlcoholForm" method="POST">
		
		<div class="navigationLine">
			<h1 class="title">Alcohol is not currently delivered to your address</h1>
			<a class="cssbutton green" href='<c:url value="/checkout/view_cart.jsp?trk=chkplc")/>'>return to cart</a>
			<c:if test="${!belowMinimum}">
				<a class="cssbutton orange icon-arrow-right-after" href="#" onclick="jQuery('#noAlcoholForm').submit()">continue checkout</a>
			</c:if>
		</div>	
	
		<div class="warningBox">
			<div class="warningTitle">Alcohol Restriction</div>
			
			<p>Unfortunately, FreshDirect does not deliver alcohol to your building. 
			You may choose a different address or continue checkout -- without the alcohol but with all of the other items in your cart.</p>
		 
		 	<c:if test="${belowMinimum}">
		 		<p><b>Because your order falls below our $${user.minimumOrderAmount} minimum when alcohol is removed, please return to your cart to add items before continuing checkout.</b></p>
		 	</c:if>
		</div>	
	
		<div class="navigationLine">
			<a class="cssbutton green" href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>">return to cart</a>			
			<c:if test="${!belowMinimum}">
				<a class="cssbutton orange icon-arrow-right-after" href="#" onclick="jQuery('#noAlcoholForm').submit()">continue checkout</a>
			</c:if>
		</div>
	</form>
</div>

</tmpl:put>
</tmpl:insert>