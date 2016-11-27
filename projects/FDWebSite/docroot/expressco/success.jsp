<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="userCOSuccess" guestAllowed="false" recognizedAllowed="false" />
<%  //--------OAS Page Variables-----------------------
  request.setAttribute("sitePage", "www.freshdirect.com/expressco/checkout/");
  request.setAttribute("listPos", "SystemMessage"); // TODO
  
  boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, userCOSuccess) && JspMethods.isMobile(request.getHeader("User-Agent"));
  String pageTemplate = "/expressco/includes/ec_template.jsp";
  if (mobWeb) {
  	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
  	String oasSitePage = request.getAttribute("sitePage").toString();
  	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
  		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
  	}
  }
%>
<potato:singlePageCheckoutSuccess />
<tmpl:insert template='<%= pageTemplate %>'>
  <tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <jwr:script src="/expressco.js" useRandomParam="false" />
    
    <script>
      (function () {
        if(FreshDirect.expressco.data.redirectUrl){
          FreshDirect.common.dispatcher.signal('redirectUrl', FreshDirect.expressco.data.redirectUrl);
        }
      }());
    </script>
    
  </tmpl:put>

  <tmpl:put name="globalnav">
    <%@ include file="/common/template/includes/globalnav.jspf" %>
  </tmpl:put>

  <tmpl:put name="bottomnav">
    <%@ include file="/common/template/includes/footer.jspf" %>
  </tmpl:put>

  <tmpl:put name="ecpage">success</tmpl:put>

  <tmpl:put name="title">Success</tmpl:put>

  <tmpl:put name='content' direct='true'>
	<% //based on step_4_receipt.jsp
	boolean _modifyOrderMode = false; 	
	String _ordNum = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	
	if(session.getAttribute("MODIFIED" + _ordNum) != null && session.getAttribute("MODIFIED" + _ordNum).equals(_ordNum)) {
		_modifyOrderMode = true;
	}
	%>
		
  <% if (_ordNum != null) { %>
	<fd:GetOrder id='order' saleId='<%=_ordNum%>'>
		<script type="text/javascript">
			<fd:CmShop9 order="<%=order%>"/>
			<fd:CmOrder order="<%=order%>"/>
			<fd:CmRegistration force="true"/>
			<fd:CmConversionEvent eventId="became_a_customer"/>
			<% if(_modifyOrderMode){ %>
				<fd:CmConversionEvent order="<%=order%>" orderModified="true"/>
			<% } %>
		</script>
	</fd:GetOrder>
  <% } %>

  <div id='successpage'>
      <div class="container">
        <div id="order-summary">
          <soy:render template="expressco.successpage" data="${singlePageCheckoutSuccessPotato.successPageData}" />
        </div>

        <%-- drawer --%>
        <div id="ec-drawer" drawer-id="ec-drawer">
          <soy:render template="expressco.drawer" data="${singlePageCheckoutSuccessPotato.drawer}" />
        </div>
        <div data-drawer-content-prerender="ec-drawer">
          <div data-drawer-default-content="address">
            <soy:render template="expressco.addresspreview" data="${singlePageCheckoutSuccessPotato.address}" />
          </div>
          <div data-drawer-default-content="timeslot">
            <soy:render template="expressco.successtimeslot" data="${singlePageCheckoutSuccessPotato.timeslot}" />
          </div>
          <div data-drawer-default-content="payment">
            <soy:render template="expressco.paymentmethodpreviewNew" data="${singlePageCheckoutSuccessPotato.payment}" />
          </div>
        </div>

        <%-- cart content --%>
        <h2 class="inyourorder">In Your Order</h2>
        <div id="cartcontent" data-ec-linetemplate="expressco.successlines" data-drawer-disabled data-ec-request-uri="/api/expresscheckout/cartdata?orderId=${param['orderId']}">

        </div>

      </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutSuccessPotato}" noHeaders="true"/>
    </script>

    <!-- semPixels -->
    
    <%-- TODO: refactor? --%>
    <%
      Map<String, Object> potato = (Map<String, Object>)pageContext.getAttribute("singlePageCheckoutSuccessPotato");
      Map<String, Object> semPixelData = (Map<String, Object>)potato.get("semPixelData");
    %>
    <%-- do not render semPixels for old orders --%>
    <c:if test="${singlePageCheckoutSuccessPotato.semPixelData.newOrder or singlePageCheckoutSuccessPotato.semPixelData.modifyOrder}">

      <%-- Google AdWords Pixel --%>
      <fd:SemPixelIncludeMedia pixelNames="GoogleAdWords" />
      
      <%
			/* CheetahMail Pixel */
			SemPixelModel semPixel_CM = FDSemPixelCache.getInstance().getSemPixel("CheetahMail");
	
			//add a param to the params sent to the FTL
			semPixel_CM.setParam("subtotal", semPixelData.get("subtotal")); // sem_cartSubtotal
			semPixel_CM.setParam("orderId", semPixelData.get("orderId")); // sem_orderNumber
			semPixel_CM.setParam("isOrderModify", String.valueOf((Boolean)semPixelData.get("isOrderModify"))); // String.valueOf(isOrderModify)
			semPixel_CM.setParam("userCounty", semPixelData.get("userCounty")); // sem_defaultCounty
			semPixel_CM.setParam("totalCartItems", ((Integer)semPixelData.get("totalCartItems")).toString()); // sem_totalCartItems
			%><fd:SemPixelIncludeMedia pixelNames="CheetahMail" />
		
      <c:if test="${not singlePageCheckoutSuccessPotato.semPixelData.modifyOrder}">
        <%
        /* TheSearchAgency Pixel */
				SemPixelModel semPixel_TSA = FDSemPixelCache.getInstance().getSemPixel("TheSearchAgency");
				
				//add a param to the params sent to the FTL
				semPixel_TSA.setParam("subtotal", semPixelData.get("subtotalND")); // sem_cartSubtotal
				semPixel_TSA.setParam("orderId", semPixelData.get("orderId")); // sem_orderNumber
				semPixel_TSA.setParam("validOrders", ((Integer)semPixelData.get("validOrders")).toString()); // sem_validOrderCount
				semPixel_TSA.setParam("discountAmount", semPixelData.get("discountAmountND")); // sem_totalDiscountAmount
				%><fd:SemPixelIncludeMedia pixelNames="TheSearchAgency" />

        <%
        /* Digo2 Pixel */
				SemPixelModel semPixel_DIGO2 = FDSemPixelCache.getInstance().getSemPixel("DiGo2");
				semPixel_DIGO2.clearParams();
	
				semPixel_DIGO2.setParam("checkout_receipt", "true");
				semPixel_DIGO2.setParam("subtotal", semPixelData.get("subtotal")); // sem_cartSubtotal
				semPixel_DIGO2.setParam("orderId", semPixelData.get("orderId")); // sem_orderNumber
				semPixel_DIGO2.setParam("totalCartItems", ((Integer)semPixelData.get("totalCartItems")).toString()); // sem_totalCartItems
				semPixel_DIGO2.setParam("productId", semPixelData.get("productId")); // sem_productId
				%><fd:SemPixelIncludeMedia pixelNames="DiGo2" />
				
		 <%
        /* Pinterest Pixel */
				SemPixelModel pinterestPixel = FDSemPixelCache.getInstance().getSemPixel("Pinterest");
		        pinterestPixel.clearParams();
	
		        pinterestPixel.setParam("checkout_receipt", "true");
		        pinterestPixel.setParam("subtotal", semPixelData.get("subtotal")); // sem_cartSubtotal
		        pinterestPixel.setParam("orderId", semPixelData.get("orderId")); // sem_orderNumber
		        pinterestPixel.setParam("totalCartItems", ((Integer)semPixelData.get("totalCartItems")).toString()); // sem_totalCartItems
		        pinterestPixel.setParam("productId", semPixelData.get("productId")); // sem_productId
				%><fd:SemPixelIncludeMedia pixelNames="Pinterest" />
				
	
		 <%
        /* Kenshoo Pixel */
				SemPixelModel kenshooPixel = FDSemPixelCache.getInstance().getSemPixel("Kenshoo");
		 		kenshooPixel.clearParams();
	
		 		kenshooPixel.setParam("checkout_receipt", "true");
		 		kenshooPixel.setParam("subtotal", semPixelData.get("subtotal")); // sem_cartSubtotal
		 		kenshooPixel.setParam("orderId", semPixelData.get("orderId")); // sem_orderNumber
		 		kenshooPixel.setParam("totalCartItems", ((Integer)semPixelData.get("totalCartItems")).toString()); // sem_totalCartItems
		 		kenshooPixel.setParam("productId", semPixelData.get("productId")); // sem_productId
				%><fd:SemPixelIncludeMedia pixelNames="Kenshoo" />			
				
						
      </c:if>
    </c:if>

  </div>
  </tmpl:put>
</tmpl:insert>
