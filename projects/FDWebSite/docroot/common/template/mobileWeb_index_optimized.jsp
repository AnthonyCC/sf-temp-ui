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
@charset "UTF-8"; 

.offscreen, .ui-helper-hidden-accessible {
	height: 1px;
	width: 1px;
	position: absolute;
	overflow: hidden
}

#smartbanner .sb-button, #smartbanner .sb-info em, .capital,
	.mm-listview .mm-divider {
	text-transform: uppercase
}

a, a:link, a:visited {
	text-decoration: none
}

.clearfix, .container, article, aside, details, dialog, figcaption,
	figure, footer, header, hgroup, menu, nav, section {
	display: block
}

.mm-listview>li>a, .mm-listview>li>span, .mm-navbar .mm-title {
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden
}

#messages.visHidden, .NOMOBWEB, .invisible {
	visibility: hidden
}

.ui-helper-hidden-accessible {
	border: 0;
	clip: rect(0, 0, 0, 0);
	margin: -1px;
	padding: 0
}

a, abbr, acronym, address, article, aside, blockquote, body, caption,
	code, dd, del, dfn, dialog, div, dl, dt, em, fieldset, figure, footer,
	form, h1, h2, h3, h4, h5, h6, header, hgroup, iframe, label, legend, li,
	nav, object, ol, p, pre, q, section, span, table, tbody, td, tfoot, th,
	thead, tr, ul {
	margin: 0;
	padding: 0;
	border: 0;
	font: inherit
}

img {
	padding: 0;
	font: inherit
}

table {
	border-collapse: separate;
	border-spacing: 0
}

caption, td, th {
	font-weight: 400;
	float: none !important
}

#messages li a, #messages li a:hover, #messages li a:visited, .bold,
	.locabar-circle-cont, dfn, strong {
	font-weight: 700
}

a img {
	border: none
}

html {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100.01%
}

body {
	background: #fff;
	margin: 0;
	-webkit-font-smoothing: subpixel-antialiased
}

a, a:link {
	color: #458b4c
}

a:visited {
	color: #458b4c
}

a:active, a:focus, a:hover {
	color: #5fb069;
	text-decoration: underline
}

p {
	margin: 1.5em 0
}

ol, ul {
	margin: 0 1.5em 1.5em 0;
	padding-left: 1.5em
}

ul {
	list-style-type: disc
}

.offscreen {
	left: -99999px;
	clip: rect(1px, 1px, 1px, 1px);
	clip: rect(1px, 1px, 1px, 1px);
	padding: 0;
	border: 0
}

hr {
	background: #888;
	color: #888;
	clear: both;
	float: none;
	width: 100%;
	height: .1em;
	margin: .5em 0;
	border: none
}

.clearfix:after, .container:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden
}

:focus {
	outline: #5fb069 solid 2px
}

#quickshop .qs-tabs a:focus, .dropdown-column li a:focus,
	.locabar-tab-fdx-cont, .pdp-evenbetter-productinfo a, .subdepartments>li>span>a:link,
	.tabbed-carousel .tabs li:focus, :active, [data-component=categorylink]:focus,
	a.accessibleOutlineFF:focus, a.portrait-item-header-name:focus, body:focus,
	cssbutton:focus, div#skip_to_content, div#topnavitem_signup a, div#topnavitem_standingorders a,
	div:focus, fieldset:focus, html:focus, td:focus, tr:focus {
	outline: 0
}

* {
	-webkit-tap-highlight-color: transparent
}

#messages {
	margin: 0 auto
}

#messages.open ul {
	box-shadow: 0 3px 9.9px .1px rgba(0, 0, 0, .3);
	margin: 10px auto
}

#messages.open ul li {
	padding: 15px;
	border-bottom: 1px solid #ccc;
	border-top: 1px solid transparent;
	margin: 0
}

#messages.open .close-handler:after, #messages.open .close-handler:before
	{
	content: '';
	width: 0
}

#messages .shadow {
	display: none
}

.locabar-search {
	background-image: url(/media/layout/nav/globalnav/fdx/search-icon.png);
	background-repeat: no-repeat;
	min-height: 22px;
	min-width: 23px
}

.posAbs {
	position: absolute;
	margin-top: 10px;
	box-shadow: 0 25px 20px -20px rgba(0, 0, 0, .3), 0 0 40px 0
		rgba(0, 0, 0, .2) !important
}

.cursor-pointer {
	cursor: pointer
}

.bold.cursor-pointer {
	-webkit-font-smoothing: antialiased
}

.locabar-section {
	display: inline-block;
	color: #fff
}

.locabar-circle-cont {
	border-radius: 14px;
	font-size: 11px;
	padding: 3px;
	line-height: 14px;
	min-width: 14px;
	text-align: center;
	display: inline-block;
	position: absolute;
	top: 3px
}

.locabar-cart-count-cont .locabar-circle-cont {
	margin-left: -5px;
	margin-top: 1px
}

.locabar-cart {
	display: inline-block;
	height: 20px;
	width: 18px;
	background-repeat: no-repeat;
	position: relative
}

.locabar_triggers {
	height: 40px
}

.locabar-cart-count-cont {
	display: inline-block;
	width: 45px;
	margin-right: 0
}

#locabar_addresses_trigger, #locabar_popupcart_trigger,
	#locabar_user_trigger {
	margin-top: 5px;
	padding-left: 8px;
	padding-right: 8px
}

.locabar_triggers_menuitem a {
	text-decoration: none;
	color: #666
}

#messages li a:hover, .icon-image a:focus {
	text-decoration: underline
}

#smartbanner .sb-close, .mm-indexer a, .mm-listview a, .mm-listview a:hover,
	.mm-navbar a, .mm-navbar a:hover, .primaryLink a {
	text-decoration: none
}

.locabar-tab-fd-cont, .locabar-tab-fdx-cont {
	background-color: #fff;
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .2)
}

.locabar-tab-fdx, .locabar-tab-fdx-cont:focus .locabar-tab-fdx,
	.locabar-tab-fdx-cont:hover .locabar-tab-fdx {
	background-image:
		url(/media/layout/nav/globalnav/fdx/locabar-tab-fdx-purple.svg)
}

.locabar-circle-cont {
	color: #fff;
	background-color: #6AAA6D;
	border: 2px solid #fff
}

#messages {
	left: 0;
	position: relative;
	text-align: center
}

#messages ul {
	list-style-type: none;
	padding: 0;
	margin: 0 auto;
	overflow: hidden;
	-webkit-transition: all .3s ease-in-out;
	-moz-transition: all .3s ease-in-out;
	-ms-transition: all .3s ease-in-out;
	-o-transition: all .3s ease-in-out;
	transition: all .3s ease-in-out;
	position: relative;
	max-height: 0;
	background: #fff
}

#messages li {
	margin: 6px;
	line-height: 1.25em;
	text-align: left;
	font-family: Verdana, Arial, sans-serif;
	font-size: 11px
}

#messages li:last-child {
	margin-bottom: 10px
}

#messages.open ul {
	max-height: 500px
}

#messages .handler:hover {
	color: #f93
}

#messages.hashandler .open-handler, #messages.open .close-handler {
	display: block
}

#messages.open .open-handler, #oas_SystemMessage {
	display: none
}

#messages .close-handler:after, #messages .close-handler:before,
	#messages .open-handler:after, #messages .open-handler:before {
	content: ' ';
	position: absolute;
	bottom: 0;
	top: -2px;
	width: 14px
}

#messages .close-handler:before, #messages .open-handler:before {
	left: 10px
}

#messages .close-handler:after, #messages .open-handler:after {
	right: 10px
}

.invisible {
	position: absolute;
	top: -100px;
	left: -2000px
}

.mm-menu, .mm-panels, .mm-panels>.mm-panel {
	margin: 0;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 0;
	box-sizing: border-box
}

#messages li.error {
	color: #000
}

#messages .error-message, #messages .maint-message, #messages .warning-message
	{
	padding-left: 45px;
	min-height: 37px
}

#messages .error-message:before, #messages .maint-message:before,
	#messages .warning-message:before {
	display: inline-block;
	height: 100%;
	min-height: 37px;
	content: '';
	vertical-align: middle
}

#messages .error-message>p, #messages .maint-message>p, #messages .warning-message>p
	{
	display: inline-block;
	vertical-align: middle;
	margin: 0;
	padding: 0;
	border: 0
}

#messages .error-message {
	background: url(/media_stat/images/locationbar/alert_icon_02.png) 10px
		center no-repeat
}

#messages .error-message-text {
	color: #c00;
	font-weight: 700
}

#messages .warning-message {
	background: url(/media_stat/images/locationbar/alert_icon_01.png) 10px
		center no-repeat
}

#messages .maint-message {
	background: url(/media_stat/images/locationbar/alert_icon_maint.png)
		10px center no-repeat
}

a.headerlink:focus, button.action.cssbutton.transparent.white.icon-pencil-before:focus,
	button.action.cssbutton.transparent.white.icon-trash-new-before:focus,
	input#MP_button:focus, input#PP_button:focus {
	outline: #5fb069 solid 2px !important
}

.icon-image a:focus, .mm-keyboardfocus a:focus {
	outline: 0
}

input#signinbtn:focus, input#signupbtn:focus {
	background-color: #5fb067 !important;
	border-color: #306238 !important;
	box-shadow: 0 0 1px #aaa, 0 0 8px #aaa
}

.mm-hidden {
	display: none !important
}

.mm-menu, .mm-panels>.mm-panel:not (.mm-hidden ){
	display: block
}

.mm-wrapper {
	overflow-x: hidden;
	position: relative
}

.mm-btn, .mm-menu, .mm-navbar, .mm-next:after, .mm-panels, .mm-panels>.mm-panel,
	.mm-prev:before {
	position: absolute;
	top: 0
}

.mm-menu {
	padding: 0
}

.mm-panels, .mm-panels>.mm-panel {
	background: inherit;
	border-color: inherit
}

.mm-btn, .mm-panel.mm-highest {
	z-index: 1
}

.mm-panels {
	overflow: hidden
}

.mm-panel {
	-webkit-transform: translate(100%, 0);
	-ms-transform: translate(100%, 0);
	transform: translate(100%, 0);
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0);
	-webkit-transition: -webkit-transform .4s ease;
	transition: -webkit-transform .4s ease;
	transition: transform .4s ease;
	transition: transform .4s ease, -webkit-transform .4s ease;
	-webkit-transform-origin: top left;
	-ms-transform-origin: top left;
	transform-origin: top left
}

