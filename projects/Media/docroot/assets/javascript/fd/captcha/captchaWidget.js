/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	fd.components = fd.components || {};
	var captchaWidgetId, captchaKey, captchaPublicKey;
	
	function init(publicKey, callback) {
		captchaPublicKey = captchaPublicKey || publicKey;
	  // load recaptcha api if it hasn't been loaded yet
	  if (!window.grecaptcha) {
	  var script = document.createElement('script');
	  script.src = 'https://www.google.com/recaptcha/api.js?onload=onCaptchaLoadCallback&render=explicit';
	  script.async = true;
	  script.defer = true;
	  document.head.appendChild(script); //or something of the likes
	  
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
	
	function render(container, callback, errorCallback, expiredCallback ) {
	    try {
	    	captchaWidgetId = grecaptcha.render(container, {
	        	'sitekey' : captchaPublicKey,
	        	'error-callback': errorCallback,
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
		return grecaptcha.getResponse(captchaWidgetId);
	}
	
	function isEnabled(container) {
		return $jq(container).length && $jq(container).val() === 'true' && window.grecaptcha;
	}
	function isValid(container) {
		return !isEnabled(container) || !!grecaptcha.getResponse();
	}
	// if component is not registered, register
	if (!fd.components.captchaWidget) {
		var captchaWidget = {
			init : init,
			render: render,
			reset: reset,
			isEnabled: isEnabled,
			isValid: isValid,
			getResponse: getResponse
		};
		if (fd.modules && fd.modules.common && fd.modules.common.utils){
			fd.modules.common.utils.register("components", "captchaWidget", captchaWidget,
					fd);
		} else {
			fd.components.captchaWidget = captchaWidget;
		}
	}
	
}(FreshDirect));