<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freshdirect.event.ImpressionLogger"%>
<%@page import="com.freshdirect.smartstore.external.scarab.ScarabInfrastructure"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%
	if (request.getParameter("impressionLogger")!=null) {
		ImpressionLogger.setGlobalEnabled(Boolean.valueOf(request.getParameter("impressionLogger")).booleanValue());	    
	}
	if (request.getParameter("scarabReload") != null) {
		ScarabInfrastructure.reload(true);
		response.sendRedirect(request.getRequestURI());
		return;
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>SMART STORE TEST PAGES - INDEX</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px 0px 10px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:left;}

	</style>
</head>
<body class="test-page">
    <div class="rec-chooser title14">
    	<p>
    	<span style="text-transform: uppercase;">Page Index:</span>
    	</p>
    	<p>
		<span class="bull">&bull;</span> <a href="all_ymal_perf_test.jsp"><span>All Smart YMALs Performance Test</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="all_ymal_perf_test.jsp"><span>All YMAL Performance Test</span></a>
		</p>        
    	<p>
		<span class="bull">&bull;</span> <a href="tabs_perf_test.jsp"><span>Cart Tabs Performance Test Page</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="tabs_test.jsp"><span>Cart Tabs Test Page</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_cohorts.jsp"><span>Cohorts Summary</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="cohorts.jsp"><span>Cohorts Tool</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="factors.jsp"><span>Factors</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="fi_debugger.jsp"><span>Featured Items Debugger</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="new_and_back.jsp"><span>New and Back in Stock Products Test Page</span></a>
		</p>
        <p>
		<span class="bull">&bull;</span> <a href="view_promovariant_cfg.jsp"><span>Promo Variant Configurations</span></a> <a href="view_promovariant_cfg.jsp?refresh=1">(reload)</a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="request_simulator.jsp"><span>Request Simulator</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_config.jsp"><span>Variant Configurations</span></a> <a href="view_config.jsp?refresh=1">(reload)</a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="my_variant.jsp"><span>Variant Lookup</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="compare_variants.jsp"><span>Variants Comparison</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_tabs.jsp"><span>View Cart Tab Strategies</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_ymals.jsp"><span>View Smart YMALs</span></a>
		</p>
		<p>
		<span class="bull">&bull;</span> <a href="ymal_display.jsp">YMAL Display Test</a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="ymal_perf_test.jsp"><span>YMAL Performance Test</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="zone_pricing.jsp"><span>Zone Pricing Test Page</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <span>Impression logger :
		<% if (ImpressionLogger.isGlobalEnabled()) { %>
			enabled, switch <a href="index.jsp?impressionLogger=false"><span>OFF</span></a>
		<% } else { %>
			disabled, switch <a href="index.jsp?impressionLogger=true"><span>ON</span></a>
		<% } %>
		</span>
 		</p>
 		<p>
		<span class="bull">&bull;</span> <a href="../search/compare.jsp"><span>Search Compare Page</span></a>
		</p>
 		<p>
		<span class="bull">&bull;</span> <a href="../search/category_scoring.jsp"><span>Search Category Scoring</span></a>
		</p>
    	
    </div>
</body>
</html>
