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
	        	var closePopup = "$jq('.overlay-dialog-new .ui-dialog-titlebar-close').click()";
	        	if(message.STATUS == "ERROR"){
	        		var errorMessagePopup = '<div class="error-container"><div class="error-img"><img src="/media/editorial/site_pages/error_message/error_icecream.svg" alt="FreshDirect" border="0"></div><div class="error-header">Hold Your Horseradishes!</div>';
	        		errorMessagePopup += '<div class="error-text">' + message.MESSAGE + '</div>';
	        		if($jq(".ui-dialog:visible .dpn").length > 0){
	        			errorMessagePopup += '<a class="error-button cssbutton cssbutton-flat green" href="javascript:' + closePopup + '">Continue Shopping</a></div>';
	        		} else {
	        			errorMessagePopup += '<a class="error-button cssbutton cssbutton-flat green" href="/">Continue Shopping</a></div>';
	        		}
	        		doOverlayDialogByHtmlNew(errorMessagePopup);
	        	} else {
	        		if($jq(".ui-dialog:visible .dpn").length > 0){
	        			$jq("#cartcontent").trigger('cartcontent-update');
	        			var successMessagePopup = '<div class="dpn"><div class="dpn-container"><div class="dpn-success"><div class="dpn-success-header"><div class="dpn-success-check">&#10004;</div><p>Success!</p><p>Thanks for signing up.</p></div>';
	        				successMessagePopup += '<div class="dpn-success-text"><p>Your DeliveryPass<sup>&reg;</sup> trial will activate on your next purchase. Get ready to save!</p><p>You can edit your DeliveryPass setting <a href="/your_account/delivery_pass.jsp">here</a>.</p></div>';
	        				successMessagePopup += '<div class="dpn-success-a"><button onclick="' + closePopup + '" class="dpn-success-start-shopping cssbutton cssbutton-flat orange">Start Saving</button></div></div></div></div>';
	        				doOverlayDialogByHtmlNew(successMessagePopup);
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
	function noThanks(){
		if($jq(".ui-dialog:visible .dpn").length > 0){
			$jq('.overlay-dialog-new .ui-dialog-titlebar-close').click()
		} else {
			window.location.href = "/";
		}
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
				<a href="javascript:noThanks();">No thanks, I hate saving money.</a>
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