<?php
/*
Plugin Name: XTH
Plugin URI: http://www.kilroyjames.co.uk/2008/09/xhtml-to-html-wordpress-plugin
Version: 2.0
Description: This plugin filters XHTML output to create a document which is valid HTML 4.01 Strict.
Author: John Kilroy
Author URI: http://www.kilroyjames.co.uk
Thanks: Leanne.
*/

    # Please remember that XTH is concerned with validty not aesthetics - it removes only those XHTML elements
    # that break W3C validity testing using the doctype HTML 4.01 Strict.
    # So, for instance, the "xml:lang" attribute is ignored because it doesn't affect validity and may in fact provide 
    # useful information to the browser about document encoding.

function HTMLify($buffer) { 

    if (is_feed()) return $buffer;
    
    
    # Patterns for finding basic XHTML document features. Expand this list as necessary and if for some reason you want to
    # change the doctype (ie. when HTML 5.0 is properly released), you can do it here.

    $x[0] = '/XHTML 1.0 Transitional|XHTML 1.0 Strict|XHTML 1.0 Frameset|XHTML 1.1|XHTML Basic 1.0|XHTML Basic 1.1/im';
    $x[1] = '/xhtml1\/DTD\/xhtml1-transitional.dtd|xhtml1\/DTD\/xhtml1-strict.dtd|xhtml11\/DTD\/xhtml11.dtd|xhtml-basic\/xhtml-basic10.dtd|xhtml-basic\/xhtml-basic11.dtd/im';
    $x[2] = '/xmlns="http:\/\/www.w3.org\/1999\/xhtml"/';

    $h[0] = 'HTML 4.01';
    $h[1] = 'html4/strict.dtd';
    $h[2] = '';
    
    
    # OK, my aim at first was to write the following loop as a single regular expression. I failed.
    # If is is actually possible to write one, I think it would be faster but I don't know by how much. 
    # I failed because a single regex will require complex lookbehind and lookahead assertions and PHP
    # (unlike the less civilised but more powerful Perl) seems to require the lookbehind assertions to be 
    # a fixed length, which makes the whole thing monolithically complicated - at least more than I 
    # can deal with right now - it also seems to make the code less readable than a string-theory equation.
    # So, if there are any PHP-Physics majors out there with a demon insight into PHP's PCRE then be
    # my guest to reformulate the following loop into an expression. As I say though, I'm not really 
    # sure it's actually possible in PHP.
    
    # Caveats
    # OK, this version seems to successfully avoid parsing inline Javascripts and external Javascript libraries.
    # This is necessary because the XHTML closing sequence is a valid pattern in javascript regex, and removing
    # it from those places would bugger up those libraries.
    # The downside to that is that it's then up to you to make sure that any JS you use doesn't
    # output any XHTML, as XTH no longer 'fixes' it for you.
    # Same goes for any other external libs that you or your plugins load.
    
    # Note that XTH doesn't address javascript URLs of the form <a href="javascript:foo()">. This is for
    # efficiency - and also because I haven't ever seen a regex used in that context, but who knows, 
    # someone might try it. I might add this as an option in a future version.
    
    # Don't forget!
    # A single XHTML closer anywhere in the output will fail HTML 4.01 validation
    # Resulting in tag soup and defeating the object of the whole exercise. 
    
    # Your RSS feeds should be fine, as far as I know Wordpress only uses plugins to filter its HTML output.
    
    # Problems, bugs and comments to me please. This is a complete rewrite of the plugin so
    # we're back to square one with the list of problems and required tweaks.

    $output = '';    
    $safe_to_replace = 1;

    $all = explode ("\n", $buffer);
    
    foreach ($all as $i){

		# Javascript
		if ( preg_match('/<\s+script.*text\/javascript/i', $i) ) {
		  $safe_to_replace = 0;
		}
		elseif ( preg_match('/<\s+\/script\s+>/i', $i) ) {
		  $safe_to_replace = 1;
		}

		# RSD files
		if ( preg_match('/<\s+rsd/i', $i)  ) {
		  $safe_to_replace = 0;
		}	
		elseif ( preg_match('/<\s+\/rsd\s+>/i', $i) ) {
		  $safe_to_replace = 1;
		}	
		elseif ( preg_match('/rel="EditURI/i', $i) ) {
		  # This one's necessary because PHP seems somehow to treat externally loaded LINKs as continuations 
		  # of the same line - the </rsd> tag doesn't reset $safe_to_replace, which it should.
		  # Think this might a problem with the way data is serialised and sent to explode() by ob_start().
		  $safe_to_replace = 1;
		}	

		# Manifests
		if ( preg_match('/<\s+manifest/i', $i) ) {
		  $safe_to_replace = 0;
		}	
		elseif ( preg_match('/<\s+\/\s*manifest\s+>/i', $i) ) {
		  $safe_to_replace = 1;
		}

		# now make the end tag '/>' replacements
		if ( $safe_to_replace ) {
			$i = preg_replace('/<script([^>]*)\/\s*>/', '<script${1}></script>', $i);
			$pat = '/\s*\/\s*>/';
			$rep = '>';
			$i = preg_replace($pat, $rep, $i );
		}
		
		# put the newlines back so the source file can be read sanely.
		$output .= $i . "\n";

    }
    

	return preg_replace($x, $h, $output);
}

  if ( is_admin() ) {
  	return TRUE;
  }
  else {
    ob_start("HTMLify");
  }

?>
