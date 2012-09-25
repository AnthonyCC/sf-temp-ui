<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/common/template/includes/ga_custom_vars.jspf" %>
	    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
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
<!--[if lt IE 7]><html class="ie ie6 newproducts <tmpl:get name="noResult"/> <tmpl:get name="startPage"/> <tmpl:get name='dfgs-container-class'/>"><![endif]-->
<!--[if IE 7]><html class="ie ie7 newproducts <tmpl:get name="noResult"/> <tmpl:get name="startPage"/> <tmpl:get name='dfgs-container-class'/>"> <![endif]-->
<!--[if IE 8]><html class="ie ie8 newproducts <tmpl:get name="noResult"/> <tmpl:get name="startPage"/> <tmpl:get name='dfgs-container-class'/>"> <![endif]-->
<!--[if gt IE 8]><!-->
	<body class="newproducts <tmpl:get name="noResult"/> <tmpl:get name="startPage"/> <tmpl:get name='dfgs-container-class'/>">
	<!--<![endif]-->
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
	<div id="deptnav" class="newprod-deptnav container"><tmpl:get name='deptnav-header' /></div>
    <div id="content_top" class="content-header container">
    	<div class="span-7 orange eagle middle content-header-height result-text"><tmpl:get name="content-title"/><span class="result-helper"></span></span></div>
    	<tmpl:get name="content-header"/>   	
		<div class="prepend-7 span-10 last newprod-header-count">
    		<tmpl:get name="count-header"/>
    	</div>
    </div>
    <!-- div class="OAS-holder container"><script>OAS_AD('CategoryNote');</script></div -->
    <div class="newprod-featured container"><tmpl:get name='featured'/></div>
    <div class="container <tmpl:get name="activeView"/>-view"> 
    	<tmpl:get name='your-cart'/>
    	<div class="newprod-grid">
		    <div class="toolbar"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
			    <div id="main" class="container">
			    	<div id="sidebar">
						<div id="selection" class="sidebar-content">
			    		<tmpl:get name='selection-header'/>
			    		<tmpl:get name='selection-list'/>
			    		</div>
			    		<div id="filters">
			    		<tmpl:get name='deparmentFilter'/>
			    		<tmpl:get name='categoryFilter'/>
			    		<tmpl:get name='subCategoryFilter'/>
			    		<tmpl:get name='brandFilter'/>
			    		<tmpl:get name='expertRatingFilter'/>
			    		<tmpl:get name='otherFilters'/>
			    		<tmpl:get name='sidebarOther'/>
			    		</div>
			    		<tmpl:get name='recipesFilter'/>
			    	</div>
			    	<div id="content" class="product-grid">
			    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
			    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
			    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
			    	</div>
			    </div>
		   </div>
		</div>
		<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	</body>
</html> 
