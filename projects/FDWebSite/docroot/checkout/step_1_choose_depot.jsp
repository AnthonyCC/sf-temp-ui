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
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
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

<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag pageId=""></fd:SEOMetaTag>
</tmpl:put>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:DepotLoginController actionName='checkByDepotCode' successPage='/checkout/step_1_choose.jsp' result='result'>

<% String[] checkErrorType = {"depotCode", "depotAccessCode", "technical_difficulty"}; %>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %> 
</fd:ErrorHandler>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	double cartTotal = user.getShoppingCart().getTotal();
%>
<%	
	FDCartModel cart = user.getShoppingCart();
	//button include count
	int incNextButtonCount = 0;
%>
<FORM method="post" name="step1Form" id="step1Form">
	<%-- Start Header --%>
<%@ include file="/includes/i_modifyorder.jspf"  %>
<tmpl:insert template='<%= ((modifyOrderMode) ? "/includes/checkout_header_modify.jsp" : "/includes/checkout_header.jsp") %>'>
<% if(modifyOrderMode) { %>
	<tmpl:put name="ordnumb"><%= modifiedOrderNumber %></tmpl:put>
	<tmpl:put name="note"><%= modifyNote %></tmpl:put>
<% } %>
	<tmpl:put name="title">DELIVERY ADDRESS</tmpl:put>
	<tmpl:put name="delivery-fee">
		<span class="checkout-delivery-fee"><% if (FDStoreProperties.isNewFDTimeslotGridEnabled()) { %><fd:IncludeMedia name="/media/editorial/timeslots/msg_timeslots_learnmore.html"/><% } %></span>
		<%@ include file="/includes/i_cart_delivery_fee.jspf" %>
	</tmpl:put>
	<tmpl:put name="next-button"><%@ include file="/includes/i_cart_next_step_button.jspf" %></tmpl:put>
</tmpl:insert>
<!-- PROFILE HEADER -->
<% if(!modifyOrderMode) { %>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<% } %>
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>">
	<TR VALIGN="TOP">
		<TD CLASS="text12" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>" VALIGN="bottom">
		<FONT CLASS="title18">FreshDirect Depot Delivery</FONT><BR>
		FreshDirect offers depot delivery to select companies. If you are eligible for depot delivery, choose your company's name below. Then enter your access code to continue with checkout. <br>
		<br>
		If you're not eligible, <a href="/checkout/step_1_choose.jsp">click here</a> to continue with home delivery checkout.<br>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
</TABLE>
		
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>">
	<tr>
		<td align="right">* Required information</td>
	</tr>
</table>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>">
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="170" height="1" alt="" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL-175%>" height="1" alt="" border="0"></td>
</tr>
<TR VALIGN="TOP">
	<TD align="right">* Find your company name:&nbsp;</td>
	<td>
<fd:GetDepots id='depots' >
		<select STYLE="width:230px" CLASS="text12" name="depotCode">
			<option value="001">CHOOSE A DEPOT LOCATION
			<logic:iterate id="depot" collection="<%= depots %>" type="com.freshdirect.fdlogistics.model.FDDeliveryDepotModel">
				<option value="<%=depot.getDepotCode()%>" <%= depot.getDepotCode().equals(request.getParameter("depotCode")) ? "SELECTED" : "" %>><%=depot.getName()%>
			</logic:iterate>
		</select>
		<fd:ErrorHandler result='<%=result%>' name='depotCode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
		<img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0">
</fd:GetDepots>	
	</td>
</TR>
<tr>
	<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<TR VALIGN="TOP">
	<TD align="right">* Enter your access code:&nbsp;</TD>
	<td>
		<input class="text11" type="text" size="21" value="<%= request.getParameter("depotAccessCode")!=null?request.getParameter("depotAccessCode"):"" %>"  name="depotAccessCode" required="true" tabindex="1"> <fd:ErrorHandler result='<%=result%>' name='depotAccessCode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><br>
	</td>	
</TR>
<tr>
<td colspan=2 CLASS="text12">Don't know your code? Contact us at <%=user.getCustomerServiceContact()%>. If your company is not listed above, <a href="/checkout/step_1_choose.jsp">click here</a> to continue with home delivery checkout.
<br><br>
</td>
</tr>
</TABLE>	
	

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<div style="margin-bottom: 10px;">
	    <div style="float: left;">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
		</div>
		<div style="float: left; margin-left: 5px; text-align: left;">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>Your Cart
		</div>
		<div style="float: right">
				<% if(modifyOrderMode) { %><a class="imgButtonWhite cancel_updates" href="/your_account/cancel_modify_order.jsp">cancel updates</a><% } %><%@ include file="/includes/i_cart_next_step_button.jspf" %>
		</div>
		<div style="clear: both;"></div>
	</div>
	
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_1_CHOOSE_DEPOT_TOTAL%>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</FORM>
</fd:DepotLoginController>
</tmpl:put>
</tmpl:insert>
