<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL = 970;
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag pageId=""></fd:SEOMetaTag>
</tmpl:put>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>
<% 
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	double cartTotal = user.getShoppingCart().getTotal();
	boolean proceedThruCheckout = false;
%>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%= "/checkout/step_3_choose.jsp?paymentId="+ request.getParameter("paymentId")%>'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<FORM name='ccEdit' method='POST'>
<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">
<TABLE WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" VALIGN="bottom">
			<FONT CLASS="title18">PAYMENT INFO</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="395" HEIGHT="1" BORDER="0">
		</TD>
	</TR>
</TABLE>

<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0"><br />
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<TABLE WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD class="text11" WIDTH="395">
			<font class="title18">Edit Credit Card</font><br>
			Please update credit card information<BR>
	</TD>
		<TD WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL-430%>" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
	</TABLE>

<%@ include file="/includes/ckt_acct/i_edit_creditcard_fields.jspf"%>
<br><br>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>">
		<TR VALIGN="TOP">
				<TD WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" ALIGN="RIGHT">
					<a class="cssbutton green transparent small" href="<%=cancelPage%>">CANCEL</a>
						<button  class="cssbutton green small" name="checkout_credit_card_edit">SAVE CHANGES</button>
				</TD>
		</TR>
	</TABLE>
	
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>">
	<TR VALIGN="TOP">
		<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL-35%>" style="text-align: left;">
				<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Time<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
</TR>
</TABLE>
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_3_CARD_EDIT_TOTAL%>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>