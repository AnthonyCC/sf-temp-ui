<%@ page autoFlush='false' %>
<%@ page import='com.freshdirect.fdstore.warmup.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<html>
<head>
<title>Warmup</title>
</head>
<body>

<h1>Warmup</h1>

<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->

<%		
		out.flush();
		
String q = request.getParameter("q");
if ("WLS".equals(q)) {
	new WLSWarmup().postStart(null);
	%>WLSWarmup started...<%
} else if ("StoreVisitor".equals(q)) {
	boolean excerciseGetters = Boolean.valueOf(request.getParameter("excerciseGetters")).booleanValue();
	boolean validateParent = Boolean.valueOf(request.getParameter("validateParent")).booleanValue();
	new StoreVisitorWarmup(excerciseGetters, validateParent).warmup();
	%>StoreVisitorWarmup started...<%
} else {
	%>
<pre>
Usage:
	?q=WLS
	?q=StoreVisitor&amp;excerciseGetters=true&amp;validateParent=true
</pre>	
	<%
}
%>

</body>
</html>
