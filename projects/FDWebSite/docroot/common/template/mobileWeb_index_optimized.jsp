<!DOCTYPE html>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri='logic' prefix='logic' %>
<%
	/* ================================================================================
	 *	THIS IS A WIP VERSION FOR OPTIMIZATION TESTS
	 * 	DO NOT USE ON PAGES BESIDES INDEX WITHOUT TESTING
	 *	20170913 batchley
	 *
	 *	This template removes a large amount of unused (on index) code. This includes
	 *	shared support libraries. Things that will not work (there may be more):
	 *		Search Autocomplete, overlays that use ifrPopup, cart prod count (init'd)
	 * ================================================================================ */

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String mobweb_uri = request.getRequestURI();
	boolean isReorder = (mobweb_uri.indexOf("/quickshop/") != -1) ? true : false;
	boolean isReorderItems = (mobweb_uri.indexOf("/qs_top_items.jsp") != -1) ? true : false;
	boolean isReorderLists = (mobweb_uri.indexOf("/qs_shop_from_list.jsp") != -1) ? true : false;
	boolean isReorderOrders = (mobweb_uri.indexOf("/qs_past_orders.jsp") != -1) ? true : false;
	boolean isCheckout = (mobweb_uri.indexOf("/expressco/") != -1) ? true : false;
	boolean isHelp = (mobweb_uri.indexOf("/help/") != -1) ? true : false;

	Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
	boolean useFdxGlobalNav = FDStoreProperties.isFdxLocationbarEnabled();

	request.setAttribute("inMobWebTemplate", true);
	request.setAttribute("inMobWebTemplateOptimized", true); //used in menu includes
