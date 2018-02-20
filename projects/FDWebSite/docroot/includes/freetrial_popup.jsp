<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus id="user_dp" guestAllowed="true" recognizedAllowed="true" />

<script>
	function getFreeDelivery(){
		$jq.ajax({
	        url: '/api/freetrial',
	        type: 'POST',
	        success: function(message) {
	        	if(message.STATUS == "ERROR"){
	        		var errorMessagePopup = '<div class="error-container"><div class="error-img"><img src="/media/editorial/site_pages/error_message/error_icecream.svg" alt="FreshDirect" border="0"></div><div class="error-header">Hold Your Horseradishes!</div>';
	        		errorMessagePopup += '<div class="error-text">' + message.MESSAGE + '</div>';
	        		errorMessagePopup += '<a class="error-button cssbutton cssbutton-flat green" href="/">Continue Shopping</a></div>';
	        		doOverlayDialogByHtmlNew(errorMessagePopup);
	        	} else {
	        		if($jq(".ui-dialog:visible .dpn").length > 0){
	        			doOverlayDialogNew("/includes/freetrialsuccess_popup.jsp");
	        		} else {
	        			window.location.href = "/freetrialsuccess.jsp";
	        		}
	        	}
	        },
	        error: function() {
	        	doOverlayDialogNew("/includes/error_message_popup.jsp");
	        }
	 	});
	}
</script>

<div class="dpn">
	<div class="dpn-container">
		<div class="dpn-header">
			<div class="dpn-header-text">Get Free Delivery for 60 Days</div>
			<div class="dpn-header-desc">Save every time you order, an unlimited amount of times!</div>
		</div>
		<div class="dpn-center">
			<div class="dpn-center-header">DeliveryPass<sup>&reg;</sup></div>
			<div class="dpn-center-list">
				<div class="dpn-center-list-item"><div class="dpn-center-list-icon">&#10003;</div><div class="dpn-center-list-text">Unlimited Free Deliveries</div></div>
				<div class="dpn-center-list-item"><div class="dpn-center-list-icon">&#10003;</div><div class="dpn-center-list-text">Exclusive Special Offers</div></div>
				<div class="dpn-center-list-item"><div class="dpn-center-list-icon">&#10003;</div><div class="dpn-center-list-text">Timeslot Reservations</div></div>
				<div class="dpn-center-list-item"><div class="dpn-center-list-icon-star">&#9733;</div><div class="dpn-center-list-text">Bonus $5 off Tue-Fri Deliveries</div></div>
			</div>
			<div class="dpn-center-agreement">By signing up for DeliveryPass<sup>&reg;</sup>, you are agreeing to the Terms and Conditions set forth <a href="javascript:pop('/shared/template/generic_popup.jsp?contentPath=/media/editorial/picks/deliverypass/dp_tc.html&windowSize=large&name=Delivery Pass Information',400,560,alt='Delivery Pass Information')">here</a>.</div>
			<div class="dpn-center-terms">Your DeliveryPass<sup>&reg;</sup> membership continues until canceled. If you do not wish to continue, you may cancel any time by visiting Your Account.</div>
		</div>
		<div class="dpn-footer dpn-footer-login-required">
			<div class="dpn-footer-no">
				<a href="/">No thanks, I hate saving money.</a>
			</div>
			<div class="dpn-footer-yes">
				<% if(user_dp.getLevel() == FDUserI.SIGNED_IN){ %>
					<div class="dpn-footer-yes-text">No commitments, cancel any time.</div>
					<button onclick="getFreeDelivery()" class="dpn-footer-yes-button cssbutton cssbutton-flat orange">Start 2-Month Free Trial</button>
				<% } else { %>
					<button class="dpn-footer-login-button cssbutton cssbutton-flat green" fd-login-required>Sign In to Get Started</button>
				<% } %>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>

<fd:css href="/assets/css/delivery_pass.css" />