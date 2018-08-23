/* requires jquery */
/*

MESSAGES AND ALERTS
	MESSAGES: Are all grouped together under one container (messageData.options.$messages). They are all either open or closed together, and have a single handler which is appended to the container.

	ALERTS: Act independently, each with it's own handler that is appended to their container(s). Each can be appended to multiple parent containers, each parent will get a handler controlling all locations of the alert.

	On the case that multiple alerts are called at once and the alerts' addTo overlap, like using:
		[ALERT1,ALERT2], { addTo: 'A1,A2' }, { addTo: 'A2,A3' }

	The handler will be set for the last alert added (ALERT2) on A2, but A2 would contain content from both ALERT1 and ALERT2.

	All non-saved data is stored in messageData, all saved data is stored in messageStorage.

MESSAGE USAGE:
	to define a new message that runs automatically:
		add the content to the page:
			<div class="messages invisible" id="message1">CONTENT</div>

		The classes "messages and invisible" are required to automatically use, the id is always required.

		Optionally use:
			data-type="IDSTRING" - string id, overrides message id.

		NOTE: The message id is used for ordering and comparison as SystemMessage. If IDSTRING is not in messageData.options.messagesOrder, it will be pushed onto the end.

	to define call manually:
		call this js:
			$('#message1').messages('add','IDSTRING');

		IDSTRING: string id

	Events:
		messageAdded : On a successful add, triggers event on callee.
		messagesOpen : On open, triggers event on callee.
		messagesClose : On close, triggers event on callee.

ALERTS USAGE:
	to define a new alert that runs automatically:
		add the content to the page:

			<div class="alerts invisible" id="alert1">CONTENT</div>

		The classes "alerts and invisible" are required to automatically use, the id is always required.

		Optionally use:
			data-type="IDSTRING" - string id, overrides alert id
			data-addto="SELECTOR1, ... ,SELECTORN" - jQuery selectors, overrides addTo. default is self
			data-closehandleraddto="SELECTOR1, ... ,SELECTORN" - jQuery selectors, overrides closeHandlerAddTo. default is self

	to define call manually:
		call this js:
			$('#alert1').messages('add', [IDSTRINGS], true, [ALERTOPTSOBJS]);

		[IDSTRINGS]: string id
		true: using 'add', the third param says its an alert (otherwise it's a message). Using 'addAlert' instead, just pass [IDSTRINGS], [ALERTOPTSOBJS]
		[ALERTOPTSOBJS]: (optional) array of objects of alert options, overrides any defaults:
			{
				html: HTML, - default: this.innerHTML,
				addTo: SELECTORS, - default $(this).attr('data-addto') || this
				closeHandlerAddTo: SELECTORS - default $(this).attr('data-closehandleraddto') || this
			}

		The index of [IDSTRINGS] is used for the index of [ALERTOPTSOBJS].

	Optionally, if using addTo, add the html content for those containers.

	Events:
		alertAdded : On a successful add, triggers event on callee.
		alertRemove :On a successful remove, triggers event on each addTo element.
		alertCloseHandlerAdded : On append of a close handler, triggers event on each closeHandlerAddTo element.
		alertUpdated : On a successful update, triggers event on each addTo element.
		alertOpen : On a successful open, triggers event on each addTo element, along with the parent container (for grouped alerts).
		alertClose : On a successful close, triggers event on each addTo element, along with the parent container (for grouped alerts).

	Event Flow:
		add [ -> update ] [ -> open]
		remove -> update -> (save messageData)
		close -> (save messageData)

SESSIONSTORAGE:
	sessionStorage is saved under a single key (messageData.jsessionId). See: messageStorage for the data.

	On script load, a cleaner will run that will remove any old leftover storage keys that are no longer applicable (old session key data).

 */
