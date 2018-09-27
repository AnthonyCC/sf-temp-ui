/*global jQuery*/
var FreshDirect = window.FreshDirect || {};

(function (fd, $) {
  var Scroll = {
    addHorizontalScrollFade: function(root){
        var SIZE_MULTIPLIER = .4;
        var w = $(window);
        var fields = $(root).find('[data-horizontal-scroll-fader]:not([data-horizontal-scroll-fader-active])')
            .attr('data-horizontal-scroll-fader-active', true)
            .each(function (_, el) {
                el = $(el);
                el.append('<div class="horizontal-scoll-fade-container">' +
                    '<div class="horizontal-scoll-fade-left"></div>' +
                    '<div class="horizontal-scoll-fade-right"></div>' +
                    '</div>');
                var container = el.find('.horizontal-scoll-fade-container');
                var left = el.find('.horizontal-scoll-fade-left');
                var right = el.find('.horizontal-scoll-fade-right');
                return el.on('scroll', function (e) {
                    container.css({ 'left': '0' });
                    var scrollable = el.get(0).scrollWidth > el.width();
                    container.css({ 'left': (scrollable ? e.target.scrollLeft: 0) + 'px' });
                    var size = Math.floor(container.width() * SIZE_MULTIPLIER);
                    var leftOpacity = 0, rightOpacity = 0;
                    if (scrollable){
                        leftOpacity = Math.min(1, e.target.scrollLeft / (size || 1));
                        rightOpacity = Math.min(1, (e.target.scrollWidth - e.target.scrollLeft - el.width()) / (size || 1));
                    }
                    left.css({ 'opacity': leftOpacity })
                    right.css({ 'opacity': rightOpacity })
                }).trigger('scroll');
            });
        w.on('resize', function () { fields.trigger('scroll') });
    }
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Scroll", Scroll, fd);
}(FreshDirect, jQuery));
