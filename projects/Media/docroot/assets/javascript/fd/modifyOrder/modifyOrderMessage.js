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
			var popupConfig = {"buttons":[{"id":"accept","class":"large cssbutton orange","name":"Cancel Changes"},{"id":"deny","class":"large cssbutton green transparent","name":"Nevermind"}]};
			
			var msg = '<div id="location-modify-order-bar" style="font-size: 14px;line-height: 16px;padding: 15px 0 15px 0;background-color: #f68139;text-shadow: 1px 1px 0 #c85c19;color: #ffffff;border-bottom: none;">'+
			'<div class="location-modify-order-message" style="float: left; text-align:left; padding-left: 15px;"><strong class="modify-delivery-label">Modifying Delivery: </strong><div class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span style="text-transform: uppercase;text-shadow: 1px 1px 0 rgba(200, 92, 25, 0.5);">' + time + '</span></div></div>'+
			
			'<div class="location-modify-order-cancel" style="float: right; padding: 10px 15px 0 0">'+
			'<a style="color:white" href="javascript:void(0);" role="alertdialog" '+
			'data-alignpopupfunction="modifyOrderAlign" '+
			'data-confirm-data=\''+ JSON.stringify(popupConfig)+'\' '+
			'data-confirm-button-accept="FreshDirect.components.modifyOrderMessage.cancelChanges" '+
			'data-confirm-class="cancel-modify-confirm-popup" '+
			'data-hide-background="true" '+
			'data-confirm data-confirm-message="Are you sure you want to <br> cancel all changes?" data-confirm-template="common.confirmpopup" class="">Cancel Changes</a></div>'+
			//'<div class="location-modify-order-cancel" style="float: right; padding: 10px 15px 0 0"><a style="color:white" href="/your_account/cancel_modify_order.jsp" >Cancel Changes</a></div>'+
			'<div style="clear:both"></div>'+
			'</div>';
			fd.popups = fd.popups || {};
			fd.popups.alignment = fd.popups.alignment || {};
			fd.popups.alignment.modifyOrderAlign = function () {
				if (this.$el) {
					this.$el.css('top', ($('#location-modify-order-bar').parent('.is-sticky').length? 150 : 170) + 'px');
				}
			}
			if ($('#location-modify-order-message').length !== 0){
				$('#location-modify-order-message').replaceWith(msg);
			} else if ($('.mobweb-topnav .navbar').length) {
				$(msg).insertBefore('.mobweb-topnav .navbar');
			}
			
		}
	function stickyMessage(sel, topSpacing) {
		$(document).ready(function() {
			$(sel).sticky({topSpacing: topSpacing, zIndex: 1000});
		});
	}
	function cancelChanges() {
		document.location.href = "/your_account/cancel_modify_order.jsp";
	}
	// if component is not registered, register this component
	if (!fd.components.modifyOrderMessage) {
		var modifyOrderMessage = {
			init : init,
			stickyMessage: stickyMessage,
			cancelChanges: cancelChanges
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