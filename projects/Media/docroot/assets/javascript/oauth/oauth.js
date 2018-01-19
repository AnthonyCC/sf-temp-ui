var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	function requestForCode() {
		document.location = '/api/oauth2/authorize' + window.location.search;
	}
	function declineForCode() {
		document.location = $('.permisson-container #redirect_uri').val() + '?error=user_cancelled_authorize&error_description=The+user+cancelled+the+authorization'+
		'&state=' + $('.permisson-container #state').val();
	}
	
	function init() {
		$('.permisson-container #accept-permission').click(requestForCode);
		$('.permisson-container #decline-permission').click(declineForCode);
	}
	$( document ).ready(init);
}(FreshDirect));