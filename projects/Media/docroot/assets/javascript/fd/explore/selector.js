/*global explore*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  var exploreSelector = Object.create(WIDGET,{
    signal:{
      value:'exploreTopList'
    },
    template:{
      value:explore.topSelector
    },
    placeholder:{
      value:'#explore-top-selector'
    },
    sdep:{
      value: null,
      writable: true
    },
    selectedDep:{
      value: "",
      writable: true
    },
    deplist:{
      value: [],
      writable: true
    },
    getCategoryList:{
      value: function() {
        var dep = this.sdep.departments.filter(function (d) { return d.id.toLowerCase() === this.selectedDep.toLowerCase(); }.bind(this))[0];

        if (dep && dep.categories) {
          return dep.categories;
        }

        return [];
      }
    },
    render:{
      value:function(data){
        data.list = data.list || this.deplist;
        data.selected = data.selected || this.selectedDep;
        data.currentDep = data.currentDep || (this.sdep && this.sdep.id);

        if (data.sdep && this.sdep !== data.sdep) {
          WIDGET.render.call(this, data);
        } else {
          $('[data-component="explore-department-button"]').removeClass('selected');
          $('[data-component="explore-department-button"][data-dept="'+data.selected+'"]').addClass('selected');
        }

        this.deplist = data.list;
        this.selectedDep = data.selected;
        this.sdep = data.sdep || this.sdep;

      }
    }
  });
  exploreSelector.listen();

  $(document).on('click', '[data-component="explore-department-button"]', function (e) {
    var el = e.currentTarget,
        href = el.href,
        depId = el.getAttribute('data-dept');

    e.preventDefault();

    window.history.pushState({selected: depId}, "Shop by category", href);

    DISPATCHER.signal('exploreTopList', { selected: depId });
    DISPATCHER.signal('exploreCategoryList', { list: exploreSelector.getCategoryList() });
  });

  fd.modules.common.utils.register("modules.explore", "selector", exploreSelector, fd);
}(FreshDirect));

// process URI on page load
(function (fd) {
  var DISPATCHER = fd.common.dispatcher,
      sdepId = fd.utils.getParameterByName('sdep').toLowerCase(),
      depId = fd.utils.getParameterByName('dep').toLowerCase(),
      expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments,
      sdep = null, dep = null;

  if (expdata) {
    sdep = expdata.filter(function (d) { return d.id.toLowerCase() === sdepId; })[0];
    if (sdep && sdep.departments) {
      dep = depId ? sdep.departments.filter(function (d) { return d.id.toLowerCase() === depId; })[0] : sdep.departments[0];
      DISPATCHER.signal('exploreTopList', { sdep: sdep, list: sdep.departments, currentDep: sdepId, selected: dep.id });
    } else {
      dep = depId ? expdata.filter(function (d) { return d.id.toLowerCase() === depId; })[0] : expdata[0];
      DISPATCHER.signal('exploreTopList', { sdep: null, list: expdata, selected: dep.id });
    }

    if (dep && dep.categories) {
      DISPATCHER.signal('exploreCategoryList', { list: dep.categories });
    }
  }
}(FreshDirect));

// subscribe for popstate if feature is active
(function (fd) {
  var el = document.querySelector('#explore-top-selector');
  var DISPATCHER = fd.common.dispatcher;

  if (el) {
    window.onpopstate = function (e) {
      var state = e.state;

      DISPATCHER.signal('exploreTopList', { selected: state.selected });
      DISPATCHER.signal('exploreCategoryList', { list: fd.modules.explore.selector.getCategoryList() });
    };
  }
}(FreshDirect));

