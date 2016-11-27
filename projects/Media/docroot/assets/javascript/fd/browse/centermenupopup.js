/*global jQuery,common,browse*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var centermenupopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'centermenupopup'
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
    bodySelector:{
        value:'.browse-popup-content'
    },
    scrollCheck: {
        value:'.browse-popup'
    },
    template:{
        value:browse.centermenupopup
    },
    trigger: {
      value: '.menuBox button.centerpopup'
    },
    popupId: {
      value: 'centermenupopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        openonclick: true,
        overlayExtraClass: 'centerpopupoverlay',
        align: false
      }
    },
    close: {
        value: function () {
          if (this.popup) { this.popup.hide(); }

          return false;
        }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget),
            bt = function () {
              return $t.siblings('.popupMenuContent').html();
            },
            header = $t.attr('data-header') || this.headerContent;

        if (bt) {
          this.refreshBody({}, bt, header);
          this.popup.show($t);
          this.popup.clicked = true;

          // update header
          $('#'+this.popupId+' .browse-popup-header span').html(header);

          // replace popup input names
          $('#'+this.popupId+' input').each(function (i, el) { el.name = 'popup_'+el.name; });

          this.noscroll(true);
        }
      }
    }
  });

  centermenupopup.render();

  $(document).on('click', centermenupopup.trigger, centermenupopup.open.bind(centermenupopup));
  $(document).on('click', '#' + centermenupopup.popupId + ' .close', centermenupopup.close.bind(centermenupopup));
  $(document).on('click', '#' + centermenupopup.popupId + '.centerpopup-helper', function (e) {
    if ($(e.target).hasClass('centerpopup-helper')) {
      centermenupopup.close();
    } 
  });

  fd.modules.common.utils.register("modules.browse", "centermenupopup", centermenupopup, fd);

}(FreshDirect));

