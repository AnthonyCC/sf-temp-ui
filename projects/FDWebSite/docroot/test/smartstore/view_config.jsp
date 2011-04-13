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
<%@page import="com.freshdirect.smartstore.ConfigurationStatus"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.EnumConfigurationState"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.service.RecommendationServiceFactory"%>
<%@page import="com.freshdirect.smartstore.service.CmsRecommenderRegistry"%>
<%@page import="com.freshdirect.smartstore.service.VariantRegistry"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.impl.AbstractRecommendationService"%>
<%@page import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.smartstore.external.scarab.ScarabInfrastructure"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%

Iterator it;
URLGenerator urlG = new URLGenerator(request);
//System.err.println(request.getServletPath());
String origURL = urlG.build();

if (urlG.get("refresh") != null) {
	VariantRegistry.getInstance().reload();
	CmsRecommenderRegistry.getInstance().reload();
	ScarabInfrastructure.reload(true);
	VariantSelectorFactory.refresh();
	urlG.remove("refresh");
	urlG.set("reloaded", "1");
	String newURL = urlG.build();
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	return;
} 

List<EnumSiteFeature> siteFeatures = EnumSiteFeature.getSmartStoreEnumList();
Collections.sort(siteFeatures);

EnumSiteFeature expandSf = null;
if (urlG.get("expand") != null)
	expandSf = EnumSiteFeature.getEnum(urlG.get("expand")); 

Map varMap = new HashMap();

VariantSelection helper = VariantSelection.getInstance();

Map<String, Integer> cohorts = helper.getCohorts();

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VARIANT CONFIGURATIONS PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

