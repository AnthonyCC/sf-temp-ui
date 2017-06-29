/*global jQuery, common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      zipCheck = Object.create(fd.modules.common.forms),
      data;

  zipCheck.register({
    id: 'zipcheck',
    submit: function (e) {
      data = zipCheck.serialize(e.form.id);
      DISPATCHER.signal('server', {
        url: '/api/locationhandling/user/ziphandling',
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
      DISPATCHER.signal('zipCheckSuccess', data);
    },
    failure: function () {
      if (FreshDirect.components.zipCheckPopup) {
        FreshDirect.components.zipCheckPopup.refreshBody(data,common.zipcheckNotify);
      }
    }
  });

  $(document).on('click', '[clear-zip-code]', function() {
    var inputField = $('input[name="zipCode"]')[0];
    inputField.value = '';
  });

  fd.modules.common.utils.register("common", "zipCheck", zipCheck, fd);

  FreshDirect.modules.common.forms.registerValidator('[name="zipCode"]', function (field) {
    var errors = [],
        val = $(field).val();

    if (!val.match(/\b\d{5}\b/)) {
      errors.push({
        field: field,
        name: 'zipCode',
        error: 'Invalid ZIP code.'
      });
    }

    return errors;
  });
}(FreshDirect));
