<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.ProducerModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
<%@page import="com.freshdirect.fdstore.content.EnumPopupType"%>
<%@page import="com.freshdirect.fdstore.content.TitledMedia"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<tmpl:insert template='/common/template/top_nav_only.jsp'>
	<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
	<tmpl:put name='title' direct='true'>FreshDirect - 4-Minute Meals</tmpl:put>
	<%-- SCRIPTS IN HEADER --%>
	<tmpl:put name='head_content'>
	</tmpl:put>
	<%-- CONTENT --%>
	<tmpl:put name='content' direct='true'>
<%
	DepartmentModel dept = FourMinuteMealsHelper.get4mmDepartment(); // 4mm department node
	CategoryModel aboutPage = FourMinuteMealsHelper.getAboutPage(); // 4mm about page
	List<Html> topMedia=aboutPage.getTopMedia();
	List<Html> middleMedia=aboutPage.getMiddleMedia();
	List<BrandModel> chefBrands=aboutPage.getFeaturedBrands();
	
%>
	<div class="fourmm aboutpage">
	<%@ include file="/includes/layouts/4mm/title.jspf"%>		
<%
	if(topMedia!=null) {
%>
<logic:iterate id="topHtml" collection="<%= topMedia %>">
<fd:IncludeMedia name="<%= ((Html) topHtml).getPath() %>"></fd:IncludeMedia>
</logic:iterate>
<%		
	}
%>
<%
	if(chefBrands!=null) {
		String chefName=null;
		String chefBlurb=null;
		Image chefPhoto=null;
		Html chefPopup=null;
		Image brandLogo=null;
		
%>
<img src="/media_stat/images/4mm/chefs_header.gif" />
<div class="chef-row">
<display:ContentNodeIterator trackingCode="dpage" itemsToShow="<%= chefBrands %>" id="chefs" noTypeCheck="true">
<%
	BrandModel brand =(BrandModel) currentItem;
	brandLogo = brand.getLogoSmall();
	chefName = brand.getChefName();
	chefBlurb = brand.getChefBlurb();
	chefPhoto = brand.getChefImage();
	chefPopup = brand.getPopupContent();
%>
<div class="chef">
	<% if ( chefPhoto != null ) { %>
		<img class="chef-image" src="<%= chefPhoto.getPath() %>" />
	<% } %>
	<div class="chef-info">
		<h3 class="chef-name"><%= chefName %></h3>
		<% if ( chefPopup != null ) { %>
			<p class="chef-bio"><%= chefBlurb %> <a href="<%= chefPopup.getPath() %>" target="_blank" onclick="popup('<%= chefPopup.getPath() %>','large'); return false;">Learn&nbsp;More</a></p>
		<% } %>
		<div style="line-height:100%">
			<% if ( brandLogo != null ) { %>
				<img src="<%= brandLogo.getPath() %>" style="vertical-align:bottom" />
			<% } %> 
			<a href="" style="font-weight:bold;">See&nbsp;Meals</a>
		</div>
	</div>
</div>

<% if(itemIndex % 2==1) { %>
	</div><div class="chef-row">
<% } %>

</display:ContentNodeIterator>
</div>
<%		
	}
%>
<%
	if(topMedia!=null) {
%>
<logic:iterate id="middleHtml" collection="<%= middleMedia %>">
<fd:IncludeMedia name="<%= ((Html) middleHtml).getPath() %>"></fd:IncludeMedia>
</logic:iterate>
<%		
	}
%>
		<img src="/media_stat/images/4mm/browse_header.gif" />
		<% boolean fourColumnLayout = true; boolean needToFilter = false; boolean enableAllItem=true; %>
		<div style="width:676px;margin:6px auto">
		<%@ include file="/includes/layouts/4mm/filter_widget.jspf"%>
		</div>					
		<div id="ads">
			<script type="text/javascript">OAS_AD('4mmAd1');</script>
			<script type="text/javascript">OAS_AD('4mmAd2');</script>
		</div>
		<a href="<%= FourMinuteMealsHelper.allPageBaseUrl %>" style="text-decoration:none"><img src="/media_stat/images/4mm/dpt_4mm_viewall.gif"  border="0" alt="View all 4-Minute Meals"/></a>
		<%@ include file="/includes/layouts/4mm/see_all_button.jspf"%>
	</div>
	</tmpl:put>
</tmpl:insert>
