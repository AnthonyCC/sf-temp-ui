New CMS admin frontend
======================

Currently using gulp, bower and polymer.

## Requirements ##

* node >0.12
* npm
* bower (npm install -g bower)
* gulp (npm install -g gulp)


## Startup ##

1. npm install
2. bower install
3. gulp serve

## Tests ##

### WCT ###

Run `gulp test:local` to execute local web component tests.

### Fixing URL encoding in mocha

Replace `encodeURIComponent(escapeRe(s))` in `mocha.js` around line 2494 with `encodeURIComponent(s)` to be able to run tests separately.

