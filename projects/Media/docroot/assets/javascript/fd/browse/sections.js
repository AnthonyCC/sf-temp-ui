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
			var hookLogicRowLimit = 4;
			var modIndex = 0;
	
			var prodSelector = ".browse-sections-top .portrait-item.regularProduct";
			var HLprodSelector = "#searchPanel > .browse-adproducts.isHookLogic-true .portrait-item.isHookLogicProduct";
			var paginationSelectedSelector = ".pagination-pager-button.green.selected";
			
			var finalFakeRow = 0;
			var HLmaxLen = Math.min( $(HLprodSelector).length, (hookLogicRowLimit+1) );
			
			//var fakeClassPrefix = "fakeRow_";
			
			function fireHookLogicBeaconImpression($elem) {
				//$elem.append('<img src="'+$elem.data('hooklogic-beacon-impress')+'" style="display: none;" />');
			}
			
			//if there are some hookLogic products returned
			if( $(HLprodSelector).length > 0 ){
				finalFakeRow = HLmaxLen;
				
				
				
				$(HLprodSelector).each(function( index ) {
					
					//hide the hooklogic product if it is sixth or greater or if it is beyond page 1
					if(index > hookLogicRowLimit || ( $.isNumeric( $(paginationSelectedSelector).attr("data-page") ) && $(paginationSelectedSelector).attr("data-page") != "1") ){
						$(this).hide();
						
						console.log("window.FreshDirect.browse.data.pager.activePage = " + window.FreshDirect.browse.data.pager.activePage);
						
						console.log('$(paginationSelectedSelector).attr("data-page") = ', $(paginationSelectedSelector).attr("data-page"));
					}else{
						$(this).show();
						
						//starting number of what will be selected
						var reg_prod_start = (index * 3) + 1;					
						var reg_prod_end = reg_prod_start + 2;
	
						fireHookLogicBeaconImpression($(this));
						
						//first, get the rowclass of this hooklogic product
						var el = $(this);//get the element whose class value has to be extracted
						var fr_val = el.attr('class').match(/\bfakeRow_(\d+)\b/)[1];
						
						var regProds = prodSelector+":nth-of-type(n+"+reg_prod_start+"):nth-of-type(-n+"+reg_prod_end+")";
	
						$(regProds).each(function(index2){
							var attr = $(this).attr("class");
							
							if( (typeof attr !== typeof undefined) && (attr !== false) && attr.indexOf("fakeRow_") != -1 ){
								$(this).attr("class", $(this).attr("class").replace(/fakeRow_(\d+)/g, "fakeRow_"+index) );
							}
						});
					}//end if/else index > hookLogicRowLimit ...
				}); //end loop through hook logic items
				
				if($(paginationSelectedSelector).attr("data-page") == "1"){
					//used to randomize the next url
					var randomTime = new Date().getTime();
					
					//beckoning for page beacon
					$(".browse-sections-top .browseContent").append("<img class='HLpageBeaconImg' src='" + window.FreshDirect.browse.data.adProducts.pageBeacon + "&random=" + randomTime + "' />");
				}
				
				//remove 'lastInLine' classname to these products. being done here because there is no need for this without hookLogic products present
				$(prodSelector).removeClass('lastInLine');
				
				//what about product rows which wrap below hookLogic section?  this would be the first one
				var afterRowStartAt = (HLmaxLen * 3) + 1;
				
				//loop through said products
				for(var i=afterRowStartAt; i<$(prodSelector).length; i++){
					var thisProd = prodSelector+":nth-of-type("+i+")";
					
					//what fake row number does this product go to?
					var toRowNum = Math.floor( (i) / 4 ) + 1;
					
					//now correct the fake row class for this regular product
					var attr = $(thisProd).attr("class");
					
					if( (typeof attr !== typeof undefined) && (attr !== false) && attr.indexOf("fakeRow_") != -1 ){
						$(thisProd).attr("class", $(thisProd).attr("class").replace(/fakeRow_(\d+)/g, "fakeRow_"+toRowNum) );
					}
					
					//
					finalFakeRow = Math.max( HLmaxLen, toRowNum);
				}
			}
			
			//if( $.contains( $(".isHookLogic-false .browse-sections-top .products.transactional"), $(".isHookLogic-true") ) == false ){
			if( $.contains( $(".isHookLogic-false"), $(".isHookLogic-spacer") ) == false ){
				//$(".isHookLogic-true").clone().prependTo( $(".isHookLogic-false .browse-sections-top .products.transactional") );
				
				var hltH = $(".isHookLogic-true").height() - 55;
				
				console.log("hltH = " + hltH);
				
				$(".isHookLogic-false .browse-sections-top .products.transactional").prepend("<div class='isHookLogic-spacer' style='height:"+hltH+"px; '></div>");
			}
			
			//get started
			var tallestColumnH = 0;
			
			var colLength = 0;
			
			var HLselectorClass = '';
			
			/*height fixes*/
			for(var i=0; i<(finalFakeRow+1); i++){
				tallestColumnH = 0;
				
				colLength = $(".browse-sections-top li.portrait-item.fakeRow_"+i).length;
				
				HLselectorClass = ".browse-sections-top li.portrait-item.fakeRow_"+i+", .isHookLogic-true li.portrait-item.fakeRow_"+i;
				
				//$(".browse-sections-top li.portrait-item.fakeRow_"+i).each(function(index3){
				$(HLselectorClass).each(function(index3){
					tallestColumnH = Math.max(tallestColumnH, $(this).height());
					
					if( (index3 == (colLength-1)) && (i >= HLmaxLen) ){						
						//$(this).css("background-color", "#88FF88");
					}else{
						//$(this).css("background-color", "pink");
					}
					
					//console.log("werewolves of london = " + i, ", index3 = " + index3);
				});
				
				//set this to be the height of the tallest row item
				$(HLselectorClass).css("min-height", tallestColumnH);
				
				if(i >= HLmaxLen){
					//$(".browse-sections-top .sectionContent li.portrait-item.fakeRow_"+i).last().addClass('lastInLine');
				}
			}//end for var i=0; ...
			
			//correct for when there are filter tags
			if( $('.filterTags').length > 0 ){
				$(".isHookLogic-true").first().css("margin-top", $('.filterTags').first().height() + "px" );
			}else{
				$(".isHookLogic-true").first().css("margin-top", "0px" );
			}
		}//end adProductSection.fixThoseHooklogicDisplayHeights

		topSections.listen();
		bottomSections.listen();
    
		//new for HookLogic
		adProductSection.listen();
    
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
		
		console.log("x = ", x);
		
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
		
		console.log("window.FreshDirect.browse.data.pager.activePage = " + window.FreshDirect.browse.data.pager.activePage);
		
		//update the page beacon url
		if( xhr.responseJSON !== undefined && xhr.responseJSON.adProducts !== undefined &&
		xhr.responseJSON.adProducts.pageBeacon !== undefined &&  xhr.responseJSON.adProducts.pageBeacon != null &&
		xhr.responseJSON.adProducts.pageBeacon.length > 3 ){
			window.FreshDirect.browse.data.adProducts.pageBeacon = xhr.responseJSON.adProducts.pageBeacon;
		}
		
		if( window.isHLchangable == true || (settings.url.indexOf("/api/filter?data=") != -1 ) ){
			adProductSection.fixThoseHooklogicDisplayHeights();
		}
		
		window.isHLchangable = false;
	});

	fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
