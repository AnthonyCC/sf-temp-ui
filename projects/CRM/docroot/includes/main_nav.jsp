<%@	page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@	page import="com.freshdirect.crm.CrmAgentRole"%>
<%@	page import='com.freshdirect.fdstore.FDStoreProperties'	%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='java.util.*' %>
<%@	taglib uri='crm' prefix='crm' %>
<%@ include file="/includes/i_globalcontext.jspf" %>
<% String pageURI =	request.getRequestURI(); %>
<crm:GetCurrentAgent id="currentAgent">
<%
	CrmAgentRole crmRole = currentAgent.getRole();
	List linksList = MenuManager.getInstance().getLinksForRole(request);
	
	
%>
<%@ include file="/includes/context_help.jspf" %>
<a name="top"></a>
<table width="100%"	cellpadding="6"	cellspacing="0"	border="0" class="main_nav">
	<tr>
		<td	style="padding-left: 0px;">
			<a href="/main/clear_session.jsp" title="Home" class="<%=pageURI.indexOf("/main/main_index.jsp") > -1?"main_nav_on":"main_nav_link"%> ui-icon-cc ui-icon-cc-home"><span>Home</span></a><%
			if (linksList.contains("case_mgmt_index.jsp")) {
				%><a href="/case_mgmt/case_mgmt_index.jsp" class="<%=pageURI.indexOf("/case_mgmt/") > -1?"main_nav_on":"main_nav_link"%>">Manage<br />Cases</a><%
			} 
			if (linksList.contains("pending_credits_list.jsp")) { 
				%><a href="/supervisor/pending_credits_list.jsp<%= request.getParameter("agent_pk") != null ? "?agent_pk=" + request.getParameter("agent_pk") : ""%>" class="<%=pageURI.indexOf("/supervisor/") > -1?"main_nav_on":"main_nav_link"%>">Supervisor<br />Resources</a><%
			}
			if (linksList.contains("admintools_index.jsp")) { 
				%><a href="/admintools/admintools_index.jsp" class="<%=pageURI.indexOf("/admintools/") > -1?"main_nav_on":"main_nav_link"%>">Admin<br />Tools</a><%
			}
			
			boolean inTransOps = pageURI.indexOf("/transportation/") > -1 ? true : false;
			boolean inReports = pageURI.indexOf("/reports/") > -1 ? true : false;
			/* exception for order by route & stop, which is in transOps menu, but the jsp is in /reports/ */
			if (pageURI.indexOf("/reports/route_stop_report.jsp") > -1) {
				inTransOps = true;
				inReports = false;
			}
			
			if (linksList.contains("crmLateIssues.jsp")) {
				%><a href="/transportation/VSStatusLog.jsp" class="<%=inTransOps?"main_nav_on":"main_nav_link"%>">Transp.<br />Ops</a><%
			} 
			if (linksList.contains("reports_index.jsp")) {
				%><a href="/reports/reports_index.jsp" class="<%=inReports?"main_nav_on":"main_nav_link"%>">Reports</a><%
			}
			
			if (linksList.contains("worklist.jsp")) { 
				%><a href="/main/worklist.jsp" class="<%=pageURI.indexOf("/main/worklist.jsp") > -1?"main_nav_on":"main_nav_link"%>">Work<br />List</a><%
			}
			if (linksList.contains("promo_home.jsp")) { 
				%><a href="/main/promo_home.jsp" class="<%= (pageURI.indexOf("/main/promo_home.jsp") > -1 || (pageURI.indexOf("/promotion/") > -1 &&  pageURI.indexOf("/promo_ws") <= -1 && pageURI.indexOf("dow_admin") == -1) && pageURI.indexOf("promo_hronly.jsp") == -1)?"main_nav_on":"main_nav_link"%>">Promos</a><%
			}
			if (linksList.contains("promo_ws_view.jsp")) {
				%><a href="/promotion/promo_ws_view.jsp" class="<%=pageURI.indexOf("/promotion/promo_ws_view.jsp") > -1 || pageURI.indexOf("/promo_ws")	> -1 || pageURI.indexOf("dow_admin") != -1?"main_nav_on":"main_nav_link"%>">WS<br />Promos</a><%
			}
			if (linksList.contains("promo_hronly.jsp")){
				%><a href="/promotion/promo_hronly.jsp" class="<%=pageURI.indexOf("/promotion/promo_hronly.jsp") > -1?"main_nav_on":"main_nav_link"%>">HR<br />Promos</a><%
			} 
			if (linksList.contains("giftcard_landing.jsp")) {
				%><a href="/gift_card/giftcard_landing.jsp" class="<%= (pageURI.indexOf("/main/information.jsp") > -1 || pageURI.indexOf("/gift_card/giftcard_landing.jsp") > -1) ?"main_nav_on":"main_nav_link"%>">Gift<br />Cards</a><%
			}
			if (linksList.contains("crm_standing_orders.jsp")) { 
				%><a href="/main/crm_standing_orders.jsp" class="<%=(pageURI.indexOf("/main/crm_standing_orders.jsp") > -1 || pageURI.indexOf("crm_standing_orders_alt_dates.jsp") != -1  || pageURI.indexOf("crm_standing_orders_sku_replacement.jsp") != -1 )?"main_nav_on":"main_nav_link"%>">Standing<br />Orders</a><% 
			}
			if (linksList.contains("ppicks_email_products.jsp")) { 
				%><a href="/main/masquerade.jsp?destination=product_promos" target="_blank" class="<%=pageURI.indexOf("/main/ppicks_email_products.jsp") > -1?"main_nav_on":"main_nav_link"%>">Product<br />Promos</a><%
			} 
			if (linksList.contains("coupon_savings_history.jsp")) {
				%><a href="/main/masquerade.jsp?destination=coupon_savings_history" target="_blank" class="<%=pageURI.indexOf("/main/coupon_savings_history.jsp") > -1?"main_nav_on":"main_nav_link"%>">Coupon&nbsp;Savings<br />History</a><% 
			}
			if (linksList.contains("address_scrubbing_tool.jsp")) {
				%><a href="/scrubbingtool/address_scrubbing_tool.jsp" class ="<%=pageURI.indexOf("/scrubbingtool/address_scrubbing_tool.jsp") > -1?"main_nav_on":"main_nav_link"%>" >Address <br /> Scrubbing&nbsp;Tool</a><% 
			}
			%><a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmMainHelpLink() %>','715','940','kbit')" title="Help" class="<%=pageURI.indexOf("/main/help.jsp") > -1?"main_nav_on":"main_nav_link"%> ui-icon-cc ui-icon-cc-help"><span>Help</span></a>
		</td>
		<td align="right" width="350">
			<div style="white-space: nowrap;">			
				Store:&nbsp;
				<%
				%>
				<select id="globalContext-Store"  <%= (crmRole.isRoleFDX(crmRole)) ? "disabled" : ""%>>
					<option <%= ((globalContextStore).equalsIgnoreCase("All"))?"selected":"" %> value="All">All</option>
					<option <%= ((globalContextStore).equals(EnumEStoreId.FD.name()))?"selected":"" %> value="<%= EnumEStoreId.FD.name() %>"><%= EnumEStoreId.FD.toString() %></option>
					<option <%= ((globalContextStore).equals(EnumEStoreId.FDX.name()))?"selected":"" %> value="<%= EnumEStoreId.FDX.name() %>"><%= EnumEStoreId.FDX.toString() %></option>
				</select>
				
				Facility:&nbsp;
				<select id="globalContext-Facility" style="width: 200px" <%= (crmRole.isRoleFDX(crmRole)) ? "disabled" : ""%>>
					<option <%= ("All".equals(globalContextFacility))?"selected":"" %> value="All"> All </option>
					<option <%= ("1000".equals(globalContextFacility))?"selected":"" %> value="1000"> 1000 </option>
					<option <%= ("1300".equals(globalContextFacility))?"selected":"" %> value="1300"> 1300 </option>
					<option <%= ("1310".equals(globalContextFacility))?"selected":"" %> value="1310"> 1310 </option>
				</select>
			</div>
			
			<div><%=currentAgent.getRole().getName()%>: <b><%=currentAgent.getLdapId()%></b></div>
		</td>
		<td align="right" width="79">
			<%-- if (!crmRole.isRoleFDX(crmRole)) { --%>
			<a href="javascript:popResize('http://www.freshdirect.com','715','1060','freshdirect')" title="Website (FD)"  class="main_nav_link ui-icon-cc ui-icon-cc-fd"><span>Open Website</span></a>
			<%-- } --%>
			<%-- <a href="javascript:popResize('http://www.freshdirect.com','715','940','freshdirect')" title="Website (FDx)"  class="main_nav_link ui-icon-cc ui-icon-cc-fdx"><span>Open Website</span></a> --%>
			<a href="/main/logout.jsp" class="main_nav_link ui-icon-cc ui-icon-cc-logoff" title="Log Out"><span>Logout</span></a>
		<!-- 
			&middot;
			<a href="javascript:popResize('http://www.freshdirect.com','715','940','freshdirect')"><img	src="/media_stat/crm/images/fd_icon.gif" width="13"	height="14"	border="0" alt="FreshDirect"></a>
		-->
	</tr>
</table>
</crm:GetCurrentAgent>
