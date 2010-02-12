<%@ page import='java.text.*' %>

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.*'%>
<%@ page import='com.freshdirect.cms.node.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
    RecipeDepartment recipeDepartment = (RecipeDepartment) ContentFactory.getInstance().getContentNode("rec");

    ContentFactory contentFactory = ContentFactory.getInstance();
    ContentNodeModel recipeCategory = contentFactory.getContentNode(request.getParameter("catId"));

	//
	// Get index of current search term
	//
	int ix = 0;
	if (request.getParameter("searchIndex") != null) {
		ix = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Browse Items</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<%@ include file='/shared/includes/layouts/i_recipe_cat_body.jspf'%>
</div>
<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>
