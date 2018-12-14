var FreshDirect = window.FreshDirect || {};

(function(fd) {
    "use strict";

    var form = {};
    var $ = fd.libs.$;
    
    form.validate = function() {
        var submitButton = $('form[fdform-disabled-if-missing-required] button[type="submit"]');
        submitButton.prop('disabled', true);
        var requiredFields = $('form[fdform-disabled-if-missing-required] [required]');
        var emptyFields = $.map(requiredFields, function(el) {
           if(el.value.length === 0) {
               return el;
           }
        })
        if (emptyFields.length > 0) {
            submitButton.prop('disabled', true);
        } else {
            submitButton.prop('disabled', false);
        }
    };


    form.initModule = function() {
        $(document).on('change input',
            'form[fdform-disabled-if-missing-required] input[required], form[fdform-disabled-if-missing-required] select[required], form[fdform-disabled-if-missing-required] textarea[required]', 
            this.validate.bind(this)
            ).bind(this);

        $(document).on('reset', 'form[fdform-disabled-if-missing-required]', function() {
            setTimeout(form.validate.bind(form), 10);
          }).bind(this);
    }
    // Initializing module
    form.validate();
    form.initModule();

    fd.utils.register("modules.common", "formDisabledIfMissingRequired", form, fd);
})(FreshDirect);