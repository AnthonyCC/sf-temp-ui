<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>GPS Route Upload Console</title>
</head>
 <style type="text/css">
  body {
    color: black;
    background-color: #999999 
  }
 </style>

<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/prototype/prototype.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/util/Util-Broadcaster.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/util/Util-BrowserDetect.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/util/Util-DateTimeFormat.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/util/Util-PluginDetect.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/util/Util-XmlConverter.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminObjectGenerator.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminGpsDataStructures.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminPluginUtils.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminDevice.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminDevicePlugin.js">&#160;</script>
<script type="text/javascript" src="http://developer.garmin.com/web/communicator-api-1.7/garmin/device/GarminDeviceControl.js">&#160;</script> 
<script type="text/javascript">
    var GarminDeviceControlDemo = Class.create();
    GarminDeviceControlDemo.prototype = {

        ////////////////////////////////////////////////////////////////////////
        //prototype constructor method:
        
        initialize: function(statusDiv, mapId) {        
            this.status = $(statusDiv);
            this.findDevicesButton = $("findDevicesButton");
            this.cancelFindDevicesButton = $("cancelFindDevicesButton");
            this.deviceSelect = $("deviceSelect");
            this.deviceInfo = $("deviceInfoText");
            this.writeDataButton = $("writeDataButton");
            this.cancelWriteDataButton = $("cancelWriteDataButton");
            this.writeDataText = $("writeDataText");
            this.writeDataFilename = $("writeDataFilename");
            this.progressBar = $("progressBar");
            this.progressBarDisplay = $("progressBarDisplay");
            this.garminController = null;
            this.tracks = null;
            initResult = this._intializeController();
            if(initResult) {
	            if(this.writeDataText.value.startsWith('<?xml')) {
	            	this._setStatus("Plug-in initialized.  Find some devices to get started.");
	            	this.findDevicesButton.disabled = false;
	            } else {
	            	this._setStatus("Plug-in initialized.  Route Directions are not available at this time.");
	            }
            } else {
            	this._setStatus("Unable to initlize Plug-in. Contact System Administrator to reconfigure the plugin.");
            }
            this.findDevicesButton.onclick = function() {
                this.findDevicesButton.disabled = true;
                this.cancelFindDevicesButton.disabled = false;
                this.garminController.findDevices();
            }.bind(this)
        },
        
        ////////////////////////////////////////////////////////////////////////
        //Garmin.DeviceControl call-back methods:

        onFinishFindDevices: function(json) {
            this.findDevicesButton.disabled = false;
            this.cancelFindDevicesButton.disabled = true;

            if(json.controller.numDevices > 0) {
                var devices = json.controller.getDevices();
                this._setStatus("Found " + devices.length + " devices.");
                this._listDevices(devices);
                
                this.cancelWriteDataButton.onclick = function() {
                    this.writeDataButton.disabled = false;
                    this.cancelWriteDataButton.disabled = true;
                    this._hideProgressBar();
                    this.garminController.cancelWriteToDevice();
                }.bind(this)
    
                this.writeDataButton.disabled = false;            
                this.writeDataButton.onclick = function() {
                    this.writeDataButton.disabled = true;
                    this.cancelWriteDataButton.disabled = false;
                    this._showProgressBar();
                    this.garminController.writeToDevice(this.writeDataText.value, this.writeDataFilename.value);
                }.bind(this);
            } else {
                this._setStatus("No devices found.");
            }
        },

        onStartFindDevices: function(json) {
            this._setStatus("Looking for connected Garmin devices");
        },

        onCancelFindDevices: function(json) {
            this._setStatus("Find cancelled");
        },

        //The device already has a file with this name on it.  Do we want to override?  1 is yes, 2 is no 
        onWaitingWriteToDevice: function(json) {    
            /*if(confirm(json.message.getText())) {
                this._setStatus('Overwriting file');
                json.controller.respondToMessageBox(true);
            } else {
                this._setStatus('Will not be overwriting file');
                json.controller.respondToMessageBox(false);
            }*/
        	this._setStatus('Overwriting file');
            json.controller.respondToMessageBox(true);
        },
    
        onProgressWriteToDevice: function(json) {
            this._updateProgressBar(json.progress.getPercentage());
            this._setStatus(json.progress);
        },
    
        onFinishWriteToDevice: function(json) {
            this._hideProgressBar();
            this._setStatus("Data written to the device.");
            this._hideProgressBar();
            this.writeDataButton.disabled = false;
            this.cancelWriteDataButton.disabled = true;
        },

    
        ////////////////////////////////////////////////////////////////////////
        //internal utility methods:

        _intializeController: function() {
            try {
                this.garminController = new Garmin.DeviceControl();
                this.garminController.unlock( ["<%= TransportationAdminProperties.getDefaultGpsDomain() %>"
                                               		,"<%= TransportationAdminProperties.getDefaultGpsPluginKey() %>"] );
                this.garminController.register(this);
            } catch (e) {
                if(e == "OutOfDatePluginException") {
                    alert("Plug-in out of date");
                } else if(e == "PluginNotInstalledException") { 
                    alert("Plug-in not installed");
                } else if(e == "BrowserNotSupportedException") { 
                    alert("Browser not supported");
                } else {
                    alert("Error initializing - " + e);
                }
                return false;
            }
            return true;
        },

        _showProgressBar: function() {
            Element.show(this.progressBar);
        },

        _hideProgressBar: function() {
            Element.hide(this.progressBar);
        },

        _updateProgressBar: function(value) {
            var percent = (value <= 100) ? value : 100;
            this.progressBarDisplay.style.width = percent + "%";
        },

        _listDevices: function(devices) {
            for( var i=0; i < devices.length; i++ ) {
                this.deviceSelect.options[i] = new Option(devices[i].getDisplayName(),devices[i].getNumber());
                if(devices[i].getNumber() == this.garminController.deviceNumber) {
                    this.deviceSelect.selectedIndex = i;
                    this._showDeviceInfo(devices[i]);
                }
            }
            this.deviceSelect.onchange = function() {
                var device = this.garminController.getDevices()[this.deviceSelect.value];
                this._showDeviceInfo(device);
                this.garminController.setDeviceNumber(this.deviceSelect.value);
            }.bind(this)
            this.deviceSelect.disabled = false;
        },

        _showDeviceInfo: function(device) {
            this.deviceInfo.innerHTML = "Part Number:\t\t" + device.getPartNumber() + "\n";
            this.deviceInfo.innerHTML += "Software Version:\t" + device.getSoftwareVersion() + "\n";
            this.deviceInfo.innerHTML += "Description:\t\t" + device.getDescription() + "\n";
            this.deviceInfo.innerHTML += "Id:\t\t\t" + device.getId();
        },
    
        _setStatus: function(statusText) {
            this.status.innerHTML = statusText;
        }
    };

    //display is created when HTML page is loaded
    var display;
        
    function load() {
        display = new GarminDeviceControlDemo("statusText", "readMap");        
    }
