var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var DISPATCHER = fd.common.dispatcher;

  var signalTarget = {
    signal: '',
    DISPATCHER: DISPATCHER,
    allowNull: false, // switch to accept signal with null body
    callback: function () {},
    listen: function() {
      var that = this;
      DISPATCHER.value.filter(function (value) {
        return ('to' in value) && (value.to === that.signal || (fd.libs.$.isArray(that.signal) && that.signal.indexOf(value.to) > -1)) && (that.allowNull || value.body !== null);
      }).onValue(function (value) {
        try {
          that.callback(value.body, value.to);
        } catch (e) {
          if (console && console.error) {
            console.error('failed to send signal: ', value && value.to, value && value.body, e.stack);
          }
        }
      });
    }
  };

  fd.modules.common.utils.register("common", "signalTarget", signalTarget, fd);
}(FreshDirect));
