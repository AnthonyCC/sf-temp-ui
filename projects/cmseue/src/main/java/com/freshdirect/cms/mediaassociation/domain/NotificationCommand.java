package com.freshdirect.cms.mediaassociation.domain;

import org.springframework.util.Assert;

public enum NotificationCommand {

    CREATE("create"),
    MOVE("move"),
    DELETE("delete"),
    UPDATE("update");

    private String command;

    NotificationCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static NotificationCommand getEnumFromString(String command) {
        Assert.notNull(command, "Command parameter can't be null!");
        NotificationCommand commandEnum = null;
        for (NotificationCommand notificationCommand : NotificationCommand.values()) {
            if (command.equals(notificationCommand.getCommand())) {
                commandEnum = notificationCommand;
                break;
            }
        }
        Assert.notNull(commandEnum, "Unknown command: " + command);
        return commandEnum;
    }
}
