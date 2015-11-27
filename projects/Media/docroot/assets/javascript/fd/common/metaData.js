var FreshDirect = FreshDirect || {};

(function(fd) {
  "use strict"

  var metaDataUpdate = Object.create(fd.common.signalTarget, {
    signal : {
      value : 'formMetaData'
    },
    callback : {
      value : function(data) {
        this.update(data);
      }
    },
    update : {
      value : function(data) {
        fd.metaData = data;
      }
    }
  });

  metaDataUpdate.listen();

  fd.modules.common.utils.register("modules.common", "metaDataUpdate",
      metaDataUpdate, fd);
}(FreshDirect));
