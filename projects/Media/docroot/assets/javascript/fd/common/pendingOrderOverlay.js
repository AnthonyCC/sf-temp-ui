/*global jQuery,common,Bacon*/
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
	"use strict";
	var $=fd.libs.$;
	var POPUPWIDGET = fd.modules.common.popupWidget;
	var pendingOrderOverlay = Object.create(POPUPWIDGET, {
		template: { /* required */
			value: common.fixedPopup
		},
		headerContent: {
			value: 'Upcoming Orders'
		},
		bodyTemplate: { /* required */
			value: function() {
				return $('.pendingOrderBar-overlay-cont').html();
			}
		},
		popupId: {
			value: 'pendingOrderOverlay'
		},
		$trigger: {
			value: null
		},
		hasClose: {
			value: true
		},
		popupConfig: { /* required */
			value: {
				zIndex: 2000,
				placeholder: false,
				overlay: true,
				overlayExtraClass: 'centerpopupoverlay', /* use to fix ghost css */
				align: false,
				hidecallback: function (e) {
					/* do nothing extra on overlay click */
				}
			}
		},	
		open: {
			value: function () {
				this.popup.show($(document.body));
				this.popup.clicked = true;
			}
		},
		close: {
			value: function() {				
				if (this.popup) { this.popup.hide(); }
			}
		}
	});
	pendingOrderOverlay.render(); /* required before open */
	$(document).on('click', '.pendingOrderBar-viewall-btn', pendingOrderOverlay.open.bind(pendingOrderOverlay));

	//register
	fd.modules.common.utils.register("components", "pendingOrderOverlay", pendingOrderOverlay, fd);

}(FreshDirect));
