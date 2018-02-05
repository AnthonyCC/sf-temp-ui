/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	
	function init(time, dayOfWeek) {
		if (!time || !dayOfWeek)
			return;
		
		if (fd.mobWeb) {
			if (window.displayModifyingOrderMobile) {
				window.displayModifyingOrderMobile(time, dayOfWeek)
			} else {
				$(document).one('displayModifyingOrderMobile-loaded', function (){
					window.displayModifyingOrderMobile(time, dayOfWeek);
				});
			}
		} else {
			displayModifyingOrder(time, dayOfWeek);
		}
		
	}
	
	function displayModifyingOrder(time, dayOfWeek) {
		var msg = '<div id="location-modify-order-message" class="position-absolute"><div class="container"><strong>Modifying Delivery: </strong><span class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span class="text-uppercase">' + time + '</span></span></div></div>';
		if ($('#location-modify-order-message').length !== 0){
			$('#location-modify-order-message').replaceWith(msg);
			$('.locabar-section.locabar-addresses-section, #location-tabs').hide();
		} else if ($('#locationbar').length ) {
			$('#locationbar').append(msg);
			$('#locabar_addresses_trigger, #location-tabs').hide();
		}
		
	}
	
	// if component is not registered, register this component
	if (!fd.components.modifyOrderMessage) {
		var modifyOrderMessage = {
			init : init
		};
		
		if (fd.modules && fd.modules.common.utils) {
			// use fd.modules to register
			fd.modules.common.utils.register("components", "modifyOrderMessage", modifyOrderMessage,
					fd);
			$(document).trigger('modifyOrderMessage-loaded').off('modifyOrderMessage-loaded');
		} 
	}
	
}(FreshDirect));