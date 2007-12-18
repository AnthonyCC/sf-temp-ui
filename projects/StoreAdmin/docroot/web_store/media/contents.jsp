<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
String servletContext = request.getContextPath();
String currentPage = request.getRequestURI();
String currentDir = currentPage.substring(0, currentPage.lastIndexOf("/")+1);
String params = request.getQueryString();
String URL = request.getRequestURI() + "?" + request.getQueryString();

boolean showDetails = false;
String view = request.getParameter("view");
	if ("details".equalsIgnoreCase(view)){
		showDetails = true;
	}

String view_editLink = currentDir + "view_edit.jsp?type=";
String disp = "&display=";
String trail = "";
%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
	
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center" class="section">
	<tr><td class="breadcrumb"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>Current Folder: <sa:File id="node"><% trail =  path.substring(0,path.lastIndexOf("\\")+1);%></sa:File><%=trail%><br><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td>
	</tr>
	<tr>
	<td>
		<table width="100%" cellpadding="2" cellspacing="0" border="0">
		<tr class="header">
		<td width="40%" class="sectionHeader">&nbsp;Manage Contents</td>
		<td width="10%" align="center" class="tabDetails">25 items</td>
		<td width="50%" align="right" class="tabDetails">Last Modified 8:00 pm 12/01/01 By XY&nbsp;</td>
		</tr>
		<tr>
		<td>
			<table cellpadding="3">
			<tr>
			<td class="menu"><font size="4">*</font> <b>new</b></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td class="menu"><a href="javascript:deleteThis('product','<%=URL%>');"><font size="4">&ndash;</font> <b>delete</b></a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td class="menu"><a href="javascript:popup('pop_move_media.jsp','s');"><font size="4">>></font> <b>move</b></a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			</tr>
			</table>
		</td>
		<td colspan="2" class="colDetails"><% if (showDetails) { %><a href="?view=summary">switch to summary</a><% } else { %><a href="?view=details">switch to details</a><% } %></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr class="colHeader">
	<td width="3%">&nbsp;</td>
	<td width="30%"><a href="#" class="colHeader">Name</a></td>
	<td width="48%" align="center"><a href="#" class="colHeader">Type</a></td>
	<td width="15%"><a href="#" class="colHeader">Last Modified</a></td>
	<td width="4%" align="center"><a href="#" class="colHeader">By</a></td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
	</table>
	<div style="width:100%;left:0;top:0;height:50%;overflow-y:scroll;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">

    <sa:File id="node">
        <%
            String fileName = node.getName();
            String type = "TXT";
            if (fileName.endsWith(".gif") || fileName.endsWith(".jpg")) type = "IMG";
            if (fileName.endsWith(".swf")) type = "SWF";
            String swfPath = path;
            swfPath = swfPath.replace('\\', '/');
        %>
        <tr>
            <td width="3%"><input type="checkbox" name="checkbox" value="checkbox"></td>
            <td width="30%"><a href="<%= view_editLink %><%= type.toLowerCase() %><%=disp%><%=path%>"><%= fileName %></a></td>
            <td width="48%" align="center"><%= type %></td>
            <td width="15%"><%= new java.util.Date(node.lastModified()) %></td>
            <td width="4%" align="center">JA</td>
        </tr>
        <%  if (showDetails) { 
                if ("TXT".equals(type)) {
        %>
        <tr><td colspan="5"><table width="80%" cellpadding="0" cellspacing="0" align="center"><tr><td class="mediaText"><fd:IncludeMedia name="<%= path %>" /></td></tr></table><br>
            <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr></table>
        </td></tr>
        <%      } else if ("IMG".equals(type)) { %>
        <tr><td colspan="5"><table width="80%" cellpadding="0" cellspacing="0" align="center"><tr><td class="mediaText" align="center"><img src="<%= servletContext %><%= path %>"></td></tr></table><br>
            <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr></table>
        </td></tr>
        <% } else if ("SWF".equals(type)) {%>
        <tr><td colspan="5"><table width="80%" cellpadding="0" cellspacing="0" align="center"><tr><td class="mediaText" align="center"><script>
                <% System.out.println("-----------"+servletContext+swfPath); %>
                document.writeln('<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0"><PARAM NAME=movie VALUE="');
                document.writeln('<%= servletContext %><%=swfPath%>');
                document.writeln('"><PARAM NAME=loop VALUE=false> <PARAM NAME=quality VALUE=high><PARAM NAME=scale VALUE=showall> <PARAM NAME=bgcolor VALUE=#FFFFFF><EMBED src="');
                document.writeln('<%= servletContext %><%=swfPath%>');
                document.writeln('" loop=false quality=high scale=showall bgcolor=#FFFFFF TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"></EMBED></OBJECT>');
                </script></td></tr></table><br>
            <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr></table>
        </td></tr>
        <%      }
            } %>
    </sa:File>
	</table>
	</div> <%-- end scroll section --%>
	
	<div>
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td colspan="2">
		<table cellpadding="3">
		<tr class="menu">
		<td><font size="4">*</font> <b>new</b></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td><a href="javascript:deleteThis('product','<%=URL%>');"><font size="4">&ndash;</font> <b>delete</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td><a href="javascript:popup('pop_move_media.jsp','s');"><font size="4">>></font> <b>move</b></a></td>
		</tr>
		</table>
	</td>
	</tr>
	</table>
	</div>
    </tmpl:put>

</tmpl:insert>
