<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.net.*"%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL = 970;
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<fd:CheckLoginStatus id="yuzer" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="DELIVERY ADDRESS"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put> --%>
<tmpl:put name='content' direct='true'>

</tmpl:put>
</tmpl:insert>
