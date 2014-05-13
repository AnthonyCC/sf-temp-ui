<?php

/**
 * Admin options page. Please see http://www.webpurify.com/wp-plugin.php for more information.
 */

// detect option update
$update = false;

// update webpurify key if any
if( isset( $_POST['webpurify_key'] ) ) {
    $update = true;
    update_option( 'webpurify_userkey', $_POST['webpurify_key'] );
}

// update language if any
if( isset( $_POST['webpurify_lang'] ) ) {
    $update = true;
    update_option( 'webpurify_lang', $_POST['webpurify_lang'] );
}

// update replacement if any
if( isset( $_POST['webpurify_r'] ) ) {
    $update = true;

    if ( empty( $_POST['webpurify_r'] ) ) {
        $_POST['webpurify_r'] = '*';
    }
    
    update_option( 'webpurify_r', $_POST['webpurify_r'] );
}

// get options
$options = webpurify_get_options();

// get available languages
$languages = webpurify_get_languages();

?>

<div class="wrap">

    <h2><?php _e( 'Configure: WebPurify Plugin', 'WebPurifyTextReplace' ) ?></h2>

    <?php if ( $update ): ?>
        <div id="setting-error-settings_updated" class="updated settings-error">
            <p><strong>Settings saved.</strong></p>
        </div>
    <?php endif; ?>

    <form name="form1" method="post" action="">

        <p>
            <?php _e( 'In order to use this plugin you must enter your "WebPurify API Key."<br/><br/><br/>Purchase a WebPurify License at: <a href="https://www.webpurify.com/?utm_source=wp_pluginlink&utm_medium=plugin&utm_campaign=wp_pluginlink"  target=_blank >www.webpurify.com</a>', 'WebPurifyTextReplace' ) ?>
        </p>

        <table class="form-table">
            <tbody>
                <tr valign="top">
                    <th scope="row">
                        <label for="webpurify_key"><?php _e( 'Enter WebPurify API Key', 'WebPurifyTextReplace' ) ?>:</label>
                    </th>
                    <td>
                        <input id="webpurify_key" type="text" size="50" name="webpurify_key" value="<?php echo $options['userkey'] ?>" />
                    </td>
                </tr>

                <tr valign="top">
                    <th scope="row">
                        <?php _e( 'Language Preference', 'WebPurifyTextReplace' ) ?>:
                    </th>
                    <td>
                        <fieldset>
                            <?php foreach ($languages as $code => $name): ?>
                            <?php $checked = $options['wplang'] == $code ? ' checked="checked"' : NULL; ?>
                                <label title="<?php echo $name ?>">
                                    <input type="radio" name="webpurify_lang" value="<?php echo $code ?>"<?php echo $checked ?> /> <?php echo $name ?>
                                </label>
                            <?php endforeach; ?>
                        </fieldset>
                    </td>
                </tr>

                <tr valign="top">
                    <th scope="row">
                        <label for="webpurify_r"><?php _e( 'Replacement Character', 'WebPurifyTextReplace' ) ?>:</label>
                    </th>
                    <td>
                        <input id="webpurify_r" type="text" size="1" name="webpurify_r" maxlength="1" value="<?php echo $options['repc'] ?>">
                    </td>
                </tr>

            </tbody>
        </table>

        <p class="submit">
            <input type="submit" name="store_key" value="<?php _e( 'Save Settings', 'WebPurifyTextReplace' ) ?>" >
        </p>

    </form>

</div>
