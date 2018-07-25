/*global Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var DECORATE_BODY = true;
  var $ = fd.libs.$;

  var bus = new Bacon.Bus();

  var signal = function(to,body){
    try {
      // add metadata and abFeatures to body if not present
      if (DECORATE_BODY && body && typeof body === 'object') {
        body.metadata = body.metadata || fd.metaData;
        body.abFeatures = body.abFeatures || fd.features && fd.features.active;
        body.mobWeb = body.mobWeb || fd.mobWeb;
        body.dlvPassCart = body.dlvPassCart || ($('#deliverypasscontent').length > 0 && !!$('#deliverypasscontent').attr('data-dlvpasscart') == true) || false;
      }
      bus.push({
        to: to,
        body: body
      });
    } catch (e) {
      if (console && console.error) {
        console.error('failed to send signal: ', to, body, e.stack);
      }
    }
  };

  var dispatcher = {
    bus: bus,
    signal: signal,
    value: bus.toProperty()
  };

  // common way to send signal with any element
  $(document).on('click', '[fd-signal]', function (e) {
    var el = $(e.target).closest('[fd-signal]')[0],
        signalName = el && el.getAttribute('fd-signal'),
        params = el && el.getAttribute('fd-signal-params'),
        paramEl = el && el.querySelector('[fd-signal-json-params]'),
        preventDefault = el && el.hasAttribute('fd-signal-prevent-default');

    if (!el) {
      return;
    }

    if (preventDefault) {
      e.preventDefault();
    }

    if (params) {
      params = params.split(';').reduce(function (p, c) {
        var kv = (""+c || "dummy").split(':');

        p[kv[0]] = kv[1] || true;

        return p;
      }, {});
    }

    if (paramEl) {
      try {
        params = JSON.parse(JSON.stringify(paramEl.textContent));
      } catch (e) {}
    }

    params = params || {};

    // send signal
    signal(signalName, params);
  });
  
  if (fd.modules && fd.modules.common && fd.modules.common.utils) {
    fd.modules.common.utils.register("common", "dispatcher", dispatcher, fd);
  } else {
    fd.modules = fd.modules || {};
	fd.modules.common = fd.modules.common || {};
	fd.modules.common.dispatcher = dispatcher;
  }
}(FreshDirect));
