<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<% //expanded page dimensions
final int W_NO_NAV_TOTAL = 970;

request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE);

String pageURI = request.getRequestURI();
%>
<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie10 lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie10 lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie10 lt-ie9" lang="en"> <![endif]-->
<!--[if IE 9]>    <html class="no-js lt-ie10" lang="en"> <![endif]-->
<!--[if gt IE 9]><!--> <html lang="en-US" xml:lang="en-US" class="no-js" lang="en"> <!--<![endif]-->
<head>
    <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<jwr:style src="/giftcards.css" media="all" />
	<jwr:style src="/timeslots.css" media="all" />
  <tmpl:get name='extraCss'/>
  	<% if (pageURI.indexOf("/help/") == -1) { %>
		<%@ include file="/shared/template/includes/ccl.jspf" %>
	<% } %>
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
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" 
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%@ include file="/shared/template/includes/i_body_start.jspf" %>      
		<%
		boolean modOrder = false;
		boolean inViewCart = false;

		String color = "999966";
		String suffix = "";


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
       	request.setAttribute("inSo", "true");
	}

		%>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
    <center class="text10">
		<table role="presentation" width="<%=W_NO_NAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr valign="TOP">
				<td align="center">
					<img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="<%=W_NO_NAV_TOTAL%>"><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
					<br /><br />
				</td>
			</tr>
		</table>
  </center>
		<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
