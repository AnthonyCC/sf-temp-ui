	  /**
	   * The list of states that the YouTube embedded player may return onStateChange
	   * http://code.google.com/apis/youtube/js_api_reference.html#Events
	   * @type {Object.<string>}
	   * @const
	   */
	  var STATE_UNSTARTED = '-1';
      var STATE_PLAYING = '1';
      var STATE_PAUSED = '2';
      var STATE_BUFFERING = '3';
      var STATE_VIDEOCUED = '5';
      var STATE_ENDED = '0';
	  var STATES = {
		'-1': 'loaded', 
		'1': 'playing', 
		'2': 'paused', 
		'3': 'buffering', 
		'5': 'videocued',
		'0': 'ended'
	  };
	
	  /**
	   * A list of states to ignore and not report through GA's event tracking capabilities.
	   * Modify this list to suit your reporting needs.
	   * @type {Object.<string>}
	   * @const
	   */
	  var STATES_TO_IGNORE = {
	  };
	
	  /**
	   * If set to true, records "resumed" when resuming a previously-paused video.
	   * If set to false, records "playing" instead when resuming a video.
	   * @type {Object.<boolean>}
	   * @const
	   */
	  var USE_RESUME = true;
	  var RESUME_LABEL = 'resuming';
	  
	  /**
	   * Function that is run whenever a video player has been loaded and initialised
	   * http://code.google.com/apis/youtube/js_api_reference.html#EventHandlers
	   * @param {string} playerId The ID of the player that generated the event
	   */
	  function onYouTubePlayerReady(playerId) {
		var player = document.getElementById(playerId);
		//player.cuePlaylist({list: "PL6C092001AAE963F5"});
		var targetFn = ["function (newState) { onytplayerStateChange (newState, \"", playerId, "\"); }"].join('');
		player.addEventListener("onStateChange", targetFn);
	  }
      
     /**
	  * Logs the video player event via Google Analytics' _trackEvent function.  This function
	  *  is called whenever there is a state change in a video player.
	  * @param {string} newState The state ID returned by the video player
	  * @param {string} playerId The ID of the player that generated the event
	  */
     function onytplayerStateChange(newState, playerId) {
	    //console.log(newState, playerId);
	    var player = document.getElementById(playerId); 
	    var video = VIDEOS[playerId];
	    
	    //If this state is not in the list of ignored states, then record the event
	    if (!STATES_TO_IGNORE.hasOwnProperty(newState)) {
		  var action = STATES[newState];
		  
		  if (newState == STATE_PLAYING && USE_RESUME == true && video.playStarted == true) {
		    action = RESUME_LABEL;
		  } 
	
		  if (newState == STATE_PLAYING) {
			  if(!video.playStarted){
			  _gaq.push(['_setCustomVar',2,'Video','Play', 3]);
			  _gaq.push(['_trackEvent','Videos','started',video_title]);
			  }
			  
			  video.playStarted = true;
			  
			  if(video.trackCheckpoint == -1){
				  video.trackCheckpoint = player.getCurrentTime() + video.trackInterval;
			  }
			  if(video.timerIsOn==0){
				  video.timerIsOn=1;
				  updateTimer(playerId,video.trackCheckpoint);				  
			  }
		  }
		
		  var video_title = video.title;
		  
		  if(newState=='-1' || newState=='0'){
			  
			var total = Math.round(player.getDuration());			
			_gaq.push(['_trackEvent','Videos',action,video_title,total]);
			
		  }else if(newState=='2'){
			if(video.timer != -1){
				clearTimeout(video.timer);
				video.timerIsOn=0;
			}
			var played = Math.round(player.getCurrentTime());
			_gaq.push(['_trackEvent','Videos',action,video_title,played]);
			
		  }else{
			  if(newState=='0'){
				  clearTimeout(video.timer);
				  video.timerIsOn=0;
			  }
			_gaq.push(['_trackEvent','Videos',action,video_title]);
		  }
        }
      }
     
     function updateTimer(playerId, intervalCheckpoint){
    	 
    	 var player = document.getElementById(playerId);
    	 var video = VIDEOS[playerId];
    	 try{
    		 if(intervalCheckpoint < player.getCurrentTime()) {
    			 //console.log("bucket reached: ", intervalCheckpoint);   		 
    			 video.viewed = video.viewed + video.trackInterval;
    			 //console.log("viewed so far: ", video.viewed);
    			 _gaq.push(['_trackEvent','Videos','viewed',video.title,Math.round(video.viewed)]);
    			 video.trackCheckpoint = player.getCurrentTime() + video.trackInterval;
    		 }
    		 
    		 video.timer = setTimeout("updateTimer('"+playerId+"',"+video.trackCheckpoint+")",1000);    		    		 
    	 }catch(err){    		 
    	 }
    	 
     }