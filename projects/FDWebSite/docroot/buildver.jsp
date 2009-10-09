<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="javax.management.ObjectName"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.management.MBeanServer"%>
<%@page import="javax.management.MalformedObjectNameException"%>
<%@page import="javax.management.MBeanException"%>
<%@page import="javax.management.AttributeNotFoundException"%>
<%@page import="javax.management.InstanceNotFoundException"%>
<%@page import="javax.management.ReflectionException"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.jar.JarFile"%>
<%@page import="java.util.jar.Manifest"%>
<%@page import="java.util.jar.Attributes"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Build Version Page - List of Deployments and Their Corresponding Versions</title>
</head>
<body>
<%
	InitialContext ctx = new InitialContext();
	MBeanServer connection = (MBeanServer) ctx
			.lookup("java:comp/env/jmx/runtime");
	if (connection != null) {

		try {
			ObjectName rts = new ObjectName(
					"com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
			ObjectName dc = (ObjectName) connection.getAttribute(rts,
					"DomainConfiguration");
			ObjectName[] ads = (ObjectName[]) connection.getAttribute(
					dc, "AppDeployments");

			for (ObjectName ad : ads) {
				String deploymentName = ad.getKeyProperty("Name");
				String sourcePath = (String) connection.getAttribute(
						ad, "AbsoluteSourcePath");
				String version = null;
				if (sourcePath.endsWith(deploymentName)) {
					version = "exploded deployment (developer mode)";
					sourcePath = sourcePath.substring(0, sourcePath.length() - (deploymentName.length() + 1));
					if (sourcePath.endsWith("projects")) {
						sourcePath = sourcePath.substring(0, sourcePath.length() - ("projects".length() + 1));
						String entries = sourcePath + File.separator + ".svn/entries";
						try {
							BufferedReader reader = new BufferedReader(new FileReader(entries));
							String line;
							line = reader.readLine();
							line = reader.readLine();
							line = reader.readLine();
							line = reader.readLine();
							version += " revision " + line;
							line = reader.readLine();
							version += " of repository " + line;
							reader.close();
						} catch (IOException e) {
							version = "unknown repository (failed to process SVN entries)";
						}
					}
				} else if (sourcePath.toLowerCase().endsWith(".ear")) {
					try {
						JarFile jarFile = new JarFile(sourcePath);
						Manifest mf = jarFile.getManifest();
						Attributes.Name key = new Attributes.Name("Build-Id");
						if (mf.getMainAttributes().keySet().contains(key)) {
							version = mf.getMainAttributes().getValue(key);
						} else
							version = "unkown version (missing Build-Id)";
					} catch (IOException e) {
						version = "unknown version (failed to process EAR)";
					}
				} else {
					version = "unknown version";
				}
%>
<%=deploymentName%> &ndash; <span style="color: red"><%=version%></span>
<br>
<%
			}
		} catch (MalformedObjectNameException e) {
%>
<span style="color: red">Trying to query an unknown object type.</span>
<%
		} catch (MBeanException e) {
%>
<span style="color: red">Unknown JMX exception.</span>
<%
		} catch (AttributeNotFoundException e) {
%>
<span style="color: red">Failed to query an attribute (WebLogic JMX node hierarchy might have been changed?).</span>
<%
		} catch (InstanceNotFoundException e) {
%>
<span style="color: red">Failed to retrieve an object (WebLogic JMX node hierarchy might have been changed?).</span>
<%
		} catch (ReflectionException e) {
%>
<span style="color: red">Failed to retrieve an attribute (WebLogic JMX node hierarchy might have been changed?).</span>
<%
		} catch (ClassCastException e) {
%>
<span style="color: red">Unexpected object type (WebLogic JMX node hierarchy might have been changed?).</span>
<%
		} catch (Exception e) {
%>
<span style="color: red">Unknown error.</span>
<%
		}
	} else {
%>
<span style="color: red">Cannot get connection to the JMX repository.</span>
<%
	}
%>
</body>
</html>