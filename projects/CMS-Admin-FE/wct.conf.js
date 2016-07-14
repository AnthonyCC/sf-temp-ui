var path = require("path");

var ret = {
  "suites": ["wct/"],
  "persistent": true,
  "webserver": {
    "pathMappings": []
  },
  "plugins": {
    "local": {
      "browsers": ["chrome"]
    }
  }
};

var mappings = {};
var rootPath = (__dirname).split(path.sep).slice(-1)[0];

mappings['/components/' + rootPath + '/src/components'] = 'bower_components';

ret.webserver.pathMappings.push(mappings);

module.exports = ret;
