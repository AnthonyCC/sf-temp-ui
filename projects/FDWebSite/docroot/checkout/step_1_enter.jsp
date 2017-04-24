<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_1_ENTER_TOTAL = 970;
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<%

    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

//	String addressId = request.getParameter("addressId");
StringBuffer redirectString = new StringBuffer();

redirectString.append("/login/login.jsp?successPage=");
redirectString.append(request.getRequestURI());
String requestQryString = request.getQueryString();

if (requestQryString !=null && requestQryString.trim().length() > 0 ) {
    redirectString.append(URLEncoder.encode("?"+request.getQueryString()));
}
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='<%=redirectString.toString()%>' />
<%
	double cartTotal = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getTotal();
%>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Add Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckLoginStatus guestAllowed="false" redirectPage='/checkout/signup_ckt.jsp' />
<fd:RegistrationController actionName="addDeliveryAddress" result="result" successPage='/checkout/step_1_choose.jsp?addressStatus=new'>

<script>var doubleSubmitAddrAdd = false;</script>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<script>doubleSubmitAddrAdd = false;</script>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<script>doubleSubmitAddrAdd = false;</script>
</fd:ErrorHandler>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>">

	  <tr valign="top"> 
			<td CLASS="text11" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" VALIGN="bottom">
				 <FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
		         <IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" HEIGHT="1" BORDER="0">
			</td>
	    </tr>
	</TABLE>
	
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="16" border="0"><br />
	<!-- PROFILE HEADER -->
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

	<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>">
		 <tr valign="top"> 
			 <td class="text12" width="375" valign="bottom"> 
					<font class="title18">Enter Delivery Address (Step 1 of 4)</font><br/>
					<%if(user.isPickupOnly() ){%>   
						<b>Please Note: </b>Your home address is not in a FreshDirect <a href="javascript:popup('/help/delivery_zones.jsp','large');">delivery zone</a>.
						Please select one of our pickup locations to place an order.
					<%}else if(!user.isDepotUser()){%>
						Please choose a delivery address for this order.
					<%}else{%>
						Where would you like to receive this order?
					<%}%>
			</td>
		</tr>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<FORM name="address" method="post" onSubmit="doubleSubmitAddrAdd=true;">
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>">
	<TR VALIGN="MIDDLE">
		<TD WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL-200%>">
<%if(user.isDepotUser()){%>
						<img src="/media_stat/images/template/depot/home_address.gif" width="96" height="9" alt="" border="0">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
<%}else{%>
						<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="133" HEIGHT="15" border="0" alt="DELIVERY ADDRESS">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
<%}%>
		</TD>
		<TD WIDTH="300" ALIGN="RIGHT">
				<a class="cssbutton green transparent small" href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">CANCEL</a>
				<button class="cssbutton green small" name="checkout_delivery_address_add">SAVE CHANGES</button>
		</TD>
	</TR>
	</TABLE>
	
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
	
	<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
	
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>">
	<TR VALIGN="TOP">
			<TD WIDTH="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" ALIGN="RIGHT">
				<a class="cssbutton green transparent small" href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">CANCEL</a>
				<button class="cssbutton green small" name="checkout_delivery_address_add">SAVE CHANGES</button>
			</TD>
</TR>
</TABLE>

	<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" height="1" border="0"><br/>
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br/>
	
	<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>">
	    <tr valign="top">
			<td width="35" style="text-align: left;">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL-35%>" style="text-align: left;">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Your Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" alt="" width="340" height="1" border="0">
			</td>
		</tr>
	</table>

<%@ include file="/checkout/includes/i_footer_text.jspf" %>


	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_1_ENTER_TOTAL%>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>


</FORM>

	
	<script>
		$jq('input.edit_delivery_address').each( function(index, elem){
			$jq(elem).on('click', function(event) {
				if (doubleSubmitAddrAdd) { event.preventDefault(); }
			});
		});
	</script>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>