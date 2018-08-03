/*global jQuery,common,Bacon*/
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
	"use strict";
	var $=fd.libs.$;
	var POPUPWIDGET = fd.modules.common.popupWidget;
	var assistiveOptionsOverlay = Object.create(POPUPWIDGET, {
		template: { /* required */
			value: common.fixedPopup
		},
		headerContent: {
			value: 'Assistive Options'
		},
		bodyTemplate: { /* required */
			value: common.assistiveOptions
		},
		popupId: {
			value: 'assistiveOptionsOverlay'
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
		},
		signal:  {
            value: 'assistiveType'
        },
        update: {
        	value: function(){
        		var assistiveMode = localStorage.getItem("assistive-enabled");
        		if(assistiveMode === "true") {
        			$(".assistiveWrapper .switch :checkbox").prop("checked", true);
					$('body').addClass("assistive-mode");
        		}
        	}
        }
	});
	
	assistiveOptionsOverlay.render(); /* required before open */
	if($("#assistiveOptionsOverlay").length>0){
		assistiveOptionsOverlay.listen();
		assistiveOptionsOverlay.update();
	}
	$(document).on('click', '.assistive-options', assistiveOptionsOverlay.open.bind(assistiveOptionsOverlay));
		//register
	fd.modules.common.utils.register("components", "assistiveOptionsOverlay", assistiveOptionsOverlay, fd);

}(FreshDirect));
