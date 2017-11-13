<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<% //expanded page dimensions

final int W_QUICK_SHOP_TOTAL = 970;
int W_QUICK_SHOP_CONTENT = 765;
final int W_QUICK_SHOP_CART = 191;
final int W_QUICK_SHOP_DELIMITER = 14;
final int W_QUICK_SHOP_LEFTNAV = 150;
int W_QUICK_SHOP_CENTER = 601;

/* If this request attribute is set to false, right-side chart won't be displayed and content spawns
whole horizontal area. Default value is true (cart is displayed, original behavior). */
Boolean isQuickShopCartVisible = (Boolean)request.getAttribute("isQuickShopCartVisible");
if(isQuickShopCartVisible==null) { isQuickShopCartVisible = true; }
if(!isQuickShopCartVisible) {
	W_QUICK_SHOP_CONTENT = W_QUICK_SHOP_TOTAL;
	W_QUICK_SHOP_CENTER = 765;
}

request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE);
%>
<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en-US" xml:lang="en-US"> <!--<![endif]-->
  <head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/ccl.jspf" %>
<%
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script language="javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>

    <tmpl:get name='head'/>
    <tmpl:get name='extrahead'/>

    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>

<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"
      onload="<%= request.getAttribute("bodyOnLoad")%>"
      onunload="<%= request.getAttribute("bodyOnUnload")%>"
      class="quickshop">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<CENTER CLASS="text10">
<TABLE WIDTH="<%= W_QUICK_SHOP_TOTAL %>" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TR VALIGN="TOP">
	<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_CONTENT %>" height="1" border="0"></TD>
	<% if(isQuickShopCartVisible) { %>
		<TD WIDTH="<%= W_QUICK_SHOP_DELIMITER %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_DELIMITER %>" height="1" border="0"></TD>
		<TD WIDTH="<%= W_QUICK_SHOP_CART %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_CART %>" height="1" border="0"></TD>
	<% } %>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>">
	<!-- nested quick shell here, content inside -->
		<TABLE WIDTH="<%= W_QUICK_SHOP_CONTENT %>" CELLPADDING="0" CELLSPACING="0" BORDER="0">
			<TR VALIGN="TOP">
				<TD WIDTH="<%= W_QUICK_SHOP_LEFTNAV %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_LEFTNAV %>" height="1" border="0"></TD>
				<TD WIDTH="<%= W_QUICK_SHOP_DELIMITER %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_DELIMITER %>" height="1" border="0"></TD>
				<TD WIDTH="<%= W_QUICK_SHOP_CENTER %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_CENTER %>" height="1" border="0"></TD>
			</TR>
			<TR>
				<TD colspan="3"><img src="/media_stat/images/layout/dot_clear.gif" width="1" height="4" border="0"></TD>
			</TR>
			<TR VALIGN="TOP">
				<TD colspan="3">
<%
String level = (String) request.getAttribute("quickshop.level");// This is defined in index.jsp or index_guest.jsp of quickshop
if ("index".equals(level)){}
else{
%>
				<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR VALIGN="MIDDLE">
					<TD><A HREF="/quickshop/index.jsp"><img src="/media_stat/images/navigation/department/quickshop/qs_depnav.gif" width="158" height="28" border="0" alt="REORDER"></A></TD>
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" alt="" width="1" height="28" border="0" HSPACE="7"><BR></TD>
					<TD valign="top">
						<A HREF="/quickshop/previous_orders.jsp">Your Previous Orders</A><BR>
						<A HREF="/quickshop/every_item.jsp">Every Item Ordered</A><BR>
					</TD>
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" alt="" width="1" height="28" border="0" HSPACE="7"><BR></TD>
					<TD valign="top">
						<% int brs = 2; %>
						<fd:GetCustomerRecipeList id="recipes">
							<% brs--; %>
							<A HREF="/quickshop/your_recipes.jsp">Your Recipes</A><BR>
						</fd:GetCustomerRecipeList>
						<fd:CCLCheck>
							<% brs--; %>
							<A HREF="/quickshop/all_lists.jsp">Your Shopping Lists</A><BR>
						</fd:CCLCheck>
						<% for (int i=0; i<brs; i++) { %><BR><% } %>
					</TD>
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" alt="" width="1" height="28" border="0" HSPACE="7"><BR></TD>

					<% if ( user.isEligibleForStandingOrders() ) { %>
						<TD valign="top">
							<A HREF="/quickshop/standing_orders.jsp">Your Standing Orders</A>
							<br>
							<br>
						</TD>
					<% } %>

				</TR>
				<TR>
					<TD><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" border="0"></TD>
				</TR>
				</TABLE>
<%}%>

			</TD>
		</TR>

<%
if ("index".equals(level)){}
else{
%>
		<TR>
			<TD COLSPAN="3" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>
<%}%>


		<TR VALIGN="TOP">
      <td bgcolor="#E0E3D0"><!-- side nav -->
        <div style="width: 140px; padding: 5px; overflow: hidden; text-overflow: ellipsis;">
          <tmpl:get name='side_nav'/>
        </div><!-- end side nav -->
      </td>
			<td></td>
			<td>
			<!-- page content goes here-->
				<tmpl:get name='content'/>
      <!-- end of page content -->
			</td>
		</TR>
		</TABLE>

	</TD>
	<% if(isQuickShopCartVisible) { %>
		<TD WIDTH="5"><img src="/media_stat/images/layout/dot_clear.gif" width="5" height="1" border="0"></TD>
		<TD WIDTH="<%= W_QUICK_SHOP_CART %>">
		<%@ include file="/includes/i_promotion_counter.jspf" %>
		<% if (FDStoreProperties.isAdServerEnabled()) { %>
      <div id='oas_QSTopRight'>
			  <SCRIPT LANGUAGE=JavaScript>
          <!--
          OAS_AD('QSTopRight');
          //-->
	      </SCRIPT><br><br>
		 <% } %>
		<%@ include file="/common/template/includes/your_cart_quick_shop.jspf" %>
		</TD>
	<% } /* if(isQuickShopCartVisible) */ %>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
