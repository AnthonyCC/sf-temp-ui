=== WordPress HTTPS ===
Contributors: Mvied
Donate link: https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=6ZL95VTJ388HG
Tags: ssl, shared ssl, private ssl, http, https, admin, administration, secure admin, login, secure login, security, encryption, encrypted
Requires at least: 2.7.0
Tested up to: 3.0.3
Stable tag: 1.8.5

WordPress HTTPS is intended to be an all-in-one solution to using SSL on WordPress sites. Free support provided!

== Description ==

WordPress HTTPS is intended to be an all-in-one solution to using SSL on WordPress sites.

Here are the currently available features:

<ul>
 <li>Change all internal scripts, stylesheets and images to HTTPS if the page is being viewed via HTTPS to prevent partially encrypted errors.</li>
 <li>Supports Shared and Private SSL.</li>
 <li>Force HTTPS on only the pages you need to be HTTPS.</li>
 <li>Force pages to HTTP that have not been forced to HTTPS.</li>
 <li>Change external elements to HTTPS. The plugin will check for the existence of external elements over HTTPS and, if available, will change them to HTTPS.</li>
 <li>Override the external element HTTPS check. If you know that all external elements can be loaded over HTTPS, this option will save some load time on your pages.</li>
 <li>Disable WordPress 3.0+ from changing all of your page, category and post links to HTTPS.</li>
</ul>

There are a few things that can cause a page to still be loaded insecurely after enabling this plugin.
<ul>
 <li>YouTube videos - YouTube does not allow videos to be streamed over HTTPS.</li>
 <li>Google Maps - Loading Google maps over HTTPS requires a Google Maps API Premiere account. (<a href="http://code.google.com/apis/maps/faq.html#ssl">source</a>)</li>
 <li>External elements that can not be delivered over HTTPS - I would suggest removing these or hosting them on your own server.</li>
</ul>

If you're still having trouble getting your site to load over HTTPS, or any other issues, please <a href="http://wordpress.org/tags/wordpress-https#postform">start a support topic</a> and I will do my best to assist you.

== Installation ==

1. Upload `wordpress-https.php` to the `/wp-content/plugins/` directory
1. Activate the plugin through the 'Plugins' menu in WordPress.

== Frequently Asked Questions ==

= How do I make my whole website HTTPS? =

To make your entire website HTTPS, you simply need to change your home url and site url to HTTPS instead of HTTP. Please read <a href="http://codex.wordpress.org/Changing_The_Site_URL">how to change the site url</a>.

= How do I make only my administration panel HTTPS? =

WordPress already has this process well documented. Please read <a href="http://codex.wordpress.org/Administration_Over_SSL">how to set up administration over SSL</a>.

= How do I make only certain pages HTTPS? =

As of version 1.5, this plugin grants that ability. Within the Publish box on the add/edit post screen, a checkbox for 'Force SSL' has been added to make this process easy. See Screenshots if you're having a hard time finding it.

= Will this plugin fix all of my partially encrypted errors? =

In most cases, yes. There are limitations to what this plugin can fix. Here are a few that I've run into:
<ul>
 <li>YouTube videos - YouTube does not allow videos to be streamed over HTTPS.</li>
 <li>Google Maps - Loading Google maps over HTTPS requires a Google Maps API Premiere account. (<a href="http://code.google.com/apis/maps/faq.html#ssl">source</a>)</li>
 <li>External elements that can not be delivered over HTTPS - I would suggest removing these or hosting them on your own server.</li>
</ul>

== Screenshots ==
1. WordPress HTTPS Settings screen
2. Force SSL checkbox added to add/edit posts screen

== Changelog ==

