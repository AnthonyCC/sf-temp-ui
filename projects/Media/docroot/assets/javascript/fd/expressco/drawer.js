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
      value: '.drawer__previewitem .change,[data-drawer-activate]'
    },
    cancelTrigger: {
        value: '[data-drawer-id] .cancel-button'
    },
    activate: {
      value: function (id) {
        if ($('[data-drawer-id="'+id+'"][data-drawer-locked]').size() === 0) {
          if($('[data-drawer-columns="1"]').length > 0){
       		  $('[data-drawer-columns="1"]').css("max-width", "none");
       	  }
          $(document.body).attr('data-drawer-active', id);
          DISPATCHER.signal(id + '-drawer-on-open');
          if (fd.mobWeb) {
        	  $('.drawer-row').removeClass('active').hide();
        	  $('.drawer-row.auto-height').removeClass('auto-height').addClass('auto-height-disabled');
        	  $('[data-drawer-default]').hide();
        	  $('[data-drawer-id="'+id+'"]').parents('.drawer-row').addClass('active').show();
          } else {
        	  $('[data-drawer-id]').removeClass('active').hide();
              $('[data-drawer-id="'+id+'"]').addClass('active').show();
              $('.cancel .cancel-button:visible').focus();
          }

          
		  $('#checkout-cart-header').hide();
        }
      }
    },
    reset: {
      value: function (config) {
        var active = $(document.body).attr('data-drawer-active');
        config = config || {};
        
        if($('[data-drawer-columns="1"]').length > 0){
    		$('[data-drawer-columns="1"]').css("max-width", "500px");
  	  	}
        if (active) {
          try {
            $('[data-drawer-id="'+active+'"]').focus();
          } catch (e) {}
        }
        $(document.body).attr('data-drawer-active', null);
        if(fd.mobWeb) {
      	  $('.drawer-row').removeClass('active').show();
      	  $('.drawer-row.auto-height-disabled').removeClass('auto-height-disabled').addClass('auto-height');
      	  $('[data-drawer-default]').show();
        } else {
        	$('[data-drawer-id]').removeClass('active').show();
        }
        
		$('#checkout-cart-header').show();
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
            target = ct.attr('data-drawer-activate') || ct.parent().attr('data-drawer-id') ||ct.parents('[data-drawer-default-content]').attr('data-drawer-default-content');

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
  $(document).on('keydown', '[data-drawer-content], .drawer-header .active', function (e) {
    if (e.keyCode === fd.utils.keyCode.ESC) {
      drawer.reset();
    }
  });
  $(document).on('click', function (e) {
    var $el = $(e.target);

    if ($el.closest('[data-component="drawer"],.ec-popup,.qs-popup,.popupcontentoverlay,.centerpopup-helper,#select2-soFreq2-results,.ui-selectmenu-menu').size() === 0) {
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
    	  $el = $ct.closest('.drawer__item').find('input[name="id"]');
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
  $(document).on('click', '[data-drawer-id] .close-button', function(e) {
	  var drawerId = $jq(this).parents('[data-drawer-id]').data('drawer-id');
	  $jq('[data-drawer-content="' + drawerId + '"] form').submit();
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
