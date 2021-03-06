<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ page import='com.freshdirect.storeapi.attributes.*'  %>
<%@ page import='com.freshdirect.storeapi.content.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='logic' prefix='logic' %>

<%
String prodId = request.getParameter("prodId");
String catId = request.getParameter("catId");

ProductModel product =  PopulatorUtil.getProductByName(catId,prodId);
Image prodImg = product.getCategoryImage();
String recTable = product.getRecommendTable().getPath();
%>
<HTML>
<HEAD>
<%--     <title><tmpl:get name='title'/></title> --%>
    <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</HEAD>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"  onLoad="window.focus()">
<CENTER>
<A NAME="top"></A>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="520">
<TR VALIGN="TOP">
	<TD WIDTH="520">
		<A HREF="javascript:window.reallyClose();"><img src="/media_stat/images/layout/pop_up_header_lg.gif" border="0" alt="freshdirect    (close window)"></A><BR>
		<FONT CLASS="space10pix"><BR></FONT>
	</TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="520" CLASS="text11">
	
		<tmpl:get name='content'/>
	
	</TD>
</TR>
</TABLE>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="520">
<TR VALIGN="TOP">
	<TD WIDTH="90" ALIGN="CENTER">
	<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" border="0" alt="Porterhouse Steak">
	</TD>
	<TD WIDTH="430">
		<FONT CLASS="title14"><%=product.getFullName()%></FONT> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="/media_stat/images/layout/star.gif" width="6" height="6" hspace="2" vspace="4" border="0" alt="most popular"><FONT CLASS="text9">Most popular thickness for this steak</FONT><BR>
		<FONT CLASS="space2pix"></BR></FONT>
		<IMG src="/media_stat/images/layout/669933.gif" alt="" WIDTH="430" HEIGHT="1" HSPACE="0" VSPACE="0"><BR>
		<FONT CLASS="space4pix"></BR></FONT>
		<fd:IncludeMedia name='<%=recTable%>'/>	
	</td>
</tr>
</table>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="520">
	<TR VALIGN="TOP">
		<TD WIDTH="260"><A HREF="#top">Back to Top</A></TD>
		<TD WIDTH="260" ALIGN="RIGHT"><A HREF="javascript:window.reallyClose();">close window</A>&nbsp;&nbsp;</TD>
	</TR>
</TABLE>
</CENTER>
</BODY>
</HTML>
