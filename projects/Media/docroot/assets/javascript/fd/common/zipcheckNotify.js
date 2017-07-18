/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      zipcheckNotify = Object.create(fd.modules.common.forms);

  zipcheckNotify.register({
    id: 'zipcheckNotify',
    submit : function (e) {
      var data = zipcheckNotify.serialize(e.form.id);
      DISPATCHER.signal('server', {
        url: '/api/locationhandling/user/zipnotification',
        method: 'POST',
        data: {
          data: JSON.stringify({
            fdform: e.form.id,
            formdata: data
          })
        }
      });
      $('.spinner-overlay').addClass('active');
    },
    success: function () {
      if (FreshDirect.components.zipCheckPopup) {
        FreshDirect.components.zipCheckPopup.refresh({data: null, zipcheckNotifyDone: true});
      }
    },
    failure : function (id, result) {
      $('.spinner-overlay').removeClass('active');
    }
  });

  fd.modules.common.utils.register("common", "zipcheckNotify", zipcheckNotify, fd);

  zipcheckNotify.registerValidator('[name="email"]', function (field) {
    var errors = [],
        val = $(field).val();

    if (!val.match(/.+@.+/)) {
      errors.push({
        field: field,
        name: 'email',
        error: 'Incomplete email address.'
      });
    }

    return errors;
  });
}(FreshDirect));
