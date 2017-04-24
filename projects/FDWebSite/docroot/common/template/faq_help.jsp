<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_FAQ_HELP_TOTAL = 970;
final int W_FAQ_HELP_LEFT = 150;
final int W_FAQ_HELP_RIGHT = 820;
%>

<%
String faqPage = FDFaqUtil.getFaqHomeId();
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
    <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
	
    <tmpl:get name="extraCss" />
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%
	request.setAttribute("listPos", "SystemMessage,DeliveryFees");
%>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<TABLE WIDTH="<%=W_FAQ_HELP_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<td width="<%=W_FAQ_HELP_TOTAL%>" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</TR>
<TR>
	<TD WIDTH="<%=W_FAQ_HELP_TOTAL%>" COLSPAN="2"><%@ include file="/common/template/includes/deptnav.jspf" %></TD>
</TR>
<TR>
	<TD WIDTH="<%=W_FAQ_HELP_TOTAL%>" BGCOLOR="#999966" COLSPAN="2"><IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_FAQ_HELP_LEFT%>" BGCOLOR="#E0E3D0">

	
		<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="<%=W_FAQ_HELP_LEFT%>">
		    <TR VALIGN="TOP">
			    <TD WIDTH="10"><IMG src="/media_stat/images/layout/clear.gif" 
		            alt="" WIDTH="10" HEIGHT="1" BORDER="0"></TD>
			    <TD WIDTH="<%=W_FAQ_HELP_LEFT-15%>" valign=top><br><br>
				<A HREF="faq_home.jsp?page=<%= FDFaqUtil.getFaqHomeId() %>" TARGET="_top"><!-- <img src="/media_stat/images/template/help/faq_catnav.gif" width="91" height="58" alt="Frequently Asked Questions" border="0"></A><BR> -->
				<span class="Container_Top_help_FAQ">FREQUENTLY ASKED QUESTIONS</span>
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
			<TD WIDTH="5"><BR></TD>
		</TR>
		</TABLE>
	
	
	</TD>
	<TD width="<%=W_FAQ_HELP_RIGHT%>" align="right">
		<img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="<%=W_FAQ_HELP_RIGHT%>"><br>
		<!-- content lands here -->
			<tmpl:get name='content'/>
		<!-- content ends above here-->
	<br><br>
	</TD>
</TR>
<TR VALIGN="BOTTOM">
	<TD WIDTH="<%=W_FAQ_HELP_LEFT%>" BGCOLOR="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></TD>
	<td width="<%=W_FAQ_HELP_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
