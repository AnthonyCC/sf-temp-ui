/* global common jquery */

var FreshDirect = FreshDirect || {};
var checkout;

(function (fd) {
	'use strict';
	var $ = fd.libs.$;
	fd.properties = fd.properties || {};
	var POPUPWIDGET = fd.modules.common.popupWidget;

	var API_URL = '/api/common/debitswitchnotice', /* API URL to call for loading/saving */
	COOKIENAME = 'FreshDirect.debitSwitchNotice'; /* used for cookie check (show once per user) */
	
	var debitswitchnotice = Object.create(POPUPWIDGET,{
		container: {
			value: null,
			writable: true
		},
		headerContent: {
			value: ''
		},
		customClass: {
			value: 'debitswitchnotice'
		},
		hideHelp: {
			value: true
		},
		hasClose: {
			value: true
		},
		$trigger: {
			value: null // TODO
		},
		trigger: {
			value: '' // TODO
		},
		bodySelector:{
			value: '.c-popup-content'
		},
		signal: {
			value: ''
		},
		scrollCheck: {
			value: '.c-popup'
		},
		template: {
			value: common.centerpopup
		},
		bodyTemplate: {
			value: common.debitswitchnotice /* /common/debitSwitchNoticePopup.soy */
		},
		popupId: {
			value: 'debitswitchnotice'
		},
		popupConfig: {
			value: {
				zIndex: 2000,
				openonclick: true,
				overlayExtraClass: 'centerpopupoverlay',
				align: false,
				hidecallback: function (e) {
					/* do nothing extra on overlay click */
				}
			}
		},
		init: {
			value: function () {
				if (!$('#'+this.popupId).length) {
					$.ajax({
						url: '/includes/debitSwitchNotice.jsp',
						context: this
					}).done(function(data) {
						this.initiated = true;
						if (data.replace(/[\n\r\s\t]*/g, '') !== '') { /* no msg === no open */
							this.open(this, {body: data});
						}
					});
				}
			}
		},
		initiated: { /* prevent infinite loop */
			value: false,
			writable:true
		},
		open: {
			value: function (e, data) {
				if (!this.initiated) {
					this.init();
				} else {
					var $t = e && $(e.currentTarget) || $(document.body);
					
					if (data && data.hasOwnProperty('body')) {
						this.refreshBody(data, this.bodyTemplate);
					}

					this.popup.show($t);
					this.popup.clicked = true;

					this.noscroll(true);
				}
			}
		},
		close: {
			value: function (e) {
				var $t = e && $(e.currentTarget) || $(document.body);

				this.popup.hide($t);
				this.popup.clicked = false;
			}
		}
	});
	$(document).on('click', '#'+debitswitchnotice.popupId+' .close', debitswitchnotice.close.bind(debitswitchnotice));
	
	debitswitchnotice.listen();
	fd.modules.common.utils.register('common', 'debitswitchnotice', debitswitchnotice, fd);

	//auto-open enabled and on checkout
	if (fd.properties.isDebitSwitchNoticeEnabled && window.location.pathname.indexOf('/expressco/checkout.jsp') !== -1) {

		//check cookie
		var cookieValue = fd.modules.common.utils.readCookie(COOKIENAME);
		var sessionId = fd.modules.common.utils.getJSessionId();
		if (cookieValue !== "seen@" + sessionId) {
			//show
			debitswitchnotice.open();
			
			//mark as seen
			fd.modules.common.utils.createCookie(COOKIENAME,'seen@'+sessionId);
		}
	}

}(FreshDirect));