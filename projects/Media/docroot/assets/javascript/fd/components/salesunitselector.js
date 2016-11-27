/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	
	function triggerEvent($quantitybox, newVal) {
		$quantitybox.trigger('salesunit-change',{ newVal: newVal });
	}

  function markProductChanged($su) {
    var $product = $su.$product || $su.parents('[data-component=product]').first();

    if ($product && $product.size()) {
      $su.$product = $product;
      $product.addClass('changed');
    }
  }

  function salesUnitChanged(e){
		var $this;

		$this=$(e.currentTarget);
		markProductChanged($this);
		triggerEvent($this,$this.val());
	}
  
  /*
  * plugin for salesUnit
  *
  * usage: $([data-component="salesunit"]).quantityBox("show")
  */

  	var methods = {
      show:function(){
      }
    };

    $.fn.salesUnitSelector = function( method ) {

      // Method calling logic
      if ( methods[method] ) {
        return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
      } else {
        $.error( 'Method ' +  method + ' does not exist on jQuery.quantityBox' );
      }    
  	};

  

	$(document).on('keyup','[data-component="salesunit"]', salesUnitChanged);
	$(document).on('change','[data-component="salesunit"]', salesUnitChanged);
	$(document).on('click','[data-component="salesunit"]', salesUnitChanged);

}(FreshDirect));
