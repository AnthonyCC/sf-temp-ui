<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import='com.freshdirect.webapp.util.*,
				   java.io.InputStream,
				   com.freshdirect.cms.fdstore.*'
%><%@ page import="com.freshdirect.framework.webapp.*"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.*"
%><%@ page import="com.freshdirect.content.attributes.*"
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"
%><%@ page import="com.freshdirect.fdstore.*"
%><%@ page import="com.freshdirect.fdstore.content.*"
%><%@ page import='com.freshdirect.fdstore.attributes.*'
%><%@ page import="java.util.*"
%><%@ page import="java.net.URLEncoder"
%><%@ page import="java.text.DecimalFormat"
%><%@ page import="com.freshdirect.framework.util.NVL"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.webapp.taglib.test.SnapshotGenerator"%>
<%@page import="com.freshdirect.smartstore.fdstore.ProductStatisticsProvider"%>
<%@page import="com.freshdirect.cms.ContentKey.InvalidContentKeyException"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<html>  
<head>
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>
	<%@ include file="/includes/search/autocomplete.jspf" %>
	<%@ include file="/includes/search/brandautocomplete.jspf" %>
</head>
<body>
	<h1>Standard product autocomplete</h1>
	<div id="searchContainer" style="position: relative">
		<INPUT TYPE="text" style="width:140px;" id="searchxParams" name="searchParams" value="" size="16" maxlength="50" class="text11">
		<div id="terms" style="position: absolute;background-color: white"></div>
	</div>
	<h1>Brand name autocomplete</h1>
	<p>to use it: include <span style="text-decoration:italic">/includes/search/brandautocomplete.jspf</span> in the page and create this html fragment:<code><br>
	&lt;div id="brandSearchContainer" style="position: relative"&gt;<br>
		&lt;INPUT TYPE="text" style="width:140px;" id="brandParams" name="brandParams" value="" size="16" maxlength="50" class="text11"&gt;<br>
		&lt;div id="brands" style="position: absolute;background-color: white"&gt;&lt;/div&gt;<br>
	&lt;/div&gt;<br>
	</code> </p>
	<div id="brandSearchContainer" style="position: relative">
		<INPUT TYPE="text" style="width:140px;" id="brandParams" name="brandParams" value="" size="16" maxlength="50" class="text11">
		<div id="brands" style="position: absolute;background-color: white"></div>
	</div>
</body>
</html>
