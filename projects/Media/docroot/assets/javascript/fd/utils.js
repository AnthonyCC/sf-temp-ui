var FreshDirect = FreshDirect || {};

(function (fd) {

  var utils = {};

  /**
   * Create the given namespace
   *
   * @param {string} namespace the namespace as dotted path
   * @param {Object} container the root of the namespace (optional)
   */
  utils.mknamespace = function (namespace, container) {
    var ns = namespace.split('.'),
        o = container || window,
        i, len;

    for (i = 0, len = ns.length; i < len; i++) {
      o = o[ns[i]] = o[ns[i]] || {};
    }

    return o;
  };

  /**
   * Register the object under the given namespace as 'name'
   *
   * @param {string} namespace the namespace
   * @param {string} name the name of the object in the namespace
   * @param {Object} obj the object to register
   * @param {Object} root the namespace root (optional)
   */
  utils.register = function (namespace, name, obj, root) {
    var ns = utils.mknamespace(namespace, root);
    ns[name] = obj;
  };

  /**
   * Extends the given object with the other objects (shallow copy of properties)
   */
  utils.extend = function (obj) {
    var length = arguments.length,
        i = 1,
        key, from;

    if (obj === null || typeof(obj) !== 'object' || length === i) {
      return obj;
    }

    for (; i < length; i++) {
      if ((from = arguments[i]) !== null) {
        for (key in from) {
          obj[key] = from[key];
        }
      }
    }

    return obj;
  };

  utils.proxy = function (fn, context) {
    var proxy = function () {
          return fn.apply(context, arguments);
        };

    return proxy;
  };

  utils.getParameters = function (source) {
    source = source || window.location.search.slice(1);

    if (!source) {
      return null;
    }

    var vars = {}, hash,
        hashes = source.split('&');

    hashes.forEach(function (h) {
      hash = h.split('=');
      vars[hash[0]] = window.decodeURIComponent(hash[1]);
    });
    
    return vars;
  };

  utils.getParameterByName = function (name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.search);
    if (results === null) {
      return "";
    } else {
      return decodeURIComponent(results[1].replace(/\+/g, " "));
    }
  };
  
  
  utils.createCookie = function(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
  }

  utils.readCookie = function(name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;
  }

  utils.eraseCookie = function(name) {
		createCookie(name,"",-1);
  }  
  
  // create dummy console if there's no real one
  if (!window.console) {
    window.console = {
    	      log: function () {},
    	      debug: function () {},
    	      error: function () {}
    };
  }

  /**
   * Discovers a function/attribute under the fully qualified path
   *
   * @param {string} The fully qualified path of the object/function
   * @param {string} Optional, the starting container
   *
   * @return {object|null} The discovered member
   */
  utils.discover = function (fqpath, container) {
    var ns = fqpath.split('.'),
        o = container || window,
        i, len;

    for (i = 0, len = ns.length; i < len; i++) {
        o = o[ns[i]] || null;
        if(!o){
          break;
        }
    }

    return o;
  };

  utils.register("modules.common", "utils", utils, fd);

}(FreshDirect));

