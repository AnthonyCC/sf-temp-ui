<%@ page import='java.util.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.FDSessionUser'
%><%@ page import='com.freshdirect.cms.CmsServiceLocator'
%><%@ page import='com.freshdirect.cms.contentio.xml.XmlContentMetadataService'
%><%@ page import="com.freshdirect.fdstore.EnumEStoreId"%><%
	FDSessionUser userx = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);
%><!--
Server: <%= request.getServerName() %>:<%= request.getServerPort() %>
EStoreId: <%= userx != null ? userx.getUserContext().getStoreContext().getEStoreId() : EnumEStoreId.FD %>
<% 
    XmlContentMetadataService metadataService = CmsServiceLocator.xmlContentMetadataService();
    if(metadataService != null){
        %>
        <%="type -> " + metadataService.getType()%>
        <%="description -> " + metadataService.getDescription()%>
        <%="date -> " + metadataService.getDate()%>
        <%
    } else {
        %>
        <%="No metadata available"%>
        <%
    }
%>-->