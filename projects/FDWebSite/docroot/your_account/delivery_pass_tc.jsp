<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import="java.util.Calendar" %>
<%@ page import='java.util.Date' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
 	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
 	FDIdentity identity  = user.getIdentity();
 	ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
 		
 	int dpTcViewCount = cm.getDpTcViewCount();
 	boolean userAgreed = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("userAgreed"), ""));
 	boolean showButtons = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("showButtons"), "false"));
 	boolean reloadParentPage = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("reload"), "false"));
 	boolean count = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("count"), "false"));
 	
	if (!userAgreed) { //normal display
		if (count) {
			/* every time this display is loaded, increase view count, assuming it should be counted */
	 		dpTcViewCount++;
			FDCustomerManager.storeDPTCViews(identity.getErpCustomerPK(), dpTcViewCount);
		}
%>
		<div>
			<div style="padding: 0 20px 10px 20px;"><img src="/media/editorial/site_pages/deliverypass/images/title_updatedterms.gif" width="551" height="27" alt="Updated Terms and Conditions" /></div>
			<div class="hline"><!--  --></div>
			<div style="padding: 10px 20px;">
				<fd:IncludeMedia name="/media/editorial/site_pages/deliverypass/DP_terms.html" />
			</div>
			
			<% if (showButtons) { %>
				<div class="hline"><!--  --></div>
				<div class="text13" style="padding: 10px 20px;">I have read and agree to the updated Delivery Pass <span class="text13bold">Terms and Conditions</span></div>
				<div class="hline"><!--  --></div>
				<div style="padding: 10px 20px;">
						<table width="100%" border="0">
							<tr>
								<td align="left">
									<table class="butCont">
										<tr>
											<td class="butBrownLeft"><!-- --></td>
											<td class="butBrownMiddle"><a class="butText" style="color:#000000;text-shadow:none;font-weight:bold;vertical-align:middle; width: 130px; text-align: center;" href="#" onclick="Modalbox.hide(); return false;">Remind&nbsp;Me&nbsp;Later</a></td>
											<td class="butBrownRight"><!-- --></td>
										</tr>
									</table>
								</td>
								<td align="right">
									<table class="butCont fright">
										<tr>
											<td class="butOrangeLeft"><!-- --></td>
											<td class="butOrangeMiddle"><a class="butText" style="font-weight:bold;text-shadow:none;vertical-align:middle; width: 130px; text-align: center;" href="#" onclick="doOverlayWindow('/your_account/delivery_pass_tc.jsp?userAgreed=true'); return false;">I&nbsp;Agree</a></td>
											<td class="butOrangeRight"><!-- --></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
				</div>
			<% } %>
		</div>
<%	
	} else { //agreed, success display
		Date now = new Date();
		FDCustomerManager.storeDPTCAgreeDate(identity.getErpCustomerPK(), now);
%>
		<div style="width: 575px;">
			<div style="padding: 0 20px 10px 20px;">
				<table>
					<tr>
						<td rowspan="2" valign="middle" align="center" width="35"><img src="/media/editorial/site_pages/deliverypass/images/checkmark.gif" width="22" height="16" alt="" /></td>
						<td><img src="/media/editorial/site_pages/deliverypass/images/title_thankyou.gif" width="172" height="22" alt="Thank You" /></td>
					</tr>
					<tr>
						<td><img src="/media/editorial/site_pages/deliverypass/images/recordedchanges.gif" width="298" height="18" alt="We Have Recorded Your Changes" /></td>
					</tr>
				</table>
			</div>
			<div class="hline"><!--  --></div>
			<div style="padding: 10px 20px 10px 20px;">
				<table width="100%" border="0">
					<tr>
						<td align="middle" class="trpad">
							<table class="butCont">
								<tr>
									<td class="butOrangeLeft"><!-- --></td>
									<td class="butOrangeMiddle"><a class="butText" style="font-weight:bold;text-shadow:none;vertical-align:middle; width: 130px; text-align: center;" href="#" onclick="Modalbox.hide();<%= (reloadParentPage) ? "window.document.location = window.document.location;" : "" %> return false;">Close</a></td>
									<td class="butOrangeRight"><!-- --></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
			<% if (reloadParentPage) { %>
				<div style="padding: 10px 20px 10px 20px;">Automatically refreshing in <span id="relTime">5</span> seconds...</div>
				<script type="text/javascript">
					$jq(document).ready(function(){
						window.setInterval(function() {
							var secs = $jq('#relTime').html();
							secs--;
							if (secs >= 0) { $jq('#relTime').html(secs); } else { return; }
							if (secs <= 0) { window.document.location = window.document.location; } 
						}, 1000);
					});
				</script>
			<% } %>
		</div>
<%
	}
 %>