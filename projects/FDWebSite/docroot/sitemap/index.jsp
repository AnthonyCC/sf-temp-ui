<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
<meta charset="UTF-8">
 <%-- <title>FreshDirect Sitemap</title> --%>
      <fd:SEOMetaTag title="FreshDirect Sitemap"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
<link rel="stylesheet" type="text/css" href="/assets/css/sitemap/sitemap.css">
</head>
<body>
	<header>
		<img alt="freshdirect" src="/media_stat/images/template/quickshop/9managers_s.jpg">
		<img alt="freshdirect" height="125" src="/media/images/navigation/department/local/dept_icons/dpt_local_whoslocal_map.gif">
		<h1>Sitemap</h1>
<!-- 		<img id="spinner" alt="spinner" src="/media_stat/images/navigation/spinner.gif"> -->
	</header>

	<fd:Sitemap />
	<c:choose>
		<c:when test="<%=!FDStoreProperties.isSiteMapEnabled()%>}">
			Sitemap is disabled
		</c:when>		
		<c:when test="${not empty siteMapGenerated}">
            Sitemap is created. Please choose from options to take other action. 
            <form method="post">
                <select name="type">
                    <option value="preview">Preview sitemap</option>
                    <option value="generate">Generate sitemap</option>
                </select>
                <input type="password" name="password">
                <input type="submit" value="Submit">
            </form>
		</c:when>
		<c:when test="${not empty siteMapData}">
			<!-- 	<div id="log">Processing</div> -->
			<div class="control">
				<button id="btn_open_all">open all</button>
				<button id="btn_close_all">close all</button>
				<button id="btn_toggle_empty">show/hide empty nodes</button>
				<button id="btn_redraw_chart">redraw chart based on selection</button>
				<a id="btn_download_csv" download="sitemap.csv" href="#">Download CSV</a>
			</div>
			<ul id="content"></ul>
			<pre id="csvcontent"></pre>
		
			<script>
				var data = ${siteMapData};
			</script>
			<script src="/assets/javascript/d3.v3.min.js" charset="utf-8"></script>
			<script src="/assets/javascript/fd/sitemap/sitemap.js" charset="utf-8"></script>
		</c:when>
		<c:otherwise>
            Please provide password to see sitemap
            <form method="post">
                <select name="type">
                    <option value="preview">Preview sitemap</option>
                    <option value="generate">Generate sitemap</option>
                </select>
                <input type="password" name="password">
                <input type="submit" value="Submit">
            </form>
		</c:otherwise>
	</c:choose>

</body>
</html>
