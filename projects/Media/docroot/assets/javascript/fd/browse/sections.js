/*global jQuery,browse*/
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

  var certonaResonaceTagPopulate = Object.create(fd.common.signalTarget,{
    signal:{
      value:'descriptiveContent'
    },
    callback:{
      value:function(data){
        if (data) {
          window.certona = window.certona || {};
          window.certona.category = data.contentId;
          window.certona.pagetype = data.navDepth;
          delete window.certona.department;
          delete window.certona.superdepartment;
        }
      } 
    }
  });
  
  certonaResonaceTagPopulate.listen();
  sections.listen();
  superSections.listen();

  $(document).on('click', '.browse-sections [data-component="categorylink"]', sections.handleClick.bind(sections));
  $(document).on('click', '.superDepartment [data-component="categorylink"]', superSections.handleClick.bind(superSections));

  fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
