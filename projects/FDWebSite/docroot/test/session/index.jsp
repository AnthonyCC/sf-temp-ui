<%@page import="java.util.Enumeration"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ObjectOutputStream"%><%!

  static class CountingStream extends OutputStream {
    int count = 0;
    public void write(int b)  {
        count ++;
    }
    
    public void write(byte[] b, int off, int len) {
        count += len;
    }
    
    public int getCount() {
        return count;
    }
    
    public void reset() {
        count = 0;
    }
}
%>
<%@page import="java.io.OutputStreamWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%><html>
<head>
 <title>Session Dump</title>
</head>
<body>
<table>
 <tr><td style="header" colspan="4">Session Attributes</td></tr>
 <tr>
 	<td style="header">Name</td>
 	<td style="header">Type</td>
 	<td style="header">Size</td>
 	<td style="header">Value</td>
 </tr>
<%
	if (session != null) {
	    CountingStream counter = new CountingStream();
	    for(Enumeration names = session.getAttributeNames(); names.hasMoreElements(); ) {
	    	counter.reset();    
	    
			String name = (String) names.nextElement();
			Object value = session.getAttribute(name);
			String type = value != null ? value.getClass().getName() : "null";
			String valueAsString = value != null ? value.toString() : "null";
			
			try {
			ObjectOutputStream os = new ObjectOutputStream(counter);
			os.writeObject(value);
			os.close();
			} catch (Exception e) {
			    e.printStackTrace();
			    StringWriter outs = new StringWriter();
			    e.printStackTrace(new PrintWriter(outs));
			    valueAsString = outs.toString();
			}
			int count = counter.getCount();
%>
	<tr>
		<td><%= name %></td>
		<td><%= type %></td>
		<td><%= count %></td>
		<td><%= valueAsString %></td>
	</tr>
<%			
	    }
	}
%>
</table>
</body>
</html>
