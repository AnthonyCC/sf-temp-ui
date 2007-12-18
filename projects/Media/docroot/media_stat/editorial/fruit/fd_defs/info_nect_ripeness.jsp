<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.content.*'  %>

<%
String productId = request.getParameter("productId");
String catId = request.getParameter("catId");
ProductModel product =  ContentFactory.getInstance().getProductByName(catId,productId);
Image prodImg = product.getCategoryImage();
%>

<tmpl:insert template='/shared/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>About Ripeness: Nectarines</tmpl:put>
		<tmpl:put name='content' direct='true'>
		
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="330">
<TR VALIGN="TOP">
<TD VALIGN="BOTTOM" WIDTH="10">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD WIDTH="320" COLSPAN="3"><FONT CLASS="title12">About Ripeness: Nectarines</FONT><BR><BR></TD>
</TR>
<TR VALIGN="TOP">
<TD VALIGN="BOTTOM" WIDTH="10" ROWSPAN="2">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD WIDTH="90" ROWSPAN="2">
<IMG SRC="<%=prodImg.getPath()%>" WIDTH="<%=prodImg.getWidth()%>" HEIGHT="<%=prodImg.getHeight()%>" HSPACE="0" VSPACE="0">

<BR>
<IMG SRC="/media/images/layout/clear.gif" WIDTH="90" HEIGHT="1" HSPACE="0" VSPACE="0"><BR></TD>
<TD VALIGN="TOP" WIDTH="220">
<FONT CLASS="title12">Ripe</FONT><BR>
Bursting with juicy sweetness, with voluptuous flesh that yields to the touch, our nectarines are at the height of ripeness. Eat them right away, or keep them in your refrigerator crisper for three to five days.<BR><BR></TD>
<TD VALIGN="BOTTOM" WIDTH="10" ROWSPAN="2">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
</TR>
<TR VALIGN="TOP">
<TD VALIGN="TOP" WIDTH="220">
<FONT CLASS="title12">Firm</FONT><BR>
Slightly firm nectarines are ideal for baking, canning, or drying. For peak juiciness and sweetness, let them ripen in a bowl or a loose paper bag for two to four days.
<BR>
<IMG SRC="/media/images/layout/clear.gif" WIDTH="215" HEIGHT="1" HSPACE="0" VSPACE="0" BORDER="0"></TD>
</TR>
</TABLE><BR><FONT CLASS="space4pix"><BR></FONT>

</tmpl:put>
</tmpl:insert>
