/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      orderId,
      reorderPopup = Object.create(fd.modules.common.overlayWidget, {
        bodyTemplate: {
          value: function(data) {
            var isData = data && data.data && data.data.orderId || data.data.error ? true : false;
            return isData ? common.reorderPopup({data: data.data, error: data.data.error, errorMessage : data.data.errorMessage}) : '';
          }
        },
        headerTemplate: {
          value: function () {
            return '';
          }
        },
        trigger: {
          value: '[data-reorder-popup]'
        },
        closeTrigger: {
          value: '[data-close-reorder]'
        },
        overlayId: {
          value: 'reorderPopup'
        },
        overlayConfig: {
          value: {
            align:false,
            overlay:true,
            overlayExtraClass:'white-popup-overlay',
            hideOnOverlayClick: true,
            zIndex:460
          }
        },
        customClass: {
          value: 'overlay-large'
        },
        openPopup: {
          value: function (_orderId,error) {
            var $t = $(document.body);
            if (_orderId){
              orderId = _orderId;
            }

            if (!error) {
              DISPATCHER.signal('server',{
                url: '/api/orderinfo?orderId=' + orderId + '&productData=true',
                type: 'GET',
                dataType: 'json'
              });
            } else {
              this.refresh(error);
            }


          }
        },
        signal: {
          value: 'order'
        },
        render: {
          value: function(data) {
            fd.modules.common.overlayWidget.render.call(this, data);

            $('#'+this.overlayId+' [data-component="product"]').trigger('reorderPopup-open');
          }
        }
      }),
      closeReorderPopup = Object.create(fd.common.signalTarget, {
        signal: {
          value: 'atcResult'
        },
        callback: {
          value: function(data) {
            var hasError= false;

            data.forEach(function(product) {
              if (product.status && product.status != "SUCCESS") {
                hasError = true;
              }
            });
            if (hasError) {
              reorderPopup.refresh({data: '', error: true, errorMessage: 'Something went wrong! We did not add all of the products.'});
            } else {
              reorderPopup.close();
            }
          }
        }
      });

  reorderPopup.listen();
  closeReorderPopup.listen();

  $(document).on('product-subtotal', function (e,data) {
    var id= Object.keys(data)[0],
        element = $(e.target).find('#' + id + '.cartline-price-value.current');

    element.text('$' + data[id]);
    element.attr('data-estimated-price', data[id]);

    var elements = document.querySelectorAll('#reorderPopup #cartcontent .cartline-price-value.current'),
        subtotal = $(e.target).find('#orderId_' + orderId + '.estimated_subtoal'),
        sum = 0;

    elements.forEach( function(e) {
      var price = +$(e).attr('data-estimated-price');
      sum += price;
    });

    sum = sum.toFixed(2);
    subtotal.text('Estimated Subtotal: $' + sum);
    subtotal.attr('data-estimated-price', sum);
  });

  $(document).on('click', '[data-remove-product]', function(e) {
    e.stopPropagation();
    e.preventDefault();
    var cartlineId = $(e.currentTarget).attr('data-remove-product'),
        product = $(document).find('#reorderPopup [data-cartlineid="' + cartlineId + '"]'),
        quantity = $(product).find('[data-component="quantitybox.value"][data-productdata-name="quantity"]'),
        customValue = $(product).find('[data-component="salesunit"][data-productdata-name="salesUnit"]'),
        subtotal = $(product).find('[data-component="subtotal"]');

    if (quantity.length > 0) {
      quantity.val(0);
    } else {
      var option = document.createElement("option"),
          suratio = subtotal.data('suratio');

      suratio.push({unit: '', ratio: '', salesUnit: ''})
      option.text = '';
      option.value = '';
      option.selected = 'selected';
      customValue[0].appendChild(option);
      subtotal.attr('data-suratio', JSON.stringify(suratio));
    }

    $('#'+reorderPopup.overlayId+' [data-component="product"]').trigger('reorderPopup-refresh');
  });

  fd.modules.common.utils.register("components", "reorderPopup", reorderPopup, fd);
}(FreshDirect));
