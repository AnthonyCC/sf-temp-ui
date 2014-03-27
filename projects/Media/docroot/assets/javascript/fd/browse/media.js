/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var media = Object.create(WIDGET,{
    signal:{
      value:'descriptiveContent'
    },
    template:{
      value:browse.media
    },
    placeholder:{
      value:'.browse-media'
    },
    render:{
      value: function(data) {
        // render media
				$(this.placeholder).html(this.template(data));
        
        // render department header
        $('.browse-titlebar').html(browse.titleBar(data));

        // set page title
        document.title = data.pageTitle || document.title;

        // set history
        FreshDirect.browse.main.setURL(data.url, document.title, true);
			}
    }
  });

  media.listen();

  fd.modules.common.utils.register("browse", "media", media, fd);
}(FreshDirect));
