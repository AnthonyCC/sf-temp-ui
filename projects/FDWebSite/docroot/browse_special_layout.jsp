<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(request.getParameter("catId"));
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);%> %>
<%@ include file="/common/template/includes/catLayoutManager.jspf" %>