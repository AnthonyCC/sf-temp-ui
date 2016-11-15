package com.freshdirect.cms._import;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.freshdirect.cms._import.tasks.ToolTask;
import com.freshdirect.cms._import.tasks.ToolTaskFactory;
import com.freshdirect.cms._import.tasks.ToolTaskFactory.Command;

public class ImportTool {

	public static final Logger LOGGER = Logger.getLogger(ImportTool.class);
	
	public static void main(String[] args) {

		// LOG4J - apply basic configuration
		BasicConfigurator.configure();

		
		ToolTaskFactory.Command cmd = null;
		
		String basePath = null;
		
		for (String arg : args){
			if ("--create_user".equalsIgnoreCase(arg)){
				cmd = Command.newUser;
			} else if ("--create_schema".equalsIgnoreCase(arg)) {
				cmd = Command.createSchema;
			} else if ("--drop_schema".equalsIgnoreCase(arg)) {
				cmd = Command.dropSchema;
			} else if ("--load_definition".equalsIgnoreCase(arg)) {
				cmd = Command.loadDefinition;
			} else if ("--import_data".equalsIgnoreCase(arg)) {
				cmd = Command.importData;
			} else if ("--switch_cms".equalsIgnoreCase(arg)) {
				cmd = Command.switchCms;
			} else {
				if (basePath == null){
					basePath = arg;
				} else {
					throw new RuntimeException("Too many arguments!");
				}
			}
		}

        
		if (cmd == null){
			showUsage();
			System.exit(0);
		}


		try {
	        final ToolTask task = ToolTaskFactory.createTask(cmd, basePath);
	        
	        if (task != null) {
	            task.run();
	        }
			
		} catch (Throwable t) {
			LOGGER.error(t);
			showUsage();
			System.exit(1);
		}
	}

	protected static void showUsage() {
		System.out.println("See README.txt for configuration and usage details!");
	}
}
