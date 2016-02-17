<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus />

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String nextSuccesspage = (String)session.getAttribute("nextSuccesspage");
	
	Boolean tcAgree = (Boolean)session.getAttribute("fdTcAgree");

	String successPage = "index.jsp";
	String navPage = NVL.apply(request.getParameter("successPage"), "");
	if(!"".equals(navPage)) {
		successPage = navPage;
		
	}
	
	if(!"".equals(navPage)&&"noIndex".equals(navPage)){
		nextSuccesspage=navPage;
	}
	
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
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
	
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js">
	</script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.1/jquery-ui.min.js">
     </script>
      
	<script type="text/javascript" async>
		function asyncPixelWithTimeout() {
			var img = new Image(1, 1);
			img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26206&pcv=46';
			setTimeout(function () { if (!img.complete) img.src = ''; /*kill the request*/ }, 33);
		};
		asyncPixelWithTimeout();
	</script>

    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/base/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/common/footer.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/freshdirect.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav_and_footer.css">

  
  <link rel="stylesheet" type="text/css" href="/assets/css/global.css">
  <link rel="stylesheet" type="text/css" href="/assets/css/pc_ie.css">
	
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/i_head_end.jspf" %> 

	
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	

    
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0" style="">



	<center>
	<% if (tcAgree!=null){ %>
		<script language="javascript">
		if(typeof top.FreshDirect.terms !== "undefined"){
				top.FreshDirect.terms=<%=tcAgree.booleanValue()%>;
		}
		</script>
	<%} %>
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
			if(typeof top.FreshDirect.terms !== "undefined"){
					top.FreshDirect.terms=true;
			}
			</script>
			<%if(nextSuccesspage==null) {%>
			<script language="javascript">
				window.top.location="/index.jsp";
			</script>
			<%}else if("nonIndex".equals((String)session.getAttribute("nextSuccesspage"))){%>
			<script language="javascript">
				if(typeof window.top.Modalbox.hide() === "undefined"){
					
					this.top.close();
				}else{
					
					window.top.Modalbox.hide();
				}
			</script>
			
			<%}else {%>
			<script language="javascript">
			
				window.top.location="<%=nextSuccesspage%>";
			</script>
			<%} %>
					
		<%		 
		} else if(session.getAttribute("TCAGREE_COMPLETE") != null){
			
		%>
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			<script language="javascript">
			if(typeof top.FreshDirect.terms !== "undefined"){
				top.FreshDirect.terms=true;
			}
			if(typeof window.top.Modalbox.hide() === "undefined"){
				window.top.location='/login/index.jsp';
				this.top.close();
			}else{
		
				window.top.Modalbox.hide();
			}
				
			</script>
					
		<%
			
		}else {

			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
			<script>
			function popupOpener(url) {
			    window.open(url, "_blank", "top=100, left=500, width=400, height=500");
			   
			}
			</script>

		
		<div id="form_feilds" style="float:auto;">
			<form id="litetcaccept" name="litetcaccept" method="post"  style="padding: 0; margin: 0;">

				<input type="hidden" name="socialNavPage" value="<%= socialNavPage %>" />
				<input type="hidden" name="successPage" value="<%= successPage %>" />
				<input type="hidden" name="nextSuccesspage" value="<%= nextSuccesspage %>" />
				
				<input type="hidden" name="litetcaccept" value="true" />
				
				<table border="0" width="360" cellpadding="0" cellspacing="0">
					<tr><td class="bodyCopySUL" style="padding-top: 10px;"><label >Hello <%=user.getFirstName()+" "+user.getLastName() %>, Our Customer Agreement & Privacy Policy have changed</label>  </td></tr>
					<br/>
					<tr><td class="SystemMessage" style="padding-top: 10px;" >By selecting "I Agree" you agree to the updates to our Customer Agreement and Privacy Policy. If you wish to view or print the new Customer Agreement or Privacy Policy, tap or click the "View Customer Agreement" or "View Privacy Policy" button below.
					 </td></tr>
					
					<tr><td style="padding-top: 4px;"></td></tr>
					<tr><td style="padding-top: 4px;"></td></tr>
					<tr><td style="padding-top: 4px;"></td></tr>
					<tr>
					<td style="padding-top: 10px;"> <a href="#" onClick="popupOpener('/registration/user_agreement.jsp')" style="display: inline-block; width: 300px;padding: 10px; margin-top: 0px; border:1px solid #009900; text-decoration: none; background-color: #ffffff; color: #009900; font-size: 13.33px; text-align: center; border-radius: 2px; margin-left: 20px;"><b>View Customer Agreement</b></a></td>
					</tr>
					<tr>
					<td style="padding-top: 10px;"> <a href="#" onClick="popupOpener('/registration/privacy_policy.jsp')" style="display: inline-block; width: 300px;padding: 10px; margin-top: 0px; border:1px solid #009900; text-decoration: none; background-color: #ffffff; color: #009900; font-size: 13.33px; text-align: center; border-radius: 2px; margin-left: 20px;"><b>View Privacy Policy</b></a></td>
					</tr>
					<tr>
					<td style="padding-top: 10px;">	<a href="#" onclick="document.litetcaccept.submit();" style="display: inline-block; width: 300px;padding: 10px; margin-top: 0px; text-decoration: none; background-color: #009900; color: #ffffff; font-size: 13.33px; text-align: center; border-radius: 2px; margin-left: 20px;"><b>I Agree</b></a></td>
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
