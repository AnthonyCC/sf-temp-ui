/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd, $) {

  var Select = function (el, config) {
    this.el = $(el);
    this.config = fd.modules.common.utils.extend({}, Select.DEFAULT_CONFIG, config);
    this.widget = this.getWidget();
    this.popup = Select.getPopup();
    this.update();
    this.widget.on('mouseover', '.selectButton', this.open.bind(this));
    this.widget.on('keydown', '.selectButton', function (e) { if(e.keyCode == '13'){ this.open(); this.popup.clicked = true; } }.bind(this));
    this.widget.on('mouseout', '.selectButton', function () { this.popup.clearDelay(); }.bind(this));
    this.bindClick();

    this.el.on('change', this.update.bind(this));
    this.el.attr('data-selectized', 'true');
  };

  Select.prototype.getWidget = function () {
    var widgetNode;
    if (this.el.next() && this.el.next().hasClass(this.config.cssClass)) {
      return this.el.next();
    } else if(this.el.attr('data-custom-select-light-class') !== typeof undefined && this.el.attr('data-custom-select-light-class') !== false) {
      widgetNode = $('<span class="'+(this.config.cssClass || '')+' '+(this.el.attr('data-custom-select-class') || '')+'"><button class="selectButton cssbutton '+(this.el.attr('data-custom-select-button-class') || '')+' '+(this.el.attr('data-custom-select-light-class'))+'" aria-haspopup="true"><span><span class="popupcontent"></span><b class="title"></b></span></button></span>');
      this.el.after(widgetNode);
      return widgetNode;
    } else {
      widgetNode = $('<span class="'+(this.config.cssClass || '')+' '+(this.el.attr('data-custom-select-class') || '')+'"><button class="selectButton cssbutton '+(this.config.buttonClass || '')+' '+(this.el.attr('data-custom-select-button-class') || '')+'" aria-haspopup="true"><span><span class="popupcontent"></span><b class="title"></b></span></button></span>');
      this.el.after(widgetNode);
      return widgetNode;
    }
  };

  Select.prototype.bindClick = function (el) {
    el = el || this.widget.find('.popupcontent');

    el.on('click', '.customselect li', function (e) {
      var $t = $(e.currentTarget);

      e.stopPropagation();

      if (!$t.hasClass('selected')) {
        this.el.val($t.attr('data-value'));
        setTimeout(function () { this.el.change() }.bind(this), 0);
      }

      this.close();
    }.bind(this));

    el.on('click', '.close-icon', function(e) {
      e.stopPropagation();
      this.close();
    }.bind(this));
  };

  Select.prototype.open = function () {
    this.popup.$el.find('.browse-popup-content').html('').append(this.widget.find('.popupcontent').clone());
    $('.selectButton.selectlight').addClass('overlay-open');
    this.popup.showWithDelay(this.widget.find('.selectButton'), 'bl-tl');
    this.bindClick(this.popup.$el.find('.popupcontent'));
    
    if(this.el.hasClass('custom-select-light')) {
      $('#FDCustomSelect.browse-popup').addClass('selectlight');
    }else{
      $('#FDCustomSelect.browse-popup').removeClass('selectlight');
    }
  };

  Select.prototype.close = function () {
    this.popup.hide();
  };

  Select.prototype.update = function () {
    Select.update(this.el, this.widget);
  };

  Select.getPopup = function () {
    if (!Select.popup) {
      Select.popup = new fd.modules.common.PopupContent(
        $('<div id="FDCustomSelect" class="browse-popup"><div class="browse-popup-content">cica</div></div>').hide().appendTo(document.body),
        $('.customselect button'),
        Select.POPUP_CONFIG
        );
      }
      

    return Select.popup;
  };

  Select.unescape = function (text) {
    return text.replace(/\[br]/g, "<br>").replace(/\[b]/g, '<b>').replace(/\[\/b]/g, '</b>');
  };

  Select.update = function (el, widget) {
    var selected = el.children('option:selected'),
        title = selected.html(),
        options = el.children('option'),
        popupcontent = '';

    widget.find('.title').first().html(Select.unescape(title));
    popupcontent += '<ul class="customselect" data-value="'+selected.val()+'">';
    if (el.hasClass('custom-select-light') !== false) {
      popupcontent = '<div class="overlay-container"></div>'
      var optionsExistClass;
      if (options.length > 1) {
        optionsExistClass = 'multiple-options';
        popupcontent = '<ul class="customselect '+optionsExistClass+'" data-value="'+selected.val()+'">';
      } else {
        optionsExistClass = 'empty-options';
        popupcontent = '<ul class="customselect '+optionsExistClass+'" data-value="'+selected.val()+'">';
      }
    }
    options.each(function (i, option) {
      var cssClass = "",
          $option = $(option);

      if (i === 0) {
        cssClass += ' default';
      }
      if (selected.val() === $option.val()) {
        cssClass += ' selected';
      }
      if (options.length === 1 && el.hasClass('custom-select-light') !== false) {
        popupcontent += '<li class="'+cssClass+'" data-value="'+$option.val()+'"><button type="button">No eligible orders found</button></li>';          
      } else {
        popupcontent += '<li class="'+cssClass+'" data-value="'+$option.val()+'"><button type="button">'+Select.unescape($option.html())+'</button></li>';
      }
    });
    popupcontent += '</ul>';

    widget.find('.popupcontent').html(popupcontent);
    el.hide();
  };

  Select.POPUP_CONFIG = {
    // placeholder: false,
    zIndex: 2000,
    placeholderDisplay: 'inline-block',
    stayonghostclick: true,
    delay: 200
  };

  Select.DEFAULT_CONFIG = {
    align: 'auto',
    offset: 0,
    buttonClass: 'green icon-arrow-down2-after',
    cssClass: 'fdselect'
  };

  Select.selectize = function (root) {
    var root = root || $(document.body),
    //els,        
    //els = root.find('select.customselect'),
    els = root.find('select.customselect'),        
    select;
    
    els.each(function (i, el) {
      if (!$(el).attr('data-selectized')) {
        select = new Select(el);
      }
    });
  };

  // module initialization
  Select.selectize();

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Select", Select, fd);
}(FreshDirect, jQuery));
