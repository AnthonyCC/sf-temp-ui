var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var readMore = {
    PROP: 'fd-readmore',
    TRIGGER: 'fd-readmore-trigger',
    STATE: 'fd-readmore-state',
    ONOPEN: 'fd-readmore-open',
    ONCLOSE: 'fd-readmore-close',
    TRUNCATE: 'fd-readmore-truncate',
    toggle: function (e) {
      var $el = $(e.currentTarget),
          $rmEl = $el.closest('['+readMore.PROP+']'),
          state = $rmEl.attr(readMore.STATE);

      if (!state) {
        readMore.init($rmEl);
      } else if (state === 'open') {
        readMore.close($rmEl);
      } else if (state === 'closed') {
        readMore.open($rmEl);
      }
    },
    initAll: function () {
      $('['+this.PROP+']').each(function (i, el) {
        var $el = $(el),
            state = $el.attr(this.STATE);

        if (!state) {
          this.init($el);
        }
      }.bind(this));
    },
    init: function (el, state) {
      var $el = $(el),
          htmlContent = $el.html(),
          textContent = this.truncate($el.text(), $el.attr(this.TRUNCATE));

      $el.html('<div fd-readmore-truncated>'+textContent+'<span '+this.TRIGGER+'>read more</span></div><div fd-readmore-html>'+htmlContent+'<span '+this.TRIGGER+'>show less</span></div>');

      $el.attr(readMore.STATE, state || 'closed');
    },
    truncate: function (text, length) {
      var result;

      length = +length || 200;
      result = text;

      if (text.length > length) {
        text = text.substring(0, length+1);
        result = text;

        text = text.replace(/\s[\S]*$/, '');
        if (text.length > length/2) {
          result = text;
        }

        result += '&hellip;';
      }

      return result;
    },
    open: function (el) {
      var openFn = fd.utils.discover($(el).attr(readMore.ONOPEN));

      if (openFn) {
        openFn(el);
      } else {
        $(el).attr(this.STATE, 'open');
      }
    },
    close: function (el) {
      var closeFn = fd.utils.discover($(el).attr(readMore.ONCLOSE));

      if (closeFn) {
        closeFn(el);
      } else {
        $(el).attr(this.STATE, 'closed');
      }
    }
  };

  $(document).on('click', '['+readMore.TRIGGER+']', readMore.toggle.bind(readMore));

  fd.utils.register("components", "readMore", readMore, fd);
}(FreshDirect));

(function (fd) {
  "use strict";

  fd.components.readMore.initAll();
}(FreshDirect));
