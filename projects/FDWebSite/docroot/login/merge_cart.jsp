<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDCartModel' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ include file="/common/template/includes/i_javascripts.jspf" %>  
<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>

<% //expanded page dimensions
final int W_MERGE_CART_TOTAL = 970;
final int W_MERGE_CART_RADIO = 30;
final int W_MERGE_CART_COL = 250;
final int W_MERGE_CART_PADDING = 60;
final int W_MERGE_CART_PADDING_RIGHT = 10;
Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");

%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<fd:MergeCartController successPage='<%= request.getParameter("successPage") %>'>
<%
// from here on, cartCurrent, cartSaved, cartMerged is set
String queryString = request.getQueryString();
String templateToUse= "/common/template/no_nav.jsp";
int inChkOut = queryString.indexOf("checkout");

if(inChkOut != -1){
templateToUse = "/common/template/checkout_nav.jsp"; //EXPANDED_PAGE_VERIFY - should be ok, when this template is widened
}

%>

<tmpl:insert template='<%=templateToUse%>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Merge Cart"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Merge Cart</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>

<%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ System.out.println("555555555555"+fdTcAgree);%>
			<script type="text/javascript">
			
			doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>')

			</script>
<%}%>
<!-- STARTS FROM HERE -->
<form name="mergecart" action="merge_cart.jsp" method="post">
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_MERGE_CART_TOTAL%>">
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
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="4" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%=W_MERGE_CART_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<FONT CLASS="space10pix"><BR></FONT>

<table width="<%=W_MERGE_CART_TOTAL%>" cellspacing="0" cellpadding="0" border="0">
<tr valign="top">

<td width="<%=W_MERGE_CART_RADIO%>" align="center"><input type="radio" name="chosen_cart" value="merge" checked></td>
<td width="<%=W_MERGE_CART_COL%>" class="text11bold">

	<table width="<%=W_MERGE_CART_COL%>" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="<%=W_MERGE_CART_COL%>"><img src="/media_stat/images/template/site_access/option_a.gif" width="128" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="<%=W_MERGE_CART_COL%>">
		<% FDCartModel cart = cartMerged; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	
</td>
<td width="<%=W_MERGE_CART_PADDING%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_MERGE_CART_PADDING%>" HEIGHT="15" BORDER="0"></td>



<td width="<%=W_MERGE_CART_RADIO%>" align="center"><input type="radio" name="chosen_cart" value="current"></td>
<td width="<%=W_MERGE_CART_COL%>" class="text11bold">

	<table width="<%=W_MERGE_CART_COL%>" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="<%=W_MERGE_CART_COL%>"><img src="/media_stat/images/template/site_access/option_b.gif" width="147" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="<%=W_MERGE_CART_COL%>">
		<% cart = cartCurrent; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	

</td>
<td width="<%=W_MERGE_CART_PADDING%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_MERGE_CART_PADDING%>" HEIGHT="15" BORDER="0"></td>

<td width="<%=W_MERGE_CART_RADIO%>" align="center"><input type="radio" name="chosen_cart" value="database"></td>
<td width="<%=W_MERGE_CART_COL%>" class="text11bold">

	<table width="<%=W_MERGE_CART_COL%>" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td width="<%=W_MERGE_CART_COL%>"><img src="/media_stat/images/template/site_access/option_c.gif" width="142" height="41" alt="" border="0"><BR>
		<font class="space4pix"><br></font>
		</td>
	</tr>
	<tr>
		<td width="<%=W_MERGE_CART_COL%>">
		<% cart = cartSaved; %>
		<%@ include file="/includes/i_viewcart_merge.jspf" %>
		</td>
	</tr>
	</table>
	


</td>

<td width="<%=W_MERGE_CART_PADDING_RIGHT%>">&nbsp;</td>

</tr>
</table>

<br>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%=W_MERGE_CART_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_MERGE_CART_TOTAL%>">
<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="<%=W_MERGE_CART_TOTAL-300%>" VALIGN="bottom">
		<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_MERGE_CART_TOTAL-300%>" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/continue_orange.gif" alt="CONTINUE TO CHECKOUT"  VSPACE="2" border="0"></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
<input type="image" name="process_merging_cart" src="/media_stat/images/buttons/checkout_arrow.gif"  BORDER="0" alt="GO" VSPACE="2"></TD>
</TR>

</TABLE>
</form>
		</tmpl:put>

</tmpl:insert>

</fd:MergeCartController>
