<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import="java.util.*"%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.pricing.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*"%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user"/>
<%
ProductModel productNode = ProductPricingFactory.getInstance().getPricingAdapter( ContentFactory.getInstance().getProductByName( request.getParameter("catId"), request.getParameter("productId") ), user.getPricingContext() );

boolean isWine = false;
if(productNode!=null){
	//productNode should be safe to set sitePage OAS param
	request.setAttribute("sitePage", productNode.getPath());
	request.setAttribute("listPos", "SystemMessage,LittleRandy");
	isWine = EnumTemplateType.WINE.equals( productNode.getTemplateType() );
	
	//special layout product ATC is not done with AJAX, so it needs to be reported to Coremetrics on this page
	pageContext.setAttribute("isSpecialLayout", productNode.getSpecialLayout()!=null);
	
}

//should we show the new leftnav
boolean shouldBeOnNew = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user);
%>
<potato:browse name="browsePotato" pdp="true" nodeId='${param.catId}'/>

<tmpl:insert template='/common/template/pdp_template.jsp'>

  <tmpl:put name='cmeventsource' direct='true'>cart_confirm_pdp</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>
  	
  	<tmpl:put name='extraCss' direct='true'>
    	<jwr:style src="/quickshop.css"/>
    	<jwr:style src="/pdp.css"/>
  	</tmpl:put>

    <tmpl:put name='title' direct='true'>FreshDirect - Confirmation</tmpl:put>
    
<% if(shouldBeOnNew) { // new leftnav, TODO: remove this after full rollout %>
   
    <tmpl:put name='deptnav' direct='true'>
    	<div class="browse-titlebar">
      		<soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    	</div>
  	</tmpl:put>   
    
   	<tmpl:put name='leftnav' direct='true'>
    	<div id="leftnav">
      		<soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    	</div>
    	<div class="backbutton">
      		<button class="cssbutton back white icon-arrow-left2-before">Back</button>
    	</div>
  	</tmpl:put>
    
	<tmpl:put name='content' direct='true'>
		<c:if test="${isSpecialLayout}"><fd:GetCart id='cart'><fd:CmShop5 wrapIntoScriptTag="true" cart="<%=cart%>"/></fd:GetCart></c:if>
		<jsp:include page="/includes/product/cartConfirm.jsp" >
			<jsp:param name="catId" value="${ param.catId }"/>
<%-- 			<jsp:param name="productId" value="${ param.productId }"/> --%>
			<jsp:param name="cartlineId" value="${ param.cartlineId }"/>
		</jsp:include>
	</tmpl:put>
	
<% } else { // old leftnav %>  
    
    <% if ( !isWine ) { // Wine template has no deptnav, and special leftnav, so only put these for regular layouts %>
	    <tmpl:put name='leftnav' direct='true'>	    	
	    	<td width="150" BGCOLOR="#E0E3D0" class="lNavTableConttd">		
			<!-- start : leftnav -->
			<% try { %><%@ include file="/common/template/includes/left_side_nav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			<!-- end : leftnav -->			
			</td>
	    </tmpl:put>
    	<tmpl:put name="extraJs">
    	</tmpl:put>
	    <tmpl:put name='deptnav' direct='true'>
		    <% try { %><%@ include file="/common/template/includes/deptnav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			<hr class="deptnav-separator">
	    </tmpl:put>
    <% } else { %>
    	<tmpl:put name="extraJs">
			<fd:javascript src="/assets/javascript/wine.js"/>
			<fd:javascript src="/assets/javascript/wine-nav.js"/>	
    	</tmpl:put>
    	<tmpl:put name="deptnav" direct="true">	
    	</tmpl:put>
    	<tmpl:put name="leftnav">
    		<% String wineAssId = JspMethods.getWineAssociateId().toLowerCase(); %>
			<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;" width="150"><div align="center"><a href="/department.jsp?deptId=<%= wineAssId %>&trk=snav"><img src="/media/editorial/win_<%= wineAssId %>/<%= wineAssId %>_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
			<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			</td>    	
		</tmpl:put>
    <% } %>
    
	<tmpl:put name='content' direct='true'>		
		<c:if test="${isSpecialLayout}"><fd:GetCart id='cart'><fd:CmShop5 wrapIntoScriptTag="true" cart="<%=cart%>"/></fd:GetCart></c:if>
		<jsp:include page="/includes/product/cartConfirm.jsp" >
			<jsp:param name="catId" value="${ param.catId }"/>
			<jsp:param name="productId" value="${ param.productId }"/>
			<jsp:param name="cartlineId" value="${ param.cartlineId }"/>
		</jsp:include>
	</tmpl:put>
<% } %>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
