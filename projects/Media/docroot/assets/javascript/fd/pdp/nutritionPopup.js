/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var keyCode = fd.utils.keyCode;

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
      }
    }
  });

  nutritionPopup.render();

  $(document).on('click','.nutropen',function(e){
    e.preventDefault();
    nutritionPopup.open();
  });

  $(document).on('keypress','.pdp-accordion-nutrition', function(e){
    e.preventDefault();
    if(e.which === keyCode.ENTER || e.which === keyCode.SPACE){
      nutritionPopup.open();
    }
  });

  fd.modules.common.utils.register("components", "nutritionPopup", nutritionPopup, fd);
}(FreshDirect));
