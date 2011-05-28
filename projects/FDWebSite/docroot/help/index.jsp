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
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put>
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

			<script language="javascript">
				function submitForm(){
				}
				
				if (document.observe) { //make sure prototype is on page
					document.observe("dom:loaded", function() {
						new Form.Observer('contact_fd', 0.33, prodReq);
						/* call prodReq the first time to catch page reloads */
						prodReq();
					});
				}

				function prodReq() {
					var textCheck = '';
					if ($('contact_subject')) {
						textCheck = $('contact_subject').options[$('contact_subject').selectedIndex].text;
					}

					(textCheck === 'Product Request')
						? prodReqToggle('show')
						: prodReqToggle('hide');
				}

				function prodReqToggle(showOrHideVar) {
					var showOrHide = showOrHideVar || 'hide';

					if (showOrHide === 'hide') {
						$('prodReqContent').hide();
						$('prodReqNonContent').show();
					}else{
						$('prodReqContent').show();
						$('prodReqNonContent').hide();
					}
				}

				function clearProdReqSelection() {
					$('contact_subject').selectedIndex = 0;
				}
			</script>

			<form method="post" name="contact_fd" id="contact_fd">
				<div style="width: 700px; vertical-align: top; text-align: left;">
					<div class="title16">
						Welcome to Help
					</div>
					<div>
						<% boolean loyaltyHelpContact = false; %>
						<br /><%@ include file="/shared/help/i_loyalty_banner.jspf"%>
					</div>
					<div style="height: 30px; background: transparent url('/media_stat/images/layout/cccccc.gif') repeat-x left center;"><!--  --></div>

					<div style="float: left; width: 34%; border-right: 1px solid #ccc; padding-right: 6px;">
						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 8px;">
								<a href="/help/faq_home.jsp?page=faqHome"><img src="/media_stat/images/template/help/hdr_quick_links.gif" width="119" height="15" border="0" alt="" /></a>
							</div>
							<div style="margin-bottom: 8px;">
								Check below to find the fastest answers to our top customer concerns.
							</div>
							<div style="margin-bottom: 8px;" class="text11bold">
								<ul class="orangeDot">
									<li><a href="/your_account/order_history.jsp">Check the status of your order</a></li>
									<li><a href="/your_account/order_history.jsp">Change or cancel your order</a></li>
									<li><a href="/search.jsp">Find a product</a></li>
									<li><a href="/your_account/signin_information.jsp">Change your password</a></li>
									<li><a href="/your_account/manage_account.jsp">Change delivery info</a></li>
									<li><a href="/your_account/payment_information.jsp">Change credit card info</a></li>
								</ul>
							</div>
						</div>

						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 8px;">
								<a href="/help/faq_home.jsp?page=faqHome"><img src="/media_stat/images/template/help/hdr_faqs.gif" width="45" height="14" border="0" alt="FAQs" /></a>
							</div>
							<div style="margin-bottom: 8px;">
								Scan our Frequently Asked Questions to get info on sign-up, delivery and everything in between.
							</div>
							<%
								List savedList=(List)pageContext.getAttribute("savedFaqs");

								if (null !=savedList && savedList.size()>0 && null != savedList.get(0)) { %>
									<div style="margin-bottom: 8px;" class="text11bold">
										<div style="margin-bottom: 4px;">
											Top Questions this Week:
										</div>
										<% if (null != faqSections) {
											%>
											<div style="margin-bottom: 8px;">
												<ul class="orangeDot">
												<%

												StringTokenizer st = new StringTokenizer(faqSections,",");
												while (st.hasMoreTokens()) {
													String nextToken=st.nextToken().trim();
													%><logic:iterate id="topfaq" indexId="idx" collection="<%= savedList %>" type="com.freshdirect.fdstore.content.Faq"><%
														if (null!=topfaq && null !=topfaq.getParentNode() && nextToken.equalsIgnoreCase((String)topfaq.getParentNode().getContentKey().getId())) { %>
															<li><a href="/help/faq_home.jsp?page=<%= (String)topfaq.getParentNode().getContentKey().getId()%>#<%= (String)topfaq.getContentKey().getId()%>"><%= topfaq.getQuestion() %></a></li>
														<% } %>
													</logic:iterate><%
												}
												%>
												</ul>
											</div>
											<%
										} %>
									</div>
								<% }
							%>
						</div>

						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 8px;" class="text11bold">
								Learn More
							</div>
							<% if(null != faqSections) {
								%>
								<div style="margin-bottom: 8px;" class="text11bold">
									<ul class="orangeDot">
									<%
									
									StringTokenizer st = new StringTokenizer(faqSections,",");
									
									while (st.hasMoreTokens()) {
										ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
										if (null != contentNode) { %>
											<li><a href="/help/faq_home.jsp?page=<%= contentNode.getContentKey().getId()%> "><%= contentNode.getCmsAttributeValue("name") %></a></li>
										<% }
									}
									%>
									</ul>
								</div>
								<%
							} %>
						</div>

						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 8px;" class="text11bold">
								Search our FAQs
							</div>
							<div style="margin-bottom: 8px;">
								<input type="text" class="search" value="" maxlength="100" style="width: 180px;" name="searchFAQ" />
								<input type="image" name="searchFAQButton" style="width: 35px; height: 14px; vertical-align: bottom; margin-left: 10px;" src="/media_stat/images/template/search/search_find_button.gif" onclick="submitForm()" />
							</div>
						</div>
					</div>

					<div style="float: right; width: 64%; padding-left: 6px;" class="lineItems">
						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 8px;">
								<a href='index.jsp'><img src="/media_stat/images/template/help/hdr_contact_us.gif" border="0" width="204" height="14" alt="CONTACT US"></a>
							</div>
							<div style="margin-bottom: 8px;">
								FreshDirect Customer Service is standing by to answer your questions, seven days a week. <span style="font-weight: bold;">The best way to get help is through email. Our dedicated service team generally responds within 1 to 3 hours during our business day.</span>
							</div>
						</div>
						
						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 4px;">
								<img src="/media_stat/images/template/help/enter_message.gif" width="152" height="9" border="0" alt="ENTER YOUR MESSAGE" />&nbsp;&nbsp;&nbsp;<span class="text9">* Required information</span>
							</div>
							<img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" alt="" />
						</div>

						<div style="margin-bottom: 16px;">
							<div style="float: left; width: 12px;">*</div><div style="float: left; width: 55px; font-weight: bold; padding-right: 10px;">Subject:</div>
							<select class="text12" name="subject" id="contact_subject" style="width: 246px;">
								<option value="">Select Subject:</option>
								<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
									<option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
								</logic:iterate>
							</select>
							<fd:ErrorHandler result='<%=result%>' name='subject' id='errorMsg'>
								<div class="text11rbold">
									<%=errorMsg%>
								</div>
							</fd:ErrorHandler>
						</div>

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
								<div style="margin-bottom: 8px;">
									<fd:IncludeMedia name="/media/editorial/faq/req_feedback.ftl" parameters="<%=params%>" withErrorReport="false"/>
								</div>
								<div style="height: 30px; background: transparent url('/media_stat/images/layout/999966.gif') repeat-x left center;"><!--  --></div>
							</div>
						<% } %>
						
						
						<div id="prodReqNonContent">
							<% if (identity != null) { %>
								<fd:OrderHistoryInfo id='orderHistoryInfo'>
									<%if (orderHistoryInfo.size() > 0) {%>
										<div style="margin-bottom: 16px;">
											<div style="float: left; width: 12px;">&nbsp;</div><div style="float: left; width: 55px; font-weight: bold; padding-right: 10px;">Order #:</div>
											<select class="text12" name="salePK">
												<option value="">Select Order:</option>
												<logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
													<% if (idx.intValue() == 5) break; %>
													<option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %></option>
												</logic:iterate>
											</select>
											&nbsp;(optional)
										</div>
									<% } %>
								</fd:OrderHistoryInfo>
							<% } %>

							<div style="margin-bottom: 16px;">
								<div style="margin-bottom: 8px;">
									<div style="float: left; width: 12px; height: 30px;">&nbsp;</div><div style="float: left; width: 55px; height: 30px; padding-right: 10px;">&nbsp;</div>
										* Please enter your message here:<br />
										<textarea cols="40" rows="5" name="message" onKeyPress="limitText(this, 2048)" onChange="limitText(this, 2048)"><%= body%></textarea>
										<fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'>
											<div class="text11rbold">
												<%=errorMsg%>
											</div>
										</fd:ErrorHandler>
								</div>
							</div>

							<%-- info --%>
							<%if (identity == null) { %>
								<div style="margin-bottom: 16px;">
									<div style="margin-bottom: 4px;">
										<img src="/media_stat/images/template/help/enter_contact_info.gif" width="246" height="9" border="0" alt="ENTER YOUR CONTACT INFORMATION" />&nbsp;&nbsp;&nbsp;<span class="text9">* Required information</span>
									</div>
									<img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" alt="" />
								</div>

								<div style="margin-bottom: 32px;">
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">* E-mail Address/User Name:</div><input type="text" class="text11" name="email" size="34" value="<%=email%>" />&nbsp;<fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">* First Name:</div><input type="text" class="text11" name="first_name" size="21" value="<%=firstName%>" />&nbsp;<fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">* Last Name:</div><input type="text" class="text11" name="last_name" size="21" value="<%=lastName%>" />&nbsp;<fd:ErrorHandler result='<%=result%>' name='last_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">Home Phone #:</div><input type="text" class="text11" name="home_phone" size="21" value="<%=homePhone%>" maxlength="15" />&nbsp;<span class="text9">Ext.</span>&nbsp;<input type="text" class="text9" name="home_phone_ext" size="4" value="<%=homePhoneExt%>" maxlength="6" />
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">Work Phone #:</div><input type="text" class="text11" name="work_phone" size="21" value="<%=workPhone%>" maxlength="15" />&nbsp;<span class="text9">Ext.</span>&nbsp;<input type="text" class="text9" name="home_phone_ext" size="4" value="<%=workPhoneExt%>" maxlength="6" />
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">Other Phone #:</div><input type="text" class="text11" name="alt_phone" size="21" value="<%=altPhone%>" maxlength="15" />&nbsp;<font class="text9">Ext.</font>&nbsp;<input type="text" class="text9" name="alt_phone_ext" size="4" value="<%=altPhoneExt%>" maxlength="6" />
									</div>
								</div>
							<% }else{ %>
								<div style="margin-bottom: 16px;">
									<div style="margin-bottom: 4px;">
										<img src="/media_stat/images/template/help/review_contact_info.gif"" width="248" height="9" border="0" alt="REVIEW YOUR CONTACT INFORMATION" />
									</div>
									<img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" alt="" />
								</div>

								<div style="margin-bottom: 32px;">
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">E-mail Address/User Name:</div><%=email%>
									</div>
									<div style="margin-bottom: 8px;">
										<div style="width: 50%; text-align: right; font-weight: bold; padding-right: 10px; float: left;">Name:</div><%=firstName%>&nbsp;<%=lastName%>
									</div>
									<div style="margin-bottom: 8px; text-align: center;" class="text10">
										(If this information is incorrect, <a href='/your_account/signin_information.jsp'>click here</a>!)
									</div>
								</div>
							<% } %>

							<div style="margin-bottom: 16px;">
								<div style="width: 50%; text-align: right; float: left;"><a href="/help/index.jsp?home_phone=&home_phone_ext=&alt_phone=&alt_phone_ext=&body="><img src="/media_stat/images/template/help/clear.gif" width="46" height="16" border="0" alt="CLEAR" /></a></div>
								<input type="image" name="sendMessage" src="/media_stat/images/template/help/send_message.gif" style="margin-left: 10px;" width="90" height="16" alt="" />
							</div>
						</div>

						<div style="margin-bottom: 16px;">
							<div style="margin-bottom: 16px;">
								<%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/help_home_hours.html" /><%--END MEDIA INCLUDE --%>
							</div>
							<% if (user.isChefsTable()) { %>
								<div>You may also call toll-free at <fd:IncludeMedia name="/media/editorial/site_pages/chef_contact_serivce_number.html" /></div>
							<% } else { %>
								<div>You may also call us at <%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/contact_serivce_number.html" /></div>
							<% } %>
						</div>
					</div>

					<div style="height: 30px; background: transparent url('/media_stat/images/layout/cccccc.gif') repeat-x left center; clear: both;"><!--  --></div>
					<div style="margin-bottom: 8px;">
						<div style="width: 33%; text-align: center; float: left;">
							<div style="font-weight: bold;"><a href="/help/terms_of_service.jsp">Customer Agreement</a></div>
							<div>
								All the details about what to<br /> expect when you shop with us.
							</div>
						</div>
						<div style="width: 33%; text-align: center; float: left;">
							<div style="font-weight: bold;"><a href="/help/privacy_policy.jsp">Privacy Policy</a></div>
							<div>
								We keep your account secure and<br /> your information private.
							</div>
						</div>
						<div style="width: 33%; text-align: center; float: left;">
							<div style="font-weight: bold;"><a href="javascript:pop('/help/freshness.jsp',335,375)">Our Freshness Guarantee</a></div>
							<div>
								100% satisfaction with every<br/> product, every time.
							</div>
						</div>
						<div style="height: 1px; background: transparent url('/media_stat/images/layout/clear.gif') repeat-x left center; clear: both;"><!--  --></div>
					</div>
				</div>
			</form>
		</fd:ContactFdController>
	</tmpl:put>
</tmpl:insert>
