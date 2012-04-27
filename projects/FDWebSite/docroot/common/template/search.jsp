<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<!--[if lt IE 7]><html class="ie ie6"><![endif]-->
<!--[if IE 7]><html class="ie ie7"> <![endif]-->
<!--[if IE 8]><html class="ie ie8"> <![endif]-->
<!--[if gt IE 8]><!--><html lang="en"><!--<![endif]-->
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
	    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/common/template/includes/ga_custom_vars.jspf" %>
	    <%@ include file="/shared/template/includes/ccl.jspf" %>
	    <tmpl:get name='customCss'/>
	    <tmpl:get name='customJs'/>
		<script>
		FD_QuickBuy.style = {
				closeButton:'quickbuy-noheader-close',
				header:'quickbuy-noheader'
		};
		</script>
	</head>
	<body class="searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>">
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
    <div id="content_top" class="content-header container"><tmpl:get name="content-header"/></div>
    <div class="search-header container"><div id="result-header" class="span-24"><tmpl:get name="search-header"/><div id="didyoumean"><tmpl:get name="didyoumean"/></div></div></div>
    <div class="tab-header container <tmpl:get name="productsOnly" />">
    	<ul id="tabs" class="span-24">
			<li id="products-tab"><a href="<tmpl:get name="productTabLink"/>">Products (<tmpl:get name="productTabItemCount"/>)</a></li>
			<li id="recipes-tab"><a href="<tmpl:get name="recipesTabLink"/>">Recipes (<tmpl:get name="recipeTabItemCount"/>)</a></li>    	
    	</ul>
    </div>
    <div class="toolbar container"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
    <div id="main" class="container">
    	<div id="sidebar">
    		<div id="filters">
    		<tmpl:get name='deparmentFilter'/>
    		<tmpl:get name='categoryFilter'/>
    		<tmpl:get name='subCategoryFilter'/>
    		<tmpl:get name='brandFilter'/>
    		<tmpl:get name='expertRatingFilter'/>
    		<tmpl:get name='otherFilters'/>
    		</div>
    		<tmpl:get name='recipesFilter'/>
    	</div>
    	<div id="content" class="product-grid">
    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
    	</div>
    </div>
		<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	</body>
</html> 
