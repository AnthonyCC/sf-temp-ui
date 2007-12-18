<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
String URL = request.getRequestURI() + "?" + request.getQueryString();
String domId = request.getParameter("domId");
if (domId==null) domId="";
Domain domain = ContentFactory.getInstance().getDomainById(domId);
%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <sa:DomainController result='result' action='<%=request.getParameter("action")%>'>
<%
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}
    if (domain!=null) {
        EnumDomainType domainType = domain.getDomainType();
        EnumAttributeType attribtype = domainType.getAttributeType();
        List domainValues = domain.getDomainValues();
%>
    <tmpl:put name='content' direct='true'>
	<form name="updateDomain" method="post"><input type="hidden" name="action" value="updateDomain"><input type="hidden" name="domainName" value="<%=domain.getName()%>">
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td align="right">
			<table cellpadding="1" cellspacing="3" border="0">
			<tr align="center" valign="middle">
			<td bgcolor="#CC0000" class="button"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td bgcolor="#006600" class="button"><a href="javascript:updateDomain.action.value='updateDomain';updateDomain.submit();" class="button">&nbsp;SAVE&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
			</table>
		</td>
	</tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr bgcolor="#000000"><th colspan="4" class="sectionHeader">&nbsp;Domain</th>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
        <fd:ErrorHandler id='errMsg' result='<%=result%>' name='error'>
            <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
        </fd:ErrorHandler>
        <fd:ErrorHandler id='errMsg' result='<%=result%>' name='domainName'>
            <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
        </fd:ErrorHandler>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">ID</td>
	<td width="45%"><%=domain.getPK().getId()%></td>
	<td width="20%">&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Domain Type</td>
	<td><%=domainType.getName()%>(<%=attribtype.getName()%>);</td>
	<td>&nbsp;</td>
	</tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Name</td>
	<td width="45%"><%=domain.getName()%></td>
	<td width="20%">&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Store_ID</td>
	<td>123456</td>
	<td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Label</td>
	<td><input name="domainDisplayLabel" type="text" style="width:80px;" size="8" class="textbox2" value="<%=domain.getLabel()%>"></td>
	<td>&nbsp;</td>
	</tr>
        <fd:ErrorHandler id='errMsg' result='<%=result%>' name='domainDisplayLabel'>
            <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
        </fd:ErrorHandler>
<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr bgcolor="#000000">
		<th class="sectionHeader">&nbsp;Domain Values</th>
		</tr>
		<tr>
		<td><table cellpadding="1">
			<tr>
			<td class="menu"><a href="javascript:popup('pop_add_domain_value.jsp?domainName=<%=domain.getName()%>','s');"><font size="4">+</font> <b>add</b></a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td class="menu"><a href="javascript:updateDomain.action.value='deleteDomainValue';updateDomain.submit();" class="button"><font size="4">&ndash;</font> <b>delete</b></a></td>
			</tr>
			</table>
		</td>
		</tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
<%
        // show the errors that occurred 
        for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
            ActionError ae = (ActionError)itE.next();
            if (!ae.getType().startsWith("displayLabel_") && !ae.getType().startsWith("priority_")) continue;
            String anchorId = ae.getType().substring(ae.getType().indexOf("_")+1);
%>
          <tr><td colspan="5" align="left"><a href="#<%=anchorId%>"><font class="error"><%=ae.getDescription()%></font></a></td></tr>
<%      }%>

	<tr class="colHeader">
	<td width="3%">&nbsp</td>
	<td width="20%"><a href="#" class="colHeader">ID</a></td>
	<td width="30%"><a href="#" class="colHeader">Label</a></td>
	<td width="18%"><a href="#" class="colHeader">Value</a></td>
	<td width="19%"><a href="#" class="colHeader">Priority</a></td>
	</tr>
	</table>

        <div style="position:relative;width:100%;left:0;top:0;height:15%;overflow-y:scroll;">
        <table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
<%
    if (domainValues !=null && domainValues.size()>0) {
%>	
	
<%
       String screenPriority;
       String screenLabel;
       for(int i=0;i<domainValues.size();i++) {
          DomainValue domainValue = (DomainValue)domainValues.get(i);
          String domainValueId = domainValue.getPK().getId();
          screenLabel = request.getParameter("displayLabel_"+domainValueId);
          screenPriority = request.getParameter("priority_"+domainValueId);
          if (screenLabel==null) screenLabel = domainValue.getLabel();
          if (screenPriority==null) screenPriority = ""+domainValue.getPriority();
%>
	<tr>
	<td width="3%"><input name="delete_<%=domainValueId%>" type="checkbox" value="<%=domainValueId%>"></td>
	<td width="20%"><%=domainValueId%>
<%
        if(result.hasError("displayLabel_"+domainValueId) || result.hasError("priority_"+domainValueId)) { %>
            &nbsp;<a name="<%=domainValueId%>"></a><font class="error">!!</font>
<%      } %>

        </td>
	<td width="30%"><input name="displayLabel_<%=domainValueId%>" type="text" size="25" style="width:250px;" class="textbox1" value="<%=screenLabel%>"></td>
	<td width="18%"><%=domainValue.getValue()%></td>
	<td width="19%"><input name="priority_<%=domainValueId%>"type="text" size="4" class="textbox1" value="<%=screenPriority%>"></td>
	</tr>
<%      } %>
	
<%
   }
%>
        </table>
        </div><%-- end scroll section 1 --%>

	<div>
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" bgcolor="#CCCCCC"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
		<td bgcolor="#CC0000" class="button"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#006600" class="button"><a href="javascript:updateDomain.action.value='updateDomain';updateDomain.submit();" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table>
	</td>
	</tr>
	</table>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="5"></td></tr>
	<tr bgcolor="#000000"><th class="sectionHeader">&nbsp;Associations</th></tr>
	</table>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr class="colHeader">
	<td width="2%">&nbsp;</td>
	<td width="25%">Property/Attribute</td>
	<td width="15%">Type</td>
	<td width="25%">ID</td>
	<td width="35%">Name</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
	</table>
	</div>
	
	<div style="width:100%;height:10%;overflow-y:scroll;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr>
	<td width="2%">&nbsp;</td>
	<td width="25%">brand 1</td>
	<td width="15%">product</td>
	<td width="25%">bblk_flet_untrmd</td>
	<td width="35%">Denmark Blue Castello</td>
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
	</table>
	
	</div> <%-- end scroll section 2 --%>
    </tmpl:put>
<% }%>	
</sa:DomainController>
</tmpl:insert>