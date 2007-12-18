<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "site_access");
	request.setAttribute("listPos", "CategoryNote");	      
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
	<head>
		<title><tmpl:get name='title'/></title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>                        
	</head>
	
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
	
	<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
	<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
	    		
		<div align="center"><br>
			<table border="0" cellspacing="0" cellpadding="0" width="720">                
				  <tr>
					<td align="center" colspan="3">
						<img src="/media_stat/images/template/site_access/zip_logo_lg.gif" width="227" height="55" alt="Fresh Direct!" border="0">
						<br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>						
					</td>
				</tr>         
                 <tr>                
                     <td align="center" colspan="3">
                        <SCRIPT LANGUAGE=JavaScript>
			                <!--
	                        OAS_AD('CategoryNote');
			                //-->
	                 	</SCRIPT><br>
                     </td>
                </tr>
				<tr>
					<td align="center" colspan="3">
						<table cellpadding="0" cellspacing="0" border="0">
							
							<tr valign="top">
								<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
								<td align="center" colspan="3">
									<!-- content lands here -->
									<tmpl:get name='content'/>
									<!-- content ends above here-->
								</td>
								<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
							</tr>
														
						</table>
					</td>
					
				</tr>
				<tr>
					<td align="center" colspan="3" class="text12">
						<br><b>Current customer? <a href='/login/login_main.jsp'>Click here to log in</a>.</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">
					</td>
				</tr>
					
				<tr>
					<td colspan="3" align="center">	
						<%	int choose = (int)(Math.random() * 4.0); %>  							
						<img src="/media_stat/images/template/site_access/food_<%=choose%>.jpg" width="300" height="160" alt="FreshDirect" border="0">
					</td>
				</tr>
				<tr>
					<td align="center" class="text12" colspan="3">
						
						<img src="/media_stat/images/template/site_access/zip_subhead.gif" width="355" height="11" alt="OUR FOOD IS FRESH. OUR CUSTOMERS ARE SPOILED." border="0" vspace="6"><br>It's food shopping at its best. Order on the web today and get next-day delivery of the best<br>food at the best price, exactly the way you want it, with 100% satisfaction guaranteed.<br>
						
						<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
						<img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"><br>
						<%@ include file="/shared/template/includes/copyright.jspf"%>
						<br><br><br>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
