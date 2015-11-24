/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var toggleAccordionARIA = fd.components.pdp.toggleAccordionARIA;
  var nutritionPopup, nutritionAccordion;

  nutritionPopup = Object.create(POPUPWIDGET,{
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
    },
    close: {
      value: function (e) {
        if (nutritionPopup.popup && nutritionPopup.popup.shown) {
          nutritionPopup.popup.hide(e);
          nutritionAccordion.close();
        }
      }
    }
  });

  nutritionPopup.render();

  nutritionAccordion = {
    SELECTOR:'.pdp-accordion-nutrition',
    OPENER:'.pdp-accordion-nutrition>label',
    CONTENT:'.pdp-accordion-nutrition>div',
    CHECKBOX:'.pdp-accordion-nutrition>input[type="checkbox"]',
    open: function(){
      $(nutritionAccordion.CHECKBOX).prop('checked', true);
      nutritionPopup.open();
      if(toggleAccordionARIA){
        toggleAccordionARIA($(nutritionAccordion.OPENER), $(nutritionAccordion.CONTENT), false);
      }
    },
    close: function(){
      $(nutritionAccordion.CHECKBOX).prop('checked', false);
      nutritionPopup.close();
      if(toggleAccordionARIA){
        toggleAccordionARIA($(nutritionAccordion.OPENER), $(nutritionAccordion.CONTENT), true);
      }
    }
  };

  $(document).on('change','.pdp-accordion-nutrition', function(e){
    e.preventDefault();
    $(e.target).prop('checked') ? nutritionAccordion.open() : nutritionAccordion.close();
  });

  fd.modules.common.utils.register("components", "nutritionPopup", nutritionPopup, fd);
}(FreshDirect));
