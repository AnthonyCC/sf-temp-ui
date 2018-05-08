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
    render:{
      value:function(data){
        WIDGET.render.call(this, data);
      }
    }
  });
  exploreSelector.listen();

  fd.modules.common.utils.register("modules.explore", "selector", exploreSelector, fd);
}(FreshDirect));

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
      DISPATCHER.signal('exploreTopList', { list: sdep.departments, currentDep: sdepId, selected: dep.id });
    } else {
      dep = depId ? expdata.filter(function (d) { return d.id.toLowerCase() === depId; })[0] : expdata[0];
      DISPATCHER.signal('exploreTopList', { list: expdata, selected: dep.id });
    }

    if (dep && dep.categories) {
      DISPATCHER.signal('exploreCategoryList', { list: dep.categories });
    }
  }
}(FreshDirect));
