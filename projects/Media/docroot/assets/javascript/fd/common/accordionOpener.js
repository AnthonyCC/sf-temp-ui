/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  /**
   * Opening handled by CSS
   */
  function openAccordion(accordionCb){
    $(accordionCb).attr('checked', 'checked');
  }
  
  /**
   * Open accordion in IE. This functionality done by css with a
   * checkbox change state (as in openAccordion) in other browsers. 
   */
  function openAccordionIE(el){
	var $el = $(el);

	$el.siblings("div").addClass("pdp-accordion-content");
	$el.siblings("label").find(".pdp-accordion-item-carrot").addClass("down");
  }

  /**
   * Open accordions from Variation Matrix links (right side of the page)
   */
  $(document).on('click','[data-component="accordion-opener"]',function(event){

      var targ = $(event.currentTarget),
          accTarg = $("#" + targ.data('accordion-target'));

      if(accTarg){
    	  if($("body").hasClass("ie8") && !accTarg.checked ){
    		  openAccordionIE( accTarg );
    	  }
    	  else{
    		  openAccordion(accTarg);
    	  }
      }
  });

  /**
   * Open accordions with JS by clicking on its label (only IE).
   * In other browsers it's handled by CSS.
   */
  function openCloseAccordion(el){
    var $el = $(el);

    $el.siblings("div").toggleClass("pdp-accordion-content");
    $el.find(".pdp-accordion-item-carrot").toggleClass("down");
  }

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
