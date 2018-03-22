/*global common*/
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  "use strict";

	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var customClass = null;
  var confirmpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'confirmpopup bubblepopup'
    },
    hasClose: {
      value: true
    },
    $trigger: {
      value: null
    },
    bodySelector:{
        value:'.qs-popup-content'
    },
    trigger: {
      value: '[data-confirm]'
    },
    popupId: {
      value: 'confirmpopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        ghostZIndex: 2010,
        halign: 'left',
        hidecallback: function() {
        	if (customClass) {
        		confirmpopup.popup.$el.removeClass(customClass);
            }
        }
      }
    },
    decorate: {
      value: function () {
        $(this.trigger).attr('role', 'alertdialog');
      }
    },
    buttonClick: {
      value: function (e) {
        var $button = $(e.currentTarget),
            btnName = $button.attr('data-button'),
            callback;

        if (this.$currentTrigger) {
          callback = fd.modules.common.utils.discover(this.$currentTrigger.attr('data-confirm-button-'+btnName));
          if (callback) {
            callback(this.$currentTrigger);
          }
        }

        this.close();
      }
    },
    close: {
      value: function () {
        if (this.popup) {
          this.popup.hide();
        }

        this.$currentTrigger = null;

        return false;
      }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget),
            data = $t.attr('data-confirm-data') && JSON.parse($t.attr('data-confirm-data')) || {},
            message = data.message || $t.attr('data-confirm-message'),
            header = (data.header || $t.attr('data-confirm-header') || this.headerContent) || (!message) && "Are you sure?",
            template = data.template || $t.attr('data-confirm-template'),
            process = data.process || $t.attr('data-confirm-process'),
            processFn = fd.modules.common.utils.discover(process),
            bt = fd.modules.common.utils.discover(template) || common.confirmpopup,
            hideBackground = $t.attr('data-hide-background') != null;
            
        customClass = $t.attr('data-confirm-class');
        this.$currentTrigger = $t;

        data.message = message || data.message;
        data.header = header;

        if (processFn) {
          data = processFn(data);
        }

        if (bt) {
          this.refreshBody(data, bt, data.header);
          this.popup.show($t, null);
          this.popup.clicked = true;
        }
        if (customClass) {
        	confirmpopup.popup.$el.addClass(customClass);
        }
        
        if (hideBackground) {
        	confirmpopup.popup.$overlay.css('opacity', '0.8');
        }
      }
    }
  });

  confirmpopup.render();

  $(document).on('click', confirmpopup.trigger, confirmpopup.open.bind(confirmpopup));
  $(document).on('click', '#' + confirmpopup.popupId + ' .close', confirmpopup.close.bind(confirmpopup));
  $(document).on('click', '#' + confirmpopup.popupId + ' [data-button]', confirmpopup.buttonClick.bind(confirmpopup));

  if (fd.modules && fd.modules.common && fd.modules.common.utils) {
	  fd.modules.common.utils.register("components", "confirmpopup", confirmpopup, fd);
  } else {
	  // self register
	  fd.components = fd.components || {};
	  fd.components.confirmpopup = confirmpopup;
  }

}(FreshDirect));
