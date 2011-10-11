<?php
/**
 * The base configurations of the WordPress.
 *
 * This file has the following configurations: MySQL settings, Table Prefix,
 * Secret Keys, WordPress Language, and ABSPATH. You can find more information
 * by visiting {@link http://codex.wordpress.org/Editing_wp-config.php Editing
 * wp-config.php} Codex page. You can get the MySQL settings from your web host.
 *
 * This file is used by the wp-config.php creation script during the
 * installation. You don't have to use the web site, you can just copy this file
 * to "wp-config.php" and fill in the values.
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'myfd');

/** MySQL database username */
define('DB_USER', 'myfd');

/** MySQL database password */
define('DB_PASSWORD', 'myfd');

/** MySQL hostname */
define('DB_HOST', 'localhost');

/** Database Charset to use in creating database tables. */
define('DB_CHARSET', 'utf8');

/** The Database Collate type. Don't change this if in doubt. */
define('DB_COLLATE', '');

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         'I7ySe/Sm{F_I~ 76zFsSBWR$NhPT?hKJs/X1rC0vDP|sB4,ysTS;nWyC/o0MH_LW');
define('SECURE_AUTH_KEY',  '|}Pf}>/Kq@e:*$Y_3aS3+s2lo4-z=x>xbA%sC!3,Z$o3G:xw)lD-84*OY)Hy[|rR');
define('LOGGED_IN_KEY',    'HmgCyO)|{!ii[raD9XWRu,%f4i$]y@C!I9CL`~AuS9P-G5(f^aaDP59_x!`)(Wjv');
define('NONCE_KEY',        'L=M-qVmx!Qj+G=#,sd]vBf@((qa)]e7flW$dR.u$Y8p^$ 0 !MwB4@ov7GC?@Y>6');
define('AUTH_SALT',        'cCW:;D4MqxBI@kpdSMq8|yi6Xvxsu2WL0^JH?74/z!QF<P1)&p/B!/}NMsjw0i_[');
define('SECURE_AUTH_SALT', '9B}aB$SR?Yj[v;0j;o-4wPF:l5jW{g>Q) ,,S;%Uhp9L>0qabs[|<?Kmkb2:M):%');
define('LOGGED_IN_SALT',   'Rm%:(`|vHZ^hE|iRXlH;.Ys:;K4Pp8?g%bav0oksQ|oN96$FEmAg-k1R[o~*bln&');
define('NONCE_SALT',       'N}lc.b{|G]9pYwLzG!l3*-+nJlU!,R~#3JH,+:YSI?Y@Kkb8P:#RjrC*]#@{^??0');

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each a unique
 * prefix. Only numbers, letters, and underscores please!
 */
$table_prefix  = 'wp_';

/**
 * WordPress Localized Language, defaults to English.
 *
 * Change this to localize WordPress.  A corresponding MO file for the chosen
 * language must be installed to wp-content/languages. For example, install
 * de.mo to wp-content/languages and set WPLANG to 'de' to enable German
 * language support.
 */
define ('WPLANG', '');

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 */
define('WP_DEBUG', false);

define('FD_STOREFRONT_BASE', 'http://cms.internal.euedge.com');

/* That's all, stop editing! Happy blogging. */

/** Absolute path to the WordPress directory. */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** Sets up WordPress vars and included files. */
require_once(ABSPATH . 'wp-settings.php');

