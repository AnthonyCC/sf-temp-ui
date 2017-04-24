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

<potato:pendingExternalAtcItem/>
<potato:cartData />
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

  <tmpl:put name="title">View Cart</tmpl:put>

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

		<% if (!mobWeb) { /* no mobWeb for now */  %>
        	<% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user_view_cart)) { %>
				<%-- APPDEV-5916 --%>
				<div id="cartCarousels">
					<potato:viewCart />
					<soy:render template="expressco.viewCartTabbedCarousel" data="${viewCartPotato}" />
					<script>
						/* figure out which tab should be selected */
						/*$jq('#cartCarousels').ready(function() {
							var $tab = $jq(this).find('.tabbed-carousel:first');
							if ($tab) {
								var cmeventsource = $tab.data('cmeventsource');
								$tab.find('.tabs [data-component="tabitem"]:first').trigger('click');
							}
							
						});*/
					</script>
				</div>
			<% } %>
		<% } %>
<%-- APPDEV-5516 : Cart Carousel - Grand Giving Donation Technology --%>
<%-- If the donationProductSampleCarousel is not enabled, the fallback div is productsamplecarousel --%>
  <% if (FDStoreProperties.isObsoleteCartCarouselsEnabled()) {
		   if(FDStoreProperties.isPropDonationProductSamplesEnabled()){ %>
		     <div id="donationProductSampleCarousel" class="donation-product-sample-carousel">
		        <soy:render template="expressco.donationProductSampleCarouselWrapper" data="${cartDataPotato}" />
		      </div>     
		<% } else { %>
			  <div id="productsamplecarousel" class="product-sample-carousel">
		        <soy:render template="expressco.productSampleCarouselWrapper" data="${cartDataPotato}" />
		      </div>
		      <%}
  } %>

      <div id="questions">
        <soy:render template="expressco.cartheaderQuestions" data="${cartDataPotato}" />
      </div>

      <%-- cart content --%>
      <div id="cartcontent" class="view_cart" data-ec-linetemplate="expressco.viewcartlines">
      </div>
    </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${cartDataPotato}" noHeaders="true"/>
      window.FreshDirect.expressco.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>
		<% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user_view_cart)) { %>
      		window.FreshDirect.expressco.viewCartPotato = <fd:ToJSON object="${viewCartPotato}" noHeaders="true"/>
		<% } else { %>
			window.FreshDirect.expressco.viewCartPotato = {};
		<% } %>

      // override coupon api url
      window.overrideCouponEndpoint = '/api/expressco/coupon';
    </script>
    
  </tmpl:put>
</tmpl:insert>
