<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus />

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String successPage = "index.jsp";
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
	//System.out.println("\n\n\n"+user.getSelectedServiceType().getName()+"\n\n\n");
	if("".equals(serviceType)) {
		if(user != null) {
			serviceType = user.getSelectedServiceType().getName();
			if("PICKUP".equals(user.getSelectedServiceType().getName()))
				serviceType = "HOME";
		} else {
			serviceType = "HOME";
		}
	}

	
    String failurePage = "/registration/tcaccept_lite.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;
    
   
%>	

<fd:SiteAccessController action='tcAgreed' successPage='<%= successPage %>' moreInfoPage='' failureHomePage='<%= failurePage %>' result='result'>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>FreshDirect</title>
	<style>
		.star {
			color: #F99E40;
		}
		.bodyCopySUL {
			font-size: 11px;
			font-weight: bold;
			padding-top: 4px;
			padding-bottom: 4px;
			
		}
		.bodyCopySULNote {
			color: #808080;
		}
	</style>
	
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.1/jquery-ui.min.js"></script>
       
	<script type="text/javascript" async>
		function asyncPixelWithTimeout() {
			var img = new Image(1, 1);
			img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26206&pcv=46';
			setTimeout(function () { if (!img.complete) img.src = ''; /*kill the request*/ }, 33);
		};
		asyncPixelWithTimeout();
	</script>
<!-- end of dstillery pixel swap  APPDEV-4287 -->

    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/base/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/common/footer.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/freshdirect.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav_and_footer.css">
    
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->
  

  
  <link rel="stylesheet" type="text/css" href="/assets/css/global.css">
  <link rel="stylesheet" type="text/css" href="/assets/css/pc_ie.css">
	
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/i_head_end.jspf" %> 

	
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	

    
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0" style="">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>


	<center>

	<%
		
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String socialNavPage = NVL.apply(request.getParameter("socialnetwork"), "");
		String zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	
		String posn = "right";

		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {

		%>
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			<script language="javascript">
				window.top.location="/index.jsp";
			</script>
					
		<%		 
		} else {

			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
			

		<div class="fright hline" id="" style="width:100%; float:left;"><!-- --></div>
		<div id="form_feilds" style="float:left;">
			<form id="litetcaccept" name="litetcaccept" method="post" action="/registration/tcaccept_lite.jsp" style="padding: 0; margin: 0;">

				<input type="hidden" name="socialNavPage" value="<%= socialNavPage %>" />
				<input type="hidden" name="successPage" value="<%= successPage %>" />
				<input type="hidden" name="litetcaccept" value="true" />
				
				<table border="0" cellpadding="0" cellspacing="0">
					<tr><td class="bodyCopySUL"><span><label>Hello <%=user.getFirstName()+" "+user.getLastName() %> </label> </span> </td></tr>
					<br/>
					<tr><span>Our terms of service have changed. By continuing to use our services, you agree to the recent modification to our terms of services.
					 If you wish to view or print the new terms of services, tap or click "View Terms Of Use" button below. </span> </td></tr>
					


					<tr><td style="padding-top: 4px;"><!-- --></td></tr>
					<tr><td><div class="fright hline" id="" style="width:100%;"><!-- --></div></td></tr>
					
					<td style="padding-top: 10px;">
					<button onclick="document.litetcaccept.submit();" style="width: 100px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center; border-radius: 5px; margin-left: 20px;">
						Continue
					</button>
									
					<button onclick="javascript:popup('/registration/user_agreement.jsp', 'large',alt='')" style="width: 150px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center; border-radius: 5px; margin-left: 20px;">
						View Terms Of Use
					</button>
					</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
		<% if (result.isFailure()) { %>
			<script type="text/javascript">
				setFrameHeightSL('signupframe', $jq('#sulCont').height());
				window.top.Modalbox_setWidthAndPosition();
			</script>
		<% } %>
	<% } %>
	</center>
</body>
</html>

</fd:SiteAccessController>