.mm-panel.mm-opened {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-panel.mm-subopened {
	-webkit-transform: translate(-30%, 0);
	-ms-transform: translate(-30%, 0);
	transform: translate(-30%, 0);
	-webkit-transform: translate3d(-30%, 0, 0);
	transform: translate3d(-30%, 0, 0)
}

.mm-panel.mm-noanimation {
	-webkit-transition: none !important;
	transition: none !important
}

.mm-menu.mm-fx-menu-zoom, .mm-slideout {
	-webkit-transition: -webkit-transform .4s ease
}

.mm-panel.mm-noanimation.mm-subopened {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-panels>.mm-panel {
	-webkit-overflow-scrolling: touch;
	overflow: scroll;
	overflow-x: hidden;
	overflow-y: auto;
	padding: 0 20px
}

.mm-panels>.mm-panel:after, .mm-panels>.mm-panel:before {
	content: '';
	display: block;
	height: 20px
}

.mm-vertical .mm-panel {
	-webkit-transform: none !important;
	-ms-transform: none !important;
	transform: none !important
}

.mm-listview .mm-vertical .mm-panel, .mm-vertical .mm-listview .mm-panel
	{
	display: none;
	padding: 10px 0 10px 10px
}

.mm-listview .mm-vertical .mm-panel .mm-listview>li:last-child:after,
	.mm-vertical .mm-listview .mm-panel .mm-listview>li:last-child:after {
	border-color: transparent
}

.mm-vertical li.mm-opened>.mm-panel, li.mm-vertical.mm-opened>.mm-panel
	{
	display: block
}

.mm-listview>li.mm-vertical>.mm-next, .mm-vertical .mm-listview>li>.mm-next
	{
	box-sizing: border-box;
	height: 40px;
	bottom: auto
}

.mm-listview>li.mm-vertical.mm-opened>.mm-next:after, .mm-vertical .mm-listview>li.mm-opened>.mm-next:after
	{
	-webkit-transform: rotate(225deg);
	-ms-transform: rotate(225deg);
	transform: rotate(225deg);
	right: 19px
}

.mm-btn {
	box-sizing: border-box;
	width: 40px;
	height: 40px
}

.mm-clear:after, .mm-clear:before, .mm-close:after, .mm-close:before {
	content: '';
	border: 2px solid transparent;
	display: block;
	width: 5px;
	height: 5px;
	margin: auto;
	position: absolute;
	top: 0;
	bottom: 0;
	-webkit-transform: rotate(-45deg);
	-ms-transform: rotate(-45deg);
	transform: rotate(-45deg)
}

.mm-clear:before, .mm-close:before {
	border-right: none;
	border-bottom: none;
	right: 18px
}

.mm-clear:after, .mm-close:after {
	border-left: none;
	border-top: none;
	right: 25px
}

.mm-next:after, .mm-prev:before {
	content: '';
	border-top: 2px solid transparent;
	border-left: 2px solid transparent;
	display: block;
	width: 8px;
	height: 8px;
	margin: auto;
	bottom: 0
}

.mm-prev:before {
	-webkit-transform: rotate(-45deg);
	-ms-transform: rotate(-45deg);
	transform: rotate(-45deg);
	left: 23px;
	right: auto
}

.mm-next:after {
	-webkit-transform: rotate(135deg);
	-ms-transform: rotate(135deg);
	transform: rotate(135deg);
	right: 23px;
	left: auto
}

.mm-navbar {
	border-bottom: 1px solid;
	border-color: inherit;
	text-align: center;
	line-height: 20px;
	padding: 0 40px;
	margin: 0;
	left: 0;
	right: 0
}

.mm-navbar>* {
	display: block;
	padding: 10px 0
}

.mm-navbar .mm-btn:first-child {
	left: 0
}

.mm-navbar .mm-btn:last-child {
	text-align: right;
	right: 0
}

.mm-panel .mm-navbar {
	display: none
}

.mm-panel.mm-hasnavbar .mm-navbar {
	display: block
}

.mm-listview, .mm-listview>li {
	list-style: none;
	display: block;
	padding: 0;
	margin: 0
}

.mm-listview {
	font: inherit;
	font-size: 14px;
	line-height: 20px
}

.mm-listview>li {
	position: relative
}

.mm-listview>li, .mm-listview>li .mm-next, .mm-listview>li .mm-next:before,
	.mm-listview>li:after {
	border-color: inherit
}

.mm-listview>li>a, .mm-listview>li>span {
	color: inherit;
	display: block;
	padding: 10px 10px 10px 20px;
	margin: 0
}

.mm-menu, .mm-menu .mm-search input {
	color: rgba(0, 0, 0, .75)
}

.mm-listview>li:not (.mm-divider ):after {
	content: '';
	border-bottom-width: 1px;
	border-bottom-style: solid;
	display: block;
	position: absolute;
	right: 0;
	bottom: 0;
	left: 20px
}

.mm-listview .mm-next {
	background: rgba(3, 2, 1, 0);
	width: 50px;
	padding: 0;
	position: absolute;
	right: 0;
	top: 0;
	bottom: 0;
	z-index: 2
}

.mm-listview .mm-next:before {
	content: '';
	border-left-width: 1px;
	border-left-style: solid;
	display: block;
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0
}

.mm-listview .mm-next+a, .mm-listview .mm-next+span {
	margin-right: 50px
}

.mm-listview .mm-next.mm-fullsubopen {
	width: 100%
}

.mm-listview .mm-next.mm-fullsubopen:before {
	border-left: none
}

.mm-menu, .mm-menu .mm-listview {
	border-color: rgba(0, 0, 0, .1)
}

.mm-listview .mm-next.mm-fullsubopen+a, .mm-listview .mm-next.mm-fullsubopen+span
	{
	padding-right: 50px;
	margin-right: 0
}

.mm-panels>.mm-panel>.mm-listview {
	margin: 20px -20px
}

.mm-panels>.mm-panel>.mm-listview:first-child, .mm-panels>.mm-panel>.mm-navbar+.mm-listview
	{
	margin-top: -20px
}

.mm-menu {
	background: #f3f3f3
}

.mm-menu .mm-listview>li .mm-next:after {
	border-color: rgba(0, 0, 0, .3)
}

.mm-menu .mm-listview>li a:not (.mm-next ){
	-webkit-tap-highlight-color: rgba(255, 255, 255, .5);
	tap-highlight-color: rgba(255, 255, 255, .5)
}

.mm-menu .mm-listview>li.mm-selected>a:not (.mm-next ), .mm-menu .mm-listview>li.mm-selected>span
	{
	background: rgba(255, 255, 255, .5)
}

.mm-menu .mm-divider, .mm-menu .mm-listview>li.mm-opened.mm-vertical>.mm-panel,
	.mm-menu .mm-listview>li.mm-opened.mm-vertical>a.mm-next, .mm-menu.mm-vertical .mm-listview>li.mm-opened>.mm-panel,
	.mm-menu.mm-vertical .mm-listview>li.mm-opened>a.mm-next {
	background: rgba(0, 0, 0, .05)
}

.mm-page {
	box-sizing: border-box;
	position: relative
}

.mm-slideout {
	transition: -webkit-transform .4s ease;
	transition: transform .4s ease;
	transition: transform .4s ease, -webkit-transform .4s ease;
	z-index: 1
}

html.mm-opened {
	overflow-x: hidden;
	position: relative
}

html.mm-blocking, html.mm-blocking body {
	overflow: hidden
}

html.mm-background .mm-page {
	background: inherit
}

#mm-blocker {
	background: rgba(3, 2, 1, 0);
	display: none;
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 2
}

html.mm-blocking #mm-blocker {
	display: block
}

.mm-menu.mm-offcanvas {
	z-index: 0;
	display: none;
	position: fixed;
	width: 80%;
	min-width: 140px;
	max-width: 440px
}

.mm-menu.mm-iconbar, .mm-menu.mm-offcanvas.mm-opened {
	display: block
}

.mm-menu.mm-offcanvas.mm-no-csstransforms.mm-opened {
	z-index: 10
}

html.mm-opening .mm-menu.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:550px) {
	html.mm-opening .mm-menu.mm-opened ~.mm-slideout {
		-webkit-transform: translate(440px, 0);
		-ms-transform: translate(440px, 0);
		transform: translate(440px, 0);
		-webkit-transform: translate3d(440px, 0, 0);
		transform: translate3d(440px, 0, 0)
	}
}

.mm-sronly {
	border: 0 !important;
	clip: rect(1px, 1px, 1px, 1px) !important;
	-webkit-clip-path: inset(50%) !important;
	clip-path: inset(50%) !important;
	white-space: nowrap !important;
	width: 1px !important;
	height: 1px !important;
	padding: 0 !important;
	overflow: hidden !important;
	position: absolute !important
}

body, html.mm-iconbar body {
	overflow-x: hidden
}

.mm-menu .mm-listview.mm-border-none>li:after, .mm-menu .mm-listview>li.mm-border-none:after,
	.mm-menu.mm-border-none .mm-listview>li:after {
	content: none
}

.mm-menu .mm-listview.mm-border-full>li:after, .mm-menu .mm-listview>li.mm-border-full:after,
	.mm-menu.mm-border-full .mm-listview>li:after {
	left: 0 !important
}

.mm-menu .mm-listview.mm-border-offset>li:after, .mm-menu .mm-listview>li.mm-border-offset:after,
	.mm-menu.mm-border-offset .mm-listview>li:after {
	right: 20px
}

.mm-menu.mm-fx-menu-zoom {
	transition: -webkit-transform .4s ease;
	transition: transform .4s ease;
	transition: transform .4s ease, -webkit-transform .4s ease
}

html.mm-opened .mm-menu.mm-fx-menu-zoom {
	-webkit-transform: scale(.7, .7) translate3d(-30%, 0, 0);
	transform: scale(.7, .7) translate3d(-30%, 0, 0);
	-webkit-transform-origin: left center;
	-ms-transform-origin: left center;
	transform-origin: left center
}

html.mm-opening .mm-menu.mm-fx-menu-zoom {
	-webkit-transform: scale(1, 1) translate3d(0, 0, 0);
	transform: scale(1, 1) translate3d(0, 0, 0)
}

html.mm-right.mm-opened .mm-menu.mm-fx-menu-zoom {
	-webkit-transform: scale(.7, .7) translate3d(30%, 0, 0);
	transform: scale(.7, .7) translate3d(30%, 0, 0);
	-webkit-transform-origin: right center;
	-ms-transform-origin: right center;
	transform-origin: right center
}

html.mm-right.mm-opening .mm-menu.mm-fx-menu-zoom {
	-webkit-transform: scale(1, 1) translate3d(0, 0, 0);
	transform: scale(1, 1) translate3d(0, 0, 0)
}

.mm-menu.mm-fx-menu-slide {
	-webkit-transition: -webkit-transform .4s ease;
	transition: -webkit-transform .4s ease;
	transition: transform .4s ease;
	transition: transform .4s ease, -webkit-transform .4s ease
}

html.mm-opened .mm-menu.mm-fx-menu-slide {
	-webkit-transform: translate(-30%, 0);
	-ms-transform: translate(-30%, 0);
	transform: translate(-30%, 0);
	-webkit-transform: translate3d(-30%, 0, 0);
	transform: translate3d(-30%, 0, 0)
}

