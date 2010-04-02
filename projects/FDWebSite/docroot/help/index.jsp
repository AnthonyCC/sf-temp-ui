<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.cms.ContentNodeI" %>
<script language="javascript">
function submitForm(){
	
}
</script>
<fd:CheckLoginStatus />

<%  FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>

<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	
	
	FDIdentity identity  = user.getIdentity();
        ErpCustomerInfoModel cm = null;
        if(identity != null){
            cm = FDCustomerFactory.getErpCustomerInfo(identity);
	}

        String faqSections = FDStoreProperties.getFaqSections();
%>		

<%
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

<% String[] checkContactForm = {"subject", "message", "email", "first_name", "last_name", "home_phone"}; %>

<fd:ErrorHandler result='<%=result%>' field='<%=checkContactForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
    <form method="post" name="contact_fd">
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" width="670">
	<tr VALIGN="TOP">
	    <TD CLASS="text11" colspan="5">
		<span class="title16">Welcome to Help</span><br>
	    </td>	    
        </TR>
		<tr>
			<td colspan="5">
				<br/>
				<%
				boolean loyaltyHelpContact = false;
				%>
				<%@ include file="/shared/help/i_loyalty_banner.jspf"%>
			</td>
		</tr>
        <TR>
            <TD colspan="5">
	    	<br>
	    	    <img src="/media_stat/images/layout/cccccc.gif" width="670" height="1"><br>
	    	<br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="2">
	    </td>
	    
        </tr>
			     
	<tr>
	<TD valign="top" width="225">
					<a href="/help/faq_home.jsp?page=faqHome"><img src="/media_stat/images/template/help/hdr_quick_links.gif" width="119" height="15" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
					Find answers to our most frequently asked questions.<br>
					<br>
					<font class="text11bold">
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/your_account/order_history.jsp">Check the status of your order</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/your_account/order_history.jsp">Change or cancel your order</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/search.jsp">Find a product</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/your_account/signin_information.jsp">Change your password</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/your_account/manage_account.jsp">Change delivery info</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/your_account/manage_account.jsp">Change credit card info</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					
		<!--<%	if(user.isDepotUser()){%>	
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=deliveryDepot">Depot Delivery</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
		<%}%>-->
					</font><p/><br/>
					<a href="/help/faq_home.jsp?page=faqHome"><img src="/media_stat/images/template/help/hdr_faqs.gif" width="45" height="14" border="0" alt="FAQs"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
					Find answers to our most frequently asked questions.
					
					<% List savedList=(List)pageContext.getAttribute("savedFaqs"); %>
					<% if(null !=savedList && savedList.size()>0){ %>
					<p/><b>Top Questions this Week:</b><br/>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<%  if(null != faqSections){
				  StringTokenizer st = new StringTokenizer(faqSections,",");
				  while (st.hasMoreTokens()) { String nextToken=st.nextToken().trim();%>
					
					<logic:iterate id="topfaq" indexId="idx" collection="<%= savedList %>" type="com.freshdirect.fdstore.content.Faq">
					<table>
					<% if(nextToken.equalsIgnoreCase((String)topfaq.getParentNode().getContentKey().getId())){ %>
					<tr><td><img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;</td><td><a href="/help/faq_home.jsp?page=<%= (String)topfaq.getParentNode().getContentKey().getId()%>#<%= (String)topfaq.getContentKey().getId()%>"><%= topfaq.getQuestion() %></a></td></tr>
					
					<%} %>
					</table>			
					</logic:iterate>
					<% } } %>										
					<% } %>										
					<p/><b>Learn More</b><br/>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<font class="text11bold">
				<% 
				  if(null != faqSections){
				  StringTokenizer st = new StringTokenizer(faqSections,",");
				  while (st.hasMoreTokens()) {
				  ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
				  if(null !=contentNode){%>
				  <img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=<%= contentNode.getContentKey().getId()%> "><%= contentNode.getCmsAttributeValue("name") %></a><br>
				  <img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br> 
				  <% }}
				}%>
						
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=what_we_do">What We Do</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=signing_up">Signing Up</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=security">Security &amp; Privacy</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=shopping">Shopping</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=payment">Payment</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=deliveryHome">Home Delivery</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=cos">Corporate Delivery</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=chefstable">Chef's Table</a><br>-->
<!--					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>-->
		
		<!--<%	if(user.isDepotUser()){%>	
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=deliveryDepot">Depot Delivery</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
		<%}%>-->
					
