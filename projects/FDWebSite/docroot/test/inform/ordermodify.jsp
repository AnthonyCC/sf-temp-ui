<%@ page import="com.freshdirect.FDCouponProperties" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.EnumEStoreId" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter" %>
<%@ page import="com.freshdirect.common.context.MasqueradeContext" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" />
<%
	EnumEStoreId curEStore = user.getUserContext().getStoreContext().getEStoreId();
	int userLevelInt = user.getLevel();
	String userLevel = (userLevelInt == FDUserI.GUEST) ? "GUEST" : (userLevelInt == FDUserI.RECOGNIZED) ? "RECOGNIZED" : (userLevelInt == FDUserI.SIGNED_IN) ? "SIGNED_IN" : "Error, "+ Integer.toString(userLevelInt);

	//force refresh properties
	FDStoreProperties.forceRefresh();
	FDCouponProperties.forceRefresh();
	
	boolean isModifyOrder_ordermodify = (user.getShoppingCart() instanceof FDModifyCartModel);
	FDOrderAdapter modifyingOrder_ordermodify = FDUserUtil.getModifyingOrder(user);
	String modifyOrderId_ordermodify = FDUserUtil.getModifyingOrderId(user);
	FDSessionUser sessionUser = (FDSessionUser) user;
	MasqueradeContext masqueradeContext = user.getMasqueradeContext();
	String makeGoodFromOrderId = (masqueradeContext != null) ? masqueradeContext.getMakeGoodFromOrderId() : null;
