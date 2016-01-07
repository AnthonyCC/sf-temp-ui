/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var cm = fd.components.coremetrics;

  var menu = Object.create(WIDGET,{
    signal:{
      value:'menuBoxes'
    },
    template:{
      value:browse.menu
    },
    placeholder:{
      value:'#leftnav'
    },
    serialize:{
      value:function(element){
        var el = (element) ? element : $(this.placeholder),
            idboxes = $('.menuBox[data-filter="id"]',el),
            result, filters = {};

        result = {
          id: this.id
        };

        $('[data-filter]').filter(function (i, el) {
          return $(el).data('filter') !== "id";
        }).each(function (i, el) {
        var selected = $('input', el).filter(function (i, el) { return el.checked || el.getAttribute('checked'); }).map(function (i, el) { return $(el).val(); });

          if (selected && selected.length) {
            filters[$(el).data('filter')] = [].slice.apply(selected);
            if ($(el).data('shouldsetall')) {
              result.all = true;
            }
          }
        });

        if (Object.keys(filters).length) {
          result.requestFilterParams = filters;
        }

        if ($('[data-component="menuitem"][data-urlparameter="all"] input').prop('checked') || $('[data-component="menubox"][data-allselected]').size() > 0) {
          result.all = true;
        }

        if (!result.id && idboxes.size()) {
          result.id = idboxes.last().data('id');
        }

        return result;
      }
    },
    setId:{
      value:function(id){
        // set id
        this.id = id;

        // reset 'all' setting
        $('[data-component="menuitem"][data-urlparameter="all"] input').attr('checked', null);
        $('[data-component="menubox"][data-allselected]').attr('data-allselected', null);

        if (cm) {
          cm.setEvent('pageview');
        }
        if (FreshDirect.browse.sorter) { FreshDirect.browse.sorter.reset(); }
      }
    },
    resetFilters:{
      value:function(){
        $('[data-boxtype="FILTER"] input').attr('checked', null);
        if (FreshDirect.browse.sorter) { FreshDirect.browse.sorter.reset(); }
      }
    },
    removeFilter:{
      value:function(parent, id){
        $('[data-boxtype="FILTER"][data-filter="'+parent+'"] input[value="'+id+'"]').attr('checked', null);
      }
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            urlparameter = clicked.data('urlparameter'),
            parent = clicked.parents('[data-component="menu"]'),
            menubox = clicked.parents('[data-component="menubox"]');

        if (clicked.hasClass('disabled')) {
          return;
        }

        // reset filters and sorter if top level category selected
        if (menubox.data('boxtype') === 'CATEGORY') {
          this.resetFilters();
        }

        if (menubox.data('type') !== 'MULTI') {
          $(".selected", menubox).removeClass('selected');
        }

        if (menubox.data('boxtype') === 'FILTER') {
          if (cm) {
            cm.setEvent('element');
          }
        }

        clicked.addClass('selected');
        if (menubox.data('filter') === 'id') {
          if (urlparameter === "all") {
            this.id = menubox.data('id');
          } else {  
            this.id = urlparameter;
          } 
          if (cm) {
            cm.setEvent('pageview');
          }
          if (FreshDirect.browse.sorter) { FreshDirect.browse.sorter.reset(); }
        }

        this.savedScrolls = this.saveScrolls();

        parent.trigger('menu-change');
      }
    },
    handlePopupClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget).parents('[data-component="menuitem"]').first(),
            itemlist = clicked.parents('[data-component="menuitemlist"]'),
            menubox = $('[data-component="menubox"][data-id="'+itemlist.data('menuitemlist')+'"]'),
            menu = menubox.parents('[data-component="menu"]'),
            id = clicked.data('urlparameter');
        if (clicked.hasClass('disabled')) {
          return;
        }

        // reset filters if top level category selected
        if (menubox.data('boxtype') === 'CATEGORY') {
          this.resetFilters();
        }

        $(".selected", menubox).removeClass('selected');
        $(".selected", itemlist).removeClass('selected');
        $('input[type="radio"]', menubox).attr('checked', null);
        $('[data-urlparameter="'+id+'"]', menubox).addClass('selected');
        $('[data-urlparameter="'+id+'"] input', menubox).attr('checked', $('input', clicked).prop('checked'));
        clicked.addClass('selected');

        if (menubox.data('filter') === 'id') {
          if (id === "all") {
            this.setId(menubox.data('id'));
            menubox.attr('data-allselected', true);
          } else {  
            this.setId(id);
          } 
          if (FreshDirect.browse.sorter) { FreshDirect.browse.sorter.reset(); }
        }

        if (menubox.data('boxtype') === 'FILTER') {
          if (cm) {
            cm.setEvent('element');
          }
        }

        clickEvent.preventDefault();

        this.savedScrolls = this.saveScrolls();

        menu.trigger('menu-change');
      }
    },
    saveScrolls: {
      value: function () {
        var scrollStates = {};

        $('[data-component="menubox"]').each(function (i, el) {
          var $el = $(el),
              ul = $el.find('ul')[0],
              key = $el.data('id') + '_' + $el.data('boxtype');

          if (ul) {
            scrollStates[key] = ul.scrollTop;
          }
        });

        return scrollStates;
      }
    },
    restoreScrolls: {
      value: function (scrollStates) {
        if (!scrollStates) {
          return;
        }

        $('[data-component="menubox"]').each(function (i, el) {
          var $el = $(el),
              ul = $el.find('ul')[0],
              key = $el.data('id') + '_' + $el.data('boxtype');

          if (scrollStates[key]) {
            ul.scrollTop = scrollStates[key];
          }
        });
      }
    },
    render:{
      value:function(data){
        $(this.placeholder).html(this.template(data));
        
        if (data.menuBoxes.length > 0) {
          $("section.container").removeClass("emptymenu");
        } else {
          $("section.container").addClass("emptymenu");
        }

        // close popups
        if (fd.common.transactionalPopup) { fd.common.transactionalPopup.close(); }
        if (fd.components.menupopup) { fd.components.menupopup.close(); }
        if (fd.modules.browse.centermenupopup) { fd.modules.browse.centermenupopup.close(); }
        if (this.savedScrolls) {
          this.restoreScrolls(this.savedScrolls);
          this.savedScrolls = {}; 
        }
        
      }
    },
    initMenu:{
      value:function(){
        var idboxes = $('.menuBox[data-filter="id"]', $(this.placeholder));

        this.id = [].slice.apply(idboxes.find('[data-component="menuitem"].selected').map(function (i, el) { return $(el).data('urlparameter');}).filter(function (i, el) { return !!el && el !== 'all'; })).pop();
        this.listen();
      }
    }
  });
  
  menu.initMenu();
  $(document).on('click',menu.placeholder+' [data-component="menuitem"]', menu.handleClick.bind(menu));
  $(document).on('click','.menupopup [data-component="menuitem"] a, .centermenupopup [data-component="menuitem"] a', menu.handlePopupClick.bind(menu));
  $(document).on('change','.menupopup [data-component="menuitem"] input, .centermenupopup [data-component="menuitem"] input', menu.handlePopupClick.bind(menu));
  $(document).on('click', 'div.popupcontentghost button.selected.category.popup.leftnav-category-popup-button.cssbutton.green.hover', function() {
  		$jq( '#menupopup.shown li.selected a').click();
  	});

  fd.modules.common.utils.register("browse", "menu", menu, fd);
}(FreshDirect));
