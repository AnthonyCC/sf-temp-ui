<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.storeapi.ContentNodeI" %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus />
<features:isActive name="selfcredit" featureName="backOfficeSelfCredit" />
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

	boolean loyaltyHelpContact = false;

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav.jsp";
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/help/index.jsp" : request.getAttribute("sitePage").toString();

	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put> --%>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Help" pageId="index_help" includeSiteSearchLink="true"/>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<% if (mobWeb) { %>
			<%-- TITLE --%>
			<h2 class="welcome-msg-title-small bold">Help</h2>
			
			<%-- SEARCH --%>
			<div class="search-cont">
				<fd:ContactFdController result="result" successPage=''>
					<form method="post" name="contact_fd" id="contact_fd_faq">
						<span class="icon-search"></span>
						<label for="search-faq" class="offscreen">search</label>
						<input type="text" id="search-faq" class="search-field fnt-18" value="" maxlength="100" name="searchFAQ" placeholder="Search" autocomplete="off" />
					</form>
				</fd:ContactFdController>
			</div>
			<%-- MAIN CONTENT --%>
			<div>
				<ul class="mm-listview cleanli">
					<%-- <li><a href="#" onclick="FreshDirect.components.ifrPopup.open({ url: '/help/contact_fd.jsp?overlay=true', width: 320, height: 400});">Contact Us (overlay)</a></li> --%>
					<li><a href="/help/contact_fd.jsp">Contact Us</a></li>
					<%--
						<li>
							<a href="#" class="gen-accord-toggler gen-accord-toggler-gray noClickThrough">Contact Us (inline)</a>
							<div class="gen-accord-content" style="display: none;">
								<div style="background-color: #fff;">
									<%@ include file="/help/i_contact_us.jspf" %>
								</div>
							</div>
						</li>
					--%>
					<li>
						<a href="#" class="gen-accord-toggler gen-accord-toggler-gray noClickThrough">FAQ</a>
						
						<% if (null != faqSections) { 
							%><ul class="mm-listview cleanli gen-accord-content" style="display: none;"><%
							
							StringTokenizer st = new StringTokenizer(faqSections,",");
								
							while (st.hasMoreTokens()) {
								ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
								if (null != contentNode) { 
									%><li class="">
										<a class="" href="/help/faq_home.jsp?page=<%= contentNode.getContentKey().getId()%> "><%= contentNode.getCmsAttributeValue("name") %></a>
									</li><% 
								}
							}
							%></ul><%
						} %>
					</li>
					<li><a href="/help/freshness.jsp">Freshness Guarantee</a></li>
					<li>
						<a href="#" class="gen-accord-toggler gen-accord-toggler-gray noClickThrough">Food Safety</a>
						<%
							CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNode("food_safety_freshdirect");
							if (cat != null && !cat.getSubcategories().isEmpty()) {
								%><ul class="mm-listview cleanli gen-accord-content" style="display: none;"><%
								for (CategoryModel subcat : cat.getSubcategories()) {
									%><li><a href="/browse.jsp?id=<%= subcat %>"><%= subcat.getFullName() %></a></li><%
								}
								%></ul><%
							}
						%>
					</li>
					<li><a href="#" data-component="self-credit-open-button">Request a Credit</a></li>
					<%-- NO TARGET, comment out for now <li><a href="#">Legal</a></li> --%>
				</ul>
			</div>
			
		<% } else { %>
			<div id="help-page">
				<%-- TITLE --%>
				<div class="welcome-msg-title-small bold">Welcome to Help</div>
				<%-- LOYALTY SECTION --%>
				<div class="bordered-bottom">
					<br /><%@ include file="/shared/help/i_loyalty_banner.jspf"%>
				</div>
				
				<%-- LEFT COLUMN --%>
				<div class="help-aside fl-left">
					
					<%-- SEARCH --%>
					<form method="post" name="contact_fd" id="contact_fd_faq">
						<h2 class="help-heading bold uppercase">Search</h2>
						<label for="search-faq" class="offscreen">search</label><input type="text" id="search-faq" class="search" value="" maxlength="100" name="searchFAQ" />
						<button class="cssbutton green small" type="submit" name="searchFAQButton">Find</button>
					</form>
					
					<%-- QUICK LINKS --%>
					<a href="/help/faq_home.jsp?page=<%= FDFaqUtil.getFaqHomeId() %>"><h2 class="help-heading bold uppercase">quick links</h2></a>
					
					<p>Check below to find the fastest answers to our top customer concerns.</p>
					
					<ul class="orangeDot bold">
						<li><a href="/your_account/order_history.jsp">Change or cancel your order</a></li>
						<li><a href="/your_account/signin_information.jsp">Change your password</a></li>
						<li><a href="/your_account/payment_information.jsp">Change payment info to my account</a></li>
						<li><a href="/your_account/delivery_information.jsp">Update delivery addresses</a></li>
						<li><a href="/your_account/signin_information.jsp">Update account, mobile and email information</a></li>
					</ul>
					
					<c:if test="${selfcredit}">
     					<button class="cssbutton green" type="button" data-component="self-credit-open-button">Request a Credit</button>
					</c:if>
					
					<%-- FAQs --%>
					<a href="/help/faq_home.jsp?page=<%= FDFaqUtil.getFaqHomeId() %>"><h2 class="help-heading bold">FAQs</h2></a>
					
					<p>Scan our Frequently Asked Questions to get info on sign-up, delivery and everything in between.</p>
					
					<%
						List savedList=(List)pageContext.getAttribute("savedFaqs");
		
						if (null !=savedList && savedList.size()>0 && null != savedList.get(0)) {
							%><h3 class="help-subheading">Top Questions</h3><%
							
							if (null != faqSections) {
								%><ul class="orangeDot bold"><%
								
								StringTokenizer st = new StringTokenizer(faqSections,",");
								
								while (st.hasMoreTokens()) {
									String nextToken=st.nextToken().trim();
									%><logic:iterate id="topfaq" indexId="idx" collection="<%= savedList %>" type="com.freshdirect.storeapi.content.Faq"><%
										if (null!=topfaq && null !=topfaq.getParentNode() && nextToken.equalsIgnoreCase((String)topfaq.getParentNode().getContentKey().getId())) { %>
											<li><a href="/help/faq_home.jsp?page=<%= (String)topfaq.getParentNode().getContentKey().getId()%>#<%= (String)topfaq.getContentKey().getId()%>"><%= topfaq.getQuestion() %></a></li>
										<% } %>
									</logic:iterate><%
								} 
								%></ul><%
							}
						}
					%>
	
					<h3 class="help-subheading">Learn More</h3>
					
					<% if (null != faqSections) { 
						%><ul class="orangeDot bold"><%
						
						StringTokenizer st = new StringTokenizer(faqSections,",");
							
						while (st.hasMoreTokens()) {
							ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
							if (null != contentNode) { 
								%><li><a href="/help/faq_home.jsp?page=<%= contentNode.getContentKey().getId()%> "><%= contentNode.getCmsAttributeValue("name") %></a></li><% 
							}
						}
						%></ul><%
					} %>
				</div>
				
				<%-- CONTACT FORM --%>
				<div class="fl-left help-main">
					<%@ include file="/help/i_contact_us.jspf" %>
				</div>
				
				<%-- FOOTER CONTENT --%>
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
		<% } %>

    <script type="application/ld+json">
    { 
      "@context" : "http://schema.org",
      "@type" : "Organization",
      "url" : "https://www.freshdirect.com/",
      "contactPoint" : [
        { "@type" : "ContactPoint",
          "telephone" : <%=StringUtil.quote(user.getCustomerServiceContact())%>,
          "contactType" : "customer service"
        }
      ]
    }
    </script>

	</tmpl:put>
</tmpl:insert>
