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
				//console.log("$elem = ", $elem);
				
				//$elem.append('<img src="'+$elem.data('hooklogic-beacon-impress')+'" style="display: none;" />');
			}
			
			//if there are some hookLogic products returned
			if( $(HLprodSelector).length > 0 ){
				finalFakeRow = HLmaxLen;
				
				$(HLprodSelector).each(function( index ) {
					
					//hide the hooklogic product if it is sixth or greater or if it is beyond page 1
					if(index > hookLogicRowLimit || ($(paginationSelectedSelector).attr("data-page") != "1") ){
						$(this).hide();
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
							$(this).attr("class", $(this).attr("class").replace(/fakeRow_(\d+)/g, "fakeRow_"+index) );
						});
					}//end if/else index > hookLogicRowLimit ...
				});
				
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
					$(thisProd).attr("class", $(thisProd).attr("class").replace(/fakeRow_(\d+)/g, "fakeRow_"+toRowNum) );
					
					//
					finalFakeRow = Math.max( HLmaxLen, toRowNum);
				}
			}
			
			//if( $.contains( $(".isHookLogic-false .browse-sections-top .products.transactional"), $(".isHookLogic-true") ) == false ){
			if( $.contains( $(".isHookLogic-false"), $(".isHookLogic-true") ) == false ){
				$(".isHookLogic-true").clone().prependTo( $(".isHookLogic-false .browse-sections-top .products.transactional") );
			}
			
			//get started
			var tallestColumnH = 0;
			
			var colLength = 0;
			
			/*height fixes*/
			for(var i=0; i<(finalFakeRow+1); i++){
				tallestColumnH = 0;
				
				colLength = $(".browse-sections-top li.portrait-item.fakeRow_"+i).length;
				
				$(".browse-sections-top li.portrait-item.fakeRow_"+i).each(function(index3){
					tallestColumnH = Math.max(tallestColumnH, $(this).height());
					
					if( (index3 == (colLength-1)) && (i >= HLmaxLen) ){
						console.log( "colLength = " + colLength );
						
						//$(this).css("background-color", "green");
					}
				});
				
				//set this to be the height of the tallest row item
				$(".browse-sections-top li.fakeRow_"+i).css("min-height", tallestColumnH);
				
				if(i >= HLmaxLen){
					//$(".browse-sections-top .sectionContent li.portrait-item.fakeRow_"+i).last().addClass('lastInLine');
				}
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
	$(document).on('page-change', function(){
		window.isHLchangable = true;
	});
  
	//fires upon using search box
	//$(document).on('change', function(){
	$(".tabs li span, .searchbutton, .menuBox li span, .menupopup li span, .sorter button span").click(function(){
		window.isHLchangable = true;
	});

	//this always fires upon each set of products load success
	$( document ).ajaxSuccess(function(e) {
		if( window.isHLchangable == true ){
			adProductSection.fixThoseHooklogicDisplayHeights();
		}
		
		window.isHLchangable = false;
	});

	fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
