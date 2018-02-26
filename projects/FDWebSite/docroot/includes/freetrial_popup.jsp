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
	        		var errorMessagePopup = '<div class="error-container"><div class="error-header error-header-noimg">Hold Your Horseradishes!</div>';
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
	        			var successMessagePopup = '<div class="dpn"><div class="dpn-container"><div class="dpn-success"><div class="dpn-success-header"><div class="dpn-success-check"><img src="/media/editorial/site_pages/deliverypass/images/large-check.svg" alt="Success check"></div><p>Success!</p><p>Thanks for signing up.</p></div>';
	        				successMessagePopup += '<div class="dpn-success-text"><p>Your DeliveryPass<sup>&reg;</sup> trial will activate on your next purchase.</br>Visit your DeliveryPass<sup>&reg;</sup> Settings for more details.</br>Get ready to save!</p></div>';
	        				successMessagePopup += '<div class="dpn-success-a"><a href="javascript:' + closePopup + '" class="dpn-success-start-shopping cssbutton cssbutton-flat orange">Start Saving</a></div><div class="dpn-success-setting"><a href="/your_account/delivery_pass.jsp">DeliveryPass<sup>&reg;</sup> Settings</a></div></div></div></div>';
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
				<div class="dpn-center-list-item">
					<div class="dpn-center-list-icon"><img src="/media/editorial/site_pages/deliverypass/images/small-check.svg" alt="check"></div>
					<div class="dpn-center-list-text">Unlimited Free Deliveries</div>
				</div>
				<div class="dpn-center-list-item">
					<div class="dpn-center-list-icon"><img src="/media/editorial/site_pages/deliverypass/images/small-check.svg" alt="check"></div>
					<div class="dpn-center-list-text">Exclusive Special Offers</div>
				</div>
				<div class="dpn-center-list-item">
					<div class="dpn-center-list-icon"><img src="/media/editorial/site_pages/deliverypass/images/small-check.svg" alt="check"></div>
					<div class="dpn-center-list-text">Timeslot Reservations</div>
				</div>
				<div class="dpn-center-list-item">
					<div class="dpn-center-list-icon"><img src="/media/editorial/site_pages/deliverypass/images/bonus-star.svg" alt="bonus star"></div>
					<div class="dpn-center-list-text">Bonus $5 off Tue-Fri Deliveries</div>
				</div>
			</div>
			<div class="dpn-center-agreement">By signing up for DeliveryPass<sup>&reg;</sup>, you are agreeing to the Terms and Conditions set forth here.</div>
			<div class="dpn-center-agreement-link"><a href="javascript:pop('/shared/template/generic_popup.jsp?contentPath=/media/editorial/picks/deliverypass/dp_tc.html&windowSize=large&name=Delivery Pass Information',400,560,alt='Delivery Pass Information')">Terms and Conditions</a></div>
			<div class="dpn-center-terms">Your DeliveryPass<sup>&reg;</sup> membership continues until canceled. You can cancel anytime by calling customer service at 1-866-283-7374.</div>
		</div>
		<div class="dpn-footer dpn-footer-login-required">
			<% if(user_dp.getLevel() == FDUserI.SIGNED_IN){ %>
				<div class="dpn-footer-no-logged">
					<a href="javascript:noThanks();">No thanks, I hate saving money.</a>
				</div>
				<div class="dpn-footer-yes">
					<div class="dpn-footer-yes-text">No commitments, cancel any time.</div>
					<button onclick="getFreeDelivery()" class="dpn-footer-yes-button cssbutton cssbutton-flat orange">Start 60 Day Free Trial</button>
				</div>
			<% } else { %>
				<div class="dpn-footer-no">
					<a href="javascript:noThanks();">No thanks, I hate saving money.</a>
				</div>
				<div class="dpn-footer-yes">
					<button class="dpn-footer-login-button cssbutton cssbutton-flat green" fd-login-required>Sign In to Get Started</button>
				</div>
			<% } %>
			<div class="clear"></div>
		</div>
	</div>
</div>