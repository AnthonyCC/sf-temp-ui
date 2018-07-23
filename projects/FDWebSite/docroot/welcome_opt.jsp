<%@ page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"
%><%@ page import='com.freshdirect.fdstore.FDStoreProperties'
%><%@ taglib uri="http://jawr.net/tags" prefix="jwr" 
%><%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='freshdirect' prefix='fd' 
%><fd:CheckLoginStatus guestAllowed='true'
/><%
	BrowserInfo welcomepageBI = new BrowserInfo(request);
%><%--
	Optimized re-write test.
	
	NOTE: This uses new, welcome page specific, includes for optimization testing. The page will be HTML5
		and should (mostly) pass validation, no old, invalid HTML, code includes.
		Because	this is OPTIMIZED, try to not use global-include-everything-in-every-page includes.
	
	Top-level items are inserted in to template, their children are assembled in to the
	top-level items in this file. This way, this file changes and the template stays static.
	All HTML that is not skeleton page tags (html/head/body) belong in THIS file.
	
	Top-level is all one word (camel-case), children are named with the top-level
	name and underscore prefixed:
		[topLevel]
			[topLevel]_[childName]
				
	TEMPLATE: /common/template/welcome_opt/t_welcome_opt.jsp
	
	head : items falling between the <head></head> tags
		head_pageTitle : document title 
		head_metatags : all meta tags
		head_css : all css includes (files/inline). Externally linked CSS goes here before JS.
		head_javascript : all javascript includes (files/inline)
						  This is js that NEEDS to be in the head tag.
						  Otherwise, place in footer_javascript.
		
	body : items falling between the <body></body> tags (but before footer items)
		body_nav : navigation includes, global nav or dept nav
		body_content : main content, layout, media includes
		
	footer : items falling between the <body></body> tags (AFTER the body items)
		footer_nav : footer nav
		footer_javascript : most javascript belongs here. Very last thing before </body> tag.

	
--%><%
	//OAS spots
	request.setAttribute("listPos", "SystemMessage,WDelivery,WAffiliate");
	request.setAttribute("noyui", true);

%><tmpl:insert template="/common/template/welcome_opt/t_welcome_opt.jsp"><%--

HEAD items
	page title
	--%>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect"/>
    </tmpl:put>
<%-- 	<tmpl:put name="head_pageTitle"><title>FreshDirect</title></tmpl:put> --%>
	
	<%--
	meta tags
	--%><tmpl:put name="head_metatags">
			<%@ include file="/common/template/welcome_opt/i_head_metatags.jspf" %>
		</tmpl:put><%--
	css
	--%><tmpl:put name="head_css">
			<%--  this file can NOT go in to jawr because of the path difference --%>
			 <fd:css href="/media/editorial/welcome/welcome-eng.css" />
			<%@ include file="/common/template/welcome_opt/i_head_css.jspf" %>
		</tmpl:put><%--
	javascript
	--%><tmpl:put name="head_javascript">
			<%@ include file="/common/template/welcome_opt/i_head_javascript.jspf" %>
		</tmpl:put><%--

HEAD

--%><tmpl:put name="head">
<%-- 		<tmpl:get name="head_pageTitle" /> --%>
		<tmpl:get name="head_metatags" />
		<tmpl:get name="head_css" />
		<tmpl:get name="head_javascript" />
\	</tmpl:put><%--
	
BODY ITEMS
	IPToolbar
	--%><tmpl:put name="body_start">
			<%-- everything else before top nav --%>
			<%@ include file="/common/template/welcome_opt/i_body_start.jspf" %>
		</tmpl:put><%--
	nav
	--%><tmpl:put name="body_nav">
			<%@ include file="/common/template/welcome_opt/i_body_nav.jspf" %>
		</tmpl:put><%--
	content
	--%><tmpl:put name="body_content" direct="true">
		    <div class="content span-24">
			<% if ( request.getParameter("lang") != null) { %>
				<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
					<fd:IncludeMedia name="/media/editorial/welcome/welcome-sp.html" />
				<% } else { %>
					<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
				<% } %>
			<% } else { %>
				<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
			<% } %>
		    </div>
		</tmpl:put><%--

BODY

--%><tmpl:put name="body">
		<tmpl:get name="body_start" />
		<tmpl:get name="body_nav" />
		<div id="main" class="container staticpage">
			<tmpl:get name="body_content" />
		</div>
	</tmpl:put><%--

FOOTER ITEMS
	nav
	--%><tmpl:put name="footer_nav">
		<%@ include file="/common/template/welcome_opt/i_footer_nav.jspf" %>
		</tmpl:put><%--
	javascript
	--%><tmpl:put name="footer_javascript">
			<%@ include file="/common/template/welcome_opt/i_footer_javascript.jspf" %>
		</tmpl:put><%--
	
FOOTER

--%><tmpl:put name="footer">
		<tmpl:get name="footer_nav" />
		<tmpl:get name="footer_javascript" />
	</tmpl:put><%--
--%></tmpl:insert>