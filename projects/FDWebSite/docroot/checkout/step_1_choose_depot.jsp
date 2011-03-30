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

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<fd:CheckLoginStatus id="yuzer" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
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

<%@ include file="/includes/i_modifyorder.jspf" %>


<FORM method="post">
	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top"> 
			<td CLASS="text11" WIDTH="395" VALIGN="bottom">
				<img src="/media_stat/images/navigation/delivery_address_top.gif" WIDTH="143" HEIGHT="16" border="0" alt="DELIVERY ADDRESS">
			</td>
			<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10">
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left"  style="color:#666666;font-weight:bold;">Delivery Charge:</td>
							<td align="right"  style="color:#666666;font-weight:bold;">
								<%	
									FDCartModel cart = user.getShoppingCart();
									String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge() );
									if(cart.isDlvPassApplied()) {
								%>
									<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
									
								<%	} else if (cart.isDeliveryChargeWaived()) {
										if((int)cart.getDeliverySurcharge() == 0){
								%>     
										Free! 
										<% }else{ %> Free!(<%= dlvCharge %> waived)<% } %>
												
								<%  } else {%>
										<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
								<%}%>
						</td>
						</tr>
						<tr>
							<td align="left"  style="color:#666666;font-weight:bold;"><A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated</A> Total:</td>
							<td align="right"  style="color:#666666;font-weight:bold;"><%= currencyFormatter.format(cart.getTotal()) %></td>
						</tr>
					</table>
			</TD>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
	            <input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_right.gif" border="0" alt="CONTINUE CHECKOUT" vspace="2">
	        </td>
	    </tr>
	</table>

<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0"><br/>
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
	<TR VALIGN="TOP">
		<TD CLASS="text12" WIDTH="675" VALIGN="bottom">
		<FONT CLASS="title18">FreshDirect Depot Delivery</FONT><BR>
		FreshDirect offers depot delivery to select companies. If you are eligible for depot delivery, choose your company's name below. Then enter your access code to continue with checkout. <br>
		<br>
		If you're not eligible, <a href="/checkout/step_1_choose.jsp">click here</a> to continue with home delivery checkout.<br>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
</TABLE>
		
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<tr>
		<td align="right">* Required information</td>
	</tr>
</table>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="170" height="1" alt="" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="510" height="1" alt="" border="0"></td>
</tr>
<TR VALIGN="TOP">
	<TD align="right">* Find your company name:&nbsp;</td>
	<td>
<fd:GetDepots id='depots' >
		<select STYLE="width:230px" CLASS="text12" name="depotCode">
			<option value="001">CHOOSE A DEPOT LOCATION
			<logic:iterate id="depot" collection="<%= depots %>" type="com.freshdirect.delivery.depot.DlvDepotModel">
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
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top">
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				View Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Delivery Time<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0">
			</td>
	    </tr>
	</table>
	
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="675" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</FORM>
</fd:DepotLoginController>
</tmpl:put>
</tmpl:insert>