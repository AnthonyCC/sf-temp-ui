<?php
/*
Plugin Name: WordPress HTTPS
Plugin URI: http://mvied.com/projects/wordpress-https/
Description: WordPress HTTPS is intended to be an all-in-one solution to using SSL on WordPress sites. Free support provided!
Author: Mike Ems
Version: 1.8.5
Author URI: http://mvied.com/
*/

/**
* Class for the WordPress plugin WordPress HTTPS
*
* @author  Mike Ems
* @package WordPressHTTPS
* @copyright Copyright 2010
*
*/

if ( !class_exists('WordPressHTTPS') ) {
 class WordPressHTTPS {

  /**
   * Plugin version
   *
   * @var int
   */
  var $plugin_version = '1.8.5';

  /**
   * Plugin URL
   *
   * @var string
   */
  var $plugin_url;

  /**
   * HTTP URL
   *
   * @var string
   */
  var $http_url;

  /**
   * HTTPS URL
   *
   * @var string
   */
  var $https_url;

  /**
   * Shared SSL
   *
   * @var boolean
   */
  var $shared_ssl = 0;

  /**
   * Default options
   *
   * @var array
   */
  var $options_default;

  /**
   * Initialize plugin
   *
   * @param none
   * @return void
   */
  function __construct() {
   // Assign plugin_url
   if ( version_compare( get_bloginfo('version'), '2.8', '>=' ) ) {
    $this->plugin_url = plugins_url('', __FILE__);
   } else {
    $this->plugin_url = WP_PLUGIN_URL . '/' . plugin_basename(dirname(__FILE__));
   }

   // Assign HTTP URL
   $this->http_url = 'http://' . parse_url(get_option('home'), PHP_URL_HOST);
   // Assign HTTPS URL
   $this->https_url = $this->replace_http($this->http_url);

   // Shared SSL
   if (get_option('wordpress-https_sharedssl') == 1 && get_option('wordpress-https_sharedssl_host') != '') {
    // Turn on Shared SSL
    $this->shared_ssl = 1;
    // Assign HTTPS URL to Shared SSL Host
    $this->https_url = get_option('wordpress-https_sharedssl_host');
    // Prevent WordPress from causing a redirect loop
    remove_filter('template_redirect', 'redirect_canonical');
   }

   // Define default options
   $this->options_default = array(
    'wordpress-https_internalurls'         => 1, // Force internal URL's to HTTPS
    'wordpress-https_externalurls'         => 0, // Force external URL's to HTTPS
    'wordpress-https_bypass'               => 0, // Bypass option to check if external elements can be loaded via HTTPS
    'wordpress-https_disable_autohttps'    => 0, // Prevents WordPress 3.0+ from making all links HTTPS when viewing a secure page.
    'wordpress-https_exclusive_https'      => 0, // Exclusively force SSL on posts and pages with the `Force SSL` option checked.
    'wordpress-https_frontpage'            => 0, // Force SSL on front page
    'wordpress-https_sharedssl'            => 0, // Enable Shared SSL
    'wordpress-https_sharedssl_host'       => '' // Hostname for Shared SSL
   );

   // Start output buffering
   add_action('plugins_loaded', array(&$this, 'buffer_start'));

   // Check for admin/login redirects
   add_action('plugins_loaded', array(&$this, 'admin_redirect'));

   if ( is_admin() ) {
    // Add admin menus
    add_action('admin_menu', array(&$this, 'menu'));

    // Load on plugins page
    if ( $GLOBALS['pagenow'] == 'plugins.php' ) {
     add_filter( 'plugin_row_meta', array( &$this, 'plugin_links' ), 10, 2);
    }

    // Load on Settings page
    if ( @$_GET['page'] == 'wordpress-https' ) {
     wp_enqueue_script('jquery-form', $this->plugin_url . '/js/jquery.form.js', array('jquery'), '2.47', true);
     wp_enqueue_script('wordpress-https', $this->plugin_url . '/js/admin.js', array('jquery'), $this->plugin_version, true);
     wp_enqueue_style('wordpress-https', $this->plugin_url . '/css/admin.css', $this->plugin_version, true);

     // Set default options
     foreach ( $this->options_default as $option => $value ) {
      if ( get_option($option) === false ) {
       add_option($option, $value);
      }
     }
    }

    // Add 'Force SSL' checkbox to add/edit post pages
    add_action('post_submitbox_misc_actions', array(&$this, 'post_checkbox'));
    add_action('save_post', array(&$this, 'post_save'));
   }

   // Check if the page needs to be redirected
   add_action('template_redirect', array(&$this, 'check_https'));

   // Filter HTTPS from links in WP 3.0+
   if ( ( get_option('wordpress-https_disable_autohttps') == 1 && !is_admin() && strpos('https://', get_option('home')) !== true ) ) {
    add_filter('page_link', array(&$this, 'replace_https'));
    add_filter('post_link', array(&$this, 'replace_https'));
    add_filter('category_link', array(&$this, 'replace_https'));
    add_filter('get_archives_link', array(&$this, 'replace_https'));
    add_filter('tag_link', array(&$this, 'replace_https'));
    add_filter('search_link', array(&$this, 'replace_https'));
    add_filter('home_url', array(&$this, 'replace_https'));
    add_filter('bloginfo', array(&$this, 'bloginfo'), 10, 2);
    add_filter('bloginfo_url', array(&$this, 'bloginfo'), 10, 2);
   // If the whole site is not HTTPS, set links to the front-end to HTTP
   } else if ( is_admin() && $this->is_ssl() && strpos('https://', get_option('home')) !== true ) {
    add_filter('page_link', array(&$this, 'replace_https'));
    add_filter('post_link', array(&$this, 'replace_https'));
    add_filter('category_link', array(&$this, 'replace_https'));
    add_filter('get_archives_link', array(&$this, 'replace_https'));
    add_filter('tag_link', array(&$this, 'replace_https'));
    add_filter('search_link', array(&$this, 'replace_https'));
   }

   // End output buffering
   //add_action('shutdown', array(&$this, 'buffer_end'));
  }

  /**
   * Process output buffer
   *
   * @param string
   * @return string
   */
  function process($buffer) {
   if ( $this->is_ssl() ) {
    preg_match_all('/\<(script|link|img|input|form|embed|param)[^>]+((http|https):\/\/[\/-\w\.#]+)[^>]+>/im', $buffer, $matches);

    for ($i = 0; $i<=sizeof($matches[0]); $i++) {
     $html   = $matches[0][$i];
     $type   = $matches[1][$i];
     $url    = $matches[2][$i];
     $scheme = $matches[3][$i];

     if ( ( $type == 'link' && ( strpos($html, 'stylesheet') !== false || strpos($html, 'pingback') !== false ) ) || ( $type == 'input' && strpos($html, 'image') !== false ) || ( $type == 'param' && strpos($html, 'movie') !== false ) || $type == 'img' || $type == 'script' || $type == 'embed' ) {
      if ( strpos($url,$this->http_url) !== false && get_option('wordpress-https_internalurls') == 1 ) {
       $buffer = str_replace($html, str_replace($this->http_url, $this->https_url, $html), $buffer);
      } else if ( $this->shared_ssl && get_option('wordpress-https_internalurls') == 1 && strpos($html,$this->http_url) !== false ) {
       $buffer = str_replace($html, str_replace($this->http_url, $this->https_url, $html), $buffer);
      } else if ( get_option('wordpress-https_externalurls') == 1 ) {
       if ( get_option('wordpress-https_bypass') == 1 ) {
        $buffer = str_replace($html, $this->replace_http($html), $buffer);
       } else if (@file_get_contents($this->replace_http($url))) {
        $buffer = str_replace($html, $this->replace_http($html), $buffer);
       }
      }
     }
    }

    // Look for any relative paths that should be udpated to the Shared SSL path
    if ( $this->shared_ssl == 1 ) {
     preg_match_all('/\<(script|link|img|input|form|embed|param|a)[^>]+[\'"](\/[\/-\w\.#?=&;]*)[^>]+>/im', $buffer, $matches);

     for ($i = 0; $i<=sizeof($matches[0]); $i++) {
      $html   = $matches[0][$i];
      $type   = $matches[1][$i];
      $url    = $matches[2][$i];

      $buffer = str_replace($html, str_replace($url, $this->https_url . $url, $html), $buffer);
     }
    }
   }

   // Update anchor tags to appropriate URL's
   preg_match_all('/\<a[^>]+[\'"]((http|https):\/\/[\/-\w\.#?=&;]+)[^>]+>/im', $buffer, $matches);

   for ($i = 0; $i<=sizeof($matches[0]); $i++) {
    $html     = $matches[0][$i];
    $url      = $matches[1][$i];
    $scheme   = $matches[2][$i];

    $url_path = parse_url($url, PHP_URL_PATH);
    if ($this->shared_ssl) {
     $url_path = str_replace(parse_url($this->https_url, PHP_URL_PATH), '', $url_path);
    } else {
     $url_path = str_replace(parse_url(get_option('home'), PHP_URL_PATH), '', $url_path);
    }

    if ($url_path == '/') {
     $post = get_option('page_on_front');
    } else {
     $post = get_page_by_path($url_path);
     $post = $post->ID;
    }

    if ($post) {
     $force_ssl = get_post_meta($post, 'force_ssl', true);

     if ($force_ssl) {
      $buffer = str_replace($html, str_replace($this->http_url, $this->https_url, $html), $buffer);
     } else if (get_option('wordpress-https_exclusive_https') == 1) {
      $buffer = str_replace($html, str_replace($this->https_url, $this->http_url, $html), $buffer);
     }
    }
   }

   // Fix any links that contain the HTTPS version of the regular domain when using Shared SSL
   if ( $this->shared_ssl && get_option('wordpress-https_internalurls') == 1 ) {
    $regex_url = preg_quote($this->replace_http($this->http_url));
    $regex_url = str_replace('/', '\/', $regex_url);
    preg_match_all('/\<a[^>]+(' . $regex_url . ')[^>]+>/im', $buffer, $matches);

    for ($i = 0; $i<=sizeof($matches[0]); $i++) {
     $html = $matches[0][$i];
     $url  = $matches[1][$i];

     $buffer = str_replace($html, str_replace($url, $this->https_url, $html), $buffer);
    }
   }

   return $buffer;
  }

  /**
   * Checks if the current page is SSL
   *
   * @param none
   * @return void
   */
  function is_ssl() {
   if ( $this->shared_ssl == 1 && strpos($this->https_url, $_SERVER['HTTP_X_FORWARDED_SERVER']) !== false ) {
    return true;
   }
   return is_ssl();
  }

  /**
   * Checks if the current page needs to be redirected
   *
   * @param none
   * @return void
   */
  function check_https() {
   global $post;
   if ( is_front_page() && get_option('show_on_front') == 'posts' ) {
    if ( get_option('wordpress-https_frontpage') == 1 && !$this->is_ssl() ) {
     $this->redirect(true);
    } else if ( get_option('wordpress-https_frontpage') != 1 && get_option('wordpress-https_exclusive_https') == 1 && $this->is_ssl() ) {
     $this->redirect(false);
    }
   } else if ( ( is_single() || is_page() || is_front_page() || is_home() ) && $post->ID > 0 ) {
    $forceSSL = get_post_meta($post->ID, 'force_ssl');
    if ( !$this->is_ssl() && $forceSSL ) {
     $this->redirect(true);
    } else if ( get_option('wordpress-https_exclusive_https') == 1 && !$forceSSL ) {
     $this->redirect(false);
    }
   }
  }

  /**
   * Used to redirect admin pages to Shared SSL host
   *
   * @param none
   * @return void
   */
  function admin_redirect() {
   // If we're using Shared SSL and the admin panel should be SSL, redirect
   if ( is_admin() && $this->shared_ssl && force_ssl_admin() && !$this->is_ssl() ) {
    $this->redirect(true);
   // If we're on the login page and it should be SSL, redirect
   } else if ( $GLOBALS['pagenow'] == 'wp-login.php' && ( force_ssl_admin() || force_ssl_login() ) && $this->shared_ssl && !$this->is_ssl() ) {
    $this->redirect(true);
   }
  }

  /**
   * Redirects page to HTTP or HTTPS accordingly
   *
   * @param boolean $ssl
   * @return void
   */
  function redirect($ssl = true) {
   if ( !$this->is_ssl() && $ssl == true ) {
    $url = parse_url($this->https_url);
    $url['scheme'] = 'https';
   } else if ( $this->is_ssl() && $ssl == false ) {
    $url = parse_url($this->http_url);
    $url['scheme'] = 'http';
   } else {
    $url = false;
   }
   if ($url) {
    $destination = $url['scheme'] . '://' . $url['host'] . (($this->shared_ssl) ? $url['path'] : '') . $_SERVER['REQUEST_URI'];
    wp_redirect($destination, 301);
    exit();
   }
  }

  /**
   * Add 'Force SSL' checkbox to add/edit post pages
   *
   * @param none
   * @return void
   */
  function post_checkbox() {
   global $post;

   wp_nonce_field(plugin_basename(__FILE__), 'wordpress-https');

   $checked = false;
   if ($post->ID) {
    $checked = get_post_meta($post->ID, 'force_ssl', true);
   }
   echo '<div class="misc-pub-section misc-pub-section-last" style="border-top: 1px solid #EEE;"><label>Force SSL: <input type="checkbox" value="1" name="force_ssl" id="force_ssl"'.(($checked) ? ' checked="checked"' : '').' /></label></div>';
  }

  /**
   * Save Force SSL option to post or page
   *
   * @param int $post_id
   * @return int $post_id
   */
  function post_save( $post_id ) {
   if ( array_key_exists('wordpress-https', $_POST) ) {
    if ( !wp_verify_nonce($_POST['wordpress-https'], plugin_basename(__FILE__))) {
      return $post_id;
    }

    if ( defined('DOING_AUTOSAVE') && DOING_AUTOSAVE ) {
     return $post_id;
    }

    if ( $_POST['post_type'] == 'page' ) {
     if ( !current_user_can('edit_page', $post_id) ) {
      return $post_id;
     }
    } else {
     if ( !current_user_can('edit_post', $post_id) ) {
      return $post_id;
     }
    }

    $forceSSL = (($_POST['force_ssl'] == 1) ? true : false);
    if ($forceSSL) {
     update_post_meta($post_id, 'force_ssl', 1);
    } else {
     delete_post_meta($post_id, 'force_ssl');
    }

    return $forceSSL;
   }
   return $post_id;
  }

  /**
   * Filters HTTPS urls from bloginfo function
   *
   * @param string $result
   * @param string $show
   * @return string
   */
  function bloginfo($result = '', $show = '') {
   if ($show == 'stylesheet_url' || $show == 'template_url' || $show == 'wpurl' || $show == 'home' || $show == 'siteurl' || $show == 'url') {
    $result = $this->replace_https($result);
   }
   return $result;
  }

  /**
   * Add admin panel menu option
   *
   * @param none
   * @return void
   */
  function menu() {
   add_options_page('WordPress HTTPS Settings', 'WordPress HTTPS', 'manage_options', 'wordpress-https', array(&$this, 'settings'));
  }

  /**
   * Add plugin links to Manage Plugins page in admin panel
   *
   * @param array $links
   * @param string $file
   * @return array
   */
  function plugin_links($links, $file) {
   if ( strpos($file, basename( __FILE__)) === false ) {
    return $links;
   }

   $links[] = '<a href="' . site_url() . '/wp-admin/options-general.php?page=wordpress-https" title="WordPress HTTPS Settings">Settings</a>';
   $links[] = '<a href="http://wordpress.org/extend/plugins/wordpress-https/faq/" title="Frequently Asked Questions">FAQ</a>';
   $links[] = '<a href="http://wordpress.org/tags/wordpress-https#postform" title="Support">Support</a>';
   $links[] = '<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=6ZL95VTJ388HG" title="Support WordPress HTTPS development with a donation!">Donate</a>';
   return $links;
  }

  /**
   * Start output buffering
   *
   * @param none
   * @return void
   */
  function buffer_start() {
   if ( get_option('wordpress-https_externalurls') == 1 && get_option('wordpress-https_bypass') != 1 ) {
    @ini_set('allow_url_fopen', 1);
   }
   ob_start(array(&$this, 'process'));
  }

  /**
   * End output buffering
   *
   * @param none
   * @return void
   */
  function buffer_end() {
   ob_end_flush();
  }

  /**
   * Replace HTTPS with HTTP
   *
   * @param string
   * @return string
   */
  function replace_https($string) {
   return str_replace('https://', 'http://', $string);
  }

  /**
   * Replace HTTP with HTTPS
   *
   * @param string
   * @return string
   */
  function replace_http($string) {
   return str_replace('http://', 'https://', $string);
  }

  /**
   * Settings page in admin panel
   *
   * @param none
   * @return void
   */
  function settings() {
   if ( !current_user_can('manage_options') ) {
    wp_die( __('You do not have sufficient permissions to access this page.') );
   }

   if ( $_SERVER['REQUEST_METHOD'] === 'POST' ) {
    $errors = array();

    foreach ($this->options_default as $key => $default) {
     if (!array_key_exists($key, $_POST) && $default == 0) {
      $_POST[$key] = 0;
      update_option($key, $_POST[$key]);
     } else {
      if ( $key == 'wordpress-https_sharedssl' && $_POST['wordpress-https_sharedssl_host'] == '' ) {
       $errors[] = '<strong>Shared SSL Host</strong> - Invalid host.';
       $_POST[$key] = 0;
       update_option($key, $_POST[$key]);
      } else if ( $key == 'wordpress-https_sharedssl_host' && $_POST[$key] != '' ) {
       $url = parse_url($_POST[$key]);
       if ( sizeof($url) > 0 ) {
        $_POST[$key] = 'https://' . $url['host'] . $url['path'];
        if ( substr($_POST[$key], -1, 1) == '/' ) {
         $_POST[$key] = substr($_POST[$key], 0, strlen($_POST[$key])-1);
        }
        update_option($key, $_POST[$key]);
       } else {
        $errors[] = '<strong>Shared SSL Host</strong> - Invalid host.';
        update_option($key, '');
       }
      } else if ( $key == 'wordpress-https_externalurls' && @ini_get('allow_url_fopen') != 1 ) {
       $errors[] = '<strong>External HTTPS Elements</strong> - PHP configuration error: allow_url_fopen must be enabled.';
       $_POST[$key] = 0;
       update_option($key, $_POST[$key]);
      } else if ($key == 'wordpress-https_disable_autohttps' && version_compare(get_bloginfo('version'),'3.0','<')) {
       $_POST[$key] = 0;
       update_option($key, $_POST[$key]);
      } else {
       update_option($key, $_POST[$key]);
      }
     }
    }

    if ( @$_POST['ajax'] == 1 ) {
     ob_clean();
     if ( sizeof( $errors ) > 0 ) {
      echo "  <ul id=\"message\">\n";
      foreach ( $errors as $error ) {
       echo "   <li class=\"error\"><p>".$error."</p></li>\n";
      }
      echo "  </ul>\n";
     } else {
      echo "   <div class=\"updated below-h2 fade\" id=\"message\"><p>Settings saved.</p></div>\n";
     }
     exit();
    }
   }
?>
 <div class="wrap">
  <div id="icon-options-general" class="icon32"><br /></div>
  <h2>WordPress HTTPS Settings</h2>

<?php
   if ( $_SERVER['REQUEST_METHOD'] === 'POST' ) {
    if ( sizeof( $errors ) > 0 ) {
     echo "  <ul id=\"message\">\n";
     foreach ( $errors as $error ) {
      echo "   <li class=\"error\"><p>".$error."</p></li>\n";
     }
     echo "  </ul>\n";
    } else {
     echo "   <div class=\"updated below-h2 fade\" id=\"message\"><p>Settings saved.</p></div>\n";
    }
   } else {
    echo "  <div id=\"message-wrap\"><div id=\"message-body\"></div></div>\n";
   }
?>

  <div id="wphttps-sidebar">

   <div class="wphttps-widget" id="wphttps-support">
    <h3 class="wphttps-widget-title">Support</h3>
    <div class="wphttps-widget-content">
     <p>Have you tried everything and your website is still giving you partially encrypted errors?</p>
     <p>If you haven't already, check out the <a href="http://wordpress.org/extend/plugins/wordpress-https/faq/" target="_blank">Frequently Asked Questions</a>.</p>
     <p>Still not fixed? Having other problems? Please <a href="http://wordpress.org/tags/wordpress-https#postform" target="_blank">start a support topic</a> and I'll do my best to assist you.</p>
    </div>
   </div>

   <div class="wphttps-widget" id="wphttps-donate">
    <h3 class="wphttps-widget-title">Donate</h3>
    <div class="wphttps-widget-content">
     <p>If you found this plugin useful, or I've already helped you with your website, please considering buying me a <a href="http://en.wikipedia.org/wiki/Newcastle_Brown_Ale" target="_blank">beer</a> or two.</p>
     <p>Donations help alleviate the time spent developing and supporting this plugin and are greatly appreciated.</p>

     <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
      <input type="hidden" name="cmd" value="_s-xclick">
      <input type="hidden" name="hosted_button_id" value="N9NFVADLVUR7A">
      <input type="image" src="https://www.paypal.com/en_US/i/btn/btn_donate_SM.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
      <img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
     </form>

    </div>
   </div>

  </div>

  <form name="form" id="wordpress-https" action="options-general.php?page=wordpress-https" method="post">
   <?php settings_fields('wordpress-https'); ?>

   <fieldset>
    <label for="wordpress-https_internalurls"><input name="wordpress-https_internalurls" type="checkbox" id="wordpress-https_internalurls" value="1"<?php echo ((get_option('wordpress-https_internalurls')) ? ' checked="checked"' : ''); ?> /> <strong>Internal HTTPS Elements</strong></label>
    <p>Force internal elements to HTTPS when viewing a secure page.</p>
    <p class="description">Fixes most partially encrypted errors.</p>
   </fieldset>

   <fieldset>
    <label for="wordpress-https_externalurls"><input name="wordpress-https_externalurls" type="checkbox" id="wordpress-https_externalurls" value="1"<?php echo ((get_option('wordpress-https_externalurls')) ? ' checked="checked"' : ''); ?> /> <strong>External HTTPS Elements</strong></label>
    <p>Attempt to automatically force external elements to HTTPS when viewing a secure page. External elements are any element not hosted on your domain.</p>
    <p class="description">Warning: This option checks that the external element can be loaded via HTTPS while the page is loading. Depending on the amount of external elements, this could affect the load times of your pages.</p>
   </fieldset>

   <fieldset>
    <label for="wordpress-https_bypass"><input name="wordpress-https_bypass" type="checkbox" id="wordpress-https_bypass" value="1"<?php echo ((get_option('wordpress-https_bypass')) ? ' checked="checked"' : ''); ?> /> <strong>Bypass External Check</strong></label>
    <p>Disable the option to check if an external element can be loaded over HTTPS.</p>
    <p class="description">Warning: Bypassing the HTTPS check for external elements may cause elements to not load at all. Only enable this option if you know that all external elements can be loaded over HTTPS.</p>
   </fieldset>

<?php if (version_compare(get_bloginfo('version'),'3.0','>=')) { ?>
   <fieldset>
    <label for="wordpress-https_disable_autohttps"><input name="wordpress-https_disable_autohttps" type="checkbox" id="wordpress-https_disable_autohttps" value="1"<?php echo ((get_option('wordpress-https_disable_autohttps')) ? ' checked="checked"' : ''); ?> /> <strong>Disable Automatic HTTPS</strong></label>
    <p>Prevents WordPress 3.0+ from making all links HTTPS when viewing a secure page.</p>
    <p class="description">When a page is viewed via HTTPS in WordPress 3.0+, all internal page, category and post links are forced to HTTPS. This option will disable that.</p>
   </fieldset>

<?php } ?>
   <fieldset>
    <label for="wordpress-https_exclusive_https"><input name="wordpress-https_exclusive_https" type="checkbox" id="wordpress-https_exclusive_https" value="1"<?php echo ((get_option('wordpress-https_exclusive_https')) ? ' checked="checked"' : ''); ?> /> <strong>Force SSL Exclusively</strong></label>
    <p>Exclusively force SSL on posts and pages with the `Force SSL` option checked. All others are redirected to HTTP.</p>
    <p class="description">WordPress HTTPS adds a 'Force SSL' checkbox to each post and page right above the publish button (<a href="<?php echo $this->plugin_url; ?>/screenshot-2.png" target="_blank">screenshot</a>). When selected, the post or page will be forced to HTTPS. With this option enabled, all posts and pages without 'Force SSL' checked will be redirected to HTTP.</p>
   </fieldset>

   <fieldset>
    <label for="wordpress-https_sharedssl"><input name="wordpress-https_sharedssl" type="checkbox" id="wordpress-https_sharedssl" value="1"<?php echo ((get_option('wordpress-https_sharedssl')) ? ' checked="checked"' : ''); ?> /> <strong>Shared SSL</strong></label>
    <p>Enable this option if you are using a Shared SSL certificate and your Shared SSL Host is something other than '<?php echo $this->replace_http($this->http_url); ?>/'.</p>
    <label><strong>Shared SSL Host</strong> <input name="wordpress-https_sharedssl_host" type="text" id="wordpress-https_sharedssl_host" value="<?php echo get_option('wordpress-https_sharedssl_host'); ?>" /></label>
   </fieldset>

<?php if (get_option('show_on_front') == 'posts') { ?>
   <fieldset>
    <label for="wordpress-https_frontpage"><input name="wordpress-https_frontpage" type="checkbox" id="wordpress-https_frontpage" value="1"<?php echo ((get_option('wordpress-https_frontpage')) ? ' checked="checked"' : ''); ?> /> <strong>HTTPS Front Page</strong></label>
    <p>It appears you are using your latest posts for your home page. If you would like that page to have SSL enforced, enable this option.</p>
   </fieldset>

<?php } ?>
   <p class="button-controls">
    <input type="submit" name="Submit" value="Save Changes" class="button-primary" />
    <img alt="Waiting..." src="<?php echo $this->plugin_url; ?>/css/images/wpspin_light.gif" class="waiting" id="submit-waiting" />
   </p>
  </form>

  <br style="clear: both" />

 </div>
<?php
  }
 } // End WordPressHTTPS Class
}

if ( class_exists('WordPressHTTPS') ) {
 $wordpress_https = new WordPressHTTPS();
}
?>