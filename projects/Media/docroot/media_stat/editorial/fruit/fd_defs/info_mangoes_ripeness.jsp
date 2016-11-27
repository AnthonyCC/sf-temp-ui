<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.content.*'  %>

<%
String productId = request.getParameter("productId");
String catId = request.getParameter("catId");
ProductModel product =  ContentFactory.getInstance().getProductByName(catId,productId);
Image prodImg = product.getCategoryImage();
%>

<tmpl:insert template='/shared/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>About Ripeness: Mangoes</tmpl:put>
		<tmpl:put name='content' direct='true'>


<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="330">
<TR VALIGN="TOP">
<TD VALIGN="BOTTOM" WIDTH="10">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD WIDTH="320" COLSPAN="3"><FONT CLASS="title12">About Ripeness: Mangoes</FONT><BR><BR></TD>
</TR>
<TR VALIGN="TOP">
<TD VALIGN="BOTTOM" WIDTH="10" ROWSPAN="2">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD WIDTH="90" ROWSPAN="2">
<IMG SRC="<%=prodImg.getPath()%>" WIDTH="<%=prodImg.getWidth()%>" HEIGHT="<%=prodImg.getHeight()%>" HSPACE="0" VSPACE="0">

<BR>
<IMG SRC="/media/images/layout/clear.gif" WIDTH="90" HEIGHT="1" HSPACE="0" VSPACE="0"><BR></TD>
<TD VALIGN="TOP" WIDTH="220">
<FONT CLASS="text10bold">Ripe</FONT><BR>
Heavy, juicy tropical mangoes are delivered at the peak of ripeness right to your door, ready to slice and eat. Store ripe mangoes in a plastic bag in the crisper of your refrigerator for up to four days.<BR><BR></TD>
<TD VALIGN="BOTTOM" WIDTH="10" ROWSPAN="2">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
</TR>
<TR VALIGN="TOP">
<TD VALIGN="TOP" WIDTH="220">
<FONT CLASS="text10bold">Half Ripe</FONT><BR>
Mangoes will ripen to tropical perfection right in your kitchen. Place in a paper bag at room temperature. They will be soft, sweet, and ready to eat in two to four days.
<BR>
<IMG SRC="/media/images/layout/clear.gif" WIDTH="215" HEIGHT="1" HSPACE="0" VSPACE="0" BORDER="0"></TD>
</TR>
</TABLE><BR><FONT CLASS="space4pix"><BR></FONT>


</tmpl:put>
</tmpl:insert>
