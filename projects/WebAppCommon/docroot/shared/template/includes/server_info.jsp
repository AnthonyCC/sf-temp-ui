<%@ page import='com.freshdirect.framework.conf.FDRegistry'
%><%@ page import='com.freshdirect.cms.application.service.*'
%><%@ page import='java.util.*' %>
<!--
Server: <%= request.getServerName() %>:<%= request.getServerPort() %>
<% 
	ResourceInfoServiceI resourceService = (ResourceInfoServiceI) FDRegistry.getInstance().getService(ResourceInfoServiceI.class);
	for(Iterator i = resourceService.getInfoLog().iterator();i.hasNext();){
		String line = (String) i.next();
%><%=line%>	
<% } %>-->
