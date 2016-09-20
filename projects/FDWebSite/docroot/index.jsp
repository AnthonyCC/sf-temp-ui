<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.cms.fdstore.FDContentTypes' %>
<%@ page import="com.freshdirect.cms.ContentKey"%>
<%@ page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.freshdirect.cms.application.CmsManager"%>
<%@ page import="com.freshdirect.cms.ContentType"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %><% 

//expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;

// no YUI required for index.jsp
request.setAttribute("noyui", true);

%><fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />
<%-- fd:WelcomeExperience / --%><%

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));        
	
	request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
        
	String pageTemplate = "/common/template/no_shell_optimized.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
%>
<tmpl:insert template="<%=pageTemplate %>">
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="index" includeSiteSearchLink="true"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name="customCss">
		<fd:css href="/assets/css/homepage/homepage.css" media="all" />
	</tmpl:put>
	<tmpl:put name="extraCss"><%-- MOBILE --%>
		<link href="/assets/javascript/jquery/mmenu/css/jquery.mmenu.all.css" type="text/css" rel="stylesheet" />
		<%-- TODO : move to a file --%>
		<style>
			body {
			    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
			    font-size: 14px;
			    line-height: 1.42857143;
			    color: #333;
			    background-color: #fff;
			}
			body{
				background:#fff;
				font-family: Verdana;
				font-weight:normal;
				-webkit-animation:androidbugfix infinite 1s;
			}
			
			img {
			    vertical-align: middle;
			}
			img {
			    border: 0;
			}
			.backnav {
				margin: 5px 0 0 10px;
				padding: 7px;
			}
			.helpDiv {
			    position: absolute;
			    height: 30px;
			    padding: 5px 10px;
			    background: #000000;
			    opacity: 0.3;
			    width: 100%;
			    bottom: 0;
			}
			.span-left {
			    color: #ffffff;
			    text-transform: uppercase;
			    display: inline-block;
			    position: absolute;
			    bottom: 4px;
			    left: 10px;
			    width: 50%;
			    font-weight: normal;
			}
			.span-right {
			    color: #ffffff;
			    text-transform: normal;
			    display: inline-block;
			    position: absolute;
			    bottom: 4px;
			    right: 10px;
			    width: 45%;
			    text-align: right;
			    font-weight: normal;
			}
			.home-page-banner {
			    margin: 10px;
			    margin-top: 0;
			    position: relative;
			}
			.home-page-banner-top img, .home-page-banner img {
			    width: 100%;
			}
			.navbar-brand, .navbar-nav>li>a {
			    text-shadow: 0 1px 0 rgba(255,255,255,.25);
			}
			.navbar-brand {
			    float: left;
			    padding: 15px 15px;
			    font-size: 18px;
			    line-height: 20px;
			}
			.navbar-brand {
				padding: 15px 0px 0;
				width: 62%;
			}
			.navbar {
			    background-color: #6AAA6D;
			    /* min-height: 120px; */
			    margin: 0;
			}
			.navbar-header {
				padding: 9px 5px;
			}
			.navbar-toggle {
			    width: 44px;
			    height: 40px;
			    margin-right: 2px;
			    padding: 0;
			}
			.navbar-toggle {
			    position: relative;
			    float: right;
			    padding: 9px 10px;
			    margin-top: 8px;
			    margin-right: 15px;
			    margin-bottom: 8px;
			    background-color: transparent;
			    background-image: none;
			    border: 1px solid transparent;
			    border-radius: 4px;
			}
			.navbar-ecomvalue {
			    float: left;
			    margin: 8px 6px 0 0;
			    position: relative;
			}
			.navbar-ecomvalue a {
				width: 40px;
				height: 30px;
				display: block;
			}
			#navMenuItems .navbar {
			    height: 65px;
			}
			.backnav span {
			    width: 28px;
			    height: 4px;
			    color: #fff;
			    font-size: 1.5em;
			}
			#navMenu {
			    position: absolute;
    			height: 100%;
    			z-index: 10;
    			top: 0;
			    background-color: #fff;
			    visibility: hidden;
			    opacity: 0;
    			overflow: hidden;
    			width: 80vw;
    			left: -80vw;
			}
			#navMenu.open {
				transition: visibility 0.2s, opacity 0.2s linear, left 0.2s linear;
				visibility: visible;
				opacity: 1;
    			left: 0;
			}
			.loginDialog {
				visibility: hidden;
    			left: -80vw;
    			width: 80vw;
    			height: 100%;
			    opacity: 0;
				background-color: #fff;
    			text-align: center;
				z-index: 15;
				top: 81px;
				position: absolute;
			}
			.loginDialog.open {
				transition: visibility 0.2s, opacity 0.2s linear, left 0.2s linear;
				visibility: visible;
				left: 0;
				opacity: 1;
			}
			.loginDialog>iframe {
			    height: 100%;
    			width: auto;
    			min-width: 440px; /* TODO fix page, too wide for mobile */
    			background-image: url('/media/foodkick/slideshow/spinner.gif'); /* TODO don't use a FK image */
    			background-repeat: no-repeat;
    			background-position: 50% 50%;
			}
			body.no-scroll {
				overflow: hidden;
			}
			.img-responsive {
			    display: block;
			    max-width: 100%;
			    height: auto;
			}
			.icon-truck {
			    background: url('/media/mobileweb/images/topbar-truck-icon.png') center center no-repeat;
			    background-size: 100%;
			    margin-right: 10px;
			}
			.navbar-toggle .icon-bar {
			    background: url('/media/mobileweb/images/topbar-menu-icon.png') center center no-repeat;
			    background-size: 100%;
			    width: 28px;
			    height: 30px;
			    display: inline-block;
			}
			.icon-shopbag {
			    color: #fff;
			    font-size: 25px;
			}
			.icon-shopbag i {
			    position: absolute;
			    left: 5px;
			    color: #fff;
			}
			#mobilehomeMainDiv {
			    min-height: 30vh;
			    margin-bottom: 20px;
			    width: 99%;
			}
			#topSearchField {
			    margin: 0px 0 0 0;
			    height: 40px;
			    padding: 3px 35px;
			    background-color: #fff;
			    width: 100%;
			    color: #444444;
			    border: 1px solid #6AAA6D;
			    border-radius: 0;
			    font-size: 18px;
			}
			#search {
			    width: 95%;
			    margin: 10px auto 0;
			    position: relative;
			}
			#search span {
			    color: #6AAA6D;
			    position: absolute;
			    font-size: 20px;
			    left: 10px;
			    top: 10px;
			}
			input[type="text"], input[type="email"], input[type="password"] {
			    border-top: 2px solid #ffffff;
			    border-right: 2px solid #ffffff;
			    border-bottom: 2px solid #d4d4d4;
			    border-left: 2px solid #ffffff;
			    border-radius: 0;
			    box-sizing: border-box;
			    color: #818181;
			    display: block;
			    font-size: 16px;
			    font-weight: normal;
			    font-family: verdana;
			    min-width: 40%;
			    width: 100%;
			    box-shadow: none;
			    height: auto;
			    outline: none;
			    padding: 15px 5px 6px 5px;
			    -webkit-font-smoothing: antialiased;
			    -webkit-appearance: none;
			    -moz-appearance: none;
			    appearance: none;
			}
			.fnt-18 {
			    font-size: 18px;
			}
			.wid57 {
				width: 57%;
			}
			footer .anchors {
			    -webkit-column-count: 2;
			    -moz-column-count: 2;
			    column-count: 2;
			    margin: 31px 41px;
			}
			footer .anchors a {
			    display: block;
			    width: 100%;
			    font-size: 15px;
			    color: #fff;
			    padding: 10px 0;
			}
			footer .footercontent {
			    background-color: #6AAA6E;
			    display: inline-block;
			    width: 100%;
			}
			footer .socialmedia {
			    border-top: 2px dotted #fff;
			    padding: 18px;
			    text-align: center;
			}
			footer .storemedia {
			    border-top: 2px dotted #fff;
			    padding: 18px 38px;
			}
			footer .storemedia img {
			    width: 48%;
			}
			footer .basicHits {
			    margin: 20px;
			    color: #458b4c;
			    font-size: 10px;
			    text-align: center;
			}
			footer .basicHits a {
			    color: #458b4c;
			    padding: 10px 0;
			    outline: none;
			}
			footer .copyright {
			    margin: 0 20px 20px;
			    color: #333333;
			    font-size: 10px;
			    text-align: center;
			}
			
			.navMenuItems-topIcon {
				display: inline-block;
				height: 40px;
		    	width: 40px;
			}
			.navMenuItems-topIcon li.ui-menu-item {
			}
			.iconnav {
			    width: 35px;
			    height: 40px;
			    background-size: 100%;
			    background-position: center center;
			    background-repeat: no-repeat;
			    display: inline-block;
			}
			
			/* left top icons */
			.icon-browse-shop {
			    background-image: url('/media/mobileweb/images/browse-shop-icon.png');
			}
			/*.mm-iconpanel .mm-panel.mm-subopened .icon-browse-shop {
			    background-image: url('/media/mobileweb/images/browse-shop-active-icon.png');
			}*/
			
			.icon-browse-reorder {
			    background-image: url('/media/mobileweb/images/browse-reorder-icon.png');
			}
			/*.mm-iconpanel .mm-panel.mm-subopened .icon-browse-reorder {
			    background-image: url('/media/mobileweb/images/browse-reorder-active-icon.png');
			}*/
			
			.icon-browse-tag {
			    background-image: url('/media/mobileweb/images/browse-tag-icon.png');
			}
			/*.mm-iconpanel .mm-panel.mm-subopened .icon-browse-tag {
			    background-image: url('/media/mobileweb/images/browse-tag-active-icon.png');
			}*/
			
			.primaryLink a {
				color: #a1a1a1;
				text-decoration: none;
				padding: 5px 10px 5px 20px;
				display: block;
			}
			.pNavLoginButton {
			    margin: 12px 18px;
			    background-color: #fafafa;
			    padding: 0px;
			    color: #6aaa6d;
			    font-size: 1.2em;
			    font-weight: normal;
			    text-align: center;
			}
			.pNavLoginButton a {
			    padding: 10px 10px 10px 20px;
			    height: 48px;
			    padding: 0px;
			    line-height: 48px;
			    color: #4fa157;
			    border-radius: 4px;
			    border: 1px solid #4fa157;
			}
			.createacc a {
			    color: #fff;
			    background: #6aaa6d;
			}
			.pNavLoginButton-cont {
				margin-top: 30px;
			}
			#navMenuItems > li {
				width: 100%;
			}
			#navMenuItems .ui-state-active,
			#navMenuItems .ui-state-focus {
				border: none;
			}
			#navMenuItems li {
			    color: #458b4c;
			    font-size: 1em;
			}
			#navMenuItems li.ui-menu-item.navMenuItems-topIcon {
			    /*margin: 20px 0 20px 10px;*/
			}
			#navMenuItems li.ui-menu-item {
			    padding-left: 30px;
			}
			.navMenuItems-topIcon > .navlabel {
				line-height: 40px;
				/*padding-left: 20px;*/
				font-weight: bold;
			}
			.navlabel {
				display: inline-block;
				vertical-align: top;
				margin-top: 1em;
			}
			
			.navMenuItems-browse {
				right: 0;
			}
			.glBreadcrumblink {
			    display: block;
			    padding: 0.8em;
			    padding-left: 10px;
			    border: 1px solid #dbdbdb;
			    border-width: 0 0 1px 0;
			}
			.glBreadcrumblink > .ui-icon {
				background-size: 100%;
				height: 30px;				
				width: 30px;
				display: inline-block;
			}
			
			@font-face {
			  font-family: 'Glyphicons Halflings';
			
			  src: url('/media/mobileweb/fonts/glyphicons-halflings-regular.eot');
			  src: url('/media/mobileweb/fonts/glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'), url('/media/mobileweb/fonts/glyphicons-halflings-regular.woff2') format('woff2'), url('/media/mobileweb/fonts/glyphicons-halflings-regular.woff') format('woff'), url('/media/mobileweb/fonts/glyphicons-halflings-regular.ttf') format('truetype'), url('/media/mobileweb/fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
			}
			.glyphicon {
			    position: relative;
			    top: 1px;
			    display: inline-block;
			    font-family: 'Glyphicons Halflings';
			    font-style: normal;
			    font-weight: 400;
			    line-height: 1;
			    -webkit-font-smoothing: antialiased;
			    -moz-osx-font-smoothing: grayscale;
			}
			.glyphicon-search:before {
			    content: "\e003";
			}
			.glyphicon-shopping-cart:before {
			    content: "\e116";
			}
			.glyphicon-menu-left:before {
			    content: "\e257";
			}
			.icon-search {
			    display: inline-block;
			    background-image: url('/media/mobileweb/images/searchbar-icon.png');
			    background-repeat: no-repeat;
			    background-size: 100%;
			    height: 20px;
			    width: 20px;
			}
			.icon-cart {
			    display: inline-block;
			    background-image: url('/media/mobileweb/images/cart-icon.png');
			    background-repeat: no-repeat;
			    background-size: 100%;
			    height: 25px;
			    width: 25px;
			}
			.pull-left {
			    float: left !important;
			}
			.pull-right {
			    float: right!important;
			}
			.close-x {
			    top: 0;
			    bottom: initial;
			    left: initial;
			    right: 0;
			    background-image: url('/media/mobileweb/images/close-grey-icon.png');
			    background-size: 100%;
			    height: 24px;
			    width: 24px;
			    margin: 6px;
			    z-index: 99;
			    position: absolute;
			    left: auto;
			}
			.locabar-tab {
			    display: inline-block;
			    padding: 5px 15px;
			    margin-top: 9px;
			    background-color: #E1F0DE;
			    color: #6AAA6D;
			    font-size: 24px;
			    font-weight: bold;
			    line-height: 23px;
			}
			.locabar-tab-fdx {
			    display: inline-block;
			    background-image: url('/media/layout/nav/globalnav/fdx/locabar-tab-fdx.png');
			    background-repeat: no-repeat;
			    height: 23px;
			    width: 84px;
			}
			.navMenuItems-menuselect > .glBreadcrumblink { /* green right-side box */
				border-right: 9px solid #4fa157;
			}
			
			/* new nav classes */
			#nav-menu {
				background-color: #fff;
			}
			.mm-navbar {
				background-color: #6AAA6D;
				height: 60px;
			}
			.mm-navbar-bottom > .navbar-cont {
				background-color: #6AAA6D;
				padding-top: 0;
				height: 50px;
			}
			.mm-navbar-bottom {
    			height: 50px;
			}
			.mm-panels>.mm-panel.mm-hasnavbar {
			    padding-top: 60px; /* this needs to be >= .mm-navbar height */
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-1 {
				left: 80px;
			}
			.navMenuItems-topIcons {
				background-color: #f1f1f1;
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .iconnav {
				display: block;
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .navlabel {
				width: 80px;
			    display: inline-block;
			    text-align: center;
			    margin-left: -20px;
			    text-overflow: ellipsis;
			    white-space: nowrap;
				overflow: hidden;
				margin-top: 0;
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .mm-selected {
				background-color: transparent;
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .hide-on-subopen {
				display: none;
			}
			.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened > .mm-subblocker {
				opacity: 0; /* don't make hidden so touch will still close subs */
			}
			.mm-menu .mm-navbar a, .mm-menu .mm-navbar>* { /* submenu title */
			    color: rgba(255,255,255,1);
			}
			.mm-menu .mm-btn:after, .mm-menu .mm-btn:before { /* submenu back arrow */
			    border-color: rgba(255,255,255,1);
			}
		</style>
		
	</tmpl:put>
	<tmpl:put name="customJsBottom">
		
	</tmpl:put>
	<tmpl:put name="extraJsFooter"><%-- MOBILE, end of body --%>		
		
		<fd:LocationHandler/>
		<%
			AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
			boolean hasFdxServices = true; /* just true in locationbar as well */  //LocationHandlerTag.hasFdxService( ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) );
		%>
		<script>
			FreshDirect = FreshDirect || {};
			FreshDirect.locabar = FreshDirect.locabar || {};
			FreshDirect.locabar.zipcode = <%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) %>;
			FreshDirect.locabar.hasFdxServices = <%=hasFdxServices %>;
			FreshDirect.locabar.selectedAddress = {
				type: null,
				address: '<%= LocationHandlerTag.formatAddressTextWithZip(selectedAddress) %>'
			};
		</script>
		<script src="/assets/javascript/jquery/mmenu/js/jquery.mmenu.all.min.js" type="text/javascript"></script>
		<script>
			(function ($) {
				$(document).ready(function() {
					$("#nav-menu").mmenu({
						"extensions": [
							"pagedim-black",
							"border-full"
                     	],
                     	"offCanvas": {
							"zposition": "front"
						},
						"navbar": {
							"title": "<img src=\"/media/mobileweb/images/topbar-fd-logo.png\" alt=\"FreshDirect\" class=\"img-responsive\" />"
						},
						"navbars": [
							<% if (hasFdxServices && FDStoreProperties.isFdxTabEnabled()) { %>
								{
									"position": "bottom",
									"content": "<div class='navbar-cont'><a href='https://foodkick.freshdirect.com' class='locabar-tab locabar-tab-fdx-cont'><div class='locabar-tab-fdx'></div></a></div>"
									}
							<% } %>
						],
						"iconPanels": true,
						"screenReader": true,
						"setSelected": {
				            "hover": true,
				            "parent": true
				         }
                  	}, {
						// configuration
						offCanvas: {
							pageSelector: "#page-content"
						}
					});
					
					var API = $("#nav-menu").data( "mmenu" );
					
					$jq('#navbarShow').on('click', function() {
						console.log('#navbarShow', 'click');
						API.open();
					});
					$jq('.signin>a').on('click', function(e) {
						e.stopPropagation();
						$jq('.loginDialog').addClass('open');
						$jq('body').addClass('no-scroll');

						API.close();
					});
					
					$jq('.loginDialog .close-x').on('click', function(e) {
						$jq('.loginDialog').removeClass('open');
						$jq('body').removeClass('no-scroll');
					});
				});
			}(jQuery));
			
		</script>
	</tmpl:put>
	
	<tmpl:put name="nav_top"><%-- MOBILE --%>
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
	</tmpl:put>
	
	<tmpl:put name="nav_bottom"><%-- MOBILE --%>
		<%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<%!

		//APPDEV- 4368:: Need Indicator for Empty Picks List Begin
		public static boolean hasProduct(CategoryModel categoryModel){
			boolean hasProduct = false;
			
			if(!categoryModel.getSubcategories().isEmpty())
			{
				List<CategoryModel> subCategories = categoryModel.getSubcategories();
				for (CategoryModel m1 : subCategories) {
					boolean result = hasProduct(m1);
					if(result){
						return result; 
					}
				}
			}
			if(categoryModel.getProducts().size()>0)
			{
				return isProductAvailable(categoryModel.getProducts());
			}else
				return false;
		}
		
		public static boolean isProductAvailable(List<ProductModel> prodList){
			boolean result = false;
			
			for(ProductModel model:prodList){
				if(!(model.isUnavailable() || model.isDiscontinued())){
					result = true;
					break;
				}
			}
			return result;
		}
	%>
	<%
		if (mobWeb) { %>
			<div id="mobilehomeMainDiv">
			<%
				//OAS setup
			   	request.setAttribute("listPos", "SystemMessage,HPMob01,HPMob02");
				/* these use OAS pages like www.freshdirect.com/mobileweb/[PAGENAME] */
			   	
			   	if (FDStoreProperties.isAdServerEnabled()) {
					%><div id="OAS_HPTab1">
			  			<script type="text/javascript">OAS_AD('HPMob01');</script>
			  		</div><% 
					%><div id="OAS_HPTab2">
		  				<script type="text/javascript">OAS_AD('HPMob02');</script>
		  			</div><% 
			  	}
						   	
				List<CategoryModel> catModels = ContentFactory.getInstance().getStore().getiPhoneHomePagePicksLists();
				
				for (CategoryModel curCat: catModels) {
					//skip if cat has no prod(s)
					if (!hasProduct(curCat)) {
						continue;
					}
					
					 %>
					 
					<a href="/browse.jsp?id=<%= curCat %>">
				    <div class="home-page-banner">
					    <%
					    	boolean bannerFound = false;
					        Set<ContentKey> banners = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
					        for (ContentKey key : banners) {
					            BannerModel curBanner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
	
				            	if (curBanner != null && curBanner.getLink() != null && curBanner.getImage() != null && StringUtils.equals(curBanner.getLink().getContentName(), curCat.getContentName())) {
					      			%><img src="<%= curBanner.getImage().getPathWithPublishId() %>" alt=""><%
					      			bannerFound = true;
					      			break;
				            	}
	
					        }
					        if (!bannerFound) {
					        	%><img src="<%= curCat.getTabletThumbnailImage().getPathWithPublishId() %>" alt=""><%
					        }
						%>
				        <div class="helpDiv"></div>
						<span class="span-left"><%= curCat.getFullName() %></span>
						<span class="span-right"><%= curCat.getPrimaryText() %></span>
					</div>
				    </a>
				<% } %>
			</div>
			<div class="loginDialog">
				<div class="close-x"></div>				
				<iframe src="/login/login.jsp"></iframe>
			</div>
		<% } else { %>
			
			<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">
			
			<%
				boolean location2Media = false;
				if(null != segmentMessage && segmentMessage.isLocation2()) {
			        	location2Media = true;
			    }
			   	request.setAttribute("listPos", "SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom,HPLeftBottom,HPMiddleBottom,HPRightBottom");
			%>
			
			<% 
			boolean showAltHome = false;
			if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || 
				(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) 
					showAltHome = true;
			
				//Coupons disabled warning msg
				if (!user.isCouponsSystemAvailable() && !sessionUser.isCouponWarningAcknowledged() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
			        sessionUser.setCouponWarningAcknowledged(true);
			%>
			        <div style="display: none;" id="fdCoupon_indexAlert">
			                <div style="text-align: center;"><img src="/media/images/ecoupon/logo-purpler_old.png" alt="fdCoupon" height="85" width="222" /></div>
			                <div class="error_title"  style="text-align: center; margin: 20px 20px;"><%= (SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE).replace("unavailable.", "unavailable.<br />") %></div>
			                <div style="text-align: center; margin-bottom: 20px;"><img id="clickToContinue" style="cursor: pointer;" src="/media/images/ecoupon/click-here-to-continue_large.png" alt="Click Here To Continue" /></div>
			        </div>
			        <script type="text/javascript" language="javascript">
			                $jq(function() {
			                        var overlayDialog = doOverlayDialogBySelector('#fdCoupon_indexAlert');
			                        $jq('#clickToContinue').live('click', function(e) { e.preventDefault(); overlayDialog.dialog('close'); });
			                });
			        </script>
			<%
				}
			%>
			 	<div class="holder">
					<%-- MAIN CONTENT--%>
						<div class="content"> 
			<% if (showAltHome && !location2Media) { 
				%><comp:homePageLetter user="<%= user %>" />
			<%} else if (!showAltHome && location2Media) { 
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
			<% 
			} else if (!showAltHome && !location2Media) { 
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
				  <comp:deliverySlotReserved user="<%= user %>" />
			<%
			}
				   	
			if (location2Media) { %><comp:location2Media user="<%= user %>" /><% } 
			%>
					<comp:OASFeature 
						top="HPFeatureTop"
						left="HPFeature"
						tab1="HPTab1"
						tab2="HPTab2"
						tab3="HPTab3"
						tab4="HPTab4"
						bottom="HPFeatureBottom"
						hpBottomLeft="HPLeftBottom"
						hpBottomMiddle="HPMiddleBottom"
						hpBottomRight="HPRightBottom"
					/>
			<%
				   		StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
				   		if (store != null) {
				   			ConfigurationContext confContext = new ConfigurationContext();
				   			confContext.setFDUser(user);
				   			ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
				   			String trkCode = "favorites";
				   			request.setAttribute("trk",trkCode);
				   			if (validOrderCount<=3){
			%>
					   			<div id="most-popular">
					   				<h2 class="homepage-categories-header">
					   					<span>most popular products</span>
					   				</h2>
					   				<potato:recommender siteFeature="FAVORITES" name="deals" maxItems="24" cmEventSource="BROWSE"  sendVariant="true" />
					   				<soy:render template="common.ymalCarousel" data="${deals}" />
					   			</div>
			<%
			          } else {
			          %>
			            <div id="top-items" class="">
			                <potato:recommender siteFeature="TOP_ITEMS_QS" name="topItems" maxItems="24" cmEventSource="BROWSE" sendVariant="true" />
			                <soy:render template="common.yourTopItemsCarousel" data="${topItems}" />
			            </div>
			          <%
			          }  
				   			Html edtMed = store.getEditorial();
							if ( edtMed != null ) { %>
								<fd:IncludeHtml html="<%= edtMed %>"/>
							<% } else {
								String categoryLinks = FDStoreProperties.getHPCategoryLinksFallback();
								if ( categoryLinks != null ) {
									%><fd:IncludeMedia name="<%= categoryLinks %>"></fd:IncludeMedia><%
								}
							}
						}
			%>
						<div class="oas_home_bottom"><script type="text/javascript">OAS_AD('HPWideBottom');</script></div>
					</div>
					<%-- Removed the learn more for marketing change. --%>
					<%-- <div id="bottom_link"><a href="/welcome.jsp"><img src="/media_stat/images/home/fd_logo_learn_more_back.jpg" alt="Learn More About Our Services"></a></div> --%>
				<%-- END MAIN CONTENT--%> 
			<!-- end of APPDEV-4287 -->
				
			</div>
			</fd:GetSegmentMessage>
		<% } %>
		
		
				
		<!-- Dstillery pixel swap -->	
			<!--commented as a part of APPDEV-4287 <script src="//action.media6degrees.com/orbserv/hbjs?pixId=26207&pcv=47" type="text/javascript" async></script>-->
		<!-- start of APPDEV-4287 -->	
		<script type="text/javascript" async>
			function asyncPixelWithTimeout() {
			var img = new Image(1, 1);
			img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26207&pcv=47';
			setTimeout(function ()
			{ if (!img.complete) img.src = ''; /*kill the request*/ }
			
			, 33);
			};
			
			asyncPixelWithTimeout();
		</script>
</tmpl:put>
</tmpl:insert>
