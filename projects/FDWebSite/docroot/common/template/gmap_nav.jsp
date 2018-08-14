<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_GMAP_NAV_TOTAL = 970;
%>

<%
	final String gmap_api_key = FDStoreProperties.getGoogleMapsAPIKey();
%>
<html lang="en-US" xml:lang="en-US">
<head>
<%--     <title><tmpl:get name='title'/></title> --%>
     <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%-- @ include file="/shared/template/includes/ccl.jspf" --%>
	<script type="text/javascript" src="https://www.google.com/jsapi?key=<%= gmap_api_key %>"></script>
	<script type="text/javascript">
		google.load("maps", "3", {other_params: "sensor=false"});
	</script>
	
	<tmpl:get name='head_content'/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333"
      onload="initialize()">
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
		<table width="<%=W_GMAP_NAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%=W_GMAP_NAV_TOTAL%>" valign="top" bgcolor="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="733" height="1" border="0"></td>
			</tr>
			<tr>
				<td width="<%=W_GMAP_NAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
			</tr>
			<tr>
				<td><%@ include file="/common/template/includes/deptnav.jspf" %></td>
			</tr>
			<tr>
				<td width="<%=W_GMAP_NAV_TOTAL%>" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
			</tr>
			<tr valign="TOP">
				<td align="center">
					<img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="<%=W_GMAP_NAV_TOTAL%>"><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
				</td>
			</tr>
			<tr>
				<td width="<%=W_GMAP_NAV_TOTAL%>" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" alt="" width="733" height="1" border="0"></td>
			</tr>
		</table>
	</center>
  <%@ include file="/common/template/includes/footer.jspf" %>
  <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
