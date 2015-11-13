<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.FDSessionUser.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ taglib uri="/WEB-INF/shared/tld/c.tld" prefix="c" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<%@ include file="includes/fk_core_settings.jspf"%>
<%!
public String result = "";
public String errorMsg = "";

public String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"};
public FDSessionUser user;

public String svg_src(String svg_f){
	return IMAGES_DIR + "/s.jsp?f=" + svg_f;
}

public String svg_obj(String svg_f){
	return "<object data=\""+ SVG_SRC + svg_f +"\" type=\"image/svg+xml\"><img src=\""+ SVG_SRC + svg_f +"\" /></object>";
}

public String svg_bg(String svg_f){
	return "background-image:url(" + SVG_SRC + svg_f + ")";
}
%>
<%
String emailAddress = request.getParameter("emailAddress");

//expanded page dimensions (for forget password sections)
final int W_FORGET_PASSWORD_TOTAL = 700;
final int W_FORGET_PASSWORD_CONFIRMATION_TOTAL = 700;
String previousPage;
%>
<!DOCTYPE html>
<html>
	<head>
		<title>A fresh kick <c:out value="${param.f}" /></title>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<!--[if lt IE 9]>
		   <script>
		      document.createElement('header');
		      document.createElement('nav');
		      document.createElement('section');
		      document.createElement('article');
		      document.createElement('aside');
		      document.createElement('footer');
		      document.createElement('figure');
		      document.createElement('figcaption');
		   </script>
		<![endif]-->
		<link href="<%=CSS_DIR %>/foodkick.css" rel="stylesheet" type="text/css" />
		<script src="ad_server_opt.js.jsp"></script>
	</head>
	<body>
		<header></header>
		<nav>
			<ul>
				<li class="mobile mobile_dropdown_master">
					<a href="#" class="mobile_link">
						<img src="<%=SVG_SRC %>hamburger_helper.svg" />
					</a>
					<ul class="dropdown mobile_dropdown">
						<li><a href="#How_it_works">How It Works</a></li>
						<li><a href="#Whats_Good">What's Good</a></li>
						<!--  <li><a href="#MYFOODKICK">#MYFOODKICK</a></li>-->
						<li><a href="#Get_Foodkick">Get Foodkick</a></li>
					</ul>
				</li>
			
				<li class="standard_link"><a href="#How_it_works">How It Works</a></li>
				<li class="standard_link"><a href="#Whats_Good">From The Feed</a></li>
				<li class="logo_link">
					<a href="#slideshow">
						<img src="<%=SVG_SRC %>freshkick_logo_v2.svg" />
					</a>
				</li>
				<!--  <li class="standard_link"><a href="#MYFOODKICK">#MYFOODKICK</a></li> -->
				<li class="standard_link"><a href="#Get_Foodkick">Get Foodkick</a></li>
			</ul>
		</nav>
		<c:choose>
			<c:when test="${param.p == 'forget_password'}">
				<section id="forgot_password_section" class="forgot_password_section">
					<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
					<fd:ForgotPasswordController results="result" successPage='<%=FKAPP_DIR %>/?p=forget_password_confirmation' password="password">	
						<%@ include file="/login/includes/forget_password.jspf" %>
					</fd:ForgotPasswordController>
				</section>
			</c:when>
			<c:when test="${param.p=='forget_password_confirmation'}">
				<section id="forgot_password_confirmation_section" class="forgot_password_section">
					<%@ include file="/login/includes/forget_password_confirmation.jspf" %>
				</section>
			</c:when>
			<!--  
			<c:when test="${param.p == 'retrieve_password'}">
				<fd:CheckLoginStatus id="user" />
				<% String fName = user.getFirstName(); %>
				<section id="forgot_password_retrieve_section" class="forgot_password_section">
					<fd:ForgotPasswordController results="result" successPage='<%=FKAPP_DIR %>/?p=forget_password_confirmation' password="password">	
						<%@ include file="/login/includes/retrieve_password.jspf" %>	
					</fd:ForgotPasswordController>
				</section>
			</c:when>
			-->
			<c:otherwise>
				<%@ include file="includes/index_main.jspf" %>
			</c:otherwise>
		</c:choose>
		<footer>
			 <section id="footer_disclaimer">
			 *This Offer is for free delivery on qualifying orders for a thirty (30) day period. Offer applies to first-time customers only. Free delivery means <span>no delivery or service fees</span>. <span>Eligible orders</span>
must (a) exceed minimum purchase requirements before taxes & fees, (b) be within eligible <span>delivery areas</span>, and (c) have a delivery window greater than one (1) hour. Delivery is subject to
availability. No promotion code necessary and the Offer will automatically apply starting with your first purchase and will continue for 30 days. This is a limited time Offer. All standard customer
terms and conditions apply. FoodKick reserves the right to cancel or modify this Offer at any time. Offer is nontransferable. Void where prohibited. All right reserved, Fresh Direct, LLC.
			</section> 
			
			<div class="stripes"><button class="download_button white">Download the APP</button></div>
			<section id="footer_subsection">
				<section>
					<img src="<%=SVG_SRC %>freshkick_logo_v2.svg" />
					<div>&copy;FRESH DIRECT, LLC</div>
				</section>
				<section>
					<h4>Contact</h4>
					<hr/>
					<p>
						Have a question?<br/>
						<a href="mailto:hello@foodkick.com">hello@foodkick.com</a>
					</p>
					<p>
						Press Inquiries<br/>
						<a href="mailto:press@foodkick.com">press@foodkick.com</a>
					</p>
					<p>
						Want to Partner?<br/>
						<a href="mailto:partnership@foodkick.com">partnership@foodkick.com</a>
					</p>
				</section>
				<section>
					<h4>Info</h4>
					<hr/>
					<p>
						<a href="#">About Us</a><br/>
						<a href="#">FAQ</a>
					</p>
				</section>
				<section class="last">
					<h4>Hang With Us</h4>
					<hr/>
					<ul>
						<li><a style="<%=svg_bg("footer/twitter.svg") %>;" href="http://www.twitter.com/FreshDirect">&nbsp;</a></li>
						<li><a style="<%=svg_bg("footer/instagram.svg") %>;" href="http://www.instagram.com">&nbsp;</a></li>
						<li><a style="<%=svg_bg("footer/facebook.svg") %>;" href="http://www.facebook.com/FreshDirect">&nbsp;</a></li>
						<li class="last"><a style="<%=svg_bg("footer/snapchat.svg") %>;" href="https://www.snapchat.com/">&nbsp;</a></li>
					</ul>
					
					<img src="<%=IMAGES_DIR %>/footer/tiles.jpg" />
				</section>
			</section>
		</footer>
	</body>

	<%-- 
	<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
	--%>
	
	<jwr:script src="/fdlibs.js" useRandomParam="false" /><%-- for jquery and other things --%>
	<script src="<%=JS_DIR %>/jquery.slides.min.js"></script>
	<script src="<%=JS_DIR %>/foodkick.js"></script>
	<%-- <jwr:script src="/foodkick.js" useRandomParam="false" />--%>
	<script type="text/javascript">
		window.IMAGES_DIR = "<%=IMAGES_DIR%>";
		OAS_AD('HPFeatureTop');
		OAS_AD('HPTab2');
		OAS_AD('HPTab3');
	</script>
</html>