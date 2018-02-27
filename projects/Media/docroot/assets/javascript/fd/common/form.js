var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var forms = {
        selector: '[fdform]',
        attrPrefix: 'fdform',
        validators: [],
        formatters: {}
      },
      utils = fd.modules.common.utils,
      $ = fd.libs.$,
      DISPATCHER = fd.common.dispatcher;

  forms.register = function (form) {
    utils.register("forms", form.id, form, fd);
  };

  forms.registerValidator = function (selector, validator) {
    this.validators.push({selector: selector, validator: validator});
  };

  forms.registerFormatter = function (name, formatter) {
    this.formatters[name] = formatter;
  };

  forms.get = function (id) {
    return (fd.forms && fd.forms[id]) || {id: id};
  };

  forms.getEl = function (id) {
    return $('['+this.attrPrefix+'="'+id+'"]');
  };

  // return validation errors
  forms.validate = function (form, silent) {
    var errors = [];

    if (!silent) {
      forms.clearErrors(form);
    }

    if (form.validate) {
      errors = errors.concat(form.validate());
    } else {
      errors = errors.concat(forms.validateDefault(form));
    }

    // mark fields
    if (errors && !silent) {
      this.showErrors(form, errors);
    }

    return errors;
  };

  forms.getValidators = function (field, form) {
    var $field = $(field),
        validators = form.validators ? form.validators.concat(this.validators) : this.validators;

    validators = validators.filter(function (v) {
      return $field.is(v.selector);
    });

    return validators;
  };

  forms.validateDefault = function (form) {
    var errors = [],
        $form = this.getEl(form.id);

    $form.find('input, select, textarea').each(function (i, el) {
      errors = errors.concat(this.validateField($(el), form));
    }.bind(this));

    return errors;
  };

  forms.validateField = function (field, form, validators) {
    var errors = [];

    validators = validators || forms.getValidators(field, form);

    validators.forEach(function (validator) {
      errors = errors.concat(validator.validator(field, form));
    }, this);

    return errors;
  };

  forms.validateInputField = function (e) {
    var $el = $(e.currentTarget),
        id = $el.parents(this.selector).first().attr(this.attrPrefix),
        form = this.get(id),
        errors = [],
        validators = this.getValidators($el, form);

    if (validators.length) {
      this.clearFieldErrors($el, form);
      errors = errors.concat(this.validateField($el, form, validators));
    }

    if (errors.length) {
      this.showErrors(form, errors);
    } else {
      setTimeout(function () {
        this.validateFieldAjax($el, form);
      }.bind(this), 10);
    }

    return errors;
  };

  forms.keyDownHandler = function (e) {
    var $el = $(e.currentTarget),
        id = $el.parents(this.selector).first().attr(this.attrPrefix),
        form = this.get(id),
        formEl = this.getEl(form.id);

    if (e.keyCode === 13) {
      // submit on enter
      e.preventDefault();
      e.stopPropagation();

      formEl.submit();
    }
  };

  forms.clearErrors = function (form) {
    var $form = this.getEl(form.id);

    $form.find('['+this.attrPrefix+'-error]').remove();
    $form.find('[invalid]').attr('invalid', null);
  };

  forms.clearFieldErrors = function ($el, form, name) {
    var $form = this.getEl(form.id);

    if (name) {
      $('['+this.attrPrefix+'-error="'+name+'"]').remove();
    }
    $form.find('['+this.attrPrefix+'-error="'+$el.attr('name')+'"]').remove();
    $el.attr('invalid', null);
  };

  forms.showErrors = function (form, errors, clearFields) {
    errors.forEach(function (error) {
      if (error) {
        this.showError(form, error.name, error.error, error.field || error.el, error.errorid, clearFields);
      }
    }, this);
  };

  forms.showError = function (form, name, error, el, errorid, clearField) {
    var $el = el ? $(el) : $('['+this.attrPrefix+'="'+form.id+'"] [name="'+name+'"]').first(),
        $errorHolder = $('['+this.attrPrefix+'="'+form.id+'"] ['+this.attrPrefix+'-error-for="'+name+'"]').first(),
        pfx = this.attrPrefix,
        customHandler = form.errorHandlers && form.errorHandlers[name],
        $parent = $errorHolder.size() ? $errorHolder : $el.parent();

    // if custom error handler found
    if (customHandler) {
      // if custom error handler returns 'true', then terminate further error handling
      if (customHandler(form, name, error)) {
        return;
      }
    }

    if ($el.size() === 0) {
      $el = $('['+this.attrPrefix+'-error-container="'+form.id+'"],['+this.attrPrefix+'="'+form.id+'"] ['+this.attrPrefix+'-error-container]');
      $parent = $el;
    }

    if (clearField) {
      this.clearFieldErrors($el, form, name);
    }

    setTimeout(function () {
        var hashName = utils.createHash(name + error);
        if ($el.attr('invalid')) {
          if ((!errorid || $('['+pfx+'-errorid="'+errorid+'"]').length === 0) && $('['+pfx+'-errorhash="'+hashName+'"]').length === 0) {
              if ($el.closest('form').attr(pfx+'-displayerrorafter') !== undefined || $el.attr(pfx+'-displayerrorafter') !== undefined) {
                  var $temp = $parent;
                  if ($el.attr(pfx+'-displayerrorafterselector') !== undefined ) {
                      $temp = $($el.attr(pfx+'-displayerrorafterselector'));
                      if (!$temp.length) {
                          $temp = $parent;
                      }
                  }
                  if ($temp.hasClass('select-wrapper')) { /* use after because selects use ::after pseudo-element */
                      $temp.after('<span '+pfx+'-error="'+name+'" '+(errorid ? pfx+'-errorid="'+errorid+'"': '') +pfx+'-errorhash="'+hashName+'" '+'>'+error+'</span>');
                  } else { /* default append, not safe for elems that user ::after */
                      $temp.append('<span '+pfx+'-error="'+name+'" '+(errorid ? pfx+'-errorid="'+errorid+'"': '') +pfx+'-errorhash="'+hashName+'" '+'>'+error+'</span>');
                  }
              } else {
                  $parent.prepend('<span '+pfx+'-error="'+name+'" '+(errorid ? pfx+'-errorid="'+errorid+'"': '') +pfx+'-errorhash="'+hashName+'" '+'>'+error+'</span>');
              }
          }
        }
      }, 100);

    $el.attr('invalid', true);
  };

  forms.serializeForm = function (form) {
    var data = {},
        dataArray = $(form).serializeArray();

    dataArray.forEach(function (d) {
      if (data[d.name]) {
        if (!data[d.name].push) {
          data[d.name] = [data[d.name]];
        }
        data[d.name].push(d.value);
      } else {
        data[d.name] = d.value || '';
      }
    });

    return data;
  };

  forms.fillForm = function (form, data) {
    // TODO: select/checkbox/radiobutton support
    var $form = $(form);

    Object.keys(data).forEach(function (key) {
      var value = data[key];

      $form.find('input[name="'+key+'"]').val(value);
    });

    return data;
  };

  forms.serialize = function (id) {
    var form = this.get(id),
        $form = this.getEl(id),
        customSerialize = (form && form.serialize) || utils.discover($form.attr(this.attrPrefix+'-serialize'));

    return customSerialize ? customSerialize($form) : forms.serializeForm($form);
  };

  forms.fill = function (id, data) {
    var form = this.get(id),
        $form = this.getEl(id),
        customFill = (form && form.fill) || utils.discover($form.attr(this.attrPrefix+'-fill'));

    return customFill ? customFill($form, data) : forms.fillForm($form, data);
  };

  forms.getAjaxEndpoint = function (id, type) {
    var form = this.get(id),
        endpoint = (form && form[type+'Endpoint']) || this.getEl(id).attr(this.attrPrefix+'-endpoint-'+type);

    return endpoint;
  };

  forms.submitAjax = function (id) {
    var data = this.serialize(id),
        endpoint = this.getAjaxEndpoint(id, 'submit');

    if (endpoint) {
      DISPATCHER.signal('server', {
        url: endpoint,
        method: 'POST',
        data: {
          data: JSON.stringify({
            fdform: id,
            formdata: data
          })
        }
      });
    }

    return endpoint;
  };

  forms.validateFieldAjax = function (field, form) {
    var data = this.serialize(form.id),
        endpoint = this.getAjaxEndpoint(form.id, 'validator');

    if (endpoint) {
      DISPATCHER.signal('server', {
        url: endpoint,
        method: 'POST',
        data: {
          data: JSON.stringify({
            fdform: form.id,
            formdata: data,
            edited: field ? $(field).attr('name') : ''
          })
        }
      });
    }

    return endpoint;
  };

  forms.submitForm = function (e) {
    var $ct = $(e.currentTarget),
        id = $ct.attr(this.attrPrefix),
        form = this.get(id),
        formEl = this.getEl(form.id),
        submitFunc = (form && form.submit) || utils.discover(this.getEl(id).attr(this.attrPrefix+'-submit')),
        errors = this.validate(form);

    e.form = form;
    e.formEl = formEl;

    // validation
    if (errors.length) {
      // has validation errors
      e.preventDefault();
      return;
    }

    // try AJAX submit, if it fails then check custom submit function, or use default submit
    if (this.submitAjax(id)) {
      forms.lockFormResubmit(formEl);
      e.preventDefault();
    } else {
      if (submitFunc) {
        forms.lockFormResubmit(formEl);
        submitFunc(e);
        e.preventDefault();
      } else {
        console.log('FDform ('+id+') not found or has no submit method, using default submit.');
      }
    }
  };

  forms.lockFormResubmit = function(formEl){
      var disableResubmit = $(formEl).attr(forms.attrPrefix+'-disable-resubmit'),
          disableResubmitSelector = disableResubmit && $(formEl).attr(forms.attrPrefix+'-disable-resubmit-selector') || '[type=submit]';

      if(disableResubmit){
        $(formEl).attr('fdform-locked', 'true');
        $(formEl).find(disableResubmitSelector).prop('disabled', true);
      }
  };

  forms.releaseLockFormResubmit = function(formEl){
      var disableResubmit = $(formEl).attr(forms.attrPrefix+'-disable-resubmit'),
          disableResubmitSelector = disableResubmit && $(formEl).attr(forms.attrPrefix+'-disable-resubmit-selector') || '[type=submit]';

      if(disableResubmit){
          $(formEl).removeAttr('fdform-locked');
          $(formEl).find(disableResubmitSelector).prop('disabled', false);
      }
  };

  forms.hasManualLockFormResubmitRelease = function(formEl){
      var manualRelease = $(formEl).attr(forms.attrPrefix+'-disable-resubmit-release');
      return (manualRelease && manualRelease === 'manual');
  };

  forms.resetForm = function (e) {
    var $ct = $(e.currentTarget),
        id = $ct.attr(this.attrPrefix),
        form = this.get(id),
        resetFunc = (form && form.reset) || utils.discover(this.getEl(id).attr(this.attrPrefix+'-reset'));

    this.clearErrors({id: id});

    if (resetFunc) {
      resetFunc(e);
      e.preventDefault();
    } else {
      console.log('FDform ('+id+') not found or has no reset method, using default reset.');
    }
  };

  forms.action = function (form, action, e, preventManually) {
    var func,
        formEl = this.getEl(form.id),
        actionAttr = formEl.attr(this.attrPrefix+'-action-'+action);

    if (form && action) {
      if (action === "submit") {
        setTimeout(function () { formEl.submit(); }, 10);
        if (!preventManually && e.preventDefault) {
          e.preventDefault();
        }
      } else {
        func = form[action] || (actionAttr && utils.discover(actionAttr));
        if (func) {
          e = e || {};
          e.form = form;
          e.formEl = formEl;
          setTimeout(function () { func(e); }, 10);
          if (!preventManually && e.preventDefault) {
            e.preventDefault();
          }
        }
      }
    }
  };

  forms.formatter = function (e, focused) {
    var $el = $(e.currentTarget),
        formatterName = $el.attr(this.attrPrefix+'-formatter'),
        formatter = this.formatters[formatterName] || utils.discover(formatterName);

    if (formatter) {
      formatter($el, focused, e);
    }
  };

  forms.focusedFormatter = function (e) {
    this.formatter(e, true);
  };

  forms.decorateFields = function (formEl) {
    $('input, select, textarea', formEl).each(function (i2, field) {
      var $el = $(field), type = $el.attr('type'),
          id, label, ph, name = $el.attr('name');

      if (type !== 'hidden' && !$el.attr('id') && name) {
        id = $(formEl).attr('fdform') + '_' + name;

        if (type === 'radio' || type === 'checkbox') {
          id = id + '_' + $el.attr('value');
        }

        $el.attr('id', id);

        // check for label
        if ($el) {
          label = $el.closest('label').first();
          if (label.size() > 0) {
            label.attr('for', id);
          } else {
            ph = $el.attr('placeholder');
            label = $('<label for="'+id+'" fdform-generated-label><span>'+(ph || name)+'</span></label>');
            label.insertBefore($el);
          }
        }
      }
    });
  };

  forms.initModule = function () {
    // prevent browsers validation
    $(document).on('submit', this.selector, this.submitForm.bind(this));
    $(document).on('reset', this.selector, this.resetForm.bind(this));
    $(document).on('click', '['+this.attrPrefix+'-button]', function (e) {
      var $ct = $(e.currentTarget),
          id = $ct.parents(this.selector).first().attr(this.attrPrefix),
          form = this.get(id),
          prevent = $ct.attr(this.attrPrefix+'-prevent'),
          preventManual = prevent && prevent === 'manual';

      this.action(form, $ct.attr(this.attrPrefix+'-button'), e, preventManual);
    }.bind(this));

    // TODO check if 'blur' is needed
    $(document).on('change', this.selector+' input, '+this.selector+' select, '+this.selector+' textarea', this.validateInputField.bind(this));
    $(document).on('keydown', this.selector+' input, '+this.selector+' select', this.keyDownHandler.bind(this));
    // if this is andriod, bind to textInput event also.
    var focusedBindEvents = fd && fd.mobWeb && navigator.userAgent.toLowerCase().indexOf('android') !== -1? 'paste keydown textInput focus' : 'paste keydown focus';
    $(document).on(focusedBindEvents, '['+this.attrPrefix+'-formatter]', this.focusedFormatter.bind(this));
    $(document).on('change', '['+this.attrPrefix+'-formatter]', this.formatter.bind(this));

    // try to create unique ids for fields that are missing it
    $(this.selector).each(function (i, form) {
      forms.decorateFields(form);
    });
  };

  // listeners for validationErrors and submitSucceeded
  var formValidationListener = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'validationResult'
    },
    callback: {
      value: function (data) {
        var id = data.fdform,
            form = forms.get(id);

        forms.showErrors(form, data.errors, true);

        if (data.result) {
          Object.keys(data.result).forEach(function (key) {
            DISPATCHER.signal(key, data.result[key]);
          });
        }

      }
    }
  });

  formValidationListener.listen();

  var formSubmitListener = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'submitForm'
    },
    callback: {
      value: function (data) {
        var id = data.fdform,
            form = forms.get(id),
            successFunc = (form && form.success) || utils.discover(forms.getEl(id).attr(forms.attrPrefix+'-success')),
            failureFunc = (form && form.failure) || utils.discover(forms.getEl(id).attr(forms.attrPrefix+'-failure'));

        if (data.success && successFunc) {
          successFunc(id, data.result);
        }

        if(!data.success && failureFunc){
          failureFunc(id, data.result);
        }

        if (data.result) {
          Object.keys(data.result).forEach(function (key) {
            DISPATCHER.signal(key, data.result[key]);
          });
        }

        if(!forms.hasManualLockFormResubmitRelease(forms.getEl(id))){
          forms.releaseLockFormResubmit(forms.getEl(id));
        }
      }
    }
  });

  formSubmitListener.listen();

  // listener for form fill signals
  var formFillListener = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'fillForm'
    },
    callback: {
      value: function (data) {
        forms.fill(data.fdform, data.data);
      }
    }
  });

  formFillListener.listen();

  // basic submit functions
  forms.submitFns = {};
  forms.submitFns.defaultButton = function (e) {
    var formEl = $(e.currentTarget);

    formEl.find('[fdform-default-button]').click();
  };

  // basic success functions
  forms.successFns = {};
  forms.successFns.reset = function (id) {
    var formEl = forms.getEl(id);

    if (formEl.size()) {
      formEl[0].reset();
    }
  };
  forms.successFns.hideOnSuccess = function (id) {
    var formEl = forms.getEl(id);

    $('[fdform-container="'+id+'"]').hide();
    $('[fdform-container-'+id+']').hide();

    if (formEl.size()) {
      formEl[0].reset();
    }
  };
  forms.successFns.showOnSuccess = function (id) {
    var formEl = forms.getEl(id);

    $('[fdform-container="'+id+'"]').show();
    $('[fdform-container-'+id+']').show();

    if (formEl.size()) {
      formEl[0].reset();
    }
  };


  // default validators

  var addError = function (errors, field, msg, errorid) {
    errors.push({
      field: field,
      name: $(field).attr('name'),
      error: msg,
      errorid: errorid
    });

    return errors;
  };

  // required validator
  forms.registerValidator('[required],[fdform-v-required]', function (field) {
    var errors = [],
        $field = $(field),
        placeholder = $field.attr('fdform-fieldname') || $field.attr('placeholder'),
        fieldname = placeholder ? '<i>'+placeholder+'</i>' : 'This information';

    if (($field.is(':checkbox') && !$field.is(':checked')) || !$field.val()) {
      errors = addError(errors, field, fieldname+' is required!');
    }

    return errors;
  });

  // one is required validator
  forms.registerValidator('[fdform-v-onerequired]', function (field) {
    var errors = [],
        $field = $(field),
        group = $field.attr('fdform-v-onerequired'),
        $form = $field.closest(forms.selector),
        formid = $form.attr(forms.attrPrefix),
        $fields = $form.find('[fdform-v-onerequired="'+group+'"]'),
        $checkedfields = $form.find('[fdform-v-onerequired="'+group+'"]:checked');

    // clear errors
    $fields.each(function (i, el) {
      this.clearFieldErrors($(el), {id: formid});
    }.bind(forms));

    if ($checkedfields.size() === 0) {
      errors = addError(errors, $fields.first(), 'Please select one of the marked options!', "onerequired-"+group);
      $fields.attr('invalid', true);
    }

    return errors;
  });

  // phone number validator
  forms.registerValidator('[fdform-formatter="phone"]', function (field) {
    var errors = [],
        $field = $(field),
        val = $field.val(),
        number = val.replace(/[\(\)\s-]/g, "");

    if (val && number !== +number+'') {
      errors = addError(errors, field, 'Valid phone number required.');
    }

    return errors;
  });

  // zip code validator
  forms.registerValidator('[fdform-v-zipcode]', function (field) {
    var errors = [],
        $field = $(field),
        zip = $field.val();

    if (zip.length !== 5 || '1'+zip !== +('1'+zip)+'') {
      errors = addError(errors, field, 'Please provide valid ZIP code!');
    }

    return errors;
  });

