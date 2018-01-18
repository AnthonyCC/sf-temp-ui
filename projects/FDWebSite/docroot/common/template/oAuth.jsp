<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
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
  	<center class="footer">
  		<tr>
		  	<td align="center" class="text11" style="padding-top: 10px">
				<%@ include file="/shared/template/includes/copyright.jspf" %><br />
				<fd:IncludeMedia name="/media/layout/nav/globalnav/footer/after_copyright_footer.ftl">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt="" /><br />
					<a href="/help/privacy_policy.jsp">Privacy Policy</a>
					&nbsp;<font color="#999999">|</font>
					&nbsp;<a href="/help/terms_of_service.jsp">Customer Agreement</a>
					&nbsp;<font color="#999999">|</font>
					&nbsp;<a href="/help/platform_agreement.jsp">Platform Terms of Use</a>
				</fd:IncludeMedia>
			</td>
		</tr>	
	</center>
</body>
</html>
