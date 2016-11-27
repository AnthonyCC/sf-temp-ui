<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.cms.ContentNodeI" %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	String faqSections = FDStoreProperties.getFaqSections();
	int subjectIndex = -1;
	String email = "";
	String firstName = "";
	String lastName = "";
	String homePhone = "";
	String homePhoneExt = "";
	String workPhone = "";
	String workPhoneExt = "";
	String altPhone = "";
	String altPhoneExt = "";
	String body = "";
	String[] checkContactForm = {"subject", "message", "email", "first_name", "last_name", "home_phone"};
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	
	ErpCustomerInfoModel cm = null;
	FDIdentity identity  = user.getIdentity();

	if (identity != null) {
		cm = FDCustomerFactory.getErpCustomerInfo(identity);
	}

	if (cm != null) {
		email = cm.getEmail();
		lastName = cm.getLastName();
		firstName = cm.getFirstName();
		homePhone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
		homePhoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
		altPhone = cm.getOtherPhone()==null?"":cm.getOtherPhone().getPhone();
		altPhoneExt = cm.getOtherPhone()==null?"":cm.getOtherPhone().getExtension();
	}

	if(request.getParameter("subject")!=null){
		try {
			subjectIndex = Integer.parseInt(request.getParameter("subject"));
		} catch (NumberFormatException Ex){
			subjectIndex = 0;
		}
	}
	if(request.getParameter("email")!=null){
		email=request.getParameter("email");
	}
	if(request.getParameter("first_name")!=null){
		firstName=request.getParameter("first_name");
	}
	if(request.getParameter("last_name")!=null){
		lastName=request.getParameter("last_name");
	}
	if(request.getParameter("home_phone")!=null){
		homePhone=request.getParameter("home_phone");
	}
	if(request.getParameter("home_phone_ext")!=null){
		homePhoneExt=request.getParameter("home_phone_ext");
	}
	if(request.getParameter("work_phone")!=null){
		workPhone=request.getParameter("work_phone");
	}
	if(request.getParameter("work_phone_ext")!=null){
		workPhoneExt=request.getParameter("work_phone_ext");
	}
	if(request.getParameter("alt_phone")!=null){
		altPhone=request.getParameter("alt_phone");
	}
	if(request.getParameter("alt_phone_ext")!=null){
		altPhoneExt=request.getParameter("alt_phone_ext");
	}
	if(request.getParameter("message")!=null){
		body=request.getParameter("message");
	}

	String csNumberMedia = user.getCustomerServiceContactMediaPath();
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="index_help" includeSiteSearchLink="true"/>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:ContactFdController result="result" successPage='/help/contact_fd_thank_you.jsp'>
			<fd:ErrorHandler result='<%=result%>' name='kana' id='errorMsg'>
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>

			<fd:ErrorHandler result='<%=result%>' field='<%=checkContactForm%>'>
				<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
				<%@ include file="/includes/i_error_messages.jspf" %>	
			</fd:ErrorHandler>

			<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
				<%@ include file="/includes/i_error_messages.jspf" %>	
			</fd:ErrorHandler>

			<div id="help-page">
				<div class="welcome-msg-title-small bold">
					Welcome to Help
				</div>
				<div class="bordered-bottom">
					<% boolean loyaltyHelpContact = false; %>
					<br /><%@ include file="/shared/help/i_loyalty_banner.jspf"%>
				</div>
				<div class="help-aside fl-left">
				<h2 class="help-heading bold uppercase">Search</h2>
				<form method="post" name="contact_fd" id="contact_fd_faq">
					<label for="search-faq"></label><input type="text" id="search-faq"class="search" value="" maxlength="100" name="searchFAQ" />
					<button class="cssbutton orange small" type="submit" name="searchFAQButton">find</button>	
				</form>
				<a href="/help/faq_home.jsp?page=<%= FDFaqUtil.getFaqHomeId() %>"><h2 class="help-heading bold uppercase">quick links</h2></a>
				<p>Check below to find the fastest answers to our top customer concerns.</p>
				<ul class="orangeDot bold">
					<li><a href="/your_account/order_history.jsp">Check the status of your order</a></li>
					<li><a href="/your_account/order_history.jsp">Change or cancel your order</a></li>
					<li><a href="/search.jsp">Find a product</a></li>
					<li><a href="/your_account/signin_information.jsp">Change your password</a></li>
					<li><a href="/your_account/manage_account.jsp">Change delivery info</a></li>
					<li><a href="/your_account/payment_information.jsp">Change credit card info</a></li>
				</ul>

				<a href="/help/faq_home.jsp?page=<%= FDFaqUtil.getFaqHomeId() %>"><h2 class="help-heading bold">FAQs</h2></a>
				<p>Scan our Frequently Asked Questions to get info on sign-up, delivery and everything in between.</p>
					
					<% List savedList=(List)pageContext.getAttribute("savedFaqs");

					if (null !=savedList && savedList.size()>0 && null != savedList.get(0)) { %>
							<h3 class="help-subheading">Top Questions</h3>
							<% if (null != faqSections) {
								%>
								<ul class="orangeDot bold">
								<%
								StringTokenizer st = new StringTokenizer(faqSections,",");
								while (st.hasMoreTokens()) {
									String nextToken=st.nextToken().trim();
									%><logic:iterate id="topfaq" indexId="idx" collection="<%= savedList %>" type="com.freshdirect.fdstore.content.Faq"><%
										if (null!=topfaq && null !=topfaq.getParentNode() && nextToken.equalsIgnoreCase((String)topfaq.getParentNode().getContentKey().getId())) { %>
											<li><a href="/help/faq_home.jsp?page=<%= (String)topfaq.getParentNode().getContentKey().getId()%>#<%= (String)topfaq.getContentKey().getId()%>"><%= topfaq.getQuestion() %></a></li>
										<% } %>
									</logic:iterate><%
								} %>
								</ul>
							<% } %>
					<% } %>

					<h3 class="help-subheading">Learn More</h3>
					<% if(null != faqSections) { %>
							<ul class="orangeDot bold">
							<%
							StringTokenizer st = new StringTokenizer(faqSections,",");
							
							while (st.hasMoreTokens()) {
								ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
								if (null != contentNode) { %>
									<li><a href="/help/faq_home.jsp?page=<%= contentNode.getContentKey().getId()%> "><%= contentNode.getCmsAttributeValue("name") %></a></li>
								<% }
							} %>
							</ul>
						<% } %>
				</div>
				<div class="fl-left help-main">
					<form method="post" name="contact_fd" id="contact_fd_contact">
								<a href='index.jsp'><h2 class="help-heading bold uppercase">contact freshdirect</h2></a>
								<p class="small-text">FreshDirect Customer Service is standing by to answer your questions, seven days a week.
								<span class="bold">The best way to get help is through email. Our dedicated service team generally responds within 1 to 3 hours during our business day.</span>
								</p>
						
						<div class="underline">
							<h4 class="bold inline">1. Enter Your Message</h4>
							<span class="small"> * Required information</span>	
						</div>
	
						<script type="text/javascript">
								var coremetricsGetHelpEmailFunctionMap = [
								<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
									<%if (idx!=0){%>,<%}%><fd:CmConversionEvent wrapIntoFunction="true" eventId="email" firstPhase="true" subject="<%=StringUtil.escapeJavaScript(subject.getDescription())%>"/>
								</logic:iterate>
							];
							<%--
								This seems sort of useless, it only logs a single subject change?
								Changed to work the same with new js (called once on page load (no log),
								then on every subject change (log only the first change))
								still... seems... weird.
								-BA 20140401
							--%>
							var coremetricsGetHelpEmailStartLogged = 0; //init
							function coremetricsGetHelpEmailStart(index) {
								if (coremetricsGetHelpEmailStartLogged == 1) {
									coremetricsGetHelpEmailFunctionMap[index]();
								}
								coremetricsGetHelpEmailStartLogged++;
							}
							
							$jq('#prodReqContent').ready(function() { $jq('#prodReqContent').hide(); });
							$jq(document).ready(function() {
								$jq('#contact_subject').change(function() {
									var selectedOpt = $jq('#contact_subject option').filter(':selected');
									if ( selectedOpt.text() == 'Product Request' ) {
										$jq('#prodReqContent').show();
										$jq('#prodReqNonContent').hide();
									} else {
										$jq('#prodReqContent').hide();
										$jq('#prodReqNonContent').show();
									}
									if (coremetricsGetHelpEmailFunctionMap[selectedOpt.val()]) {
										coremetricsGetHelpEmailStart(selectedOpt.val());
									}
								});
								$jq('#contact_subject').change();
							});
	
						</script>
					<div class="from-elements-wrapper">
						<div class="form-group">
							<label for="contact_subject" class="bold inline text-right">* Subject:</label>
							<select name="subject" id="contact_subject" onchange="">
								<option value="">Select Subject:</option>
								<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
									<option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
								</logic:iterate>
							</select>
							<fd:ErrorHandler result='<%=result%>' name='subject' id='errorMsg'>
								<%--if error occured with subject selection, tracking is needed, because subject will be changed automatically--%>
								<script type="text/javascript">
									var subjectSelect = document.getElementById('contact_subject');	
									coremetricsGetHelpEmailStart(subjectSelect.options[subjectSelect.selectedIndex].value)
								</script>
								<div class="bold error">
									<%=errorMsg%>
								</div>
							</fd:ErrorHandler>
	
						<%
							/* APPDEV-1744	Product Request - 2011 CS Project */
							Map params = new HashMap();
							params.put("baseUrl", "");
							boolean isDefaultFtl = true;
							String faqPage = "req_feedback"; //set as product request
							
							/* only add this if we have faq sections (since we need them to determine this) */
							if (null != faqSections) {
								params.put("faqNodes", FDFaqUtil.getFaqsByCategory(faqPage));
	
								StringTokenizer st = new StringTokenizer(faqSections, ",");
								while (st.hasMoreTokens()) {
									String nextToken = st.nextToken().trim();
									params.put(nextToken, FDFaqUtil.getFaqsByCategory(nextToken));
									if (nextToken.equalsIgnoreCase(faqPage) && isDefaultFtl) {
										isDefaultFtl = false;
									}
								}
	
								/* add additional param special for this, so we can change the look of the included ftl */
								params.put("faqContact", "true");
						%>
							<div id="prodReqContent" style="display: none;">
									<fd:IncludeMedia name="/media/editorial/faq/req_feedback.ftl" parameters="<%=params%>" withErrorReport="false"/>
							</div>
						<% } %>
						</div>
						
						<div id="prodReqNonContent">
							<div class="form-group">
								<% if (identity != null) { %>
									<fd:OrderHistoryInfo id='orderHistoryInfo'>
										<%if (orderHistoryInfo.size() > 0) {%>
												<label for="order-field" class="bold inline text-right">Order #:</label>
												<select name="salePK" id="order-field">
													<option value="">Select Order:</option>
													<logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
														<% if (idx.intValue() == 5) break; %>
														<option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %></option>
													</logic:iterate>
												</select>
												<span class="small">(optional)</span>
										<% } %>
									</fd:OrderHistoryInfo>
								<% } %>
							</div>
							<div class="form-group">
								<label for="msg-textarea" id="msg-textarea-label" class="bold inline">* Please enter your message here:</label>
								<textarea id="msg-textarea" cols="40" rows="5" name="message" onKeyPress="limitText(this, 2048)" onChange="limitText(this, 2048)"><%= body%></textarea>
								<fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'>
									<div class="bold error">
										<%=errorMsg%>
									</div>
								</fd:ErrorHandler>
							</div>
							</div>
						</div>
							<%-- info --%>
							<%if (identity == null) { %>
							<div class="underline">
								<h4 class="bold inline">2. Enter Your Contact Information</h4>
								<span class="small"> * Required information</span>
							</div>
							<div class="from-elements-wrapper">
							<div class="form-group">	
								<label for="email-field" class="bold inline">* E-mail Address/User Name:</label>
								<input type="text" name="email" id="email-field" size="34" value="<%=email%>" />
								<fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
							</div>
							<div class="form-group">
								<label for="first-name-field" class="bold inline">* First Name:</label>
								<input type="text" name="first_name" id="first-name-field" size="21" value="<%=firstName%>" />
								<fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
							</div>
							<div class="form-group">
								<label for="last-name-field" class="bold inline">* Last Name:</label>
								<input type="text" name="last_name" id="last-name-field" size="21" value="<%=lastName%>" />
								<fd:ErrorHandler result='<%=result%>' name='last_name' id='errorMsg'><span class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
							</div>
							<div class="form-group">
								<label for="home-phone-field" class="bold inline">Home Phone #:</label>
								<input type="text" name="home_phone" id="home-phone-field" size="21" value="<%=homePhone%>" maxlength="15" />
								<label for="home-phone-ext-field">Ext.</label>
								<input type="text" name="home_phone_ext" id="home-phone-ext-field" size="4" value="<%=homePhoneExt%>" maxlength="6" />
							</div>
							<div class="form-group">
								<label for="work-phone-field" class="bold inline">Work Phone #:</label>
								<input type="text" name="work_phone" id="work-phone-field" size="21" value="<%=workPhone%>" maxlength="15" />
								<label for="work-phone-ext-field">Ext.</label>
								<input type="text" id="work-phone-ext-field" name="work-phone-ext-field" size="4" value="<%=workPhoneExt%>" maxlength="6" />
							</div>
							<div class="form-group">
								<label for="other-phone-field" class="bold inline">Other Phone #:</label>
								<input type="text" name="alt_phone" id="other-phone-field" size="21" value="<%=altPhone%>" maxlength="15" />
								<label for="other-phone-ext-field">Ext.</label>
								<input type="text" name="alt_phone_ext" id="other-phone-ext-field"  size="4" value="<%=altPhoneExt%>" maxlength="6" />
							</div>
						</div>
							<% }else{ %>
								<div class="underline">
									<h4 class="bold inline">2. Enter Your Contact Information</h4>
								</div>
								<div class="form-group text-center small-text">
									<span class="bold">E-mail Address/User Name:</span><span> <%=email%></span>
								</div>
								<div class="form-group text-center small-text">
									<span class="bold">Name:</span><span> <%=firstName%> <%=lastName%></span>
								</div>
								<div class="form-group text-center small-text">
									<a href='/your_account/signin_information.jsp'>(If this information is incorrect, click here!)</a>
								</div>
							<% } %>
	
							<div class="text-center separator">
								<button type="reset" class="cssbutton grey">clear</button>
								<button type="submit" name="sendMessage" class="cssbutton orange">Send Message</button>
							</div>
						<div class="small-text separator">
							<%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/help_home_hours.html" /><%--END MEDIA INCLUDE --%>
							You may also call <%= (user.isChefsTable()) ? "toll-free" : "us" %> at <fd:IncludeMedia name="<%= csNumberMedia %>" />
						</div>
				</form>
				</div>
				<div class="bordered-box">
					<div class="trisect inline text-center">
						<div class="bold"><a href="/help/terms_of_service.jsp">Customer Agreement</a></div>
						<div>
							All the details about what to<br /> expect when you shop with us.
						</div>
					</div>
					<div class="trisect inline text-center">
						<div class="bold"><a href="/help/privacy_policy.jsp">Privacy Policy</a></div>
						<div>
							We keep your account secure and<br /> your information private.
						</div>
					</div>
					<div class="trisect inline text-center">
						<div class="bold"><a href="javascript:pop('/help/freshness.jsp',335,375)">Our Freshness Guarantee</a></div>
						<div>
							100% satisfaction with every<br/> product, every time.
						</div>
					</div>
				</div>
			</div>
		</fd:ContactFdController>
	</tmpl:put>
</tmpl:insert>