= 1.8.5 =
* In version 1.8.5, when a page is forced to HTTPS, any links to that page will always be HTTPS, even when using the 'Disable Automatic HTTPS' option. Likewise, when the 'Force SSL Exclusively' option is enabled, all links to pages not forced to HTTPS will be changed to HTTP on HTTPS pages.
* Updated RegEx's for more complicated URL's.
* Bug fix - When in the admin panel, only link URL's are changed back to HTTP again.
* Added support for using Shared SSL together with the FORCE_SSL_ADMIN and FORCE_SSL_LOGIN options.
= 1.8.1 =
* Re-enabled the canonical redirect for WordPres sites not using Shared SSL.
= 1.8 =
* Fixed cross-browser CSS issue on plugin settings page.
* Corrected and updated plugin settings validation.
* Lengthened the fade out timer on messages from the plugin settings page from 2 to 5 seconds so that the more lengthy error messages could be read before the message faded.
* If viewing an admin page via SSL, and your Home URL is not set to HTTPS, links to the front-end of the website will be forced to HTTP. By default, WordPress changes these links to HTTPS.
* When using Shared SSL, any anchor that links to the regular HTTPS version of the domain will be changed to use the Shared SSL Host.
* Added embed and param tags to the list of tags that are fixed by WordPress HTTPS. This is to fix flash movies.
= 1.7.5 =
* Bug fix - When using 'Latest Posts' as the front page, the front page would redirect to HTTP when viewed over HTTPS even if the 'Force SSL Exclusively' option was disabled.
* Prevented the 'Disable Automatic HTTPS' option from parsing URL's in the admin panel.
* General code cleanup and such.
= 1.7 =
* Bug fix - External URL's were not being forced to HTTPS after the last update.
* Added the functionality to correct relative URL's when using Shared SSL.
* General code cleanup and such.
= 1.6.5 =
* Added support for Shared SSL.
= 1.6.3 =
* Changed the redirection check to use `template_redirect` hook rather than `get_header`.
= 1.6.2 =
* Tag links were not being set back to HTTP when the 'Disable Automatic HTTPS' option was enabled.
= 1.6.1 =
* Bug fix - front page redirection was causing issues when a static page was selected for the posts page.
= 1.6 =
* Added the ability to force the front page to HTTPS.
* Multiple enhancements to core functionality of plugin. Mostly just changing code to integrate more smoothely with WordPress.
* Enhancements have been made to the plugin's settings page.
= 1.5.2 =
* Fixed a bug that would prevent stylesheets from being fixed if the rel attribute came after the href attribute. Bug could have also caused errors with other tags.
= 1.5.1 =
* Added input elements with the type of 'image' to be filtered for insecure content.
= 1.5 =
* Added the ability to force SSL on certain pages.
* Also added the option to exclusively force SSL on certain pages. Pages not forced to HTTPS are forced to HTTP.
* Plugin now filters the `bloginfo` and `bloginfo_url` functions for HTTPS URL's when the 'Disable Automatic HTTPS' option is enabled in WordPress 3.0+.
= 1.0.1 =
* Bug fix.
= 1.0 =
* Major modifications to plugin structure, efficiency, and documentation.
* Added the option to disable WordPress 3.0+ from changing all of your page, category and post links to HTTPS.
= 0.5.1 =
* Bug fix.
= 0.5 =
* Due to increasing concerns about plugin performance, the option to bypass the HTTPS check on external elements has been added.
= 0.4 =
* Plugin functions converted to OOP class.
* The plugin will now attempt to set the allow_url_fopen option to true with `ini_set` function if possible.
= 0.3 =
* Added the option to change external elements to HTTPS if the external server allows the elements to be accessed via HTTPS.
= 0.2 =
* Changed the way in which HTTPS was detected to be more reliable.
= 0.1 =
* Initial Release.

== Upgrade Notice ==
= 1.7 =
1.6.5 created a bug in which external elements were no longer forced to HTTPS. Please update to fix this.
= 1.6.1 =
Version 1.6.1 fixes a bug with using a static page for the posts page.
= 1.0.1 =
Version 1.0.1 fixes a bug in 1.0 that made it to release. Apologies!
= 1.0 =
Version 1.0 gives you the ability to disable WordPress 3.0+ from changing all of your page, category and post links to HTTPS.
= 0.5.1 =
Fixes `PHP Warning:  Invalid argument supplied for foreach()` error.
= 0.3 =
Version 0.3 gives you the option to change external elements to HTTPS if the external server allows the elements to be accessed via HTTPS.
= 0.2 =
Version 0.1 did not correctly detect HTTPS on IIS and possibly other servers. Please update to version 0.2 to fix this issue.