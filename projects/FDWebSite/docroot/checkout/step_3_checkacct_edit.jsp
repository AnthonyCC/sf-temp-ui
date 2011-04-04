<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Checking Account</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%= "/checkout/step_3_choose.jsp?paymentId="+ request.getParameter("paymentId")%>'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
boolean proceedThruCheckout = false;
%>
<form method="post">

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
<font class="title18">Edit Checking Account</font><br>
Learn more about how this service works.
			To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a><BR>
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
</TABLE>

<%@ include file="/includes/ckt_acct/checkacct_fields.jspf"%>
<br><br>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="693">
	<TR VALIGN="TOP">
			<TD WIDTH="693" ALIGN="RIGHT">
				<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a>
				<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
			</TD>
	</TR>
</TABLE>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
		<TR VALIGN="TOP">
				<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
						<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
				</td>
				<td width="640">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
					<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
					Delivery Time<br/>
					<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
				</td>
		</TR>
	</TABLE>

<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</form>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>