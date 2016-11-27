/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var customerRatingPopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: ''
    },
    template:{
      value:common.tooltip
    },
    bodySelector:{
    	value:'.tooltip-body'
    },
    bodyTemplate: {
      value: common.customerRatingPopup
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'customerRatingPopup'
    },
    popupConfig: {
      value: {
	    valign: 'bottom',
	    halign: 'left',
	    align:'tc-bc',
	    placeholder: false,
	    stayOnClick: false,
        overlay:false
      }
    },
    open: {
      value: function (config) {
		var target = config.element;
		// console.log(target.data());
		this.refreshBody({
			ratingValue:target.data('customerRating'),
			reviewCount:target.data('customerReviewcount')
		});
		this.popup.show(target);
		this.popup.clicked = true;
      }
    }
  });

  customerRatingPopup.render();
  
  $(document).on('mouseover',"[data-customer-rating]",function(event){
	  customerRatingPopup.open({
		  element: $(event.currentTarget)
	  });
  });
  
  $(document).on('mouseout',"[data-customer-rating]",function(event){
	  customerRatingPopup.close();
  });
  
  fd.modules.common.utils.register("components", "customerRatingPopup", customerRatingPopup, fd);
}(FreshDirect));
