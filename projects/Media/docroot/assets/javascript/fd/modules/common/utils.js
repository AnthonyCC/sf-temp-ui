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

  utils.register("modules.common", "utils", utils, fd);

}(FreshDirect));

