/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.freshdirect.cms.CmsRuntimeException;

/**
 * Publish task to invoke an external process. Passes the base-path
 * of the publish as a parameter to the script.
 */
public class RunScriptTask implements PublishTask {

	private final String scriptPath;

	/**
	 * @param scriptPath full path of the process to invoke
	 */
	public RunScriptTask(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	private void executeScript(String scriptPath, String publishPath) throws IOException, InterruptedException {
		Process child = Runtime.getRuntime().exec(scriptPath + " " + publishPath);

		BufferedReader buff = new BufferedReader(new InputStreamReader(child.getInputStream()));
		String line;

		while ((line = buff.readLine()) != null) {
			System.out.println(line);
		}
		child.waitFor();
	}

	public void execute(Publish publish) {
		try {
			executeScript(scriptPath, publish.getPath());
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