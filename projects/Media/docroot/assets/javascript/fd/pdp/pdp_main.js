var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var keyCode = fd.utils.keyCode;

  fd.components.Subtotal.update($('[data-component="product"][data-cmeventsource="pdp_main"]'));

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

  $('[data-component="customercm"]').on('click', function(e){
    e.stopPropagation();

    $(this).is(":checked") && FreshDirect.pdp.coremetrics && FreshDirect.pdp.coremetrics();
  });

  setUpPdpAccordions = function(){
    $(".pdp-accordion-item").each(function(){
      $(this).attr('tabindex', '0');

      var accordionContent = $(this).children("div");
      var accordionOpener = $(this).children("label");
      var newContentId = $(this).find('input[type="checkbox"]').attr('id') + '-accordion-content';

      accordionOpener.attr('aria-controls', newContentId);
      accordionContent.attr('id', newContentId);

      toggleAccordionARIA(accordionOpener, accordionContent, !accordionContent.height());
    });
  };

  toggleAccordionARIA = function($accordionOpener, $content, isHidden){
      $content.attr('aria-hidden', isHidden+"");
      $accordionOpener.attr('aria-expanded', !isHidden+"");
      console.log($accordionOpener[0]);
      console.log($content[0]);
  };

  $(".pdp-accordion-item").on('keypress keydown', function(e){
    var target = $(e.currentTarget),
        cb = target.find('input[type="checkbox"]');
    if(e.which === keyCode.ENTER || e.which === keyCode.SPACE){
      cb.prop('checked', !cb.prop('checked')).trigger('change');
    }
  });

  $(".pdp-accordion-item").on('change', function(){
      var content = $(this).children("div");
      toggleAccordionARIA($(this).children("label"), content, !!content.height());
  });

  $(document).ready(setUpPdpAccordions);

  fd.modules.common.utils.register("components", "pdp", { toggleAccordionARIA: toggleAccordionARIA }, fd);
}(FreshDirect));
