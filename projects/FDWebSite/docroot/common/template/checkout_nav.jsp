<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>

<% //expanded page dimensions
final int W_CHECKOUT_NAV_TOTAL = 970;
%>


<html>
	<head>
    	<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<fd:javascript src="/assets/javascript/timeslots.js"/>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<fd:css href="/assets/css/timeslots.css"/>
		<%@ include file="/shared/template/includes/ccl.jspf" %>
	</head>
	
	<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" class="text10">
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
				} else if (tmplUser.getCheckoutMode() != EnumCheckoutMode.NORMAL) {
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

		</center>
	</body>
</html>
