/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var ajaxPopup = Object.create(POPUPWIDGET,{
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
    	value:'.qs-popup-content'
    },    
    bodyTemplate: {
      value: common.ajaxPopup
    },
    $trigger: {
      value: null
    },
    hasClose: {
        value: true
    },
    popupId: {
      value: 'ajaxpopup'
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
		var target = config.element,
			url = target.attr('href');
		
		$.get(url,function(data){
    		ajaxPopup.refreshBody({body:data});
    		ajaxPopup.popup.show($('body'),false);			
		});
		
      }
    }
  });

  ajaxPopup.render();
  
  $(document).on('click','[data-component="ajaxpopup"]',function(event){
	  ajaxPopup.open({
		  element: $(event.currentTarget)
	  });
	  event.preventDefault();
  });
  
  fd.modules.common.utils.register("components", "ajaxPopup", ajaxPopup, fd);
}(FreshDirect));
