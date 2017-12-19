<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>
	<jwr:script src="/oauth.js" useRandomParam="false" />
	<jwr:style src="/oauth.css"></jwr:style>
	<script>
		window.$jq = window.$jq || $;
		window.FreshDirect = window.FreshDirect || {};
	</script>
  <tmpl:get name='css'/>
  <tmpl:get name='js'/>
</head>
<body class="oauth-body">
    <center class="text10">
    	<div>
    		<img src="/media/layout/nav/globalnav/fdx/logo.png" alt="FreshDirect" border="0">
   		</div>
		<tmpl:get name='content'/>
  	</center>
</body>
</html>
