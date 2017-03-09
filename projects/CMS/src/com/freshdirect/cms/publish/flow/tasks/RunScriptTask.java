package com.freshdirect.cms.publish.flow.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.PublishTask;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class RunScriptTask extends PublishTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RunScriptTask.class);

    private final PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    private final String publishPath;

    private final String scriptPath;

    public RunScriptTask(String publishId, Phase phase, String publishPath, String scriptPath) {
        super(publishId, phase);

        this.publishPath = publishPath;
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
                publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.DEBUG, "Running script: " + line, RunScriptTask.class.getSimpleName()));
            }

            int exitCode = child.waitFor();
            if (exitCode == 0) {
                publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.WARNING, "Exit code: " + exitCode, RunScriptTask.class.getSimpleName()));
            } else {
                publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Finished successfully", RunScriptTask.class.getSimpleName()));
            }
        }
    }

    @Override
    public void run() {
        try {
            executeScript(scriptPath, publishPath);
        } catch (Exception e) {
            LOGGER.error("System error occurred while executing script " + scriptPath, e);
            throw new CmsRuntimeException(e);
        }
    }
}
