<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	final String gmap_api_key = FDStoreProperties.getGoogleMapsAPIKey();
%>
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%-- @ include file="/shared/template/includes/ccl.jspf" --%>
	<script type="text/javascript" src="http://www.google.com/jsapi?key=<%= gmap_api_key %>"></script>
	<script type="text/javascript">
	google.load("maps", "2");
	</script>
	
	<tmpl:get name='head_content'/>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10" 
      onload="initialize()" onunload="GUnload()">
	<center>
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

		<table width="745" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
				<td width="733" valign="top" bgcolor="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
			</tr>
			<tr>
				<td width="733" valign="top" colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
			</tr>
			<tr>
				<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td colspan="3"><%@ include file="/common/template/includes/deptnav.jspf" %></td>
				<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			</tr>
			<tr>
				<td width="745" bgcolor="#999966" colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			</tr>
			<tr valign="TOP">
				<td bgcolor="#<%=color%>" valign="bottom" width="1" rowspan="2"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td colspan="3" align="center">
					<img src="/media_stat/images/layout/clear.gif" height="15" width="733"><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
				</td>
				<td bgcolor="#<%=color%>" valign="bottom" width="1" rowspan="2"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			</tr>
			<tr>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
				<td width="733" align="center"><img src="/media_stat/images/layout/clear.gif" height="1" width="733"><br /></td>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
			</tr>
			<tr valign="bottom">
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
				<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
			</tr>
			<tr>
				<td width="733" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
			</tr>
		</table>
		<%@ include file="/common/template/includes/footer.jspf" %>
	</center>
</body>
</html>
