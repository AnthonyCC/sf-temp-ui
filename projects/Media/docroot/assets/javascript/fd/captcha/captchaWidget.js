var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	fd.components = fd.components || {};
	var hasError = false;
	var captchaWidgetId, captchaKey;
	var defaultWidgetName = 'default';
	var widgetSettings = {};
	
	function init(widgetName, publicKey, callback) {
		// associate name to key, in some (edge) cases we render multiple widgets with the same key but different names
		getWidgetSettings(widgetName).key = publicKey;
		// load recaptcha api if it hasn't been loaded yet
		if (!window.grecaptcha) {
			var script = document.createElement('script');
		 	script.src = 'https://www.google.com/recaptcha/api.js?onload=onCaptchaLoadCallback&render=explicit';
		 	script.async = true;
		 	script.defer = true;
		  	document.head.appendChild(script);
		  
		  	window.onCaptchaLoadCallback = function () {
		  		if (callback && typeof callback === 'function') {
		  			callback();
		  		}
		  	};
		} else {
			if (callback && typeof callback === 'function') {
				callback();
			}
		}
	}
	
	function setKey(widgetName, key) {
		getWidgetSettings(widgetName).key = key;
	}
	
	function getKey(widgetName) {
		return getWidgetSettings(widgetName).key;
	}
	function render(widgetName, callback, errorCallback, expiredCallback ) {
		var captchaWidgetId;
	    try {
	    	hasError = false;
	    	captchaWidgetId = grecaptcha.render(widgetName, {
	        	'sitekey' : getWidgetSettings(widgetName).key,
	        	'error-callback': function() {
	        		hasError = true;
	        		errorCallback();
	        	},
	        	'expired-callback': expiredCallback,
	        	'callback': callback
	      });
	    	getWidgetSettings(widgetName).id = captchaWidgetId;
	    
	    } catch(e){
	    	errorCallback();
	    }
		return captchaWidgetId;
	}
	function reset(widgetName) {
		grecaptcha.reset(getWidgetSettings(widgetName).id);

	}
	function getResponse(widgetName){
		try {
			return grecaptcha.getResponse(getWidgetSettings(widgetName).id);
		} catch(e) {
			return null;
		}
	}
	
	function isEnabled(widgetName) {
		return (getWidgetSettings(widgetName).id !=null) && !hasError && window.grecaptcha;
	}
	function isValid(widgetName) {
		try {
			return !isEnabled(widgetName) || !!grecaptcha.getResponse(getWidgetSettings(widgetName).id);
		} catch(e) {
			hasError = true;
			return true;
		}
	}
	
	function getWidgetSettings(widgetName) {
		widgetName = widgetName || defaultWidgetName;
		widgetSettings[widgetName] = widgetSettings[widgetName] || {};
		return widgetSettings[widgetName];
	}
	// if component is not registered, register
	if (!fd.components.captchaWidget) {
		var captchaWidget = {
			init : init,
			render: render,
			reset: reset,
			isEnabled: isEnabled,
			isValid: isValid,
			getResponse: getResponse,
			setKey: setKey,
			getKey: getKey
		};
		if (fd.modules && fd.modules.common && fd.modules.common.utils){
			fd.modules.common.utils.register("components", "captchaWidget", captchaWidget,
					fd);
		} else {
			fd.components.captchaWidget = captchaWidget;
		}
	}
	
}(FreshDirect));