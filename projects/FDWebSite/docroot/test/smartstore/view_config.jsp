<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.smartstore.impl.AbstractRecommendationService"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%

Iterator it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

String[] siteFeaturesArray = { "DYF", "FEATURED_ITEMS", "FAVORITES" };
List siteFeatures = Arrays.asList(siteFeaturesArray);

Map varMap = new HashMap();

VariantSelection helper = VariantSelection.getInstance();

Map cohorts = helper.getCohorts();

/* redirect */
String newURL = urlG.build();

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	return;
}

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VARIANT CONFIGURATIONS PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

body{margin:20px 60px;color:#333333;background-color:#fff;}
input{font-weight:normal;}
p{margin:0px;padding:0px 0px 8px;}
p.head{padding:10px 0px 20px;}
a{color:#336600;}.test-page a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
table.t1{width:auto;margin-bottom:20px;}
table.t1 td{border:1px solid black;padding:4px 8px;}
table.t1 td.sf{background-color:#ccc;}
table.t1 td.space{border-width:1px 0px;}
info{color:red}

	</style>
</head>
<body>
<%
	if (request.getParameter("refresh")!=null) {
    	SmartStoreServiceConfiguration.getInstance().refresh();
%>
   	<span class="text11bold info" colspan="2" title="Configuration reloaded">Configuration reloaded</span>
<% 
	}
%>

   	<%
	it = siteFeatures.iterator();
	while (it.hasNext()) {
		String sf = (String) it.next();
		Map curVars = SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.getEnum(sf));
		varMap.put(sf, curVars);
   	%>
    <h1 title="Site Feature"><%= sf %></h1>
   	<%
 		Iterator it2 = curVars.keySet().iterator();
   		if (!it2.hasNext()) {
   	%>
   	<p>No configuration.</p>
   	<%
   		} else {
   	%>
    <table class="t1">
    	<%
    		boolean notFirst = false;
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
   				RecommendationService service = (RecommendationService) curVars.get(varId);
    			Variant variant = service.getVariant();
    			if (notFirst) {
    	%>
    	<tr><td class="text13bold space" colspan="2">&nbsp;</td>
    	</tr>
    	<%
    			}
    	%>
    	<tr>
    	<td class="text13bold var" colspan="2" title="Variant">
    		<%= varId %>
    	</td>
    	</tr>
    	<tr>
    	<td class="text13 left" title="Parameter Name">Variant type </td>
    	<td class="text13" title="Parameter Value"><%= variant.getServiceConfig().getType().getName() %> (<%= service.getClass().getName() %>)</td>
    	</tr>
    	<tr>
    	<td class="text13 left" title="Parameter Name">Description</td>
    	<td class="text13" title="Parameter Value"><%= service.getDescription() %></td>
    	</tr>
    	<tr>
    	<td class="text13 left" title="Parameter Name"><%= AbstractRecommendationService.CAT_AGGR %></td>
    	<td class="text13" title="Parameter Value"><%= "" + "true".equals(variant.getServiceConfig()
    				.get(AbstractRecommendationService.CAT_AGGR)) %></td>
    	</tr>
    	<tr>
    	<td class="text13 left" title="Parameter Name"><%= AbstractRecommendationService.INCLUDE_CART_ITEMS %></td>
    	<td class="text13" title="Parameter Value"><%= "" + Boolean.valueOf(
    				variant.getServiceConfig().get(
    				AbstractRecommendationService.INCLUDE_CART_ITEMS)).booleanValue() %></td>
    	</tr>
    	<%
    			Iterator it3 = variant.getServiceConfig().keys().iterator();
    			while (it3.hasNext()) {
    				String paramName = (String) it3.next();
    				String paramValue = variant.getServiceConfig().get(paramName);
    				if (AbstractRecommendationService.INCLUDE_CART_ITEMS.equals(paramName) ||
    						AbstractRecommendationService.CAT_AGGR.equals(paramName)) {
    					continue;
    				}
    	%>
    	<tr>
    	<td class="text13 left" title="Parameter Name"><%= paramName %></td>
    	<td class="text13" title="Parameter Value"><%= paramValue %></td>
    	</tr>
    	<%
    			}
				notFirst = true;
   			}
	  	%>
  	</table>
    <%
    	}
   	}
	%>
    <p>Hover over the items in the table to view their meanings.</p>
<a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a>
</body>
</html>