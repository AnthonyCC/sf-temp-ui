/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var helppopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: 'help'
    },
    customClass: {
      value: 'helppopup'
    },
    hideHelp: {
      value: true
    },
    hasClose: {
      value: true
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: 'button.qs-popup-help-icon'
    },
    popupId: {
      value: 'helppopup'
    },
    popupConfig: {
      value: {
        zIndex: 3000,
        openonclick: true,
        halign: 'right'
      }
    },
    close: {
        value: function () {
          this.popup.hide();

          return false;
        }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget),
            bt = $t.attr('data-bodytemplate'),
            header = $t.attr('data-header') || this.headerContent;

        if (bt) {
          this.refreshBody({}, bt, header);
          this.popup.show($t);
          this.popup.clicked = true;
        }
      }
    }
  });

  helppopup.render();

  $(document).on('click', helppopup.trigger, helppopup.open.bind(helppopup));
  $(document).on('click', '#' + helppopup.popupId + ' .close', helppopup.close.bind(helppopup));

  fd.modules.common.utils.register("modules.common", "helppopup", helppopup, fd);

}(FreshDirect));

