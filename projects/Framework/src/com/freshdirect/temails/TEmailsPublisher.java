package com.freshdirect.temails;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author knadeem Date Apr 12, 2005
 */
public class TEmailsPublisher implements TEmailPublisherI {

	private final String basePath;
	private final String publishCommand;

	public TEmailsPublisher(String basePath, String publishCommand) {
		this.basePath = basePath;
		this.publishCommand = publishCommand;
	}

	public void publish(Map templates, String subsystem, String targetEnv) {

		TEmailsConfig config = TEmailsRegistry.getTEmailsConfig(subsystem);

		String filename = subsystem + "-templates.xml";

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String publishPath = basePath + File.separator + sf.format(new Date());

		File f = new File(publishPath);
		f.mkdir();

		XMLTEmailStore xmlStore = new XMLTEmailStore(filename, publishPath, config);

		xmlStore.writeTemplates(templates);
		
		MessageFormat mf = new MessageFormat(publishCommand);
		Object [] scriptArgs = {targetEnv, publishPath, filename};
		
		System.out.println("Going to publish to: " + targetEnv);
		
		try{
			System.out.println("RulesPublisher.publish()"+publishCommand);
			this.executeScript(mf.format(scriptArgs));
		}catch(IOException e){
			throw new TEmailRuntimeException(e);
		} catch (InterruptedException e) {
			throw new TEmailRuntimeException(e);
		}
		
	}

	private void executeScript(String script) throws IOException, InterruptedException {
		System.out.println("TEmailsPublisher.executeScript()"+script);
		Process child = Runtime.getRuntime().exec(script);

		BufferedReader buff = new BufferedReader(new InputStreamReader(child.getInputStream()));
		String line;

		while ((line = buff.readLine()) != null) {
			System.out.println(line);
		}
		child.waitFor();
	}

}
