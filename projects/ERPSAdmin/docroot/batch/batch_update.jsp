<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.erp.model.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Erpsy - BatchUpdate</title>
		<link rel="shortcut icon" href="/blackbirdjs/favicon.ico" type="image/x-icon" />		
		<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css">	
		<script type="text/javascript" src="blackbirdjs/blackbird.js"></script>
		<link type="text/css" rel="Stylesheet" href="blackbirdjs/blackbird.css" />
		<script type="text/javascript" src="batch_update.js"></script>
		<%@ include file="/common/css/popcalendar.css" %>
		<style>		
			#container {
				margin-right: 5px;
			}
			#title {
				background-color: #000;
				font-size: 11px;
				font-family: Verdana,sans-serif;
				margin-bottom: 12px;
			}
			.sec_title {
				padding: 2px 0px 2px 6px;
				background-color: #666;
				color: #fff;
				font-size: 11px;
				font-weight: bold;
				line-height: 18px;
				text-transform: uppercase;
				width: auto;
			}
			.sec_title_options {
				padding: 2px 15px 2px 6px;
				color: #fff;
				font-size: 11px;
				font-weight: bold;
				line-height: 18px;
				width: 50px;
				float: right;
			}
			.data {
				width: 260px;
			}
			.choices {
				width: 300px;
			}
			.actions {
				margin: 5px;
			}
			.opt_descrip {
				width: 98%;
				height: 80px;
				border: 1px dashed #666;
				margin: 12px 0 0 0;
				padding: 2px;
				clear: both;
			}
			.field {
				font-size: 11px;
				font-family: Verdana,sans-serif;
				text-indent: 6px;
			}
			.data {
				float: left;
			}
			.choices, .actions {
				float: right;
			}
			.actions input {
				width: 90px;
			}
			#rest, #webcopy, #CAO, #NBIS #KOS {
				overflow: hidden;
				background-color: #fff;
				color: #000;
				width: 575px;
				font-size: 11px;
				font-family: Verdana,sans-serif;
				height: 70%;
			}
			.dataentry {
				width: 100%;
				font-size: 11px;
				background-color: #fff;
				color: #000;
				font-family: monospace;
				border: 1px solid #666;
			}
			.idTitle {
				padding: 3px 0px 2px 0px;
			}
			.idlist {
				height: 78%;
			}
			#user {
				width: 45%;
				float: left;
			}
			#activity {
				width: 450px;
				float: right;
			}
			#opt_additional_options, #postDelay {
				font-size: 11px;
				font-family: monospace;
			}
			#postDelay {
				width: 40px;
				text-align: center;
				border: 1px solid #888;
			}
			#status {
				height: 70%;
				overflow-y: auto;
			}
			#status div {
				height: auto;
				font-size: 11px;
				font-family: monospace;
				padding: 0 3px;
			}
			#status_note {
				float: left;
			}
			#status_verb, #status_current, #status_of, #status_count {
				float: right;
				text-align: center;
			}
			#status_verb {
				width: 70px;
			}
			#status_current, #status_count {
				width: 50px;
			}
			#status_of {
				width: 10px;
			}
			#status_cont {
				clear: both;
			}
			#parse {
				width: 1px;
				height: 1px;
				overflow: hidden;
				visibility: hidden;
			}
			#rest_data_title, #WD_data_title, #advOrder_title, #LN_data_title, #DEP_data_title {
				float: left;
				margin-top: 4px;
			}
			#rests_use, #WD_use, #advOrder_use, #LN_use, #DEP_use {
				float: right;
			}
			#WD_data_descrip, #advOrder_descrip {
				margin-top: 4px;
			}
			.toggler {
				float: right;
				padding-right: 3px;
			}
			.err {
				color: #f00;
			}
			#CAO_data {
				height:556px;
				overflow: auto;				
			}
			#KOS_data {
				overflow:auto; 
				height:556px;
			}
			#CAO_data table, #KOS_data table  {
				border-collapse: collapse;
				padding: 0;
				margin: 0;
				width: 100%;
			}
			#CAO_data table th, #KOS_data table th {
					text-transform: capitalize;
			}
			td.CAO_opt, td.KOS_opt {
				text-align: center;
				width: 15px;
			}
			td.CAO_opt input, td.KOS_opt input {
				background-color: #fff;
				color: #333;
				padding: 0;
				font-weight: bold;
				border-color: #666;
				font-size: 11px;
				font-family: monospace;
			}
			#CAO_data input.o, #KOS_data input.o {
				background-color: #ccc;
			}
			#CAO_data input.p, #KOS_data input.p  {
				background-color: #cfc;
			}
			#CAO_data input.m, #KOS_data input.m {
				background-color: #fcc;
			}
			#overlay {
				width: 800px;
				height: 500px;
				top: 50%;
				left: 50%;
				margin-left: -400px;
				margin-top: -250px;
				background-color: #eee;
				position: absolute;
				font-family: monospace;
				overflow-y: scroll;
				border: 1px solid #666;
			}
			#overlay table td {
				font-size: 11px;
				font-family: monospace;
				vertical-align: top;
			}
			#overlay table {
				border-collapse: collapse;
			}
			.w100per {
				width: 100%;
			}
			.fleft {
				float: left;
			}
			.fright {
				float: right;
			}
			.marTop4 {
				margin-top: 4px;
			}
			#changeLogLink {
				color: #2f0;
			}
			.toggler_choice {
				color: #fff;
				border-color: #666;
				background-color: #333;
				font-size: 11px;
				font-family: monospace;
				padding-right: 3px;
			}
		</style>		
	</head>
	<body>
		<tmpl:insert template='/common/templates/main1.jsp'/>
		<div id="main">		
			<div id="content">			
				<script language="javascript">					
					batch_update();
				</script>
			</div>
		</div>       
	</body>
</html>