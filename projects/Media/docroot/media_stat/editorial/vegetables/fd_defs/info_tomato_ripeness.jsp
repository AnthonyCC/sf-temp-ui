<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.content.*'  %>

<%
String productId = request.getParameter("productId");
String catId = request.getParameter("catId");
ProductModel product =  ContentFactory.getInstance().getProductByName(catId,productId);
Image prodImg = product.getCategoryImage();
%>

<tmpl:insert template='/shared/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>About Ripeness: Tomatoes</tmpl:put>
		<tmpl:put name='content' direct='true'> 

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="330">
<TR VALIGN="TOP">
<TD VALIGN="BOTTOM" WIDTH="10">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD WIDTH="320" COLSPAN="3"><FONT CLASS="title12">About Ripeness: Tomatoes</FONT><BR><BR></TD>
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
We deliver juicy, super-plump tomatoes that have just reached the peak of ripeness.  Store them at room temperature for up to two days, but please don't refrigerate &#151; tomatoes lose their terrifically sweet flavor when exposed to very cool air. 
<BR><BR></TD>
<TD VALIGN="BOTTOM" WIDTH="10" ROWSPAN="2">
<IMG SRC="/media/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
</TR>
<TR VALIGN="TOP">
<TD VALIGN="TOP" WIDTH="220">
<FONT CLASS="text10bold">Firm</FONT><BR>
These tomatoes are right on the brink of juicy ripeness, and will actually reach peak sweetness and acidity when kept at room temperature, out of direct sunlight, for a couple of days. They are ready to eat when they smell tomatoey and are slightly heavy with the weight of their sweet, wholesome juice.
<BR>
<IMG SRC="/media/images/layout/clear.gif" WIDTH="215" HEIGHT="1" HSPACE="0" VSPACE="0" BORDER="0"></TD>
</TR>
</TABLE><BR><FONT CLASS="space4pix"><BR></FONT>

</tmpl:put>
</tmpl:insert>
