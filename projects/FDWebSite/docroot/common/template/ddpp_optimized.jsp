<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%
	//expanded page dimensions
	final int W_DNAV_NO_SPACE_TOTAL = 970;	
%>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		<%@ include file="/common/template/includes/ga_custom_vars.jspf" %>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
	    
	    <tmpl:get name='customCss'/>
	    <tmpl:get name='customJs'/>
		<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	</head>
	<!--[if lt IE 7]><body class="ie ie6 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"><![endif]-->
	<!--[if IE 7]><body class="ie ie7 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
	<!--[if IE 8]><body class="ie ie8 searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>"> <![endif]-->
	<!--[if gt IE 8]><!-->
	<body class="searchpage <tmpl:get name="activeTab"/>-active <tmpl:get name="activeView"/>-view <tmpl:get name="noResult"/> <tmpl:get name="startPage"/>">
	<!--<![endif]-->
		<%@ include file="/shared/template/includes/i_body_start.jspf" %>      
		<%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
		
		<center class="text10">
			<table width="<%=W_DNAV_NO_SPACE_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" width="<%=W_DNAV_NO_SPACE_TOTAL%>" height="5" border="0" alt="" /></td>
				</tr>
				<tr>
					<td width="<%=W_DNAV_NO_SPACE_TOTAL%>">
						<%@ include file="/common/template/includes/deptnav.jspf" %>
					</td>
				</tr>
				<tr>
					<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" /></td>
				</tr>
			</table>
		
		    <div id="content_top" class="content-header container">
			    <tmpl:get name="content-header"/>
		    </div>
				    
			<div class="toolbar container"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
				
		    <div id="main" class="container">
		    	<div id="sidebar">
		        	<tmpl:get name='filterNavigator'/>
		    	</div>
			    <div id="content" class="product-grid">
			    	
				    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
				    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
				    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
				    		<tmpl:get name='recommendations'/>
			    </div>
		    </div>
			<tmpl:get name='deal-carousel-grofrodai' />
			<tmpl:get name='bottom-ads' />
			<tmpl:get name='bottom-media' />
			
		</center>
	    
		<%@ include file="/common/template/includes/footer.jspf" %>
    	<%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	  <tmpl:get name='customJsBottom'/>
	</body>
</html> 
