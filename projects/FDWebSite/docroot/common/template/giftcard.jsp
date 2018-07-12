<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% //expanded page dimensions
final int W_GIFTCARD_TOTAL = 970;
%>

<%
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
<html lang="en-US" xml:lang="en-US">
<head>
	<tmpl:get name="seoMetaTag"/>

	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<jwr:script src="/giftcards.js" useRandomParam="false" />
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<jwr:style src="/giftcards.css" media="all" />

	<%-- NOT THIS INCLUDE @ include file="/shared/template/includes/ccl.jspf" --%>

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
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body onload="<%= request.getAttribute("bodyOnLoad")%>" onunload="<%= request.getAttribute("bodyOnUnload")%>" data-gc-page >	
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
	<%@ include file="/common/template/includes/i_giftcard_nav.jspf" %> 

	<table role="presentation" width="<%=W_GIFTCARD_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="<%=W_GIFTCARD_TOTAL%>" valign="top" bgcolor="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_GIFTCARD_TOTAL%>" height="1" border="0"></td>
		</tr>
		<tr>
			<td width="<%=W_GIFTCARD_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
		</tr>
		<tr valign="top">
			<td align="center">
				<img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="<%=W_GIFTCARD_TOTAL%>"><br />
				<!-- content lands here -->
				<tmpl:get name='content'/>
				<!-- content ends above here-->
				<br />
			</td>
		</tr>
		<tr>
			<td width="<%=W_GIFTCARD_TOTAL%>" align="center"><img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_GIFTCARD_TOTAL%>"><br></td>
		</tr>
		<tr valign="bottom">
			<td width="<%=W_GIFTCARD_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
		</tr>
		<tr>
			<td width="<%=W_GIFTCARD_TOTAL%>" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_GIFTCARD_TOTAL%>" height="1" border="0"></td>
		</tr>
	</table>
	<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	<tmpl:get name='customJsBottom'/>
</center>

</body>
</html>