body{margin:20px 60px;color:#333333;background-color:#fff;}
input{font-weight:normal;}
p{margin:0px;padding:0px 0px 15px;}
p.head{padding:10px 0px 20px;}
a{color:#336600;}.test-page a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
table.t1{width:auto;margin-bottom:20px; border: 3px}
table.t1 td{border:1px solid black;padding:4px 8px;}
table.t1 td.sf{background-color:#ccc;}
table.t1 td.space{border-width:0px 0px;}
table.t1 td.expander{padding:15px 0px 0px;}
table.t1 td.in-use{border-width:0px;padding:10px 0px 4px;}
.info{color:red}
.no-use{color:#999 !important;}
td.no-use{border-color:#999 !important;}
.faulty{border-color:red !important;}
.erring{color:red; !important}
.warning{color:#FF6633; !important}
.unused{color:green; !important}
.valid{font-weight:bold;}
.overridden{text-decoration: underline;}
.default{font-style: italic;}
.hidden{display: none;}
.visible{display: block;}
.hand{cursor: pointer;}
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
	<script type="text/javascript">
	function expand(sf) {
		var label = document.getElementById('lbl-' + sf);
		label.title = 'Site Feature';
		label.onclick = null;
		label.className = '';
		document.getElementById('sf-' + sf).className =
		document.getElementById('sf-' + sf).className.replace(/hidden/, '');
	}

	function expandAll() {

<%
	it = siteFeatures.iterator();
	while (it.hasNext()) {
		EnumSiteFeature sf = (EnumSiteFeature) it.next();
		response.getWriter().println("document.getElementById('lbl-" + sf.getName() + "').className = 'hidden';");
		response.getWriter().println("document.getElementById('sf-" + sf.getName() +
				"').className = document.getElementById('sf-" + sf.getName() + "').className.replace(/hidden/, '');");
	}
%>
	}

	function expandNotInUse(sf) {
		document.getElementById('vnu-' + sf).style.display = 'none';
		var sfDiv = document.getElementById('sf-' + sf);
		var trs = sfDiv.getElementsByTagName('tr');
		for (var i = 0; i < trs.length; i++) {
			var node = trs.item(i);
			if (node.className != null)
				node.className = node.className.replace(/hidden/, '');
		}
	}

	</script>
   	<%
	it = siteFeatures.iterator();
	while (it.hasNext()) {
		EnumSiteFeature sf = (EnumSiteFeature) it.next();
		Map<String, Variant> curVars = VariantRegistry.getInstance().getServices(sf);
		Map sortedVars = SmartStoreUtil.getVariantsSortedInWeight(sf);
		varMap.put(sf, curVars);

 		Iterator it2 = sortedVars.keySet().iterator();
 		List varErrors = new ArrayList();
		while (it2.hasNext()) {
			String varId = (String) it2.next();
			Variant variant = ((Variant) curVars.get(varId));
			if (variant != null) {
				RecommendationService service = variant.getRecommender();
				int weight = ((Integer) sortedVars.get(varId)).intValue();
	  			boolean faulty = (!variant.getServiceConfig().getType().equals(RecommendationServiceType.NIL)
	  					&& !variant.getServiceConfig().getType().equals(RecommendationServiceType.TAB_STRATEGY)) &&
	  					service instanceof NullRecommendationService;
	  			if (weight > 0 && faulty) {
	  				varErrors.add(varId);
	  			}
			} else {
			    varErrors.add(varId);
			}
		}
		it2 = varErrors.iterator();
		String sfError = "";
		if (it2.hasNext()) {
			sfError = " &ndash; Error in variant &lsquo;" + (String) it2.next();
			sfError += "&rsquo;";
		}
		while (it2.hasNext()) {
			String varId = (String) it2.next();
			if (it2.hasNext())
				sfError += ", &lsquo;" + varId + "&rsquo;";
			else
				sfError += " and &lsquo;" + varId + "&rsquo;";					
		}
 		it2 = sortedVars.keySet().iterator();
 		boolean emptySf = !it2.hasNext();
 		String sfClass = sfError.length() != 0 ? "erring" : null;
 		if (emptySf)
 			sfClass = "no-use";
 		boolean expand = sf.equals(expandSf);
	%>
    <div title="Site Feature"<%= sfClass != null ?
    		" class=\"" + sfClass + "\"" : "" %> style="padding: 10px 0px; 4px;">
    	<% if (expand) { %>
    	<span id="lbl-<%= sf.getName() %>" title="Site Feature">
    	<span class="title18"><%= sf.getTitle() %> (<%= sf.getName() %>)<%= sfError %></span>
    	</span>
    	<% } else { %>
    	<span id="lbl-<%= sf.getName() %>" title="Click to expand" class="hand" onclick="expand('<%= sf.getName() %>'); return false;">
    	<span class="title18"><%= sf.getTitle() %> (<%= sf.getName() %>)<%= sfError %></span>
    	</span>
    	<% } %>
    </div>
   	<%
 		it2 = sortedVars.keySet().iterator();
   		if (emptySf) {
   	%>
   	<p class="text13 no-use hidden" id="sf-<%= sf.getName() %>" style="margin-left: 3em;">No variants.</p>
   	<%
   		} else {
   	%>
   	<div <%= expand ? "" : "class=\"hidden\" "%>id="<%= "sf-" + sf.getName() %>" style="margin-left: 3em;">
    <table class="t1">
    	<%
    		boolean notFirst = false;
    		int inUse = -1;
    		int newInUse;
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
    			Variant variant = (Variant) curVars.get(varId);
    			if (variant == null) {
    			    %><tr><td class="text13bold space" colspan="2"><a name="<%= varId %>">&nbsp;</a></td></tr>
    			    <tr><td class="text13bold faulty" colspan="2"><%= varId %> is missing ! (Probably <%= varId %> is an alias to an archived variant, which is not archived?)</td></tr>
    			    <%
    			    continue;
    			}
   				RecommendationService service = variant.getRecommender();
   				int weight = ((Integer) sortedVars.get(varId)).intValue();
   				boolean faulty = (!variant.getServiceConfig().getType().equals(RecommendationServiceType.NIL)
   						&& !variant.getServiceConfig().getType().equals(RecommendationServiceType.TAB_STRATEGY)) &&
   						service instanceof NullRecommendationService;
   				String tdClass = faulty ? " faulty" : (weight > 0 ? "" : " no-use");
   				String trClass = weight > 0 ? "" : "hidden";
				SortedMap statuses = service.getVariant().getServiceConfig().getConfigStatus();

				newInUse = weight > 0 ? 1 : 0;
				if (notFirst) {
    	%>
    	<tr class="<%= trClass %>">
    		<td class="text13bold space<%= tdClass %>" colspan="2">
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
    	<% 
    			if (newInUse != inUse) {
    				inUse = newInUse;
    				if (inUse == 0) {
    	%>
    	<tr id="vnu-<%= sf.getName() %>">
    		<td class="text10 left space expander" colspan="2"><a href="#" onclick="expandNotInUse('<%= sf.getName() %>'); return false;">Expand unused</a></td>
    	</tr>
    	<%
    				}
    	%>
    	<tr class="<%= trClass %>">
    	<td class="title16 in-use<%= tdClass %>" colspan="2">
    		<%= inUse > 0 ? "Variants in Use" : "Variants not in Use" %>
    	</td>
    	</tr>
    	<tr class="<%= trClass %>">
    		<td class="text13bold space<%= tdClass %>" colspan="2"><a name="<%= varId %>">&nbsp;</a></td>
    	</tr>
    	<%
    			}
    	%>
    	<tr class="<%= trClass %>">
    	<td class="text13bold var<%= tdClass %>" colspan="2" title="Variant">
    		<span class="<%= faulty ? "erring" : "" %>"><%= varId %><%= faulty ? " &ndash; Misconfigured and Not in Operation" : "" %></span>
    	</td>
    	</tr>
    	<tr class="<%= trClass %>">
    	<td class="text13 left<%= tdClass %>" title="Parameter Name">Variant type</td>
    	<td class="text13<%= tdClass %>" title="<%= service.getClass().getName() %>"><%= variant.getServiceConfig().getType().getName() %></td>
    	</tr>
    	<tr class="<%= trClass %>">
    	<td class="text13 left<%= tdClass %>" title="Parameter Name">Customers percentage</td>
    	<td class="text13<%= tdClass %>" title="Parameter Value"><%= weight %>%</td>
    	</tr>
    	<%
				for (Iterator keyIt = statuses.keySet().iterator(); keyIt.hasNext(); ) {
					String configKey = (String) keyIt.next();
					ConfigurationStatus status = (ConfigurationStatus) statuses.get(configKey);
					String description = RecommendationServiceFactory.CONFIG_LABELS.containsKey(configKey) ?
							(String) RecommendationServiceFactory.CONFIG_LABELS.get(configKey) : "&lt;Unknown description&gt;";
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
    	<tr class="<%= trClass %>">
			<td class="text13 left<%= tdClass %>" title="<%= description %>">
				<span class="<%= textClass %>"><%= configKey %></span>
			</td>
	    	<td class="text13<%= tdClass %>" title="Parameter Value"><span class="<%= textClass %>"><%= status.getAppliedValue() +
	    			(!status.isValueSame() ? " (loaded: " + status.getLoadedValue() + ")" : "") %></span><%
	    					
	    			if (error != null || warning != null) { 
	    	%><span class="valid <%= error != null ? "erring" : "warning" %>"> &ndash; <%= error != null ? error : warning %></span><%
	    			} 
	    	%></td>
		</tr>
		<%
				}
				notFirst = true;
   			}
	  	%>
  	</table>
  	</div>
    <%
    	}
   	}
	%>
	<% if (siteFeatures.size() > 0) { %>
    <p style="padding-top: 20px;">Hover over the items in the table to view their meanings.</p>
    <div>
    	Legend for variant configuration parameters:
    	<ul>
    		<li><b>Bold</b> &ndash; valid</li>
    		<li><i>Italic</i> &ndash; using default value</li>
    		<li><u>Underlined</u> &ndash; overridden</li>
    		<li><span class="erring valid">Red Bold</span> &ndash; invalid</li>
    		<li><span class="warning">Orange</span> &ndash; warning</li>
    	</ul>
    </div>
	<p><a href="<%= urlG.set("refresh",1).build() %>">Click to reload configuration</a></p>
    <% } %>
</body>
</html>