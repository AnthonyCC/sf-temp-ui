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
          shorthtmlContent = this.truncate($el.html(), $el.text(), $el.attr(this.TRUNCATE));

      if($el.text().length > 200){
    	  shorthtmlContent = shorthtmlContent + '<span ' + this.TRIGGER + '>read more</span>';
    	  var closeHTML = document.createElement('div');
    	  closeHTML.innerHTML=shorthtmlContent;
    	  shorthtmlContent = closeHTML.innerHTML;
    	  $el.html('<div fd-readmore-truncated>'+shorthtmlContent+'</div><div fd-readmore-html>'+htmlContent+'<span '+this.TRIGGER+'>show less</span></div>');
          $el.attr(readMore.STATE, state || 'closed');
      } else {
    	  $el.html('<div fd-readmore-html>'+htmlContent+'</div>');
    	  $el.attr(readMore.STATE, state || 'open');
      }
      
    },
    truncate: function (html, text, length) {

      var result;
      length = +length || 200;
      result = text;
      length = length + (html.length - text.length);

      if (html.length > length) {
    	html = html.substring(0, length+1);
        result = text;

        html = html.replace(/\s[\S]*$/, '');
        if (html.length > length/2) {
          result = html;
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
