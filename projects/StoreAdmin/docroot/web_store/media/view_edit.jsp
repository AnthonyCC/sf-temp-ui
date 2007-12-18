<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
String path = request.getParameter("display");
String submitURL = "javascript:mediaForm.submit();";
boolean isSwf = (request.getParameter("type")).equals("swf");
boolean isImg = (request.getParameter("type")).equals("img");
boolean isTxt = (request.getParameter("type")).equals("txt");
String mediaType = isImg ? MediaModel.TYPE_IMAGE : MediaModel.TYPE_HTML;
String action = request.getParameter("action")==null ? "create" : request.getParameter("action");
String mediaId = request.getParameter("mediaId");
%>
<sa:MediaController action='<%=action%>' path='<%=path%>' result='result' id='mediaModel' mediatype='<%=mediaType%>' mediaId='<%=mediaId%>'>
<%
String submitButtonText;
if (mediaModel.getPK()==null) { 
	submitButtonText = "SAVE" ;
	action="create";
} else {
	 submitButtonText = "UPDATE";
	 action="update";
}

Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}
%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
	<form name="mediaForm" method="post">
	<input name="action" type="hidden" value="<%=action%>">
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td class="breadcrumb">Current Folder: <%= path.substring(0,path.lastIndexOf("\\")+1)%></td>
		<td align="right">
			<table cellpadding="1" cellspacing="3" border="0">
			<tr align="center" valign="middle">
			<td class="cancel"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			<td class="save"><a href="<%=submitURL%>" class="button">&nbsp;<%=submitButtonText%>&nbsp;</a></td>
			<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
			</table>
		</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
        
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr class="header"><th class="sectionHeader">&nbsp;&nbsp;View/Edit Details</th>
	<th align="right">
		<table width="100%" cellpadding="0">
		<tr>
		<td align="right" class="tabDetails">Created 8:00 pm, 12/01/01 By XY &middot; Last Modified 8:00 pm 12/01/01 By XY</td>
		</tr>
		</table>
	</th>
	</tr>
	</table>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%" rowspan="2">&nbsp;</td>
	<td width="30%">Type</td>
	<td width="50%"><% if (isImg) { %>image<% } else if (isTxt) { %>text<% } else if (isSwf) { %>flash<%}%> (<%= path.substring(path.lastIndexOf("."))%>)</td>
	<td width="20%" rowspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center">
		<td class="add_move" class="button"><a href="#" class="button">&nbsp;GET FILE&nbsp;</a></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td>Name</td>
	<td><input name="mediaPath" type="text" style="width:250px" size="45" class="textbox2" value="<%= path %>"></td>
	</tr>
	<% if (isImg || isSwf)  { %>
	<tr>
	<td rowspan="3">&nbsp;</td>
	<td>Size (KB)</td>
	<td>21</td>
	<td>&nbsp;</td>
	</tr>
	<tr>
	<td>Dimensions</td>
	<td>
		<table cellpadding="0" cellspacing="0" border="0" class="section">
		<tr>
		<td align="right">width:&nbsp;&nbsp;</td>
		<td><input name="imageWidth" type="text" style="width:60px;" size="6" class="textbox2" value="<%=mediaModel.getWidth()%>">&nbsp;px</td>
		<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;height:&nbsp;&nbsp;</td>
		<td><input name="imageHeight" type="text" style="width:60px;" size="6" class="textbox2" value="<%=mediaModel.getHeight()%>">&nbsp;px</td>
		</tr>
		</table>
	</td>
	<td>&nbsp;</td>
	</tr>
	<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="4">
		<table width="100%" cellspacing="0" cellpadding="10" class="contrast" border="0">
		<tr><td align="center">
                <% if (isSwf) {%>
                <% path = path.replace('\\', '/'); %>
                <script>
                document.writeln('<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="280"><PARAM NAME=movie VALUE="');
                document.writeln('<%= servletContext %><%=path%>');
                document.writeln('"><PARAM NAME=loop VALUE=false> <PARAM NAME=quality VALUE=high> <PARAM NAME=scale VALUE=showall> <PARAM NAME=bgcolor VALUE=#FFFFFF> <EMBED src="');
                document.writeln('<%= servletContext %><%=path%>');
                document.writeln('" loop=false quality=high scale=showall bgcolor=#FFFFFF TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"></EMBED></OBJECT>');
                </script>
                <% } else { %>
		<img src="<%= servletContext %><%=path%>">
                <%}%>
		</td></tr>
		</table>
	</td>
	</tr>
	<% } %>
	<% if (isTxt) { %>
	<tr>
	<td rowspan="3">&nbsp;</td>
	<td>No. of Words</td>
	<td>62</td>
	<td>&nbsp;</td>
	</tr>
	<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td colspan="3">
		<table width="100%" cellspacing="0" cellpadding="10" class="contrast" border="0">
		<tr><td align="center">
		<textarea name="textfield" style="width:500px;" cols="20" rows="10" wrap="VIRTUAL" class="textbox1"><fd:IncludeMedia name="<%= path %>" /></textarea>
		</td></tr>
		</table>
	</td>
	</tr>
	<% } %>
	</table>
         
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
		<td class="cancel"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="save"><a href="<%=submitURL%>" class="button">&nbsp;<%=submitButtonText%>&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table>
	</td>
	</tr>
	</table>
	</form>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	<tr class="header"><th colspan="6" class="sectionHeader">&nbsp;&nbsp;Associations</th></tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr class="colHeader">
	<td width="2%">&nbsp;</td>
	<td width="25%">Property/Attribute</td>
	<td width="15%">Type</td>
	<td width="25%">ID</td>
	<td width="35%">Name</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
	</table>
	
	<div style="width:100%;left:0;top:0;height:10%;overflow-y:scroll;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr>
	<td width="2%">&nbsp;</td>
	<td width="25%">brand 1</td>
	<td width="15%">product</td>
	<td width="25%">bblk_flet_untrmd</td>
	<td width="35%">Denmark Blue Castello</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>brand 2</td>
	<td>product</td>
	<td>bblk_fflt_untrmd</td>
	<td>Something something something</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>brand 3</td>
	<td>product</td>
	<td>bblk_flet_untrmd_blah_blah</td>
	<td>Roast Suckling Pig</td>
	</tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	
	</div> <%-- end scroll section --%>
    </tmpl:put>

</tmpl:insert>
</sa:MediaController>