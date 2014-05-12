if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function ($) {
	var USQLegalWarning = FreshDirect.USQLegalWarning || {};
	
	FreshDirect.USQLegalWarning = USQLegalWarning;
	
	var rootWindow = window;
	while (rootWindow.parent && rootWindow.parent != rootWindow)
		rootWindow = rootWindow.parent;

	USQLegalWarning.rootWindow = rootWindow;

	var normalSubmit = function(data) {
		var oldOnSubmit = data.form.onsubmit;
		//remove
		data.form.onsubmit = null;
		data.form.submit();
		//(replace else older IEs will double submit)
		data.form.onsubmit = oldOnSubmit;
	};
	
	var isAlcoholicProductAdded = function(data) {
		var form = data.form;
		var alcoholicFlag = false;
		if ('true' == data.quantityCheck) {
			$( "#" + form.id +" .wine_quantity" ).each(function( index, quantityElement ) {
				var quantity = USQLegalWarning.containsElement(form, quantityElement.value);
				if (quantity != null && quantity.value != null && quantity.value != "" &&  quantity.value > 0) {
					alcoholicFlag = true;
					return false;
				};
		    });
		} else {
			return true;
		}		
		return alcoholicFlag;
	};
	
	USQLegalWarning.showPendingChoice = function(data) {
		var id = data.id;
		
		if (data.instant && data.instant != null && data.instant != '') {
			
			if (window.parent.FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')== true || data.usq == 'false') { //AJAX add to cart (search page, ddpp pages)
				if (data.atc == 'true') {
					USQLegalWarning.submitWithAtc(data);
				} else {
					if ($.isFunction(data.instant)) {
						eval(data.instant)();
					}
				}
				return;
			}

		} else if (data.multiForm && data.multiForm == 'true') { //multi form ~ whole form submit combined with row level submits (grocery pages)
			
			if (data.usq == 'false' || data.containsAlcoholic == 'false') {
				if (data.atc == 'true') {
					USQLegalWarning.submitWithAtc(data);
				} else {
					normalSubmit(data);
				}
				return;
			};

		} else { //normal add to cart(quickbuy, (view cart) ymal, cart confirm recommender), recipie pages
		
			var alcoholicFlag = isAlcoholicProductAdded(data);
	
			if (!alcoholicFlag || data.usq == 'false') {
				if (data.atc == 'true') {
					USQLegalWarning.submitWithAtc(data);
				} else {
					normalSubmit(data);
				}
				return;
			};
		}

		var uri = document.location.protocol + '//' + document.location.host + '/overlays/health_warning_popup.jsp?formId=' + id + '&decorate=' + data.decorate;
		if (data.instant != "") {
			uri += '&instant=' + data.instant;
		} 
		if (data.atc == "true") {
			uri += '&atc=true';
		} 
    if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
      FreshDirect.components.ifrPopup.open({
        url: uri,
        borderRadius: "10px",
        opacity: 0.5
      });
    } else {
      if (!data.panel) {
        data.panel = new YAHOO.widget.Panel('wine_legal_warning_choice_' + id, {
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

        YAHOO.util.Dom.addClass('wine_legal_warning_choice_' + id + '_c', 'wine_legal_warning_choice_c');
        YAHOO.util.Dom.addClass('wine_legal_warning_choice_' + id, 'wine_legal_warning_choice');
      }

      var content = "";
      content += '<div id="wine_legal_warning_choice_content_' + id + '" class="wine_legal_warning_choice_content">\n';
      content += '<div id="wine_legal_warning_choice_progress_' + id + '" class="wine_legal_warning_choice_progress">\n';
      content += 'Please wait... <img src="/media_stat/images/navigation/spinner.gif" width="50" height="50">\n';
      content += '</div>\n';
      content += '  <iframe id="wine_legal_warning_choice_frame_' + id + '" class="wine_legal_warning_choice_frame" style="z-index:150" frameborder="0" src="' + uri + '"></iframe>';
      content += '</div>\n';
      
      data.panel.setBody(content);
      
      data.panel.center();
      data.panel.show();	
    }
	};	
	
	USQLegalWarning.refreshPanelInner = function(query) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id] && FreshDirect.PopupDispatcher.forms[id].panel) {
			var progress = rootWindow.document.getElementById('wine_legal_warning_choice_progress_' + id);
			if (progress)
				progress.style.display = 'none';
			FreshDirect.PopupDispatcher.forms[id].panel.center();
		}
	};

	USQLegalWarning.refreshPanel = function(query) {
		return rootWindow.FreshDirect.USQLegalWarning.refreshPanelInner(query);
	};
	
	USQLegalWarning.submitWithAtc = function(data) {
		data.panel = null;
		FreshDirect.ATC_Pending.onSubmitBind(data);
	};
		
	USQLegalWarning.doNormalSubmitInner = function(query, atc) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id]) {
			if (FreshDirect.PopupDispatcher.forms[id].panel) {
				FreshDirect.PopupDispatcher.forms[id].panel.hide();
//				FreshDirect.PopupDispatcher.forms[id].panel.destroy();
			}
			if (atc == 'true') {
				FreshDirect.PopupDispatcher.forms[id].panel = null;
				FreshDirect.ATC_Pending.onSubmitBind(FreshDirect.PopupDispatcher.forms[id]);
			} else {
				normalSubmit(FreshDirect.PopupDispatcher.forms[id]);
			}
		}
	};
	
	USQLegalWarning.doNormalSubmit = function(query, atc) {
		return rootWindow.FreshDirect.USQLegalWarning.doNormalSubmitInner(query, atc);
	};	

	USQLegalWarning.blockSubmitInner = function(query, atc) {
		var id = FreshDirect.PopupDispatcher.getFormId(query);
		if (id && FreshDirect.PopupDispatcher.forms[id]) {
			if (FreshDirect.PopupDispatcher.forms[id].panel){
				FreshDirect.PopupDispatcher.forms[id].panel.hide();
//				FreshDirect.PopupDispatcher.forms[id].panel.destroy();
			}
		}
		if (atc == 'true') {
			FreshDirect.PopupDispatcher.forms[id].panel = null;
			FreshDirect.ATC_Pending.onSubmitBind(FreshDirect.PopupDispatcher.forms[id]);
		}
	};
	
	USQLegalWarning.blockSubmit = function(query, atc) {
		return rootWindow.FreshDirect.USQLegalWarning.blockSubmitInner(query, atc);
	};	

	USQLegalWarning.hidePanelByIdInner = function(id) {
		if (id && FreshDirect.PopupDispatcher.forms[id] && FreshDirect.PopupDispatcher.forms[id].panel)
			FreshDirect.PopupDispatcher.forms[id].panel.hide();
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
		return rootWindow.FreshDirect.USQLegalWarning.sessionStore;
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
})(jQuery);
