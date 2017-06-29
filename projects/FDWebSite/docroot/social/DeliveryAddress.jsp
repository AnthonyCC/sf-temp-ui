
<%@page
	import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.AddressModel"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.mail.EmailUtil"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ taglib uri="freshdirect" prefix="fd"%>

<fd:CheckLoginStatus />

<%
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	HashMap socialUser = (HashMap)session.getAttribute(SessionName.SOCIAL_USER);
	String successPage = "index.jsp";
	String serviceType = NVL.apply(request.getParameter("serviceType"),
			"").trim();
	//System.out.println("\n\n\n"+user.getSelectedServiceType().getName()+"\n\n\n");
	if ("".equals(serviceType)) {
		if (user != null) {
			serviceType = user.getSelectedServiceType().getName();
			if ("PICKUP"
					.equals(user.getSelectedServiceType().getName()))
				serviceType = "HOME";
		} else {
			serviceType = "HOME";
		}
	}

	boolean isCorporate = "CORPORATE".equalsIgnoreCase(serviceType);

	String failurePage = "/registration/DeliveryAddress.jsp?successPage="
			+ URLEncoder.encode(successPage)
			+ "&ol=na&serviceType="
			+ serviceType;

	CmRegistrationTag.setRegistrationLocation(session, "social signup");
	System.out
			.println("-----------------------------------------------------"
					+ serviceType + "");
%>

