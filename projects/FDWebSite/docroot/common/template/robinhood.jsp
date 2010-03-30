<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %><%
	/*
	 *
	 *	Template based off of no_nav.jsp for giftcard use
	 *
	 *	DOES NOT CALL:
	 *		/common/template/includes/globalnav.jspf
	 *		/common/template/includes/globalnav_top.jspf
	 *		/common/template/includes/footer.jspf
	 *
	 *	These files have been built-in / replaced
	 *
	 *	For globalnav elements see:
	 *		/common/template/includes/i_giftcard_nav.jspf
	 *
	 *	batchley : 2009.07.31
	 *
	 */
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>

    <%@ include file="/common/template/includes/metatags.jspf" %>

    <script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
	<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/FD_GiftCards.js" type="text/javascript" language="javascript"></script>

    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<link href="/assets/css/giftcards.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/modalbox.css" rel="stylesheet" type="text/css" />
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
<body onload="<%= request.getAttribute("bodyOnLoad")%>" onunload="<%= request.getAttribute("bodyOnUnload")%>" >
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

		// temp fix
		/*
			FDUserI tmplUser = (FDUserI) session.getAttribute( SessionName.USER );
			FDCartModel tmplCart = null;
			if(tmplUser != null) {
				tmplCart = (FDCartModel) tmplUser.getShoppingCart();
			}
			if (tmplCart != null && tmplCart instanceof FDModifyCartModel && inViewCart) {
				modOrder = true;
				color = "6699CC";
				suffix = "_blue";
			}
		*/
	%>
	<%@ include file="/common/template/includes/i_robinhood_nav.jspf" %> 

	<table width="745" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
			<td width="733" valign="top" bgcolor="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
			<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
		</tr>
		<tr>
			<td width="733" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
		</tr>
		<tr valign="top">
			<td bgcolor="#<%=color%>" valign="bottom" width="1" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			<td colspan="3" align="center">
				<img src="/media_stat/images/layout/clear.gif" height="15" width="733"><br />
				<!-- content lands here -->
				<tmpl:get name='content'/>
				<!-- content ends above here-->
				<br /><br />
			</td>
			<td bgcolor="#<%=color%>" valign="bottom" width="1" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
		<tr>
			<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
			<td width="733" align="center"><img src="/media_stat/images/layout/clear.gif" height="1" width="733"><br></td>
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

	<table width="745" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td align="center" class="text11bold">
				<img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt="" />
				<br /><a href="/index.jsp">Home</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="/your_account/manage_account.jsp">Your Account</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="/search.jsp">Search</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="/help/index.jsp">Help/FAQ</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="/help/index.jsp">Contact Us</a>
				<br /><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" />
			</td>
		</tr>
		<tr>
			<td align="center" class="text11">
				<%@ include file="/shared/template/includes/copyright.jspf" %>
				<br /><img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt="" />
				<br /><a href="/help/privacy_policy.jsp">Privacy Policy</a>
				&nbsp;<font color="#999999">|</font>
				&nbsp;<a href="/help/terms_of_service.jsp">Customer Agreement</a>
				&nbsp;<font color="#999999">|</font>
				&nbsp;<a href="/help/aol_note.jsp">A note on images for AOL users</a>
				<br /><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" />
			</td>
		</tr>
	</table>
</center>

</body>
</html>
