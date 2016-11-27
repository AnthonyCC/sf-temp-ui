<%@ page import='com.freshdirect.framework.conf.FDRegistry'
%><%@ page import='com.freshdirect.cms.application.service.*'
%><%@ page import='java.util.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.FDSessionUser'
%><%@ page import="com.freshdirect.fdstore.EnumEStoreId"%><%
	FDSessionUser userx = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);
%><!--
Server: <%= request.getServerName() %>:<%= request.getServerPort() %>
EStoreId: <%= userx != null ? userx.getUserContext().getStoreContext().getEStoreId() : EnumEStoreId.FD %>
<% 
	ResourceInfoServiceI resourceService = (ResourceInfoServiceI) FDRegistry.getInstance().getService(ResourceInfoServiceI.class);
	for (final String line : resourceService.getInfoLog()) {
		%><%=line%>	
		<%		
	}
%>-->