if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function () {
	var USQLegalWarning = {};
	
	FreshDirect.USQLegalWarning = USQLegalWarning;
	
	var forms = {};
	var maxTagCounter = 0;
	
	var getUniqueId = (function() {
		var id = 12; // DO NOT SET THIS TO ZERO otherwise it will be FALSE if tested
		return function() {
			return id++;
		};
	})();
	
	var rootWindow = window;
	while (rootWindow.parent && rootWindow.parent != rootWindow)
		rootWindow = rootWindow.parent;

	USQLegalWarning.rootWindow = rootWindow;

	// inner method should not be called directly
	// the non-inner counterpart will find the root window and perform operations in its context
	USQLegalWarning.attachFormInner = function(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate) {
		// we must have the form
		if (form && href && event) {
			var data = {};
			data.form = form;
			data.reload = false;
			data.action = form.action;
			data.event = event;
			data.elementId = elementId;
			data.instant = instant;
			data.decorate = decorate;
			data.quantityCheck = quantityCheck;
			if (tagCounter > maxTagCounter) {
				maxTagCounter = tagCounter;
			};
			if (!data.action) {
				data.action = href;
			}
			if (data.action) {
				var id = getUniqueId();
				data.id = id;
				forms[id] = data;
				bindSubmit(data, event);
			}
		}
	};
	
	var destroy = function(data) {
		data.form = null;
		data.action = null;
		if (data.panel)
			data.panel.destroy();
		data.panel = null;
	};

	USQLegalWarning.attachForm = function(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate) {
		return rootWindow.FreshDirect.USQLegalWarning.attachFormInner(form, href, event, elementId, instant, quantityCheck, tagCounter, decorate);
	};

	var normalSubmit = function(data) {
		data.form.onsubmit = null;
		data.form.submit();
	};
	
	var bindSubmit = function(data) {
		var form = data.form;
		var id = data.id;
		data.panel = null;
	
		var showPendingChoice = function() {
			if (window.parent.FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')== true) {
				if (data.instant && data.instant != null && data.instant != "") {
					eval(data.instant)();
					return;
				}
			}
			var counter = 0;
			var alcoholicFlag = null;
			while (counter <= maxTagCounter) {
				alcoholicFlag = USQLegalWarning.containsElement(form, 'alcoholic_' + counter);
				if ( alcoholicFlag != null) {
					var quantity = USQLegalWarning.containsElement(form, alcoholicFlag.value);
					if ('true' == data.quantityCheck) {
						if (quantity != null && quantity.value != null && quantity.value != "" &&  quantity.value > 0) {
							break;
						};
					} else {
						break;
					}
				}
				alcoholicFlag = null;
				counter ++;
			};
			if (!alcoholicFlag) {
				normalSubmit(data);
				return;
			};
	
			var uri;
			if (data.instant != "") {
				uri = document.location.protocol + '//' + document.location.host + '/health_warning_popup.jsp?formId=' + id + '&instant=' + data.instant + '&decorate=' + data.decorate;
			} else {
				uri = document.location.protocol + '//' + document.location.host + '/health_warning_popup.jsp?formId=' + id + '&decorate=' + data.decorate;
			}
			if (!data.panel) {
				data.panel = new YAHOO.widget.Panel('usq_legal_warning_choice_' + id, {
					fixedcenter: true, 
					constraintoviewport: true, 
					underlay: "matte", 
					close: true, 
					visible: true,
					modal: true,
					zIndex: 250,
					draggable: false}
				);
				
				data.panel.setHeader( "&nbsp;" );
				
				data.panel.hideEvent.subscribe(function() {
					if (data.reload) {
						FreshDirect.USQLegalWarning.rootWindow.location.reload();
						return;
					}
					// during hide we delete iframe to avoid nasty flicker
					data.panel.setBody('');
				});
	
				// we always stick the modal overlay to the root window
				var body = rootWindow.document.body;
				data.panel.render(body);
	
				YAHOO.util.Dom.addClass('usq_legal_warning_choice_' + id + '_c', 'usq_legal_warning_choice_c');
				YAHOO.util.Dom.addClass('usq_legal_warning_choice_' + id, 'usq_legal_warning_choice');
			}

			var content = "";
			content += '<div id="usq_legal_warning_choice_content_' + id + '" class="usq_legal_warning_choice_content">\n';
			content += '<div id="usq_legal_warning_choice_progress_' + id + '" class="usq_legal_warning_choice_progress">\n';
			content += 'Please wait... <img src="/media_stat/images/navigation/spinner.gif" width="50" height="50">\n';
			content += '</div>\n';
			content += '  <iframe id="usq_legal_warning_choice_frame_' + id + '" class="usq_legal_warning_choice_frame" style="z-index:150" frameborder="0" src="' + uri + '"></iframe>';
			content += '</div>\n';
			
			data.panel.setBody(content);
			
			data.panel.center();
			data.panel.show();	

		};	
		
		if (data.event == 'onsubmit') {
			data.onsubmit = function(){
				showPendingChoice();
				return false;
			};
		} else if (data.event == 'onclick' && data.elementId != "") {
			data.elementId[data.event] = function() {
				showPendingChoice();
			};
			data.onsubmit = function(){
				return false;
			};
		};

		form.onsubmit = data.onsubmit;
	};
	
	var getFormId = function(query) {
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
	
	USQLegalWarning.refreshPanelInner = function(query) {
		var id = getFormId(query);
		if (id && forms[id] && forms[id].panel) {
			var progress = rootWindow.document.getElementById('usq_legal_warning_choice_progress_' + id);
			if (progress)
				progress.style.display = 'none';
			forms[id].panel.center();
		}
	};

	USQLegalWarning.refreshPanel = function(query) {
		return rootWindow.FreshDirect.USQLegalWarning.refreshPanelInner(query);
	};
	
	USQLegalWarning.doNormalSubmitInner = function(query) {
		var id = getFormId(query);
		if (id && forms[id]) {
			if (forms[id].panel) {
				forms[id].panel.hide();
				forms[id].panel.destroy();
			}
			normalSubmit(forms[id]);
		}
	};
	
	USQLegalWarning.doNormalSubmit = function(query) {
		return rootWindow.FreshDirect.USQLegalWarning.doNormalSubmitInner(query);
	};	

	USQLegalWarning.blockSubmitInner = function(query) {
		var id = getFormId(query);
		if (id && forms[id]) {
			if (forms[id].panel){
				forms[id].panel.hide();
			}
		}
	};
	
	USQLegalWarning.blockSubmit = function(query) {
		return rootWindow.FreshDirect.USQLegalWarning.blockSubmitInner(query);
	};	

	USQLegalWarning.hidePanelByIdInner = function(id) {
		if (id && forms[id] && forms[id].panel)
			forms[id].panel.hide();
	};

	USQLegalWarning.hidePanelById = function(id) {
		return rootWindow.FreshDirect.USQLegalWarning.hidePanelByIdInner(id);
	};
	
	USQLegalWarning.setCookie = function(name, value)
	{
		var cookieValue = escape(value)+"@"+rootWindow.FreshDirect.USQLegalWarning.getJSessionId();
		document.cookie = name + "=" + cookieValue+";path=/;";
	};
	
	USQLegalWarning.getCookie = function(c_name)
	{
		
		var i, name, value, cookieArray = document.cookie.split(";");
		for (i = 0; i < cookieArray.length; i ++) {
		    name = cookieArray[i].split('=')[0];
		    value = cookieArray[i].split('=')[1];
			name = name.replace(/^\s+|\s+$/g,"");
			if (name == c_name) {
			    return unescape(value);
			}
		}
	};
	
	USQLegalWarning.checkHealthCondition = function(c_name, value)
	{
		var cookieValue = rootWindow.FreshDirect.USQLegalWarning.getCookie(c_name);
		var jSessionId = rootWindow.FreshDirect.USQLegalWarning.getJSessionId();
		if (cookieValue == value + "@" + jSessionId) return true;
		return false;
	};
	
	USQLegalWarning.getJSessionId = function(name)
	{
		var jSessionId = rootWindow.FreshDirect.USQLegalWarning.getQueryParameterByName('jsessionid');
		if (jSessionId == "") jSessionId = unescape(rootWindow.FreshDirect.USQLegalWarning.getCookie('JSESSIONID'));
		return jSessionId;
	};

	USQLegalWarning.getQueryParameterByName = function(name)
	{
	  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	  var regexS = "[\\?&]" + name + "=([^&#]*)";
	  var regex = new RegExp(regexS);
	  var results = regex.exec(window.location.search);
	  if(results == null)
	    return "";
	  else
	    return decodeURIComponent(results[1].replace(/\+/g, " "));
	};

	USQLegalWarning.containsElement = function(parentElement, childElementName)
	{
		var children = new Array();
		children = parentElement.getElementsByTagName('*');

		for (var i = 0; i < children.length; i++) { 
            var child = children.item(i); 
			if (child.name == childElementName) {
				return child; 
			}
        } 
			   
		return null;	
	};
})();