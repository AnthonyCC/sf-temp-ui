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
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
 	FDIdentity identity  = user.getIdentity();
 	ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
 		
 	int dpTcViewCount = cm.getDpTcViewCount();
 	boolean userAgreed = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("userAgreed"), ""));
 	boolean showButtons = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("showButtons"), "false"));
 	boolean reloadParentPage = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("reload"), "false"));
 	boolean viaModal = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("modal"), "false"));
 	boolean count = ("true").equalsIgnoreCase(NVL.apply(request.getParameter("count"), "false"));
 	
	if (!userAgreed) { //normal display
		if (count) {
			/* every time this display is loaded, increase view count, assuming it should be counted */
	 		dpTcViewCount++;
			FDCustomerManager.storeDPTCViews(identity.getErpCustomerPK(), dpTcViewCount);
			//set as seen for session
			sessionUser.setSeenDpNewTc(true);
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
				<div style="padding: 10px 20px;">
					<div class="fleft">
						<a class="butText" href="#" onclick="Modalbox.hide(); return false;"><img src="/media/editorial/site_pages/deliverypass/images/but_remindmelater_f1.gif" width="173" height="32" border="0" alt="Remind Me Later" /></a>
					</div>
					<div class="fright">
						<a class="butText" href="#" onclick="doOverlayWindow('/overlays/delivery_pass_tc.jsp?userAgreed=true&modal=true'); return false;"><img src="/media/editorial/site_pages/deliverypass/images/but_iagree_f1.gif" width="170" height="33" border="0" alt="I Agree" /></a>
					</div>
					<br style="clear:both" />
				</div>
			<% } %>
		</div>
<%	
	} else { //agreed, success display
		Date now = new Date();
		FDCustomerManager.storeDPTCAgreeDate(AccountActivityUtil.getActionInfo(session, (viaModal) ? " (via modal overlay)" : " (via TS overlay)"), identity.getErpCustomerPK(), now);
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
			<div style="padding: 10px 20px 10px 20px; text-align: center;">
				<a class="butText" href="#" onclick="Modalbox.hide();<%= (reloadParentPage) ? "window.document.location = window.document.location;" : "" %> return false;"><img src="/media/editorial/site_pages/deliverypass/images/but_close.jpg" width="216" height="33" border="0" alt="Close" /></a>
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