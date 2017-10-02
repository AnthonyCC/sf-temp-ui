<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="bsh.Interpreter"%>
<%@page import="java.io.PrintStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html lang="en-US" xml:lang="en-US">
<head>
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <title>CMS Cache Invalidation</title>
</head>
<body>

 <form method="post">
    <textarea id="data" rows="16" cols="160"></textarea>
    <input type="submit" value="RUN" id="invalidate_cms"/>
 </form>
</body>
<script type="text/javascript">
$jq(document).ready(function() {
	$jq('#invalidate_cms').click(function(event){
		event.preventDefault();
		$jq('#invalidate_cms').submit();
	});
	$jq('#invalidate_cms').submit(function(event){
		event.preventDefault();
		//Sku:DAI0076451
		var contentKeys = $jq('#data').val();
		contentKeys = contentKeys.split(",");
		console.log('contentKeys: ' + JSON.stringify(contentKeys));
// 		$jq.ajax({
// 			type: "PUT",
// 			url: "/api/cache",
// 			data: JSON.stringify({"contentKeys": contentKeys})
// 		})
// 		.done(function(msg) {
// 			console.log('success');
// 		});
		$jq.post("/api/cache", "data="+JSON.stringify({"contentKeys": contentKeys}),function(data){
			console.log('success');
		});
	});
});
</script>
</html> 
