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

          if (controls.size() !== 0) {
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
        var element = $('[id="'+item.itemId+'"], [id="'+item.atcItemId+'"]'),
            controls, product, oSu, cSu, oQ, cQ;

        if(element.size()) {
          element.addClass('atc-info-message');
          product = element.closest('[data-component="product"]');
          product.find('[data-component="ATCButton"],[data-component="customizeButton"]').addClass('incart');

          if (item.status === "ERROR") {
            product.addClass("hasErrors");
          } else {
            product.removeClass("hasErrors");
          }
          
          if (item.status === "SUCCESS"){        	  
        	  // Dstillery Script Pixel
        	 //APPDEV-4287  
        	  function asyncPixelWithTimeout() {
        	  var img = new Image(1, 1);
        	  img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26208&pcv=48';
        	  setTimeout(function ()
        	  { if (!img.complete) img.src = ''; //kill the request }

        	  , 33);
        	  };
        	  asyncPixelWithTimeout();
        	              
              
        	  //Facebook Conversion Pixel
              (function() {
                var _fbq = window._fbq || (window._fbq = []);
                if (!_fbq.loaded) {
                  var fbds = document.createElement('script');
                  fbds.async = true;
                  fbds.src = '//connect.facebook.net/en_US/fbds.js';
                  var s = document.getElementsByTagName('script')[0];
                  s.parentNode.insertBefore(fbds, s);
                  _fbq.loaded = true;
                }
              })();
              window._fbq = window._fbq || [];
              window._fbq.push(['track', '6028257776486', {'value':'0.00','currency':'USD'}]);        	  
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

          controls = element.closest('[data-component="product"]').find('[data-component="product-controls"]');
          if (controls.size() !== 0) {
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