html.mm-opening .mm-menu.mm-fx-menu-slide {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

html.mm-right.mm-opened .mm-menu.mm-fx-menu-slide {
	-webkit-transform: translate(30%, 0);
	-ms-transform: translate(30%, 0);
	transform: translate(30%, 0);
	-webkit-transform: translate3d(30%, 0, 0);
	transform: translate3d(30%, 0, 0)
}

html.mm-right.mm-opening .mm-menu.mm-fx-menu-slide {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu.mm-fx-menu-fade {
	opacity: 0;
	-webkit-transition: opacity .4s ease;
	transition: opacity .4s ease
}

html.mm-opening .mm-menu.mm-fx-menu-fade {
	opacity: 1
}

.mm-menu .mm-fx-panels-none.mm-panel, .mm-menu.mm-fx-panels-none .mm-panel
	{
	-webkit-transition-property: none;
	transition-property: none
}

.mm-menu .mm-fx-panels-none.mm-panel.mm-subopened, .mm-menu.mm-fx-panels-none .mm-panel.mm-subopened
	{
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu .mm-fx-panels-zoom.mm-panel, .mm-menu.mm-fx-panels-zoom .mm-panel
	{
	-webkit-transform-origin: left center;
	-ms-transform-origin: left center;
	transform-origin: left center;
	-webkit-transform: scale(1.5, 1.5) translate3d(100%, 0, 0);
	transform: scale(1.5, 1.5) translate3d(100%, 0, 0)
}

.mm-menu .mm-fx-panels-zoom.mm-panel.mm-opened, .mm-menu.mm-fx-panels-zoom .mm-panel.mm-opened
	{
	-webkit-transform: scale(1, 1) translate3d(0, 0, 0);
	transform: scale(1, 1) translate3d(0, 0, 0)
}

.mm-menu .mm-fx-panels-zoom.mm-panel.mm-subopened, .mm-menu.mm-fx-panels-zoom .mm-panel.mm-subopened
	{
	-webkit-transform: scale(.7, .7) translate3d(-30%, 0, 0);
	transform: scale(.7, .7) translate3d(-30%, 0, 0)
}

.mm-menu .mm-fx-panels-slide-0.mm-panel.mm-subopened, .mm-menu.mm-fx-panels-slide-0 .mm-panel.mm-subopened
	{
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu .mm-fx-panels-slide-100.mm-panel.mm-subopened, .mm-menu.mm-fx-panels-slide-100 .mm-panel.mm-subopened
	{
	-webkit-transform: translate(-100%, 0);
	-ms-transform: translate(-100%, 0);
	transform: translate(-100%, 0);
	-webkit-transform: translate3d(-100%, 0, 0);
	transform: translate3d(-100%, 0, 0)
}

.mm-menu .mm-fx-panels-slide-up.mm-panel, .mm-menu.mm-fx-panels-slide-up .mm-panel
	{
	-webkit-transform: translate(0, 100%);
	-ms-transform: translate(0, 100%);
	transform: translate(0, 100%);
	-webkit-transform: translate3d(0, 100%, 0);
	transform: translate3d(0, 100%, 0)
}

.mm-menu .mm-fx-panels-slide-up.mm-panel.mm-opened, .mm-menu .mm-fx-panels-slide-up.mm-panel.mm-subopened,
	.mm-menu.mm-fx-panels-slide-up .mm-panel.mm-opened, .mm-menu.mm-fx-panels-slide-up .mm-panel.mm-subopened
	{
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li {
	-webkit-transition: none .4s ease;
	transition: none .4s ease
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(1) {
	-webkit-transition-delay: 50ms;
	transition-delay: 50ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(2) {
	-webkit-transition-delay: .1s;
	transition-delay: .1s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(3) {
	-webkit-transition-delay: 150ms;
	transition-delay: 150ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(4) {
	-webkit-transition-delay: .2s;
	transition-delay: .2s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(5) {
	-webkit-transition-delay: 250ms;
	transition-delay: 250ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(6) {
	-webkit-transition-delay: .3s;
	transition-delay: .3s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(7) {
	-webkit-transition-delay: 350ms;
	transition-delay: 350ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(8) {
	-webkit-transition-delay: .4s;
	transition-delay: .4s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(9) {
	-webkit-transition-delay: 450ms;
	transition-delay: 450ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(10) {
	-webkit-transition-delay: .5s;
	transition-delay: .5s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(11) {
	-webkit-transition-delay: 550ms;
	transition-delay: 550ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(12) {
	-webkit-transition-delay: .6s;
	transition-delay: .6s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(13) {
	-webkit-transition-delay: 650ms;
	transition-delay: 650ms
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(14) {
	-webkit-transition-delay: .7s;
	transition-delay: .7s
}

.mm-menu[class*=mm-fx-listitems-] .mm-listview>li:nth-child(15) {
	-webkit-transition-delay: 750ms;
	transition-delay: 750ms
}

.mm-menu.mm-fx-listitems-slide .mm-listview>li {
	-webkit-transition-property: opacity, -webkit-transform;
	transition-property: opacity, -webkit-transform;
	transition-property: transform, opacity;
	transition-property: transform, opacity, -webkit-transform;
	-webkit-transform: translate(50%, 0);
	-ms-transform: translate(50%, 0);
	transform: translate(50%, 0);
	-webkit-transform: translate3d(50%, 0, 0);
	transform: translate3d(50%, 0, 0);
	opacity: 0
}

html.mm-opening .mm-menu.mm-fx-listitems-slide .mm-panel.mm-opened .mm-listview>li
	{
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0);
	opacity: 1
}

.mm-menu.mm-fx-listitems-fade .mm-listview>li {
	-webkit-transition-property: opacity;
	transition-property: opacity;
	opacity: 0
}

html.mm-opening .mm-menu.mm-fx-listitems-fade .mm-panel.mm-opened .mm-listview>li
	{
	opacity: 1
}

.mm-menu.mm-fx-listitems-drop .mm-listview>li {
	-webkit-transition-property: opacity, top;
	transition-property: opacity, top;
	opacity: 0;
	top: -25%
}

html.mm-opening .mm-menu.mm-fx-listitems-drop .mm-panel.mm-opened .mm-listview>li
	{
	opacity: 1;
	top: 0
}

html.mm-iconbar .mm-page {
	background: inherit;
	min-height: 100vh
}

html.mm-iconbar .mm-slideout {
	box-sizing: border-box;
	padding-right: 60px;
	-webkit-transform: translate(60px, 0);
	-ms-transform: translate(60px, 0);
	transform: translate(60px, 0);
	-webkit-transform: translate3d(60px, 0, 0);
	transform: translate3d(60px, 0, 0)
}

.mm-menu.mm-offcanvas.mm-fullscreen {
	width: 100%;
	min-width: 140px;
	max-width: 10000px
}

html.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout {
	-webkit-transform: translate(100%, 0);
	-ms-transform: translate(100%, 0);
	transform: translate(100%, 0);
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0)
}

@media all and (max-width:140px) {
	html.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:10000px) {
	html.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout {
		-webkit-transform: translate(10000px, 0);
		-ms-transform: translate(10000px, 0);
		transform: translate(10000px, 0);
		-webkit-transform: translate3d(10000px, 0, 0);
		transform: translate3d(10000px, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout
	{
	-webkit-transform: translate(-100%, 0);
	-ms-transform: translate(-100%, 0);
	transform: translate(-100%, 0);
	-webkit-transform: translate3d(-100%, 0, 0);
	transform: translate3d(-100%, 0, 0)
}

@media all and (max-width:140px) {
	html.mm-right.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout
		{
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:10000px) {
	html.mm-right.mm-opening .mm-menu.mm-fullscreen.mm-opened ~.mm-slideout
		{
		-webkit-transform: translate(-10000px, 0);
		-ms-transform: translate(-10000px, 0);
		transform: translate(-10000px, 0);
		-webkit-transform: translate3d(-10000px, 0, 0);
		transform: translate3d(-10000px, 0, 0)
	}
}

.mm-menu.mm-offcanvas.mm-fullscreen.mm-bottom, .mm-menu.mm-offcanvas.mm-fullscreen.mm-top
	{
	height: 100%;
	min-height: 140px;
	max-height: 10000px
}

.mm-menu .mm-panels>.mm-panel.mm-listview-justify:after, .mm-menu .mm-panels>.mm-panel.mm-listview-justify:before,
	.mm-menu.mm-listview-justify .mm-panels>.mm-panel:after, .mm-menu.mm-listview-justify .mm-panels>.mm-panel:before
	{
	content: none;
	display: none
}

.mm-menu .mm-panels>.mm-panel.mm-listview-justify>.mm-listview, .mm-menu.mm-listview-justify .mm-panels>.mm-panel>.mm-listview
	{
	display: -webkit-box;
	display: -webkit-flex;
	display: -ms-flexbox;
	display: flex;
	-webkit-box-orient: vertical;
	-webkit-box-direction: normal;
	-webkit-flex-direction: column;
	-ms-flex-direction: column;
	flex-direction: column;
	height: 100%;
	margin-top: 0;
	margin-bottom: 0
}

.mm-menu .mm-panels>.mm-panel.mm-listview-justify>.mm-listview>li,
	.mm-menu.mm-listview-justify .mm-panels>.mm-panel>.mm-listview>li {
	-webkit-box-flex: 1;
	-webkit-flex: 1 0 auto;
	-ms-flex: 1 0 auto;
	flex: 1 0 auto;
	min-height: 40px
}

.mm-menu .mm-panels>.mm-panel.mm-listview-justify>.mm-listview>li:not (.mm-divider
	), .mm-menu.mm-listview-justify .mm-panels>.mm-panel>.mm-listview>li:not
	(.mm-divider ){
	display: -webkit-box;
	display: -webkit-flex;
	display: -ms-flexbox;
	display: flex;
	-webkit-box-orient: vertical;
	-webkit-box-direction: normal;
	-webkit-flex-direction: column;
	-ms-flex-direction: column;
	flex-direction: column
}

.mm-menu .mm-panels>.mm-panel.mm-listview-justify>.mm-listview>li>a:not
	(.mm-next ), .mm-menu .mm-panels>.mm-panel.mm-listview-justify>.mm-listview>li>span,
	.mm-menu.mm-listview-justify .mm-panels>.mm-panel>.mm-listview>li>a:not
	(.mm-next ), .mm-menu.mm-listview-justify .mm-panels>.mm-panel>.mm-listview>li>span
	{
	box-sizing: border-box;
	-webkit-box-flex: 1;
	-webkit-flex: 1 0 auto;
	-ms-flex: 1 0 auto;
	flex: 1 0 auto;
	display: -webkit-box;
	display: -webkit-flex;
	display: -ms-flexbox;
	display: flex;
	-webkit-box-align: center;
	-webkit-align-items: center;
	-ms-flex-align: center;
	align-items: center
}

.mm-listview-small .mm-listview>li>a:not (.mm-next ), .mm-listview-small .mm-listview>li>span
	{
	padding: 7px 10px 7px 20px
}

.mm-listview-small .mm-listview>li.mm-vertical>.mm-next,
	.mm-listview-small .mm-vertical>.mm-listview>li>.mm-next {
	height: 34px
}

.mm-listview-large .mm-listview>li>a:not (.mm-next ), .mm-listview-large .mm-listview>li>span
	{
	padding: 15px 10px 15px 20px
}

.mm-listview-large .mm-listview>li.mm-vertical>.mm-next,
	.mm-listview-large .mm-vertical>.mm-listview>li>.mm-next {
	height: 50px
}

.mm-listview-huge .mm-listview>li>a:not (.mm-next ), .mm-listview-huge .mm-listview>li>span
	{
	padding: 20px 10px 20px 20px
}

.mm-listview-huge .mm-listview>li.mm-vertical>.mm-next,
	.mm-listview-huge .mm-vertical>.mm-listview>li>.mm-next {
	height: 60px
}

.mm-listview .mm-divider {
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;
	font-size: 10px;
	text-indent: 20px;
	line-height: 25px
}

.mm-listview .mm-spacer {
	padding-top: 40px
}

.mm-listview .mm-spacer>.mm-next {
	top: 40px
}

.mm-listview .mm-spacer.mm-divider {
	padding-top: 25px
}

.mm-listview .mm-inset {
	list-style: disc inside;
	padding: 0 10px 15px 40px;
	margin: 0
}

.mm-listview .mm-inset>li {
	padding: 5px 0
}

.mm-menu .mm-listview.mm-multiline>li>a, .mm-menu .mm-listview.mm-multiline>li>span,
	.mm-menu .mm-listview>li.mm-multiline>a, .mm-menu .mm-listview>li.mm-multiline>span,
	.mm-menu.mm-multiline .mm-listview>li>a, .mm-menu.mm-multiline .mm-listview>li>span
	{
	text-overflow: clip;
	white-space: normal
}

.mm-divider>span, .mm-navbar .mm-breadcrumbs {
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden
}

.mm-menu.mm-opened[class*=mm-pagedim] ~#mm-blocker {
	opacity: 0
}

html.mm-opening .mm-menu.mm-opened[class*=mm-pagedim] ~#mm-blocker {
	opacity: .3;
	-webkit-transition: opacity .4s ease .4s;
	transition: opacity .4s ease .4s
}

.mm-menu.mm-opened.mm-pagedim ~#mm-blocker {
	background: inherit
}

.mm-menu.mm-opened.mm-pagedim-white ~#mm-blocker {
	background: #fff
}

.mm-menu.mm-opened.mm-pagedim-black ~#mm-blocker {
	background: #000
}

.mm-menu.mm-popup {
	-webkit-transition: opacity .4s ease;
	transition: opacity .4s ease;
	opacity: 0;
	box-shadow: 0 2px 10px rgba(0, 0, 0, .3);
	height: 80%;
	min-height: 140px;
	max-height: 880px;
	top: 50%;
	left: 50%;
	bottom: auto;
	right: auto;
	z-index: 2;
	-webkit-transform: translate(-50%, -50%);
	-ms-transform: translate(-50%, -50%);
	transform: translate(-50%, -50%);
	-webkit-transform: translate3d(-50%, -50%, 0);
	transform: translate3d(-50%, -50%, 0)
}

.mm-menu.mm-popup.mm-opened ~.mm-slideout {
	-webkit-transform: none !important;
	-ms-transform: none !important;
	transform: none !important;
	z-index: 0
}

.mm-menu.mm-popup.mm-opened ~#mm-blocker {
	-webkit-transition-delay: 0s !important;
	transition-delay: 0s !important;
	z-index: 1
}

html.mm-opening .mm-menu.mm-popup {
	opacity: 1
}

.mm-menu.mm-offcanvas.mm-right {
	left: auto
}

html.mm-right.mm-opening .mm-menu.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:550px) {
	html.mm-right.mm-opening .mm-menu.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-440px, 0);
		-ms-transform: translate(-440px, 0);
		transform: translate(-440px, 0);
		-webkit-transform: translate3d(-440px, 0, 0);
		transform: translate3d(-440px, 0, 0)
	}
}

html.mm-front .mm-slideout, html.mm-widescreen .mm-slideout {
	-webkit-transform: none !important;
	-ms-transform: none !important
}

html.mm-front .mm-slideout {
	transform: none !important;
	z-index: 0
}

html.mm-front #mm-blocker {
	z-index: 1
}

html.mm-front .mm-menu.mm-offcanvas {
	z-index: 2
}

.mm-menu.mm-offcanvas.mm-front, .mm-menu.mm-offcanvas.mm-next {
	-webkit-transition: -webkit-transform .4s ease;
	transition: -webkit-transform .4s ease;
	transition: transform .4s ease;
	transition: transform .4s ease, -webkit-transform .4s ease;
	-webkit-transform: translate(-100%, 0);
	-ms-transform: translate(-100%, 0);
	transform: translate(-100%, 0);
	-webkit-transform: translate3d(-100%, 0, 0);
	transform: translate3d(-100%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-front.mm-right, .mm-menu.mm-offcanvas.mm-next.mm-right
	{
	-webkit-transform: translate(100%, 0);
	-ms-transform: translate(100%, 0);
	transform: translate(100%, 0);
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-top {
	-webkit-transform: translate(0, -100%);
	-ms-transform: translate(0, -100%);
	transform: translate(0, -100%);
	-webkit-transform: translate3d(0, -100%, 0);
	transform: translate3d(0, -100%, 0)
}

.mm-menu.mm-offcanvas.mm-bottom {
	-webkit-transform: translate(0, 100%);
	-ms-transform: translate(0, 100%);
	transform: translate(0, 100%);
	-webkit-transform: translate3d(0, 100%, 0);
	transform: translate3d(0, 100%, 0);
	top: auto
}

.mm-menu.mm-offcanvas.mm-bottom, .mm-menu.mm-offcanvas.mm-top {
	width: 100%;
	min-width: 100%;
	max-width: 100%;
	height: 80%;
	min-height: 140px;
	max-height: 880px
}

html.mm-opening .mm-menu.mm-offcanvas.mm-front, html.mm-opening .mm-menu.mm-offcanvas.mm-next
	{
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu.mm-shadow-page:after {
	content: "";
	display: block;
	width: 20px;
	height: 120%;
	position: absolute;
	left: 100%;
	top: -10%;
	z-index: 100;
	-webkit-clip-path: polygon(-20px 0, 0 0, 0 100%, -20px 100%);
	clip-path: polygon(-20px 0, 0 0, 0 100%, -20px 100%)
}

.mm-menu.mm-shadow-page.mm-front:after, .mm-menu.mm-shadow-page.mm-next:after,
	.mm-menu.mm-shadow-page.mm-theme-black:after {
	content: none;
	display: none
}

.mm-menu.mm-shadow-page.mm-right:after {
	left: auto;
	right: 100%
}

.mm-menu.mm-shadow-page:after, .mm-menu.mm-shadow-panels .mm-panel.mm-opened:nth-child(n+2)
	{
	box-shadow: 0 0 10px rgba(0, 0, 0, .3)
}

.mm-menu.mm-theme-dark {
	background: #333;
	border-color: rgba(0, 0, 0, .15);
	color: rgba(255, 255, 255, .8)
}

.mm-menu.mm-theme-dark .mm-navbar a, .mm-menu.mm-theme-dark .mm-navbar>*,
	.mm-menu.mm-theme-dark em.mm-counter {
	color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-dark .mm-btn:after, .mm-menu.mm-theme-dark .mm-btn:before
	{
	border-color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-dark .mm-listview {
	border-color: rgba(0, 0, 0, .15)
}

.mm-menu.mm-theme-dark .mm-listview>li .mm-next:after {
	border-color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-dark .mm-listview>li a:not (.mm-next ){
	-webkit-tap-highlight-color: rgba(0, 0, 0, .1);
	tap-highlight-color: rgba(0, 0, 0, .1)
}

.mm-menu.mm-theme-dark .mm-listview>li.mm-selected>a:not (.mm-next ),
	.mm-menu.mm-theme-dark .mm-listview>li.mm-selected>span {
	background: rgba(0, 0, 0, .1)
}

.mm-menu.mm-theme-dark .mm-divider, .mm-menu.mm-theme-dark .mm-fixeddivider span,
	.mm-menu.mm-theme-dark .mm-listview>li.mm-opened.mm-vertical>.mm-panel,
	.mm-menu.mm-theme-dark .mm-listview>li.mm-opened.mm-vertical>a.mm-next,
	.mm-menu.mm-theme-dark.mm-vertical .mm-listview>li.mm-opened>.mm-panel,
	.mm-menu.mm-theme-dark.mm-vertical .mm-listview>li.mm-opened>a.mm-next
	{
	background: rgba(255, 255, 255, .05)
}

.mm-menu.mm-theme-dark label.mm-check:before {
	border-color: rgba(255, 255, 255, .8)
}

.mm-menu.mm-shadow-page.mm-theme-dark:after, .mm-menu.mm-shadow-panels.mm-theme-dark .mm-panel.mm-opened:nth-child(n+2)
	{
	box-shadow: 0 0 20px rgba(0, 0, 0, .5)
}

.mm-menu.mm-theme-dark .mm-search input {
	background: rgba(255, 255, 255, .3);
	color: rgba(255, 255, 255, .8)
}

.mm-menu.mm-theme-dark .mm-indexer a, .mm-menu.mm-theme-dark .mm-noresultsmsg
	{
	color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-hoverselected.mm-theme-dark .mm-listview>li>a.mm-fullsubopen:hover+span,
	.mm-menu.mm-hoverselected.mm-theme-dark .mm-listview>li>a:not (.mm-fullsubopen
	):hover, .mm-menu.mm-parentselected.mm-theme-dark .mm-listview>li>a.mm-selected.mm-fullsubopen+a,
	.mm-menu.mm-parentselected.mm-theme-dark .mm-listview>li>a.mm-selected.mm-fullsubopen+span,
	.mm-menu.mm-parentselected.mm-theme-dark .mm-listview>li>a.mm-selected:not
	(.mm-fullsubopen ){
	background: rgba(0, 0, 0, .1)
}

.mm-menu.mm-theme-dark label.mm-toggle {
	background: rgba(0, 0, 0, .15)
}

.mm-menu.mm-theme-dark label.mm-toggle:before {
	background: #333
}

.mm-menu.mm-theme-dark input.mm-toggle:checked ~label.mm-toggle {
	background: #4bd963
}

.mm-menu.mm-theme-white {
	background: #fff;
	border-color: rgba(0, 0, 0, .1);
	color: rgba(0, 0, 0, .6)
}

.mm-menu.mm-hoverselected.mm-theme-white .mm-listview>li>a.mm-fullsubopen:hover+span,
	.mm-menu.mm-hoverselected.mm-theme-white .mm-listview>li>a:not (.mm-fullsubopen
	):hover, .mm-menu.mm-keyboardfocus a:focus, .mm-menu.mm-parentselected.mm-theme-white .mm-listview>li>a.mm-selected.mm-fullsubopen+a,
	.mm-menu.mm-parentselected.mm-theme-white .mm-listview>li>a.mm-selected.mm-fullsubopen+span,
	.mm-menu.mm-parentselected.mm-theme-white .mm-listview>li>a.mm-selected:not
	(.mm-fullsubopen ), .mm-menu.mm-theme-white .mm-divider, .mm-menu.mm-theme-white .mm-fixeddivider span,
	.mm-menu.mm-theme-white .mm-listview>li.mm-opened.mm-vertical>.mm-panel,
	.mm-menu.mm-theme-white .mm-listview>li.mm-opened.mm-vertical>a.mm-next,
	.mm-menu.mm-theme-white .mm-listview>li.mm-selected>a:not (.mm-next ),
	.mm-menu.mm-theme-white .mm-listview>li.mm-selected>span, .mm-menu.mm-theme-white.mm-vertical .mm-listview>li.mm-opened>.mm-panel,
	.mm-menu.mm-theme-white.mm-vertical .mm-listview>li.mm-opened>a.mm-next
	{
	background: rgba(0, 0, 0, .05)
}

.mm-menu.mm-theme-white .mm-navbar a, .mm-menu.mm-theme-white .mm-navbar>*,
	.mm-menu.mm-theme-white em.mm-counter {
	color: rgba(0, 0, 0, .3)
}

.mm-menu.mm-theme-white .mm-btn:after, .mm-menu.mm-theme-white .mm-btn:before
	{
	border-color: rgba(0, 0, 0, .3)
}

.mm-menu.mm-theme-white .mm-listview {
	border-color: rgba(0, 0, 0, .1)
}

.mm-menu.mm-theme-white .mm-listview>li .mm-next:after {
	border-color: rgba(0, 0, 0, .3)
}

.mm-menu.mm-theme-white .mm-listview>li a:not (.mm-next ){
	-webkit-tap-highlight-color: rgba(0, 0, 0, .05);
	tap-highlight-color: rgba(0, 0, 0, .05)
}

.mm-menu.mm-theme-white label.mm-check:before {
	border-color: rgba(0, 0, 0, .6)
}

.mm-menu.mm-shadow-page.mm-theme-white:after, .mm-menu.mm-shadow-panels.mm-theme-white .mm-panel.mm-opened:nth-child(n+2)
	{
	box-shadow: 0 0 10px rgba(0, 0, 0, .2)
}

.mm-menu.mm-theme-white .mm-search input {
	background: rgba(0, 0, 0, .05);
	color: rgba(0, 0, 0, .6)
}

.mm-menu.mm-theme-white .mm-indexer a, .mm-menu.mm-theme-white .mm-noresultsmsg
	{
	color: rgba(0, 0, 0, .3)
}

.mm-menu.mm-theme-white label.mm-toggle {
	background: rgba(0, 0, 0, .1)
}

.mm-menu.mm-theme-white label.mm-toggle:before {
	background: #fff
}

.mm-menu.mm-theme-white input.mm-toggle:checked ~label.mm-toggle {
	background: #4bd963
}

.mm-menu.mm-theme-black {
	background: #000;
	border-color: rgba(255, 255, 255, .2);
	color: rgba(255, 255, 255, .6)
}

.mm-menu.mm-theme-black .mm-navbar a, .mm-menu.mm-theme-black .mm-navbar>*,
	.mm-menu.mm-theme-black em.mm-counter {
	color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-black .mm-btn:after, .mm-menu.mm-theme-black .mm-btn:before
	{
	border-color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-black .mm-listview {
	border-color: rgba(255, 255, 255, .2)
}

.mm-menu.mm-theme-black .mm-listview>li .mm-next:after {
	border-color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-theme-black .mm-listview>li a:not (.mm-next ){
	-webkit-tap-highlight-color: rgba(255, 255, 255, .3);
	tap-highlight-color: rgba(255, 255, 255, .3)
}

.mm-menu.mm-theme-black .mm-listview>li.mm-selected>a:not (.mm-next ),
	.mm-menu.mm-theme-black .mm-listview>li.mm-selected>span {
	background: rgba(255, 255, 255, .3)
}

.mm-menu.mm-theme-black .mm-divider, .mm-menu.mm-theme-black .mm-fixeddivider span,
	.mm-menu.mm-theme-black .mm-listview>li.mm-opened.mm-vertical>.mm-panel,
	.mm-menu.mm-theme-black .mm-listview>li.mm-opened.mm-vertical>a.mm-next,
	.mm-menu.mm-theme-black.mm-vertical .mm-listview>li.mm-opened>.mm-panel,
	.mm-menu.mm-theme-black.mm-vertical .mm-listview>li.mm-opened>a.mm-next
	{
	background: rgba(255, 255, 255, .2)
}

.mm-menu.mm-theme-black label.mm-check:before {
	border-color: rgba(255, 255, 255, .6)
}

.mm-menu.mm-shadow-panels.mm-theme-black .mm-panel.mm-opened:nth-child(n+2)
	{
	box-shadow: false
}

.mm-menu.mm-theme-black .mm-search input {
	background: rgba(255, 255, 255, .3);
	color: rgba(255, 255, 255, .6)
}

.mm-menu.mm-theme-black .mm-indexer a, .mm-menu.mm-theme-black .mm-noresultsmsg
	{
	color: rgba(255, 255, 255, .4)
}

.mm-menu.mm-hoverselected.mm-theme-black .mm-listview>li>a.mm-fullsubopen:hover+span,
	.mm-menu.mm-hoverselected.mm-theme-black .mm-listview>li>a:not (.mm-fullsubopen
	):hover, .mm-menu.mm-parentselected.mm-theme-black .mm-listview>li>a.mm-selected.mm-fullsubopen+a,
	.mm-menu.mm-parentselected.mm-theme-black .mm-listview>li>a.mm-selected.mm-fullsubopen+span,
	.mm-menu.mm-parentselected.mm-theme-black .mm-listview>li>a.mm-selected:not
	(.mm-fullsubopen ){
	background: rgba(255, 255, 255, .3)
}

.mm-menu.mm-theme-black label.mm-toggle {
	background: rgba(255, 255, 255, .2)
}

.mm-menu.mm-theme-black label.mm-toggle:before {
	background: #000
}

.mm-menu.mm-theme-black input.mm-toggle:checked ~label.mm-toggle {
	background: #4bd963
}

.mm-menu .mm-tileview.mm-listview:after, .mm-menu.mm-tileview .mm-listview:after
	{
	content: '';
	display: block;
	clear: both
}

.mm-menu .mm-tileview.mm-listview>li>.mm-next:after, .mm-menu .mm-tileview.mm-listview>li>.mm-next:before,
	.mm-menu.mm-tileview .mm-listview>li>.mm-next:after, .mm-menu.mm-tileview .mm-listview>li>.mm-next:before,
	.mm-menu.mm-tileview .mm-panel:after {
	display: none;
	content: none
}

.mm-menu .mm-tileview.mm-listview>li, .mm-menu.mm-tileview .mm-listview>li
	{
	width: 50%;
	height: 0;
	padding: 50% 0 0;
	float: left;
	position: relative
}

.mm-menu .mm-tileview.mm-listview>li:after, .mm-menu.mm-tileview .mm-listview>li:after
	{
	left: 0;
	top: 0;
	border-right-width: 1px;
	border-right-style: solid;
	z-index: -1
}

.mm-menu .mm-tileview.mm-listview>li.mm-tile-xs, .mm-menu.mm-tileview .mm-listview>li.mm-tile-xs
	{
	width: 12.5%;
	padding-top: 12.5%
}

.mm-menu .mm-tileview.mm-listview>li.mm-tile-s, .mm-menu.mm-tileview .mm-listview>li.mm-tile-s
	{
	width: 25%;
	padding-top: 25%
}

.mm-menu .mm-tileview.mm-listview>li.mm-tile-l, .mm-menu.mm-tileview .mm-listview>li.mm-tile-l
	{
	width: 75%;
	padding-top: 75%
}

.mm-menu .mm-tileview.mm-listview>li.mm-tile-xl, .mm-menu.mm-tileview .mm-listview>li.mm-tile-xl
	{
	width: 100%;
	padding-top: 100%
}

.mm-menu .mm-tileview.mm-listview>li>a, .mm-menu .mm-tileview.mm-listview>li>span,
	.mm-menu.mm-tileview .mm-listview>li>a, .mm-menu.mm-tileview .mm-listview>li>span
	{
	line-height: 1px;
	text-align: center;
	padding: 50% 10px 0;
	margin: 0;
	position: absolute;
	top: 0;
	right: 1px;
	bottom: 1px;
	left: 0
}

.mm-menu.mm-autoheight:not (.mm-offcanvas ), html.mm-widescreen body {
	position: relative
}

.mm-menu .mm-tileview.mm-listview>li>.mm-next, .mm-menu.mm-tileview .mm-listview>li>.mm-next
	{
	width: auto
}

.mm-menu.mm-tileview .mm-panel {
	padding-left: 0;
	padding-right: 0
}

.mm-menu.mm-tileview .mm-listview {
	margin: 0
}

html.mm-widescreen #mm-blocker {
	display: none !important
}

html.mm-widescreen .mm-slideout {
	transform: none !important;
	width: 70% !important;
	margin-left: 30% !important
}

html.mm-widescreen .mm-page {
	background: inherit;
	box-sizing: border-box
}

html.mm-widescreen.mm-blocking, html.mm-widescreen.mm-blocking body {
	overflow: auto
}

.mm-menu.mm-widescreen {
	border-right-width: 1px;
	border-right-style: solid;
	display: block !important;
	width: 30% !important;
	min-width: 0 !important;
	max-width: none !important;
	top: 0 !important;
	right: auto !important;
	bottom: 0 !important;
	left: 0 !important;
	z-index: 100 !important;
	-webkit-transform: none !important;
	-ms-transform: none !important;
	transform: none !important
}

.mm-menu.mm-widescreen.mm-pageshadow:after {
	content: none;
	display: none
}

.mm-menu.mm-autoheight {
	-webkit-transition: none .4s ease;
	transition: none .4s ease;
	-webkit-transition-property: height, -webkit-transform;
	transition-property: height, -webkit-transform;
	transition-property: transform, height;
	transition-property: transform, height, -webkit-transform
}

.mm-menu.mm-measureheight .mm-panel.mm-vertical.mm-opened, .mm-menu.mm-measureheight .mm-panel:not
	(.mm-vertical ){
	display: block !important
}

.mm-menu.mm-measureheight .mm-panels>.mm-panel {
	bottom: auto !important;
	height: auto !important
}

.mm-columns {
	-webkit-transition-property: width;
	transition-property: width
}

.mm-columns .mm-panels>.mm-panel {
	right: auto;
	-webkit-transition-property: width, -webkit-transform;
	transition-property: width, -webkit-transform;
	transition-property: width, transform;
	transition-property: width, transform, -webkit-transform
}

.mm-columns .mm-panels>.mm-panel.mm-opened, .mm-columns .mm-panels>.mm-panel.mm-subopened
	{
	border-left: 1px solid;
	border-color: inherit;
	display: block !important
}

.mm-columns .mm-panels>.mm-columns-0 {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-columns-0 .mm-panels>.mm-panel {
	z-index: 0
}

.mm-columns-0 .mm-panels>.mm-panel else {
	width: 100%
}

.mm-columns-0 .mm-panels>.mm-panel:not (.mm-opened ):not (.mm-subopened
	){
	-webkit-transform: translate(100%, 0);
	-ms-transform: translate(100%, 0);
	transform: translate(100%, 0);
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-columns-0 {
	width: 80%;
	min-width: 140px;
	max-width: 0
}

html.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:0px) {
	html.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
		-webkit-transform: translate(0, 0);
		-ms-transform: translate(0, 0);
		transform: translate(0, 0);
		-webkit-transform: translate3d(0, 0, 0);
		transform: translate3d(0, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:0px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-0.mm-opened ~.mm-slideout {
		-webkit-transform: translate(0, 0);
		-ms-transform: translate(0, 0);
		transform: translate(0, 0);
		-webkit-transform: translate3d(0, 0, 0);
		transform: translate3d(0, 0, 0)
	}
}

.mm-columns .mm-panels>.mm-columns-1 {
	-webkit-transform: translate(100%, 0);
	-ms-transform: translate(100%, 0);
	transform: translate(100%, 0);
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0)
}

.mm-columns-1 .mm-panels>.mm-panel {
	z-index: 1;
	width: 100%
}

.mm-columns-1 .mm-panels>.mm-panel else {
	width: 100%
}

.mm-columns-1 .mm-panels>.mm-panel:not (.mm-opened ):not (.mm-subopened
	){
	-webkit-transform: translate(200%, 0);
	-ms-transform: translate(200%, 0);
	transform: translate(200%, 0);
	-webkit-transform: translate3d(200%, 0, 0);
	transform: translate3d(200%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-columns-1 {
	width: 80%;
	min-width: 140px;
	max-width: 440px
}

html.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:550px) {
	html.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
		-webkit-transform: translate(440px, 0);
		-ms-transform: translate(440px, 0);
		transform: translate(440px, 0);
		-webkit-transform: translate3d(440px, 0, 0);
		transform: translate3d(440px, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:550px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-1.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-440px, 0);
		-ms-transform: translate(-440px, 0);
		transform: translate(-440px, 0);
		-webkit-transform: translate3d(-440px, 0, 0);
		transform: translate3d(-440px, 0, 0)
	}
}

.mm-columns .mm-panels>.mm-columns-2 {
	-webkit-transform: translate(200%, 0);
	-ms-transform: translate(200%, 0);
	transform: translate(200%, 0);
	-webkit-transform: translate3d(200%, 0, 0);
	transform: translate3d(200%, 0, 0)
}

.mm-columns-2 .mm-panels>.mm-panel {
	z-index: 2;
	width: 50%
}

.mm-columns-2 .mm-panels>.mm-panel else {
	width: 100%
}

.mm-columns-2 .mm-panels>.mm-panel:not (.mm-opened ):not (.mm-subopened
	){
	-webkit-transform: translate(300%, 0);
	-ms-transform: translate(300%, 0);
	transform: translate(300%, 0);
	-webkit-transform: translate3d(300%, 0, 0);
	transform: translate3d(300%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-columns-2 {
	width: 80%;
	min-width: 140px;
	max-width: 880px
}

html.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:1100px) {
	html.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
		-webkit-transform: translate(880px, 0);
		-ms-transform: translate(880px, 0);
		transform: translate(880px, 0);
		-webkit-transform: translate3d(880px, 0, 0);
		transform: translate3d(880px, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:1100px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-2.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-880px, 0);
		-ms-transform: translate(-880px, 0);
		transform: translate(-880px, 0);
		-webkit-transform: translate3d(-880px, 0, 0);
		transform: translate3d(-880px, 0, 0)
	}
}

.mm-columns .mm-panels>.mm-columns-3 {
	-webkit-transform: translate(300%, 0);
	-ms-transform: translate(300%, 0);
	transform: translate(300%, 0);
	-webkit-transform: translate3d(300%, 0, 0);
	transform: translate3d(300%, 0, 0)
}

.mm-columns-3 .mm-panels>.mm-panel {
	z-index: 3;
	width: 33.34%
}

.mm-columns-3 .mm-panels>.mm-panel else {
	width: 100%
}

.mm-columns-3 .mm-panels>.mm-panel:not (.mm-opened ):not (.mm-subopened
	){
	-webkit-transform: translate(400%, 0);
	-ms-transform: translate(400%, 0);
	transform: translate(400%, 0);
	-webkit-transform: translate3d(400%, 0, 0);
	transform: translate3d(400%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-columns-3 {
	width: 80%;
	min-width: 140px;
	max-width: 1320px
}

html.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:1650px) {
	html.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
		-webkit-transform: translate(1320px, 0);
		-ms-transform: translate(1320px, 0);
		transform: translate(1320px, 0);
		-webkit-transform: translate3d(1320px, 0, 0);
		transform: translate3d(1320px, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:1650px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-3.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-1320px, 0);
		-ms-transform: translate(-1320px, 0);
		transform: translate(-1320px, 0);
		-webkit-transform: translate3d(-1320px, 0, 0);
		transform: translate3d(-1320px, 0, 0)
	}
}

.mm-columns .mm-panels>.mm-columns-4 {
	-webkit-transform: translate(400%, 0);
	-ms-transform: translate(400%, 0);
	transform: translate(400%, 0);
	-webkit-transform: translate3d(400%, 0, 0);
	transform: translate3d(400%, 0, 0)
}

.mm-columns-4 .mm-panels>.mm-panel {
	z-index: 4;
	width: 25%
}

.mm-columns-4 .mm-panels>.mm-panel else {
	width: 100%
}

.mm-columns-4 .mm-panels>.mm-panel:not (.mm-opened ):not (.mm-subopened
	){
	-webkit-transform: translate(500%, 0);
	-ms-transform: translate(500%, 0);
	transform: translate(500%, 0);
	-webkit-transform: translate3d(500%, 0, 0);
	transform: translate3d(500%, 0, 0)
}

.mm-menu.mm-offcanvas.mm-columns-4 {
	width: 80%;
	min-width: 140px;
	max-width: 1760px
}

html.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
	-webkit-transform: translate(80%, 0);
	-ms-transform: translate(80%, 0);
	transform: translate(80%, 0);
	-webkit-transform: translate3d(80%, 0, 0);
	transform: translate3d(80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
		-webkit-transform: translate(140px, 0);
		-ms-transform: translate(140px, 0);
		transform: translate(140px, 0);
		-webkit-transform: translate3d(140px, 0, 0);
		transform: translate3d(140px, 0, 0)
	}
}

@media all and (min-width:2200px) {
	html.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
		-webkit-transform: translate(1760px, 0);
		-ms-transform: translate(1760px, 0);
		transform: translate(1760px, 0);
		-webkit-transform: translate3d(1760px, 0, 0);
		transform: translate3d(1760px, 0, 0)
	}
}

html.mm-right.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
	-webkit-transform: translate(-80%, 0);
	-ms-transform: translate(-80%, 0);
	transform: translate(-80%, 0);
	-webkit-transform: translate3d(-80%, 0, 0);
	transform: translate3d(-80%, 0, 0)
}

@media all and (max-width:175px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-140px, 0);
		-ms-transform: translate(-140px, 0);
		transform: translate(-140px, 0);
		-webkit-transform: translate3d(-140px, 0, 0);
		transform: translate3d(-140px, 0, 0)
	}
}

@media all and (min-width:2200px) {
	html.mm-right.mm-opening .mm-menu.mm-columns-4.mm-opened ~.mm-slideout {
		-webkit-transform: translate(-1760px, 0);
		-ms-transform: translate(-1760px, 0);
		transform: translate(-1760px, 0);
		-webkit-transform: translate3d(-1760px, 0, 0);
		transform: translate3d(-1760px, 0, 0)
	}
}

.mm-columns.mm-offcanvas.mm-bottom, .mm-columns.mm-offcanvas.mm-top {
	width: 100%;
	max-width: 100%;
	min-width: 100%
}

html.mm-opening .mm-columns.mm-offcanvas.mm-front, html.mm-opening .mm-columns.mm-offcanvas.mm-next
	{
	-webkit-transition-property: width, min-width, max-width,
		-webkit-transform;
	transition-property: width, min-width, max-width, -webkit-transform;
	transition-property: width, min-width, max-width, transform;
	transition-property: width, min-width, max-width, transform,
		-webkit-transform
}

em.mm-counter {
	font: inherit;
	font-size: 14px;
	font-style: normal;
	text-indent: 0;
	line-height: 20px;
	display: block;
	margin-top: -10px;
	position: absolute;
	right: 45px;
	top: 50%
}

.mm-collapsed:not (.mm-uncollapsed ), .mm-nosubresults>.mm-counter {
	display: none
}

em.mm-counter+a.mm-next {
	width: 90px
}

em.mm-counter+a.mm-next+a, em.mm-counter+a.mm-next+span {
	margin-right: 90px
}

em.mm-counter+a.mm-fullsubopen {
	padding-left: 0
}

.mm-listview em.mm-counter+.mm-next.mm-fullsubopen+a, .mm-listview em.mm-counter+.mm-next.mm-fullsubopen+span,
	em.mm-counter+a.mm-fullsubopen+a, em.mm-counter+a.mm-fullsubopen+span {
	padding-right: 90px
}

.mm-vertical>.mm-counter {
	top: 12px;
	margin-top: 0
}

.mm-vertical.mm-spacer>.mm-counter {
	margin-top: 40px
}

.mm-menu em.mm-counter {
	color: rgba(0, 0, 0, .3)
}

.mm-divider>span {
	padding: 0;
	line-height: 25px
}

.mm-divider.mm-opened a.mm-next:after {
	-webkit-transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	transform: rotate(45deg)
}

.mm-fixeddivider {
	background: inherit;
	display: none;
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	z-index: 10;
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu .mm-fixeddivider span, .mm-menu .mm-search input {
	background: rgba(0, 0, 0, .05)
}

.mm-fixeddivider:after {
	content: none !important;
	display: none !important
}

.mm-hasdividers .mm-fixeddivider {
	display: block
}

html.mm-opened.mm-dragging .mm-menu, html.mm-opened.mm-dragging .mm-slideout
	{
	-webkit-transition-duration: 0s;
	transition-duration: 0s
}

.mm-menu.mm-dropdown {
	box-shadow: 0 2px 10px rgba(0, 0, 0, .3);
	height: 80%
}

html.mm-dropdown .mm-slideout {
	-webkit-transform: none !important;
	-ms-transform: none !important;
	transform: none !important;
	z-index: 0
}

html.mm-dropdown #mm-blocker {
	-webkit-transition-delay: 0s !important;
	transition-delay: 0s !important;
	z-index: 1
}

html.mm-dropdown .mm-menu {
	z-index: 2
}

html.mm-dropdown.mm-opened:not (.mm-opening ) .mm-menu {
	display: none
}

.mm-menu.mm-tip:before {
	content: '';
	background: inherit;
	box-shadow: 0 2px 10px rgba(0, 0, 0, .3);
	display: block;
	width: 15px;
	height: 15px;
	position: absolute;
	z-index: 0;
	-webkit-transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	transform: rotate(45deg)
}

.mm-menu.mm-tipleft:before {
	left: 22px
}

.mm-menu.mm-tipright:before {
	right: 22px
}

.mm-menu.mm-tiptop:before {
	top: -8px
}

.mm-menu.mm-tipbottom:before {
	bottom: -8px
}

.mm-iconpanel .mm-panels>.mm-panel {
	-webkit-transition-property: left, -webkit-transform;
	transition-property: left, -webkit-transform;
	transition-property: transform, left;
	transition-property: transform, left, -webkit-transform
}

.mm-iconpanel .mm-panels>.mm-panel.mm-opened, .mm-iconpanel .mm-panels>.mm-panel.mm-subopened
	{
	border-left: 1px solid;
	border-color: inherit;
	display: block !important
}

.mm-iconpanel .mm-panels>.mm-panel.mm-subopened {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-iconpanel .mm-panel.mm-iconpanel-0 {
	left: 0
}

.mm-iconpanel .mm-panel.mm-iconpanel-2 {
	left: 80px
}

.mm-iconpanel .mm-panel.mm-iconpanel-3 {
	left: 120px
}

.mm-iconpanel .mm-panel.mm-iconpanel-4 {
	left: 160px
}

.mm-iconpanel .mm-panel.mm-iconpanel-5 {
	left: 200px
}

.mm-iconpanel .mm-panel.mm-iconpanel-6 {
	left: 240px
}

.mm-subblocker {
	background: inherit;
	opacity: 0;
	display: block;
	max-height: 100%;
	position: absolute;
	top: 0;
	right: 0;
	left: 0;
	z-index: 3;
	-webkit-transition: opacity .4s ease;
	transition: opacity .4s ease
}

.mm-subopened .mm-subblocker {
	opacity: .6;
	bottom: -100000px
}

.mm-menu.mm-keyboardfocus a:focus {
	background: rgba(255, 255, 255, .5)
}

.mm-navbars-bottom, .mm-navbars-top {
	background: inherit;
	border-color: inherit;
	border-width: 0;
	overflow: hidden;
	position: absolute;
	left: 0;
	right: 0;
	z-index: 3
}

.mm-navbars-bottom>.mm-navbar, .mm-navbars-top>.mm-navbar {
	border: none;
	padding: 0;
	position: relative;
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-navbars-top {
	border-bottom-style: solid;
	border-bottom-width: 1px;
	top: 0
}

.mm-navbars-bottom {
	border-top-style: solid;
	border-top-width: 1px;
	bottom: 0
}

.mm-navbar.mm-hasbtns {
	padding: 0 40px
}

.mm-navbar[class*=mm-navbar-content-]>* {
	box-sizing: border-box;
	display: block;
	float: left
}

.mm-navbar .mm-breadcrumbs {
	-webkit-overflow-scrolling: touch;
	overflow-x: auto;
	text-align: left;
	padding: 0 0 0 17px
}

.mm-navbar .mm-breadcrumbs>* {
	display: inline-block;
	padding: 10px 3px
}

.mm-navbar .mm-breadcrumbs>a {
	text-decoration: underline
}

.mm-navbar.mm-hasbtns .mm-breadcrumbs {
	margin-left: -40px
}

.mm-navbar.mm-hasbtns .mm-btn:not (.mm-hidden )+.mm-breadcrumbs {
	margin-left: 0;
	padding-left: 0
}

.mm-hasnavbar-top-1 .mm-panels {
	top: 40px
}

.mm-hasnavbar-top-2 .mm-panels {
	top: 80px
}

.mm-hasnavbar-top-3 .mm-panels {
	top: 120px
}

.mm-hasnavbar-top-4 .mm-panels {
	top: 160px
}

.mm-hasnavbar-bottom-1 .mm-panels {
	bottom: 40px
}

.mm-hasnavbar-bottom-2 .mm-panels {
	bottom: 80px
}

.mm-hasnavbar-bottom-3 .mm-panels {
	bottom: 120px
}

.mm-hasnavbar-bottom-4 .mm-panels {
	bottom: 160px
}

.mm-navbar-size-2 {
	height: 80px
}

.mm-navbar-size-3 {
	height: 120px
}

.mm-navbar-size-4 {
	height: 160px
}

.mm-navbar-content-2>* {
	width: 50%
}

.mm-navbar-content-3>* {
	width: 33.33%
}

.mm-navbar-content-4>* {
	width: 25%
}

.mm-navbar-content-5>* {
	width: 20%
}

.mm-navbar-content-6>* {
	width: 16.67%
}

.mm-menu.mm-rtl {
	direction: rtl
}

.mm-menu.mm-rtl.mm-offcanvas {
	right: auto
}

.mm-menu.mm-rtl .mm-panel:not (.mm-opened ){
	-webkit-transform: translate(-100%, 0);
	-ms-transform: translate(-100%, 0);
	transform: translate(-100%, 0);
	-webkit-transform: translate3d(-100%, 0, 0);
	transform: translate3d(-100%, 0, 0)
}

.mm-menu.mm-rtl .mm-panel.mm-subopened {
	-webkit-transform: translate(30%, 0);
	-ms-transform: translate(30%, 0);
	transform: translate(30%, 0);
	-webkit-transform: translate3d(30%, 0, 0);
	transform: translate3d(30%, 0, 0)
}

.mm-menu.mm-rtl .mm-navbar .mm-btn:first-child {
	left: auto;
	right: 0
}

.mm-menu.mm-rtl .mm-navbar .mm-btn:last-child {
	right: auto;
	left: 0
}

.mm-menu.mm-rtl .mm-navbar .mm-next:after {
	-webkit-transform: rotate(-45deg);
	-ms-transform: rotate(-45deg);
	transform: rotate(-45deg);
	left: 23px;
	right: auto
}

.mm-menu.mm-rtl .mm-navbar .mm-prev:before {
	-webkit-transform: rotate(135deg);
	-ms-transform: rotate(135deg);
	transform: rotate(135deg);
	right: 23px;
	left: auto
}

.mm-menu.mm-rtl .mm-listview>li:not (.mm-divider )::after {
	left: 0;
	right: 20px
}

.mm-menu.mm-rtl .mm-listview>li>a:not (.mm-next ), .mm-menu.mm-rtl .mm-listview>li>span:not
	(.mm-next ){
	padding-left: 10px;
	padding-right: 20px !important;
	margin-right: 0 !important
}

.mm-menu.mm-rtl .mm-listview .mm-next {
	right: auto;
	left: 0
}

.mm-menu.mm-rtl .mm-listview .mm-next:before {
	left: auto;
	right: 0
}

.mm-menu.mm-rtl .mm-listview .mm-next:after {
	-webkit-transform: rotate(-45deg);
	-ms-transform: rotate(-45deg);
	transform: rotate(-45deg);
	left: 23px;
	right: auto
}

.mm-menu.mm-rtl .mm-listview .mm-next+a, .mm-menu.mm-rtl .mm-listview .mm-next+span
	{
	margin-left: 50px
}

.mm-menu.mm-rtl .mm-listview .mm-next.mm-fullsubopen+a, .mm-menu.mm-rtl .mm-listview .mm-next.mm-fullsubopen+span
	{
	padding-left: 50px
}

.mm-menu.mm-rtl em.mm-counter {
	left: 45px;
	right: auto
}

.mm-menu.mm-rtl em.mm-counter+a.mm-next+a, .mm-menu.mm-rtl em.mm-counter+a.mm-next+span
	{
	margin-left: 90px
}

.mm-menu.mm-rtl .mm-listview em.mm-counter+.mm-fullsubopen+a, .mm-menu.mm-rtl .mm-listview em.mm-counter+.mm-fullsubopen+span
	{
	padding-left: 90px
}

.mm-menu.mm-rtl label.mm-check, .mm-menu.mm-rtl label.mm-toggle {
	left: 20px;
	right: auto !important
}

.mm-menu.mm-rtl label.mm-toggle+a, .mm-menu.mm-rtl label.mm-toggle+span
	{
	padding-left: 80px
}

.mm-menu.mm-rtl label.mm-check+a, .mm-menu.mm-rtl label.mm-check+span {
	padding-left: 60px
}

.mm-menu.mm-rtl a.mm-next+label.mm-check, .mm-menu.mm-rtl a.mm-next+label.mm-toggle
	{
	left: 60px
}

.mm-menu.mm-rtl a.mm-next+label.mm-check+a, .mm-menu.mm-rtl a.mm-next+label.mm-check+span,
	.mm-menu.mm-rtl a.mm-next+label.mm-toggle+a, .mm-menu.mm-rtl a.mm-next+label.mm-toggle+span
	{
	margin-left: 50px
}

.mm-menu.mm-rtl a.mm-next+label.mm-toggle+a, .mm-menu.mm-rtl a.mm-next+label.mm-toggle+span
	{
	padding-left: 70px
}

.mm-menu.mm-rtl a.mm-next+label.mm-check+a, .mm-menu.mm-rtl a.mm-next+label.mm-check+span
	{
	padding-left: 50px
}

.mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-check, .mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-toggle
	{
	left: 100px
}

.mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-check+a, .mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-check+span,
	.mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-toggle+a, .mm-menu.mm-rtl em.mm-counter+a.mm-next+label.mm-toggle+span
	{
	margin-left: 90px
}

.mm-menu.mm-rtl .mm-panel[class*=mm-iconpanel-] {
	left: 0
}

.mm-menu.mm-rtl .mm-panel[class*=mm-iconpanel-].mm-subopened {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel {
	-webkit-transition-property: right, -webkit-transform;
	transition-property: right, -webkit-transform;
	transition-property: transform, right;
	transition-property: transform, right, -webkit-transform
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-0 {
	right: 0
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-1 {
	right: 40px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-2 {
	right: 80px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-3 {
	right: 120px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-4 {
	right: 160px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-5 {
	right: 200px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-iconpanel-6 {
	right: 240px
}

.mm-menu.mm-rtl.mm-iconpanel .mm-panel.mm-opened {
	border-left: none;
	border-right: 1px solid;
	border-color: inherit
}

.mm-search, .mm-search input {
	box-sizing: border-box
}

.mm-search {
	height: 40px;
	padding: 7px 10px 0;
	position: relative
}

.mm-search input {
	border: none !important;
	outline: 0 !important;
	box-shadow: none !important;
	border-radius: 4px;
	font: inherit;
	font-size: 14px;
	line-height: 26px;
	display: block;
	box-sizing: border-box;
	width: 100%;
	height: 26px;
	min-height: 26px;
	max-height: 26px;
	margin: 0;
	padding: 0 10px
}

.mm-search input::-ms-clear {
	display: none
}

.mm-search .mm-clear, .mm-search .mm-next {
	right: 0
}

.mm-panel>.mm-search {
	width: 100%;
	position: absolute;
	top: 0;
	left: 0
}

.mm-panel.mm-hassearch {
	padding-top: 40px
}

.mm-panel.mm-hassearch.mm-hasnavbar {
	padding-top: 80px
}

.mm-panel.mm-hassearch.mm-hasnavbar>.mm-search {
	top: 40px
}

.mm-noresultsmsg {
	text-align: center;
	font-size: 21px;
	padding: 40px 0
}

.mm-noresults .mm-indexer {
	display: none !important
}

li.mm-nosubresults>a.mm-next {
	display: none
}

li.mm-nosubresults>a.mm-next+a, li.mm-nosubresults>a.mm-next+span {
	padding-right: 10px
}

.mm-menu .mm-indexer a, .mm-menu .mm-noresultsmsg {
	color: rgba(0, 0, 0, .3)
}

.mm-indexer {
	background: inherit;
	text-align: center;
	font-size: 12px;
	box-sizing: border-box;
	width: 20px;
	position: absolute;
	top: 0;
	bottom: 0;
	right: -100px;
	z-index: 15;
	-webkit-transition: right .4s ease;
	transition: right .4s ease;
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0)
}

.mm-indexer a {
	display: block;
	height: 3.85%
}

.mm-indexer ~.mm-panel.mm-hasindexer {
	padding-right: 40px
}

.mm-hasindexer .mm-indexer {
	right: 0
}

.mm-hasindexer .mm-fixeddivider {
	right: 20px
}

.mm-menu.mm-hoverselected .mm-listview>li>a.mm-fullsubopen+a, .mm-menu.mm-hoverselected .mm-listview>li>a.mm-fullsubopen+span,
	.mm-menu.mm-hoverselected .mm-listview>li>a:not (.mm-fullsubopen ),
	.mm-menu.mm-parentselected .mm-listview>li>a.mm-fullsubopen+a, .mm-menu.mm-parentselected .mm-listview>li>a.mm-fullsubopen+span,
	.mm-menu.mm-parentselected .mm-listview>li>a:not (.mm-fullsubopen ){
	-webkit-transition: background .4s ease;
	transition: background .4s ease
}

.mm-menu.mm-hoverselected .mm-listview>li>a.mm-fullsubopen:hover+span,
	.mm-menu.mm-hoverselected .mm-listview>li>a:not (.mm-fullsubopen ):hover,
	.mm-menu.mm-parentselected .mm-listview>li>a.mm-selected.mm-fullsubopen+a,
	.mm-menu.mm-parentselected .mm-listview>li>a.mm-selected.mm-fullsubopen+span,
	.mm-menu.mm-parentselected .mm-listview>li>a.mm-selected:not (.mm-fullsubopen
	){
	background: rgba(255, 255, 255, .5)
}

input.mm-check, input.mm-toggle {
	position: absolute;
	left: -10000px
}

label.mm-check, label.mm-toggle {
	margin: 0;
	position: absolute;
	top: 50%;
	z-index: 2;
	right: 20px
}

label.mm-check:before, label.mm-toggle:before {
	content: '';
	display: block
}

label.mm-toggle {
	border-radius: 30px;
	width: 50px;
	height: 30px;
	margin-top: -15px
}

label.mm-toggle:before {
	border-radius: 30px;
	width: 28px;
	height: 28px;
	margin: 1px
}

input.mm-toggle:checked ~label.mm-toggle:before {
	float: right
}

label.mm-check {
	width: 30px;
	height: 30px;
	margin-top: -15px
}

label.mm-check:before {
	border-left: 3px solid;
	border-bottom: 3px solid;
	width: 40%;
	height: 20%;
	margin: 25% 0 0 20%;
	opacity: .1;
	-webkit-transform: rotate(-45deg);
	-ms-transform: rotate(-45deg);
	transform: rotate(-45deg)
}

input.mm-check:checked ~label.mm-check:before {
	opacity: 1
}

li.mm-vertical label.mm-check, li.mm-vertical label.mm-toggle {
	bottom: auto;
	margin-top: 0;
	top: 5px
}

label.mm-toggle+a, label.mm-toggle+span {
	padding-right: 80px
}

label.mm-check+a, label.mm-check+span {
	padding-right: 60px
}

a.mm-next+label.mm-check, a.mm-next+label.mm-toggle {
	right: 60px
}

a.mm-next+label.mm-check+a, a.mm-next+label.mm-check+span, a.mm-next+label.mm-toggle+a,
	a.mm-next+label.mm-toggle+span {
	margin-right: 50px
}

a.mm-next+label.mm-toggle+a, a.mm-next+label.mm-toggle+span {
	padding-right: 70px
}

a.mm-next+label.mm-check+a, a.mm-next+label.mm-check+span {
	padding-right: 50px
}

em.mm-counter+a.mm-next+label.mm-check, em.mm-counter+a.mm-next+label.mm-toggle
	{
	right: 100px
}

em.mm-counter+a.mm-next+label.mm-check+a, em.mm-counter+a.mm-next+label.mm-check+span,
	em.mm-counter+a.mm-next+label.mm-toggle+a, em.mm-counter+a.mm-next+label.mm-toggle+span
	{
	margin-right: 90px
}

.mm-menu label.mm-toggle {
	background: rgba(0, 0, 0, .1)
}

.mm-menu label.mm-toggle:before {
	background: #f3f3f3
}

.mm-menu input.mm-toggle:checked ~label.mm-toggle {
	background: #4bd963
}

.mm-menu label.mm-check:before {
	border-color: rgba(0, 0, 0, .75)
}

body {
	font-family: Verdana, "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 14px;
	line-height: 1.42857143;
	color: #333;
	background-color: #fff;
	font-weight: 400;
	-webkit-animation: androidbugfix infinite 1s
}

img {
	vertical-align: middle;
	border: 0
}

.invisible {
	width: 0
}

.home-page-banner {
	margin: 0 10px 10px;
	position: relative
}

.home-page-banner img, .home-page-banner-top img {
	width: 100%
}

.home-page-banner-subtext-cont {
	position: absolute;
	bottom: 0;
	width: 100%;
	background: -moz-linear-gradient(top, rgba(0, 0, 0, .3) 0,
		rgba(0, 0, 0, .3) 100%);
	background: -webkit-linear-gradient(top, rgba(0, 0, 0, .3) 0,
		rgba(0, 0, 0, .3) 100%);
	background: linear-gradient(to bottom, rgba(0, 0, 0, .3) 0,
		rgba(0, 0, 0, .3) 100%);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#4d000000',
		endColorstr='#4d000000', GradientType=0)
}

.home-page-banner-subtext {
	line-height: 30px;
	height: 30px;
	color: #fff;
	text-align: center
}

.navbar-brand, .navbar-nav>li>a {
	text-shadow: 0 1px 0 rgba(255, 255, 255, .25)
}

.navbar-brand {
	float: left;
	font-size: 18px;
	line-height: 20px;
	padding: 15px 0 0;
	width: 62%
}

@media screen and (max-width:320px) {
	.navbar-brand {
		padding: 18px 0 0
	}
}

.navbar {
	background-color: #6AAA6D;
	margin: 0
}

.navbar-header {
	padding: 2px 5px
}

.navbar-toggle {
	width: 44px;
	height: 40px;
	position: relative;
	float: right;
	padding: 9px 10px;
	margin-right: 15px;
	margin-bottom: 5px;
	background-color: transparent;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px
}

.navbar-ecomvalue {
	float: left;
	margin: 8px 6px 0 0;
	position: relative
}

@media screen and (max-width:320px) {
	.navbar-ecomvalue {
		margin: 8px 0 0
	}
}

.navbar-ecomvalue a {
	width: 40px;
	height: 30px;
	display: block
}

#navMenuItems .navbar {
	height: 65px
}

.navbar .navbar-header .rightSide {
	margin: 0 6px 0 0;
	max-height: 40px
}

@media screen and (max-width:320px) {
	.navbar .navbar-header .rightSide {
		margin: 0
	}
}

.img-responsive {
	display: block;
	max-width: 100%;
	height: auto
}

.icon-truck {
	background: url(/media/mobileweb/images/topbar-truck-icon.png) center
		center no-repeat;
	background-size: 100%;
	margin-right: 10px
}

.navbar-toggle .icon-bar {
	background: url(/media/mobileweb/images/topbar-menu-icon.png) center
		center no-repeat;
	background-size: 100%;
	width: 28px;
	height: 30px;
	display: inline-block
}

.locabar-cart-count-cont .locabar-circle-cont {
	top: -6px
}

#mobilehomeMainDiv {
	min-height: 30vh;
	margin-bottom: 20px;
	width: 99%
}

#search-faq, #topSearchField, .search-field {
	margin: 0;
	height: 40px;
	padding: 3px 35px;
	background-color: #fff;
	width: 100%;
	color: #444;
	border: 1px solid #6AAA6D;
	border-radius: 0;
	font-size: 18px
}

#search, .search-cont {
	width: 95%;
	margin: 10px auto;
	position: relative
}

#search span, .search-cont span {
	color: #6AAA6D;
	position: absolute;
	font-size: 20px;
	left: 10px;
	top: 6px
}

.search-cont #search-faq {
	height: 32px;
	font-weight: 700
}

input[type=email], input[type=password], input[type=text] {
	border-top: 2px solid #fff;
	border-right: 2px solid #fff;
	border-bottom: 2px solid #d4d4d4;
	border-left: 2px solid #fff;
	border-radius: 0;
	box-sizing: border-box;
	color: #818181;
	display: block;
	font-size: 16px;
	font-weight: 400;
	font-family: verdana;
	min-width: 40%;
	width: 100%;
	box-shadow: none;
	height: auto;
	outline: 0;
	padding: 15px 5px 6px;
	-webkit-font-smoothing: antialiased;
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none
}

.fnt-18 {
	font-size: 18px
}

.wid56 {
	width: 56%
}

.wid57 {
	width: 57%
}

.navbar-header-left {
	width: 51%
}

footer .anchors {
	-webkit-column-count: 2;
	-moz-column-count: 2;
	column-count: 2;
	margin: 31px 41px
}

footer .anchors a {
	display: block;
	width: 100%;
	font-size: 15px;
	color: #fff;
	margin: 10px 0
}

footer .footercontent {
	background-color: #6AAA6E;
	display: inline-block;
	width: 100%
}

footer .socialmedia {
	border-top: 2px dotted #fff;
	padding: 18px;
	text-align: center
}

footer .storemedia {
	border-top: 2px dotted #fff;
	padding: 18px 38px
}

footer .storemedia img {
	width: 48%
}

footer .basicHits {
	margin: 20px;
	color: #458b4c;
	font-size: 10px;
	text-align: center
}

footer .basicHits a {
	color: #458b4c;
	padding: 10px 0;
	outline: 0
}

footer .copyright {
	margin: 0 20px 20px;
	color: #333;
	font-size: 10px;
	text-align: center
}

.iconnav {
	width: 35px;
	height: 40px;
	background-size: 100%;
	background-position: center center;
	background-repeat: no-repeat;
	display: inline-block
}

.icon-browse-shop {
	background-image: url(/media/mobileweb/images/browse-shop-icon.png)
}

.icon-browse-reorder {
	background-image: url(/media/mobileweb/images/browse-reorder-icon.png)
}

.icon-browse-tag {
	background-image: url(/media/mobileweb/images/browse-tag-icon.png)
}

.primaryLink a {
	color: #a1a1a1;
	padding: 5px 10px 5px 20px;
	display: block
}

.pNavLoginButton {
	margin: 12px 18px;
	background-color: #fafafa;
	padding: 0;
	color: #6aaa6d;
	font-size: 1.2em;
	font-weight: 400;
	text-align: center
}

.pNavLoginButton a {
	height: 48px;
	padding: 0;
	line-height: 48px;
	color: #4fa157;
	border-radius: 4px;
	border: 1px solid #4fa157
}

.createacc a {
	color: #fff;
	background: #6aaa6d
}

.icon-cart, .icon-search, .locabar-tab-fdx {
	background-repeat: no-repeat
}

.pNavLoginButton-cont {
	margin-top: 30px
}

#navMenuItems>li {
	width: 100%
}

#navMenuItems .ui-state-active, #navMenuItems .ui-state-focus {
	border: none
}

#navMenuItems li {
	color: #458b4c;
	font-size: 1em
}

#navMenuItems li.ui-menu-item {
	padding-left: 30px
}

.navMenuItems-topIcon>.navlabel {
	line-height: 40px;
	font-weight: 700
}

.navlabel {
	display: inline-block;
	vertical-align: top;
	margin-top: 1em
}

.navMenuItems-browse {
	right: 0
}

.glBreadcrumblink {
	display: block;
	padding: .8em .8em .8em 10px;
	border: 1px solid #dbdbdb;
	border-width: 0 0 1px
}

.icon-cart, .icon-search, .locabar-tab, .locabar-tab-fdx {
	display: inline-block
}

.glBreadcrumblink>.ui-icon {
	background-size: 100%;
	height: 30px;
	width: 30px;
	display: inline-block
}

.icon-search {
	background-image: url(/media/mobileweb/images/searchbar-icon.png);
	background-size: 100%;
	height: 20px;
	width: 20px
}

.icon-cart {
	background-image: url(/media/mobileweb/images/cart-icon.png);
	background-size: 100%;
	height: 25px;
	width: 25px
}

.pull-left {
	float: left !important
}

.pull-right {
	float: right !important
}

#messages.open .close-handler, #modifyorderalert .alert-closeHandler,
	.close-x {
	top: 0;
	bottom: initial;
	right: 0;
	background-image: url(/media/mobileweb/images/close-grey-icon.png);
	background-size: 100%;
	height: 24px;
	width: 24px;
	margin: 6px;
	z-index: 99;
	position: absolute;
	left: auto
}

.locabar-tab {
	padding: 5px 15px;
	margin-top: 9px;
	background-color: #E1F0DE;
	color: #6AAA6D;
	font-size: 24px;
	font-weight: 700;
	line-height: 23px
}

.locabar-tab-fdx {
	background-image:
		url(/media/layout/nav/globalnav/fdx/locabar-tab-fdx.png);
	height: 23px;
	width: 84px
}

.mm-menu .mm-panel ul.mm-listview li.navMenuItems-menuselect>span.glBreadcrumblink
	{
	border-right: 9px solid #4fa157;
	padding: 9px 10px 10px 20px
}

.mm-subopened .mm-subblocker {
	z-index: 3
}

#nav-menu {
	background-color: #fff
}

.mm-navbar {
	background-color: #6AAA6D;
	height: 60px
}

.mm-navbar-bottom>.navbar-cont {
	background-color: #6AAA6D;
	padding-top: 0;
	height: 50px
}

.mm-navbar-bottom {
	height: 50px
}

.mm-panels>.mm-panel.mm-hasnavbar {
	padding-top: 60px
}

.mm-panel.mm-hasnavbar.mm-hasnavbar-rem .mm-navbar {
	display: none
}

.mm-panels>.mm-panel.mm-hasnavbar-rem {
	margin-top: 60px;
	padding-top: 0
}

.mm-iconpanel .mm-panel.mm-iconpanel-1 {
	left: 80px
}

.navMenuItems-topIcons {
	background-color: #f1f1f1
}

.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .iconnav {
	display: block
}

.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .navlabel {
	width: 80px;
	display: inline-block;
	text-align: center;
	margin-left: -20px;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;
	margin-top: 0
}

.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .mm-selected {
	background-color: transparent
}

.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened .hide-on-subopen {
	display: none
}

.mm-iconpanel .mm-panel.mm-iconpanel-0.mm-subopened>.mm-subblocker {
	opacity: 0
}

.mm-menu .mm-panel ul.mm-listview li span.glBreadcrumblink {
	padding: 0
}

.mm-menu .mm-panel ul.mm-listview li span.glBreadcrumblink a {
	display: block;
	padding: 9px 10px 10px 20px
}

.mm-menu .mm-navbar a, .mm-menu .mm-navbar>* {
	color: rgba(255, 255, 255, 1)
}

.mm-menu .mm-btn:after, .mm-menu .mm-btn:before {
	border-color: rgba(255, 255, 255, 1)
}

.boldgray {
	font-weight: 700;
	color: #656565
}

.no-divider:after {
	border-bottom-width: 0
}

.NOMOBWEB {
	display: none
}

#topSearchField {
	float: none
}

#messages, #messages ul, .alert-cont {
	width: 100%
}

.oas-cnt img {
	max-width: 100%
}

.locabar-cart {
	background-image: url(/media/layout/nav/globalnav/fdx/locabar-cart.png)
}

#smartbanner {
	position: absolute;
	left: 0;
	top: -82px;
	border-bottom: 1px solid #e8e8e8;
	width: 100%;
	height: 78px;
	font-family: 'Helvetica Neue', sans-serif;
	background: -webkit-linear-gradient(top, #f4f4f4 0, #cdcdcd 100%);
	background-image: -ms-linear-gradient(top, #F4F4F4 0, #CDCDCD 100%);
	background-image: -moz-linear-gradient(top, #F4F4F4 0, #CDCDCD 100%);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .5);
	z-index: 9998;
	-webkit-font-smoothing: antialiased;
	overflow: hidden;
	-webkit-text-size-adjust: none
}

#smartbanner, html.sb-animation {
	-webkit-transition: all .3s ease
}

#smartbanner .sb-container {
	margin: 0 auto
}

#smartbanner .sb-close {
	position: absolute;
	left: 5px;
	top: 5px;
	display: block;
	border: 2px solid #fff;
	width: 14px;
	height: 14px;
	font-family: ArialRoundedMTBold, Arial;
	font-size: 15px;
	line-height: 15px;
	text-align: center;
	color: #fff;
	background: #070707;
	text-shadow: none;
	border-radius: 14px;
	box-shadow: 0 2px 3px rgba(0, 0, 0, .4);
	-webkit-font-smoothing: subpixel-antialiased
}

#smartbanner .sb-button, #smartbanner .sb-info {
	font-weight: 700;
	text-shadow: 0 1px 0 rgba(255, 255, 255, .8);
	position: absolute
}

#smartbanner .sb-close:active {
	font-size: 13px;
	color: #aaa
}

#smartbanner .sb-icon {
	position: absolute;
	left: 30px;
	top: 10px;
	display: block;
	width: 57px;
	height: 57px;
	background: rgba(0, 0, 0, .6);
	background-size: cover;
	border-radius: 10px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, .3)
}

#smartbanner.no-icon .sb-icon {
	display: none
}

#smartbanner .sb-info {
	left: 98px;
	top: 18px;
	width: 44%;
	font-size: 11px;
	line-height: 1.2em;
	color: #6a6a6a
}

#smartbanner #smartbanner.no-icon .sb-info {
	left: 34px
}

#smartbanner .sb-info strong {
	display: block;
	font-size: 13px;
	color: #4d4d4d;
	line-height: 18px
}

#smartbanner .sb-info>span {
	display: block
}

#smartbanner .sb-info em {
	font-style: normal
}

#smartbanner .sb-button {
	right: 20px;
	top: 24px;
	border: 1px solid #bfbfbf;
	padding: 0 10px;
	min-width: 10%;
	height: 24px;
	font-size: 14px;
	line-height: 24px;
	text-align: center;
	color: #6a6a6a;
	background: -webkit-linear-gradient(top, #efefef 0, #dcdcdc 100%);
	text-decoration: none;
	border-radius: 3px;
	box-shadow: 0 1px 0 rgba(255, 255, 255, .6), 0 1px 0
		rgba(255, 255, 255, .7) inset
}

#smartbanner .sb-button:active, #smartbanner .sb-button:hover {
	background: -webkit-linear-gradient(top, #dcdcdc 0, #efefef 100%)
}

#smartbanner .sb-icon.gloss:after {
	content: '';
	position: absolute;
	left: 0;
	top: -1px;
	border-top: 1px solid rgba(255, 255, 255, .8);
	width: 100%;
	height: 50%;
	background: -webkit-linear-gradient(top, rgba(255, 255, 255, .7) 0,
		rgba(255, 255, 255, .2) 100%);
	border-radius: 10px 10px 12px 12px
}

