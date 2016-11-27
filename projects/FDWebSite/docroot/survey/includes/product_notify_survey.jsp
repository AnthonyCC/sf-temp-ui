<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.customer.ErpCustomerInfoModel'%>
<fd:CheckLoginStatus guestAllowed="true" />
<% 
boolean submitted = request.getParameter("info") != null && request.getParameter("info").indexOf("thankyou") > -1;
String referringPage = request.getRequestURI();

String redirectSuccessPage = NVL.apply(request.getParameter("successPage"), "");
if ("".equals(redirectSuccessPage)) {
    redirectSuccessPage = "/index.jsp";
}

String survey=NVL.apply(request.getParameter("survey"), "Product_Notifications"); //defaults to this survey
String survey_source=NVL.apply((String)request.getAttribute("survey_source"), survey);

String sPage = request.getRequestURI();


FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel cm = (identity!=null)?FDCustomerFactory.getErpCustomerInfo(identity):null;



//see if we know their email already
String custEmail = "";
String custEnteredEmail = NVL.apply(request.getParameter("email"), "");
String custRecogEmail = (cm!=null)?cm.getEmail():"";

if ("".equals(custEnteredEmail)) { //if nothing has been entered ...
	custEmail = custRecogEmail; //try using recognized id
} else { // they entered something ...
	custEmail = custEnteredEmail; //use it regardless
}

//and now the same thing for zip
String custZip = "";
String custEnteredZip = NVL.apply(request.getParameter("zip"), "");
String custRecogZip = (cm!=null)?user.getZipCode():""; //don't use auto detected zip unless customer is recog'd

if ("".equals(custEnteredZip)) { //if nothing has been entered ...
	custZip = custRecogZip; //try using recognized zip
} else { // they entered something ...
	custZip = custEnteredZip; //use it regardless
}

//and now service type
String custType = "";
String custEnteredType = NVL.apply(request.getParameter("custType"), "");
String custRecogType = user.getZPServiceType().toString();

if ("".equals(custEnteredType)) { //if nothing has been entered ...
	custType = custRecogType; //try using recognized type
	if (!"HOME".equals(custType) && !"CORPORATE".equals(custType)) {
		custType = "HOME";
	}
} else { // they entered something ...
	custType = custEnteredType; //use it regardless
}

boolean custTypeHome = true;
if ("CORPORATE".equals(custType)) {
	custTypeHome = false;
}

//and now first name
String custFirstName = "";
String custEnteredFirstName = NVL.apply(request.getParameter("firstName"), "");
String custRecogFirstName = (cm!=null)?cm.getFirstName():"";

if ("".equals(custEnteredFirstName)) { //if nothing has been entered ...
	custFirstName = custRecogFirstName; //try using recognized id
} else { // they entered something ...
	custFirstName = custEnteredFirstName; //use it regardless
}

//and now last name
String custLastName = "";
String custEnteredLastName = NVL.apply(request.getParameter("lastName"), "");
String custRecogLastName = (cm!=null)?cm.getLastName():"";

if ("".equals(custEnteredLastName)) { //if nothing has been entered ...
	custLastName = custRecogLastName; //try using recognized id
} else { // they entered something ...
	custLastName = custEnteredLastName; //use it regardless
}


//and now work number
String custPhone = "";
String custEnteredPhone = NVL.apply(request.getParameter("phone"), "");
String custRecogPhone = (cm!=null)?(cm.getBusinessPhone()==null?"":cm.getBusinessPhone().getPhone()):"";

if ("".equals(custEnteredPhone)) { //if nothing has been entered ...
	custPhone = custRecogPhone; //try using recognized id
} else { // they entered something ...
	custPhone = custEnteredPhone; //use it regardless
}



%>
<fd:CorporateServiceSurvey result='result' actionName='<%=survey%>' successPage='<%=sPage%>'>

