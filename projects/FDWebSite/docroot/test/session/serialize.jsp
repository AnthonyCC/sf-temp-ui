<?xml version="1.0" encoding="UTF-8"?>
<%@page import="com.thoughtworks.xstream.XStream" contentType="application/xml; charset=UTF-8"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ObjectOutputStream"%><%

	XStream xs = new XStream();
	String name = request.getParameter("object");
	if (name != null) {
	    Object object = session.getAttribute(name);
	    
	    ObjectOutputStream xmlOutput = xs.createObjectOutputStream(out);
		xmlOutput.writeObject(object);
	    xmlOutput.close();
	} else {
	    %><object-stream>not found object</object-stream><%
	}

%>
