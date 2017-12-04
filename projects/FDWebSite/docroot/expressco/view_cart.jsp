<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<%  //--------OAS Page Variables-----------------------
  request.setAttribute("sitePage", "www.freshdirect.com/expressco/view_cart/");
  request.setAttribute("listPos", "SystemMessage"); // TODO
  
%>

<fd:CheckLoginStatus id="user_view_cart" guestAllowed='true' recognizedAllowed='true' />

<%
String template = "/expressco/includes/ec_template.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user_view_cart) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>

<potato:cartData/>

<potato:pendingExternalAtcItem/>

<tmpl:insert template='<%=template %>'>
	<tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>
	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/expressco.js" useRandomParam="false" />
	</tmpl:put>

  <% if(!mobWeb){ %>
  <tmpl:put name="globalnav">
    <%@ include file="/common/template/includes/globalnav.jspf" %>
  </tmpl:put>
  

  <tmpl:put name="bottomnav">
    <%@ include file="/common/template/includes/footer.jspf" %>
  </tmpl:put>
  <% } %>
	
  <tmpl:put name="ecpage">view_cart</tmpl:put>
  
  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="YOUR CART" pageId="ec_view_cart"></fd:SEOMetaTag>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div class="container <%= (mobWeb)? "mobweb-checkout" : "" %>">
      <div id="warningmessage">
        <soy:render template="expressco.warningmessage" data="${cartDataPotato}" />
      </div>

      <div id="modifyorder">
        <soy:render template="expressco.modifyorder" data="${cartDataPotato}" />
      </div>

      <div id="cartheader">
        <soy:render template="expressco.cartheader" data="${cartDataPotato}" />
      </div>
      
		<% if (mobWeb) { /* copy of this text from expressco.cartheader */ %>
			<div class='cartheader__text mobweb'>
				<h1 class='cartheader__title'>Your Cart</h1>
			</div>
			<%-- cart content --%>
			<div id="cartcontent" class="view_cart" data-ec-linetemplate="expressco.viewcartlines"></div>
		<% } else { //normal web %>
		
			<% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user_view_cart)) { %>
				<div id="cartCarousels">
					<script>
			          	$jq.ajax('/carousel/carousel.jsp?type=cart').then(function(page) {
			          		$jq('#cartCarousels').html(page);
			          	});
			        </script>
				</div>
	        <% } else { 
				//APPDEV-5516 : Cart Carousel - Grand Giving Donation Technology
				// If the donationProductSampleCarousel is not enabled, the fallback div is productsamplecarousel
			    if(FDStoreProperties.isPropDonationProductSamplesEnabled()){ %>
			     <div id="donationProductSampleCarousel" class="donation-product-sample-carousel">
			     	<potato:viewCart/>
			        <soy:render template="expressco.donationProductSampleCarouselWrapper" data="${viewCartPotato}" />
			      </div>     
				<% } else { %>
				  <div id="productsamplecarousel" class="product-sample-carousel">
				  	<potato:viewCart/>
			        <soy:render template="expressco.productSampleCarouselWrapper" data="${viewCartPotato}" />
			      </div>
				<% }
			} %>
			 <div id="cartcontent" class="view_cart" data-ec-linetemplate="expressco.viewcartlines"></div>
		  <% } %>		
    </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${cartDataPotato}" noHeaders="true"/>
      window.FreshDirect.expressco.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>

      // override coupon api url
      window.overrideCouponEndpoint = '/api/expressco/coupon';
    </script>
    
  </tmpl:put>
</tmpl:insert>
