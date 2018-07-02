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
      data.action = 'setZipCode';
      DISPATCHER.signal('server', {
        url: '/api/locationhandler',
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
      if(typeof FreshDirect.zipCheck !== 'undefined' && FreshDirect.zipCheck == true){
    	  FreshDirect.components.zipCheckPopup.refresh({data: data, zipcheckFound: true});
    	  FreshDirect.zipCheck = false;
    	  $('.zipcheck').off();
    	  $('.zipcheck').on('click', '[zip-check-shop]', function() {
    		  DISPATCHER.signal('zipCheckSuccess', data);
    	  });
      } else {
    	  DISPATCHER.signal('zipCheckSuccess', data);
      }
    },
    failure: function () {
      if (FreshDirect.components.zipCheckPopup) {
        FreshDirect.components.zipCheckPopup.refresh({data: data, zipcheckNotify: true});
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
