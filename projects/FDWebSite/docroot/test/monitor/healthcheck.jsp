<%@page contentType="text/plain" session="false"%><%@ page import='com.freshdirect.fdstore.monitor.*'%><%
    long start = System.currentTimeMillis();
    FDMonitor.healthCheck();
    out.print(System.currentTimeMillis() - start);
    out.println(" milliseconds");
%>
