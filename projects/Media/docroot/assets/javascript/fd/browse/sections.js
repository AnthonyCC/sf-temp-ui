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

	if(window.srch){
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
    
		var adProductSection = Object.create(WIDGET,{
			signal:{
				value:'adProducts'
			},
			template:{
				value:common.simpleFixedProductList
			},
			placeholder:{
				value:'.browse-adproducts'
			}
		});
    
		adProductSection.fixThoseHooklogicDisplayHeights = function(){
			/* make all items in row the same min height (the height of the tallest elem in row) */
			$('ul.products.transactional').each(function (i,e) {
				var $children = $(this).children('li.browseTransactionalProduct');
				$children.css('min-height', Math.max.apply(null, 
					$children.map(function() {
						return $jq(this).outerHeight(true);
					})
				)+'px');
			});
			
			/* add page beacon (if it doesn't already exist) */
			if(FreshDirect.browse.data.pager.activePage == 1){
				if ($(".browse-sections-top .browseContent .HLpageBeaconImg").length === 0) { /* only one instance at a time */
					$(".browse-sections-top .browseContent").append("<img class='HLpageBeaconImg' src='" + window.FreshDirect.browse.data.adProducts.pageBeacon + "&random=" + new Date().getTime() + "' />");
				}
			}
		}//end adProductSection.fixThoseHooklogicDisplayHeights

		topSections.listen();
		bottomSections.listen();
    
		//new for HookLogic
		adProductSection.listen();
    
		//needed?
		adProductSection.fixThoseHooklogicDisplayHeights();
	}//end if(window.srch)
	
	window.isHLchangable = false;

	sections.listen();
	superSections.listen();

	$(document).on('click', '.browse-sections [data-component="categorylink"]', sections.handleClick.bind(sections));
	$(document).on('click', '.superDepartment [data-component="categorylink"]', superSections.handleClick.bind(superSections));

	// page button change
	$(document).on('page-change', function(x){
		window.isHLchangable = true;
		
		
		//class="pagination-pager-button cssbutton green transparent"
	});
  
	//fires upon using search box
	//$(document).on('change', function(){
	$(".tabs li span, .searchbutton, .menuBox li span, .menupopup li span, .sorter button span, .pagination-showall-cssbutton").click(function(){
		window.isHLchangable = true;
	});
	
	/*window.onpopstate = function() {
		alert("pop!");
		
		window.isHLchangable = true;
	}*/
	
	$(window).on('popstate', function(event) {
		//alert("pop");
		
		window.isHLchangable = true;
		
		//change it here for the back button
		adProductSection.fixThoseHooklogicDisplayHeights();
	});

	//this always fires upon each set of products load success
	$( document ).ajaxSuccess(function(event, xhr, settings) {
		
		//console.log("window.FreshDirect.browse.data.pager.activePage = " + window.FreshDirect.browse.data.pager.activePage);
		
		//update the page beacon url
		if( xhr.responseJSON !== undefined && xhr.responseJSON.adProducts !== undefined &&
		xhr.responseJSON.adProducts.pageBeacon !== undefined &&  xhr.responseJSON.adProducts.pageBeacon != null &&
		xhr.responseJSON.adProducts.pageBeacon.length > 3 ){
			window.FreshDirect.browse.data.adProducts.pageBeacon = xhr.responseJSON.adProducts.pageBeacon;
		}
		
		if( (adProductSection && adProductSection.hasOwnProperty('fixThoseHooklogicDisplayHeights')) && (window.isHLchangable == true || (settings.url.indexOf("/api/filter?data=") != -1 ) || (settings.url.indexOf("cartdata?change=") != -1 )) ){
			adProductSection.fixThoseHooklogicDisplayHeights();
		}
		
		window.isHLchangable = false;
	});

	fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
