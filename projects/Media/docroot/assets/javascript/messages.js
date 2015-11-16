(function(fd){

		var $=fd.libs.$;
		var console = console || { log:function(){} };
	
	  var SystemMessage='SystemMessage',
	  messages = {},order = ['sitemessage',SystemMessage,'cutoff','platterwarning','reservationwarning','deliveryetawarning'],
	  messageObj = document.getElementById('messages'),
	  hashes = {},
	  /* REMOVE THIS DEPENDENCY - USQLegalWarning can be an object with only sessionStore prop
	   * jsessionId=fd.USQLegalWarning.getJSessionId(), */
	  jsessionId = ($.isFunction(fd.USQLegalWarning.getJSessionId)) ? fd.USQLegalWarning.getJSessionId() : function() {
		  return (fd.USQLegalWarning.hasOwnProperty('sessionStore')) ? fd.USQLegalWarning.sessionStore : 'FD_NO_SESSION_ID';
	  },
	  hashesStorageKey = jsessionId+'/messageHashes',
	  closedStorageKey = jsessionId+'/messagesClose',
	  isClosed = false,
	  content, handler;
	  
	  var getHash = function(s){
			var hash = 0;
			if (s.length == 0) return hash;
			for (i = 0; i < s.length; i++) {
				ch = s.charCodeAt(i);
				hash = ((hash<<5)-hash)+ch;
				hash = hash & hash; 
			}
			return hash;
	  };
	  
	  var removeType = function(type){
	  	if(type!=SystemMessage) {
	    	 delete messages[type];
	    	 delete hashes[type];	  		
	  	}
	  };
	  
    /**
     * https://gist.github.com/paulirish/5558557
     */
    function hasSessionStorage(){
      try {
        sessionStorage.setItem('test', 1);
        sessionStorage.removeItem('test');
        return true;
      } catch(e) {
        return false;
      }
    }

	  var methods = {
	     init : function( options ) {

	       return this.each(function(){
	    	   var data = sessionStorage.getItem(hashesStorageKey) || '{}',
	    	   	   closed = sessionStorage.getItem(closedStorageKey) || 'false';
	    	   // console.log('hashes: '+data);
	    	   hashes = JSON.parse(data);
	    	   isClosed = JSON.parse(closed);
	       });

	     },
	     destroy : function( ) {

	       return this.each(function(){
	       })

	     },
	     open : function( ) { 
	    	 var $messages = $(document.getElementById('messages'));
	    	 isClosed = false;
         if(hasSessionStorage()){
	    	   sessionStorage.setItem(closedStorageKey,JSON.stringify(isClosed));
         }
	    	 $messages.addClass('open');
	     },
	     close : function( ) {
	    	 var $messages = $(document.getElementById('messages'));
	    	 isClosed = true;
         if(hasSessionStorage()){
           sessionStorage.setItem(hashesStorageKey,JSON.stringify(hashes));
           sessionStorage.setItem(closedStorageKey,JSON.stringify(isClosed));
         }
	    	 $messages.removeClass('open');
	     },
	     add:function(type){
	    	 var opened = false,hash, update, result = this.each(function(){
	    		 var $this, ntype, text;
	    		 
	    		 $('script',this).remove();
	    		 $this = $(this);
	    		 ntype = type || $this.attr('data-type');
	    		 text = $this.text();
	    		 if($.trim(text).length >1) {
	    			 hash = getHash(text);
	    			 messages[ntype] = this.innerHTML;
	    			 if (!$.inArray(ntype, order)) {
	    				 order.push(ntype); //make available for update
	    			 }
	    			 if(hashes[ntype] != hash ) {			 
		    		 	 opened = true;
		    		 	 hashes[ntype]=hash;
	    			 }
	    			 $this.trigger({
	    				 type:'messageAdded'
	    			 });
	    		 } else {
	    			 removeType(ntype);
	    		 }
	    	 });
	    	 
	    	 update = $(messageObj).messages('update');
	    	 if(opened) {
	    		 update.messages('open');
	    	 }
	    	 
	    	 return result;
	     },
	     remove:function(type) {
	    	 removeType(type);
	    	 $(messageObj).messages('update');
	     },
	     update:function(){
	    	 var i,l=order.length,
	    	 	 message, html='',id='',result,handler = false;
	    	 for(i=0;i<l;i++) {
	    		 id = order[i];
	    		 if(messages[id]) {
	    			 html+='<li class="'+id+'">'+messages[id]+'</li>';
	    			 handler = true;
	    		 }
	    	 };
	    	 
	    	 for(message in messages) {
	    		 if(messages.hasOwnProperty(message) && order.indexOf(message) == -1 ) {
	    			 html+='<li class="'+message+'">'+messages[message]+'</li>';
	    			 handler = true;
	    		 }
	    	 }
	    	 
	    	 if(handler) {
		    	 $(this).addClass('hashandler');
	    	 } else {
		    	 $(this).removeClass('hashandler');
		    	 $(this).messages('close');
	    	 }
	    	 
	    	 return this.each(function(){
	    		 $('ul',this)[0].innerHTML=html;
	    	 });
	     }
	  };

	  $.fn.messages = function( method ) {
	    
	    if ( methods[method] ) {
	      return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
	    } else if ( typeof method === 'object' || ! method ) {
	      return methods.init.apply( this, arguments );
	    } else {
	      $.error( 'Method ' +  method + ' does not exist on jQuery.messages' );
	    }    
	  
	  };
	  
	  
	  // When OAS pushes a system message:
	  $(document).bind('OAS_DONE',function(event,id){
		  if(id == SystemMessage) {
			  $(document.getElementById("OAS_"+SystemMessage)).messages('add',SystemMessage);
		  }
	  });
	  
	  $(document).on('click','#messages .handler',function(e){
		  var $messages = $(document.getElementById('messages'));
		  if($messages.hasClass('open')) {
			  $messages.messages('close');
		  } else {
			  $messages.messages('open');
		  }
	  });
	  
	  $(document).messages('init');
	  $('.message.invisible').messages('add');
})(FreshDirect);