<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>

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
		<span class="bull">&bull;</span> <a href="compare_variants.jsp"><span>Variants Comparison</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_cohorts.jsp"><span>Cohorts Summary</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="view_config.jsp"><span>Variant Configurations</span></a>
		</p>
    	<p>
		<span class="bull">&bull;</span> <a href="fi_debugger.jsp"><span>Featured Items Debugger</span></a>
		</p>
    </div>
</body>
</html>