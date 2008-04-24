<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri="freshdirect" prefix="fd" %>


<%
	boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	String successPage = request.getParameter("successPage");
	String zipcode = NVL.apply(request.getParameter("zipCode"), "");
    String serviceType=NVL.apply(request.getParameter("serviceType"), "");
    
	if (successPage == null) {
  		// null, default to index.jsp
  		successPage = "/index.jsp";
 	}
    
    
    
    if (successPage.startsWith("/index.jsp") && EnumServiceType.CORPORATE.getName().equalsIgnoreCase(serviceType))  {
		successPage = "/department.jsp?deptId=COS";
	}
 
	if (successPage.startsWith("/index.jsp") && isBestCellars) {
		successPage = "/department.jsp?deptId=win";
	}
    
    String refProStr="ref_prog_id=";
    if(successPage.indexOf(refProStr)!=-1)
    {
        String refProgId=successPage.substring(successPage.indexOf(refProStr)+refProStr.length(),successPage.indexOf(refProStr)+refProStr.length()+successPage.substring(successPage.indexOf(refProStr)+refProStr.length(),successPage.length()).indexOf("&"));        
        request.setAttribute("RefProgId",refProgId);
    }

	String moreInfoPage = "site_access_address.jsp?successPage="+ URLEncoder.encode(successPage);
	String failurePage = "delivery.jsp?successPage="+ URLEncoder.encode(successPage)+"&serviceType="+serviceType;	
    
    
    //--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "site_access");