%>
<html lang="en-US" xml:lang="en-US">
  <head>
  	<title><tmpl:get name="title"/></title>
  	
	<style>
		<%-- INLINING CSS from /assets/css/jquery/ui/1.12.1/jquery-ui.css --%>
		.ui-helper-hidden-accessible {
			border: 0;
			clip: rect(0 0 0 0);
			height: 1px;
			margin: -1px;
			overflow: hidden;
			padding: 0;
			position: absolute;
			width: 1px;
		}
		<%-- INLINING CSS from grid.css --%>
		html {
			margin: 0;
			padding: 0;
			border: 0;
		}
		
		body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, code, del, dfn, em, q, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, dialog, figure, footer, header, hgroup, nav, section {
			margin: 0;
			padding: 0;
			border: 0;
			font-size: 100%;
			font: inherit;
		}
		
		img {
			padding: 0;
			border: 0;
			font-size: 100%;
			font: inherit;
		}
		article, aside, details, figcaption, figure, dialog, footer, header, hgroup, menu, nav, section {
			display: block;
		}
		
		body {
			line-height: 1.5;
			background: white;
		}
		
		table {
			border-collapse: separate;
			border-spacing: 0;
		}
		
		caption, th, td {
			font-weight: normal;
			float: none !important;
		}
		a img {
			border: none;
		}
		html {
			font-size: 100.01%;
		}
		
		body {
			font-size: 70%;
			color: #333;
			background: #fff;
			font-family: Verdana, "DejaVu Sans", sans-serif;
		}
		
		a, a:link {
			color: #458b4c;
			text-decoration: none;
		}
		
		a:visited {
			color: #777;
			text-decoration: none;
		}
		
		a:hover, a:active, a:focus {
			color: #5fb069;
			text-decoration: underline;
		}
		p {
			margin: 1.5em 0;
		}
		strong,dfn {
			font-weight: bold;
		}
		ul, ol {
			margin: 0 1.5em 1.5em 0;
			padding-left: 1.5em;
		}
		
		ul {
			list-style-type: disc;
		}
		.bold {
			font-weight: bold
		}
		.offscreen {
			position: absolute;
			left: -99999px;
		}
		.offscreen {
			position: absolute;
			clip: rect(1px 1px 1px 1px);
			clip: rect(1px, 1px, 1px, 1px);
			padding: 0;
			border: 0;
			height: 1px;
			width: 1px;
			overflow: hidden;
		}
		body {
			margin: 0;
		}
		hr {
			background: #888;
			color: #888;
			clear: both;
			float: none;
			width: 100%;
			height: .1em;
			margin: .5em 0 .5em;
			border: none;
		}
		.clearfix:after, .container:after {
			content: ".";
			display: block;
			height: 0;
			clear: both;
			visibility: hidden;
		}
		
		.clearfix, .container {
			display: inline-block;
		}
		.clearfix, .container {
			display: block;
		}
				
		<%-- INLINING CSS from global.css --%>
		body {
			-webkit-font-smoothing: subpixel-antialiased;
		}
		:focus {
			outline: #5fb069 solid 2px;
		}
		
		div#skip_to_content, .locabar-tab-fdx-cont, body:focus, html:focus, div:focus, *:active, a.accessibleOutlineFF:focus, cssbutton:focus, tr:focus, td:focus, fieldset:focus, .tabbed-carousel .tabs li:focus, #quickshop .qs-tabs a:focus, a.portrait-item-header-name:focus, [data-component="categorylink"]:focus, div#topnavitem_signup a, div#topnavitem_standingorders a, .dropdown-column li a:focus, .subdepartments > li > span > a:link, .pdp-evenbetter-productinfo a {
			outline: none;
		}
		* {
			-webkit-tap-highlight-color: transparent;
		}

		<%-- INLINING CSS from /assets/css/common/locationbar_fdx.css --%>
		#messages {
			width: 970px;
			position: relative;
			text-align: center;
			margin: 0 auto;
		}
		#messages.open ul {
			max-height: none;
			box-shadow: 0px 3px 9.9px 0.1px rgba(0, 0, 0, 0.3);
			margin: 10px auto;
		}
		#messages.open ul li {
			padding: 15px;
			border-bottom: 1px solid #ccc;
			border-top: 1px solid transparent;
			margin: 0;
		}
		#messages.open .close-handler {
			top: 0;
			bottom: initial;
			left: initial;
			right: 0;
			background-image:  url('/media/layout/nav/globalnav/fdx/close-x.png');
			height: 12px;
			width: 12px;
			margin: 6px;
			z-index: 99;
			position: absolute;
			left: auto;
		}
		#messages.open .close-handler:before, #messages.open .close-handler:after {
			content: '';
			width: 0;
		}
		#messages .shadow {
			display: none;
		}
		.locabar-search {
			background-image: url('/media/layout/nav/globalnav/fdx/search-icon.png');
			background-repeat: no-repeat;
			min-height: 22px;
			min-width: 23px;
		}
		.posAbs {
			position: absolute;
			margin-top:10px;
			box-shadow: 0px 25px 20px -20px rgba(0, 0, 0, 0.3), 0px 0px 40px 0px rgba(0, 0, 0, 0.2)!important;
		}
		.cursor-pointer {
			cursor: pointer;
		}
		.bold.cursor-pointer{
			-webkit-font-smoothing: antialiased;
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
		
		.locabar-section {
			display: inline-block;
			color: #fff;
		}
		
		.locabar-circle-cont {
			border-radius: 14px;
			color: #6AAA6D;
			background-color: #fff;
			font-size: 11px;
			font-weight: bold;
			padding: 3px;
			line-height: 14px;
			min-width: 14px;
			text-align: center;
			display: inline-block;
			position: absolute;
			top: 3px;
			border: 2px solid #6AAA6D;
		}
		
		.locabar-cart-count-cont .locabar-circle-cont{
			top: 2px;
			margin-left:-5px;
			margin-top: 1px;
		}
		.locabar-cart {
			display: inline-block;
			background-image: url('/media/layout/nav/globalnav/fdx/locabar-cart_v2.png');
			height: 20px;
			width: 18px;
			background-repeat: no-repeat;
		    position: relative;
		}
		.locabar-tab-fdx {
			display: inline-block;
			background-image: url('/media/layout/nav/globalnav/fdx/locabar-tab-fdx.png');
			background-repeat: no-repeat;
			height: 23px;
			width: 84px;
		}
		.locabar_triggers{
			height:40px;
		}
		.locabar-cart-count-cont {
			display: inline-block;
			width: 45px;
			margin-right:0px;
		}
		#locabar_addresses_trigger, #locabar_popupcart_trigger, #locabar_user_trigger {
			margin-top: 5px;
			padding-left: 8px;
			padding-right: 8px;
		}
		.locabar_triggers_menuitem a {
		    color: #fff;
		    text-decoration: none;
		}
		.locabar_triggers_menuitem a {
		    color: #666;
		}
		.locabar-tab-fd-cont, .locabar-tab-fdx-cont {
			background-color: #fff;
		}
		.locabar-tab-fd-cont, .locabar-tab-fdx-cont {
			box-shadow: 0px 2px 12px 0px rgba(0, 0, 0, 0.2);
		}
		.locabar-cart {
		    background-image: url('/media/layout/nav/globalnav/fdx/locabar-cart-green.svg');
		}
		.locabar-tab-fdx, .locabar-tab-fdx-cont:hover .locabar-tab-fdx, .locabar-tab-fdx-cont:focus .locabar-tab-fdx {
		    background-image: url('/media/layout/nav/globalnav/fdx/locabar-tab-fdx-purple.svg');
		}
		.locabar-circle-cont {
	    	color: #fff;
	    	background-color: #6AAA6D;
	    	border: 2px solid #fff;
		}
		.locabar-tab {
			padding: 8px 15px 5px;
		}

		
		<%-- INLINING CSS from /assets/css/common/messages.css - ENTIRE FILE --%>
		<fd:IncludeMedia name="/assets/css/common/messages.css" withErrorReport="false" />
		<%-- INLINING CSS from /assets/css/accessibility/override.css - ENTIRE FILE --%>
		<fd:IncludeMedia name="/assets/css/accessibility/override.css" withErrorReport="false" />
		<%-- INLINING CSS from /assets/css/jquery/mmenu/css/jquery.mmenu.all.css - ENTIRE FILE --%>
		<fd:IncludeMedia name="/assets/css/jquery/mmenu/css/jquery.mmenu.all.css" withErrorReport="false" />
		
		<%-- INLINING CSS from /assets/css/mobileweb/mobileweb_common.css --%>
		body {
			font-family: Verdana,"Helvetica Neue",Helvetica,Arial,sans-serif;
			font-size: 14px;
			line-height: 1.42857143;
			color: #333;
			background-color: #fff;
			font-weight: normal;
			-webkit-animation:androidbugfix infinite 1s;
			overflow-x: hidden;
		}
		
		img {
			vertical-align: middle;
		}
		img {
			border: 0;
		}
		.capital { text-transform: uppercase; }
		.invisible {
			width: 0;
		}
		.home-page-banner {
			margin: 10px;
			margin-top: 0;
			position: relative;
		}
		.home-page-banner-top img, .home-page-banner img {
			width: 100%;
		}
		.home-page-banner-subtext-cont {
			position: absolute;
			bottom: 0;
			width: 100%;
			background: -moz-linear-gradient(top,  rgba(0,0,0,0.3) 0%, rgba(0,0,0,0.3) 100%); /* FF3.6-15 */
			background: -webkit-linear-gradient(top,  rgba(0,0,0,0.3) 0%,rgba(0,0,0,0.3) 100%); /* Chrome10-25,Safari5.1-6 */
			background: linear-gradient(to bottom,  rgba(0,0,0,0.3) 0%,rgba(0,0,0,0.3) 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#4d000000', endColorstr='#4d000000',GradientType=0 ); /* IE6-9 */			
		}
		.home-page-banner-subtext {
			line-height: 30px;
			height: 30px;
			color: #fff; 
			text-align: center;
		}
		.navbar-brand, .navbar-nav>li>a {
			text-shadow: 0 1px 0 rgba(255,255,255,.25);
		}
		.navbar-brand {
			float: left;
			padding: 15px 15px;
			font-size: 18px;
			line-height: 20px;
			padding: 15px 0 0 0;
			width: 62%;
		}
		@media screen and (max-width: 320px) {
			.navbar-brand {
				padding: 18px 0 0 0;
			}
		}
		.navbar {
			background-color: #6AAA6D;
			/* min-height: 120px; */
			margin: 0;
		}
		.navbar-header {
			padding: 2px 5px;
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
			margin-right: 15px;
			margin-bottom: 5px;
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
		@media screen and (max-width: 320px) {
			.navbar-ecomvalue {
				margin: 8px 0 0 0;
			}
		}
		.navbar-ecomvalue a {
			width: 40px;
			height: 30px;
			display: block;
		}
		#navMenuItems .navbar {
			height: 65px;
		}
		.navbar .navbar-header .rightSide {
			margin: 0 6px 0 0;
			max-height: 40px;
		}
		@media screen and (max-width: 320px) {
			.navbar .navbar-header .rightSide {
				margin: 0 0 0 0;
			}
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
		.locabar-cart-count-cont .locabar-circle-cont {
			top: -6px;
		}
		#mobilehomeMainDiv {
			min-height: 30vh;
			margin-bottom: 20px;
			width: 99%;
		}
		#topSearchField, /* id is an override */
		#search-faq, /* id is an override */
		.search-field {
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
		#search, /* id is an override */
		.search-cont {
			width: 95%;
			margin: 10px auto 10px auto;
			position: relative;
		}
		#search span, /* id is an override */
		.search-cont span {
			color: #6AAA6D;
			position: absolute;
			font-size: 20px;
			left: 10px;
			top: 6px;
		}
		.search-cont #search-faq {
			height: 32px;
			font-weight: bold;
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
		.wid56 { /* obs */
			width: 56%;
		}
		.wid57 { /* obs */
			width: 57%;
		}
		.navbar-header-left {
			width: 51%;
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
			margin: 10px 0;
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
			padding: 9px 10px 10px 20px;
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
		.close-x, #messages.open .close-handler, #modifyorderalert .alert-closeHandler {
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
		.mm-menu .mm-panel ul.mm-listview li.navMenuItems-menuselect > span.glBreadcrumblink { /* green right-side box */
			border-right: 9px solid #4fa157;
			padding: 9px 10px 10px 20px;
		}
		
		/* new nav classes */
		.mm-subopened .mm-subblocker {
			z-index: 3;
		}
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
		.mm-panel.mm-hasnavbar.mm-hasnavbar-rem .mm-navbar {
		    display: none;
		}
		.mm-panels>.mm-panel.mm-hasnavbar-rem {
		    margin-top: 60px; /* this needs to be >= .mm-navbar height */
		    padding-top: 0;
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
		.mm-menu .mm-panel ul.mm-listview li span.glBreadcrumblink{
			padding: 0;
		}
		.mm-menu .mm-panel ul.mm-listview li span.glBreadcrumblink a{
			display: block;
			padding: 9px 10px 10px 20px;	
		}
		.mm-menu .mm-navbar a, .mm-menu .mm-navbar>* { /* submenu title */
			color: rgba(255,255,255,1);
		}
		.mm-menu .mm-btn:after, .mm-menu .mm-btn:before { /* submenu back arrow */
			border-color: rgba(255,255,255,1);
		}
		.boldgray {
			font-weight: bold;
			color: #656565;
		}
		.no-divider:after {
			border-bottom-width: 0;
		}
		.NOMOBWEB { /* generic hidden class */
			display: none;
			visibility: hidden;
		}
		
		/* global nav overrides */
			#topSearchField {
				float: none;
			}
			#messages,
			#messages ul,
			.alert-cont {
				width: 100%;
			}
		
		/* OAS */
			.oas-cnt img {
			    max-width: 100%;
			}
		/* mobWeb needs white icon */
		.locabar-cart {
		    background-image: url('/media/layout/nav/globalnav/fdx/locabar-cart.png');
		}
	</style>
    
	<%-- Keep the media include last, so it can always override any css auto-loaded --%>
	<fd:IncludeMedia name="/media/editorial/site_pages/stylesheet.html" />
	
  	<tmpl:get name="seoMetaTag"/><%-- if title is used, overrides previous tag --%>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<meta name="fragment" content="!">

  	<tmpl:get name='facebookmeta'/>

    <tmpl:get name="extraCss" />
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />
    <tmpl:get name="extraJs" />
    <tmpl:get name='nutritionCss'/>

		<%-- Feature version switcher --%>
		<features:potato />
		<%-- LOAD JS --%>
		<jwr:script src="/mobileweb_index_optimized_jquerylibs.js" useRandomParam="false" />
		<script type="text/javascript">
			(function () {
				window.FreshDirect = window.FreshDirect || {};
				
				var FD = FreshDirect || {};
				FD.USQLegalWarning = FreshDirect.USQLegalWarning || {};
				FD.USQLegalWarning.sessionStore = '<%=session.getId()%>';
				FD.USQLegalWarning.getJSessionId = FD.USQLegalWarning.getJSessionId ||
				function () { return FD.USQLegalWarning.sessionStore; };
				
				var fd = window.FreshDirect;
				fd.libs = fd.libs || {};
				fd.libs.$ = jQuery;
	
			    <%-- THIS SETUP NEEDS TO BE BEFORE THE LOCABAR JS --%>
				FreshDirect.locabar = FreshDirect.locabar || {};
				FreshDirect.locabar.isFdx = <%= useFdxGlobalNav %>;
				$jq.fn.messages = function( method ) {};
				
				fd.features = fd.features || {};
				
				fd.features.active = <fd:ToJSON object="${featuresPotato.active}" noHeaders="true"/>
				
				fd.properties = fd.properties || {};
				fd.properties.isLightSignupEnabled = <%= FDStoreProperties.isLightSignupEnabled() ? "true" : "false" %>;
				fd.properties.isSocialLoginEnabled = <%= FDStoreProperties.isSocialLoginEnabled() ? "true" : "false" %>;
				fd.properties.isDebitSwitchNoticeEnabled = <%= FDStoreProperties.isDebitSwitchNoticeEnabled() ? "true" : "false" %>;
	
				<%
					FDSessionUser jsUser = (FDSessionUser)session.getAttribute(SessionName.USER);
					if (jsUser != null) {
						int sessionUserLevel = jsUser.getLevel();
						FDSessionUser sessionUser = (FDSessionUser) jsUser;
						boolean hideZipCheckPopup = (!FDStoreProperties.isZipCheckOverLayEnabled() || (jsUser.getLevel() != FDSessionUser.GUEST || jsUser.isZipCheckPopupUsed() || sessionUser.isZipPopupSeenInSession()));
	
						if (!hideZipCheckPopup){
						    sessionUser.setZipPopupSeenInSession(true);
						}
				
						String zipCode = jsUser.getZipCode();
						String cohortName = jsUser.getCohortName();
						MasqueradeContext jsMasqueradeContext = jsUser.getMasqueradeContext();
			    	%>
						fd.user = {};
						fd.user.recognized = <%= sessionUserLevel == FDUserI.RECOGNIZED %>;
						fd.user.guest = <%= sessionUserLevel == FDUserI.GUEST %>;
						fd.mobWeb = <%= FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, jsUser) && JspMethods.isMobile(request.getHeader("User-Agent")) %>;
						fd.user.isZipPopupUsed = <%= hideZipCheckPopup %>;
						fd.user.zipCode = '<%= zipCode %>';
						fd.user.cohortName = '<%= cohortName %>';
						fd.user.sessionId = '<%=session.getId()%>';
						fd.user.isCorporateUser = <%= sessionUser.isCorporateUser() %>;
						<% if (jsMasqueradeContext != null) {%>
							fd.user.masquerade = true;
						<% } %>
					<% } %>
			}());
		</script>
		<jwr:script src="/mobileweb_index_optimized_everythingelse.js" useRandomParam="false" />

	</head>
<!--[if lt IE 9]><body class="ie8" data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if gt IE 8]><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if !IE]><!--><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><!--<![endif]-->
    
    <div class="container-fluid" id="page-content"><!-- body cont s -->

		<!-- top nav s -->
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
		<!-- top nav e -->

		<div id="content" autoscroll="true"><!-- content starts here-->
			<!-- popcart s -->
				<%-- PLACEHOLDER(S) FOR NOW --%>
			<!-- popcart e -->

		   	<!-- messages s -->
		   	<%--<jsp:include page="/shared/messages/messages_fdx.jsp" />--%>
		   	<%-- inline so we can load js by itself --%>
			<div id="messages" class="visHidden">
				<ul class="content"></ul>
				<hr class="shadow">
				<a href="#" class="handler close-handler" onclick="return false;" id="locabar-messages-close"><span class="offscreen">close</span></a>
				<br class="NOMOBWEB" />
			</div>

		  	<% if (FDStoreProperties.isAdServerEnabled()) {
				%><div id="oas_SystemMessage">
  					<script type="text/javascript">OAS_AD('SystemMessage');</script>
  				</div>
			<% } else { %>
		    	<div class="message" data-type="system"><fd:GetSiteAnnouncements id="announcments" user="<%=user%>">
			    <logic:iterate id="ann" collection="<%=announcments%>" type="com.freshdirect.fdstore.FDSiteAnnouncementI">
			        <table width="100%" cellpadding="0" cellspacing="0" border="0">
			            <tr align="center">
			                <td>
			                    <font class="text12rbold"><%=ann.getHeader()%></font><br>
			                    <%=ann.getCopy()%>
			                    <br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6">
			                </td>
			            </tr>
			            <tr bgcolor="#999966"><td class="onePxTall"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td></tr>
			        </table><br>
			    </logic:iterate></fd:GetSiteAnnouncements></div><%
			} %>

			<%@ include file="/common/template/includes/i_cutoff_warning.jspf"%>

    		<div class="message invisible" id="deliveryetawarning" data-type="deliveryetawarning"><%@ include file="/common/template/includes/i_delivery_eta_info.jspf"%></div>

		   	<!-- messages e -->

		   	<!-- modorder s -->
		   	<%-- THIS IS HIDDEN FROM DISPLAY FOR NOW --%>
		   	<div id="modifyorderalert_cont" class="">
			   	<div id="modifyorderalert" class="alerts invisible" data-type="modifyorderalert">
					<comp:modifyOrderBar user="<%= user %>" modifyOrderAlert="true" htmlId="test_modifyorderalert" />
				</div>
		   	</div>
		   	<!-- modorder e -->

			<section class="tabs">
    			<!-- start : tabs -->
    			<tmpl:get name='tabs'/>
    			<!-- end : tabs -->
  			</section>
		    <nav class="leftnav" style="display: none;">
		      <!-- start : leftnav -->
		      <tmpl:get name='leftnav'/>
		      <!-- end : leftnav -->
		    </nav>
		   
			<tmpl:get name="content" />
	    </div><!-- content ends above here-->

	    <!-- bottom nav s -->
	    <%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	    <!-- bottom nav e -->
	</div><!-- body cont e -->
	<!-- body cont dialogs start -->
	<!-- body cont dialogs end -->


			
			<%@ include file="/common/template/includes/extol_tags.jspf" %>
			
			<%-- GTM initialization --%>
			<%@include file="/common/template/includes/i_gtm_datalayer.jsp" %>
			
			<fd:IncludeMedia name="/media/editorial/site_pages/javascript.html"/>
		
			<%
				FDUserI dpTcCheckUser = (FDUserI)session.getAttribute(SessionName.USER);
				FDSessionUser dpTcCheckSessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
		
				if (dpTcCheckUser != null && request.getRequestURI().indexOf("brownie_points.jsp") == -1 &&
						(dpTcCheckUser.getLevel() == FDSessionUser.SIGNED_IN && Boolean.FALSE.equals(dpTcCheckSessionUser.hasSeenDpNewTc()) && dpTcCheckUser.isDpNewTcBlocking())
					) {
					%>
						<script type="text/javascript">
				    		$jq(document).ready(function() {
					    		pop('/overlays/delivery_pass_tc.jsp?showButtons=true&count=true');
				    		});
				    	</script>
			    	<%
				}
			%>
			
			<% 
			//System.out.println("DELIVERYADDRESS_COMPLETE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+session.getAttribute("DELIVERYADDRESS_COMPLETE")); 
			if (session.getAttribute("DELIVERYADDRESS_COMPLETE") != null) {%>	
					
			<script>
			$jq(document).ready(function() {
				if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { 
					FreshDirect.components.ifrPopup.open({ 
						url: '/social/DeliveryAddressCreateSuccess.jsp'}); 
				} else {
					pop('/social/DeliveryAddressCreateSuccess.jsp');
				}
			});
			</script>
			
		<% 
			session.setAttribute("DELIVERYADDRESS_COMPLETE",null);
			} 
		%>
		
		<% 
			if (session.getAttribute("SOCIAL_LOGIN_EXIST") != null) {%>	
					
			<script>
			$jq(document).ready(function() {
				if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { 
					FreshDirect.components.ifrPopup.open({ 
						url: '/social/SocialAccountExist.jsp'}); 
				} else {
					pop('/social/SocialAccountExist.jsp');
				}
			});
			</script>
			
		<% 
			session.setAttribute("SOCIAL_LOGIN_EXIST",null);
			} 
		%>

    <%-- tmpl:get name="jsmodules" / --%>
	<tmpl:get name='extraJsModules'/>

    <tmpl:get name="extraJsFooter" />

	<script><%-- manually fire this for now, this will need changing --%>
		OAS_DONE('SystemMessage');
	</script>
	<% if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ %>
		<script>
			$jq(document).on('ready',  function() {
				FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp', width: 320, height: 400});
			});
		</script>
	<% } %>
	<%-- these should just be called "coremetrics includes" --%>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	<script>
		(function ($) {
			$(function () {
				$.smartbanner({daysHidden: 0, daysReminder: 0,author:'FreshDirect',button: 'VIEW'});
				if(!$jq('#smartbanner.shown').is(':visible')) { $jq('#smartbanner').show(); }
			});
		})($jq);
	</script>
  </body>
</html>
