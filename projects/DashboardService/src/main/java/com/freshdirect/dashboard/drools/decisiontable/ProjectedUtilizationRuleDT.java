package com.freshdirect.dashboard.drools.decisiontable;

import java.util.Arrays;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.command.CommandFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.dashboard.model.ProjectedUtilizationBase;

/**
 * This shows off a decision table.
 */
public class ProjectedUtilizationRuleDT {

	private static Logger logger = LoggerFactory.getLogger(ProjectedUtilizationRuleDT.class);
	
	private StatelessKnowledgeSession ksession;

	private KnowledgeBase kbase;

	public KnowledgeBase getKbase() {
		return kbase;
	}

	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}

	public StatelessKnowledgeSession getKsession() {
		return ksession;
	}

	public void setKsession(StatelessKnowledgeSession ksession) {
		this.ksession = ksession;
	}

	@SuppressWarnings("unchecked")
	public List<ProjectedUtilizationBase> executeSuggestedActionRule(
			List<ProjectedUtilizationBase> projectedUtilization)
			throws Exception {
		ksession.execute(CommandFactory.newInsertElements(projectedUtilization));
		return projectedUtilization;
	}
	
	public String executeSuggestedAction(ProjectedUtilizationBase projectedUtilization) {
		// now create some test data
		ksession.execute(Arrays.asList(new Object[] { projectedUtilization }));

		logger.debug("SUGGESTED ACTION IS: " + projectedUtilization.toString());

		return projectedUtilization.getSuggestedAction();
	}

}
