/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;

	$(document).on('click','[data-component="ecoupon"]', function(e){
		var couponId = $(e.currentTarget).data('ecouponid');
		if(couponId && fdCouponClip) {
			fdCouponClip(couponId);
		}
	});

}(FreshDirect));
