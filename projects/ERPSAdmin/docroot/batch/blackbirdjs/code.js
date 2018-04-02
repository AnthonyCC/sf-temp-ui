( { "exec1": "log.debug( 'this is a debug message' );|\
log.info( 'this is an info message' );|\
log.warn( 'this is a warning message' );|\
log.error( 'this is an error message' );",

"profiling": "log.profile( 'generate test string' );|\
|\
var testContent = '';|\
for ( var i = 0; i < 3000; i++ ) {|\
  testContent += '-';|\
}|\
|\
log.profile( 'generate test string' );",

"inpage": '<html>|\
  <head>|\
    <script type="text/javascript" src="/PATH/TO/blackbird.js"></script>|\
    <link type="text/css" rel="Stylesheet" href="/PATH/TO/blackbird.css" />|\
    ...|\
  </head>|\
  ...',

"override": "var log = {|\
  toggle: function() {},|\
  move: function() {},|\
  resize: function() {},|\
  clear: function() {},|\
  debug: function() {},|\
  info: function() {},|\
  warn: function() {},|\
  error: function() {},|\
  profile: function() {}|\
};" } )
