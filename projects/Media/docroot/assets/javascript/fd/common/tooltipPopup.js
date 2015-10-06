/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var tooltipPopup = Object.create(POPUPWIDGET,{
    customClass: {
    	value:'noarrow'
    },
    template:{
      value:common.tooltip
    },
    bodySelector:{
    	value:'.tooltip-body'
    },    
    bodyTemplate: {
      value: common.tooltipPopup
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'tooltipPopup'
    },
    popupConfig: {
      value: {
	    valign: 'bottom',
	    halign: 'left',
	    placeholder: false,
	    stayOnClick: false,
        overlay:false
      }
    },
    open: {
      value: function (config) {
		var target = config.element,
			align = target.data("tooltipalign") || 'tc-bc',
			body = target.nextAll('[data-component="tooltipContent"]')[0];
		
		if(body){
			this.popup.align = align;
			this.refreshBody({body:body.innerHTML});
			this.popup.show(target,align);
		}
      }
    }
  });

  tooltipPopup.render();
  
  $(document).on('mouseover','[data-component="tooltip"]',function(event){
	  tooltipPopup.open({
		  element: $(event.currentTarget)
	  });
  });
  
  $(document).on('mouseout','[data-component="tooltip"]',function(event){
	  tooltipPopup.close();
  });
  
  fd.modules.common.utils.register("components", "tooltipPopup", tooltipPopup, fd);
}(FreshDirect));
