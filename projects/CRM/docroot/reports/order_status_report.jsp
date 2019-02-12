<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Orders by Status</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/i_globalcontext.jspf" %>
<jsp:include page="/includes/reports_nav.jsp" />

</tmpl:put>

</tmpl:insert>