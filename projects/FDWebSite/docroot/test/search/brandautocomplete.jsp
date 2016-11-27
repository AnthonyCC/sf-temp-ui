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
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' lang="en-US">
	<link rel="stylesheet" href="/assets/css/autocomplete.css" type="text/css">
	<%@ include file="/includes/search/brandautocomplete.jspf" %>
	<%@ include file="/includes/search/autocomplete.jspf" %>
</head>
<body>
	<div>
	<h1>Standard product autocomplete</h1>
		<div style="clear: both; font-size: 0px;"></div>
	</div>
	<div id="searchContainer" style="position: relative">
		<INPUT TYPE="text" style="width:140px;" id="searchxParams" name="searchParams" value="" size="16" maxlength="50" class="text11">
		<div id="terms" style="position: absolute;background-color: white" class="termsStyle"></div>
	</div>
	<h1>Brand name autocomplete</h1>
	<p>to use it: include <span style="text-decoration:italic">/includes/search/brandautocomplete.jspf</span> in the page and create this html fragment:<code><br>
	&lt;div id="brandSearchContainer" style="position: relative"&gt;<br>
		&lt;INPUT TYPE="text" style="width:140px;" id="brandParams" name="brandParams" value="" size="16" maxlength="50" class="text11"&gt;<br>
		&lt;div id="brands" style="position: absolute;background-color: white" class="termsStyle"&gt;&lt;/div&gt;<br>
	&lt;/div&gt;<br>
	</code> </p>
	<div id="brandSearchContainer" style="position: relative">
		<INPUT TYPE="text" style="width:140px;" id="brandParams" name="brandParams" value="" size="16" maxlength="50" class="text11">
		<div id="brands" style="position: absolute;background-color: white" class="termsStyle"></div>
	</div>
	<br />
	<select id="">
		<option value="" selected="selected">test select box 1</option>
		<option value="">test select box 2</option>
	</select>
	<br />
	<textarea id="" rows="" cols=""></textarea>
	<br />
	<input type="text" id="" />
</body>
</html>
