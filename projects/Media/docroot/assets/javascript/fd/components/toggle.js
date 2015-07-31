var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var toggle = {
    PROP: 'fd-toggle',
    TRIGGER: 'fd-toggle-trigger',
    STATE: 'fd-toggle-state',
    toggle: function (e) {
      var toggleId = $(e.currentTarget).attr(this.TRIGGER),
          el = $('['+this.PROP+'='+toggleId+']'),
          state = this.get(el);

      if (state) {
        this.disable(el);
      } else {
        this.enable(el);
      }
    },
    enable: function (el) {
      $(el).attr(this.STATE, 'enabled');
    },
    disable: function (el) {
      $(el).attr(this.STATE, 'disabled');
    },
    get: function (el) {
      return $(el).attr(this.STATE) === 'enabled';
    }
  };

  $(document).on('click', '['+toggle.TRIGGER+']', toggle.toggle.bind(toggle));

  fd.modules.common.utils.register("components", "toggle", toggle, fd);
}(FreshDirect));
