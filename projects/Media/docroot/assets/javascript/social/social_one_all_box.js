		/* This is an event */
		var my_on_login_redirect = function(args) {
			
			return true;
		}

		/* Initialise the asynchronous queue */
		var _oneall = _oneall || [];

		/* Social Login Example */
		_oneall.push([ 'social_login', 'set_providers',
				[ 'facebook', 'google' ] ]);
		_oneall.push([ 'social_login', 'set_grid_sizes', [ 4, 4 ] ]);
		_oneall.push([ 'social_login', 'set_callback_uri',
				'http://127.0.0.1:7001/social/social_login_success.jsp' ]);
		_oneall.push([ 'social_login', 'set_event', 'on_login_redirect',
				my_on_login_redirect ]);
		_oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);