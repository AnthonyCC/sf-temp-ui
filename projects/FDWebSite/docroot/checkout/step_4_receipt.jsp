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
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_4_RECEIPT_TOTAL = 970;
%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Order Placed</tmpl:put>
<tmpl:put name='content' direct='true'>
<%  boolean isModifyOrder = Boolean.parseBoolean(NVL.apply((String)request.getAttribute("modifyOrderMode"), "false"));
    if(!isModifyOrder) {
	FDUserI curruser = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity curridentity  = curruser.getIdentity();
	ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(curridentity);
	if(cm.getMobilePreference() == null) {
		String _ordNum = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
		//session.removeAttribute("SMSSubmission"+ _ordNum);
		if(session.getAttribute("SMSSubmission" + _ordNum) == null) { %>
		<script type="text/javascript" src="/assets/javascript/rounded_corners.inc.js"></script>
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
			afterLoad: function() {
					$('MB_frame').style.border = '1px solid #CCCCCC';
					$('MB_header').style.border = '0px solid #CCCCCC';
					$('MB_header').style.display = 'block';
					window.scrollTo(0,0);					
					$('MB_window').style.width = 'auto';
					$('MB_window').style.height = 'auto';
					$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
					$('MB_content').style.padding = '0px';
					$('MB_close').style.color = 'gray';
					$('MB_close').style.background = "url(/media/editorial/site_access/images/round_x.gif) no-repeat";

					curvyCornersHelper('MB_frame', ccSettings);
			},
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }	
		});
	}
</script>
<script language="javascript">
doRemoteOverlay('sms_capture.jsp');
</script>
<%  } } }%>
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
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="" /><br />
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

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>">
<TR VALIGN="BOTTOM">      
    <TD colspan="2" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>" ALIGN="RIGHT">
<%
	if (user.isChefsTable()) {        
%>		<a href="/your_account/manage_account.jsp"><img src="/media_stat/images/template/checkout/loy_global_member_stars_2008.gif" width="314" height="9" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0"/></a><br />
<%
	}
%>
    </TD>
</TR>
<TR VALIGN="BOTTOM">
  <TD WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL-200%>"><A HREF="/index.jsp"><img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" border="0" alt="FreshDirect"></A><img src="/media_stat/images/layout/clear.gif" width="95" height="1"><img src="/media_stat/images/navigation/egplnt_reg_checkout.jpg" WIDTH="87" HEIGHT="69" border="0" alt="Eggplant" HSPACE="10"></TD>
  <TD WIDTH="200" ALIGN="right">    
        <A HREF="javascript:window.print();" onMouseOver="swapImage('print','/media/images/navigation/global_nav/print_page_01.gif')" onMouseOut="swapImage('print','/media/images/navigation/global_nav/print_page.gif')"><img name="print" src="/media_stat/images/navigation/global_nav/print_page.gif" width="54" height="26" border="0" alt="PRINT PAGE"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/index.jsp" onMouseOver="swapImage('home_top','/media/images/navigation/global_nav/go_home_01.gif')" onMouseOut="swapImage('home_top','/media/images/navigation/global_nav/go_home.gif')"><img name="home_top" src="/media_stat/images/navigation/global_nav/go_home.gif" width="56" height="26" border="0" alt="GO HOME"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/quickshop/index.jsp" onMouseOver="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quickshop_r.gif')" onMouseOut="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif')"><img name="quickshop" src="/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif" width="54" height="26" border="0" alt="QUICKSHOP"></A>
  </TD>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
  <td colspan="2"><IMG src="/media_stat/images/layout/669933.gif" HEIGHT="4" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>"></td>
</tr>
</TABLE>
<%
	//this needs to be BEFORE the i_checkout_receipt.jspf include, since it's used in it
	String sem_orderNumber = "0";
	sem_orderNumber = NVL.apply((String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER), "0");
%>

<%@include file="/checkout/includes/i_checkout_receipt.jspf"%>

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

		/* Publicis Pixel */
		SemPixelModel semPixel_PUB = FDSemPixelCache.getInstance().getSemPixel("Publicis");

		//add a param to the params sent to the FTL
		semPixel_PUB.setParam("orderId", sem_orderNumber);
		semPixel_PUB.setParam("validOrders", sem_validOrderCount);
		semPixel_PUB.setParam("isNew", ("0".equals(sem_validOrderCount))?"true":"false");
		semPixel_PUB.setParam("subtotal", sem_cartSubtotal);
		semPixel_PUB.setParam("isOrderModify",String.valueOf(isOrderModify));
		semPixel_PUB.setParam("userCounty", sem_defaultCounty);
		semPixel_PUB.setParam("totalCartItems", sem_totalCartItems);
		semPixel_PUB.setParam("curPage", request.getRequestURI());
		%><fd:SemPixelIncludeMedia pixelNames="Publicis" /><%


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

		/* LinkShare Pixel */
		if ( !isOrderModify ) { //do not send on order modify
		    //get ref to Pixel
			SemPixelModel semPixel_LS = FDSemPixelCache.getInstance().getSemPixel("LinkShare");
			
			sem_totalDiscountAmount = sem_totalDiscountAmount.replace(".", "");

			//change triple zero ($0.00 -> 000) to single zero
			if ("000".equals(sem_totalDiscountAmount)) { sem_totalDiscountAmount = "0"; }

			//add a param to the params sent to the FTL
			semPixel_LS.setParam("orderId", sem_orderNumber);
			semPixel_LS.setParam("subtotal", sem_cartSubtotal);
			semPixel_LS.setParam("discountAmount", sem_totalDiscountAmount);
			semPixel_LS.setParam("isNew", ("1".equals(sem_validOrderCount))?"true":"false");
			semPixel_LS.setParam("isOrderModify",String.valueOf(isOrderModify));
			%><fd:SemPixelIncludeMedia pixelNames="LinkShare" />
			
<%
		}
	}
%>

</tmpl:put>
</tmpl:insert>
