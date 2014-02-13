/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  
  var QuantityBox = { initialized: false };

  function triggerEvent($quantitybox, newVal) {
    $quantitybox.trigger('quantity-change',{ newVal: newVal });
    $quantitybox.trigger('quantity-change-'+($quantitybox.find('input.qty').val()<newVal?'inc':'dec'));
  }

  function getInput($quantitybox) {
    return $('input[data-component="quantitybox.value"]',$quantitybox);
  }

  function markProductChanged($quantitybox) {
    var $product = $quantitybox.$product || $quantitybox.parents('[data-component=product]').first();

    if ($product && $product.size()) {
      $quantitybox.$product = $product;
      $product.addClass('changed');
    }
  }

  function chgQty(value, min, max, inc) {
    var qty = parseFloat(value) + 0;

    if (isNaN(qty) || qty < min) {
      qty = min;
    } else if (qty >= max) {
      qty = max;
    }
    qty = Math.floor( (qty-min)/inc )*inc  + min;

    return qty;
  }

  var getValue = function($quantitybox){
    var $qtybox = $($quantitybox),
        min = $qtybox.data('min'),
        max = $qtybox.data('max'),
        inc = $qtybox.data('step');
    
    if($quantitybox) {
      return chgQty( getInput($quantitybox).val(), parseFloat(min), parseFloat(max), parseFloat(inc) )+'';
    } else {
      return '0';
    }
  };


  $(document).on('click','[data-component="quantitybox"]',function(e){
    var $input,$this,mul,increment, newVal, oldVal, min, button;

    $this=$(this);
    $input=getInput($this);
    button = $(e.target).data("component");

    mul = 0;
    if(button === "quantitybox.dec" ) {
      mul = -1;
    } else if(button === "quantitybox.inc") {
      mul = 1;
    }

    if(mul) {
      increment = +$this.data("step")*mul;
      oldVal = +$input.val();
      min = +$this.data("min");

      newVal= oldVal+increment;
      if($this.data("mayempty") && newVal < min  && increment < 0) {
        newVal = 0;
      } else {
        newVal=Math.max(min,Math.min(+$this.data("max"),newVal));
      }

      $input.val(newVal);
      markProductChanged($this);
      triggerEvent($this,newVal);     
    }
  });

  $(document).on('keyup','[data-component="quantitybox"]',function(e){
    var $input,$this;
    $this=$(this);
    $input=getInput($this);
    markProductChanged($this);
    triggerEvent($this,$input.val());
  });

  $(document).on('change','[data-component="quantitybox.value"]',function(e){

    var $qtyinput = $(this),
        $qtybox = $qtyinput.closest('[data-component="quantitybox"]'),
        min = $qtybox.data('min'),
        max = $qtybox.data('max'),
        inc = $qtybox.data('step');
    
    $(this).val(chgQty($qtyinput.val(), parseFloat(min), parseFloat(max), parseFloat(inc)));
    triggerEvent($qtybox,$qtyinput.val());
  });

/*
* plugin for quantitybox
*
* usage: $(quantitybox selector).quantityBox("value")
* return value of the quantityboxes
*/

  var getBoxAndValue = function() {
    return { 
      quantityBox:this,
      value:getValue(this)
    };
  };
  
  var _updateSubtotal = function(index) {
    var $input,$this;

    $this=$(this);
    $input=getInput($this);
  };

  var methods = {
    value : function( ) { 
       return getValue(this[0]);
    },
    boxValue:function(){
      return this.map(getBoxAndValue);
    },
    show:function(){
    return this.map(_updateSubtotal);
    }
  };

  $.fn.quantityBox = function( method ) { 
      // Method calling logic
    if ( methods[method] ) {
      return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
    } else {
      $.error( 'Method ' +  method + ' does not exist on jQuery.quantityBox' );
    }    
  };
  
  fd.modules.common.utils.register("components", "QuantityBox", QuantityBox, fd);
}(FreshDirect));
