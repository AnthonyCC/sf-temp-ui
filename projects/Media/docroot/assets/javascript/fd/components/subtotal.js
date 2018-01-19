/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  function _update($subtotal){
    var pricingInfo = $subtotal.attr('data-prices') || '[]',
        salesunitInfo = $subtotal.attr('data-suratio') || '[]',
        gpricingInfo = $subtotal.attr('data-grpprices') || '[]',
        cvprices = $subtotal.attr('data-cvprices') || '[]',
        cartdata = fd.modules.common.getCartData($subtotal),
        qInc = parseFloat($subtotal.attr('data-qinc')) || 1,
        qMin = parseFloat($subtotal.attr('data-qmin')) || 0,
        qMax = parseFloat($subtotal.attr('data-qmax')) || 10000,
        bySalesUnit = $subtotal.attr('data-bysalesunit') || false,
        price = 0,
        itemInfo = fd.modules.common.productSerialize($subtotal).pop();
  	  	if(typeof itemInfo != 'undefined'){
  	  		var salesUnit = itemInfo.salesUnit,
  	  		qty = parseFloat(itemInfo.quantity),
  	        origqty = qty;
  	  	}
        var pricingUnit = "",
        template = $subtotal.attr('data-template') || '[]';

    template = FreshDirect.modules.common.utils.discover(template);

    /* Convert JSON string to JSON object */
    try { pricingInfo = JSON.parse(pricingInfo); } catch (e) {}
    try { salesunitInfo = JSON.parse(salesunitInfo); } catch (e) {}
    try { gpricingInfo = JSON.parse(gpricingInfo); } catch (e) {}
    try { cvprices = JSON.parse(cvprices); } catch (e) {}

    /* grab the pricingUnit because we will need that - first one is just fine because all are the same */
    try { pricingUnit = pricingInfo[0].pricingUnit; } catch (e) {}

    /* we may need to convert if actual salesUnit and pricingUnit is not the same - in that case multiply with the appropriate ratio */
    try {
      if ( salesUnit !== pricingUnit ) {
        salesunitInfo.forEach(function (si) {
          if ( salesUnit === si.unit ) {
            qty = qty * si.ratio;
          }
        });
      }
    } catch (e) {
    }

    try {
      pricingInfo.forEach(function (pi) {
        if (+qty >= +pi.lowerBound && +qty < +pi.upperBound) {
          price = Math.round(100 * +qty * +pi.price) / 100;
        }
      });
    } catch (e) {
    }

    try {
      gpricingInfo.forEach(function (pi) {
        if (+qty >= +pi.lowerBound && +qty < +pi.upperBound) {
          price = Math.round(100 * +qty * +pi.price) / 100;
        }
      });
    } catch (e) {
    }

    try {
      if('configuration' in itemInfo){
        cvprices.forEach(function(cvp){
          if(itemInfo.configuration[cvp.name] === cvp.value) {
            if( cvp.applyHow === '1' ) {
              price += origqty * cvp.price;
            } else if( cvp.applyHow === '0' ) {
              price += qty * cvp.price;
            }
          }
        });
      }
    } catch (e) {
    }

    price = Math.round( price * 100 ) /100;

    if ((origqty > qMax-(cartdata.incart||0)) ||
        (origqty < qMin) ||
        (!bySalesUnit && origqty % qInc !== 0)) {
      price = 0;
    }
//APPDEV-4331
    if (price >= 0 && !isNaN(origqty)) {
      price = price.toFixed(2);
      if( template && template.call ){
        $subtotal.html( template.call( null, { 'price' : price, 'quantity' : qty, 'pricingUnit' : pricingUnit } ) );
      }
      else{ // fallback
        $subtotal.html( Subtotal.innerTemplate.call(null, { 'price' : price }) );
      }
      $subtotal.addClass('hasPrice');
      $subtotal.closest('[data-component="product-controls"]').addClass('subtotalShown');
      $subtotal.siblings('[data-component="product-controls"]').addClass('subtotalShown');
      $subtotal.closest('[data-component="product"]').removeClass('invalidQty');
    } else {
      $subtotal.removeClass('hasPrice');
      $subtotal.closest('[data-component="product-controls"]').removeClass('subtotalShown');
      $subtotal.siblings('[data-component="product-controls"]').removeClass('subtotalShown');
      $subtotal.closest('[data-component="product"]').addClass('invalidQty');
      $subtotal.html("");
    }

    return price;
  }

  var Subtotal = {
      update:function(product) {
        var $subtotal = $(product).find('[data-component="subtotal"]'),
            sendsubtotal = $(product).find('[data-sendsubtotal="true"]').length > 0,
            id = $(product).find('[data-sendPriceId]').attr('data-sendPriceId'),
            price;
        if($subtotal.length) {
          price = _update($subtotal);
        }
        if (sendsubtotal) {
          var data = {};
          data[id] = price;
          $(document).trigger("product-subtotal", data);
        }
      },
      innerTemplate:common.subtotalInner
  };

  function eventHandler(event){
    var ct = $(event.currentTarget);
    if( ct.data('component') !== 'product' ) {
      ct = ct.closest('[data-component="product"]')[0];
    }
    Subtotal.update(ct);
  }

  function pccHandler (e) {
    var $product = $('[data-productdata-name="atcItemId"][value="'+e.productId+'"]').parents('[data-product-id], [data-productid]');

    if ($product.size() < 1) {
      $product = $('[data-productid="'+e.productId+'"], [data-product-id="'+e.productId+'"]');
    }


    if ($product.size() > 0) {
      Subtotal.update($product);
    }
  }

  $(document).on('quantity-change','[data-component="product"]',eventHandler);

  $(document).on('salesunit-change','[data-component="product"]',eventHandler);

  $(document).on('change','[data-component="productDataConfiguration"]',eventHandler);

  $(document).on('transactionalPopup-open','[data-component="product"]',eventHandler);

  $(document).on('reorderPopup-open','[data-component="product"]',eventHandler);

  $(document).on('reorderPopup-refresh','[data-component="product"]',eventHandler);

  $(document).on('soCustomizePopup', function(){Subtotal.update($jq("#customizePopup form[data-component='product']"))});

  $(document).on('productConfigurationChange', pccHandler);

  fd.modules.common.utils.register("components", "Subtotal", Subtotal, fd);
}(FreshDirect));
