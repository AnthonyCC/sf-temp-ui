<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet href="debug.xsl" type="text/xsl"?>
<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ page import='org.dom4j.*' %>
<%@ page import='com.freshdirect.fdstore.mail.FDXMLSerializer' %>
<%

Map sessMap = new HashMap();
Enumeration e = session.getAttributeNames();
while (e.hasMoreElements()) {
  String name = (String)e.nextElement();
  sessMap.put( name, session.getAttribute(name) );

}

FDXMLSerializer ser = new FDXMLSerializer();
Document doc = ser.serializeDocument("session", sessMap);
out.print( doc.getRootElement().asXML() );

%>