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
			var popupConfig = {"buttons":[{"id":"accept","class":"large cssbutton orange","name":"Exit Modifying Order"},{"id":"deny","class":"large cssbutton green transparent","name":"Nevermind"}]};

			var msg = '<div id="location-modify-order-bar" class="modify-order-bar">'+
				'<div class="location-modify-order-message"><strong class="modify-delivery-label">Modifying Order: </strong><div class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span class="text-uppercase">' + time + '</span></div></div>'+
			
				'<div class="location-modify-order-btn-cont">'+
					'<span><a class="cssbutton small whiteborder transparent cancel-changes-link" aria-label="exit modify order mode" href="javascript:void(0);" role="alertdialog" '+
						'data-alignpopupfunction="modifyOrderAlign" '+
						'data-confirm-data=\''+ JSON.stringify(popupConfig)+'\' '+
						'data-confirm-button-accept="FreshDirect.components.modifyOrderMessage.cancelChanges" '+
						'data-confirm-button-deny="FreshDirect.components.modifyOrderMessage.keepModifyMode" '+
						'data-confirm-class="cancel-modify-confirm-popup" '+
						'data-hide-background="true" '+
						'data-confirm data-confirm-message="Are you sure? Exiting will cancel all unsaved changes to your order." data-confirm-template="common.confirmpopup" class="">Exit</a></span>'+
					'<a class="cssbutton small orange transparent save-changes-btn" href="/expressco/checkout.jsp">Save</a>'+
				'</div>'+
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
		});
	}
	
	function keepModifyMode() {
		if (fd.gtm && fd.gtm.updateDataLayer) {
			fd.gtm.updateDataLayer({
				  keepModifyOrder: null
			  });
		}
	}
	
	function cancelChanges() {
		if (fd.gtm && fd.gtm.updateDataLayer) {
			fd.gtm.updateDataLayer({
			  cancelModifyOrder: 'banner modal'
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
			keepModifyMode: keepModifyMode,
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

	function saveChanges() {
		if (fd.gtm && fd.gtm.updateDataLayer) {
			fd.gtm.updateDataLayer({
				'event': 'savechanges-click',
				'eventCategory': 'modify',
				'eventAction': 'save changes',
				'eventLabel': 'active'
			});
		}
	}
	
	$(document).on('click', '.modify-order-bar .save-changes-btn', saveChanges);
	
}(FreshDirect));