<%@ page import='com.freshdirect.framework.util.NVL'%>
<%
	//check for a passed pId
		String pId = NVL.apply(request.getParameter("pId"), "");
		String tType =  NVL.apply(request.getParameter("tType"), "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title> Gateway Testing</title>
	
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>

	<script type="text/javascript">
		function ajaxUpdate(targetId, url, params) {
			if (!$(targetId) || url == '') { return; }
			
			new Ajax.Updater(targetId, url, { evalScripts: true, method: 'get', parameters: params });
			
		}
		

		function disableFields(f) {
			var form = $(f);
			for (var i = 0; i<form.elements.length; i++) {
				if (form.elements[i].value == '') {
					form.elements[i].disabled = true;
				}
			}
			return true;
		}

			var cursorX = 0; var cursorY = 0; var rollX = 0; var rollY = 0;

			function UpdateCursorPosition(e) {
				cursorX = e.pageX; cursorY = e.pageY;
			}

			function UpdateCursorPositionDocAll(e) {
				cursorX = event.clientX; cursorY = event.clientY;
			}

			document.observe("dom:loaded", function() {
				if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
					else { document.onmousemove = UpdateCursorPosition; }
			});

			function AssignPosition(d) {
				if(self.pageYOffset) {
					rollX = self.pageXOffset;
					rollY = self.pageYOffset;
					}
				else if(document.documentElement && document.documentElement.scrollTop) {
					rollX = document.documentElement.scrollLeft;
					rollY = document.documentElement.scrollTop;
					}
				else if(document.body) {
					rollX = document.body.scrollLeft;
					rollY = document.body.scrollTop;
					}
				if(document.all) {
					cursorX += rollX; 
					cursorY += rollY;
					}
				d.style.left = (cursorX+10) + "px";
				d.style.top = (cursorY+10) + "px";
			}

			function hideContent(d) {
				if(d.length < 1) { return; }
				document.getElementById(d).style.display = "none";
			}

			function showContent(d) {
				if(d.length < 1) { return; }
				var dd = document.getElementById(d);
				AssignPosition(dd);
				dd.style.display = "block";
			}

			function helper(showFromId){
				if (!$(showFromId+'_help')) {
					 while (!helper_changer('No help available.', 'overDiv'));
					showContent('overDiv');
					return;
				}
				while (!helper_changer(showFromId+'_help', 'overDiv'));
				showContent('overDiv');
			}

			function helper_changer(from, to){
				if (typeof(from) == 'String') {
					$(to).innerHTML = from;
					return true;
				}
				if (!$(from)) {
					$(to).innerHTML = 'No help available.';
				}else{
					$(to).innerHTML = $(from).innerHTML;
				}
				return true;
			}
	</script>
	<style>
		#overDiv {
			display: none;
			z-index:1000;
			border: 2px solid #666;
			background-color: #eee;
			position: absolute;
			padding: 4px;
			text-align: left;
			-moz-border-radius-topleft: 5px;
			-webkit-border-top-left-radius: 5px;
			-moz-border-radius-topright: 5px;
			-webkit-border-top-right-radius: 5px;
			-moz-border-radius-bottomleft: 5px;
			-webkit-border-bottom-left-radius: 5px;
			-moz-border-radius-bottomright: 5px;
			-webkit-border-bottom-right-radius: 5px;
		}
		#resData {
			border: 1px solid #ccc;
			float: left;
			height: auto;
			width: auto;
		}
		.ind { padding-left: 20px; }
		.help { display: none; font-size: 12px; }
	</style>
</head>

<body>
	<div id="overDiv"></div>
	
	<div class="help" id="dyn_pId_help">
		<div>Profile ID to fetch details.</div>
		<div>DEFAULT: nothing</div>
		<div class="ind">
			If no ID or an invalid ID is passed, no data is returned.<br />
		</div>
	</div>
	<br/><br/><br/>
	<form id="formData" onsubmit="ajaxUpdate('resData', '/test/gateway/gatewayResponse.jsp', $(this.id).serialize()); return false;">
	
<input type="hidden" name="changeAction" />
<input type="hidden" name="navaction" value="" />
<input type="hidden"  name="proaction"/>
<input type="hidden" name="pageNo" value="" />
<input type="hidden" name="action" id="action" value="addEditProfileEC"/>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="98%">
<tr>
	<td valign="top" align="center">
		
		
	</td>
</tr>



<tr>
	<td valign="top">
		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			<!-- Begin Legend Information -->
			<tr>
				<td valign="top">
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td align="right"><img alt="Required Field" border="0" height="9" src="global/images/glo_ico_reqleg.gif" width="9"> = Required Field</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- End Legend Information -->
		</table>
		<br>
		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			<!-- Begin Profile ID/Use Next Available Profile ID -->
			<tr>
				<td nowrap class="form" width="200">Profile ID</td>
								
				
				
					<td>
						<input type="text" name="userProfile.profileID" maxlength="30" size="25" value="" class="inputfield">
					</td>
					
				
			</tr>
			<!-- End Profile ID/Use Next Available Profile ID -->
			
			
			<!-- Begin Profile Create Date -->
			
			<!-- End Profile Create Date -->			
			
			<!-- Begin Customer Name -->
			<tr>
				<td nowrap class="form">Customer Name
					 &nbsp;<img alt="Required Field" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"> 
					
				</td>
				<td colspan="3">
					<input type="text" name="userProfile.customerName" maxlength="30" size="25" value="" class="inputfield">
				</td>
			</tr>
			<!-- End Customer Name -->
			<!-- Begin Street -->
			<tr>
				<td nowrap class="form" valign="top">Street</td>
				<td colspan="3">
					<input type="text" name="userProfile.streetAddress1" maxlength="30" size="25" value="" class="inputfield">
					<br>
					<input type="text" name="userProfile.streetAddress2" maxlength="30" size="25" value="" class="inputfield">
				</td>
			</tr>
			<!-- End Street -->
			<!-- Begin City -->
			<tr>
				<td nowrap class="form">City</td>
				<td colspan="3">
					<input type="text" name="userProfile.city" maxlength="30" size="25" value="" class="inputfield">
				</td>
			</tr>
			
			
				
					<!-- Begin State -->
					<tr>
						<td nowrap class="form">State</td>
						<td colspan="3">
							<select name="userProfile.state" class="inputfield"><option value="" selected="selected"></option>
