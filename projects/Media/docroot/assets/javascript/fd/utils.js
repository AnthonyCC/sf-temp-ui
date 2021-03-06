var FreshDirect = FreshDirect || {};

(function(fd) {
  "use strict";

  var utils = {};

  /**
   * Create the given namespace
   *
   * @param {string} namespace the namespace as dotted path
   * @param {Object} container the root of the namespace (optional)
   */
  utils.mknamespace = function(namespace, container) {
    var ns = namespace.split("."),
      o = container || window,
      i,
      len;

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
   * @param {string} feature feature for module (optional)
   * @param {string} version feature version for module (optional)
   */
  utils.register = function(namespace, name, obj, root, feature, version) {
    var ns = utils.mknamespace(namespace, root),
      oldModule = ns[name],
      isActive = utils.isActive(feature, version);

    if (!oldModule || !feature || isActive) {
      ns[name] = obj;

      if (oldModule && oldModule._versions) {
        ns[name]._versions = oldModule._versions;
        delete oldModule._versions;
      }
    }

    if (feature && version) {
      ns[name]._versions = ns[name]._versions || {};
      ns[name]._versions[feature + ":" + version] = obj;
    }
  };

  utils.registerModule = function(
    namespace,
    name,
    obj,
    root,
    feature,
    version
  ) {
    if (typeof obj === "function") {
      obj = obj(root, feature, version);
    }

    utils.register(namespace, name, obj, root, feature, version);
  };

  utils.module = function(name, root, feature, version) {
    var ns = utils.mknamespace(name, root);

    if (ns._versions && feature && version) {
      ns = ns._versions[feature + ":" + version] || ns;
    }

    return ns;
  };

  utils.initModule = function(name, root, feature, version) {
    var module = utils.module(name, root, feature, version);

    if (module && module.initModule) {
      module.initModule(root, feature, version);
    }
  };

  utils.getActiveFeaturesFromCookie = function(cname) {
    var featureStr,
      features = {};

    cname = cname || "features";

    featureStr = utils.readCookie(cname);

    if (featureStr) {
      featureStr.split("|").forEach(function(f) {
        var splitF = f.split(":");

        features[splitF[0]] = splitF[1];
      });
    }

    return features;
  };

  utils.setActiveFeatures = function(features, cname) {
    var featureArr = [];

    cname = cname || "features";

    if (features) {
      Object.keys(features).forEach(function(k) {
        featureArr.push(k + ":" + features[k]);
      });
      utils.setCookie(cname, featureArr.join("|"));
    } else {
      utils.eraseCookie(cname);
    }
  };

  utils.getActiveFeatures = function() {
    return (
      (fd.features && fd.features.active) || utils.getActiveFeaturesFromCookie()
    );
  };

  utils.getActive = function(feature) {
    return utils.getActiveFeatures()[feature] || "default";
  };

  utils.isActive = function(feature, version) {
    return utils.getActive(feature) === version;
  };

  /**
   * Extends the given object with the other objects (shallow copy of properties)
   */
  utils.extend = function(obj) {
    var length = arguments.length,
      i = 1,
      key,
      from;

    if (obj === null || typeof obj !== "object" || length === i) {
      return obj;
    }

    for (; i < length; i++) {
      if ((from = arguments[i]) !== null) {
        for (key in from)
          if (from.hasOwnProperty(key)) {
            obj[key] = from[key];
          }
      }
    }

    return obj;
  };

  // deprecated, use .bind()
  utils.proxy = function(fn, context) {
    var proxy = function() {
      return fn.apply(context, arguments);
    };

    return proxy;
  };

  utils.getParameters = function(source) {
    source = source || window.location.search.slice(1);

    if (!source) {
      return null;
    }

    var vars = {},
      hash,
      hashes = source.split("&");

    hashes.forEach(function(h) {
      hash = h.split("=");
      vars[hash[0]] = window.decodeURIComponent(encodeURIComponent(hash[1]));
    });

    return vars;
  };

  utils.setParameters = function(originalParameter, newParameter) {
    var string = originalParameter;
    for (var i = 0; i < newParameter.length; ++i) {
      var key = Object.keys(newParameter[i])[0];
      string = string.concat("&", key, "=", newParameter[i][key]);
    }
    return string;
  };

  // deprecated use getParameters()[name] instead
  utils.getParameterByName = function(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.search);

    if (results === null) {
      return "";
    }

    return decodeURIComponent(
      encodeURIComponent(results[1]).replace(/\+/g, " ")
    );
  };

  utils.createCookie = function(name, value, days) {
    var date,
      expires = "";

    if (days) {
      date = new Date();
      date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
      expires = "; expires=" + date.toGMTString();
    }
    document.cookie = name + "=" + value + expires + "; path=/";
  };
  utils.setCookie = utils.createCookie;

  utils.readCookie = function(name) {
    var nameEQ = name + "=",
      ca = document.cookie.split(";"),
      i,
      c;

    for (i = 0; i < ca.length; i++) {
      c = ca[i];
      while (c.charAt(0) === " ") {
        c = c.substring(1, c.length);
      }
      if (c.indexOf(nameEQ) === 0) {
        return c.substring(nameEQ.length, c.length);
      }
    }
    return null;
  };

  utils.eraseCookie = function(name) {
    utils.createCookie(name, "", -1);
  };

  utils.isDeveloper = function() {
    return utils.readCookie("developer");
  };

  // create dummy console if there's no real one
  if (!window.console) {
    window.console = {
      log: function() {},
      debug: function() {},
      warn: function() {},
      error: function() {}
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
  utils.discover = function(fqpath, container) {
    var ns = (fqpath && fqpath.split(".")) || null,
      o = container || window,
      i,
      len;

    if (!ns) {
      return null;
    }

    for (i = 0, len = ns.length; i < len; i++) {
      o = o[ns[i]] || null;
      if (!o) {
        break;
      }
    }

    return o;
  };

  /**
   * Similar to discover(), checks for existence of nested Object attribute
   * except: 
   *   uses hasOwnProperty to verify
   *   returns a boolean instead of object|null
   * 
   * @param {string} propertyPath - Path to check for existence of
   * @param {string} [obj=window] - Object to search within
   * 
   * @return {boolean} checkObj contains propertyPath
   * 
   * @example
      FreshDirect.utils.hasOwnNestedProperty('utils', FreshDirect);
      //returns true

      FreshDirect.utils.hasOwnNestedProperty('FreshDirect.utils');
      //returns true, defaults to window

      FreshDirect.utils.hasOwnNestedProperty('FreshDirect.utils', FreshDirect);
      //returns false (expects FreshDirect.FreshDirect.utils)
   */
  utils.hasOwnNestedProperty = function(propertyPath, obj) {
    if (!propertyPath) return false;

    var obj = obj || window;

    var properties = propertyPath.split(".");

    for (var i = 0; i < properties.length; i++) {
      var prop = properties[i];

      if (!obj || !obj.hasOwnProperty(prop)) {
        return false;
      } else {
        obj = obj[prop];
      }
    }

    return true;
  };

  //hash generate
  utils.createHash = function(string) {
    var hash = 0,
      i = 0,
      char = 0;
    if (string.length === 0) return hash;
    for (i = 0; i < string.length; i++) {
      char = string.charCodeAt(i);
      hash = (hash << 5) - hash + char;
      hash = hash & hash; // Convert to 32bit integer
    }
    return hash;
  };

  utils.reloadOnSuccess = function(id) {
    window.location = window.location;
  };

  /* throttle events helper */
  utils.throttle = function(fn, threshold, scope) {
    threshold || (threshold = 250);
    var last, deferTimer;

    return function() {
      var context = scope || this;

      var now = +new Date(),
        args = arguments;
      if (last && now < last + threshold) {
        // hold on to it
        clearTimeout(deferTimer);
        deferTimer = setTimeout(function() {
          last = now;
          fn.apply(context, args);
        }, threshold);
      } else {
        last = now;
        fn.apply(context, args);
      }
    };
  };

  /* copy out util methods from USQ code */
  //USQLegalWarning.setCookie -- use createCookie
  //USQLegalWarning.getCookie -- use readCookie
  //USQLegalWarning.getJSessionId -- uses new marker (fd.user.sessionId)
  utils.getJSessionId = function() {
    return FreshDirect && FreshDirect.user && FreshDirect.user.sessionId
      ? FreshDirect.user.sessionId
      : "FD_NO_SESSION_ID";
  };

  /* alt code (from common_javascript.js):
   * $jq.QueryString["PARAM_TO_GETVALUE_OF"]
   * */
  utils.getQueryParameterByName = function(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.search);
    if (results == null) {
      return "";
    } else {
      return decodeURIComponent(
        encodeURIComponent(results[1]).replace(/\+/g, " ")
      );
    }
  };

  //USQLegalWarning.containsElement
  /* might be safer if this returned an empty array instead of null */
  utils.containsElement = function(parentElement, childElementName) {
    var children = [];
    children = parentElement.getElementsByTagName("*");

    for (var i = 0; i < children.length; i++) {
      var child = children.item(i);
      if (child.name == childElementName) {
        return child;
      }
    }
    return null;
  };

  // escape unsafe input
  utils.escapeHtml = function(unsafe) {
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  };

  // slugify - make a string "title-safe"
  utils.slugify = function(text) {
    return text
      .toString()
      .toLowerCase()
      .replace(/\s+/g, "-") // spaces to -
      .replace(/[^\w\-]+/g, "") // remove non-word chars
      .replace(/\-\-+/g, "-") // multiple - to single -
      .replace(/^-+/, "") // trim - from start
      .replace(/-+$/, ""); // trim - from end
  };

  utils.dateFormatter = function(timeStamp) {
    var date = new Date(timeStamp);
    var dateFormatter = {
      date: date
    };
    dateFormatter.zeroFill = function(integer) {
      return integer < 10 ? "0" + integer : "" + integer;
    };
    dateFormatter.shortDayName = function() {
      var dayNames = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
      dayNames[-1] = "Sun";
      return dayNames[date.getDay() - 1];
    };
    dateFormatter.shortTime = function() {
      var hour = date.getHours();
      var minutes = date.getMinutes();
      var postFix = "AM";
      if (hour >= 12) {
        hour -= 12;
        postFix = "PM";
      }
      if (hour === 0) {
        hour = 12;
      }
      return (
        hour + (minutes ? ":" + dateFormatter.zeroFill(minutes) : "") + postFix
      );
    };
    dateFormatter.shortDate = function() {
      return (
        dateFormatter.zeroFill(date.getMonth() + 1) +
        "/" +
        dateFormatter.zeroFill(date.getDate())
      );
    };
    dateFormatter.year = function() {
      return dateFormatter.date.getFullYear();
    };
    return dateFormatter;
  };

  // register utils under FreshDirect.modules.common.utils
  utils.register("modules.common", "utils", utils, fd);

  // register utils under FreshDirect.utils
  utils.register("FreshDirect", "utils", utils, window);

  // meaningful keyCode mappings
  utils.keyCode = {
    TAB: 9,
    ENTER: 13,
    SPACE: 32,
    ESC: 27,
    LEFT: 37,
    UP: 38,
    RIGHT: 39,
    DOWN: 40
  };
})(FreshDirect);
