if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function () {
	var ATC_Pending = {};
	var $=jQuery;
	
	FreshDirect.ATC_Pending = ATC_Pending;
	
	var rootWindow = window;
	while (rootWindow.parent && rootWindow.parent != rootWindow)
		rootWindow = rootWindow.parent;

	ATC_Pending.rootWindow = rootWindow;

	var normalSubmit = function(data) {
		if (data.rebindSubmit && data.oldOnsubmit) {
			data.form.onsubmit = data.oldOnsubmit;
			if (data.form.onsubmit())
				
				data.form.submit();
		} else {
			data.form.onsubmit = null;
			data.form.submit();
		}
	};
	
	ATC_Pending.onSubmitBind = function(data) {
		var form = data.form;
		if (data.multiForm=='true') {
		      var submitButton = document.createElement('input');
		      submitButton.setAttribute('type', 'hidden');
		      submitButton.setAttribute('id', data.elementId.id + '.x');
		      submitButton.setAttribute('name', data.elementId.id + '.x');
		      form.appendChild(submitButton);
		}
		YAHOO.util.Connect.setForm(form);
		var ajax = YAHOO.util.Connect.asyncRequest('POST', data.atcPendingUri, {
			success: function(o) {
					if (o.responseText.length > 0
							&& o.responseText.charAt(0) == '1') {
						ATC_Pending.showPendingChoice(data);
					} else {
						if (data.redo) {
							showError(data, "You have no more pending order.");
						} else {
							if (data.instant && data.instant != null && data.instant != '') {
								eval(data.instant)();
								return false;
							} else {
								normalSubmit(data);
							}
						}
					}
				},
			failure: function(o) {
					if (data.redo)
						showError(data, "You have no more pending order.");
					else
						normalSubmit(data);
				},
			argument: []
		});
		
	};
	
	ATC_Pending.showPendingChoice = function(data) {
		var id = data.id;
		// construct the form
		var uri = document.location.protocol + '//' + document.location.host + '/overlays/merge_cart_penOrder_choice.jsp?formId=' + id;
		if (data.instant != "") {
			uri += '&instant=' + data.instant;
		} 

		if (!data.panel) {
			data.panel = new YAHOO.widget.Panel('modify_pending_choice_' + id, {
				underlay: "matte", 
				visible: true,
				modal: true,
				zIndex: 250,
				draggable: false}
			);
			
			data.panel.setHeader( "&nbsp;" );
			
			data.panel.hideEvent.subscribe(function() {
				$(document.getElementById('modify_pending_choice_'+id+'_c')).removeClass('step2');
				if (data.reload) {
					FreshDirect.ATC_Pending.rootWindow.location.reload();
					return;
				}
				// during hide we delete iframe to avoid nasty flicker
				data.panel.setBody('');
			});

			// we always stick the modal overlay to the root window
			var body = rootWindow.document.body;
			data.panel.render(body);

			YAHOO.util.Dom.addClass('modify_pending_choice_' + id + '_c', 'pending_choice_c');
			YAHOO.util.Dom.addClass('modify_pending_choice_' + id, 'pending_choice');
		}

		var content = "";
		content += '<div id="modify_pending_choice_content_' + id + '" class="modify_pending_choice_content">\n';
		content += '<div id="modify_pending_choice_progress_' + id + '" class="modify_pending_choice_progress">\n';
		content += 'Please wait... <img src="/media_stat/images/navigation/spinner.gif" alt="spinner" width="50" height="50">\n';
		content += '</div>\n';
		content += '  <iframe id="modify_pending_choice_frame_' + id + '" class="modify_pending_choice_frame" frameborder="0" src="' + uri + '"></iframe>';
		content += '</div>\n';
		
		data.panel.setBody(content);

		data.panel.show();		
	};		

/*	var getFormId = function(query) {
		if (query && query.length > 0 && query.charAt(0) == '?') {
			query = query.substring(1); // remove question mark
			var params = query.split('&');
			for (var i = 0; i < params.length; i++) {
				var param = params[i].split('=');
				if (param[0] == 'formId')
					return parseInt(unescape(param[1]), 10);
			}
		}
		return null;
	};
*/	
	ATC_Pending.refreshPanelInner = function(query) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id] && FreshDirect.PopupDispatcher.forms[id].panel) {
			var progress = rootWindow.document.getElementById('modify_pending_choice_progress_' + id);
			if (progress)
				progress.style.display = 'none';
			FreshDirect.PopupDispatcher.forms[id].panel.center();
		}
	};

	ATC_Pending.refreshPanel = function(query) {
		return rootWindow.FreshDirect.ATC_Pending.refreshPanelInner(query);
	};
	
	ATC_Pending.doNormalSubmitInner = function(query) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id]) {
			if (FreshDirect.PopupDispatcher.forms[id].panel)
				FreshDirect.PopupDispatcher.forms[id].panel.hide();
			normalSubmit(FreshDirect.PopupDispatcher.forms[id]);
		}
	};
	
	ATC_Pending.doNormalSubmit = function(query) {
		return rootWindow.FreshDirect.ATC_Pending.doNormalSubmitInner(query);
	};	

	ATC_Pending.hidePanelByIdInner = function(id) {
		if (id && FreshDirect.PopupDispatcher.forms[id] && FreshDirect.PopupDispatcher.forms[id].panel)
			FreshDirect.PopupDispatcher.forms[id].panel.hide();
	};

	ATC_Pending.hidePanelById = function(id) {
		return rootWindow.FreshDirect.ATC_Pending.hidePanelByIdInner(id);
	};
	
	var renderCloseButton = function(action) {
		var content = '';
		content += '<a class="butCont butRed" style="margin: 0px auto;" href="#" onclick="' + action + '"; return false;">\n';
		content += '	<span class="butLeft"><!-- --></span><span class="butMiddle butText">OK</span><span class="butRight"><!-- --></span>\n';
		content += '</a>\n';
		return content;
	};

	var showError = function(data, message, action) {
		var panel = data.panel;
		panel.hide();
		if (action) {
			if (action == 'reload') {
				action = 'FreshDirect.ATC_Pending.rootWindow.location.reload()';
				data.reload = true;
			}
		} else {
			action = 'FreshDirect.ATC_Pending.hidePanelById(' + data.id + ')';
		} 
		var content = '';
		content += '<div class="error-content">\n';
		content += '<div class="error_title">\n';
		content += message + '\n';
		content += '</div>\n';
		content += renderCloseButton(action);
		content += '</div>\n';

		panel.setBody(content);
		
		panel.center();
		
		panel.show();		
	};

	ATC_Pending.redoPendingSubmitInner = function(id) {
		if (id && FreshDirect.PopupDispatcher.forms[id]) {
			if (FreshDirect.PopupDispatcher.forms[id].panel)
				FreshDirect.PopupDispatcher.forms[id].panel.hide();
			// we do not restore the onsubmit hook
			FreshDirect.PopupDispatcher.forms[id].redo = true;
			FreshDirect.PopupDispatcher.forms[id].onsubmit();
		}
	};
	
	ATC_Pending.redoPendingSubmit = function(id) {
		return rootWindow.FreshDirect.ATC_Pending.redoPendingSubmitInner(id);
	};

	var showCutoffPassed = function(data, pendingOrderId) {
		var message = 'Order #' + pendingOrderId + " is no longer modifiable. Please choose another order!";
		var action = 'FreshDirect.ATC_Pending.redoPendingSubmit(' + data.id + ')';
		showError(data, message, action);
	};

	
	ATC_Pending.doPendingOrderModifyInner = function(query, pendingForm) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (!id || !FreshDirect.PopupDispatcher.forms[id] || FreshDirect.PopupDispatcher.forms[id].panel) {
			// error
		}

		var data = FreshDirect.PopupDispatcher.forms[id];
		var pendingOrderInput = pendingForm.pendingOrderId;
		if (!pendingOrderInput) {
			showError(data, "No order ID was specified");
		} else {
			var cartItems = '';
			var i;
			for (i = 0; i < pendingForm.elements.length; i++) {
				var elem = pendingForm.elements[i];
				if (elem.tagName == 'INPUT' && elem.type == 'checkbox') {
					if (elem.checked) {
						if (cartItems.length == 0) {
							cartItems += elem.name + '=1';
						} else {
							cartItems += '&' + elem.name + '=1';
						}
					}
				}
			}

			var pendingOrderId = pendingOrderInput.value;
			var uri = '/api/merge_pend_order_request.jsp?pendingOrderId=' + pendingOrderId;
			var ajax = YAHOO.util.Connect.asyncRequest('POST', uri, {
				success: function(o) {
						if (o.responseText.length > 0) {
							var status = parseInt(o.responseText.charAt(0), 10);
							switch (status) {
							case 0: // ok
//								if (data.instant != "") {
	//								data.namespace.updateStatus('Order modified');
		//						} else {
									rootWindow.location.assign('/your_account/modify_order.jsp?action=modify&mergePending=1&orderId=' + pendingOrderId);
			//					}
								break;
							case 1: // not logged in / session timeout
								//data.panel.hide();
								rootWindow.location.assign('/login/login.jsp?successPage=' + document.location.pathname + escape(document.location.search));
//								showError(data, "Session timed out. Reload page!", "reload");
								break;
							case 2: // invalid pending order id / no pending order id
								showError(data, "Illegal order ID was specified. Reload page!", "reload");
								break;
							case 3: // no such pending order / cut-off passed
								showCutoffPassed(data, pendingOrderId);
								break;
							case 4: // unknown error
								showError(data, "Error processing your request. Unknown error.", "reload");
								break;
							default:
								break;
							}
						} else {
							showError(data, "Error processing your request. Server may be down.", "reload");
						}
					},
				failure: function(o) {
						showError(data, "Error processing your request. Server may be down.", "reload");
					},
				argument: []
			}, cartItems);
		}
	};

	ATC_Pending.doPendingOrderModify = function(query, pendingForm) {
		return rootWindow.FreshDirect.ATC_Pending.doPendingOrderModifyInner(query, pendingForm);
	};

	// this section is triggered on the checkout receipt page
	// it is used to check the order status on AJAX way
	// once the order becomes modifiable the corresponding DIV element
	// is displayed
	var orderStatusTimeout = null;
	
	ATC_Pending.doCheckOrderStatus = function(orderId, divId) {
		var uri = '/api/check_order_status.jsp?orderId=' + orderId;
		var ajax = YAHOO.util.Connect.asyncRequest('POST', uri, {
			success: function(o) {
					if (o.responseText.length > 0) {
						if("Modifiable" == o.responseText) {
							clearTimeout(orderStatusTimeout);
							var node = document.getElementById(divId);
							if(node){
								node.style.display="block";
							}
						}
						
					} else {
						//timer start
						orderStatusTimeout = setTimeout("FreshDirect.ATC_Pending.doCheckOrderStatus('" + orderId + "', '" + divId + "')", 2000);
					}
				},
			failure: function(o) {
					// we simply ingore the error
				},
			argument: []
		});
		
	};

	ATC_Pending.checkOrderStatus = function(orderId, divId) {
		if (orderStatusTimeout == null) {
			ATC_Pending.doCheckOrderStatus(orderId, divId);
		}
	};
	
	ATC_Pending.blockSubmitInner = function(query) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id]) {
			if (FreshDirect.PopupDispatcher.forms[id].panel){
				FreshDirect.PopupDispatcher.forms[id].panel.hide();
				//FreshDirect.PopupDispatcher.forms[id].panel.destroy();
			}
		}
	};
	
	ATC_Pending.blockSubmit = function(query) {
		return rootWindow.FreshDirect.ATC_Pending.blockSubmitInner(query);
	};	

	ATC_Pending.step2 = function(query){
		var id = FreshDirect.PopupDispatcher.getFormId(query);
	}
	
	ATC_Pending.setSize = function(query,w,h){
		var id = FreshDirect.PopupDispatcher.getFormId(query),
				dialog = $(document.getElementById('modify_pending_choice_'+id+'_c'));
		
		dialog.css({
			width:w+'px',
			height:h+'px'
		})
	};
	
	
})();
