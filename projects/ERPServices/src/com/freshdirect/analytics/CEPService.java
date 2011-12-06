package com.freshdirect.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.QueryResults;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.rule.QueryResultsRow;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CEPService{
		
	private static final Category LOGGER = LoggerFactory.getInstance(CEPService.class);

		private static StatelessKnowledgeSession ksession = null;
		
		@SuppressWarnings("unchecked")
		public static Map insert(List e)
		{
			Map map = new HashMap();
			
			try
			{
				KnowledgeBase kbase = null;
				if("local".equalsIgnoreCase(FDStoreProperties.getKbSource()))
					 kbase = getKnowledgeBase();
				else
					 kbase = getGuvnorKnowledgeBase();
				
			ksession = kbase.newStatelessKnowledgeSession();
			if(FDStoreProperties.isDebugEventAnalysis())
			{
			ksession.addEventListener( new CustomAgendaEventListener() );
			ksession.addEventListener(new CustomWorkingMemoryEventListener());
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "auditlogfile");
			}
	
			if(e!=null && e.size()>0)
			{
				ArrayList RollEventList = new ArrayList();
				ArrayList BounceEventList = new ArrayList();
				ksession.setGlobal("RollEventList", RollEventList);
				ksession.setGlobal("BounceEventList", BounceEventList);
				
				ksession.execute(CommandFactory.newInsertElements(e));
				
				RollEventList = (ArrayList) ksession.getGlobals().get("RollEventList");
				BounceEventList = (ArrayList) ksession.getGlobals().get("BounceEventList");
				map.put("roll", RollEventList);
				map.put("bounce", BounceEventList);
			}
			}
			catch(Exception ex)
			{
				LOGGER.info("Exception during event processing: ", (Throwable) ex);
			}
			return map;
		}
		private static KnowledgeBase getKnowledgeBase()
		{
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			kbuilder.add(ResourceFactory.newInputStreamResource(ClassLoader.getSystemResourceAsStream(FDStoreProperties.getRuleRepository())),  ResourceType.DRL);
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error: errors) {
					System.err.println(error);
				}
				throw new IllegalArgumentException("Could not parse knowledge.");
			}
			
			KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
			return kbase;
			
			
		}
		

		private static KnowledgeBase getGuvnorKnowledgeBase() {
			 KnowledgeAgentConfiguration kaconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
			 kaconf.setProperty( "drools.agent.scanDirectories", "false" );
			 KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent( "test agent", kaconf );
			 kagent.applyChangeSet( ResourceFactory.newInputStreamResource(CEPService.class.getResourceAsStream(FDStoreProperties.getRuleRepository())));
			 return kagent.getKnowledgeBase();
		}
	}
	
