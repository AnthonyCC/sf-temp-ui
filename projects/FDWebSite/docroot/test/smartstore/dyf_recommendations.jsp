<%@ page import="com.freshdirect.framework.webapp.ActionResult" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/common/template/no_shell.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
<%
FDUserI user = (FDUserI) session.getAttribute("fd.user");

request.setAttribute("skipDYFCheck", Boolean.TRUE);

ActionResult result = new ActionResult();
%><%@ include file="/includes/smartstore/i_dyf.jspf" %>
</tmpl:put>
</tmpl:insert>