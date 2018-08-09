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
        var element = $('[id="'+item.itemId+'"], [id="'+item.atcItemId+'"], [id="trnp_'+item.itemId+'"], [id="trnp_'+item.atcItemId+'"]'),
            controls, amount, product, oSu, cSu, oQ, cQ;

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

          if (controls.length !== 0) {
            // reset qty
            oQ = product.find('[data-quantity-original]').attr('data-quantity-original');
            oSu = product.find('[data-salesunit-original]').attr('data-salesunit-original');
            cQ = controls.find('input.qty').val();
            cSu = controls.find('select.salesunit').val();

            if (!oQ || oQ !== cQ) {
              controls.find('input.qty').val(+oQ || +controls.find('.qtyinput').data('min') || 1).change();
            }
            if (!oSu || oSu !== cSu) {
              controls.find('select.salesunit').val('');
              controls.find('select.salesunit').val(oSu || controls.find('select.salesunit').find('option').first().attr('value'));
            }
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
        var element = $('[id="'+item.itemId+'"], [id="'+item.atcItemId+'"], [id="trnp_'+item.itemId+'"], [id="trnp_'+item.atcItemId+'"]'),
            controls, product, oSu, cSu, oQ, cQ;

        if(element.length) {
          element.addClass('atc-info-message');
          product = element.closest('[data-component="product"]');
          product.find('[data-component="ATCButton"],[data-component="customizeButton"]').addClass('incart');

          if (item.status === "ERROR") {
            product.addClass("hasErrors");
          } else {
            product.removeClass("hasErrors");
          }
          
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
          if($('.mm-page').length){
        	  product.find('button.addtocart').html(element.attr('data-amount')).addClass("ATCHasItemsMobile");
    	  }

          controls = element.closest('[data-component="product"]').find('[data-component="product-controls"]');
          if (controls.length !== 0) {
            controls.addClass('atc-info-message');

            oQ = product.find('[data-quantity-original]').attr('data-quantity-original');
            oSu = product.find('[data-salesunit-original]').attr('data-salesunit-original');
            cQ = controls.find('input.qty').val();
            cSu = controls.find('select.salesunit').val();

            if (!oQ || oQ !== cQ) {
              controls.find('input.qty').val(+oQ || +controls.find('.qtyinput').data('min') || 1).change();
            }
            if (!oSu || oSu !== cSu) {
              controls.find('select.salesunit').val('');
              controls.find('select.salesunit').val(oSu || controls.find('select.salesunit').find('option').first().attr('value'));
            }

            setTimeout(this.removeMessage.bind(controls),1800);
          }

          setTimeout(this.removeMessage.bind(element),1800);
          if(FreshDirect.standingorder && FreshDirect.standingorder.isSoCartOverlayFirstTime && !element.parent().hasClass("pdp-atc-button-wrapper")){
        	  doOverlayDialogNew("/quickshop/includes/accidental_add_to_cart.jsp");
          }
        }
      }
    },
    callback:{
      value:function( atcResultList ) {
        atcResultList.forEach(this.renderItem,this);
        $('[data-component="ATCButton"]').removeClass('ATCinProgress');
        fd.modules.header.Cart.update();
      }
    }
  });

  atcInfo.listen();

  fd.modules.common.utils.register("components", "atcInfo", atcInfo, fd);
}(FreshDirect));
