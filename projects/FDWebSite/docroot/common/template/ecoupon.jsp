<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ page import='com.freshdirect.FDCouponProperties' %>
<%
	boolean onLandingPage = false;
	//toggle off landing page
	//Boolean.parseBoolean(NVL.apply(request.getParameter("refinement"), "true"));

%>
<html lang="en-US" xml:lang="en-US">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
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
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<!--[if lt IE 7]><body class="ie ie6 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"><![endif]-->
<!--[if IE 7]><body class="ie ie7 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
<!--[if IE 8]><body class="ie ie8 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
<!--[if gt IE 8]><!-->
	<body class="searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>">
<!--<![endif]-->
		<%@ include file="/shared/template/includes/i_body_start.jspf" %>
		<%@ include file="/common/template/includes/globalnav.jspf" %>
	    <div id="content_top" class="content-header container" style="margin-top:10px">
	    	<div class="orange eagle top content-header-height-coupons result-text">
	    		<span>Coupon Savings with </span><img src="/media/images/ecoupon/logo_fd_coups_56h.png" alt="FDCoupons" style="margin-bottom: -10px" /><span class="result-helper"></span>
		    	<tmpl:get name="content-header"/>
	    	</div>
	    </div>

    	<% if (onLandingPage) { %>
	    	<% if (user.isCouponsSystemAvailable()) { %>
				<div class="OAS-holder container">
					<div class="oas_feature_left left" id='oas_ECouponTop'>
						<script type="text/javascript">
							OAS_AD('ECouponTop');
						</script>
					</div>
					<div class="oas_feature_right right">
						<div class="oas_feature_right_tab" id='oas_ECouponTab1'>
							<script type="text/javascript">
								OAS_AD('ECouponTab1');
							</script>
						</div>
						<div class="oas_feature_right_tab" id='oas_ECouponTab2'>
							<script type="text/javascript">
								OAS_AD('ECouponTab2');
							</script>
						</div>
						<div class="oas_feature_right_tab" id='oas_ECouponTab3'>
							<script type="text/javascript">
								OAS_AD('ECouponTab3');
							</script>
						</div>
					</div>
					<div class="clear" style="font-size: 0px;"></div>
				</div>
			<% } %>
	    <% } %>

	    <% if (!onLandingPage) { %>
	    	<div class="search-header container"></div>
	    <% } %>

	    	<% if (user.isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) { %>
	    		<% if (!onLandingPage) { %>
				    <div class="tab-header container <tmpl:get name="productsOnly" />">
				    	<div class="span-24 last">
					    	<ul id="tabs">
								<li id="products-tab"><a href="<tmpl:get name="productTabLink"/>">Products (<tmpl:get name="productTabItemCount"/>)</a></li>
					    	</ul>
					    	<div id="result-header"><span id="result-header-aligner"></span><span id="result-header-content"><div><tmpl:get name="search-header"/></div><div><tmpl:get name="didyoumean"/></div></span></div>
				    	</div>
				    </div>
					<div class="toolbar container"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
				<% } %>
			    <div id="main" class="fdCoupon container">
			    	<div id="sidebar">
			    		<% if (!onLandingPage) { %>
							<div id="selection" class="sidebar-content">
				    			<tmpl:get name='selection-header'/>
				    			<tmpl:get name='selection-list'/>
				    		</div>
				    	<% } %>
			    		<% if (onLandingPage) { %>
			    			<div class="filterbox sidebar-content"><ul><li><a href="/ecoupon.jsp?sort=prio&searchParams=&view=grid&pageSize=0&refinement=1">View all <span class="count">(<tmpl:get name="productTabItemCount"/>)</span></a></li></ul></div>
			    		<% } %>
			        	<tmpl:get name='filterNavigator'/>
			    	</div>
				    <div id="content" class="product-grid">
				    	<% if (onLandingPage) { %>
				    		<%-- best offers carousel --%>
				    			<table width="100%"><tr><td align="center"><tmpl:get name='bestOffers-carousel' /></td></tr></table>
				    		<%-- deal spots --%>
				    			<tmpl:get name='dealSpots' />
				    		<%-- media includes --%>
				    			<div id="fdCouponBottomAdCont" id='oas_ECouponBottom'>
				    				<center>
					    				<script type="text/javascript">
											OAS_AD('ECouponBottom');
										</script>
									</center>
								</div>
								<div id="fdCouponBottomViewAll">
				    				<a href="/ecoupon.jsp?sort=prio&searchParams=&view=grid&pageSize=0&refinement=1"><img src="/media/images/ecoupon/botViewAll.gif" alt="View All FDCoupons" /></a><br />
				    			</div>
				    	<% } else { %>
					    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
					    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
					    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
					    		<tmpl:get name='recommendations'/>
					    <% } %>
				    </div>
			    </div>
		    <% } else { %>
		    	<div class="container">
		    		<div class="fdCoupon_down_circ"><%= SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE %></div>
		    	</div>
		    <% } %>

		<%@ include file="/common/template/includes/footer.jspf" %>
    	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	  <tmpl:get name='customJsBottom'/>
	</body>
</html>
