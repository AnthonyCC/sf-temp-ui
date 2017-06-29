<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		<%@ include file="/common/template/includes/ga_custom_vars.jspf" %>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
    <!--[if lt IE 10]>
      <jwr:style src="/search_opt.css"/>
    <![endif]-->
    <tmpl:get name='customCss'/>
    <tmpl:get name='customJs'/>
		<script>
		FD_QuickBuy.style = {
				closeButton:'quickbuy-noheader-close',
				header:'quickbuy-noheader'
		};
		</script>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<!--[if lt IE 7]><body class="ie ie6 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"><![endif]-->
<!--[if IE 7]><body class="ie ie7 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
<!--[if IE 8]><body class="ie ie8 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
<!--[if gt IE 8]><!-->
	<body class="searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>">
<!--<![endif]-->
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
		<%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
    <div id="content_top" class="content-header container"><div class="span-7 orange eagle middle content-header-height result-text">Search Results<span class="result-helper"></span></span></div><tmpl:get name="content-header"/></div>
    <div class="OAS-holder container" id='oas_CategoryNote'><script>OAS_AD('CategoryNote');</script></div>
    <div class="search-header container"></div>
    <div class="tab-header container <tmpl:get name="productsOnly" />">
    	<div class="span-24 last">
	    	<ul id="tabs">
				<li id="products-tab"><a href="<tmpl:get name="productTabLink"/>">Products (<tmpl:get name="productTabItemCount"/>)</a></li>
				<li id="recipes-tab"><a href="<tmpl:get name="recipesTabLink"/>">Recipes (<tmpl:get name="recipeTabItemCount"/>)</a></li>
	    	</ul>
	    	<div id="result-header"><span id="result-header-aligner"></span><span id="result-header-content"><div><tmpl:get name="search-header"/></div><div><tmpl:get name="didyoumean"/></div></span></div>
    	</div>
    </div>
    <div class="toolbar container"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
    <div id="main" class="container">
    	<div id="sidebar">
			<div id="selection" class="sidebar-content">
    		<tmpl:get name='selection-header'/>
    		<tmpl:get name='selection-list'/>
    		</div>
        <tmpl:get name='filterNavigator'/>
    	</div>
    	<div id="content" class="product-grid">
    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
    		<tmpl:get name='recommendations'/>
    	</div>
    </div>
		<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	  <tmpl:get name='customJsBottom'/>
	</body>
</html>
