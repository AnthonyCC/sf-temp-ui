<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.regex.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.data.*" %>
<%@ page import="org.apache.oltu.oauth2.common.message.OAuthResponse" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<% 
pageContext.setAttribute("clientMap", FDStoreProperties.VENDOR_MAP);
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
OAuth2Service authService = OAuth2Service.defaultService();
String code = request.getParameter("code");
String token = request.getParameter("token");
String phone = request.getParameter("phone");
String item = request.getParameter("item");
if (code != null ){
	OAuthResponse respone = authService.getAccessToken("internalTestClient", "internalTestClientSecret", "authorization_code", "/test/oauth/user.jsp?phone=" + phone, URLDecoder.decode(code.replace("+", "%2B"), "UTF-8"), null);
	String tokenBody = respone.getBody();
	if( tokenBody != null) {
		Pattern pattern = Pattern.compile("access_token=(.*)");
		Matcher matcher = pattern.matcher(tokenBody);
		if(matcher.find())
			token = matcher.group(1);
	}
	if (token != null && !token.isEmpty() && phone!=null) {
		FDStoreProperties.VENDOR_MAP.put(phone, token);
		response.sendRedirect("/test/oauth/client.jsp?phone="+phone+"&item=" + item +"&token=" + token);
	}
}
%>
<html lang="en-US" xml:lang="en-US">
<head>
	<title>OAuth Test Page</title>
	<jwr:script src="/oauth.js" useRandomParam="false" />
	<style>
		.container{
			padding: 5px 0 0 20px;
		}
		.hidden{
			display: none;
		}
		.label {
			width: 110px;
			display: inline-block;
		}
		.label:after {
			content: ':';
		}
	</style>

</head>
	<body class="container">
		<div class="phone">
			<p><div class="label">Phone number</div><input type="tel" class="phone-number" /></p>
			<p><div class="label">Message Body</div><input type="text" class="message-body" /></p>
			<p><button class="send-message" >Send Message </button></p>
			
		</div>
		<div class="shopping-cart">
			<h3>Vendor site</h3>
			<div>Items you are adding</div>
			<ul>
				<li class="shopping-cart-item"></li>
			</ul>
			<button class="add-to-cart" <%=token!=null? "disabled": "" %> ><%=token== null? "Add to cart >" : "Success"%></button>
		</div>
	</body>
		<script>
		window.vendor = {};
		window.vendor.clientMap = <fd:ToJSON object="${clientMap}" noHeaders="true"/>;
		$('.shopping-cart').hide();
		$(document).on('click', '.send-message', function () {
			$('.shopping-cart').show();
			$('.phone').hide();
			$('.shopping-cart-item').html($('.message-body').val());
		});
		$(document).on('click', '.add-to-cart', function () {
			window.location.href = window.vendor.clientMap[$('.phone-number').val()]? '/test/oauth/client.jsp'+'?item='+$('.message-body').val()+'&phone=' + $('.phone-number').val() + '&token=' + window.vendor.clientMap[$('.phone-number').val()] : '/oauth/auth.jsp?response_type=code&redirect_uri=/test/oauth/user.jsp' + encodeURIComponent('?item='+$('.message-body').val()+'&phone=' + $('.phone-number').val()) + '&client_id=internalTestClient'
		});
		$('.message-body, .phone-number').keypress(function (e) {
		  if (e.which == 13) {
		    $('.send-message').click();
		  }
		});
	</script>
</html>