(function(fd){

	var $=fd.libs.$;
	var console = console || window.top.console || { log: function(){} };

	var messageData = {
		options: {
			SystemMessage: 'SystemMessage',
			messagesOrder: [],
			$messages: $('#messages'),
			handlerSelector: '.handler',
			messagesOpenClass: 'open',
			messagesOpenVisHiddenClass: 'visHidden',
			alertsOpenClass: 'open', //class to show alert is open
			alertsContainerClass: 'alert-cont', //class to find parent container for alert
			alertsCloseHandlerClass: 'alert-closeHandler', //class to add to default close handler
			alertsCloseHandler: function(e) {
				e.preventDefault();
				var parentSelector = $(this).data('parentselector') || '.alert-cont';
				var closeIdsArr = $(this).attr('data-alertcloseids') || [];
				if (!$.isArray(closeIdsArr)) { closeIdsArr = closeIdsArr.split(',')}
				$(this).parent(parentSelector).messages('closeAlerts', closeIdsArr);
				//$(this).hide();
			}
		},
		jsessionId: getJsessionId(),
		messages: {}, /* MSG1 : HTML */
		alerts: {}, /* ALERT1: { html: HTML, addTo: SELECTORS || '', closeHandlerAddTo: SELECTORS || '' } */
		messagesIsClosed: false
	};
	messageData.options.messagesOrder = [messageData.options.SystemMessage, 'cutoff', 'platterwarning', 'reservationwarning', 'deliveryetawarning'];


	var oldHashesStorageKey = messageData.jsessionId+'/messageHashes',
		oldClosedStorageKey = messageData.jsessionId+'/messagesClose';

	var messageStorage = { /* saved to storageSession */
		messageStorage: new Date().getTime(), /* last save timestamp */
		messages: { /* messages all show/close together */
			messagesHashes : {}, /* { MSG1: HASH } */
			isClosed: false
		},
		alerts: {} /* alerts can act independently, ALERT1: { hash: HASH, isClosed: BOOL } */
	};

	function getJsessionId() {
		var jsessionId = 'FD_NO_SESSION_ID';
		
		if ($.isFunction(fd.USQLegalWarning.getJSessionId)) {
			jsessionId = fd.USQLegalWarning.getJSessionId();
		} else if (fd.USQLegalWarning.hasOwnProperty('sessionStore')) {
			jsessionId = fd.USQLegalWarning.sessionStore;
		}
		return jsessionId;
	}

	function getMessageStorage() {
		var msgS = {};
		if (hasSessionStorage()) {
			msgS = $.extend(messageStorage, JSON.parse(sessionStorage.getItem(messageData.jsessionId)));
		}
		return msgS;
	};

	function setMessageStorage() {
		if (hasSessionStorage()) {
			messageStorage.messageStorage = new Date().getTime();
			sessionStorage.setItem(messageData.jsessionId, JSON.stringify(messageStorage));
		}
	}

	/* clean out leftover sessionStorage items */
	function cleanMessageStorage() {
		if (hasSessionStorage()) {
			var keyHashes = oldHashesStorageKey.split('/')[1];
			var keyClosed = oldClosedStorageKey.split('/')[1];

			for (var item in sessionStorage) {
				if (item !== messageData.jsessionId) {
					var temp = {};
					try {
						temp = JSON.parse(sessionStorage.getItem(item));

						if (temp.hasOwnProperty('messageStorage')) {
							sessionStorage.removeItem(item);
							continue;
						}
					} catch(e) {
					}

					/* remove previous implementation's leftover data */
					var itemArr = item.split('/');

					if (itemArr[0] !== messageData.jsessionId && itemArr.length == 2 &&
						(itemArr[1] == keyHashes || itemArr[1] == keyClosed)
					) {
						sessionStorage.removeItem(item);
					} else {
						//do nothing
					}
				}
			}
		}
	}

	function getHash(s){
		var hash = 0;
		if (s.length ==	0) return hash;
		for (i = 0; i < s.length; i++) {
			ch = s.charCodeAt(i);
			hash = ((hash<<5)-hash)+ch;
			hash = hash & hash;
		}
		return hash;
	}

	function removeItem(_id){
		if (_id != messageData.options.SystemMessage) {
			delete messageData.messages[_id];
			delete messageStorage.messages.messagesHashes[_id];

			delete messageData.alerts[_id];
			delete messageStorage.alerts[_id];
		}
	}

	function getSelector(elem) {
		return (typeof($(elem).attr('id')) !== 'undefined' || $(elem).attr('id') !== null)
			? '#' + $(elem).attr('id')
			: '.' + $(elem).attr('class');
	}

	/**
	 * https://gist.github.com/paulirish/5558557
	 */
	function hasSessionStorage(){
		try {
			sessionStorage.setItem('test', 1);
			sessionStorage.removeItem('test');
			return true;
		} catch(e) {
			return false;
		}
	}

	function setOptions(_options) {
		$.extend(messageData.options, _options);
	}

	var methods = {
		isClosed: function() { //for messages ONLY
			try {
				return (hasSessionStorage()) ? getMessageStorage().messages.isClosed : messageData.messagesIsClosed;
			} catch(e) {
				return true; //no other choice, return as closed
			}
		},
		init: function( _options ) {
			setOptions(_options);

			getMessageStorage();
			cleanMessageStorage();
		},
		setOptions: function(_options) {
		},
		destroy: function() {
			return this.each(function(){});
		},
		open: function(_opts) {
			var opts = {
				openMessages: true,
				openAlerts: ['_all'] //array of alerts to open, or _all for all
			};
			$.extend(opts, _opts);
			if (opts.openMessages) {
				this.messages('openMessages');
			}

			this.messages('openAlerts', opts.openAlerts);

			setMessageStorage();
		},
		openMessages: function() {
			messageData.messagesIsClosed = false;
			messageStorage.messages.isClosed = false;
			$(messageData.options.$messages.selector).addClass(messageData.options.messagesOpenClass);
			$(messageData.options.$messages.selector).removeClass(messageData.options.messagesOpenVisHiddenClass);
			$(messageData.options.$messages.selector).trigger({
				type: 'messagesOpen'
			});
		},
		openAlerts: function(alertsArr) {
			if ($.isArray(alertsArr)) {
				for (var i = 0; i < alertsArr.length; i++) {
					var curAlert = alertsArr[i];
					if (curAlert !== '_all' && !messageData.alerts[curAlert]) {
						continue;
					}


					for (var alert in messageData.alerts) {
						if (curAlert !== '_all' && alert !== curAlert) {
							continue;
						}

						messageData.alerts[alert].isClosed = false;
						$(messageData.alerts[alert].addTo).addClass(messageData.options.alertsOpenClass);

						$(messageData.alerts[alert].addTo).trigger({
							type: 'alertOpen',
							alert: alert
						});

						//trigger on parent as well (to catch grouped alerts)
						$parentCont = $(messageData.alerts[alert].addTo).parent('.'+messageData.options.alertsContainerClass);
						if ($parentCont !== $(messageData.alerts[alert].addTo)) { //avoid double fire, if possible
							$parentCont.addClass(messageData.options.alertsOpenClass);

							$parentCont.trigger({
								type: 'alertOpen',
								alert: alert
							});
						}
					}
				}
			}
		},
		close: function(_opts) {
			var opts = {
				closeMessages: true,
				closeAlerts: [] //array of alerts to close, or _all for all
			};
			$.extend(opts, _opts);

			if (opts.closeMessages) {
				this.messages('closeMessages');
			}

			this.messages('closeAlerts', opts.closeAlerts);
		},
		closeMessages: function() {
			messageData.messagesIsClosed = true;
			messageStorage.messages.isClosed = true;
			$(messageData.options.$messages.selector).removeClass(messageData.options.messagesOpenClass);
			$(messageData.options.$messages.selector).addClass(messageData.options.messagesOpenVisHiddenClass);
			$(messageData.options.$messages.selector).trigger({
				type: 'messagesClose'
			});

			setMessageStorage();
		},
		closeAlerts: function(alertsArr) {
			if (!$.isArray(alertsArr)) {
				alertsarry = [].push(alertsArr);
			}
			for (var i = 0; i < alertsArr.length; i++) {
				var curAlert = alertsArr[i];
				if (messageData.alerts[curAlert] || curAlert[i] === '_all') {
					messageStorage.alerts[curAlert].isClosed = true;
					$(messageData.alerts[curAlert].addTo).removeClass(messageData.options.alertsOpenClass);

					$(messageData.alerts[curAlert].addTo).trigger({
						type: 'alertClose'
					});

					//close parent if needed
					$parent = $(messageData.alerts[curAlert].addTo).parent('.'+messageData.options.alertsContainerClass);
					if ($parent.find('.'+messageData.options.alertsOpenClass).length === 0) {
						$parent.removeClass(messageData.options.alertsOpenClass);

						$parent.trigger({
							type: 'alertClose'
						});
					}
				}
			}

			setMessageStorage();
		},
		remove: function(id) {
			if (id != messageData.options.SystemMessage) {
				if (messageData.messages[id]) {
					delete messageData.messages[id];
					messageData.options.$messages.messages('updateMessages');
				}

				if (messageData.alerts[id]) {
					var $addTo = $(messageData.alerts[curAlert].addTo);
					delete messageData.alerts[id];

					$addTo.trigger({
						type: 'alertRemove'
					});

					$addTo.messages('updateAlerts');
				}

				setMessageStorage();
			}
		},
		update: function() {
			this.messages('updateMessages');
			this.messages('updateAlerts');
		},
		updateMessages: function() {
			var id, html = '', addHandler = false;

			//show ordered messages
			for (var i = 0; i < messageData.options.messagesOrder.length; i++) {
				id = messageData.options.messagesOrder[i];

				if (messageData.messages[id]) {
					html += '<li class="'+id+'">'+messageData.messages[id].html+'</li>';
					addHandler = true;
				}
			}

			//show any unordered messages at the end
			for (id in messageData.messages) {
				if ($.inArray(id, messageData.options.messagesOrder) == -1) {
					html += '<li class="'+id+'">'+messageData.messages[id].html+'</li>';
					addHandler = true;
				}
			}


			if (addHandler) {
				$(messageData.options.$messages.selector).addClass('hashandler');
			} else {
				$(messageData.options.$messages.selector).removeClass('hashandler');
				messageData.options.$messages.messages('closeMessages');
			}

			$(messageData.options.$messages.selector+' ul').html(html);
			
			return messageData.options.$messages;
		},
		updateAlerts: function(alertsArr) {
			if ($.isArray(alertsArr)) {
				var alertCont = {}; var contArr = []; /* addToSelector: HTML */

				for (var i = 0; i < alertsArr.length; i++) {
					var curAlert = alertsArr[i];
					if (curAlert !== '_all' && !messageData.alerts[curAlert]) {
						continue;
					}

					for (var alert in messageData.alerts) {
						if (curAlert !== alert && curAlert !== '_all') {
							continue;
						}

						var curAddToArr = $(messageData.alerts[alert].addTo).get();
						$(curAddToArr).each(function() {
							var selector = getSelector(this);

							if (!alertCont.hasOwnProperty(selector)) {
								alertCont[selector] = { selectors: [], html: '', alertIds: [] };
							}

							alertCont[selector].html += messageData.alerts[alert].html;

							if ($.inArray(selector, alertCont[selector].selectors) === -1) {
								alertCont[selector].selectors.push(selector);
							}

							if ($.inArray(alert, alertCont[selector].alertIds) === -1) {
								alertCont[selector].alertIds.push(alert);
							}
						});
					}

				}

				for (var cont in alertCont) {
					var curAlertCont = alertCont[cont];

					$(curAlertCont.selectors).each(function(i,e) {
						var $this = $(e);

						$this.removeClass('invisible').addClass('alert-cont').html(curAlertCont.html);

						contArr.push($this);

						$this.trigger({
							type: 'alertUpdated'
						});
					});

					//add close handler(s)
					$(curAlertCont.alertIds).each(function(i,e) {
						var closerCurAlert = messageData.alerts[e];

						if ($(closerCurAlert.closeHandlerAddTo).find('.'+messageData.options.alertsCloseHandlerClass).length === 0) {
							$(closerCurAlert.closeHandlerAddTo).append('<a href="#" onclick="event.preventDefault();" class="'+messageData.options.alertsCloseHandlerClass+'" data-alertcloseids="'+curAlertCont.alertIds.join(',')+'" data-parentselector="'+messageData.options.alertsContainerClass+'"><span class="offscreen">close</span></a>');

							$(closerCurAlert.closeHandlerAddTo).find('.'+messageData.options.alertsCloseHandlerClass).on('click', messageData.options.alertsCloseHandler);

							$(closerCurAlert.closeHandlerAddTo).trigger({
								type: 'alertCloseHandlerAdded'
							});

						}
					});
				}

				return $(contArr).each(function() {
				});
			} else {
				return $('');
			}
		},
		add: function(_id, _isAlert, _alertOpts) {
			if (_isAlert) { //alert
				return this.messages('addAlert', _id, _alertOpts || []);
			} else { //message
				return this.messages('addMessage', _id, _alertOpts);
			}
		},

		addMessage: function(_id, _text) {
			var opened = false, update;

			var result = this.each(function(){
				var $this = $(this), text, hash, id;

				$('script', this).remove();
				id = _id || $this.attr('data-type');

				text = _text || $this.text();

				if ($.trim(text).length > 1) {
					hash = getHash(text);
					messageData.messages[id] = {hash: hash, html: _text || this.innerHTML};

					if(messageStorage.messages.messagesHashes[id] != hash ) { //check for newly updated message
						opened = true;
						messageStorage.messages.messagesHashes[id] = hash;
					}

					$this.trigger({
						type: 'messageAdded'
					});
				} else {
					removeItem(id);
				}
			});

			update = messageData.options.$messages.messages('updateMessages');

			if (opened) {
				update.messages('openMessages');
			}
			/* don't close here, it'll close on timing, instead of the msgs status
			 else {
				update.messages('closeMessages');
			}
			*/

			return result;
		},
		addAlert: function(_id, _alertOpts) {
			var opened = false, update, alertData = {}, updateIdArr = [], openIdArr = [];

			var result = this.each(function(i,e){
				var $this = $(this), text, hash, id, curAlertOpts = {}, defaultAddTo = '', defaultCloseHandlerAddTo = '';

				if ($this.attr('data-addto')) {
					defaultAddTo = $this.attr('data-addto');
				} else {
					defaultAddTo = getSelector(this);
				}

				if ($this.attr('data-closehandleraddto')) {
					defaultCloseHandlerAddTo = $this.attr('data-closehandleraddto');
				} else {
					defaultCloseHandlerAddTo = getSelector(this);
				}

				if (_alertOpts[i]) {
					curAlertOpts = _alertOpts[i];
				}

				alertData = { html: this.innerHTML, addTo: defaultAddTo, closeHandlerAddTo: defaultCloseHandlerAddTo };
				$.extend(alertData, curAlertOpts);

				$('script', this).remove();
				id = $this.id || $this.attr('data-type');
				text = $this.text();

				if ($.trim(text).length > 1) {
					hash = getHash(text);
					messageData.alerts[id] = alertData;

					if (!messageStorage.alerts[id]) {
						messageStorage.alerts[id] = { hash: '', isClosed: false };
					}

					if(messageStorage.alerts[id].hash != hash || messageStorage.alerts[id].isClosed === false) { //check for newly updated alert
						opened = true;
						messageStorage.alerts[id].hash = hash;
						messageStorage.alerts[id].isClosed = false;

						openIdArr.push(id);
					}

					$this.trigger({
						type: 'alertAdded'
					});

					updateIdArr.push(id);
				} else {
					removeItem(id);
				}


			});

			setMessageStorage();

			if (alertData.hasOwnProperty('addTo') && $(alertData.addTo).length !== -1) {
				update = $(alertData.addTo).messages('updateAlerts', updateIdArr);
			}

			if (opened) {
				update.messages('openAlerts', openIdArr);
			}

			return result;
		}
	};

	$.fn.messages = function( method ) {
		if ( methods[method] ) {
			return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else if ( typeof method === 'object' || !method ) {
			return methods.init.apply( this, arguments );
		} else {
			$.error( 'Method ' + method + ' does not exist on jQuery.messages' );
		}
	};

	// When OAS pushes a system message:
	$(document).on('OAS_DONE',function(event,id){
		if (id == messageData.options.SystemMessage) {
			$(document.getElementById("oas_"+messageData.options.SystemMessage)).messages('add', messageData.options.SystemMessage);
		}
	});

	$(document).on('click','#messages .handler',function(e){
		var $messages = $(document.getElementById('messages'));
		if ($messages.hasClass('open')) {
			$messages.messages('closeMessages');


		} else {
			$messages.messages('openMessages');
		}
	});

	$(document).messages('init');
	$(function() {
		$('.message.invisible').messages('add');
		$('.alerts.invisible').messages('add', $.unique($('.alerts.invisible[id]').map(function() { return this.id; }).get()), true);

		$(".newziptext").on('keydown', function(e){

			var text_length=$(this).val().length;
			var keycode = (e.keyCode ? e.keyCode : e.which);
			$(this).removeClass("input-error");
			$(this).parent().next().css("visibility","");
			if(keycode == '13'){
				if(text_length<5 && text_length>=1){
						$(this).parent().next().css("visibility","visible");
						$(this).focus();
						setTimeout(function(){
							$(this).parent().find("input").focus();
							$(".newziptext")[0].setSelectionRange(text_length,text_length);
						},500);
						$(this).addClass("input-error");
				}
				else if(text_length!="" && text_length == 5){
					$('#messages').removeClass("open");
					sendZip(e);
				}
				else{
					$("#newziptext").focus();
				}
				e.preventDefault();
			}
		});
/*
		$(".newzipgo").on('keydown', function(e){

			var text_length=$(this).parent().find("input").val().length;
			console.log(text_length);
			var keycode = (e.keyCode ? e.keyCode : e.which);
			if(keycode == '13'){
				if(text_length<5 && text_length>=1){
						$(this).parent().next().css("visibility","visible");
						$(this).parent().find("input").addClass("input-error");
						$(this).focus();
						//console.log($(this));
						setTimeout(function(){
							$(this).focus();
						},500);
				}
				else if(text_length!= "" && text_length == 5){
					$('#messages').removeClass("open");
					sendZip(e);
				}

				e.preventDefault();
			}
		});
	*/	
		$(".location-email-text").on('keydown', function(e){

			var email_text=$(this).val();
			$(this).parent().find(".error-msg").css("visibility","");
			$(this).parent().find("input").removeClass("input-error");
			var email_regex=/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

		  	 if(e.which == '13'){
		  		var IsValidEmail=email_regex.test(email_text);
		  		if(IsValidEmail==false && email_text!=""){
					$(this).parent().find(".error-msg").css("visibility","visible");
					$(this).parent().find("input").addClass("input-error");
					setTimeout(function(){
						$(this).parent().find("input").focus();
					},500)
		  		}
		  		else if(IsValidEmail==true){

		  			//submt email method
		  			$("#location-submit.fdxgreen").trigger("click");
		  			//$("#location-submit").click();
		  			$(this).parent().find(".error-msg").css("visibility","hidden");
					$(this).parent().find("input").removeClass("input-error");
		  		}
		  		e.preventDefault();
		  	}
		  	//e.preventDefault();

		});
/*
		$("#location-submit.fdxgreen").on('keydown', function(e){

			var email_text=$(this).parent().find('input').val();
			var email_regex=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			var IsValidEmail=email_regex.test(email_text);
		  	 if(e.which == '13'){
		  		if(IsValidEmail==false && email_text!=""){
		  			$(this).parent().find("input").removeClass("input-error");
		  			$(this).parent().find(".error-msg").css("visibility","visible");
					$(this).parent().find("input").addClass("input-error");
					setTimeout(function(){
						$(this).focus();
					},500);
					//$(this).parent().find("input").focus();
		  		}
		  		else if(IsValidEmail==true && email_text!=""){
		  			//submt email method
		  			$(this).trigger("click");
		  			//$("#location-submit").click();

		  		}
		  		e.preventDefault();
		  	}
		  	//e.preventDefault();

		});
*/
    function addMessage(id, isAlert, alertOpts) {
      return $('body').messages('add', id, isAlert, alertOpts);
    }

    /* removed requirement to register for mobweb index optimization testing */
    if (fd && fd.modules && fd.modules.common && fd.modules.common.utils) {
    	fd.modules.common.utils.register("messages", "add", addMessage, fd);
    }

	});
})(FreshDirect);
