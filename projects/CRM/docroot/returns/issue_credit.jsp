<%@page import="com.freshdirect.common.customer.EnumCardType"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.*" %>
<%@ page import="com.freshdirect.storeapi.content.*"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.mail.*" %>
<%@ page import="com.freshdirect.framework.xml.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.ComplaintUtil" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.MathUtil" %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.ErpServicesProperties"%>
<%@ page import="com.freshdirect.crm.CrmCaseConstants" %>
<%@ page import="com.freshdirect.webapp.util.JSHelper" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
String errorLineColor = "#FFCCCC";
String bgcolor="#CCCCFF";
ContentFactory contentfactory = ContentFactory.getInstance();

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

// Include YUI selector and buttons API
request.setAttribute("__yui_load_selector__", Boolean.TRUE);
request.setAttribute("__yui_load_buttons__", Boolean.TRUE);

String orderId = (String) request.getParameter("orderId");

boolean isClassicView = "dept".equalsIgnoreCase(request.getParameter("view"));
%>
<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>
<%@ include file="/includes/i_globalcontext.jspf"%>
<tmpl:put name='title' direct='true'>Order <%= orderId%> Issue Credits</tmpl:put>

</tmpl:insert>