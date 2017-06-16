<%@ include file="fk_presenter_vars.jspf" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ include file="password_flow_vars.jspf" %>
<!DOCTYPE html>
<html>
	<head>
		<title> <tmpl:get name='title'/> </title>
        <%@ include file="/common/template/includes/metatags.jspf" %>
		<meta charset="UTF-8" />
		<link rel="icon" type="image/x-icon" href="<%=IMAGES_DIR %>/favicon2.ico" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
		
		<meta property="og:image" content="https://foodkick.freshdirect.com/media/images/email/foodkick/header_logo.png" />
		<meta property="og:title" content="FoodKick | Same-Day Food Delivery NYC" />
		<meta property="og:description" content="Get what you need in an hour. 30 days FREE delivery. Customizable meals; farm fresh produce, meat, fish and dairy; perfect food and booze pairings from our experts."/>
		<meta property="og:url" content="http://foodkick.com" />  
		
		<!--[if lt IE 9]>
		   <script src="<%=FK_CONFIG_DIRS.get("JS_DIR") %>/modernizr-custom.js"></script>
		   <script src="<%=FK_CONFIG_DIRS.get("JS_DIR") %>/fk_ie8.js"></script>
		<![endif]-->
		<link href="<%=FK_CONFIG_DIRS.get("BOOTSTRAP_DIR") %>/css/bootstrap.min.css" rel="stylesheet" />
		<link href="<%=FK_CONFIG_DIRS.get("CSS_DIR") %>/foodkick.css" rel="stylesheet" type="text/css" />
		<script src="ad_server_opt.js.jsp"></script>
		<script type="text/javascript" src="//libs.coremetrics.com/eluminate.js"></script>
		<script type="text/javascript">cmSetClientID("51640000|33000004",true,"data.coremetrics.com","freshdirect.com");</script>
		<tmpl:get name='header'/>
	</head>
	<body>
		<header></header>
		<nav>
			<div id="locationbar">
				<div id="locationbar_inner">
					<a href="<%=FK_CONFIG_URLS.get("FD_SITE_LINK") %>" class="locabar-tab locabar-tab-fdx-cont"><div class="locabar-tab-fdx"></div></a>
				</div>
			</div>
		
			<ul>
				<li class="mobile mobile_dropdown_master">
					<a href="#" class="mobile_link" id="mobile_link_home">
						<img src="<%=FK_CONFIG_DIRS.get("SVG_SRC") %>hamburger_helper.svg" />
					</a>
					<ul class="dropdown mobile_dropdown" id="mobile_dropdown_menu">
						<li><a href="<%=url_prefix %>#How_it_works">How It Works</a></li>
						<li><a href="<%=url_prefix %>#Featured_Food">Featured Food</a></li>
						<!--  <li><a href="<%=url_prefix %>#MYFOODKICK">#MYFOODKICK</a></li>-->
						<li><a href="about_us.jsp">About Us</a></li>
						<li><a href="<%=url_prefix %>#Get_Foodkick">Get Foodkick</a></li>
						
						<li>
							<a href="<%=FK_CONFIG_URLS.get("FD_SITE_LINK") %>" class="locabar-tab locabar-tab-fdx-cont"><div class="locabar-tab-fdx"></div></a>
						</li>
					</ul>
				</li>
			
				<li class="standard_link"><a href="<%=url_prefix %>#How_it_works">How It Works</a></li>
				<li class="standard_link"><a href="<%=url_prefix %>#Featured_Food">Featured Food</a></li>
				<li class="logo_link">
					<a href="<%=url_prefix %>#slideshow">
						<img src="<%=FK_CONFIG_DIRS.get("SVG_SRC") %>freshkick_logo_v2.svg" />
					</a>
				</li>
				<!--  <li class="standard_link"><a href="<%=url_prefix %>#MYFOODKICK">#MYFOODKICK</a></li> -->
				<li class="standard_link"><a href="about_us.jsp">About Us</a></li>
				<li class="standard_link"><a href="<%=url_prefix %>#Get_Foodkick">Get Foodkick</a></li>
			</ul>
		</nav>

		<tmpl:get name='content'/>

		<a name="footer_anchor" id="footer_anchor"></a>
		<footer>
			<section id="footer_disclaimer">
			 	<tmpl:get name='special_disclaimer'/>
			</section> 
			
			<div class="stripes">
				<%-- 
				<button class="download_button white" onclick="location.href='<%=FK_IOSAPP_DLINK %>'">Download the APP</button>
				--%>
				<%=iosapp_button( (String)FK_CONFIG_URLS.get("FK_IOSAPP_DLINK"), "Download the APP", "white") %>
			</div>
				
			<section id="footer_subsection">
				<section>
					<img src="<%=FK_CONFIG_DIRS.get("SVG_SRC") %>freshkick_logo_recommended_white.svg" />
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
						<a href="mailto:pr@foodkick.com">pr@foodkick.com</a>
					</p>
					<p>
						Want to Partner?<br/>
						<a href="mailto:partners@foodkick.com">partners@foodkick.com</a>
					</p>
				</section>
				<section>
					<h4>Info</h4>
					<hr/>
					<p>
						<a href="about_us.jsp">About Us</a><br/>
						<a href="faq.jsp">FAQ</a>
					</p>
				</section>
				<section class="last">
					<h4>Hang With Us</h4>
					<hr/>
					<ul>
						<li><a style="<%=svg_bg("footer/twitter.svg") %>;" href="https://twitter.com/FoodKick">&nbsp;</a></li>
						<li><a style="<%=svg_bg("footer/instagram.svg") %>;" href="https://instagram.com/foodkick/">&nbsp;</a></li>
						<li><a style="<%=svg_bg("footer/facebook.svg") %>;" href="https://www.facebook.com/FoodKick/">&nbsp;</a></li>
						<li class="last"><a style="<%=svg_bg("footer/snapchat.svg") %>;" href="#footer_anchor">&nbsp;</a></li>
					</ul>
					
					<img src="<%=FK_CONFIG_DIRS.get("IMAGES_DIR") %>/footer/tiles.jpg" />
				</section>
			</section>
		</footer>
	</body>
	
	<jwr:script src="/fdlibs.js" useRandomParam="false" /><%-- for jquery and other things --%>
	<script src="/assets/javascript/jquery/jquery_validate/jquery.validate.js"></script>
	<script src="<%=FK_CONFIG_DIRS.get("JS_DIR") %>/foodkick.js"></script>
	<%-- <jwr:script src="/foodkick.js" useRandomParam="false" />--%>
	
	<tmpl:get name='special_js'/>
</html>