<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.ProfileModel"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %> 
<%@ page import="com.freshdirect.fdstore.promotion.Promotion" %> 
<%@ page import="com.freshdirect.fdstore.promotion.SignupDiscountRule" %> 
<%@ page import="com.freshdirect.crm.CrmCustomerHeaderInfo" %>
<%@ page import="com.freshdirect.framework.core.PrimaryKey" %>

<%@ taglib uri="crm" prefix="crm" %>

<%
    String pageURI = request.getRequestURI();
    FDUserI user = CrmSession.getUser(session);
%>
<crm:GetCurrentAgent id="currentAgent">
<%  if (user != null && !currentAgent.isGuest() && user.getIdentity() != null) { %>
<% CrmCustomerHeaderInfo info = CrmSession.getCustomerHeaderInfo(session, user.getIdentity()); 
    ProfileModel profile = user.getFDCustomer().getProfile();
    boolean newOrder = false;
    if (pageURI.indexOf("/order/") > -1 && pageURI.indexOf("reverse_credit") < 0 && pageURI.indexOf("order_modify") < 0 && pageURI.indexOf("cancel_order") < 0 && pageURI.indexOf("payment_exception") < 0 && pageURI.indexOf("charge_order") < 0) {
        newOrder = true;
    }
    int validOrderCount=user.getAdjustedValidOrderCount() ;
    String label =  profile.isChefsTable() ? "chefs_club" : profile.isVIPCustomer() ? "vip" : validOrderCount >= 2 && validOrderCount <= 4 ? "newbie" : "" ;
%>
<table width="100%" cellpadding="0" cellspacing="0" class="cust_header_shell">
<tr><td>
    <div class="cust_header<%= info.isActive() ? "" : "_inactive" %>" <%= !"".equals(label) ? "style=\"padding-left:0px; padding-top:0px; padding-bottom:0px;\"" : "" %>>
        <table width="100%" cellpadding="0" cellspacing="0" class="cust_text">
            <tr valign="middle">
                <% if("chefs_club".equals(label)){ %>
                    <td width="25" class="<%=label%>"><img src="/media_stat/crm/images/chef.gif" width="18" height="17" border="0" hspace="4" vspace="2"></td>
				 <% } else if ("vip".equals(label)) { %>
                	<td width="25" class="<%=label%>"><img src="/media_stat/crm/images/vip.gif" width="24" height="15" border="0" hspace="4" vspace="4"></td>
				<% } else if("newbie".equals(label)){ %>
                    <td width="25" class="<%=label%>"><img src="/media_stat/crm/images/star.gif" width="17" height="17" border="0" hspace="5" vspace="2"></td>
                <% } %>
				<% if (info.isOnAlert()) {%>
				    <td width="25" class="<%=label%>"><img src="/media_stat/crm/images/alert.gif" width="20" height="19" border="0" hspace="0" vspace="0" alt="Customer on Alert status"></td>
                <% } %>
				<%
                String displayPhone = info.getHomePhone() != null ?  "Home #: "+info.getHomePhone().getPhone() : 
                    info.getBusinessPhone() != null ? "Business #: "+info.getBusinessPhone().getPhone() :
                    info.getCellPhone() != null ? "Cell #: " + info.getCellPhone().getPhone() : " Unknown #: None provided";
                %>
				<td><div class="<%=label%>" style="float:left"> <span class="<%=!"".equals(label)? label+"_":""%>cust_title"><%= info.getFirstName() %> <%= info.getMiddleName() %> <%= info.getLastName() %></span> (ID: <%= user.getIdentity().getErpCustomerPK() %> | Type: <%=profile.getHouseholdType()%> | <%=displayPhone%> | <a href="mailto:<%=info.getEmail()%>" class="<%=label%>_link" name="email_link" onmouseover="return overlib('<%=info.getEmail()%>', AUTOSTATUS, WRAP, REF,'email_link',REFP,'LL');"
 onmouseout="nd();">Email</a>)&nbsp;</div><div style="float:left">
                <span class="<%=!"".equals(label)? label+"_":""%>cust_title">&nbsp;</span><span class="cust_header_field">Order<%= user.getAdjustedValidOrderCount() > 1 ? "s" : "" %>:</span> <b><%= user.getAdjustedValidOrderCount() %></b>
                &nbsp;<span class="cust_header_field">Phone:</span> <b><%= user.getValidPhoneOrderCount() %></b>
                &nbsp;<span class="cust_header_field">Web:</span> <b><%= user.getAdjustedValidOrderCount() - user.getValidPhoneOrderCount() %></b>
                &nbsp;<span class="cust_header_field">Dept. Credit<%=info.getCredits() > 1 ? "s" : "" %>:</span> <b><%=info.getCredits()%></b>
                &nbsp;<span class="cust_header_field">Case<%=(info.getCases() > 1)?"s":"" %>:</span> <b><%= info.getCases() %></b>
				</div>
                </td>
                <td align="right">
                <% if (info.isActive()) {
                    int promoValue = 0;
                %>
                    <% if (user.isFraudulent()) { %>
                        <span class="error">! <b>Matching Information</b> !</span><span class="cust_header_field">Ineligible for Promotion</span>
                    <% } else if (user.isEligibleForSignupPromotion()) {
                            Promotion promo = (Promotion) user.getEligibleSignupPromotion();
                            List rules = promo != null ? promo.getHeaderDiscountRules() : null;
                            promoValue = promo != null ? (int)promo.getHeaderDiscountTotal() : 0;
                            SignupDiscountRule rule = user.getSignupDiscountRule();
                        %>
                        
                        <% if (promoValue > 0) { %><span class="cust_header_field">Eligible for</span> <b>$<%= promoValue %></b> <span class="cust_header_field">promotion over</span> <b><%=rules.size()%></b> <span class="cust_header_field">order<%=rules.size() > 1 ? "s" : ""%>, next order:</span> <b>$<%=(int)rule.getMaxAmount()%></b> <span class="cust_header_field">off with</span> <b>$<%=(int)rule.getMinSubtotal()%></b> <span class="cust_header_field">purchase</span>
                        <% } %>
                    <% } 
                        double availCredit = info.getRemainingAmount();
                    %>
                    
                    <% if (availCredit > 0.0) { %>
						<%= promoValue > 0 || user.isFraudulent() ? " | " : "" %><span class="cust_header_field">Available store credit</span> <b><%= CCFormatter.formatCurrency(availCredit) %></b>
					<% } %>
                
                <% } else { %>
                    > <b>Account Deactivated</b> <
                <% } %>
                </td>
            </tr>
        </table>
    </div>
	</td></tr>
	<tr><td class="cust_nav">
        <a href="/main/summary.jsp" class="<%= pageURI.indexOf("summary.jsp") > -1  ? "cust_nav_tab_on" : "cust_nav_tab" %>">Summary</a>

        <a href="<%= response.encodeURL("/main/account_details.jsp") %>" class="<%= pageURI.indexOf("account_details.jsp") > -1 || pageURI.indexOf("/customer_account") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Account Details</a>
    
        <a href="<%= response.encodeURL("/main/case_history.jsp?action=searchCase") %>" class="<%= pageURI.indexOf("case_history.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Case History</a>

        <a href="<%= response.encodeURL("/main/order_history.jsp") %>" class="<%= pageURI.indexOf("order_history.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Order History</a>
        
        <a href="<%= response.encodeURL("/main/credit_history.jsp") %>" class="<%= pageURI.indexOf("credit_history.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Credit History</a>
                
        <a href="<%= response.encodeURL("/main/promotion_history.jsp") %>" class="<%= pageURI.indexOf("promotion_history.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Promotion History</a>
        
		<a href="<%= response.encodeURL("/order/place_order_build.jsp") %>" class="<%= newOrder ? "cust_nav_tab_on" : "cust_nav_tab" %>">New Order</a>

		<a href="<%= response.encodeURL("/main/delivery_available_slots.jsp") %>" class="<%= pageURI.indexOf("available_slots.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Available Delivery Slots</a>

        <a href="<%= response.encodeURL("/main/activity_log.jsp") %>" class="<%= pageURI.indexOf("activity_log") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Activity Log</a>

        <a href="<%= response.encodeURL("/main/referral_history.jsp") %>" class="<%= pageURI.indexOf("referral_history.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Referral History</a>
	<%
		//if (currentAgent.isSupervisor()) {
	%>
        <a href="<%= response.encodeURL("/main/profile_list.jsp") %>" class="<%= pageURI.indexOf("profile_list.jsp") > -1 ? "cust_nav_tab_on" : "cust_nav_tab" %>">Profile List</a>
	<%
		//}
	%>  
    
    <a href="<%= response.encodeURL("/gift_card/giftcard_summary.jsp") %>" class="<%= pageURI.indexOf("gift_card") > -1  ? "cust_nav_tab_on" : "cust_nav_tab" %>">GiftCard</a>
	<a href="<%= response.encodeURL("/main/delivery_pass.jsp") %>" class="<%= pageURI.indexOf("delivery_pass.jsp") > -1  ? "cust_nav_tab_on" : "cust_nav_tab" %>">Delivery Pass</a>
    <a href="<%= response.encodeURL("/customerprofile/customer_profile_summary.jsp") %>" class="<%= pageURI.indexOf("customer_profile") > -1  ? "cust_nav_tab_on" : "cust_nav_tab" %>">Customer Profile</a>
	</td></tr>
</table>
<%  } %>
</crm:GetCurrentAgent>
