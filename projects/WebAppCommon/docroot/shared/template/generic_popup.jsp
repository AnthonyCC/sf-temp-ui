<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%

String name = "";
String contentPath = "";
String windowSize = "";


// Default setting for small pop up.
String wSize = "375,335";
String headerImg = "/media_stat/images/layout/pop_up_header_sm.gif";
String tableWidth = "330";
String cellWidth = "155";
boolean customWidth = false;
Integer customMiddleWidth = 0;


if(request.getParameter("name") !=null){
	name = request.getParameter("name");
}

if(request.getParameter("contentPath") !=null){
	contentPath = request.getParameter("contentPath");
}

if(request.getParameter("windowSize") !=null){
	windowSize = request.getParameter("windowSize");
}

if(windowSize.equalsIgnoreCase("large")){
	wSize = "400,585";
	headerImg = "/media_stat/images/layout/pop_up_header_lg.gif";
	tableWidth = "520";
	cellWidth = "275";
}
if(windowSize.equalsIgnoreCase("custom")){
	headerImg = "/media_stat/images/layout/pop_up_header_sm.gif";
	tableWidth = request.getParameter("w");
	cellWidth = "";
	customWidth = true;
	//calc based on custom width - the width of the logo and close images
	customMiddleWidth = Integer.parseInt(tableWidth)-129-32;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html lang="en-US" xml:lang="en-US">
<head>
      <%--   <title> <%=name%></title> --%>
         <fd:SEOMetaTag title="<%=name%>"/>
        <%@ include file="/common/template/includes/metatags.jspf" %>
        <%@ include file="/common/template/includes/i_javascripts_browse.jspf"%>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>

<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333"  onLoad="window.focus()">


<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="<%=tableWidth%>">
	<tr valign="top">
		<% if (!customWidth) { %>
			<td width="<%=tableWidth%>" colspan="3">
        <a href="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}"><img src="<%=headerImg%>" border="0" alt="freshdirect (close window)"></a><br>
				<font class="space10pix"><br></font>
			</td>
		<% }else{ %>
			<td width="">
				<a href="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}"><img src="/media_stat/images/layout/pop_up_header_custom_left.gif" border="0" alt="freshdirect (close window)"></a><br>
				<font class="space10pix"><br></font>
			</td>
			<td width="<%=customMiddleWidth%>">
				<a href="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}"><img src="/media_stat/images/layout/pop_up_header_custom_middle.gif" height="43" width="<%=customMiddleWidth%>" border="0" alt="freshdirect (close window)"></a><br>
				<font class="space10pix"><br></font>
			</td>
			<td width="">
				<a href="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}"><img src="/media_stat/images/layout/pop_up_header_custom_right.gif" border="0" alt="freshdirect (close window)"></a><br>
				<font class="space10pix"><br></font>
			</td>
		<% } %>
	</tr>
	<tr valign="top">
		<td height="230" colspan="3">
			<!-- content lands here -->

			<fd:IncludeMedia name='<%=contentPath%>'/>

			<!-- content ends above here-->
		</td>
	</tr>
</table>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="<%=tableWidth%>">
<TR VALIGN="TOP">
	<TD WIDTH="<%=cellWidth%>">&nbsp;<A HREF="#top">Back to top</A></TD>
	<TD WIDTH="<%=cellWidth%>" ALIGN="RIGHT">&nbsp;<A HREF="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}">close window</A></TD>
</TR>
</TABLE>

</CENTER>
<%@ include file="/common/template/includes/i_jsmodules.jspf"%>
</BODY>
</HTML>
