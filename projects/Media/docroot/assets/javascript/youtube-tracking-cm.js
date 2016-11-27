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
		'-1': 'Loaded', 
		'1': 'Play', 
		'2': 'Pause', 
		'3': 'Buffering', 
		'5': 'Videocued',
		'0': 'Completion'
	  };
	
	  /**
	   * A list of states to ignore and not report through GA's event tracking capabilities.
	   * Modify this list to suit your reporting needs.
	   * @type {Object.<string>}
	   * @const
	   */
	  var STATES_TO_IGNORE = {
	  };
	  STATES_TO_IGNORE[STATE_UNSTARTED]='';
	  STATES_TO_IGNORE[STATE_BUFFERING]='';
	  STATES_TO_IGNORE[STATE_VIDEOCUED]='';
	
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
		var targetFn = ["function (newState) { onytplayerStateChange (newState, \"", playerId, "\"); }"].join('');
		player.addEventListener("onStateChange", targetFn);
	  }
      
     /**
	  * Logs the video player event via IBM Coremetrics cmCreateElementTag() function.  This function
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
		  
//		  if (newState == STATE_PLAYING && USE_RESUME == true && video.playStarted == true) {
//		    action = RESUME_LABEL;
//		  } 
	
		  if (newState == STATE_PLAYING && !video.playStarted) {
			  logEvent(player,video,"0");
			  video.playStarted = true;
			  return;
			  
		  }
		
		  logEvent(player,video,newState);
		  //cmCreateElementTag(player.id,"video","-_--_--_--_--_--_--_--_--_--_--_-"+video.title+"-_-"+newState+"-_-"+Math.round(player.getCurrentTime())+"-_-"+Math.round(player.getDuration()));
        }
      }