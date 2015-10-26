/*global jQuery,browse,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var sections = Object.create(WIDGET,{
    signal:{
      value:'sections'
    },
    template:{
      value:browse.content
    },
    placeholder:{
      value:'.browse-sections'
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents(this.placeholder),
            menu = fd.browse.menu;

        if (menu) {
          clickEvent.preventDefault();
          menu.setId(clicked.data('id'));
          parent.trigger('menu-change');
        }

      }
    }
  });

  var superSections = Object.create(WIDGET,{
    signal:{
      value:'sections'
    },
    template:{
      value:browse.superDepartment
    },
    placeholder:{
      value:'.browse-superdepartment'
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents(this.placeholder),
            menu = fd.browse.menu;

        if (menu) {
          clickEvent.preventDefault();
          menu.setId(clicked.data('id'));
          parent.trigger('menu-change');
        }

      }
    }
  });

  if (window.srch) {
    var topSections = Object.create(WIDGET,{
      signal:{
        value:'sections'
      },
      template:{
        value:srch.topContent
      },
      placeholder:{
        value:'.browse-sections-top'
      }
    });

    var bottomSections = Object.create(WIDGET,{
      signal:{
        value:'sections'
      },
      template:{
        value:srch.bottomContent
      },
      placeholder:{
        value:'.browse-sections-bottom'
      }
    });

    topSections.listen();
    bottomSections.listen();
  }

  sections.listen();
  superSections.listen();

  $(document).on('click', '.browse-sections [data-component="categorylink"]', sections.handleClick.bind(sections));
  $(document).on('click', '.superDepartment [data-component="categorylink"]', superSections.handleClick.bind(superSections));

  fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
