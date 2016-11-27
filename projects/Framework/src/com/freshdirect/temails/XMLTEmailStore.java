package com.freshdirect.temails;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.framework.conf.ResourceUtil;
import com.thoughtworks.xstream.XStream;

/**
 * @author unknown
 */
public class XMLTEmailStore extends AbstractTEmailStore {

	private final String xmlFile;
	private final TEmailsConfig config;
	
	private final XStream xstream = new XStream();
	
	private File file;

	public XMLTEmailStore(String xmlFile, String subsystem, List configurations) {
		
		this.xmlFile = xmlFile;
		
		this.config = this.findConfig(configurations, subsystem);
		
		this.configureXstream();
		
		this.loadTemplates();
		
		this.file = new File((xmlFile.startsWith("file:")) ? xmlFile.substring("file:".length()) : xmlFile);
	}
	
	public XMLTEmailStore (String xmlFile, String basePath, TEmailsConfig config) {
		this.config = config;
		this.xmlFile = xmlFile;
		
		this.configureXstream();
		
		this.file = new File(basePath + File.separator + xmlFile);
	}
	

	public Map getTemplates() {
		return this.loadTemplates();
	}

	public Template getTemplate(String templateId) {
		Map m = this.loadTemplates();
		return (Template) m.get(templateId);
	}

	public void storeTemplate(Template template) {
		Map m = this.loadTemplates();
		m.put(template.getId(), template);
		
		this.writeTemplates(m);
	}

	public void deleteTemplate(String templateId) {
		
		Map m = this.loadTemplates();
		
		if(m.containsKey(templateId)){
			m.remove(templateId);
			this.writeTemplates(m);
		}
	}
	
	public String getSubsystem() {
		return this.config.getSubsystem();
	}
	
	private void configureXstream(){
		
		List l = new ArrayList();
		l.addAll(config.getParserTypes());
			
		xstream.alias("template", Template.class);
		for (Iterator i = l.iterator(); i.hasNext();) {
			TemplateDescriptor desc = (TemplateDescriptor) i.next();
			xstream.alias(desc.getXmlTag(), desc.getTargetClass());
		}
	}
	
