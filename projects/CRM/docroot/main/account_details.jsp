<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodModel"%>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType"%>
<%@ page import="com.freshdirect.payment.fraud.RestrictedPaymentMethodModel"%>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting"%>
<%@ page import="com.freshdirect.customer.EnumUnattendedDeliveryFlag"%>
<%@ page import="com.freshdirect.common.address.PhoneNumber"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ page import="com.freshdirect.delivery.EnumRestrictedAddressReason"%>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.crm.CrmCaseTemplate" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"  %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager" %>
<%@ page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil" %>
<%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature" %>
<%@ page import='java.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<crm:GetFDUser id="user">
<%
CrmSession.getSessionStatus(session).setSaleId(null);
//request.setAttribute("bodyOnLoad","setInterval('blinkIt()',500)");
boolean forPrint = "print".equalsIgnoreCase(request.getParameter("for"));
String tmpl = "/template/" + (forPrint ? "print" : "top_nav") + ".jsp";
String actionName = request.getParameter("actionName");
String successPage = "resubmitCustomer".equals(actionName) ? "/main/resubmit_success.jsp" : null;
boolean saveCart = request.getParameter("saveCart") == null ? false : true ;
%>
<script type="text/javascript">
function blinkIt() {
 if (!document.all) return;
 else {
   for(i=0;i<document.all.tags('blink').length;i++){
      s=document.all.tags('blink')[i];
      s.style.visibility=(s.style.visibility=='visible')?'hidden':'visible';
   }
 }
}
</script>
<crm:CrmPaymentMethodController actionName="<%=actionName%>" result="ccResult">
<crm:CrmAddressController actionName="<%=actionName%>" result="adResult">
<% boolean editable = false; %>

<crm:GetErpCustomer id="customer" user="<%=user%>">
<crm:GetFdCustomer id="fdCustomer" user="<%=user%>">
<fd:AccountActivity activities='activities'>
<crm:GetCurrentAgent id="agent">
<% ErpCustomerInfoModel custInfo = customer.getCustomerInfo();%> 

<%!
private final static Map ACTIVITY_COMPARATORS = new HashMap();
static {
    ACTIVITY_COMPARATORS.put("date", ErpActivityRecord.COMP_DATE);
}

private ErpActivityRecord getLatest(List activities, EnumAccountActivityType type) {
    Set s = new HashSet();
    s.add(type);
    return getLatest(activities, s, null);
}

private ErpActivityRecord getLatest(List activities, EnumAccountActivityType type, String pk) {
    Set s = new HashSet();
    s.add(type);
    return getLatest(activities, s, pk);
}

private ErpActivityRecord getLatest(List activities, Set types, String pk) {
    ErpActivityRecord mostRecent = null;
    for (Iterator it = activities.iterator(); it.hasNext(); ) {
        ErpActivityRecord currentEntry = (ErpActivityRecord) it.next();
        if (pk != null && !"".equals(pk) && currentEntry.getNote() != null && !"".equals(currentEntry.getNote()) ) {
            if (currentEntry.getNote().indexOf(pk) > -1) {
                mostRecent = currentEntry;
                return mostRecent;
            } else continue;
        } else if (types.contains(currentEntry.getActivityType())) {
            mostRecent = currentEntry;
            return mostRecent;
        }
    }
    return null;
}

private String getLastModified(ErpActivityRecord mostRecent) {
    StringBuffer lastModified = new StringBuffer();
    
    lastModified.append(" <div class=\"cust_module_footer\">Last Modified: ");
    lastModified.append(CCFormatter.formatDateTime(mostRecent.getDate()));
    lastModified.append(" by ");
    lastModified.append(mostRecent.getInitiator());
    lastModified.append("</div>");
    
    return lastModified.toString();
}

%>          
<%
JspTableSorter sort = new JspTableSorter(request);
Collections.sort(activities, new ReverseComparator(ErpActivityRecord.COMP_DATE));

int numCreditCards = 0;
int numEChecks = 0;
int ccNum = 0;
int ecNum = 0;

%>

<crm:GetLockedCase id="cm">
    <% if (cm!=null && cm.getCustomerPK()!=null) { 
        String erpCustId = cm.getCustomerPK().getId();
            if (user.getIdentity().getErpCustomerPK().equals(erpCustId)) {
                editable = true;
            } 
        } %>
