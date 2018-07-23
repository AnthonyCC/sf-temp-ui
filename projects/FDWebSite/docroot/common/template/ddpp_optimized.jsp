<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%
	//expanded page dimensions
	final int W_DNAV_NO_SPACE_TOTAL = 970;	
%>
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
<%-- 		<title><tmpl:get name='title'/></title> --%>
        <tmpl:get name="seoMetaTag"/>
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
		
		<%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
		      
		<div style="margin: 0 auto 15px auto; width: <%=W_DNAV_NO_SPACE_TOTAL%>px;">
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
					<td width="<%=W_DNAV_NO_SPACE_TOTAL%>" bgcolor="#999966" class="onePxTall"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt="" /></td>
				</tr>
			</table>
		</div>	
	    <div id="content_top" class="content-header container">
		    <tmpl:get name="content-header"/>
	    </div>
			    
		<div class="toolbar container"><div class="toolbar-content"><tmpl:get name="toolbar" /></div></div>
			
	    <div id="main" class="container">
	    	<div id="sidebar">
	    		<%-- spacer above left filter sidebar --%>
				<div id="selection" class="sidebar-content">
	    			<tmpl:get name='selection-header'/>
	    			<tmpl:get name='selection-list'/>
	    		</div>
	        	<tmpl:get name='filterNavigator'/>
	    	</div>
		    <div id="content" class="product-grid">
		    	
			    		<div id="pager-top" class="pager"><tmpl:get name='pagerTop'/></div>
			    		<div class="items"><tmpl:get name='content'/><div class="clear"></div></div>
			    		<div id="pager-bottom" class="pager"><tmpl:get name='pagerBottom'/></div>
			    		<tmpl:get name='recommendations'/>
		    </div>
	    </div>
	    
		<div style="margin: 0 auto; width: <%=W_DNAV_NO_SPACE_TOTAL%>px;">
			<tmpl:get name='deal-carousel-grofrodai' />
	    </div>
	    
		<div style="margin: 0 auto; width: <%=W_DNAV_NO_SPACE_TOTAL%>px;">
			<tmpl:get name='bottom-ads' />
	    </div>
	    
		<div style="margin: 0 auto; width: <%=W_DNAV_NO_SPACE_TOTAL%>px;">
			<tmpl:get name='bottom-media' />
	    </div>
			
	    
		<%@ include file="/common/template/includes/footer.jspf" %>
    	<%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
		<tmpl:get name='customJsBottom'/>
	</body>
</html> 
