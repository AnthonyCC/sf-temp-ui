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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user"/>

<%

request.setAttribute("sitePage", "www.freshdirect.com/");
request.setAttribute("listPos", "SystemMessage,LittleRandy");
	
ProductModel productNode = ProductPricingFactory.getInstance().getPricingAdapter( ContentFactory.getInstance().getProductByName( request.getParameter("catId"), request.getParameter("productId") ), user.getPricingContext() );

// Handle no-product case
if (productNode==null) {
    throw new JspException("Product not found in Content Management System");
} else if (productNode.isDiscontinued()) {
    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
}

boolean isWine = EnumTemplateType.WINE.equals( productNode.getTemplateType() );
%>

<tmpl:insert template='/common/template/pdp_template.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Confirmation</tmpl:put>    
    
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
			<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;" width="150"><div align="center" style="background: #272324"><a href="/department.jsp?deptId=usq&trk=snav"><img src="/media/editorial/win_usq/usq_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
			<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			</td>    	
		</tmpl:put>
    <% } %>
    
	<tmpl:put name='content' direct='true'>		
		<jsp:include page="/includes/product/cartConfirm.jsp" >
			<jsp:param name="catId" value="${ param.catId }"/>
			<jsp:param name="productId" value="${ param.productId }"/>
			<jsp:param name="cartlineId" value="${ param.cartlineId }"/>
		</jsp:include>
	</tmpl:put>

</tmpl:insert>