</crm:GetLockedCase>
<% 
String case_required = "<span class=\"cust_module_content_edit\">-Case required to edit-</span>"; 
String case_required_add = "<span class=\"cust_module_content_edit\">Case required to add</span>";
%>
<tmpl:insert template="<%=tmpl%>">

    <tmpl:put name='title' direct='true'>Account Details</tmpl:put>

    <tmpl:put name='content' direct='true'>
    <% if (forPrint) { %>
        <div class="sub_nav">Customer: <b><%= custInfo.getFirstName() %> <%= custInfo.getMiddleName() %> <%= custInfo.getLastName() %></b> &middot; ID: <b><%= user.getIdentity().getErpCustomerPK() %></b> &middot; Email: <%= custInfo.getEmail() %><br><span class="cust_header_field">Order<%= user.getAdjustedValidOrderCount() > 1 ? "s" : "" %>:</span> <b><%= user.getAdjustedValidOrderCount() %></b>
&nbsp;&nbsp;<span class="cust_header_field">Phone:</span> <b><%= user.getValidPhoneOrderCount() %></b>
&nbsp;&nbsp;<span class="cust_header_field">Web:</span> <b><%= user.getAdjustedValidOrderCount() - user.getValidPhoneOrderCount() %></b>
&nbsp;&nbsp;<span class="cust_header_field">Credit<%=customer.getCustomerCredits().size() > 1 ? "s" : "" %>:</span> <b><%=customer.getCustomerCredits().size()%></b>
<%
                CrmCaseTemplate template = new CrmCaseTemplate();
                template.setCustomerPK( new PrimaryKey(user.getIdentity().getErpCustomerPK()) );
                CrmSession.findCases(session, template);
                %>
                &nbsp;&nbsp;<span class="cust_header_field">Case<%= (template.getTotalRows() > 1) ? "s" : "" %>:</span> <b><%= template.getTotalRows() %></b>
</div>
    <% } %>
        <table width="100%" cellpadding="0" cellspacing="0" class="content_fixed">
		<tr><td>
        <%  ErpActivityRecord acctOpenActivity = null;
            for (Iterator it = activities.iterator(); it.hasNext(); ) {
                ErpActivityRecord rec = (ErpActivityRecord) it.next();
                if (EnumAccountActivityType.CREATE_ACCOUNT.equals(rec.getActivityType())) {
                    acctOpenActivity = rec;
                }
            }
        %>
			<%-- IF ALERT IMPLEMENTED --%>
			<div class="cust_sub_nav"><%if(editable){%><a href="<%= response.encodeURL("/main/customer_alert_list.jsp") %>" class="cust_sub_nav_text">Edit Alerts</a><%} else {%><b>Edit Alerts</a></b><%=case_required%><%}%></div>
			<%-- IF ALERT IMPLEMENTED --%>
			
			<div class="cust_sub_nav"><b><% if ( customer.isActive() ) { %>Active<% } else { %>Deactivated<% } %></b> <%if(editable){%><a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp") %>" class="cust_sub_nav_text"><% if ( customer.isActive() ) { %>Deactivate<% } else { %>Activate<% } %></a><%} else {%><%=case_required%><%}%></div>

            <div class="cust_sub_nav">Available Store Credit: <b><%= CCFormatter.formatCurrency(customer.getCustomerCreditRemainingAmount()) %></b></div>
            <% boolean promoEnabled = !user.isFraudulent(); %>
            <div class="cust_sub_nav">
                <b><%= promoEnabled ? "<span class='yes'>Eligible" : "<span class='no'>Ineligible" %></span> for Promotion</b><% if (!promoEnabled) { %> (Matching Information)<% } %><% if (!forPrint) { %>&nbsp;&nbsp;<%if (editable) {%><a href="/customer_account/promo_eligibility.jsp?successPage=<%= request.getRequestURI() %>" class="cust_sub_nav_text">Edit Eligibility</a><%} else {%><%=case_required%><%}%><% } %>
            </div>
            <div class="cust_sub_nav">
                Created: <b><%= acctOpenActivity == null ? "<span class='not_set'>- No date -</span>" : CCFormatter.formatDate(acctOpenActivity.getDate()) %></b>
            </div>
            
            <% if(customer.getSapId() == null && agent.isSupervisor()) { %> 
                <crm:ResubmitCustomerController actionName="<%=actionName%>" result="resubmitResult" customerId="<%=user.getIdentity().getErpCustomerPK()%>" successPage="<%= successPage %>"/>
                <form method='POST' action='<%=request.getRequestURI() + "?" + request.getQueryString()%>' name='resubmitCustomer' style="padding: 0; margin: 0">
                    <input type='hidden' name='actionName' value='resubmitCustomer'>
                    <div class="cust_sub_nav" style="width: 150x;"><a href="javascript:document.resubmitCustomer.submit()"><b>Resubmit Customer</b></a></div>
                </form>
            <% } %>
            <%if (!forPrint){%><div class="cust_sub_nav"><a href="javascript:popResize('/main/account_details.jsp?for=print','600','680')"><b>Print Version</b></a></div>
            <div class="cust_sub_nav"><a href="http://www.usps.com/zip4/" target="usps" class="home_search_module_field" title="USPS ZIP check" style="color:#003399; text-decoration:none;"><span style="color:#CC0000;">&raquo;</span> USPS</a></div><%}%>
            <% 
            	String referrerName = FDReferralManager.loadReferrerNameFromReferralCustomerId(user.getIdentity().getErpCustomerPK());
            	if (referrerName != null) {
            %>
            <div class="cust_sub_nav">
                Referred by: <b><%= referrerName%></b>
            </div>
            <% 	} %>
            <!-- SmartStore -->
            <div class="cust_sub_nav" title="SmartStore">
                <b>DYF</b>
