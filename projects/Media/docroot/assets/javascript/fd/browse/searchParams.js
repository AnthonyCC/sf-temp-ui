/*global jQuery,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var cm = fd.components.coremetrics;

  if(!window.srch){ return; }

  var searchParams = Object.create(WIDGET,{
    signal:{
      value:'searchParams'
    },
    template:{
      value:srch.searchParams
    },
    placeholder:{
      value:'.search-input'
    },
    serialize: {
      value: function () {
        var searchParams = $(this.placeholder+' input.searchinput').val();
        if (!searchParams) {
        	searchParams = $('input.searchinput:first').val()
        }

        return { searchParams: searchParams };
      }
    },
    handleClick:{
      value:function(clickEvent){
        clickEvent.preventDefault();
        fd.browse.menu.resetFilters();
        if (cm) {
            cm.setEvent('pageview');
          }
        $(this.placeholder).trigger('searchParams-change');
      }
    },
    render:{
      value:function(data){
        WIDGET.render.call(this, data);
        FreshDirect.components.autoComplete.init(this.placeholder+' input.searchinput');
        $('input.searchinput,input[name="searchParams"]').val(data.searchParams || "");
      }
    }
  });

  var searchTabs = Object.create(WIDGET,{
    signal:{
      value:'searchParams'
    },
    template:{
      value:srch.searchTabs
    },
    placeholder:{
      value:'nav.tabs'
    },
    serialize: {
      value:function(){
          return { activeTab: $(this.placeholder + ' .active').first().data("type") };
      }
    },
    purgeActiveTabs : {
      value: function(){
        $(this.placeholder + ' .active').removeClass("active");
      }
    },
    handleClick:{
      value:function(clickEvent){
        this.purgeActiveTabs();
        
        // select new active tab
        var currentTabType = $(clickEvent.currentTarget).data("type");
        $(this.placeholder).find("[data-type='"+ currentTabType+"']").addClass("active");

        cm && cm.setEvent('element');

        clickEvent.preventDefault();
        $(this.placeholder).trigger('searchParams-change');
      }
    }
  });

  var ddppList = Object.create(WIDGET,{
    signal:{
      value:'ddppproducts'
    },
    template:{
      value:srch.ddppWrapper
    },
    placeholder:{
      value:'.srch-ddpp'
    }
  });

  var searchSuggestions = Object.create(WIDGET,{
    signal:{
      value:'searchParams'
    },
    template:{
      value:srch.searchSuggestions
    },
    placeholder:{
      value:'section.itemcount'
    }
  });

  var listSearchParams = Object.create(WIDGET,{
	    signal:{
	      value:'searchParams'
	    },
	    template:{
	      value:srch.listSearch
	    },
	    placeholder:{
	      value:'#listsearch'
	    }
	  });

  searchParams.listen();
  searchTabs.listen();
  searchSuggestions.listen();
  ddppList.listen();
  listSearchParams.listen();

  $(document).on('click',searchParams.placeholder+' button.searchbutton',searchParams.handleClick.bind(searchParams));
  $(document).on('click',searchTabs.placeholder+' li',searchTabs.handleClick.bind(searchTabs));

  fd.modules.common.utils.register("browse", "searchParams", searchParams, fd);
  fd.modules.common.utils.register("browse", "searchTabs", searchTabs, fd);
}(FreshDirect));

