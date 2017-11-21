package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class RunScriptTask extends PublishTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunScriptTask.class);

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    private String publishPath;

    private String scriptPath;

    public void setPublishPath(String publishPath) {
        this.publishPath = publishPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    @Override
    public String getName() {
        return "Run Shell Script Task";
    }

    private void executeScript(String scriptPath, String publishPath) throws IOException, InterruptedException {

    	LOGGER.info("  Publish Runtime execute PostScript: " + scriptPath + " " + publishPath);
        if (scriptPath != null) {
            Process child = Runtime.getRuntime().exec(scriptPath + " " + publishPath);

            BufferedReader buff = new BufferedReader(new InputStreamReader(child.getInputStream()));
            String line;

            while ((line = buff.readLine()) != null) {
                LOGGER.info(line);
                publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.DEBUG, "Running script: " + line, RunScriptTask.class.getSimpleName()));
            }

            int exitCode = child.waitFor();
            if (exitCode != 0) {
                publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.WARNING, "Exit code: " + exitCode, RunScriptTask.class.getSimpleName()));
            } else {
                publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Finished successfully", RunScriptTask.class.getSimpleName()));
            }
        }
    }

    @Override
    public void run() {
        try {
            executeScript(scriptPath, publishPath);
        } catch (Exception e) {
            LOGGER.error("System error occurred while executing script " + scriptPath, e);
            throw new RuntimeException(e);
        }
    }
}
