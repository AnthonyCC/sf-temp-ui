package com.freshdirect.cms._import.tasks;

import org.apache.log4j.Logger;

public class ToolTaskFactory {
    private static final Logger LOGGER = Logger.getLogger(ToolTaskFactory.class);

    private ToolTaskFactory() {}
    
    public enum Command {
        newUser, createSchema, dropSchema, loadDefinition, importData, switchCms
    }
    
    public static ToolTask createTask(Command cmd, String basePath) {
        final ToolTask task;
        switch (cmd) {
            case newUser:
                task = new CreateUserTask(basePath);
                break;
                
            case createSchema:
                task = new CreateSchemaTask(basePath);
                break;

            case dropSchema:
                task = new DropSchemaTask(basePath);
                break;

            case loadDefinition:
                task = new LoadCMSDefinitionTask(basePath);
                break;

            case importData:
                task = new ImportStoreDataTask(basePath);
                break;

            case switchCms:
                task = new SwitchCMSTask(basePath);
                break;

            default:
                task = null;
        }

        if (task == null) {
            LOGGER.error("No task for " + cmd);
        }

        return task;
    }
}
