<%@ page autoFlush='false' %>
<%@ page import='com.freshdirect.framework.console.*' %>
<html>
<head>
<title>Debug Console</title>
</head>
<body>

<h1>Debug Console</h1>

<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->

<%		
		out.flush();
		new DebugConsole().postStart(null);		
%>

</body>
</html>
