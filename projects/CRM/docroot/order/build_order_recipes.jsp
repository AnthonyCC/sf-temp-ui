<%@ page import='java.text.*' %>

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.*'%>
<%@ page import='com.freshdirect.cms.node.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
    FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
    RecipeDepartment recipeDepartment = (RecipeDepartment) ContentFactory.getInstance().getContentNode("rec");

	//
	// Get index of current search term
	//
	int idx = 0;
	if (request.getParameter("searchIndex") != null) {
		idx = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Browse Items</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>
<!--- start of recipe  -->
<div class="order_content">
<%@ include file='/shared/includes/layouts/i_recipe_dept_body.jspf'%>
<!---- !!!!END Of RECIPES  -->
</div>
<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>
