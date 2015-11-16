var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var keyCode = fd.utils.keyCode;

  fd.components.Subtotal.update($('[data-component="product"][data-cmeventsource="pdp_main"]'));

  var productId=$('#BVRRContainer').data('productid');

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


  var setUpPdpAccordions = function(){
    $(".pdp-accordion-item").each(function(){
      var content = $(this).find("div");
      var accordionOpener = $(this).find("label");
      var isContentOpen = content.height() ? true : false;
      var newContentId = $(this).find('input[type="checkbox"]').attr('id') + '-accordion-content';

      accordionOpener.attr('aria-controls', newContentId);
      content.attr('id', newContentId);

      accordionOpener.attr('aria-expanded', isContentOpen+"");
      content.attr('aria-hidden', !isContentOpen+"");
    });
  };

  var toggleAccordionARIA = function($accordionOpener, $content){
      var isContentOpen = $content.height() ? true : false;
      // reverse aria attributes
      $accordionOpener.attr('aria-expanded', !isContentOpen+"");
      $content.attr('aria-hidden', isContentOpen+"");
  };

  $(".pdp-accordion-item").each(function(){
    $(this).attr('tabindex', '0');
  });

  $(".pdp-accordion-item").on('keypress', function(e){
    var target = $(e.currentTarget),
        cb = target.find('input[type="checkbox"]');
    if(e.which === keyCode.ENTER || e.which === keyCode.SPACE){
      cb.prop('checked', !cb.prop('checked'));
      toggleAccordionARIA($(this).find("label"), $(this).find("div"));
    }
  });

  $(".pdp-accordion-item").on('change', function(){
      toggleAccordionARIA($(this).find("label"), $(this).find("div"));
  });

  $(document).ready(setUpPdpAccordions);

  fd.modules.common.utils.register("components", "pdp", {}, fd);
}(FreshDirect));
