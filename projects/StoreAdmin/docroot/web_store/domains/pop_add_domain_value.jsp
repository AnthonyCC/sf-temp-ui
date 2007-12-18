<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%
String servletContext = request.getContextPath();
String domainName = request.getParameter("domainName")!=null? request.getParameter("domainName") : "";
String valueOfDomain = request.getParameter("valueOfDomain")!=null ?  request.getParameter("valueOfDomain") : "";
String displayLabel = request.getParameter("displayLabel")!=null ? request.getParameter("displayLabel") : "";
String priority = request.getParameter("priority")!=null ? request.getParameter("priority") : "";
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - Add Domain Value</tmpl:put>
<tmpl:put name='header' direct='true'>Add Domain Value:  Domain:<b><%=domainName%></b></tmpl:put>
<tmpl:put name='content' direct='true'>
<sa:DomainController  action="createDomainValue" result="result">
<%
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}%>
    <form name="addDomainValue" method="post"><input type="hidden" value="<%=domainName%>">
        <div style="float:right;width:98%;height:50%;overflow-y:scroll;">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
                <fd:ErrorHandler id='errMsg' result='<%=result%>' name='error'>
                    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
                </fd:ErrorHandler>
                <fd:ErrorHandler id='errMsg' result='<%=result%>' name='domainName'>
                    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
                </fd:ErrorHandler>
                <tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="20"></td></tr>
                <tr><td width="25%" align="right">Value&nbsp;&nbsp;</td><td width="75%"><input name="valueOfDomain" type="text" value="<%=valueOfDomain%>" style="width:200px;" size="20" class="textbox2"></td>
                </tr>
                <fd:ErrorHandler id='errMsg' result='<%=result%>' name='valueOfDomain'>
                    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
                </fd:ErrorHandler>
                <tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
                <tr><td align="right">Label&nbsp;&nbsp;</td><td><input name="displayLabel" type="text" value="<%=displayLabel%>"style="width:200px;" size="20" class="textbox2"></td>
                </tr>
                <fd:ErrorHandler id='errMsg' result='<%=result%>' name='displayLabel'>
                    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
                </fd:ErrorHandler>
                <tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
                <tr><td align="right">priority&nbsp;&nbsp;</td><td><input name="priority" type="text" value="<%=priority%>" style="width:200px;" size="20" class="textbox2"></td>
                </tr>
                <fd:ErrorHandler id='errMsg' result='<%=result%>' name='priority'>
                    <tr><td align="center" colspan="2"> <font class="error"><%=errMsg%></font></td></tr>
                </fd:ErrorHandler>
            </table>
        </div>
        <tmpl:put name='button' direct='true'>
        <table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="javascript:addDomainValue.submit();" class="button">&nbsp;add&nbsp;</a></td></tr></table>
        </tmpl:put>
    </form>
</sa:DomainController>
</tmpl:put>

</tmpl:insert>