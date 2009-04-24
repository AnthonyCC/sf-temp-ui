<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%
BrowserInfo bi = new BrowserInfo(request);
%><html>
<head><title>Opaque Box Test</title></head>
<body>
Client info: <span id="ua_div"></span><br/>
BrowserInfo: <%= new BrowserInfo(request) %><br/>
<br>
IE ? <%= bi.isInternetExplorer() %> / &lt;=6 ? <%= bi.isIE6() %><br/>
Firefox ? <%= bi.isFirefox() %><br/>
WebKit ? <%= bi.isSafari() %> / iPhone ? <%= bi.isIPhone() %> / Chrome ? <%= bi.isChrome() %><br/>
Opera ? <%= bi.isOpera() %><br/>
<h2>Enabled</h2>
<fd:OpaqueBox disabled="false">
	<img src="/media_stat/images/thanksgiving/home_turkey.jpg">
	<div>Lorem ipsum</div>
</fd:OpaqueBox>
<h2>Disabled</h2>
<fd:OpaqueBox disabled="true">
	<img src="/media_stat/images/thanksgiving/home_turkey.jpg">
	<div>Lorem ipsum</div>
</fd:OpaqueBox>
<script type="text/javascript">
document.getElementById('ua_div').innerHTML = navigator.userAgent;
</script>
</body>
</html>
