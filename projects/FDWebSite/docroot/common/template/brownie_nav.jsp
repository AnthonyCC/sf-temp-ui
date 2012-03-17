<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DNAV_TOTAL = 970;
%>
<html xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
    <title><tmpl:get name='title'/></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<fd:css href="/assets/css/brownie_points.css"/>
	
	<fd:css href="/assets/css/TextboxList.css"/>
	
		<fd:javascript src="/assets/javascript/jquery-1.7.1.js" />
	<% if(!"fb".equals(request.getParameter("current"))) { %>	
	
		<script type="text/javascript" src="https://platform.twitter.com/widgets.js"></script>

		<!-- required for TextboxList -->
		<script type="text/javascript" src="/assets/javascript/GrowingInput.js"></script>
					
		<script type="text/javascript" src="/assets/javascript/TextboxList.js"></script>
	
		<script type="text/javascript" src="https://www.plaxo.com/css/m/js/util.js"></script>
		<script type="text/javascript" src="https://www.plaxo.com/css/m/js/basic.js"></script>
		<script type="text/javascript" src="https://www.plaxo.com/css/m/js/abc_launcher.js"></script>
	
		<script type="text/javascript"><!--
			var t2;
			$(function(){
				// With custom adding keys 
				t2 = new $.TextboxList('#form_tags_input', {bitsOptions:{editable:{addKeys: [188,13]}}});
			});
			
			function onABCommComplete() {
			  // OPTIONAL: do something here after the new data has been populated in your text area
			  var eCount = 0;
			  var element = document.getElementById("recipient_list")
			  if(element != null) {
				  var data = element.value;  
				  //window.alert("data:" + data);			  
				  if(data.trim().length > 0) {
					var currentTagTokens = data.split( "," );
					for(i=0;i<currentTagTokens.length;i++) {
						var email = currentTagTokens[i].substring(currentTagTokens[i].indexOf("<") + 1, currentTagTokens[i].indexOf(">"));
						//window.alert("email:"+email);
						t2.add(email);
					}
				  }
				  element.value = "";
			   }
			   //document.getElementById("emailnumber").innerHTML=eCount;
			}
		//--></script>	
<% } %>

<script type="text/javascript" src="/assets/javascript/ZeroClipboard.js"></script>
<script type="text/javascript" src="/assets/javascript/shadedborder.js"></script>

</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333">
<%@ include file="/common/template/includes/globalnav.jspf" %> 		
<CENTER class="text10">
<TABLE WIDTH="<%=W_DNAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
<td width="<%=W_DNAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
</TR>

<TR>
<TD WIDTH="<%=W_DNAV_TOTAL%>">
<%@ include file="/common/template/includes/deptnav.jspf" %></TD>
</TR>
<TR>
<TD WIDTH="<%=W_DNAV_TOTAL%>" BGCOLOR="#999966" COLSPAN="7"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>

<TR VALIGN="TOP">
<TD width="<%=W_DNAV_TOTAL%>" align="center">
<img src="/media_stat/images/layout/clear.gif" height="20" width="<%=W_DNAV_TOTAL%>"><br>
<!-- content lands here -->
<tmpl:get name='content'/>
<!-- content ends above here-->
<br><br></TD>
</TR>

<%-- spacers --%>
<tr valign="top">
	<td><img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_DNAV_TOTAL%>"></td>
</tr>

<TR VALIGN="BOTTOM">
<td width="<%=W_DNAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
</HTML>
