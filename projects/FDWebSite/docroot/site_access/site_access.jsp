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
	String zipcode = NVL.apply(request.getParameter("zipcode"), "");
    String serviceType=NVL.apply(request.getParameter("serviceType"), "");
    String corpZipcode = NVL.apply(request.getParameter("corpZipcode"), "");
    String corpServiceType=NVL.apply(request.getParameter("corpServiceType"), "");
    
    if (successPage == null) {
  		// null, default to index.jsp
  		successPage = "/index.jsp";
 	}
    
    
    
    if (successPage.startsWith("/index.jsp") && EnumServiceType.CORPORATE.getName().equalsIgnoreCase(corpServiceType))  {
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
    
    //check for new serviceType, and if either GC or RH is enabled
	String gcLanding = FDStoreProperties.getGiftCardLandingUrl();
	String rhLanding = FDStoreProperties.getRobinHoodLandingUrl();
	boolean isGiftCardEnabled = FDStoreProperties.isGiftCardEnabled();
	boolean isRobinHoodEnabled = FDStoreProperties.isRobinHoodEnabled();

	/* service type is "WEB" AND either of GC/RH is enabled check successPage */
    if ( EnumServiceType.WEB.getName().equalsIgnoreCase(serviceType)) {
        String successPageFinal="";
		if (successPage.indexOf(rhLanding)>-1){
            //Successpage is robinhood
            if(isRobinHoodEnabled)
				successPageFinal = rhLanding;
            else
                successPageFinal = "/index.jsp";
		} else	if (successPage.indexOf(gcLanding)>-1){
            //Successpage is giftcard  
            if(isGiftCardEnabled)
				successPageFinal = gcLanding;
            else
                successPageFinal = "/index.jsp";
		} 
		if ("".equalsIgnoreCase(successPageFinal)) {
            System.out.println("Inside else ");
			//success page has not been set, default to giftcard
            if(isGiftCardEnabled)
				successPageFinal = gcLanding;
            else
                successPageFinal = "/index.jsp";
		}
        //Set the success page
        successPage = successPageFinal;
	
		//moreInfoPage = successPageFinal;
		//failurePage = successPageFinal;
	}
	
    
    //--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "site_access");
request.setAttribute("listPos", "CategoryNote");
    
	String siteAccessPage = request.getParameter("siteAccessPage");
	if(siteAccessPage == null){
		siteAccessPage = "aboutus";
	}
	//String actionURI = "/site_access/site_access.jsp";
	String actionURI = request.getRequestURI()+"?siteAccessPage="+siteAccessPage+"&successPage="+successPage;

	if (request.getParameter("newRequest") == null) {
		response.sendRedirect(response.encodeRedirectURL("/site_access/site_access_lite.jsp?successPage="+successPage));
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="verify-v1" content="2MXiorvt33Hqj6QEBylmr/TwpVMfiUQArG0etUIxV2c=" />
		<meta name="description" content="Online grocer providing high quality fresh foods and popular grocery and household items at incredible prices delivered to the New York area.">
		<meta name="keywords" content="FreshDirect, Fresh Direct, Online Groceries, Online food, Grocery delivery, Food delivery, New York food delivery, New York food, New York grocer, Online grocery shopping">
		<title><%= isBestCellars ? "Best Cellars" : "FreshDirect"%></title>
		<script language="javascript" src="/assets/javascript/prototype.js"></script>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<script language='javascript'>
			function validate(){
				var zipcode = document.forms[0].zipcode.value;
				var corpZipCode = document.forms[0].corpZipcode.value;
				if(zipcode !='' && corpZipCode == ''){
					if(zipcode.length!=5 || isNaN(zipcode)){
						document.forms[0].action = '<%=actionURI%>';
					}else{
						document.forms[0].action = "/site_access/site_access.jsp";
					}
					document.forms[0].corpServiceType.value = '';
				}else{
					if(corpZipCode.length != 5 || isNaN(corpZipCode)){
						document.forms[0].action = '<%=actionURI%>';
					}else{
						document.forms[0].action = "/site_access/site_access.jsp";
					}
				}
				
				return true;
			}
			function gcValidate() {
				var gcZip = $('gc_<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>').value;
				if (gcZip.length == 5 && !isNaN(gcZip)) {
					$('site_access_gc').action = "/site_access/site_access.jsp";
				}else{
					$('site_access_gc').action = '<%=actionURI%>';
				}
				$('site_access_gc').submit();
				
			}
		</script>
	</head>
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20" onLoad="window.document.site_access_corp.<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>.focus();">
		
	<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		
		<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
			 <div align="center">
				<table border="0" cellspacing="0" cellpadding="0" width="745">
					<tr>
						<td width="555" valign="top">
							<table valign="top" border="0" cellspacing="0" cellpadding="0" width="555">
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td></tr>
								<tr>
									<td align="left"><img src="/media/editorial/site_access/images/siteaccess_header.gif" width="551" height="49" alt="Our food is fresh. Our customers are spoiled." border="0"><br></td>
								</tr>
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="10"><br></td></tr>
								<tr><td>
								<img src="/media/editorial/site_access/images/siteaccess_<%= siteAccessPage %>_rev.gif" width="550" height="20" alt="" border="0" usemap="#siteAccessNav" vspace="0">
								<map name="siteAccessNav" id="siteAccessNav">
									<area shape="rect" coords="0,0,85,20" href='<%= "/about/index.jsp?siteAccessPage=aboutus"%>' alt="About Us" />
									<area shape="rect" coords="90,0,210,20" href='<%= "/help/delivery_info.jsp?siteAccessPage=delivery&successPage="+successPage%>' alt="Delivery Info" />
									<area shape="rect" coords="215,0,315,20" href='<%= "/about/plant_tour/index.jsp?siteAccessPage=tour"%>' alt="Take A Tour" />
									<area shape="rect" coords="320,0,425,20" href='<%= "/about/tips.jsp?siteAccessPage=tips&successPage="+successPage%>' alt="Tips & Tricks" />
									<area shape="rect" coords="430,0,550,20" href='<%= "/about/testimonial.jsp?siteAccessPage=testimonials&successPage="+successPage%>' alt="Testimonials" />
								</map>
								</td></tr>
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="196" height="20"></td></tr>
								<tr>
									<td>
										<!-- content lands here -->
										<tmpl:get name='content'/>
										<!-- content ends above here-->
									</td>
								</tr>
							</table>
						</td>
						<td valign="top"><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
						<td width="180" valign="top">
							<table valign="top" border="0" cellspacing="0" cellpadding="0" width="180">
								<tr valign="top">
									<td valign="top" align="center"><font class="text12"><b>Current customer? <a href='<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>'>Log In</a></b></font></td>
								</tr>
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="180" height="10"></td></tr>
							</table>
							<table valign="top" border="0" cellspacing="0" cellpadding="0">
								<form name="site_access_corp" method="post" onsubmit="return validate();">
									<input type="hidden" name="successPage" value="<%= successPage %>">
									<input type="hidden" name="siteAccessPage" value="<%= siteAccessPage %>">
									<input type="hidden" name="newRequest" value="pass" />
									<tr>
										<td align="center" colspan="5"><img src="/media/editorial/site_access/images/zipcheck_side.gif" width="179" height="163"></td>
									</tr>
									<tr>
										<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="165" height="5"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
										<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
									</tr>
									<tr valign="middle">
										<input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>">
										<td colspan="3" align="center" class="text10bold">
											<% if ( !"WEB".equals(serviceType) ) { %>
												<% if ( result.hasError("technicalDifficulty") ) { %>
													<font class="text11rbold"><%=result.getError("technicalDifficulty").getDescription() %></font><br /><br />
												<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
													<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %></font><br /><br />
												<%}%>
											<% } %>
											HOME ZIP CODE:<br>
											<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
											<input class="text11" type="text" size="13" style="width: 122px" maxlength="5" value='<%= zipcode %>' name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="1">
											<img src="/media_stat/images/layout/clear.gif" width="1" height="6">				
										</td>
									</tr>
									<tr>
										<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="165" height="5"></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
										<td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="15"></td>
									</tr>
									<tr valign="middle">
										<input type="hidden" name="corpServiceType" value="<%= EnumServiceType.CORPORATE.getName()%>">
										<td colspan="3" align="center" class="text10bold">CORPORATE ZIP CODE:<br>
											<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
											<input class="text11" type="text" size="13" style="width: 122px" maxlength="5" value='<%= corpZipcode %>' name="corpZipcode" required="true" tabindex="1"><br>
											<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
											<input type="image" src="/media_stat/images/template/site_access/go.gif" width="39" height="21" name="site_access_home_go" border="0" value="Check My Area" alt="GO" hspace="4" tabindex="2"><br>
											<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
											<img src="/media_stat/images/template/site_access/truck_zipcheck.jpg"  width="111" height="80" ><br>
											<img src="/media_stat/images/layout/999966.gif" width="70%" height="2" vspace="10"><br>
											<font class="text12"><br><b>Current customer?<br> <a href='<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>'>Click here to log in</a>.</b></font>	
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
									<tr><td align="center" colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
									<tr><td align="center" colspan="5"><img src="/media/editorial/site_access/images/site_access_questions.jpg" width="132" height="196"></td></tr>
									<tr><td align="center" colspan="5"><font class="text11">E-mail us at<br><a href="mailto:service@freshdirect.com">service@freshdirect.com</a><br>or call 1-212-796-8002</font></td></tr>
									<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="20"></td></tr>
									</form>
							</table>
							<% if (FDStoreProperties.isGiftCardEnabled() || FDStoreProperties.isRobinHoodEnabled()) { %>
								<table cellpadding="0" cellspacing="0" border="0">
									<form name="site_access_gc" id="site_access_gc" method="post" action="/site_access/site_access.jsp">
									<input type="hidden" name="successPage" value="<%= successPage %>">
									<input type="hidden" name="serviceType" value="<%= EnumServiceType.WEB.getName()%>">
									<input type="hidden" name="newRequest" value="pass">
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
											<% if ( "WEB".equals(serviceType) ) { %>
												<% if ( result.hasError("technicalDifficulty") ) { %>
													<font class="text11rbold"><%=result.getError("technicalDifficulty").getDescription() %></font><br /><br />
												<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
													<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %></font><br /><br />
												<%}%>
											<% } %>
											To get started, please<br /> Enter your zip code:<br /><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br /><input class="text11" type="text" size="13" style="width: 122px" value="<%= zipcode%>" maxlength="5" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" id="gc_<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="3"></form><br />
											<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br />
											<input type="image" src="/media_stat/images/template/site_access/go.gif" width="39" height="21" name="site_access_gc_go" border="0" value="Check My Area" alt="GO" tabindex="4" onclick="gcValidate();" />
										</td>
									</tr>
										<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_lft_crnr_purp.gif" width="7" height="8"></td>
										<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td>
										<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/layout/qs_bottom_rt_crnr_purp.gif" width="7" height="8"></td>
									</tr>
									<tr>
										<td bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
									</tr>
									<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="50"></td></tr>
									
								</table>
							<% } %>
						</td>
					</tr>
				</table>
			</div>	
		
		<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>
		</fd:SiteAccessController>
	</body>
</html>
