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
<input type="hidden" name="action" id="action" value="reverseAuthorize"/>
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
							<td align="right"><img alt="*" border="0" height="9" src="global/images/glo_ico_reqleg.gif" width="9"> = Required Field</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- End Legend Information -->
		</table>
		<br>
		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			
			
			
					
			
			
			<!-- Begin Order ID  -->
			<tr>
				<td nowrap class="form" valign="top">Order ID<img alt="*" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"></td>
				<td colspan="3">
					<input type="text" name="userProfile.orderID" maxlength="30" size="25" value="" class="inputfield">
					<br>
				</td>
			</tr>
			<!-- End Order ID -->
			<!-- Begin Tx Ref -->
			<tr>
				<td nowrap class="form">Transaction Ref Num
					 &nbsp;<img alt="*" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"> 
					
				</td>
				<td colspan="3">
					<input type="text" name="userProfile.txRefNum" maxlength="40" size="40" value="" class="inputfield">
				</td>
			</tr>
			<!-- End Tx Ref Num  -->
			<!-- Begin Tx Ref  Index-->
			<tr>
				<td nowrap class="form">Transaction Ref Index
					 &nbsp;<img alt="*" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"> 
					
				</td>
				<td colspan="3">
					<input type="text" name="userProfile.txRefIdx" maxlength="40" size="40" value="" class="inputfield">
				</td>
			</tr>
			<!-- End Tx Ref Index  -->
			
			<tr>
							<td class="form" nowrap width="5%">Merchant&nbsp;<img alt="*" border="0" height="13" src="global/images/glo_ico_req.gif" width="9"></td>
							<td>
									<select name="userProfile.merchant" class="inputfield"><option value=""></option>
									<option value="FRESHDIRECT">FreshDirect </option>
									<option value="USQ">USQ</option>
									</select>	
                                                                    </td>									
							
							</tr>
			<!-- End Payment Method -->

	
			
			
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
										
											<input type="submit" name="reverseAuthorize" id="reverseAuthorize" value="Void Capture" />
											&nbsp;&nbsp; 
										
										
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
