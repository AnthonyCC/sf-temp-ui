/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
  
  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

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
        $popupBody = $(popup).find(".globalnav-popup-content"),
        $content = $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first();

    if($popupBody.length && $content.length){
      removeSubItemHighlights();

      // fancy: hide down arrows from superdepartment level menu items
      $popupBody.find(".arrow-down").hide();

      // remove previously shown departments
      $popupBody.children(".animcontainer").remove();

      // append dept content related to pointed sub dept
      $popupBody = $popupBody.append("<div class='animcontainer'></div>").children('.animcontainer');
      $content.clone().appendTo($popupBody);
      $content = $popupBody.children(".deptcontainer");

      // bring up popup content to animate later
      $content.css('top', '-' + $popupBody.outerHeight() + "px");

      // stop animations before
      $content.stop();
      $("#globalnavpopup").removeClass("shadow-for-superdepart");

      if(subDown === false){
         $content.animate({
          top: "0px"
          }, 350, "easeOutQuad", function(){
        	 $("#globalnavpopup").addClass("shadow-for-superdepart");
             subDown = true;
            $(".seasonal-media").fadeIn(200);
          });
      }
      else{
    	$("#globalnavpopup").addClass("shadow-for-superdepart");
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

    // precautions
    if(!$popupBody.length){ return; }

    repos($topnav, $ghost, $popup);

    // re-init subdown animation logic
    subDown = false;

    // fill popup with proper content
    $popupBody.html("");
    $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first().clone().appendTo($popupBody);

    removeTopItemHighlights();
    addTopItemHighlight($ghostMenuItem.find("a"));

    // show items
    $overlay.show();
    $ghost.show();  

    if($t.hasClass("top-item")){
      // bring up popup content to animate later
      $popupBody.css('top', '-' + $popupBody.outerHeight() + "px");

      // stop animations before
      $popupBody.stop();
      $("#globalnavpopup").removeClass("shadow-for-superdepart");
      
      if(down === false){
        $popupBody.animate({
          top: "0px"
          }, 350, "easeOutQuad", function(){
          	$("#globalnavpopup").addClass("shadow-for-superdepart");
             down = true;
            $(".seasonal-media").fadeIn(200);
          });
      }
      else{
    	$("#globalnavpopup").addClass("shadow-for-superdepart");
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

  /**
   * repositioning content ghost and popup according to topnav
   */
  function repos($topnav, $ghost, $popup){

    if(!$topnav.length || !$ghost.length || !$popup.length){
      return;
    }

    // cloning topnav menu to ghost
    $ghost.css({
        top: $topnav.offset().top,
        left: $topnav.offset().left,
        width: '968px',
        height: '50px',
        'z-index': '1002'
    });
      
    $popup.css({
        top: $topnav.offset().top + $topnav.height(),
        left: $topnav.offset().left,
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

  // load up only if we find new global nav and trigger elements in DOM
  if($(topnav).length > 0 && $(trigger).length > 0){
    render();
  }

}(FreshDirect));
