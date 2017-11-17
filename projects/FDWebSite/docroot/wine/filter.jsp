<%@page import="com.freshdirect.storeapi.content.DomainValue"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.EnumTrackingSource"%>
<%@ taglib uri="template" prefix="tmpl"%>
<%@ taglib uri="freshdirect" prefix="fd"%>

<fd:CheckLoginStatus />
<% request.setAttribute("trkSource", EnumTrackingSource.WINE_FILTER);%>
<tmpl:insert template="/common/template/usq_sidenav.jsp">
	<%
	
	//
	String pageName = "Wine Filter";
	final String wfParam = request.getParameter("wineFilter");
	if (wfParam != null && !"".equals(wfParam)) {
		if (wfParam.indexOf(",") == -1) {
			DomainValue dv = (DomainValue) ContentFactory.getInstance().getContentNode( wfParam );
			if (dv != null)
				pageName = dv.getLabel();
		}
	}
	
	String title = "FreshDirect - " + pageName;
	%>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="<%= title %>"/>
  </tmpl:put>
  <tmpl:put name="title" direct="true"><%= title %></tmpl:put>

	<tmpl:put name="content" direct="true">
	<jsp:include page="/includes/layouts/wine_filter.jsp"/>
	</tmpl:put>

</tmpl:insert>
