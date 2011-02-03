jQuery(document).ready(function() {
 jQuery('#message-body').fadeOut();
 
 jQuery('#wordpress-https').submit(function() {
  jQuery('#submit-waiting').show();
 });
 //
 var options = {
  data: { ajax: '1'},
  success: function(responseText, textStatus, XMLHttpRequest) {
   jQuery('#message-body').html(responseText);
   // .animate is used to delay the fadeOut by 5 seconds
   jQuery('#message-body').fadeIn().animate({opacity: 1.0}, 5000).fadeOut();
   jQuery('#submit-waiting').hide();
  }
 };
 
 jQuery('#wordpress-https').ajaxForm(options);
});