<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.customer.ejb.EnumCustomerListType"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.framework.util.DateUtil"%>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />
<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_SO3_DETAIL %>"/>

<tmpl:insert template='/quickshop/includes/standing_order.jsp'>

  <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="Freshdirect-StandingOrder" pageId="standing_orders"></fd:SEOMetaTag>
  </tmpl:put>

  <tmpl:put name="soytemplates"><soy:import packageName="standingorder"/><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name='soSelected'>selected</tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <jwr:script src="/expressco.js" useRandomParam="false" />

  </tmpl:put>
  <tmpl:put name='content' direct='true'>
    	
    	<% if (user.isEligibleForStandingOrders()) { %>
		    <% if (user.isCustomerHasStandingOrders()) { %>
				<div class="standing-orders-3">
					<%@ include file="/quickshop/includes/manage_standing_orders.jspf" %>
				</div>
		    <% } else { %>
				<div class="standing-orders-3">
					<%@ include file="/quickshop/includes/first_standing_order.jspf" %>
				</div>
		    <% } %>
		    <% if(user.isSoFeatureOverlay()){ %>
 				<script>setTimeout(function(){ doOverlayDialogNew("/quickshop/includes/feature_change_tutorial.jsp"); }, 1000);</script>
 			<% } %>
	   	<%  } %>
    	
    <script>
      // potato loading 

    </script>
    
    </tmpl:put>

  <tmpl:put name="extraCss">
    <jwr:style src="/timeslots.css" media="all" />
    <jwr:style src="/expressco.css" media="all" />
  </tmpl:put>
  
	
  <tmpl:put name="extraJs">
  	<jwr:script src="/protoscriptbox.js" useRandomParam="false" />
    <fd:javascript src="/assets/javascript/timeslots.js" />
    <%@ include file="/shared/template/includes/ccl.jspf" %> <%-- FIXME: absolute horror, currently needed for some functionality on standing orders pages, an extreme cleanup or replacement would be essential here ... --%>
  </tmpl:put>

</tmpl:insert>