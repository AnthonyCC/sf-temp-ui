<%@page import="javax.naming.Context"%>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.survey.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.text.SimpleDateFormat, java.util.*" %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
<%@ page import='com.freshdirect.common.context.MasqueradeContext' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
// final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<% 
	//expanded page dimensions
	final int W_CHECKOUT_STEP_4_RECEIPT_TOTAL = 970;

	/* this is necessary for the cart n tabs */
	request.setAttribute("__yui_load_dispatcher__", Boolean.TRUE);
%>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag pageId=""></fd:SEOMetaTag>
</tmpl:put>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Order Placed</tmpl:put>
<tmpl:put name='content' direct='true'>
	
	<% 
		FDSessionUser fdSessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
		MasqueradeContext masqueradeContext = fdSessionUser.getMasqueradeContext();
		if (masqueradeContext != null) {
			String makeGoodFromOrderId = masqueradeContext.getMakeGoodFromOrderId();
	%>
	<div id="topwarningbar">
		You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user.getUserId()%> (Store: <%= user.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user.getUserContext().getFulfillmentContext().getPlantId() %>)
		<%if (makeGoodFromOrderId!=null) {%>
			<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
			(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
			<a class="imgButtonOrange" href="/cancelmakegood.jsp">Cancel MakeGood</a>
		<%}%>
	</div>
	<% } %>

	<%
		boolean _modifyOrderMode = false; 	
		String _ordNum = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
		
		if(session.getAttribute("MODIFIED" + _ordNum) != null && session.getAttribute("MODIFIED" + _ordNum).equals(_ordNum)) {
			_modifyOrderMode = true;
		}
		%>
		
		<fd:GetOrder id='order' saleId='<%=_ordNum%>'>
			<script type="text/javascript">
				<fd:CmShop9 order="<%=order%>"/>
				<fd:CmOrder order="<%=order%>"/>
				<fd:CmRegistration force="true"/>
				<fd:CmConversionEvent eventId="became_a_customer"/>
				<% if(_modifyOrderMode){ %>
					<fd:CmConversionEvent order="<%=order%>" orderModified="true"/>
				<% } %>
			</script>
		</fd:GetOrder>
		<%
	
		if (!_modifyOrderMode) {
			FDUserI curruser = (FDUserI)session.getAttribute(SessionName.USER);
			FDIdentity curridentity  = curruser.getIdentity();
			ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(curridentity);
			
			FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(curridentity);
			// if atleast one of the smsAlerts is none -- Will have to change this
			if(FDStoreProperties.getSMSOverlayFlag() && fdCustomer.getCustomerSmsPreferenceModel().getSmsPreferenceflag()==null ){
				if(session.getAttribute("SMSAlert" + _ordNum) == null){
					%>
		<script type="text/javascript"
			src="/assets/javascript/rounded_corners.inc.js"></script>
		<script language="javascript">
						function curvyCornersHelper(elemId, settingsObj) {
							if (document.getElementById(elemId)) {
								var temp = new curvyCorners(settingsObj, document.getElementById(elemId)).applyCornersToAll();
							}
						}
						
						var ccSettings = {
							tl: { radius: 18 },
							tr: { radius: 18 },
							bl: { radius: 18 },
							br: { radius: 18 },
							topColour: "#FFFFFF",
							bottomColour: "#FFFFFF",
							antiAlias: true,
							autoPad: true
						};
	
						/* display an overlay containing a remote page */
						function doSmsRemoteOverlay(olURL) {
							var olURL = olURL || '';
							if (olURL == '') { return false; }
					
							 Modalbox.show(olURL, {
		                        loadingString: 'Loading Preview...',
		                        title: ' ',
		                        overlayOpacity: .80,
		                        width: 750,
		                        centered: true,
		                        method: 'post',
		                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
		                        afterLoad: function() {
                                       $('MB_frame').style.border = '1px solid #CCCCCC';
                                       $('MB_header').style.border = '0px solid #CCCCCC';
                                       $('MB_header').style.display = 'block';
                                       window.scrollTo(0,0);
                                       $('MB_window').style.width = 'auto';
                                       $('MB_window').style.height = 'auto';
                                       $('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
                                       $('MB_content').style.padding = '0px';

                                       curvyCornersHelper('MB_frame', ccSettings);
		                        },
		                        afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			                });
						}
						
						function doSmsRemoteOverlay1(olURL) {
							var olURL = olURL || '',
								paramsVar;
							if (olURL == '') { return false; }
							paramsVar = Form.serialize('smsalertform');
							Modalbox.hide();
							 Modalbox.show(olURL, {
		                        loadingString: 'Loading Preview...',
		                        title: ' ',
		                        overlayOpacity: .80,
		                        width: 750, 
		                        centered: true,
		                        method: 'post',
		                        params: paramsVar,
		                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
		                        afterLoad: function() {
			                        
                                    	$('MB_frame').style.border = '1px solid #CCCCCC';
										$('MB_header').style.border = '0px solid #CCCCCC';
										$('MB_header').style.display = 'block';
										window.scrollTo(0,0);
										$('MB_window').style.width = 'auto';
										$('MB_window').style.height = 'auto';
										$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
										$('MB_content').style.padding = '0px';

                                       curvyCornersHelper('MB_frame', ccSettings);
		                        },
		                        afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			                });
						}
						
						doSmsRemoteOverlay('sms_alerts.jsp');
					</script>
		<% }
				
			}else if(cm.getMobilePreference() == null || (com.freshdirect.fdstore.customer.ejb.EnumMobilePreferenceType.SAW_MOBILE_PREF.getName()).equals(cm.getMobilePreference())) {		
				//session.removeAttribute("SMSSubmission"+ _ordNum);
				if(session.getAttribute("SMSSubmission" + _ordNum) == null) { 
					//Store the flag to capture the user has seen the window atleast once event
					FDCustomerManager.storeSMSWindowDisplayedFlag(curridentity.getErpCustomerPK());
					%>
		<script type="text/javascript"
			src="/assets/javascript/rounded_corners.inc.js"></script>
		<script language="javascript">
						function curvyCornersHelper(elemId, settingsObj) {
							if (document.getElementById(elemId)) {
								var temp = new curvyCorners(settingsObj, document.getElementById(elemId)).applyCornersToAll();
							}
						}
						
						var ccSettings = {
							tl: { radius: 6 },
							tr: { radius: 6 },
							bl: { radius: 6 },
							br: { radius: 6 },
							topColour: "#FFFFFF",
							bottomColour: "#FFFFFF",
							antiAlias: true,
							autoPad: true
						};
	
						/* display an overlay containing a remote page */
						function doRemoteOverlay(olURL) {
							var olURL = olURL || '';
							if (olURL == '') { return false; }
					
							 Modalbox.show(olURL, {
		                        loadingString: 'Loading Preview...',
		                        title: ' ',
		                        overlayOpacity: .80,
		                        width: 750,
		                        centered: true,
		                        method: 'post',
		                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
		                        afterLoad: function() {
                                       $('MB_frame').style.border = '1px solid #CCCCCC';
                                       $('MB_header').style.border = '0px solid #CCCCCC';
                                       $('MB_header').style.display = 'block';
                                       window.scrollTo(0,0);
                                       $('MB_window').style.width = '750';
                                       $('MB_window').style.height = 'auto';
                                       $('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
                                       $('MB_content').style.padding = '0px';

                                       curvyCornersHelper('MB_frame', ccSettings);
		                        },
		                        afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			                });
						}
						
						function doRemoteOverlay1(olURL) {
							var olURL = olURL || '',
								paramsVar;
							if (olURL == '') { return false; }
							paramsVar = Form.serialize('smsform');
							Modalbox.hide();
							 Modalbox.show(olURL, {
		                        loadingString: 'Loading Preview...',
		                        title: ' ',
		                        overlayOpacity: .80,
		                        width: 750, 
		                        centered: true,
		                        method: 'post',
		                        params: paramsVar,
		                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
		                        afterLoad: function() {
			                        
                                    	$('MB_frame').style.border = '1px solid #CCCCCC';
										$('MB_header').style.border = '0px solid #CCCCCC';
										$('MB_header').style.display = 'block';
										window.scrollTo(0,0);
										$('MB_window').style.width = '750';
										$('MB_window').style.height = 'auto';
										$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
										$('MB_content').style.padding = '0px';

                                       curvyCornersHelper('MB_frame', ccSettings);
		                        },
		                        afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			                });
						}
						
						doRemoteOverlay('sms_capture.jsp');
					</script>
		<% }
			}
		}
	%>
	<%
		//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/checkout");
        request.setAttribute("listPos", "ReceiptTop,ReceiptBotLeft,ReceiptBotRight,SystemMessage,CategoryNote");
	%>
	<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
	<div class="groupScaleBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/buttons/exit_trans.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td>
					<%-- all your content goes in this div, it controls the height/width --%>
					<div id="group_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="Close Window" /><br />
					</div>
				</td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_left_curve_8A6637.gif" width="6" height="6" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /></td>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_right_curve_8A6637.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
		</table>
	</div>
	
	<% /* top rows (everything above the top solid line, line is in /checkout/includes/i_checkout_receipt.jspf) */	%>
	
	<table class="globalnav_top" border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>" style="margin-top: 5px;">
		<tr valign="bottom">      
			<td colspan="2" width="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>" align="right">
			<%
				if (user.isChefsTable()) {        
			%>		<a href="/your_account/manage_account.jsp"><img src="/media_stat/images/template/checkout/loy_global_member_stars_2008.gif" width="314" height="9" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0" /></a><br />
			<%
				}
			%>
			</td>
		</tr>
		<tr valign="bottom">
			<td width="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL-450%>">
				<a href="/index.jsp">
					<!--  
					<img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" border="0" alt="FreshDirect" />
					-->
					<img src="/media/layout/nav/globalnav/fdx/fd-logo_v2.png" width="216" height="44" border="0" alt="FreshDirect" />
				</a>
			</td>
			<td width="450" align="right">
			
				<% String dlvInfoLink = ""; 
					if (user.isPickupOnly()) {
						dlvInfoLink = "/help/delivery_lic_pickup";
					} else if (user.isDepotUser()) {
						dlvInfoLink = "/help/delivery_info_depot";
					} else if (user.getAdjustedValidOrderCount() >= 1) {
						dlvInfoLink = "/your_account/delivery_info_avail_slots";
					} else {
						dlvInfoLink = "/help/delivery_info";
							if (EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
								dlvInfoLink += "_cos";
							} 
					}
				%>
			
				<div class="topnavitem" id="topnavitem_help">
			    	<a href="/help/index.jsp?trk=gnav" style="margin-right: 10px;">Help</a>
			    </div>    
			    <div class="topnavitem" id="topnavitem_deliveryInfo">
			    	<a href="<%=dlvInfoLink%>.jsp">Delivery Info</a>
			    </div>
			    <div class="topnavitem" id="topnavitem_reorder">
			    	<a href="/quickshop/index.jsp"><div id="reorder-icon"></div>Reorder</a>
			    </div>
			    <div class="topnavitem" id="topnavitem_yourAccount">
			    	<a href="/your_account/manage_account.jsp">Your Account</a>
			    </div>
			</td>
		</tr>
		<tr>
			<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0" /></td>
		</tr>
	</table>
	<% /* main body comes from i_checkout_receipt.jspf */ %>
	<%
		//this needs to be BEFORE the i_checkout_receipt.jspf include, since it's used in it
		String sem_orderNumber = "0";
		sem_orderNumber = NVL.apply((String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER), "0");
	%>
	<%@include file="/checkout/includes/i_checkout_receipt_summary.jspf"%>
	
	<br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	
	<% if (FDStoreProperties.isAdServerEnabled()) { %>
		<table width="<%=W_CHECKOUT_RECEIPT_TOTAL%>" cellpadding="0" cellspacing="0">
			<tr>
				<td width="50%" style="border-right: solid 1px #CCCCCC; padding-right: 10px;" align="center">
					<script type="text/javascript">
						OAS_AD('ReceiptBotLeft');
					</script>
				</td>
				<td width="50%" style="padding-left: 10px;" align="center">
					<script type="text/javascript">
						OAS_AD('ReceiptBotRight');
					</script>
				</td>
			</tr>		
		</table>
	<% } %>
	
<% if (session != null && session.getAttribute(SessionName.USER) != null) { %>
	<%
		FDUserI sem_user = (FDUserI)session.getAttribute(SessionName.USER);
		
		if(sem_user != null && sem_user.getShoppingCart() != null && request.getRequestURI().startsWith("/checkout/step_4_receipt.jsp")) {
			//Shared Pixel vars
			String sem_validOrderCount = "0";
				sem_validOrderCount = Integer.toString(sem_user.getAdjustedValidOrderCount());
			double sem_checkCartSubtotal = 0;
			String sem_cartSubtotal = "0";
			DecimalFormat sem_df = new DecimalFormat("0.00");
				sem_cartSubtotal = NVL.apply((String)request.getAttribute("cartSubtotal"), "0").replace("$", "").replace(",","");
				sem_cartSubtotal = sem_df.format(Double.parseDouble(sem_cartSubtotal));
					
			String sem_totalDiscountAmount = "0";
				sem_totalDiscountAmount = NVL.apply((String)request.getAttribute("totalDiscountAmount"), "0").replace("$", "");
				sem_totalDiscountAmount = sem_df.format(Double.parseDouble(sem_totalDiscountAmount));
			boolean isOrderModify = Boolean.parseBoolean(NVL.apply((String)request.getAttribute("modifyOrderMode"), "false"));
			String sem_defaultCounty = sem_user.getDefaultCounty();
			String sem_totalCartItems = "0";
				sem_totalCartItems = NVL.apply((Integer)request.getAttribute("totalCartItems"), 0).toString();
	
			/* Google AdWords Pixel */
			%><fd:SemPixelIncludeMedia pixelNames="GoogleAdWords" /><%
	
			/* CheetahMail Pixel */
			SemPixelModel semPixel_CM = FDSemPixelCache.getInstance().getSemPixel("CheetahMail");
	
			//add a param to the params sent to the FTL
			semPixel_CM.setParam("subtotal", sem_cartSubtotal);
			semPixel_CM.setParam("orderId", sem_orderNumber);
			semPixel_CM.setParam("isOrderModify",String.valueOf(isOrderModify));
			semPixel_CM.setParam("userCounty", sem_defaultCounty);
			semPixel_CM.setParam("totalCartItems", sem_totalCartItems);
			%><fd:SemPixelIncludeMedia pixelNames="CheetahMail" /><%
		
			/* TheSearchAgency Pixel */
			if ( !isOrderModify ) { //do not send on order modify
				//get ref to Pixel
				SemPixelModel semPixel_TSA = FDSemPixelCache.getInstance().getSemPixel("TheSearchAgency");
				
				sem_cartSubtotal = sem_cartSubtotal.replace(".", "");
				sem_totalDiscountAmount = sem_totalDiscountAmount.replace(".", "");
	
				//change triple zero ($0.00 -> 000) to single zero
				if ("000".equals(sem_cartSubtotal)) { sem_cartSubtotal = "0"; }
				if ("000".equals(sem_totalDiscountAmount)) { sem_totalDiscountAmount = "0"; }
				
	
				//add a param to the params sent to the FTL
				semPixel_TSA.setParam("subtotal", sem_cartSubtotal);
				semPixel_TSA.setParam("orderId", sem_orderNumber);
				semPixel_TSA.setParam("validOrders", sem_validOrderCount);
				semPixel_TSA.setParam("discountAmount", sem_totalDiscountAmount);
				%><fd:SemPixelIncludeMedia pixelNames="TheSearchAgency" /><%
			}
			

			/* Digo2 Pixel */
			if ( !isOrderModify ) { //do not send on order modify
				SemPixelModel semPixel_DIGO2 = FDSemPixelCache.getInstance().getSemPixel("DiGo2");
				FDUserI user_DIGO2 = (FDSessionUser)session.getAttribute(SessionName.USER);
				semPixel_DIGO2.clearParams();
	
				semPixel_DIGO2.setParam("checkout_receipt", "true");
				semPixel_DIGO2.setParam("subtotal", sem_cartSubtotal);
				semPixel_DIGO2.setParam("orderId", sem_orderNumber);
				%><fd:SemPixelIncludeMedia pixelNames="DiGo2" /><%
			}
			
		}
	%>
<% } %>
</tmpl:put>
</tmpl:insert>
