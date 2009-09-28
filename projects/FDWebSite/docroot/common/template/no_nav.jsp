<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
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

</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" 
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<CENTER>
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
	}

%>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
<td width="733" valign="top" BGCOLOR="#<%=color%>"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
<td width="733" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR VALIGN="TOP">
<TD BGCOLOR="#<%=color%>" VALIGN="BOTTOM" WIDTH="1" rowspan="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
<TD colspan="3" align="center">
<img src="/media_stat/images/layout/clear.gif" height="15" width="733"><br>
<!-- content lands here -->
<tmpl:get name='content'/>
<!-- content ends above here-->
<br><br></TD>
<TD BGCOLOR="#<%=color%>" VALIGN="BOTTOM" WIDTH="1" rowspan="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR>
<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
<TD width="733" align="center"><img src="/media_stat/images/layout/clear.gif" height="1" width="733"><br></TD>
<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
</TR>
<TR VALIGN="BOTTOM">
<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
<td width="733" BGCOLOR="#<%=color%>" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