//apartment validator
  forms.registerValidator('[fdform-v-apartment]', function (field) {
    var errors = [],
        $field = $(field),
        apt = $field.val();

    if (apt!==null && apt!=="" && apt.length > 20 ) {
      errors = addError(errors, field, 'Please provide valid apartment!');
    }

    return errors;
  });

//floor validator
  forms.registerValidator('[fdform-v-floor]', function (field) {
    var errors = [],
        $field = $(field),
        apt = $field.val();

    if (apt!==null && apt!=="" && apt.length > 20 ) {
      errors = addError(errors, field, 'Please provide valid floor/suite!');
    }

    return errors;
  });

//street addess 1 validator
  forms.registerValidator('[fdform-v-address1]', function (field) {
    var errors = [],
        $field = $(field),
        address1 = $field.val();

    if (address1!==null && address1!=="" && address1.length > 50 ) {
      errors = addError(errors, field, 'Please provide valid street address!');
    }

    return errors;
  });


  var checkPhoneInput = function (keyCode, e, mask, $el) {
	  var val = $el.val(),
	  	pos = $el.prop('selectionStart'),
	  	oldch, ch, newpos;
	  if (keyCode >= 48 && keyCode <= 57 && pos < mask.length) {
        // numbers
        ch = (keyCode - 48) + '';
        oldch = val.substr(pos, 1);

        for (newpos = pos; newpos < mask.length && oldch !== 'x' && oldch !== +oldch+''; newpos++) {
          oldch = val.substr(newpos, 1);
          pos = newpos;
        }

        if (newpos < mask.length) {
          var newValue = val.substr(0, pos) + ch + val.substr(pos+1);
	      $el.val(newValue);
          setTimeout(function () {
			$el[0].setSelectionRange(pos+1, pos+1);
	  	  });
        }

        e.preventDefault();
      } else if (keyCode >= 96 && keyCode <= 105 && pos < mask.length) {
        // keypad
        ch = (keyCode - 96) + '';
        oldch = val.substr(pos, 1);

        for (newpos = pos; newpos < mask.length && oldch !== 'x' && oldch !== +oldch+''; newpos++) {
          oldch = val.substr(newpos, 1);
          pos = newpos;
        }

        if (newpos < mask.length) {
          var newValue = val.substr(0, pos) + ch + val.substr(pos+1);
          $el.val(newValue);
          $el[0].setSelectionRange(pos+1, pos+1);
        }

        e.preventDefault();
      } else if (keyCode === 32 && pos < mask.length) {
        // Space
        $el[0].setSelectionRange(pos+1, pos+1);

        e.preventDefault();
      } else if (keyCode === 8 && pos > 0) {
        // BS
        $el.val(val.substr(0, pos-1) + mask.substr(pos-1, 1) + val.substr(pos));
        $el[0].setSelectionRange(pos-1, pos-1);

        e.preventDefault();
      } else if (keyCode === 37 || keyCode === 39 || keyCode === 9) {
        // cursors/tab, do nothing
      } else if (keyCode === 229){
        // Andriod specific event, unrecognizable key code
        setTimeout(function () {
        	$el[0].setSelectionRange(pos, pos);
        });
        e.preventDefault();
      } else {
        e.preventDefault();
      }
  }
  // default formatters
  var enableTextInputCheck = false;
  // phone number formatter
  forms.registerFormatter('phone', function ($el, focused, e) {
    var val = $el.val(),
    	pos = $el.prop('selectionStart'),
        mask = $el.attr('fdform-mask') || $el.prop('placeholder');

    if (!val || val.length < mask.length) {
      $el.val(mask);
      val = mask;
    }

    if (e.type === 'focusin' || e.type === 'focus') {
      pos = val.indexOf('x');

      if (pos > -1) {
        setTimeout(function () {
          $el[0].setSelectionRange(pos, pos);
        }, 0);
      }
    }

    if (e.type === 'paste') {
      e.preventDefault();
    }
    if (e.type === 'textInput' ) {
    	if (enableTextInputCheck) {
    		var textEntered = e.originalEvent && e.originalEvent.data;
    			// allow number only
	    		if (textEntered && textEntered < 10) {
	    			for (var i = 0; i < textEntered.length; i++) {
	    				var charCode = textEntered.charCodeAt(i);
	    				checkPhoneInput(charCode, e, mask, $el);
	    			}	
    			} else {
    				e.preventDefault();
    			}
		} else {
			e.preventDefault();
		}
    } else if (e.type === 'keydown') {
      checkPhoneInput(e.keyCode, e, mask, $el);
      enableTextInputCheck = e.keyCode === 229;
    }

  });

  // register in fd namespace
  utils.register("modules.common", "forms", forms, fd);

  forms.initModule();
}(FreshDirect));