<%
	String personal_variant_id = user.getFDCustomer().getProfile().getAttribute("DYF.VariantID");
	boolean isValid = SmartStoreUtil.checkVariantId(personal_variant_id, EnumSiteFeature.DYF);

	if (personal_variant_id == null) {
%>
                Variant: <b><%= SmartStoreUtil.getRecommendationService(user, EnumSiteFeature.DYF,null).getVariant().getId() %></b>
<%		
	} else {
%>
                Variant: <b><%= personal_variant_id %> <%= isValid ? "<i>(Overridden)</i>" : "<i>(Overridden, <span style=\"color: red;\">Invalid!</span>)</i>" %></b>
<%		
	}
%>
                Eligible: <b><%= DYFUtil.isCustomerEligible(user) ? "Yes" : "No" %></b>
            </div>
        </td></tr>
		</table>

		<% if ("no".equalsIgnoreCase(request.getParameter("cc"))) { %>
			<div class="content_fixed"><span class="error">Credit card information will be required upon checkout.</span></div>
		<% } %>
        <div class="<%= forPrint? "content_fixed" : "content_scroll"%>" style="padding-top: 0px; <%= !forPrint ? "height: 78%;" : ""%>" id="result">
			
		<%	if(saveCart) {
				((FDSessionUser) user).saveCart(true);%>
				 <span class="error_detail">Cart Successfully Saved</span><br>
		<%	}	
        %>
			
            <%-- 1st row --%>
            <div class="cust_module" style="width: 32%; margin-right: 4px;">
				<table width="100%" cellpadding="0" cellspacing="0" class="cust_module_header"><tr><td>Name & Contact Info</td></tr></table>
                <div class="cust_module_content">
                    <table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                        <tr>
                            <td align="right" width="40%" class="cust_module_content_note">Name:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getTitle()%> <%=custInfo.getFirstName()%>&nbsp;<%=custInfo.getMiddleName()%>&nbsp;<%=custInfo.getLastName()%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Home #:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getHomePhone()!=null?custInfo.getHomePhone().getPhone():""%><%=custInfo.getHomePhone()!=null && custInfo.getHomePhone().getExtension()!=null && !"".equals(custInfo.getHomePhone().getExtension()) ?" <span class=\"cust_module_content_note\">x</span>"+custInfo.getHomePhone().getExtension():""%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Work #:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getBusinessPhone()!=null?custInfo.getBusinessPhone().getPhone():""%><%=custInfo.getBusinessPhone()!=null && custInfo.getBusinessPhone().getExtension()!=null && !"".equals(custInfo.getBusinessPhone().getExtension()) ?" <span class=\"cust_module_content_note\">x</span>"+custInfo.getBusinessPhone().getExtension():""%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Cell #:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getCellPhone()!=null?custInfo.getCellPhone().getPhone():""%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Alt. Email:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getAlternateEmail()%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Dept/Division:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getWorkDepartment()%></td>
                        </tr>
                        <tr>
                            <td align="right" class="cust_module_content_note">Employee Id:&nbsp;&nbsp;</td>
                            <td><%=custInfo.getEmployeeId()%></td>
                        </tr>
                        <%if (!forPrint) {%>
                            <tr><td colspan="2" height="8"></td></tr>
                            <tr>
                                <td colspan="2" align="center"><%if(editable){%><a href="/customer_account/name_contact_info.jsp" class="edit">EDIT</a><%}else{%><%=case_required%><%}%></td>
                            </tr>
                        <%}%>
                    </table>
                </div>

                <% ErpActivityRecord lastCustomerInfo = getLatest(activities, EnumAccountActivityType.UPDATE_ERP_CUSTOMERINFO); 
                    if (lastCustomerInfo != null) { %>
                        <%= getLastModified(lastCustomerInfo) %> 
                <% } %>
            
            </div>

            <div class="cust_module" style="width: 32%; margin-right: 4px;">
				<table width="100%" cellpadding="0" cellspacing="0" class="cust_module_header"><tr><td>Username & Password</td></tr></table>
                <div class="cust_module_content">
				<% boolean hasNewsletterSubscription = custInfo.isReceiveOptinNewsletter(); %>
                    <table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                        <tr>
                            <td align="right" class="cust_module_content_note">Username:&nbsp;&nbsp;</td>
                            <td><%=customer.getUserId()%></td>
                        </tr>
                        <tr valign="top">
                            <td align="right" class="cust_module_content_note">Password Hint:&nbsp;&nbsp;</td>
                            <td><%=fdCustomer.getPasswordHint()%><br><span class="field_note">Town of birth or Mother's Maiden Name</span></td>
                        </tr>
                        <tr>
                            <td colspan="2"><hr class="gray1px"><b>Options:</b><%= !custInfo.isReceiveNewsletter() && !custInfo.isEmailPlaintext() ? " <span class=\"field_note\">Notification and email settings.</span>" : ""%>
							<%= custInfo.isReceiveNewsletter() ? "<br>&nbsp;&nbsp;&nbsp;Included on FD emails/newsletters" : ""%>
							<%= custInfo.isEmailPlaintext() ? "<br>&nbsp;&nbsp;&nbsp;Send TEXT-only email" : ""%>
							<%= hasNewsletterSubscription ? "<br>&nbsp;&nbsp;&nbsp;Subscribed to President\'s Picks newsletters" : "" %>
							</td>
                        </tr>
                        <%if (!forPrint) {%>
                            <tr><td colspan="2" height="8"></td></tr>
                            <tr>
                                <td colspan="2" align="center"><%if(editable){%><a href="/customer_account/username_password_option.jsp" class="edit">EDIT</a><%}else{%><%=case_required%><%}%></td>
                            </tr>
                        <%}%>
                    </table>
                </div>
                
                <% Set usernamePasswordActivity = new HashSet();
                usernamePasswordActivity.add(EnumAccountActivityType.CHANGE_PASSWORD);
                usernamePasswordActivity.add(EnumAccountActivityType.UPDATE_ERP_CUSTOMER);
                ErpActivityRecord lastUsernamePassword = getLatest(activities, usernamePasswordActivity, null); 
                if (lastUsernamePassword != null) { %>
                    <%= getLastModified(lastUsernamePassword) %> 
                <% } %>
                
            </div>

            <div class="cust_module" style="width: 32%; margin: 0px;">
              <table width="100%" cellpadding="0" cellspacing="0" class="cust_module_header"><tr><td>Affiliations</td></tr></table>
              <%
                int adjOrdrCnt = user.getAdjustedValidOrderCount();
                boolean isVIP = fdCustomer.getProfile().isVIPCustomer();
                boolean isChefsClub = fdCustomer.getProfile().isChefsTable();
                boolean isNewbie = adjOrdrCnt >= 2 && adjOrdrCnt <= 4;
              %>
                <div class="cust_module_content">
                    <table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                        <tr>
                            <td><b>Depot Info</b><br>
                                <%if(user.isDepotUser()){%>
                                    <fd:GetDepots id="depots">
                                    <logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
                                        <%if (depot.getDepotCode().equals(user.getDepotCode())){%>
                                            <%=depot.getName()%> 
                                        <%}%>
                                    </logic:iterate>
                                    </fd:GetDepots> 
                                <%}else{%>
                                    Not a Depot Member
                                <%}%>
                            </td>
                        </tr>
                        <%if (!forPrint) {%>
                            <tr>
                                <td align="center"><%if(editable){%><a href="/customer_account/depot_info.jsp" class="edit">EDIT</a><%}else{%><%=case_required%><%}%></td>
                            </tr>
                        <% } %>
                        <tr><td><hr class="gray1px"></td></tr>
                        <%if (!forPrint) {%>
						<% if (isChefsClub) { %>  
								<tr><td align="center">
									<table width="100%" class="chefs_club_label">
									<tr>
										<td width="30" align="center"><img src="/media_stat/crm/images/chef.gif" width="18" height="17" border="0" vspace="4"></td>
										<td class="chefs_club">Chef's Table member</td>
									</tr>
								</table>
								</td></tr>
						<% } %>
						
                        <% if (agent.isSupervisor() && !isVIP) { %>
                                <tr><td align="center">
                                <% if(editable){ %>
                                    <a href="/supervisor/vip_info.jsp" class="edit" style="width: 140px;">ADD VIP STATUS</a>
                                <% } else { %>
                                    Add VIP status<br>
                                    <%=case_required_add %>
                                <% } %>
                                </td></tr>
                            <% } %> 
                        <% } %>
                            
