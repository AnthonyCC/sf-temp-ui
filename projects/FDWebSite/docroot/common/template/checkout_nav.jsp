<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
*/%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%
boolean modOrder = false;
String color = "999966";
String suffix = "";

	FDUserI tmplUser = (FDUserI) session.getAttribute( SessionName.USER );
	FDCartModel tmplCart = (FDCartModel) tmplUser.getShoppingCart();
	
	if (tmplCart instanceof FDModifyCartModel) {
		modOrder = true;
        color = "6699CC";
        suffix = "_blue";
		}

%>
<%@ include file="/common/template/includes/checkoutnav.jspf" %> 
<TABLE width="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<tr valign="TOP">
<td bgcolor="#<%=color%>" valign="bottom" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
<td width="733" align="center">
<img src="/media_stat/images/layout/clear.gif" height="15" width="733"><br>

<!-- content lands here -->

<tmpl:get name='content'/>

<!-- content ends above here-->

<br><br></td>
<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
<td bgcolor="#<%=color%>" valign="bottom" width="1"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<tr valign="bottom">
<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve<%=suffix%>.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
<td width="733" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"></td>
</tr>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
