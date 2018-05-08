/*global explore*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var exploreCategoryList = Object.create(WIDGET,{
    signal:{
      value:'exploreCategoryList'
    },
    template:{
      value:explore.categoryList
    },
    placeholder:{
      value:'#explore-category-list'
    },
    render:{
      value:function(data){
        WIDGET.render.call(this, data);
      }
    }
  });
  exploreCategoryList.listen();

  fd.modules.common.utils.register("modules.explore", "categoryList", exploreCategoryList, fd);
}(FreshDirect));