<% if (isVIP) { %> 
    <tr><td align="center">
        <table width="100%" class="vip_label">
            <tr>
                <td width="30" align="center"><img src="/media_stat/crm/images/vip.gif" width="24" height="15" border="0" vspace="5"></td>
                <td class="vip">VIP Customer</td>
                <td align="right">
                    <% if(editable){ %>
                        <a href="/supervisor/vip_info.jsp" class="edit">EDIT</a>
                    <% } else { %>
                        <%=case_required %>
                    <% } %>
                </td>
            </tr>
        </table>
    </td></tr>
<% }  %>
   
    <% if (isNewbie)  {   %>
        <tr><td align="center">
            <table width="100%" class="newbie_label">
            <tr>
                <td width="30" align="center"><img src="/media_stat/crm/images/star.gif" width="17" height="17" border="0" vspace="4"></td>
                <td class="newbie">New customer, <b><%=adjOrdrCnt%></b> order<%=adjOrdrCnt>1?"s":""%></td>
            </tr>
        </table>
        </td></tr>
<% } %>      
<%-- if (hasMetalRating)  {  % >
        <tr><td align="center"><hr class="gray1px">
			<table><tr><td><img src="/media_stat/crm/images/m_gold.gif" width="17" height="20" border="0"><img src="/media_stat/crm/images/m_silver.gif" width="17" height="20" border="0"><img src="/media_stat/crm/images/m_bronze.gif" width="17" height="20" border="0"></td><td><b>Gold Silver Bronze</b> Customer</td></tr></table>
		</td></tr>
< % } --%>      
                    </table>
                </div>         
            </div>

            <br clear="all"><%-- line break --%>

            <%-- 2nd row --%>
                <table width="100%" cellpadding="0" cellspacing="0" class="cust_full_module_header">
                    <tr>
                        <td class="cust_module_header_text">Delivery Addresses</td>
                        <td align="center">
                        <% if (user.isEligibleForPreReservation()) { 
                            FDReservation rsv = user.getReservation();
                                if (rsv != null) {%><span style="color:#FF6600;">&raquo;</span> Reserved timeslot: <b><%=CCFormatter.formatShortDlvDate(rsv.getStartTime())%>, <%=CCFormatter.formatTime(rsv.getStartTime())%> - <%=CCFormatter.formatDeliveryTime(rsv.getEndTime())%></b> <span style="color:#FF6600;">&laquo;</span>
                        <% }
                        } %>
                        <fd:ErrorHandler result="<%=adResult%>" name="case_not_attached" id='errorMsg'><br><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                        <td align="right"><%if (!forPrint) {%><%if(editable){%><a href="/customer_account/add_delivery_address.jsp" class="add">ADD</a><%}else{%><%=case_required_add%><%}%><%}%></td>
                    </tr>
                </table>
            <script language="javascript">
                function confirmDelete(thisForm,entryType) {
                    var doCancel = confirm ("Are you sure you want to delete this " + entryType + "?");
                    if (doCancel == true) {
                        thisForm.submit();
                    }
                }
            
            </script>               
            <%-- START:ADDRESS ENTRIES --%>
            <logic:iterate id="address" collection="<%=customer.getShipToAddresses()%>" type="com.freshdirect.customer.ErpAddressModel" indexId="idx">
            <div class="cust_inner_module" style="width: 32%;<%=idx.intValue() < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">
                <table width="100%" cellpadding="0" cellspacing="0" class="cust_module_content_text">
                <tr valign="top">
                    <td class="cust_module_content_note"><%=idx.intValue()+1%></td>
                    <td>
                    <table width="100%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                    <form name="addressForm_<%=idx.intValue()%>" method="POST">
                        <tr>
                            <td colspan="2"><%= address.getFirstName() %> <%= address.getLastName() %><br>
                            <%=address.getCompanyName() != null ? address.getCompanyName() + "<BR>" : "" %> <%=address.getAddress1()%>&nbsp;Apt. <%=address.getApartment()%>
                            <%if(address.getAddress2() != null && !"".equalsIgnoreCase(address.getAddress2())){%>
                            <br><%=address.getAddress2()%><%}%><br><%=address.getCity()%>,&nbsp;<%=address.getState()%>&nbsp;<%=address.getZipCode()%> &middot; <%=address.getPhone()!=null?address.getPhone().getPhone():""%>
                            <%boolean deliverAlcohol = com.freshdirect.fdstore.FDDeliveryManager.getInstance().checkForAlcoholDelivery(address);
                            if (deliverAlcohol) { %>
                                <br><br><span class="cust_module_content_note">This address <b>IS</b> eligible for alcohol delivery</span>
                            <% } else { %>                  
                                <br><br><span class="cust_module_content_note">This address <b>IS NOT</b> eligible for alcohol delivery</span>
                            <% }%>
                                <br><span class="cust_module_content_note">Service Type: <b><%=address.getServiceType().getName()%></b></span>
                            <%
                            EnumRestrictedAddressReason restrictionReason = FDDeliveryManager.getInstance().checkAddressForRestrictions(address);
                            if(EnumRestrictedAddressReason.COMMERCIAL.equals(restrictionReason)){%>
                                <br><br><span class="cust_module_content_note">This address is a <b>COMMERCIAL</b> address and is blocked</span>
                            <%}%>
                            <%if(EnumRestrictedAddressReason.FRAUD.equals(restrictionReason)){%>
                                <br><br><span class="cust_module_content_note">This address is blocked due to <b>FRAUD</b></span>
                            <%}%>

                            </td>
                        </tr>
                        <% if (address.getInstructions() != null) { %>
                            <tr><td colspan="2" class="cust_module_content_note"><span class="cust_module_content_note_heading">Special delivery instructions:</span> <%=address.getInstructions()%><br>
                            </td></tr>
                        <% } %>

			<fd:UnattendedDelivery id='zone' address="<%= address %>" checkUserOptions="true">
			<%
			    if (zone.isUnattended() && !EnumUnattendedDeliveryFlag.OPT_OUT.equals(address.getUnattendedDeliveryFlag())) {
			    %>
			    <tr><td colspan="2" class="cust_module_content_note">
			    <font color="#0000aa">Unattended Delivery:</font> <%= NVL.apply(address.getUnattendedDeliveryInstructions(),"OK") %> 
			    </td></tr>
			 <% } %>
			 </fd:UnattendedDelivery>
                        
                        <% if (!EnumDeliverySetting.NONE.equals(address.getAltDelivery())) { %>
                            <tr><td colspan="2" class="cust_module_content_note"><span class="cust_module_content_note_heading">Alternate Delivery:</span> 
                            <%=EnumDeliverySetting.DOORMAN.equals(address.getAltDelivery())?"Doorman":""%><%=EnumDeliverySetting.NEIGHBOR.equals(address.getAltDelivery()) ? "Neighbor<br>" : ""%>
                            <% if (EnumDeliverySetting.NEIGHBOR.equals(address.getAltDelivery())) { %>
                            <%=address.getAltFirstName()%> <%=address.getAltLastName()%>, Apt.<%=address.getAltApartment()%>, <%=address.getAltPhone()!=null?address.getAltPhone().getPhone():""%></td></tr>
                            <% } %>
                        <% } %>
                        <%if (!forPrint){%>
                            <tr><td colspan="2" height="8"></td></tr>
                            <tr>
                                <%if(editable){%>
                                    <td><a href="javascript:confirmDelete(addressForm_<%=idx.intValue()%>,'address')" class="delete">DELETE</a></td>
                                    <td align="right"><a href="/customer_account/edit_delivery_address.jsp?addressId=<%=address.getPK().getId()%>" class="edit">EDIT</a></td>
                                <%}else{%>
                                    <td colspan="2" align="center" class="cust_module_content_edit"><%=case_required%></td>
                                <%}%>
                            </tr>
                        <%}%>
                        <% if (user.isEligibleForPreReservation()) { %>
                        <tr><td colspan="2" height="10"></td></tr>
                        <tr><td colspan="2" style="border-top: 1px dashed #CCCCCC;" align="center">
                        <%
                            FDReservation rsv = user.getReservation();
                                if (rsv != null && rsv.getAddressId().equals(address.getPK().getId())) {%>
                                	<span style="color:#FF6600;">&raquo;</span> Reserved timeslot: <b><%=CCFormatter.formatShortDlvDate(rsv.getStartTime())%>, <%=CCFormatter.formatTime(rsv.getStartTime())%> - <%=CCFormatter.formatDeliveryTime(rsv.getEndTime())%></b> 
                                		<% FDDeliveryManager.getInstance().geocodeAddress(address);
                                		   String zoneId = FDDeliveryManager.getInstance().getZoneInfo(address, Calendar.getInstance().getTime()).getZoneId();
                                		
                                		if(zoneId == null || !zoneId.equalsIgnoreCase(rsv.getZoneId())) { %>
                                			<blink><span class='no'>ZONE MISMATCH</span></blink>
                                		<% }                                		
                                		 if (editable) {%>(<a href="/customer_account/reserve_timeslot.jsp?addressId=<%=address.getPK().getId()%>">Edit</a>)
                                			<% } else { %><br><%=case_required%><% } %>
                            <% } else { %>
                           		 <% if (editable) {%><a href="/customer_account/reserve_timeslot.jsp?addressId=<%=address.getPK().getId()%>">Reserve Timeslot</a><% } else { %>Reserve Timeslot <br><%=case_required%><% } %>
                            <% } %>
                        </td></tr>
                        <% } %>
                    </table>
                    </td>
                    <input type="hidden" name="actionName" value="deleteAddress">
                    <input type="hidden" name="deleteAddressId" value="<%=address.getPK().getId()%>">
                    </form>
                </tr>
                </table>
                </div>
                
                <% ErpActivityRecord lastDlvInfo = getLatest(activities, EnumAccountActivityType.UPDATE_DLV_ADDRESS, address.getPK().getId()); 
                    if (lastDlvInfo != null) { %>
                        <%= getLastModified(lastDlvInfo) %> 
                <% } %>

            </div>
            <%if(idx.intValue() != 0 && (idx.intValue() + 1) % 3 == 0 && ((idx.intValue() + 1) < customer.getShipToAddresses().size())){%>
                <br clear="all">
            <%}%>
            </logic:iterate>
            <%-- END:ADDRESS ENTRIES --%>

            <br clear="all"><%-- line break --%>

            <%-- 3rd row --%>
                <table width="100%" cellpadding="0" cellspacing="0" class="cust_full_module_header" style="margin-bottom: 5px; margin-top: 10px;">
                    <tr>
                        <td class="cust_module_header_text">Payment Information</td>
                        <td><fd:ErrorHandler result="<%=ccResult%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
                        <td class="note" width="70%" style="font-weight: normal;">Note: Under <b>NO</b> circumstances should credit card or checking account information be read to customer. If customer provides correct information to you it may be confirmed or denied.</td>
                        <td width="10%" align="right"></td>
                    </tr>
                </table>

			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td width="20%" style="padding: 4px; margin-top: 5px; border-bottom: none; background:#E8FFE8;"><b>Credit cards</b></td><td align="right" width="78%"><%if (!forPrint){%><%if(editable){%><a href="/customer_account/new_credit_card.jsp" class="add">ADD</a><%}else{%><%=case_required_add%><%}%><%}%></td><td width="1%"></td></tr></table>
            <%-- START:PAYMENT ENTRIES --%>
            <logic:iterate id="payment" collection="<%=customer.getPaymentMethods()%>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="idx">
            <%
            if(EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())){
            	numCreditCards++;
            }else if(EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())){
            	numEChecks++;
            }
            %>
            </logic:iterate>

            <logic:iterate id="payment" collection="<%=customer.getPaymentMethods()%>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="idx">
            <%
            if(EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())){
            %>
                <div class="cust_inner_module" style="width: 32%;<%=ccNum < 3 ?"border-top: none;":""%>">
                     <div class="cust_module_content">
                     <table width="100%" cellpadding="0" cellspacing="0" class="cust_module_content_text">
                        <tr valign="top">
                        <td class="cust_module_content_note"><%=ccNum+1%></td>
                        <td>
                        <form name="paymentForm_<%=idx.intValue()%>" method="POST">
                        <table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                            <tr>
                                <td align="right">Name on account:&nbsp;&nbsp;</td>
                                <td><%=payment.getName()%></td>
                            </tr>
                            <tr>
                                <td align="right">Card Type:&nbsp;&nbsp;</td>
                                <td><%=payment.getCardType()%></td>
                            </tr>
                            <tr>
                                <td align="right">Account Number:&nbsp;&nbsp;</td>
                                <td><%=payment.getMaskedAccountNumber()%></td>
                            </tr>
                            <tr>
                                <td align="right">Expires:&nbsp;&nbsp;</td>
                                <td><%=CCFormatter.formatCreditCardExpDate(payment.getExpirationDate())%></td>
                            </tr>
                            <tr>
                                <td align="right">Billing Address:&nbsp;&nbsp;</td>
                                <td><%=payment.getAddress1()%>&nbsp;Apt. <%=payment.getApartment()%>
                                    <%if(payment.getAddress2() != null && !"".equalsIgnoreCase(payment.getAddress2())){%>
                                        <br><%=payment.getAddress2()%><%}%><br><%=payment.getCity()%>,&nbsp;<%=payment.getState()%>&nbsp;<%=payment.getZipCode()%>
                                </td>
                            </tr>
                            <%if (!forPrint){%>
                                <tr><td colspan="3" height="8"></td></tr>
                                <tr>
                                <% if (editable) { %>
                                    <% //if (numCreditCards > 1) {%><td><a href="javascript:confirmDelete(paymentForm_<%=idx.intValue()%>, 'payment method')" class="delete">DELETE</a></td><% //} %>
                                    <td <%= numCreditCards > 1 ? "align=\"right\"" : "colspan=\"2\" align=\"center\""%> ><a href="/customer_account/edit_credit_card.jsp?paymentId=<%=((ErpPaymentMethodModel)payment).getPK().getId()%>" class="edit">EDIT</a></td>
                                <% } else { %>
                                    <td colspan="2" align="center" class="cust_module_content_edit"><%=case_required%></td>
                                <% } %>
                                </tr>
                            <% } %>
                            <input type="hidden" name="actionName" value="deletePaymentMethod">
                            <input type="hidden" name="deletePaymentId" value="<%=((ErpPaymentMethodModel)payment).getPK().getId()%>">
                            </form>
                        </table>
                        </td></tr></table>
                    </div>
                    
                    <% Set billingInfoActivity = new HashSet();
                    billingInfoActivity.add(EnumAccountActivityType.UPDATE_BIL_ADDRESS);
                    billingInfoActivity.add(EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
                    
                    ErpActivityRecord lastBillingInfo = getLatest(activities, billingInfoActivity, ((ErpPaymentMethodModel)payment).getPK().getId()); 
                    if (lastBillingInfo != null) { %>
                        <%= getLastModified(lastBillingInfo) %> 
                    <% } %>
                    
                </div>
                <%if(ccNum != 0 && (ccNum + 1) % 3 == 0 && ((ccNum + 1) < numCreditCards)){%>
                    <br clear="all">
                <%}
                ccNum++;
             } %>
            </logic:iterate>
            <%-- END:PAYMENT ENTRIES --%>
			<br clear="all">			
			<%-- START CHECKING ACCT --%>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td width="20%"  style="padding: 4px; margin-top: 5px; border-bottom: none; background:#E8FFE8;"><b>Checking accounts</b></td><td width="59%" class="field_note">&nbsp;<span class="error">A valid credit card is required as a guarantee for orders using check as payment.</span><%--a href="#">View check usage promotion</a--%></td><td align="right" width="20%"><%if (!forPrint){%><%if(editable){%><a href="/customer_account/new_checking_account.jsp" class="add">ADD</a><%}else{%><%=case_required_add%><%}%><%}%></td><td width="1%"></td></tr></table>
        	 <%-- using cc data for display purposes --%>			 
			 <logic:iterate id="payment" collection="<%=customer.getPaymentMethods()%>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="idx">
            <%
            if(EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())){
            %>
                <div class="cust_inner_module" style="width: 32%;<%=ecNum < 3 ?"border-top: none;":""%>">
                     <div class="cust_module_content">
                     <table width="100%" cellpadding="0" cellspacing="0" class="cust_module_content_text">
                        <tr valign="top">
                        <td class="cust_module_content_note"><%=ecNum+1%></td>
                        <td>
                        <form name="paymentForm_<%=idx.intValue()%>" method="POST">
                        <table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
                            <tr>
                                <td align="right" width="40%">Name on account:&nbsp;&nbsp;</td>
                                <td width="60%"><%=payment.getName()%></td>
                            </tr>
							<tr valign="top">
                                <td align="right">Account type:&nbsp;&nbsp;</td>
                                <td><%=payment.getBankAccountType().getDescription()%></td>
                            </tr>
							<tr>
                                <td align="right">Account number:&nbsp;&nbsp;</td>
                                <td><%=payment.getMaskedAccountNumber()%></td>
                            </tr>
							<tr>
                                <td align="right">Routing number:&nbsp;&nbsp;</td>
                                <td><%=payment.getAbaRouteNumber()%></td>
                            </tr>
                            <tr>
                                <td align="right">Bank name:&nbsp;&nbsp;</td>
                                <td><%=payment.getBankName()%></td>
                            </tr>
                            <tr>
                                <td align="right">Account address:&nbsp;&nbsp;</td>
                                <td><%=payment.getAddress1()%>&nbsp;Apt. <%=payment.getApartment()%>
                                    <%if(payment.getAddress2() != null && !"".equalsIgnoreCase(payment.getAddress2())){%>
                                        <br><%=payment.getAddress2()%><%}%><br><%=payment.getCity()%>,&nbsp;<%=payment.getState()%>&nbsp;<%=payment.getZipCode()%>
                                </td>
                            </tr>
                            <%if (!forPrint){%>
                                <tr><td colspan="3" height="8"></td></tr>
                                <tr>
                                <% if (editable) { %>
                                    <td><a href="javascript:confirmDelete(paymentForm_<%=idx.intValue()%>, 'payment method')" class="delete">DELETE</a></td>
                                    <td <%= numEChecks > 1 ? "align=\"right\"" : "colspan=\"2\" align=\"center\""%> ><a href="/customer_account/edit_checking_account.jsp?paymentId=<%=((ErpPaymentMethodModel)payment).getPK().getId()%>" class="edit">EDIT</a></td>
                                <% } else { %>
                                    <td colspan="2" align="center" class="cust_module_content_edit"><%=case_required%></td>
                                <% } %>
                                </tr>
                            <% } %>
							<crm:CrmGetIsBadAccount id="isRestrictedAccount" paymentMethod="<%=payment%>">
							<% 
								if (isRestrictedAccount.booleanValue()) {
							%>
							<tr><td colspan="2"><div style="border: solid 1px #CC0000; padding 5px; font-size: 8pt; text-align: center;"><b>Payment for an order <span class="error_detail">failed</span> with this account</b></div></td></tr>
							<% 
								} else {
							%>
							<tr><td colspan="2">&nbsp;</td></tr>
							<%
								}
							%>
							</crm:CrmGetIsBadAccount>
                            <input type="hidden" name="actionName" value="deletePaymentMethod">
                            <input type="hidden" name="deletePaymentId" value="<%=((ErpPaymentMethodModel)payment).getPK().getId()%>">
                            </form>
                        </table>
                        </td></tr></table>	
                    </div>
                    
                    <% Set billingInfoActivity = new HashSet();
                    billingInfoActivity.add(EnumAccountActivityType.UPDATE_BIL_ADDRESS);
                    billingInfoActivity.add(EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
                    
                    ErpActivityRecord lastBillingInfo = getLatest(activities, billingInfoActivity, ((ErpPaymentMethodModel)payment).getPK().getId()); 
                    if (lastBillingInfo != null) { %>
                        <%= getLastModified(lastBillingInfo) %> 
                    <% } %>

                </div>
                <%if(ecNum != 0 && (ecNum + 1) % 3 == 0 && ((ecNum + 1) < numEChecks)){%>
                    <br clear="all">
                <%}
                  ecNum++;
            	}%>
            </logic:iterate>
			<%-- END CHECKING ACCT --%>
		<br>
		</div>
    </tmpl:put>

    </tmpl:insert>
</crm:GetCurrentAgent>
</fd:AccountActivity>
</crm:GetFdCustomer>
</crm:GetErpCustomer>
</crm:CrmAddressController>
</crm:CrmPaymentMethodController>

</crm:GetFDUser>
