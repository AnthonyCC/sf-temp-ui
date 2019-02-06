<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ include file="/includes/i_order_comparators.jspf"%>

<tmpl:insert template='/template/top_nav.jsp'>

<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDIdentity identity = user.getIdentity();
    // Get return page value
	String successPage = request.getParameter("successPage") != null ? request.getParameter("successPage") : "/main/account_details.jsp";
%>



</tmpl:insert>


