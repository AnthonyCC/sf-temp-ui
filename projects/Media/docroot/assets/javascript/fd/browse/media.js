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
      value: {
        general: browse.media,
        top: browse.topMedia,
        middle: browse.middleMedia,
        bottom: browse.bottomMedia
      }
    },
    placeholder:{
      value: {
        general: '.browse-media',
        top: '.browse-media-top',
        middle: '.browse-media-middle',
        bottom: '.browse-media-bottom'
      }
    },
    render:{
      value: function(data) {
        var pos;
        // render media
        ['top', 'middle', 'bottom'].forEach(function (pos) {
          $(this.placeholder[pos]).html(this.template[pos](data));
        }, this);
        
        // render department header
        $('.browse-titlebar').html(browse.titleBar(data));

        // set page title, allow diacritics
        var _temp = document.createElement('div');
        _temp.innerHTML = data.pageTitle || document.title;
        if (_temp.childNodes.length !== 0) {
        	document.title = _temp.childNodes[0].nodeValue;
        }

        // set history
        FreshDirect.browse.main.setURL(data.url, document.title, true);
			}
    }
  });

  media.listen();

  fd.modules.common.utils.register("browse", "media", media, fd);
}(FreshDirect));
