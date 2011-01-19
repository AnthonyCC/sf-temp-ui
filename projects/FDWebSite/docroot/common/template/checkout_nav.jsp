<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<html>
	<head>
    	<title><tmpl:get name='title'/></title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
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
			
			<table width="745" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
					<td bgcolor="#<%=color%>" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"/></td>
					<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"/></td>
					<td width="733" align="center">
						<img src="/media_stat/images/layout/clear.gif" height="15" width="733"/><br/>
			
						<!-- content lands here -->
						
						<tmpl:get name='content'/>
						
						<!-- content ends above here-->
			
						<br/><br/>
					</td>
					<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"/></td>
					<td bgcolor="#<%=color%>" valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
				</tr>
				
				<tr valign="bottom">
					<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve<%=suffix%>.gif" width="6" height="6" border="0"/></td>
					<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"/></td>
					<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve<%=suffix%>.gif" width="6" height="6" border="0"/></td>
				</tr>
				
				<tr>
					<td width="733" bgcolor="#<%=color%>" valign="bottom"><img src="/media_stat/images/layout/clear.gif" width="733" height="1" border="0"/></td>
				</tr>
			</table>
			
			<%@ include file="/common/template/includes/footer.jspf" %>

		</center>
	</body>
</html>
