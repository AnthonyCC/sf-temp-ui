/*global jQuery, common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;

  var ecouponInfo = Object.create(fd.common.signalTarget,{
    signal:{
      value:'couponStatus'
    },
    template:{
      value:common.ecoupon.statusTextHtml
    },
    renderItem:{
      value:function(couponId, message) {
        var atcId = message.atcId,
            elements = $('[data-productdata-name="atcItemId"][value="'+atcId+'"]').parents('[data-component="product"]').find('[data-component="ecoupon-status"]');

        if (elements.size() === 0) {
          elements = $('[data-ecouponid="'+couponId+'"] [data-component="ecoupon-status"]');
        }

        // clear old ecoupon data
        $('[data-component="ecoupon-status"]').html('');

        // set new message
        elements.html(this.template({status:message.message}));
      }
    },
    callback:{
      value:function( statusTexts ) {
        Object.keys(statusTexts).forEach(function(ecouponId){
          var message = statusTexts[ecouponId];

          if (message && message.atcId) {
            this.renderItem(ecouponId,statusTexts[ecouponId]);
          }
        },this);
      }
    }
  });

  ecouponInfo.listen();

  fd.modules.common.utils.register("components", "ecouponInfo", ecouponInfo, fd);
}(FreshDirect));
