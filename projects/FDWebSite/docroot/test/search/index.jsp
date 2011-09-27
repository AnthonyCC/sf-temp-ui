<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>SEARCH TEST PAGES - INDEX</title>
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
		<span class="bull">&bull;</span> <a href="brandautocomplete.jsp"><span>Autocomplete test page - brandautocomplete.jsp	</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="category_scoring.jsp"><span>Search scoring - category_scoring.jsp</span></a>
		</p>        
    	<p>
		<span class="bull">&bull;</span> <a href="compare.jsp"><span>Search compare page - compare.jsp</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="relevancy_config.jsp"><span>Relevancy config - relevancy_config.jsp</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="report.jsp"><span>report.jsp - not working</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="search_results_report.jsp"><span>search_results_report.jsp - not working</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="search_results.jsp"><span>CSV upload something - search_results.jsp</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="search_terms.jsp"><span>Search terms listing - search_terms.jsp</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="smart_search_log.jsp"><span>Smart search log - smart_search_log.jsp</span></a>
		</p>
        <p>
		<span class="bull">&bull;</span> <a href="smart_search.jsp"><span>Smart search test page - smart_search.jsp</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="spell.jsp"><span>CSV upload something - spell.jsp</span></a>
		</p>
    	
    </div>
</body>
</html>
