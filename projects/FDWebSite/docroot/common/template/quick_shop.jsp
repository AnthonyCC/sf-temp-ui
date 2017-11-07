<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en-US" xml:lang="en-US"> <!--<![endif]-->
  <head>
    <%@ taglib uri='template' prefix='tmpl' %>
    <%@ taglib uri='logic' prefix='logic' %>
    <%@ taglib uri='freshdirect' prefix='fd' %>
    <%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
    <%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<% //expanded page dimensions
    final int W_QUICK_SHOP_TOTAL = 970;
    final int W_QUICK_SHOP_CONTENT = 765;
    final int W_QUICK_SHOP_CART = 191;
    final int W_QUICK_SHOP_DELIMITER = 14;
%>
<%request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE);%>

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

    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>

<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"
    onload="<%= request.getAttribute("bodyOnLoad")%>"
    onunload="<%= request.getAttribute("bodyOnUnload")%>"
    class="quickshop">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %>

<CENTER CLASS="text10">


<%
String level = (String) request.getAttribute("quickshop.level");// This is defined in index.jsp or index_guest.jsp of quickshop

if ("indexGuest".equals(level)){%>

			<!-- page content goes here-->
				<tmpl:get name='content'/>
			<!-- end of page content -->

<%
}else{
%>

<TABLE WIDTH="<%= W_QUICK_SHOP_TOTAL %>" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TR VALIGN="TOP">
  <TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_CONTENT %>" height="1" border="0"></TD>
  <TD WIDTH="<%= W_QUICK_SHOP_DELIMITER %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="10" height="1" border="0"></TD>
  <TD WIDTH="<%= W_QUICK_SHOP_CART %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_CART %>" height="1" border="0"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>">
	<!-- nested quick shell here, content inside -->

		<TABLE WIDTH="<%= W_QUICK_SHOP_CONTENT %>" CELLPADDING="0" CELLSPACING="0" BORDER="0">

		<TR VALIGN="TOP">
			<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>">

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
							<A HREF="/quickshop/all_lists.jsp">Your Shopping Lists</a><BR>
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
			</TD>
		</TR>
		<TR>
			<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>


		<TR VALIGN="TOP">
      <td width="<%= W_QUICK_SHOP_CONTENT %>" align="center" valign="top">
			<!-- page content goes here-->
				<tmpl:get name='content'/>
			<!-- end of page content -->
			</td>
		</TR>
		<TR VALIGN="TOP">
			<TD WIDTH="<%= W_QUICK_SHOP_CONTENT %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" border="0"></TD>
		</TR>
		</TABLE>

	</TD>
  <TD WIDTH="<%= W_QUICK_SHOP_DELIMITER %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_QUICK_SHOP_DELIMITER %>" height="1" border="0"></TD>
	<TD WIDTH="<%= W_QUICK_SHOP_CART %>">
	<%@ include file="/includes/i_promotion_counter.jspf" %>
	 <% if (FDStoreProperties.isAdServerEnabled()) { %>
    <div id='oas_QSTopRight'>
		  <SCRIPT LANGUAGE=JavaScript>
        <!--
        OAS_AD('QSTopRight');
        //-->
      </SCRIPT><br><br>
    </div>
	 <% } %>
	<%@ include file="/common/template/includes/your_cart_quick_shop.jspf" %>
	</TD>
</TR>
</TABLE>
<%}%>

</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
