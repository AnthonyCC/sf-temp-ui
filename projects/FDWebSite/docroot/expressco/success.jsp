<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%  //--------OAS Page Variables-----------------------
  request.setAttribute("sitePage", "www.freshdirect.com/expressco/checkout/");
  request.setAttribute("listPos", "SystemMessage"); // TODO
%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<potato:singlePageCheckoutSuccess />
<tmpl:insert template='/expressco/includes/ec_template.jsp'>
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
    
     <!-- APPDEV-4287  Dstillery pixel -->
  <script type="text/javascript" async>
function asyncPixelWithTimeout() {
var img = new Image(1, 1);
img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26210&pcv=42';;
setTimeout(function ()
{ if (!img.complete) img.src = ''; /*kill the request*/ }

, 33);
};
asyncPixelWithTimeout();
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
            <soy:render template="expressco.paymentmethodpreview" data="${singlePageCheckoutSuccessPotato.payment}" />
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
    
    <%-- TODO: only for new/modified orders --%>
    <%
      /*
			String sem_validOrderCount = "0";
				sem_validOrderCount = Integer.toString(sem_user.getAdjustedValidOrderCount());
			double sem_checkCartSubtotal = 0;
			String sem_cartSubtotal = "0";
			DecimalFormat sem_df = new DecimalFormat("0.00");
				sem_cartSubtotal = NVL.apply((String)request.getAttribute("cartSubtotal"), "0").replace("$", "").replace(",","");
				sem_cartSubtotal = sem_df.format(Double.parseDouble(sem_cartSubtotal));
					
			String sem_totalDiscountAmount = "0";
				sem_totalDiscountAmount = NVL.apply((String)request.getAttribute("totalDiscountAmount"), "0").replace("$", "");
				sem_totalDiscountAmount = sem_df.format(Double.parseDouble(sem_totalDiscountAmount));
			boolean isOrderModify = Boolean.parseBoolean(NVL.apply((String)request.getAttribute("modifyOrderMode"), "false"));
			String sem_defaultCounty = sem_user.getDefaultCounty();
			String sem_totalCartItems = "0";
				sem_totalCartItems = NVL.apply((Integer)request.getAttribute("totalCartItems"), 0).toString();
      */
	
			/* Google AdWords Pixel */
			%><fd:SemPixelIncludeMedia pixelNames="GoogleAdWords" /><%
	
			/* CheetahMail Pixel */
			SemPixelModel semPixel_CM = FDSemPixelCache.getInstance().getSemPixel("CheetahMail");
	
			//add a param to the params sent to the FTL
			semPixel_CM.setParam("subtotal", "10"); // sem_cartSubtotal
			semPixel_CM.setParam("orderId", "12345"); // sem_orderNumber
			semPixel_CM.setParam("isOrderModify", "false"); // String.valueOf(isOrderModify)
			semPixel_CM.setParam("userCounty", "NY"); // sem_defaultCounty
			semPixel_CM.setParam("totalCartItems", "10"); // sem_totalCartItems
			%><fd:SemPixelIncludeMedia pixelNames="CheetahMail" /><%
		
			/* TheSearchAgency Pixel */
      /*
			if ( !isOrderModify ) { //do not send on order modify
      */
				//get ref to Pixel
				SemPixelModel semPixel_TSA = FDSemPixelCache.getInstance().getSemPixel("TheSearchAgency");
				
        /*
				sem_cartSubtotal = sem_cartSubtotal.replace(".", "");
				sem_totalDiscountAmount = sem_totalDiscountAmount.replace(".", "");
	
				//change triple zero ($0.00 -> 000) to single zero
				if ("000".equals(sem_cartSubtotal)) { sem_cartSubtotal = "0"; }
				if ("000".equals(sem_totalDiscountAmount)) { sem_totalDiscountAmount = "0"; }
        */
	
				//add a param to the params sent to the FTL
				semPixel_TSA.setParam("subtotal", "1000"); // sem_cartSubtotal
				semPixel_TSA.setParam("orderId", "12345"); // sem_orderNumber
				semPixel_TSA.setParam("validOrders", "10"); // sem_validOrderCount
				semPixel_TSA.setParam("discountAmount", "2"); // sem_totalDiscountAmount
				%><fd:SemPixelIncludeMedia pixelNames="TheSearchAgency" /><%
      /*
			}
      */
			

			/* Digo2 Pixel */
      /*
			if ( !isOrderModify ) { //do not send on order modify
      */
				SemPixelModel semPixel_DIGO2 = FDSemPixelCache.getInstance().getSemPixel("DiGo2");
				semPixel_DIGO2.clearParams();
	
				semPixel_DIGO2.setParam("checkout_receipt", "true");
				semPixel_DIGO2.setParam("subtotal", "10"); // sem_cartSubtotal
				semPixel_DIGO2.setParam("orderId", "12345"); // sem_orderNumber
				%><fd:SemPixelIncludeMedia pixelNames="DiGo2" /><%
      /*
      }
      */
		%>

  </div>
  </tmpl:put>
</tmpl:insert>
