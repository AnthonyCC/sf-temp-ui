var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var gridlistchange = Object.create(fd.common.signalTarget,{
    allowNull: {
      value: true
    },
    signal: {
      value: 'items'
    },
    defaultViewType: {
      value: 'list'
    },
    getViewType: {
      value: function () {
        return fd.modules.common.utils.readCookie('viewtype') || this.defaultViewType;
      }
    },
    setViewType: {
      value: function (viewtype) {
        fd.modules.common.utils.createCookie('viewtype', viewtype);
        this.checkViewType();

        $(document.body).trigger('viewtypeChanged');
      }
    },
    checkViewType: {
      value: function () {
        // get viewtype from cookie
        var viewtype = this.getViewType();

        $('[data-component="GridListButton"]').attr('data-active', null);
        $('[data-component="GridListButton"][data-type="'+viewtype+'"]').attr('data-active', true);
        $(document.body).attr('data-viewtype', viewtype);
      }
    },
    callback:{
      value: function (data) {
        this.checkViewType();
      }
    }
  });
  gridlistchange.listen();
  gridlistchange.checkViewType();

  $(document).on('click', '[data-component="GridListButton"]', function (e) {
    var type = $(e.currentTarget).attr('data-type');

    gridlistchange.setViewType(type);
  });

  fd.modules.common.utils.register("quickshop.common", "gridlistchange", gridlistchange, fd);
}(FreshDirect));