<%
	String fd_successPage=NVL.apply((String)request.getAttribute("fd_successPage"), "");
	if (fd_successPage.indexOf("thankyou") > -1 || redirectSuccessPage.indexOf("thankyou") > -1) {
		submitted = true;
	}
%>
<% if (submitted) { %>
	<fd:IncludeMedia name="/media/editorial/site_pages/survey/product_notifications_success.html" />
<% } else { %>
	 	
	<form method="post" name="productNotifySurvey" id="productNotifySurvey" action="<%=request.getRequestURI()%>#survey">
		
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
			    			    
			    <tr><td colspan="4" class="bodyCopySUL"><span><label>Email Address</label><span class="star"> *</span></span></td></tr>
				<tr><td colspan="4"><input type="text" size="35" class="text11ref inputUser" name="email" value="<%=custEmail%>"></td></tr>
				<tr><td colspan="4" class="errMsg"><span class="text11rbold"><fd:ErrorHandler result="<%=result%>" name="email" id="errorMsg"><%=errorMsg%></fd:ErrorHandler>&nbsp;</span></td></tr>
			    
			    <tr><td colspan="4" class="bodyCopySUL"><span><label>Delivery Zip Code</label><span class="star"> *</span></span></td></tr>
				<tr><td colspan="4"><input type="text" size="35" class="text11ref inputUser" name="zip" value="<%=custZip%>"></td></tr>
				<tr><td colspan="4" class="errMsg"><span class="text11rbold"><fd:ErrorHandler result="<%=result%>" name="zip" id="errorMsg"><%=errorMsg%></fd:ErrorHandler>&nbsp;</span></td></tr>
			
				<tr>
					<td class="bodyCopySUL" width="30"><input type="radio" class="custType" name="custType" id="custTypeH" value="HOME" <%= (custTypeHome)?"checked":"" %>></td>
					<td class="bodyCopySUL"><label for="custTypeH">HOME&nbsp;</label></td>
					<td class="bodyCopySUL" width="30"><input type="radio" class="custType" name="custType" id="custTypeC" value="CORPORATE" <%=(!custTypeHome)?"checked":"" %>></td>
					<td class="bodyCopySUL"><label for="custTypeC">OFFICE&nbsp;</label></td>
				</tr>
			    
			</table>
			
			
			<div id="productNotifySurvey-subcontent" style="<%=(!custTypeHome)?"":"display: none; height: 0;" %>">
				
				
				    <div class="bodyCopySUL"><span><label>First Name <span class="bodyCopySUL-nonBold">(optional)</span></label></span></div>
					<div><input type="text" size="35" class="text11ref inputUser" name="firstName" value="<%=custFirstName%>"></div>
					
				    <div class="bodyCopySUL"><span><label>Last Name <span class="bodyCopySUL-nonBold">(optional)</span></label></span></div>
					<div><input type="text" size="35" class="text11ref inputUser" name="lastName" value="<%=custLastName%>"></div>
				    
				    <div class="bodyCopySUL"><span><label>Work Phone Number <span class="bodyCopySUL-nonBold">(optional)</span></div>
				    <div><input type="text" size="35" class="text11ref inputUser" name="phone" value="<%=custPhone%>"></div>
			</div>
			    
			    
			<div class="fright hline" id="" style="width:100%; float:left;"><!-- --></div><br />
			
			<button class="cssbutton orange medium" alt="Submit" id="productNotifySurvey-submitBtn">Submit</button>
		
		<input type="hidden" name="notifyId" value="<%=request.getParameter("notifyId")%>">
		<input type="hidden" name="successPage" value="<%=sPage%>">
		<% if(survey_source!=null && survey_source.trim().length()>0) { %>
			<input type="hidden" name="survey_source" value="<%=survey_source%>">
		<% } %>
	</form>
	
	<script>
		$jq('#productNotifySurvey-submitBtn').on('click', function() {
			$jq('#productNotifySurvey').submit();
		});
	</script>
<% } %>
</fd:CorporateServiceSurvey>