</script>

<body onload="load()">

<h2 style="text-align:center;">GPS Route Upload Console</h2>

<div id="actionStatus" align="center" style="color:blue;">
    <div id="statusText">Status Text</div>

    <div id="progressBar" style="display: none;" align="left">
        <div id="progressWrapper"><div id="progressBarDisplay">&#160;</div></div>
    </div>
</div>

<div id="deviceBox" align="center">
    <input type="button" value="Find Devices" id="findDevicesButton" disabled="true" />
    <input type="button" value="Cancel Find Devices" id="cancelFindDevicesButton" disabled="true" />
    
    <select name="deviceSelect" id="deviceSelect" disabled="true">
        <option value="-1">No Devices Found</option>
    </select>
    <br />
    <textarea id="deviceInfoText" rows="3" cols="50"></textarea>
</div>

<div id="writeBox" align="center">
    <hr />
    <input type="button" value="Write To Device" id="writeDataButton" disabled="true" />
    <input type="button" value="Cancel Write To Device" id="cancelWriteDataButton" disabled="true" />       
    <input type="hidden" id="writeDataFilename" value="<%= TransportationAdminProperties.getGpsUploadFilename() %>"><br />
    <div style="display:none;">
    <textarea id="writeDataText" name="writeDataText" rows="12" cols="50"><%= request.getAttribute("gpsgpxxml") %></textarea>
    </div>    
    
</div>

</body>
</html>
