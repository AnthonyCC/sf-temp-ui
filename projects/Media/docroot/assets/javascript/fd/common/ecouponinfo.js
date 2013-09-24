/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

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
			value:function(couponId,message) {
				var elements = $('[data-ecouponid="'+couponId+'"] [data-component="ecoupon-status"]');
				elements.html(this.template({status:message}));
			}
		},
		callback:{
			value:function( statusTexts ) {
				Object.keys(statusTexts).forEach(function(ecouponId){
					this.renderItem(ecouponId,statusTexts[ecouponId]);
				},this)
			}
		}
	});

	ecouponInfo.listen();

	fd.modules.common.utils.register("components", "ecouponInfo", ecouponInfo, fd);
}(FreshDirect));
