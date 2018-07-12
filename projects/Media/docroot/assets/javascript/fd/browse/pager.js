/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var pager = Object.create(WIDGET,{
    signal:{
      value:'pager'
    },
    template:{
      value:browse.pager
    },
    placeholder:{
      value:'.pager-holder'
    },
    serialize:{
      value:function(el){
        var $el = el ? $(el) : $('[data-component="pager"]:first'),
            activepage = parseInt($el.data('activepage'),10);

        if (+activepage === activepage) {
          return {
            activePage: activepage
          };
        }
      }
    },
    innerSerialize:{
      value:function(el){
        var $el = el ? $(el) : $('[data-component="pager"]:first');
        return {
          activePage : parseInt($el.data('activepage'),10),
          pageSize : parseInt($el.data('pagesize'),10),
          pageCount : parseInt($el.data('pagecount'),10),
          itemCount : parseInt($el.data('itemcount'),10),
          firstItemIndex: parseInt($el.data('firstitemindex'),10),
          lastItemIndex: parseInt($el.data('lastitemindex'),10)
        };
      }
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.target),
          component = clicked.data('component'),
          parent = clicked.parents('[data-component="pager"]'),
          newPage, activePage = parent.data('activepage'),
          pageCount = parseInt(parent.data('pagecount'),10),
          newPageSize = parseInt(parent.data('pagesize'),10);
        if(component === 'pager-prev') {
          activePage = parent.data('activepage')*1;
          newPage=Math.max(activePage-1,1);
        } else if(component === 'pager-next') {
          activePage = parent.data('activepage')*1;
          newPage=Math.min(activePage+1,pageCount);
        } else if(component === 'showall'){
          newPage = activePage ? 0 : 1;
        } else {
          newPage = clicked.data('page');
        }
        if(newPage!==undefined) {
          var json = this.innerSerialize(parent);
          json.activePage = newPage;
          json.pageSize = newPageSize;

          this.render(json);

          $(this.placeholder).first().trigger('page-change');
        }
      }
    }
  });

  pager.listen();
  $(document).on('click',pager.placeholder,pager.handleClick.bind(pager));

  fd.modules.common.utils.register("browse", "pager", pager, fd);
}(FreshDirect));