<!-- 'signupSocialDlvAddr' for account creation  or 'addDeliveryAddress' for checing out -->
<fd:SiteAccessController action='addDeliveryAddress' successPage='<%=successPage%>' moreInfoPage='' failureHomePage='<%=failurePage%>' result='result'>


	<%
		if (session.getAttribute("morepage") != null) {
				String mPage = (String) session.getAttribute("morepage");
	%>
	<jsp:include page="<%=mPage%>" flush="false" />
	<%
		} else {
	%>

	<!DOCTYPE html>
	<html>
<head>
    <title>FreshDirect</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf"%>
    <%@ include	file="/shared/template/includes/style_sheet_grid_compat.jspf"%>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf"%>

<jwr:style src="/assets/css/social_login_signup.css" media="all" />

<%@ include file="/shared/template/includes/i_head_end.jspf"%>

<%@ include file="/shared/template/includes/i_body_start.jspf"%>

<script type="text/javascript" language="javascript">
	$jq('#serviceType').live('change', function() {
		if ($jq(this).val() === 'CORPORATE') {
			$jq('#company_name').show();
			$jq('#busphone').show();
			$jq('.busphoneField').show();
		} else {
			$jq('#company_name').hide();
			$jq('#busphone').hide();
			$jq('.busphoneField').hide();
		}
	});
</script>

</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10"
	topmargin="10">

	<center>

		<%
		
		String companyname;
		String firstname;
		String lastname;
		String streetaddr;
		String suite;
		String zipcode;
		String city;
		String state;
		String busphone;
		String mobilephno;
		String email;
		
		if((String)session.getAttribute("lastpage") == "FailedAddrPage" ){

					companyname = NVL.apply(
							request.getParameter("companyname"), "").trim();
					System.out.println("companyname:" + companyname);
					firstname = NVL.apply(
							request.getParameter("firstname"), "").trim();
					System.out.println("firstname:" + firstname);
					lastname = NVL.apply(
							request.getParameter("lastname"), "").trim();
					System.out.println("lastname:" + lastname);
					streetaddr = NVL.apply(
							request.getParameter("streetaddr"), "").trim();
					System.out.println("streetaddr:" + streetaddr);
					suite = NVL.apply(request.getParameter("suite"), "")
							.trim();
					System.out.println("suite:" + suite);
					zipcode = NVL.apply(request.getParameter("zipcode"),
							"").trim();
					System.out.println("zipcode:" + zipcode);
					city = NVL.apply(request.getParameter("city"), "")
							.trim();
					System.out.println("city:" + city);
					state = NVL.apply(request.getParameter("state"), "")
							.trim();
					System.out.println("state:" + state);
					busphone = NVL.apply(
							request.getParameter("busphone"), "").trim();
					System.out.println("busphone:" + busphone);
					mobilephno = NVL.apply(
							request.getParameter("mobilephno"), "").trim();
					System.out.println("mobilephno:" + mobilephno);
					email = NVL.apply(request.getParameter("email"), "")
							.trim();
					System.out.println("email:" + email);
		}
		else
		{
					companyname = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "").trim();
					System.out.println("companyname:" + companyname);
					firstname = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "").trim();
					System.out.println("firstname:" + firstname);
					lastname = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "").trim();
					System.out.println("lastname:" + lastname);
					streetaddr = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim();
					System.out.println("streetaddr:" + streetaddr);
					suite = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "")
							.trim();
					System.out.println("suite:" + suite);
					zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()),
							"").trim();
					System.out.println("zipcode:" + zipcode);
					city = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "")
							.trim();
					System.out.println("city:" + city);
					state = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "")
							.trim();
					System.out.println("state:" + state);
					busphone = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_WORK_PHONE.getCode()), "").trim();
					System.out.println("busphone:" + busphone);
					mobilephno = NVL.apply(
							request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode()), "").trim();
					System.out.println("mobilephno:" + mobilephno);
					if(socialUser != null){	
						session.setAttribute("SOCIALONLYACCOUNT", "true");
						email = (String)socialUser.get("email");
					} else {
						email = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "")
								.trim();
					}					
					System.out.println("email:" + email);
		}

		session.setAttribute("lastpage", "DeliveryAddress");

				 	//out.println("DELIVERYADDRESS_COMPLETE:"+session.getAttribute("DELIVERYADDRESS_COMPLETE")); 
					if (session.getAttribute("DELIVERYADDRESS_COMPLETE") != null) {
		%>
		<div style="width: 500px;">
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
		</div>
		<script language="javascript">
			//window.top.location = window.top.location;// +((window.top.location.search).indexOf('&')!==-1)?"&":"?"+ "deliveryaddresspage=true";
			window.top.location = '/checkout/view_cart.jsp';   // return to the next page of the check out flow
		</script>		
		<%
			} else {
		%>


		<div id="sulCont" class="signup-style-social">

			<span
				style="font-size: 14px; font-weight: bold; font-family: Verdana, Arial, sans-serif; margin-bottom: 20px; margin-left: 20px">Delivery
				Information:</span>


			<form id="deliveryaddress" name="deliveryaddress" method="post"
				action="/social/DeliveryAddress.jsp" style="margin-top: 40px;">
				<input type="hidden" name="submission" value="done" /> <input
					type="hidden" name="actionName" value="ordermobilepref" /> <input
					type="hidden" name="successPage" value="<%=successPage%>" /> <input
					type="hidden" name="terms" value="true" /> <input type="hidden"
					name="DELIVERYADDRESS" value="true" style="margin-top: 20px;" /> <input
					type="hidden" name="<%=EnumUserInfoName.EMAIL.getCode()%>"
					value="<%=email%>" id="email" /> <input type="radio"
					name="serviceType" id="serviceType" value="HOME"
					<%=(serviceType.equals("HOME")) ? "checked" : ""%> />HOME&nbsp; <input
					type="radio" name="serviceType" id="serviceType" value="CORPORATE"
					<%=(serviceType.equals("CORPORATE"))
								? "checked"
								: ""%> />OFFICE

				<table style="margin-top: 20px; width: 100%;">

					<tr>
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_COMPANY_NAME
									.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="company_name_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_COMPANY_NAME.getCode()%>"
							value="<%=companyname%>" id="company_name"
							placeholder="Company Name         (Optional)"
							style="display: none"></td>
					</tr>

					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
						<td class="errMsg"><fd:ErrorHandler result='<%=result%>'
								name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>'
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
					</tr>


					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="first_name_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>"
							value="<%=firstname%>" id="first_name" placeholder="First Name"></td>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="last_name_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>"
							value="<%=lastname%>" id="last_name" placeholder="Last Name"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="street_addr_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>"
							value="<%=streetaddr%>" id="street_addr"
							placeholder="Street Address"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="apt_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>"
							value="<%=suite%>" id="suite" placeholder="Floor/Suite"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg"><fd:ErrorHandler result='<%=result%>'
								name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>'
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="zipcode_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="5" class="" size="20" name="zipcode"
							value="<%=zipcode%>" id="zipcode" placeholder="Delivery ZIP Code"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_CITY.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="city_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_CITY.getCode()%>" value="<%=city%>"
							id="city" placeholder="City"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_STATE.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="state_img"></span>
						</td>
						<td>
							<select name="state">
								<option value="AL">Alabama</option>
								<option value="AK">Alaska</option>
								<option value="AZ">Arizona</option>
								<option value="AR">Arkansas</option>
								<option value="CA">California</option>
								<option value="CO">Colorado</option>
								<option value="CT">Connecticut</option>
								<option value="DE">Delaware</option>
								<option value="DC">District of Columbia</option>
								<option value="FL">Florida</option>
								<option value="GA">Georgia</option>
								<option value="HI">Hawaii</option>
								<option value="ID">Idaho</option>
								<option value="IL">Illinois</option>
								<option value="IN">Indiana</option>
								<option value="IA">Iowa</option>
								<option value="KS">Kansas</option>
								<option value="KY">Kentucky</option>
								<option value="LA">Louisiana</option>
								<option value="ME">Maine</option>
								<option value="MD">Maryland</option>
								<option value="MA">Massachusetts</option>
								<option value="MI">Michigan</option>
								<option value="MN">Minnesota</option>
								<option value="MS">Mississippi</option>
								<option value="MO">Missouri</option>
								<option value="MT">Montana</option>
								<option value="NE">Nebraska</option>
								<option value="NV">Nevada</option>
								<option value="NH">New Hampshire</option>
								<option value="NJ">New Jersey</option>
								<option value="NM">New Mexico</option>
								<option value="NY">New York</option>
								<option value="NC">North Carolina</option>
								<option value="ND">North Dakota</option>
								<option value="OH">Ohio</option>
								<option value="OK">Oklahoma</option>
								<option value="OR">Oregon</option>
								<option value="PA">Pennsylvania</option>
								<option value="RI">Rhode Island</option>
								<option value="SC">South Carolina</option>
								<option value="SD">South Dakota</option>
								<option value="TN">Tennessee</option>
								<option value="TX">Texas</option>
								<option value="UT">Utah</option>
								<option value="VT">Vermont</option>
								<option value="VA">Virginia</option>
								<option value="WA">Washington</option>
								<option value="WV">West Virginia</option>
								<option value="WI">Wisconsin</option>
								<option value="WY">Wyoming</option>
							</select>											
						</td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg "><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_HOME_PHONE.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img" id="mobile_num_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_HOME_PHONE.getCode()%>"
							value="<%=mobilephno%>" id="mobilenum"
							placeholder="Mobile Phone Number"></td>
					</tr>


					<tr style="margin-top: 20px;">
						<td>&nbsp;</td>
						<td class="errMsg busphoneField"><fd:ErrorHandler result="<%=result%>"
								name="<%=EnumUserInfoName.DLV_WORK_PHONE.getCode()%>"
								id='errorMsg'>
								<span class="text11rbold"><%=errorMsg%></span>
							</fd:ErrorHandler>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" --> <span
							class="error_img busphoneField" id="workphone_img"></span>
						</td>
						<td><input
							class="padding-input-box text11ref inputUser required busphoneField"
							type="text" maxlength="25" size="20"
							name="<%=EnumUserInfoName.DLV_WORK_PHONE.getCode()%>"
							value="<%=busphone%>" id="busphone" placeholder="Work Number"
							style="display: none"></td>
					</tr>


					<tr>
						<td></td>
						<td style="padding-top: 10px;"><a class="butText"
							style="font-weight: bold; font-size: 14px;"> <input
								type="submit" id="signupbtn" maxlength="25" size="19"
								value="Continue">
						</a></td>
					</tr>


				</table>
			</form>
		</div>

		<%
			if (serviceType.equals("CORPORATE")) {
		%>
		<script type="text/javascript">
			$jq('#company_name').show();
			$jq('#busphone').show();
			$jq('.busphoneField').show();
		</script>
		<%
			} else {
		%>
		<script type="text/javascript">
			$jq('#company_name').hide();
			$jq('#busphone').hide();
			$jq('.busphoneField').hide();
		</script>
		<%
			}
		%>

		<%
			if (result.isFailure()) {
		%>
		<script type="text/javascript">
			setFrameHeightSL('signupframe', $jq('#sulCont').height());
			window.top.Modalbox_setWidthAndPosition();
		</script>
		<%
			}
		%>

		<%
			}
		%>

	</center>


</body>
	</html>
	<%
		}
	%>
</fd:SiteAccessController>
