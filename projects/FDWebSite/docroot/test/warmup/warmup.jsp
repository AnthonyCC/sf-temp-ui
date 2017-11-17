<%@ page import='com.freshdirect.fdstore.warmup.*' %>
<%! static boolean loaded; %>
<html lang="en-US" xml:lang="en-US">
<head>
<title>Warmup</title>
</head>
<body>

<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->
<!-- These comments are needed for the flush. We have to fill up the buffer with rubbish at least. -->

<h1>Warmup...</h1>
<%
	out.flush();
	if (loaded) {
%>
<p>Warmup already executed</p>
<%		
	} else {
		Warmup wup = new Warmup();
		wup.warmup();
		loaded = true;
%>
<p>Warmup done</p>
<%
	}
	out.flush();
%>

<p><a href="/index.jsp">Go to home page</a></p>
<p><a href="/test/smartstore/index.jsp">Go to SmartStore test pages index</a></p>
<p><a href="/test/search/index.jsp">Go to Search test pages index</a></p>
<p><a href="/cms-gwt">Go to CMS editor</a></p>

</body>
</html>