	public void writeTemplates(Map rules){
		ObjectOutputStream out = null;
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter (new FileOutputStream(file));
			out = xstream.createObjectOutputStream(osw, "templates");
			for(Iterator i = rules.values().iterator(); i.hasNext(); ) {
				Template r = (Template)i.next();
				if (r.getId() == null || "".equals(r.getId())) {
					//r.setId(Long.toString(System.currentTimeMillis()));
					throw new TEmailRuntimeException("template id cannot be null");
				}
				out.writeObject(r);
			}
			out.flush();
			osw.flush();
		} catch (IOException e) {
			throw new TEmailRuntimeException(e);
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
	
			
	private Map loadTemplates() {
		InputStream is = null;
		ObjectInputStream in = null;
		try {
			System.out.println("opening xml file :"+xmlFile);
			is = ResourceUtil.openResource(xmlFile);
			if (is == null) {
				throw new IOException("cannot find the file " + xmlFile + " on classpath");
			}
			Map templates = new HashMap();

			in = xstream.createObjectInputStream(new InputStreamReader(is));
			while (true) {
				try {
					Template r = (Template) in.readObject();
					//System.out.println("adding templates :"+r.getEid());
					if(this.getSubsystem().equals(r.getSubsystem())){						
						templates.put(r.getId(), r);						
						List parsers=r.getParsers();	
						
						
						TreeMap treeMap=new TreeMap();
						
						
						for(int i=0;i<parsers.size();i++){
							ParserI tmpP=(ParserI)parsers.get(i);
						    
							List list=(List)treeMap.get(new Integer(tmpP.getLevel()));
							if(list==null){
								list=new ArrayList();
								treeMap.put(new Integer(tmpP.getLevel()), list);
							}							
							list.add(tmpP);
							
							List l=config.getParserTypes();							
							for (Iterator it = l.iterator(); it.hasNext();) {
						    	TemplateDescriptor desc = (TemplateDescriptor) it.next();
						    	System.out.println("desc :"+desc.getTargetClass());						    	
						    	System.out.println("p.getClass().getName() :"+tmpP.getClass().getName());
						    	if(desc.getTargetClass().getName().equals(tmpP.getClass().getName())){
						    		tmpP.setOrder(desc.getOrder());
						    	}
						    } 			
							
						}
						
												
						Set keySet=treeMap.keySet();
						
						Iterator iterator=keySet.iterator();
						
						while(iterator.hasNext()){
							
							Integer level=(Integer)iterator.next();																					
							
							if(level.intValue()>=keySet.size()-1) break;
							
							List tmpChildList=(List)treeMap.get(level);
							
							Integer level1=new Integer(level.intValue()+1);
							
							List tmpparentList=(List) treeMap.get(level1);														
							
							for(int j=0;j<tmpChildList.size();j++){							
								ParserI tmpP=(ParserI)tmpChildList.get(j);							
								for(int i=0;i<tmpparentList.size();i++){
									ParserI parentP=(ParserI)tmpparentList.get(i);
									if(parentP.getParentId().equalsIgnoreCase(tmpP.getId())){
										tmpP.setChildParser(parentP);
										System.out.println("setting childParser id for  :"+tmpP.getId()+" and child Parser Id is:"+parentP.getId());
									} 																
								}
							}																					
						}
						
						Integer key= (Integer)treeMap.firstKey();
						
						r.setParsers((List)treeMap.get(key));
						/*
						
						
						System.out.println("parsers size:"+parsers.size());																		
						List tmpChildList=new ArrayList();	
						List tmpparentList=new ArrayList();
						  
						for(int i=0;i<parsers.size();i++){
							ParserI tmpP=(ParserI)parsers.get(i);
							if(tmpP.getParentId()!=null && tmpP.getParentId().trim().length()>0){							
									tmpparentList.add(tmpP);
							}else{
									tmpChildList.add(tmpP);
							}							
							List l=config.getParserTypes();							
							for (Iterator it = l.iterator(); it.hasNext();) {
						    	TemplateDescriptor desc = (TemplateDescriptor) it.next();
						    	System.out.println("desc :"+desc.getTargetClass());						    	
						    	System.out.println("p.getClass().getName() :"+tmpP.getClass().getName());
						    	if(desc.getTargetClass().getName().equals(tmpP.getClass().getName())){
						    		tmpP.setOrder(desc.getOrder());
						    	}
						    } 														
						}		
						for(int j=0;j<tmpChildList.size();j++){							
							ParserI tmpP=(ParserI)tmpChildList.get(j);							
							for(int i=0;i<tmpparentList.size();i++){
								ParserI parentP=(ParserI)tmpparentList.get(i);
								if(parentP.getParentId().equalsIgnoreCase(tmpP.getId())){
									tmpP.setChildParser(parentP);
									System.out.println("setting childParser id for  :"+tmpP.getId()+" and child Parser Id is:"+parentP.getId());
								} 																
							}			
						}
						
						for(int j=0;j<tmpChildList.size();j++){							
							ParserI tmpP=(ParserI)tmpChildList.get(j);							
							for(int i=0;i<tmpparentList.size();i++){
								ParserI parentP=(ParserI)tmpparentList.get(i);
								if(parentP.getParentId().equalsIgnoreCase(tmpP.getId())){
									tmpP.setChildParser(parentP);
									System.out.println("setting childParser id for  :"+tmpP.getId()+" and child Parser Id is:"+parentP.getId());
								} 																
							}
							
							for(int i=0;i<tmpparentList.size();i++){
								ParserI parentP=(ParserI)tmpparentList.get(i);
								if(tmpP.getChildParser()!=null && (parentP.getParentId().equalsIgnoreCase(tmpP.getChildParser().getId()))){
									tmpP.getChildParser().setChildParser(parentP);
									System.out.println("setting child childParser id for  :"+tmpP.getChildParser().getId()+" and child Parser Id is:"+parentP.getId());
								} 																
							}
						}		

						*/						
						
						/*
						
						for(int i=0;i<parsers.size();i++){
							ParserI p=(ParserI)parsers.get(i);
							List l=config.getParserTypes();
							
							for (Iterator it = l.iterator(); it.hasNext();) {
						    	TemplateDescriptor desc = (TemplateDescriptor) it.next();
						    	System.out.println("desc :"+desc.getTargetClass());						    	
						    	System.out.println("p.getClass().getName() :"+p.getClass().getName());
						    	if(desc.getTargetClass().getName().equals(p.getClass().getName())){
						    		p.setOrder(desc.getOrder());
						    	}
						    } 
						}  */
						
					}
				} catch (EOFException e) {
					break;
				}
			}
			return templates;
		} catch (IOException e) {
			throw new TEmailRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new TEmailRuntimeException(e);
		} finally {
			if (in != null)
				try {
					in.close();
					in = null;
					is = null;
				} catch (IOException e) {
					if (is != null)
						try {
							is.close();
							is = null;
						} catch (IOException e1) {
						}
				}
			else if (is != null)
				try {
					is.close();
					is = null;
				} catch (IOException e1) {
				}
		}
	}
}
