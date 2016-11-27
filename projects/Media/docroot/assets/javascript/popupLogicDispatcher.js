if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function () {
	PopupDispatcher = {
			maxTagCounter : 0,
			forms : {},
			alcInfo : {}
	};
	
	FreshDirect.PopupDispatcher = PopupDispatcher;
	
	var getUniqueId = (function() {
		var id = 12; // DO NOT SET THIS TO ZERO otherwise it will be FALSE if tested
		return function() {
			return id++;
		};
	})();
	
	PopupDispatcher.attachFormInner = function(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate, reuse, rebindSubmit, usq, atc, isAlcoholic, multiForm) {
		// we must have the form
		if (form && href && event) {
/*			var fid;
			for (fid in PopupDispatcher.forms) {
				if (PopupDispatcher.forms[fid] && PopupDispatcher.forms[fid].form) {
					var f = PopupDispatcher.forms[fid].form;
					if (f.id == form.id && f.ownerDocument == form.ownerDocument) {
						PopupDispatcher.destroy.destroy(PopupDispatcher.forms[fid]);
					}
				}
			}*/
//			if (reuse) {
//				// demolish previous struct including YAHOO panel
//				var fid;
//				for (fid in PopupDispatcher.forms) {
//					if (PopupDispatcher.forms[fid] && PopupDispatcher.forms[fid].form) {
//						var f = PopupDispatcher.forms[fid].form;
//						if (f.id == form.id && f.ownerDocument == form.ownerDocument) {
//							PopupDispatcher.destroy.destroy(PopupDispatcher.forms[fid]);
//						}
//					}
//				}
//			}

			if(isAlcoholic == 'true'){
				PopupDispatcher.alcInfo[form.id] = true;				
			}

			var data = {};
			data.form = form;
			data.redo = false;
			data.reload = false;
			data.rebindSubmit = rebindSubmit;
			data.oldOnsubmit = null;
			data.action = form.action;
			data.event = event;
			data.elementId = elementId;
			data.instant = instant;
			data.decorate = decorate;
			data.pendingActionField = form.atc_pending_action;
			data.usq = usq;
			data.atc = atc;
			data.multiForm = multiForm;
			data.containsAlcoholic = isAlcoholic;
			data.quantityCheck = quantityCheck;
			
			if (tagCounter > PopupDispatcher.maxTagCounter) {
				PopupDispatcher.maxTagCounter = tagCounter;
			};
			if (!data.action) {
				data.action = href;
			}
			if (data.action) {
				var id = getUniqueId();
				data.id = id;
				PopupDispatcher.forms[id] = data;

				if (data.pendingActionField) {
					data.atcPendingUri = '/api/atc_pending.jsp';
					var idx = data.action.indexOf('?');
					if (idx >= 0) {
						data.atcPendingUri += '?';
						data.atcPendingUri += data.action.substring(idx + 1);
					}
				}

				PopupDispatcher.bindSubmit(data, event);
			}
		}
	};

	PopupDispatcher.attachForm = function(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate, reuse, rebindSubmit, usq, atc, isAlcoholic, multiForm) {
		return FreshDirect.PopupDispatcher.attachFormInner(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate, reuse, rebindSubmit, usq, atc, isAlcoholic, multiForm);
	};
	
	PopupDispatcher.bindSubmit = function(data) {

		data.panel = null;
		var alcoholicForm = PopupDispatcher.alcInfo[data.form.id];
		var submitSet = false;

		if (data.usq=='true') {
			if (data.event == 'onsubmit') {
				data.onsubmit = function(){
					FreshDirect.USQLegalWarning.showPendingChoice(data);
					return false;
				};
				submitSet = true;
			} else if (data.event == 'onclick' && data.elementId != "") {
				data.elementId[data.event] = function() {
					FreshDirect.USQLegalWarning.showPendingChoice(data);
				};
				data.onsubmit = function(){
					return false;
				};
				submitSet = true;
			};
		} else if (data.atc=='true') {
			if (data.event == 'onsubmit' && !alcoholicForm) {
				data.onsubmit = function(){
					FreshDirect.ATC_Pending.onSubmitBind(data);
					return false;
				};
				submitSet = true;
			} else if (data.event == 'onclick' && data.elementId != "" && !alcoholicForm) {
				data.elementId[data.event] = function() {
					FreshDirect.ATC_Pending.onSubmitBind(data);
				};
				data.onsubmit = function(){
					return false;
				};
				submitSet = true;
			};

			if (data.rebindSubmit){
				data.oldOnsubmit = form.onsubmit;	
			}
		}

		if(submitSet){
			data.form.onsubmit = data.onsubmit;			
		}
	};
	
	PopupDispatcher.getFormId = function(query) {
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
	
	PopupDispatcher.destroy = function(data) {
		data.form = null;
		data.action = null;
		if (data.panel)
			data.panel.destroy();
		data.panel = null;
	};


})();