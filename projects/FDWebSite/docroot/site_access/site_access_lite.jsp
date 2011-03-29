<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<% 	
	boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	String successPage = request.getParameter("successPage");
	String zipcode = NVL.apply(request.getParameter("zipCode"), "");
	String serviceType=NVL.apply(request.getParameter("serviceType"), "");
	String corpZipcode = NVL.apply(request.getParameter("corpZipCode"), "");
	String corpServiceType=NVL.apply(request.getParameter("corpServiceType"), "");
	
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
    
	String siteAccessPage = request.getParameter("siteAccessPage");
	if(siteAccessPage == null){
		siteAccessPage = "aboutus";
	}
	String actionURI = "/site_access/site_access_lite.jsp?successPage="+URLEncoder.encode(successPage);
	
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="verify-v1" content="2MXiorvt33Hqj6QEBylmr/TwpVMfiUQArG0etUIxV2c=" />
		<meta name="description" content="Online grocer providing high quality fresh foods and popular grocery and household items at incredible prices delivered to the New York area.">
		<meta name="keywords" content="FreshDirect, Fresh Direct, Online Groceries, Online food, Grocery delivery, Food delivery, New York food delivery, New York food, New York grocer, Online grocery shopping">
		<title><%= isBestCellars ? "Best Cellars" : "FreshDirect"%></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<script language='javascript'>
		function validate(){
			var zipcode = document.forms[0].zipcode.value;
			var corpZipCode = document.forms[0].corpZipcode.value;
			if(zipcode!='' && corpZipCode==''){
				if(zipcode.length!=5 || zipcode==''){
					document.forms[0].action = '<%=actionURI%>';
				}else{
					document.forms[0].action = "/site_access/site_access.jsp";
				}
			}else{
				if(corpZipCode.length!=5 || corpZipCode==''){
					document.forms[0].action = '<%=actionURI%>';
				}else{
					document.forms[0].action = "/site_access/site_access.jsp";
				}
			}
			document.forms[0].submit();
			return true;
		}
	</script>
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20" onLoad="window.document.site_access_corp.<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>.focus();">
		
	<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
    
    
	<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
		 <div align="center">
			<table valign="top" border="0" cellspacing="0" cellpadding="0" width="700">
				<tr valign="top">
					<td valign="top" align="right" ><font class="text12"><b>Current customer? <a href='<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>'>Log In</a></b></font></td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="1"></td></tr>
			</table>
			<br>
			<table border="0" cellspacing="0" cellpadding="0" width="600">
				<tr><td align="left"><img src="/media_stat/images/template/site_access/siteaccess_header.gif" width="551" height="49" alt="Our food is fresh. Our customers are spoiled." border="0"><br></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"><br></td></tr>
				<tr><td align="left"><img src="/media/editorial/site_access/images/siteaccess_aboutus_mangrs.jpg" width="550" height="220" alt="Managers" border="0"><br></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"><br></td></tr>
				<tr><td align="left"><font class="text12"><b>Before we direct you to our store, please enter your ZIP code and hit "Go" to<br>
				see if you are within our delivery area. We will automatically redirect you to the<br>page you requested. </b></font></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"><br></td></tr>
			</table>
			<br>
			<% if ( result.hasError("technicalDifficulty") ) { %>
				<font class="text11rbold"><%=result.getError("technicalDifficulty").getDescription() %></font><br /><br />
			<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
				<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %></font><br /><br />
			<%}else if ( result.hasError(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()) ) { %>
				<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()).getDescription() %></font><br /><br />
			<%}%>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td>
				<table width="350" border="0" cellspacing="0" cellpadding="0">
					<form name="site_access_corp" method="post" action="<%= actionURI %>">
						<input type="hidden" name="successPage" value="<%= successPage %>">
						<tr>
							<td align="center" colspan="5"><img src="/media/editorial/site_access/images/zipcheck_lite_header.gif" width="350" height="57"></td>
						</tr>
						<tr>
							<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
							<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
							<td><img src="/media_stat/images/layout/clear.gif" width="336" height="5"></td>
							<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
							<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
						</tr>
						<tr valign="middle">
	                    	<td align="center" colspan="3" class="text12">
	                    	<table>
	                   			<tr><td>
	                  				<table width="160" align="center" border="0" cellspacing="0" cellpadding="0">
										<tr><td><img src="/media_stat/images/layout/clear.gif" width="160" height="5"><br></td></tr>
										<tr valign="middle">
											<input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>">
											<td align="center" class="text10bold">HOME ZIP CODE:
												<br>
												<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
												<input class="text11" type="text" size="13" style="width: 122px" maxlength="5" value='<%= zipcode %>' name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" tabindex="1">
												<img src="/media_stat/images/layout/clear.gif" width="1" height="10">
											</td>
										</tr>
										<tr><td><img src="/media_stat/images/layout/clear.gif" width="160" height="10"><br></td></tr>
										<tr valign="middle">
											<input type="hidden" name="corpServiceType" value="<%= EnumServiceType.CORPORATE.getName()%>">
											<td align="center" class="text10bold">CORPORATE ZIP CODE:<br>
												<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
												<input class="text11" type="text" size="13" style="width: 122px" maxlength="5" value='<%= corpZipcode %>' name="corpZipcode" tabindex="1"><br>
												<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
												<input type="image" src="/media_stat/images/template/site_access/go.gif" width="27" height="16" name="site_access_home_go" border="0" value="Check My Area" alt="GO" hspace="4" tabindex="2"><br>
												<img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>	
											</td>
										</tr>
										
									</table>
	                   				</td>
	                   				<td><img src="/media_stat/images/layout/999966.gif" width="1" height="90"></td>
	                   				<td>
	                   					<table width="160" align="center" border="0" cellspacing="0" cellpadding="0">
											<tr valign="middle">
												<td align="center">
													<img src="/media_stat/images/template/site_access/truck.gif"  width="73" height="47" ><br>
													<font class="text10"><br><b>Current customer?<br> <a href='<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>'>Click here to log in</a>.</b></font>
												</td>
											</tr>
										</table>
	                   				</td>
	                   			</tr>
	                   		</table>
	                    	</td>
						</tr>
						<tr>
							<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_lft_crnr_purp.gif" width="7" height="8"></td>
							<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td>
							<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_rt_crnr_purp.gif" width="7" height="8"></td>
						</tr>
						<tr>
							<td bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
						</tr>
					</form>
				</table>
				</td>
				<% if (FDStoreProperties.isGiftCardEnabled() || FDStoreProperties.isRobinHoodEnabled()) { %>
				<td>
					<img src="/media_stat/images/layout/clear.gif" width="50" height="1">
				</td>
				<td>
				<table cellpadding="0" cellspacing="0" border="0">
					<form name="site_access_gc" method="post" action="<%= actionURI %>">
					<input type="hidden" name="successPage" value="<%= successPage %>">
					<input type="hidden" name="serviceType" value="<%= EnumServiceType.WEB.getName()%>">
					<tr>
						<td align="center" colspan="5"><img src="/media/editorial/site_access/images/giftcards_side.gif" width="179" height="52"></td>
					</tr>
					<tr>
						<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
						<td><img src="/media_stat/images/layout/clear.gif" width="6" height="10"></td>
						<td><img src="/media_stat/images/layout/clear.gif" width="165" height="10"></td>
						<td><img src="/media_stat/images/layout/clear.gif" width="6" height="10"></td>
						<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
					</tr>
					<tr valign="middle">
						<td colspan="3" align="center" class="text10">
							To get started, please<br /> Enter your zip code:<br /><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br /><input class="text11" type="text" size="13" style="width: 122px" value="<%= zipcode%>" maxlength="5" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="3"><br /><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br /><input type="image" src="/media_stat/images/template/site_access/go.gif" width="27" height="16" name="site_access_gc_go" border="0" value="Check My Area" alt="GO" tabindex="4">
						</td>
					</tr>
					<tr>
						<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_lft_crnr_purp.gif" width="7" height="8"></td>
						<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td>
						<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_rt_crnr_purp.gif" width="7" height="8"></td>
					</tr>
					<tr>
						<td bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
					</tr>
					<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="50"></td></tr>
					</form>
				</table>
				</td>
			<% } %>
			</tr>
			</table>
			<table border="0" cellspacing="0" cellpadding="0" width="350">
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"><br></td></tr>
				<tr><td align="center"><font class="text12"><b>Or, <a href="/about/index.jsp?siteAccessPage=aboutus">click here</a> to learn more before you shop. </b></font></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="100"><br></td></tr>
				<tr><td colspan="5" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"><br></td></tr>
			</table>
			<%@ include file="/shared/template/includes/copyright.jspf" %>
		</div>	
	<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>
	</fd:SiteAccessController>
	</body>
</html>