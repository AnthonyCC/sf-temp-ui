<%@ page autoFlush='false' %>
<%@ page import='com.freshdirect.fdstore.warmup.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<html>
<head>
<title>Warmup</title>
</head>
<body>

<h1>Warmup...</h1>

<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->
<!-- fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooobaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar -->

<%
	boolean newStore = request.getParameter("newStore")!=null;
	if (newStore) {
%>
<h3>Loading new Store</h3>
<%		
		out.flush();
		
		ContentFactory cf = new ContentFactory();
		new Warmup(cf).warmup();
		ContentFactory.setInstance( cf );
		
	} else {
	
		out.flush();
		
		Warmup wup = new Warmup();
		wup.warmup();

	}
%>
<p>Warmup done</p>

<a href="/">Go to home page</a>
</body>
</html>
