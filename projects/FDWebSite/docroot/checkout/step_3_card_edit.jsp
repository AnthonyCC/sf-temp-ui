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
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
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
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="690" VALIGN="bottom">
			<FONT CLASS="title18">PAYMENT INFO</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="395" HEIGHT="1" BORDER="0">
		</TD>
	</TR>
</TABLE>

<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0"><br/>
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD class="text11" WIDTH="395">
			<font class="title18">Edit Credit Card</font><br>
			Please update credit card information<BR>
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
</TABLE>

<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf"%>
<br><br>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
		<TR VALIGN="TOP">
				<TD WIDTH="675" ALIGN="RIGHT">
					<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="19" VSPACE="4" alt="CANCEL" border="0"></a>
					<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
				</TD>
		</TR>
	</TABLE>
        
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>