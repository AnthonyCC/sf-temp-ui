<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>


<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>

<% //expanded page dimensions
final int W_CHECKOUT_NAV_TOTAL = 970;
%>


<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<fd:javascript src="/assets/javascript/timeslots.js"/>
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<fd:css href="/assets/css/timeslots.css"/>
		<%@ include file="/shared/template/includes/ccl.jspf" %>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	
		<jwr:style src="/atp.css"/>
		
		<!--  Added for Password Strength Display -->
	    <script type="text/javascript" src="/assets/javascript/jquery-2.1.0.min.js"></script>
		<script type="text/javascript" src="/assets/javascript/jquery.hint.js"></script>
		<script type="text/javascript" src="/assets/javascript/jquery.pwstrength.js"></script>
		<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
		
		<script type="text/javascript">
	        jQuery(function($) { $('#password1').pwstrength(); });
	  	 </script>
	    <!--  Added for Password Strength Display -->
	    
	    <!--  Added for Password Strength Display -->
	    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
		<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
		<!--  Added for Password Strength Display -->
</head>
	
	<!--[if lt IE 9]><body class="ie8"><![endif]-->
	<!--[if gt IE 8]><body class="ie9"><![endif]-->
	<!--[if !IE]><!--><body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" class="text10"><!--<![endif]-->
	
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	
			<center>
		
			<%
			boolean modOrder = false;
			boolean standingOrder = false;
			String color = "999966";
			String suffix = "";
			
			FDUserI tmplUser = (FDUserI) session.getAttribute( SessionName.USER );
			FDCartModel tmplCart = tmplUser.getShoppingCart();
			
			if (tmplCart instanceof FDModifyCartModel) {
				modOrder = true;
		        color = "6699CC";
		        suffix = "_blue";
			} 
			if (tmplUser.getCheckoutMode() != EnumCheckoutMode.NORMAL) {
				// STANDING ORDER
				standingOrder = true;		
		       	color = "996699";
		       	suffix = "_purple";
			}
			
			%>

			<%@ include file="/common/template/includes/checkoutnav.jspf" %> 
			
			<table width="<%=W_CHECKOUT_NAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
					<td width="<%=W_CHECKOUT_NAV_TOTAL%>" align="center">
						<img src="/media_stat/images/layout/clear.gif" height="15" width="<%=W_CHECKOUT_NAV_TOTAL%>"/><br/>
			
						<!-- content lands here -->
						
						<tmpl:get name='content'/>
						
						<!-- content ends above here-->
			
						<br/><br/>
					</td>
				</tr>
				
				<tr valign="bottom">
					<td width="<%=W_CHECKOUT_NAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"/></td>
				</tr>
				
				<tr>
					<td width="<%=W_CHECKOUT_NAV_TOTAL%>" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" width="<%=W_CHECKOUT_NAV_TOTAL%>" height="1" border="0"/></td>
				</tr>
			</table>
			
			<%@ include file="/common/template/includes/footer.jspf" %>
			
			<%-- JavaScript modules including carousel logic --%>
			<tmpl:get name='soypackage'/>
			<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
			<jwr:script src="/fdmodules.js"  useRandomParam="false" />
			<jwr:script src="/fdcomponents.js"  useRandomParam="false" />

		</center>
	</body>
</html>
