/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Publish task to invoke an external process. Passes the base-path
 * of the publish as a parameter to the script.
 */
public class RunScriptTask implements PublishTask {

    private static final Logger LOG = LoggerFactory.getInstance(RunScriptTask.class);
    
	private final String scriptPath;

	/**
	 * @param scriptPath full path of the process to invoke
	 */
	public RunScriptTask(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	private void executeScript(Publish publish, String scriptPath, String publishPath) throws IOException, InterruptedException {
		Process child = Runtime.getRuntime().exec(scriptPath + " " + publishPath);

		BufferedReader buff = new BufferedReader(new InputStreamReader(child.getInputStream()));
		String line;

		while ((line = buff.readLine()) != null) {
		    LOG.info(line);
                    publish.getMessages().add(new PublishMessage(PublishMessage.DEBUG, "script : " + line));
		}
		int exitCode = child.waitFor();
                publish.getMessages().add(new PublishMessage(exitCode != 0 ? PublishMessage.WARNING : PublishMessage.INFO, "Exit code : " + exitCode));
	}

	public void execute(Publish publish) {
		try {
			executeScript(publish, scriptPath, publish.getPath());
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		} catch (InterruptedException e) {
			throw new CmsRuntimeException(e);
		}
	}

	public String getComment() {
		return "Executing script " + scriptPath;
	}

}