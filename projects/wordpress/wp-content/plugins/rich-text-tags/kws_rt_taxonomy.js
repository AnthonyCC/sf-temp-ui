// For use with Rich Text Tags, Categories, and Taxonomies WordPress Plugin

//this is for compatability with cforms
var purl = '';

jQuery(document).ready(function($) { 
		
		$("textarea#description, textarea#category_description").attr('id', 'content');
		$("label[for='description'], label[for='category_description']").attr('for', 'content');
		
		// Add the Rich Text Editor to the "description" textarea
		tinyMCE.execCommand('mceAddControl',false,'content');
		tinyMCE.settings['save_callback'] = function (e,c,body) { return c; }
		
		$('label[for="content"]').parent().append('<div id="toggleRichText" class="hide-if-no-js"><p><a href="#">Toggle Rich Text Editor</a></p></div>');
		
		$('#toggleRichText').click(function(e) {  
			e.preventDefault();
			tinyMCE.execCommand('mceToggleEditor',false,'content');
			return false;
		});	
		
		// Move floating media buttons into the media buttons div
		$('#editor-toolbar').remove().prependTo($("textarea#content").parent());
		$('.wrap form a[href*=TB_inline]:has(img)').remove().appendTo('#media-buttons');
		$('.wrap form a[href=#]:has(img)').remove().appendTo('#media-buttons');
		
});	/* end ready() */	