/*global jQuery, common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;


  var atcInfo = Object.create(fd.common.signalTarget,{
    signal:{
      value:'atcResult'
    },
    template:{
      value:common.atcInCartContent
    },
    setMessage:{
      value:function(item) {
        // the original element might be copied (like in case of transactional popup), so we have to match for multiple ids
        var element = $('[id="'+item.itemId+'"], [id="'+item.atcItemId+'"]'),
            controls, amount, product;

        if(element) {
          element.addClass('atc-info-message');
          amount = item.inCartAmount || element.find('.incart-info').data('amount');
          element.html(this.template({
            amount: amount,
            message:'Adding to cart...',
            type:'ADDING'
          }));
          element.attr('data-amount', amount);

          product = element.closest('[data-component="product"]');
          controls = product.find('[data-component="product-controls"]');

          if (controls.size() !== 0) {
            // reset qty
            controls.find('input.qty').val(+controls.find('.qtyinput').data('min') || 1).change();
            controls.find('select.salesunit').val('');
            controls.find('select.salesunit').val(controls.find('select.salesunit').find('option').first().attr('value'));
            // IE 8 helper
            controls.find('.iehelper').html(controls.find('.addtocart[data-amount]').attr('data-amount') + ' Added');
          }
        }
      }
    },
    setServerMessage:{
      value:function(atcList){
        atcList.forEach(this.setMessage,this);
      }
    },
    removeMessage:{
      value:function(){
        this.removeClass('atc-info-message');
      }
    },
    renderItem:{
      value:function(item) {
        var element = $('[id="'+item.itemId+'"], [id="'+item.atcItemId+'"]'),
            controls, product;

        if(element.size()) {
          element.addClass('atc-info-message');
          product = element.closest('[data-component="product"]');
          product.find('[data-component="ATCButton"],[data-component="customizeButton"]').addClass('incart');

          if (item.status === "SUCCESS" && product.attr('data-atcRemoveOnSuccess')) {
            product.remove();
            return;
          }

          element.html(this.template({
            amount:item.inCartAmount,
            message:item.message,
            type:item.status
          }));
          element.attr('data-amount', item.inCartAmount);

          controls = element.closest('[data-component="product"]').find('[data-component="product-controls"]');
          if (controls.size() !== 0) {
            controls.addClass('atc-info-message');
            controls.find('input.qty').val(+controls.find('.qtyinput').data('min') || 1).change();
            controls.find('select.salesunit').val('');
            controls.find('select.salesunit').val(controls.find('select.salesunit').find('option').first().attr('value'));
            setTimeout(this.removeMessage.bind(controls),1800);
          }

          setTimeout(this.removeMessage.bind(element),1800);

        }
      }
    },
    callback:{
      value:function( atcResultList ) {
        atcResultList.forEach(this.renderItem,this);
        fd.modules.header.Cart.update();
      }
    }
  });

  atcInfo.listen();

  fd.modules.common.utils.register("components", "atcInfo", atcInfo, fd);
}(FreshDirect));
