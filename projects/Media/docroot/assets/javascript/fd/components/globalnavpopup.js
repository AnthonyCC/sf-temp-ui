/*global jQuery,common, FDModalDialog */
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {

  var console = console || window.top.console || { log:function(){} };
  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
	  $.fn.getHiddenDimensions = function(includeMargin) {
		  var $item = this,
		  	props = { position: 'absolute', visibility: 'hidden', display: 'block' },
	        dim = { width:0, height:0, innerWidth: 0, innerHeight: 0, outerWidth: 0, outerHeight: 0 },
	        $hiddenParents = $item.parents().andSelf().not(':visible'),
	        includeMargin = (includeMargin == null)? false : includeMargin;

	    var oldProps = [];
	    $hiddenParents.each(function() {
	        var old = {};
	
	        for ( var name in props ) {
	            old[ name ] = this.style[ name ];
	            this.style[ name ] = props[ name ];
	        }
	
	        oldProps.push(old);
	    });
	
	    dim.width = $item.width();
	    dim.outerWidth = $item.outerWidth(includeMargin);
	    dim.innerWidth = $item.innerWidth();
	    dim.height = $item.height();
	    dim.innerHeight = $item.innerHeight();
	    dim.outerHeight = $item.outerHeight(includeMargin);
	
	    $hiddenParents.each(function(i) {
	        var old = oldProps[i];
	        for ( var name in props ) {
	            this.style[ name ] = old[ name ];
	        }
	    });
	
	    return dim;
	}

  /**
   * This module used to build all Global Navigation popups
   *
   * There are two categories: first level and second level popups
   */

  var overlay = "[data-component='globalnav-overlay']"; // overlay under popup elements  will handle close cases
  var ghost = "[data-component='globalnav-ghost']"; // ghost is a copy of original topnav before the overlay
  var popup = "[data-component='globalnav-popup']"; // generated popup for depts and superdepts 

  var topnav = "[data-component='globalnav-menu']"; // the whole nav element put to DOM from media 
  
  var trigger = "[data-component='globalnav-item']"; // triggers department level opens
  var subTrigger = "[data-component='globalnav-submenu-item']"; // triggers department level opens in subdepartment

  var down = false; // handles first level animation reopens
  var subDown = false; // handles second level animation reopens

  /**
   * open second level departments with animations
   */
  function openSub(e){
    e.stopPropagation();

    var $t = $(e.currentTarget),
    	$topnav = $(topnav),
        $popup = $(popup),
        $popupBody = $(popup).find(".globalnav-popup-content"),
        $content = $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first();

	if ($content.is(':animated')) { return; }

    if($popupBody.length && $content.length){
      removeSubItemHighlights();

      // fancy: hide down arrows from superdepartment level menu items
      $popupBody.find(".arrow-down").hide();

      // remove previously shown departments
      //$popupBody.children(".animcontainer").remove();
      $popup.children(".deptcontainer").remove();

      // append dept content related to pointed sub dept
      //$popupBody = $popupBody.append("<div class='animcontainer'></div>").children('.animcontainer');
      $content.clone().appendTo($popup);
      $content = $popup.children(".deptcontainer");
      

      repos($t.parent('.subdepartments_cont'), null, $content, $t);

      // bring up popup content to animate later
      $content.css('top', '-' + ($content.outerHeight() + $popupBody.outerHeight()) + "px");
      $popup.css('margin-bottom', '-'+$content.outerHeight());

      // stop animations before
      $content.stop();
      //$("#globalnavpopup").removeClass("shadow-for-superdepart");

      if(subDown === false){
          $popupBody.css('overflow', 'visible');
    	  $content.css('margin', '');
    	  $content.css('margin-bottom', '3px');
    	  $popupBody.css('padding-bottom', '');
         $content.animate({
          top: "0px"
          }, 350, "easeOutQuad", function(){
        	 //$("#globalnavpopup").addClass("shadow-for-superdepart");
             subDown = true;
            $(".seasonal-media").fadeIn(200);
          });
      }
      else{
    	//$("#globalnavpopup").addClass("shadow-for-superdepart");
        $content.css('top', '0px');
        $(".seasonal-media").show();
      }
    }

    // stick with hover color on supdept level item
    addSubItemHighlight($(this).find("a"));

    // fancy: show related down arrow on supdept level item
    $(this).find(".arrow-down").show();
  }
 
  /**
   * close globalnav popups et all
   */
  function close(e){
    e.stopPropagation();

    $(overlay).hide();
    $(ghost).hide();
    removeTopItemHighlights();
    
    $(popup).find(".globalnav-popup-content").html("");
    $(popup).find(".deptcontainer").remove();

    // init animation logic for both levels
    down = false;
    subDown = false;
  }
 
  /**
   * open first department level popup with animations
   */
  function open(e){
    e.stopPropagation();

    var $topnav = $(topnav),
        $ghost = $(ghost),
        $overlay = $(overlay),
        $popup = $(popup),
        $t = $(e.currentTarget),
        $ghostMenuItem = $ghost.find("[data-id='" + $t.data('id') + "']"),
        $popupBody = $popup.find(".globalnav-popup-content");

    
    // close all autocomplete popups
    $('[data-component="autocomplete"]').autocomplete("close");

    // close all select dropdowns
    $('select').blur();


    // precautions
    if(!$popupBody.length){ return; }
    if ($popupBody.is(':animated')) { return; }

    //repos($topnav, $ghost, $popup);

    // re-init subdown animation logic
    subDown = false;

    // fill popup with proper content
    $popupBody.html("");
    $popup.find(".deptcontainer").remove();
    $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first().clone().appendTo($popupBody);


    repos($topnav, $ghost, $popup, $t);
    //realigner($topnav, $popup, $t);

    removeTopItemHighlights();
    addTopItemHighlight($ghostMenuItem.find("a"));

    // show items
    $overlay.show();
    $ghost.show();
    $popupBody.css('overflow', 'hidden');
    $popupBody.css('padding-bottom', '3px');
    //$popupBody.css({'padding-bottom': '3px', 'margin-top': '-3px'});
    
    if($t.hasClass("top-item")){
        // bring up popup content to animate later
        $popupBody.css('top', '-' + $popupBody.outerHeight() + "px");

        // stop animations before
        $popupBody.stop();
        //$("#globalnavpopup").removeClass("shadow-for-superdepart");
        
        if(down === false){
          $popupBody.animate({
            top: "0px"
            }, 350, "easeOutQuad", function(){
            	//$("#globalnavpopup").addClass("shadow-for-superdepart");
               down = true;
              $(".seasonal-media").fadeIn(200);
              //$popupBody.css('overflow', 'visible');
            });
        } else {
        	//$("#globalnavpopup").addClass("shadow-for-superdepart");
          $popupBody.css('top', '0px');
          $(".seasonal-media").show();
        }
      }
    
  }
  
  /**
  * appends globalnav popup elements to DOM
  */
  function render(){
    var $topnav = $(topnav),
        $ghost = $("<div class='popupcontentghost' data-component='globalnav-ghost'></div>"),
        $popup = $("<div data-component='globalnav-popup' id='globalnavpopup' class='globalnav-popup'><div class='globalnav-popup-content'></div></div>"),
        $overlay = $("<div class='popupcontentoverlay' data-component='globalnav-overlay'></div>");

    $(document.body).append($overlay);
    $(document.body).append($ghost);
    $(document.body).append($popup);

    //dynamically adjust contents and their alignments
    //realigner();

    // attach event handlers
    $overlay.on('mouseover', close); // attach works only this way in iPad/Safari
    $(document).on('mouseover', trigger, open);
    $(document).on('mouseover', subTrigger, openSub);
    $(window).on('resize', function(){ repos($(topnav), $(ghost), $(popup)); });
    
    // set init position
    repos($topnav, $ghost, $popup);

    // we'll show content ghost before the real topnav so we init it now
    $topnav.clone().appendTo($ghost);
  }

function getElemDim($elem){
	var retDim = {
		width: 0,
		height: 0,
		offset: {
			top: 0,
			left: 0
		},
		center: 0
	};
	
	if ($elem !== undefined && $elem != null) {
		retDim.width = $elem.outerWidth();
		retDim.height = $elem.outerHeight();
		retDim.offset = $elem.offset();
		retDim.center = (retDim.width / 2) + retDim.offset.left;
	}
	
	return retDim;
}

/* adjust left/right positioning of popup elements and their children */
function realigner($topnav, $popup, $t) {
	if ($t === undefined || $t === null) { return $topnav.offset().left; }
	//console.log('realignerA', $topnav, $popup, $t);
  
	var navDim = getElemDim($topnav);
	var tarDim = getElemDim($t);
	
	var $popupContRef = $('#globalnavpopup');
	var $imgRef = $popupContRef.find('.heroimg_cont').first();
	var $deptContRef = $popupContRef.find('.department').first();
	var $subDeptsRef = $popupContRef.find('.subdepartments').first();
	var isRightOfNavCenter = false;
	var possibleOffset = -1;
	
	//check alignments
	if (tarDim.center < navDim.center) { //left of center
		//adjust img to right in contents
		if ($imgRef.length !== 0) {
			$imgRef.css('float', 'right');
		}
	} else { //right of center
		isRightOfNavCenter = true;
		//return $topnav.offset().left;
	}
  
	if ($imgRef.length !== 0) { //img & dept
		
		//check if image can and needs to shrink to accomidate space
		if ($popupContRef.outerWidth() > navDim.width) {
			var imgWidthAdjust = navDim.width - $deptContRef.outerWidth();
			if (imgWidthAdjust < 0) { imgWidthAdjust = 0; }
			
			$imgRef.css('width', imgWidthAdjust);
		}

		//now re-align the popup to the nav item if possible
		if (isRightOfNavCenter) {
			//attempt to align popout to RIGHT side of selected nav item
			possibleOffset = (tarDim.offset.left + tarDim.width) - $popupContRef.outerWidth();
			if (possibleOffset > navDim.offset.left) {
				//we have space
				return possibleOffset;
			}
		} else {
			//attempt to align popout to LEFT side of selected nav item
			possibleOffset = (navDim.offset.left + navDim.width) - tarDim.offset.left;
			if ($popupContRef.outerWidth() < possibleOffset) {
				//we have space
				return tarDim.offset.left;
			}
		}
	}
	if ($subDeptsRef.length !== 0) { //superdept, subdept popup
		//console.log('subdepts');
		$subDeptsRef.parent('.subdepartments_cont').css('width', $topnav.outerWidth());
		var subDeptsDim = getElemDim($subDeptsRef);
		//see if we can center under selected nav item
		possibleOffset = (tarDim.center - navDim.offset.left) - (subDeptsDim.width / 2);
		//console.log(possibleOffset);
		if (possibleOffset > 0) {
			//we have space
			$subDeptsRef.css('left', possibleOffset-(tarDim.width/2));
		}
	}
	
	//no space
	return navDim.offset.left;
	
	//console.log('realignerB', ($topnav.offset().left+$topnav.outerWidth()), navCenter, $t);  
}
  
  /**
   * repositioning content ghost and popup according to topnav
   */
  function repos($topnav, $ghost, $popup, $t){

    if(!$topnav.length || !$popup.length){
      return;
    }
    
    var alignAt = realigner($topnav, $popup, $t);

    if ($ghost !== null && $ghost.length) {
	    // cloning topnav menu to ghost
	    $ghost.css({
	        top: $topnav.offset().top,
	        left: $topnav.offset().left, //don't change this, ghost is on globalnav
	        height: '50px',
	        'z-index': '1002'
	    });
    }
      
    $popup.css({
        top: $topnav.offset().top + $topnav.height(),
        left: alignAt
    });
  }

  function removeTopItemHighlights(){
    $("[data-component='globalnav-item']").find("a.opened").removeClass("opened");
  }

  function addTopItemHighlight(elem){
    $(elem).addClass("opened");
  }

  function removeSubItemHighlights(){
    $(popup).find("a.subopened").removeClass("subopened");
  }
  
  function addSubItemHighlight(elem){
    $(elem).addClass("subopened");
  }
  
  function showTutorial() {
	  FDModalDialog.openUrl('/media/layout/nav/globalnav/tutorial/slide_01.html',' ', 970,600, 'globalnav-tutorial');
  }

  // load up only if we find new global nav and trigger elements in DOM
  if($(topnav).length > 0 && $(trigger).length > 0){
    render();
    
    if (fd.globalnav.hasOwnProperty('showTutorial') && fd.globalnav.showTutorial === true) {
    	showTutorial();
    } 
  }

}(FreshDirect));
