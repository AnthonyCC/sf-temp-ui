/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;

	$(document).on('click','[data-component="ecoupon"] .fdCoupon_cb', function(e){
		var couponId = $(e.currentTarget).closest('[data-component="ecoupon"]').data('ecouponid');
		if(couponId && fdCouponClip) {
			fdCouponClip(couponId);
		}
	});

}(FreshDirect));
