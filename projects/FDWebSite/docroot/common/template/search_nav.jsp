<%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"
%><%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<% //expanded page dimensions
final int W_SEARCH_NAV_TOTAL = 970;
final int W_SEARCH_NAV_LEFT = 150;
final int W_SEARCH_NAV_RIGHT = 820;
//EXPANDED_PAGE_VERIFY - adjust sizes 
%>
<html lang="en-US" xml:lang="en-US">  
<head>
    <title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' lang="en-US">

	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<fd:css href="/assets/css/ccl.css"/>
	<style type="text/css">
	#OAS_CategoryNote {
	text-align: center;
	}
	</style>
<%
final String trk = "srch"; // tracking code
String criteria = request.getParameter("searchParams");
%>
<fd:javascript src="/assets/javascript/rounded_corners.inc.js"/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<%@ include file="/includes/search/autocomplete.jspf" %>
<CENTER CLASS="text10">
<TABLE WIDTH="<%=W_SEARCH_NAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">

<TR>
	<td width="<%=W_SEARCH_NAV_TOTAL%>" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
</TR>
<TR>

	<TD WIDTH="<%=W_SEARCH_NAV_TOTAL%>" COLSPAN="2">
		<form name="adv_search" id="adv_search" method="GET" accept-charset="iso-8859-1">
		<table width="<%=W_SEARCH_NAV_TOTAL%>" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<div style="margin-top:15px;padding-bottom:15px;border-bottom: 4px solid #FF9933">
						<table>
							<tr>
								<td style="padding-right:35px;"><img src="/media_stat/images/template/search/search_deptnav_2.gif" style="border: none;" alt=""></td>
								<td style="vertical-align:bottom;">
									<div id="searchContainer" class="x-yui-skin-sam" style="position: relative; margin-right: 4px;">
										<INPUT TYPE="text" style="width: 284px; position: static;" id="searchxParams" name="searchParams" value="<%= StringEscapeUtils.escapeHtml(criteria) %>" size="16" maxlength="80" class="text11">
										<div id="terms" class="termsStyle" style="position: absolute; background-color: white"></div>
									</div>
								</td>			
								<td style="vertical-align:bottom">
									<input name="submit" type="image" src="/media_stat/images/template/search/search_find_button.gif" alt="Find" style="border:none;">
								</td>
								<td><div id="searchDebug"></div></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
		</form>
	</TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_SEARCH_NAV_LEFT%>">		
		<tmpl:get name='categoryPanel'/>		
		<BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_SEARCH_NAV_LEFT%>" alt="">
	</TD>
	<td width="<%=W_SEARCH_NAV_RIGHT%>" valign="top">
	<div style="width: <%=W_SEARCH_NAV_RIGHT - 14%>px;padding-left: 14px;">		
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->		
	</div>
	<BR><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="<%=W_SEARCH_NAV_RIGHT%>" HEIGHT="1" BORDER="0" alt=""><BR>	
	<br><br></TD>
</TR>
</TABLE>

</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>

</BODY>
</HTML>
