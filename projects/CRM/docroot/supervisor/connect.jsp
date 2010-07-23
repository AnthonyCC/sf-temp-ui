<%@ page errorPage="/main/error.jsp"%>
<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="java.util.*"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources >Givex Admin</tmpl:put>
		<tmpl:put name='content' direct='true'>
			<jsp:include page="/includes/supervisor_nav.jsp" />

			<iframe src ="/supervisor/connect.html" width="100%" height="500">
				<p>Your browser does not support iframes.</p>
			</iframe>

		</tmpl:put>
</tmpl:insert>