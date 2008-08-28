<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>List of Product having bad Primary Home</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebestyén Gábor">
	<!-- Date: 2008-05-21 -->
</head>
<body>
<%@ page import="java.util.*"
%><%@ page import="com.freshdirect.cms.application.CmsManager"
%><%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"
%><%@ page import="com.freshdirect.cms.*"
%><%@ page import="com.freshdirect.cms.context.*"
%><%@ page import="com.freshdirect.cms.report.HCProducer"
%><%@ page import="com.freshdirect.cms.report.HCWalker"
%><%
CmsManager mgr = CmsManager.getInstance();
ContextService svc = ContextService.getInstance();	


%><%
	String action = request.getParameter("action");
	String dbg = request.getParameter("debug");

	boolean restartFlag = ("restart".equalsIgnoreCase(action));
	boolean killFlag = ("kill".equalsIgnoreCase(action));
	boolean debugFlag = ("1".equalsIgnoreCase(dbg) || "true".equalsIgnoreCase(dbg));


	// find thread
	HCProducer hcp = null;
	
	Object obj = request.getSession().getAttribute("HCProducer");
	if (obj instanceof HCProducer) {
		// special case
		hcp = (HCProducer) obj;
	} else {
		if (obj != null) {
			%>Aye! hcp is not the one I expected: <%= obj != null ? obj.getClass().toString() : "null" %><br/><%
		}
	}

	if (hcp != null && killFlag) {
		hcp.killMe();
	}
	

	if (hcp == null || (hcp != null && hcp.getStatus() == HCProducer.END && restartFlag)) {
		// start a new one
		if (hcp != null && hcp.getStatus() == HCProducer.END && restartFlag) {
			%>Restart in action!<br/><%
		}
		
		hcp = new HCProducer(mgr, svc, debugFlag);
		
		request.getSession().setAttribute("HCProducer", hcp);
		
		// start computing
		new Thread(hcp, "hcproducer").start();
	}
%>
<%
	if (hcp != null) {
		int s = hcp.getStatus();
	
		switch(s) {
			case HCProducer.INIT:
%>
Status: <b>Initializing job</b> <a href="<%= request.getRequestURI() %>">[refresh]</a> <a href="<%= request.getRequestURI() %>?action=kill">[kill]</a><br/>
<%
				break;
			case HCProducer.START:
%>
Status: <b>Starting job ...</b> <a href="<%= request.getRequestURI() %>">[refresh]</a> <a href="<%= request.getRequestURI() %>?action=kill">[kill]</a><br/>
<%
				break;
			case HCProducer.GETTING_PRODUCTS:
%>
Status: <b>Retrieving product nodes ...</b> <a href="<%= request.getRequestURI() %>">[refresh]</a> <a href="<%= request.getRequestURI() %>?action=kill">[kill]</a><br/>
<%
				break;
			case HCProducer.SEARCHING_FOR_BAD_NODES:
%>
Status: <b>Processing node <%= hcp.getProcessedNodes() %> / <%= hcp.getMaxNodes() %> ...</b> <a href="<%= request.getRequestURI() %>">[refresh]</a> <a href="<%= request.getRequestURI() %>?action=kill">[kill]</a><br/>
<div style="width: 200px; height: 18px; border: 2px solid grey; background-color: white;">
	<div style="width: <%= Math.round(((float)(200*hcp.getProcessedNodes()))/hcp.getMaxNodes()) %>px; height: 18px; background-color: green;"></div>
</div>
<%
				break;
			case HCProducer.END:
				List bn = hcp.getBadNodes();
%>
Status: <b>Finished, found <%= bn.size() %> bad nodes.</b> <a href="?action=restart">[restart]</a><br/>
<%
				if (bn.size() > 0) {
%>
<h3>Broken Products</h3>
<div>List of products having hidden or not searchable category in their primary home.</div>
<ol>
<%
					for (Iterator it=bn.iterator(); it.hasNext(); ) {
						ContentNodeI node = (ContentNodeI) it.next();
						%><li><i><%= node.getLabel() %></i> / <%= node.getKey().getId() %></li><%
						
					}
%>
</ol>
<%
				}

				break;
			default:
				break;
		}
		
		if (s == HCProducer.SEARCHING_FOR_BAD_NODES || s == HCProducer.END) {
			Map stats = hcp.getStats();
%>
<h3>Statistics</h3>
<table style="font-size: 12px; color: #333">
  <tr>
    <td>Processed:</td>
    <td><%= hcp.getProcessedNodes() %></td>
  </tr>
  <tr>
    <td>Found bad:</td>
    <td><%= hcp.getBadNodes().size() %></td>
  </tr>
  <tr>
    <td>Nodes have no multiple parents:</td>
    <td><%= stats.get("statNoMultipleParents") %></td>
  </tr>
  <tr>
    <td>Nodes have no primary home:</td>
    <td><%= stats.get("statNoPriHome") %></td>
  </tr>
  <tr>
    <td>Nodes have valid primary home:</td>
    <td><%= stats.get("statPriHomeValid") %></td>
  </tr>
  <tr>
    <td>Nodes have invalid primary home but no valid alternative:</td>
    <td><%= stats.get("statNoValidAltHome") %></td>
  </tr>
</table>
<%
		}
	}
%>
</body>
</html>
