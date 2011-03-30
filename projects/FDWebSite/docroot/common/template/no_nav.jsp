<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE); %>
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<% if (request.getRequestURI().contains("/myfd")) { %>
	<script type="text/javascript" language="javascript" src="/assets/javascript/cufon-yui.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/EagleCufon.font.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/EagleCufonBold.font.js"></script>
	<script type="text/javascript" language="javascript">
		Cufon.replace('.myfd-header-text', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.myfd-category a', { fontFamily: 'EagleCufon' });
		Cufon.replace('.myfd-category a strong', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.myfd-category a b', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.eagle-bold', { fontFamily: 'EagleCufonBold' });
	</script>
	<% } %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<link rel="stylesheet" href="/assets/css/giftcards.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="/assets/css/timeslots.css"/>
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

</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10 yui-skin-sam" 
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
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
       	color = "6699CC";
       	suffix = "_blue";
	} else if (tmplUser.getCheckoutMode() != EnumCheckoutMode.NORMAL) {
		// STANDING ORDER
		modOrder = true;		
       	color = "996699";
       	suffix = "_purple";
	}

		%>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
		<table width="745" border="0" cellpadding="0" cellspacing="0">
			<tr><td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
				<td width="733" valign="top" bgcolor="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
			</tr>
			<tr>
				<td width="733" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
			</tr>
			<tr valign="TOP">
				<td bgcolor="#<%=color%>" valign="bottom" width="1" rowspan="2"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td colspan="3" align="center">
					<img src="/media_stat/images/layout/clear.gif" height="15" width="733"><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
					<br /><br />
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
