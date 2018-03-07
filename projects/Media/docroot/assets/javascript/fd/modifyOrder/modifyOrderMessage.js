var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	
	function init(time, dayOfWeek) {
		if (!time || !dayOfWeek)
			return;
		
		if (fd.mobWeb) {
			displayModifyingOrderMobile(time, dayOfWeek);
		}
		
	}
	
	function displayModifyingOrderMobile(time, dayOfWeek) {
			if (!time || !dayOfWeek)
				return;
			var msg = '<div id="location-modify-order-message" style="position: static;text-align: center;font-size: 14px;line-height: 16px;padding: 15px 0 15px 0;background-color: #f68139;text-shadow: 1px 1px 0 #c85c19;color: #ffffff;border-bottom: none;">'+
			'<div><strong class="modify-delivery-label">Modifying Delivery: </strong><div class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span style="text-transform: uppercase;text-shadow: 1px 1px 0 rgba(200, 92, 25, 0.5);">' + time + '</span></div></div></div>';
			if ($('#location-modify-order-message').length !== 0){
				$('#location-modify-order-message').replaceWith(msg);
			} else if ($('.mobweb-topnav .navbar').length) {
				$(msg).insertBefore('.mobweb-topnav .navbar');
			}
			
		}
	function stickyMessage(sel, topSpacing) {
		$(document).ready(function() {
			$(sel).sticky({topSpacing: topSpacing, zIndex: 9999999});
		});
	}
	// if component is not registered, register this component
	if (!fd.components.modifyOrderMessage) {
		var modifyOrderMessage = {
			init : init,
			stickyMessage: stickyMessage
		};
		
		if (fd.modules && fd.modules.common.utils) {
			// use fd.modules to register
			fd.modules.common.utils.register("components", "modifyOrderMessage", modifyOrderMessage,
					fd);
			
		} else {
			fd.components.modifyOrderMessage = modifyOrderMessage;
		}
		$(document).trigger('modifyOrderMessage-loaded').off('modifyOrderMessage-loaded');
	}
	
}(FreshDirect));