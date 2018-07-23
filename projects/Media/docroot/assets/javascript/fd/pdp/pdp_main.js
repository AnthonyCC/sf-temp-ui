var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

 var $ = fd.libs.$;
  var keyCode = fd.utils.keyCode;

 fd.components.Subtotal.update($('[data-component="product"][data-eventsource="pdp_main"]'));

 var productId=$('#BVRRContainer').data('productid');
  var setUpPdpAccordions, toggleAccordionARIA;

 if(window.$BV){
    window.$BV.ui('rr', 'show_reviews', { productId: productId });
  } else {
    //console.log("missing $BV");
  }

 window.pop=function(url,width,height){
    fd.components.ifrPopup.open({
      width:width,
      height:height,
      url:url
    });
  };

 setUpPdpAccordions = function(){
    $(".pdp-accordion-item").each(function(){
      //$(this).attr('tabindex', '0');

     var accordionContent = $(this).children("div");
      var toggler = $(this).find(".pdp-item-description-toggle:first");
      var newContentId = toggler.attr('id') + '-accordion-content';

     toggler.attr('aria-controls', newContentId);
      accordionContent.attr('id', newContentId);

     toggleAccordionARIA(toggler, accordionContent, !(!!toggler.attr('checked')));
    });
  };

 toggleAccordionARIA = function($accordionOpener, $content, isHidden){
      $content.attr('aria-hidden', isHidden+"");
      $accordionOpener.attr('aria-expanded', !isHidden+"");
  };


 $(".pdp-item-description-toggle").on('click', function(e){
      e.stopPropagation();
      $(this).attr('checked', (!!$(this).attr('checked')) ? null : 'checked');
      $(".pdp-accordion-item").trigger('change');
      return false;
  });

 $(".pdp-accordion-item").on('change', function(){
      var toggler = $(this).find(".pdp-item-description-toggle:first");
      toggleAccordionARIA(toggler, $(this).children("div"), !(!!toggler.attr('checked')));
  });

 $(document).ready(setUpPdpAccordions);

 fd.modules.common.utils.register("components", "pdp", { toggleAccordionARIA: toggleAccordionARIA }, fd);
}(FreshDirect));