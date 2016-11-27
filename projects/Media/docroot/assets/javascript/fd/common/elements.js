/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd, $) {

  var Elements = function (el, config) {
    this.el = $(el);
    this.config = fd.modules.common.utils.extend({}, Elements.DEFAULT_CONFIG, config);
    this.widget = this.getWidget();
  };

  Elements.prototype.getWidget = function () {
    var widgetNode,
    	orig = this.el;
  
    if(this.el.hasClass('customradio')) {
      if (this.el.parent().hasClass('fake-radio-wrapper')) {
        return this.el;
      } else {
        $(orig).wrap( "<span class='fake-radio-wrapper'></span>" );
    	$(orig).parent().append('<span class="fake-radio"></span>');
      }
   } else if (this.el.hasClass('customsimpleselect')) {
      if (this.el.parent().hasClass('select-wrapper')) {
	    return this.el;
	   } else {
	     $(orig).wrap( "<div class='select-wrapper'></div>" );
	   }
   } else if (this.el.hasClass('customcheckbox')) {
      if (this.el.parent().hasClass('fake-checkbox-wrapper')) {
  	    return this.el;
  	   } else {
  	     $(orig).wrap( "<span class='fake-checkbox-wrapper'></span>");
  	     $(orig).parent().append('<span class="fake-checkbox"></span>');
  	   }
     };
  };
 
  Elements.DEFAULT_CONFIG = {
    display: 'none'
  };

  Elements.decorate = function (root) {
    var root = root || $(document.body),
    	els  = root.find('input.customradio, select.customsimpleselect, input.customcheckbox'),
        element;
    
    if (els.length === 0) {
    	root = $(document.body);
    	els = root.find('input.customradio, select.customsimpleselect, input.customcheckbox');
    }
    
    els.each(function (i, el) {
        element = new Elements(el);
    });
  };

  // module initialization
  Elements.decorate();

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Elements", Elements, fd);
}(FreshDirect, jQuery));