#smartbanner.android {
	border-color: #212228;
	background:
		url(data:image/gif;base64,R0lGODlhCAAIAIABAFVVVf///yH5BAEHAAEALAAAAAAIAAgAAAINRG4XudroGJBRsYcxKAA7)
		#3d3d3d;
	border-top: 5px solid #88B131;
	box-shadow: none
}

#smartbanner.android .sb-close {
	border: 0;
	width: 17px;
	height: 17px;
	line-height: 17px;
	color: #b1b1b3;
	background: #1c1e21;
	text-shadow: 0 1px 1px #000;
	box-shadow: 0 1px 2px rgba(0, 0, 0, .8) inset, 0 1px 1px
		rgba(255, 255, 255, .3)
}

#smartbanner.android .sb-close:active {
	color: #eee
}

#smartbanner.android .sb-info {
	color: #ccc;
	text-shadow: 0 1px 2px #000
}

#smartbanner.android .sb-info strong {
	color: #fff
}

#smartbanner.android .sb-button {
	min-width: 12%;
	border: 1px solid #DDDCDC;
	padding: 1px;
	color: #d1d1d1;
	background: 0 0;
	border-radius: 0;
	box-shadow: none;
	min-height: 28px
}

#smartbanner.android .sb-button span {
	text-align: center;
	display: block;
	padding: 0 10px;
	background-color: #42B6C9;
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#42B6C9),
		to(#39A9BB));
	background-image: -moz-linear-gradient(top, #42B6C9, #39A9BB);
	text-transform: none;
	text-shadow: none;
	box-shadow: none
}

#smartbanner.android .sb-button:active, #smartbanner.android .sb-button:hover
	{
	background: 0 0
}

#smartbanner.android .sb-button:active span, #smartbanner.android .sb-button:hover span
	{
	background: #2AC7E1
}

#smartbanner.windows .sb-icon {
	border-radius: 0
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
					
				<%-- updateOAS code --%>
					function OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
						return OAS_url + 'adstream_mjx.ads/' +
								OAS_sitepage + '/1' + OAS_rns + '@' +
								OAS_listpos + '?' + OAS_query;
					}
	
					function done(listPos) {
						var $ = $jq;
						listPos.forEach(function (pos) {
							var selector = "#oas_"+pos+",#oas_b_"+pos;
							$(selector).each(function(i,e){
								$(e).html('');
								postscribe($(e), '<sc'+'ript>OAS_RICH("'+pos+'");</scr'+'ipt>', {
									error: function () {},
									done: function (pos) {
										$.each($('a[href*="/default/empty.gif/"]'), function(ii, ee) {
											$(ee).attr("tabindex", "-1");
											$(ee).attr("role", "presentation");
											$(ee).attr("aria-hidden", "true");
	
											if (fd.utils.isDeveloper()) {
												console.log('updateOAS: done', pos, $(ee));
											}
										});
									}
								});
							});
						});
					}
	
					function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
						var scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos.join(','), OAS_query);
	
						postscribe(document.body, '<sc'+'ript src="'+scriptUrl+'"></scr'+'ipt>', {
							done: function () {
							  done(OAS_listpos);
							}, error: function () {}
						});
					}
					FreshDirect.updateOAS = {};
					FreshDirect.updateOAS.done = done;
					
					<%-- copied from utils.js for dfp.js --%>
					fd.utils = fd.utils || {};
					fd.utils.getParameters = function (source) {
					  source = source || window.location.search.slice(1);
					
					  if (!source) {
					    return null;
					  }
					
					  var vars = {}, hash,
					      hashes = source.split('&');
					
					  hashes.forEach(function (h) {
					    hash = h.split('=');
					    vars[hash[0]] = window.decodeURIComponent(hash[1]);
					  });
					
					  return vars;
					};
					
			}());
		</script>
		<jwr:script src="/mobileweb_index_optimized_everythingelse.js" useRandomParam="false" />
    	<jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />

    	<tmpl:get name="extraJs" />
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
		
				if (dpTcCheckUser != null &&
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
   	<%-- //
   		we can't bundle dfp.js because it needs the global DFP_query (set in ad_server.jsp) before it's loaded
   		it also needs to be after all the ad positions, since it fires on parse and selects all spots
   		added to a footer bundle to minimize impact
   	// --%>
   	<% if (FDStoreProperties.isDfpEnabled()) { /* only load if needed */ %>
		<jwr:script src="/mobileweb_index_optimized_footer.js" useRandomParam="false" defer="true" async="true" />
	<% } %>
  </body>
</html>
