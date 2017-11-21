package com.freshdirect.storeapi.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.freshdirect.rules.Rule;

/**
 * @author knadeem Date Apr 12, 2005
 */
@Service
public class RulesPublisher {

    @Value("${com.freshdirect.rules.publish.dir}")
	private String basePath;

    @Value("${com.freshdirect.rules.publish.script}")
	private String publishCommand;

    @Autowired
    private RulesRegistry rulesRegistry;

    public void publish(Map<String, Rule> rules, String subsystem, String targetEnv) {

		RulesConfig config = rulesRegistry.getRulesConfig(subsystem);

		String filename = subsystem + "-rules.xml";

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String publishPath = basePath + File.separator + sf.format(new Date());

		File f = new File(publishPath);
		f.mkdir();

		XMLRulesStore xmlStore = new XMLRulesStore(filename, publishPath, config);

		xmlStore.writeRules(rules);

		MessageFormat mf = new MessageFormat(publishCommand);
		Object [] scriptArgs = {targetEnv, publishPath, filename};

		System.out.println("Going to publish to: " + targetEnv);

		try{
			System.out.println("RulesPublisher.publish()"+publishCommand);
			this.executeScript(mf.format(scriptArgs));
		}catch(IOException e){
			throw new RulesRuntimeException(e);
		} catch (InterruptedException e) {
			throw new RulesRuntimeException(e);
		}

	}

	private void executeScript(String script) throws IOException, InterruptedException {
		System.out.println("RulesPublisher.executeScript()"+script);
		Process child = Runtime.getRuntime().exec(script);

		BufferedReader buff = new BufferedReader(new InputStreamReader(child.getInputStream()));
		String line;

		while ((line = buff.readLine()) != null) {
			System.out.println(line);
		}
		child.waitFor();
	}

}
