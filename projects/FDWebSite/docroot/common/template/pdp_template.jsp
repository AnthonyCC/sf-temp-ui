<!DOCTYPE html>

<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>


<html>
<head>
    <title><tmpl:get name='title'/></title>
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE" /> 

	<%@ include file="/common/template/includes/metatags.jspf" %>
	
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
	<tmpl:get name='facebookmeta'/>

  	<jwr:style src="/grid.css"/>
	<jwr:style src="/global.css"/>
	<jwr:style src="/oldglobal.css"/>
  	<jwr:style src="/pdp.css"/>
    <jwr:style src="/quickshop.css"/>
    
	<jwr:script src="/fdlibs.js" useRandomParam="false" />

	<tmpl:get name='extraJs'/>
	<tmpl:get name='nutritionCss'/>
	
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	
</head>

<!--[if lt IE 9]><body class="ie8" data-cmeventsource="pdp_main"><![endif]-->
<!--[if gt IE 8]><body data-cmeventsource="pdp_main"><![endif]-->
<!--[if !IE]><!--><body data-cmeventsource="pdp_main"><!--<![endif]-->

<%@ include file="/shared/template/includes/i_body_start.jspf" %>
    
<%@ include file="/common/template/includes/globalnav.jspf" %> 




<% //expanded page dimensions
final int W_TOTAL = 970;
final int W_LEFTNAV = 150;
final int W_CONTENT = 820;
%>
<center>
<table width="<%=W_TOTAL %>" cellpadding="0" cellspacing="0" border="0" >

	<tr>
		<td width="<%=W_TOTAL%>" colspan="2">
			<!-- start : deptnav -->
			<tmpl:get name='deptnav'/>
			<!-- end : deptnav -->		
		</td>
	</tr>
	
	<tr valign="TOP">
		
			<tmpl:get name='leftnav'/>
			
		<td width="<%=W_CONTENT%>" align="center">			
			<!-- start : content -->
			<tmpl:get name='content'/>
			<!-- end : content -->			
		</td>
	</tr>

</table>
</center>



<%@ include file="/common/template/includes/footer.jspf" %>

<soy:import packageName="pdp" />

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
<jwr:script src="/fdmodules.js"  useRandomParam="false" />
<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
<jwr:script src="/pdp.js"  useRandomParam="false" />

</body>
</html>
