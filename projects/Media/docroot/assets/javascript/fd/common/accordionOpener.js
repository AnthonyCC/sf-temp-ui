/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  function openAccordion(accordionCb){
    $(accordionCb).attr('checked', 'checked');
  }

  $(document).on('click','[data-component="accordion-opener"]',function(event){

      var targ = $(event.currentTarget),
          accTarg = $("#" + targ.data('accordion-target'));

      if(accTarg){
          openAccordion(accTarg);
      }
  });

  function openCloseAccordion(el){
    var $el = $(el);

    $el.siblings("div").toggleClass("pdp-accordion-content");
    $el.find(".pdp-accordion-item-carrot").toggleClass("down");
  }

  //open accordions from JS by hand if IE8
  if($("body").hasClass("ie8")){
    $(document).on('click','.pdp-accordion > li > label',function(event){
        openCloseAccordion( $(event.currentTarget) );
    });

    // open these accordions by default
    openCloseAccordion( $(".pdp-accordion-description > label") );
    openCloseAccordion( $(".pdp-accordion-allergens > label") );
  }
  
  fd.modules.common.utils.register("components", "openAccordion", openAccordion, fd);
}(FreshDirect));
