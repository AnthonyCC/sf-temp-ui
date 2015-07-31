<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<%  //--------OAS Page Variables-----------------------
  request.setAttribute("sitePage", "www.freshdirect.com/expressco/view_cart/");
  request.setAttribute("listPos", "SystemMessage"); // TODO
%>

<fd:CheckLoginStatus />
<potato:pendingExternalAtcItem/>
<potato:cartData />
<tmpl:insert template='/expressco/includes/ec_template.jsp'>
  <tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <jwr:script src="/expressco.js" useRandomParam="false" />

 <!-- APPDEV-4287  Dstillery pixel -->
        <script type="text/javascript" async>
		function asyncPixelWithTimeout() {
		var img = new Image(1, 1);
		img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26209&pcv=49';
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

  <tmpl:put name="ecpage">view_cart</tmpl:put>

  <tmpl:put name="title">View Cart</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div class="container">
      <div id="warningmessage">
        <soy:render template="expressco.warningmessage" data="${cartDataPotato}" />
      </div>

      <div id="modifyorder">
        <soy:render template="expressco.modifyorder" data="${cartDataPotato}" />
      </div>

      <%-- cart content --%>
      <div id="cartcontent" fd-toggle="product-sample-carousel" class="view_cart" data-ec-linetemplate="expressco.viewcartlines">
      </div>
    </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${cartDataPotato}" noHeaders="true"/>
      window.FreshDirect.expressco.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>
    </script>
  </tmpl:put>
</tmpl:insert>
