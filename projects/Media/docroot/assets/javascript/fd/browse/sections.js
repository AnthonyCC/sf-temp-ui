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
			var prodSelector = ".browse-sections-top .products.transactional>.portrait-item.regularProduct";
			var HLprodSelector = "#searchPanel > .browse-adproducts.isHookLogic-true .portrait-item.isHookLogicProduct";
			var paginationSelectedSelector = ".pagination-pager-button.green.selected";
			
			var hookLogicRowLimit = 4;
			var HLmaxLen = Math.min( $(HLprodSelector).length, (hookLogicRowLimit+1) );
			var $regProdArr = $(prodSelector);
			var $hlProdArr = $(HLprodSelector);
			var regItemsPerRow = 4 + (($hlProdArr.length === 0)?1:0);
			var start = 0;
			var end = start + regItemsPerRow - 1;
			var index = 0;
			
			while (end < $regProdArr.length) {
				var rowHeights = [0];
				
				$regProdArr.slice(start, end+1).each(function(){
					var classNames = $(this).attr("class");
					if (classNames) {
						$(this).attr("class", classNames.replace(/fakeRow_(\d+)/g, 'fakeRowTop_'+index) );
					}
					
					//remove 'lastInLine' classname to these products. being done here because there is no need for this without hookLogic products present
					$(this).removeClass('lastInLine');
					
					rowHeights.push($(this).outerHeight(true));
				});
				start += regItemsPerRow;
				end = start + regItemsPerRow - 1;
				
					
				$hlProdArr.slice(index, index+1).each(function() {
					if ( $.isNumeric( $(paginationSelectedSelector).attr("data-page") ) && $(paginationSelectedSelector).attr("data-page") != "1" ){
						$(this).hide();
					}else{
						$(this).show();
					}
					
					$(this).addClass('fakeRowTop_'+index);
					rowHeights.push($(this).outerHeight(true));
				});
				
				if (index === hookLogicRowLimit || index === $hlProdArr.length-1) {
					regItemsPerRow++;
				}
				
				$('.fakeRowTop_'+index).css('min-height', Math.max.apply(null, rowHeights)+'px');
				
				index++;
			}
			
			
			if($(paginationSelectedSelector).attr("data-page") == "1"){
				//used to randomize the next url
				var randomTime = new Date().getTime();
				
				//beckoning for page beacon
				if ($(".browse-sections-top .browseContent .HLpageBeaconImg").length === 0) { /* only one instance at a time */
					$(".browse-sections-top .browseContent").append("<img class='HLpageBeaconImg' src='" + window.FreshDirect.browse.data.adProducts.pageBeacon + "&random=" + randomTime + "' />");
				}
			}
				
			
			//if( $.contains( $(".isHookLogic-false .browse-sections-top .products.transactional"), $(".isHookLogic-true") ) == false ){
			if( $.contains( $(".isHookLogic-false"), $(".isHookLogic-spacer") ) == false ){
				//$(".isHookLogic-true").clone().prependTo( $(".isHookLogic-false .browse-sections-top .products.transactional") );
				
				var hltH = $(".isHookLogic-true").height() - 55;
				
				//console.log("hltH = " + hltH);
				
				if( $(".isHookLogic-spacer").length < 1 ){
					$(".isHookLogic-false .browse-sections-top .products.transactional").prepend("<div class='isHookLogic-spacer' style='height:"+hltH+"px; '></div>");
				}
			}
			
			$(".isHookLogic-false .browse-sections-top .products.transactional").css("min-height", $(".isHookLogic-true").outerHeight(true) );
			
			//correct for when there are filter tags
			if( $('.filterTags').length > 0 ){
				$(".isHookLogic-true").first().css("margin-top", $('.filterTags').first().outerHeight(true)+5 + "px" );
			}else{
				$(".isHookLogic-true").first().css("margin-top", "30px" );
			}
		}//end adProductSection.fixThoseHooklogicDisplayHeights

		topSections.listen();
		bottomSections.listen();
    
		//new for HookLogic
		adProductSection.listen();
    
		//needed?
		//adProductSection.fixThoseHooklogicDisplayHeights(197);
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
		
		if( window.isHLchangable == true || (settings.url.indexOf("/api/filter?data=") != -1 ) || (settings.url.indexOf("cartdata?change=") != -1 ) ){
			adProductSection.fixThoseHooklogicDisplayHeights();
		}
		
		window.isHLchangable = false;
	});

	fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
