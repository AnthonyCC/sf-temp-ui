/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var evenBetterPopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: ''
    },
    bodyValue:{
    	value:'',
    	writable:true
    },
    template:{
      value:pdp.evenBetterPopup
    },
    bodySelector:{
    	value:'.evenbetter-popup-body'
    },
    bodyTemplate: {
      value: function(){
    	  return evenBetterPopup.bodyValue;
      }
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'evenBetterPopup'
    },
    popupConfig: {
      value: {
	    valign: 'top',
	    halign: 'left',
	    placeholder: true,
	    stayOnClick: true,
      zIndex: 500,
        overlay:true,
        delay: 300        
      }
    },
    open: {
      value: function (config) {
		var target = config.element,
			popupId=this.popupId;
		if(target.length){
			this.bodyValue = target[0].innerHTML;
			this.refreshBody({},this.bodyTemplate,pdp.evenBetterPopupHeader(config));
			this.popup.show(target);
		}
      }
    }
  });

  evenBetterPopup.render();
  
  $(document).on('mouseover','[data-evenbetteritem-trigger]',function(event){
	  var element = $(event.currentTarget).closest('[data-component="evenBetterItem"]');
	  evenBetterPopup.open({
		  element: element,
		  productId:element.data('productId'),
		  catId:element.data('catId'),
		  grpId: element.data('grpId')||null,
		  grpVersion: element.data('grpVersion')||null
	  });
  });
  
  fd.modules.common.utils.register("pdp", "evenBetterPopup", evenBetterPopup, fd);
}(FreshDirect));