%>
<tmpl:insert template="/common/template/no_nav_html5.jsp">
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="" includeSiteSearchLink="false" title="Informational: Order Modify"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='extraCss'>
		<jwr:style src="/assets/css/test/inform/ordermodify.css" media="all" />
	</tmpl:put>
	<tmpl:put name='jsmodules'>
		<%-- required for login --%>
		<%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	</tmpl:put>
	<tmpl:put name='extraJs'>
	
		<jwr:script src="/assets/javascript/test/inform/ordermodify.js" useRandomParam="false" />
		<script>
			(function(fd) {
				"use strict";
				var $ = fd.libs.$;
				fd.inform.ordermodify.test = {};

				$(document).ready(function() {
					fd.inform.ordermodify.test.init();

					/* use "\${}" for templates in jsps */
					//update would fire display...
					$('#wouldFireOnPageLoad').html(`
						\${(fd.user) ? fd.inform.ordermodify.fire : false}
						\${fd.inform.ordermodify.fire ? '' : '(reason(s):'} 
							\${(<%= isModifyOrder_ordermodify %>) ? '' : '[Not Modifying]'}
							\${(<%= userLevelInt == FDSessionUser.SIGNED_IN %>) ? '' : '[Not Signed In]'}
							\${(<%= sessionUser.isShowingInformOrderModify() %>) ? '' : '[Not showing]'}
							\${(<%= !(masqueradeContext != null) %>) ? '' : '[Masquerading]'}
							
							\${(fd.inform.ordermodify.test.viewCount >= 0 && <%= FDStoreProperties.getInformOrderModifyViewCountLimit() >= 0 %>) ? '' : '[View/Limit -1]'}
							\${(fd.inform.ordermodify.test.viewCount <= <%= FDStoreProperties.getInformOrderModifyViewCountLimit() %>) ? '' : '[View Limit Reached]'}
						\${fd.inform.ordermodify.fire ? '' : ')'} 
					`).addClass('updatetransition');
				});
			})(FreshDirect);
		</script>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<div id="toptoolbar">
			<% if (isModifyOrder_ordermodify && modifyingOrder_ordermodify != null) { %>
				<div id="locationbar">
					<%@ include file="/shared/template/includes/i_modifyorderbar.jspf" %>
				</div>
			<% } %>
			<%-- MASQUERADE bar --%>
			<% if (masqueradeContext != null) {	%>
				<div id="topwarningbar">
					You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user.getUserId()%> (Store: <%= user.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user.getUserContext().getFulfillmentContext().getPlantId() %>)
					<%if (makeGoodFromOrderId!=null) {%>
						<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
						(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
						<a class="imgButtonOrange" href="/cancelmakegood.jsp">Cancel MakeGood</a>
					<%}%>
				</div>
			<% } %>
			<div id="header">
				<h1 class="inblock"><span class="inverted">Informational</span>: Order Modify</h1>
				<div class="col rightActions">
					<% if (userLevelInt <= FDUserI.RECOGNIZED) { %>
						<button class="cssbutton green" fd-login-required="" fd-login-nosignup="" fd-login-successpage="/test/inform/ordermodify.jsp">Sign in</button>
					<% } %>
					<% if (userLevelInt == FDUserI.RECOGNIZED) { %>
						<button class="cssbutton green transparent signOutBtn">Sign out<div class="loader"></div></button>
					<% } %>
					<% if (userLevelInt == FDUserI.SIGNED_IN) { %>
						<button class="cssbutton green transparent signOutBtn">Sign out<div class="loader"></div></button>
						<button class="cssbutton red transparent killSessionBtn">Kill Session<div class="loader"></div></button>
						<%
							/* simple dropdown of modifiable orders */
							List<FDOrderInfoI> modifiableOrders = FDUserUtil.getModifiableOrders(user);
							if (modifiableOrders.size() > 0) { %>
								<div class="select-wrapper"><select id="modifiableOrders" class="customsimpleselect">
									<option value="-1">Modifiable Orders</option>
									<%
									for (Iterator<FDOrderInfoI> i = modifiableOrders.iterator(); i.hasNext();) {
										FDOrderInfoI o = i.next();
										%><option class="customsimpleselect" value="<%= o.getErpSalesId() %>" <%= (modifyOrderId_ordermodify!=null && modifyOrderId_ordermodify.equals(o.getErpSalesId())) ? "selected data-currentlyModifying" : "" %>><%= o.getErpSalesId() %></option><%
									}
								%></select></div>
								<button id="startModify" class="cssbutton orange">Modify<div class="loader"></div></button>
						<% } %>
					<% } %>
					<div>reloads page on success</div>
				</div>
			</div>
		</div>
		<hr />
		<div class="subsection">
			<div>
				<h2 class="inblock">User Data</h2>
				<div class="col rightActions">
				</div>
			</div>
			<div class="col col50p"><%-- SECTION DATA --%>
				<div><strong>User EStore</strong>: <span id="eStoreId"><%= curEStore.getContentId() %></span></div>
				<div><strong>FDCustomer Id</strong>: <span id="fdCustid"><%= (userLevel.equals("GUEST")) ? userLevel : user.getFDCustomer().getId() %></span><%= (userLevel.equals("GUEST")) ? "" : " <strong>("+user.getUserId()+" ["+userLevel+"])</strong>" %></div>
				<div><strong>View Count</strong>: <span id="viewCount"><%= user.getInformOrderModifyViewCount(curEStore, false) %></span></div>
				<div><strong>Is Showing</strong>: <span id="show"><%= sessionUser.isShowingInformOrderModify() %></span></div>
				<div><strong>Fire on page load?</strong>: <span id="wouldFireOnPageLoad">[UNKNOWN]</span></div>
			</div>
			<div class="col col50p"><%-- NOTES ON SECTION --%>
				Only a <strong>SIGNED_IN</strong> user can use the functionality. <strong>GUEST</strong> or <strong>RECOGNIZED</strong> should receive error values (<strong>View Count</strong>: -1)<br />
				<strong>Is Showing</strong> will only be true when modifying (or manually setting <strong>seen</strong>: false)<br />
				<strong>Fire on page load</strong> is only calculated on page load<br />
			</div>
			<br class="clear" />
		</div>
		<div class="subsection">
			<h2>Properties</h2>
			<div class="col col50p"><%-- SECTION DATA --%>
				<div><strong>Enabled</strong>: fdstore.inform.ordermodify.enabled=<span id="enabled"><%= FDStoreProperties.isInformOrderModifyEnabled() %></span> <strong>(Default: false)</strong></div>
				<div><strong>View Count Limit</strong>: fdstore.inform.ordermodify.viewCountLimit=<span id="viewCountLimit"><%= FDStoreProperties.getInformOrderModifyViewCountLimit() %></span> <strong>(Default: 5)</strong></div>
				<div><strong>Media Path</strong>: fdstore.inform.ordermodify.mediaPath=<span id="mediaPath"><%= FDStoreProperties.getInformOrderModifyMediaPath() %></span> <strong>(Default: "")</strong></div>
			</div>
			<div class="col col50p"><%-- NOTES ON SECTION --%>
				All props are in <strong>fdstore.properties</strong><br />
				This page refreshes the properties when loaded<br />
				The <strong>enabled</strong> property does not have to be true to use this page
			</div>
			<br class="clear" />
		</div>
		<div class="subsection">
			<h2>API</h2>
			<div class="col col50p"><%-- SECTION DATA --%>
				<div><strong>API Path</strong>: <span id="apiPath">/api/informational</span></div>
				<div><strong>Payload</strong>: <textarea id="apiPayload"></textarea></div>
				<div><strong>Result</strong>: <textarea id="apiPostResult"></textarea></div>
			</div>
			<div class="col col50p"><%-- NOTES ON SECTION --%>
				Payload Templates:
				<div class="rightActions">ctrl+leftClick to set and GET/POST payload</div> 
				<div class="horizSep"></div>
				
				<button id="" class="cssbutton small green transparent apiPayloadTemplate" data-template="DEFAULT" data-template-method="GET">default</button>: proper payload for a GET<br />
				<br />
				<button id="" class="cssbutton small green apiPayloadTemplate" data-template="MIN" data-template-method="POST">minimize</button>: set viewCount to 0, POST&nbsp;only<br />
				<br />
				<button id="" class="cssbutton small green apiPayloadTemplate" data-template="MAX" data-template-method="POST">maximize</button>: set viewCount to viewCountLimit, POST&nbsp;only<br />
				<br />
				<button id="" class="cssbutton small green apiPayloadTemplate" data-template="SEEN" data-template-method="POST">seen</button>: set seen value, POST&nbsp;only
					<button id="" class="cssbutton small green apiPayloadTemplate w75px" data-template="SEEN_TRUE" data-template-method="POST">true</button>
					<button id="" class="cssbutton small green apiPayloadTemplate w75px" data-template="SEEN_FALSE" data-template-method="POST"><strong>false</strong></button>
					<br />
				
				<div class="horizSep"></div>
				<div>
					<div class="col widthauto">
						<button id="apiPayloadGet" class="cssbutton green transparent">GET<div class="loader"></div></button>
						<button id="apiPayloadPost" class="cssbutton green">POST<div class="loader"></div></button>
					</div>
					<div class="col widthauto">
						<button id="apiForce" class="cssbutton red transparent">FORCE FIRE</button>: requires <strong>SIGNED_IN</strong>
					</div>
					<br class="clear" />
				</div>
			</div>
			<br class="clear" />
		</div>
		<div class="subsection">
			<h2>SQL</h2>
			<div class="col col70p"><%-- SECTION DATA --%>
				<div><textarea id="sqlSrc"></textarea></div>
			</div>
			<div class="col col30p"><%-- NOTES ON SECTION --%>
				<button id="sqlRefresh" class="cssbutton green transparent">Refresh</button>
			</div>
			<br class="clear" />
		</div>
		<div class="subsection">
			<h2>Media</h2>
			<div class="col col70p"><%-- SECTION DATA --%>
				<div><textarea id="mediaSrc"></textarea></div>
			</div>
			<div class="col col30p"><%-- NOTES ON SECTION --%>
				Media will only load if viewCount&nbsp;<=&nbsp;viewCountLimit
			</div>
			<br class="clear" />
		</div>
	</tmpl:put>
</tmpl:insert>