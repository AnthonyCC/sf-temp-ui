<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.MyFD"%>
<%@page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@page import="com.freshdirect.fdstore.myfd.poll.PollDaddyService"%>
<%@taglib uri='template' prefix='tmpl'%>
<%@taglib uri='logic' prefix='logic'%>
<%@taglib uri='freshdirect' prefix='fd'%>
<% //expanded page dimensions
final int W_MYFD_TOTAL = 970;
final int W_MYFD_MAIN = 724;
final int W_MYFD_LEFT = 232; /* 6 columns */
%>
<fd:CheckLoginStatus id="user" guestAllowed="false" />
<%
	//--------OAS Page Variables-----------------------
    request.setAttribute("sitePage", "www.freshdirect.com/myfd");
    request.setAttribute("listPos", "SystemMessage,MyFDRight");
%><%
	// Cart'n'Tabs posts these parameters to the FDShoppingCart tag
	String successPage = request.getParameter("fdsc.succpage");
	String actionName = request.getParameter("fdsc.action");
	if (actionName == null) {
		actionName = "updateQuantities";
	}
	String cartSource = request.getParameter("fdsc.source");
	
%><%
	// MyFD content fabrication
	MyFD myfd = MyFD.getMyFDInstance();
	
	if (!FDStoreProperties.isMyfdEnabled() || myfd == null) {
		response.sendRedirect("/index.jsp");
		return;
	}
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - MyFD</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<%-- MyFD header --%>
		<div>
		<%@ include file="/includes/myfd/i_myfd_header.jspf" %>
		</div>
		<%-- MyFD content --%>
		<fd:FDShoppingCart id='cart' action='<%= actionName %>'
			result='result' successPage="<%= successPage %>"
			source="<%= cartSource %>">
		<div style="padding: 30px 0;">
      <table cellpadding="0" cellspacing="0" style="width: <%= W_MYFD_TOTAL %>px; text-align: center; margin: 0px auto;">
				<tr>
          <td style="width: <%= W_MYFD_MAIN + 14 %>px; text-align: left; vertical-align: top;">
            <div style="width: <%= W_MYFD_MAIN %>px; margin-right: 14px; overflow: hidden;">
							<div>
							<%@ include file="/includes/myfd/i_myfd_greetings.jspf" %>
							</div>
							<div style="padding-top: 30px;">
							<%@ include file="/includes/myfd/i_myfd_blogs.jspf" %>
							</div>
							<div style="padding-top: 30px;">
							<%@ include file="/includes/myfd/i_myfd_editorial_main.jspf" %>
							</div>
						</div>
					</td>
          <td style="width: <%= W_MYFD_LEFT %>px; text-align: left; vertical-align: top;">
            <div style="width: <%= W_MYFD_LEFT %>px; overflow: hidden; margin: 0 auto;">
							<% if (PollDaddyService.getFirstOpenPoll() != null) { %>
							<div style="padding-bottom: 15px;">
							<%@ include file="/includes/myfd/i_myfd_poll.jspf" %>
							</div>
							<% } %>
							<div style="padding-bottom: 10px;">
							<%@ include file="/includes/myfd/i_myfd_quickshop.jspf" %>
							</div>
							<div style="border-top: 1px dashed gray; padding-bottom: 25px; font-size: 0px;">&nbsp;</div>
			                <% if (FDStoreProperties.isAdServerEnabled()) { %>
							<div style="padding-bottom: 25px;">
			                    <script type="text/javascript">
			                    OAS_AD("MyFDRight");
			                    </script>                    
							</div>
			                <% } %>
							<div>
							<%@ include file="/includes/myfd/i_myfd_editorial_side.jspf" %>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<%-- Cart'n'Tabs --%>
			<a name="cartRec"></a>
			<div style="padding-top: 1em"><%-- display items just added to cart  --%>
			<%
				if (cart.getRecentOrderLines().size() > 0 && "1".equals(request.getParameter("confirm"))) {
			%><%@ include file="/includes/smartstore/i_recent_orderlines.jspf"%>
			<br />
			<%
				}
			%>
			</div>
			<%
				String smartStoreFacility = "myfd";
			%>
			<fd:TabLook><%@ include file="/includes/smartstore/i_recommender_tabs.jspf" %></fd:TabLook>
			<fd:TabLook look="flat"><%@ include file="/includes/smartstore/i_flat_tabs.jspf" %></fd:TabLook>
		</fd:FDShoppingCart>
	</tmpl:put>
</tmpl:insert>
