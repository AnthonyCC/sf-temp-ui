<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
String faqPage = "faqHome";
if (request.getParameter("page")!= null){
faqPage = request.getParameter("page");
}
%>

<%FDUserI faqUser = (FDUserI)session.getAttribute(SessionName.USER);
String faqSections = FDStoreProperties.getFaqSections();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" COLSPAN="2" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
	<td width="733" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
	<TD WIDTH="743" COLSPAN="5"><%@ include file="/common/template/includes/deptnav.jspf" %></TD>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
</TR>
<TR>
	<TD WIDTH="745" BGCOLOR="#999966" COLSPAN="7"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" BGCOLOR="#E0E3D0"><BR></TD>
	<TD WIDTH="155" BGCOLOR="#E0E3D0">

	
		<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="147">
		    <TR VALIGN="TOP">
			    <TD WIDTH="6"><IMG src="/media_stat/images/layout/clear.gif" 
		            WIDTH="6" HEIGHT="1" BORDER="0"></TD>
			    <TD WIDTH="141" valign=top><br><br>
				<A HREF="faq_home.jsp?page=faqHome" TARGET="_top"><img src="/media_stat/images/template/help/faq_catnav.gif" width="91" height="58" alt="" border="0"></A><BR>
				<font class="space4pix"><br></font>
				
				<%  if(null != faqSections){
				  StringTokenizer st = new StringTokenizer(faqSections,",");
				  while (st.hasMoreTokens()) {
					String nextToken=st.nextToken().trim();
					ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(nextToken);
					if(null !=contentNode){	
				  	if(nextToken.equals(faqPage)){
								
				%><b><%= contentNode.getCmsAttributeValue("name") %></b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}else{%><A HREF="faq_home.jsp?page=<%= contentNode.getContentKey().getId() %>" TARGET="_top"><%= contentNode.getCmsAttributeValue("name") %></A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}}%>	
				<% }} %>
				
				
			</TD>
			<TD WIDTH="4"><BR></TD>
		</TR>
		</TABLE>
	
	
	</TD>
	<TD width="570" align="center" colspan=2>
		<img src="/media_stat/images/layout/clear.gif" height="15" width="570"><br>
		<!-- content lands here -->
			<tmpl:get name='content'/>
		<!-- content ends above here-->
	<br><br>
	</TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="BOTTOM">
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve_nav.gif" width="6" height="6" border="0"></td>
	<TD WIDTH="125" BGCOLOR="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></TD>
	<td width="608"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
	<td width="733" COLSPAN="2" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
