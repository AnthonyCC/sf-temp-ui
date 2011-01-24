<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.referral.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri="crm" prefix="crm" %>
<% 
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
FDCustomerModel customer = user.getFDCustomer();
String agentId = CrmSecurityManager.getUserName(request);
String agentRole = CrmSecurityManager.getUserRole(request);
%>
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Profile List</tmpl:put>
<tmpl:put name='content' direct='true'>

<%@ include file="/shared/includes/ss_cust_info.jspf" %>

</tmpl:put>
</tmpl:insert>
