<html>
<head>
<title>Snoop Request</title>
</head>
<body bgcolor=#ffffff>

<h2>Snoop request</h2>

<h3>
Requested URL
</h3>

<pre>
<%= HttpUtils.getRequestURL(request) %>
</pre>

<h3>
Request information
</h3>

<pre>
Request Method: <%= request.getMethod() %>
Request URI: <%= request.getRequestURI() %>
Request Protocol: <%= request.getProtocol() %>
Servlet Path: <%= request.getServletPath() %>
Path Info: <%= request.getPathInfo() %>
Path Translated: <%= request.getPathTranslated() %>
Query String: <%= request.getQueryString() %>
Content Length: <%= request.getContentLength() %>
Content Type: <%= request.getContentType() %>
Server Name: <%= request.getServerName() %>
Server Port: <%= request.getServerPort() %>
Remote User: <%= request.getRemoteUser() %>
Remote Address: <%= request.getRemoteAddr() %>
Remote Host: <%= request.getRemoteHost() %>
Authorization Scheme: <%= request.getAuthType() %>
</pre>


<h3>
Request headers
</h3>

<pre>
<%
Enumeration e = request.getHeaderNames();
while (e.hasMoreElements()) {
  String name = (String)e.nextElement();
  out.println(name + ": " + request.getHeader(name));
}
%>
</pre>


<h3>
Request parameters
</h3>

<pre>
<%
e = request.getParameterNames();
while (e.hasMoreElements()) {
  String name = (String)e.nextElement();
  out.println(name + ": " + request.getParameter(name));
}
%>
</pre>


<h3>
Session attributes
</h3>
<pre>
session created : <%= new java.util.Date(session.getCreationTime()) %>
session is new? : <%= session.isNew() %>
SessionId : <%= request.getRequestedSessionId() %>
SessionId from Cookie? : <%= request.isRequestedSessionIdFromCookie() %>
SessionId from URL? : <%= request.isRequestedSessionIdFromURL() %>
SessionId is valid? : <%= request.isRequestedSessionIdValid() %>
<%
e = session.getAttributeNames();
while (e.hasMoreElements()) {
  String name = (String)e.nextElement();
  out.println(name + ": " + session.getAttribute(name));
}
%>
</pre>


<h3>Certificate Information</h3>
<pre>
<%
  try {
	  java.security.cert.X509Certificate[] certs = (java.security.cert.X509Certificate[]) request
      .getAttribute("javax.servlet.request.X509Certificate");

    if (certs != null) {
      java.security.cert.X509Certificate cert = certs[0];
%>
Subject Name : <%= cert.getSubjectDN().getName() %> <br>
Issuer Name :<%= cert.getIssuerDN().getName() %> <br>
Certificate Chain Length : <%= certs.length %> <br>
<%
      // List the Certificate chain
      for (int i=0; i<certs.length;i++) {
%>  Certificate[<%= i %>] : <%= certs[i].toString() %> 
<%
      } // end of for loop
%>
<%
    } 
    else // certs==null 
    {
%>
Not using SSL or client certificate not required in weblogic.properties.
<%
    }
  } catch (ClassCastException cce) {
    System.out.println(cce.getMessage());
    cce.printStackTrace();
  }
%>
</pre>

</body>
</html>
