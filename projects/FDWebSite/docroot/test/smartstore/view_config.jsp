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
<%@page import="java.util.SortedMap"%>
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
<%@ taglib uri="freshdirect" prefix="fd"%>
<%

Iterator it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

if (urlG.get("refresh") != null) {
	SmartStoreServiceConfiguration.getInstance().refreshAll();
	urlG.remove("refresh");
	urlG.set("reloaded", "1");
	String newURL = urlG.build();
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	return;
} 

List siteFeatures = EnumSiteFeature.getSmartStoreEnumList();
Collections.sort(siteFeatures);

Map varMap = new HashMap();

VariantSelection helper = VariantSelection.getInstance();

Map cohorts = helper.getCohorts();

%>

<%@page import="com.freshdirect.smartstore.ConfigurationStatus"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.smartstore.EnumConfigurationState"%><html>
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
.faulty{border-color:red !important;}
.erring{color:red; !important}
.warning{color:#FF6633; !important}
.unused{color:green; !important}
.valid{font-weight:bold;}
.overridden{text-decoration: underline;}
.default{font-style: italic;}
	</style>
</head>
<body>
<%
	if (request.getParameter("reloaded") != null) {
%>
   	<span class="text11bold info" title="Configuration reloaded">Configuration reloaded</span>
<% 
	}
%>
	<p><a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a></p>
    <div>
    	Legend for variant configuration parameters:
    	<ul>
    		<li><b>Bold</b> - valid</li>
    		<li><i>Italic</i> - using default value</li>
    		<li><u>Underlined</u> - overridden</li>
    		<li><span class="erring valid">Red Bold</span> - invalid</li>
    		<li><span class="warning">Orange</span> - warning</li>
    	</ul>
    </div>

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
   	<p class="text13">No variants.</p>
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
   				RecommendationService service = (RecommendationService) curVars.get(varId);
    			Variant variant = service.getVariant();
   				int weight = ((Integer) sortedVars.get(varId)).intValue();
   				boolean faulty = (!variant.getServiceConfig().getType().equals(RecommendationServiceType.NIL)
   						&& !variant.getServiceConfig().getType().equals(RecommendationServiceType.TAB_STRATEGY)) &&
   						service instanceof NullRecommendationService;
   				String weiStr = faulty ? " faulty" : (weight > 0 ? "" : " no-use");
				SortedMap statuses = service.getVariant().getServiceConfig().getConfigStatus();

				newInUse = weight > 0 ? 1 : 0;
				if (notFirst) {
    	%>
    	<tr>
    		<td class="text13bold space<%= weiStr %>" colspan="2">
    		<% if (newInUse == inUse) { %>
    			<a name="<%= varId %>">&nbsp;</a>
    		<% } else { %>
    			&nbsp;
    		<% } %>
    		</td>
    	</tr>
    	<%
    			}
    	%>
    	<tr>
    	<% 
    			if (newInUse != inUse) {
    				inUse = newInUse;
    	%>
    	<td class="title18 in-use<%= weiStr %>" colspan="2">
    		<%= inUse > 0 ? "Variants in Use" : "Variants not in Use" %>
    	</td>
    	</tr>
    	<tr><td class="text13bold space<%= weiStr %>" colspan="2"><a name="<%= varId %>">&nbsp;</a></td>
    	</tr>
    	<%
    			}
    	%>
    	<tr>
    	<td class="text13bold var<%= weiStr %>" colspan="2" title="Variant">
    		<span class="<%= faulty ? "erring" : "" %>"><%= varId %><%= faulty ? " - Misconfigured and Not in Operation" : "" %></span>
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
				for (Iterator keyIt = statuses.keySet().iterator(); keyIt.hasNext(); ) {
					String configKey = (String) keyIt.next();
					ConfigurationStatus status = (ConfigurationStatus) statuses.get(configKey);
					String description = SmartStoreServiceConfiguration.configDesc.containsKey(configKey) ?
							(String) SmartStoreServiceConfiguration.configDesc.get(configKey) : "&lt;Unknown description&gt;";
					String warning = status.getWarning();
					String error = status.getError();
					String textClass = "";
					if (status.isValid())
						textClass += "valid";
					else
						textClass += "erring valid";
					if (warning != null)
						textClass += " warning";
					if (status.isDefault())
						textClass += " default";
					if (status.isOverridden())
						textClass += " overridden";
		%>
    	<tr>
			<td class="text13 left<%= weiStr %>" title="<%= description %>">
				<span class="<%= textClass %>"><%= configKey %></span>
			</td>
	    	<td class="text13<%= weiStr %>" title="Parameter Value"><span class="<%= textClass %>"><%= status.getAppliedValue() +
	    			(!status.isValueSame() ? " (loaded: " + status.getLoadedValue() + ")" : "") %></span><%
	    					
	    			if (error != null || warning != null) { 
	    	%><span class="valid <%= error != null ? "erring" : "warning" %>"> - <%= error != null ? error : warning %></span><%
	    			} 
	    	%></td>
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
	<% if (siteFeatures.size() > 0) { %>
    <p>Hover over the items in the table to view their meanings.</p>
    <div>
    	Legend for variant configuration parameters:
    	<ul>
    		<li><b>Bold</b> - valid</li>
    		<li><i>Italic</i> - using default value</li>
    		<li><u>Underlined</u> - overridden</li>
    		<li><span class="erring valid">Red Bold</span> - invalid</li>
    		<li><span class="warning">Orange</span> - warning</li>
    	</ul>
    </div>
	<p><a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a></p>
    <% } %>
</body>
</html>