<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.util.*' %>
<%

    String deptLink = request.getParameter("deptLink");

    String action = request.getParameter("action");
    if (action==null) action="select";
    int itemCount = 1;

    String currentPage = request.getRequestURI();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>FreshDirect Web Manager</title>
	<link rel="stylesheet" href="/StoreAdmin/common/css/store_admin.css" type="text/css">
	<script language="Javascript" src="/StoreAdmin/common/javascript/common.js"></script>
</head>
<sa:ContentRefSelection action='<%=action%>' id='theAttribute' result='result'>
<BODY topmargin="0" leftmargin="0" BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" SCROLL="yes">

<% if (result.isSuccess() && "refreshParent".equalsIgnoreCase(request.getParameter("action"))) { %>
<script>
	window.opener.location='<%=(String)session.getAttribute("parentURI")%>?action=updateAttribute';
	window.close();
</script>
<%} else {  //not closing window...so show stuff %>
<%-- display error messages --%>
<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Priority'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='duplicateItem'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Error'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
</table>
<TABLE WIDTH="585" CELLSPACING="0" CELLPADDING="0" BORDER="0">
<TR VALIGN="TOP">
<TD WIDTH="68%">
<TABLE WIDTH="380" CELLSPACING="2" CELLPADDING="0" BORDER="0">
<TR>
<TD WIDTH="380" ALIGN="RIGHT"><FONT CLASS="space4pix"><BR></FONT>
<A HREF="javascript:window.close()">CLOSE</A>&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR>
<TD WIDTH="380"><FONT CLASS="title13or">FD Web Manager: Product Associations</FONT><BR>
<FONT CLASS="space4pix"><BR></FONT>
 <img src="images/CCCCCC.gif" WIDTH="380" HEIGHT="1"><BR>
<FONT CLASS="space4pix"><BR></FONT>
</TD>
</TR>
<tr><td>Attribute:<%=theAttribute!=null ? theAttribute.getKey() : ""%></td></tr>
</TABLE>

<FONT CLASS="space4pix"><BR></FONT>
<img src="images/CCCCCC.gif" WIDTH="380" HEIGHT="1"><BR>
<FONT CLASS="space4pix"><BR></FONT>
<%
if (isMultiAttribute.booleanValue() && theAttribute!=null) {
	itemCount = ((MultiAttribute)theAttribute).numberOfValues();
%>
<FONT CLASS="text11bold">Number of items: (<%=itemCount%>)</FONT><BR>
<FONT CLASS="space4pix"><BR></FONT>
<% }%>
<TABLE WIDTH="100%" CELLSPACING="2" CELLPADDING="0" BORDER="0">
<TR CLASS="text11bold">
<TD WIDTH="120">Item Id</TD>
<TD WIDTH="10"></TD>
<TD WIDTH="180">Item Short Desc.</TD>
<TD WIDTH="10"></TD>
<TD colspan="2" WIDTH="60">Priority</TD>
</TR>
</TABLE>
<DIV STYLE="overflow-y:scroll;position:relative;width:68%;left:10;top:5;height:200;">
<TABLE WIDTH="68%" CELLSPACING="2" CELLPADDING="0" BORDER="0" class='tree'>
<form name="selectedProducts" action="pop_contref_assoc.jsp" method="post">
<input type="hidden" name="brandNodes" value="<%=request.getParameter("brandNodes") %>">
<%
	int priority=1;
	List contentRefs;
	if (isMultiAttribute.booleanValue()) {
		contentRefs = ((MultiAttribute)theAttribute).getValues();
	} else {
		contentRefs = new ArrayList();
		contentRefs.add(theAttribute.getValue());
	}
	int indx = 0;
	for (Iterator refItr=contentRefs.iterator(); refItr.hasNext(); ) {
		ContentRef refItem = (ContentRef)refItr.next();

		//ContentRef refItem = (ContentRef)((Attribute)refItr.next()).getValue();
		ContentNodeModel theNode = null;
		String itemId = "---";
		String itmDesc = "--- No Description ---";
		if (refItem==null || refItem.getType()==null) continue;
		if (refItem.getType().equals(ContentNodeI.TYPE_PRODUCT)) {
			theNode = ((ProductRef) refItem).lookupProduct();
			itemId = "(P) "+refItem.getRefName() +"/"+ refItem.getRefName2();
			itmDesc = theNode.getFullName();
		} else if (refItem.getType().equals(ContentNodeI.TYPE_CATEGORY)){
			theNode = ((CategoryRef) refItem).getCategory();
			itemId = "(C) "+refItem.getRefName();
			itmDesc = theNode.getNavName();
		} else if (refItem.getType().equals(ContentNodeI.TYPE_DEPARTMENT)){
			theNode = ((DepartmentRef) refItem).getDepartment();
			itemId = "(D) "+refItem.getRefName();
			itmDesc = theNode.getFullName();
		} else if (refItem.getType().equals(ContentNodeI.TYPE_SKU)) {
			theNode = ((SkuRef) refItem).getSku();
			itemId = "(S) "+refItem.getRefName() +"/"+ refItem.getRefName2() +"/"+ refItem.getRefName3();
			itmDesc = theNode.getFullName();
		} else if (refItem.getType().equals(ContentNodeI.TYPE_BRAND)){
			theNode = ((BrandRef) refItem).getBrand();
			itemId = "(B) "+refItem.getRefName();
			itmDesc = theNode.getFullName();
		}
%>
<TR>
<td width="10"><input type="checkbox" value="<%=indx%>" name="deleteItem"></td>
<TD WIDTH="5"><BR></TD>
<TD WIDTH="120"><%=itemId%></TD>
<TD WIDTH="10"><BR></TD>
<TD WIDTH="180"><%=itmDesc%></TD>
<TD WIDTH="10"><BR></TD>
<TD WIDTH="40"><INPUT name="priority" TYPE="text" SIZE="4" value="<%=priority%>"></TD>
</TR>
<%
		priority++;
        indx++;
	}
    //if (contentRefs.size()>1){
%>
<tr>
<TD colspan="7">&nbsp;</TD>
<TD WIDTH="10"><input type="submit" value="Update"></TD>
</TR>
<% // } %>
</form>
</TABLE></div><BR>
<TABLE WIDTH="67%" CELLSPACING="2" CELLPADDING="0" BORDER="0">
<TR>
<TD><A HREF="javascript:window.close();" class="button"><font color="#CC0000">&nbsp;CANCEL&nbsp;</font></A></td> &nbsp;
<TD><A HREF="pop_contref_assoc.jsp?action=refreshParent" class="button"><font color="#006600">&nbsp;SAVE&nbsp;</A></TD>
</TR>
</TABLE>
&nbsp;<BR>
&nbsp;<BR>

</TD>
<TD WIDTH="14"><img src="images/clear.gif" WIDTH="14" HEIGHT="1"></TD>
<TD WIDTH="33%">
<FONT CLASS="space4pix"><BR></FONT>
<FONT CLASS="title13or">Browse Hierarchy</FONT><BR>
<FONT CLASS="space4pix"><BR></FONT>
<DIV STYLE="overflow-y:scroll;overflow-x:scroll;position:relative;width:204;left:0;top:10;height:400;">
<TABLE WIDTH="100%" CELLPADDING="1" CELLSPACING="1" BORDER="0" class="tree">
<!----*****************************-  -->
<sa:Tree id='node'>
    <%
      int indentValue = 16 * (depth.intValue());

      if (node.getContentType().equals(ContentNodeI.TYPE_DEPARTMENT)) {
    %>
   	<tr valign="top">
		<td width="100%"><div style="margin-left:<%=indentValue %>px; text-indent: -12px;"><ximg src="<%= request.getContextPath() %>/images/clear.gif" height="1" xwidth="<%=indentValue %>">
		<a name="<%= node.getContentName() %>" href="pop_contref_assoc.jsp?expand=<%= path %>#<%= node.getContentName() %>">&curren;</a>&nbsp;
		<a href="pop_contref_assoc.jsp?action=select&deptId=<%=node.getContentName()%>#<%= node.getContentName() %>"><%= node.getFullName() %></a></div></td>
	</tr>
    <%  } else if (node.getContentType().equals(ContentNodeI.TYPE_CATEGORY)) { %>
		<tr valign="top">
	        <td width="100%"><div style="margin-left:<%=indentValue-2 %>px; text-indent: -12px;"><ximg src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%=indentValue%>">
	        <a name="<%= node.getContentName() %>" href="pop_contref_assoc.jsp?expand=<%= path %>#<%= node.getContentName() %>">&ordm;</a>&nbsp;
	        <a href="pop_contref_assoc.jsp?action=select&catId=<%= node.getContentName() %>#<%= node.getContentName() %>"><%= node.getFullName() %></a></div></td>
		</tr>
    <%  } else if (node.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) { %>
	<tr valign="top">
        <td width="100%"><div style="margin-left:<%=indentValue-2 %>px; text-indent: -12px;"><ximg src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%=indentValue %>">
        &middot;&nbsp;<a name="<%=node.getContentName()%>" href="pop_contref_assoc.jsp?action=select&catId=<%= node.getParentNode().getContentName() %>&prodId=<%= node.getContentName() %>"><%= node.getFullName() %></a></div></td>
    <%  } else if (node.getContentType().equals(ContentNodeI.TYPE_BRAND)) { %>
	<tr valign="top">
        <td width="100%"><div style="margin-left:<%=indentValue-2 %>px; text-indent: -12px;"><ximg src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%=indentValue %>">
        &middot;&nbsp;<a name="<%=node.getContentName()%>" href="pop_contref_assoc.jsp?action=select&brandId=<%= node.getContentName() %>&brandNodes=true"><%= node.getFullName() %></a></div></td>
<%      } %>
	</tr>
</sa:Tree>

<!-- ******************************** -->
<TR>
<TD WIDTH="100%" COLSPAN="3">
<A HREF="javascript:soon()"><img src="images/folder_icon_products.gif" width="12" height="9" vspace="1" border="0"></A>&nbsp;<A HREF="javascript:soon()">Products</A><BR>
</TD>
</TR>
</TABLE>
</DIV>

</TD>
</TR>
</TABLE>
<% } // End of check to see if we are closing the window %>
</body>
</sa:ContentRefSelection>

</html>