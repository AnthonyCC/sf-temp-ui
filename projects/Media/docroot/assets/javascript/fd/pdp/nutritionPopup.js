/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var nutritionPopup = Object.create(POPUPWIDGET,{
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
    	value:'.qs-popup-content'
    },    
    bodyTemplate: {
      value: function(){
    	  return $('.pdp-accordion-nutrition [data-component="popupContent"]').html();
      }
    },
    $trigger: {
      value: null
    },
    hasClose: {
        value: true
    },
    popupId: {
      value: 'nutritionPopup'
    },
    popupConfig: {
      value: {
	    valign: 'bottom',
	    halign: 'left',
	    placeholder: false,
	    stayOnClick: false,
        overlay:true
      }
    },
    open: {
      value: function (config) {
        	  nutritionPopup.refreshBody(config);
        	  nutritionPopup.popup.clicked=true;
        	  nutritionPopup.popup.show($('body'),false);
      }
    }
  });

  nutritionPopup.render();
  
  $(document).on('click','.pdp-accordion-nutrition .nutrition-overlay',function(){
	  nutritionPopup.open();
  });
  
  fd.modules.common.utils.register("components", "nutritionPopup", nutritionPopup, fd);
}(FreshDirect));
