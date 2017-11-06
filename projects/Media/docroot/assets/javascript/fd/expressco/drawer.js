/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget,
      DISPATCHER = fd.common.dispatcher;

  // TODO: refactor into a general drawer widget?
  var drawer = Object.create(WIDGET,{
    signal: {
      value: 'drawer'
    },
    template: {
      value: expressco.drawer
    },
    placeholder: {
      value: '#ec-drawer'
    },
    drawerId: {
      value: 'ec-drawer'
    },
    changeTrigger: {
      value: '[data-drawer-id] .change,[data-drawer-activate]'
    },
    cancelTrigger: {
      value: '[data-drawer-id] .cancel,[data-drawer-active] [data-drawer-disabled],[data-drawer-active] [data-drawer-id]'
    },
    activate: {
      value: function (id) {
        if ($('[data-drawer-id="'+id+'"][data-drawer-locked]').size() === 0) {
          $(document.body).attr('data-drawer-active', id);
          $('[data-drawer-content="'+id+'"]').focus();
        }
      }
    },
    reset: {
      value: function (config) {
        var active = $(document.body).attr('data-drawer-active');
        config = config || {};

        if (active) {
          try {
            $('[data-drawer-id="'+active+'"]').focus();
          } catch (e) {}
        }
        $(document.body).attr('data-drawer-active', null);
        $("#ec-drawer").trigger("drawer-reset");

        if (!config.noEvent) {
          DISPATCHER.signal('ec-drawer-reset', {active: active});
        }
      }
    },
    cancel: {
      value: function () {
        var active = $(document.body).attr('data-drawer-active');
        this.reset({noEvent: true});
        DISPATCHER.signal('ec-drawer-cancel', {active: active});
      }
    },
    changeClick: {
      value: function (e) {
        var ct = $(e.currentTarget),
            target = ct.attr('data-drawer-activate') || ct.parent().attr('data-drawer-id');

        DISPATCHER.signal('ec-drawer-click', {target: target});

        this.activate(target);
      }
    },
    lock: {
      value: function (id) {
        $('[data-drawer-id="'+id+'"]').attr('data-drawer-locked', true);
        $('[data-drawer-content="'+id+'"]').attr('data-drawer-locked', true);
        $('[data-drawer-default-content="'+id+'"]').attr('data-drawer-locked', true);
      }
    },
    unlock: {
      value: function (id) {
        $('[data-drawer-id="'+id+'"]').attr('data-drawer-locked', null);
        $('[data-drawer-content="'+id+'"]').attr('data-drawer-locked', null);
        $('[data-drawer-default-content="'+id+'"]').attr('data-drawer-locked', null);
      }
    },
    init: {
      value: function () {
        $('[data-drawer-content-prerender="'+this.drawerId+'"] [data-drawer-default-content]').each(function (i, el) {
          var $el = $(el),
              cid = $el.attr('data-drawer-default-content');

          $('[drawer-id="'+this.drawerId+'"] [data-drawer-default-content="'+cid+'"]').html($el.html());
        }.bind(this));
        $('[data-drawer-content-prerender="'+this.drawerId+'"] [data-drawer-content]').each(function (i, el) {
          var $el = $(el),
              cid = $el.attr('data-drawer-content');

          $('[drawer-id="'+this.drawerId+'"] [data-drawer-content="'+cid+'"]').html($el.html());
        }.bind(this));
      }
    }
  });

  drawer.listen();
  drawer.init();

  $(document).on('click', drawer.changeTrigger, drawer.changeClick.bind(drawer));
  $(document).on('click', drawer.cancelTrigger, drawer.cancel.bind(drawer));
  $(document).on('keydown', '[data-drawer-content]', function (e) {
    if (e.keyCode === fd.utils.keyCode.ESC) {
      drawer.reset();
    }
  });
  $(document).on('click', function (e) {
    var $el = $(e.target);

    if ($el.closest('[data-component="drawer"],.ec-popup,.qs-popup,.popupcontentoverlay,.centerpopup-helper,#select2-soFreq2-results').size() === 0) {
      drawer.reset();
    }
  });

  // edit / delete buttons
  $(document).on('click', '[data-component="drawer"] [data-action]', function (e) {
    var $ct = $(e.currentTarget),
        $form = $ct.closest('[fdform]'),
        action = $ct.attr('data-action'),
        formid, endpoint, submitFn, $el, id;

    if ($form.size()) {
      formid = $form.attr('fdform');
      /* override endpoint with button attr */
      if ($ct.attr('fdform-endpoint-submit')) {
        endpoint = $ct.attr('fdform-endpoint-submit');
      } else {
        endpoint = fd.modules.common.forms.getAjaxEndpoint(formid, 'submit');
      }
      $el = $ct.parent().siblings('input');

      if ($el.size() === 0) {
        $el = $ct.parent().siblings('label').find('input');
      }
      id = $el.val();

      submitFn = function () {
        DISPATCHER.signal('server', {
          url: endpoint,
          method: 'POST',
          data: {
            data: JSON.stringify({
              fdform: formid+'_'+action,
              formdata: {
                action: action,
                id: id
              }
            })
          }
        });
      };

      if ($ct.is('[data-confirm]')) {
        fd.confirmFunctions = fd.confirmFunctions || {};
        fd.confirmFunctions[formid+'_'+action+'_'+id] = submitFn;
      } else {
        submitFn();
      }
    }
  });
  
  $(document).on('click', '.clickable,.fake-radio', function(e) {
	    $jq(this).closest('.drawer__item').find('[type="radio"]').click().focus();
	});

  $(document).on('keydown', "[data-drawer-id]", function(e){
    // make drawer open by enter key
    if (e.which === 13) {
      $(e.currentTarget).click();
      $(e.currentTarget).find(".change").click();
    }
  });

  fd.modules.common.utils.register("expressco", "drawer", drawer, fd);
}(FreshDirect));
