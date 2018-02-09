var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;

  var widget = Object.create(fd.common.signalTarget, {
    contentTemplate: {
      value: function(){
        return '';
      }
    },
    previewTemplate: {
      value: function(){
        return '';
      }
    },
    contentHolder: {
      value: function() { 
    	  return '[data-drawer-content="' + ($.isArray(this.signal)?this.signal[0] : this.signal) + '"]'; }
    },
    previewHolder: {
      value: function() { 
    	  return '[data-drawer-default-content="' + ($.isArray(this.signal)?this.signal[0] : this.signal) + '"]'; }
    },
    openSignal: {
    	value: '',
    	writable: true
    },
    openSignalPostfix: {
      value: '-drawer-on-open'
    },
    isOpenSignal: {
    	value: function(signal) {
    		// if open signal is null or empty
    		if (!this.openSignal || !this.openSignal.length) {
    			return false;
    		}
    		if (fd.libs.$.isArray(this.openSignal)) {
    			return this.openSignal.indexOf(signal) !== -1;
    		}
    		return signal === openSignal;
    	}
    },
    render:{
      value:function(data, placeHolderSelector, templateFn){
        var $ph =  $(placeHolderSelector);

        data = data || {};
        data.metadata = data.metadata || fd.metaData;

        if($ph.length){
          $ph.html(templateFn(data));
          fd.modules.common.Select.selectize($ph);
        }
      }
    },
    callback:{
      value: function(value, signal, renderPreview) {
    	  if (this.isOpenSignal(signal)) {
    		  if (!this.hasRenderedContent) {
	    		  this.render(this.data, this.contentHolder(), this.contentTemplate);
	    		  this.hasRenderedContent = true;
    		  }
    	  } else {
    		  this.hasRenderedContent = false;
    		  this.data = value;
    		  this.render(value, this.previewHolder(), this.previewTemplate);
    		  // If drawer content is visible, update it
    		  var contentHolderElement = $(this.contentHolder());
    		  if (contentHolderElement.is(':visible') && contentHolderElement.css('visibility') !== 'hidden') {
    			  this.render(value, this.contentHolder(), this.contentTemplate);
	    		  this.hasRenderedContent = true;
    		  }
    		  
    	  }
        
      }
    },
    listen: {
    	value: function() {
    		// if signal is assigned but open signal has not been generated 
    		if (!this.openSignal && this.signal) {
    			// if signal is an array, iterate it and generate a open signal per each of them
    			if ($.isArray(this.signal)) {
    				this.openSignal = [];
    				this.signal.forEach( function(s) {
    					var newOpenSignal = s + this.openSignalPostfix;
    					this.openSignal.push(newOpenSignal);
    					this.signal.push(newOpenSignal);
    				}, this);
    			} else {
    				var newOpenSignal = this.signal + this.openSignalPostfix;
    				this.openSignal = [newOpenSignal];
    				this.signal = [this.signal, newOpenSignal];
    			}
    		}
    		fd.common.signalTarget.listen.apply(this);
    	}
    }
  });

  fd.modules.common.utils.register('modules.common', 'drawerWidget', widget, fd);
}(FreshDirect));