request.setAttribute("listPos", "CategoryNote");
    
    
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="verify-v1" content="2MXiorvt33Hqj6QEBylmr/TwpVMfiUQArG0etUIxV2c=" />
		<meta name="description" content="Online grocer providing high quality fresh foods and popular grocery and household items at incredible prices delivered to the New York area.">
		<meta name="keywords" content="FreshDirect, Fresh Direct, Online Groceries, Online food, Grocery delivery, Food delivery, New York food delivery, New York food, New York grocer, Online grocery shopping">
		<title><%= isBestCellars ? "Best Cellars" : "FreshDirect"%></title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>                        
	</head>
	
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20" onLoad="window.document.site_access_home.<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>.focus();">
		<script language="JavaScript">
			var axel = Math.random()+"";
			var a = axel * 10000000000000;
			document.write('<IFRAME SRC=http://fls.doubleclick.net/activityi;src=1495506;type=sitea997;cat=sitea363;ord=1;num='+ a + '? WIDTH=1 HEIGHT=1 FRAMEBORDER=0></IFRAME>');
		</script>
		<noscript>
			<IFRAME SRC="http://fls.doubleclick.net/activityi;src=1495506;type=sitea997;cat=sitea363;ord=1;num=1?" WIDTH=1 HEIGHT=1 FRAMEBORDER=0></IFRAME>
		</noscript>
	<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
		<div align="center"><br>
			<table border="0" cellspacing="0" cellpadding="0" width="720">                
				<tr>
					<td align="center" colspan="3">
						<% if (isBestCellars) { %>
							<img src="/media_stat/images/template/wine/bc_logo_home_original.gif" width="336" height="52" alt="Best Cellars"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="16"><br><img src="/media_stat/images/template/site_access/find_out_if_we_deliver.gif" width="454" height="70" alt="Find out if we're delivering to your neighborhood!" border="0">
						<% } else { %>
							<img src="/media_stat/images/template/site_access/zip_logo.gif" width="436" height="127" alt="Find out if we're delivering to your neighborhood!" border="0">
						<% } %>
						<br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>
						<% if ( result.hasError("technicalDifficulty") ) { %>
							<font class="text11rbold"><%=result.getError("technicalDifficulty").getDescription() %></font><br><br>
						<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %></font><br><br>
						<%}%>
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
					<td align="right">
						<table cellpadding="0" cellspacing="0" border="0">
							<form name="site_access_home" method="post" action="<%= request.getRequestURI() %>">
							<input type="hidden" name="successPage" value="<%= successPage %>">
							<input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>">
							<tr>
								<td align="center" colspan="5"><img src="/media_stat/images/template/site_access/hdr_home_dlv.gif" width="236" height="24"></td>
							</tr>
							<tr>
								<td rowspan="2" bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="212" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td rowspan="2" bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
							</tr>
							<tr valign="middle">
								<td colspan="3" align="center" class="text12">
                                                                    Enter your ZIP CODE here:<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><input class="text11" type="text" size="13" style="width: 122px" value="<%= zipcode%>" maxlength="5" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><input type="image" src="/media_stat/images/template/site_access/go_home.gif" width="39" height="21" name="site_access_home_go" border="0" value="Check My Area" alt="GO" hspace="4" tabindex="2">
                                                                </td>
							</tr>
							
							<tr>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_green_left.gif" width="12" height="11"></td>
								<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_green_right.gif" width="12" height="11"></td>
							</tr>
							<tr>
								<td bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
							</tr>
							</form>
						</table>
					</td>
					<td><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
					<td>
						<table cellpadding="0" cellspacing="0" border="0">
							<form name="site_access_corp" method="post" action="<%= request.getRequestURI() %>">
							<input type="hidden" name="successPage" value="<%= successPage %>">
							<input type="hidden" name="serviceType" value="<%= EnumServiceType.CORPORATE.getName()%>">
							<tr>
								<td align="center" colspan="5"><img src="/media_stat/images/template/site_access/hdr_corp_dlv.gif" width="236" height="24"></td>
							</tr>
							<tr>
								<td rowspan="2" bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="212" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td rowspan="2" bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
							</tr>
							<tr valign="middle">
								<td colspan="3" align="center" class="text12">
                                                                    Enter your ZIP CODE here:<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><input class="text11" type="text" size="13" style="width: 122px" value="<%= zipcode%>" maxlength="5" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="3"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><input type="image" src="/media_stat/images/template/site_access/go_corp.gif" width="39" height="21" name="site_access_corp_go" border="0" value="Check My Area" alt="GO" tabindex="4">
                                                                </td>
							</tr>
							
							<tr>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_orange_left.gif" width="12" height="11"></td>
								<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_orange_right.gif" width="12" height="11"></td>
							</tr>
							<tr>
								<td bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
							</tr>
							</form>
						</table>
					</td>
				</tr>
				<% if (!isBestCellars) { %>
					<tr>
						<td align="center" colspan="3" class="text12">
							<br><b>Current customer? <a href='<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>'>Click here to log in</a>.</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">
						</td>
					</tr>
				<% } %>
					
				<tr>
					<td colspan="3" align="center">
						<% if (isBestCellars) { %>
							<img src="/media_stat/images/template/wine/bc_zipcheck.jpg" width="555" height="156" alt="Best Cellars Wines">
						<% } else { 
							int choose = (int)(Math.random() * 4.0);
						%>
							<img src="/media_stat/images/template/site_access/food_<%=choose%>.jpg" width="300" height="160" alt="FreshDirect" border="0">
						<% } %>
					</td>
				</tr>
				<tr>
					<td align="center" class="text12" colspan="3">
						<% if (!isBestCellars) { %>
							<img src="/media_stat/images/template/site_access/zip_subhead.gif" width="355" height="11" alt="OUR FOOD IS FRESH. OUR CUSTOMERS ARE SPOILED." border="0" vspace="6"><br>It's food shopping at its best. Order on the web today and get next-day delivery of the best<br>food at the best price, exactly the way you want it, with 100% satisfaction guaranteed.<br>
						<% } %>
						<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
						<img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"><br>
						<% if (!isBestCellars) { %>
							<%@ include file="/shared/template/includes/copyright.jspf"%>
						<% } else { %>
							<%@ include file="/includes/copyright_wine.jspf" %>
						<% } %>
						<br><br><br>
					</td>
				</tr>
			</table>
		</div>
		</fd:SiteAccessController>
	</body>
</html>
