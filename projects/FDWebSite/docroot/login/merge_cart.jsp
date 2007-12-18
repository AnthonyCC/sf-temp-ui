<%@ page import='com.freshdirect.fdstore.customer.FDCartModel' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<fd:MergeCartController successPage='<%= request.getParameter("successPage") %>'>
<!-- from here on, cartCurrent, cartSaved, cartMerged is set -->
<%
String queryString = request.getQueryString();
String templateToUse= "/common/template/no_nav.jsp";
int inChkOut = queryString.indexOf("checkout");

if(inChkOut != -1){
templateToUse = "/common/template/checkout_nav.jsp";
}

%>

<tmpl:insert template='<%=templateToUse%>'>
	<tmpl:put name='title' direct='true'>Merge Cart</tmpl:put>
		<tmpl:put name='content' direct='true'>


<!-- STARTS FROM HERE -->
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<form name="mergecart" action="merge_cart.jsp" method="post">
<input type="hidden" name="successPage" value="<%= request.getParameter("successPage") %>">

<tr>
	<td valign="bottom">
		<FONT CLASS="title18">You already have items in your cart from a previous visit.</FONT><BR>
		Please choose from the options below.
	
	</td>	
	<TD WIDTH="60" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/continue_orange.gif" alt="CONTINUE TO CHECKOUT" VSPACE="2" border="0">
	</TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
		<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/checkout_arrow.gif" BORDER="0" alt="GO" VSPACE="2">
	</TD>
</TR>
</TABLE>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<FONT CLASS="space10pix"><BR></FONT>

<table width="675" cellspacing="0" cellpadding="0" border="0">
<tr valign="top">

<td width="30" align="center"><input type="radio" name="chosen_cart" value="current" checked></td>
<td width="150" class="text11bold">

	<table width="150" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="150"><img src="/media_stat/images/template/site_access/option_a.gif" width="147" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="150">
		<% FDCartModel cart = cartCurrent; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	

</td>
<td width="60"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="60" HEIGHT="15" BORDER="0"></td>

<td width="30" align="center"><input type="radio" name="chosen_cart" value="database"></td>
<td width="150" class="text11bold">

	<table width="150" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="150"><img src="/media_stat/images/template/site_access/option_b.gif" width="142" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="150">
		<% cart = cartSaved; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	


</td>
<td width="60"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="60" HEIGHT="15" BORDER="0"></td>

<td width="30" align="center"><input type="radio" name="chosen_cart" value="merge"></td>
<td width="150" class="text11bold">

	<table width="150" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="150"><img src="/media_stat/images/template/site_access/option_c.gif" width="128" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="150">
		<% cart = cartMerged; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	
</td>
<td width="15">&nbsp;</td>

</tr>
</table>

<br>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="375" VALIGN="bottom">
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/continue_orange.gif" alt="CONTINUE TO CHECKOUT"  VSPACE="2" border="0"></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/checkout_arrow.gif"  BORDER="0" alt="GO" VSPACE="2"></TD>
</TR>

</form>
</TABLE>
		</tmpl:put>

</tmpl:insert>

</fd:MergeCartController>
