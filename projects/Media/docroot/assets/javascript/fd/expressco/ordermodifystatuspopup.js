/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var POPUPWIDGET = fd.modules.common.popupWidget;

	var ordermodifystatus = Object.create(POPUPWIDGET, {
		eventListenersAdded: { value: false, writable: true },
		polling: { value: false, writable: true },
		pollingOrderId: { value: null, writable: true },
		pollingHandle: { value: null, writable: true },
		pollingCount: { value: 0, writable: true },
		POLLING_LIMIT: { value: 10 },
		POLLING_INTERVAL: { value: 3000 },
		POLLING_STARTDELAY: { value: 0 },
		$callbackElem: { value: null, writable: true },
		template: { /* required */
			value: common.fixedPopup
		},
		bodyTemplate: { /* required */
			value: common.fixedPopupBody
		},
		popupId: {
			value: 'orderModifyStatus'
		},
		$trigger: {
			value: null
		},
		hasClose: {
			value: true
		},
		popupConfig: { /* required */
			value: {
				zIndex: 2000,
				overlayExtraClass: 'centerpopupoverlay', /* use to fix ghost css */
				align: false,
				hidecallback: function (e) {
					/* do nothing extra on overlay click */
				}
			}
		},
		init: { /* always init first */
			value: function(orderId) {
				this.pollingOrderId = orderId;

				if (!this.eventListenersAdded) {
					//event listeners
					$(document).on('click', '#'+this.popupId+' .close', this.close.bind(this));
					$(document).on('click', '#'+this.popupId+' [data-click-navigate]', (function(e) {
						window.top.location = $(e.currentTarget).attr('data-click-navigate');
						this.close();
					}).bind(this));
				}
			}
		},
		open: {
			value: function ($callBackElem) {
				if (this.pollingOrderId === null || this.pollingOrderId === undefined || this.pollingOrderId === '') { return; } /* required */

				this.$callbackElem = $callBackElem || null;

				this.startPolling();

				this.popup.show($(document.body));
				this.popup.clicked = true;
			}
		},
		close: {
			value: function() {
				this.endPolling();
				
				if (this.popup) { this.popup.hide(); }
			}
		},
		pollingFunc: {
			value: function() {
				var that = this;
				if (that === window) { that = FreshDirect.components.ordermodifystatus; }

				if (that.pollingOrderId === null || that.pollingOrderId === undefined || that.pollingOrderId === '' || that === window) { return; }

				if (that.pollingCount < that.POLLING_LIMIT) {
					$.get('/api/orderinfo', { 'orderId': that.pollingOrderId }, that.pollingFuncSuccess.bind(that)).fail(that.pollingFuncError.bind(that));
				} else {
					that.pollingFuncError();
				}
			}
		},
		pollingFuncSuccess: {
			value: function(data) {
				if (fd.utils.hasOwnNestedProperty('order.canModify', data) && data.order.canModify) {
					/* remove blocking attribute */
					$('[data-gtm-click-error]').attr('data-gtm-click-error', null);
					if (this.$callbackElem) {
						/* fire new click */
						this.$callbackElem.trigger('click');
					}
					
					this.endPolling();
				} else if (this.pollingCount < this.POLLING_LIMIT) {
					/* start again */
					this.pollingHandle = setTimeout(this.pollingFunc, this.POLLING_INTERVAL);
					this.pollingCount++;
				}
			}
		},
		pollingFuncError: {
			value: function(data) {
				this.refreshBody({ body: '<div class="warning"><img src="/media_stat/images/common/warning_icon.svg" alt="An error occurred" /></div><div>Sorry, something went wrong.<br />We cannot bring up your order at this time.</div><div><button class="cssbutton green transparent view-receipt-btn" data-click-navigate="/your_account/order_details.jsp?orderId='+this.pollingOrderId+'">View Receipt</button></div>' });
				this.endPolling();
			}
		},
		startPolling: { /* resets polling status */
			value: function () {
				var that = this;
				if (that === window) { that = FreshDirect.components.ordermodifystatus; }
				
				if (that.pollingOrderId === null || that.pollingOrderId === undefined || that.pollingOrderId === '' || that === window) { return; }
				
				that.refreshBody({ body: '<div class="spinner"></div><div>Bringing up your order! Please wait...</div><div><button class="cssbutton green transparent close">Cancel</button></div>' });

				that.pollingCount = 0;

				if (!that.polling) {
					setTimeout((that.pollingFunc).bind(that), that.POLLING_STARTDELAY);
				}

				that.polling = true;
			}
		},
		endPolling: {
			value: function (){ /* don't clear orderId so close/open can re-call */
				clearTimeout(this.pollingHandle);
				this.polling = false;
				this.pollingCount = 0;
				this.$callbackElem = null;
			}
		}
	});

	//register
	fd.modules.common.utils.register("components", "ordermodifystatus", ordermodifystatus, fd);
}(FreshDirect));