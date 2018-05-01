<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>
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
		(function (fd) {
			if(fd.expressco && fd.expressco.data && fd.expressco.data.redirectUrl){
				fd.common.dispatcher.signal('redirectUrl', fd.expressco.data.redirectUrl);
			}
		}(FreshDirect));
	</script>
    
  </tmpl:put>

  <tmpl:put name="globalnav">
    <%@ include file="/common/template/includes/globalnav.jspf" %>
  </tmpl:put>

  <tmpl:put name="bottomnav">
    <%@ include file="/common/template/includes/footer.jspf" %>
  </tmpl:put>

  <tmpl:put name="ecpage">success</tmpl:put>

  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="Success" pageId="ec_success"></fd:SEOMetaTag>
  </tmpl:put>

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
	

  <div id='successpage'>
      <div class="container">
      	<% if(FDStoreProperties.isDlvPassStandAloneCheckoutEnabled() && order.getOrderType().equals(EnumSaleType.SUBSCRIPTION)){ %>
	  		<div class="dpn-content">
	        	<%@ include file="/includes/deliverypasssuccess.jsp" %>
	        </div>
  		<% } else { %>
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
          	<div class="drawer__previewitem">
            	<soy:render template="expressco.timeslot" data="${singlePageCheckoutSuccessPotato.timeslot}" />
            </div>
          </div>
          <div data-drawer-default-content="payment">
            <soy:render template="expressco.paymentmethodpreviewNew" data="${singlePageCheckoutSuccessPotato.payment}" />
          </div>
        </div>

        <%-- cart content --%>
        <h2 class="inyourorder">In Your Order</h2>
        <div id="cartcontent" data-ec-linetemplate="expressco.successlines" data-drawer-disabled data-ec-request-uri="/api/expresscheckout/cartdata?orderId=${param['orderId']}">

        </div>
		<% } %>
      </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutSuccessPotato}" noHeaders="true"/>
    </script>

  </div>
  </fd:GetOrder>
  <% } %>
  </tmpl:put>
</tmpl:insert>
