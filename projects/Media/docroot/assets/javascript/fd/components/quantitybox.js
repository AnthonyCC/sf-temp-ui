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

  $(document).on('quantity-change', '[data-component="product"]', function (e, data) {
    var $t = $(e.currentTarget),
        id = $t.find('[data-productdata-name="atcItemId"]').val(),
        qty = data && data.newVal;

    if (id && (qty || qty === 0)) {
      fd.modules.common.productConfigurationChange(id, {quantity: qty});
    }
  });

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

  function chgQty(value, min, max, inc, incart) {
    var qty = parseFloat(value) + 0;

    incart = incart || 0;

    if (isNaN(qty) || qty < min) {
      qty = min;
    } else if (qty >= max-incart) {
      qty = Math.max(max-incart, min);
    }
    qty = Math.floor( (qty-min)/inc )*inc  + min;

    return qty;
  }

  var getValue = function($quantitybox){
    var $qtybox = $($quantitybox),
        cartdata = fd.modules.common.getCartData($qtybox);
    
    if($quantitybox) {
      return chgQty( getInput($quantitybox).val(), parseFloat(cartdata.min), parseFloat(cartdata.max), parseFloat(cartdata.step), cartdata.incart )+'';
    } else {
      return '0';
    }
  };


  $(document).on('click','[data-component="quantitybox"]',function(e){
    var $input,$this,mul,increment, newVal, oldVal, min, button, cartdata;

    $this=$(this);
    cartdata = fd.modules.common.getCartData($this);
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
        newVal=Math.max(min,Math.min(+$this.data("max")-(cartdata.incart||0),newVal));
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
        cartdata = fd.modules.common.getCartData($qtybox);
    
    $(this).val(chgQty($qtyinput.val(), parseFloat(cartdata.min), parseFloat(cartdata.max), parseFloat(cartdata.step), cartdata.incart));
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
