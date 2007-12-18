<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>

<%
String servletContext = request.getContextPath();
String action = request.getParameter("action")==null ? "createBrand" : request.getParameter("action");
System.out.println("*&*&*&*&**& This is before the tag...****");
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>
<tmpl:put name='title' direct='true'>FD Admin - Add Brand</tmpl:put>
<tmpl:put name='header' direct='true'>Add Brand</tmpl:put>
<tmpl:put name='content' direct='true'>
<sa:BrandController action='<%=action%>' result='result' nodeId='<%=request.getParameter("nodeId")%>' id='brandModel'>
<%
System.out.println(" *^*^*^*^*^** This is right after the TAG *********** ");
System.out.println(" session has :"+(BrandModel)session.getAttribute(com.freshdirect.fdstore.content.BrandModel.class.getName()));
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}

System.out.println("Brand Name = "+brandModel.getFullName());
System.out.println("Brand Id = "+brandModel.getContentName());
System.out.println("Brand PK = "+brandModel.getPK());
System.out.println("Last Modified by "+brandModel.getLastModifiedBy());
if (brandModel.getLastModified()!=null) {
	System.out.println("Last Modified at "+brandModel.getLastModified().toLocaleString());
}
String submitButtonText="add";

if (brandModel.getPK()!=null) {
	action = "updateBrand";
	submitButtonText = "update"; 
} else {
	action="createBrand";
	submitButtonText="add";
}

%>
<form name="addBrand" method="post">
<div style="float:right;width:98%;height:50%;overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<%-- display message for type:error --%>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Technical Error'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='DuplicateName'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="20"></td></tr>
<tr><td width="25%" align="right">Brand Id&nbsp;&nbsp;</td><td width="75%"><input name="brandId" type="text" value="<%=brandModel.getContentName()%>" style="width:200px;" size="20" class="textbox2"></td>
</tr>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='brandId'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
<tr><td align="right">Brand Name&nbsp;&nbsp;</td><td><input name="brandName" type="text" value="<%=brandModel.getFullName()%>" style="width:200px;" size="20" class="textbox2"></td>
</tr>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='brandName'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
	<input type="hidden" name="action" value="<%=action%>">
</table>
</div>
<tmpl:put name='button' direct='true'>
<table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="javascript:addBrand.submit();" class="button">&nbsp;<%=submitButtonText%>&nbsp;</a></td></tr></table>
</tmpl:put>
</form>
</sa:BrandController>
</tmpl:put>

</tmpl:insert>