<!--					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=inside">Jobs &amp; Corporate Info</a>-->
					</font>
					
					<p/><br/><b>Search our FAQs</b><br/>
					<table><tr>
					<td><input type="text" class="search" value="" maxlength="100" style="width: 100px;" name="searchFAQ" /></td>
					<td><input type="image" name="searchFAQButton" style="border: 0pt none ; padding: 3px; width: 35px; height: 14px;" src="/media_stat/images/template/search/search_find_button.gif" onclick="submitForm()"/></td>
					</tr></table>
	    </td>
	    
	    <TD valign="top" align="CENTER" width="40">
	 	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="600"><br>
	    </td>	    
		
		<TD valign="top" width="370" colspan="3">
		            <a href='index.jsp'><img src="/media_stat/images/template/help/hdr_contact_us.gif" border="0" width="204" height="14" alt="CONTACT US"></a>
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
	            	    FreshDirect Customer Service is standing by to answer your questions, seven days a week.  <b>The best way to get help is through email.</b> Please select an order number and include as much specific information as possible to ensure a prompt response to your inquiry.<br>
	            	    <br>
                        
                        <img src="/media_stat/images/template/help/enter_message.gif" width="120" height="9" border="0" alt="ENTER YOUR MESSAGE">&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br>
						<img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" VSPACE="3"><br>
						<font class="space4pix"><br></font>						
     <table border="0" cellspacing="0" cellpadding="2" width="360" >
		    			
						
					    <tr valign="middle">
						    <td  align="right" width="100" class="lineItems">*&nbsp;Subject:&nbsp;</td>
							<td >
							<select class="text12" name="subject">
							<option value="">Select Subject:</option>
							<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
								<option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
							</logic:iterate>
							</select>
							&nbsp;<fd:ErrorHandler result='<%=result%>' name='subject' id='errorMsg'><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
							
						</tr>
							
						<%if (identity != null) {%>
						<fd:OrderHistoryInfo id='orderHistoryInfo'>
							<%if(orderHistoryInfo.size() > 0){%>
									<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" alt="" border="0"></td></tr>		
								    <tr>
									    <td align="right" width="10%" class="lineItems">Order #:&nbsp;</td>
										<td>
										<select class="text12" name="salePK">
											<option value="">Select Order:</option>
											<logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
											<% if (idx.intValue() == 5) break; %>
											<option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %>
											</logic:iterate>
										</select>&nbsp;(optional)
										</td>
									</tr>			
							<%}%>		
						</fd:OrderHistoryInfo>	
						<%}%>		
								
								    <tr valign="middle">
									     <td></td>
									    <td class="lineItems">
										<br>
										* Please enter your message here:
										<textarea cols="40" rows="5" name="message" onKeyPress="limitText(this, 2048)" onChange="limitText(this, 2048)"><%=body%></textarea><br>
										<fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
										</td>
									</tr>
						            <tr>
						                <td></td>

						            </tr>
								</table>
                        <br>
	            	    <br>
	            	    <%-- info --%>
