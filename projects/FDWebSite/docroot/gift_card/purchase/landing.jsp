<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.giftcard.EnumGiftCardType' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<% //expanded page dimensions
final int W_GIFTCARD_LANDING_TOTAL = 970;
final int W_GIFTCARD_OPTIONS_TOTAL = 654;
final int W_GIFTCARD_DONATION_TOTAL = 300;
%>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/giftcard.jsp'>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Purchase Gift Card</tmpl:put> --%>
  <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Purchase Gift Card" pageId="landing"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<%
			String fdTemplateId = "";

			if (request.getParameter("gcTempId") !=null) {
				fdTemplateId = request.getParameter("gcTempId");
			}

			String fdGCType = "";

			/*
			 *	Call include to gather giftcard info from CMS
			 */
			
			//set the options first
			String deptId = "GC_testDept";
			String catId = "GC_testCat";
			String prodName = "GC_testProd";
			
			String mediaRoot = "/media/editorial/giftcards/";
			String mediaStaticRoot = "/media_stat/images/giftcards/";
			String gcDisplayType = "3"; //for a carousel
			String gcDisplayContainer = "gcDisplayContainer"; //id of div to use for display
			String gcDisplayId = "gcDisplay"; //id of gc Display object
			String gcDisplayTemplateContainerId = "gcTempId";

			String fdGCDonId = "";

			if (request.getParameter("gcDonId") != null) {
				fdGCDonId = request.getParameter("gcDonId");
			}
			request.setAttribute("donation", "true");
		%>
		<%@ include file="/gift_card/purchase/includes/i_fetch_giftcard_info_from_cms.jspf" %>
		
		<table border="0" cellspacing="0" cellpadding="0" width="<%=W_GIFTCARD_LANDING_TOTAL%>">
			<tr>
				<td valign="top">
					<table border="0" cellspacing="0" cellpadding="2" width="<%= (FDStoreProperties.isGiftCardDonationEnabled() && donationOrgList != null && donationOrgList.size() > 0) ? W_GIFTCARD_OPTIONS_TOTAL : W_GIFTCARD_LANDING_TOTAL %>">
						<tr>
							<td align="left">
								<fd:IncludeMedia name="/media/editorial/giftcards/media_includes/landing_header.html" />
							</td>
						</tr>
						<tr>
							<td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
						</tr>
						<tr>
							<td align="center">
								<form name="giftcard_form" id="giftcard_form" method="get" action="add_giftcard.jsp">
									<input type="hidden" id="gcTempId" name="gcTempId" value="<%=fdTemplateId%>">					
									<div id="gcDisplayContainer"><!-- --></div>
								</form>	
							</td>
						</tr>
						<tr>
							<td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
						</tr> 				
					</table>
				</td>
		<% if(FDStoreProperties.isGiftCardDonationEnabled() && donationOrgList != null && donationOrgList.size() > 0) { %>
				<td>&nbsp;</td>
				<td bgcolor="#cccccc">
					<img src="/media_stat/images/layout/dotted_line_w.gif" width="1" alt="" height="1" border="0" />
				</td>
				<td>&nbsp;&nbsp;</td>
				<td valign="top">
					<table border="0" cellspacing="0" cellpadding="2" width="<%=W_GIFTCARD_DONATION_TOTAL%>" >
						<tr>
							<td align="left">
								<fd:IncludeMedia name="/media/editorial/giftcards/media_includes/landing_donation_header.html" />
							</td>
						</tr>
						<tr>
							<td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
						</tr>
						<tr>
							<td>
								<form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter name="giftcard_donation_form" id="giftcard_donation_form" method="get" action="add_donation_giftcard.jsp">	
									<div style="overflow-y:scroll;overflow-x:hidden;height:300px;border:1px solid #cccccc;padding-top: 10px;">			
									<table border="0" cellspacing="0" cellpadding="2" width="278" >
										<logic:iterate id="donId" collection="<%= donationOrgList %>" type="com.freshdirect.storeapi.content.DonationOrganization">
											<tr>
												<td width="15" valign="top">
													<div style="padding-top:20px"><input type="radio" class="text11" name="gcDonId" id="gcDonId_<%= donId.getContentName() %>" value="<%= donId.getContentName() %>" <%= (fdGCDonId != null && donId.getContentName().equals(fdGCDonId)) ? "checked" : "" %> />
													</div>
												</td>
												<td width="65" valign="top">											
													<label for="gcDonId_<%= donId.getContentName() %>">
													<%
														String addDefaultOrgLogo = "<input type=\"image\" name=\"default_org_logo\" src=\"/media/editorial/giftcards/org_logo_blank.gif\" width=\"61\" height=\"61\" alt=\"\" border=\"0\" />";
													%>		
													<%= (donId.getLogoSmall() != null) ? donId.getLogoSmall().toHtml(donId.getOrganizationName()) : addDefaultOrgLogo %>
													</label>&nbsp;
												</td>
												<td valign="top" width="255">
													<%
														Html piece = donId.getEditorialMain();
														if (piece != null) {
													%>
														<fd:IncludeMedia name='<%= piece.getPath() %>' />
													<%															
														}
													%>													
													<br/><br/>
												</td>
											</tr>										
										</logic:iterate> 
									</table>
									</div>
								</form>
							</td>
						</tr>
					</table>
				</td>
				<% } %>
			</tr>
			<tr>
				<td align="center" colspan="5"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="16" border="0" /></td>
			</tr>
			<tr>
				<td colspan="3" align="center">
					<button class="cssbutton orange" name="gcLand_shopNow" id="gcLand_shopNow" border="0" onclick="$('giftcard_form').submit();return false;" >CUSTOMIZE YOUR GIFT CARD</button>
				</td>
			<%if(FDStoreProperties.isGiftCardDonationEnabled() && donationOrgList != null && donationOrgList.size() > 0) { %>
				<td align="center" colspan="2">
					<button class="cssbutton orange" name="gcDonateLand_shopNow" id="gcLand_shopNow" onClick="return pendGC();return false;" >Donate Gift Card</button>
				</td>
			<% } %>
			</tr>
			<tr>
				<td align="center" colspan="5"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="16" border="0" /></td>
			</tr>
			<tr>
				<td align="center" colspan="5">
					<div class="card_TOC">Purchasing a Gift Card indicates agreement to our Gift Card<br />
						<a href="/gift_card/purchase/tac.jsp">Terms and Conditions</a>. Click for details. 
					</div>
				</td>
			</tr>
		</table>

		<script>
		function pendGC() {
			showPopUp = true;
			var arrRadioBtn = document.getElementsByName("gcDonId");
			for(var i=0;i<arrRadioBtn.length;i++){
				if($(arrRadioBtn[i]).checked){
					showPopUp = false;
					break;
				}
			}
			if (showPopUp) {
				var popUp='<div style="text-align: left;" class="pendGCPop" id="pendGCPop"><a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a><br style="clear: both;" /><br /><br />Please select organization you want to donate giftcard, then click "Donate A Gift Card."<br /><br />Close this popup to choose organization you want to donate.<br /><br /><div style="text-align: center;"><a href="#" onclick="Modalbox.hide(); return false;">Close</a></div></div>';


				Modalbox.show(popUp, {
					loadingString: 'Loading Preview...',
					title: '',
					overlayOpacity: .85,
					overlayClose: false,
					width: 250,
					transitions: false,
					autoFocusing: false,
					centered: true,
					afterLoad: function() { window.scrollTo(0,0); },
					afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
				});
				showPopUp = false;
				return false;
			}else{
				setCheckOut();
				return true;
			}
		}
		
		 function setCheckOut() {
			 document.giftcard_donation_form.submit();
		 }
		</script>
	</tmpl:put>
</tmpl:insert>