/*global common*/
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
    scrollCheck: {
        value:['.fixedPopupContent','.qs-popup-content']
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
        nutritionPopup.noscroll();
        if($('.mm-page').length){        	
        	$('#nutritionPopup').addClass("mobWeb-nutritionPopup");
    	}
      }
    },
    close: {
      value: function (e) {
        if (nutritionPopup.popup && nutritionPopup.popup.shown) {
          nutritionPopup.popup.hide(e);
          $('.pdp-accordion-nutrition>input[type="checkbox"]').prop('checked', false).trigger('change');
        }
      }
    }
  });

  nutritionPopup.render();

  $(document).on('change','.pdp-accordion-nutrition', function(e){
    e.preventDefault();
    $(e.target).prop('checked') ? nutritionPopup.open() : nutritionPopup.close();
  });

  fd.modules.common.utils.register("components", "nutritionPopup", nutritionPopup, fd);
}(FreshDirect));
