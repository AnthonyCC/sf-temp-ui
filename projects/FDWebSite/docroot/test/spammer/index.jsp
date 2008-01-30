<%@ page import='org.dom4j.*' %>
<%@ page import='com.freshdirect.fdstore.mail.*' %>
<%@ page import='com.freshdirect.framework.mail.XMLEmailI' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<html>
<body>
<%!

	private static final String[] ENTITIES = new String[64];
	static {
		ENTITIES['"'] = "&quot;";
		ENTITIES['<'] = "&lt;";
		ENTITIES['>'] = "&gt;";
		ENTITIES['&'] = "&amp;";
	}
 
	public static void printEscaped(JspWriter out, String str) throws IOException {
    	char[] chrs = str.toCharArray();
    	for (int i=0; i<chrs.length; i++) {
    		char c=chrs[i];
    		if (c<64 && ENTITIES[c]!=null) {
    			out.print(ENTITIES[c]);
    		} else {
    			out.print(c);
    		}
    	}
	}
%>
<%
	String addresses = request.getParameter("addresses");
	if (addresses ==null) {
		addresses = "vszathmary@freshdirect.com";
	}

	String subject = request.getParameter("subject");
	if (subject==null) {
		subject = "FreshDirect";
	}

	String body = request.getParameter("body");
	if (body==null) {
		body = "Hello <b><firstName/></b>!\n<br/><br/>\n";
	}

	if ("POST".equals(request.getMethod())) {

		List customers = new ArrayList();
		StringTokenizer tok = new StringTokenizer(addresses, "\n\r,;");
		while (tok.hasMoreTokens()) {
			String addr = tok.nextToken().trim();
			System.out.println("Finding "+addr);
			/*
			FDCustomerModel fdCust = FDCustomerFactory.getFDCustomerByUserId(addr);
			FDIdentity id = new FDIdentity(fdCust.getErpCustomerPK(), fdCust.getPK().getId());
			FDCustomerInfo custInfo = FDCustomerManager.getCustomerInfo(id);
			*/
			FDCustomerInfo custInfo = new FDCustomerInfo("Dummy", "Dummy");
			custInfo.setEmailAddress( addr );
			custInfo.setHtmlEmail( true );
			customers.add( custInfo );
		}
		
		try {
			Document doc = DocumentHelper.parseText("<body>" + body + "</body>");

			for (Iterator i=customers.iterator(); i.hasNext(); ) {
				XMLEmailI m = FDEmailFactory.getInstance().createGenericEmail( (FDCustomerInfo)i.next(), subject, doc );
				FDCustomerManager.doEmail(m);
			}

		} catch (DocumentException ex) {
			ex.printStackTrace();
			out.println("<font color='red'>"+ex+"</font>");
		}
		
	}
%>
<form action="index.jsp" method="post">

<table width="90%">
<tr>
<td>
<b>To:</b><br/>
<textarea name="addresses" cols="30" rows="10"><%= addresses %></textarea>
</td>

<td>
<b>Subject:</b> <input type="text" name="subject" value="<%= subject %>" />

<br/><br/>

<textarea name="body" cols="80" rows="10"><% printEscaped(out, body); %></textarea>

<br/>

<input type="submit"/>
</td>
</tr>
</table>

</form>
</body>
</html>