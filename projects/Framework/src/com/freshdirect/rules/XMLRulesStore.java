package com.freshdirect.rules;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.conf.ResourceUtil;
import com.thoughtworks.xstream.XStream;

/**
 * @author knadeem Date Apr 1, 2005
 */
public class XMLRulesStore extends AbstractRuleStore {

	private final String xmlFile;
	private final RulesConfig config;
	
	private final XStream xstream = new XStream();
	
	private File file;

	public XMLRulesStore(String xmlFile, String subsystem, List configurations) {
		
		this.xmlFile = xmlFile;
		
		this.config = this.findConfig(configurations, subsystem);
		
		this.configureXstream();
		
		this.loadRules();
		
		this.file = new File((xmlFile.startsWith("file:")) ? xmlFile.substring("file:".length()) : xmlFile);
	}
	
	public XMLRulesStore (String xmlFile, String basePath, RulesConfig config) {
		this.config = config;
		this.xmlFile = xmlFile;
		
		this.configureXstream();
		
		this.file = new File(basePath + File.separator + xmlFile);
	}
	

	public Map getRules() {
		return this.loadRules();
	}

	public Rule getRule(String ruleId) {
		Map m = this.loadRules();
		return (Rule) m.get(ruleId);
	}

	public void storeRule(Rule rule) {
		Map m = this.loadRules();
		m.put(rule.getId(), rule);
		
		this.writeRules(m);
	}

	public void deleteRule(String ruleId) {
		
		Map m = this.loadRules();
		
		if(m.containsKey(ruleId)){
			m.remove(ruleId);
			this.writeRules(m);
		}
	}
	
	public String getSubsystem() {
		return this.config.getSubsystem();
	}
	
	private void configureXstream(){
		
		List l = new ArrayList();
		l.addAll(config.getConditionTypes());
		l.addAll(config.getOutcomeTypes());
		
		xstream.alias("rule", Rule.class);
		for (Iterator i = l.iterator(); i.hasNext();) {
			ClassDescriptor desc = (ClassDescriptor) i.next();
			xstream.alias(desc.getXmlTag(), desc.getTargetClass());
		}
	}
	
	public void writeRules(Map rules){
		ObjectOutputStream out = null;
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter (new FileOutputStream(file));
			out = xstream.createObjectOutputStream(osw, "rules");
			for(Iterator i = rules.values().iterator(); i.hasNext(); ) {
				Rule r = (Rule)i.next();
				if (r.getId() == null || "".equals(r.getId())) {
					r.setId(Long.toString(System.currentTimeMillis()));
				}
				out.writeObject(r);
			}
			out.flush();
			osw.flush();
		} catch (IOException e) {
			throw new RulesRuntimeException(e);
		}finally{
			try{
				if(out != null) {
					out.close();
				}
				if(osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Map loadRules() {
		try {
			InputStream is = ResourceUtil.openResource(xmlFile);
			if (is == null) {
				throw new IOException("cannot find the file rules.xml on classpath");
			}
			Map rules = new HashMap();

			ObjectInputStream in = xstream.createObjectInputStream(new InputStreamReader(is));
			while (true) {
				try {
					Rule r = (Rule) in.readObject();
					if(this.getSubsystem().equals(r.getSubsystem())){
						rules.put(r.getId(), r);
					}
				} catch (EOFException e) {
					break;
				}
			}
			return rules;
		} catch (IOException e) {
			throw new RulesRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RulesRuntimeException(e);
		}
	}
}
