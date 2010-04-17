<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
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


</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" 
    onload="<%= request.getAttribute("bodyOnLoad")%>" 
    onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<TABLE WIDTH="733" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TR VALIGN="TOP">
	<TD WIDTH="595"><img src="/media_stat/images/layout/clear.gif" width="595" height="1" border="0"></TD>
	<TD WIDTH="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" border="0"></TD>
	<TD WIDTH="140"><img src="/media_stat/images/layout/clear.gif" width="140" height="1" border="0"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="595">
	<!-- nested quick shell here, content inside -->
		
		<TABLE WIDTH="595" CELLPADDING="0" CELLSPACING="0" BORDER="0">
		<TR VALIGN="TOP">
			<TD WIDTH="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
			<TD WIDTH="4"><img src="/media_stat/images/layout/clear.gif" width="4" height="1" border="0"></TD>
			<TD WIDTH="585"><img src="/media_stat/images/layout/clear.gif" width="585" height="1" border="0"></TD>
			<TD WIDTH="4"><img src="/media_stat/images/layout/clear.gif" width="4" height="1" border="0"></TD>
			<TD WIDTH="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
		</TR>
		<TR>
			<TD WIDTH="5" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/qs_top_left_curve.gif" width="5" height="5" border="0"></TD>
			<TD WIDTH="585" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
			<TD WIDTH="5" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/qs_top_right_curve.gif" width="5" height="5" border="0"></TD>
		</TR>
		<TR>
			<TD WIDTH="585"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></TD>
		</TR>
		<TR VALIGN="TOP">
			<TD WIDTH="1" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
			<TD WIDTH="4"><BR></TD>
			<TD WIDTH="585">
				
<% 
String level = (String) request.getAttribute("quickshop.level");// This is defined in index.jsp or index_guest.jsp of quickshop

if ("index".equals(level)){}
else{
%>
	
				<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR VALIGN="MIDDLE">
					<TD><A HREF="/quickshop/index.jsp"><img src="/media_stat/images/navigation/department/quickshop/qs_depnav.gif" width="158" height="28" border="0" alt="QUICKSHOP"></A></TD>
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" width="1" height="28" border="0" HSPACE="7"><BR></TD>
					<TD valign="top">
						<A HREF="/quickshop/previous_orders.jsp">Your Previous Orders</A><BR>
						<A HREF="/quickshop/every_item.jsp">Every Item Ordered</A><BR>
					</TD>
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" width="1" height="28" border="0" HSPACE="7"><BR></TD>
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
					<TD WIDTH="11" ALIGN="CENTER" VALIGN="BOTTOM"><img src="/media_stat/images/layout/cccccc.gif" width="1" height="28" border="0" HSPACE="7"><BR></TD>
					<TD valign="top">
						<A HREF="/quickshop/standing_orders.jsp">Your Standing Orders</A>
						<br>
						<br>
					</TD>
				</TR>
				<TR>
					<TD><img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></TD>
				</TR>
				</TABLE>
<%}%>				
				
			</TD>
			<TD WIDTH="4"><BR></TD>
			<TD WIDTH="1" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>

<% 
if ("index".equals(level)){}
else{
%>	
		<TR>
			<TD WIDTH="595" COLSPAN="5" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>
<%}%>		


		<TR VALIGN="TOP">
			<TD WIDTH="1" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="220" border="0"></TD>
			<TD WIDTH="4"><BR></TD>
			<td width="585" align="center" valign="top">
			<!-- page content goes here-->
				<tmpl:get name='content'/>
			<!-- end of page content -->
			</td>
			<TD WIDTH="4"><BR></TD>
			<TD WIDTH="1" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>
		<TR VALIGN="TOP">
			<TD WIDTH="5" ROWSPAN="2" COLSPAN="2"><img src="/media_stat/images/layout/qs_bottom_left_curve.gif" width="5" height="5" border="0"></TD>
			<TD WIDTH="585"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></TD>
			<TD WIDTH="5" ROWSPAN="2" COLSPAN="2"><img src="/media_stat/images/layout/qs_bottom_right_curve.gif" width="5" height="5" border="0"></TD>
		</TR>
		<TR VALIGN="TOP">
			<TD WIDTH="585" BGCOLOR="#996699"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></TD>
		</TR>
		</TABLE>

	</TD>
	<TD WIDTH="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" border="0"></TD>
	<TD WIDTH="140">
	<%@ include file="/includes/i_promotion_counter.jspf" %>
	 <% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('QSTopRight');
                //-->
      	</SCRIPT><br><br>
	 <% } %>
	<%@ include file="/common/template/includes/your_cart_quick_shop.jspf" %>
	</TD>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
