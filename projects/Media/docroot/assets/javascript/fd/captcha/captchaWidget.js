var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	fd.components = fd.components || {};
	var hasError = false;
	var captchaWidgetId, captchaKey, captchaPublicKey;
	
	function init(publicKey, callback) {
		captchaPublicKey = captchaPublicKey || publicKey;
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
	
	function setKey(key) {
		captchaPublicKey = key;
	}
	function render(container, callback, errorCallback, expiredCallback ) {
	    try {
	    	hasError = false;
	    	captchaWidgetId = grecaptcha.render(container, {
	        	'sitekey' : captchaPublicKey,
	        	'error-callback': function() {
	        		hasError = true;
	        		errorCallback();
	        	},
	        	'expired-callback': expiredCallback,
	        	'callback': callback
	      });
	    
	    } catch(e){
	    	errorCallback();
	    }
		return captchaWidgetId;
	}
	function reset() {
		grecaptcha.reset(captchaWidgetId);

	}
	function getResponse(){
		try {
			return grecaptcha.getResponse(captchaWidgetId);
		} catch(e) {
			return null;
		}
	}
	
	function isEnabled() {
		return (captchaWidgetId !=null) && !hasError && window.grecaptcha;
	}
	function isValid() {
		return !isEnabled() || !!grecaptcha.getResponse();
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
			setKey: setKey
		};
		if (fd.modules && fd.modules.common && fd.modules.common.utils){
			fd.modules.common.utils.register("components", "captchaWidget", captchaWidget,
					fd);
		} else {
			fd.components.captchaWidget = captchaWidget;
		}
	}
	
}(FreshDirect));