(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	
	$(document).ready(function() {
		/* setup data */
		fd.inform.ordermodify = $.extend(fd.inform.ordermodify, { test: {
			eStoreId: $('#eStoreId').text(),
			fdCustid: $('#fdCustid').text(),
			viewCount: $('#viewCount').text(),
			PAYLOADS: { /* "private" payloads */
				SEEN_TRUE: $.extend({}, fd.components.inform.ordermodify.PAYLOADS.SEEN, { "value": true }),
				SEEN_FALSE: $.extend({}, fd.components.inform.ordermodify.PAYLOADS.SEEN, { "value": false })
			},
			init: function() {
				/* proxy ajax methods */
				var handlerGetSuccessOrig = fd.components.inform.ordermodify.handlerGetSuccess;
				var handlerGetErrorOrig = fd.components.inform.ordermodify.handlerGetError;
				fd.components.inform.ordermodify.handlerGetSuccess = function(resp) {
					afterAjax('GET', resp);
					handlerGetSuccessOrig.apply(fd.components.inform.ordermodify, arguments);
				};
				fd.components.inform.ordermodify.handlerGetError = function(resp) {
					afterAjax('GET', resp, true);
					handlerGetErrorOrig.apply(fd.components.inform.ordermodify, arguments);
				};
				var handlerPostSuccessOrig = fd.components.inform.ordermodify.handlerPostSuccess;
				var handlerPostErrorOrig = fd.components.inform.ordermodify.handlerPostError;
				fd.components.inform.ordermodify.handlerPostSuccess = function(resp) {
					afterAjax('POST', resp);
					handlerPostSuccessOrig.apply(fd.components.inform.ordermodify, arguments);
				};
				fd.components.inform.ordermodify.handlerPostError = function(resp) {
					afterAjax('POST', resp, true);
					handlerPostErrorOrig.apply(fd.components.inform.ordermodify, arguments);
				};
				
				/* SQL helpers */
				function getSqlSrcHtml() {
					return (fd.inform.ordermodify.test.fdCustid !== 'GUEST')
					? `--INFORM_ORDERMODIFY should equal ${fd.inform.ordermodify.test.viewCount}\nselect FDCUSTOMER_ID, E_STORE, INFORM_ORDERMODIFY from CUST.FDCUSTOMER_ESTORE fde where FDE.E_STORE = '${fd.inform.ordermodify.test.eStoreId}' and FDE.FDCUSTOMER_ID = '${fd.inform.ordermodify.test.fdCustid}';` 
							: ``;
				}
				
				function refreshSqlSrcHtml() {
					$('#sqlSrc').html(getSqlSrcHtml());
				}
				
				$('#sqlRefresh').on('click', function(e) {
					refreshSqlSrcHtml();
				});

				/* API helpers */
				function getApiPayloadObj(template) {
					/* PAYLOAD TEMPLATES */
					if (fd.components.inform.ordermodify.PAYLOADS.hasOwnProperty(template)) {
						return fd.components.inform.ordermodify.PAYLOADS[template];
					}
					if (fd.inform.ordermodify.test.PAYLOADS.hasOwnProperty(template)) {
						return fd.inform.ordermodify.test.PAYLOADS[template];
					}
				}
				
				function refreshApiPayloadHtml(template) {
					$('#apiPayload').html(JSON.stringify(getApiPayloadObj(template), null, '\t'));
				}
				
				$('.apiPayloadTemplate').on('click', function(e) {
					refreshApiPayloadHtml($(this).attr('data-template'));

					//ctrl click
					if (e.ctrlKey) {
						if ($(this).attr('data-template-method') === 'POST') { $('#apiPayloadPost').click(); }
						if ($(this).attr('data-template-method') === 'GET') { $('#apiPayloadGet').click(); }
					}
				});
				//GET
				$('#apiPayloadGet').on('click', function(e) {
					var sendData = JSON.parse($('#apiPayload').text());
					$(this).addClass('loading');
					$.get($('#apiPath').text(), sendData, function(data) {
						afterAjax('GET', data);
					}).fail(function(data) {
						afterAjax('GET', data, true);
					});
				});
				//POST
				$('#apiPayloadPost').on('click', function(e) {
					var sendData = JSON.parse($('#apiPayload').text());
					$(this).addClass('loading');
					$.post($('#apiPath').text(), sendData, function(data) {
						afterAjax('POST', data);
					}).fail(function(data) {
						afterAjax('POST', data, true);
					});
				});
				
				/* helper for any ajax response */
				function afterAjax(type, data, error) {
					//remove loaders
					$('.loading').removeClass('loading');

					//update display
					$('#apiPostResult').html(JSON.stringify(data, null, '\t'));

					if (!error) {
						//update data
						fd.inform.ordermodify.test = $.extend(fd.inform.ordermodify.test, data);
						
						//update display
						if (data.hasOwnProperty('viewCount')) {
							$('#viewCount').text(data.viewCount).addClass('updatetransition'); //update to current
						}
						if (data.hasOwnProperty('viewCountLimit')) {
							match($('#viewCountLimit'), data.viewCountLimit+'');
						}
						if (data.hasOwnProperty('media')) {
							$('#mediaSrc').html(data.media).addClass('updatetransition');
						}
						if (data.hasOwnProperty('show')) {
							$('#show').html(data.show+'').addClass('updatetransition');
						}
						refreshSqlSrcHtml();
					}
					
					if (error) {
						$('#apiPostResult').addClass('nomatchtransition');
					}
				}

				/* TRANSITION helpers */
				$(document).on('transitionend', '.matchtransition,.nomatchtransition,.updatetransition', function(e) { $(this).removeClass('matchtransition nomatchtransition updatetransition'); });
				
				function match($elem, matchVal) {
					if ($elem.text() === matchVal) {
						$elem.addClass('matchtransition');
					} else {
						$elem.addClass('nomatchtransition');
					}
				}

				/* USER ACTION BUTTONS */
				$('.killSessionBtn').on('click', function(e) {
					$(this).addClass('loading');
					$.get('/test/session/killme.jsp', function() { window.location.reload(); });
				});
				$('.signOutBtn').on('click', function(e) {
					$(this).addClass('loading');
					$.get('/test/session/killme.jsp?logout', function() { window.location.reload(); });
				});
				$('#apiForce').on('click', function(e) {
					fd.components.inform.ordermodify.fire();
				});
				$('#startModify').on('click', function(e) {
					var selectedId = $('#modifiableOrders').val();
					if (selectedId !== '-1') {
						$(this).addClass('loading');
						$.get('/your_account/modify_order.jsp?orderId='+selectedId+'&action=modify&noSuccess=true', function() { window.location.reload(); });
					}
				});					
				
				/* PAGE INIT */
				refreshSqlSrcHtml();
				refreshApiPayloadHtml('DEFAULT');
			} }
		});
	});
})(FreshDirect);