<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>

<%!
public static SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a MM/dd/yyyy");
%>

<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
System.out.println("!!!! this is the action in: "+request.getParameter("action"));
String action = request.getParameter("action")==null ? "create" : request.getParameter("action");
String nodeId = request.getParameter("nodeId");

String cancelHref = "attributes.jsp?"+request.getQueryString();
String saveHref = "javascript:brandDetailsForm.submit();";
System.out.println(" ################### just before tag: request = "+request.getParameter("action"));
%>
<sa:BrandController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='brandModel'>
<%
// *************************  debug stuff ************************
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}

String submitButtonText="add";


//************************** End of Debug Stuff *******************

%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
	<form name="brandDetailsForm" method="post" action="attributes.jsp">
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td class="breadcrumb">Current Folder: <a href="<%= servletContext %>/web_store/brands/index.jsp">Brands</a> / <a href=""><%=brandModel.getFullName()%>></a></td>
		<td align="right">
			<table cellpadding="1" cellspacing="3" border="0">
			<tr align="center" valign="middle">
			<td bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
			</table>
		</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr bgcolor="#000000"><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('properties'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader">View/Edit Folder Properties</th>
	<th align="right">
		<table width="100%" cellpadding="0">
		<tr>
		<td align="right" class="tabDetails">Last Modified:<%=(brandModel.getLastModified()==null) ? "" : dateFormatter.format(brandModel.getLastModified())%> by: <%=brandModel.getLastModifiedBy()%></td>
		</tr>
		</table>
	</th>
	</tr>
	</table>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Technical Error'>
    <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='DuplicateName'>
    <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='nodeId'>
    <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Brand ID</td>
	<td width="45%">
<% 	if (brandModel.getPK()==null) { %>
	<input name="brandId" type="text" value="<%=brandModel.getContentName()%>" style="width:250px; font-size:9pt;" size="25" class="textbox2">
	<input type="hidden" name="action" value="create">
<%	} else { %>
	<%=brandModel.getContentName() %>
	<input name="action" type="hidden" value="update">
	<input type="hidden" name="brandId" value="<%=brandModel.getContentName()%>">
<% } %>
	</td>
	<td width="20%">&nbsp;</td>
	</tr>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='brandId'>
    <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Brand Name</td>
	<td><input name="brandName" type="text" value="<%=brandModel.getFullName()%>" style="width:250px; font-size:9pt;" size="25" class="textbox2"></td>
	<td>&nbsp;</td>
	</tr>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='brandName'>
    <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	</table>
<div>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
	<tr bgcolor="#000000"><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('brandAttributes'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader">View/Edit Folder Attributes</th></tr>
	</table>
	
	<div id="brandAttributes" style="display= none;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('brandAttributes1'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">PROMOS</td>
	</tr>
	</table>
	<div id="brandAttributes1" style="display= none;">
		<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_prod_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FEATURED_PRODUCTS','l');">FEATURED_PRODUCTS</a></td>
		<td width="45%"><a href="#">Product Reference</a></td>
		<td width="20%"><a href="#" title="attribute from: /bread/crumb/link | value from: /bread/crumb/link" class="icon">&nbsp;<b>i</b>&nbsp;</a></td>
		</tr>
		</table>
	</div>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('brandAttributes2'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - IMAGES</td>
	</tr>
	</table>
<%
	String brand_logo = "";
	String brand_logo_medium="";
	String brand_logo_small="";
	String brand_popup="";
	
	Attribute brandAttrib = brandModel.getAttribute("BRAND_LOGO");
	if (brandAttrib!=null) {
		brand_logo = ((MediaModel)brandAttrib.getValue()).getPath().replace('/','\\');
	}
	
	brandAttrib = brandModel.getAttribute("BRAND_LOGO_MEDIUM");
	if (brandAttrib!=null) {
		brand_logo_medium = ((MediaModel)brandAttrib.getValue()).getPath().replace('/','\\');
	}
	brandAttrib = brandModel.getAttribute("BRAND_LOGO_SMALL");
	if (brandAttrib!=null) {
		brand_logo_small = ((MediaModel)brandAttrib.getValue()).getPath().replace('/','\\');
	}
	brandAttrib = brandModel.getAttribute("BRAND_POPUP");
	if (brandAttrib!=null) {
		brand_popup = ((MediaModel)brandAttrib.getValue()).getPath().replace('/','\\');
	}
%>

	<div id="brandAttributes2" style="display= none;">
		<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">
		<a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=BRAND_LOGO','l');">BRAND LOGO</a></td>
		<td width="45%"><%=brand_logo%></td>
		<td width="20%"><a href="#" title="attribute from: /bread/crumb/link | value from: /bread/crumb/link" class="icon">&nbsp;<b>i</b>&nbsp;</a></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>
		<a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=BRAND_LOGO_MEDIUM','l');">BRAND LOGO MEDIUM</a></td>
		<td><%=brand_logo_medium%></td>
		<td><a href="#" title="attribute from: /bread/crumb/link | value from: /bread/crumb/link" class="icon">&nbsp;<b>i</b>&nbsp;</a></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>
		<a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=BRAND_LOGO_SMALL','l');">BRAND LOGO SMALL</a></td>
		<td><%=brand_logo_small%></td>
		<td><a href="#" title="attribute from: /bread/crumb/link | value from: /bread/crumb/link" class="icon">&nbsp;<b>i</b>&nbsp;</a></td>
		</tr>
		</table>
	</div>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('brandAttributes3'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - MIXED</td>
	</tr>
	</table>
	<div id="brandAttributes3" style="display= none;">
		<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">
		<a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=BRAND_POPUP_CONTENT','l');">BRAND POPUP</a></td>
		<td width="45%">Titled Media</td>
		<td width="20%"><a href="#" title="attribute from: /bread/crumb/link | value from: /bread/crumb/link" class="icon">&nbsp;<b>i</b>&nbsp;</a></td>
		</tr>
		</table>
	</div>

	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	</div>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	<tr bgcolor="#000000"><th class="sectionHeader" width="2%">&nbsp;</th><th class="sectionHeader">Associations</th></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr class="colHeader">
	<td width="2%">&nbsp;</td>
	<td width="25%">Property/Attribute</td>
	<td width="15%">Type</td>
	<td width="25%">ID</td>
	<td width="35%">Name</td>
	</tr>
	<tr> 
	<td>&nbsp;</td>
	<td>brand 1</td>
	<td>product</td>
	<td>bblk_flet_untrmd</td>
	<td>Denmark Blue Castello</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>brand 2</td>
	<td>product</td>
	<td>bblk_fflt_untrmd</td>
	<td>Something something something</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>brand 3</td>
	<td>product</td>
	<td>bblk_flet_untrmd_blah_blah</td>
	<td>Roast Suckling Pig</td>
	</tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	</div>
	
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" bgcolor="#CCCCCC"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
		<td bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table><br>
	</td>
	</tr>
	</table>
	</div> <%-- end scroll section --%>
	</form>
    </tmpl:put>
	
</tmpl:insert>
</sa:BrandController>