<option value="AL">AL</option>
<option value="AK">AK</option>
<option value="AZ">AZ</option>
<option value="AR">AR</option>
<option value="CA">CA</option>
<option value="CO">CO</option>
<option value="CT">CT</option>
<option value="DE">DE</option>
<option value="DC">DC</option>
<option value="FL">FL</option>
<option value="GA">GA</option>
<option value="HI">HI</option>
<option value="ID">ID</option>
<option value="IL">IL</option>
<option value="IN">IN</option>
<option value="IA">IA</option>
<option value="KS">KS</option>
<option value="KY">KY</option>
<option value="LA">LA</option>
<option value="ME">ME</option>
<option value="MD">MD</option>
<option value="MA">MA</option>
<option value="MI">MI</option>
<option value="MN">MN</option>
<option value="MS">MS</option>
<option value="MO">MO</option>
<option value="MT">MT</option>
<option value="NE">NE</option>
<option value="NV">NV</option>
<option value="NH">NH</option>
<option value="NJ">NJ</option>
<option value="NM">NM</option>
<option value="NY">NY</option>
<option value="NC">NC</option>
<option value="ND">ND</option>
<option value="OH">OH</option>
<option value="OK">OK</option>
<option value="OR">OR</option>
<option value="PA">PA</option>
<option value="RI">RI</option>
<option value="SC">SC</option>
<option value="SD">SD</option>
<option value="TN">TN</option>
<option value="TX">TX</option>
<option value="UT">UT</option>
<option value="VT">VT</option>
<option value="VA">VA</option>
<option value="WA">WA</option>
<option value="WV">WV</option>
<option value="WI">WI</option>
<option value="WY">WY</option></select>
						</td>
					</tr>
				
			<!-- Begin State -->
		
		
		
			<!-- Begin Postal Code -->
			<tr>
				<td nowrap class="form">Postal Code</td>
				<td colspan="3">
					<input type="text" name="userProfile.zip" maxlength="20" size="10" value="" class="inputfield">
				</td>
			</tr>
			<!-- End Postal Code -->
			
			
			<tr>
				<td colspan="4">
					<hr>
				</td>
			</tr>
			
			
			
			
			
			
			
			
			
				<!-- Begin ECP Specfic Fields -->
				<!-- Begin Deposit Account #/Bank Routing Number -->
				<tr>
					<td class="form" nowrap>Deposit Account #&nbsp;<img alt="Required Field" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"></td>
					<td>
						<input type="text" name="userProfile.depositAccount" maxlength="17" size="20" value="" onkeyup="checkDDAForMasking(this);" class="inputfield">
					</td>
					<td class="form" nowrap>Bank Routing Number&nbsp;<img alt="Required Field" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"></td>
					<td>
						<input type="text" name="userProfile.bankRoutingNumber" maxlength="9" size="20" value="" class="inputfield">
					</td>
				</tr>
				<!-- End Deposit Account #/Bank Routing Number -->
				<!-- Begin Account Type -->
				<tr>
					<td class="form" nowrap>Account Type</td>
					<td colspan="3">
						<select name="userProfile.ecpAccountType"><option value="C">Consumer Check</option>
							<option value="S">Consumer Saving</option>
							</select>
					</td>
				</tr>
				<!-- End Account Type -->
				<!-- End ECP Specfic Fields -->
			
			
			<!-- Begin EUDD -->
								
			<!-- End EUDD -->
			
			<tr>
				<td colspan="4">
					<hr>
				</td>
			</tr>
			
			<tr>
				<td colspan="4">
					<hr>
				</td>
			</tr>
			
								<tr>
									<td nowrap><img alt="" border="0" height="1" src="a.gif" width="160"></td>
									<td width="10">&nbsp;</td>
									<td nowrap><img alt="" border="0" height="1" src="a.gif" width="100"></td>
								</tr>
							</table>
							<br>
							<table border="0" cellpadding="0" cellspacing="5" width="100%">
								<tr>
									<td class="form" colspan="2" valign="top"> <a name="jumpToAnchor"> 
								</tr>
								<!-- Begin Buttons -->
								<tr>
									<td align="center">
										
											<input type="submit" name="addProfile" id="addProfile" value="Add/Edit Profile" />
											&nbsp;&nbsp; 
										
										&nbsp;&nbsp;&nbsp;&nbsp;  
										<input type="submit" name="subaction" value="Cancel" class="inputfield">
									</td>
								</tr>
								<!-- End Buttons -->
							</table>

						<!-- Start Account Updater Request -->							
						
							
						</td>
					</tr>
			
		</table>
</form>
<script language="JavaScript" type="Text/JavaScript">
function confirmDelete() {
	if(confirm("Are you sure you want to delete? ")) {
		document.forms[0].changeAction.value = "Delete";
		document.forms[0].submit();
	}
}


function vtlink(link){
 document.forms[0].navaction.value=link;
  document.forms[0].submit();
}

</script>

<!-- ***** END CONTENT SECTION ***** -->
	</form>
	<div id="resData"><!-- --></div>
</body>
</html>
