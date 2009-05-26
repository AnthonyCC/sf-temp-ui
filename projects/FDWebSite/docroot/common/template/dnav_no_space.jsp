<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10">
	<center>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
		<table width="745" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="6" colspan="2" rowspan="2">
					<img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0" alt="" />
				</td>
				<td width="733" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt="" /></td>
				<td width="6" colspan="2" rowspan="2">
					<img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0" alt="" />
				</td>
			</tr>
			<tr>
				<td width="733" valign="top"><img src="/media_stat/images/layout/clear.gif" width="733" height="5" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="1" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="34" border="0" alt="" /></td>
				<td width="743" colspan="3">
					<%@ include file="/common/template/includes/deptnav.jspf" %>
				</td>
				<td width="1" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="34" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="745" bgcolor="#999966" colspan="7"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" /></td>
			</tr>
			<tr valign="top">
				<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" /></td>
				<td width="743" colspan="3" align="center">
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
				</td>
				<td bgcolor="#999966" valign="bottom" width="1">
					<img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" />
				</td>
			</tr>
			<%-- spacers --%>
			<tr valign="top">
				<td bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" height="1" width="733" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt="" /></td>
				<td bgcolor="#999966"><img src="/media_stat/images/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr valign="bottom">
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0" alt="" /></td>
				<td width="733"><img src="/media_stat/images/layout/clear.gif" width="733" height="5" border="0" alt="" /></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="733" bgcolor="#999966" valign="bottom"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt="" /></td>
			</tr>
		</table>
		<%@ include file="/common/template/includes/footer.jspf" %>
	</center>
</body>
</html>
