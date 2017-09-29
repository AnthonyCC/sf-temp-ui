<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='true' />
<potato:images images="images" productId='${param.productId}' categoryId='${param.catId}'/>
<html lang="en-US" xml:lang="en-US">
	<head>
		<meta charset="utf-8">
		<title>Product Extra Data Test Page</title>
		<style type="text/css">
			textarea.debug {
				width: 100%;
				height: 4em;
			}
			
			code.debug {
				white-space: pre-wrap;      /* CSS3 */   
				white-space: -moz-pre-wrap; /* Firefox */    
				white-space: -pre-wrap;     /* Opera <7 */   
				white-space: -o-pre-wrap;   /* Opera 7 */    
				word-wrap: break-word;      /* IE */
 			}
			
			table.example th {
				font-size: 12px;
			}
			
			.example-desc {
				background-color: #eff;
			}
			
			.example-value {
				text-align: center;
			}
		</style>
		<link rel="stylesheet" href="http://yandex.st/highlightjs/7.4/styles/default.min.css">
		<script src="http://yandex.st/highlightjs/7.4/highlight.min.js"></script>
	</head>
	<body>
		<div>User: <span>${ user.userId }</span></div>

		<textarea class="debug"><fd:ToJSON object="${images}" noHeaders="true"/></textarea>
	</body>
</html>