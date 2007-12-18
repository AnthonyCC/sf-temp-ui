<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.content.EnumDomainType" %>

<%
String servletContext = request.getContextPath();
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - Add Domain</tmpl:put>
<tmpl:put name='header' direct='true'>Add Domain</tmpl:put>

<tmpl:put name='content' direct='true'>
<sa:DomainController action='createDomain' result='result'>
<%
String domainName = request.getParameter("domainName")!=null? request.getParameter("domainName") : "";
String domainType = request.getParameter("domainType")!=null ?  request.getParameter("domainType") : "";
String displayLabel = request.getParameter("displayLabel")!=null ? request.getParameter("displayLabel") : "";


Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}
%>
<form name="addDomain" method="post">
<div style="float:right;width:98%;height:50%;overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<%-- display message for type:error --%>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='error'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="20"></td></tr>
<tr><td width="25%" align="right">Name&nbsp;&nbsp;</td><td width="75%"><input name="domainName" type="text" value="<%=domainName%>" style="width:200px;" size="20" class="textbox2"></td>
</tr>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='domainName'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
<tr><td align="right">Label&nbsp;&nbsp;</td><td><input name="displayLabel" type="text" value="<%=displayLabel%>" style="width:200px;" size="20" class="textbox2"></td>
</tr>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='displayLabel'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
<tr><td align="right">Type&nbsp;&nbsp;</td><td><select name="domainType" style="width:200px;" class="textbox2">
<%
List domainTypes = EnumDomainType.getAttributeTypes();
for (Iterator itrTypes=domainTypes.iterator();itrTypes.hasNext(); ) {
    EnumDomainType dmt = (EnumDomainType)itrTypes.next();
    String selectedOption=dmt.getName().equals(domainType) ? "selected" : "";
%>
<option  <%=selectedOption%> value='<%=dmt.getId()%>'><%=dmt.getName()%>
<% } %>
</select></td>
 </tr>
<fd:ErrorHandler id='errMsg' result='<%=result%>' name='domainType'>
    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
</fd:ErrorHandler>
</table>
</div>
<tmpl:put name='button' direct='true'>
<table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="javascript:addDomain.submit();" class="button">&nbsp;add&nbsp;</a></td></tr></table>
</tmpl:put>
</form>
</sa:DomainController>
</tmpl:put>

</tmpl:insert>

