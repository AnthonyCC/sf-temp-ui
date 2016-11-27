var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var $ = fd.libs.$,
      DISPATCHER = fd.common.dispatcher;

  // form
  fd.modules.common.forms.register({
    id: "gogreen",
    goGreenEndpoint: "/api/expresscheckout/gogreen",
    submitgogreen: function (e) {
      var gogreen = $(e.formEl).find('input[name="goGreen"]').prop('checked');

      DISPATCHER.signal('server', {
        url: e.form.goGreenEndpoint,
        method: 'POST',
        data: {
          data: JSON.stringify({
            fdform: "gogreen",
            formdata: {
              goGreen: gogreen ? "Y" : "N"
            }
          })
        }
      });
    }
  });

}(FreshDirect));
