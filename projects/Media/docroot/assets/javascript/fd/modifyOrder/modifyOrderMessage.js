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
		initAlignFunction();
	}
	
	function displayModifyingOrderMobile(time, dayOfWeek) {
			if (!time || !dayOfWeek)
				return;
			var popupConfig = {"buttons":[{"id":"accept","class":"large cssbutton orange","name":"Cancel Changes"},{"id":"deny","class":"large cssbutton green transparent","name":"Nevermind"}]};

			var msg = '<div id="location-modify-order-bar" class="modify-order-bar" style="font-size: 14px;line-height: 16px;">'+
			'<div class="location-modify-order-message" style="float: left; text-align:left; padding-left: 15px;"><strong class="modify-delivery-label">Modifying Order: </strong><div class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span style="text-transform: uppercase;">' + time + '</span></div></div>'+
			
			'<div class="location-modify-order-cancel" style="float: right">'+
			'<span><a class="cancel-changes-link" href="javascript:void(0);" role="alertdialog" '+
			'data-alignpopupfunction="modifyOrderAlign" '+
			'data-confirm-data=\''+ JSON.stringify(popupConfig)+'\' '+
			'data-confirm-button-accept="FreshDirect.components.modifyOrderMessage.cancelChanges" '+
			'data-confirm-class="cancel-modify-confirm-popup" '+
			'data-hide-background="true" '+
			'data-confirm data-confirm-message="Are you sure you want to <br> cancel all changes?" data-confirm-template="common.confirmpopup" class="">Cancel Changes</a></span></div>'+
			'<div style="clear:both"></div>'+
			'</div>';
			
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
		
		if (!fd.mobWeb){
			var originalZIndex = null;
			
			$(sel).on('sticky-start', function() {
				originalZIndex = $('#toptoolbar').css('z-index');
				$('#toptoolbar').css('z-index', "600");
			});
		    $(sel).on('sticky-end', function() {
		    	$('#toptoolbar').css('z-index', originalZIndex);
		    });
		}
		
	}
	
	function cancelChanges() {
		if (fd.gtm) {
			fd.gtm.updateDataLayer({
			  cancelModifyOrder: 'banner'
		  });
		}
		document.location.href = "/your_account/cancel_modify_order.jsp";
	}
	
	function initAlignFunction(mobweb) {
		fd.popups = fd.popups || {};
		fd.popups.alignment = fd.popups.alignment || {};
		fd.popups.alignment.modifyOrderAlign = function () {
			if (this.$el) {
				if (!fd.mobWeb) {
					this.$el.css('left', '50%');
					this.$el.css('transform', 'translateX(-50%)');
					this.$el.css('top', '100px');
				} else {
					this.$el.css('top', ($('#location-modify-order-bar').parent('.is-sticky').length? 130 : 170) + 'px');
					
				}
			}
		}
	}
	// if component is not registered, register this component
	if (!fd.components.modifyOrderMessage) {
		var modifyOrderMessage = {
			init : init,
			stickyMessage: stickyMessage,
			cancelChanges: cancelChanges,
			initAlignFunction: initAlignFunction
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