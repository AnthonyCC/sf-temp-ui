<%@ page import ="com.freshdirect.framework.util.NVL"%>
<%@ page import ="com.freshdirect.webapp.taglib.fdstore.SessionName"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<fd:CancelOrderFeedbackController successPage="/your_account/feedback_thank_you.jsp" result="result">
<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Order Cancelled</tmpl:put>
    <tmpl:put name='content' direct='true'>

<%
	String orderId = request.getParameter("orderId");
	String comment = NVL.apply(request.getParameter("comment"), "");
%>

<%-- error message handling here --%>
<TABLE WIDTH="675" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><TD class="text11">
<font class="title18">Order # <%= orderId %> Cancelled</font><br>
<FONT CLASS="space4pix"><BR></FONT></td>
</tr></table>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0" HSPACE="0" VSPACE="0"><BR>
<BR>
<TABLE WIDTH="675" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><TD class="text11">
<b>Your order has been cancelled. You will not receive a delivery and your account will not be charged.</b>
<BR>
<BR>
To return to Your Orders <a href="/your_account/order_history.jsp">click here</a>.
<br>
<br>
Your feedback will help us serve you better. Please let us know how you'd like to see us improve.
<BR>
<br>
* Required information

</TABLE>
		<form action="" method="post">
<TABLE WIDTH="475" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="475" class="text11">
		<FONT CLASS="text11bold">*Reason for Canceling Order</FONT><BR>
		<FONT CLASS="space2pix"><BR></FONT>
		<input type="Hidden" name="orderId" value="<%=orderId%>">

		<select name="reason" class="text11">
			<OPTION value="">PLEASE SELECT ONE:</OPTION>
			<OPTION value="dlvtime">delivery time is no longer convenient</OPTION>
		    <OPTION value="noneed">I no longer needed the items</OPTION>
		    <OPTION value="prevorder">unhappy with previous order</OPTION>
		    <OPTION value="custserv">unhappy with customer service</OPTION>
		    <OPTION value="other">other</OPTION>
		</SELECT>		<fd:ErrorHandler result="<%=result%>" name="reason" id="errorMsg"><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
		<BR>
		<BR>
		<FONT CLASS="text11bold">Comments</FONT><BR>
		<FONT CLASS="space2pix"><BR></FONT>

		<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0">
		<TR>
			<TD ALIGN="RIGHT">
			<textarea cols="30" rows="7" name="comment"><%=comment%></textarea>
			<FONT CLASS="space4pix"><BR></FONT>
			<input type="image" src="/media_stat/images/buttons/send_feedback.gif" ALT="Send Feedback" BORDER="0" WIDTH="90" HEIGHT="16" VSPACE="4">
			</TD>
		</TR>
		</TABLE>
		</form>
	</TD>
</TR>
</TABLE>
<BR>
<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="675" HEIGHT="1" BORDER="0" HSPACE="0" VSPACE="8"><BR>
<TABLE CELLPADDING="0" CELLSPACING="0" border="0" WIDTH="675">
	<TR VALIGN="MIDDLE">
		<TD WIDTH="35"><A HREF="/index.jsp"><img src="/media_stat/images/template/confirmation/arrow_green_left.gif"
			width="28" height="28" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></A></TD>
		<TD WIDTH="640"><A HREF="/index.jsp"><img src="/media_stat/images/template/confirmation/continue_shopping_text.gif"
			width="117" height="13" border="0" alt="CONTINUE SHOPPING"></A><BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT></TD>
	</TR>
</TABLE><BR>
<br>
<table>
	<tr>
		<td ><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
		<td><b>Having Problems?</b><br>
			<%@ include file="/includes/i_footer_account.jspf" %>
		</td>
	</tr>
</table>
</tmpl:put>

</tmpl:insert>
</fd:CancelOrderFeedbackController>