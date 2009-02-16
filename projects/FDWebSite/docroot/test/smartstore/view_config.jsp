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
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig "%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.impl.AbstractRecommendationService"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig.ConfigEntryStatus"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%

Iterator it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

List siteFeatures = new ArrayList();
for (Iterator fit=EnumSiteFeature.iterator(); fit.hasNext();) {
	EnumSiteFeature feature = (EnumSiteFeature) fit.next();
	if (feature.isSmartStore()) {
		siteFeatures.add(feature);
	}
}

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
table.t1{width:auto;margin-bottom:20px; border: 3px}
table.t1 td{border:1px solid black;padding:4px 8px;}
table.t1 td.sf{background-color:#ccc;}
table.t1 td.space{border-width:0px 0px;}
table.t1 td.in-use{border-width:0px;padding:10px 0px 4px;}
.info{color:red}
.no-use{color:#999 !important;border-color:#999 !important;}

	</style>
</head>
<body>
<%
	if (request.getParameter("refresh")!=null) {
    	SmartStoreServiceConfiguration.getInstance().refresh();
    	VariantSelectorFactory.refresh();
%>
   	<span class="text11bold info" title="Configuration reloaded">Configuration reloaded</span>
<% 
	}
%>
	<p><a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a></p>

   	<%
	it = siteFeatures.iterator();
	while (it.hasNext()) {
		EnumSiteFeature sf = (EnumSiteFeature) it.next();
		Map curVars = SmartStoreServiceConfiguration.getInstance().getServices(sf);
		Map sortedVars = SmartStoreUtil.getVariantsSortedInWeight(sf);
		varMap.put(sf, curVars);
   	%>
    <h1 title="Site Feature"><%= sf.getTitle() %> (<%= sf.getName() %>)</h1>
   	<%
 		Iterator it2 = sortedVars.keySet().iterator();
   		if (!it2.hasNext()) {
   	%>
   	<p>No configuration.</p>
   	<%
   		} else {
   	%>
    <table class="t1">
    	<%
    		boolean notFirst = false;
    		int inUse = -1;
    		int newInUse;
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
   				int weight = ((Integer) sortedVars.get(varId)).intValue();
   				String weiStr = weight > 0 ? "" : " no-use";
   				RecommendationService service = (RecommendationService) curVars.get(varId);
    			Variant variant = service.getVariant();
    			
    			// validate config
    			RecommendationServiceConfig cfg = variant.getServiceConfig();
    			RecommendationServiceConfig.ConfigStatus cfgStatus = cfg.validate(variant);
    			List configKeys = cfg.getConfigKeys(variant);
    			
    			// retrieve live (actual) configuration keys
				Map liveConfig = service.getConfiguration();

				if (notFirst) {
    	%>
    	<tr><td class="text13bold space<%= weiStr %>" colspan="2">&nbsp;</td>
    	</tr>
    	<%
    			}
    	%>
    	<tr>
    	<% 
    			newInUse = weight > 0 ? 1 : 0;
    			if (newInUse != inUse) {
    				inUse = newInUse;
    	%>
    	<td class="title18 in-use<%= weiStr %>" colspan="2">
    		<%= inUse > 0 ? "Variants in Use" : "Variants not in Use" %>
    	</td>
    	</tr>
    	<tr><td class="text13bold space<%= weiStr %>" colspan="2">&nbsp;</td>
    	</tr>
    	<%
    			}
    	%>
    	<tr>
    	<td class="text13bold var<%= weiStr %>" colspan="2" title="Variant">
    		<%= varId %>
    	</td>
    	</tr>
    	<tr>
    	<td class="text13 left<%= weiStr %>" title="Parameter Name">Variant type</td>
    	<td class="text13<%= weiStr %>" title="<%= service.getClass().getName() %>"><%= variant.getServiceConfig().getType().getName() %></td>
    	</tr>
    	<tr>
    	<td class="text13 left<%= weiStr %>" title="Parameter Name">Customers percentage</td>
    	<td class="text13<%= weiStr %>" title="Parameter Value"><%= weight %>%</td>
    	</tr>
    	<tr>
    	<td class="text13 left<%= weiStr %>" title="Parameter Name">Description</td>
    	<td class="text13<%= weiStr %>" title="Parameter Value"><%= service.getDescription() %></td>
    	</tr>
    	<%
				for (Iterator keyIt = configKeys.iterator(); keyIt.hasNext(); ) {
					String configKey = (String) keyIt.next();
					RecommendationServiceConfig.ConfigEntryStatus ces = cfgStatus.get(configKey);
					RecommendationServiceConfig.ConfigEntryInfo info = ces.getInfo();

					// active value (may be null if variant does not have such a parameter
					Object activeValue = liveConfig.get(configKey);
					// configured value
					String configValue = cfg.get(ces.getKeyName());
					boolean sameValue = false;
					
					// Let's check whether they are same
					if (activeValue != null && configValue != null) {
						if (activeValue instanceof String) {
							sameValue = ( ((String)activeValue).equalsIgnoreCase(configValue) );
						} else if (activeValue instanceof Integer) {
							sameValue = ( ((Integer)activeValue).equals( Integer.valueOf(configValue) ) );
						} else if (activeValue instanceof Float) {
							sameValue = ( ((Float)activeValue).equals( Float.valueOf(configValue) ) );
						} else if (activeValue instanceof Double) {
							sameValue = ( ((Double)activeValue).equals( Double.valueOf(configValue) ) );
						} else {
							// if no active value regard it 'same'
							sameValue = activeValue == null || (activeValue != null && activeValue.toString().equalsIgnoreCase(configValue));
						}
					} else {
					    // handle null - null case ... somehow ...
					    sameValue = (activeValue == configValue); 
					}
					
		%>
    	<tr>
		<%		
					if (ces.isValid()) {
						// key exists and has value
		%>
			<td class="text13 left<%= weiStr %>" style="font-weight: bold;" title="<%= ces.getKeyDesc() %>"><%= ces.getKeyName() %></td>
	    	<td class="text13<%= weiStr %>" title="Parameter Value"><%
	    				if (sameValue) {
	    					%><%= activeValue %><%
	    				} else {
	    					// the two values differ
%>				<div>ACTUAL: <%= activeValue %></div>
				<div>Configured: <%= configValue %></div>
	    					<%
	   					}
%>			</td>
		<%
					} else if (ces.isUnconfigured()) {
						// key does not exists - default value used instead
						
		%>
	    	<td class="text13 left<%= weiStr %>" style="font-style: italic;" title="<%= ces.getKeyDesc() %>"><%= ces.getKeyName() %></td>
	    	<td class="text13<%= weiStr %>" style="font-style: italic;" title="Parameter Value"><%= activeValue != null ? activeValue.toString() : "<b>(null)</b>" %></td>
		<%
					} else if (ces.isInvalid()) {
						// not used key - maybe it's mispelled
		%>
	    	<td class="text13 left<%= weiStr %>" style="color: red;" title="<%= ces.getKeyDesc() %>"><%= ces.getKeyName() %></td>
	    	<td class="text13<%= weiStr %>" style="color: red;" title="Parameter Value"><%= info == null ? "(no default value)" : info.getDefaultValue() %></td>
		<%
					}
		%>
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
    <div>
    	Legend for variant configuration parameters:
    	<ul>
    		<li><b>Bold</b> - defined and valid</li>
    		<li><i>Italic</i> - undefined and using default value</li>
    		<li><span style="color: red">Red</span> - defined and invalid</li>
    	</ul>
    </div>
	<p><a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a></p>
</body>
</html>