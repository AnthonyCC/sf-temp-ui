<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(request.getParameter("catId"));%>
<%@ include file="/common/template/includes/catLayoutManager.jspf" %>