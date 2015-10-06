/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd, $) {
  "use strict";

  /**
   * Tooltip for links and such with HTML content.
   *
   * @param {Element} el The <input> element with the placeholder
   * @config {Object} Extra configuration
   * config:
   *    - content: HTML content for the tooltip
   *    - cssClass: CSS class for the tooltip
   *    - orientation: "top", "bottom"
   *    - offset: the gap between the element and the tooltip
   */
  var Tooltip = function (el, config) {
    this.el = $(el);
    this.config = fd.modules.common.utils.extend({}, Tooltip.DEFAULT_CONFIG, config);
    this.config.content = this.getContent();
    this.node = null;

    this.el.on("mouseover", this.show.bind(this));
    this.el.on("mouseout", this.hide.bind(this));
  };

  Tooltip.prototype.createNode = function () {
    if (this.node) {
      return this.node;
    }
    this.node = $('<div>');

    this.node.addClass(this.config.cssClass);
    this.node.addClass("tooltip_" + this.config.orientation);

    this.node.css({
      display: 'none',
      position: 'absolute',
      zIndex: 1000
    });

    this.arrow = document.createElement("span");
    this.arrow.className = "arrow";
    this.node.append(this.config.content);
    this.node.append(this.arrow);
    this.node.appendTo(document.body);

    return this.node;
  };

  Tooltip.prototype.reposition = function () {
    var elregion = this.el.first()[0].getBoundingClientRect(),
        myregion = this.node.first()[0].getBoundingClientRect(),
        top, bottom, left, right;
    
    var elregion_offset = $(this.el.first()[0]).offset();

    // Position based on orientation
    if (this.config.orientation === 'bottom') {
      top = (elregion.bottom + this.config.offset) + "px";
      left = (elregion.left + (elregion.width / 2) - (myregion.width / 2)) + "px";
    } else { // top
      top = 'auto';
      top = elregion_offset.top - this.config.offset - myregion.height + "px";
      left = elregion_offset.left + (elregion.width / 2) - (myregion.width / 2) + "px";
    }

    this.node.css({
      top: top,
      left: left
    });
  };

  Tooltip.prototype.show = function () {
    this.createNode();
    this.node.css({
      display: 'block'
    });
    this.reposition();
  };

  Tooltip.prototype.hide = function () {
    if (!!this.node) {
      this.node.css({
        display: 'none'
      });
    }
  };

  Tooltip.prototype.getContent = function () {
    var next = this.el.next(),
        title;
    if (typeof(this.config.content) === "string") {
      return document.createTextNode(this.config.content);
    } else if (next.hasClass('tooltipcontent')) {
      return next.remove();
    } else if (!!(title = this.el.attr('title'))) {
      this.el.attr('title', "");
      return document.createTextNode(title);
    }
  };

  Tooltip.DEFAULT_CONFIG = {
    orientation: 'top',
    offset: 10,
    cssClass: 'tooltip'
  };

  Tooltip.init = function () {
    var els = $('.hastooltip'),
        ttip, i;

    for (i = 0; i < els.length; i++) {
      ttip = new FreshDirect.modules.common.Tooltip(els[i]);
    }
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Tooltip", Tooltip, fd);
}(FreshDirect, jQuery));

// module initialization
(function () {
  "use strict";

  FreshDirect.modules.common.Tooltip.init();
}());
