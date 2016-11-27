/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var menupopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'menupopup'
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
    template:{
        value:common.menupopup
    },
    bodySelector:{
        value:'.browse-popup-content'
    },
    trigger: {
      value: '.menuBox button.popup'
    },
    popupId: {
      value: 'menupopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        ghostZIndex: 2010,
        halign: 'left',
        delay: 300,
        stayonghostclick: true,
        lateplaceholder: true
      }
    },
    close: {
        value: function () {
          if (this.popup) {
            this.popup.hide();
            this.popup.clearDelay();
          }

          return false;
        }
    },
    open: {
      value: function (e, stay) {
        var $t = $(e.currentTarget),
            bt = function () {
              return $t.siblings('.popupMenuContent').html();
            },
            header = $t.attr('data-header') || this.headerContent;

        if (bt) {
          this.refreshBody({}, bt, header);
          this.popup.showWithDelay($t, null);

          if (stay) {
            this.popup.clicked = true;
          }

          // replace popup input names
          $('#'+this.popupId+' input').each(function (i, el) { el.name = 'popup_'+el.name; });
        }
      }
    }
  });

  menupopup.render();

  $(document).on('mouseover', menupopup.trigger, menupopup.open.bind(menupopup));
  $(document).on('keyup', menupopup.trigger, function (e) {
    if (e.keyCode === fd.utils.keyCode.SPACE) {
      menupopup.open(e, true);
      e.stopPropagation();
      e.preventDefault();
    }
  });
  $(document).on('mouseout', menupopup.trigger, function () {
    if (menupopup.popup) {
      menupopup.popup.clearDelay();
    }
  });
  $(document).on('click', '#' + menupopup.popupId + ' .close', menupopup.close.bind(menupopup));

  fd.modules.common.utils.register("components", "menupopup", menupopup, fd);

}(FreshDirect));