<%if(identity == null){%>
                <table border="0" cellspacing="0" cellpadding="2" width="100%">
                    <tr valign="TOP">
                        <td width="100%"><img src="/media_stat/images/template/help/enter_contact_info.gif"" width="246" height="9" border="0" alt="ENTER YOUR CONTACT INFORMATION">&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br>
				<img src="/media_stat/images/layout/999966.gif" width="100%" height="1" border="0" VSPACE="3"><br><font class="space4pix"><br></font>
			</td>
                    </tr>
                </table>

		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td width="180" align="right" class="lineItems">* E-mail Address/User Name&nbsp;</td>
				<td><input type="text" class="text11" name="email" size="30" value="<%=email%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			</tr>
                        <tr>
			    <td align="right" class="lineItems">* First Name&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="first_name" size="21" value="<%=firstName%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">* Last Name&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="last_name" size="21" value="<%=lastName%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='last_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler> </td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Home Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="home_phone" size="21" value="<%=homePhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="home_phone_ext" size="4" value="<%=homePhoneExt%>" maxlength="6"></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Work Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="work_phone" size="21" value="<%=workPhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="home_phone_ext" size="4" value="<%=workPhoneExt%>" maxlength="6"></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Other Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="alt_phone" size="21" value="<%=altPhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="alt_phone_ext" size="4" value="<%=altPhoneExt%>" maxlength="6"></td>
			</tr>
		</table>
<%}else{%>
		<table border="0" cellspacing="0" cellpadding="2" width="100%">
                    <tr valign="TOP">
                        <td width="100%"><img src="/media_stat/images/template/help/review_contact_info.gif"" width="150" height="9" border="0" alt="REVIEW YOUR CONTACT INFORMATION">&nbsp;&nbsp;&nbsp;<br>
				<img src="/media_stat/images/layout/999966.gif" width="100%" height="1" border="0" VSPACE="3"><br><font class="space4pix"><br></font>
			</td>
                    </tr>
		</table>
                
                <table border="0" cellspacing="0" cellpadding="2">
                    <tr>
                        <td width="190" align="right" class="lineItems" valign='bottom'><b>E-mail Address/User Name:&nbsp;</b></td>
                        <td valign='bottom'><%=email%></td>
                    </tr>
                    <tr>
                        <td align="right" class="lineItems" valign='bottom'><b>Name:&nbsp;</b></td>
                        <td valign='bottom'><%=firstName%>&nbsp;<%=lastName%></td>
                    </tr>
                    <tr>
                        <td colspan='2' align='center'><font class='text10'>(If this information is incorrect, <a href='/your_account/signin_information.jsp'>click here</a>!)</font></td>
                    </tr>
                </table>
<%}%>
		<br>
		<img src="/media_stat/images/layout/FF9933.gif" width="100%" height="1" border="0" vspace="4">
		<table border="0" cellspacing="0" cellpadding="2" width="100%">
			<tr valign="right">
				<td width="25%"></td>
				<td width="50%" align="center">
				<a href="/help/index.jsp?home_phone=&home_phone_ext=&alt_phone=&alt_phone_ext=&body="><img src="/media_stat/images/template/help/clear.gif" width="46" height="16" border="0" alt="CLEAR"></A>&nbsp;&nbsp;
				<input type="image" src="/media_stat/images/template/help/send_message.gif" width="90" height="16"></td>
				<td width="25%"></td>
			</tr>
		</table>
                        <%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/help_home_hours.html" /><%--END MEDIA INCLUDE --%>
                        <% if(user.isChefsTable()){ %>
                        <br/>You may also call toll-free at <fd:IncludeMedia name="/media/editorial/site_pages/chef_contact_serivce_number.html" /><br><%--END MEDIA INCLUDE --%>
                        <% } else { %>
                        <br/>You may also call us at <%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/contact_serivce_number.html" /><br><%--END MEDIA INCLUDE --%>
                        <% } %>
                       <%-- </br>You may also call <%=user.isChefsTable()?"toll-free":"us"%> at <%=user.getCustomerServiceContact()%>.<br> --%>
	            	    <br>
	            	    <%--For more information on our <b>Corporate and Commercial Services</b>, <a href="mailto:service@freshdirect.com">click here</a>--%>
	    </td>
	</tr>
	<tr>
	    <TD colspan="5">
		<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
		    <img src="/media_stat/images/layout/cccccc.gif" width="670" height="1"><br>
		<br>	
	    </td>
	</tr>
	<tr><TD colspan="5">
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
		<tr VALIGN="TOP">
					<TD align='center' width="33%">					
					<b><a href="terms_of_service.jsp">Customer Agreement</a></b>
					<br>
					All the details about what to<br/> expect when you shop with us.<br>
					<br></td>
					<td align='center' width="33%"><b><a href="/help/privacy_policy.jsp">Privacy Policy</a></b>
					<br>
					We keep your account secure and<br/> your information private.<br>
					<br></td>
					<td align='center' width="33%"><b><a href="javascript:pop('/help/freshness.jsp',335,375)">Our Freshness Guarantee</a></b>
					<br>
					100% satisfaction with every<br/> product, every time.
	    			</td>
		</tr>
		</TABLE>
	</td></tr>
				
</TABLE>
</form>
</fd:ContactFdController>
</tmpl:put>
</tmpl:insert>


					
