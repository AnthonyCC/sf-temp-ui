<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DNAV_NO_SPACE_TOTAL = 970;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<head>
<%--     <title><tmpl:get name='title'/></title> --%>
     <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
  <%@ include file="/common/template/includes/globalnav.jspf" %> 
	<center class="text10">
		<table width="<%=W_DNAV_NO_SPACE_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" width="<%=W_DNAV_NO_SPACE_TOTAL%>" height="5" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_NO_SPACE_TOTAL%>">
					<%@ include file="/common/template/includes/deptnav.jspf" %>
				</td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" /></td>
			</tr>
			<tr valign="top">
				<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" colspan="3" align="center">
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
				</td>
			</tr>
			<%-- spacers --%>
			<tr valign="top">
				<td><img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_DNAV_NO_SPACE_TOTAL%>" alt="" /></td>
			</tr>
			<tr valign="bottom">
				<td width="<%=W_DNAV_NO_SPACE_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_DNAV_NO_SPACE_TOTAL%>" height="5" border="0" alt="" /></td>
			</tr>
		</table>
	</center>
  <%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
</body>
</html>
