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