<%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd' %><%
	//expanded page dimensions
	final int W_TOP_NAV_ONLY_TOTAL = 970;
	int layoutType = (request.getAttribute("layoutType") == null) ? -1 : Integer.parseInt(request.getAttribute("layoutType").toString());
	
	request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE); %><%
	
	if (layoutType == EnumLayoutType.PRESIDENTS_PICKS.getId() || layoutType == EnumLayoutType.PRODUCTS_ASSORTMENTS.getId()) {
		%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"><%
	} else {
		%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%
	} %>
<html lang="en-US" xml:lang="en-US">
<head>
    <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
 	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
<%		
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script type="text/javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>
	<tmpl:get name='facebookmeta'/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333"
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%@ include file="/shared/template/includes/i_body_start.jspf" %>      
		<%
		boolean modOrder = false;
		boolean inViewCart = false;

		String color = "999966";
		String suffix = "";

		String pageURI = request.getRequestURI();

			if (pageURI.indexOf("view_cart") > -1) {
				inViewCart = true;
			}

			FDUserI tmplUser = (FDUserI) session.getAttribute( SessionName.USER );
			FDCartModel tmplCart = null;
			if(tmplUser != null) {
				tmplCart = (FDCartModel) tmplUser.getShoppingCart();
			}
			if (tmplCart != null && tmplCart instanceof FDModifyCartModel && inViewCart) {
				modOrder = true;
					color = "6699cc";
					suffix = "_blue";
			}

		%>
		<%@ include file="/common/template/includes/globalnav.jspf" %>

	<center class="text10">
		<table width="<%=W_TOP_NAV_ONLY_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="fivePxTall" width="<%=W_TOP_NAV_ONLY_TOTAL%>" valign="top" colspan="5"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
			</tr>
			<tr>
				<td><%@ include file="/common/template/includes/deptnav.jspf" %></td>
			</tr>
			<tr>
				<td class="onePxTall" width="<%=W_TOP_NAV_ONLY_TOTAL%>" bgcolor="#999966" colspan="5"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
			</tr>
			<tr valign="top">
				<td align="center">
					<img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="<%=W_TOP_NAV_ONLY_TOTAL%>"><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
				</td>
			</tr>
		</table>
	</center>
		<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
