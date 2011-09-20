package com.freshdirect.analytics;

import java.util.List;

import org.apache.log4j.Category;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
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

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CEPService{
		
	private static final Category LOGGER = LoggerFactory.getInstance(CEPService.class);

		private static StatelessKnowledgeSession ksession = null;
	/*	static
		{
			KnowledgeBase kbase = getKnowledgeBase();
			ksession = kbase.newStatelessKnowledgeSession();
			ksession.addEventListener( new CustomAgendaEventListener() );
			ksession.addEventListener(new CustomWorkingMemoryEventListener());
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "auditlogfile");
			
		}*/
		
		@SuppressWarnings("unchecked")
		public static void insert(List e)
		{
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
				e.add(new EventHelper());
				ksession.execute(CommandFactory.newInsertElements(e));
			}
			}
			catch(Exception ex)
			{
				LOGGER.info("Exception during event processing: ", (Throwable) ex);
			}
		}
		private static KnowledgeBase getKnowledgeBase()
		{
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			kbuilder.add(ResourceFactory.newInputStreamResource(CEPService.class.getResourceAsStream(FDStoreProperties.getRuleRepository())),  ResourceType.DRL);
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
		
	/*	private static StatefulKnowledgeSession session = null;
		
		static
		{
			if(session == null)
				getInstance();
		}
		public static synchronized StatefulKnowledgeSession getInstance(){
			try
			{
			KnowledgeBase knowledgeBase = readKnowledgeBase();
			session = knowledgeBase.newStatefulKnowledgeSession();
			session.addEventListener( new CustomAgendaEventListener() );
			session.addEventListener(new CustomWorkingMemoryEventListener());
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "auditlogfile");
			
			new Thread(new Runnable() {
				public void run() {
				session.fireUntilHalt();
				}
				}).start();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}

			return session;
			
			


		}

		public static void insert(TimeslotEventModel event)
		{
			try
			{
			if(session == null)
			{
				getInstance();
			    
			}
			session.insert(event);
			
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		private static KnowledgeBase readKnowledgeBase() throws Exception {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newInputStreamResource(CEPService.class.getResourceAsStream("Bounce.drl")),  ResourceType.DRL);
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error: errors) {
					System.err.println(error);
				}
				throw new IllegalArgumentException("Could not parse knowledge.");
			}
			KnowledgeBaseConfiguration configuration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		    configuration.setOption(EventProcessingOption.STREAM);
			KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(configuration);
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
			return kbase;
		}
		
		*/
	}
	
