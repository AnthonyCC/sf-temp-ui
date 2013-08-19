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

<tmpl:insert template='/template/supervisor_resources.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources >Paymentech Gateway</tmpl:put>
		<tmpl:put name='content' direct='true'>

			<iframe src ="https://securevar.paymentech.com/signin/pages/login.faces?ct_orig_uri=/manager/loginAction.do?timeout=Y" width="100%" height="100%">
				<p>Your browser does not support iframes.</p>
			</iframe>

		</tmpl:put>
</tmpl:insert>